package org.arquillian.reporter.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.event.TestClassNode;
import org.arquillian.reporter.api.event.TestMethodNode;
import org.arquillian.reporter.api.event.TestSuiteNode;
import org.arquillian.reporter.api.event.TestSuiteConfigurationNode;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
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

    public void observeSectionReports(@Observes(precedence = -100) ReportNodeEvent event){
        processEvent(null, event, null, null);
    }

    public void observeTestSuiteReports(@Observes TestSuiteNode event) {
        processEvent(TestSuiteNode.class, event, report(), "getTestSuiteReports");
    }

    public void observeTestClassReports(@Observes TestClassNode event) {
        AbstractSectionReport lastTestSuiteReport = getLastTestSuiteReport();
        if (lastTestSuiteReport != null) {
            processEvent(TestClassNode.class, event, lastTestSuiteReport, "getTestClassReports");
        }
    }

    public void observeTestMethodReports(@Observes TestMethodNode event) {
        processEvent(TestMethodNode.class, event, null, "getTestMethodReports");

    }

    public void observeReportTestSuiteConfigurationEvents(@Observes TestSuiteConfigurationNode event) {
        TestSuiteReport lastTestSuiteReport = getLastTestSuiteReport();
        if (lastTestSuiteReport != null) {
            processEvent(TestSuiteConfigurationNode.class, event,
                         lastTestSuiteReport.getConfiguration(), null);
        }
    }

    private void processEvent(Class<? extends ReportNodeEvent> parentEventClass, ReportNodeEvent event,
        AbstractSectionReport parentSectionReport, String methodNameToGetList) {
        if (event.isProcessed()){
            return;
        }

        if (parentEventClass == null || !event.getClass().isAssignableFrom(parentEventClass)) {
            processSubclassEvent(parentEventClass, event, parentSectionReport, methodNameToGetList);
        } else {

            AbstractSectionReport alreadyExisting = report().getSectionReportByIdentifier(event);
            if (alreadyExisting != null) {
                alreadyExisting.merge(event.getSection());
            } else if (parentSectionReport != null) {
                getSectionList(parentSectionReport, methodNameToGetList).add(event.getSection());
                //                report().getTestSuiteReports().getTestClassReports().add(event.getSection());
            } else if (event.getParentEvent() != null) {
                processParentEventIsSet(event, methodNameToGetList);
            } else {
                // todo
            }

        }

        report().register(event);
        event.setProcessed(true);
    }

    private void processSubclassEvent(Class<? extends ReportNodeEvent> expectedReportEventClass, ReportNodeEvent actualEvent,
        AbstractSectionReport parentSectionReport, String methodNameToGetList) {

        AbstractSectionReport alreadyExisting = report().getSectionReportByIdentifier(actualEvent);
        if (alreadyExisting != null) {
            alreadyExisting.merge(actualEvent.getSection());
            return;
        }

        if (actualEvent.getParentEvent() != null) {
            processParentEventIsSet(actualEvent, null);
        } else {
            Class<?> superclass = actualEvent.getClass().getSuperclass();
            if (!superclass.isAssignableFrom(expectedReportEventClass)) {
                getSectionList(parentSectionReport, methodNameToGetList).add(actualEvent.getSection());
            }
        }
    }

    private void processParentEventIsSet(ReportNodeEvent actualEvent, String methodNameToGetList) {

        ReportNodeEvent parentEvent = actualEvent.getParentEvent();
        Identifier parentIdentifier = new Identifier(parentEvent.getClass(), parentEvent.getIdentifier());

        AbstractSectionReport parentSection = report().getSectionReportByIdentifier(parentIdentifier);
        if (parentSection != null) {
            if (methodNameToGetList != null) {
                getSectionList(parentSection, methodNameToGetList).add(actualEvent.getSection());
            } else {
                parentSection.getSectionReports().add(actualEvent.getSection());
            }
        } else {
            // todo
            return;
        }
    }

    private List<AbstractSectionReport> getSectionList(AbstractSectionReport parentSectionReport,
        String methodNameToGetList) {
        if (methodNameToGetList == null){
            return parentSectionReport.getSectionReports();
        }
        try {
            return (List<AbstractSectionReport>) parentSectionReport.getClass().getMethod(methodNameToGetList)
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
