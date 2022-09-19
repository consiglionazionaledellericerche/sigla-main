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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.persistency.Keyed;

public class Tipo_sezionaleBase extends Tipo_sezionaleKey implements Keyed {
	// CD_ATTIVITA_COMMERCIALE VARCHAR(10)
	private java.lang.String cd_attivita_commerciale;

	// DS_TIPO_SEZIONALE VARCHAR(50) NOT NULL
	private java.lang.String ds_tipo_sezionale;

	// FL_AUTOFATTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_autofattura;

	// FL_EXTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_extra_ue;

	// FL_INTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_intra_ue;

	// FL_ORDINARIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_ordinario;

	// FL_SAN_MARINO_CON_IVA CHAR(1) NOT NULL
	private java.lang.Boolean fl_san_marino_con_iva;

	// FL_SAN_MARINO_SENZA_IVA CHAR(1) NOT NULL
	private java.lang.Boolean fl_san_marino_senza_iva;

	// SEZIONALE_VEN_LIQUIDAZIONE VARCHAR(10)
	private java.lang.String sezionale_ven_liquidazione;

	// TI_ACQUISTI_VENDITE CHAR(1) NOT NULL
	private java.lang.String ti_acquisti_vendite;

	// TI_BENE_SERVIZIO CHAR(1) NOT NULL
	private java.lang.String ti_bene_servizio;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;
	
	private java.lang.Boolean fl_servizi_non_residenti;

	private Integer ordina;

	// FL_SPLIT_PAYMENT CHAR(1) NOT NULL
	private java.lang.Boolean fl_split_payment;
	
	private java.lang.Boolean fl_reg_tardiva;

	private java.lang.String cd_contributo_ritenuta_iva;

