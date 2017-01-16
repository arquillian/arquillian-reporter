package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Configuration;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestClassConfiguration extends ReportEvent<Configuration,ReportTestClass> {


    public ReportTestClassConfiguration(String identifier) {
        super(identifier);
    }

    public ReportTestClassConfiguration(Configuration configuration, Class<?> testClass) {
        super(configuration);
        setParentEvent(new ReportTestClass(testClass));
    }

    public ReportTestClassConfiguration(Configuration configuration, String identifier, Class<?> testClass) {
        super(configuration, identifier);
        setParentEvent(new ReportTestClass(testClass));
    }
}
