package org.arquillian.environment.reporter.impl;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

public class EnvironmentKey extends AbstractStringKey {

    //section name
    public static final StringKey ENVIRONMENT_SECTION_NAME = new EnvironmentKey();

    //elements
    public static final StringKey JAVA_VERSION = new EnvironmentKey();
    public static final StringKey TEST_RUNNER = new EnvironmentKey();
    public static final StringKey DOCKER = new EnvironmentKey();
    public static final StringKey TIMEZONE = new EnvironmentKey();
    public static final StringKey CHARSET = new EnvironmentKey();
    public static final StringKey OPERATIVE_SYSTEM = new EnvironmentKey();
    public static final StringKey OPERATIVE_SYSTEM_ARCH = new EnvironmentKey();
    public static final StringKey OPERATIVE_SYSTEM_VERSION = new EnvironmentKey();

}
