package org.arquillian.core.reporter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.arquillian.core.reporter.event.ContainerReportEventTestSuiteConfiguration;
import org.arquillian.core.reporter.event.DeploymentReportTestSuiteConfiguration;
import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportEventTestClass;
import org.arquillian.reporter.api.event.ReportEventTestMethod;
import org.arquillian.reporter.api.event.ReportEventTestSuite;
import org.arquillian.reporter.api.event.ReportEventTestSuiteConfiguration;
import org.arquillian.reporter.api.model.TestClassReport;
import org.arquillian.reporter.api.model.TestMethodReport;
import org.arquillian.reporter.api.model.TestSuiteReport;
import org.arquillian.reporter.api.utils.Reporter;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreReporterLifecycleManager {

    private static String TEST_SUITE_NAME = "Arquillian Test Suite";

    @Inject
    private Event<ReportEvent> reportEvent;

    public void startTestSuite(@Observes(precedence = Integer.MAX_VALUE) BeforeSuite managerProcessing) {
        TestSuiteReport testSuiteReport = new TestSuiteReport(TEST_SUITE_NAME);

        reportEvent.fire(new ReportEventTestSuite(testSuiteReport, TEST_SUITE_NAME));
    }

    public void reportContainer(@Observes Container event) {
        Map<String, String> containerProperties = event.getContainerConfiguration().getContainerProperties();

        ReportEventTestSuiteConfiguration containers = new ReportEventTestSuiteConfiguration("containers");
        Reporter.section("Containers").fireUsingEvent(containers).fire(reportEvent);

        String containerId = event.getContainerConfiguration().isDefault() ? "_DEFAULT_" : event.getName();

        Reporter.section("Container")
                .addKeyValueEntry("Container name", event.getName())
                .addSection(
                    Reporter.section("Configuration")
                        .feedKeyValueListFromMap(containerProperties))
            .fireUsingEvent(new ContainerReportEventTestSuiteConfiguration(containerId)
                                .setParentEvent(containers)).fire(reportEvent);
    }

    public void reportDeployment(@Observes BeforeDeploy event) {
        DeploymentDescription description = event.getDeployment();
        String targetContainer = description.getTarget().getName();

        Reporter
            .section("Deployment")
            .addKeyValueEntry("Deployment name", description.getName())
            .addKeyValueEntry("Archive name", description.getArchive().getName())
            .addKeyValueEntry("Order", description.getOrder())
            .addKeyValueEntry("Protocol", description.getProtocol().getName())
            .fireUsingEvent(new DeploymentReportTestSuiteConfiguration(description.getName(), targetContainer))
            .fire(reportEvent);
    }

    public void startTestClass(@Observes(precedence = Integer.MAX_VALUE) BeforeClass event) {
        TestClass testClass = event.getTestClass();
        boolean runAsClient = event.getTestClass().isAnnotationPresent(RunAsClient.class);

        TestClassReport testClassReport = new TestClassReport(testClass.getName());
        Reporter
            .section(testClassReport.getConfiguration())
            .addKeyValueEntry("Runs as client", runAsClient);

        String reportMessage = ReportMessageParser.parseTestClassReportMessage(event.getTestClass().getJavaClass());
        Reporter
            .section(testClassReport)
            .addKeyValueEntry("Report message", reportMessage)
            .fireUsingEvent(new ReportEventTestClass(testClass.getJavaClass())
                                .inTestSuite("TEST_SUITE_NAME"))
            .fire(reportEvent);
    }

    public void startTestMethod(@Observes(precedence = Integer.MAX_VALUE) Before event) {

        Method testMethod = event.getTestMethod();
        boolean runAsClient = event.getTestMethod().isAnnotationPresent(RunAsClient.class);
        String deploymentName = "_DEFAULT_";
        if (event.getTestMethod().isAnnotationPresent(OperateOnDeployment.class)) {
            deploymentName = event.getTestMethod().getAnnotation(OperateOnDeployment.class).value();
        }

        Reporter
            .section(new TestMethodReport(testMethod.getName()))
            .addKeyValueEntry("Operates on deployment", deploymentName)
            .addKeyValueEntry("Runs as client", runAsClient)
            .fireUsingEvent(new ReportEventTestMethod(testMethod))
            .fire(reportEvent);
    }

    public void stopTestMethod(@Observes(precedence = Integer.MIN_VALUE) After event,
        TestResult result) {

        Method testMethod = event.getTestMethod();
        TestMethodReport testMethodReport = new TestMethodReport(testMethod.getName());
        testMethodReport.setStatus(result.getStatus());
        testMethodReport.setStop(new Date(result.getEnd() * 1000));

        String reportMessage = ReportMessageParser.parseTestReportMessage(event.getTestMethod());
        Reporter.section(testMethodReport).addKeyValueEntry("Report message", reportMessage);

        if (result.getStatus() == TestResult.Status.FAILED && result.getThrowable() != null) {
            String stackTrace = getStackTrace(result.getThrowable());
            Reporter.section(testMethodReport.getFailure()).addKeyValueEntry("stacktrace", stackTrace);
        }

        //            inTestResourceReportEvent.fire(new InTestResourceReport());
        //            reporter.get().setReporterCursor(new ReporterCursor(reporter.get().getLastTestClassReport()));
        //            report(event, descriptor.get());

        ReportEventTestMethod reportEventTestMethod = new ReportEventTestMethod(testMethodReport, testMethod);
        reportEvent.fire(reportEventTestMethod);
    }

    public void stopTestClass(@Observes(precedence = Integer.MIN_VALUE) AfterClass event) {

        TestClassReport testClassReport = new TestClassReport(event.getTestClass().getName()).stop();

        ReportEventTestClass reportEventTestClass =
            new ReportEventTestClass(testClassReport, event.getTestClass().getJavaClass()).inTestSuite(TEST_SUITE_NAME);
        reportEvent.fire(reportEventTestClass);

        //        reporter.get().setReporterCursor(new ReporterCursor(reporter.get().getLastTestSuiteReport()));
        //        report(event, descriptor.get());
    }

    public void stopTestSuite(@Observes(precedence = Integer.MIN_VALUE) AfterSuite event) {

        TestSuiteReport testSuiteReport = new TestSuiteReport(TEST_SUITE_NAME).stop();
        reportEvent.fire(new ReportEventTestSuite(testSuiteReport, TEST_SUITE_NAME));

//        exportReportEvent.fire(new ExportReport(reporter.get().getReport()));
    }

//    public void observeReportEvent(@Observes PropertyReportEvent event) {
//        reporter.get().getReporterCursor().getCursor().getPropertyEntries().add(event.getPropertyEntry());
//    }
//
//    private void report(org.jboss.arquillian.core.spi.event.Event event, ArquillianDescriptor descriptor) {
//        if (shouldReport(event, configuration.get().getReportAfterEvery())) {
//            List<ExtensionReport> extensionReports = reporter.get().getReport().getExtensionReports();
//            if (extensionReports.isEmpty()) {
//                extensionReports.addAll(getExtensionReports(descriptor));
//            }
//
//            reporter.get().getLastTestClassReport().setStop(new Date(System.currentTimeMillis()));
//            reporter.get().getLastTestSuiteReport().setStop(new Date(System.currentTimeMillis()));
//
//            exportReportEvent.fire(new ExportReport(reporter.get().getReport()));
//        }
//    }
//
//    private boolean shouldReport(org.jboss.arquillian.core.spi.event.Event event, String frequency) {
//        if (event instanceof AfterClass && ReportFrequency.CLASS.toString().equals(frequency)
//            || (event instanceof After && ReportFrequency.METHOD.toString().equals(frequency))) {
//            return true;
//        }
//        return false;
//    }
//
//    private Collection<? extends ExtensionReport> getExtensionReports(ArquillianDescriptor descriptor) {
//        List<ExtensionReport> extensionReports = new ArrayList<ExtensionReport>();
//
//        for (ExtensionDef extensionDef : descriptor.getExtensions()) {
//            ExtensionReport extensionReport = new ExtensionReport();
//            extensionReport.setQualifier(extensionDef.getExtensionName());
//            extensionReport.setConfiguration(extensionDef.getExtensionProperties());
//            extensionReports.add(extensionReport);
//        }
//        return extensionReports;
//    }

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
