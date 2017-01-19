package org.arquillian.reporter;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.ReportBuilder;
import org.arquillian.reporter.api.builder.impl.ReportBuilderImpl;
import org.arquillian.reporter.api.builder.impl.UnknownKey;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.SectionTree;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport extends AbstractReport<ExecutionReport, ReportBuilder> {

    private final List<TestSuiteReport> testSuiteReports = new ArrayList<>();
    private final ExecutionSection executionSection;
    private final SectionTree sectionTree;


    public ExecutionReport() {
        super(new UnknownKey("execution"));
        this.executionSection = new ExecutionSection(this);
        sectionTree = new SectionTree(executionSection.identifyYourself(), this);
    }

    public List<TestSuiteReport> getTestSuiteReports() {
        return testSuiteReports;
    }

    public SectionTree getSectionTree() {
        return sectionTree;
    }

    @Override
    public ReportBuilderImpl getReportBuilderClass() {
        return new ReportBuilderImpl(this);
    }

    @Override
    public ExecutionReport merge(ExecutionReport newReport) {
        if (newReport != null) {
            getTestSuiteReports().addAll(newReport.getTestSuiteReports());
        }
        return this;
    }

    @Override
    public AbstractReport addNewReport(AbstractReport newReport) {
        Class<? extends AbstractReport> newReportClass = newReport.getClass();
        if (TestSuiteReport.class.isAssignableFrom(newReportClass)) {
            getTestSuiteReports().add((TestSuiteReport) newReport);
        } else {
            getSubreports().add(newReport);
        }
        return newReport;
    }

    public ExecutionSection getExecutionSection() {
        return executionSection;
    }
}
