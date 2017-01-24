package org.arquillian.reporter.api.model.entry.graph;

import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DirectionalGraphEdge implements GraphEdge {

    public static final GraphEdgeType TYPE = GraphEdgeType.DIRECTIONAL;

    private StringKey name;

    public DirectionalGraphEdge(StringKey name) {
        this.name = name;
    }

    public DirectionalGraphEdge(String name) {
        this.name = new UnknownStringKey(name);
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }

}
