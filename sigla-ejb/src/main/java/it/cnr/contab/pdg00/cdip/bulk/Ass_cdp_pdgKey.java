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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_cdp_pdgKey extends OggettoBulk implements KeyedPersistent {
	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// PG_SPESA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_spesa;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ID_MATRICOLA VARCHAR(10) NOT NULL (PK)
	private java.lang.String id_matricola;

	// TI_PREV_CONS VARCHAR(10) NOT NULL (PK)
	private java.lang.String ti_prev_cons;

	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_linea_attivita;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

public Ass_cdp_pdgKey() {
	super();
}
public Ass_cdp_pdgKey(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super();
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_elemento_voce = cd_elemento_voce;
	this.cd_linea_attivita = cd_linea_attivita;
	this.esercizio = esercizio;
	this.id_matricola = id_matricola;
	this.pg_spesa = pg_spesa;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
	this.ti_prev_cons = ti_prev_cons;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_cdp_pdgKey)) return false;
	Ass_cdp_pdgKey k = (Ass_cdp_pdgKey)o;
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;
	if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getId_matricola(),k.getId_matricola())) return false;
	if(!compareKey(getPg_spesa(),k.getPg_spesa())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	if(!compareKey(getTi_prev_cons(),k.getTi_prev_cons())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_linea_attivita
 */
public java.lang.String getCd_linea_attivita() {
	return cd_linea_attivita;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo id_matricola
 */
public java.lang.String getId_matricola() {
	return id_matricola;
}
/* 
 * Getter dell'attributo pg_spesa
 */
public java.lang.Long getPg_spesa() {
	return pg_spesa;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Getter dell'attributo ti_prev_cons
 */
public java.lang.String getTi_prev_cons() {
	return ti_prev_cons;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_elemento_voce())+
		calculateKeyHashCode(getCd_linea_attivita())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getId_matricola())+
		calculateKeyHashCode(getPg_spesa())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_gestione())+
		calculateKeyHashCode(getTi_prev_cons());
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_linea_attivita
 */
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.cd_linea_attivita = cd_linea_attivita;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo id_matricola
 */
public void setId_matricola(java.lang.String id_matricola) {
	this.id_matricola = id_matricola;
}
/* 
 * Setter dell'attributo pg_spesa
 */
public void setPg_spesa(java.lang.Long pg_spesa) {
	this.pg_spesa = pg_spesa;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
/* 
 * Setter dell'attributo ti_prev_cons
 */
public void setTi_prev_cons(java.lang.String ti_prev_cons) {
	this.ti_prev_cons = ti_prev_cons;
}
}
