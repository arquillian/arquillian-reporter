package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.builder.impl.TestMethodReportBuilderImpl;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReport extends AbstractReport<TestMethodReport,TestMethodReportBuilderImpl> {

    private String start = Utils.getCurrentDate();
    private String stop;
    private TestResult.Status status;
    private FailureReport failureReport = new FailureReport("Failure report");
    private ConfigurationReport configuration = new ConfigurationReport("config");

    public TestMethodReport() {
    }

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

    @Override
    public TestMethodReport merge(TestMethodReport newReport) {
        if (newReport == null){
            return this;
        }
        defaultMerge(newReport);

        if (newReport.getStop() != null){
            setStop(newReport.getStop());
        }

        getConfiguration().merge(newReport.getConfiguration());
        getFailureReport().merge(newReport.getFailureReport());

        if (newReport.getStatus() != null){
            setStatus(newReport.getStatus());
        }
        return this;
    }

    @Override
    public AbstractReport addNewReport(AbstractReport newReport) {
        Class<? extends AbstractReport> newReportClass = newReport.getClass();

        if (ConfigurationReport.class.isAssignableFrom(newReportClass)){
            return getConfiguration().addNewReport((ConfigurationReport) newReport);

        } else if (FailureReport.class.isAssignableFrom(newReportClass)){
            return getFailureReport().addNewReport((FailureReport) newReport);

        } else {
            getSubreports().add(newReport);
            return newReport;
        }
    }

    @Override
    public TestMethodReportBuilderImpl getReportBuilderClass() {
        return new TestMethodReportBuilderImpl(this);
    }
}
