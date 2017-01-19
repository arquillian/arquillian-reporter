package org.arquillian.reporter.api.model.entry.graph;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GraphEntry implements Entry {

    List<GraphNode> nodes = new ArrayList<>();


}
