package org.arquillian.reporter.api.utils;

import java.util.Date;

import org.arquillian.reporter.api.model.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSectionBuilderImpl extends AbstractSectionBuilderImpl<TestClassReport, TestClassSectionBuilderImpl> {

    public TestClassSectionBuilderImpl(TestClassReport sectionReport) {
        super(sectionReport);
    }

    @Override
    protected TestClassReport createSectionInstanceWithName(String name) {
        return new TestClassReport(name);
    }


    public TestClassSectionBuilderImpl stop() {
        getSectionReport().setStop(new Date(System.currentTimeMillis()));
        return this;
    }
}
