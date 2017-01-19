package org.arquillian.reporter.impl;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.AbstractReport;
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

    public void observeSectionReports(@Observes(precedence = -100) SectionEvent event) {
        processEvent(event);
    }

    public void observeTestSuiteReports(@Observes TestSuiteSection event) {
        processEvent(event);
    }

    public void observeTestClassReports(@Observes TestClassSection event) {
            processEvent(event);
    }

    public void observeTestMethodReports(@Observes TestMethodSection event) {
        processEvent(event);

    }

    public void observeReportTestSuiteConfigurationEvents(@Observes TestSuiteConfigurationSection event) {
            processEvent(event);
    }

    private <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent>
    void processEvent(SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE> event) {

        if (event.isProcessed()){
            return;
        }
        SectionTree eventTree = createTreeRecursively(event, null);
        report().getSectionTree().merge(eventTree);
        event.setProcessed(true);
    }

    private <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent>, REPORT_TYPE extends AbstractReport>
    SectionTree<SECTIONTYPE, REPORT_TYPE> createTreeRecursively(
        SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent> sectionEvent,
        SectionTree<SECTIONTYPE, REPORT_TYPE> subtree) {

        if (sectionEvent == null){
            return subtree;
        }

        SectionTree<SECTIONTYPE, REPORT_TYPE> sectionTree = new SectionTree<>(sectionEvent.identifyYourself(), sectionEvent.getReport());
        if (subtree != null) {
            sectionTree.getSubtrees().add(subtree);
        }

        SectionEvent parentSectionThisSectionBelongsTo = sectionEvent.getParentSectionThisSectionBelongsTo();
        if (parentSectionThisSectionBelongsTo == null && sectionEvent.getClass() == TestSuiteSection.class){
            parentSectionThisSectionBelongsTo = new ExecutionSection();
        }

        return createTreeRecursively(parentSectionThisSectionBelongsTo, sectionTree);
    }

    public void afterSuite(@Observes ManagerStopping event) throws IOException {
        printJson();
    }

    private void printJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(report().getTestSuiteReports());
        try {
            FileUtils.writeStringToFile(new File("target/fire.json"), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
