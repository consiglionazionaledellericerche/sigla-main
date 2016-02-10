package it.cnr.contab.util;

import java.sql.Date;
import java.sql.Timestamp;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLConverter;
import it.cnr.jada.util.DateUtils;

/**
 * SQLConverter che effettua la conversione tra il tipo Java boolean e il tipo
 * SQL CHAR.
 */
public class DateTruncConverter implements SQLConverter<Date>,
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * CHARToBooleanConverter constructor comment.
	 */
	public DateTruncConverter() {
		super();
	}

	/**
	 * getJavaType method comment.
	 */
	public Class<Date> getTargetJavaType(int sqlType, boolean nullable) {
		return Date.class;
	}

	/**
	 * javaToSql method comment.
	 */
	public void javaToSql(LoggableStatement statement, java.lang.Object value,
			int position, int sqlType) throws java.sql.SQLException {
		if (value == null)
			statement.setDate(position, null);
		else
			statement.setTimestamp(position, DateUtils.truncate((Timestamp)value));
	}

	/**
	 * sqlToJava method comment.
	 */
	public Object sqlToJava(java.sql.ResultSet resultSet, String columnName)
			throws java.sql.SQLException {
		return resultSet.getDate(columnName);
	}

	public Object javaToSql(Object obj) {
		return obj;
	}

	public Object sqlToJava(Object obj) {
		return obj;
	}

	public String columnName(String columnName) {
		return "TRUNC(" + columnName + ")";
	}
}
