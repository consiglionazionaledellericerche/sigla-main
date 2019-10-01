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

public class Pdg_aggregato_etr_detBase extends Pdg_aggregato_etr_detKey implements Keyed {
	// IM_RA_RCE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ra_rce;

	// IM_RB_RSE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rb_rse;

	// IM_RC_ESR DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rc_esr;

	// IM_RD_A2_RICAVI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rd_a2_ricavi;

	// IM_RE_A2_ENTRATE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_re_a2_entrate;

	// IM_RF_A3_RICAVI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rf_a3_ricavi;

	// IM_RG_A3_ENTRATE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rg_a3_entrate;

public Pdg_aggregato_etr_detBase() {
	super();
}
public Pdg_aggregato_etr_detBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_aggregato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_natura,esercizio,ti_aggregato,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo im_ra_rce
 */
public java.math.BigDecimal getIm_ra_rce() {
	return im_ra_rce;
}
/* 
 * Getter dell'attributo im_rb_rse
 */
public java.math.BigDecimal getIm_rb_rse() {
	return im_rb_rse;
}
/* 
 * Getter dell'attributo im_rc_esr
 */
public java.math.BigDecimal getIm_rc_esr() {
	return im_rc_esr;
}
/* 
 * Getter dell'attributo im_rd_a2_ricavi
 */
public java.math.BigDecimal getIm_rd_a2_ricavi() {
	return im_rd_a2_ricavi;
}
/* 
 * Getter dell'attributo im_re_a2_entrate
 */
public java.math.BigDecimal getIm_re_a2_entrate() {
	return im_re_a2_entrate;
}
/* 
 * Getter dell'attributo im_rf_a3_ricavi
 */
public java.math.BigDecimal getIm_rf_a3_ricavi() {
	return im_rf_a3_ricavi;
}
/* 
 * Getter dell'attributo im_rg_a3_entrate
 */
public java.math.BigDecimal getIm_rg_a3_entrate() {
	return im_rg_a3_entrate;
}
/* 
 * Setter dell'attributo im_ra_rce
 */
public void setIm_ra_rce(java.math.BigDecimal im_ra_rce) {
	this.im_ra_rce = im_ra_rce;
}
/* 
 * Setter dell'attributo im_rb_rse
 */
public void setIm_rb_rse(java.math.BigDecimal im_rb_rse) {
	this.im_rb_rse = im_rb_rse;
}
/* 
 * Setter dell'attributo im_rc_esr
 */
public void setIm_rc_esr(java.math.BigDecimal im_rc_esr) {
	this.im_rc_esr = im_rc_esr;
}
/* 
 * Setter dell'attributo im_rd_a2_ricavi
 */
public void setIm_rd_a2_ricavi(java.math.BigDecimal im_rd_a2_ricavi) {
	this.im_rd_a2_ricavi = im_rd_a2_ricavi;
}
/* 
 * Setter dell'attributo im_re_a2_entrate
 */
public void setIm_re_a2_entrate(java.math.BigDecimal im_re_a2_entrate) {
	this.im_re_a2_entrate = im_re_a2_entrate;
}
/* 
 * Setter dell'attributo im_rf_a3_ricavi
 */
public void setIm_rf_a3_ricavi(java.math.BigDecimal im_rf_a3_ricavi) {
	this.im_rf_a3_ricavi = im_rf_a3_ricavi;
}
/* 
 * Setter dell'attributo im_rg_a3_entrate
 */
public void setIm_rg_a3_entrate(java.math.BigDecimal im_rg_a3_entrate) {
	this.im_rg_a3_entrate = im_rg_a3_entrate;
}
}
