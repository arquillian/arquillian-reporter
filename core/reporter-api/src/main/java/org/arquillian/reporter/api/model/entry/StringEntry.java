package org.arquillian.reporter.api.model.entry;

import org.arquillian.reporter.api.builder.impl.UnknownStringKey;
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
}
