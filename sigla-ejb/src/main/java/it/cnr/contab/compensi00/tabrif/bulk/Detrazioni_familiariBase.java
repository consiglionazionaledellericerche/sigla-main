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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Detrazioni_familiariBase extends Detrazioni_familiariKey implements Keyed {
	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

	// IM_DETRAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_detrazione;

	// IM_DETRAZIONE_PRIMO_FIGLIO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_detrazione_primo_figlio;

	// IM_SUPERIORE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_superiore;

	private java.math.BigDecimal im_maggiorazione;
	
	private java.math.BigDecimal moltiplicatore;

	private java.math.BigDecimal numeratore;

	private java.math.BigDecimal denominatore;
public Detrazioni_familiariBase() {
	super();
}
public Detrazioni_familiariBase(java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.Integer numero,java.lang.String ti_persona) {
	super(dt_inizio_validita,im_inferiore,numero,ti_persona);
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo im_detrazione
 */
public java.math.BigDecimal getIm_detrazione() {
	return im_detrazione;
}
/* 
 * Getter dell'attributo im_detrazione_primo_figlio
 */
public java.math.BigDecimal getIm_detrazione_primo_figlio() {
	return im_detrazione_primo_figlio;
}
/* 
 * Getter dell'attributo im_superiore
 */
public java.math.BigDecimal getIm_superiore() {
	return im_superiore;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo im_detrazione
 */
public void setIm_detrazione(java.math.BigDecimal im_detrazione) {
	this.im_detrazione = im_detrazione;
}
/* 
 * Setter dell'attributo im_detrazione_primo_figlio
 */
public void setIm_detrazione_primo_figlio(java.math.BigDecimal im_detrazione_primo_figlio) {
	this.im_detrazione_primo_figlio = im_detrazione_primo_figlio;
}
/* 
 * Setter dell'attributo im_superiore
 */
public void setIm_superiore(java.math.BigDecimal im_superiore) {
	this.im_superiore = im_superiore;
}
public java.math.BigDecimal getDenominatore() {
	return denominatore;
}
public void setDenominatore(java.math.BigDecimal denominatore) {
	this.denominatore = denominatore;
}
public java.math.BigDecimal getIm_maggiorazione() {
	return im_maggiorazione;
}
public void setIm_maggiorazione(java.math.BigDecimal im_maggiorazione) {
	this.im_maggiorazione = im_maggiorazione;
}
public java.math.BigDecimal getMoltiplicatore() {
	return moltiplicatore;
}
public void setMoltiplicatore(java.math.BigDecimal moltiplicatore) {
	this.moltiplicatore = moltiplicatore;
}
public java.math.BigDecimal getNumeratore() {
	return numeratore;
}
public void setNumeratore(java.math.BigDecimal numeratore) {
	this.numeratore = numeratore;
}
}
