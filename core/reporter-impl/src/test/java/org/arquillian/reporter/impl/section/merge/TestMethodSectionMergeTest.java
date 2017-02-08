package org.arquillian.reporter.impl.section.merge;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.event.TestMethodFailureSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getConfigReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getFailureReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodConfigSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodFailureSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteSectionName;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSectionMergeTest extends AbstractMergeTest {

    @Test
    public void testMergeTestMethodSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        TestMethodReport methodReport = getPreparedReportToMergeOnIndex(TestMethodReport.class);
        TestMethodSection toMerge =
            new TestMethodSection(methodReport, getDummyMethod(SECTION_MERGE_INDEX),
                                  getTestSuiteSectionName(SECTION_MERGE_INDEX));

        String testMethodReportName =
            getTestMethodReportName(SECTION_MERGE_INDEX, getTestSuiteSectionName(SECTION_MERGE_INDEX),
                                    DummyTestClass.class.getCanonicalName());
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, testMethodReportName);
    }

    @Test
    public void testMergeTestMethodConfigurationSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        ConfigurationReport configReport = getPreparedReportToMergeOnIndex(ConfigurationReport.class);
        String suiteName = getTestSuiteSectionName(SECTION_MERGE_INDEX);
        Method dummyMethod = getDummyMethod(SECTION_MERGE_INDEX);

        TestMethodConfigurationSection toMerge =
            new TestMethodConfigurationSection(configReport, dummyMethod,
                                               getTestMethodConfigSectionName(SECTION_MERGE_INDEX), suiteName);

        String testMethodNameSuffix = getTestMethodNameSuffix(ReporterUtils.getTestMethodId(dummyMethod), suiteName);
        String configReportName = getConfigReportName(SECTION_MERGE_INDEX, testMethodNameSuffix);

        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, configReportName);
    }

    @Test
    public void testMergeTestMethodFailureSectionUsingIdInComplexTreeUsingEventManager() throws Exception {
        FailureReport failureReport = getPreparedReportToMergeOnIndex(FailureReport.class);
        String suiteName = getTestSuiteSectionName(SECTION_MERGE_INDEX);
        Method dummyMethod = getDummyMethod(SECTION_MERGE_INDEX);

        TestMethodFailureSection toMerge =
            new TestMethodFailureSection(failureReport, dummyMethod,
                                         getTestMethodFailureSectionName(SECTION_MERGE_INDEX), suiteName);

        String testMethodNameSuffix = getTestMethodNameSuffix(ReporterUtils.getTestMethodId(dummyMethod), suiteName);
        String configReportName = getFailureReportName(SECTION_MERGE_INDEX, testMethodNameSuffix);

        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(toMerge, configReportName);
    }

    @Test
    public void testMergeLatestTestMethodSectionInComplexTreeUsingEventManager() throws Exception {
        TestMethodReport methodReport = getPreparedReportToMergeLatest(TestMethodReport.class);
        TestMethodSection toMerge = new TestMethodSection(methodReport);

        String testMethodReportName =
            getTestMethodReportName(LATEST_SECTION_INDEX, getTestSuiteSectionName(LATEST_SECTION_INDEX),
                                    DummyTestClass.class.getCanonicalName());
        String methodSectionId = ReporterUtils.getTestMethodId(getDummyMethod(LATEST_SECTION_INDEX));

        verifyMergeLatestSectionInComplexTreeUsingEventManager(toMerge, methodSectionId, testMethodReportName);
    }

    @Test
    public void testMergeLatestTestMethodConfigurationSectionInComplexTreeUsingEventManager() throws Exception {
        ConfigurationReport configReport = getPreparedReportToMergeLatest(ConfigurationReport.class);

        TestMethodConfigurationSection toMerge = new TestMethodConfigurationSection(configReport);

        String suiteName = getTestSuiteSectionName(LATEST_SECTION_INDEX);
        Method dummyMethod = getDummyMethod(LATEST_SECTION_INDEX);
        String testMethodNameSuffix = getTestMethodNameSuffix(ReporterUtils.getTestMethodId(dummyMethod), suiteName);
        String configReportName = getConfigReportName(LATEST_SECTION_INDEX, testMethodNameSuffix);

        String methodId = ReporterUtils.getTestMethodId(getDummyMethod(LATEST_SECTION_INDEX));
        String configId = ReporterUtils.buildId(methodId, getTestMethodConfigSectionName(LATEST_SECTION_INDEX));

        verifyMergeLatestSectionInComplexTreeUsingEventManager(toMerge, configId, configReportName);
    }

    @Test
    public void testMergeLatestTestMethodFailureSectionInComplexTreeUsingEventManager() throws Exception {
        FailureReport failureReport = getPreparedReportToMergeLatest(FailureReport.class);

        TestMethodFailureSection toMerge = new TestMethodFailureSection(failureReport);

        String suiteName = getTestSuiteSectionName(LATEST_SECTION_INDEX);
        Method dummyMethod = getDummyMethod(LATEST_SECTION_INDEX);
        String testMethodNameSuffix = getTestMethodNameSuffix(ReporterUtils.getTestMethodId(dummyMethod), suiteName);
        String configReportName = getFailureReportName(LATEST_SECTION_INDEX, testMethodNameSuffix);

        String methodId = ReporterUtils.getTestMethodId(getDummyMethod(LATEST_SECTION_INDEX));
        String failureId = ReporterUtils.buildId(methodId, getTestMethodFailureSectionName(LATEST_SECTION_INDEX));

        verifyMergeLatestSectionInComplexTreeUsingEventManager(toMerge, failureId, configReportName);
    }

    private Method getDummyMethod(int index) {
        return DummyTestClass.class.getDeclaredMethods()[index];
    }
}
