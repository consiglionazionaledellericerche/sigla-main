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

package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_economaleBase extends Fondo_economaleKey implements Keyed {
	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_SOSPESO VARCHAR(24)
	private java.lang.String cd_sospeso;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// DS_FONDO VARCHAR(300)
	private java.lang.String ds_fondo;

	// ESERCIZIO_REVERSALE DECIMAL(4,0)
	private java.lang.Integer esercizio_reversale;

	// FL_APERTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_aperto;

	// FL_REV_DA_EMETTERE CHAR(1) NOT NULL
	private java.lang.Boolean fl_rev_da_emettere;

	// IM_AMMONTARE_FONDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ammontare_fondo;

	// IM_AMMONTARE_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ammontare_iniziale;

	// IM_LIMITE_MIN_REINTEGRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_limite_min_reintegro;

	// IM_MAX_GG_SPESA_DOC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_max_gg_spesa_doc;

	// IM_MAX_GG_SPESA_NON_DOC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_max_gg_spesa_non_doc;

	// IM_MAX_MM_SPESA_DOC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_max_mm_spesa_doc;

	// IM_MAX_MM_SPESA_NON_DOC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_max_mm_spesa_non_doc;

	// IM_RESIDUO_FONDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_residuo_fondo;

	// IM_TOTALE_NETTO_SPESE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_netto_spese;

	// IM_TOTALE_REINTEGRI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_reintegri;

	// IM_TOTALE_SPESE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_spese;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// PG_MANDATO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_mandato;

	// PG_REVERSALE DECIMAL(10,0)
	private java.lang.Long pg_reversale;

	// TI_ES_SOSPESO CHAR(1)
	private java.lang.String ti_es_sospeso;

	// TI_SR_SOSPESO CHAR(1)
	private java.lang.String ti_sr_sospeso;
	
	// CD_CDS_SOSPESO VARCHAR(30)
		private java.lang.String cd_cds_sospeso;

public Fondo_economaleBase() {
	super();
}
public Fondo_economaleBase(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio);
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_sospeso
 */
public java.lang.String getCd_sospeso() {
	return cd_sospeso;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo ds_fondo
 */
public java.lang.String getDs_fondo() {
	return ds_fondo;
}
/* 
 * Getter dell'attributo esercizio_reversale
 */
public java.lang.Integer getEsercizio_reversale() {
	return esercizio_reversale;
}
/* 
 * Getter dell'attributo fl_aperto
 */
public java.lang.Boolean getFl_aperto() {
	return fl_aperto;
}
/* 
 * Getter dell'attributo fl_rev_da_emettere
 */
public java.lang.Boolean getFl_rev_da_emettere() {
	return fl_rev_da_emettere;
}
/* 
 * Getter dell'attributo im_ammontare_fondo
 */
public java.math.BigDecimal getIm_ammontare_fondo() {
	return im_ammontare_fondo;
}
/* 
 * Getter dell'attributo im_ammontare_iniziale
 */
public java.math.BigDecimal getIm_ammontare_iniziale() {
	return im_ammontare_iniziale;
}
/* 
 * Getter dell'attributo im_limite_min_reintegro
 */
public java.math.BigDecimal getIm_limite_min_reintegro() {
	return im_limite_min_reintegro;
}
/* 
 * Getter dell'attributo im_max_gg_spesa_doc
 */
public java.math.BigDecimal getIm_max_gg_spesa_doc() {
	return im_max_gg_spesa_doc;
}
/* 
 * Getter dell'attributo im_max_gg_spesa_non_doc
 */
public java.math.BigDecimal getIm_max_gg_spesa_non_doc() {
	return im_max_gg_spesa_non_doc;
}
/* 
 * Getter dell'attributo im_max_mm_spesa_doc
 */
public java.math.BigDecimal getIm_max_mm_spesa_doc() {
	return im_max_mm_spesa_doc;
}
/* 
 * Getter dell'attributo im_max_mm_spesa_non_doc
 */
public java.math.BigDecimal getIm_max_mm_spesa_non_doc() {
	return im_max_mm_spesa_non_doc;
}
/* 
 * Getter dell'attributo im_residuo_fondo
 */
public java.math.BigDecimal getIm_residuo_fondo() {
	return im_residuo_fondo;
}
/* 
 * Getter dell'attributo im_totale_netto_spese
 */
public java.math.BigDecimal getIm_totale_netto_spese() {
	return im_totale_netto_spese;
}
/* 
 * Getter dell'attributo im_totale_reintegri
 */
public java.math.BigDecimal getIm_totale_reintegri() {
	return im_totale_reintegri;
}
/* 
 * Getter dell'attributo im_totale_spese
 */
public java.math.BigDecimal getIm_totale_spese() {
	return im_totale_spese;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo pg_reversale
 */
public java.lang.Long getPg_reversale() {
	return pg_reversale;
}
/* 
 * Getter dell'attributo ti_es_sospeso
 */
public java.lang.String getTi_es_sospeso() {
	return ti_es_sospeso;
}
/* 
 * Getter dell'attributo ti_sr_sospeso
 */
public java.lang.String getTi_sr_sospeso() {
	return ti_sr_sospeso;
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_sospeso
 */
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.cd_sospeso = cd_sospeso;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo ds_fondo
 */
public void setDs_fondo(java.lang.String ds_fondo) {
	this.ds_fondo = ds_fondo;
}
/* 
 * Setter dell'attributo esercizio_reversale
 */
public void setEsercizio_reversale(java.lang.Integer esercizio_reversale) {
	this.esercizio_reversale = esercizio_reversale;
}
/* 
 * Setter dell'attributo fl_aperto
 */
public void setFl_aperto(java.lang.Boolean fl_aperto) {
	this.fl_aperto = fl_aperto;
}
/* 
 * Setter dell'attributo fl_rev_da_emettere
 */
public void setFl_rev_da_emettere(java.lang.Boolean fl_rev_da_emettere) {
	this.fl_rev_da_emettere = fl_rev_da_emettere;
}
/* 
 * Setter dell'attributo im_ammontare_fondo
 */
public void setIm_ammontare_fondo(java.math.BigDecimal im_ammontare_fondo) {
	this.im_ammontare_fondo = im_ammontare_fondo;
}
/* 
 * Setter dell'attributo im_ammontare_iniziale
 */
public void setIm_ammontare_iniziale(java.math.BigDecimal im_ammontare_iniziale) {
	this.im_ammontare_iniziale = im_ammontare_iniziale;
}
/* 
 * Setter dell'attributo im_limite_min_reintegro
 */
public void setIm_limite_min_reintegro(java.math.BigDecimal im_limite_min_reintegro) {
	this.im_limite_min_reintegro = im_limite_min_reintegro;
}
/* 
 * Setter dell'attributo im_max_gg_spesa_doc
 */
public void setIm_max_gg_spesa_doc(java.math.BigDecimal im_max_gg_spesa_doc) {
	this.im_max_gg_spesa_doc = im_max_gg_spesa_doc;
}
/* 
 * Setter dell'attributo im_max_gg_spesa_non_doc
 */
public void setIm_max_gg_spesa_non_doc(java.math.BigDecimal im_max_gg_spesa_non_doc) {
	this.im_max_gg_spesa_non_doc = im_max_gg_spesa_non_doc;
}
/* 
 * Setter dell'attributo im_max_mm_spesa_doc
 */
public void setIm_max_mm_spesa_doc(java.math.BigDecimal im_max_mm_spesa_doc) {
	this.im_max_mm_spesa_doc = im_max_mm_spesa_doc;
}
/* 
 * Setter dell'attributo im_max_mm_spesa_non_doc
 */
public void setIm_max_mm_spesa_non_doc(java.math.BigDecimal im_max_mm_spesa_non_doc) {
	this.im_max_mm_spesa_non_doc = im_max_mm_spesa_non_doc;
}
/* 
 * Setter dell'attributo im_residuo_fondo
 */
public void setIm_residuo_fondo(java.math.BigDecimal im_residuo_fondo) {
	this.im_residuo_fondo = im_residuo_fondo;
}
/* 
 * Setter dell'attributo im_totale_netto_spese
 */
public void setIm_totale_netto_spese(java.math.BigDecimal im_totale_netto_spese) {
	this.im_totale_netto_spese = im_totale_netto_spese;
}
/* 
 * Setter dell'attributo im_totale_reintegri
 */
public void setIm_totale_reintegri(java.math.BigDecimal im_totale_reintegri) {
	this.im_totale_reintegri = im_totale_reintegri;
}
/* 
 * Setter dell'attributo im_totale_spese
 */
public void setIm_totale_spese(java.math.BigDecimal im_totale_spese) {
	this.im_totale_spese = im_totale_spese;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo pg_reversale
 */
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.pg_reversale = pg_reversale;
}
/* 
 * Setter dell'attributo ti_es_sospeso
 */
public void setTi_es_sospeso(java.lang.String ti_es_sospeso) {
	this.ti_es_sospeso = ti_es_sospeso;
}
/* 
 * Setter dell'attributo ti_sr_sospeso
 */
public void setTi_sr_sospeso(java.lang.String ti_sr_sospeso) {
	this.ti_sr_sospeso = ti_sr_sospeso;
}
public java.lang.String getCd_cds_sospeso() {
	return cd_cds_sospeso;
}
public void setCd_cds_sospeso(java.lang.String cd_cds_sospeso) {
	this.cd_cds_sospeso = cd_cds_sospeso;
}
}
