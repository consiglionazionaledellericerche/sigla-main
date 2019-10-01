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

public class V_mod_saldi_obbligBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_VOCE VARCHAR(50)
	private java.lang.String cd_voce;

	// ESERCIZIO DECIMAL(22,0)
	private java.lang.Integer esercizio;

	// IM_DELTA_MAN_VOCE DECIMAL(22,0)
	private java.math.BigDecimal im_delta_man_voce;

	// IM_DELTA_PAG_VOCE DECIMAL(22,0)
	private java.math.BigDecimal im_delta_pag_voce;

	// IM_DELTA_VOCE DECIMAL(22,0)
	private java.math.BigDecimal im_delta_voce;

	// ESERCIZIO_ORIGINALE DECIMAL(10,0)
  	private java.lang.Integer esercizio_originale;

	// PG_OBBLIGAZIONE DECIMAL(22,0)
	private java.lang.Long pg_obbligazione;

	// PG_OLD DECIMAL(22,0)
	private java.lang.Long pg_old;

	// TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;

public V_mod_saldi_obbligBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
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
 * Getter dell'attributo im_delta_man_voce
 */
public java.math.BigDecimal getIm_delta_man_voce() {
	return im_delta_man_voce;
}
/* 
 * Getter dell'attributo im_delta_pag_voce
 */
public java.math.BigDecimal getIm_delta_pag_voce() {
	return im_delta_pag_voce;
}
/* 
 * Getter dell'attributo im_delta_voce
 */
public java.math.BigDecimal getIm_delta_voce() {
	return im_delta_voce;
}
/* 
 * Getter dell'attributo esercizio_originale
 */
public java.lang.Integer getEsercizio_originale() {
	return esercizio_originale;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_old
 */
public java.lang.Long getPg_old() {
	return pg_old;
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
 * Setter dell'attributo cd_voce
 */
public void setCd_voce(java.lang.String cd_voce) {
	this.cd_voce = cd_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/* 
 * Setter dell'attributo im_delta_man_voce
 */
public void setIm_delta_man_voce(java.math.BigDecimal im_delta_man_voce) {
	this.im_delta_man_voce = im_delta_man_voce;
}
/* 
 * Setter dell'attributo im_delta_pag_voce
 */
public void setIm_delta_pag_voce(java.math.BigDecimal im_delta_pag_voce) {
	this.im_delta_pag_voce = im_delta_pag_voce;
}
/* 
 * Setter dell'attributo im_delta_voce
 */
public void setIm_delta_voce(java.math.BigDecimal im_delta_voce) {
	this.im_delta_voce = im_delta_voce;
}
/* 
 * Setter dell'attributo esercizio_originale
 */
public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
	this.esercizio_originale=esercizio_originale;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long newPg_obbligazione) {
	pg_obbligazione = newPg_obbligazione;
}
/* 
 * Setter dell'attributo pg_old
 */
public void setPg_old(java.lang.Long newPg_old) {
	pg_old = newPg_old;
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
