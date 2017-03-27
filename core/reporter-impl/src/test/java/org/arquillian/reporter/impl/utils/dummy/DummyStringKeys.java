package org.arquillian.reporter.impl.utils.dummy;

import org.arquillian.reporter.api.model.AbstractStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DummyStringKeys extends AbstractStringKey {

    public static DummyStringKeys TEST_SUITE_NAME = new DummyStringKeys() {{
        setValue("test suite name");
    }};
    public static DummyStringKeys CONFIG_NAME = new DummyStringKeys() {{
        setValue("config name");
    }};
    public static DummyStringKeys TEST_CLASS_NAME = new DummyStringKeys() {{
        setValue("test class name");
    }};
    public static DummyStringKeys TEST_CLASS_CONFIG_NAME = new DummyStringKeys() {{
        setValue("test class config name");
    }};
    public static DummyStringKeys TEST_METHOD_NAME = new DummyStringKeys() {{
        setValue("test method name");
    }};
    public static DummyStringKeys FAILURE_REPORT_NAME = new DummyStringKeys() {{
        setValue("failure report name");
    }};

    public static DummyStringKeys DUMMY_1_NAME = new DummyStringKeys() {{
        setValue("dummy name 1");
    }};

    public static DummyStringKeys DUMMY_2_NAME = new DummyStringKeys() {{
        setValue("dummy name 2");
    }};

    public static DummyStringKeys DUMMY_3_NAME = new DummyStringKeys() {{
        setValue("dummy name 3");
    }};
}
