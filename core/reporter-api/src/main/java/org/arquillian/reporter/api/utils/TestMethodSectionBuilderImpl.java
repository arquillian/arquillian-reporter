package org.arquillian.reporter.api.utils;

import java.util.Date;

import org.arquillian.reporter.api.model.Failure;
import org.arquillian.reporter.api.model.TestMethodReport;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSectionBuilderImpl extends AbstractSectionBuilderImpl<TestMethodReport, TestMethodSectionBuilderImpl> {


    public TestMethodSectionBuilderImpl(TestMethodReport sectionReport) {
        super(sectionReport);
    }

    public TestMethodSectionBuilderImpl stop(){
        getSectionReport().setStop(new Date(System.currentTimeMillis()));
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
            Failure failure = new Failure("Test failure");
            Reporter.section(failure).addKeyValueEntry("stacktrace", stackTrace);
            getSectionReport().setFailure(failure);
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
