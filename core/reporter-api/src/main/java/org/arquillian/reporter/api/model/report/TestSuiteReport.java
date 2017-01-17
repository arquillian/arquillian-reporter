package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.builder.impl.TestSuiteSectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport extends AbstractSectionReport<TestSuiteReport,TestSuiteSectionBuilderImpl> {

    private String start = Utils.getCurrentDate();
    private String stop;
    private ConfigurationReport configuration = new ConfigurationReport();
    private List<TestClassReport> testClassReports = new ArrayList<>();

    public TestSuiteReport(String name) {
        super(name);
    }

    public ConfigurationReport getConfiguration() {
        return configuration;
    }

    public List<TestClassReport> getTestClassReports() {
        return testClassReports;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    @Override
    public TestSuiteReport merge(TestSuiteReport newSection) {
        if (newSection == null){
            return this;
        }
        defaultMerge(newSection);

        getTestClassReports().addAll(newSection.getTestClassReports());

        if (newSection.getStop() != null){
            setStop(newSection.getStop());
        }


        getConfiguration().merge(newSection.getConfiguration());

        return this;
    }

    @Override
    public TestSuiteSectionBuilderImpl getSectionBuilderClass() {
        return new TestSuiteSectionBuilderImpl(this);
    }
}
