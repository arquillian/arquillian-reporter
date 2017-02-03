package org.arquillian.reporter.impl.asserts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ListAssert;

import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.DEFAULT_END_INDEX_FOR_GENERATED_REPORT_PAYLOAD;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.DEFAULT_START_INDEX_FOR_GENERATED_REPORT_PAYLOAD;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.getKeyValueEntryWitIndex;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.getReportWithIndex;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getConfigReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getFailureReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteSectionName;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportAssert extends AbstractAssert<ReportAssert, Report> {

    public ReportAssert(Report actual) {
        super(actual, ReportAssert.class);
    }

    public static ReportAssert assertThatReport(Report actual) {
        return new ReportAssert(actual);
    }

    public ListAssert<Entry> entries() {
        return new ListAssert<>(actual.getEntries());

    }

    public ReportAssert hasName(AbstractStringKey name) {
        isNotNull();

        if (!Objects.equals(actual.getName(), name)) {
            failWithMessage("Expected name of the report should be <%s> but was <%s>", name, actual.getName());
        }
        return this;
    }

    public ReportAssert hasName(String name) {
        return hasName(new UnknownStringKey(name));
    }

    public ReportAssert hasNumberOfSubreports(int number) {
        isNotNull();

        if (actual.getSubReports().size() != number) {
            failWithMessage("Expected number of the sub-reports of the report should be <%s> but was <%s>", number,
                            actual.getSubReports().size());
        }
        return this;
    }

    public ReportAssert hasNumberOfSubreportsAndEntries(int number) {
        hasNumberOfSubreports(number);
        hasNumberOfEntries(number);
        return this;
    }

    public ReportAssert hasNumberOfEntries(int number) {
        isNotNull();

        if (actual.getEntries().size() != number) {
            failWithMessage("Expected number of entries of the report should be <%s> but was <%s>", number,
                            actual.getEntries().size());
        }
        return this;
    }

    public ReportAssert hassSubReportsContaining(AbstractReport... expectedReports) {
        isNotNull();

        assertThat(actual.getSubReports())
            .usingRecursiveFieldByFieldElementComparator()
            .as("The report should contain the expected set of sub-reports")
            .contains(expectedReports);
        return this;
    }

    public ReportAssert hassSubReportsContaining(List<AbstractReport> expectedReports) {
        isNotNull();

        assertThat(actual.getSubReports())
            .usingRecursiveFieldByFieldElementComparator()
            .as("The report should contain the expected set of sub-reports")
            .contains(expectedReports);
        return this;
    }

    public ReportAssert hasEntriesContaining(Entry... expectedEntries) {
        isNotNull();

        assertThat(actual.getEntries())
            .usingRecursiveFieldByFieldElementComparator()
            .as("The report should contain the expected set of entries")
            .contains(expectedEntries);
        return this;
    }

    public ReportAssert hasEntriesContaining(List<Entry> expectedEntries) {
        isNotNull();

        assertThat(actual.getEntries())
            .usingRecursiveFieldByFieldElementComparator()
            .as("The report should contain the expected set of entries")
            .contains(expectedEntries);
        return this;
    }

    public ReportAssert hasGeneratedSubreportsAndEntriesWithDefaults() {
        return hasGeneratedSubreportsAndEntries(DEFAULT_START_INDEX_FOR_GENERATED_REPORT_PAYLOAD,
                                                DEFAULT_END_INDEX_FOR_GENERATED_REPORT_PAYLOAD);
    }

    public ReportAssert hasGeneratedSubreportsAndEntries(int startIndex, int endIndex) {

        KeyValueEntry[] expectedEntries = IntStream
            .range(startIndex, endIndex)
            .mapToObj(index -> getKeyValueEntryWitIndex(index)).toArray(KeyValueEntry[]::new);

        AbstractReport[] expectedReports = IntStream
            .range(startIndex, endIndex)
            .mapToObj(index -> getReportWithIndex(index)).toArray(BasicReport[]::new);

        hassSubReportsContaining(expectedReports);
        hasEntriesContaining(expectedEntries);
        return this;
    }

    public ReportAssert hasSubReportsEndingWith(AbstractReport... reports) {

        assertThat(actual.getSubReports())
            .as("The sub-reports of the report should be ending with %s", reports)
            .endsWith(reports);

        return this;
    }

    public ReportAssert hasSubReportsWithout(AbstractReport... reports) {

        assertThat(actual.getSubReports())
            .as("The sub-reports should not contain any report from the set %s", reports)
            .doesNotContain(reports);

        return this;
    }

    public ReportAssert hasSubReportsThatContainsExactly(AbstractReport reports) {
        assertThat(actual.getSubReports())
            .as("The list of sub-reports of the report should contain exactly %s", reports).containsExactly(reports);
        return this;
    }

    public ReportAssert wholeExecutionReportTreeConsistOfAllGeneratedReports() {
        return wholeExecutionReportTreeConsistOfAllGeneratedReports(new ArrayList<Report>());
    }

    public ReportAssert wholeExecutionReportTreeConsistOfAllGeneratedReports(Report merged) {
        return wholeExecutionReportTreeConsistOfAllGeneratedReports(Arrays.asList(merged));
    }

    public ReportAssert wholeExecutionReportTreeConsistOfAllGeneratedReports(List<Report> merged) {
        return wholeExecutionReportTreeConsistOfGeneratedReports(
            null,
            null,
            null,
            merged,
            TestSuiteReport.class,
            ConfigurationReport.class,
            TestClassReport.class,
            ConfigurationReport.class,
            TestMethodReport.class,
            ConfigurationReport.class,
            FailureReport.class);
    }

    public ReportAssert wholeExecutionReportTreeConsistOfGeneratedReports(Class<? extends Report>... reportTypes) {
        return wholeExecutionReportTreeConsistOfGeneratedReports(null, null, null, new ArrayList<Report>(),
                                                                 reportTypes);
    }

    public ReportAssert wholeExecutionReportTreeConsistOfGeneratedReports(
        String parentSectionTestSuiteName,
        String parentSectionTestClassName,
        String parentSectionTestMethodName,
        List<Report> merged,
        Class<? extends Report>... reportTypes) {

        List<Class<? extends Report>> types = Arrays.asList(reportTypes);

        if (actual instanceof ExecutionReport && types.contains(TestSuiteReport.class)) {
            List<TestSuiteReport> testSuiteReports = ((ExecutionReport) actual).getTestSuiteReports();
            List<Report> mergedReports = getReportsOfType(merged, TestSuiteReport.class);

            assertThat(testSuiteReports)
                .hasSize(EXPECTED_NUMBER_OF_SECTIONS);

            IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
                TestSuiteReport reportOnIndex = testSuiteReports.get(index);

                if (mergedReports.contains(reportOnIndex)) {
                    mergedReports.remove(reportOnIndex);
                } else {
                    assertThatReport(reportOnIndex)
                        .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                        .hasNumberOfSubreportsAndEntries(9);

                }
                assertThatReport(reportOnIndex)
                    .hasName(getTestSuiteReportName(index))
                    .wholeExecutionReportTreeConsistOfGeneratedReports(getTestSuiteSectionName(index), null, null,
                                                                       merged,
                                                                       reportTypes);
            });
            assertThat(mergedReports).isEmpty();

        } else if (actual instanceof TestSuiteReport) {
            TestSuiteReport actualReport = (TestSuiteReport) actual;
            List<Report> mergedReports = getReportsOfType(merged, TestClassReport.class);

            if (types.contains(TestClassReport.class)) {
                List<TestClassReport> testClassReports = actualReport.getTestClassReports();
                assertThat(testClassReports)
                    .hasSize(1);

                int index = 0;
                TestClassReport reportOnIndex = testClassReports.get(index);

                if (mergedReports.contains(reportOnIndex)) {
                    mergedReports.remove(reportOnIndex);
                } else {
                    assertThatReport(reportOnIndex)
                        .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                        .hasNumberOfSubreportsAndEntries(9 * EXPECTED_NUMBER_OF_SECTIONS);
                }
                assertThatReport(reportOnIndex)
                    .hasName(getTestClassReportName(index, parentSectionTestSuiteName))
                    .wholeExecutionReportTreeConsistOfGeneratedReports(parentSectionTestSuiteName,
                                                                       DummyTestClass.class.getCanonicalName(),
                                                                       null, merged, reportTypes);
            }
            assertThat(mergedReports).isEmpty();
            if (shouldBeVerified(ConfigurationReport.class, TestSuiteReport.class, types)) {
                verifyConfigReports(actualReport.getConfiguration(),
                                    getTestSuiteNameSuffix(parentSectionTestSuiteName), merged);
            }
        } else if (actual instanceof TestClassReport) {
            TestClassReport actualReport = (TestClassReport) actual;
            List<Report> mergedReports = getReportsOfType(merged, TestClassReport.class);

            if (types.contains(TestMethodReport.class)) {
                List<TestMethodReport> testMethodReports = actualReport.getTestMethodReports();
                assertThat(testMethodReports)
                    .hasSize(EXPECTED_NUMBER_OF_SECTIONS);

                IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
                    TestMethodReport reportOnIndex = testMethodReports.get(index);

                    if (mergedReports.contains(reportOnIndex)) {
                        mergedReports.remove(reportOnIndex);
                    } else {
                        assertThatReport(reportOnIndex)
                            .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                            .hasNumberOfSubreportsAndEntries(9);
                    }
                    assertThatReport(reportOnIndex)
                        .hasName(getTestMethodReportName(index, parentSectionTestSuiteName, parentSectionTestClassName))
                        .wholeExecutionReportTreeConsistOfGeneratedReports(parentSectionTestSuiteName,
                                                                           DummyTestClass.class.getCanonicalName(),
                                                                           ReporterUtils.getTestMethodId(
                                                                               getTestMethodSectionName(index)),
                                                                           merged,
                                                                           reportTypes);
                });
            }
            assertThat(mergedReports).isEmpty();
            if (shouldBeVerified(ConfigurationReport.class, TestClassReport.class, types)) {
                verifyConfigReports(actualReport.getConfiguration(),
                                    getTestClassNameSuffix(parentSectionTestClassName, parentSectionTestSuiteName),
                                    merged);
            }
        } else if (actual instanceof TestMethodReport) {
            TestMethodReport actualReport = (TestMethodReport) actual;
            String testMethodNameSuffix =
                getTestMethodNameSuffix(parentSectionTestMethodName, parentSectionTestSuiteName);

            if (shouldBeVerified(ConfigurationReport.class, TestMethodReport.class, types)) {
                verifyConfigReports(actualReport.getConfiguration(), testMethodNameSuffix, merged);
            }
            if (shouldBeVerified(FailureReport.class, TestMethodReport.class, types)) {
                verifyFailureReports(actualReport.getFailureReport(), testMethodNameSuffix, merged);
            }
        }
        return this;
    }

    private List<Report> getReportsOfType(List<Report> reports, Class<? extends Report> reportType) {
        return reports.stream().filter(report -> report.getClass().equals(reportType)).collect(Collectors.toList());
    }

    private void verifyConfigReports(ConfigurationReport configuration, String nameSuffix, List<Report> merged) {
        assertThatReport(configuration)
            .hasNumberOfSubreports(EXPECTED_NUMBER_OF_SECTIONS);
        List<Report> mergedReports = getReportsOfType(merged, TestClassReport.class);

        IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
            Report reportOnIndex = configuration.getSubReports().get(index);

            if (mergedReports.contains(reportOnIndex)) {
                mergedReports.remove(reportOnIndex);
            } else {
                assertThatReport(reportOnIndex)
                    .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                    .hasNumberOfSubreportsAndEntries(9);
            }
            assertThatReport(reportOnIndex)
                .hasName(getConfigReportName(index, nameSuffix));
        });
        assertThat(mergedReports).isEmpty();
    }

    private void verifyFailureReports(FailureReport failure, String nameSuffix, List<Report> merged) {
        assertThatReport(failure)
            .hasNumberOfSubreports(EXPECTED_NUMBER_OF_SECTIONS);
        List<Report> mergedReports = getReportsOfType(merged, TestClassReport.class);

        IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
            Report reportOnIndex = failure.getSubReports().get(index);

            if (mergedReports.contains(reportOnIndex)) {
                mergedReports.remove(reportOnIndex);
            } else {
                assertThatReport(reportOnIndex)
                    .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                    .hasNumberOfSubreportsAndEntries(9);
            }
            assertThatReport(reportOnIndex)
                .hasName(getFailureReportName(index, nameSuffix));
        });
        assertThat(mergedReports).isEmpty();
    }

    private boolean shouldBeVerified(Class<? extends Report> toBeVerifiedClass,
        Class<? extends Report> parentReportClass, List<Class<? extends Report>> reportTypes) {
        if (parentReportClass != null) {
            int parentIndex = reportTypes.indexOf(parentReportClass);
            if (reportTypes.size() > parentIndex + 1) {
                return reportTypes.get(parentIndex + 1).equals(toBeVerifiedClass);
            }
            return false;
        }
        return reportTypes.contains(toBeVerifiedClass);
    }

}
