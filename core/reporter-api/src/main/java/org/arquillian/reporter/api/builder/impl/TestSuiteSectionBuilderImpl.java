package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.AbstractSectionBuilder;
import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSectionBuilderImpl extends AbstractSectionBuilder<TestSuiteReport, TestSuiteSectionBuilderImpl> {

    public TestSuiteSectionBuilderImpl(TestSuiteReport sectionReport) {
        super(sectionReport);
    }

    public TestSuiteSectionBuilderImpl stop() {
        getSectionReport().setStop(Utils.getCurrentDate());
        return this;
    }
}
