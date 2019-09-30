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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_terzo_per_compensoKey extends OggettoBulk implements KeyedPersistent {

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;
	// TI_DIPENDENTE_ALTRO CHAR(1) NOT NULL
	private java.lang.String ti_dipendente_altro;
public V_terzo_per_compensoKey() {
	super();
}
public V_terzo_per_compensoKey(java.lang.Integer cd_terzo, String ti_dipendente_altro) {
	this.cd_terzo = cd_terzo;
	this.ti_dipendente_altro = ti_dipendente_altro;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof V_terzo_per_compensoKey)) return false;
	V_terzo_per_compensoKey k = (V_terzo_per_compensoKey)o;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getTi_dipendente_altro(),k.getTi_dipendente_altro())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (18/06/2002 17.09.11)
 * @return java.lang.String
 */
public java.lang.String getTi_dipendente_altro() {
	return ti_dipendente_altro;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getTi_dipendente_altro());
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (18/06/2002 17.09.11)
 * @param newTi_dipendente_altro java.lang.String
 */
public void setTi_dipendente_altro(java.lang.String newTi_dipendente_altro) {
	ti_dipendente_altro = newTi_dipendente_altro;
}
}
