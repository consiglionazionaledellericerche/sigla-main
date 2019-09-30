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

public class VTerzoPerCompensoKey extends OggettoBulk implements KeyedPersistent {

	// DT_INI_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_validita;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;
	// TI_DIPENDENTE_ALTRO CHAR(1) NOT NULL
	private java.lang.String ti_dipendente_altro;
public VTerzoPerCompensoKey() {
	super();
}
public VTerzoPerCompensoKey(java.sql.Timestamp dt_ini_validita,java.lang.Integer cd_terzo, String ti_dipendente_altro) {
	this.cd_terzo = cd_terzo;
	this.ti_dipendente_altro = ti_dipendente_altro;
	this.dt_ini_validita = dt_ini_validita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof VTerzoPerCompensoKey)) return false;
	VTerzoPerCompensoKey k = (VTerzoPerCompensoKey)o;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getDt_ini_validita(),k.getDt_ini_validita())) return false;
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
		calculateKeyHashCode(getDt_ini_validita())+
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
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
}
