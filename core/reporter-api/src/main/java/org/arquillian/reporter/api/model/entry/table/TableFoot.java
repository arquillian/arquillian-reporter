package org.arquillian.reporter.api.model.entry.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TableFoot {
    private List<TableRow> footRows = new ArrayList<>();

    public TableFoot(List<TableRow> footRows) {
        this.footRows = footRows;
    }

    public TableFoot() {

    }

    public List<TableRow> getFootRows() {
        return footRows;
    }

    public void setFootRows(List<TableRow> footRows) {
        this.footRows = footRows;
    }

    public TableFoot copy() {
        return new TableFoot(footRows.stream().map(row -> row.copy()).collect(Collectors.toList()));
    }
}
