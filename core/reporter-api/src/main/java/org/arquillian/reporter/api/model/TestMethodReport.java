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
}
