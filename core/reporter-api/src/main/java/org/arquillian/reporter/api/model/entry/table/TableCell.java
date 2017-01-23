package org.arquillian.reporter.api.model.entry.table;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.StringEntry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableCell {

    private Entry cellContent;

    public TableCell() {
    }

    public TableCell(Entry cellContent) {
        this.cellContent = cellContent;
    }

    public TableCell(String cellContent) {
        this.cellContent = new StringEntry(cellContent);
    }

    public TableCell(StringKey cellContent) {
        this.cellContent = new StringEntry(cellContent);
    }

    public Entry getCellContent() {
        return cellContent;
    }

    public void setCellContent(Entry cellContent) {
        this.cellContent = cellContent;
    }

    public TableCell copy() {
        return new TableCell(cellContent);
    }
}
