package org.arquillian.reporter.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.api.model.report.WithConfigurationReport;
import org.arquillian.reporter.impl.ExecutionReport;

import java.lang.reflect.Type;
import java.util.List;

import static org.arquillian.reporter.parser.ReportJsonParser.prepareGsonParser;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportJsonDeserializer implements JsonDeserializer<Report> {

    @Override
    public Report deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

        JsonObject jsonReport = (JsonObject) json;

        if (jsonReport.get("testSuiteReports") != null) {
            return parseExecutionReport(jsonReport);

        } else if (jsonReport.get("testClassReports") != null) {
            return parseTestSuiteReport(jsonReport);

        } else if (jsonReport.get("testClassReports") != null) {
            return parseTestClassReport(jsonReport);

        } else if (jsonReport.get("start") != null) {
            return parseTestMethodReport(jsonReport);

        } else {
            BasicReport basicReport = new BasicReport();
            return setDefaultValues(basicReport, jsonReport);
        }
    }

    private Report parseExecutionReport(JsonObject jsonReport){
        ExecutionReport executionReport = new ExecutionReport();

        JsonArray getTestSuiteReports = jsonReport.get("testSuiteReports").getAsJsonArray();
        executionReport.getTestSuiteReports().addAll(prepareGsonParser().fromJson(getTestSuiteReports, List.class));

        return setDefaultValues(executionReport, jsonReport);
    }

    private Report parseTestSuiteReport(JsonObject jsonReport){
        TestSuiteReport testSuiteReport = new TestSuiteReport();

        JsonArray testClassReportsJson = jsonReport.get("testClassReports").getAsJsonArray();
        testSuiteReport.getTestClassReports()
            .addAll(prepareGsonParser().fromJson(testClassReportsJson, List.class));

        setConfigurationStartAndStop(testSuiteReport, jsonReport);
        return setDefaultValues(testSuiteReport, jsonReport);
    }

    private Report parseTestClassReport(JsonObject jsonReport){
        TestClassReport testClassReport = new TestClassReport();

        JsonArray testMethodReportsJson = jsonReport.get("testMethodReports").getAsJsonArray();
        testClassReport.getTestMethodReports()
            .addAll(prepareGsonParser().fromJson(testMethodReportsJson, List.class));

        setConfigurationStartAndStop(testClassReport, jsonReport);
        return setDefaultValues(testClassReport, jsonReport);
    }

    private Report parseTestMethodReport(JsonObject jsonReport){
        TestMethodReport testClassReport = new TestMethodReport();

        JsonElement failureReportJson = jsonReport.get("failureReport");
        if (failureReportJson != null) {
            testClassReport.setFailureReport(prepareGsonParser().fromJson(failureReportJson, FailureReport.class));
        }

        setConfigurationStartAndStop(testClassReport, jsonReport);
        return setDefaultValues(testClassReport, jsonReport);
    }

    private void setConfigurationStartAndStop(WithConfigurationReport report, JsonObject jsonReport) {

        JsonElement configurationJson = jsonReport.get("configuration");
        if (configurationJson != null) {
            report.setConfiguration(prepareGsonParser().fromJson(configurationJson, ConfigurationReport.class));
        }
    }

    private Report setDefaultValues(Report report, JsonObject jsonReport) {
        JsonElement name = jsonReport.get("name");
        JsonElement entries = jsonReport.get("entries");
        JsonElement subReports = jsonReport.get("subReports");

        report.setName(prepareGsonParser().fromJson(name, StringKey.class));
        report.setEntries(prepareGsonParser().fromJson(entries, List.class));
        report.setEntries(prepareGsonParser().fromJson(subReports, List.class));

        return report;
    }
}
