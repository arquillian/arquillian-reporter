package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.utils.ReporterUtils;

import java.util.ArrayList;
import java.util.List;

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
public class TestClassReport extends AbstractReport<TestClassReport, TestClassReportBuilder>
    implements WithConfigurationReport, WithStartAndStopReport {

    private String startTime = ReporterUtils.getCurrentDate();
    private String stopTime;
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

    @Override
    public ConfigurationReport getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(ConfigurationReport configuration) {
        this.configuration = configuration;
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
     * <li>is a {@link TestMethodReport} class then it adds it into the list of test-method-reports</li>
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

        } else if (expectedReportTypeClass == TestMethodReport.class) {
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
