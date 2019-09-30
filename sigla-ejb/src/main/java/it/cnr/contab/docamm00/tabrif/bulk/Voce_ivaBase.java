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

public class Voce_ivaBase extends Voce_ivaKey implements Keyed {
	private java.lang.Boolean fl_solo_italia;

	private java.lang.String ti_bene_servizio;
		
	private java.lang.String ti_bollo;

	// CD_GRUPPO_IVA VARCHAR(10) NOT NULL
	private java.lang.String cd_gruppo_iva;
	
	private java.lang.Boolean fl_obb_dichiarazione_intento;

/* 
 * Getter dell'attributo cd_gruppo_iva
 */
public java.lang.String getCd_gruppo_iva() {
	return cd_gruppo_iva;
}

/* 
 * Setter dell'attributo cd_gruppo_iva
 */
public void setCd_gruppo_iva(java.lang.String cd_gruppo_iva) {
	this.cd_gruppo_iva = cd_gruppo_iva;
}
	// DS_VOCE_IVA VARCHAR(300) NOT NULL
	private java.lang.String ds_voce_iva;

/* 
 * Getter dell'attributo ds_voce_iva
 */
public java.lang.String getDs_voce_iva() {
	return ds_voce_iva;
}

/* 
 * Setter dell'attributo ds_voce_iva
 */
public void setDs_voce_iva(java.lang.String ds_voce_iva) {
	this.ds_voce_iva = ds_voce_iva;
}
	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}

/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_inizio_validita;

/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}

/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
	// FL_AUTOFATTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_autofattura;

/* 
 * Getter dell'attributo fl_autofattura
 */
public java.lang.Boolean getFl_autofattura() {
	return fl_autofattura;
}

/* 
 * Setter dell'attributo fl_autofattura
 */
public void setFl_autofattura(java.lang.Boolean fl_autofattura) {
	this.fl_autofattura = fl_autofattura;
}
	// FL_BOLLA_DOGANALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_bolla_doganale;

/* 
 * Getter dell'attributo fl_bolla_doganale
 */
public java.lang.Boolean getFl_bolla_doganale() {
	return fl_bolla_doganale;
}

/* 
 * Setter dell'attributo fl_bolla_doganale
 */
public void setFl_bolla_doganale(java.lang.Boolean fl_bolla_doganale) {
	this.fl_bolla_doganale = fl_bolla_doganale;
}
	// FL_DEFAULT_ISTITUZIONALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_default_istituzionale;

/* 
 * Getter dell'attributo fl_default_istituzionale
 */
public java.lang.Boolean getFl_default_istituzionale() {
	return fl_default_istituzionale;
}

/* 
 * Setter dell'attributo fl_default_istituzionale
 */
public void setFl_default_istituzionale(java.lang.Boolean fl_default_istituzionale) {
	this.fl_default_istituzionale = fl_default_istituzionale;
}
	// FL_DETRAIBILE CHAR(1) NOT NULL
	private java.lang.Boolean fl_detraibile;

/* 
 * Getter dell'attributo fl_detraibile
 */
public java.lang.Boolean getFl_detraibile() {
	return fl_detraibile;
}

/* 
 * Setter dell'attributo fl_detraibile
 */
public void setFl_detraibile(java.lang.Boolean fl_detraibile) {
	this.fl_detraibile = fl_detraibile;
}
	// FL_ESCLUSO CHAR(1) NOT NULL
	private java.lang.Boolean fl_escluso;

/* 
 * Getter dell'attributo fl_escluso
 */
public java.lang.Boolean getFl_escluso() {
	return fl_escluso;
}

/* 
 * Setter dell'attributo fl_escluso
 */
public void setFl_escluso(java.lang.Boolean fl_escluso) {
	this.fl_escluso = fl_escluso;
}
	// FL_ESENTE CHAR(1) NOT NULL
	private java.lang.Boolean fl_esente;

/* 
 * Getter dell'attributo fl_esente
 */
public java.lang.Boolean getFl_esente() {
	return fl_esente;
}

/* 
 * Setter dell'attributo fl_esente
 */
public void setFl_esente(java.lang.Boolean fl_esente) {
	this.fl_esente = fl_esente;
}
	// FL_INTRA CHAR(1) NOT NULL
	private java.lang.Boolean fl_intra;

/* 
 * Getter dell'attributo fl_intra
 */
public java.lang.Boolean getFl_intra() {
	return fl_intra;
}

/* 
 * Setter dell'attributo fl_intra
 */
public void setFl_intra(java.lang.Boolean fl_intra) {
	this.fl_intra = fl_intra;
}
	// FL_NON_IMPONIBILE CHAR(1) NOT NULL
	private java.lang.Boolean fl_non_imponibile;

