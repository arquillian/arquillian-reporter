package org.arquillian.reporter.impl.asserts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestMethodReport;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getFailureReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodNameSuffix;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReportAssert extends WithConfigReportAssert<TestMethodReportAssert, TestMethodReport> {

    public TestMethodReportAssert(TestMethodReport actual) {
        super(actual, TestMethodReportAssert.class);
    }

    public static TestMethodReportAssert assertThatTestMethodReport(TestMethodReport actual) {
        return new TestMethodReportAssert(actual);
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also ConfigurationReport for FailureReport)
     * according to the given set of report types. This tells which reports should be verified for their presence.
     * To verify both config and failure reports provide this set:
     * <p>
     * ConfigurationReport.class, FailureReport.class
     * </p>
     * When the report types are matched it checks that it contains only generated sub-reports and entries, except for
     * the given set of merged reports. It is also checked that all given merged reports are contained in the report tree
     */
    public TestMethodReportAssert reportSubtreeConsistOfGeneratedReports(
        String parentSectionTestSuiteName,
        String parentSectionTestMethodName,
        ArrayList<Report> merged,
        Class<? extends Report>... reportTypes) {

        List<Class<? extends Report>> types = Arrays.asList(reportTypes);

        String testMethodNameSuffix =
            getTestMethodNameSuffix(parentSectionTestMethodName, parentSectionTestSuiteName);

        // check if ConfigurationReport of the actual test method report should be verified
        if (shouldBeVerified(ConfigurationReport.class, TestMethodReport.class, types)) {
            verifyConfigReports(actual.getConfiguration(), testMethodNameSuffix, merged);
        }

        // check if FailureReport of the actual test method report should be verified
        if (shouldBeVerified(FailureReport.class, null, types)) {
            verifyFailureReports(actual.getFailureReport(), testMethodNameSuffix, merged);
        }

        return this;
    }

    public TestMethodReportAssert hasFailureSubReportsContainingExactly(FailureReport... failureReports) {
        assertThat(actual.getFailureReport().getSubReports())
            .as("The test method report with name <%s> should contain exactly the given failure sub-reports",
                actual.getName())
            .containsExactly(failureReports);
        return this;
    }

    public TestMethodReportAssert hasFailureSubReportListEqualTo(List<FailureReport> failureReportList) {
        assertThat(actual.getFailureReport().getSubReports())
            .as("The failure report list stored in the test method report with name <%s> should be equal to the given one",
                actual.getName())
            .isEqualTo(failureReportList);
        return this;
    }

    public TestMethodReportAssert hasFailureWithNumberOfSubreportsAndEntries(int number) {
        ReportAssert.assertThatReport(actual.getFailureReport())
            .as("The failure report stored in the test method report with name <%s> should contain the given number of sub-reports and entries: <%s>",
                actual.getName(), number)
            .hasNumberOfSubReportsAndEntries(number);
        return this;
    }

    private void verifyFailureReports(FailureReport failure, String nameSuffix, List<Report> merged) {
        assertThatReport(failure)
            .hasNumberOfSubReports(EXPECTED_NUMBER_OF_SECTIONS);

        IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
            Report reportOnIndex = failure.getSubReports().get(index);

            defaultCheckOfIfMergedOrContainsGeneratedSubReports(merged, index, reportOnIndex);

            assertThatReport(reportOnIndex)
                .hasName(getFailureReportName(index, nameSuffix));
        });
    }
}
