package org.arquillian.reporter.api.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry that keeps builder interfaces and their implementations.
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
class BuilderRegistry {

    private static final Map<Class<? extends Builder>, Class<? extends Builder>> builderRegistry = new HashMap<>();

    private BuilderRegistry() {
    }

    /**
     * Returns implementation class of the given {@link Builder} interface
     *
     * @param builder A {@link Builder} interface an implementation class we are looking for
     * @param <T>     Builder type
     * @return An implementation class of the given {@link Builder} interface
     */
    static <T extends Builder> Class<T> getImplementationForBuilder(Class<T> builder) {
        synchronized (builderRegistry) {
            return (Class<T>) builderRegistry.get(builder);
        }
    }

    /**
     * Adds implementation class of some {@link Builder} interface to the registry
     *
     * @param interfaceName A {@link Builder} interface
     * @param className     An implementation class of the given {@link Builder} interface
     * @param <T>           Builder type
     */
    static <T> void addServiceToBuilderRegistry(Class<T> interfaceName, Class<? extends T> className) {
        synchronized (builderRegistry) {
            builderRegistry.put((Class<? extends Builder>) interfaceName, (Class<? extends Builder>) className);
        }
    }
}
