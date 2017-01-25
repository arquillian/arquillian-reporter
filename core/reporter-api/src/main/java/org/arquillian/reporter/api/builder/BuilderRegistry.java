package org.arquillian.reporter.api.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
class BuilderRegistry {

    private static final Map<Class<? extends Builder>, Class<? extends Builder>> builderRegistry = new HashMap<>();

    private BuilderRegistry() {
    }

    static <T extends Builder> Class<T> getImplementationForBuilder(Class<T> builder) {
        synchronized (builderRegistry) {
            return (Class<T>) builderRegistry.get(builder);
        }
    }

    static <T> void addServiceToBuilderRegistry(Class<T> interfaceName, Class<? extends T> className){
        synchronized (builderRegistry) {
            builderRegistry.put((Class<? extends Builder>) interfaceName,(Class<? extends Builder>) className);
        }
    }
}
