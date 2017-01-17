package org.arquillian.reporter.api.model.report;

import java.util.Date;

import org.arquillian.reporter.api.builder.impl.TestMethodSectionBuilderImpl;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReport extends AbstractSectionReport<TestMethodReport,TestMethodSectionBuilderImpl> {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private TestResult.Status status;
    private FailureReport failureReport = new FailureReport("Failures");
    private ConfigurationReport configuration = new ConfigurationReport();

    public TestMethodReport(String name) {
        super(name);
    }

    public ConfigurationReport getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ConfigurationReport configuration) {
        this.configuration = configuration;
    }

    public TestResult.Status getStatus() {
        return status;
    }

    public void setStatus(TestResult.Status status) {
        this.status = status;
    }

    public FailureReport getFailureReport() {
        return failureReport;
    }

    public void setFailureReport(FailureReport failureReport) {
        this.failureReport = failureReport;
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

    @Override
    public TestMethodReport merge(TestMethodReport newSection) {
        if (newSection == null){
            return this;
        }
        defaultMerge(newSection);

        if (newSection.getStop() != null){
            setStop(newSection.getStop());
        }

        getConfiguration().merge(newSection.getConfiguration());
        getFailureReport().merge(newSection.getFailureReport());

        if (newSection.getStatus() != null){
            setStatus(newSection.getStatus());
        }

        return this;
    }

    @Override
    public TestMethodSectionBuilderImpl getSectionBuilderClass() {
        return new TestMethodSectionBuilderImpl(this);
    }

}
