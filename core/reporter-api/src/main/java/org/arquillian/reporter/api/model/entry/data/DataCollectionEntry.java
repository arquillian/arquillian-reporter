package org.arquillian.reporter.api.model.entry.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * A collection of {@link DataItem}s labeled by a lists of {@link Label}s
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DataCollectionEntry implements Entry {

    private StringKey name;
    private List<DataItem> dataList = new ArrayList<>();

    public DataCollectionEntry() {
    }

    public DataCollectionEntry(StringKey name) {
        this.name = name;
    }

    public DataCollectionEntry(String name) {
        this.name = new UnknownStringKey(name);
    }

    public DataCollectionEntry(List<DataItem> dataList) {
        this.dataList = dataList;
    }

    public List<DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataItem> dataList) {
        this.dataList = dataList;
    }

    public Entry outputEntry(){
        OutputDataCollectionEntry outputDataCollectionEntry = new OutputDataCollectionEntry(name);

        dataList.stream().forEach(dataItem -> {
            List<Label> labels = dataItem.getLabels();
            OutputDataItem outputDataItem = new OutputDataItem(dataItem.getDataValue());
            List<OutputDataSubCollection> subCollections = outputDataCollectionEntry.getSubCollections();

            Label superLabel = labels.stream().reduce((first, second) -> assignToLastParent(first, second)).get();
            OutputDataSubCollection toAdd = matchOrCreate(superLabel, subCollections);
            toAdd.getDataList().add(outputDataItem);
        });
        return outputDataCollectionEntry;
    }

    private Label assignToLastParent(Label label, Label newParent) {
        assignParentLabel(label, newParent);
        return label;
    }

    private void assignParentLabel(Label label, Label parentLabel) {
        if (label.getParentLabel() == null) {
            label.setParentLabel(new Label(parentLabel));
        } else {
            assignParentLabel(label.getParentLabel(), parentLabel);
        }
    }

    private OutputDataSubCollection matchOrCreate(Label label, List<OutputDataSubCollection> outputDataSubCollection) {
        List<OutputDataSubCollection> subCollections = outputDataSubCollection;

        if (label.getParentLabel() != null) {
            subCollections = matchOrCreate(label.getParentLabel(), outputDataSubCollection).getSubCollections();
        }

        Optional<OutputDataSubCollection> foundSubCollection = subCollections
            .stream()
            .filter(collection -> collection.getLabelName().equals(label.getName()))
            .findFirst();

        if (foundSubCollection.isPresent()) {
            return foundSubCollection.get();
        } else {
            OutputDataSubCollection subCollection = new OutputDataSubCollection(label.getName());
            subCollections.add(subCollection);
            return subCollection;
        }
    }

    class OutputDataCollectionEntry implements Entry {

        private StringKey entryName;
        private List<OutputDataSubCollection> subCollections = new ArrayList<>();

        OutputDataCollectionEntry(StringKey entryName) {
            this.entryName = entryName;
        }

        public StringKey getEntryName() {
            return entryName;
        }

        public void setEntryName(StringKey entryName) {
            this.entryName = entryName;
        }

        public List<OutputDataSubCollection> getSubCollections() {
            return subCollections;
        }

        public void setSubCollections(
            List<OutputDataSubCollection> subCollections) {
            this.subCollections = subCollections;
        }

        @Override
        public Entry outputEntry() {
            return this;
        }
    }

    class OutputDataSubCollection {

        private StringKey labelName;
        private List<OutputDataSubCollection> subCollections = new ArrayList<>();
        private List<OutputDataItem> dataList = new ArrayList<>();

        OutputDataSubCollection(StringKey labelName) {
            this.labelName = labelName;
        }

        public StringKey getLabelName() {
            return labelName;
        }

        public void setLabelName(StringKey labelName) {
            this.labelName = labelName;
        }

        public List<OutputDataSubCollection> getSubCollections() {
            return subCollections;
        }

        public void setSubCollections(List<OutputDataSubCollection> subCollections) {
            this.subCollections = subCollections;
        }

        public List<OutputDataItem> getDataList() {
            return dataList;
        }

        public void setDataList(
            List<OutputDataItem> dataList) {
            this.dataList = dataList;
        }
    }

    class OutputDataItem {

        private Entry dataValue;

        OutputDataItem(Entry dataValue) {
            this.dataValue = dataValue;
        }

        public Entry getDataValue() {
            return dataValue;
        }

        public void setDataValue(Entry dataValue) {
            this.dataValue = dataValue;
        }
    }
}
