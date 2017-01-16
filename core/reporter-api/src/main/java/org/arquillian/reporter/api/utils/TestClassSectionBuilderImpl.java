package org.arquillian.reporter.api.utils;

import java.util.Date;

import org.arquillian.reporter.api.model.TestClassSection;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSectionBuilderImpl extends AbstractSectionBuilderImpl<TestClassSection, TestClassSectionBuilderImpl> {

    public TestClassSectionBuilderImpl(TestClassSection sectionReport) {
        super(sectionReport);
    }

    public TestClassSectionBuilderImpl stop() {
        getSectionReport().setStop(new Date(System.currentTimeMillis()));
        return this;
    }
}
