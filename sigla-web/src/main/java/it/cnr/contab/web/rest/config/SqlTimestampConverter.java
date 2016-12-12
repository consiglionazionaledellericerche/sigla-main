package it.cnr.contab.web.rest.config;

import it.cnr.contab.web.rest.MissioneResource;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SqlTimestampConverter implements JsonSerializer<Timestamp>{
		static SimpleDateFormat sdf = new SimpleDateFormat(MissioneResource.DATE_FORMAT);

		@Override
		public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
	         return new JsonPrimitive(sdf.format(src));
	    }
}
