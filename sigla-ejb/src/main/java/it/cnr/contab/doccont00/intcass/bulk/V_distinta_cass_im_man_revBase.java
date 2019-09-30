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

public class V_distinta_cass_im_man_revBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// DT_EMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_emissione;

	// DT_INVIO TIMESTAMP
	private java.sql.Timestamp dt_invio;

	// ESERCIZIO DECIMAL(4,0)
	private Integer esercizio;

	// IM_MANDATI DECIMAL(22,0)
	private java.math.BigDecimal im_mandati;

	// IM_MANDATI_ANNULLATI DECIMAL(22,0)
	private java.math.BigDecimal im_mandati_annullati;

	// IM_REVERSALI DECIMAL(22,0)
	private java.math.BigDecimal im_reversali;

	// IM_REVERSALI_ANNULLATE DECIMAL(22,0)
	private java.math.BigDecimal im_reversali_annullate;

	// PG_DISTINTA DECIMAL(10,0)
	private java.lang.Long pg_distinta;
	
	// PG_DISTINTA_DEF DECIMAL(10,0)
	private java.lang.Long pg_distinta_def;
public V_distinta_cass_im_man_revBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo dt_emissione
 */
public java.sql.Timestamp getDt_emissione() {
	return dt_emissione;
}
/* 
 * Getter dell'attributo dt_invio
 */
public java.sql.Timestamp getDt_invio() {
	return dt_invio;
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo im_mandati
 */
public java.math.BigDecimal getIm_mandati() {
	return im_mandati;
}
/* 
 * Getter dell'attributo im_mandati_annullati
 */
public java.math.BigDecimal getIm_mandati_annullati() {
	return im_mandati_annullati;
}
/* 
 * Getter dell'attributo im_reversali
 */
public java.math.BigDecimal getIm_reversali() {
	return im_reversali;
}
/* 
 * Getter dell'attributo im_reversali_annullate
 */
public java.math.BigDecimal getIm_reversali_annullate() {
	return im_reversali_annullate;
}
public java.lang.Long getPg_distinta() {
	return pg_distinta;
}
/**
 * @return java.lang.Long
 */
public java.lang.Long getPg_distinta_def() {
	return pg_distinta_def;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo dt_emissione
 */
public void setDt_emissione(java.sql.Timestamp dt_emissione) {
	this.dt_emissione = dt_emissione;
}
/* 
 * Setter dell'attributo dt_invio
 */
public void setDt_invio(java.sql.Timestamp dt_invio) {
	this.dt_invio = dt_invio;
}
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/* 
 * Setter dell'attributo im_mandati
 */
public void setIm_mandati(java.math.BigDecimal im_mandati) {
	this.im_mandati = im_mandati;
}
/* 
 * Setter dell'attributo im_mandati_annullati
 */
public void setIm_mandati_annullati(java.math.BigDecimal im_mandati_annullati) {
	this.im_mandati_annullati = im_mandati_annullati;
}
/* 
 * Setter dell'attributo im_reversali
 */
public void setIm_reversali(java.math.BigDecimal im_reversali) {
	this.im_reversali = im_reversali;
}
/* 
 * Setter dell'attributo im_reversali_annullate
 */
public void setIm_reversali_annullate(java.math.BigDecimal im_reversali_annullate) {
	this.im_reversali_annullate = im_reversali_annullate;
}
public void setPg_distinta(java.lang.Long newPg_distinta) {
	pg_distinta = newPg_distinta;
}
/**
 * @param newPg_distinta_def java.lang.Long
 */
public void setPg_distinta_def(java.lang.Long newPg_distinta_def) {
	pg_distinta_def = newPg_distinta_def;
}
}
