package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arquillian.reporter.api.utils.TestClassSectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReport extends AbstractSectionReport<TestClassReport, TestClassSectionBuilderImpl> {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestMethodReport> testMethodReports = new ArrayList<>();

    public TestClassReport(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
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
    public Class<? extends TestClassSectionBuilderImpl> getSectionBuilderClass() {
        return TestClassSectionBuilderImpl.class;
    }

}
