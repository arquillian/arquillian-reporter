package org.arquillian.reporter.api.utils;

import java.util.Map;

import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionModifier {


    public static SectionReport feedKeyValueListFromMap(SectionReport section, Map<String, String> keyValueMap){
        keyValueMap.forEach((k,v) -> section.getEntries().add(new KeyValueEntry(k, v)));
        return section;
    }

    public static SectionReport merge(SectionReport originalSection, SectionReport newSection) {
//        if (Validate.isNotEmpty(newSection.getName())){
//            originalSection.setName(newSection.getName());
//        }
        originalSection.getEntries().addAll(newSection.getEntries());
        newSection.getSectionReports().forEach(sr -> originalSection.getSectionReports().add(sr));
        return originalSection;
    }

}
