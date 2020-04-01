package it.cnr.contab.reports.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class JsonTimestampSeraializer implements JsonSerializer<Timestamp> {
    public JsonElement serialize(Timestamp timestamp, Type typeOfSrc,
                                 JsonSerializationContext context) {
        return timestamp == null ? null : new JsonPrimitive(timestamp.getTime());
    }
}
