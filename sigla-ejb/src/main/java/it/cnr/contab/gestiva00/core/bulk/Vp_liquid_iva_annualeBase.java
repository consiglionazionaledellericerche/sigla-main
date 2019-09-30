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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vp_liquid_iva_annualeBase extends Vp_liquid_iva_annualeKey  implements Keyed {
	// CD_VOCE_IVA VARCHAR(200)
	private java.lang.String cd_voce_iva;

	// CHIAVE VARCHAR(100) NOT NULL
	private java.lang.String chiave;

	// DS_MESE VARCHAR(200)
	private java.lang.String ds_mese;

	// DS_VOCE_IVA VARCHAR(200)
	private java.lang.String ds_voce_iva;

	// IMPONIBILE DECIMAL(20,3)
	private java.math.BigDecimal imponibile;

	// IM_IVA DECIMAL(20,3)
	private java.math.BigDecimal im_iva;

	// IM_TOTALE DECIMAL(20,3)
	private java.math.BigDecimal im_totale;
public Vp_liquid_iva_annualeBase() {
		
	super();
}
public Vp_liquid_iva_annualeBase(
	Integer esercizio,
	Long id,
	Long sequenza,
	String tipo) {
		
	super(esercizio, id, sequenza, tipo);
}
/* 
 * Getter dell'attributo cd_voce_iva
 */
public java.lang.String getCd_voce_iva() {
	return cd_voce_iva;
}
/* 
 * Getter dell'attributo chiave
 */
public java.lang.String getChiave() {
	return chiave;
}
/* 
 * Getter dell'attributo ds_mese
 */
public java.lang.String getDs_mese() {
	return ds_mese;
}
/* 
 * Getter dell'attributo ds_voce_iva
 */
public java.lang.String getDs_voce_iva() {
	return ds_voce_iva;
}
/* 
 * Getter dell'attributo im_iva
 */
public java.math.BigDecimal getIm_iva() {
	return im_iva;
}
/* 
 * Getter dell'attributo im_totale
 */
public java.math.BigDecimal getIm_totale() {
	return im_totale;
}
/* 
 * Getter dell'attributo imponibile
 */
public java.math.BigDecimal getImponibile() {
	return imponibile;
}
/* 
 * Setter dell'attributo cd_voce_iva
 */
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.cd_voce_iva = cd_voce_iva;
}
/* 
 * Setter dell'attributo chiave
 */
public void setChiave(java.lang.String chiave) {
	this.chiave = chiave;
}
/* 
 * Setter dell'attributo ds_mese
 */
public void setDs_mese(java.lang.String ds_mese) {
	this.ds_mese = ds_mese;
}
/* 
 * Setter dell'attributo ds_voce_iva
 */
public void setDs_voce_iva(java.lang.String ds_voce_iva) {
	this.ds_voce_iva = ds_voce_iva;
}
/* 
 * Setter dell'attributo im_iva
 */
public void setIm_iva(java.math.BigDecimal im_iva) {
	this.im_iva = im_iva;
}
/* 
 * Setter dell'attributo im_totale
 */
public void setIm_totale(java.math.BigDecimal im_totale) {
	this.im_totale = im_totale;
}
/* 
 * Setter dell'attributo imponibile
 */
public void setImponibile(java.math.BigDecimal imponibile) {
	this.imponibile = imponibile;
}
}
