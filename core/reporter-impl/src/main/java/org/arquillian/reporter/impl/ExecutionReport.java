package org.arquillian.reporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * Report containing all reports related to the whole test execution
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport extends AbstractReport<ExecutionReport, ReportBuilder> {

    private final List<TestSuiteReport> testSuiteReports = new ArrayList<>();
    public static final String EXECUTION_REPORT_NAME = "execution";

    public ExecutionReport() {
        super(new UnknownStringKey(EXECUTION_REPORT_NAME));
    }

    public List<TestSuiteReport> getTestSuiteReports() {
        return testSuiteReports;
    }


    @Override
    public Class<ReportBuilder> getReportBuilderClass() {
        return ReportBuilder.class;
    }

    @Override
    public ExecutionReport merge(ExecutionReport newReport) {
        if (newReport != null) {
            defaultMerge(newReport);
            getTestSuiteReports().addAll(newReport.getTestSuiteReports());
        }
        return this;
    }

    @Override
    public Report addNewReport(Report newReport, Class<? extends Report> expectedReportTypeClass) {
        if (expectedReportTypeClass == TestSuiteReport.class) {
            getTestSuiteReports().add((TestSuiteReport) newReport);
        } else {
            getSubReports().add(newReport);
        }
        return newReport;
    }
}
