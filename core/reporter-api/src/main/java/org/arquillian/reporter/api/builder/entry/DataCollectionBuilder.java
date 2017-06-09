package org.arquillian.reporter.api.builder.entry;

import java.util.List;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.data.DataCollectionEntry;
import org.arquillian.reporter.api.model.entry.data.DataItem;
import org.arquillian.reporter.api.model.entry.data.Label;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * A builder of {@link DataCollectionEntry}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface DataCollectionBuilder extends Builder {

    /**
     * Adds the given data value as a {@link DataItem} with the given {@link Label}s to the {@link DataCollectionEntry)
     * stored in this builder.
     *
     * @param dataValue A value to add as a {@link DataItem}
     * @param labels    Labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataItem(String dataValue, Label... labels);

    /**
     * Adds the given data value as a {@link DataItem} with the given {@link Label}s to the {@link DataCollectionEntry)
     * stored in this builder.
     *
     * @param dataValue A value to add as a {@link DataItem}
     * @param labels    Labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataItem(StringKey dataValue, Label... labels);

    /**
     * Adds the given data value as a {@link DataItem} with the given {@link Label}s to the {@link DataCollectionEntry)
     * stored in this builder.
     *
     * @param dataValue A value to add as a {@link DataItem}
     * @param labels    Labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataItem(Entry dataValue, Label... labels);

    /**
     * Adds the given data value as a {@link DataItem} with the given {@link Label}s to the {@link DataCollectionEntry)
     * stored in this builder.
     *
     * @param dataValue A value to add as a {@link DataItem}
     * @param labels    A list of labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataItem(String dataValue, List<Label> labels);

    /**
     * Adds the given data value as a {@link DataItem} with the given {@link Label}s to the {@link DataCollectionEntry)
     * stored in this builder.
     *
     * @param dataValue A value to add as a {@link DataItem}
     * @param labels    A list of labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataItem(StringKey dataValue, List<Label> labels);

    /**
     * Adds the given data value as a {@link DataItem} with the given {@link Label}s to the {@link DataCollectionEntry)
     * stored in this builder.
     *
     * @param dataValue A value to add as a {@link DataItem}
     * @param labels    A list of labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataItem(Entry dataValue, List<Label> labels);

    /**
     * Takes the given {@link Long} value as a byte count and transforms it into a human readable form using method
     * {@link ReporterUtils#humanReadableByteCount(Long, boolean)}. The resulting String is then added as a {@link DataItem}
     * with the given {@link Label}s into the {@link DataCollectionEntry) stored in this builder.
     *
     * @param data    A byte count to be added as a {@link DataItem} in a human readable form
     * @param decimal If the byte count is in decimal
     * @param labels  A list of labels to be set in the instance of {@link DataItem} as its labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addByteDataItem(Long data, boolean decimal, Label... labels);

    /**
     * Adds all {@link DataItem}s from {@link DataCollectionEntry} that is built from the given {@link DataCollectionBuilder}
     * into the {@link DataCollectionEntry) stored in this builder. All data items are stored with the given {@link Label}s
     * appended into the already existing list of labels of each item.
     * <p>
     * In other words, the built entry will be appended to the current one and all new items will be labeled using the
     * given set of labels
     * </p>
     *
     * @param dataCollectionBuilder An instance of {@link DataCollectionBuilder} whose {@link DataCollectionEntry} should be
     *                              appended to the current entry stored in this builder
     * @param labels                A set of labels which the retrieved {@link DataItem}s will be added with
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataCollection(DataCollectionBuilder dataCollectionBuilder, Label... labels);

    /**
     * Adds all {@link DataItem}s from the given {@link DataCollectionEntry} into the entry stored in this builder.
     * All data items are stored with the given {@link Label}s appended into the already existing list of labels of each item.
     * <p>
     * In other words, the given entry will be appended to the current one and all new items will be labeled using the
     * given set of labels
     * </p>
     *
     * @param dataCollection An instance of {@link DataCollectionEntry} which will be appended to the current entry stored in this builder
     * @param labels         A set of labels which the retrieved {@link DataItem}s will be added with
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataCollection(DataCollectionEntry dataCollection, Label... labels);

    /**
     * Adds all {@link DataItem}s from the given set of {@link DataCollectionEntry}(-ies) into the entry stored in this builder.
     * <p>
     * In other words, the given entries will be appended to the current one.
     * </p>
     *
     * @param dataCollections A set of {@link DataCollectionEntry}(-ies) which will be appended to the current entry stored in this builder
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry } instance
     */
    DataCollectionBuilder addDataCollection(DataCollectionEntry... dataCollections);

    /**
     * Adds all {@link DataItem}s from {@link DataCollectionEntry}(-ies) that are built from the given
     * {@link DataCollectionBuilder}s into the {@link DataCollectionEntry) stored in this builder.
     * <p>
     * In other words, the built entries will be appended to the current one
     * </p>
     *
     * @param dataCollectionBuilders A set of {@link DataCollectionBuilder}s whose {@link DataCollectionEntry}(-ies) should be
     *                               appended to the current entry stored in this builder
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry} instance
     */
    DataCollectionBuilder addDataCollection(DataCollectionBuilder... dataCollectionBuilders);

    /**
     * Goes through all {@link DataItem}s from the {@link DataCollectionEntry} stored in this builder and sets the given
     * parent {@link Label} to every label set in every item. If any label has already a parent label set, then the
     * label is set to the existing parent as its parent label.
     *
     * @param parentLabel A {@link Label} to be set as a parent label to all labels stored in the {@link DataCollectionEntry}
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry} instance
     */
    DataCollectionBuilder assignParentLabel(Label parentLabel);

    /**
     * Goes through all {@link DataItem}s from the {@link DataCollectionEntry} stored in this builder and adds the given
     * set of {@link Label}s to every item's list of labels.
     *
     * @param labels A set of {@link Label}s to be added to every {@link DataItem}'s list of of labels
     * @return The same instance of {@link DataCollectionBuilder } with modified {@link DataCollectionEntry} instance
     */
    DataCollectionBuilder assignLabel(Label... labels);

    /**
     * Returns a resulting instance of {@link DataCollectionEntry}
     *
     * @return A resulting instance of {@link DataCollectionEntry}
     */
    DataCollectionEntry build();
}
