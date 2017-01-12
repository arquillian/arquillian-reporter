package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEvent  {

    private Section section;

    public ReportEvent(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
