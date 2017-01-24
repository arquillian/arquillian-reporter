package org.arquillian.reporter.api.model.entry.graph;

import java.util.Map;

import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GraphNode {

    private StringKey name;
    private Map<GraphNode, GraphEdge> connections;

    public GraphNode() {
    }

    public GraphNode(StringKey name) {
        this.name = name;
    }

    public GraphNode(String name) {
        this.name = new UnknownStringKey(name);
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }

    public Map<GraphNode, GraphEdge> getConnections() {
        return connections;
    }

    public void setConnections(
        Map<GraphNode, GraphEdge> connections) {
        this.connections = connections;
    }
}
