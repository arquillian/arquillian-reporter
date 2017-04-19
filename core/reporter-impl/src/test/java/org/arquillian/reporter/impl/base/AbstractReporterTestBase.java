package org.arquillian.reporter.impl.base;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.builder.BuilderRegistryDelegate;
import org.arquillian.reporter.api.builder.entry.TableBuilder;
import org.arquillian.reporter.api.builder.report.BasicReportBuilder;
import org.arquillian.reporter.api.builder.report.ConfigurationReportBuilder;
import org.arquillian.reporter.api.builder.report.FailureReportBuilder;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.builder.report.ReportInSectionBuilder;
import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.model.ReporterCoreKey;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.impl.ReporterLifecycleManager;
import org.arquillian.reporter.impl.builder.entry.TableBuilderImpl;
import org.arquillian.reporter.impl.builder.report.BasicReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.ConfigurationReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.FailureReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.ReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.ReportInSectionBuilderImpl;
import org.arquillian.reporter.impl.builder.report.TestClassReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.TestMethodReportBuilderImpl;
import org.arquillian.reporter.impl.builder.report.TestSuiteReportBuilderImpl;
import org.arquillian.reporter.impl.model.TestExtensionStringKey;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * An abstract base class for reporter tests. Initializes/registers StringKeys, Builders, a serviceLoader in an ApplicationScope,
 * and {@link ReporterLifecycleManager} that is spied, so it can be verified using method {@link #verifyInReporterLifecycleManager()}
 * if the have been the observers invoked.
 *
 * <p>
 *     In case you want to register any additional class/service/observer, override any of the following methods:
 *     <ul>
 *         <li>{@link #addReporterStringKeys(List)} to register additional StringKey implementation (except for the core ones - they are already registered)</li>
 *         <li>{@link #registerBuilders(BuilderRegistryDelegate)} to register builder interface and its implementation (except for the core ones - they are already registered)</li>
 *         <li>{@link #addAdditionalExtensions(List)} for any other arquillian extension services (observer, enricher, ...)</li>
 *     </ul>
 * </p>
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractReporterTestBase extends AbstractTestTestBase {

    @Mock
    private ServiceLoader serviceLoader;

    @Spy
    private ReporterLifecycleManager lifecycleManager;

    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(ReporterLifecycleManagerInvoker.class);
        addAdditionalExtensions(extensions);

    }

    @Before
    public final void initializeReporterAndRegisterServices() {
        initMocks(this);

        initiateStringKeys();
        bind(ApplicationScoped.class, ServiceLoader.class, serviceLoader);

        fire(new InitiateReporterEvent(lifecycleManager));
        registerBuilders();
    }

    protected ReporterLifecycleManagerVerifier verifyInReporterLifecycleManager() {
        return new ReporterLifecycleManagerVerifier(lifecycleManager);
    }

    private void registerBuilders() {
        BuilderRegistryDelegate builderRegistry = new BuilderRegistryDelegate();
        builderRegistry.addServiceToBuilderRegistry(ReportBuilder.class, ReportBuilderImpl.class);
        builderRegistry.addServiceToBuilderRegistry(BasicReportBuilder.class, BasicReportBuilderImpl.class);
        builderRegistry
            .addServiceToBuilderRegistry(ConfigurationReportBuilder.class, ConfigurationReportBuilderImpl.class);
        builderRegistry.addServiceToBuilderRegistry(FailureReportBuilder.class, FailureReportBuilderImpl.class);

        builderRegistry.addServiceToBuilderRegistry(ReportInSectionBuilder.class, ReportInSectionBuilderImpl.class);
        builderRegistry.addServiceToBuilderRegistry(TableBuilder.class, TableBuilderImpl.class);
        builderRegistry.addServiceToBuilderRegistry(TestClassReportBuilder.class, TestClassReportBuilderImpl.class);
        builderRegistry.addServiceToBuilderRegistry(TestMethodReportBuilder.class, TestMethodReportBuilderImpl.class);
        builderRegistry.addServiceToBuilderRegistry(TestSuiteReportBuilder.class, TestSuiteReportBuilderImpl.class);

        registerBuilders(builderRegistry);
    }

    private void initiateStringKeys() {
        ArrayList<StringKey> stringKeys = new ArrayList<>();
        stringKeys.add(new ReporterCoreKey());
        stringKeys.add(new TestExtensionStringKey());
        addReporterStringKeys(stringKeys);
        when(serviceLoader.all(StringKey.class)).thenReturn(stringKeys);
    }

    /**
     * Add any type of arquillian extension (observer, enricher, service, ...). Don't add the {@link ReporterLifecycleManager}
     * as it is already managed. For builders use the {@link #registerBuilders(BuilderRegistryDelegate)} method and for
     * StringKeys use {@link #addReporterStringKeys(List)} method.
     *
     * @param extensions Set of extensions
     */
    protected void addAdditionalExtensions(List<Class<?>> extensions){
    }

    /**
     * Add your implementation of {@link StringKey} you want to register. You don't have to add {@link ReporterCoreKey}
     * as it is already there.
     *
     * @param stringKeys List of {@link StringKey} implementations
     */
    protected void addReporterStringKeys(List<StringKey> stringKeys){
    }

    /**
     * Register pair of {@link Builder} interfaces and its implementations. You don't have to add any builder from the
     * reporter-core as all of them are already registered.
     *
     * @param builderRegistry Builder registry
     */
    protected void registerBuilders(BuilderRegistryDelegate builderRegistry){
    }

}
