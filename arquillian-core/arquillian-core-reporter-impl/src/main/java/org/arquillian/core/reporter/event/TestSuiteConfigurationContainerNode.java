package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.event.TestSuiteConfigurationNode;
import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationContainerNode extends ReportNodeEvent<SectionReport, TestSuiteConfigurationNode> {

    public TestSuiteConfigurationContainerNode(){
    }

    public TestSuiteConfigurationContainerNode(String identifier) {
        super(identifier);
    }

    public TestSuiteConfigurationContainerNode(SectionReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }

    // todo support multiple test suites
}
