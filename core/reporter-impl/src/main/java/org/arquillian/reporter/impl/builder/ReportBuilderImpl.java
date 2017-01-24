package org.arquillian.reporter.impl.builder;

import org.arquillian.reporter.api.builder.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.ReportBuilder;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportBuilderImpl<T extends AbstractReport<T,ReportBuilder>> extends
    AbstractReportBuilder<T, ReportBuilderImpl> {

    public ReportBuilderImpl(T sectionReport) {
        super(sectionReport);
    }

}
