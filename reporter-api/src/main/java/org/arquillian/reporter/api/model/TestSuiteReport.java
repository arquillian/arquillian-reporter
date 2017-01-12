package org.arquillian.reporter.api.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReport {

    private Date start = new Date(System.currentTimeMillis());
    private Date stop;
    private Configuration configuration = new Configuration();
    private List<TestMethodReport> testMethodReport;

    public Configuration getConfiguration() {
        return configuration;
    }

}
