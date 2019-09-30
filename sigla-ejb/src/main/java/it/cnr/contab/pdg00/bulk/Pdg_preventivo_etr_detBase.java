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

package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_preventivo_etr_detBase extends Pdg_preventivo_etr_detKey implements Keyed {
	// CATEGORIA_DETTAGLIO VARCHAR(3) NOT NULL
	private java.lang.String categoria_dettaglio;

	// CD_CENTRO_RESPONSABILITA_CLGS VARCHAR(30)
	private java.lang.String cd_centro_responsabilita_clgs;

	// CD_ELEMENTO_VOCE_CLGS VARCHAR(20)
	private java.lang.String cd_elemento_voce_clgs;

	// CD_FUNZIONE VARCHAR(2)
	private java.lang.String cd_funzione;

	// CD_LINEA_ATTIVITA_CLGS VARCHAR(10)
	private java.lang.String cd_linea_attivita_clgs;

	// CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;

	// DESCRIZIONE VARCHAR(300) NOT NULL
	private java.lang.String descrizione;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// FL_SOLA_LETTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_sola_lettura;

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

	// ORIGINE VARCHAR(3) NOT NULL
	private java.lang.String origine;

	// PG_SPESA_CLGS DECIMAL(10,0)
	private java.lang.Long pg_spesa_clgs;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// TI_APPARTENENZA_CLGS CHAR(1)
	private java.lang.String ti_appartenenza_clgs;

	// TI_GESTIONE_CLGS CHAR(1)
	private java.lang.String ti_gestione_clgs;
	
    //	ESERCIZIO_PDG_VARIAZIONE NUMBER(4)
	private java.lang.Integer esercizio_pdg_variazione;
	
	//  PG_VARIAZIONE_PDG NUMBER(10)
	private java.lang.Long pg_variazione_pdg;
	
public Pdg_preventivo_etr_detBase() {
	super();
}
public Pdg_preventivo_etr_detBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Long pg_entrata,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,pg_entrata,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo categoria_dettaglio
 */
public java.lang.String getCategoria_dettaglio() {
	return categoria_dettaglio;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita_clgs
 */
public java.lang.String getCd_centro_responsabilita_clgs() {
	return cd_centro_responsabilita_clgs;
}
/* 
 * Getter dell'attributo cd_elemento_voce_clgs
 */
public java.lang.String getCd_elemento_voce_clgs() {
	return cd_elemento_voce_clgs;
}
/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}
/* 
 * Getter dell'attributo cd_linea_attivita_clgs
 */
public java.lang.String getCd_linea_attivita_clgs() {
	return cd_linea_attivita_clgs;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
/* 
 * Getter dell'attributo descrizione
 */
public java.lang.String getDescrizione() {
	return descrizione;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo fl_sola_lettura
 */
public java.lang.Boolean getFl_sola_lettura() {
	return fl_sola_lettura;
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
 * Getter dell'attributo origine
 */
public java.lang.String getOrigine() {
	return origine;
}
/* 
 * Getter dell'attributo pg_spesa_clgs
 */
public java.lang.Long getPg_spesa_clgs() {
	return pg_spesa_clgs;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Getter dell'attributo ti_appartenenza_clgs
 */
public java.lang.String getTi_appartenenza_clgs() {
	return ti_appartenenza_clgs;
}
/* 
 * Getter dell'attributo ti_gestione_clgs
 */
public java.lang.String getTi_gestione_clgs() {
	return ti_gestione_clgs;
}
/* 
 * Setter dell'attributo categoria_dettaglio
 */
public void setCategoria_dettaglio(java.lang.String categoria_dettaglio) {
	this.categoria_dettaglio = categoria_dettaglio;
}
/* 
 * Setter dell'attributo cd_centro_responsabilita_clgs
 */
public void setCd_centro_responsabilita_clgs(java.lang.String cd_centro_responsabilita_clgs) {
	this.cd_centro_responsabilita_clgs = cd_centro_responsabilita_clgs;
}
/* 
 * Setter dell'attributo cd_elemento_voce_clgs
 */
public void setCd_elemento_voce_clgs(java.lang.String cd_elemento_voce_clgs) {
	this.cd_elemento_voce_clgs = cd_elemento_voce_clgs;
}
/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
/* 
 * Setter dell'attributo cd_linea_attivita_clgs
 */
public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs) {
	this.cd_linea_attivita_clgs = cd_linea_attivita_clgs;
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
/* 
 * Setter dell'attributo descrizione
 */
public void setDescrizione(java.lang.String descrizione) {
	this.descrizione = descrizione;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo fl_sola_lettura
 */
public void setFl_sola_lettura(java.lang.Boolean fl_sola_lettura) {
	this.fl_sola_lettura = fl_sola_lettura;
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
/* 
 * Setter dell'attributo origine
 */
public void setOrigine(java.lang.String origine) {
	this.origine = origine;
}
/* 
 * Setter dell'attributo pg_spesa_clgs
 */
public void setPg_spesa_clgs(java.lang.Long pg_spesa_clgs) {
	this.pg_spesa_clgs = pg_spesa_clgs;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
/* 
 * Setter dell'attributo ti_appartenenza_clgs
 */
public void setTi_appartenenza_clgs(java.lang.String ti_appartenenza_clgs) {
	this.ti_appartenenza_clgs = ti_appartenenza_clgs;
}
/* 
 * Setter dell'attributo ti_gestione_clgs
 */
public void setTi_gestione_clgs(java.lang.String ti_gestione_clgs) {
	this.ti_gestione_clgs = ti_gestione_clgs;
}
/**
 * @return
 */
public java.lang.Integer getEsercizio_pdg_variazione() {
	return esercizio_pdg_variazione;
}

/**
 * @return
 */
public java.lang.Long getPg_variazione_pdg() {
	return pg_variazione_pdg;
}

/**
 * @param integer
 */
public void setEsercizio_pdg_variazione(java.lang.Integer integer) {
	esercizio_pdg_variazione = integer;
}

/**
 * @param long1
 */
public void setPg_variazione_pdg(java.lang.Long long1) {
	pg_variazione_pdg = long1;
}

}
