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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_contributo_ritenutaKey extends OggettoBulk implements KeyedPersistent {
	// DT_INI_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_ini_validita;

	// CD_CONTRIBUTO_RITENUTA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_contributo_ritenuta;

public Tipo_contributo_ritenutaKey() {
	super();
}
public Tipo_contributo_ritenutaKey(java.lang.String cd_contributo_ritenuta,java.sql.Timestamp dt_ini_validita) {
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	this.dt_ini_validita = dt_ini_validita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_contributo_ritenutaKey)) return false;
	Tipo_contributo_ritenutaKey k = (Tipo_contributo_ritenutaKey)o;
	if(!compareKey(getCd_contributo_ritenuta(),k.getCd_contributo_ritenuta())) return false;
	if(!compareKey(getDt_ini_validita(),k.getDt_ini_validita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_contributo_ritenuta
 */
public java.lang.String getCd_contributo_ritenuta() {
	return cd_contributo_ritenuta;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_contributo_ritenuta())+
		calculateKeyHashCode(getDt_ini_validita());
}
/* 
 * Setter dell'attributo cd_contributo_ritenuta
 */
public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
}
