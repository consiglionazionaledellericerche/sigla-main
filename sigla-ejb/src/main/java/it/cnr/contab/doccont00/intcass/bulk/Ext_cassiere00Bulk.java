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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ext_cassiere00Bulk extends Ext_cassiere00Base {

	public final static String TIPO_RECORD_01 = "01";
	public final static String TIPO_RECORD_30 = "30";
	public final static String TIPO_RECORD_32 = "32";
	private SimpleBulkList logs;

public Ext_cassiere00Bulk() {
	super();
}
public Ext_cassiere00Bulk(java.lang.Integer esercizio,java.lang.String nome_file,java.lang.Long pg_rec) {
	super(esercizio,nome_file,pg_rec);
}
public int addToLogs (Ext_cassiere00_logsBulk nuovoRigo)
{

	getLogs().add(nuovoRigo);
	//nuovoRigo.setFile(this);
	
	return getLogs().size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.22.47)
 * @return it.cnr.jada.bulk.SimpleBulkList
 */
public it.cnr.jada.bulk.SimpleBulkList getLogs() {
	return logs;
}
public Ext_cassiere00_logsBulk removeFromLogs( int indiceDiLinea ) {

	Ext_cassiere00_logsBulk element = (Ext_cassiere00_logsBulk)getLogs().get(indiceDiLinea);

	return (Ext_cassiere00_logsBulk)getLogs().remove(indiceDiLinea);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.22.47)
 * @param newLogs it.cnr.jada.bulk.SimpleBulkList
 */
public void setLogs(it.cnr.jada.bulk.SimpleBulkList newLogs) {
	logs = newLogs;
}
}
