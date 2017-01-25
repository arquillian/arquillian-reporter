package org.arquillian.reporter;

import org.arquillian.reporter.api.builder.entry.TableBuilder;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.builder.report.ReportInSectionBuilder;
import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.model.ReporterCoreKeys;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.impl.ReporterLifecycleManager;
import org.arquillian.reporter.impl.builder.entry.TableBuilderImpl;
import org.arquillian.reporter.impl.builder.report.ReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.ReportInSectionBuilderImpl;
import org.arquillian.reporter.impl.builder.report.TestClassReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.TestMethodReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.TestSuiteReportBuilderImpl;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterExtension implements LoadableExtension {

    public void register(ExtensionBuilder builder) {
        builder.observer(ReporterLifecycleManager.class);
        builder.service(StringKey.class, ReporterCoreKeys.class);

        // builders
        builder.service(ReportBuilder.class, ReportBuilderImpl.class);
        builder.service(ReportInSectionBuilder.class, ReportInSectionBuilderImpl.class);
        builder.service(TableBuilder.class, TableBuilderImpl.class);
        builder.service(TestClassReportBuilder.class, TestClassReportBuilderImpl.class);
        builder.service(TestMethodReportBuilder.class, TestMethodReportBuilderImpl.class);
        builder.service(TestSuiteReportBuilder.class, TestSuiteReportBuilderImpl.class);
    }
}
