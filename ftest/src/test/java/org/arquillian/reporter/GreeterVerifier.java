package org.arquillian.reporter;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.parser.ReportJsonParser;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GreeterVerifier {

    @BeforeClass
    public static void setUp() {
        Result result = JUnitCore.runClasses(GreeterTest.class);
    }

    @Test
    public void verify_test_report_creation() throws FileNotFoundException {
        Gson gson = new Gson();

        File file = new File("target/report.json");
        //JsonObject json = gson.fromJson(new FileReader(file), JsonObject.class);

        Assertions.assertThat(file).exists();
        ExecutionReport executionReport = ReportJsonParser.parse("target/report.json");

        //TestSuiteReport testSuiteReport = executionReport.getTestSuiteReports().get(0);

        /*TestSuiteReport testSuiteReport =
            Reporter.createReport(new TestSuiteReport(ReporterCoreKey.GENERAL_TEST_SUITE_CONFIGURATION_REPORT))
                .addReport(
                    Reporter
                        .createReport(new ConfigurationReport(ReporterCoreKey.GENERAL_TEST_METHOD_CONFIGURATION_REPORT))
                        .addReport(Reporter.createReport(ArquillianCoreKey.CONTAINER_REPORT))
                        .addReport(Reporter.createReport(ArquillianCoreKey.CONTAINER_NAME)))

                .build();
*/

        /*executionReport.getTestSuiteReports().get(0).
        ExecutionReportAssert.assertThatExecutionReport(executionReport)
            .hasName("execution")
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(0)
            .hasTestSuiteReportsExactly(new TestSuiteReport(ReporterCoreKey.GENERAL_TEST_SUITE_CONFIGURATION_REPORT));*/

        System.out.println(executionReport.getTestSuiteReports().get(0).getConfiguration().getName());
    }
}
