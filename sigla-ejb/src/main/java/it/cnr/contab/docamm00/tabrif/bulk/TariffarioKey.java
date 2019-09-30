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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class TariffarioKey extends OggettoBulk implements KeyedPersistent {
	// DT_INI_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_ini_validita;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// CD_TARIFFARIO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tariffario;

public TariffarioKey() {
	super();
}
public TariffarioKey(java.lang.String cd_tariffario,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_ini_validita) {
	super();
	this.cd_tariffario = cd_tariffario;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.dt_ini_validita = dt_ini_validita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof TariffarioKey)) return false;
	TariffarioKey k = (TariffarioKey)o;
	if(!compareKey(getCd_tariffario(),k.getCd_tariffario())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getDt_ini_validita(),k.getDt_ini_validita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tariffario
 */
public java.lang.String getCd_tariffario() {
	return cd_tariffario;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tariffario())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getDt_ini_validita());
}
/* 
 * Setter dell'attributo cd_tariffario
 */
public void setCd_tariffario(java.lang.String cd_tariffario) {
	this.cd_tariffario = cd_tariffario;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
}
