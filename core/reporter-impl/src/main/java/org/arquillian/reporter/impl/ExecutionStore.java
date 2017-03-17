package org.arquillian.reporter.impl;

/**
 * Store containing all information about the whole test execution
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionStore {

    private final ExecutionReport executionReport;
    private final ExecutionSection executionSection;
    private final SectionTree sectionTree;

    public ExecutionStore() {
        executionReport = new ExecutionReport();
        this.executionSection = new ExecutionSection(executionReport);
        sectionTree = new SectionTree(executionSection.identifyYourself(), executionReport, ExecutionReport.class);
    }

    public SectionTree getSectionTree() {
        return sectionTree;
    }

    public ExecutionSection getExecutionSection() {
        return executionSection;
    }

    public ExecutionReport getExecutionReport() {
        return executionReport;
    }
}
