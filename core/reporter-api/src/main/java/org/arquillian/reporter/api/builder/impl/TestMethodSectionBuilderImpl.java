package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.AbstractSectionBuilder;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSectionBuilderImpl extends
    AbstractSectionBuilder<TestMethodReport, TestMethodSectionBuilderImpl> {


    public TestMethodSectionBuilderImpl(TestMethodReport sectionReport) {
        super(sectionReport);
    }

    public TestMethodSectionBuilderImpl stop(){
        getSectionReport().setStop(Utils.getCurrentDate());
        return this;
    }

//    public TestMethodSectionBuilderImpl setFailure(Failure failure){
//        setFailure(failure);
//        return this;
//    }
//
//    public TestMethodSectionBuilderImpl setStatus(TestResult.Status failure){
//        setStatus(failure);
//        return this;
//    }

    public TestMethodSectionBuilderImpl setResult(TestResult result){
        if (result.getStatus() == TestResult.Status.FAILED && result.getThrowable() != null) {
            String stackTrace = getStackTrace(result.getThrowable());
            FailureReport failureReport = new FailureReport("Test failure");
            Reporter.section(failureReport).addKeyValueEntry("stacktrace", stackTrace);
            getSectionReport().setFailureReport(failureReport);
        }
        getSectionReport().setStatus(result.getStatus());
        return this;
    }

    private String getStackTrace(Throwable aThrowable) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        sb.append(aThrowable.toString());
        sb.append(newLine);

        for (StackTraceElement element : aThrowable.getStackTrace()) {
            sb.append(element);
            sb.append(newLine);
        }
        return sb.toString();
    }
}
