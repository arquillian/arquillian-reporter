package org.arquillian.reporter.api.model;

/**
 * An interface representing any string contained in report
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface StringKey {

    /**
     * Returns the actual value
     *
     * @return The actual value
     */
    String getValue();

    /**
     * Sets the given value
     *
     * @param value A string value to be set
     */
    void setValue(String value);

    /**
     * Returns a description of this {@link StringKey}
     *
     * @return A description of this {@link StringKey}
     */
    String getDescription();

    /**
     * Sets the given string as a description of this {@link StringKey}
     *
     * @param description A description to be set
     */
    void setDescription(String description);

    /**
     * Returns an icon that is designed for this {@link StringKey}
     *
     * @return An icon that is designed for this {@link StringKey}
     */
    String getIcon();

    /**
     * Sets the string as an icon of this {@link StringKey}
     *
     * @param icon An icon to be set
     */
    void setIcon(String icon);
}
