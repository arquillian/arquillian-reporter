package org.arquillian.core.reporter.impl;

import java.util.Map;

import org.arquillian.core.reporter.event.ContainerReportEvent;
import org.arquillian.core.reporter.event.DeploymentReport;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportTestClassEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteConfigurationEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteEvent;
import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.TestClassReport;
import org.arquillian.reporter.api.model.TestMethodReport;
import org.arquillian.reporter.api.model.TestSuiteReport;
import org.arquillian.reporter.api.utils.Report;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeTestLifecycleEvent;

import static org.arquillian.reporter.api.utils.Report.section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreReporterLifecycleManager {

    private static String TEST_SUITE_NAME = "Arquillian Test Suite";


    @Inject
    private Instance<ExecutionReport> report;

    @Inject
    private Event<ReportEvent> reportEvent;

    public void testCaseStart(@Observes(precedence = Integer.MAX_VALUE) BeforeSuite managerProcessing) {
        TestSuiteReport testSuiteReport = new TestSuiteReport(TEST_SUITE_NAME);

        reportEvent.fire(new ReportTestSuiteEvent(testSuiteReport, TEST_SUITE_NAME));
    }

    public void observeContainer(@Observes Container event) {
        Map<String, String> containerProperties = event.getContainerConfiguration().getContainerProperties();

        SectionReport containers = Report.section("Containers").build();
        reportEvent.fire(new ReportTestSuiteConfigurationEvent(containers, "containers"));

        SectionReport container =
            Report.section(event.getName())
                .addSection(
                    section("Configuration")
                        .feedKeyValueListFromMap(containerProperties))
                .build();

        String containerId = event.getContainerConfiguration().isDefault() ? "_DEFAULT_" : event.getName();
        ContainerReportEvent containerReportEvent = new ContainerReportEvent(container, containerId);
        containerReportEvent.setParentEvent(new ReportTestSuiteConfigurationEvent("containers"));
        reportEvent.fire(containerReportEvent);
    }

    public void observeBeforeDeploy(@Observes BeforeDeploy event) {
        DeploymentDescription description = event.getDeployment();

        SectionReport deploymentReport = Report
            .section(description.getName())
            .addKeyValueEntry("Archive name", description.getArchive().getName())
            .addKeyValueEntry("Order", description.getOrder())
            .addKeyValueEntry("Protocol", description.getProtocol().getName())
            .build();

        String targetContainer = description.getTarget().getName();

        DeploymentReport deploymentReportEvent = new DeploymentReport(deploymentReport, description.getName());
        deploymentReportEvent.setParentEvent(new ContainerReportEvent(targetContainer));
        this.reportEvent.fire(deploymentReportEvent);

    }

    public void observeBeforeClass(@Observes(precedence = Integer.MAX_VALUE) BeforeClass event) {
        TestClassReport testClassReport = new TestClassReport(event.getTestClass().getName());
        boolean runAsClient = event.getTestClass().isAnnotationPresent(RunAsClient.class);
        Report
            .modifySection(testClassReport.getConfiguration())
            .addKeyValueEntry("Runs as client", runAsClient);

        String reportMessage = ReportMessageParser.parseTestClassReportMessage(event.getTestClass().getJavaClass());
        Report.modifySection(testClassReport).addKeyValueEntry("Report message", reportMessage);

        ReportTestClassEvent reportTestClassEvent =
            new ReportTestClassEvent(testClassReport, event.getTestClass().getName()).inTestSuite("TEST_SUITE_NAME");
        reportEvent.fire(reportTestClassEvent);
    }

    public void observeBeforeTest(@Observes(precedence = Integer.MAX_VALUE) BeforeTestLifecycleEvent event) {

        TestMethodReport testMethodReport = new TestMethodReport(event.getTestMethod().getName());

        Integer c = lifecycleCountRegister.get(event.getTestMethod());
        int count = (c != null ? c.intValue() : 0);

        if (count == 0) {

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
