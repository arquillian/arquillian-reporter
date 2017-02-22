package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

import static org.arquillian.reporter.api.model.ReporterCoreKeys.GENERAL_TEST_SUITE_CONFIGURATION_REPORT;

/**
 * A {@link Report} implementation that represents any report information related to a test suite.
 * Apart from the basic implementation it also provides some additional information:
 * <p>
 * <ul>
 * Basic
 * <li>name</li>
 * <li>list of entries</li>
 * <li>list of sub-reports</li>
 * </ul>
 * <ul>
 * Additional provided by {@link TestSuiteReport}
 * <li>start time</li>
 * <li>stop time</li>
 * <li>configuration that contains reports and entries related to a test class configuration</li>
 * <li>list of test classes that are run in the associated test suite</li>
 * </ul>
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport extends AbstractReport<TestSuiteReport, TestSuiteReportBuilder> {

    private String start = ReporterUtils.getCurrentDate();
    private String stop;
    private ConfigurationReport configuration = new ConfigurationReport(GENERAL_TEST_SUITE_CONFIGURATION_REPORT);
    private List<TestClassReport> testClassReports = new ArrayList<>();

    /**
     * Creates an instance of {@link TestSuiteReport}
     */
    public TestSuiteReport() {
    }

    /**
     * Creates an instance of {@link TestSuiteReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public TestSuiteReport(StringKey name) {
        super(name);
    }

    /**
     * Creates an instance of {@link TestSuiteReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public TestSuiteReport(String name) {
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
     * Returns a list of {@link TestClassReport}s associated with test classes run in the corresponding test suite
     *
     * @return A list of {@link TestClassReport}s associated with test classes run in the corresponding test suite
     */
    public List<TestClassReport> getTestClassReports() {
        return testClassReports;
    }

    /**
     * Returns time when the test suite execution started
     *
     * @return Time when the test suite execution started
     */
    public String getStart() {
        return start;
    }

    /**
     * Returns time when the test suite execution stopped
     *
     * @return Time when the test suite execution stopped
     */
    public String getStop() {
        return stop;
    }

    /**
     * Sets the given time as time when an associated test suite execution stopped
     *
     * @param stop Stop time to be set
     */
    public void setStop(String stop) {
        this.stop = stop;
    }

    /**
     * Apart from the default functionality of merging ({@link AbstractReport#defaultMerge(AbstractReport)})
     * it also manages the additional types of information. The default just takes the entries and sub-reports contained
     * in the given {@link TestSuiteReport} and adds them into the list of entries and sub-reports respectively withing this
     * instance of report. In addition, {@link TestSuiteReport} takes the {@link ConfigurationReport} and merges it with
     * the current one. The list of {@link TestClassReport}s is taken as well and added into the corresponding list contained in this report.
     * The last thing is that it check if the new report contains stop time and if yes, then it sets it as stop time in this report.
     *
     * @param newReport A {@link TestSuiteReport} to be merged
     * @return This same instance of {@link TestSuiteReport}
     */
    public TestSuiteReport merge(TestSuiteReport newReport) {
        if (newReport == null) {
            return this;
        }
        defaultMerge(newReport);

        getTestClassReports().addAll(newReport.getTestClassReports());

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
     * <li>if it is a {@link TestClassReport} then it adds it into the list of test-class-reports</li>
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

        } else if (TestClassReport.class.isAssignableFrom(newReportClass)) {
            getTestClassReports().add((TestClassReport) newReport);
            return newReport;

        } else {
            getSubReports().add(newReport);
            return newReport;
        }
    }

    @Override
    public Class<TestSuiteReportBuilder> getReportBuilderClass() {
        return TestSuiteReportBuilder.class;
    }
}
