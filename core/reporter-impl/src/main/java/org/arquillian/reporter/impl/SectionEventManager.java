package org.arquillian.reporter.impl;

import java.util.List;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.Standalone;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionEventManager {

    public static <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent>
    void processEvent(SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE> event, ExecutionStore executionStore) {

        // process all entries (in this report and all sub-reports) and get output entry instances
        processEntries(event.getReport());

        Class<REPORT_TYPE> expectedPayload = event.getReportTypeClass();
        Class<? extends AbstractReport> actualReportClass = event.getReport().getClass();

        // check if the report should be added as basic sub-report (into the list of sub-reports)
        if (event.isContainsSubReport()) {
            // if yes, then wrap it so it is merged and report attached in the list
            wrapReport(expectedPayload, event, actualReportClass);

            // if not, then do another check - if the expected payload type is different then the actual one and also that
            // the report should not be treated as a standalone report
        } else if (!expectedPayload.isAssignableFrom(actualReportClass)
            && (!Standalone.getStandaloneId().equals(event.getSectionId()) || !Standalone.class
            .isAssignableFrom(event.getClass()))) {
            // then wrap it
            wrapReport(expectedPayload, event, actualReportClass);
        }

        // create an expected path in the section tree to the report
        SectionTree eventTree = createTreeRecursively(event, null);
        // and merge the expected path with the current state of section tree
        executionStore.getSectionTree().mergeSectionTree(eventTree);
    }

    private static void processEntries(Report report){
        report.processEntries();
        List<Report> subReports = report.getSubReports();
        subReports.forEach(SectionEventManager::processEntries);
    }

    private static <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent>
    void wrapReport(Class<REPORT_TYPE> expectedPayload, SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE> event,
        Class<? extends AbstractReport> actualReportClass) {
        REPORT_TYPE wrapper = SecurityActions.newInstance(expectedPayload, new Class[] {}, new Object[] {});
        wrapper.addNewReport(event.getReport(), actualReportClass);
        event.setReport((REPORT_TYPE) wrapper);
    }

    private static <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent>, REPORT_TYPE extends AbstractReport>
    SectionTree<SECTIONTYPE, REPORT_TYPE> createTreeRecursively(
        SectionEvent<SECTIONTYPE, REPORT_TYPE, ? extends SectionEvent> sectionEvent,
        SectionTree<SECTIONTYPE, REPORT_TYPE> subtree) {

        if (sectionEvent == null) {
            return subtree;
        }

        SectionTree<SECTIONTYPE, REPORT_TYPE> sectionTree =
            new SectionTree<>(sectionEvent.identifyYourself(), sectionEvent.getReport(),
                              sectionEvent.getReportTypeClass());
        if (subtree != null) {
            sectionTree.getSubtrees().add(subtree);
        }

        SectionEvent parentSectionThisSectionBelongsTo = sectionEvent.getParentSectionThisSectionBelongsTo();
        if (parentSectionThisSectionBelongsTo == null && sectionEvent.getClass() == TestSuiteSection.class) {
            parentSectionThisSectionBelongsTo = new ExecutionSection();
        }

        return createTreeRecursively(parentSectionThisSectionBelongsTo, sectionTree);
    }

}
