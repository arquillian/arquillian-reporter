package org.arquillian.core.reporter.impl;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreKeys extends AbstractStringKey {

    //whole test suite
    public static final StringKey TEST_SUITE_NAME = new ArquillianCoreKeys();

    // container
    public static final StringKey CONTAINER_CONFIGURATION_REPORT = new ArquillianCoreKeys();
    public static final StringKey CONTAINER_NAME = new ArquillianCoreKeys();
    public static final StringKey CONTAINER_REPORT = new ArquillianCoreKeys();

    // deployment
    public static final StringKey DEPLOYMENT_IN_TEST_CLASS_REPORT = new ArquillianCoreKeys();
    public static final StringKey DEPLOYMENT_IN_TEST_CLASS_NAME = new ArquillianCoreKeys();
    public static final StringKey ARCHIVE_NAME_OF_DEPLOYMENT = new ArquillianCoreKeys();
    public static final StringKey ORDER_OF_DEPLOYMENT = new ArquillianCoreKeys();
    public static final StringKey PROTOCOL_USED_FOR_DEPLOYMENT = new ArquillianCoreKeys();

    // test class
    public static final StringKey TEST_CLASS_CONFIGURATION = new ArquillianCoreKeys();
    public static final StringKey CLASS_RUNS_AS_CLIENT = new ArquillianCoreKeys();
    public static final StringKey TEST_CLASS_REPORT_MESSAGE = new ArquillianCoreKeys();

    // test method
    public static final StringKey TEST_METHOD_OPERATES_ON_DEPLOYMENT = new ArquillianCoreKeys();
    public static final StringKey TEST_METHOD_RUNS_AS_CLIENT = new ArquillianCoreKeys();
    public static final StringKey TEST_METHOD_REPORT_MESSAGE = new ArquillianCoreKeys();
}
