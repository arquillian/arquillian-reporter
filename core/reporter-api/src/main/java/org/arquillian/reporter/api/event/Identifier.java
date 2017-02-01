package org.arquillian.reporter.api.event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Identifier<SECTIONTYPE extends SectionEvent> {

    private Class<SECTIONTYPE> sectionEventClass;
    private String sectionId;

    public Identifier(Class<SECTIONTYPE> sectionEventClass, String sectionId) {
        this.sectionEventClass = sectionEventClass;
        this.sectionId = sectionId;
    }

    public Class<?> getSectionEventClass() {
        return sectionEventClass;
    }

    public void setSectionEventClass(Class<SECTIONTYPE> sectionEventClass) {
        this.sectionEventClass = sectionEventClass;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Identifier that = (Identifier) o;

        if (getSectionEventClass() != null ? !getSectionEventClass().equals(that.getSectionEventClass()) :
            that.getSectionEventClass() != null)
            return false;
        if (getSectionId() != null ? !getSectionId().equals(that.getSectionId()) : that.getSectionId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getSectionEventClass() != null ? getSectionEventClass().hashCode() : 0;
        result = 31 * result + (getSectionId() != null ? getSectionId().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Identifier{" +
            "sectionEventClass=" + sectionEventClass +
            ", sectionId='" + sectionId + '\'' +
            '}';
    }
}
