package org.arquillian.reporter.api.model.report;

import java.util.List;

import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * An interface representing a report. This is the main building blog of the resulting report.
 * This Report class may contain several {@link Entry}(-ies) and additional sub-reports
 *
 * @param <TYPE>        A type of the {@link Report} implementation itself
 * @param <BUILDERTYPE> A {@link ReportBuilder} type of a builder that should be used for building the {@link Report} implementation
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Report<TYPE extends Report, BUILDERTYPE extends ReportBuilder> {

    /**
     * Returns the name of this report
     *
     * @return The name of this report
     */
    StringKey getName();

    /**
     * Sets the given name as a name of this report
     *
     * @param name A name to be set as a name of this report
     */
    void setName(StringKey name);

    /**
     * Returns a list of {@link Entry}(-ies) contained in this report
     *
     * @return A list of {@link Entry}(-ies) contained in this report
     */
    List<Entry> getEntries();

    /**
     * Sets the given list of {@link Entry}(-ies)
     *
     * @param entries A list of {@link Entry}(-ies) to be set
     */
    void setEntries(List<Entry> entries);

    /**
     * Returns a list of {@link Report}s that are stored as sub-reports of this report
     *
     * @return A list of {@link Report}s that are stored as sub-reports of this report
     */
    List<Report> getSubReports();

    /**
     * Sets the given list of {@link Report}s as sub-reports of this report
     *
     * @param subReports A list of {@link Report}s to be set as sub-reports of this report
     */
    void setSubReports(List<Report> subReports);

    /**
     * Returns a {@link ReportBuilder} class that should be used as a builder for the report implementations
     *
     * @return A {@link ReportBuilder} class that should be used as a builder for the report implementations
     */
    Class<BUILDERTYPE> getReportBuilderClass();

    /**
     * Takes the information contained in the given {@link Report} and add them into this report instance. In other words.
     * merges information that were already present in this report with those that are brought in the given report.
     * When the reports are merged no information should be lost. The type of the given report should be same as this one.
     * If the type is different then the method {@link this#addNewReport(Report)} should be used
     *
     * @param newReport A {@link Report} to be merged
     * @return This instance of {@link Report} with the new merged information
     */
    TYPE merge(TYPE newReport);

    /**
     * Takes the given {@link Report} and based on its type and the type of the given expectedReportTypeClass, it adds
     * the whole report into some list of sub-reports contained in this report instance.
     *
     * @param newReport A {@link Report} to be added
     * @param expectedReportTypeClass A {@link Report} class of a type that is expected as the default one of the given report
     * @return The same instance of {@link Report} that has been added into some list of sub-reports.
     */
    Report addNewReport(Report newReport, Class<? extends Report> expectedReportTypeClass);


    void processEntries();

}
