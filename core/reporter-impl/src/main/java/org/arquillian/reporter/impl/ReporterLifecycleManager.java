package org.arquillian.reporter.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportTestClassEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteConfigurationEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteEvent;
import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.utils.SectionModifier;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStarted;
import org.jboss.arquillian.core.api.event.ManagerStopping;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManager {

    @Inject
    @ApplicationScoped
    private InstanceProducer<ExecutionReport> report;

    private ExecutionReport report() {
        return report.get();
    }

    public void observeFirstEvent(@Observes ManagerStarted arquillianDescriptor) {
        if (report() == null) {
            ExecutionReport executionReport = new ExecutionReport();
            report.set(executionReport);
        }
    }

    public void observeTestSuiteReports(@Observes ReportTestSuiteEvent event) {
        if (report().getTestSuiteReport() == null) {
            report().setTestSuiteReport(event.getSectionReport());
            report().register(event);
        } else {
            processSubclassEvent(ReportTestSuiteEvent.class, event, report().getTestSuiteReport());
        }
    }

    public void observeTestSuiteReports(@Observes ReportTestClassEvent event) {
        Class<? extends ReportEvent> eventClass = event.getClass();
        if (eventClass.isAssignableFrom(ReportTestClassEvent.class)) {
            SectionReport alreadyExists = report()
                .getSectionReportByIdentifier(new Identifier(ReportTestClassEvent.class, event.getIdentifier()));
            if (alreadyExists != null) {
                SectionModifier.merge(alreadyExists, event.getSectionReport());
            } else {
                report().getTestSuiteReport().getTestClassReports().add(event.getSectionReport());
            }
        } else {
            processEvent(ReportTestSuiteEvent.class, event, report().getTestSuiteReport());
        }
    }

    public void observeReportTestSuiteConfigurationEvents(@Observes ReportTestSuiteConfigurationEvent event) {
        processEvent(ReportTestSuiteConfigurationEvent.class, event,
                     report().getTestSuiteReport().getConfiguration());
    }

    private void processEvent(Class<? extends ReportEvent> parentEventClass, ReportEvent event,
        SectionReport rootSectionReport) {

        if (!event.getClass().isAssignableFrom(parentEventClass)) {
            processSubclassEvent(parentEventClass, event, rootSectionReport);
        } else {
            rootSectionReport.getSectionReports().add(event.getSectionReport());
        }

        report().register(event);
    }

    private void processSubclassEvent(Class<? extends ReportEvent> expectedReportEventClass, ReportEvent actualEvent,
        SectionReport rootSectionReport) {

        SectionReport alreadyExisting = report().getSectionReportByIdentifier(actualEvent);
        if (alreadyExisting != null) {
            SectionModifier.merge(alreadyExisting, actualEvent.getSectionReport());
            return;
        }

        ReportEvent parentEvent = actualEvent.getParentEvent();

        if (parentEvent != null) {
            Identifier parentIdentifier = new Identifier(parentEvent.getClass(), parentEvent.getIdentifier());
            SectionReport parentSection = report().getSectionReportByIdentifier(parentIdentifier);
            if (parentSection != null) {
                parentSection.getSectionReports().add(actualEvent.getSectionReport());
            } else {
                // todo
                return;
            }

        } else {
            Class<?> superclass = actualEvent.getClass().getSuperclass();
            if (!superclass.isAssignableFrom(expectedReportEventClass)) {
                rootSectionReport.getSectionReports().add(actualEvent.getSectionReport());
            }
        }
    }

    public void afterSuite(@Observes ManagerStopping stopping) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(report().getTestSuiteReport());
        System.out.println(json);

    }

}
