package org.arquillian.reporter.api.model.entry.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableBody {

    private List<TableRow> bodyRows = new ArrayList<>();

    public TableBody() {
    }

    public TableBody(List<TableRow> bodyRows) {
        this.bodyRows = bodyRows;
    }

    public List<TableRow> getBodyRows() {
        return bodyRows;
    }

    public void setBodyRows(List<TableRow> bodyRows) {
        this.bodyRows = bodyRows;
    }

    public TableBody copy() {
        return new TableBody(bodyRows.stream().map(row -> row.copy()).collect(Collectors.toList()));
    }
}
