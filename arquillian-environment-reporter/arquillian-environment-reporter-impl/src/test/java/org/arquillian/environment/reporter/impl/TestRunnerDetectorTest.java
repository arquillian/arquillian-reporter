package org.arquillian.environment.reporter.impl;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestRunnerDetectorTest {

    @Test
    public void should_return_maven_as_runner() {

        final TestRunnerDetector.TestRunner testRunner = TestRunnerDetector.detect();

        assertThat(testRunner).isEqualTo(TestRunnerDetector.TestRunner.MAVEN);
    }

}
