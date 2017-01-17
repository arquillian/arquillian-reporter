package org.arquillian.core.reporter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.arquillian.core.reporter.event.TestSuiteConfigurationContainerDeploymentNode;
import org.arquillian.core.reporter.event.TestSuiteConfigurationContainerNode;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.event.TestClassNode;
import org.arquillian.reporter.api.event.TestMethodNode;
import org.arquillian.reporter.api.event.TestSuiteConfigurationNode;
import org.arquillian.reporter.api.event.TestSuiteNode;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
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
    private Event<ReportNodeEvent> reportEvent;

    @Inject
    private Event<TestSuiteConfigurationNode> config;

    public void startTestSuite(@Observes(precedence = Integer.MAX_VALUE) BeforeSuite managerProcessing) {
        Reporter
            .section(new TestSuiteReport(TEST_SUITE_NAME))
            .attachToNode(new TestSuiteNode(TEST_SUITE_NAME))
            .report(reportEvent);
    }

    public void reportContainer(@Observes Container event) {
        Map<String, String> containerProperties = event.getContainerConfiguration().getContainerProperties();

        TestSuiteConfigurationNode containers = (TestSuiteConfigurationNode) Reporter
            .section("Containers")
            .attachToNode(new TestSuiteConfigurationNode(TEST_SUITE_NAME))
            .report(config);

        String containerId = event.getContainerConfiguration().isDefault() ? "_DEFAULT_" : event.getName();

        Reporter.section("Container")
                .addKeyValueEntry("Container name", event.getName())
                .addSection(
                    Reporter.section("Configuration")
                        .feedKeyValueListFromMap(containerProperties))
            .attachToNode(new TestSuiteConfigurationContainerNode(containerId).setParentEvent(containers))
            .report(reportEvent);
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
            .attachToNode(new TestSuiteConfigurationContainerDeploymentNode(description.getName(), targetContainer))
            .report(reportEvent);
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

            .attachToNode(new TestClassNode(testClass.getJavaClass(), "TEST_SUITE_NAME"))
            .report(reportEvent);
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
            .attachToNode(new TestMethodNode(testMethod))
            .report(reportEvent);
    }

    public void stopTestMethod(@Observes(precedence = Integer.MIN_VALUE) After event, TestResult result) {

        Method testMethod = event.getTestMethod();
        String reportMessage = ReportMessageParser.parseTestReportMessage(event.getTestMethod());

        Reporter
            .section(new TestMethodReport(testMethod.getName()))
            .stop()
            .setResult(result)
            .addKeyValueEntry("Report message", reportMessage)
            .attachToNode(new TestMethodNode(testMethod))
            .report(reportEvent);

        //            inTestResourceReportEvent.report(new InTestResourceReport());
        //            reporter.get().setReporterCursor(new ReporterCursor(reporter.get().getLastTestClassReport()));
        //            report(event, descriptor.get());
    }

    public void stopTestClass(@Observes(precedence = Integer.MIN_VALUE) AfterClass event) {

        Reporter
            .section(new TestClassReport(event.getTestClass().getName()))
            .stop()
            .attachToNode(new TestClassNode(event.getTestClass().getJavaClass(), TEST_SUITE_NAME))
            .report(reportEvent);

        //        reporter.get().setReporterCursor(new ReporterCursor(reporter.get().getLastTestSuiteReport()));
        //        report(event, descriptor.get());
    }

    public void stopTestSuite(@Observes(precedence = Integer.MIN_VALUE) AfterSuite event) {

        Reporter
            .section(new TestSuiteReport(TEST_SUITE_NAME).stop())
            .attachToNode(new TestSuiteNode(TEST_SUITE_NAME))
            .report(reportEvent);

        //        exportReportEvent.report(new ExportReport(reporter.get().getReport()));
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
    //            exportReportEvent.report(new ExportReport(reporter.get().getReport()));
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
