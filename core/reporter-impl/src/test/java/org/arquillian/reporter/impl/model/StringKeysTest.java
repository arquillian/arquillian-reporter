package org.arquillian.reporter.impl.model;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.impl.base.AbstractReporterTestBase;
import org.junit.Test;

import static org.arquillian.reporter.impl.model.TestExtensionStringKey.DRONE_BROWSER;
import static org.arquillian.reporter.impl.model.TestExtensionStringKey.DRONE_REPORT;
import static org.arquillian.reporter.impl.model.TestExtensionStringKey.DRONE_REPORT_SCOPE;
import static org.arquillian.reporter.impl.model.TestExtensionStringKey.DRONE_SCOPE_IN_GENERAL;
import static org.assertj.core.api.Assertions.assertThat;


public class StringKeysTest extends AbstractReporterTestBase {

    @Test
    public void verify_stringKey_properties_file_exists() {
        URL resource = getClass().getClassLoader()
            .getResource("org.arquillian.reporter.impl.model.TestExtensionStringKey.properties");
        File file = Paths.get(resource.getPath()).toFile();

        assertThat(file).exists();
    }

    @Test
    public void should_load_stringKey_contents_from_properties_file() {
        String value = DRONE_REPORT.getValue();
        String description = DRONE_REPORT.getDescription();
        String icon = DRONE_REPORT.getIcon();

        assertThat(value).isEqualTo("Drone report");
        assertThat(description).isEqualTo("Report containing information about Drone instance used in the test");
        assertThat(icon).isEqualTo("./path/to/drone.icon");
    }

    @Test
    public void should_load_default_value_when_stringKey_not_in_properties_file() {
        String value = DRONE_BROWSER.getValue();
        String description = DRONE_BROWSER.getDescription();
        String icon = DRONE_BROWSER.getIcon();

        assertThat(value).isEqualTo("drone_browser");
        assertThat(description).isNull();
        assertThat(icon).isNull();
    }

    @Test
    public void should_load_multi_line_description_from_properties_file() {
        String multi_line_description = DRONE_SCOPE_IN_GENERAL.getDescription();

        assertThat(multi_line_description).isEqualTo("Scope of the drone instance as given a multiline description. \n"
            + "This is a new line in the description.");
    }

    @Test
    public void should_load_value_ignoring_leading_whitespaces_from_properties_file() {
        String value = DRONE_REPORT_SCOPE.getValue();

        assertThat(value).isEqualTo("Scope");
    }

    @Override
    protected void addReporterStringKeys(List<StringKey> stringKeys) {
        stringKeys.add(new TestExtensionStringKey());
    }
}
