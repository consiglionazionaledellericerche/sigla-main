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

public class V_disp_cassa_cdsBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(22,0)
	private Integer esercizio;

	// IM_ACCREDITAMENTI DECIMAL(22,0)
	private java.math.BigDecimal im_accreditamenti;

	// IM_ACCREDITAMENTI_IC DECIMAL(22,0)
	private java.math.BigDecimal im_accreditamenti_ic;

	// IM_CASSA_INIZIALE DECIMAL(22,0)
	private java.math.BigDecimal im_cassa_iniziale;

	// IM_MANDATI DECIMAL(22,0)
	private java.math.BigDecimal im_mandati;

	// IM_REVERSALI DECIMAL(22,0)
	private java.math.BigDecimal im_reversali;
	
	// IM_MANDATI DECIMAL(22,0)
	private java.math.BigDecimal im_disponibilita_cassa;
public V_disp_cassa_cdsBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo im_accreditamenti
 */
public java.math.BigDecimal getIm_accreditamenti() {
	return im_accreditamenti;
}
/* 
 * Getter dell'attributo im_accreditamenti_ic
 */
public java.math.BigDecimal getIm_accreditamenti_ic() {
	return im_accreditamenti_ic;
}
/* 
 * Getter dell'attributo im_cassa_iniziale
 */
public java.math.BigDecimal getIm_cassa_iniziale() {
	return im_cassa_iniziale;
}
/* 
 * Getter dell'attributo im_mandati
 */
public java.math.BigDecimal getIm_mandati() {
	return im_mandati;
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2003 16.08.57)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_reversali() {
	return im_reversali;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo im_accreditamenti
 */
public void setIm_accreditamenti(java.math.BigDecimal im_accreditamenti) {
	this.im_accreditamenti = im_accreditamenti;
}
/* 
 * Setter dell'attributo im_accreditamenti_ic
 */
public void setIm_accreditamenti_ic(java.math.BigDecimal im_accreditamenti_ic) {
	this.im_accreditamenti_ic = im_accreditamenti_ic;
}
/* 
 * Setter dell'attributo im_cassa_iniziale
 */
public void setIm_cassa_iniziale(java.math.BigDecimal im_cassa_iniziale) {
	this.im_cassa_iniziale = im_cassa_iniziale;
}
/* 
 * Setter dell'attributo im_mandati
 */
public void setIm_mandati(java.math.BigDecimal im_mandati) {
	this.im_mandati = im_mandati;
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2003 16.08.57)
 * @param newIm_reversali java.math.BigDecimal
 */
public void setIm_reversali(java.math.BigDecimal newIm_reversali) {
	im_reversali = newIm_reversali;
}
public java.math.BigDecimal getIm_disponibilita_cassa() {
	return im_disponibilita_cassa;
}
public void setIm_disponibilita_cassa(
		java.math.BigDecimal im_disponibilita_cassa) {
	this.im_disponibilita_cassa = im_disponibilita_cassa;
}
}
