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

package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_reintegro_fondoBase extends Vsx_reintegro_fondoKey implements Keyed {
	// CD_CDS VARCHAR(100)
	private java.lang.String cd_cds;

	// CD_CODICE_FONDO VARCHAR(100)
	private java.lang.String cd_codice_fondo;

	// CD_UO VARCHAR(100)
	private java.lang.String cd_uo;

	// ESERCIZIO DECIMAL(9,0)
	private java.lang.Integer esercizio;

	// MESSAGETOUSER VARCHAR(200)
	private java.lang.String messagetouser;

	// PG_FONDO_SPESA DECIMAL(19,0)
	private java.lang.Long pg_fondo_spesa;

	// PROC_NAME VARCHAR(100) NOT NULL
	private java.lang.String proc_name;

public Vsx_reintegro_fondoBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_codice_fondo
 */
public java.lang.String getCd_codice_fondo() {
	return cd_codice_fondo;
}
/* 
 * Getter dell'attributo cd_uo
 */
public java.lang.String getCd_uo() {
	return cd_uo;
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
/* 
 * Getter dell'attributo pg_fondo_spesa
 */
public java.lang.Long getPg_fondo_spesa() {
	return pg_fondo_spesa;
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
 * Setter dell'attributo cd_codice_fondo
 */
public void setCd_codice_fondo(java.lang.String cd_codice_fondo) {
	this.cd_codice_fondo = cd_codice_fondo;
}
/* 
 * Setter dell'attributo cd_uo
 */
public void setCd_uo(java.lang.String cd_uo) {
	this.cd_uo = cd_uo;
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
/* 
 * Setter dell'attributo pg_fondo_spesa
 */
public void setPg_fondo_spesa(java.lang.Long pg_fondo_spesa) {
	this.pg_fondo_spesa = pg_fondo_spesa;
}
/* 
 * Setter dell'attributo proc_name
 */
public void setProc_name(java.lang.String proc_name) {
	this.proc_name = proc_name;
}
}
