package org.arquillian.reporter;

import java.util.HashMap;
import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.TestSuiteReport;
import org.arquillian.reporter.api.utils.Validate;
import org.arquillian.reporter.impl.Identifier;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport {

    private TestSuiteReport testSuiteReport;
    private Map<String, SectionReport> sectionsWithIdentifier = new HashMap<>();

    private Map<Identifier, SectionReport> sectionsAssociatedWithEvents = new HashMap<>();

    public TestSuiteReport getTestSuiteReport() {
        return testSuiteReport;
    }

    public void setTestSuiteReport(TestSuiteReport testSuiteReport) {
        this.testSuiteReport = testSuiteReport;
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



    public SectionReport getSectionReportByIdentifier(Identifier identifier){
        return sectionsAssociatedWithEvents.get(identifier);
    }

    public SectionReport getSectionReportByIdentifier(ReportEvent event){
        return sectionsAssociatedWithEvents.get(new Identifier(event.getClass(), event.getIdentifier()));
    }


}
