package org.arquillian.reporter.api.model.entry.graph;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface GraphEdge {

    enum GraphEdgeType{
        DIRECTIONAL, NON_DIRECTIONAL;
    }

}
