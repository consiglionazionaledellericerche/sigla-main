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

import it.cnr.jada.persistency.*;

public class V_impegnoBase extends V_impegnoKey implements KeyedPersistent {

	private Long pg_ver_rec_scadenza;
	
	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;

	// DT_SCADENZA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_scadenza;

	// FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pgiro;

	// IM_ASSOCIATO_DOC_AMM DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato_doc_amm;

	// IM_ASSOCIATO_DOC_CONTABILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato_doc_contabile;

	// IM_SCADENZA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_scadenza;

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

public V_impegnoBase() {
	super();
}
public V_impegnoBase( String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione, Long pg_obbligazione_scadenzario ) 
{	super( cd_cds, esercizio, esercizio_originale, pg_obbligazione, pg_obbligazione_scadenzario);
	
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont
 */
public java.lang.String getCd_tipo_documento_cont() {
	return cd_tipo_documento_cont;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
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
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/* 
 * Getter dell'attributo fl_pgiro
 */
public java.lang.Boolean getFl_pgiro() {
	return fl_pgiro;
}
/* 
 * Getter dell'attributo im_associato_doc_amm
 */
public java.math.BigDecimal getIm_associato_doc_amm() {
	return im_associato_doc_amm;
}
/* 
 * Getter dell'attributo im_associato_doc_contabile
 */
public java.math.BigDecimal getIm_associato_doc_contabile() {
	return im_associato_doc_contabile;
}
/* 
 * Getter dell'attributo im_scadenza
 */
public java.math.BigDecimal getIm_scadenza() {
	return im_scadenza;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 16.22.29)
 * @return java.lang.Long
 */
public java.lang.Long getPg_ver_rec_scadenza() {
	return pg_ver_rec_scadenza;
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
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont
 */
public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont) {
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
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
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/* 
 * Setter dell'attributo fl_pgiro
 */
public void setFl_pgiro(java.lang.Boolean fl_pgiro) {
	this.fl_pgiro = fl_pgiro;
}
/* 
 * Setter dell'attributo im_associato_doc_amm
 */
public void setIm_associato_doc_amm(java.math.BigDecimal im_associato_doc_amm) {
	this.im_associato_doc_amm = im_associato_doc_amm;
}
/* 
 * Setter dell'attributo im_associato_doc_contabile
 */
public void setIm_associato_doc_contabile(java.math.BigDecimal im_associato_doc_contabile) {
	this.im_associato_doc_contabile = im_associato_doc_contabile;
}
/* 
 * Setter dell'attributo im_scadenza
 */
public void setIm_scadenza(java.math.BigDecimal im_scadenza) {
	this.im_scadenza = im_scadenza;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 16.22.29)
 * @param newPg_ver_rec_scadenza java.lang.Long
 */
public void setPg_ver_rec_scadenza(java.lang.Long newPg_ver_rec_scadenza) {
	pg_ver_rec_scadenza = newPg_ver_rec_scadenza;
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
