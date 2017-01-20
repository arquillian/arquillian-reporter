package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.model.AbstractStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class UnknownStringKey extends AbstractStringKey {

    private String keyValue;

    public UnknownStringKey(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
