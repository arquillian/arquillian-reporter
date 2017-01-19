package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

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
    private List<FailureReport> failureReports = new ArrayList<>();
    private List<ConfigurationReport> configurations = new ArrayList<>();

    public TestMethodReport(String name) {
        super(name);
    }

    public List<ConfigurationReport> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<ConfigurationReport> configurations) {
        this.configurations = configurations;
    }

    public TestResult.Status getStatus() {
        return status;
    }

    public void setStatus(TestResult.Status status) {
        this.status = status;
    }

    public List<FailureReport> getFailureReports() {
        return failureReports;
    }

    public void setFailureReports(List<FailureReport> failureReports) {
        this.failureReports = failureReports;
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

        getConfigurations().addAll(newReport.getConfigurations());
        getFailureReports().addAll(newReport.getFailureReports());

        if (newReport.getStatus() != null){
            setStatus(newReport.getStatus());
        }

        return this;
    }

    @Override public TestMethodReport addNewReport(AbstractReport newReport) {
        Class<? extends AbstractReport> newReportClass = newReport.getClass();
        if (ConfigurationReport.class.isAssignableFrom(newReportClass)){
            getConfigurations().add((ConfigurationReport) newReport);
        } else if (FailureReport.class.isAssignableFrom(newReportClass)){
            getFailureReports().add((FailureReport) newReport);
        } else {
            getSubreports().add(newReport);
        }
        return this;
    }

    @Override
    public TestMethodReportBuilderImpl getReportBuilderClass() {
        return new TestMethodReportBuilderImpl(this);
    }

}
