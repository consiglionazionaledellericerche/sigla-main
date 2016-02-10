package it.cnr.contab.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLConverter;

public class LISTToStringConverter implements SQLConverter<String>, Serializable{

	private static final long serialVersionUID = 1L;

	public Class<String> getTargetJavaType(int i, boolean flag) {
		return String.class;
	}

	@SuppressWarnings("unchecked")
	public void javaToSql(LoggableStatement preparedstatement, Object obj,
			int i, int j) throws SQLException {
		if (obj == null)
			preparedstatement.setNull(i, j);
		else
			preparedstatement.setString(i, StringUtils.join(((List<String>)obj).toArray(), ";"));		
	}

	public Object sqlToJava(ResultSet resultset, String s) throws SQLException {
		String s1 = resultset.getString(s);
		if (s1 == null || s1.length() == 0) {
			return null;
		} else {
			return Arrays.asList(StringUtils.split(s1, ";"));
		}
	}

	@SuppressWarnings("unchecked")
	public Object javaToSql(Object obj) {
		return StringUtils.join(((List<String>)obj).toArray(), ";");	
	}

	public Object sqlToJava(Object obj) {
		return Arrays.asList(StringUtils.split(String.valueOf(obj), ";"));
	}

	public String columnName(String columnName) {
		return columnName;
	}

}
