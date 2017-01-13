package org.arquillian.reporter.api.model.entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class KeyValueEntry implements Entry {

    private String key;
    private Entry value;

    public KeyValueEntry(String key, Entry value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueEntry(String key, String value) {
        this.key = key;
        this.value = new StringEntry(value);
    }

    public KeyValueEntry(String key, boolean value) {
        this.key = key;
        this.value = new StringEntry(String.valueOf(value));
    }
}
