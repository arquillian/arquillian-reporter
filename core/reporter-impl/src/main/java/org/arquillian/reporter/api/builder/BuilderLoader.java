package org.arquillian.reporter.api.builder;

import java.util.Collection;
import java.util.Collections;

import org.jboss.arquillian.core.impl.loadable.JavaSPIExtensionLoader;
import org.jboss.arquillian.core.impl.loadable.LoadableExtensionLoader;
import org.jboss.arquillian.core.spi.ExtensionLoader;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.core.spi.context.Context;

/**
 * Loads implementation of {@link Builder} implementations registered using Arquillian SPI
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BuilderLoader {

    /**
     * Loads implementation of {@link Builder} implementations registered using Arquillian SPI
     */
    public static void load() {
        ExtensionLoader extensionLoader = locateExtensionLoader(new JavaSPIExtensionLoader());
        Collection<LoadableExtension> extensions = extensionLoader.load();

        for (LoadableExtension extension : extensions) {
            extension.register(new LoadableExtension.ExtensionBuilder() {
                @Override
                public <T> LoadableExtension.ExtensionBuilder service(Class<T> service, Class<? extends T> impl) {
                    if (Builder.class.isAssignableFrom(service)) {
                        BuilderRegistry.addServiceToBuilderRegistry(service, impl);
                    }
                    return this;
                }

                @Override
                public <T> LoadableExtension.ExtensionBuilder override(Class<T> aClass, Class<? extends T> aClass1,
                    Class<? extends T> aClass2) {
                    return this;
                }

                @Override
                public LoadableExtension.ExtensionBuilder observer(Class<?> aClass) {
                    return this;
                }

                @Override
                public LoadableExtension.ExtensionBuilder context(Class<? extends Context> aClass) {
                    return this;
                }

            });
        }
    }

    /**
     * Some environments need to handle their own ExtensionLoading and can't rely on Java's META-INF/services approach.
     *
     * @return configured ExtensionLoader if found or defaults to JavaSPIServiceLoader
     */
    private static ExtensionLoader locateExtensionLoader(JavaSPIExtensionLoader serviceLoader) {
        Collection<ExtensionLoader> loaders = Collections.emptyList();
        if (SecurityActions.getThreadContextClassLoader() != null) {
            loaders = serviceLoader.all(SecurityActions.getThreadContextClassLoader(), ExtensionLoader.class);
        }
        if (loaders.size() == 0) {
            loaders = serviceLoader.all(LoadableExtensionLoader.class.getClassLoader(), ExtensionLoader.class);
        }
        if (loaders.size() == 1) {
            return loaders.iterator().next();
        }
        return serviceLoader;
    }
}
