package org.arquillian.reporter.impl.section.nonexisting;

import java.util.List;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.SectionTree;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.prepareReportWithDefaults;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSectionIdentifier;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdentifierFromList;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class AbstractNonExistingTreeSectionTest {

    public static final String NON_EXISTING_SECTION_NAME = "non-existing-section";
    public static final String SECTION_IN_NON_EXISTING_SECTION_NAME = "section-in-non-existing-section";
    public static final String REPORT_NAME_IN_NON_EXISTING_SECTION = "report-in-non-existing-section";

    // creation
    public static <T extends Report> T createReportInNonExistingSection(Class<T> reportClass) throws Exception {
        return prepareReportWithDefaults(reportClass, REPORT_NAME_IN_NON_EXISTING_SECTION);
    }

    // verification
    protected SectionTree verifyNonExistingSuiteSectionAddedAndGetTree(
        SectionTree parentTree,
        SectionEvent expectedSubsection,
        int expectedSubtreesNum,
        int expectedAllTreeNodesNum) {

        SectionEvent subsection = expectedSubsection;
        try {
            subsection = expectedSubsection.getClass().newInstance();
            subsection.setSectionId(NON_EXISTING_SECTION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SectionEvent expectedSection = expectedSubsection.getParentSectionThisSectionBelongsTo();
        if (expectedSection.getSectionId() == null) {
            expectedSection.setSectionId(NON_EXISTING_SECTION_NAME);
        }
        Identifier suiteIdentifier = getSectionIdentifier(expectedSection);

        assertThatSectionTree(parentTree)
            .hasSubtreeWithIdentifier(suiteIdentifier)
            .hasNumberOfSubTrees(expectedSubtreesNum)
            .wholeTreeHasNumberOfTreeNodes(expectedAllTreeNodesNum);

        SectionTree sectionTree =
            (SectionTree) getTreeWithIdentifierFromList(parentTree.getSubtrees(), suiteIdentifier).get();

        if (expectedSubsection.getReport() != null) {
            assertThatSectionTree(sectionTree)
                .hasSubtreeMatchingSection(expectedSubsection);
        } else {
            assertThatSectionTree(sectionTree)
                .hasSubtreeWithIdentifier(getSectionIdentifier(expectedSubsection));
        }
        assertThatSectionTree(sectionTree)
            .hasNumberOfSubTrees(1)
            .associatedReport()
            .hasName(expectedSection.getSectionId());

        return sectionTree;

    }

    protected <T extends Report> T verifyNonExistingSectionAddedAndGetReport(
        SectionTree parentTree,
        SectionEvent expectedSubsection,
        List<T> reportsToFilter,
        int expectedSubtreesNum,
        int expectedAllTreeNodesNum) {

        SectionTree sectionTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(parentTree, expectedSubsection, expectedSubtreesNum,
                                                         expectedAllTreeNodesNum);

        return reportsToFilter
            .stream()
            .filter(section -> section.equals(sectionTree.getAssociatedReport()))
            .findFirst()
            .get();
    }

    protected void verifyConfigAddedInNonExistingSection(ConfigurationReport configuration) {

        assertThatReport(configuration)
            .hasNumberOfSubreports(1);

        assertThatReport(configuration.getSubReports().get(0))
            .hasName(REPORT_NAME_IN_NON_EXISTING_SECTION)
            .hasGeneratedSubreportsAndEntriesWithDefaults();
    }

}
