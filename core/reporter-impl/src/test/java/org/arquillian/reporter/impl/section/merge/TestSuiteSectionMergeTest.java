package org.arquillian.reporter.impl.section.merge;

import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getConfigReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteConfigSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteSectionName;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSectionMergeTest extends AbstractMergeTest {

    @Test
    public void testMergeTestSuiteSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        TestSuiteReport testSuiteReport = getPreparedReportToMerge(TestSuiteReport.class);
        TestSuiteSection toMerge = new TestSuiteSection(testSuiteReport, getTestSuiteSectionName(SECTION_MERGE_INDEX));

        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, getTestSuiteReportName(SECTION_MERGE_INDEX));
    }

    @Test
    public void testMergeTestSuiteConfigurationSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        ConfigurationReport configReport = getPreparedReportToMerge(ConfigurationReport.class);
        String sectionName = getTestSuiteSectionName(SECTION_MERGE_INDEX);
        TestSuiteConfigurationSection toMerge =
            new TestSuiteConfigurationSection(configReport, sectionName,
                                              getTestSuiteConfigSectionName(SECTION_MERGE_INDEX));

        String configReportName = getConfigReportName(SECTION_MERGE_INDEX, getTestSuiteNameSuffix(sectionName));
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, configReportName);

    }
}
