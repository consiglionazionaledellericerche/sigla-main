/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.util;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLConverter;

/**
 * SQLConverter che effettua la conversione tra il tipo Java boolean e il tipo
 * SQL CHAR.
 */
public class AsteriskToNullConverter implements SQLConverter<String>,
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * CHARToBooleanConverter constructor comment.
	 */
	public AsteriskToNullConverter() {
		super();
	}

	/**
	 * getJavaType method comment.
	 */
	public Class<String> getTargetJavaType(int sqlType, boolean nullable) {
		return String.class;
	}

	/**
	 * javaToSql method comment.
	 */
	public void javaToSql(LoggableStatement statement, java.lang.Object value,
			int position, int sqlType) throws java.sql.SQLException {
		if (value == null)
			statement.setString(position, "*");
		else
			statement.setString(position, value.toString());
	}

	/**
	 * sqlToJava method comment.
	 */
	public Object sqlToJava(java.sql.ResultSet resultSet, String columnName)
			throws java.sql.SQLException {
		String value = resultSet.getString(columnName);
		if (value == null || value.equals("*"))
			return null;
		return value;
	}

	public Object javaToSql(Object obj) {
		return obj == null ? "*" : null;
	}

	public Object sqlToJava(Object obj) {
		return obj == null || obj.equals("*") ? null : obj;
	}

	public String columnName(String columnName) {
		return columnName;
	}
}
