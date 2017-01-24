package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

import static org.arquillian.reporter.api.model.ReporterCoreKeys.GENERAL_TEST_SUITE_CONFIGURATION_REPORT;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport extends AbstractReport<TestSuiteReport, TestSuiteReportBuilder> {

    private String start = ReporterUtils.getCurrentDate();
    private String stop;
    private ConfigurationReport configuration = new ConfigurationReport(GENERAL_TEST_SUITE_CONFIGURATION_REPORT);
    private List<TestClassReport> testClassReports = new ArrayList<>();

    public TestSuiteReport() {
    }

    public TestSuiteReport(StringKey name) {
        super(name);
    }

    public TestSuiteReport(String name) {
        super(new UnknownStringKey(name));
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
    public TestSuiteReport merge(TestSuiteReport newReport) {
        if (newReport == null){
            return this;
        }
        defaultMerge(newReport);

        getTestClassReports().addAll(newReport.getTestClassReports());

        if (newReport.getStop() != null){
            setStop(newReport.getStop());
        }

        getConfiguration().merge(newReport.getConfiguration());

        return this;
    }

    @Override
    public AbstractReport addNewReport(AbstractReport newReport) {
        Class<? extends AbstractReport> newReportClass = newReport.getClass();

        if (ConfigurationReport.class.isAssignableFrom(newReportClass)){
            return getConfiguration().addNewReport((ConfigurationReport) newReport);

        } else if (TestClassReport.class.isAssignableFrom(newReportClass)){
            getTestClassReports().add((TestClassReport) newReport);
            return newReport;

        } else {
            getSubreports().add(newReport);
            return newReport;
        }
    }

    @Override
    public Class<TestSuiteReportBuilder> getReportBuilderClass() {
        return TestSuiteReportBuilder.class;
    }
}
