package org.arquillian.reporter.impl;

import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.junit.Test;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionTreeTest {

    KeyValueEntry firstEntry = new KeyValueEntry("first entry", "cool first entry");
    KeyValueEntry secondEntry = new KeyValueEntry("second entry", "cool second entry");
    KeyValueEntry thirdEntry = new KeyValueEntry("third entry", "cool third entry");
    KeyValueEntry fourthEntry = new KeyValueEntry("fourth entry", "cool fourth entry");

    @Test
    public void mergeExecutionSectionTree() {

        Identifier<ExecutionSection> executionSectionId = new Identifier<>(ExecutionSection.class, "test-execution");

        ExecutionReport firstTestSuiteReport = new ExecutionReport();
        feedReportWithData(firstTestSuiteReport, true);

        ExecutionReport secondTestSuiteReport = new ExecutionReport();
        feedReportWithData(secondTestSuiteReport, true);

        SectionTree<ExecutionSection, ExecutionReport> executionTree =
            new SectionTree<>(executionSectionId, firstTestSuiteReport);

        SectionTree<ExecutionSection, ExecutionReport> executionTreeToMerge =
            new SectionTree<>(executionSectionId, secondTestSuiteReport);

        executionTree.merge(executionTreeToMerge);
    }


    private void verifyContent(AbstractReport report){
//        assertThat(report)
    }

    private void feedReportWithData(AbstractReport report, boolean isFirst){
        if (isFirst) {
            report.getEntries().add(firstEntry);
            report.getEntries().add(secondEntry);
        } else {
            report.getEntries().add(thirdEntry);
            report.getEntries().add(fourthEntry);
        }
    }



}
