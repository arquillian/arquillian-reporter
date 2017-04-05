package org.arquillian.reporter.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.api.model.report.WithConfigurationReport;
import org.arquillian.reporter.api.model.report.WithStartAndStop;
import org.arquillian.reporter.impl.ExecutionReport;

import static org.arquillian.reporter.parser.ReportJsonParser.prepareGsonParser;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportJsonDeserializer implements JsonDeserializer<Report> {

    @Override
    public Report deserialize(JsonElement json, Type typeOfReport, JsonDeserializationContext context)
        throws JsonParseException {

        JsonObject jsonReport = (JsonObject) json;

        if (jsonReport.get("testSuiteReports") != null) {
            return parseExecutionReport(jsonReport, context);

        } else if (jsonReport.get("testClassReports") != null) {
            return parseTestSuiteReport(jsonReport);

        } else if (jsonReport.get("testMethodReports") != null) {
            return parseTestClassReport(jsonReport);

        } else if (jsonReport.get("start") != null) {
            return parseTestMethodReport(jsonReport);

        } else {
            Report report;
            if (typeOfReport.getTypeName().equals(ConfigurationReport.class.getTypeName())){
                report = new ConfigurationReport();
            } else if (typeOfReport.getTypeName().equals(FailureReport.class.getTypeName())){
                report = new FailureReport();
            } else {
                report = new BasicReport();
            }
            return setDefaultValues(report, jsonReport);
        }
    }

    private Report parseExecutionReport(JsonObject jsonReport, JsonDeserializationContext context){
        ExecutionReport executionReport = new ExecutionReport();

        JsonArray testSuiteReportsJson = jsonReport.get("testSuiteReports").getAsJsonArray();
        testSuiteReportsJson.forEach(suite -> {
            Report testSuiteReport = prepareGsonParser().fromJson(suite, TestSuiteReport.class);
            executionReport.getTestSuiteReports().add((TestSuiteReport) testSuiteReport);
        });

        return setDefaultValues(executionReport, jsonReport);
    }

    private Report parseTestSuiteReport(JsonObject jsonReport){
        TestSuiteReport testSuiteReport = new TestSuiteReport();

        JsonArray testClassReportsJson = jsonReport.get("testClassReports").getAsJsonArray();
        testClassReportsJson.forEach(testClass -> {
            Report testClassReport = prepareGsonParser().fromJson(testClass, TestClassReport.class);
            testSuiteReport.getTestClassReports().add((TestClassReport) testClassReport);
        });

        setConfiguration(testSuiteReport, jsonReport);
        setStartAndStop(testSuiteReport, jsonReport);
        return setDefaultValues(testSuiteReport, jsonReport);
    }

    private Report parseTestClassReport(JsonObject jsonReport){
        TestClassReport testClassReport = new TestClassReport();

        JsonArray testMethodReportsJson = jsonReport.get("testMethodReports").getAsJsonArray();
        testMethodReportsJson.forEach(testMethod -> {
            Report testMethodReport = prepareGsonParser().fromJson(testMethod, TestMethodReport.class);
            testClassReport.getTestMethodReports().add((TestMethodReport) testMethodReport);
        });

        setConfiguration(testClassReport, jsonReport);
        setStartAndStop(testClassReport, jsonReport);
        return setDefaultValues(testClassReport, jsonReport);
    }

    private Report parseTestMethodReport(JsonObject jsonReport){
        TestMethodReport testMethodReport = new TestMethodReport();

        JsonElement failureReportJson = jsonReport.get("failureReport");
        if (failureReportJson != null) {
            testMethodReport.setFailureReport(prepareGsonParser().fromJson(failureReportJson, FailureReport.class));
        }

        setConfiguration(testMethodReport, jsonReport);
        setStartAndStop(testMethodReport, jsonReport);
        return setDefaultValues(testMethodReport, jsonReport);
    }

    private void setConfiguration(WithConfigurationReport report, JsonObject jsonReport) {

        JsonElement configurationJson = jsonReport.get("configuration");
        if (configurationJson != null) {
            report.setConfiguration(
                (ConfigurationReport) deserialize(configurationJson, ConfigurationReport.class, null));
        }
    }

    private void setStartAndStop(WithStartAndStop report, JsonObject jsonReport){
        JsonElement start = jsonReport.get("start");
        report.setStart(start.getAsString());
        JsonElement stop = jsonReport.get("stop");
        report.setStop(stop.getAsString());
    }

    private Report setDefaultValues(Report report, JsonObject jsonReport) {
        JsonElement name = jsonReport.get("name");
        JsonArray entries = jsonReport.get("entries").getAsJsonArray();
        JsonArray subReports = jsonReport.get("subReports").getAsJsonArray();

        report.setName(prepareGsonParser().fromJson(name, StringKey.class));


        entries.forEach(subEntry -> {
            Entry entry = prepareGsonParser().fromJson(subEntry, Entry.class);
            report.getEntries().add(entry);
        });

        subReports.forEach(subReport -> {
            Report subReportEntry = prepareGsonParser().fromJson(subReport, Report.class);
            report.getSubReports().add(subReportEntry);
        });

        return report;
    }
}
