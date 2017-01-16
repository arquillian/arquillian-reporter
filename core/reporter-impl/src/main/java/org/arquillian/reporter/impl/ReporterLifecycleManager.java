package org.arquillian.reporter.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportTestClass;
import org.arquillian.reporter.api.event.ReportTestMethod;
import org.arquillian.reporter.api.event.ReportTestSuite;
import org.arquillian.reporter.api.event.ReportTestSuiteConfiguration;
import org.arquillian.reporter.api.model.AbstractSection;
import org.arquillian.reporter.api.model.TestSuiteSection;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManager {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ExecutionReport> report;

    private ExecutionReport report() {
        return report.get();
    }

    public void observeFirstEvent(@Observes ManagerStarted event) {
        if (report() == null) {
            ExecutionReport executionReport = new ExecutionReport();
            report.set(executionReport);
        }
    }

    public void observeSectionReports(@Observes(precedence = -100) ReportEvent event){
        processEvent(null, event, null, null);
    }

    public void observeTestSuiteReports(@Observes ReportTestSuite event) {
        processEvent(ReportTestSuite.class, event, report(), "getTestSuiteSections");
    }

    public void observeTestClassReports(@Observes ReportTestClass event) {
        AbstractSection lastTestSuiteReport = getLastTestSuiteReport();
        if (lastTestSuiteReport != null) {
            processEvent(ReportTestClass.class, event, lastTestSuiteReport, "getTestClassSections");
        }
    }

    public void observeTestMethodReports(@Observes ReportTestMethod event) {
        processEvent(ReportTestMethod.class, event, null, "getTestMethodSections");

    }

    public void observeReportTestSuiteConfigurationEvents(@Observes ReportTestSuiteConfiguration event) {
        TestSuiteSection lastTestSuiteSection = getLastTestSuiteReport();
        if (lastTestSuiteSection != null) {
            processEvent(ReportTestSuiteConfiguration.class, event,
                         lastTestSuiteSection.getConfiguration(), null);
        }
    }

    private void processEvent(Class<? extends ReportEvent> parentEventClass, ReportEvent event,
        AbstractSection parentSectionReport, String methodNameToGetList) {
        if (event.isProcessed()){
            return;
        }

        if (parentEventClass == null || !event.getClass().isAssignableFrom(parentEventClass)) {
            processSubclassEvent(parentEventClass, event, parentSectionReport, methodNameToGetList);
        } else {

            AbstractSection alreadyExisting = report().getSectionReportByIdentifier(event);
            if (alreadyExisting != null) {
                alreadyExisting.merge(event.getSectionReport());
            } else if (parentSectionReport != null) {
                getSectionList(parentSectionReport, methodNameToGetList).add(event.getSectionReport());
                //                report().getTestSuiteReports().getTestClassReports().add(event.getSectionReport());
            } else if (event.getParentEvent() != null) {
                processParentEventIsSet(event, methodNameToGetList);
            } else {
                // todo
            }

        }

        report().register(event);
        event.setProcessed(true);
    }

    private void processSubclassEvent(Class<? extends ReportEvent> expectedReportEventClass, ReportEvent actualEvent,
        AbstractSection parentSectionReport, String methodNameToGetList) {

        AbstractSection alreadyExisting = report().getSectionReportByIdentifier(actualEvent);
        if (alreadyExisting != null) {
            alreadyExisting.merge(actualEvent.getSectionReport());
            return;
        }

        if (actualEvent.getParentEvent() != null) {
            processParentEventIsSet(actualEvent, null);
        } else {
            Class<?> superclass = actualEvent.getClass().getSuperclass();
            if (!superclass.isAssignableFrom(expectedReportEventClass)) {
                getSectionList(parentSectionReport, methodNameToGetList).add(actualEvent.getSectionReport());
            }
        }
    }

    private void processParentEventIsSet(ReportEvent actualEvent, String methodNameToGetList) {

        ReportEvent parentEvent = actualEvent.getParentEvent();
        Identifier parentIdentifier = new Identifier(parentEvent.getClass(), parentEvent.getIdentifier());

        AbstractSection parentSection = report().getSectionReportByIdentifier(parentIdentifier);
        if (parentSection != null) {
            if (methodNameToGetList != null) {
                getSectionList(parentSection, methodNameToGetList).add(actualEvent.getSectionReport());
            } else {
                parentSection.getSectionReports().add(actualEvent.getSectionReport());
            }
        } else {
            // todo
            return;
        }
    }

    private List<AbstractSection> getSectionList(AbstractSection parentSectionReport,
        String methodNameToGetList) {
        if (methodNameToGetList == null){
            return parentSectionReport.getSectionReports();
        }
        try {
            return (List<AbstractSection>) parentSectionReport.getClass().getMethod(methodNameToGetList)
                .invoke(parentSectionReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public void afterSuite(@Observes ManagerStopping event) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(report().getTestSuiteSections());
        FileUtils.writeStringToFile(new File("target/report.json"), json);
        System.out.println(json);

    }

    // todo support multiple test suites within one execution
    private TestSuiteSection getLastTestSuiteReport() {
        List<TestSuiteSection> testSuiteSections = report().getTestSuiteSections();
        if (testSuiteSections.size() > 0) {
            return testSuiteSections.get(testSuiteSections.size() - 1);
        }
        return null;
    }

}
