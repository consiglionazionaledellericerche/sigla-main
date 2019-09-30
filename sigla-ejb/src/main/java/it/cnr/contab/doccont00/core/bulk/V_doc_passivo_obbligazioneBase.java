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

public class V_doc_passivo_obbligazioneBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cd_cds_origine;

	// CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cd_modalita_pag;

	// CD_SOSPESO VARCHAR(20)
	private java.lang.String cd_sospeso;

	// CD_TERZO DECIMAL(22,0)
	private Integer cd_terzo;

	// CD_TERZO_CESSIONARIO DECIMAL(22,0)
	private Integer cd_terzo_cessionario;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10)
	private java.lang.String cd_tipo_documento_amm;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cd_uo_origine;

	// COGNOME VARCHAR(50)
	// Eliminati dalla vista: Err. CNR 780 - BORRIELLO	
//	private java.lang.String cognome;

	// DT_FATTURA_FORNITORE TIMESTAMP
	private java.sql.Timestamp dt_fattura_fornitore;

	// DT_SCADENZA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_scadenza;

	// ESERCIZIO DECIMAL(22,0)
	private Integer esercizio;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(22,0)
	private Integer esercizio_obbligazione;

	// FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pgiro;

	// FL_SELEZIONE VARCHAR(1)
	private java.lang.String fl_selezione;

	// FL_FAI_REVERSALE VARCHAR(1)
	private java.lang.Boolean fl_fai_reversale;


	// IM_ASSOCIATO_DOC_CONTABILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato_doc_contabile;

	// IM_IMPONIBILE_DOC_AMM DECIMAL(22,0)
	private java.math.BigDecimal im_imponibile_doc_amm;

	// IM_IVA_DOC_AMM DECIMAL(22,0)
	private java.math.BigDecimal im_iva_doc_amm;

	// IM_SCADENZA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_scadenza;

	// IM_TOTALE_DOC_AMM DECIMAL(22,0)
	private java.math.BigDecimal im_totale_doc_amm;

	// NOME VARCHAR(50)
	// Eliminati dalla vista: Err. CNR 780 - BORRIELLO
//	private java.lang.String nome;

	// NR_FATTURA_FORNITORE VARCHAR(20)
	private java.lang.String nr_fattura_fornitore;

	// PG_BANCA DECIMAL(22,0)
	private Long pg_banca;

	// PG_DOCUMENTO_AMM DECIMAL(22,0)
	private Long pg_documento_amm;

	// PG_LETTERA DECIMAL(22,0)
	private Long pg_lettera;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(22,0)
	private Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(22,0)
	private Long pg_obbligazione_scadenzario;

	// RAGIONE_SOCIALE VARCHAR(100)
	// Eliminati dalla vista: Err. CNR 780 - BORRIELLO
//	private java.lang.String ragione_sociale;

	// STATO_COFI CHAR(1)
	private java.lang.String stato_cofi;

	// TI_ENTRATA_SPESA VARCHAR(1)
	private java.lang.String ti_entrata_spesa;

	// TI_FATTURA VARCHAR(1)
	private java.lang.String ti_fattura;

	// TI_PAGAMENTO CHAR(1) NOT NULL
	private java.lang.String ti_pagamento;

	// TI_SOSPESO_RISCONTRO VARCHAR(1)
	private java.lang.String ti_sospeso_riscontro;
	
	// PG_VER_REC
	private Long pg_ver_rec;

public V_doc_passivo_obbligazioneBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_obbligazione
 */
