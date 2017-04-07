package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.jboss.arquillian.test.spi.TestResult;

import static org.arquillian.reporter.api.model.ReporterCoreKey.GENERAL_METHOD_FAILURE_REPORT;
import static org.arquillian.reporter.api.model.ReporterCoreKey.GENERAL_TEST_METHOD_CONFIGURATION_REPORT;

/**
 * A {@link Report} implementation that represents any report information related to a test method.
 * Apart from the basic implementation it also provides some additional information:
 * <p>
 * <ul>
 * Basic
 * <li>name</li>
 * <li>list of entries</li>
 * <li>list of sub-reports</li>
 * </ul>
 * <ul>
 * Additional provided by {@link TestMethodReport}
 * <li>start time</li>
 * <li>stop time</li>
 * <li>test execution status</li>
 * <li>configuration that contains reports and entries related to a test method configuration</li>
 * <li>list of failures that occurred during a test execution</li>
 * </ul>
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReport extends AbstractReport<TestMethodReport, TestMethodReportBuilder>
    implements WithConfigurationReport, WithStartAndStopReport {

    private String startTime = ReporterUtils.getCurrentDate();
    private String stopTime;
    private TestResult.Status status;
    private FailureReport failureReport = new FailureReport(GENERAL_METHOD_FAILURE_REPORT);
    private ConfigurationReport configuration = new ConfigurationReport(GENERAL_TEST_METHOD_CONFIGURATION_REPORT);

    /**
     * Creates an instance of {@link TestMethodReport}
     */
    public TestMethodReport() {
    }

    /**
     * Creates an instance of {@link TestMethodReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public TestMethodReport(StringKey name) {
        super(name);
    }

    /**
     * Creates an instance of {@link TestMethodReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public TestMethodReport(String name) {
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
     * Returns the {@link TestResult.Status} of a test method execution
     *
     * @return The {@link TestResult.Status} of a test method execution
     */
    public TestResult.Status getStatus() {
        return status;
    }

    /**
     * Sets the given {@link TestResult.Status} as a result of test method execution
     *
     * @param status A {@link TestResult.Status} to be set
     */
    public void setStatus(TestResult.Status status) {
        this.status = status;
    }

    /**
     * Returns the {@link FailureReport}
     *
     * @return The {@link FailureReport}
     */
    public FailureReport getFailureReport() {
        return failureReport;
    }

    /**
     * Sets the given {@link FailureReport}
     *
     * @param failureReport A {@link FailureReport} to be set
     */
    public void setFailureReport(FailureReport failureReport) {
        this.failureReport = failureReport;
    }

    /**
     * Apart from the default functionality of merging ({@link AbstractReport#defaultMerge(AbstractReport)})
     * it also manages the additional types of information. The default just takes the entries and sub-reports contained
     * in the given {@link TestMethodReport} and adds them into the list of entries and sub-reports respectively withing this
     * instance of report. In addition, {@link TestMethodReport} takes the {@link ConfigurationReport} and {@link FailureReport}
     * and merges them with those ones contained in this report.s
     * The last thing is that it check if the new report contains stop time and {@link TestResult.Status}; if yes,
     * then it sets them in this report.
     *
     * @param newReport A {@link TestMethodReport} to be merged
     * @return This same instance of {@link TestMethodReport}
     */
    public TestMethodReport merge(TestMethodReport newReport) {
        if (newReport == null) {
            return this;
        }
        defaultMerge(newReport);

        if (newReport.getExecutionStopTime() != null) {
            setExecutionStopTime(newReport.getExecutionStopTime());
        }

        getConfiguration().merge(newReport.getConfiguration());
        getFailureReport().merge(newReport.getFailureReport());

        if (newReport.getStatus() != null) {
            setStatus(newReport.getStatus());
        }
        return this;
    }

    /**
     * Takes the given {@link Report} and if the given expectedReportTypeClass:
     * <ul>
     * <li>is a {@link ConfigurationReport} class then it adds it into the list of
     * sub-reports contained in this report's configuration-report</li>
     * <li>is a {@link FailureReport} class then it adds it into the list of
     * sub-reports contained in this report's failure-report</li>
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

        } else if (expectedReportTypeClass == FailureReport.class) {
            return getFailureReport().addNewReport(newReport, expectedReportTypeClass);

        } else {
            getSubReports().add(newReport);
            return newReport;
        }
    }

    @Override
    public Class<TestMethodReportBuilder> getReportBuilderClass() {
        return TestMethodReportBuilder.class;
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
