package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReport extends SectionReport {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestMethodReport> testMethodReports = new ArrayList<>();

    public TestClassReport(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public TestClassReport stop() {
        this.stop = new Date(System.currentTimeMillis());
        return this;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public List<TestMethodReport> getTestMethodReports() {
        return testMethodReports;
    }
}
