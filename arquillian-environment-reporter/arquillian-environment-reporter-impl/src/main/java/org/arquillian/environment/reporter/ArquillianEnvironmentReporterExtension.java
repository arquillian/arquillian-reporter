package org.arquillian.environment.reporter;

import org.arquillian.environment.reporter.impl.EnvironmentKey;
import org.arquillian.environment.reporter.impl.EnvironmentReportCreator;
import org.arquillian.reporter.api.model.StringKey;
import org.jboss.arquillian.core.spi.LoadableExtension;

class ArquillianEnvironmentReporterExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder
                .observer(EnvironmentReportCreator.class)
                .service(StringKey.class, EnvironmentKey.class);
    }
}
