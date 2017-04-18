package org.arquillian.reporter;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.impl.model.TestExtensionStringKey;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class TestReporterExtension implements LoadableExtension {
    @Override
    public void register(LoadableExtension.ExtensionBuilder extensionBuilder) {
        /*
        extensionBuilder.observer(ReporterLifecycleManager.class);
        extensionBuilder.observer(ReporterConfigurator.class);
     */
        extensionBuilder.service(StringKey.class, TestExtensionStringKey.class);

    }
}
