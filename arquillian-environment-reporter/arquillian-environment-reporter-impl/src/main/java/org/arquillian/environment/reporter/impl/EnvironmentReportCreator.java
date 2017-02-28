package org.arquillian.environment.reporter.impl;

import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

import java.nio.charset.Charset;
import java.util.TimeZone;

import static org.arquillian.environment.reporter.impl.EnvironmentKey.CHARSET;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.DOCKER;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.ENVIRONMENT_SECTION_NAME;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.JAVA_VERSION;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.OPERATIVE_SYSTEM;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.OPERATIVE_SYSTEM_ARCH;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.OPERATIVE_SYSTEM_VERSION;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.TEST_RUNNER;
import static org.arquillian.environment.reporter.impl.EnvironmentKey.TIMEZONE;

public class EnvironmentReportCreator {

    @Inject
    Event<SectionEvent> reportEvent;

    public void startTestSuite(@Observes BeforeSuite managerProcessing) {
        Reporter.createReport(new ConfigurationReport(ENVIRONMENT_SECTION_NAME))
                .addKeyValueEntry(JAVA_VERSION, getJavaVersion())
                .addKeyValueEntry(TEST_RUNNER, getTestRunner())
                .addKeyValueEntry(DOCKER, isRunningInDocker())
                .addKeyValueEntry(TIMEZONE, getTimeZone())
                .addKeyValueEntry(CHARSET, getDefaultCharset())
                .addKeyValueEntry(OPERATIVE_SYSTEM, getOperativeSystemName())
                .addKeyValueEntry(OPERATIVE_SYSTEM_ARCH, getOperativeSystemArchitecture())
                .addKeyValueEntry(OPERATIVE_SYSTEM_VERSION,getOperativeSystemVersion())
                .inSection(new TestSuiteConfigurationSection("environment"))
                .fire(reportEvent);
    }

    private String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private String getTestRunner() {
        return TestRunnerDetector.detect().name();
    }

    private String isRunningInDocker() {
        return Boolean.toString(DockerDetector.detect());
    }

    private String getTimeZone() {
        return TimeZone.getDefault().getDisplayName();
    }

    private String getDefaultCharset() {
        return Charset.defaultCharset().displayName();
    }

    private String getOperativeSystemName() {
        return System.getProperty("os.name");
    }

    private String getOperativeSystemArchitecture() {
        return System.getProperty("os.arch");
    }

    private String getOperativeSystemVersion() {
        return System.getProperty("os.version");
    }

}
