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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_cofiBase extends Stipendi_cofiKey implements Keyed {
	// CD_CDS_COMP VARCHAR(30)
	private java.lang.String cd_cds_comp;

	// CD_CDS_DOC_GEN VARCHAR(30)
	private java.lang.String cd_cds_doc_gen;

	// CD_CDS_MANDATO VARCHAR(30)
	private java.lang.String cd_cds_mandato;

	// CD_TIPO_DOC_GEN VARCHAR(10)
	private java.lang.String cd_tipo_doc_gen;

	// CD_UO_COMP VARCHAR(30)
	private java.lang.String cd_uo_comp;

	// CD_UO_DOC_GEN VARCHAR(30)
	private java.lang.String cd_uo_doc_gen;

	// ESERCIZIO_COMP DECIMAL(4,0)
	private java.lang.Integer esercizio_comp;

	// ESERCIZIO_DOC_GEN DECIMAL(4,0)
	private java.lang.Integer esercizio_doc_gen;

	// ESERCIZIO_MANDATO DECIMAL(4,0)
	private java.lang.Integer esercizio_mandato;

	// PG_COMP DECIMAL(10,0)
	private java.lang.Long pg_comp;

	// PG_DOC_GEN DECIMAL(10,0)
	private java.lang.Long pg_doc_gen;

	// PG_MANDATO DECIMAL(10,0)
	private java.lang.Long pg_mandato;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

public Stipendi_cofiBase() {
	super();
}
public Stipendi_cofiBase(java.lang.Integer esercizio,java.lang.Integer mese) {
	super(esercizio,mese);
}
/* 
 * Getter dell'attributo cd_cds_comp
 */
public java.lang.String getCd_cds_comp() {
	return cd_cds_comp;
}
/* 
 * Getter dell'attributo cd_cds_doc_gen
 */
public java.lang.String getCd_cds_doc_gen() {
	return cd_cds_doc_gen;
}
/* 
 * Getter dell'attributo cd_cds_mandato
 */
public java.lang.String getCd_cds_mandato() {
	return cd_cds_mandato;
}
/* 
 * Getter dell'attributo cd_tipo_doc_gen
 */
public java.lang.String getCd_tipo_doc_gen() {
	return cd_tipo_doc_gen;
}
/* 
 * Getter dell'attributo cd_uo_comp
 */
public java.lang.String getCd_uo_comp() {
	return cd_uo_comp;
}
/* 
 * Getter dell'attributo cd_uo_doc_gen
 */
public java.lang.String getCd_uo_doc_gen() {
	return cd_uo_doc_gen;
}
/* 
 * Getter dell'attributo esercizio_comp
 */
public java.lang.Integer getEsercizio_comp() {
	return esercizio_comp;
}
/* 
 * Getter dell'attributo esercizio_doc_gen
 */
public java.lang.Integer getEsercizio_doc_gen() {
	return esercizio_doc_gen;
}
/* 
 * Getter dell'attributo esercizio_mandato
 */
public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}
/* 
 * Getter dell'attributo pg_comp
 */
public java.lang.Long getPg_comp() {
	return pg_comp;
}
/* 
 * Getter dell'attributo pg_doc_gen
 */
public java.lang.Long getPg_doc_gen() {
	return pg_doc_gen;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo cd_cds_comp
 */
public void setCd_cds_comp(java.lang.String cd_cds_comp) {
	this.cd_cds_comp = cd_cds_comp;
}
/* 
 * Setter dell'attributo cd_cds_doc_gen
 */
public void setCd_cds_doc_gen(java.lang.String cd_cds_doc_gen) {
	this.cd_cds_doc_gen = cd_cds_doc_gen;
}
/* 
 * Setter dell'attributo cd_cds_mandato
 */
public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.cd_cds_mandato = cd_cds_mandato;
}
/* 
 * Setter dell'attributo cd_tipo_doc_gen
 */
public void setCd_tipo_doc_gen(java.lang.String cd_tipo_doc_gen) {
	this.cd_tipo_doc_gen = cd_tipo_doc_gen;
}
/* 
 * Setter dell'attributo cd_uo_comp
 */
public void setCd_uo_comp(java.lang.String cd_uo_comp) {
	this.cd_uo_comp = cd_uo_comp;
}
/* 
 * Setter dell'attributo cd_uo_doc_gen
 */
public void setCd_uo_doc_gen(java.lang.String cd_uo_doc_gen) {
	this.cd_uo_doc_gen = cd_uo_doc_gen;
}
/* 
 * Setter dell'attributo esercizio_comp
 */
public void setEsercizio_comp(java.lang.Integer esercizio_comp) {
	this.esercizio_comp = esercizio_comp;
}
/* 
 * Setter dell'attributo esercizio_doc_gen
 */
public void setEsercizio_doc_gen(java.lang.Integer esercizio_doc_gen) {
	this.esercizio_doc_gen = esercizio_doc_gen;
}
/* 
 * Setter dell'attributo esercizio_mandato
 */
public void setEsercizio_mandato(java.lang.Integer esercizio_mandato) {
	this.esercizio_mandato = esercizio_mandato;
}
/* 
 * Setter dell'attributo pg_comp
 */
public void setPg_comp(java.lang.Long pg_comp) {
	this.pg_comp = pg_comp;
}
/* 
 * Setter dell'attributo pg_doc_gen
 */
public void setPg_doc_gen(java.lang.Long pg_doc_gen) {
	this.pg_doc_gen = pg_doc_gen;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
