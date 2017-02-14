package org.arquillian.reporter.impl.section.merge;

import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getConfigReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassConfigSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteSectionName;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSectionMergeTest extends AbstractMergeTest {

    @Test
    public void testMergeTestClassSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        TestClassReport testClassReport = getPreparedReportToMergeOnIndex(TestClassReport.class);
        TestClassSection toMerge =
            new TestClassSection(testClassReport, DummyTestClass.class, getTestSuiteSectionName(SECTION_MERGE_INDEX));

        String testClassReportName =
            getTestClassReportName(0, getTestSuiteSectionName(SECTION_MERGE_INDEX));
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, testClassReportName,
                                                                EXPECTED_NUMBER_OF_SECTIONS * 9 + 10);
    }

    @Test
    public void testMergeTestClassConfigurationSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        ConfigurationReport configReport = getPreparedReportToMergeOnIndex(ConfigurationReport.class);
        String sectionName = getTestSuiteSectionName(SECTION_MERGE_INDEX);

        TestClassConfigurationSection toMerge =
            new TestClassConfigurationSection(configReport,
                                              getTestClassConfigSectionName(SECTION_MERGE_INDEX),
                                              DummyTestClass.class, sectionName);

        String testClassNameSuffix = getTestClassNameSuffix(DummyTestClass.class.getCanonicalName(), sectionName);
        String configReportName = getConfigReportName(SECTION_MERGE_INDEX, testClassNameSuffix);

        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, configReportName);

    }

    @Test
    public void testMergeLatestTestClassSectionInComplexTreeUsingEventManager() throws Exception {
        TestClassReport testClassReport = getPreparedReportToMergeLatest(TestClassReport.class);
        TestClassSection toMerge = new TestClassSection(testClassReport);

        String testClassReportName =
            getTestClassReportName(0, getTestSuiteSectionName(LATEST_SECTION_INDEX));
        verifyMergeLatestSectionInComplexTreeUsingEventManager(toMerge, DummyTestClass.class.getCanonicalName(),
                                                               testClassReportName,
                                                               EXPECTED_NUMBER_OF_SECTIONS * 9 + 10);
    }

    @Test
    public void testMergeLatestTestClassConfigurationSectionInComplexTreeUsingEventManager() throws Exception {
        ConfigurationReport configReport = getPreparedReportToMergeLatest(ConfigurationReport.class);

        TestClassConfigurationSection toMerge = new TestClassConfigurationSection(configReport);

        String classCanonicalName = DummyTestClass.class.getCanonicalName();
        String sectionName = getTestSuiteSectionName(LATEST_SECTION_INDEX);
        String testClassNameSuffix = getTestClassNameSuffix(classCanonicalName, sectionName);
        String configReportName = getConfigReportName(LATEST_SECTION_INDEX, testClassNameSuffix);

        String configSectionName =
            ReporterUtils.buildId(classCanonicalName, getTestClassConfigSectionName(LATEST_SECTION_INDEX));

        verifyMergeLatestSectionInComplexTreeUsingEventManager(toMerge,
                                                               configSectionName,
                                                               configReportName);
    }

}
