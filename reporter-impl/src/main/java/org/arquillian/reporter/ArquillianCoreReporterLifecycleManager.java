package org.arquillian.reporter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteConfigurationEvent;
import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.TestClassReport;
import org.arquillian.reporter.api.model.TestMethodReport;
import org.arquillian.reporter.api.model.TestSuiteReport;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.utils.SectionBuilder;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.event.ManagerProcessing;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeTestLifecycleEvent;

import static org.arquillian.reporter.api.utils.SectionBuilder.attachTo;
import static org.arquillian.reporter.api.utils.SectionBuilder.create;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreReporterLifecycleManager {

    private static String CONTAINERS_REPORT_IDENTIFIER =
        ArquillianCoreReporterLifecycleManager.class.toString().concat("_containers");


    private static String DEPLOYMENTS_REPORT_IDENTIFIER =
        ArquillianCoreReporterLifecycleManager.class.toString().concat("_deployments");

    @Inject
    private Instance<ExecutionReport> report;

    @Inject
    private Event<ReportEvent> reportEvent;

    public void testCaseStart(@Observes(precedence = Integer.MAX_VALUE) ManagerProcessing managerProcessing) {
        TestSuiteReport testSuiteReport = new TestSuiteReport();

        report.get().setTestSuiteReport(testSuiteReport);
    }

    public void observeContainer(@Observes Container event) {
        Map<String, String> containerProperties = event.getContainerConfiguration().getContainerProperties();

        Section containers = SectionBuilder
            .create("Containers")
            .withIdentifier(CONTAINERS_REPORT_IDENTIFIER)
            .addSection(create(event.getName())
                               .withIdentifier(CONTAINERS_REPORT_IDENTIFIER.concat("_" + event.getName()))
                               .addSection(create("Configuration")
                                                  .feedKeyValueListFromMap(containerProperties)))
            .build();

        reportEvent.fire(new ReportTestSuiteConfigurationEvent(containers));
    }

    public void observeBeforeDeploy(@Observes BeforeDeploy event) {
        DeploymentDescription description = event.getDeployment();

        Section deploymentReport = SectionBuilder
            .create(description.getName())
            .addKeyValueEntry("Archive name", description.getArchive().getName())
            .addKeyValueEntry("Order", description.getOrder())
            .addKeyValueEntry("Protocol", description.getProtocol().getName())
            .build();

        String targetContainer = description.getTarget().getName();
        attachTo(CONTAINERS_REPORT_IDENTIFIER,
                 attachTo(CONTAINERS_REPORT_IDENTIFIER + "_" + targetContainer, deploymentReport));


//        boolean reported = false;
//
//        for (ContainerReport containerReport : reporter.get().getLastTestSuiteReport().getContainerReports()) {
//            if (containerReport.getQualifier().equals(deploymentReport.getTarget())) {
//                containerReport.getDeploymentReports().add(deploymentReport);
//                reported = true;
//                break;
//            }
//        }
//
//        if (!reported) {
//            if (reporter.get().getLastTestSuiteReport().getContainerReports().size() == 1) {
//                reporter.get().getLastTestSuiteReport().getContainerReports().get(0).getDeploymentReports()
//                    .add(deploymentReport);
//            }
//        }

    }

    public void observeBeforeClass(@Observes(precedence = Integer.MAX_VALUE) BeforeClass event) {
        TestClassReport testClassReport = new TestClassReport(event.getTestClass().getName());
        testClassReport.getConfiguration().addEntry(new KeyValueEntry("Runs as client", event.getTestClass().isAnnotationPresent(RunAsClient.class)));

        testClassReport
            .setReportMessage(ReportMessageParser.parseTestClassReportMessage(event.getTestClass().getJavaClass()));

        reporter.get().getLastTestSuiteReport().getTestClassReports().add(testClassReport);
        reporter.get().setTestClassReport(testClassReport);
    }

    public void observeBeforeTest(@Observes(precedence = Integer.MAX_VALUE) BeforeTestLifecycleEvent event) {

        Integer c = lifecycleCountRegister.get(event.getTestMethod());
        int count = (c != null ? c.intValue() : 0);

        if (count == 0) {
            TestMethodReport testMethodReport = new TestMethodReport();
            testMethodReport.setName(event.getTestMethod().getName());

            if (event.getTestMethod().isAnnotationPresent(OperateOnDeployment.class)) {
                OperateOnDeployment ood = event.getTestMethod().getAnnotation(OperateOnDeployment.class);
                testMethodReport.setOperateOnDeployment(ood.value());
            } else {
                testMethodReport.setOperateOnDeployment("_DEFAULT_");
            }

            testMethodReport.setRunAsClient(event.getTestMethod().isAnnotationPresent(RunAsClient.class));

            reporter.get().getLastTestClassReport().getTestMethodReports().add(testMethodReport);
            reporter.get().setTestMethodReport(testMethodReport);
        }

        lifecycleCountRegister.put(event.getTestMethod(), ++count);
    }

    public void observeAfterTest(@Observes(precedence = Integer.MIN_VALUE) AfterTestLifecycleEvent event,
        TestResult result) {

        int count = lifecycleCountRegister.get(event.getTestMethod());

        lifecycleCountRegister.put(event.getTestMethod(), --count);

        if (lifecycleCountRegister.get(event.getTestMethod()) == 0) {
            TestMethodReport testMethodReport = reporter.get().getLastTestMethodReport();

            testMethodReport.setStatus(result.getStatus());
            testMethodReport.setDuration(result.getEnd() - result.getStart());
            testMethodReport.setReportMessage(ReportMessageParser.parseTestReportMessage(event.getTestMethod()));

            if (result.getStatus() == Status.FAILED && result.getThrowable() != null) {
                testMethodReport.setException(getStackTrace(result.getThrowable()));
            }

            inTestResourceReportEvent.fire(new InTestResourceReport());

            reporter.get().setReporterCursor(new ReporterCursor(reporter.get().getLastTestClassReport()));

            report(event, descriptor.get());

            lifecycleCountRegister.remove(event.getTestMethod());
        }
    }

    public void observeAfterClass(@Observes(precedence = Integer.MIN_VALUE) AfterClass event) {

        reporter.get().setReporterCursor(new ReporterCursor(reporter.get().getLastTestSuiteReport()));

        report(event, descriptor.get());
    }

    public void observeAfterSuite(@Observes(precedence = Integer.MIN_VALUE) AfterSuite event) {

        reporter.get().getLastTestClassReport().setStop(new Date(System.currentTimeMillis()));
        reporter.get().getLastTestSuiteReport().setStop(new Date(System.currentTimeMillis()));

        exportReportEvent.fire(new ExportReport(reporter.get().getReport()));
    }

    public void observeReportEvent(@Observes PropertyReportEvent event) {
        reporter.get().getReporterCursor().getCursor().getPropertyEntries().add(event.getPropertyEntry());
    }

    private void report(org.jboss.arquillian.core.spi.event.Event event, ArquillianDescriptor descriptor) {
        if (shouldReport(event, configuration.get().getReportAfterEvery())) {
            List<ExtensionReport> extensionReports = reporter.get().getReport().getExtensionReports();
            if (extensionReports.isEmpty()) {
                extensionReports.addAll(getExtensionReports(descriptor));
            }

            reporter.get().getLastTestClassReport().setStop(new Date(System.currentTimeMillis()));
            reporter.get().getLastTestSuiteReport().setStop(new Date(System.currentTimeMillis()));

            exportReportEvent.fire(new ExportReport(reporter.get().getReport()));
        }
    }

    private boolean shouldReport(org.jboss.arquillian.core.spi.event.Event event, String frequency) {
        if (event instanceof AfterClass && ReportFrequency.CLASS.toString().equals(frequency)
            || (event instanceof After && ReportFrequency.METHOD.toString().equals(frequency))) {
            return true;
        }
        return false;
    }

    private Collection<? extends ExtensionReport> getExtensionReports(ArquillianDescriptor descriptor) {
        List<ExtensionReport> extensionReports = new ArrayList<ExtensionReport>();

        for (ExtensionDef extensionDef : descriptor.getExtensions()) {
            ExtensionReport extensionReport = new ExtensionReport();
            extensionReport.setQualifier(extensionDef.getExtensionName());
            extensionReport.setConfiguration(extensionDef.getExtensionProperties());
            extensionReports.add(extensionReport);
        }
        return extensionReports;
    }

    private static String getStackTrace(Throwable aThrowable) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        sb.append(aThrowable.toString());
        sb.append(newLine);

        for (StackTraceElement element : aThrowable.getStackTrace()) {
            sb.append(element);
            sb.append(newLine);
        }
        return sb.toString();
    }

    private static final class ReportMessageParser {

        private ReportMessageParser() {
            throw new UnsupportedOperationException("no instantiation");
        }

        public static String parseTestReportMessage(Method testMethod) {
            return getReportMessage(testMethod.getAnnotations());
        }

        public static String parseTestClassReportMessage(Class<?> testClass) {
            return getReportMessage(testClass.getAnnotations());
        }

        private static String getReportMessage(Annotation[] annotations) {
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().isAssignableFrom(ReportMessage.class)) {
                        return ((ReportMessage) annotation).value();
                    }
                }
            }

            return null;
        }
    }

}
