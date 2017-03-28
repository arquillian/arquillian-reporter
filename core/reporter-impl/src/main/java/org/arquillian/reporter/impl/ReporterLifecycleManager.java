package org.arquillian.reporter.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.config.ReporterConfiguration;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.jboss.arquillian.core.spi.ServiceLoader;

import static org.arquillian.reporter.impl.SectionEventManager.processEvent;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManager {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ExecutionStore> executionStore;

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    // observe the first event that is available for us and prepare the execution store that contains all information
    // about the whole test execution; then it loads registered implementation of builders and loads and sets
    // all string-keys that are available
    public void observeFirstEvent(@Observes(precedence = 100) ManagerStarted event) {
        if (executionStore.get() == null) {
            ExecutionStore store = new ExecutionStore();
            executionStore.set(store);

            BuilderLoader.load();

            Collection<StringKey> allStringKeys = serviceLoader.get().all(StringKey.class);
            allStringKeys.forEach(StringKeysBuilder::buildStringKey);
        }
    }

    // observe all section-events
    public void observeEventsForAllSections(@Observes SectionEvent event) {
        processEvent(event, executionStore.get());
    }

    public void observeLastEvent(@Observes ManagerStopping event, ReporterConfiguration reporterConfiguration) throws IOException {
        printJson(reporterConfiguration);
    }

    private void printJson(ReporterConfiguration reporterConfiguration) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(executionStore.get().getExecutionReport());
        try {
            FileUtils.writeStringToFile(reporterConfiguration.getReportFile(), json, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        System.out.println(json);
        //        try {
        //            Thread.sleep(500);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
    }
}
