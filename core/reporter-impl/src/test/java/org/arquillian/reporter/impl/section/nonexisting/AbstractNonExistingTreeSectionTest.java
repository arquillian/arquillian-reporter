package org.arquillian.reporter.impl.section.nonexisting;

import java.util.List;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;
import org.arquillian.reporter.impl.utils.dummy.SecondDummyTestClass;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.prepareReportWithDefaults;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSectionIdentifier;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdentifierFromList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class AbstractNonExistingTreeSectionTest {

    public static final String NON_EXISTING_SECTION_NAME = "non-existing-section";
    public static final String REPORT_NAME_IN_NON_EXISTING_SECTION = "report-in-non-existing-section";

    // creation

    public static <T extends Report> T createReportInNonExistingSection(Class<T> reportClass)
        throws InstantiationException, IllegalAccessException {
        return prepareReportWithDefaults(reportClass, REPORT_NAME_IN_NON_EXISTING_SECTION);
    }

    protected TestSuiteConfigurationSection createTestSuiteConfigSectionInNonExistingSection()
        throws IllegalAccessException, InstantiationException {
        return new TestSuiteConfigurationSection(
            createReportInNonExistingSection(ConfigurationReport.class),
            NON_EXISTING_SECTION_NAME,
            REPORT_NAME_IN_NON_EXISTING_SECTION);
    }

    protected TestClassSection createTestClassSectionInNonExistingSection()
        throws IllegalAccessException, InstantiationException {
        return new TestClassSection(
            createReportInNonExistingSection(TestClassReport.class),
            DummyTestClass.class,
            NON_EXISTING_SECTION_NAME);
    }

    protected TestClassConfigurationSection createTestClassConfigSectionInNonExistingSection()
        throws IllegalAccessException, InstantiationException {
        return new TestClassConfigurationSection(
            createReportInNonExistingSection(ConfigurationReport.class),
            SecondDummyTestClass.class,
            NON_EXISTING_SECTION_NAME);
    }

    // verification

    protected <T extends Report> T verifyNonExistingSuiteSectionAddedAndGetReport(
        SectionTree parentTree,
        SectionEvent expectedSubsection,
        List<T> reportsToFilter,
        int expectedSubtreesNum,
        int expectedAllTreeNodesNum) {

//        SectionTree executionTree = executionReport.getSectionTree();
        SectionEvent subsection = expectedSubsection;
        try {
            subsection = expectedSubsection.getClass().newInstance();
            subsection.setSectionId(NON_EXISTING_SECTION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Identifier suiteIdentifier = getSectionIdentifier(new TestSuiteSection(NON_EXISTING_SECTION_NAME));

        assertThatSectionTree(parentTree)
            .hasSubtreeWithIdentifier(suiteIdentifier)
            .hasNumberOfSubTrees(expectedSubtreesNum)
            .wholeTreeHasNumberOfTreeNodes(expectedAllTreeNodesNum);

        SectionTree suiteTree =
            (SectionTree) getTreeWithIdentifierFromList(parentTree.getSubtrees(), suiteIdentifier).get();

        if (expectedSubsection != null){
            assertThatSectionTree(suiteTree)
                .hasSubtreeMatchingSection(expectedSubsection);
        }
        assertThatSectionTree(suiteTree)
            .hasNumberOfSubTrees(1)
            .associatedReport()
            .hasName(NON_EXISTING_SECTION_NAME);

        return reportsToFilter
            .stream()
            .filter(section -> section.equals(suiteTree.getAssociatedReport()))
            .findFirst()
            .get();
    }

    protected void verifyTestClassAddedInNonExistingSection(List<TestClassReport> classReports){
        assertThat(classReports).hasSize(1);
        TestClassReport testClassReport = classReports.get(0);

        assertThatReport(testClassReport)
            .hasName(REPORT_NAME_IN_NON_EXISTING_SECTION)
            .hasGeneratedSubreportsAndEntriesWithDefaults();

        assertThatReport(testClassReport.getConfiguration()).hasNumberOfSubreportsAndEntries(0);
    }

    protected void verifyConfigAddedInNonExistingSection(ConfigurationReport configuration){

        assertThatReport(configuration)
            .hasNumberOfSubreports(1);

        assertThatReport(configuration.getSubReports().get(0))
            .hasName(REPORT_NAME_IN_NON_EXISTING_SECTION)
            .hasGeneratedSubreportsAndEntriesWithDefaults();
    }

}
