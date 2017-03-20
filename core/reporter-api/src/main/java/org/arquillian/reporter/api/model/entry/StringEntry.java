package org.arquillian.reporter.api.model.entry;

import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * An {@link Entry} representing a string
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class StringEntry implements Entry {

    private StringKey content;

    /**
     * Creates an instance of {@link StringEntry} with the given {@link StringKey} as a content
     *
     * @param content A {@link StringKey} that should be content of the {@link StringEntry}
     */
    public StringEntry(StringKey content) {
        this.content = content;
    }

    /**
     * Creates an instance of {@link StringEntry} with the given {@link StringKey}
     *
     * @param content A String that should be stored as an {@link UnknownStringKey} content of the {@link StringEntry}
     */
    public StringEntry(String content) {
        this.content = new UnknownStringKey(content);
    }

    /**
     * Returns the content
     *
     * @return The content
     */
    public StringKey getContent() {
        return content;
    }

    /**
     * Sets the given content
     *
     * @param content content to set
     */
    public void setContent(StringKey content) {
        this.content = content;
    }

    public Entry outputEntry(){
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        StringEntry that = (StringEntry) o;

        if (getContent() != null ? !getContent().equals(that.getContent()) : that.getContent() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getContent() != null ? getContent().hashCode() : 0;
    }
}
