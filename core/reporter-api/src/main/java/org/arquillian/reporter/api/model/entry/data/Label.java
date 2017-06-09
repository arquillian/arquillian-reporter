package org.arquillian.reporter.api.model.entry.data;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * A label that brings additional semantic to a {@link DataItem}. Consist of its name and a parent label
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Label {

    private StringKey name;

    private Label parentLabel;

    public Label(String name) {
        this.name = new UnknownStringKey(name);
    }

    public Label(StringKey name) {
        this.name = name;
    }

    public Label(Label label){
        if (label != null) {
            name = label.getName();
            if (label.getParentLabel() != null) {
                parentLabel = new Label(label.getParentLabel());
            }
        }
    }

    public Label(String name, Label parentLabel) {
        this.name = new UnknownStringKey(name);
        this.parentLabel = parentLabel;
    }

    public Label(StringKey name, Label parentLabel) {
        this.name = name;
        this.parentLabel = parentLabel;
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }

    public Label getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(Label parentLabel) {
        this.parentLabel = parentLabel;
    }

    /**
     * Takes the given parent {@link Label} and sets it as a parent label to a new cloned instance of this label
     *
     * @param parentLabel A parent label
     * @return A new cloned instance of this label with the given label set as its parent label
     */
    public Label parent(Label parentLabel){
        Label newLabel = new Label(name);
        newLabel.setParentLabel(new Label(parentLabel));
        return newLabel;
    }
}
