package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.jboss.arquillian.test.spi.TestResult;

import static org.arquillian.reporter.api.model.ReporterCoreKeys.GENERAL_METHOD_FAILURE_REPORT;
import static org.arquillian.reporter.api.model.ReporterCoreKeys.GENERAL_TEST_METHOD_CONFIGURATION_REPORT;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReport extends AbstractReport<TestMethodReport,TestMethodReportBuilder> {

    private String start = ReporterUtils.getCurrentDate();
    private String stop;
    private TestResult.Status status;
    private FailureReport failureReport = new FailureReport(GENERAL_METHOD_FAILURE_REPORT);
    private ConfigurationReport configuration = new ConfigurationReport(GENERAL_TEST_METHOD_CONFIGURATION_REPORT);

    public TestMethodReport() {
    }

    public TestMethodReport(StringKey name) {
        super(name);
    }

    public TestMethodReport(String name) {
        super(new UnknownStringKey(name));
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
    public Class<TestMethodReportBuilder> getReportBuilderClass() {
        return TestMethodReportBuilder.class;
    }
}
