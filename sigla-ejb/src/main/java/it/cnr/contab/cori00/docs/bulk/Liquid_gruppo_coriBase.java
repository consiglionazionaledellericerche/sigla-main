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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_coriBase extends Liquid_gruppo_coriKey implements Keyed {
	// CD_CDS_ACC_COMPENS VARCHAR(30)
	private java.lang.String cd_cds_acc_compens;

	// CD_CDS_DOC VARCHAR(30)
	private java.lang.String cd_cds_doc;

	// CD_CDS_OBB_ACCENTR VARCHAR(30)
	private java.lang.String cd_cds_obb_accentr;

	// ESERCIZIO_ACC_COMPENS DECIMAL(4,0)
	private java.lang.Integer esercizio_acc_compens;

	// ESERCIZIO_DOC DECIMAL(4,0)
	private java.lang.Integer esercizio_doc;

	// ESERCIZIO_OBB_ACCENTR DECIMAL(4,0)
	private java.lang.Integer esercizio_obb_accentr;

	// FL_ACCENTRATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_accentrato;

	// IM_LIQUIDATO DECIMAL(15,2)
	private java.math.BigDecimal im_liquidato;

	// ESERCIZIO_ORI_ACC_COMPENS DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_acc_compens;

	// PG_ACC_COMPENS DECIMAL(10,0)
	private java.lang.Long pg_acc_compens;

	// PG_DOC DECIMAL(10,0)
	private java.lang.Long pg_doc;

	// PG_GRUPPO_CENTRO DECIMAL(10,0)
	private java.lang.Long pg_gruppo_centro;

	// ESERCIZIO_ORI_OBB_ACCENTR DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obb_accentr;

	// PG_OBB_ACCENTR DECIMAL(10,0)
	private java.lang.Long pg_obb_accentr;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

public Liquid_gruppo_coriBase() {
	super();
}
public Liquid_gruppo_coriBase(java.lang.String cd_cds,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione) {
	super(cd_cds,cd_gruppo_cr,cd_regione,esercizio,pg_comune,pg_liquidazione);
}
public Liquid_gruppo_coriBase(java.lang.String cd_cds,java.lang.String cd_cds_origine,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.String cd_unita_organizzativa,java.lang.String cd_uo_origine,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione,java.lang.Integer pg_liquidazione_origine) {
	super(cd_cds,cd_cds_origine,cd_gruppo_cr,cd_regione,cd_unita_organizzativa,cd_uo_origine,esercizio,pg_comune,pg_liquidazione,pg_liquidazione_origine);
}
/* 
 * Getter dell'attributo cd_cds_acc_compens
 */
public java.lang.String getCd_cds_acc_compens() {
	return cd_cds_acc_compens;
}
/* 
 * Getter dell'attributo cd_cds_doc
 */
public java.lang.String getCd_cds_doc() {
	return cd_cds_doc;
}
/* 
 * Getter dell'attributo cd_cds_obb_accentr
 */
public java.lang.String getCd_cds_obb_accentr() {
	return cd_cds_obb_accentr;
}
/* 
 * Getter dell'attributo esercizio_acc_compens
 */
public java.lang.Integer getEsercizio_acc_compens() {
	return esercizio_acc_compens;
}
/* 
 * Getter dell'attributo esercizio_doc
 */
public java.lang.Integer getEsercizio_doc() {
	return esercizio_doc;
}
/* 
 * Getter dell'attributo esercizio_obb_accentr
 */
public java.lang.Integer getEsercizio_obb_accentr() {
	return esercizio_obb_accentr;
}
/* 
 * Getter dell'attributo fl_accentrato
 */
public java.lang.Boolean getFl_accentrato() {
	return fl_accentrato;
}
/* 
 * Getter dell'attributo im_liquidato
 */
public java.math.BigDecimal getIm_liquidato() {
	return im_liquidato;
}
/* 
 * Getter dell'attributo esercizio_ori_acc_compens
 */
public java.lang.Integer getEsercizio_ori_acc_compens() {
	return esercizio_ori_acc_compens;
}
/* 
 * Getter dell'attributo pg_acc_compens
 */
public java.lang.Long getPg_acc_compens() {
	return pg_acc_compens;
}
/* 
 * Getter dell'attributo pg_doc
 */
public java.lang.Long getPg_doc() {
	return pg_doc;
}
/* 
 * Getter dell'attributo pg_gruppo_centro
 */
public java.lang.Long getPg_gruppo_centro() {
	return pg_gruppo_centro;
}
/* 
 * Getter dell'attributo esercizio_ori_obb_accentr
 */
public java.lang.Integer getEsercizio_ori_obb_accentr() {
	return esercizio_ori_obb_accentr;
}
/* 
 * Getter dell'attributo pg_obb_accentr
 */
public java.lang.Long getPg_obb_accentr() {
	return pg_obb_accentr;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo cd_cds_acc_compens
 */
public void setCd_cds_acc_compens(java.lang.String cd_cds_acc_compens) {
	this.cd_cds_acc_compens = cd_cds_acc_compens;
}
/* 
 * Setter dell'attributo cd_cds_doc
 */
public void setCd_cds_doc(java.lang.String cd_cds_doc) {
	this.cd_cds_doc = cd_cds_doc;
}
/* 
 * Setter dell'attributo cd_cds_obb_accentr
 */
public void setCd_cds_obb_accentr(java.lang.String cd_cds_obb_accentr) {
	this.cd_cds_obb_accentr = cd_cds_obb_accentr;
}
/* 
 * Setter dell'attributo esercizio_acc_compens
 */
public void setEsercizio_acc_compens(java.lang.Integer esercizio_acc_compens) {
	this.esercizio_acc_compens = esercizio_acc_compens;
}
/* 
 * Setter dell'attributo esercizio_doc
 */
public void setEsercizio_doc(java.lang.Integer esercizio_doc) {
	this.esercizio_doc = esercizio_doc;
}
/* 
 * Setter dell'attributo esercizio_obb_accentr
 */
public void setEsercizio_obb_accentr(java.lang.Integer esercizio_obb_accentr) {
	this.esercizio_obb_accentr = esercizio_obb_accentr;
}
/* 
 * Setter dell'attributo fl_accentrato
 */
public void setFl_accentrato(java.lang.Boolean fl_accentrato) {
	this.fl_accentrato = fl_accentrato;
}
/* 
 * Setter dell'attributo im_liquidato
 */
public void setIm_liquidato(java.math.BigDecimal im_liquidato) {
	this.im_liquidato = im_liquidato;
}
/* 
 * Setter dell'attributo esercizio_ori_acc_compens
 */
public void setEsercizio_ori_acc_compens(java.lang.Integer esercizio_ori_acc_compens) {
	this.esercizio_ori_acc_compens = esercizio_ori_acc_compens;
}
/* 
 * Setter dell'attributo pg_acc_compens
 */
public void setPg_acc_compens(java.lang.Long pg_acc_compens) {
	this.pg_acc_compens = pg_acc_compens;
}
/* 
 * Setter dell'attributo pg_doc
 */
public void setPg_doc(java.lang.Long pg_doc) {
	this.pg_doc = pg_doc;
}
/* 
 * Setter dell'attributo pg_gruppo_centro
 */
public void setPg_gruppo_centro(java.lang.Long pg_gruppo_centro) {
	this.pg_gruppo_centro = pg_gruppo_centro;
}
/* 
 * Setter dell'attributo esercizio_ori_obb_accentr
 */
public void setEsercizio_ori_obb_accentr(java.lang.Integer esercizio_ori_obb_accentr) {
	this.esercizio_ori_obb_accentr = esercizio_ori_obb_accentr;
}
/* 
 * Setter dell'attributo pg_obb_accentr
 */
public void setPg_obb_accentr(java.lang.Long pg_obb_accentr) {
	this.pg_obb_accentr = pg_obb_accentr;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