/* 
 * Getter dell'attributo fl_non_imponibile
 */
public java.lang.Boolean getFl_non_imponibile() {
	return fl_non_imponibile;
}

/* 
 * Setter dell'attributo fl_non_imponibile
 */
public void setFl_non_imponibile(java.lang.Boolean fl_non_imponibile) {
	this.fl_non_imponibile = fl_non_imponibile;
}
	// FL_NON_SOGGETTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_non_soggetto;

/* 
 * Getter dell'attributo fl_non_soggetto
 */
public java.lang.Boolean getFl_non_soggetto() {
	return fl_non_soggetto;
}

/* 
 * Setter dell'attributo fl_non_soggetto
 */
public void setFl_non_soggetto(java.lang.Boolean fl_non_soggetto) {
	this.fl_non_soggetto = fl_non_soggetto;
}
	// PERCENTUALE DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale;

/* 
 * Getter dell'attributo percentuale
 */
public java.math.BigDecimal getPercentuale() {
	return percentuale;
}

/* 
 * Setter dell'attributo percentuale
 */
public void setPercentuale(java.math.BigDecimal percentuale) {
	this.percentuale = percentuale;
}
	// PERCENTUALE_DETRAIBILITA DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale_detraibilita;

/* 
 * Getter dell'attributo percentuale_detraibilita
 */
public java.math.BigDecimal getPercentuale_detraibilita() {
	return percentuale_detraibilita;
}

/* 
 * Setter dell'attributo percentuale_detraibilita
 */
public void setPercentuale_detraibilita(java.math.BigDecimal percentuale_detraibilita) {
	this.percentuale_detraibilita = percentuale_detraibilita;
}
	// TI_APPLICAZIONE CHAR(1) NOT NULL
	private java.lang.String ti_applicazione;

	// NATURA_OPER_NON_IMP_SDI CHAR(2) NOT NULL
	private java.lang.String naturaOperNonImpSdi;

	// RIF_NORM_OPER_NON_IMP_SDI CHAR(30) NOT NULL
	private java.lang.String rifNormOperNonImpSdi;



/* 
 * Getter dell'attributo ti_applicazione
 */
public java.lang.String getTi_applicazione() {
	return ti_applicazione;
}

/* 
 * Setter dell'attributo ti_applicazione
 */
public void setTi_applicazione(java.lang.String ti_applicazione) {
	this.ti_applicazione = ti_applicazione;
}
//FL_IVA_NON_RECUPERABILE CHAR(1) NOT NULL
private java.lang.Boolean fl_iva_non_recuperabile; 

public Voce_ivaBase() {
	super();
}

public Voce_ivaBase(java.lang.String cd_voce_iva) {
	super(cd_voce_iva);
}

public java.lang.Boolean getFl_iva_non_recuperabile() {
	return fl_iva_non_recuperabile;
}

public void setFl_iva_non_recuperabile(java.lang.Boolean fl_iva_non_recuperabile) {
	this.fl_iva_non_recuperabile = fl_iva_non_recuperabile;
}

public java.lang.String getNaturaOperNonImpSdi() {
	return naturaOperNonImpSdi;
}

public void setNaturaOperNonImpSdi(java.lang.String naturaOperNonImpSdi) {
	this.naturaOperNonImpSdi = naturaOperNonImpSdi;
}

public java.lang.String getRifNormOperNonImpSdi() {
	return rifNormOperNonImpSdi;
}

public void setRifNormOperNonImpSdi(java.lang.String rifNormOperNonImpSdi) {
	this.rifNormOperNonImpSdi = rifNormOperNonImpSdi;
}

public java.lang.Boolean getFl_solo_italia() {
	return fl_solo_italia;
}

public void setFl_solo_italia(java.lang.Boolean fl_solo_italia) {
	this.fl_solo_italia = fl_solo_italia;
}

public java.lang.String getTi_bene_servizio() {
	return ti_bene_servizio;
}

public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
	this.ti_bene_servizio = ti_bene_servizio;
}

public java.lang.Boolean getFl_obb_dichiarazione_intento() {
	return fl_obb_dichiarazione_intento;
}

public void setFl_obb_dichiarazione_intento(
		java.lang.Boolean fl_obb_dichiarazione_intento) {
	this.fl_obb_dichiarazione_intento = fl_obb_dichiarazione_intento;
}

public java.lang.String getTi_bollo() {
	return ti_bollo;
}

public void setTi_bollo(java.lang.String ti_bollo) {
	this.ti_bollo = ti_bollo;
}

}
