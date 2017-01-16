package org.arquillian.reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.AbstractSection;
import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.TestSuiteSection;
import org.arquillian.reporter.api.utils.Validate;
import org.arquillian.reporter.impl.Identifier;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport extends Section {

    private List<TestSuiteSection> testSuiteSections = new ArrayList<>();
    private Map<String, Section> sectionsWithIdentifier = new HashMap<>();

    private Map<Identifier, AbstractSection> sectionsAssociatedWithEvents = new HashMap<>();

    public ExecutionReport() {
        super("execution");
    }

    public Map<String, Section> getSectionsWithIdentifier() {
        return sectionsWithIdentifier;
    }

    public void setSectionsWithIdentifier(
        Map<String, Section> sectionsWithIdentifier) {
        this.sectionsWithIdentifier = sectionsWithIdentifier;
    }

    public boolean isIdentifierRegistered(String identifier){
        return sectionsWithIdentifier.containsKey(identifier);
    }

    public Section getRegisteredSection(String identifier){
        return sectionsWithIdentifier.get(identifier);
    }

    public void register(Section section) {
        if (section != null && Validate.isNotEmpty(section.getIdentifier())){
            sectionsWithIdentifier.put(section.getIdentifier(), section);
        }
    }

    public void register(Identifier identifier, Section section){
        sectionsAssociatedWithEvents.put(identifier, section);
    }

    public void register(ReportEvent event){
        Identifier identifier = new Identifier(event.getClass(), event.getIdentifier());
        sectionsAssociatedWithEvents.put(identifier, event.getSectionReport());
    }



    public AbstractSection getSectionReportByIdentifier(Identifier identifier){
        return sectionsAssociatedWithEvents.get(identifier);
    }

    public AbstractSection getSectionReportByIdentifier(ReportEvent event){
        return sectionsAssociatedWithEvents.get(new Identifier(event.getClass(), event.getIdentifier()));
    }

    public List<TestSuiteSection> getTestSuiteSections() {
        return testSuiteSections;
    }

    public void setTestSuiteSections(List<TestSuiteSection> testSuiteSections) {
        this.testSuiteSections = testSuiteSections;
    }
}
