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

public class Mandato_rigaKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_documento_amm;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_CDS_DOC_AMM VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_doc_amm;

    // ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_obbligazione_scadenzario;

	// PG_DOC_AMM DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_doc_amm;

	// PG_MANDATO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_mandato;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_obbligazione;

	// CD_UO_DOC_AMM VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_uo_doc_amm;

	// ESERCIZIO_DOC_AMM DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_doc_amm;

public Mandato_rigaKey() {
	super();
}
public Mandato_rigaKey(java.lang.String cd_cds,java.lang.String cd_cds_doc_amm,java.lang.String cd_tipo_documento_amm,java.lang.String cd_uo_doc_amm,java.lang.Integer esercizio,java.lang.Integer esercizio_doc_amm,java.lang.Integer esercizio_obbligazione,java.lang.Long pg_doc_amm,java.lang.Long pg_mandato,java.lang.Integer esercizio_ori_obbligazione,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario) {
	this.cd_cds = cd_cds;
	this.cd_cds_doc_amm = cd_cds_doc_amm;
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
	this.cd_uo_doc_amm = cd_uo_doc_amm;
	this.esercizio = esercizio;
	this.esercizio_doc_amm = esercizio_doc_amm;
	this.esercizio_obbligazione = esercizio_obbligazione;
	this.pg_doc_amm = pg_doc_amm;
	this.pg_mandato = pg_mandato;
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
	this.pg_obbligazione = pg_obbligazione;
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Mandato_rigaKey)) return false;
	Mandato_rigaKey k = (Mandato_rigaKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_cds_doc_amm(),k.getCd_cds_doc_amm())) return false;
	if(!compareKey(getCd_tipo_documento_amm(),k.getCd_tipo_documento_amm())) return false;
	if(!compareKey(getCd_uo_doc_amm(),k.getCd_uo_doc_amm())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_doc_amm(),k.getEsercizio_doc_amm())) return false;
	if(!compareKey(getEsercizio_obbligazione(),k.getEsercizio_obbligazione())) return false;
	if(!compareKey(getPg_doc_amm(),k.getPg_doc_amm())) return false;
	if(!compareKey(getPg_mandato(),k.getPg_mandato())) return false;
	if(!compareKey(getEsercizio_ori_obbligazione(),k.getEsercizio_ori_obbligazione())) return false;
	if(!compareKey(getPg_obbligazione(),k.getPg_obbligazione())) return false;
	if(!compareKey(getPg_obbligazione_scadenzario(),k.getPg_obbligazione_scadenzario())) return false;
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
 * Getter dell'attributo esercizio_doc_amm
 */
public java.lang.Integer getEsercizio_doc_amm() {
	return esercizio_doc_amm;
}
/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/* 
 * Getter dell'attributo pg_doc_amm
 */
public java.lang.Long getPg_doc_amm() {
	return pg_doc_amm;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_cds_doc_amm())+
		calculateKeyHashCode(getCd_tipo_documento_amm())+
		calculateKeyHashCode(getCd_uo_doc_amm())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_doc_amm())+
		calculateKeyHashCode(getEsercizio_obbligazione())+
		calculateKeyHashCode(getPg_doc_amm())+
		calculateKeyHashCode(getPg_mandato())+
		calculateKeyHashCode(getEsercizio_ori_obbligazione())+
		calculateKeyHashCode(getPg_obbligazione())+
		calculateKeyHashCode(getPg_obbligazione_scadenzario());
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
 * Setter dell'attributo esercizio_doc_amm
 */
public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm) {
	this.esercizio_doc_amm = esercizio_doc_amm;
}
/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}
/* 
 * Setter dell'attributo pg_doc_amm
 */
public void setPg_doc_amm(java.lang.Long pg_doc_amm) {
	this.pg_doc_amm = pg_doc_amm;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
}
