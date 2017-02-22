package org.arquillian.reporter.api.builder.entry;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.table.TableCell;
import org.arquillian.reporter.api.model.entry.table.TableEntry;
import org.arquillian.reporter.api.model.entry.table.TableRow;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * A {@link Builder} providing an API that helps you to build a {@link TableEntry}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TableBuilder extends Builder {

    /**
     * Adds given rows to a list of rows in table header
     *
     * @param rows {@link TableRow}s to add
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderRows(TableRow... rows);

    /**
     * Creates a {@link TableRow} instance and adds the given entries into it. This row is then added into the list of
     * rows in table header
     *
     * @param entries {@link Entry}(-ies) that should be contained in the new header row
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderRow(Entry... entries);

    /**
     * Creates a {@link TableRow} instance and adds the given String values as {@link UnknownStringKey}(-ies) into it.
     * This row is then added into the list of rows in table header
     *
     * @param rowValues String values that should be contained in the new header row
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderRow(String... rowValues);

    /**
     * Creates a {@link TableRow} instance and adds the given {@link StringKey}(-ies) into it.
     * This row is then added into the list of rows in table header
     *
     * @param rowValues {@link StringKey}(-ies) that should be contained in the new header row
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderRow(StringKey... rowValues);

    /**
     * Adds given rows to a list of rows in table body
     *
     * @param rows {@link TableRow}s to add
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyRows(TableRow... rows);

    /**
     * Creates a {@link TableRow} instance and adds the given entries into it. This row is then added into the list of
     * rows in the table body
     *
     * @param entries {@link Entry}(-ies) that should be contained in the new body row
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyRow(Entry... entries);

    /**
     * Creates a {@link TableRow} instance and adds the given String values as {@link UnknownStringKey}(-ies) into it.
     * This row is then added into the list of rows in the table body
     *
     * @param rowValues String values that should be contained in the new body row
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyRow(String... rowValues);

    /**
     * Creates a {@link TableRow} instance and adds the given {@link StringKey}(-ies) into it.
     * This row is then added into the list of rows in table body
     *
     * @param rowValues {@link StringKey}(-ies) that should be contained in the new body row
     * @return the same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyRow(StringKey... rowValues);

    /**
     * Adds the given {@link TableCell} to the latest header row
     *
     * @param cell A {@link TableCell} to be added into the latest header row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderCell(TableCell cell);

    /**
     * Adds the given {@link Entry} to the latest header row as a {@link TableCell}
     *
     * @param entry A {@link Entry} to be added into the latest header row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderCell(Entry entry);

    /**
     * Adds the given String value to the latest header row as a {@link UnknownStringKey} contained within {@link TableCell}
     *
     * @param value A String value to be added into the latest header row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderCell(String value);

    /**
     * Adds the given {@link StringKey} to the latest header row as a {@link TableCell}
     *
     * @param value A {@link StringKey} to be added into the latest header row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addHeaderCell(StringKey value);

    /**
     * Adds the given {@link TableCell} to the latest body row
     *
     * @param cell A {@link TableCell} to be added into the latest body row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyCell(TableCell cell);

    /**
     * Adds the given {@link Entry} to the latest body row as a {@link TableCell}
     *
     * @param entry A {@link Entry} to be added into the latest body row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyCell(Entry entry);

    /**
     * Adds the given String value to the latest body row as a {@link UnknownStringKey} contained within {@link TableCell}
     *
     * @param value A String value to be added into the latest body row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyCell(String value);

    /**
     * Adds the given {@link StringKey} to the latest body row as a {@link TableCell}
     *
     * @param value A {@link StringKey} to be added into the latest body row
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder addBodyCell(StringKey value);

    /**
     * Creates a new instance of {@link TableRow} and adds it into the list of header rows
     *
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder newHeaderRow();

    /**
     * Creates a new instance of {@link TableRow} and adds it into the list of body rows
     *
     * @return The same instance of {@link TableBuilder} with modified {@link TableEntry} instance
     */
    TableBuilder newBodyRow();

    /**
     * Finishes all necessary building actions and returns the resulting {@link TableEntry} instance.
     *
     * @return A built {@link TableEntry} instance
     */
    TableEntry build();

    /**
     * Creates and returns a deep copy of the {@link TableBuilder} instance
     *
     * @return A deep copy of the {@link TableBuilder} instance
     */
    TableBuilder copy();
}
