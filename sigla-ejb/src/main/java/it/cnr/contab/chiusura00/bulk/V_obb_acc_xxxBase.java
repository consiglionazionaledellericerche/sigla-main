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

package it.cnr.contab.chiusura00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_obb_acc_xxxBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cd_cds_origine;

	// CD_CDS_ORI_RIPORTO VARCHAR(30)
	private java.lang.String cd_cds_ori_riporto;

	// CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;

	// CD_VOCE VARCHAR(50)
	private java.lang.String cd_voce;

	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	private java.lang.String cd_tipo_documento_cont;

	// CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cd_uo_origine;

	// ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

	// ESERCIZIO_COMPETENZA DECIMAL(4,0)
	private java.lang.Integer esercizio_competenza;

	// ESERCIZIO_ORI_RIPORTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_riporto;

	// FL_PGIRO CHAR(1)
	private java.lang.Boolean fl_pgiro;

	// FL_RISCONTRATO VARCHAR(1)
//	private java.lang.String fl_riscontrato;

	// IM_ACC_OBB DECIMAL(15,2)
	private java.math.BigDecimal im_acc_obb;

	// ESERCIZIO_ORI_ACC_OBB DECIMAL(10,0)
  	private java.lang.Integer esercizio_ori_acc_obb;

	// PG_ACC_OBB DECIMAL(10,0)
	private java.lang.Long pg_acc_obb;

	// ESERCIZIO_ORI_ORI_RIPORTO DECIMAL(10,0)
	private java.lang.Integer esercizio_ori_ori_riporto;

	// PG_ACC_OBB_ORI_RIPORTO DECIMAL(10,0)
	private java.lang.Long pg_acc_obb_ori_riporto;

	// RIPORTATO CHAR(1)
	private java.lang.String riportato;

	// TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;

public V_obb_acc_xxxBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_ori_riporto
 */
public java.lang.String getCd_cds_ori_riporto() {
	return cd_cds_ori_riporto;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont
 */
public java.lang.String getCd_tipo_documento_cont() {
	return cd_tipo_documento_cont;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (09/01/2004 11.27.15)
 * @return java.lang.String
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
 * Getter dell'attributo esercizio_ori_riporto
 */
public java.lang.Integer getEsercizio_ori_riporto() {
	return esercizio_ori_riporto;
}
/* 
 * Getter dell'attributo fl_pgiro
 */
public java.lang.Boolean getFl_pgiro() {
	return fl_pgiro;
}
/* 
 * Getter dell'attributo im_acc_obb
 */
public java.math.BigDecimal getIm_acc_obb() {
	return im_acc_obb;
}
/* 
 * Getter dell'attributo esercizio_ori_acc_obb
 */
public java.lang.Integer getEsercizio_ori_acc_obb () {
	return esercizio_ori_acc_obb;
}
/* 
 * Getter dell'attributo pg_acc_obb
 */
public java.lang.Long getPg_acc_obb() {
	return pg_acc_obb;
}
/* 
 * Getter dell'attributo esercizio_ori_ori_riporto
 */
public java.lang.Integer getEsercizio_ori_ori_riporto () {
	return esercizio_ori_ori_riporto;
}
/* 
 * Getter dell'attributo pg_acc_obb_ori_riporto
 */
public java.lang.Long getPg_acc_obb_ori_riporto() {
	return pg_acc_obb_ori_riporto;
}
/* 
 * Getter dell'attributo riportato
 */
public java.lang.String getRiportato() {
	return riportato;
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
 * Setter dell'attributo cd_cds_ori_riporto
 */
public void setCd_cds_ori_riporto(java.lang.String cd_cds_ori_riporto) {
	this.cd_cds_ori_riporto = cd_cds_ori_riporto;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont
 */
public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont) {
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (09/01/2004 11.27.15)
 * @param newCd_voce java.lang.String
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
 * Setter dell'attributo esercizio_ori_riporto
 */
public void setEsercizio_ori_riporto(java.lang.Integer esercizio_ori_riporto) {
	this.esercizio_ori_riporto = esercizio_ori_riporto;
}
/* 
 * Setter dell'attributo fl_pgiro
 */
public void setFl_pgiro(java.lang.Boolean fl_pgiro) {
	this.fl_pgiro = fl_pgiro;
}
/* 
 * Setter dell'attributo im_acc_obb
 */
public void setIm_acc_obb(java.math.BigDecimal im_acc_obb) {
	this.im_acc_obb = im_acc_obb;
}
/* 
 * Setter dell'attributo esercizio_ori_acc_obb
 */
public void setEsercizio_ori_acc_obb(java.lang.Integer esercizio_ori_acc_obb)  {
	this.esercizio_ori_acc_obb=esercizio_ori_acc_obb;
}
/* 
 * Setter dell'attributo pg_acc_obb
 */
public void setPg_acc_obb(java.lang.Long pg_acc_obb) {
	this.pg_acc_obb = pg_acc_obb;
}
/* 
 * Setter dell'attributo esercizio_ori_ori_riporto
 */
public void setEsercizio_ori_ori_riporto(java.lang.Integer esercizio_ori_ori_riporto)  {
	this.esercizio_ori_ori_riporto=esercizio_ori_ori_riporto;
}
/* 
 * Setter dell'attributo pg_acc_obb_ori_riporto
 */
public void setPg_acc_obb_ori_riporto(java.lang.Long pg_acc_obb_ori_riporto) {
	this.pg_acc_obb_ori_riporto = pg_acc_obb_ori_riporto;
}
/* 
 * Setter dell'attributo riportato
 */
public void setRiportato(java.lang.String riportato) {
	this.riportato = riportato;
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
