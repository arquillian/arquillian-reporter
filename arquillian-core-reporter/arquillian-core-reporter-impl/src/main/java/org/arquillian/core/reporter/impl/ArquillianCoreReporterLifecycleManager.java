package org.arquillian.core.reporter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import org.arquillian.core.reporter.event.TestClassConfigurationDeploymentSection;
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
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.AfterTestLifecycleEvent;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

import static org.arquillian.core.reporter.impl.ArquillianCoreKey.ARCHIVE_NAME_OF_DEPLOYMENT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.CLASS_RUNS_AS_CLIENT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.CONTAINER_CONFIGURATION_REPORT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.CONTAINER_NAME;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.CONTAINER_REPORT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.DEPLOYMENT_IN_TEST_CLASS_NAME;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.DEPLOYMENT_IN_TEST_CLASS_REPORT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.ORDER_OF_DEPLOYMENT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.PROTOCOL_USED_FOR_DEPLOYMENT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.TEST_CLASS_CONFIGURATION;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.TEST_CLASS_REPORT_MESSAGE;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.TEST_METHOD_OPERATES_ON_DEPLOYMENT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.TEST_METHOD_REPORT_MESSAGE;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.TEST_METHOD_RUNS_AS_CLIENT;
import static org.arquillian.core.reporter.impl.ArquillianCoreKey.TEST_SUITE_NAME;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreReporterLifecycleManager {

    public final static String DEFAULT_TEST_SUITE_ID = "arquillian-default-test-suite";

    @Inject
    private Event<SectionEvent> sectionEvent;

    @Inject
    private Instance<TestClass> testClass;

    @Inject
    private Event<TestSuiteConfigurationSection> config;

    @Inject
    private Instance<ArquillianDescriptor> descriptor;

    public void startTestSuite(@Observes(precedence = Integer.MAX_VALUE) BeforeSuite managerProcessing) {
        Reporter
            .createReport(new TestSuiteReport(TEST_SUITE_NAME))
            .inSection(new TestSuiteSection(DEFAULT_TEST_SUITE_ID))
            .fire(sectionEvent);

        // todo differentiate between loaded extensions and configs that are in arquillian.xml
        //        ReportBuilder extensionsReport = Reporter.createReport(new ConfigurationReport("Loaded extensions"));
        //        for (ExtensionDef extensionDef : descriptor.get().getExtensions()) {
        //            ReportBuilder extBuilder = Reporter
        //                .createReport(extensionDef.getExtensionName())
        //                .feedKeyValueListFromMap(extensionDef.getExtensionProperties());
        //            extensionsReport.addReport(extBuilder);
        //        }
        //        extensionsReport
        //            .inSection(new TestSuiteConfigurationSection("loaded-extension"))
        //            .fire(sectionEvent);
    }

    public void reportContainer(@Observes Container event) {
        Map<String, String> containerProperties = event.getContainerConfiguration().getContainerProperties();

        String containerId = event.getContainerConfiguration().isDefault() ? "_DEFAULT_" : event.getName();

        Reporter.createReport(CONTAINER_REPORT)
            .addKeyValueEntry(CONTAINER_NAME, event.getName())
            .addReport(
                Reporter.createReport(CONTAINER_CONFIGURATION_REPORT)
                    .feedKeyValueListFromMap(containerProperties))
            .inSection(new TestSuiteConfigurationContainerSection(containerId, DEFAULT_TEST_SUITE_ID))
            .fire(sectionEvent);
    }

    public void reportDeployment(@Observes BeforeDeploy event) {
        DeploymentDescription description = event.getDeployment();
        //         String targetContainer = description.getTarget().getName();

        Reporter
            .createReport(DEPLOYMENT_IN_TEST_CLASS_REPORT)
            .addKeyValueEntry(DEPLOYMENT_IN_TEST_CLASS_NAME, description.getName())
            .addKeyValueEntry(ARCHIVE_NAME_OF_DEPLOYMENT, description.getArchive().getName())
            .addKeyValueEntry(ORDER_OF_DEPLOYMENT, description.getOrder())
            .addKeyValueEntry(PROTOCOL_USED_FOR_DEPLOYMENT, description.getProtocol().getName())
            .inSection(new TestClassConfigurationDeploymentSection(description.getName()))
            .fire(sectionEvent);

        // todo add info into container report - optimally keep oll deployments in a table - to do so we need to have functionality of merging tables. Or is there any better way?
    }

    public void startTestClass(@Observes(precedence = Integer.MAX_VALUE) BeforeClass event) {
        TestClass testClass = event.getTestClass();
        boolean runAsClient = event.getTestClass().isAnnotationPresent(RunAsClient.class);

        TestClassReport testClassReport = new TestClassReport(testClass.getName());
        Reporter
            .createReport(new ConfigurationReport(TEST_CLASS_CONFIGURATION))
            .addKeyValueEntry(CLASS_RUNS_AS_CLIENT, runAsClient);

        String reportMessage = ReportMessageParser.parseTestClassReportMessage(event.getTestClass().getJavaClass());
        Reporter
            .createReport(testClassReport)
            .addKeyValueEntry(TEST_CLASS_REPORT_MESSAGE, reportMessage)

            .inSection(new TestClassSection(testClass.getJavaClass(), DEFAULT_TEST_SUITE_ID))
            .fire(sectionEvent);
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
            .addKeyValueEntry(TEST_METHOD_OPERATES_ON_DEPLOYMENT, deploymentName)
            .addKeyValueEntry(TEST_METHOD_RUNS_AS_CLIENT, runAsClient)
            .inSection(new TestMethodSection(testMethod))
            .fire(sectionEvent);
    }

    public void stopTestMethod(@Observes(precedence = Integer.MIN_VALUE) AfterTestLifecycleEvent event,
        TestResult result) {
        if (!(event instanceof After)) {
            Method testMethod = event.getTestMethod();
            String reportMessage = ReportMessageParser.parseTestReportMessage(event.getTestMethod());

            Reporter
                .createReport(new TestMethodReport(testMethod.getName()))
                .stop()
                .setResult(result)
                .addKeyValueEntry(TEST_METHOD_REPORT_MESSAGE, reportMessage)
                .inSection(new TestMethodSection(testMethod))
                .fire(sectionEvent);
        }
    }

    public void stopTestClass(@Observes(precedence = Integer.MIN_VALUE) AfterClass event) {

        Reporter
            .createReport(new TestClassReport(event.getTestClass().getName()))
            .stop()
            .inSection(new TestClassSection(event.getTestClass().getJavaClass(), DEFAULT_TEST_SUITE_ID))
            .fire(sectionEvent);
    }

    public void stopTestSuite(@Observes(precedence = Integer.MIN_VALUE) AfterSuite event) {

        Reporter
            .createReport(new TestSuiteReport(TEST_SUITE_NAME))
            .stop()
            .inSection(new TestSuiteSection(DEFAULT_TEST_SUITE_ID))
            .fire(sectionEvent);
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
