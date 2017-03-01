package org.arquillian.reporter.api.model.report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface WithConfigurationReport {

    /**
     * Returns the {@link ConfigurationReport}
     *
     * @return The {@link ConfigurationReport}
     */
    ConfigurationReport getConfiguration();

    /**
     * Sets the given {@link ConfigurationReport}
     *
     * @param configuration A {@link ConfigurationReport} to be set
     */
    void setConfiguration(ConfigurationReport configuration);
}
