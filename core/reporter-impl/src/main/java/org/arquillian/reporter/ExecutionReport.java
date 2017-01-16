package org.arquillian.reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.TestSuiteReport;
import org.arquillian.reporter.api.utils.Validate;
import org.arquillian.reporter.impl.Identifier;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport extends SectionReport {

    private List<TestSuiteReport> testSuiteReports = new ArrayList<>();
    private Map<String, SectionReport> sectionsWithIdentifier = new HashMap<>();

    private Map<Identifier, Section> sectionsAssociatedWithEvents = new HashMap<>();

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

    public void register(SectionReport section) {
        if (section != null && Validate.isNotEmpty(section.getIdentifier())){
            sectionsWithIdentifier.put(section.getIdentifier(), section);
        }
    }

    public void register(Identifier identifier, SectionReport sectionReport){
        sectionsAssociatedWithEvents.put(identifier, sectionReport);
    }

    public void register(ReportEvent event){
        Identifier identifier = new Identifier(event.getClass(), event.getIdentifier());
        sectionsAssociatedWithEvents.put(identifier, event.getSectionReport());
    }



    public Section getSectionReportByIdentifier(Identifier identifier){
        return sectionsAssociatedWithEvents.get(identifier);
    }

    public Section getSectionReportByIdentifier(ReportEvent event){
        return sectionsAssociatedWithEvents.get(new Identifier(event.getClass(), event.getIdentifier()));
    }

    public List<TestSuiteReport> getTestSuiteReports() {
        return testSuiteReports;
    }

    public void setTestSuiteReports(List<TestSuiteReport> testSuiteReports) {
        this.testSuiteReports = testSuiteReports;
    }
}
