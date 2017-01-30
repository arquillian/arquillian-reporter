package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.utils.Utils;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThat;
import static org.arquillian.reporter.impl.utils.DummyStringKeys.CONFIG_NAME;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigurationReportTest {

    @Test
    public void testAddNewReportToConfigurationReport() throws InstantiationException, IllegalAccessException {
        ConfigurationReport configurationReport = Utils.prepareReport(ConfigurationReport.class, "any config", 1, 5);

        // add a normal report - should be added into List of subReports
        Report basicReport = Utils.prepareReport(Report.class, "report", 5, 10);
        configurationReport.addNewReport(basicReport);

        // verify
        assertThat(configurationReport)
            .hasSubReportsEndingWith(basicReport)
            .hasNumberOfSubreports(5);

        // add configuration report - should be added into List of subReports
        ConfigurationReport configToAdd = Utils.prepareReport(ConfigurationReport.class, "config", 5, 10);
        configurationReport.addNewReport(configToAdd);

        // verify
        assertThat(configurationReport)
            .hasSubReportsEndingWith(configToAdd)
            .hasName("any config")
            .hasNumberOfSubreports(6)
            .hasGeneratedSubreportsAndEntries(1, 5);
    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        ConfigurationReport mainConfigReport = Utils.prepareReport(ConfigurationReport.class, CONFIG_NAME, 1, 5);
        List<ConfigurationReport> firstConfigs = Utils.prepareSetOfReports(ConfigurationReport.class, 5, "first", 1, 5);
        mainConfigReport.getSubReports().addAll(firstConfigs);

        ConfigurationReport configToMerge = Utils.prepareReport(ConfigurationReport.class, "to merge", 5, 10);
        List<ConfigurationReport> secondConfigs =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "second", 5, 10);
        configToMerge.getSubReports().addAll(secondConfigs);

        //merge
        mainConfigReport.merge(configToMerge);

        // the report that has been merged is still same
        assertThat(configToMerge)
            .hasSubReportsEndingWith(secondConfigs.stream().toArray(ConfigurationReport[]::new))
            .hasName("to merge")
            .hasNumberOfSubreports(10)
            .hasGeneratedSubreportsAndEntries(5,10);

        // the main report should contain all information
        assertThat(mainConfigReport)
            .hassSubReportsContaining(firstConfigs.stream().toArray(ConfigurationReport[]::new))
            .hasSubReportsEndingWith(secondConfigs.stream().toArray(ConfigurationReport[]::new))
            .hasName(CONFIG_NAME)
            .hasNumberOfSubreports(19)
            .hasGeneratedSubreportsAndEntries(1,10);
    }
}