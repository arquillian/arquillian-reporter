package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.CONFIG_NAME;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigurationReportTest {

    @Test
    public void testAddNewReportToConfigurationReport() throws InstantiationException, IllegalAccessException {
        ConfigurationReport configurationReport = ReportGeneratorUtils
            .prepareReport(ConfigurationReport.class, "any config", 1, 5);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 5, 10);
        configurationReport.addNewReport(basicReport);

        // verify
        assertThatReport(configurationReport)
            .hasSubReportsEndingWith(basicReport)
            .hasNumberOfSubreports(5);

        // add configuration report - should be added into List of subReports
        ConfigurationReport configToAdd = ReportGeneratorUtils.prepareReport(ConfigurationReport.class, "config", 5, 10);
        configurationReport.addNewReport(configToAdd);

        // verify
        assertThatReport(configurationReport)
            .hasSubReportsEndingWith(configToAdd)
            .hasName("any config")
            .hasNumberOfSubreports(6)
            .hasNumberOfEntries(4)
            .hasGeneratedSubreportsAndEntries(1, 5);
    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        ConfigurationReport mainConfigReport = ReportGeneratorUtils
            .prepareReport(ConfigurationReport.class, CONFIG_NAME, 1, 5);
        List<ConfigurationReport> firstConfigs = ReportGeneratorUtils
            .prepareSetOfReports(ConfigurationReport.class, 5, "first", 1, 5);
        mainConfigReport.getSubReports().addAll(firstConfigs);

        ConfigurationReport configToMerge = ReportGeneratorUtils
            .prepareReport(ConfigurationReport.class, "to merge", 5, 10);
        List<ConfigurationReport> secondConfigs =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "second", 5, 10);
        configToMerge.getSubReports().addAll(secondConfigs);

        //merge
        mainConfigReport.merge(configToMerge);

        // the report that has been merged is still same
        assertThatReport(configToMerge)
            .hasSubReportsEndingWith(secondConfigs.stream().toArray(ConfigurationReport[]::new))
            .hasName("to merge")
            .hasNumberOfSubreports(10)
            .hasNumberOfEntries(5)
            .hasGeneratedSubreportsAndEntries(5,10);

        // the main report should contain all information
        assertThatReport(mainConfigReport)
            .hassSubReportsContaining(firstConfigs.stream().toArray(ConfigurationReport[]::new))
            .hasSubReportsEndingWith(secondConfigs.stream().toArray(ConfigurationReport[]::new))
            .hasName(CONFIG_NAME)
            .hasNumberOfSubreports(19)
            .hasNumberOfEntries(9)
            .hasGeneratedSubreportsAndEntries(1,10);
    }
}
