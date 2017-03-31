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

        /*ExecutionReportAssert.assertThatExecutionReport(executionReport)
            .hasNumberOfEntries(0)
            .hasNumberOfSubReports(0)
            .hasTestSuiteReportsExactly(report);*/

        System.out.println(executionReport);
    }
}
