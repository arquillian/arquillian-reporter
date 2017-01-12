package org.arquillian.reporter;

import java.util.HashMap;
import java.util.Map;

import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.TestSuiteReport;
import org.arquillian.reporter.api.utils.Validate;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReport {

    private TestSuiteReport testSuiteReport;
    private Map<String, Section> sectionsWithIdentifier = new HashMap<>();

    public TestSuiteReport getTestSuiteReport() {
        return testSuiteReport;
    }

    public void setTestSuiteReport(TestSuiteReport testSuiteReport) {
        this.testSuiteReport = testSuiteReport;
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
}
