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

public class Reversale_rigaKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_documento_amm;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_CDS_DOC_AMM VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_doc_amm;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento;

	// PG_REVERSALE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_reversale;

	// PG_DOC_AMM DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_doc_amm;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_accertamento;

	// CD_UO_DOC_AMM VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_uo_doc_amm;

	// ESERCIZIO_DOC_AMM DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_doc_amm;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento_scadenzario;

public Reversale_rigaKey() {
	super();
}
public Reversale_rigaKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario,java.lang.Long pg_reversale) {
	super();
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.esercizio_accertamento = esercizio_accertamento;
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
	this.pg_accertamento = pg_accertamento;
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
	this.pg_reversale = pg_reversale;
}
public Reversale_rigaKey(java.lang.String cd_cds,java.lang.String cd_cds_doc_amm,java.lang.String cd_tipo_documento_amm,java.lang.String cd_uo_doc_amm,java.lang.Integer esercizio,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_doc_amm,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario,java.lang.Long pg_doc_amm,java.lang.Long pg_reversale) {
	super();
	this.cd_cds = cd_cds;
	this.cd_cds_doc_amm = cd_cds_doc_amm;
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
	this.cd_uo_doc_amm = cd_uo_doc_amm;
	this.esercizio = esercizio;
	this.esercizio_accertamento = esercizio_accertamento;
	this.esercizio_doc_amm = esercizio_doc_amm;
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
	this.pg_accertamento = pg_accertamento;
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
	this.pg_doc_amm = pg_doc_amm;
	this.pg_reversale = pg_reversale;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Reversale_rigaKey)) return false;
	Reversale_rigaKey k = (Reversale_rigaKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_accertamento(),k.getEsercizio_accertamento())) return false;
	if(!compareKey(getEsercizio_ori_accertamento(),k.getEsercizio_ori_accertamento())) return false;
	if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
	if(!compareKey(getPg_accertamento_scadenzario(),k.getPg_accertamento_scadenzario())) return false;
	if(!compareKey(getPg_reversale(),k.getPg_reversale())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Reversale_rigaKey)) return false;
	Reversale_rigaKey k = (Reversale_rigaKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_cds_doc_amm(),k.getCd_cds_doc_amm())) return false;
	if(!compareKey(getCd_tipo_documento_amm(),k.getCd_tipo_documento_amm())) return false;
	if(!compareKey(getCd_uo_doc_amm(),k.getCd_uo_doc_amm())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_accertamento(),k.getEsercizio_accertamento())) return false;
	if(!compareKey(getEsercizio_doc_amm(),k.getEsercizio_doc_amm())) return false;
	if(!compareKey(getEsercizio_ori_accertamento(),k.getEsercizio_ori_accertamento())) return false;
	if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
	if(!compareKey(getPg_accertamento_scadenzario(),k.getPg_accertamento_scadenzario())) return false;
	if(!compareKey(getPg_doc_amm(),k.getPg_doc_amm())) return false;
	if(!compareKey(getPg_reversale(),k.getPg_reversale())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_doc_amm
 */
public java.lang.String getCd_cds_doc_amm() {
	return cd_cds_doc_amm;
}
/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
/* 
 * Getter dell'attributo cd_uo_doc_amm
 */
public java.lang.String getCd_uo_doc_amm() {
	return cd_uo_doc_amm;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_accertamento
 */
public java.lang.Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
}
/* 
 * Getter dell'attributo esercizio_doc_amm
 */
public java.lang.Integer getEsercizio_doc_amm() {
	return esercizio_doc_amm;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento_scadenzario
 */
public java.lang.Long getPg_accertamento_scadenzario() {
	return pg_accertamento_scadenzario;
}
/* 
 * Getter dell'attributo pg_doc_amm
 */
public java.lang.Long getPg_doc_amm() {
	return pg_doc_amm;
}
/* 
 * Getter dell'attributo pg_reversale
 */
public java.lang.Long getPg_reversale() {
	return pg_reversale;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_accertamento())+
		calculateKeyHashCode(getEsercizio_ori_accertamento())+
		calculateKeyHashCode(getPg_accertamento())+
		calculateKeyHashCode(getPg_accertamento_scadenzario())+
		calculateKeyHashCode(getPg_reversale());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_cds_doc_amm())+
		calculateKeyHashCode(getCd_tipo_documento_amm())+
		calculateKeyHashCode(getCd_uo_doc_amm())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_accertamento())+
		calculateKeyHashCode(getEsercizio_doc_amm())+
		calculateKeyHashCode(getEsercizio_ori_accertamento())+
		calculateKeyHashCode(getPg_accertamento())+
		calculateKeyHashCode(getPg_accertamento_scadenzario())+
		calculateKeyHashCode(getPg_doc_amm())+
		calculateKeyHashCode(getPg_reversale());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_doc_amm
 */
public void setCd_cds_doc_amm(java.lang.String cd_cds_doc_amm) {
	this.cd_cds_doc_amm = cd_cds_doc_amm;
}
/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}
/* 
 * Setter dell'attributo cd_uo_doc_amm
 */
public void setCd_uo_doc_amm(java.lang.String cd_uo_doc_amm) {
	this.cd_uo_doc_amm = cd_uo_doc_amm;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
}
/* 
 * Setter dell'attributo esercizio_doc_amm
 */
public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm) {
	this.esercizio_doc_amm = esercizio_doc_amm;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento_scadenzario
 */
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) {
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
}
/* 
 * Setter dell'attributo pg_doc_amm
 */
public void setPg_doc_amm(java.lang.Long pg_doc_amm) {
	this.pg_doc_amm = pg_doc_amm;
}
/* 
 * Setter dell'attributo pg_reversale
 */
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.pg_reversale = pg_reversale;
}
}
