package org.arquillian.reporter.api.model.entry.graph;

import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class NonDirectionalGraphEdge implements GraphEdge {

    public static final GraphEdgeType TYPE = GraphEdgeType.NON_DIRECTIONAL;

    private StringKey name;

    public NonDirectionalGraphEdge(StringKey name) {
        this.name = name;
    }

    public NonDirectionalGraphEdge(String name) {
        this.name = new UnknownStringKey(name);
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }
}
