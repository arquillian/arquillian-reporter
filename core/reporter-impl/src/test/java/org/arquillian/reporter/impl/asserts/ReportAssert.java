package org.arquillian.reporter.impl.asserts;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getConfigReportName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getFailureReportName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestClassReportName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestClassNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestMethodReportName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestMethodSectionName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestMethodNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestSuiteNameSuffix;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestSuiteReportName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestSuiteSectionName;
import static org.arquillian.reporter.impl.utils.Utils.getKeyValueEntryWitIndex;
import static org.arquillian.reporter.impl.utils.Utils.getReportWithIndex;
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

    public ReportAssert wholeExecutionReportTreeConsistOfGeneratedReports(
        Class<? extends Report>... reportTypes) {
        return wholeExecutionReportTreeConsistOfGeneratedReports(null, null, null, reportTypes);
    }

    public ReportAssert wholeExecutionReportTreeConsistOfGeneratedReports(
        String parentSectionTestSuiteName,
        String parentSectionTestClassName,
        String parentSectionTestMethodName,
        Class<? extends Report>... reportTypes) {

        List<Class<? extends Report>> types = Arrays.asList(reportTypes);

        if (actual instanceof ExecutionReport && types.contains(TestSuiteReport.class)) {
            List<TestSuiteReport> testSuiteReports = ((ExecutionReport) actual).getTestSuiteReports();
            assertThat(testSuiteReports)
                .hasSize(EXPECTED_NUMBER_OF_SECTIONS);

            IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
                TestSuiteReport reportOnIndex = testSuiteReports.get(index);

                assertThatReport(reportOnIndex)
                    .hasName(getTestSuiteReportName(index))
                    .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                    .wholeExecutionReportTreeConsistOfGeneratedReports(getTestSuiteSectionName(index), null, null,
                                                                       reportTypes);

            });

        } else if (actual instanceof TestSuiteReport) {
            TestSuiteReport actualReport = (TestSuiteReport) actual;
            if (types.contains(TestClassReport.class)) {
                List<TestClassReport> testClassReports = actualReport.getTestClassReports();
                assertThat(testClassReports)
                    .hasSize(1);

                int index = 0;
                TestClassReport reportOnIndex = testClassReports.get(index);

                assertThatReport(reportOnIndex)
                    .hasName(getTestClassReportName(index, parentSectionTestSuiteName))
                    .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                    .wholeExecutionReportTreeConsistOfGeneratedReports(parentSectionTestSuiteName,
                                                                       DummyTestClass.class.getCanonicalName(),
                                                                       null, reportTypes);
            }
            if (shouldBeVerified(ConfigurationReport.class, TestSuiteReport.class, types)) {
                verifyConfigReports(actualReport.getConfiguration(),
                                    getTestSuiteNameSuffix(parentSectionTestSuiteName));
            }
        } else if (actual instanceof TestClassReport) {
            TestClassReport actualReport = (TestClassReport) actual;
            if (types.contains(TestMethodReport.class)) {
                List<TestMethodReport> testMethodReports = actualReport.getTestMethodReports();
                assertThat(testMethodReports)
                    .hasSize(EXPECTED_NUMBER_OF_SECTIONS);

                IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
                    TestMethodReport reportOnIndex = testMethodReports.get(index);

                    assertThatReport(reportOnIndex)
                        .hasName(getTestMethodReportName(index, parentSectionTestSuiteName, parentSectionTestClassName))
                        .hasGeneratedSubreportsAndEntries(index + 1, index + 10)
                        .hasNumberOfSubreportsAndEntries(9)
                        .wholeExecutionReportTreeConsistOfGeneratedReports(parentSectionTestSuiteName,
                                                                           DummyTestClass.class.getCanonicalName(),
                                                                           ReporterUtils.getTestMethodId(
                                                                               getTestMethodSectionName(index)),
                                                                           reportTypes);
                });
            }
            if (shouldBeVerified(ConfigurationReport.class, TestClassReport.class, types)) {
                verifyConfigReports(actualReport.getConfiguration(),
                                    getTestClassNameSuffix(parentSectionTestClassName, parentSectionTestSuiteName));
            }
        } else if (actual instanceof TestMethodReport) {
            TestMethodReport actualReport = (TestMethodReport) actual;
            String testMethodNameSuffix =
                getTestMethodNameSuffix(parentSectionTestMethodName, parentSectionTestSuiteName);

            if (shouldBeVerified(ConfigurationReport.class, TestMethodReport.class, types)) {
                verifyConfigReports(actualReport.getConfiguration(), testMethodNameSuffix);
            }
            if (shouldBeVerified(FailureReport.class, TestMethodReport.class, types)) {
                verifyFailureReports(actualReport.getFailureReport(), testMethodNameSuffix);
            }
        }
        return this;
    }

    private void verifyConfigReports(ConfigurationReport configuration, String nameSuffix) {
        assertThatReport(configuration)
            .hasNumberOfSubreports(EXPECTED_NUMBER_OF_SECTIONS);

        IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
            Report reportOnIndex = configuration.getSubReports().get(index);

            assertThatReport(reportOnIndex)
                .hasName(getConfigReportName(index, nameSuffix))
                .hasGeneratedSubreportsAndEntries(index + 1, index + 10);
        });
    }

    private void verifyFailureReports(FailureReport failure, String nameSuffix) {
        assertThatReport(failure)
            .hasNumberOfSubreports(EXPECTED_NUMBER_OF_SECTIONS);

        IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
            Report reportOnIndex = failure.getSubReports().get(index);

            assertThatReport(reportOnIndex)
                .hasName(getFailureReportName(index, nameSuffix))
                .hasGeneratedSubreportsAndEntries(index + 1, index + 10);
        });
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
