package org.arquillian.reporter.api.model;

/**
 * A {@link StringKey} implementation used for Arquillian Reporter-core purposes
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterCoreKey extends AbstractStringKey {

    public static final StringKey GENERAL_TEST_CLASS_CONFIGURATION_REPORT = new ReporterCoreKey();
    public static final StringKey GENERAL_METHOD_FAILURE_REPORT = new ReporterCoreKey();
    public static final StringKey METHOD_FAILURE_REPORT = new ReporterCoreKey();
    public static final StringKey METHOD_FAILURE_REPORT_STACKTRACE = new ReporterCoreKey();
    public static final StringKey GENERAL_TEST_METHOD_CONFIGURATION_REPORT = new ReporterCoreKey();
    public static final StringKey GENERAL_TEST_SUITE_CONFIGURATION_REPORT = new ReporterCoreKey();

}
