package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport extends SectionReport {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestClassReport> testClassReports = new ArrayList<>();

    public TestSuiteReport(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<TestClassReport> getTestClassReports() {
        return testClassReports;
    }

    public TestSuiteReport stop() {
        this.stop = new Date(System.currentTimeMillis());
        return this;
    }

}
