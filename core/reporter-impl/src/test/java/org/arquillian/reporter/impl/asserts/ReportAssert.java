package org.arquillian.reporter.impl.asserts;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ListAssert;

import static org.arquillian.reporter.impl.ExecutionReport.EXECUTION_REPORT_NAME;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getParentSectionsOfSomeType;
import static org.arquillian.reporter.impl.utils.Utils.getKeyValueEntryWitIndex;
import static org.arquillian.reporter.impl.utils.Utils.getReportWithIndex;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportAssert extends AbstractAssert<ReportAssert, AbstractReport> {

    public ReportAssert(AbstractReport actual) {
        super(actual, ReportAssert.class);
    }

    public static ReportAssert assertThatReport(AbstractReport actual) {
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
            .mapToObj(index -> getReportWithIndex(index)).toArray(Report[]::new);

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

    public ReportAssert wholeExecutionReportTreeConsistOf(
        Map<SectionEvent, List<? extends SectionEvent>> mapOfParentsAndListsOfChildren) {

        if (!(actual instanceof ExecutionReport)) {
            throw new IllegalArgumentException(
                "the assert method wholeExecutionReportTreeConsistOf is applicable only for execution reports");
        }

        List<SectionEvent> parentSectionsOfSomeType =
            getParentSectionsOfSomeType(ExecutionSection.class, mapOfParentsAndListsOfChildren);

        assertThat(parentSectionsOfSomeType).as("In the tree can be only one Execution section").hasSize(1);

        SectionEvent executionSection = parentSectionsOfSomeType.get(0);

        List<? extends SectionEvent> subSectionEvents = mapOfParentsAndListsOfChildren.get(executionSection);

        assertThatReport(actual)
            .isEqualTo(executionSection.getReport())
            .hasName(EXECUTION_REPORT_NAME);


        assertThat(((ExecutionReport) actual).getTestSuiteReports()).hasSize(subSectionEvents.size());

        return this;
    }

}
