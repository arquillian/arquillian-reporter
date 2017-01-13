package org.arquillian.reporter.api.model.entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class StringEntry implements Entry{

    private String entry;

    public StringEntry(String entry) {
        this.entry = entry;
    }
}
