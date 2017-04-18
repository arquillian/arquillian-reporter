package org.arquillian.reporter.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.FileEntry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.entry.StringEntry;

import static org.arquillian.reporter.parser.ReportJsonParser.prepareGsonParser;

public class EntryJsonDeserializer implements JsonDeserializer<Entry> {

    @Override
    public Entry deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
        JsonObject jsonEntry = (JsonObject) json;

        JsonElement key = jsonEntry.get("key");
        JsonElement content = jsonEntry.get("content");
        JsonElement filePath = jsonEntry.get("filePath");

        if (key != null) {
            StringKey keyEntry = prepareGsonParser().fromJson(key, StringKey.class);
            JsonElement value = jsonEntry.get("value");
            if (value != null) {
                return new KeyValueEntry(keyEntry, prepareGsonParser().fromJson(value, Entry.class));
            }
        } else if (content != null) {
            StringKey stringEntry = prepareGsonParser().fromJson(content, StringKey.class);
            return new StringEntry(stringEntry);
        } else if (filePath != null) {
            String fileEntry = prepareGsonParser().fromJson(filePath, String.class);
            return new FileEntry(fileEntry);
        }

        return new StringEntry("");
    }
}
