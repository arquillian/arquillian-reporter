package org.arquillian.reporter.api.model;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface StringKey {

    String getValue();

    void setValue(String value);

    String getDescription();

    void setDescription(String description);

    String getIcon();

    void setIcon(String icon);

    StringKey newInstance();
}
