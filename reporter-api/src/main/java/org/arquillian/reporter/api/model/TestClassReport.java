package org.arquillian.reporter.api.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReport extends Section {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestMethodReport> testMethodReport;

    public TestClassReport(String name) {
        super(name);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
