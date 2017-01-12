package org.arquillian.reporter.config;

import java.util.Map;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterConfigurator {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ReporterConfiguration> reporterConfigurationInstanceProducer;

    public void configure(@Observes ArquillianDescriptor arquillianDescriptor){
        Map<String, String> reporterProps = arquillianDescriptor.extension("reporter").getExtensionProperties();
        ReporterConfiguration reporterConfiguration = ReporterConfiguration.fromMap(reporterProps);
        reporterConfigurationInstanceProducer.set(reporterConfiguration);
    }
}
