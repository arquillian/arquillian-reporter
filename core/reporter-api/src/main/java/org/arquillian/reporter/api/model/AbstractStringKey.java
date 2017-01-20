package org.arquillian.reporter.api.model;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class AbstractStringKey implements StringKey{

    private String value;
    private String description;
    private String icon;

    public AbstractStringKey(){

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public AbstractStringKey newInstance(){
        return new AbstractStringKey();
    }
}