	public Tipo_sezionaleBase() {
	super();
}
public Tipo_sezionaleBase(java.lang.String cd_tipo_sezionale) {
	super(cd_tipo_sezionale);
}
/* 
 * Getter dell'attributo cd_attivita_commerciale
 */
public java.lang.String getCd_attivita_commerciale() {
	return cd_attivita_commerciale;
}
/* 
 * Getter dell'attributo ds_tipo_sezionale
 */
public java.lang.String getDs_tipo_sezionale() {
	return ds_tipo_sezionale;
}
/* 
 * Getter dell'attributo fl_autofattura
 */
public java.lang.Boolean getFl_autofattura() {
	return fl_autofattura;
}
/* 
 * Getter dell'attributo fl_extra_ue
 */
public java.lang.Boolean getFl_extra_ue() {
	return fl_extra_ue;
}
/* 
 * Getter dell'attributo fl_intra_ue
 */
public java.lang.Boolean getFl_intra_ue() {
	return fl_intra_ue;
}
/* 
 * Getter dell'attributo fl_ordinario
 */
public java.lang.Boolean getFl_ordinario() {
	return fl_ordinario;
}
/* 
 * Getter dell'attributo fl_san_marino_con_iva
 */
public java.lang.Boolean getFl_san_marino_con_iva() {
	return fl_san_marino_con_iva;
}
/* 
 * Getter dell'attributo fl_san_marino_senza_iva
 */
public java.lang.Boolean getFl_san_marino_senza_iva() {
	return fl_san_marino_senza_iva;
}
/* 
 * Getter dell'attributo sezionale_ven_liquidazione
 */
public java.lang.String getSezionale_ven_liquidazione() {
	return sezionale_ven_liquidazione;
}
/* 
 * Getter dell'attributo ti_acquisti_vendite
 */
public java.lang.String getTi_acquisti_vendite() {
	return ti_acquisti_vendite;
}
/* 
 * Getter dell'attributo ti_bene_servizio
 */
public java.lang.String getTi_bene_servizio() {
	return ti_bene_servizio;
}
/* 
 * Getter dell'attributo ti_istituz_commerc
 */
public java.lang.String getTi_istituz_commerc() {
	return ti_istituz_commerc;
}
/* 
 * Setter dell'attributo cd_attivita_commerciale
 */
public void setCd_attivita_commerciale(java.lang.String cd_attivita_commerciale) {
	this.cd_attivita_commerciale = cd_attivita_commerciale;
}
/* 
 * Setter dell'attributo ds_tipo_sezionale
 */
public void setDs_tipo_sezionale(java.lang.String ds_tipo_sezionale) {
	this.ds_tipo_sezionale = ds_tipo_sezionale;
}
/* 
 * Setter dell'attributo fl_autofattura
 */
public void setFl_autofattura(java.lang.Boolean fl_autofattura) {
	this.fl_autofattura = fl_autofattura;
}
/* 
 * Setter dell'attributo fl_extra_ue
 */
public void setFl_extra_ue(java.lang.Boolean fl_extra_ue) {
	this.fl_extra_ue = fl_extra_ue;
}
/* 
 * Setter dell'attributo fl_intra_ue
 */
public void setFl_intra_ue(java.lang.Boolean fl_intra_ue) {
	this.fl_intra_ue = fl_intra_ue;
}
/* 
 * Setter dell'attributo fl_ordinario
 */
public void setFl_ordinario(java.lang.Boolean fl_ordinario) {
	this.fl_ordinario = fl_ordinario;
}
/* 
 * Setter dell'attributo fl_san_marino_con_iva
 */
public void setFl_san_marino_con_iva(java.lang.Boolean fl_san_marino_con_iva) {
	this.fl_san_marino_con_iva = fl_san_marino_con_iva;
}
/* 
 * Setter dell'attributo fl_san_marino_senza_iva
 */
public void setFl_san_marino_senza_iva(java.lang.Boolean fl_san_marino_senza_iva) {
	this.fl_san_marino_senza_iva = fl_san_marino_senza_iva;
}
/* 
 * Setter dell'attributo sezionale_ven_liquidazione
 */
public void setSezionale_ven_liquidazione(java.lang.String sezionale_ven_liquidazione) {
	this.sezionale_ven_liquidazione = sezionale_ven_liquidazione;
}
/* 
 * Setter dell'attributo ti_acquisti_vendite
 */
public void setTi_acquisti_vendite(java.lang.String ti_acquisti_vendite) {
	this.ti_acquisti_vendite = ti_acquisti_vendite;
}
/* 
 * Setter dell'attributo ti_bene_servizio
 */
public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
	this.ti_bene_servizio = ti_bene_servizio;
}
/* 
 * Setter dell'attributo ti_istituz_commerc
 */
public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
	this.ti_istituz_commerc = ti_istituz_commerc;
}
public java.lang.Boolean getFl_servizi_non_residenti() {
	return fl_servizi_non_residenti;
}
public void setFl_servizi_non_residenti(
		java.lang.Boolean fl_servizi_non_residenti) {
	this.fl_servizi_non_residenti = fl_servizi_non_residenti;
}
public Integer getOrdina() {
	return ordina;
}
public java.lang.Boolean getFl_reg_tardiva() {
	return fl_reg_tardiva;
}
public void setFl_reg_tardiva(java.lang.Boolean fl_reg_tardiva) {
	this.fl_reg_tardiva = fl_reg_tardiva;
}
public void setOrdina(Integer ordina) {
	this.ordina = ordina;
}
public java.lang.Boolean getFl_split_payment() {
	return fl_split_payment;
}
public void setFl_split_payment(java.lang.Boolean fl_split_payment) {
	this.fl_split_payment = fl_split_payment;
}

	public String getCd_contributo_ritenuta_iva() {
		return cd_contributo_ritenuta_iva;
	}

	public void setCd_contributo_ritenuta_iva(String cd_contributo_ritenuta_iva) {
		this.cd_contributo_ritenuta_iva = cd_contributo_ritenuta_iva;
	}
}
