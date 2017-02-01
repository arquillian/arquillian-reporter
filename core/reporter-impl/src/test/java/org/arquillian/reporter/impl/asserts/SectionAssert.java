package org.arquillian.reporter.impl.asserts;

import java.util.Objects;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.assertj.core.api.AbstractAssert;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionAssert extends AbstractAssert<SectionAssert, SectionEvent> {

    public SectionAssert(SectionEvent actual) {
        super(actual, SectionAssert.class);
    }

    public static SectionAssert assertThatSection(SectionEvent actual) {
        return new SectionAssert(actual);
    }

    public SectionAssert hasSectionId(String id) {
        isNotNull();

        if (!Objects.equals(actual.getSectionId(), id)) {
            failWithMessage("Expected id of the section should be <%s> but was <%s>", id, actual.getSectionId());
        }
        return this;
    }

    public SectionAssert hasReportOfTypeThatIsAssignableFrom(Class<?> superClass) {
        isNotNull();

        if (!actual.getReportTypeClass().isAssignableFrom(superClass)) {
            failWithMessage("The set report-type-class <%s> of the section should be assignable from <%s>",
                            actual.getReportTypeClass(), superClass);
        }
        return this;
    }

    public SectionAssert hasReportEqualTo(AbstractReport report) {
        isNotNull();

        if (!Objects.equals(actual.getReport(), report)) {
            failWithMessage("Expected report stored in the section should be <%s> but was <%s>", report,
                            actual.getReport());
        }
        return this;
    }

    public SectionAssert isProccessed() {
        isNotNull();
        if (!actual.isProcessed()) {
            failWithMessage("The section should be processed but the value of the variable \"processed\" is <%s>",
                            actual.isProcessed());
        }
        return this;
    }
}
