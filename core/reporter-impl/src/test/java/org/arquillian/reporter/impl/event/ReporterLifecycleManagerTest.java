package org.arquillian.reporter.impl.event;

import org.arquillian.reporter.config.ReporterConfiguration;
import org.arquillian.reporter.impl.ExecutionStore;
import org.arquillian.reporter.impl.base.AbstractReporterTestBase;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.junit.Test;

import java.io.IOException;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.mockito.ArgumentMatchers.any;
/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManagerTest extends AbstractReporterTestBase {

    @Inject
    private Instance<ExecutionStore> executionStore;

    @Test
    public void testWhenManagerIsStartedExecutionReportShouldBeInstantiatedAndFirstObserverInvoked()
        throws IOException {

        assertThatExecutionReport(executionStore.get().getExecutionReport())
            .as("The execution report should be already instantiated")
            .isNotNull();

        assertThatSectionTree(executionStore.get().getSectionTree())
            .as("The execution report should contain instantiated section tree")
            .isNotNull()
            .hasNumberOfSubTrees(0);

        verifyInReporterLifecycleManager().wasCalled(1).observeFirstEvent(any(ManagerStarted.class));
        verifyInReporterLifecycleManager().wasCalled(0).observeEventsForAllSections(any());
        verifyInReporterLifecycleManager().wasCalled(0).observeLastEvent(any(ManagerStopping.class), any(ReporterConfiguration.class));
    }

    @Test
    public void testWhenManagerIsStoppedLastObserverShouldBeInvoked() throws IOException {
        fire(new ManagerStopping());

        verifyInReporterLifecycleManager().wasCalled(1).observeFirstEvent(any(ManagerStarted.class));
        verifyInReporterLifecycleManager().wasCalled(0).observeEventsForAllSections(any());
        verifyInReporterLifecycleManager().wasCalled(1).observeLastEvent(any(ManagerStopping.class), any(ReporterConfiguration.class));
    }
}
