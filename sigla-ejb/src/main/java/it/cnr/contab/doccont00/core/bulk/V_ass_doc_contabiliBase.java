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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ass_doc_contabiliBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_CDS_COLL VARCHAR(30)
	private java.lang.String cd_cds_coll;

	// CD_TIPO_DOCUMENTO_CONT CHAR(3)
	private java.lang.String cd_tipo_documento_cont;

	// CD_TIPO_DOCUMENTO_CONT_COLL CHAR(3)
	private java.lang.String cd_tipo_documento_cont_coll;

	// ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

	// ESERCIZIO_COLL DECIMAL(4,0)
	private java.lang.Integer esercizio_coll;

	// FL_CON_MAN_PRC CHAR(1)
	private java.lang.Boolean fl_con_man_prc;

	// PG_DOCUMENTO_CONT DECIMAL(10,0)
	private java.lang.Long pg_documento_cont;

	// PG_DOCUMENTO_CONT_COLL DECIMAL(10,0)
	private java.lang.Long pg_documento_cont_coll;

public V_ass_doc_contabiliBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_coll
 */
public java.lang.String getCd_cds_coll() {
	return cd_cds_coll;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont
 */
public java.lang.String getCd_tipo_documento_cont() {
	return cd_tipo_documento_cont;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont_coll
 */
public java.lang.String getCd_tipo_documento_cont_coll() {
	return cd_tipo_documento_cont_coll;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_coll
 */
public java.lang.Integer getEsercizio_coll() {
	return esercizio_coll;
}
/* 
 * Getter dell'attributo fl_con_man_prc
 */
public java.lang.Boolean getFl_con_man_prc() {
	return fl_con_man_prc;
}
/* 
 * Getter dell'attributo pg_documento_cont
 */
public java.lang.Long getPg_documento_cont() {
	return pg_documento_cont;
}
/* 
 * Getter dell'attributo pg_documento_cont_coll
 */
public java.lang.Long getPg_documento_cont_coll() {
	return pg_documento_cont_coll;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_coll
 */
public void setCd_cds_coll(java.lang.String cd_cds_coll) {
	this.cd_cds_coll = cd_cds_coll;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont
 */
public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont) {
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont_coll
 */
public void setCd_tipo_documento_cont_coll(java.lang.String cd_tipo_documento_cont_coll) {
	this.cd_tipo_documento_cont_coll = cd_tipo_documento_cont_coll;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo esercizio_coll
 */
public void setEsercizio_coll(java.lang.Integer esercizio_coll) {
	this.esercizio_coll = esercizio_coll;
}
/* 
 * Setter dell'attributo fl_con_man_prc
 */
public void setFl_con_man_prc(java.lang.Boolean fl_con_man_prc) {
	this.fl_con_man_prc = fl_con_man_prc;
}
/* 
 * Setter dell'attributo pg_documento_cont
 */
public void setPg_documento_cont(java.lang.Long pg_documento_cont) {
	this.pg_documento_cont = pg_documento_cont;
}
/* 
 * Setter dell'attributo pg_documento_cont_coll
 */
public void setPg_documento_cont_coll(java.lang.Long pg_documento_cont_coll) {
	this.pg_documento_cont_coll = pg_documento_cont_coll;
}
}
