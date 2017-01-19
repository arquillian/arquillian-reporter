package org.arquillian.reporter.api.model.entry.graph;

import java.util.Map;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GraphNode {

    private String name;
    private Map<GraphNode, GraphEdge> connctions;
}
