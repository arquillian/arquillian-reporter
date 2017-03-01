package org.arquillian.reporter.impl.asserts;

import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BasicReportAssert extends ReportAssert<BasicReportAssert, Report> {

    public BasicReportAssert(Report actual) {
        super(actual, BasicReportAssert.class);
    }
}
