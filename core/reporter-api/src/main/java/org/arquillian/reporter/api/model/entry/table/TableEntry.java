package org.arquillian.reporter.api.model.entry.table;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableEntry implements Entry {

    private StringKey name;

    private TableHeader rowHeader = new TableHeader();

    private TableHeader columnHeader = new TableHeader();

    private TableBody body = new TableBody();

    private TableFoot rowFoot = new TableFoot();

    private TableFoot columnFoot = new TableFoot();

    public TableEntry(StringKey name) {
        this.name = name;
    }

    public TableEntry(StringKey name, TableHeader rowHeader,
        TableHeader columnHeader, TableBody body, TableFoot rowFoot,
        TableFoot columnFoot) {
        this.name = name;
        this.rowHeader = rowHeader;
        this.columnHeader = columnHeader;
        this.body = body;
        this.rowFoot = rowFoot;
        this.columnFoot = columnFoot;
    }

    public TableHeader getRowHeader() {
        return rowHeader;
    }

    public void setRowHeader(TableHeader rowHeader) {
        this.rowHeader = rowHeader;
    }

    public TableHeader getColumnHeader() {
        return columnHeader;
    }

    public void setColumnHeader(TableHeader columnHeader) {
        this.columnHeader = columnHeader;
    }

    public TableBody getBody() {
        return body;
    }

    public void setBody(TableBody body) {
        this.body = body;
    }

    public TableFoot getRowFoot() {
        return rowFoot;
    }

    public void setRowFoot(TableFoot rowFoot) {
        this.rowFoot = rowFoot;
    }

    public TableFoot getColumnFoot() {
        return columnFoot;
    }

    public void setColumnFoot(TableFoot columnFoot) {
        this.columnFoot = columnFoot;
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }

    public TableEntry copy() {
        return new TableEntry(name, rowHeader.copy(), columnHeader.copy(), body.copy(), rowFoot.copy(),
                              columnFoot.copy());
    }
}
