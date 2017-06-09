package org.arquillian.reporter.api.model.entry.data;

import java.util.Arrays;
import java.util.List;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.StringEntry;

/**
 * An item of data stored as an {@link Entry} with associated list of {@link Label}s
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DataItem {

    private Entry dataValue;

    private List<Label> labels;

    public DataItem(String dataValue, Label... labels) {
        this.dataValue = new StringEntry(dataValue);
        this.labels = Arrays.asList(labels);
    }

    public DataItem(StringKey dataValue, Label... labels) {
        this.dataValue = new StringEntry(dataValue);
        this.labels = Arrays.asList(labels);
    }

    public DataItem(Entry dataValue, Label... labels) {
        this.dataValue = dataValue;
        this.labels = Arrays.asList(labels);
    }

    public DataItem(String dataValue, List<Label> labels) {
        this.dataValue = new StringEntry(dataValue);
        this.labels = labels;
    }

    public DataItem(StringKey dataValue, List<Label> labels) {
        this.dataValue = new StringEntry(dataValue);
        this.labels = labels;
    }

    public DataItem(Entry dataValue, List<Label> labels) {
        this.dataValue = dataValue;
        this.labels = labels;
    }

    public Entry getDataValue() {
        return dataValue;
    }

    public void setDataValue(Entry dataValue) {
        this.dataValue = dataValue;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}
