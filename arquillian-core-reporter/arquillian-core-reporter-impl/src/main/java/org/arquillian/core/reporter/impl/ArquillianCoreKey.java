package org.arquillian.core.reporter.impl;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianCoreKey extends AbstractStringKey {

    //whole test suite
    public static final StringKey TEST_SUITE_NAME = new ArquillianCoreKey();

    // container
    public static final StringKey CONTAINER_CONFIGURATION_REPORT = new ArquillianCoreKey();
    public static final StringKey CONTAINER_NAME = new ArquillianCoreKey();
    public static final StringKey CONTAINER_REPORT = new ArquillianCoreKey();

    // deployment
    public static final StringKey DEPLOYMENT_IN_TEST_CLASS_REPORT = new ArquillianCoreKey();
    public static final StringKey DEPLOYMENT_IN_TEST_CLASS_NAME = new ArquillianCoreKey();
    public static final StringKey ARCHIVE_NAME_OF_DEPLOYMENT = new ArquillianCoreKey();
    public static final StringKey ORDER_OF_DEPLOYMENT = new ArquillianCoreKey();
    public static final StringKey PROTOCOL_USED_FOR_DEPLOYMENT = new ArquillianCoreKey();

    // test class
    public static final StringKey TEST_CLASS_CONFIGURATION = new ArquillianCoreKey();
    public static final StringKey CLASS_RUNS_AS_CLIENT = new ArquillianCoreKey();
    public static final StringKey TEST_CLASS_REPORT_MESSAGE = new ArquillianCoreKey();

    // test method
    public static final StringKey TEST_METHOD_OPERATES_ON_DEPLOYMENT = new ArquillianCoreKey();
    public static final StringKey TEST_METHOD_RUNS_AS_CLIENT = new ArquillianCoreKey();
    public static final StringKey TEST_METHOD_REPORT_MESSAGE = new ArquillianCoreKey();
}
