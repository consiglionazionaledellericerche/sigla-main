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

public class Missione_abbattimentiKey extends OggettoBulk implements KeyedPersistent {
	// FL_PASTO CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_pasto;

	// PG_RIF_INQUADRAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rif_inquadramento;

	// FL_VITTO_ALLOGGIO_GRATUITO CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_vitto_alloggio_gratuito;

	// FL_NAVIGAZIONE CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_navigazione;

	// FL_ALLOGGIO CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_alloggio;

	// PG_NAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_nazione;

	// FL_TRASPORTO CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_trasporto;

	// FL_VITTO_GRATUITO CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_vitto_gratuito;

	// TI_AREA_GEOGRAFICA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_area_geografica;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

	// DURATA_ORE VARCHAR(5) NOT NULL (PK)
	private java.lang.String durata_ore;

	// FL_ALLOGGIO_GRATUITO CHAR(1) NOT NULL (PK)
	private java.lang.Boolean fl_alloggio_gratuito;

public Missione_abbattimentiKey() {
	super();
}
public Missione_abbattimentiKey(java.sql.Timestamp dt_inizio_validita,java.lang.String durata_ore,java.lang.Boolean fl_alloggio,java.lang.Boolean fl_alloggio_gratuito,java.lang.Boolean fl_navigazione,java.lang.Boolean fl_pasto,java.lang.Boolean fl_trasporto,java.lang.Boolean fl_vitto_alloggio_gratuito,java.lang.Boolean fl_vitto_gratuito,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica) {
	this.dt_inizio_validita = dt_inizio_validita;
	this.durata_ore = durata_ore;
	this.fl_alloggio = fl_alloggio;
	this.fl_alloggio_gratuito = fl_alloggio_gratuito;
	this.fl_navigazione = fl_navigazione;
	this.fl_pasto = fl_pasto;
	this.fl_trasporto = fl_trasporto;
	this.fl_vitto_alloggio_gratuito = fl_vitto_alloggio_gratuito;
	this.fl_vitto_gratuito = fl_vitto_gratuito;
	this.pg_nazione = pg_nazione;
	this.pg_rif_inquadramento = pg_rif_inquadramento;
	this.ti_area_geografica = ti_area_geografica;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Missione_abbattimentiKey)) return false;
	Missione_abbattimentiKey k = (Missione_abbattimentiKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getDurata_ore(),k.getDurata_ore())) return false;
	if(!compareKey(getFl_alloggio(),k.getFl_alloggio())) return false;
	if(!compareKey(getFl_alloggio_gratuito(),k.getFl_alloggio_gratuito())) return false;
	if(!compareKey(getFl_navigazione(),k.getFl_navigazione())) return false;
	if(!compareKey(getFl_pasto(),k.getFl_pasto())) return false;
	if(!compareKey(getFl_trasporto(),k.getFl_trasporto())) return false;
	if(!compareKey(getFl_vitto_alloggio_gratuito(),k.getFl_vitto_alloggio_gratuito())) return false;
	if(!compareKey(getFl_vitto_gratuito(),k.getFl_vitto_gratuito())) return false;
	if(!compareKey(getPg_nazione(),k.getPg_nazione())) return false;
	if(!compareKey(getPg_rif_inquadramento(),k.getPg_rif_inquadramento())) return false;
	if(!compareKey(getTi_area_geografica(),k.getTi_area_geografica())) return false;
	return true;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Getter dell'attributo durata_ore
 */
public java.lang.String getDurata_ore() {
	return durata_ore;
}
/* 
 * Getter dell'attributo fl_alloggio
 */
public java.lang.Boolean getFl_alloggio() {
	return fl_alloggio;
}
/* 
 * Getter dell'attributo fl_alloggio_gratuito
 */
public java.lang.Boolean getFl_alloggio_gratuito() {
	return fl_alloggio_gratuito;
}
/* 
 * Getter dell'attributo fl_navigazione
 */
public java.lang.Boolean getFl_navigazione() {
	return fl_navigazione;
}
/* 
 * Getter dell'attributo fl_pasto
 */
public java.lang.Boolean getFl_pasto() {
	return fl_pasto;
}
/* 
 * Getter dell'attributo fl_trasporto
 */
public java.lang.Boolean getFl_trasporto() {
	return fl_trasporto;
}
/* 
 * Getter dell'attributo fl_vitto_alloggio_gratuito
 */
public java.lang.Boolean getFl_vitto_alloggio_gratuito() {
	return fl_vitto_alloggio_gratuito;
}
/* 
 * Getter dell'attributo fl_vitto_gratuito
 */
public java.lang.Boolean getFl_vitto_gratuito() {
	return fl_vitto_gratuito;
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
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getDurata_ore())+
		calculateKeyHashCode(getFl_alloggio())+
		calculateKeyHashCode(getFl_alloggio_gratuito())+
		calculateKeyHashCode(getFl_navigazione())+
		calculateKeyHashCode(getFl_pasto())+
		calculateKeyHashCode(getFl_trasporto())+
		calculateKeyHashCode(getFl_vitto_alloggio_gratuito())+
		calculateKeyHashCode(getFl_vitto_gratuito())+
		calculateKeyHashCode(getPg_nazione())+
		calculateKeyHashCode(getPg_rif_inquadramento())+
		calculateKeyHashCode(getTi_area_geografica());
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Setter dell'attributo durata_ore
 */
public void setDurata_ore(java.lang.String durata_ore) {
	this.durata_ore = durata_ore;
}
/* 
 * Setter dell'attributo fl_alloggio
 */
public void setFl_alloggio(java.lang.Boolean fl_alloggio) {
	this.fl_alloggio = fl_alloggio;
}
/* 
 * Setter dell'attributo fl_alloggio_gratuito
 */
public void setFl_alloggio_gratuito(java.lang.Boolean fl_alloggio_gratuito) {
	this.fl_alloggio_gratuito = fl_alloggio_gratuito;
}
/* 
 * Setter dell'attributo fl_navigazione
 */
public void setFl_navigazione(java.lang.Boolean fl_navigazione) {
	this.fl_navigazione = fl_navigazione;
}
/* 
 * Setter dell'attributo fl_pasto
 */
public void setFl_pasto(java.lang.Boolean fl_pasto) {
	this.fl_pasto = fl_pasto;
}
/* 
 * Setter dell'attributo fl_trasporto
 */
public void setFl_trasporto(java.lang.Boolean fl_trasporto) {
	this.fl_trasporto = fl_trasporto;
}
/* 
 * Setter dell'attributo fl_vitto_alloggio_gratuito
 */
public void setFl_vitto_alloggio_gratuito(java.lang.Boolean fl_vitto_alloggio_gratuito) {
	this.fl_vitto_alloggio_gratuito = fl_vitto_alloggio_gratuito;
}
/* 
 * Setter dell'attributo fl_vitto_gratuito
 */
public void setFl_vitto_gratuito(java.lang.Boolean fl_vitto_gratuito) {
	this.fl_vitto_gratuito = fl_vitto_gratuito;
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
