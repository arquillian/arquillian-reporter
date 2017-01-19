package org.arquillian.reporter.api.model;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterCoreKeys extends AbstractStringKey {

    public static final StringKey GENERAL_TEST_CLASS_CONFIGURATION_REPORT = new ReporterCoreKeys();
    public static final StringKey GENERAL_METHOD_FAILURE_REPORT = new ReporterCoreKeys();
    public static final StringKey METHOD_FAILURE_REPORT = new ReporterCoreKeys();
    public static final StringKey METHOD_FAILURE_REPORT_STACKTRACE = new ReporterCoreKeys();
    public static final StringKey GENERAL_TEST_METHOD_CONFIGURATION_REPORT = new ReporterCoreKeys();
    public static final StringKey GENERAL_TEST_SUITE_CONFIGURATION_REPORT = new ReporterCoreKeys();



}
