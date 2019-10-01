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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;


public class Vsx_liquidazione_coriBase extends Vsx_liquidazione_coriKey implements Keyed {
	// CD_CDS VARCHAR(100)
	private java.lang.String cd_cds;

	// CD_CDS_ORIGINE VARCHAR(100)
	private java.lang.String cd_cds_origine;

	// CD_GRUPPO_CR VARCHAR(100)
	private java.lang.String cd_gruppo_cr;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(100)
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(100)
	private java.lang.String cd_uo_origine;

	// ESERCIZIO DECIMAL(9,0)
	private java.lang.Integer esercizio;

	// MESSAGETOUSER VARCHAR(200)
	private java.lang.String messagetouser;

	// PG_LIQUIDAZIONE DECIMAL(9,0)
	private java.lang.Integer pg_liquidazione;

	// PG_LIQUIDAZIONE_ORIGINE DECIMAL(9,0)
	private java.lang.Integer pg_liquidazione_origine;

	// PROC_NAME VARCHAR(100) NOT NULL
	private java.lang.String proc_name;

	// CD_REGIONE VARCHAR(100)
	private java.lang.String cd_regione;

	// PG_COMUNE DECIMAL(19,0)
	private java.lang.Long pg_comune;

public Vsx_liquidazione_coriBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_gruppo_cr
 */
public java.lang.String getCd_gruppo_cr() {
	return cd_gruppo_cr;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_regione'
 *
 * @return Il valore della proprietà 'cd_regione'
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo messagetouser
 */
public java.lang.String getMessagetouser() {
	return messagetouser;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_comune'
 *
 * @return Il valore della proprietà 'pg_comune'
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
/* 
 * Getter dell'attributo pg_liquidazione
 */
public java.lang.Integer getPg_liquidazione() {
	return pg_liquidazione;
}
/* 
 * Getter dell'attributo pg_liquidazione_origine
 */
public java.lang.Integer getPg_liquidazione_origine() {
	return pg_liquidazione_origine;
}
/* 
 * Getter dell'attributo proc_name
 */
public java.lang.String getProc_name() {
	return proc_name;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_gruppo_cr
 */
public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr) {
	this.cd_gruppo_cr = cd_gruppo_cr;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_regione'
 *
 * @param newCd_regione	Il valore da assegnare a 'cd_regione'
 */
public void setCd_regione(java.lang.String newCd_regione) {
	cd_regione = newCd_regione;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo messagetouser
 */
public void setMessagetouser(java.lang.String messagetouser) {
	this.messagetouser = messagetouser;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_comune'
 *
 * @param newPg_comune	Il valore da assegnare a 'pg_comune'
 */
public void setPg_comune(java.lang.Long newPg_comune) {
	pg_comune = newPg_comune;
}
/* 
 * Setter dell'attributo pg_liquidazione
 */
public void setPg_liquidazione(java.lang.Integer pg_liquidazione) {
	this.pg_liquidazione = pg_liquidazione;
}
/* 
 * Setter dell'attributo pg_liquidazione_origine
 */
public void setPg_liquidazione_origine(java.lang.Integer pg_liquidazione_origine) {
	this.pg_liquidazione_origine = pg_liquidazione_origine;
}
/* 
 * Setter dell'attributo proc_name
 */
public void setProc_name(java.lang.String proc_name) {
	this.proc_name = proc_name;
}
}
