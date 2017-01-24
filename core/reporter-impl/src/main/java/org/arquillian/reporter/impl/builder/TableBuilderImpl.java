package org.arquillian.reporter.impl.builder;

import java.util.Arrays;
import java.util.List;

import org.arquillian.reporter.api.builder.TableBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.StringEntry;
import org.arquillian.reporter.api.model.entry.table.TableCell;
import org.arquillian.reporter.api.model.entry.table.TableEntry;
import org.arquillian.reporter.api.model.entry.table.TableRow;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableBuilderImpl implements TableBuilder {

    private TableEntry tableEntry;

    public TableBuilderImpl(String name) {
        tableEntry = new TableEntry(new UnknownStringKey(name));
    }

    public TableBuilderImpl(StringKey name) {
        tableEntry = new TableEntry(name);
    }

    public TableBuilderImpl(TableEntry tableEntry) {
        this.tableEntry = tableEntry;
    }

    @Override
    public TableBuilder addHeaderRows(TableRow... rows) {
        tableEntry.getRowHeader().getHeaderRows().addAll(Arrays.asList(rows));
        return this;
    }

    @Override
    public TableBuilder addHeaderRow(Entry... entries) {
        tableEntry.getRowHeader().getHeaderRows().add(createRow(entries));
        return this;
    }

    @Override
    public TableBuilder addHeaderRow(String... rowValues) {
        tableEntry.getRowHeader().getHeaderRows().add(createRow(rowValues));
        return this;
    }

    @Override
    public TableBuilder addHeaderRow(StringKey... rowValues) {
        tableEntry.getRowHeader().getHeaderRows().add(createRow(rowValues));
        return this;
    }

    @Override
    public
    TableBuilder addBodyRows(TableRow... rows) {
        tableEntry.getBody().getBodyRows().addAll(Arrays.asList(rows));
        return this;
    }

    @Override
    public TableBuilder addBodyRow(Entry... entries) {
        tableEntry.getBody().getBodyRows().add(createRow(entries));
        return this;
    }

    @Override
    public TableBuilder addBodyRow(String... rowValues) {
        tableEntry.getBody().getBodyRows().add(createRow(rowValues));
        return this;
    }

    @Override
    public TableBuilder addBodyRow(StringKey... rowValues) {
        tableEntry.getBody().getBodyRows().add(createRow(rowValues));
        return this;
    }

    @Override
    public TableBuilder addHeaderCell(TableCell cell) {
        addCellToRow(getLastHeaderRow(), cell);
        return this;
    }


    @Override
    public TableBuilder addHeaderCell(Entry entry) {
        addEntryToRow(getLastHeaderRow(), entry);
        return this;
    }

    @Override
    public TableBuilder addHeaderCell(String value) {
        addCellToRow(getLastHeaderRow(), new TableCell(value));
        return this;
    }

    @Override
    public TableBuilder addHeaderCell(StringKey value) {
        addCellToRow(getLastHeaderRow(), new TableCell(value));
        return this;
    }

    @Override
    public TableBuilder addBodyCell(TableCell cell) {
        addCellToRow(getLastBodyRow(), cell);
        return this;
    }

    @Override
    public TableBuilder addBodyCell(Entry entry) {
        addCellToRow(getLastBodyRow(), new TableCell(entry));
        return this;
    }

    @Override
    public TableBuilder addBodyCell(String value) {
        addCellToRow(getLastBodyRow(), new TableCell(value));
        return this;
    }

    @Override
    public TableBuilder addBodyCell(StringKey value) {
        addCellToRow(getLastBodyRow(), new TableCell(value));
        return this;
    }

    @Override
    public TableBuilder newHeaderRow() {
        tableEntry.getRowHeader().getHeaderRows().add(new TableRow());
        return this;
    }

    @Override
    public TableBuilder newBodyRow() {
        tableEntry.getBody().getBodyRows().add(new TableRow());
        return this;
    }

    @Override
    public TableEntry build() {
        return tableEntry;
    }

    @Override
    public TableBuilder copy() {
        new TableBuilderImpl(tableEntry.copy());
        return this;
    }

    // Utils
    private TableRow createRow(Entry... entries){
        TableRow row = new TableRow();
        Arrays
            .stream(entries)
            .forEach(entry -> addEntryToRow(row, entry));
        return row;
    }

    private TableRow createRow(String... rowValues){
        TableRow row = new TableRow();
        Arrays
            .stream(rowValues)
            .forEach(value -> addEntryToRow(row, new StringEntry(value)));
        tableEntry.getRowHeader().getHeaderRows().add(row);
        return row;
    }

    private TableRow createRow(StringKey... rowValues){
        TableRow row = new TableRow();
        Arrays.stream(rowValues)
            .forEach(value -> addEntryToRow(row, new StringEntry(value)));
        tableEntry.getRowHeader().getHeaderRows().add(row);
        return row;
    }

    private void addCellToRow(TableRow row, TableCell cell){
        row.getCells().add(cell);
    }

    private void addEntryToRow(TableRow row, Entry entry){
        addCellToRow(row, new TableCell(entry));
    }

    private TableRow getLastHeaderRow(){
        List<TableRow> headerRows = tableEntry.getRowHeader().getHeaderRows();
        if (headerRows.isEmpty()){
            headerRows.add(new TableRow());
        }
        return headerRows.get(headerRows.size());
    }

    private TableRow getLastBodyRow(){
        List<TableRow> bodyRows = tableEntry.getBody().getBodyRows();
        if (bodyRows.isEmpty()){
            bodyRows.add(new TableRow());
        }
        return bodyRows.get(bodyRows.size());
    }
}
