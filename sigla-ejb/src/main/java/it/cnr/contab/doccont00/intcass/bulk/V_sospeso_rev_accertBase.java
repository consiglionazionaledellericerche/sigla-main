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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_sospeso_rev_accertBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;

	// CD_SOSPESO VARCHAR(20) NOT NULL
	private java.lang.String cd_sospeso;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// IM_ACCERTAMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_accertamento;

	// IM_ASSOCIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato;

	// IM_SOSPESO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_sospeso;

	//	ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(10,0)
  	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_accertamento;

	// PG_REVERSALE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_reversale;

	// TI_ENTRATA_SPESA CHAR(1) NOT NULL
	private java.lang.String ti_entrata_spesa;

	// TI_SOSPESO_RISCONTRO CHAR(1) NOT NULL
	private java.lang.String ti_sospeso_riscontro;

	private java.math.BigDecimal im_reversale;

public java.math.BigDecimal getIm_reversale() {
		return im_reversale;
	}
	public void setIm_reversale(java.math.BigDecimal im_reversale) {
		this.im_reversale = im_reversale;
	}
public V_sospeso_rev_accertBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_sospeso
 */
public java.lang.String getCd_sospeso() {
	return cd_sospeso;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
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
 * Getter dell'attributo im_accertamento
 */
public java.math.BigDecimal getIm_accertamento() {
	return im_accertamento;
}
/* 
 * Getter dell'attributo im_associato
 */
public java.math.BigDecimal getIm_associato() {
	return im_associato;
}
/* 
 * Getter dell'attributo im_sospeso
 */
public java.math.BigDecimal getIm_sospeso() {
	return im_sospeso;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento () {
	return esercizio_ori_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_reversale
 */
public java.lang.Long getPg_reversale() {
	return pg_reversale;
}
/* 
 * Getter dell'attributo ti_entrata_spesa
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}
/* 
 * Getter dell'attributo ti_sospeso_riscontro
 */
public java.lang.String getTi_sospeso_riscontro() {
	return ti_sospeso_riscontro;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_sospeso
 */
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.cd_sospeso = cd_sospeso;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
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
 * Setter dell'attributo im_accertamento
 */
public void setIm_accertamento(java.math.BigDecimal im_accertamento) {
	this.im_accertamento = im_accertamento;
}
/* 
 * Setter dell'attributo im_associato
 */
public void setIm_associato(java.math.BigDecimal im_associato) {
	this.im_associato = im_associato;
}
/* 
 * Setter dell'attributo im_sospeso
 */
public void setIm_sospeso(java.math.BigDecimal im_sospeso) {
	this.im_sospeso = im_sospeso;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento)  {
	this.esercizio_ori_accertamento=esercizio_ori_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo pg_reversale
 */
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.pg_reversale = pg_reversale;
}
/* 
 * Setter dell'attributo ti_entrata_spesa
 */
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.ti_entrata_spesa = ti_entrata_spesa;
}
/* 
 * Setter dell'attributo ti_sospeso_riscontro
 */
public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro) {
	this.ti_sospeso_riscontro = ti_sospeso_riscontro;
}
}
