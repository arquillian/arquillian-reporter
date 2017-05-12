package org.arquillian.reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import org.arquillian.core.reporter.impl.ArquillianCoreKey;
import org.arquillian.environment.reporter.impl.DockerDetector;
import org.arquillian.environment.reporter.impl.EnvironmentKey;
import org.arquillian.environment.reporter.impl.TestRunnerDetector;
import org.arquillian.reporter.api.model.ReporterCoreKey;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.parser.ReportJsonParser;
import org.jboss.arquillian.test.spi.TestResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import static org.arquillian.reporter.api.model.ReporterCoreKey.GENERAL_METHOD_FAILURE_REPORT;
import static org.arquillian.reporter.api.model.ReporterCoreKey.METHOD_FAILURE_REPORT_STACKTRACE;
import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.TestClassReportAssert.assertThatTestClassReport;
import static org.arquillian.reporter.impl.asserts.TestMethodReportAssert.assertThatTestMethodReport;
import static org.arquillian.reporter.impl.asserts.TestSuiteReportAssert.assertThatTestSuiteReport;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GreeterReportVerifier {

    private static File reportInJson;
    private static ExecutionReport executionReport;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        JUnitCore.runClasses(GreeterTest.class);
        reportInJson = new File("target/report.json");
        executionReport = ReportJsonParser.parse("target/report.json");
    }

    @Test
    public void verify_report_in_json_exists() throws FileNotFoundException {
        assertThat(reportInJson).exists();
    }

    @Test
    public void verify_execution_report_exists_with_contents() {
        assertThatExecutionReport(executionReport)
            .hasName("execution")
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(0);
    }

    @Test
    public void verify_test_suite_report_exists_with_contents() {
        List<TestSuiteReport> testSuiteReports = executionReport.getTestSuiteReports();
        TestSuiteReport testSuiteReport = testSuiteReports.get(0);

        assertThat(testSuiteReports).size().isEqualTo(1);

        assertThatTestSuiteReport(testSuiteReport)
            .hasName(ArquillianCoreKey.TEST_SUITE_NAME)
            .hasNumberOfSubReportsAndEntries(0);

        assertThat(testSuiteReport.getConfiguration().getName())
            .isEqualTo(ReporterCoreKey.GENERAL_TEST_SUITE_CONFIGURATION_REPORT);

        assertThat(testSuiteReport.getTestClassReports()).size().isEqualTo(1);
    }

    @Test
    public void verify_test_suite_configuration_report_exists_with_contents() {
        ConfigurationReport configuration = executionReport.getTestSuiteReports().get(0).getConfiguration();
        List<Report> subReports = configuration.getSubReports();

        assertThatReport(configuration)
            .hasName(ReporterCoreKey.GENERAL_TEST_SUITE_CONFIGURATION_REPORT)
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(2);

        for (Report subReport : subReports) {
            if (isContainerConfigurationSubReport(subReport)) {
                verify_container_configuration_sub_report(subReport);
            }
            if (isEnvironmentConfigurationSubReport(subReport)) {
                verify_environment_configuration_sub_report(subReport);
            }
        }
    }

    private void verify_environment_configuration_sub_report(Report subReport) {
        assertThat(subReport.getName())
            .isEqualTo(EnvironmentKey.ENVIRONMENT_SECTION_NAME);
        assertThat(subReport.getEntries()).size().isEqualTo(8);
        assertThatReport(subReport)
            .hasEntriesContaining(new KeyValueEntry(EnvironmentKey.JAVA_VERSION, System.getProperty("java.version")),
                new KeyValueEntry(EnvironmentKey.TEST_RUNNER, TestRunnerDetector.detect().name()),
                new KeyValueEntry(EnvironmentKey.TIMEZONE, TimeZone.getDefault().getDisplayName()),
                new KeyValueEntry(EnvironmentKey.CHARSET, Charset.defaultCharset().displayName()),
                new KeyValueEntry(EnvironmentKey.DOCKER, Boolean.toString(DockerDetector.detect())),
                new KeyValueEntry(EnvironmentKey.OPERATIVE_SYSTEM, System.getProperty("os.name")),
                new KeyValueEntry(EnvironmentKey.OPERATIVE_SYSTEM_ARCH, System.getProperty("os.arch")),
                new KeyValueEntry(EnvironmentKey.OPERATIVE_SYSTEM_VERSION, System.getProperty("os.version")));
        assertThat(subReport.getSubReports()).size().isEqualTo(0);
    }

    private void verify_container_configuration_sub_report(Report subReport) {
        Report nestedContainerReport = (Report) subReport.getSubReports().get(0);
        Report nestedContainerConfigurationReport = (Report) nestedContainerReport.getSubReports().get(0);

        assertThat(subReport.getName())
            .isEqualTo(new UnknownStringKey("containers"));
        assertThat(subReport.getEntries()).size().isEqualTo(0);
        assertThat(subReport.getSubReports()).size().isEqualTo(1);
        assertThatReport(nestedContainerReport)
            .hasName(ArquillianCoreKey.CONTAINER_REPORT)
            .hasNumberOfEntries(1)
            .hasEntriesContaining(new KeyValueEntry(ArquillianCoreKey.CONTAINER_NAME, "chameleon"))
            .hasNumberOfSubReports(1);
        assertThatReport(nestedContainerConfigurationReport)
            .hasName(ArquillianCoreKey.CONTAINER_CONFIGURATION_REPORT)
            .hasNumberOfEntries(2)
            .hasNumberOfSubReports(0);
    }

    private boolean isEnvironmentConfigurationSubReport(Report subReport) {
        StringKey subReportName = EnvironmentKey.ENVIRONMENT_SECTION_NAME;
        if (subReport.getName().equals(subReportName)) {
            return true;
        }
        return false;
    }

    private boolean isContainerConfigurationSubReport(Report subReport) {
        StringKey subReportName = new UnknownStringKey("containers");
        if (subReport.getName().equals(subReportName)) {
            return true;
        }
        return false;
    }

    @Test
    public void verify_test_class_report_exists_with_contents() {
        List<TestClassReport> testClassReports = executionReport.getTestSuiteReports().get(0).getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(0);
        ConfigurationReport configurationReport = testClassReport.getConfiguration();
        List<TestMethodReport> testMethodReports = testClassReport.getTestMethodReports();

        assertThat(testClassReports).size().isEqualTo(1);

        assertThatTestClassReport(testClassReport)
            .hasName("org.arquillian.reporter.GreeterTest")
            .hasNumberOfEntries(1)
            .hasKeyValueEntryContainingKeys(ArquillianCoreKey.TEST_CLASS_REPORT_MESSAGE)
            .hasNumberOfSubReports(0);

        assertThat(configurationReport.getName())
            .isEqualTo(ReporterCoreKey.GENERAL_TEST_CLASS_CONFIGURATION_REPORT);

        assertThat(testMethodReports).size().isEqualTo(4);
    }

    @Test
    public void verify_test_class_configuration_report_exists_with_contents() {
        List<TestClassReport> testClassReports = executionReport.getTestSuiteReports().get(0).getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(0);
        ConfigurationReport configurationReport = testClassReport.getConfiguration();
        Report deploymentReport = configurationReport.getSubReports().get(0);

        assertThatReport(configurationReport)
            .hasName(ReporterCoreKey.GENERAL_TEST_CLASS_CONFIGURATION_REPORT)
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(1);

        verify_deployment_configuration_sub_report(deploymentReport);
    }

    private void verify_deployment_configuration_sub_report(Report deploymentReport) {
        Report nestedDeploymentReport = (Report) deploymentReport.getSubReports().get(0);

        assertThat(deploymentReport.getName())
            .isEqualTo(new UnknownStringKey("deployments"));
        assertThat(deploymentReport.getEntries()).size().isEqualTo(0);
        assertThat(deploymentReport.getSubReports()).size().isEqualTo(1);

        assertThatReport(nestedDeploymentReport)
            .hasName(ArquillianCoreKey.DEPLOYMENT_IN_TEST_CLASS_REPORT)
            .hasNumberOfEntries(4)
            .hasEntriesContaining(new KeyValueEntry(ArquillianCoreKey.ARCHIVE_NAME_OF_DEPLOYMENT, "greeterArchive.jar"),
                new KeyValueEntry(ArquillianCoreKey.DEPLOYMENT_IN_TEST_CLASS_NAME, "_DEFAULT_"),
                new KeyValueEntry(ArquillianCoreKey.ORDER_OF_DEPLOYMENT, "-1"),
                new KeyValueEntry(ArquillianCoreKey.PROTOCOL_USED_FOR_DEPLOYMENT, "_DEFAULT_"))
            .hasNumberOfSubReports(0);
    }

    @Test
    public void verify_test_method_reports_exists_with_contents() {
        final List<TestMethodReport> testMethodReports = getTestMethodReports();
        final List<StringKey> testMethodReportNameList = Arrays.asList(new UnknownStringKey("run_client_test"),
            new UnknownStringKey("should_create_greeting"), new UnknownStringKey("should_report_as_failed"),
            new UnknownStringKey("should_report_as_skipped"));

        assertThat(testMethodReports).size().isEqualTo(4);

        for (TestMethodReport testMethodReport : testMethodReports) {
            StringKey testMethodName = testMethodReport.getName();

            assertThat(testMethodName).isIn(testMethodReportNameList);
            assertThatTestMethodReport(testMethodReport)
                .hasNumberOfEntries(3)
                .hasEntriesContaining(
                    new KeyValueEntry(ArquillianCoreKey.TEST_METHOD_OPERATES_ON_DEPLOYMENT, "_DEFAULT_"),
                    new KeyValueEntry(ArquillianCoreKey.TEST_METHOD_REPORT_MESSAGE, (String) null))
                .hasNumberOfSubReports(0);
            assertThat(testMethodReport.getFailureReport().getName())
                .isEqualTo(GENERAL_METHOD_FAILURE_REPORT);
            assertThat(testMethodReport.getConfiguration().getName())
                .isEqualTo(ReporterCoreKey.GENERAL_TEST_METHOD_CONFIGURATION_REPORT);
        }
    }

    @Test
    public void verify_method_report_for_run_as_client_test() {
        final TestMethodReport run_client_test_report = getTestMethodReport("run_client_test");

        assertThatTestMethodReport(run_client_test_report)
            .hasStatus(TestResult.Status.PASSED)
            .hasEntriesContaining(new KeyValueEntry(ArquillianCoreKey.TEST_METHOD_RUNS_AS_CLIENT, "true"));
    }

    @Test
    public void verify_method_report_for_run_as_client_false_test() {
        final TestMethodReport run_client_test_report = getTestMethodReport("should_create_greeting");

        assertThatTestMethodReport(run_client_test_report)
            .hasStatus(TestResult.Status.PASSED)
            .hasEntriesContaining(new KeyValueEntry(ArquillianCoreKey.TEST_METHOD_RUNS_AS_CLIENT, "false"));
    }

    @Test
    public void verify_method_report_for_failed_test() {
        final TestMethodReport should_report_as_failed = getTestMethodReport("should_report_as_failed");
        final FailureReport failureReport = should_report_as_failed.getFailureReport();

        assertThatTestMethodReport(should_report_as_failed)
            .hasStatus(TestResult.Status.FAILED)
            .hasNumberOfEntries(3);

        assertThatReport(failureReport)
            .hasName(GENERAL_METHOD_FAILURE_REPORT)
            .hasNumberOfEntries(1)
            .hasKeyValueEntryContainingKeys(METHOD_FAILURE_REPORT_STACKTRACE);
    }

    @Test
    public void verify_method_report_for_skipped_test() {

        final TestMethodReport should_report_as_skipped = getTestMethodReport("should_report_as_skipped");

        assertThatTestMethodReport(should_report_as_skipped)
            .hasStatus(TestResult.Status.SKIPPED)
            .hasNumberOfEntries(3);
    }

    private List<TestMethodReport> getTestMethodReports() {
        List<TestClassReport> testClassReports = executionReport.getTestSuiteReports().get(0).getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(0);

        return testClassReport.getTestMethodReports();
    }

    private TestMethodReport getTestMethodReport(String methodName) {
        final List<TestMethodReport> testMethodReports = getTestMethodReports();

        return testMethodReports.stream()
            .filter(testMethodReport -> testMethodReport.getName().getValue().equals(methodName))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No method found with name: " + methodName));
    }
}
