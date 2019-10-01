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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_linea_attivitaHome extends BulkHome {
	private final static String CD_TIPO_LINEA_ATTIVITA_COMUNE_DEFAULT = "0000000";
	private final static java.text.DecimalFormat CD_TIPO_LINEA_ATTIVITA_COMUNE_FORMAT = new java.text.DecimalFormat(CD_TIPO_LINEA_ATTIVITA_COMUNE_DEFAULT);
	public final static int CD_TIPO_LINEA_ATTIVITA_COMUNE_LENGTH = CD_TIPO_LINEA_ATTIVITA_COMUNE_DEFAULT.length();
protected Tipo_linea_attivitaHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Tipo_linea_attivitaHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 */
public Tipo_linea_attivitaHome(java.sql.Connection conn) {
	this(Tipo_linea_attivitaBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 * @param persistentCache	
 */
public Tipo_linea_attivitaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	this(Tipo_linea_attivitaBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	try {
		Tipo_linea_attivitaBulk tipo_linea_attivita = (Tipo_linea_attivitaBulk)bulk;
		if (tipo_linea_attivita.getCd_tipo_linea_attivita() == null) {
			SQLBuilder sqlMax = selectMax(tipo_linea_attivita,"cd_tipo_linea_attivita");
			sqlMax.addClause("AND","ti_tipo_la",sqlMax.EQUALS,tipo_linea_attivita.getTi_tipo_la());
			String max = (String)fetchAndLockMax(sqlMax,tipo_linea_attivita,"cd_tipo_linea_attivita",null);
			try {
				if (max == null) {
					tipo_linea_attivita.setCd_tipo_linea_attivita(CD_TIPO_LINEA_ATTIVITA_COMUNE_DEFAULT);
				} else {
					int maxValue = Integer.parseInt(max);
					tipo_linea_attivita.setCd_tipo_linea_attivita(CD_TIPO_LINEA_ATTIVITA_COMUNE_FORMAT.format(maxValue+1));
				}
			} catch(Throwable e) {
			}
		}
	} catch(BusyResourceException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	}
}
}
