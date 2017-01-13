package org.arquillian.reporter.impl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Identifier {

    private Class<?> reportEventClass;
    private String identifier;

    public Identifier(Class<?> reportEventClass, String identifier) {
        this.reportEventClass = reportEventClass;
        this.identifier = identifier;
    }

    public Class<?> getReportEventClass() {
        return reportEventClass;
    }

    public void setReportEventClass(Class<?> reportEventClass) {
        this.reportEventClass = reportEventClass;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Identifier that = (Identifier) o;

        if (getReportEventClass() != null ? !getReportEventClass().equals(that.getReportEventClass()) :
            that.getReportEventClass() != null)
            return false;
        if (getIdentifier() != null ? !getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getReportEventClass() != null ? getReportEventClass().hashCode() : 0;
        result = 31 * result + (getIdentifier() != null ? getIdentifier().hashCode() : 0);
        return result;
    }
}
