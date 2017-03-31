package org.arquillian.reporter.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.impl.ExecutionReport;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportJsonParser {

    private static Gson gson;

    public static ExecutionReport parse(String pathToJSONFile) throws FileNotFoundException {
        Gson gson = prepareGsonParser();

        ExecutionReport executionReport = gson.fromJson(new FileReader(pathToJSONFile), ExecutionReport.class);
        return executionReport;
    }

    public static Gson prepareGsonParser(){
        if (gson == null) {
            gson = new GsonBuilder()
                .registerTypeAdapter(StringKey.class, new StringKeyJsonDeserializer())
                .registerTypeAdapter(Report.class, new ReportJsonDeserializer())
                .registerTypeAdapter(TestMethodReport.class, new ReportJsonDeserializer())
                .registerTypeAdapter(ExecutionReport.class, new ReportJsonDeserializer())
                .registerTypeAdapter(TestClassReport.class, new ReportJsonDeserializer())
                .registerTypeAdapter(ConfigurationReport.class, new ReportJsonDeserializer())
                .registerTypeAdapter(FailureReport.class, new ReportJsonDeserializer())
                .create();
        }
        return gson;
    }
}
