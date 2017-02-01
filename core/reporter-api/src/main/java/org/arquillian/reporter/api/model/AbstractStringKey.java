package org.arquillian.reporter.api.model;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class AbstractStringKey implements StringKey {

    private String value;
    private String description;
    private String icon;

    public AbstractStringKey() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public AbstractStringKey newInstance() {
        return new AbstractStringKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AbstractStringKey))
            return false;

        AbstractStringKey that = (AbstractStringKey) o;

        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getIcon() != null ? !getIcon().equals(that.getIcon()) : that.getIcon() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getValue() != null ? getValue().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "AbstractStringKey{" +
            "value='" + value + '\'' +
            ", description='" + description + '\'' +
            ", icon='" + icon + '\'' +
            '}';
    }
}
