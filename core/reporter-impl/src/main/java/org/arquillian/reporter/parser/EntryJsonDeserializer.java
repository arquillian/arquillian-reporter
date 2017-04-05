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

        if (jsonEntry.get("key") != null) {
            JsonElement key = jsonEntry.get("key");
            StringKey stringKey = prepareGsonParser().fromJson(key, StringKey.class);
            if (jsonEntry.get("value") != null) {
                JsonElement content = jsonEntry.get("value");
                 return new KeyValueEntry(stringKey, prepareGsonParser().fromJson(content, Entry.class));
            }
        } else if (jsonEntry.get("content") != null) {
            JsonElement content = jsonEntry.get("content");
            StringKey stringKey1 = prepareGsonParser().fromJson(content, StringKey.class);

            return new StringEntry(stringKey1);
        }

        return new StringEntry("");

    }
}
