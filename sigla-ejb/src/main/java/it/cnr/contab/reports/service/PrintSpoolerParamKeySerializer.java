package it.cnr.contab.reports.service;

import com.google.gson.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramKey;

import java.lang.reflect.Type;

class PrintSpoolerParamKeySerializer implements JsonSerializer<Print_spooler_paramKey> {
    @Override
    public JsonElement serialize(Print_spooler_paramKey src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
                obj.addProperty("nomeParam", src.getNomeParam());
                //obj.addProperty("pgStampa", "");
        return obj;
    }


}