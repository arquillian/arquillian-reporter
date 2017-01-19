package org.arquillian.core.reporter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.arquillian.core.reporter.event.TestSuiteConfigurationContainerDeploymentSection;
import org.arquillian.core.reporter.event.TestSuiteConfigurationContainerSection;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
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
    private Event<SectionEvent> reportEvent;

    @Inject
    private Event<TestSuiteConfigurationSection> config;

    public void startTestSuite(@Observes(precedence = Integer.MAX_VALUE) BeforeSuite managerProcessing) {
        Reporter
            .createReport(new TestSuiteReport(TEST_SUITE_NAME))
            .inSection(new TestSuiteSection(TEST_SUITE_NAME))
            .fire(reportEvent);
    }

    public void reportContainer(@Observes Container event) {
        Map<String, String> containerProperties = event.getContainerConfiguration().getContainerProperties();

//        Reporter
//            .createReport(new ConfigurationReport("Containers"))
//            .inSection(new TestSuiteConfigurationSection(TEST_SUITE_NAME, "containers"))
//            .fire(config);

        String containerId = event.getContainerConfiguration().isDefault() ? "_DEFAULT_" : event.getName();

        Reporter.createReport("Container")
                .addKeyValueEntry("Container name", event.getName())
                .addReport(
                    Reporter.createReport("Configuration")
                        .feedKeyValueListFromMap(containerProperties))
            .inSection(new TestSuiteConfigurationContainerSection(containerId, TEST_SUITE_NAME))
            .fire(reportEvent);
    }

    public void reportDeployment(@Observes BeforeDeploy event) {
        DeploymentDescription description = event.getDeployment();
        String targetContainer = description.getTarget().getName();

        Reporter
            .createReport("Deployment")
            .addKeyValueEntry("Deployment name", description.getName())
            .addKeyValueEntry("Archive name", description.getArchive().getName())
            .addKeyValueEntry("Order", description.getOrder())
            .addKeyValueEntry("Protocol", description.getProtocol().getName())
            .inSection(new TestSuiteConfigurationContainerDeploymentSection(description.getName(), targetContainer))
            .fire(reportEvent);
    }

    public void startTestClass(@Observes(precedence = Integer.MAX_VALUE) BeforeClass event) {
        TestClass testClass = event.getTestClass();
        boolean runAsClient = event.getTestClass().isAnnotationPresent(RunAsClient.class);

        TestClassReport testClassReport = new TestClassReport(testClass.getName());
        Reporter
            .createReport(new ConfigurationReport("Test class config"))
            .addKeyValueEntry("Runs as client", runAsClient);

        String reportMessage = ReportMessageParser.parseTestClassReportMessage(event.getTestClass().getJavaClass());
        Reporter
            .createReport(testClassReport)
            .addKeyValueEntry("Report message", reportMessage)

            .inSection(new TestClassSection(testClass.getJavaClass(), TEST_SUITE_NAME))
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
            .createReport(new TestMethodReport(testMethod.getName()))
            .addKeyValueEntry("Operates on deployment", deploymentName)
            .addKeyValueEntry("Runs as client", runAsClient)
            .inSection(new TestMethodSection(testMethod))
            .fire(reportEvent);
    }

    public void stopTestMethod(@Observes(precedence = Integer.MIN_VALUE) After event, TestResult result) {

        Method testMethod = event.getTestMethod();
        String reportMessage = ReportMessageParser.parseTestReportMessage(event.getTestMethod());

        Reporter
            .createReport(new TestMethodReport(testMethod.getName()))
            .stop()
            .setResult(result)
            .addKeyValueEntry("Report message", reportMessage)
            .inSection(new TestMethodSection(testMethod))
            .fire(reportEvent);
    }

    public void stopTestClass(@Observes(precedence = Integer.MIN_VALUE) AfterClass event) {

        Reporter
            .createReport(new TestClassReport(event.getTestClass().getName()))
            .stop()
            .inSection(new TestClassSection(event.getTestClass().getJavaClass(), TEST_SUITE_NAME))
            .fire(reportEvent);
    }

    public void stopTestSuite(@Observes(precedence = Integer.MIN_VALUE) AfterSuite event) {

        Reporter
            .createReport(new TestSuiteReport(TEST_SUITE_NAME))
            .stop()
            .inSection(new TestSuiteSection(TEST_SUITE_NAME))
            .fire(reportEvent);
    }

//
//    private Collection<? extends ExtensionReport> getExtensionReports(ArquillianDescriptor descriptor) {
//        List<ExtensionReport> extensionReports = new ArrayList<ExtensionReport>();
//
//        for (ExtensionDef extensionDef : descriptor.getExtensions()) {
//            ExtensionReport extensionReport = new ExtensionReport();
//            extensionReport.setQualifier(extensionDef.getExtensionName());
//            extensionReport.setConfigurations(extensionDef.getExtensionProperties());
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
