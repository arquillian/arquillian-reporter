package org.arquillian.reporter.api.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.builder.impl.SectionInNodeImpl;
import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractSectionBuilder<SECTIONTYPE extends AbstractSectionReport<SECTIONTYPE,? extends SectionBuilder>, BUILDERTYPE extends SectionBuilder>
    implements SectionBuilder<SECTIONTYPE, BUILDERTYPE> {

    private final SECTIONTYPE sectionReport;
    private List<BUILDERTYPE> subsectionBuilders = new ArrayList<>();


    public AbstractSectionBuilder(SECTIONTYPE sectionReport) {
        this.sectionReport = sectionReport;
    }

    public BUILDERTYPE setIdentifier(String identifier) {
        sectionReport.setIdentifier(identifier);
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap) {
        keyValueMap.forEach((k,v) -> getSectionReport().getEntries().add(new KeyValueEntry(k, v)));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addSection(BUILDERTYPE section) {
        subsectionBuilders.add(section);
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addEntry(Entry entry) {
        sectionReport.getEntries().add(entry);
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(String key, Entry value) {
        sectionReport.getEntries().add(new KeyValueEntry(key, value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(String key, String value) {
        sectionReport.getEntries().add(new KeyValueEntry(key, value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(String key, int value) {
        addKeyValueEntry(key, String.valueOf(value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(String key, boolean value) {
        addKeyValueEntry(key, String.valueOf(value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addSection(AbstractSectionReport newSection) {
        // todo should we take care of merging?
        sectionReport.getSectionReports().add(newSection);
        return (BUILDERTYPE) this;
    }

    public SECTIONTYPE build(){
        subsectionBuilders.forEach(builder -> addSection(builder.build()));
        return sectionReport;
    }

    public <NODETYPE extends ReportNodeEvent<SECTIONTYPE, ? extends ReportNodeEvent>> SectionInNode<SECTIONTYPE, NODETYPE> attachToNode(NODETYPE event){
        return new SectionInNodeImpl<>(build(), event);
    }



    protected List<BUILDERTYPE> getSubsectionBuilders() {
        return subsectionBuilders;
    }

    protected SECTIONTYPE getSectionReport() {
        return sectionReport;
    }
}

