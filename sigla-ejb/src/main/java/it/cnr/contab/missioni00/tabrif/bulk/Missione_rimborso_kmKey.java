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

public class Missione_rimborso_kmKey extends OggettoBulk implements KeyedPersistent {
	// PG_NAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_nazione;

	// TI_AUTO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_auto;

	// TI_AREA_GEOGRAFICA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_area_geografica;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Missione_rimborso_kmKey() {
	super();
}
public Missione_rimborso_kmKey(java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.String ti_area_geografica,java.lang.String ti_auto) {
	this.dt_inizio_validita = dt_inizio_validita;
	this.pg_nazione = pg_nazione;
	this.ti_area_geografica = ti_area_geografica;
	this.ti_auto = ti_auto;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_rimborso_kmKey)) return false;
	Missione_rimborso_kmKey k = (Missione_rimborso_kmKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getPg_nazione(),k.getPg_nazione())) return false;
	if(!compareKey(getTi_auto(),k.getTi_auto())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_rimborso_kmKey)) return false;
	Missione_rimborso_kmKey k = (Missione_rimborso_kmKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getPg_nazione(),k.getPg_nazione())) return false;
	if(!compareKey(getTi_area_geografica(),k.getTi_area_geografica())) return false;
	if(!compareKey(getTi_auto(),k.getTi_auto())) return false;
	return true;
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
/* 
 * Getter dell'attributo ti_area_geografica
 */
public java.lang.String getTi_area_geografica() {
	return ti_area_geografica;
}
/* 
 * Getter dell'attributo ti_auto
 */
public java.lang.String getTi_auto() {
	return ti_auto;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getPg_nazione())+
		calculateKeyHashCode(getTi_area_geografica())+
		calculateKeyHashCode(getTi_auto());
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
/* 
 * Setter dell'attributo ti_area_geografica
 */
public void setTi_area_geografica(java.lang.String ti_area_geografica) {
	this.ti_area_geografica = ti_area_geografica;
}
/* 
 * Setter dell'attributo ti_auto
 */
public void setTi_auto(java.lang.String ti_auto) {
	this.ti_auto = ti_auto;
}
}
