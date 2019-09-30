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

package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_rif_protocollo_ivaBase extends Vsx_rif_protocollo_ivaKey implements Persistent {
	// CD_CDS VARCHAR(100)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(100)
	private java.lang.String cd_unita_organizzativa;

	// DT_STAMPA TIMESTAMP
	private java.sql.Timestamp dt_stampa;

	// ESERCIZIO DECIMAL(9,0)
	private java.lang.Integer esercizio;

	// MESSAGETOUSER VARCHAR(200)
	private java.lang.String messagetouser;

	// PG_FATTURA DECIMAL(9,0)
	private java.lang.Long pg_fattura;

	// PROC_NAME VARCHAR(100) NOT NULL
	private java.lang.String proc_name;

	// TIPO_DOCUMENTO_AMM VARCHAR(100)
	private java.lang.String tipo_documento_amm;

	// UTENTE VARCHAR(100)
	private java.lang.String utente;

public Vsx_rif_protocollo_ivaBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo dt_stampa
 */
public java.sql.Timestamp getDt_stampa() {
	return dt_stampa;
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
 * Getter dell'attributo pg_fattura
 */
public java.lang.Long getPg_fattura() {
	return pg_fattura;
}
/* 
 * Getter dell'attributo proc_name
 */
public java.lang.String getProc_name() {
	return proc_name;
}
/* 
 * Getter dell'attributo tipo_documento_amm
 */
public java.lang.String getTipo_documento_amm() {
	return tipo_documento_amm;
}
/* 
 * Getter dell'attributo utente
 */
public java.lang.String getUtente() {
	return utente;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo dt_stampa
 */
public void setDt_stampa(java.sql.Timestamp dt_stampa) {
	this.dt_stampa = dt_stampa;
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
 * Setter dell'attributo pg_fattura
 */
public void setPg_fattura(java.lang.Long pg_fattura) {
	this.pg_fattura = pg_fattura;
}
/* 
 * Setter dell'attributo proc_name
 */
public void setProc_name(java.lang.String proc_name) {
	this.proc_name = proc_name;
}
/* 
 * Setter dell'attributo tipo_documento_amm
 */
public void setTipo_documento_amm(java.lang.String tipo_documento_amm) {
	this.tipo_documento_amm = tipo_documento_amm;
}
/* 
 * Setter dell'attributo utente
 */
public void setUtente(java.lang.String utente) {
	this.utente = utente;
}
}
