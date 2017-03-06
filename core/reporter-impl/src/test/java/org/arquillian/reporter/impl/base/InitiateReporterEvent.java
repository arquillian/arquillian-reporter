package org.arquillian.reporter.impl.base;

import org.arquillian.reporter.impl.ReporterLifecycleManager;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class InitiateReporterEvent {

    private ReporterLifecycleManager reporterLifecycleManager;

    public InitiateReporterEvent(ReporterLifecycleManager reporterLifecycleManager) {
        this.reporterLifecycleManager = reporterLifecycleManager;
    }

    public ReporterLifecycleManager getReporterLifecycleManager() {
        return reporterLifecycleManager;
    }

    public void setReporterLifecycleManager(ReporterLifecycleManager reporterLifecycleManager) {
        this.reporterLifecycleManager = reporterLifecycleManager;
    }
}
