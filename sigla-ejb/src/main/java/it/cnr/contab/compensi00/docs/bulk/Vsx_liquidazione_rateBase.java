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

public class Vsx_liquidazione_rateBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(100)
	private java.lang.String cd_cds;

	// CD_CENTRO_RESPONSABILITA VARCHAR(100)
	private java.lang.String cd_centro_responsabilita;

	// CD_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String cd_elemento_voce;

	// CD_LINEA_ATTIVITA VARCHAR(100)
	private java.lang.String cd_linea_attivita;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(100)
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE VARCHAR(100)
	private java.lang.String cd_voce;

	// ESERCIZIO DECIMAL(9,0)
	private java.lang.Integer esercizio;

	// ESERCIZIO_COMPETENZA DECIMAL(9,0)
	private java.lang.Integer esercizio_competenza;

	// MESSAGETOUSER VARCHAR(200)
	private java.lang.String messagetouser;

	// PAR_NUM DECIMAL(5,0) NOT NULL
	private java.lang.Integer par_num;

	// PG_CALL DECIMAL(15,0) NOT NULL
	private java.lang.Long pg_call;

	// PG_MINICARRIERA DECIMAL(19,0)
	private java.lang.Long pg_minicarriera;

	// PG_RATA DECIMAL(19,0)
	private java.lang.Long pg_rata;

	// PROC_NAME VARCHAR(100) NOT NULL
	private java.lang.String proc_name;

	// TI_APPARTENENZA VARCHAR(100)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE VARCHAR(100)
	private java.lang.String ti_gestione;

public Vsx_liquidazione_rateBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
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
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_voce
 */
public java.lang.String getCd_voce() {
	return cd_voce;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_competenza
 */
public java.lang.Integer getEsercizio_competenza() {
	return esercizio_competenza;
}
/* 
 * Getter dell'attributo messagetouser
 */
public java.lang.String getMessagetouser() {
	return messagetouser;
}
/* 
 * Getter dell'attributo par_num
 */
public java.lang.Integer getPar_num() {
	return par_num;
}
/* 
 * Getter dell'attributo pg_call
 */
public java.lang.Long getPg_call() {
	return pg_call;
}
/* 
 * Getter dell'attributo pg_minicarriera
 */
public java.lang.Long getPg_minicarriera() {
	return pg_minicarriera;
}
/* 
 * Getter dell'attributo pg_rata
 */
public java.lang.Long getPg_rata() {
	return pg_rata;
}
/* 
 * Getter dell'attributo proc_name
 */
public java.lang.String getProc_name() {
	return proc_name;
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
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
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
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_voce
 */
public void setCd_voce(java.lang.String cd_voce) {
	this.cd_voce = cd_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo esercizio_competenza
 */
public void setEsercizio_competenza(java.lang.Integer esercizio_competenza) {
	this.esercizio_competenza = esercizio_competenza;
}
/* 
 * Setter dell'attributo messagetouser
 */
public void setMessagetouser(java.lang.String messagetouser) {
	this.messagetouser = messagetouser;
}
/* 
 * Setter dell'attributo par_num
 */
public void setPar_num(java.lang.Integer par_num) {
	this.par_num = par_num;
}
/* 
 * Setter dell'attributo pg_call
 */
public void setPg_call(java.lang.Long pg_call) {
	this.pg_call = pg_call;
}
/* 
 * Setter dell'attributo pg_minicarriera
 */
public void setPg_minicarriera(java.lang.Long pg_minicarriera) {
	this.pg_minicarriera = pg_minicarriera;
}
/* 
 * Setter dell'attributo pg_rata
 */
public void setPg_rata(java.lang.Long pg_rata) {
	this.pg_rata = pg_rata;
}
/* 
 * Setter dell'attributo proc_name
 */
public void setProc_name(java.lang.String proc_name) {
	this.proc_name = proc_name;
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
}
