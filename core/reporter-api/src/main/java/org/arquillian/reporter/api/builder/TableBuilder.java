package org.arquillian.reporter.api.builder;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.table.TableCell;
import org.arquillian.reporter.api.model.entry.table.TableEntry;
import org.arquillian.reporter.api.model.entry.table.TableRow;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TableBuilder extends Builder{

    TableBuilder addHeaderRows(TableRow... rows);
    TableBuilder addHeaderRow(Entry... entries);
    TableBuilder addHeaderRow(String... rowValues);
    TableBuilder addHeaderRow(StringKey... rowValues);

    TableBuilder addBodyRows(TableRow... rows);
    TableBuilder addBodyRow(Entry... entries);
    TableBuilder addBodyRow(String... rowValues);
    TableBuilder addBodyRow(StringKey... rowValues);

    TableBuilder addHeaderCell(TableCell cell);
    TableBuilder addHeaderCell(Entry entry);
    TableBuilder addHeaderCell(String value);
    TableBuilder addHeaderCell(StringKey value);

    TableBuilder addBodyCell(TableCell cell);
    TableBuilder addBodyCell(Entry entry);
    TableBuilder addBodyCell(String value);
    TableBuilder addBodyCell(StringKey value);

    TableBuilder newHeaderRow();
    TableBuilder newBodyRow();

    TableEntry build();

    TableBuilder copy();
}
