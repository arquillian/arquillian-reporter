package org.arquillian.reporter.impl.model;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

public class TestExtensionStringKey extends AbstractStringKey {
    public static final StringKey DRONE_REPORT = new TestExtensionStringKey();
    public static final StringKey DRONE_SCOPE_IN_GENERAL = new TestExtensionStringKey();
    public static final StringKey DRONE_BROWSER = new TestExtensionStringKey();
    public static final StringKey DRONE_REPORT_SCOPE = new TestExtensionStringKey();
}
