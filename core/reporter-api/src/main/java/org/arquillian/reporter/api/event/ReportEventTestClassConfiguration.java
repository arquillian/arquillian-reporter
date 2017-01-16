package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Configuration;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestClassConfiguration extends ReportEvent<Configuration,ReportEventTestClass> {


    public ReportEventTestClassConfiguration(String identifier) {
        super(identifier);
    }

    public ReportEventTestClassConfiguration(Configuration configuration, Class<?> testClass) {
        super(configuration);
        setParentEvent(new ReportEventTestClass(testClass));
    }

    public ReportEventTestClassConfiguration(Configuration configuration, String identifier, Class<?> testClass) {
        super(configuration, identifier);
        setParentEvent(new ReportEventTestClass(testClass));
    }
}
