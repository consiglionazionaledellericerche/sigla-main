package it.cnr.contab.util;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLConverter;

/**
 * SQLConverter che effettua la conversione tra il tipo Java boolean
 * e il tipo SQL CHAR.
 */
public class AsteriskToNullConverter implements SQLConverter,java.io.Serializable {
/**
 * CHARToBooleanConverter constructor comment.
 */
public AsteriskToNullConverter() {
	super();
}
/**
 * getJavaType method comment.
 */
public Class getTargetJavaType(int sqlType,boolean nullable) {
	return String.class;
}
/**
 * javaToSql method comment.
 */
public void javaToSql(LoggableStatement statement, java.lang.Object value, int position, int sqlType) throws java.sql.SQLException {
	if (value == null)
		statement.setString(position,"*");
	else
		statement.setString(position,value.toString());
}
/**
 * sqlToJava method comment.
 */
public Object sqlToJava(java.sql.ResultSet resultSet, String columnName) throws java.sql.SQLException {
	String value = resultSet.getString(columnName);
	if (value == null || value.equals("*")) return null;
	return value;
}

public Object javaToSql(Object obj) {
	return obj==null?"*":null;
}
public Object sqlToJava(Object obj) {
	return obj==null||obj.equals("*")?null:obj;
}
}
