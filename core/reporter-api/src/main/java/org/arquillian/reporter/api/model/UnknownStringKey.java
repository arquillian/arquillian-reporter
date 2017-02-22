package org.arquillian.reporter.api.model;

/**
 * A {@link StringKey} implementation used for all strings that are not defined using any real implementation of {@link StringKey}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class UnknownStringKey extends AbstractStringKey {

    public UnknownStringKey(String stringValue) {
        setValue(stringValue);
    }
}
