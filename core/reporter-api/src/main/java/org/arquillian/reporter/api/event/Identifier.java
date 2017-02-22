package org.arquillian.reporter.api.event;

/**
 * An identifier of {@link SectionEvent} used in section tree to identify nodes
 *
 * @param <SECTIONTYPE> Type of {@link SectionEvent} this identifier identifies
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Identifier<SECTIONTYPE extends SectionEvent> {

    private Class<SECTIONTYPE> sectionEventClass;
    private String sectionId;

    /**
     * Creates an instance of {@link Identifier} with the given {@link SectionEvent} class and sectionId that
     * should be unique in this section.
     *
     * @param sectionEventClass A {@link SectionEvent} class
     * @param sectionId         A string identifier that should be unique in this section
     */
    public Identifier(Class<SECTIONTYPE> sectionEventClass, String sectionId) {
        this.sectionEventClass = sectionEventClass;
        this.sectionId = sectionId;
    }

    /**
     * Returns the set {@link SectionEvent} class
     *
     * @return The set {@link SectionEvent} class
     */
    public Class<?> getSectionEventClass() {
        return sectionEventClass;
    }

    /**
     * Sets the given {@link SectionEvent} class
     *
     * @param sectionEventClass A {@link SectionEvent} class to be set
     */
    public void setSectionEventClass(Class<SECTIONTYPE> sectionEventClass) {
        this.sectionEventClass = sectionEventClass;
    }

    /**
     * Returns the set id of the section node
     *
     * @return The set id of the section node
     */
    public String getSectionId() {
        return sectionId;
    }

    /**
     * Sets the given id of the section node
     *
     * @param sectionId A string id of the section node
     */
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

    @Override
    public String toString() {
        return "Identifier{" +
            "sectionEventClass=" + sectionEventClass +
            ", sectionId='" + sectionId + '\'' +
            '}';
    }
}
