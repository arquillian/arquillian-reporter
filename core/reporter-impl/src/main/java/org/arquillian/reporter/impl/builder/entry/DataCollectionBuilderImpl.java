package org.arquillian.reporter.impl.builder.entry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.arquillian.reporter.api.builder.entry.DataCollectionBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.StringEntry;
import org.arquillian.reporter.api.model.entry.data.DataCollectionEntry;
import org.arquillian.reporter.api.model.entry.data.DataItem;
import org.arquillian.reporter.api.model.entry.data.Label;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DataCollectionBuilderImpl implements DataCollectionBuilder {

    private DataCollectionEntry dataCollectionEntry;

    public DataCollectionBuilderImpl() {
        dataCollectionEntry = new DataCollectionEntry();
    }

    public DataCollectionBuilderImpl(String name) {
        dataCollectionEntry = new DataCollectionEntry(name);
    }

    public DataCollectionBuilderImpl(StringKey name) {
        dataCollectionEntry = new DataCollectionEntry(name);
    }

    @Override
    public DataCollectionBuilder addDataItem(String dataValue, Label... labels) {
        return addDataItem(new UnknownStringKey(dataValue), labels);
    }

    @Override
    public DataCollectionBuilder addDataItem(StringKey dataValue, Label... labels) {
        return addDataItem(new StringEntry(dataValue), labels);
    }

    @Override
    public DataCollectionBuilder addDataItem(Entry dataValue, Label... labels) {
        return addDataItem(dataValue, Arrays.asList(labels));
    }

    @Override
    public DataCollectionBuilder addDataItem(String dataValue, List<Label> labels) {
        return addDataItem(new UnknownStringKey(dataValue), labels);
    }

    @Override
    public DataCollectionBuilder addDataItem(StringKey dataValue, List<Label> labels) {
        return addDataItem(new StringEntry(dataValue), labels);
    }

    @Override
    public DataCollectionBuilder addDataItem(Entry dataValue, List<Label> labels) {
        List<Label> labelsToAdd = labels.stream().map(Label::new).collect(Collectors.toList());
        dataCollectionEntry.getDataList().add(new DataItem(dataValue, labelsToAdd));
        return this;
    }

    @Override
    public DataCollectionBuilder addByteDataItem(Long data, boolean decimal, Label... labels) {
        String dataValue = ReporterUtils.humanReadableByteCount(data, decimal);
        List<Label> labelsToAdd = Arrays.stream(labels).map(Label::new).collect(Collectors.toList());
        dataCollectionEntry.getDataList().add(new DataItem(dataValue, labelsToAdd));
        return this;
    }

    @Override
    public DataCollectionBuilder addDataCollection(DataCollectionBuilder dataCollectionBuilder, Label... labels) {
        return addDataCollection(dataCollectionBuilder.build(), labels);
    }

    @Override
    public DataCollectionBuilder addDataCollection(DataCollectionEntry dataCollection, Label... labels) {
        List<Label> labelsToAdd = Arrays.stream(labels).map(Label::new).collect(Collectors.toList());
        dataCollection.getDataList().forEach(dataItem -> {
            if (labelsToAdd != null && !labelsToAdd.isEmpty()) {
                dataItem.getLabels().addAll(labelsToAdd);
            }
            dataCollectionEntry.getDataList().add(dataItem);
        });
        return this;
    }

    @Override
    public DataCollectionBuilder addDataCollection(DataCollectionEntry... dataCollections) {
        Arrays.stream(dataCollections).forEach(collection -> addDataCollection(collection, new Label[]{}));
        return this;
    }

    @Override
    public DataCollectionBuilder addDataCollection(DataCollectionBuilder... dataCollectionBuilders) {
        Arrays.stream(dataCollectionBuilders).forEach(builder -> addDataCollection(builder.build(), new Label[]{}));
        return this;
    }

    @Override
    public DataCollectionBuilder assignParentLabel(Label parentLabel) {
        dataCollectionEntry.getDataList()
            .forEach(dataItem -> dataItem.getLabels()
                .forEach(label -> assignParentLabel(label, parentLabel)));
        return this;
    }

    private void assignParentLabel(Label label, Label parentLabel) {
        if (label.getParentLabel() == null) {
            label.setParentLabel(new Label(parentLabel));
        } else {
            assignParentLabel(label.getParentLabel(), parentLabel);
        }
    }

    @Override
    public DataCollectionBuilder assignLabel(Label... labels) {
        dataCollectionEntry.getDataList().forEach(dataItem -> dataItem.getLabels().addAll(Arrays.asList(labels)));
        return this;
    }

    @Override
    public DataCollectionEntry build() {
        return dataCollectionEntry;
    }
}
