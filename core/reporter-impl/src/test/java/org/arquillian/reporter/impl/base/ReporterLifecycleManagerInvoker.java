package org.arquillian.reporter.impl.base;

import java.io.IOException;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.impl.ReporterLifecycleManager;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.jboss.arquillian.core.spi.ServiceLoader;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManagerInvoker {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ReporterLifecycleManager> reporterLifecycleManager;

    @Inject
    private Instance<Injector> injector;

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    public void observeAndInvokeFirstEvent(@Observes InitiateReporterEvent event) {
        ReporterLifecycleManager lifecycleManager = event.getReporterLifecycleManager();
        injector.get().inject(lifecycleManager);
        reporterLifecycleManager.set(lifecycleManager);

        lifecycleManager.observeFirstEvent(new ManagerStarted());
    }

    public void observeAndInvokeEventsForAllSections(@Observes SectionEvent event) {
        reporterLifecycleManager.get().observeEventsForAllSections(event);
    }

    public void observeAndInvokeLastEvent(@Observes ManagerStopping event) throws IOException {
        reporterLifecycleManager.get().observeLastEvent(event);
    }
}
