package org.arquillian.reporter.api.builder.report;

import java.util.Map;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * A {@link Builder} providing an API that helps you to build a {@link Report}
 *
 * @param <BUILDERTYPE> The {@link Builder} type of this implementation itself
 * @param <REPORTTYPE>  The {@link Report} type the builder should build
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface ReportBuilder<BUILDERTYPE extends ReportBuilder, REPORTTYPE extends Report<REPORTTYPE, ? extends ReportBuilder>>
    extends Builder {

    /**
     * Finishes all necessary building actions and returns the resulting instance of {@link Report}.
     *
     * @return A built instance of {@link Report}
     */
    REPORTTYPE build();

    /**
     * Adds the given {@link Entry} into the list of entries in the associated {@link Report} instance
     *
     * @param entry An {@link Entry} to add
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addEntry(Entry entry);

    /**
     * Takes the given map of Strings and each pair stores as a {@link KeyValueEntry} in the list of entries in the associated {@link Report} instance.
     * The String values are stored as {@link UnknownStringKey}.
     *
     * @param keyValueMap Map of Strings to be added as {@link KeyValueEntry}(-ies)
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap);

    /**
     * Takes the given {@link ReportBuilder} that represents a sub-report. When the method {@link this#build()} is called,
     * the builder is built as well and the resulting report is added into the list of sub-reports in the associated {@link Report} instance
     *
     * @param reportBuilder An {@link Report} to add into the list of sub-reports
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addReport(ReportBuilder reportBuilder);

    /**
     * Adds the given {@link Report} into the list of sub-reports in the associated {@link Report} instance
     *
     * @param report An {@link Report} to add into the list of sub-reports
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addReport(REPORTTYPE report);

    /**
     * Adds the given {@link StringKey} and {@link Entry} as a {@link KeyValueEntry} into the list of entries in the associated {@link Report} instance
     *
     * @param key   A {@link StringKey} to be added as key of the {@link KeyValueEntry}
     * @param value An {@link Entry} to be added as value of the {@link KeyValueEntry}
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addKeyValueEntry(StringKey key, Entry value);

    /**
     * Adds the given {@link StringKey} and String value as a {@link KeyValueEntry} into the list of entries in the associated {@link Report} instance
     *
     * @param key   A {@link StringKey} to be added as key of the {@link KeyValueEntry}
     * @param value A String to be added as value of the {@link KeyValueEntry}. It is stored as an {@link UnknownStringKey}
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addKeyValueEntry(StringKey key, String value);

    /**
     * Adds the given Strings as a {@link KeyValueEntry} into the list of entries in the associated {@link Report} instance
     *
     * @param key   A String to be added as key of the {@link KeyValueEntry}. It is stored as an {@link UnknownStringKey}
     * @param value A String to be added as value of the {@link KeyValueEntry}. It is stored as an {@link UnknownStringKey}
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addKeyValueEntry(String key, String value);

    /**
     * Adds the given {@link StringKey} and integer value as a {@link KeyValueEntry} into the list of entries in the associated {@link Report} instance
     *
     * @param key   A {@link StringKey} to be added as key of the {@link KeyValueEntry}
     * @param value An integer to be added as value of the {@link KeyValueEntry}. It is stored as an {@link UnknownStringKey}
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addKeyValueEntry(StringKey key, int value);

    /**
     * Adds the given {@link StringKey} and boolean as a {@link KeyValueEntry} into the list of entries in the associated {@link Report} instance
     *
     * @param key   A {@link StringKey} to be added as key of the {@link KeyValueEntry}
     * @param value A boolean to be added as value of the {@link KeyValueEntry}. It is stored as an {@link UnknownStringKey}
     * @return The same instance of {@link ReportBuilder} with modified {@link Report} instance
     */
    BUILDERTYPE addKeyValueEntry(StringKey key, boolean value);

    /**
     * Builds the associated {@link Report} and assigns it into the given {@link SectionEvent}. Then, it is possible to fire the event.
     *
     * @param event         A {@link SectionEvent} that should carry the built {@link Report}.
     * @param <SECTIONTYPE> Type of the {@link SectionEvent}
     * @return An instance of {@link ReportInSectionBuilder} with the given {@link SectionEvent} containing the built {@link Report}.
     */
    <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>> ReportInSectionBuilder<REPORTTYPE, SECTIONTYPE> inSection(
        SECTIONTYPE event);
}
