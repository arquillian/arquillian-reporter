package org.arquillian.environment.reporter;

import org.arquillian.environment.reporter.impl.ArquillianEnvironmentKeys;
import org.arquillian.environment.reporter.impl.ArquillianEnvironmentReporterLifecycleManager;
import org.arquillian.reporter.api.model.StringKey;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class ArquillianEnvironmentReporterExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder
                .observer(ArquillianEnvironmentReporterLifecycleManager.class)
                .service(StringKey.class, ArquillianEnvironmentKeys.class);
    }
}
