package org.arquillian.reporter.api.model.entry;

/**
 * An {@link Entry} representing a link
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class LinkEntry implements Entry {

    public Entry outputEntry(){
        return this;
    }
}
