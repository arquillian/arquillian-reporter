package org.arquillian.reporter.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.arquillian.reporter.api.model.StringKey;

import java.lang.reflect.Type;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class StringKeyJsonDeserializer implements JsonDeserializer<StringKey> {

    @Override
    public StringKey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

        JsonObject jsonStringKey = (JsonObject) json;
        JsonElement value = jsonStringKey.get("value");
        JsonElement description = jsonStringKey.get("description");
        JsonElement icon = jsonStringKey.get("icon");
        return new ParsedStringKey(value != null ? value.getAsString() : "",
                                   description != null ? description.getAsString() : "",
                                   icon != null ? icon.getAsString() : "");
    }
}
