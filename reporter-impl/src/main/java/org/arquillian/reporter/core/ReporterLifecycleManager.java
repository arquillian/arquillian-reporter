package org.arquillian.reporter.core;

import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteConfigurationEvent;
import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.utils.SectionModifier;
import org.arquillian.reporter.api.utils.Validate;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterLifecycleManager {

    @Inject
    private Instance<ExecutionReport> report;

    public void observeAllReportEvent(@Observes EventContext<ReportEvent> event) {
        Section section = event.getEvent().getSection();
        String identifier = section.getIdentifier();

        if (Validate.isNotEmpty(identifier)) {
            if (report.get().isIdentifierRegistered(identifier)) {
                Section registeredSection = report.get().getRegisteredSection(identifier);
                SectionModifier.merge(registeredSection, section);
                goThroughAllSubsection(section);
                return;
            } else {
                report.get().register(section);
            }
        }

        goThroughAllSubsection(section);
        event.proceed();
    }

    private void goThroughAllSubsection(Section section) {
        section
            .getSections()
            .stream()
            .filter(s -> Validate.isNotEmpty(s.getIdentifier()))
            .forEach(s -> report.get().register(s));
    }

    public void observeReportTestSuiteConfigurationEvents(@Observes ReportTestSuiteConfigurationEvent event) {
        report.get().getTestSuiteReport().getConfiguration().addSection(event.getSection());
    }

}
