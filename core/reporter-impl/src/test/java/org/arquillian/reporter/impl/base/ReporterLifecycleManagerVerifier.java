package org.arquillian.reporter.impl.base;

import org.arquillian.reporter.impl.ReporterLifecycleManager;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManagerVerifier {

    private ReporterLifecycleManager lifecycleManager;

    public ReporterLifecycleManagerVerifier(ReporterLifecycleManager lifecycleManager) {
        this.lifecycleManager = lifecycleManager;
    }

    public ReporterLifecycleManager wasCalled(int numberOfCalls){
        return verify(lifecycleManager, times(numberOfCalls));
    }

}
