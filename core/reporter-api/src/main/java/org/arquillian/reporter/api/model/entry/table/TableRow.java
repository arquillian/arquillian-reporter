package org.arquillian.reporter.api.model.entry.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableRow {

    private List<TableCell> cells = new ArrayList<>();

    public TableRow() {
    }

    public TableRow(List<TableCell> cells) {
        this.cells = cells;
    }

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

    public TableRow copy() {
        return new TableRow(cells.stream().map(cell -> cell.copy()).collect(Collectors.toList()));
    }
}
