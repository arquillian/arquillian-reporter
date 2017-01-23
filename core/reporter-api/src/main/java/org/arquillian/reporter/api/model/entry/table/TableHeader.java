package org.arquillian.reporter.api.model.entry.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableHeader {

    private List<TableRow> headerRows = new ArrayList<>();

    public TableHeader() {
    }

    public TableHeader(List<TableRow> headerRows) {
        this.headerRows = headerRows;
    }

    public List<TableRow> getHeaderRows() {
        return headerRows;
    }

    public void setHeaderRows(List<TableRow> headerRows) {
        this.headerRows = headerRows;
    }

    public TableHeader copy() {
        return new TableHeader(headerRows.stream().map(row -> row.copy()).collect(Collectors.toList()));
    }
}
