package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.utils.ReporterUtils;

import java.util.ArrayList;
import java.util.List;

import static org.arquillian.reporter.api.model.ReporterCoreKey.GENERAL_TEST_SUITE_CONFIGURATION_REPORT;

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
public class TestSuiteReport extends AbstractReport<TestSuiteReport, TestSuiteReportBuilder>
    implements WithConfigurationReport, WithStartAndStopReport {

    private String startTime = ReporterUtils.getCurrentDate();
    private String stopTime;
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

    @Override
    public ConfigurationReport getConfiguration() {
        return configuration;
    }

    @Override
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

        if (newReport.getExecutionStopTime() != null) {
            setExecutionStopTime(newReport.getExecutionStopTime());
        }

        getConfiguration().merge(newReport.getConfiguration());

        return this;
    }

    /**
     * Takes the given {@link Report} and if the given expectedReportTypeClass:
     * <ul>
     * <li>is a {@link ConfigurationReport} class then it adds it into the list of
     * sub-reports contained in this report's configuration-report.</li>
     * <li>is a {@link TestClassReport} class then it adds it into the list of test-class-reports</li>
     * <li>is any other type of {@link Report} class then it adds it into the list of sub-reports</li>
     * </ul>
     *
     * @param newReport A {@link Report} to be added
     * @param expectedReportTypeClass A {@link Report} class of a type that is expected as the default one of the given report
     * @return If the report can be tied with section node then, the same instance of {@link Report} that has been added, null otherwise
     */
    public Report addNewReport(Report newReport, Class<? extends Report> expectedReportTypeClass) {
        if (expectedReportTypeClass == ConfigurationReport.class) {
            return getConfiguration().addNewReport(newReport, expectedReportTypeClass);

        } else if (expectedReportTypeClass == TestClassReport.class) {
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

    @Override public void setExecutionStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override public String getExecutionStartTime() {
        return startTime;
    }

    @Override public void setExecutionStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    @Override public String getExecutionStopTime() {
        return stopTime;
    }
}
