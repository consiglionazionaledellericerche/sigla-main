package it.cnr.contab.util.rest;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SqlTimestampConverter implements JsonSerializer<Timestamp>{
		static SimpleDateFormat sdf = new SimpleDateFormat(SIGLAResource.DATE_FORMAT);

		@Override
		public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
	         return new JsonPrimitive(sdf.format(src));
	    }
}
