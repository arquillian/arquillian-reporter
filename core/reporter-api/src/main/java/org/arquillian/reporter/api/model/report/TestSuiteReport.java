package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arquillian.reporter.api.builder.impl.SectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport extends AbstractSectionReport<TestSuiteReport,SectionBuilderImpl> {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
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

    public TestSuiteReport stop() {
        this.stop = new Date(System.currentTimeMillis());
        return this;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
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
    public SectionBuilderImpl getSectionBuilderClass() {
        return new SectionBuilderImpl(this);
    }
}
