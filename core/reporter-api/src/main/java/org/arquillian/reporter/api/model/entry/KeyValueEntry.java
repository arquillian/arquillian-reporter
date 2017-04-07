package org.arquillian.reporter.api.model.entry;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * An {@link Entry} representing a key-value pair. The key is stored as a {@link StringKey} and value as an {@link Entry}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class KeyValueEntry implements Entry {

    private StringKey key;
    private Entry value;

    public StringKey getKey() {
        return key;
    }

    /**
     * Creates an instance of {@link KeyValueEntry} with the given key and value
     *
     * @param key   An {@link StringKey} to be added as a key
     * @param value An {@link Entry} to be added as a value
     */
    public KeyValueEntry(StringKey key, Entry value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Creates an instance of {@link KeyValueEntry} with the given key and value
     *
     * @param key   An {@link StringKey} to be added as a key
     * @param value A {@link StringKey} to be added as an {@link StringEntry} value
     */
    public KeyValueEntry(StringKey key, StringKey value) {
        this.key = key;
        this.value = new StringEntry(value);
    }

    /**
     * Creates an instance of {@link KeyValueEntry} with the given key and value
     *
     * @param key   An {@link StringKey} to be added as a key
     * @param value A String value to be added as an {@link UnknownStringKey} in an {@link StringEntry} value
     */
    public KeyValueEntry(StringKey key, String value) {
        this.key = key;
        this.value = new StringEntry(value);
    }

    /**
     * Creates an instance of {@link KeyValueEntry} with the given key and value
     *
     * @param key   A String value to be added as an {@link UnknownStringKey} key
     * @param value A String value to be added as an {@link UnknownStringKey} in an {@link StringEntry} value
     */
    public KeyValueEntry(String key, String value) {
        this.key = new UnknownStringKey(key);
        this.value = new StringEntry(value);
    }

    @Override
    public String toString() {
        return "KeyValueEntry{" +
            "key=" + key +
            ", value=" + value +
            '}';
    }
}
