package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.builder.impl.TestClassSectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReport extends AbstractSectionReport<TestClassReport,TestClassSectionBuilderImpl> {

    private String start = Utils.getCurrentDate();
    private String stop;
    private ConfigurationReport configuration = new ConfigurationReport();
    private List<TestMethodReport> testMethodReports = new ArrayList<>();

    public TestClassReport(String name) {
        super(name);
    }

    public ConfigurationReport getConfiguration() {
        return configuration;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<TestMethodReport> getTestMethodReports() {
        return testMethodReports;
    }

    @Override
    public TestClassReport merge(TestClassReport newSection) {
        if (newSection == null) {
            return this;
        }
        defaultMerge(newSection);

        getTestMethodReports().addAll(newSection.getTestMethodReports());

        if (newSection.getStop() != null) {
            setStop(newSection.getStop());
        }

        getConfiguration().merge(newSection.getConfiguration());

        return this;
    }

    @Override
    public TestClassSectionBuilderImpl getSectionBuilderClass() {
        return new TestClassSectionBuilderImpl(this);
    }

}
