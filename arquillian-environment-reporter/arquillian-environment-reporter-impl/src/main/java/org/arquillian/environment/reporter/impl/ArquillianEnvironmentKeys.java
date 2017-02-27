package org.arquillian.environment.reporter.impl;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

public class ArquillianEnvironmentKeys extends AbstractStringKey {

    //section nme
    public static final StringKey ENVIRONMENT_SECTION_NAME = new ArquillianEnvironmentKeys();

    //elements
    public static final StringKey JAVA_VERSION = new ArquillianEnvironmentKeys();
    public static final StringKey TEST_RUNNER = new ArquillianEnvironmentKeys();
    public static final StringKey DOCKER = new ArquillianEnvironmentKeys();
    public static final StringKey TIMEZONE = new ArquillianEnvironmentKeys();
    public static final StringKey CHARSET = new ArquillianEnvironmentKeys();
    public static final StringKey OPERATIVE_SYSTEM = new ArquillianEnvironmentKeys();

}
