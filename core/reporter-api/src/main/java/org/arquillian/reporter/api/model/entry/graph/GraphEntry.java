package org.arquillian.reporter.api.model.entry.graph;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GraphEntry implements Entry {

    private StringKey name;
    private List<GraphNode> nodes = new ArrayList<>();

    public GraphEntry() {
    }

    public GraphEntry(StringKey name) {
        this.name = name;
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }

    public Entry outputEntry(){
        return this;
    }
}
