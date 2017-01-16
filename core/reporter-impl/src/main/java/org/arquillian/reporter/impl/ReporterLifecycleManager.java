package org.arquillian.reporter.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportEventTestClass;
import org.arquillian.reporter.api.event.ReportEventTestMethod;
import org.arquillian.reporter.api.event.ReportEventTestSuite;
import org.arquillian.reporter.api.event.ReportEventTestSuiteConfiguration;
import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.TestSuiteReport;
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
        processEvent(null, event, null, "getSectionReports");
    }

    public void observeTestSuiteReports(@Observes ReportEventTestSuite event) {
        processEvent(ReportEventTestSuite.class, event, report(), "getTestSuiteReports");
    }

    public void observeTestClassReports(@Observes ReportEventTestClass event) {
        Section lastTestSuiteReport = getLastTestSuiteReport();
        if (lastTestSuiteReport != null) {
            processEvent(ReportEventTestClass.class, event, lastTestSuiteReport, "getTestClassReports");
        }
    }

    public void observeTestMethodReports(@Observes ReportEventTestMethod event) {
        processEvent(ReportEventTestMethod.class, event, null, "getTestMethodReports");

    }

    public void observeReportTestSuiteConfigurationEvents(@Observes ReportEventTestSuiteConfiguration event) {
        TestSuiteReport lastTestSuiteReport = getLastTestSuiteReport();
        if (lastTestSuiteReport != null) {
            processEvent(ReportEventTestSuiteConfiguration.class, event,
                         lastTestSuiteReport.getConfiguration(), "getSectionReports");
        }
    }

    private void processEvent(Class<? extends ReportEvent> parentEventClass, ReportEvent event,
        Section parentSectionReport, String methodNameToGetList) {
        if (event.isProcessed()){
            return;
        }

        if (parentEventClass == null || !event.getClass().isAssignableFrom(parentEventClass)) {
            processSubclassEvent(parentEventClass, event, parentSectionReport, methodNameToGetList);
        } else {

            Section alreadyExisting = report().getSectionReportByIdentifier(event);
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
        Section parentSectionReport, String methodNameToGetList) {

        Section alreadyExisting = report().getSectionReportByIdentifier(actualEvent);
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

        Section parentSection = report().getSectionReportByIdentifier(parentIdentifier);
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

    private List<Section> getSectionList(Section parentSectionReport,
        String methodNameToGetList) {
        try {
            return (List<Section>) parentSectionReport.getClass().getMethod(methodNameToGetList)
                .invoke(parentSectionReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public void afterSuite(@Observes ManagerStopping event) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(report().getTestSuiteReports());
        FileUtils.writeStringToFile(new File("target/report.json"), json);
        System.out.println(json);

    }

    // todo support multiple test suites within one execution
    private TestSuiteReport getLastTestSuiteReport() {
        List<TestSuiteReport> testSuiteReports = report().getTestSuiteReports();
        if (testSuiteReports.size() > 0) {
            return testSuiteReports.get(testSuiteReports.size() - 1);
        }
        return null;
    }

}
