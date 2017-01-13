package org.arquillian.core.reporter;

import org.arquillian.core.reporter.impl.ArquillianCoreReporterLifecycleManager;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreReporterExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(ArquillianCoreReporterLifecycleManager.class);
    }
}
