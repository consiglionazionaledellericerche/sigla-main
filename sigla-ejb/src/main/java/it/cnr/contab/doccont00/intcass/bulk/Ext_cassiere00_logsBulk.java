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

public class Ext_cassiere00_logsBulk extends Ext_cassiere00_logsBase {

	//private Ext_cassiere00Bulk file;
	private V_ext_cassiere00Bulk file;

public Ext_cassiere00_logsBulk() {
	super();
}
public Ext_cassiere00_logsBulk(java.lang.Integer esercizio,java.lang.String nome_file,java.math.BigDecimal pg_esecuzione) {
	super(esercizio,nome_file,pg_esecuzione);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @return it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public V_ext_cassiere00Bulk getFile() {
	return file;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @return it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public Ext_cassiere00Bulk OLDgetFile() {
	//return file;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @param newFile it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public void OLDsetFile(Ext_cassiere00Bulk newFile) {
	//file = newFile;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2003 17.25.12)
 * @param newFile it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk
 */
public void setFile(V_ext_cassiere00Bulk newFile) {
	file = newFile;
}
}
