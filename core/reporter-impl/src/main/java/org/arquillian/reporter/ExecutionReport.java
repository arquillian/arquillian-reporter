package org.arquillian.reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.builder.ReportBuilder;
import org.arquillian.reporter.api.builder.impl.ReportBuilderImpl;
import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.SectionTree;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport extends AbstractReport<ExecutionReport, ReportBuilder> {

    private final List<TestSuiteReport> testSuiteReports = new ArrayList<>();
    private final Map<String, Report> reportsWithIdentifier = new HashMap<>();
    private final ExecutionSection executionSection;
    private final SectionTree sectionTree;

    private Map<Identifier, AbstractReport> sectionsAssociatedWithEvents = new HashMap<>();

    public ExecutionReport() {
        super("execution");
        this.executionSection = new ExecutionSection(this);
        sectionTree = new SectionTree(executionSection.identifyYourself(), this);
    }

    public Map<String, Report> getReportsWithIdentifier() {
        return reportsWithIdentifier;
    }

    //    public void setReportsWithIdentifier(
    //        Map<String, Report> reportsWithIdentifier) {
    //        this.reportsWithIdentifier = reportsWithIdentifier;
    //    }

    public boolean isIdentifierRegistered(String identifier){
        return reportsWithIdentifier.containsKey(identifier);
    }

    public Report getRegisteredSection(String identifier) {
        return reportsWithIdentifier.get(identifier);
    }

    public void register(Identifier identifier, Report sectionReport) {
        sectionsAssociatedWithEvents.put(identifier, sectionReport);
    }

    public void register(SectionEvent event) {
        Identifier identifier = new Identifier(event.getClass(), event.getSectionId());
        sectionsAssociatedWithEvents.put(identifier, event.getReport());
    }

    public AbstractReport getSectionReportByIdentifier(Identifier identifier) {
        return sectionsAssociatedWithEvents.get(identifier);
    }

    public AbstractReport getSectionReportByIdentifier(SectionEvent event) {
        return sectionsAssociatedWithEvents.get(new Identifier(event.getClass(), event.getSectionId()));
    }

    public List<TestSuiteReport> getTestSuiteReports() {
        return testSuiteReports;
    }

    //    public void setTestSuiteReports(List<TestSuiteReport> testSuiteReports) {
    //        this.testSuiteReports = testSuiteReports;
    //    }

    public SectionTree getSectionTree() {
        return sectionTree;
    }

    //    public void setSectionTree(SectionTree sectionTree) {
    //        this.sectionTree = sectionTree;
    //    }

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
    public ExecutionReport addNewReport(AbstractReport newReport) {
        Class<? extends AbstractReport> newReportClass = newReport.getClass();
        if (TestSuiteReport.class.isAssignableFrom(newReportClass)) {
            getTestSuiteReports().add((TestSuiteReport) newReport);
        } else {
            getSubreports().add(newReport);
        }
        return this;
    }

    public ExecutionSection getExecutionSection() {
        return executionSection;
    }
}
