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

public class V_doc_cont_compBase extends OggettoBulk implements Persistent {
	// CD_CDS_COMPENSO VARCHAR(30)
	private java.lang.String cd_cds_compenso;

	// CD_CDS_DOC_CONT VARCHAR(30)
	private java.lang.String cd_cds_doc_cont;

	// CD_UO_COMPENSO VARCHAR(30)
	private java.lang.String cd_uo_compenso;

	// DS_DOC_CONT VARCHAR(300)
	private java.lang.String ds_doc_cont;

	// ESERCIZIO_COMPENSO DECIMAL(4,0)
	private java.lang.Integer esercizio_compenso;

	// ESERCIZIO_DOC_CONT DECIMAL(4,0)
	private java.lang.Integer esercizio_doc_cont;

	// PG_COMPENSO DECIMAL(10,0)
	private java.lang.Long pg_compenso;

	// PG_DOC_CONT DECIMAL(10,0)
	private java.lang.Long pg_doc_cont;

	// PRINCIPALE CHAR(1)
	private java.lang.String principale;

	// TIPO_DOC_CONT CHAR(1)
	private java.lang.String tipo_doc_cont;

public V_doc_cont_compBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds_compenso
 */
public java.lang.String getCd_cds_compenso() {
	return cd_cds_compenso;
}
/* 
 * Getter dell'attributo cd_cds_doc_cont
 */
public java.lang.String getCd_cds_doc_cont() {
	return cd_cds_doc_cont;
}
/* 
 * Getter dell'attributo cd_uo_compenso
 */
public java.lang.String getCd_uo_compenso() {
	return cd_uo_compenso;
}
/* 
 * Getter dell'attributo ds_doc_cont
 */
public java.lang.String getDs_doc_cont() {
	return ds_doc_cont;
}
/* 
 * Getter dell'attributo esercizio_compenso
 */
public java.lang.Integer getEsercizio_compenso() {
	return esercizio_compenso;
}
/* 
 * Getter dell'attributo esercizio_doc_cont
 */
public java.lang.Integer getEsercizio_doc_cont() {
	return esercizio_doc_cont;
}
/* 
 * Getter dell'attributo pg_compenso
 */
public java.lang.Long getPg_compenso() {
	return pg_compenso;
}
/* 
 * Getter dell'attributo pg_doc_cont
 */
public java.lang.Long getPg_doc_cont() {
	return pg_doc_cont;
}
/* 
 * Getter dell'attributo principale
 */
public java.lang.String getPrincipale() {
	return principale;
}
/* 
 * Getter dell'attributo tipo_doc_cont
 */
public java.lang.String getTipo_doc_cont() {
	return tipo_doc_cont;
}
/* 
 * Setter dell'attributo cd_cds_compenso
 */
public void setCd_cds_compenso(java.lang.String cd_cds_compenso) {
	this.cd_cds_compenso = cd_cds_compenso;
}
/* 
 * Setter dell'attributo cd_cds_doc_cont
 */
public void setCd_cds_doc_cont(java.lang.String cd_cds_doc_cont) {
	this.cd_cds_doc_cont = cd_cds_doc_cont;
}
/* 
 * Setter dell'attributo cd_uo_compenso
 */
public void setCd_uo_compenso(java.lang.String cd_uo_compenso) {
	this.cd_uo_compenso = cd_uo_compenso;
}
/* 
 * Setter dell'attributo ds_doc_cont
 */
public void setDs_doc_cont(java.lang.String ds_doc_cont) {
	this.ds_doc_cont = ds_doc_cont;
}
/* 
 * Setter dell'attributo esercizio_compenso
 */
public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
	this.esercizio_compenso = esercizio_compenso;
}
/* 
 * Setter dell'attributo esercizio_doc_cont
 */
public void setEsercizio_doc_cont(java.lang.Integer esercizio_doc_cont) {
	this.esercizio_doc_cont = esercizio_doc_cont;
}
/* 
 * Setter dell'attributo pg_compenso
 */
public void setPg_compenso(java.lang.Long pg_compenso) {
	this.pg_compenso = pg_compenso;
}
/* 
 * Setter dell'attributo pg_doc_cont
 */
public void setPg_doc_cont(java.lang.Long pg_doc_cont) {
	this.pg_doc_cont = pg_doc_cont;
}
/* 
 * Setter dell'attributo principale
 */
public void setPrincipale(java.lang.String principale) {
	this.principale = principale;
}
/* 
 * Setter dell'attributo tipo_doc_cont
 */
public void setTipo_doc_cont(java.lang.String tipo_doc_cont) {
	this.tipo_doc_cont = tipo_doc_cont;
}
}
