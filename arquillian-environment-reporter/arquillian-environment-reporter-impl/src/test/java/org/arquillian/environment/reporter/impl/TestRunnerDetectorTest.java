package org.arquillian.environment.reporter.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TestRunnerDetectorTest {

    @Test
    public void should_return_maven_as_runner() {

        final StackTraceElement stackTraceElement = getStackTraceElement("org.apache.maven.surefire.Runner");

        final TestRunnerDetector.TestRunner testRunner = TestRunnerDetector.detect(new StackTraceElement[] { stackTraceElement });

        assertThat(testRunner).isEqualTo(TestRunnerDetector.TestRunner.MAVEN);
    }

    @Test
    public void should_return_gradle_as_runner() {

        final StackTraceElement stackTraceElement = getStackTraceElement("org.gradle.test.Runner");

        final TestRunnerDetector.TestRunner testRunner = TestRunnerDetector.detect(new StackTraceElement[] { stackTraceElement });

        assertThat(testRunner).isEqualTo(TestRunnerDetector.TestRunner.GRADLE);
    }

    @Test
    public void should_return_ant_as_runner() {

        final StackTraceElement stackTraceElement = getStackTraceElement("org.apache.tools.ant.test.Runner");

        final TestRunnerDetector.TestRunner testRunner = TestRunnerDetector.detect(new StackTraceElement[] { stackTraceElement });

        assertThat(testRunner).isEqualTo(TestRunnerDetector.TestRunner.ANT);
    }

    @Test
    public void should_return_intellij_as_runner() {

        final StackTraceElement stackTraceElement = getStackTraceElement("com.intellij.test.Runner");

        final TestRunnerDetector.TestRunner testRunner = TestRunnerDetector.detect(new StackTraceElement[] { stackTraceElement });

        assertThat(testRunner).isEqualTo(TestRunnerDetector.TestRunner.INTELLIJ);
    }

    @Test
    public void should_return_eclipse_as_runner() {

        final StackTraceElement stackTraceElement = getStackTraceElement("org.eclipse.test.Runner");

        final TestRunnerDetector.TestRunner testRunner = TestRunnerDetector.detect(new StackTraceElement[] { stackTraceElement });

        assertThat(testRunner).isEqualTo(TestRunnerDetector.TestRunner.ECLIPSE);
    }

    private StackTraceElement getStackTraceElement(String clazz) {
        return new StackTraceElement(clazz, "test", clazz + ".java", 1);
    }

}
