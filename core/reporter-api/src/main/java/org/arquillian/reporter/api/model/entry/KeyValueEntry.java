package org.arquillian.reporter.api.model.entry;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class KeyValueEntry implements Entry {

    private StringKey key;
    private Entry value;

    public KeyValueEntry(StringKey key, Entry value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueEntry(StringKey key, String value) {
        this.key = key;
        this.value = new StringEntry(value);
    }

    public KeyValueEntry(String key, String value) {
        this.key = new UnknownStringKey(key);
        this.value = new StringEntry(value);
    }

    public KeyValueEntry(StringKey key, boolean value) {
        this.key = key;
        this.value = new StringEntry(String.valueOf(value));
    }
}
