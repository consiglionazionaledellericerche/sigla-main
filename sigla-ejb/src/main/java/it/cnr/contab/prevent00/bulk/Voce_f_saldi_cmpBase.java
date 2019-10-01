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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_saldi_cmpBase extends Voce_f_saldi_cmpKey implements Keyed {
	// FL_SOLA_LETTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_sola_lettura;

	// IM_MANDATI_REVERSALI DECIMAL(15,2)
	private java.math.BigDecimal im_mandati_reversali;

	// IM_OBBLIG_IMP_ACR DECIMAL(15,2)
	private java.math.BigDecimal im_obblig_imp_acr;

	// IM_PAGAMENTI_INCASSI DECIMAL(15,2)
	private java.math.BigDecimal im_pagamenti_incassi;

	// IM_STANZ_INIZIALE_A1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_stanz_iniziale_a1;

	// IM_STANZ_INIZIALE_A2 DECIMAL(15,2)
	private java.math.BigDecimal im_stanz_iniziale_a2;

	// IM_STANZ_INIZIALE_A3 DECIMAL(15,2)
	private java.math.BigDecimal im_stanz_iniziale_a3;

	// ORIGINE VARCHAR(5) NOT NULL
	private java.lang.String origine;

	// VARIAZIONI_MENO DECIMAL(15,2)
	private java.math.BigDecimal variazioni_meno;

	// VARIAZIONI_PIU DECIMAL(15,2)
	private java.math.BigDecimal variazioni_piu;

public Voce_f_saldi_cmpBase() {
	super();
}
public Voce_f_saldi_cmpBase(java.lang.String cd_cds,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_competenza_residuo,java.lang.String ti_gestione) {
	super(cd_cds,cd_voce,esercizio,ti_appartenenza,ti_competenza_residuo,ti_gestione);
}
/* 
 * Getter dell'attributo fl_sola_lettura
 */
public java.lang.Boolean getFl_sola_lettura() {
	return fl_sola_lettura;
}
/* 
 * Getter dell'attributo im_mandati_reversali
 */
public java.math.BigDecimal getIm_mandati_reversali() {
	return im_mandati_reversali;
}
/* 
 * Getter dell'attributo im_obblig_imp_acr
 */
public java.math.BigDecimal getIm_obblig_imp_acr() {
	return im_obblig_imp_acr;
}
/* 
 * Getter dell'attributo im_pagamenti_incassi
 */
public java.math.BigDecimal getIm_pagamenti_incassi() {
	return im_pagamenti_incassi;
}
/* 
 * Getter dell'attributo im_stanz_iniziale_a1
 */
public java.math.BigDecimal getIm_stanz_iniziale_a1() {
	return im_stanz_iniziale_a1;
}
/* 
 * Getter dell'attributo im_stanz_iniziale_a2
 */
public java.math.BigDecimal getIm_stanz_iniziale_a2() {
	return im_stanz_iniziale_a2;
}
/* 
 * Getter dell'attributo im_stanz_iniziale_a3
 */
public java.math.BigDecimal getIm_stanz_iniziale_a3() {
	return im_stanz_iniziale_a3;
}
/* 
 * Getter dell'attributo origine
 */
public java.lang.String getOrigine() {
	return origine;
}
/* 
 * Getter dell'attributo variazioni_meno
 */
public java.math.BigDecimal getVariazioni_meno() {
	return variazioni_meno;
}
/* 
 * Getter dell'attributo variazioni_piu
 */
public java.math.BigDecimal getVariazioni_piu() {
	return variazioni_piu;
}
/* 
 * Setter dell'attributo fl_sola_lettura
 */
public void setFl_sola_lettura(java.lang.Boolean fl_sola_lettura) {
	this.fl_sola_lettura = fl_sola_lettura;
}
/* 
 * Setter dell'attributo im_mandati_reversali
 */
public void setIm_mandati_reversali(java.math.BigDecimal im_mandati_reversali) {
	this.im_mandati_reversali = im_mandati_reversali;
}
/* 
 * Setter dell'attributo im_obblig_imp_acr
 */
public void setIm_obblig_imp_acr(java.math.BigDecimal im_obblig_imp_acr) {
	this.im_obblig_imp_acr = im_obblig_imp_acr;
}
/* 
 * Setter dell'attributo im_pagamenti_incassi
 */
public void setIm_pagamenti_incassi(java.math.BigDecimal im_pagamenti_incassi) {
	this.im_pagamenti_incassi = im_pagamenti_incassi;
}
/* 
 * Setter dell'attributo im_stanz_iniziale_a1
 */
public void setIm_stanz_iniziale_a1(java.math.BigDecimal im_stanz_iniziale_a1) {
	this.im_stanz_iniziale_a1 = im_stanz_iniziale_a1;
}
/* 
 * Setter dell'attributo im_stanz_iniziale_a2
 */
public void setIm_stanz_iniziale_a2(java.math.BigDecimal im_stanz_iniziale_a2) {
	this.im_stanz_iniziale_a2 = im_stanz_iniziale_a2;
}
/* 
 * Setter dell'attributo im_stanz_iniziale_a3
 */
public void setIm_stanz_iniziale_a3(java.math.BigDecimal im_stanz_iniziale_a3) {
	this.im_stanz_iniziale_a3 = im_stanz_iniziale_a3;
}
/* 
 * Setter dell'attributo origine
 */
public void setOrigine(java.lang.String origine) {
	this.origine = origine;
}
/* 
 * Setter dell'attributo variazioni_meno
 */
public void setVariazioni_meno(java.math.BigDecimal variazioni_meno) {
	this.variazioni_meno = variazioni_meno;
}
/* 
 * Setter dell'attributo variazioni_piu
 */
public void setVariazioni_piu(java.math.BigDecimal variazioni_piu) {
	this.variazioni_piu = variazioni_piu;
}
}
