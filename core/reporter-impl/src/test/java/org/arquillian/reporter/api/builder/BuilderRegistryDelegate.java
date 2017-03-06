package org.arquillian.reporter.api.builder;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BuilderRegistryDelegate {

    public BuilderRegistryDelegate(){
    }

    /**
     * Returns implementation class of the given {@link Builder} interface
     *
     * @param builder A {@link Builder} interface an implementation class we are looking for
     * @param <T>     Builder type
     * @return An implementation class of the given {@link Builder} interface
     */
    public <T extends Builder> Class<T> getImplementationForBuilder(Class<T> builder) {
        return BuilderRegistry.getImplementationForBuilder(builder);
    }

    /**
     * Adds implementation class of some {@link Builder} interface to the registry
     *
     * @param interfaceClass A {@link Builder} interface
     * @param implClass     An implementation class of the given {@link Builder} interface
     * @param <T>           Builder type
     */
    public <T extends Builder> void addServiceToBuilderRegistry(Class<T> interfaceClass, Class<? extends T> implClass) {
        BuilderRegistry.addServiceToBuilderRegistry(interfaceClass, implClass);
    }
}
