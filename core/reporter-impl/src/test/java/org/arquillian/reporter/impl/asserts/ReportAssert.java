package org.arquillian.reporter.impl.asserts;

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
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ListAssert;

import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.DEFAULT_END_INDEX_FOR_GENERATED_REPORT_PAYLOAD;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.DEFAULT_START_INDEX_FOR_GENERATED_REPORT_PAYLOAD;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.getKeyValueEntryWitIndex;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.getReportWithIndex;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportAssert<REPORTASSERTTYPE extends ReportAssert<REPORTASSERTTYPE, REPORTTYPE>, REPORTTYPE extends Report>
    extends AbstractAssert<REPORTASSERTTYPE, REPORTTYPE> {

    public ReportAssert(REPORTTYPE actual) {
        super(actual, ReportAssert.class);
    }

    public ReportAssert(REPORTTYPE actual, Class<? extends ReportAssert> reportAssertClass) {
        super(actual, reportAssertClass);
    }

    public static <REPORTTYPE extends Report, REPORTASSERTTYPE extends ReportAssert<REPORTASSERTTYPE, REPORTTYPE>> REPORTASSERTTYPE assertThatReport(
        REPORTTYPE actual, Class<REPORTASSERTTYPE> returnType) {

        if (returnType == ExecutionReportAssert.class) {
            return (REPORTASSERTTYPE) new ExecutionReportAssert((ExecutionReport) actual);
        } else if (returnType == TestSuiteReportAssert.class) {
            return (REPORTASSERTTYPE) new TestSuiteReportAssert((TestSuiteReport) actual);
        } else if (returnType == TestClassReportAssert.class) {
            return (REPORTASSERTTYPE) new TestClassReportAssert((TestClassReport) actual);
        } else if (returnType == TestMethodReportAssert.class) {
            return (REPORTASSERTTYPE) new TestMethodReportAssert((TestMethodReport) actual);
        } else {
            return (REPORTASSERTTYPE) new BasicReportAssert((BasicReport) actual);
        }
    }

    public static BasicReportAssert assertThatReport(Report actual) {
        return new BasicReportAssert(actual);
    }

    public ListAssert<Entry> entries() {
        return new ListAssert<>(actual.getEntries());

    }

    public REPORTASSERTTYPE hasName(AbstractStringKey name) {
        isNotNull();

        if (!Objects.equals(actual.getName(), name)) {
            failWithMessage("Expected name of the report should be <%s> but was <%s>", name, actual.getName());
        }
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasName(String name) {
        return hasName(new UnknownStringKey(name));
    }

    public REPORTASSERTTYPE hasNumberOfSubReports(int number) {
        isNotNull();
        assertThat(actual.getSubReports())
            .as("Expected number of the sub-reports of the report with name <%s> should be <%s> but was <%s>",
                actual.getName().getValue(), number, actual.getSubReports().size())
            .hasSize(number);
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasNumberOfSubReportsAndEntries(int number) {
        assertThatReport(actual)
            .as("The report with name <%s> and of the type <%s> should contain the exact number of sub-report and entries: <%s>",
                actual.getName(), actual.getClass(), number)
            .verifyNumberOfSubReportsAndEntries(number);
        return (REPORTASSERTTYPE) this;
    }

    protected REPORTASSERTTYPE verifyNumberOfSubReportsAndEntries(int number) {
        hasNumberOfSubReports(number);
        hasNumberOfEntries(number);
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasNumberOfEntries(int number) {
        isNotNull();

        if (actual.getEntries().size() != number) {
            failWithMessage("Expected number of entries of the report should be <%s> but was <%s>", number,
                            actual.getEntries().size());
        }
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasSubReportsContaining(AbstractReport... expectedReports) {
        return hasSubReportsContaining(Arrays.asList(expectedReports));
    }

    public REPORTASSERTTYPE hasSubReportsContaining(List<AbstractReport> expectedReports) {
        isNotNull();

        // because of complexity we need to run it in parallel
        expectedReports.parallelStream().forEach(report -> {
            assertThat(actual.getSubReports())
                .usingRecursiveFieldByFieldElementComparator()
                .as("The report with name <%s> and of the type <%s> should contain sub-report (with a name <%s>) "
                        + "- the used comparator strategy: usingRecursiveFieldByFieldElementComparator",
                    actual.getName(), actual.getClass(), report.getName())
                .contains(report);
        });

        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasEntriesContaining(Entry... expectedEntries) {
        return hasEntriesContaining(Arrays.asList(expectedEntries));
    }

    public REPORTASSERTTYPE hasEntriesContaining(List<Entry> expectedEntries) {
        isNotNull();

        // because of complexity we need to run it in parallel
        expectedEntries.parallelStream().forEach(entry -> {
            assertThat(actual.getEntries())
                .usingRecursiveFieldByFieldElementComparator()
                .as("The report with name <%s> and of the type <%s> should contain an entry <%s> "
                        + "- the used comparator strategy: usingRecursiveFieldByFieldElementComparator",
                    actual.getName(), actual.getClass(), entry)
                .contains(entry);
        });

        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasGeneratedSubReportsAndEntriesWithDefaults() {
        return hasGeneratedSubReportsAndEntries(DEFAULT_START_INDEX_FOR_GENERATED_REPORT_PAYLOAD,
                                                DEFAULT_END_INDEX_FOR_GENERATED_REPORT_PAYLOAD);
    }

    protected REPORTASSERTTYPE verifyGeneratedSubReportsAndEntries(int startIndex, int endIndex) {
        KeyValueEntry[] expectedEntries = IntStream
            .range(startIndex, endIndex).parallel()
            .mapToObj(index -> getKeyValueEntryWitIndex(index)).toArray(KeyValueEntry[]::new);

        AbstractReport[] expectedReports = IntStream
            .range(startIndex, endIndex).parallel()
            .mapToObj(index -> getReportWithIndex(index)).toArray(BasicReport[]::new);

        hasSubReportsContaining(expectedReports);
        hasEntriesContaining(expectedEntries);
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasGeneratedSubReportsAndEntries(int startIndex, int endIndex) {
        assertThatReport(actual)
            .as("The report with name <%s> and of the type <%s> should contain generated sub-reports and entries "
                    + "from index <%s> to index <%s> with the suffix \". report\"", actual.getName(), actual.getClass(),
                startIndex, endIndex)
            .verifyGeneratedSubReportsAndEntries(startIndex, endIndex);
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasSubReportsEndingWith(AbstractReport... reports) {

        assertThat(actual.getSubReports())
            .as("The sub-reports of the report should be ending with %s", reports)
            .endsWith(reports);

        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasSubReportsWithout(AbstractReport... reports) {

        assertThat(actual.getSubReports())
            .as("The sub-reports should not contain any report from the set %s", reports)
            .doesNotContain(reports);

        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasSubReportsThatContainsExactly(AbstractReport reports) {
        assertThat(actual.getSubReports())
            .as("The list of sub-reports of the report should contain exactly %s", reports).containsExactly(reports);
        return (REPORTASSERTTYPE) this;
    }

    protected List<Report> getReportsOfType(List<Report> reports, Class<? extends Report> reportType) {
        return reports.stream().filter(report -> report.getClass().equals(reportType)).collect(Collectors.toList());
    }

    protected boolean shouldBeVerified(Class<? extends Report> toBeVerifiedClass,
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

    protected void defaultCheckOfIfMergedOrContainsGeneratedSubReports(List<Report> merged, int index, Report reportOnIndex) {
        // if the report on index is one of the merged reports
        if (merged.contains(reportOnIndex)) {
            // then remove from the list of merged
            merged.remove(reportOnIndex);
        } else {
            // otherwise check if it contains the default number of generated sub-reports and entries - 9
            assertThatReport(reportOnIndex)
                .hasGeneratedSubReportsAndEntries(index + 1, index + 10)
                .hasNumberOfSubReportsAndEntries(9);

        }
    }
}
