package org.arquillian.reporter.impl.section.merge;

import java.util.Arrays;
import java.util.Collection;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.event.TestMethodFailureSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.arquillian.reporter.impl.utils.dummy.SectionUnderTestMethodConfigSection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Parameterized.class)
public class AllSectionTreeMergeTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { ExecutionSection.class, ExecutionReport.class },
            { ExecutionSection.class, BasicReport.class },
            { TestSuiteSection.class, TestSuiteReport.class },
            { TestSuiteSection.class, BasicReport.class },
            { TestSuiteConfigurationSection.class, ConfigurationReport.class },
            { TestSuiteConfigurationSection.class, BasicReport.class },
            { TestClassSection.class, TestClassReport.class },
            { TestClassSection.class, BasicReport.class },
            { TestClassConfigurationSection.class, ConfigurationReport.class },
            { TestClassConfigurationSection.class, BasicReport.class },
            { TestMethodSection.class, TestMethodReport.class },
            { TestMethodSection.class, BasicReport.class },
            { TestMethodConfigurationSection.class, ConfigurationReport.class },
            { TestMethodConfigurationSection.class, BasicReport.class },
            { TestMethodFailureSection.class, FailureReport.class },
            { TestMethodFailureSection.class, BasicReport.class },
            { SectionUnderTestMethodConfigSection.class, BasicReport.class }
        });
    }

    private Class<SectionEvent> sectionClass;
    private Class<AbstractReport> reportClass;

    public AllSectionTreeMergeTest(Class<SectionEvent> sectionClass, Class<AbstractReport> reportClass) {
        this.sectionClass = sectionClass;
        this.reportClass = reportClass;
    }

    @Test
    public void mergeExecutionSectionTree() throws Exception {

        // use same identifier to merge
        Identifier executionSectionId = new Identifier<>(sectionClass, sectionClass.getCanonicalName());

        // create first dummy execution report
        AbstractReport firstExecutionReport = ReportGeneratorUtils
            .prepareReport(reportClass, reportClass.getCanonicalName(), 1, 5);

        // create second dummy execution report
        AbstractReport secondExecutionReport = ReportGeneratorUtils
            .prepareReport(reportClass, reportClass.getCanonicalName(), 5, 10);

        // create execution section tree that should consume another tree
        SectionTree originalExecutionTree = new SectionTree<>(executionSectionId, firstExecutionReport);

        // create execution section tree that should be merged
        SectionTree executionTreeToMerge = new SectionTree<>(executionSectionId, secondExecutionReport);

        // merge
        originalExecutionTree.mergeSectionTree(executionTreeToMerge);

        // verify
        assertThatSectionTree(originalExecutionTree)
            .hasRootIdentifier(executionSectionId)
            .doesNotHavyAnySubtree()
            .associatedReport() // verify the associated report
            .hasName(reportClass.getCanonicalName())
            .hasGeneratedSubreportsAndEntries(1, 10)
            .hasNumberOfSubreportsAndEntries(9);
    }

}
