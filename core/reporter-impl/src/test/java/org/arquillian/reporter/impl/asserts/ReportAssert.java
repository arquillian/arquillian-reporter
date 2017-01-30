package org.arquillian.reporter.impl.asserts;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;

import static org.arquillian.reporter.impl.utils.Utils.getKeyValueEntryWitIndex;
import static org.arquillian.reporter.impl.utils.Utils.getReportWithIndex;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportAssert extends AbstractAssert<ReportAssert, AbstractReport> {

    public ReportAssert(AbstractReport actual) {
        super(actual, ReportAssert.class);
    }

    public static ReportAssert assertThat(AbstractReport actual) {
        return new ReportAssert(actual);
    }

    //    public StringKeyAssert name(){
    //        return new StringKeyAssert(actual.getName());
    //    }

    public ListAssert<Entry> entries() {
        return new ListAssert<>(actual.getEntries());

    }

    public ListAssert<AbstractReport> subReports() {
        return new ListAssert<>(actual.getSubReports());
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

    public ReportAssert hassSubReportsContaining(AbstractReport... expectedReports) {
        isNotNull();

        Assertions.assertThat(actual.getSubReports()).as("The report should contain the expected set of sub-reports")
            .contains(expectedReports);
        return this;
    }

    public ReportAssert hassSubReportsContaining(List<AbstractReport> expectedReports) {
        isNotNull();

        Assertions.assertThat(actual.getSubReports()).as("The report should contain the expected set of sub-reports")
            .contains(expectedReports);
        return this;
    }

    public ReportAssert hasEntriesContaining(Entry... expectedEntries) {
        isNotNull();

        Assertions.assertThat(actual.getEntries()).as("The report should contain the expected set of entries")
            .contains(expectedEntries);
        return this;
    }

    public ReportAssert hasEntriesContaining(List<Entry> expectedEntries) {
        isNotNull();

        Assertions.assertThat(actual.getEntries()).as("The report should contain the expected set of entries")
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

    public ReportAssert hasSubReportsEndingWith(AbstractReport... report) {

        Assertions.assertThat(actual.getSubReports()).as("The sub-reports of the report should be ending with %s", report)
            .endsWith(report);

        return this;
    }

}
