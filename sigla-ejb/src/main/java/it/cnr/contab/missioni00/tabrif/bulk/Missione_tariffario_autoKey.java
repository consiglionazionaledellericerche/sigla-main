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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tariffario_autoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TARIFFA_AUTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tariffa_auto;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Missione_tariffario_autoKey() {
	super();
}
public Missione_tariffario_autoKey(java.lang.String cd_tariffa_auto,java.sql.Timestamp dt_inizio_validita) {
	super();
	this.cd_tariffa_auto = cd_tariffa_auto;
	this.dt_inizio_validita = dt_inizio_validita;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_tariffario_autoKey)) return false;
	Missione_tariffario_autoKey k = (Missione_tariffario_autoKey)o;
	if(!compareKey(getCd_tariffa_auto(),k.getCd_tariffa_auto())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_tariffario_autoKey)) return false;
	Missione_tariffario_autoKey k = (Missione_tariffario_autoKey)o;
	if(!compareKey(getCd_tariffa_auto(),k.getCd_tariffa_auto())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tariffa_auto
 */
public java.lang.String getCd_tariffa_auto() {
	return cd_tariffa_auto;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tariffa_auto())+
		calculateKeyHashCode(getDt_inizio_validita());
}
/* 
 * Setter dell'attributo cd_tariffa_auto
 */
public void setCd_tariffa_auto(java.lang.String cd_tariffa_auto) {
	this.cd_tariffa_auto = cd_tariffa_auto;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
}
