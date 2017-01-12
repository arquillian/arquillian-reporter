package org.arquillian.reporter.api.utils;

import java.util.Map;

import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionModifier {


    public static Section feedKeyValueListFromMap(Section section, Map<String, String> keyValueMap){
        keyValueMap.forEach((k,v) -> section.getEntries().add(new KeyValueEntry(k, v)));
        return section;
    }

    public static Section merge(Section originalSection, Section newSection) {
        if (Validate.isNotEmpty(newSection.getName())){
            originalSection.setName(newSection.getName());
        }
        originalSection.getEntries().addAll(newSection.getEntries());
        newSection.getSections().forEach(ss -> originalSection.addSection(ss));
        return originalSection;
    }
}
