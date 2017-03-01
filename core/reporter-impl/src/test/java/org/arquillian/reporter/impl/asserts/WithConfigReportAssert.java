package org.arquillian.reporter.impl.asserts;

import java.util.List;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.WithConfigurationReport;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getConfigReportName;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class WithConfigReportAssert<REPORTASSERTTYPE extends ReportAssert<REPORTASSERTTYPE, REPORTTYPE>, REPORTTYPE extends Report & WithConfigurationReport>
    extends ReportAssert<REPORTASSERTTYPE, REPORTTYPE> {

    public WithConfigReportAssert(REPORTTYPE actual, Class<? extends ReportAssert> reportAssertClass) {
        super(actual, reportAssertClass);
    }

    public REPORTASSERTTYPE hasConfigSubReportsContainingExactly(ConfigurationReport... configurationReports) {
        assertThat(actual.getConfiguration().getSubReports())
            .as("The report with name <%s> and type <%s> should contain exactly the given configuration sub-reports",
                actual.getName(), actual.getClass())
            .containsExactly(configurationReports);
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasConfigSubReportListEqualTo(List<ConfigurationReport> configurationReportList) {
        assertThat(actual.getConfiguration().getSubReports())
            .as("The configuration report list stored in the report with name <%s> and type <%s> should be equal to the given one",
                actual.getName(), actual.getClass())
            .isEqualTo(configurationReportList);
        return (REPORTASSERTTYPE) this;
    }

    public REPORTASSERTTYPE hasConfigWithNumberOfSubreportsAndEntries(int number) {
        assertThatReport(actual.getConfiguration())
            .as("The configuration report stored in the report with name <%s> and type <%s> should contain the given number of sub-reports and entries: <%s>",
                actual.getName(), actual.getClass(), number)
            .hasNumberOfSubReportsAndEntries(number);
        return (REPORTASSERTTYPE) this;
    }

    protected void verifyConfigReports(ConfigurationReport configuration, String nameSuffix, List<Report> merged) {
        // check the expected number of configuration reports - should be same as the number of generated sections
        assertThatReport(configuration)
            .as("Configuration report should contain an exact number of test config sub-reports")
            .hasNumberOfSubReports(EXPECTED_NUMBER_OF_SECTIONS);

        // for each configuration sub-report
        IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
            Report reportOnIndex = configuration.getSubReports().get(index);

            defaultCheckOfIfMergedOrContainsGeneratedSubReports(merged, index, reportOnIndex);

            assertThatReport(reportOnIndex)
                .as("The config report should have same name that was generated for the index <%s> ", index)
                .hasName(getConfigReportName(index, nameSuffix));
        });
    }
}
