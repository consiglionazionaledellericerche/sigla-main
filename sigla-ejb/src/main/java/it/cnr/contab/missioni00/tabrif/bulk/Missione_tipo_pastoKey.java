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

public class Missione_tipo_pastoKey extends OggettoBulk implements KeyedPersistent {
	// PG_RIF_INQUADRAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rif_inquadramento;

	// PG_NAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_nazione;

	// CD_TI_PASTO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_ti_pasto;

	// TI_AREA_GEOGRAFICA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_area_geografica;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

	// CD_AREA_ESTERA VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_area_estera;

public Missione_tipo_pastoKey() {
	super();
}
public Missione_tipo_pastoKey(java.lang.String cd_ti_pasto,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica,java.lang.String cd_area_estera) {
	this.cd_ti_pasto = cd_ti_pasto;
	this.dt_inizio_validita = dt_inizio_validita;
	this.pg_nazione = pg_nazione;
	this.pg_rif_inquadramento = pg_rif_inquadramento;
	this.ti_area_geografica = ti_area_geografica;
	this.cd_area_estera = cd_area_estera;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_tipo_pastoKey)) return false;
	Missione_tipo_pastoKey k = (Missione_tipo_pastoKey)o;
	if(!compareKey(getCd_ti_pasto(),k.getCd_ti_pasto())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getPg_nazione(),k.getPg_nazione())) return false;
	if(!compareKey(getPg_rif_inquadramento(),k.getPg_rif_inquadramento())) return false;
	if(!compareKey(getTi_area_geografica(),k.getTi_area_geografica())) return false;
	if(!compareKey(getCd_area_estera(),k.getCd_area_estera())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_ti_pasto
 */
public java.lang.String getCd_ti_pasto() {
	return cd_ti_pasto;
}
/* 
 * Getter dell'attributo cd_area_estera
 */
public java.lang.String getCd_area_estera() {
	return cd_area_estera;
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
 * Getter dell'attributo pg_rif_inquadramento
 */
public java.lang.Long getPg_rif_inquadramento() {
	return pg_rif_inquadramento;
}
/* 
 * Getter dell'attributo ti_area_geografica
 */
public java.lang.String getTi_area_geografica() {
	return ti_area_geografica;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_ti_pasto())+
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getPg_nazione())+
		calculateKeyHashCode(getPg_rif_inquadramento())+
		calculateKeyHashCode(getTi_area_geografica())+
		calculateKeyHashCode(getCd_area_estera());
}
/* 
 * Setter dell'attributo cd_ti_pasto
 */
public void setCd_ti_pasto(java.lang.String cd_ti_pasto) {
	this.cd_ti_pasto = cd_ti_pasto;
}
/* 
 * Setter dell'attributo cd_area_estera
 */
public void setCd_area_estera(java.lang.String cd_area_estera) {
	this.cd_area_estera = cd_area_estera;
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
 * Setter dell'attributo pg_rif_inquadramento
 */
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.pg_rif_inquadramento = pg_rif_inquadramento;
}
/* 
 * Setter dell'attributo ti_area_geografica
 */
public void setTi_area_geografica(java.lang.String ti_area_geografica) {
	this.ti_area_geografica = ti_area_geografica;
}
}
