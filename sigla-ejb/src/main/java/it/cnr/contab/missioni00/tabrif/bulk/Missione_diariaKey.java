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

public class Missione_diariaKey extends OggettoBulk implements KeyedPersistent {
	// CD_GRUPPO_INQUADRAMENTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_inquadramento;

	// PG_NAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_nazione;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Missione_diariaKey() {
	super();
}
public Missione_diariaKey(java.lang.String cd_gruppo_inquadramento,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione) {
	this.cd_gruppo_inquadramento = cd_gruppo_inquadramento;
	this.dt_inizio_validita = dt_inizio_validita;
	this.pg_nazione = pg_nazione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_diariaKey)) return false;
	Missione_diariaKey k = (Missione_diariaKey)o;
	if(!compareKey(getCd_gruppo_inquadramento(),k.getCd_gruppo_inquadramento())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getPg_nazione(),k.getPg_nazione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_gruppo_inquadramento
 */
public java.lang.String getCd_gruppo_inquadramento() {
	return cd_gruppo_inquadramento;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Getter dell'attributo pg_nazione
 */
public java.lang.Long getPg_nazione() {
	return pg_nazione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_gruppo_inquadramento())+
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getPg_nazione());
}
/* 
 * Setter dell'attributo cd_gruppo_inquadramento
 */
public void setCd_gruppo_inquadramento(java.lang.String cd_gruppo_inquadramento) {
	this.cd_gruppo_inquadramento = cd_gruppo_inquadramento;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Setter dell'attributo pg_nazione
 */
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.pg_nazione = pg_nazione;
}
}
