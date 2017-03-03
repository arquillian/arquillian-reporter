package org.arquillian.reporter.impl;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.Standalone;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionEventManager {

    public static <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent>
    void processEvent(SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE> event, ExecutionReport executionReport) {

        // check if the expected payload type is same as the actual one
        Class<REPORT_TYPE> expectedPayload = event.getReportTypeClass();
        Class<? extends AbstractReport> actualReportClass = event.getReport().getClass();
        if (!expectedPayload.isAssignableFrom(actualReportClass)
            && (!Standalone.getStandaloneId().equals(event.getSectionId()) || !Standalone.class
            .isAssignableFrom(event.getClass()))) {
            // if not, then create a report class that will be a wrapper of the actual report
            REPORT_TYPE wrapper = SecurityActions.newInstance(expectedPayload, new Class[] {}, new Object[] {});
            wrapper.addNewReport(event.getReport(), actualReportClass);
            event.setReport((REPORT_TYPE) wrapper);
        }

        // create an expected path in the section tree to the report
        SectionTree eventTree = createTreeRecursively(event, null);
        // and merge the expcted path with the current state of section tree
        executionReport.getSectionTree().mergeSectionTree(eventTree);
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
