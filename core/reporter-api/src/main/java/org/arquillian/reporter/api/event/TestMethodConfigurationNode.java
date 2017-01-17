package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodConfigurationNode extends ReportNodeEvent<ConfigurationReport, TestMethodNode> {

    public TestMethodConfigurationNode(ConfigurationReport configuration) {
        super(configuration);
    }

    public TestMethodConfigurationNode(String identifier) {
        super(identifier);
    }

    public TestMethodConfigurationNode(ConfigurationReport configuration, String identifier) {
        super(configuration, identifier);
    }

    public TestMethodConfigurationNode(ConfigurationReport configuration, Method testMethod) {
        super(configuration);
        setParentEvent(new TestMethodNode(testMethod));
    }

    public TestMethodConfigurationNode(String identifier, Method testMethod) {
        super(identifier);
        setParentEvent(new TestMethodNode(testMethod));
    }

    public TestMethodConfigurationNode(ConfigurationReport configuration, String identifier, Method testMethod) {
        super(configuration, identifier);
        setParentEvent(new TestMethodNode(testMethod));
    }
}
