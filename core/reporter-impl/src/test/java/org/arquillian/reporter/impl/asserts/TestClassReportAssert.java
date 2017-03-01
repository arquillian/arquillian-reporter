package org.arquillian.reporter.impl.asserts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;

import static org.arquillian.reporter.impl.asserts.TestMethodReportAssert.assertThatTestMethodReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodForSection;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReportAssert extends WithConfigReportAssert<TestClassReportAssert, TestClassReport> {

    public TestClassReportAssert(TestClassReport actual) {
        super(actual, TestClassReportAssert.class);
    }

    public static TestClassReportAssert assertThatTestClassReport(TestClassReport actual) {
        return new TestClassReportAssert(actual);
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also TestMethodReport for TestClassReport)
     * according to the given set of report types. This tells which reports should be verified for their presence. The order
     * in the set is important. Start with the TestMethodReport.class and then continue with its sub-reports. If you want to
     * verify also the ConfigurationReport then you need to specify this type after each type where the config report should
     * be present. to verify config reports in both TestClassReport and TestMethodReport, provide this set:
     * <p>
     * TestClassReport.class, ConfigurationReport.class, TestMethodReport.class, ConfigurationReport.class,
     * </p>
     * When the report types are matched it checks that it contains only generated sub-reports and entries, except for
     * the given set of merged reports. It is also checked that all given merged reports are contained in the report tree
     */
    public TestClassReportAssert reportSubtreeConsistOfGeneratedReports(
        String parentSectionTestSuiteName,
        ArrayList<Report> merged,
        Class<? extends Report>... reportTypes) {

        List<Class<? extends Report>> types = Arrays.asList(reportTypes);
        String testClassName = DummyTestClass.class.getCanonicalName();

        // check if the test method reports should be verified
        if (types.contains(TestMethodReport.class)) {
            List<TestMethodReport> testMethodReports = actual.getTestMethodReports();

            // check the expected number of test method reports - should be same as the number of generated sections
            assertThat(testMethodReports)
                .as("Test class report with name <%s> should contain an exact number of test method reports",
                    actual.getName())
                .hasSize(EXPECTED_NUMBER_OF_SECTIONS);

            // for each test method report
            IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
                TestMethodReport reportOnIndex = testMethodReports.get(index);

                defaultCheckOfIfMergedOrContainsGeneratedSubReports(merged, index, reportOnIndex);

                assertThatTestMethodReport(reportOnIndex)
                    .hasName(getTestMethodReportName(index, parentSectionTestSuiteName,
                                                     testClassName))
                    .reportSubtreeConsistOfGeneratedReports(parentSectionTestSuiteName,
                                                            ReporterUtils.getTestMethodId(
                                                                getTestMethodForSection(index)),
                                                            merged,
                                                            reportTypes);
            });
        }

        // check if ConfigurationReport of TestClassReport should be verified
        if (shouldBeVerified(ConfigurationReport.class, TestClassReport.class, types)) {
            // if yes, then verify the ConfigurationReport in this test class report
            verifyConfigReports(actual.getConfiguration(),
                                getTestClassNameSuffix(testClassName, parentSectionTestSuiteName), merged);
        }
        return this;
    }

    public TestClassReportAssert hasTestMethodReportsExactly(TestMethodReport... testMethodReports) {
        assertThat(actual.getTestMethodReports())
            .as("List of test methods reports stored in the test class report with name <%s> should consist exactly of the given set of test method reports",
                actual.getName())
            .containsExactly(testMethodReports);
        return this;
    }

    public TestClassReportAssert hasTestMethodReportListEqualTo(List<TestMethodReport> testMethodReportList) {
        assertThat(actual.getTestMethodReports())
            .as("The test method report list stored in test class report with name <%s> should be equal to the given one",
                actual.getName())
            .isEqualTo(testMethodReportList);
        return this;
    }

}
