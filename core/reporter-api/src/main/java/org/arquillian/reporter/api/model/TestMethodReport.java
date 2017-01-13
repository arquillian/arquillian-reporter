package org.arquillian.reporter.api.model;

import java.util.Date;

import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReport extends SectionReport {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private TestResult.Status status;
    private Failure failure;
    private Configuration configuration;

    public TestMethodReport(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public TestResult.Status getStatus() {
        return status;
    }

    public void setStatus(TestResult.Status status) {
        this.status = status;
    }

    public Failure getFailure() {
        return failure;
    }

    public void setFailure(Failure failure) {
        this.failure = failure;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
}
