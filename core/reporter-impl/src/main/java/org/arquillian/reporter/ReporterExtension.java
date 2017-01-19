package org.arquillian.reporter;

import org.arquillian.reporter.api.model.ReporterCoreKeys;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.impl.ReporterLifecycleManager;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterExtension implements LoadableExtension {

    public void register(ExtensionBuilder builder) {
        builder.observer(ReporterLifecycleManager.class);
        builder.service(StringKey.class, ReporterCoreKeys.class);
    }
}
