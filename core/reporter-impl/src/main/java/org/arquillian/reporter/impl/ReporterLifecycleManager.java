package org.arquillian.reporter.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.jboss.arquillian.core.spi.ServiceLoader;

import static org.arquillian.reporter.impl.SectionEventManager.processEvent;
import static org.arquillian.reporter.impl.StringKeysBuilder.buildStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManager {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ExecutionReport> report;

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    public void observeFirstEvent(@Observes ManagerStarted event) {
        if (report.get() == null) {
            ExecutionReport executionReport = new ExecutionReport();
            report.set(executionReport);

            BuilderLoader.load();

            Collection<StringKey> allStringKeys = serviceLoader.get().all(StringKey.class);
            allStringKeys.stream().forEach(stringKey -> buildStringKey(stringKey));
        }
    }

    public void observeEventsForAllSections(@Observes SectionEvent event) {
        processEvent(event, report.get());
    }

    // technically, I don't need any more specific observer than the general one...

    //    public void observeEventsFroTestSuiteSection(@Observes TestSuiteSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestSuiteConfigurationSection(@Observes TestSuiteConfigurationSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestClassSection(@Observes TestClassSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestClassSection(@Observes TestClassConfigurationSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestClassConfigurationSection(@Observes TestSuiteConfigurationSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestMethodSection(@Observes TestMethodSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestMethodConfigurationSection(@Observes TestMethodConfigurationSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    //    public void observeEventsForTestMethodFailureSection(@Observes TestMethodFailureSection event) {
    //        processEvent(event, report.get());
    //    }
    //
    public void afterSuite(@Observes ManagerStopping event) throws IOException {
        printJson();
    }

    private void printJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(report.get().getTestSuiteReports());
        try {
            FileUtils.writeStringToFile(new File("target/report.json"), json);
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
