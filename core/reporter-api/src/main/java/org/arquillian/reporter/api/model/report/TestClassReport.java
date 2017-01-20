package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.builder.impl.TestClassReportBuilderImpl;
import org.arquillian.reporter.api.builder.impl.UnknownStringKey;
import org.arquillian.reporter.api.model.StringKey;

import static org.arquillian.reporter.api.model.ReporterCoreKeys.GENERAL_TEST_CLASS_CONFIGURATION_REPORT;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReport extends AbstractReport<TestClassReport,TestClassReportBuilderImpl> {

    private String start = Utils.getCurrentDate();
    private String stop;
    private ConfigurationReport configuration = new ConfigurationReport(GENERAL_TEST_CLASS_CONFIGURATION_REPORT);
    private List<TestMethodReport> testMethodReports = new ArrayList<>();

    public TestClassReport() {
    }

    public TestClassReport(StringKey name) {
        super(name);
    }

    public TestClassReport(String name) {
        super(new UnknownStringKey(name));
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
    public TestClassReport merge(TestClassReport newReport) {
        if (newReport == null) {
            return this;
        }
        defaultMerge(newReport);

        getTestMethodReports().addAll(newReport.getTestMethodReports());

        if (newReport.getStop() != null) {
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

        } else if (TestMethodReport.class.isAssignableFrom(newReportClass)){
            getTestMethodReports().add((TestMethodReport) newReport);
            return newReport;

        } else {
            getSubreports().add(newReport);
            return newReport;
        }
    }

    @Override
    public TestClassReportBuilderImpl getReportBuilderClass() {
        return new TestClassReportBuilderImpl(this);
    }

}
