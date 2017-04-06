package org.arquillian.reporter;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.arquillian.core.reporter.impl.ArquillianCoreKey;
import org.arquillian.environment.reporter.impl.EnvironmentKey;
import org.arquillian.reporter.api.model.ReporterCoreKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.parser.ReportJsonParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.TestClassReportAssert.assertThatTestClassReport;
import static org.arquillian.reporter.impl.asserts.TestMethodReportAssert.assertThatTestMethodReport;
import static org.arquillian.reporter.impl.asserts.TestSuiteReportAssert.assertThatTestSuiteReport;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GreeterVerifier {

    static File file;
    static ExecutionReport executionReport;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {

        Result result = JUnitCore.runClasses(GreeterTest.class);
        Gson gson = new Gson();
        file = new File("target/report.json");
        executionReport = ReportJsonParser.parse("target/report.json");
    }

    @Test
    public void verify_test_report_creation() throws FileNotFoundException {
        assertThat(file).exists();
    }

    @Test
    public void verify_execution_report() {
        assertThatExecutionReport(executionReport)
            .hasName("execution")
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(0);
    }

    @Test
    public void verify_test_suite_report() {
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
    public void verify_test_suite_configuration_report() {
        ConfigurationReport configuration = executionReport.getTestSuiteReports().get(0).getConfiguration();
        List<Report> subReports = configuration.getSubReports();

        assertThatReport(configuration)
            .hasName(ReporterCoreKey.GENERAL_TEST_SUITE_CONFIGURATION_REPORT)
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(2);

        assertThat(subReports.get(0).getName())
            .isEqualTo(new UnknownStringKey("containers"));
        assertThat(subReports.get(0).getEntries()).size().isEqualTo(0);
        assertThat(subReports.get(0).getSubReports()).size().isEqualTo(1);

        assertThat(subReports.get(1).getName())
            .isEqualTo(EnvironmentKey.ENVIRONMENT_SECTION_NAME);
        assertThat(subReports.get(1).getEntries()).size().isEqualTo(8);
        assertThat(subReports.get(1).getSubReports()).size().isEqualTo(0);
    }

    @Test
    public void verify_test_class_report() {
        List<TestClassReport> testClassReports = executionReport.getTestSuiteReports().get(0).getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(0);
        ConfigurationReport configurationReport = testClassReport.getConfiguration();
        List<TestMethodReport> testMethodReports = testClassReport.getTestMethodReports();

        assertThat(testClassReports).size().isEqualTo(1);

        assertThatTestClassReport(testClassReport)
            .hasName("org.arquillian.reporter.GreeterTest")
            .hasNumberOfEntries(1)
            .hasNumberOfSubReports(0);

        assertThat(configurationReport.getName())
            .isEqualTo(ReporterCoreKey.GENERAL_TEST_CLASS_CONFIGURATION_REPORT);

        assertThat(testMethodReports).size().isEqualTo(2);
    }

    @Test
    public void verify_test_class_configuration_report() {
        List<TestClassReport> testClassReports = executionReport.getTestSuiteReports().get(0).getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(0);
        ConfigurationReport configurationReport = testClassReport.getConfiguration();
        Report deploymentReport = configurationReport.getSubReports().get(0);

        assertThatReport(configurationReport)
            .hasName(ReporterCoreKey.GENERAL_TEST_CLASS_CONFIGURATION_REPORT)
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(1);

        assertThat(deploymentReport.getName())
            .isEqualTo(new UnknownStringKey("deployments"));
        assertThat(deploymentReport.getEntries()).size().isEqualTo(0);
        assertThat(deploymentReport.getSubReports()).size().isEqualTo(1);
    }

    @Test
    public void verify_test_method_reports() {
        List<TestClassReport> testClassReports = executionReport.getTestSuiteReports().get(0).getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(0);
        List<TestMethodReport> testMethodReports = testClassReport.getTestMethodReports();

        assertThat(testMethodReports.get(0).getName())
            .isEqualTo(new UnknownStringKey("run_client_test"));

        assertThat(testMethodReports.get(1).getName())
            .isEqualTo(new UnknownStringKey("should_create_greeting"));

        for (TestMethodReport testMethodReport : testMethodReports) {
            assertThatTestMethodReport(testMethodReport)
                .hasNumberOfEntries(3)
                .hasNumberOfSubReports(0);
            assertThat(testMethodReport.getFailureReport().getName())
                .isEqualTo(ReporterCoreKey.GENERAL_METHOD_FAILURE_REPORT);
            assertThat(testMethodReport.getConfiguration().getName())
                .isEqualTo(ReporterCoreKey.GENERAL_TEST_METHOD_CONFIGURATION_REPORT);
        }
    }
}
