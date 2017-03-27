package org.arquillian.reporter.impl.builder;

import org.arquillian.reporter.api.builder.BuilderRegistryDelegate;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.FileEntry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.entry.StringEntry;
import org.arquillian.reporter.api.model.report.*;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.base.AbstractReporterTestBase;
import org.arquillian.reporter.impl.utils.dummy.DummyStringKeys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.arquillian.reporter.api.builder.Reporter.createReport;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AllBuildersTest extends AbstractReporterTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ExecutionReport.class},
                {BasicReport.class},
                {TestSuiteReport.class},
                {ConfigurationReport.class},
                {TestClassReport.class},
                {TestMethodReport.class},
                {FailureReport.class}
        });
    }

    private Class<AbstractReport> reportClass;

    public AllBuildersTest(Class<AbstractReport> reportClass) {
        this.reportClass = reportClass;
    }

    @Test
    public void should_create_empty_report() throws IllegalAccessException, InstantiationException {

        // Given
        AbstractReport report = reportClass.newInstance();
        report.setName(new UnknownStringKey(""));

        // When
        Report buildReport = Reporter.createReport(report).build();

        // Then
        assertThatReport(buildReport)
                .hasName("")
                .hasNumberOfSubReportsAndEntries(0);

    }

    @Test
    public void test_basic_reporter_builder_api() throws IllegalAccessException, InstantiationException {

        // Given
        AbstractReport report = reportClass.newInstance();
        report.setName(new UnknownStringKey("report"));

        Map<String, String> mapOfStrings = new HashMap<String,String>() {
            {
                put("Map_Key_1", "Map_Value_1");
                put("Map_Key_2", "Map_Value_2");
            }
        };

        // When
        ReportBuilder reportBuilder = Reporter.createReport(report)
                .addEntry("entry")
                .addEntry(new StringEntry("String Entry"))
                .addEntry(new KeyValueEntry("Key Entry", "Value Entry"))
                .addEntry(new FileEntry("file path"))
                .addEntries(Arrays.asList(new StringEntry("list_entries_1"), new StringEntry("list_entries_2")))
                .addEntries(new String[]{"array_strings_1", "array_strings_1"})
                .addEntries(new StringEntry[]{new StringEntry("array_entries_1"), new StringEntry("array_entries_2")})
                .addKeyValueEntry("key", "value")
                .feedKeyValueListFromMap(mapOfStrings)
                .addKeyValueEntry(DummyStringKeys.DUMMY_1_NAME, "value")
                .addKeyValueEntry(DummyStringKeys.DUMMY_2_NAME, 1)
                .addKeyValueEntry(DummyStringKeys.DUMMY_3_NAME, true)
                .addReport(createReport("sub report 1"))
                .addReport(createReport("sub report 2"));

        Report builtReport = reportBuilder.build();

        // Then
        assertThat(reportBuilder).isInstanceOf(report.getReportBuilderClass());

        assertThatReport(builtReport)
                .hasName("report")
                .hasEntriesContaining(new StringEntry("entry"),
                        new StringEntry("String Entry"),
                        new KeyValueEntry("Key Entry", "Value Entry"),
                        new FileEntry("file path"),
                        new KeyValueEntry("key", "value"),
                        new StringEntry("list_entries_1"),
                        new StringEntry("list_entries_2"),
                        new StringEntry("array_entries_1"),
                        new StringEntry("array_entries_2"),
                        new StringEntry("array_entries_1"),
                        new StringEntry("array_entries_2"),
                        new KeyValueEntry("Map_Key_1", "Map_Value_1"),
                        new KeyValueEntry("Map_Key_2", "Map_Value_2"),
                        new KeyValueEntry(DummyStringKeys.DUMMY_1_NAME, "value"),
                        new KeyValueEntry(DummyStringKeys.DUMMY_2_NAME, "1"),
                        new KeyValueEntry(DummyStringKeys.DUMMY_3_NAME, "true"))
                .hasSubReportsContaining(Arrays.asList(new BasicReport("sub report 1"), new BasicReport("sub report 2")))
                .hasNumberOfEntries(16)
                .hasNumberOfSubReports(2)
                .isInstanceOf(reportClass);
    }

    @Override
    protected void addAdditionalExtensions(List<Class<?>> extensions) {

    }

    @Override
    protected void addReporterStringKeys(List<StringKey> stringKeys) {

    }

    @Override
    protected void registerBuilders(BuilderRegistryDelegate builderRegistry) {

    }
}
