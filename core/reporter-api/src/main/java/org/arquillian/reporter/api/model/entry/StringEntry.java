package org.arquillian.reporter.api.model.entry;

import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class StringEntry implements Entry{

    private StringKey entry;

    public StringEntry(StringKey entry) {
        this.entry = entry;
    }

    public StringEntry(String entry) {
        this.entry = new UnknownStringKey(entry);
    }

    public StringKey getEntry() {
        return entry;
    }

    public void setEntry(StringKey entry) {
        this.entry = entry;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        StringEntry that = (StringEntry) o;

        if (getEntry() != null ? !getEntry().equals(that.getEntry()) : that.getEntry() != null)
            return false;

        return true;
    }

    @Override public int hashCode() {
        return getEntry() != null ? getEntry().hashCode() : 0;
    }
}
