package org.arquillian.reporter.impl.asserts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

import static org.arquillian.reporter.impl.asserts.TestClassReportAssert.assertThatTestClassReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteNameSuffix;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReportAssert extends WithConfigReportAssert<TestSuiteReportAssert, TestSuiteReport> {

    public TestSuiteReportAssert(TestSuiteReport actual) {
        super(actual, TestSuiteReportAssert.class);
    }

    public static TestSuiteReportAssert assertThatTestSuiteReport(TestSuiteReport actual) {
        return new TestSuiteReportAssert(actual);
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also TestMethodReport for TestClassReport)
     * according to the given set of report types. This tells which reports should be verified for their presence. The order
     * in the set is important. Start with the TestClassReport.class and then continue with its sub-reports. If you want to
     * verify also the ConfigurationReport then you need to specify this type after each type where the config report should
     * be present. to verify config reports in both TestClassReport and TestMethodReport, provide this set:
     * <p>
     * TestClassReport.class, ConfigurationReport.class, TestMethodReport.class, ConfigurationReport.class,
     * </p>
     * When the report types are matched it checks that it contains only generated sub-reports and entries, except for
     * the given set of merged reports. It is also checked that all given merged reports are contained in the report tree
     */
    public TestSuiteReportAssert reportSubtreeConsistOfGeneratedReports(
        String parentSectionTestSuiteName,
        ArrayList<Report> merged,
        Class<? extends Report>... reportTypes) {

        List<Class<? extends Report>> types = Arrays.asList(reportTypes);

        // check if TestClassReport should be verified
        if (types.contains(TestClassReport.class)) {
            List<TestClassReport> testClassReports = actual.getTestClassReports();

            // check that there is only one test class report present - since we have only one test class
            assertThat(testClassReports)
                .as("The test suite report with name <%s> should contain only one generated test method report",
                    actual.getName())
                .hasSize(1);

            int index = 0;
            TestClassReport reportOnIndex = testClassReports.get(index);

            // if the report on index is one of the merged reports
            if (merged.contains(reportOnIndex)) {
                // then remove from the list of merged
                merged.remove(reportOnIndex);
            } else {
                // otherwise check if it contains the number of generated sub-reports and entries
                assertThatTestClassReport(reportOnIndex)
                    .hasGeneratedSubReportsAndEntries(index + 1, index + 10)
                    .hasNumberOfSubReportsAndEntries(9 * EXPECTED_NUMBER_OF_SECTIONS);
            }

            assertThatTestClassReport(reportOnIndex)
                .hasName(getTestClassReportName(index, parentSectionTestSuiteName))
                .reportSubtreeConsistOfGeneratedReports(parentSectionTestSuiteName,
                                                        merged,
                                                        reportTypes);
        }

        // check if ConfigurationReport of TestClassReport should be verified
        if (shouldBeVerified(ConfigurationReport.class, TestSuiteReport.class, types)) {
            verifyConfigReports(actual.getConfiguration(),
                                getTestSuiteNameSuffix(parentSectionTestSuiteName), merged);
        }
        return this;
    }

    public TestSuiteReportAssert hasTestClassReportsExactly(TestClassReport... testClassReports) {
        assertThat(actual.getTestClassReports())
            .as("List of test class reports stored in the test suite report with name <%s> should consist exactly of the given set of test class reports",
                actual.getName())
            .containsExactly(testClassReports);
        return this;
    }

    public TestSuiteReportAssert hasTestClassReportListEqualTo(List<TestClassReport> testClassReportList) {
        assertThat(actual.getTestClassReports())
            .as("The test class report list stored in test suite report with name <%s> should be equal to the given one",
                actual.getName())
            .isEqualTo(testClassReportList);
        return this;
    }
}
