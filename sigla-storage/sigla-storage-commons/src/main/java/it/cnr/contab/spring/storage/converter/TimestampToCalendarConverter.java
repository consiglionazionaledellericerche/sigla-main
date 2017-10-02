package it.cnr.contab.spring.storage.converter;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimestampToCalendarConverter implements Converter<Calendar, Timestamp> {

	public Calendar convert(Timestamp timestamp) {
		if (timestamp == null)
			return null;
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		cal.setTimeInMillis(timestamp.getTime());
		return cal;
	}

}
