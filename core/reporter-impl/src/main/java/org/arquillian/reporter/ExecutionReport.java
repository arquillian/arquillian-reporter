package org.arquillian.reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;
import org.arquillian.reporter.api.model.report.SectionReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.api.builder.Validate;
import org.arquillian.reporter.impl.Identifier;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport extends SectionReport {

    private List<TestSuiteReport> testSuiteReports = new ArrayList<>();
    private Map<String, SectionReport> sectionsWithIdentifier = new HashMap<>();

    private Map<Identifier, AbstractSectionReport> sectionsAssociatedWithEvents = new HashMap<>();

    public ExecutionReport() {
        super("execution");
    }

    public Map<String, SectionReport> getSectionsWithIdentifier() {
        return sectionsWithIdentifier;
    }

    public void setSectionsWithIdentifier(
        Map<String, SectionReport> sectionsWithIdentifier) {
        this.sectionsWithIdentifier = sectionsWithIdentifier;
    }

    public boolean isIdentifierRegistered(String identifier){
        return sectionsWithIdentifier.containsKey(identifier);
    }

    public SectionReport getRegisteredSection(String identifier){
        return sectionsWithIdentifier.get(identifier);
    }

    public void register(SectionReport sectionReport) {
        if (sectionReport != null && Validate.isNotEmpty(sectionReport.getIdentifier())){
            sectionsWithIdentifier.put(sectionReport.getIdentifier(), sectionReport);
        }
    }

    public void register(Identifier identifier, SectionReport sectionReport){
        sectionsAssociatedWithEvents.put(identifier, sectionReport);
    }

    public void register(ReportNodeEvent event){
        Identifier identifier = new Identifier(event.getClass(), event.getIdentifier());
        sectionsAssociatedWithEvents.put(identifier, event.getSection());
    }



    public AbstractSectionReport getSectionReportByIdentifier(Identifier identifier){
        return sectionsAssociatedWithEvents.get(identifier);
    }

    public AbstractSectionReport getSectionReportByIdentifier(ReportNodeEvent event){
        return sectionsAssociatedWithEvents.get(new Identifier(event.getClass(), event.getIdentifier()));
    }

    public List<TestSuiteReport> getTestSuiteReports() {
        return testSuiteReports;
    }

    public void setTestSuiteReports(List<TestSuiteReport> testSuiteReports) {
        this.testSuiteReports = testSuiteReports;
    }
}
