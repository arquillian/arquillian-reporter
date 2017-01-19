package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.builder.impl.TestSuiteReportBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport extends AbstractReport<TestSuiteReport,TestSuiteReportBuilderImpl> {

    private String start = Utils.getCurrentDate();
    private String stop;
    private List<ConfigurationReport> configurations = new ArrayList<>();
    private List<TestClassReport> testClassReports = new ArrayList<>();

    public TestSuiteReport(String name) {
        super(name);
    }

    public List<ConfigurationReport> getConfigurations() {
        return configurations;
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

        getConfigurations().addAll(newReport.getConfigurations());

        return this;
    }

    @Override
    public TestSuiteReport addNewReport(AbstractReport newReport) {
        Class<? extends AbstractReport> newReportClass = newReport.getClass();
        if (ConfigurationReport.class.isAssignableFrom(newReportClass)){
            getConfigurations().add((ConfigurationReport) newReport);
        } else if (TestClassReport.class.isAssignableFrom(newReportClass)){
            getTestClassReports().add((TestClassReport) newReport);
        } else {
            getSubreports().add(newReport);
        }
        return this;
    }

    @Override
    public TestSuiteReportBuilderImpl getReportBuilderClass() {
        return new TestSuiteReportBuilderImpl(this);
    }
}
