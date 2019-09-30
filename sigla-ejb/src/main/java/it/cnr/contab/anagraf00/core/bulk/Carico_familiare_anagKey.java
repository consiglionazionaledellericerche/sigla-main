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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Carico_familiare_anagKey extends OggettoBulk implements KeyedPersistent {
	// DT_INI_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_ini_validita;

	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_anag;

	// PG_CARICO_ANAG DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_carico_anag;

public Carico_familiare_anagKey() {
	super();
}
public Carico_familiare_anagKey(java.lang.Integer cd_anag,java.sql.Timestamp dt_ini_validita,java.lang.Long pg_carico_anag) {
	super();
	this.cd_anag = cd_anag;
	this.dt_ini_validita = dt_ini_validita;
	this.pg_carico_anag = pg_carico_anag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Carico_familiare_anagKey)) return false;
	Carico_familiare_anagKey k = (Carico_familiare_anagKey)o;
	if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
	if(!compareKey(getDt_ini_validita(),k.getDt_ini_validita())) return false;
	if(!compareKey(getPg_carico_anag(),k.getPg_carico_anag())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_anag
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
/* 
 * Getter dell'attributo pg_carico_anag
 */
public java.lang.Long getPg_carico_anag() {
	return pg_carico_anag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_anag())+
		calculateKeyHashCode(getDt_ini_validita())+
		calculateKeyHashCode(getPg_carico_anag());
}
/* 
 * Setter dell'attributo cd_anag
 */
public void setCd_anag(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
/* 
 * Setter dell'attributo pg_carico_anag
 */
public void setPg_carico_anag(java.lang.Long pg_carico_anag) {
	this.pg_carico_anag = pg_carico_anag;
}
}
