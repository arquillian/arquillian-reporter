package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationNode extends ReportNodeEvent<ConfigurationReport, TestSuiteNode> {

    public TestSuiteConfigurationNode(ConfigurationReport sectionReport) {
        super(sectionReport);
    }

    public TestSuiteConfigurationNode(String identifier) {
        super(identifier);
    }

    public TestSuiteConfigurationNode(ConfigurationReport section, String identifier) {
        super(section, identifier);
    }

    public TestSuiteConfigurationNode(String identifier, String testSuiteIdentifier) {
        super(identifier);
        setParentEvent(new TestSuiteNode(testSuiteIdentifier));
    }

    public TestSuiteConfigurationNode(ConfigurationReport section, String identifier, String testSuiteIdentifier) {
        super(section, identifier);
        setParentEvent(new TestSuiteNode(testSuiteIdentifier));
    }
}
