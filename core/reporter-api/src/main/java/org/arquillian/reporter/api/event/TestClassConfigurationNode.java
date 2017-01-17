package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationNode extends ReportNodeEvent<ConfigurationReport,TestClassNode> {

    public TestClassConfigurationNode(ConfigurationReport configuration) {
        super(configuration);
    }

    public TestClassConfigurationNode(String identifier) {
        super(identifier);
    }

    public TestClassConfigurationNode(ConfigurationReport configuration, String identifier) {
        super(configuration, identifier);
    }

    public TestClassConfigurationNode(String identifier, Class<?> testClass) {
        super(identifier);
        setParentEvent(new TestClassNode(testClass));
    }

    public TestClassConfigurationNode(ConfigurationReport configuration, Class<?> testClass) {
        super(configuration);
        setParentEvent(new TestClassNode(testClass));
    }

    public TestClassConfigurationNode(ConfigurationReport configuration, String identifier, Class<?> testClass) {
        super(configuration, identifier);
        setParentEvent(new TestClassNode(testClass));
    }
}
