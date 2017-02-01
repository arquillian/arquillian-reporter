package org.arquillian.reporter.impl.utils.dummy;

import org.arquillian.reporter.api.model.AbstractStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DummyStringKeys extends AbstractStringKey {

    public static DummyStringKeys TEST_SUITE_NAME = new DummyStringKeys();
    public static DummyStringKeys CONFIG_NAME = new DummyStringKeys();
    public static DummyStringKeys TEST_CLASS_NAME = new DummyStringKeys();
    public static DummyStringKeys TEST_CLASS_CONFIG_NAME = new DummyStringKeys();
    public static DummyStringKeys TEST_METHOD_NAME = new DummyStringKeys();
    public static DummyStringKeys FAILURE_REPORT_NAME = new DummyStringKeys();
}
