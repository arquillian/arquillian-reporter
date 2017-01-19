package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.FailureReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodFailureSection extends SectionEvent<TestMethodFailureSection, FailureReport, TestMethodSection> {

private Method testMethod;

    public TestMethodFailureSection(Method testMethod) {
        super(Utils.getTestMethodId(testMethod));
        this.testMethod = testMethod;
    }

    public TestMethodFailureSection(FailureReport failureReport, Method testMethod) {
        super(failureReport, Utils.getTestMethodId(testMethod));
        this.testMethod = testMethod;
    }

    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        return new TestMethodSection(testMethod);
    }

//    @Override
//    public Identifier<TestMethodFailureSection> identifyYourself() {
//        return null;
//    }
}
