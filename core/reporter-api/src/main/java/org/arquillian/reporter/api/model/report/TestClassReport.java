package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

import static org.arquillian.reporter.api.model.ReporterCoreKey.GENERAL_TEST_CLASS_CONFIGURATION_REPORT;

/**
 * A {@link Report} implementation that represents any report information related to a test class.
 * Apart from the basic implementation it also provides some additional information:
 * <p>
 * <ul>
 * Basic
 * <li>name</li>
 * <li>list of entries</li>
 * <li>list of sub-reports</li>
 * </ul>
 * <ul>
 * Additional provided by {@link TestClassReport}
 * <li>start time</li>
 * <li>stop time</li>
 * <li>configuration that contains reports and entries related to a test class configuration</li>
 * <li>list of test methods declared in the test class</li>
 * </ul>
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReport extends AbstractReport<TestClassReport, TestClassReportBuilder> {

    private String start = ReporterUtils.getCurrentDate();
    private String stop;
    private ConfigurationReport configuration = new ConfigurationReport(GENERAL_TEST_CLASS_CONFIGURATION_REPORT);
    private List<TestMethodReport> testMethodReports = new ArrayList<>();

    /**
     * Creates an instance of {@link TestClassReport}
     */
    public TestClassReport() {
    }

    /**
     * Creates an instance of {@link TestClassReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public TestClassReport(StringKey name) {
        super(name);
    }

    /**
     * Creates an instance of {@link TestClassReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public TestClassReport(String name) {
        super(new UnknownStringKey(name));
    }

    /**
     * Returns the {@link ConfigurationReport}
     *
     * @return The {@link ConfigurationReport}
     */
    public ConfigurationReport getConfiguration() {
        return configuration;
    }

    /**
     * Sets the given {@link ConfigurationReport}
     *
     * @param configuration A {@link ConfigurationReport} to be set
     */
    public void setConfiguration(ConfigurationReport configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns time when the test class execution stopped
     *
     * @return Time when the test class execution stopped
     */
    public String getStop() {
        return stop;
    }

    /**
     * Sets the given time as time when an associated test class execution stopped
     *
     * @param stop Stop time to be set
     */
    public void setStop(String stop) {
        this.stop = stop;
    }

    /**
     * Returns time when the test class execution started
     *
     * @return Time when the test class execution started
     */
    public String getStart() {
        return start;
    }

    /**
     * Returns a list of {@link TestMethodReport}s that have been run and are declared in associated test class.
     *
     * @return A list of {@link TestMethodReport}s that have been run and are declared in associated test class.
     */
    public List<TestMethodReport> getTestMethodReports() {
        return testMethodReports;
    }

    /**
     * Apart from the default functionality of merging ({@link AbstractReport#defaultMerge(AbstractReport)})
     * it also manages the additional types of information. The default just takes the entries and sub-reports contained
     * in the given {@link TestClassReport} and adds them into the list of entries and sub-reports respectively withing this
     * instance of report. In addition, {@link TestClassReport} takes the {@link ConfigurationReport} and merges it with
     * the current one. The list of {@link TestMethodReport} is taken as well and added into the list contained in this report.
     * The last thing is that it check if the new report contains stop time and if yes, then it sets it as stop time in this report.
     *
     * @param newReport A {@link TestClassReport} to be merged
     * @return This same instance of {@link TestClassReport}
     */
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

    /**
     * Takes the given {@link Report} and:
     * <ul>
     * <li>if it is a {@link ConfigurationReport} then it adds it into the list of
     * sub-reports contained in this report's configuration-report.</li>
     * <li>if it is a {@link TestMethodReport} then it adds it into the list of test-method-reports</li>
     * <li>if it is any other type of {@link Report} then it adds it into the list of sub-reports</li>
     * </ul>
     *
     * @param newReport A {@link Report} to be added
     * @return The same instance of {@link Report} that has been added
     */
    public Report addNewReport(Report newReport) {
        Class<? extends Report> newReportClass = newReport.getClass();

        if (ConfigurationReport.class.isAssignableFrom(newReportClass)) {
            return getConfiguration().addNewReport((ConfigurationReport) newReport);

        } else if (TestMethodReport.class.isAssignableFrom(newReportClass)) {
            getTestMethodReports().add((TestMethodReport) newReport);
            return newReport;

        } else {
            getSubReports().add(newReport);
            return newReport;
        }
    }

    @Override
    public Class<TestClassReportBuilder> getReportBuilderClass() {
        return TestClassReportBuilder.class;
    }

}
