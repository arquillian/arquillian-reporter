package org.arquillian.reporter.impl.event;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderRegistryDelegate;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.event.TestMethodFailureSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.base.AbstractReporterTestBase;
import org.arquillian.reporter.impl.utils.dummy.SectionUnderTestMethodConfigSection;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Parameterized.class)
public class FiringSectionEventsUsingReporterTest extends AbstractReporterTestBase {

    @Inject
    private Event<SectionEvent> sectionEvent;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { ExecutionSection.class, ExecutionReport.class },
            { ExecutionSection.class, BasicReport.class },
            { TestSuiteSection.class, TestSuiteReport.class },
            { TestSuiteSection.class, BasicReport.class },
            { TestSuiteConfigurationSection.class, ConfigurationReport.class },
            { TestSuiteConfigurationSection.class, BasicReport.class },
            { TestClassSection.class, TestClassReport.class },
            { TestClassSection.class, BasicReport.class },
            { TestClassConfigurationSection.class, ConfigurationReport.class },
            { TestClassConfigurationSection.class, BasicReport.class },
            { TestMethodSection.class, TestMethodReport.class },
            { TestMethodSection.class, BasicReport.class },
            { TestMethodConfigurationSection.class, ConfigurationReport.class },
            { TestMethodConfigurationSection.class, BasicReport.class },
            { TestMethodFailureSection.class, FailureReport.class },
            { TestMethodFailureSection.class, BasicReport.class },
            { SectionUnderTestMethodConfigSection.class, BasicReport.class }
        });
    }

    private Class<SectionEvent> sectionClass;
    private Class<AbstractReport> reportClass;

    public FiringSectionEventsUsingReporterTest(Class<SectionEvent> sectionClass, Class<AbstractReport> reportClass) {
        this.sectionClass = sectionClass;
        this.reportClass = reportClass;
    }

    @Test
    public void testWhenTestSuiteReportIsFiredUsingBuilderAnObserverShouldBeInvoked()
        throws IllegalAccessException, InstantiationException, IOException {

        SectionEvent sectionToFire = sectionClass.newInstance();
        AbstractReport payload = reportClass.newInstance();
        payload.setName(new UnknownStringKey("Basic report"));

        Reporter
            .createReport(payload)
            .addEntry("dummy entry")
            .inSection(sectionToFire)
            .fire(sectionEvent);

        assertEventFired(sectionClass, 1);
        verifyInReporterLifecycleManager().wasCalled(1).observeFirstEvent(any(ManagerStarted.class));
        verifyInReporterLifecycleManager().wasCalled(1).observeEventsForAllSections(any(sectionClass));
        verifyInReporterLifecycleManager().wasCalled(0).observeLastEvent(any(ManagerStopping.class));
    }

    @Override
    protected void addAdditionalExtensions(List<Class<?>> extensions) {
    }

    @Override
    protected void addReporterStringKeys(List<StringKey> stringKeys) {
    }

    @Override
    protected void registerBuilders(BuilderRegistryDelegate builderRegistry) {
    }
}
