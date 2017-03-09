package org.arquillian.reporter.api.model.entry.data;

import java.util.ArrayList;
import java.util.List;

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
}