public java.lang.String getCd_cds_obbligazione() {
	return cd_cds_obbligazione;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_sospeso
 */
public java.lang.String getCd_sospeso() {
	return cd_sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @return java.lang.Integer
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_terzo_cessionario
 */
public Integer getCd_terzo_cessionario() {
	return cd_terzo_cessionario;
}
/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont
 */
public java.lang.String getCd_tipo_documento_cont() {
	return cd_tipo_documento_cont;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo dt_fattura_fornitore
 */
public java.sql.Timestamp getDt_fattura_fornitore() {
	return dt_fattura_fornitore;
}
/* 
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2002 16.09.23)
 * @return java.lang.String
 */
public java.lang.Boolean getFl_fai_reversale() {
	return fl_fai_reversale;
}
/* 
 * Getter dell'attributo fl_pgiro
 */
public java.lang.Boolean getFl_pgiro() {
	return fl_pgiro;
}
/* 
 * Getter dell'attributo fl_selezione
 */
public java.lang.String getFl_selezione() {
	return fl_selezione;
}
/* 
 * Getter dell'attributo im_associato_doc_contabile
 */
public java.math.BigDecimal getIm_associato_doc_contabile() {
	return im_associato_doc_contabile;
}
/* 
 * Getter dell'attributo im_imponibile_doc_amm
 */
public java.math.BigDecimal getIm_imponibile_doc_amm() {
	return im_imponibile_doc_amm;
}
/* 
 * Getter dell'attributo im_iva_doc_amm
 */
public java.math.BigDecimal getIm_iva_doc_amm() {
	return im_iva_doc_amm;
}
/* 
 * Getter dell'attributo im_scadenza
 */
public java.math.BigDecimal getIm_scadenza() {
	return im_scadenza;
}
/* 
 * Getter dell'attributo im_totale_doc_amm
 */
public java.math.BigDecimal getIm_totale_doc_amm() {
	return im_totale_doc_amm;
}
/* 
 * Getter dell'attributo nr_fattura_fornitore
 */
public java.lang.String getNr_fattura_fornitore() {
	return nr_fattura_fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @return java.lang.Long
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @return java.lang.Long
 */
public java.lang.Long getPg_documento_amm() {
	return pg_documento_amm;
}
/* 
 * Getter dell'attributo pg_lettera
 */
public Long getPg_lettera() {
	return pg_lettera;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public Long getPg_obbligazione() {
	return pg_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @return java.lang.Long
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/06/2003 21:44:40)
 * @return java.lang.Long
 */
public java.lang.Long getPg_ver_rec() {
	return pg_ver_rec;
}
/* 
 * Getter dell'attributo stato_cofi
 */
public java.lang.String getStato_cofi() {
	return stato_cofi;
}
/* 
 * Getter dell'attributo ti_entrata_spesa
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}
/* 
 * Getter dell'attributo ti_fattura
 */
public java.lang.String getTi_fattura() {
	return ti_fattura;
}
/* 
 * Getter dell'attributo ti_pagamento
 */
public java.lang.String getTi_pagamento() {
	return ti_pagamento;
}
/* 
 * Getter dell'attributo ti_sospeso_riscontro
 */
public java.lang.String getTi_sospeso_riscontro() {
	return ti_sospeso_riscontro;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_obbligazione
 */
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.cd_cds_obbligazione = cd_cds_obbligazione;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_sospeso
 */
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.cd_sospeso = cd_sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newCd_terzo java.lang.Integer
 */
public void setCd_terzo(java.lang.Integer newCd_terzo) {
	cd_terzo = newCd_terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (25/03/2002 16.47.05)
 * @param newCd_terzo_cessionario java.lang.Integer
 */
public void setCd_terzo_cessionario(java.lang.Integer newCd_terzo_cessionario) {
	cd_terzo_cessionario = newCd_terzo_cessionario;
}
/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont
 */
public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont) {
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo dt_fattura_fornitore
 */
public void setDt_fattura_fornitore(java.sql.Timestamp dt_fattura_fornitore) {
	this.dt_fattura_fornitore = dt_fattura_fornitore;
}
/* 
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newEsercizio_obbligazione java.lang.Integer
 */
public void setEsercizio_obbligazione(java.lang.Integer newEsercizio_obbligazione) {
	esercizio_obbligazione = newEsercizio_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2002 16.09.23)
 * @param newFl_fai_reversale java.lang.String
 */
public void setFl_fai_reversale(java.lang.Boolean newFl_fai_reversale) {
	fl_fai_reversale = newFl_fai_reversale;
}
/* 
 * Setter dell'attributo fl_pgiro
 */
public void setFl_pgiro(java.lang.Boolean fl_pgiro) {
	this.fl_pgiro = fl_pgiro;
}
/* 
 * Setter dell'attributo fl_selezione
 */
public void setFl_selezione(java.lang.String fl_selezione) {
	this.fl_selezione = fl_selezione;
}
/* 
 * Setter dell'attributo im_associato_doc_contabile
 */
public void setIm_associato_doc_contabile(java.math.BigDecimal im_associato_doc_contabile) {
	this.im_associato_doc_contabile = im_associato_doc_contabile;
}
/* 
 * Setter dell'attributo im_imponibile_doc_amm
 */
public void setIm_imponibile_doc_amm(java.math.BigDecimal im_imponibile_doc_amm) {
	this.im_imponibile_doc_amm = im_imponibile_doc_amm;
}
/* 
 * Setter dell'attributo im_iva_doc_amm
 */
public void setIm_iva_doc_amm(java.math.BigDecimal im_iva_doc_amm) {
	this.im_iva_doc_amm = im_iva_doc_amm;
}
/* 
 * Setter dell'attributo im_scadenza
 */
public void setIm_scadenza(java.math.BigDecimal im_scadenza) {
	this.im_scadenza = im_scadenza;
}
/* 
 * Setter dell'attributo im_totale_doc_amm
 */
public void setIm_totale_doc_amm(java.math.BigDecimal im_totale_doc_amm) {
	this.im_totale_doc_amm = im_totale_doc_amm;
}
/* 
 * Setter dell'attributo nr_fattura_fornitore
 */
public void setNr_fattura_fornitore(java.lang.String nr_fattura_fornitore) {
	this.nr_fattura_fornitore = nr_fattura_fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newPg_banca java.lang.Long
 */
public void setPg_banca(java.lang.Long newPg_banca) {
	pg_banca = newPg_banca;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newPg_documento_amm java.lang.Long
 */
public void setPg_documento_amm(java.lang.Long newPg_documento_amm) {
	pg_documento_amm = newPg_documento_amm;
}
/* 
 * Setter dell'attributo pg_lettera
 */
public void setPg_lettera(Long pg_lettera) {
	this.pg_lettera = pg_lettera;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newPg_obbligazione java.lang.Long
 */
public void setPg_obbligazione(java.lang.Long newPg_obbligazione) {
	pg_obbligazione = newPg_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 18.55.00)
 * @param newPg_obbligazione_scadenzario java.lang.Long
 */
public void setPg_obbligazione_scadenzario(java.lang.Long newPg_obbligazione_scadenzario) {
	pg_obbligazione_scadenzario = newPg_obbligazione_scadenzario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/06/2003 21:44:40)
 * @param newPg_ver_rec java.lang.Long
 */
public void setPg_ver_rec(java.lang.Long newPg_ver_rec) {
	pg_ver_rec = newPg_ver_rec;
}
/* 
 * Setter dell'attributo stato_cofi
 */
public void setStato_cofi(java.lang.String stato_cofi) {
	this.stato_cofi = stato_cofi;
}
/* 
 * Setter dell'attributo ti_entrata_spesa
 */
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.ti_entrata_spesa = ti_entrata_spesa;
}
/* 
 * Setter dell'attributo ti_fattura
 */
public void setTi_fattura(java.lang.String ti_fattura) {
	this.ti_fattura = ti_fattura;
}
/* 
 * Setter dell'attributo ti_pagamento
 */
public void setTi_pagamento(java.lang.String ti_pagamento) {
	this.ti_pagamento = ti_pagamento;
}
/* 
 * Setter dell'attributo ti_sospeso_riscontro
 */
public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro) {
	this.ti_sospeso_riscontro = ti_sospeso_riscontro;
}
}
