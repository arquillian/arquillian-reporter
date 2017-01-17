package org.arquillian.reporter.api.builder;

import java.util.Map;

import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface SectionBuilder<SECTIONTYPE extends AbstractSectionReport<SECTIONTYPE, ? extends SectionBuilder>, BUILDERTYPE extends SectionBuilder> {

    SECTIONTYPE build();

    BUILDERTYPE addEntry(Entry entry);

    BUILDERTYPE setIdentifier(String identifier);

    BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap);

    BUILDERTYPE addSection(BUILDERTYPE sectionBuilder);

    BUILDERTYPE addSection(SECTIONTYPE section);

    BUILDERTYPE addKeyValueEntry(String key, Entry value);

    BUILDERTYPE addKeyValueEntry(String key, String value);

    BUILDERTYPE addKeyValueEntry(String key, int value);

    BUILDERTYPE addKeyValueEntry(String s, boolean runAsClient);

    <NODETYPE extends ReportNodeEvent<SECTIONTYPE, ? extends ReportNodeEvent>> SectionInNode<SECTIONTYPE, NODETYPE> attachToNode(NODETYPE event);
}
