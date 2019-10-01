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

public class V_sit_bil_cds_cnrBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_VOCE VARCHAR(50)
	private java.lang.String cd_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// IM_ACCERTATO_IMPEGNATO DECIMAL(22,0)
	private java.math.BigDecimal im_accertato_impegnato;

	// IM_ACCREDITATO DECIMAL(22,0)
	private java.math.BigDecimal im_accreditato;

	// IM_ASSESTATO DECIMAL(22,0)
	private java.math.BigDecimal im_assestato;

	// IM_PAGATO_INCASSATO_COMPETENZA DECIMAL(22,0)
	private java.math.BigDecimal im_pagato_incassato_competenza;

	// IM_PAGATO_INCASSATO_RESIDUO DECIMAL(22,0)
	private java.math.BigDecimal im_pagato_incassato_residuo;

	// IM_RESIDUO_INIZIALE DECIMAL(22,0)
	private java.math.BigDecimal im_residuo_iniziale;

	// IM_STANZ_INIZIALE DECIMAL(22,0)
	private java.math.BigDecimal im_stanz_iniziale;

	// IM_VARIAZIONI_MENO DECIMAL(22,0)
	private java.math.BigDecimal im_variazioni_meno;

	// IM_VARIAZIONI_PIU DECIMAL(22,0)
	private java.math.BigDecimal im_variazioni_piu;

	// TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;

public V_sit_bil_cds_cnrBase() {
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
 * Getter dell'attributo im_accertato_impegnato
 */
public java.math.BigDecimal getIm_accertato_impegnato() {
	return im_accertato_impegnato;
}
/* 
 * Getter dell'attributo im_accreditato
 */
public java.math.BigDecimal getIm_accreditato() {
	return im_accreditato;
}
/* 
 * Getter dell'attributo im_assestato
 */
public java.math.BigDecimal getIm_assestato() {
	return im_assestato;
}
/* 
 * Getter dell'attributo im_pagato_incassato_competenza
 */
public java.math.BigDecimal getIm_pagato_incassato_competenza() {
	return im_pagato_incassato_competenza;
}
/* 
 * Getter dell'attributo im_pagato_incassato_residuo
 */
public java.math.BigDecimal getIm_pagato_incassato_residuo() {
	return im_pagato_incassato_residuo;
}
/* 
 * Getter dell'attributo im_residuo_iniziale
 */
public java.math.BigDecimal getIm_residuo_iniziale() {
	return im_residuo_iniziale;
}
/* 
 * Getter dell'attributo im_stanz_iniziale
 */
public java.math.BigDecimal getIm_stanz_iniziale() {
	return im_stanz_iniziale;
}
/* 
 * Getter dell'attributo im_variazioni_meno
 */
public java.math.BigDecimal getIm_variazioni_meno() {
	return im_variazioni_meno;
}
/* 
 * Getter dell'attributo im_variazioni_piu
 */
public java.math.BigDecimal getIm_variazioni_piu() {
	return im_variazioni_piu;
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
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo im_accertato_impegnato
 */
public void setIm_accertato_impegnato(java.math.BigDecimal im_accertato_impegnato) {
	this.im_accertato_impegnato = im_accertato_impegnato;
}
/* 
 * Setter dell'attributo im_accreditato
 */
public void setIm_accreditato(java.math.BigDecimal im_accreditato) {
	this.im_accreditato = im_accreditato;
}
/* 
 * Setter dell'attributo im_assestato
 */
public void setIm_assestato(java.math.BigDecimal im_assestato) {
	this.im_assestato = im_assestato;
}
/* 
 * Setter dell'attributo im_pagato_incassato_competenza
 */
public void setIm_pagato_incassato_competenza(java.math.BigDecimal im_pagato_incassato_competenza) {
	this.im_pagato_incassato_competenza = im_pagato_incassato_competenza;
}
/* 
 * Setter dell'attributo im_pagato_incassato_residuo
 */
public void setIm_pagato_incassato_residuo(java.math.BigDecimal im_pagato_incassato_residuo) {
	this.im_pagato_incassato_residuo = im_pagato_incassato_residuo;
}
/* 
 * Setter dell'attributo im_residuo_iniziale
 */
public void setIm_residuo_iniziale(java.math.BigDecimal im_residuo_iniziale) {
	this.im_residuo_iniziale = im_residuo_iniziale;
}
/* 
 * Setter dell'attributo im_stanz_iniziale
 */
public void setIm_stanz_iniziale(java.math.BigDecimal im_stanz_iniziale) {
	this.im_stanz_iniziale = im_stanz_iniziale;
}
/* 
 * Setter dell'attributo im_variazioni_meno
 */
public void setIm_variazioni_meno(java.math.BigDecimal im_variazioni_meno) {
	this.im_variazioni_meno = im_variazioni_meno;
}
/* 
 * Setter dell'attributo im_variazioni_piu
 */
public void setIm_variazioni_piu(java.math.BigDecimal im_variazioni_piu) {
	this.im_variazioni_piu = im_variazioni_piu;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
}
