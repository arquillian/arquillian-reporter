package org.arquillian.environment.reporter.impl;

public class TestRunnerDetector {

    private TestRunnerDetector() {
    }

    public static TestRunner detect() {
        return detect(Thread.currentThread().getStackTrace());
    }

    static TestRunner detect(StackTraceElement[] stackTrace) {

        // Iterate from last element to first
        for (int i = stackTrace.length - 1; i >= 0; i--) {

            String currentClassName = stackTrace[i].getClassName();

            if (currentClassName.contains(TestRunner.GRADLE.getPackageName())) {
                return TestRunner.GRADLE;
            }

            if (currentClassName.contains(TestRunner.MAVEN.getPackageName())) {
                return TestRunner.MAVEN;
            }

            if (currentClassName.contains(TestRunner.ANT.getPackageName())) {
                return TestRunner.ANT;
            }

            if (currentClassName.contains(TestRunner.INTELLIJ.getPackageName())) {
                return TestRunner.INTELLIJ;
            }

            if (currentClassName.contains(TestRunner.ECLIPSE.getPackageName())) {
                return TestRunner.ECLIPSE;
            }

        }

        return TestRunner.UNKNOWN;
    }

    public enum TestRunner {
        //Netbeans just runs the build tool runner

        GRADLE("org.gradle"), MAVEN("org.apache.maven"), ANT("org.apache.tools.ant"), INTELLIJ("com.intellij"), ECLIPSE("org.eclipse"), UNKNOWN("");

        private String packageName;

        TestRunner(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return packageName;
        }
    }

}
