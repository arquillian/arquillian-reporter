package org.arquillian.reporter.impl.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.BasicReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Utils {

    public static final int DEFAULT_START_INDEX_FOR_GENERATED_REPORT_PAYLOAD = 1;
    public static final int DEFAULT_END_INDEX_FOR_GENERATED_REPORT_PAYLOAD = 10;

    public static void feedReportWithData(AbstractReport report, int startIndex, int endIndex) {
        report.getEntries().addAll(getSetOfEntries(startIndex, endIndex));
        report.getSubReports().addAll(getSetOfReports(startIndex, endIndex));
    }

    public static List<BasicReport> getSetOfReports(int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex).mapToObj(index -> getReportWithIndex(index))
            .collect(Collectors.toList());
    }

    public static List<Entry> getSetOfEntries(int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex).mapToObj(index -> getKeyValueEntryWitIndex(index))
            .collect(Collectors.toList());
    }

    public static KeyValueEntry getKeyValueEntryWitIndex(int index) {
        return new KeyValueEntry(index + ". entry", index + ". cool entry");
        // todo add more entries
    }

    public static BasicReport getReportWithIndex(int index) {
        BasicReport dummyBasicReport = new BasicReport(index + ". report");
        dummyBasicReport.getEntries().add(getKeyValueEntryWitIndex(index));
        // todo add more entries
        return dummyBasicReport;
    }

    public static <T extends AbstractReport> List<T> prepareSetOfReports(Class<T> reportClass, int count,
        String prefixName, int startIndex,
        int endIndex) throws IllegalAccessException, InstantiationException {
        return IntStream.range(0, count).mapToObj(index -> {
            try {
                return prepareReport(reportClass, prefixName + index, startIndex, endIndex);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public static <T extends AbstractReport> T prepareReport(Class<T> reportClass, String name, int startIndex,
        int endIndex) throws IllegalAccessException, InstantiationException {
        return prepareReport(reportClass, new UnknownStringKey(name), startIndex, endIndex);
    }

    public static <T extends AbstractReport> T prepareReportWithDefaults(Class<T> reportClass, String name)
        throws IllegalAccessException, InstantiationException {
        return prepareReport(reportClass,
                             new UnknownStringKey(name),
                             DEFAULT_START_INDEX_FOR_GENERATED_REPORT_PAYLOAD,
                             DEFAULT_END_INDEX_FOR_GENERATED_REPORT_PAYLOAD);
    }

    public static <T extends AbstractReport> T prepareReport(Class<T> reportClass, StringKey name, int startIndex,
        int endIndex) throws IllegalAccessException, InstantiationException {
        T report = reportClass.newInstance();
        report.setName(name);
        feedReportWithData(report, startIndex, endIndex);
        return report;
    }

}
