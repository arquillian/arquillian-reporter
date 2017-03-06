package org.arquillian.reporter.impl.event;

import java.io.IOException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderRegistryDelegate;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.base.AbstractReporterTestBase;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManagerTest extends AbstractReporterTestBase {

    @Inject
    private Instance<ExecutionReport> executionReport;

    @Test
    public void testWhenManagerIsStartedExecutionReportShouldBeInstantiatedAndFirstObserverInvoked()
        throws IOException {

        assertThatExecutionReport(executionReport.get())
            .as("The execution report should be already instantiated")
            .isNotNull()
            .sectionTree()
            .as("The execution report should contain instantiated section tree")
            .isNotNull()
            .hasNumberOfSubTrees(0);

        verifyInReporterLifecycleManager().wasCalled(1).observeFirstEvent(any(ManagerStarted.class));
        verifyInReporterLifecycleManager().wasCalled(0).observeEventsForAllSections(any());
        verifyInReporterLifecycleManager().wasCalled(0).observeLastEvent(any(ManagerStopping.class));
    }

    @Test
    public void testWhenManagerIsStoppedLastObserverShouldBeInvoked() throws IOException {
        fire(new ManagerStopping());

        verifyInReporterLifecycleManager().wasCalled(1).observeFirstEvent(any(ManagerStarted.class));
        verifyInReporterLifecycleManager().wasCalled(0).observeEventsForAllSections(any());
        verifyInReporterLifecycleManager().wasCalled(1).observeLastEvent(any(ManagerStopping.class));
    }


    @Override
    protected void addAdditionalExtensions(List<Class<?>> extensions) {
    }

    @Override
    protected void addReporterStringKeys(List<StringKey> stringKeys) {
    }

    @Override
    protected void registerBuilders(BuilderRegistryDelegate builderRegistryDelegate) {
    }
}
