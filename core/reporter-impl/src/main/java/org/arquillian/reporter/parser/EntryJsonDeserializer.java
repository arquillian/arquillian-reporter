package org.arquillian.reporter.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.entry.StringEntry;

import static org.arquillian.reporter.parser.ReportJsonParser.prepareGsonParser;

/**
 * Created by hemani on 4/4/17.
 */
public class EntryJsonDeserializer implements JsonDeserializer<Entry> {

    @Override
    public Entry deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
        JsonObject jsonEntry = (JsonObject) json;

        JsonElement key = jsonEntry.get("key");

        if (key != null) {
            StringKey keyEntry = prepareGsonParser().fromJson(key, StringKey.class);
            JsonElement value = jsonEntry.get("value");
            if (value != null) {
                return new KeyValueEntry(keyEntry, prepareGsonParser().fromJson(value, Entry.class));
            }
        } else {
            JsonElement content = jsonEntry.get("content");
            if (content != null) {
                StringKey valueEntry = prepareGsonParser().fromJson(content, StringKey.class);
                return new StringEntry(valueEntry);
            }
        }
        return new StringEntry("");
    }
}
