package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_attivo_accertamentoBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cd_cds_origine;

	// CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cd_modalita_pag;

	// CD_TERZO DECIMAL(22,0)
	private Integer cd_terzo;

	// CD_TERZO_UO_CDS DECIMAL(22,0)
	private Integer cd_terzo_uo_cds;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10)
	private java.lang.String cd_tipo_documento_amm;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cd_uo_origine;

	// COGNOME VARCHAR(50)
//	private java.lang.String cognome;

	// ESERCIZIO DECIMAL(22,0)
	private Integer esercizio;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(22,0)
	private Integer esercizio_accertamento;

	// FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pgiro;

	// FL_SELEZIONE VARCHAR(1)
	private java.lang.String fl_selezione;

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
//	private java.lang.String nome;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(22,0)
	private Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(22,0)
	private Long pg_accertamento;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(22,0)
	private Long pg_accertamento_scadenzario;

	// PG_BANCA DECIMAL(22,0)
	private Long pg_banca;

	// PG_DOCUMENTO_AMM DECIMAL(22,0)
	private Long pg_documento_amm;

	// RAGIONE_SOCIALE VARCHAR(100)
//	private java.lang.String ragione_sociale;

	// STATO_COFI CHAR(1)
	private java.lang.String stato_cofi;

	// TI_FATTURA VARCHAR(1)
	private java.lang.String ti_fattura;

	// TI_PAGAMENTO CHAR(1) NOT NULL
	private java.lang.String ti_pagamento;

	// DT_SCADENZA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_scadenza;

	// PG_VER_REC
	private Long pg_ver_rec;

	private java.lang.Boolean flFatturaElettronica;
	
	private java.lang.String statoInvioSdi;

	private java.lang.String codiceUnivocoUfficioIpa;
	public java.lang.String getCodiceUnivocoUfficioIpa() {
		return codiceUnivocoUfficioIpa;
	}
	public void setCodiceUnivocoUfficioIpa(java.lang.String codiceUnivocoUfficioIpa) {
		this.codiceUnivocoUfficioIpa = codiceUnivocoUfficioIpa;
	}
	public V_doc_attivo_accertamentoBase() {
	super();
}
public java.lang.Boolean getFlFatturaElettronica() {
		return flFatturaElettronica;
	}
	public void setFlFatturaElettronica(java.lang.Boolean flFatturaElettronica) {
		this.flFatturaElettronica = flFatturaElettronica;
	}
	public java.lang.String getStatoInvioSdi() {
		return statoInvioSdi;
	}
	public void setStatoInvioSdi(java.lang.String statoInvioSdi) {
		this.statoInvioSdi = statoInvioSdi;
	}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_accertamento
 */
public java.lang.String getCd_cds_accertamento() {
	return cd_cds_accertamento;
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
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (30/01/2002 15.00.28)
 * @return java.lang.Integer
 */
public java.lang.Integer getCd_terzo_uo_cds() {
	return cd_terzo_uo_cds;
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
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/* 
 * Getter dell'attributo esercizio
 */
public Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_accertamento
 */
public Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
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
public java.lang.Integer getEsercizio_ori_accertamento () {
	return esercizio_ori_accertamento;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 15.37.39)
 * @return java.lang.Long
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 15.37.39)
 * @return java.lang.Long
 */
public java.lang.Long getPg_accertamento_scadenzario() {
	return pg_accertamento_scadenzario;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
/* 
 * Getter dell'attributo pg_documento_amm
 */
public Long getPg_documento_amm() {
	return pg_documento_amm;
}
/**
 * Insert the method's description here.
 * Creation date: (03/06/2003 21:43:59)
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
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_accertamento
 */
public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
	this.cd_cds_accertamento = cd_cds_accertamento;
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
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (30/01/2002 15.00.28)
 * @param newCd_terzo_uo_cds java.lang.Integer
 */
public void setCd_terzo_uo_cds(java.lang.Integer newCd_terzo_uo_cds) {
	cd_terzo_uo_cds = newCd_terzo_uo_cds;
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
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/* 
 * Setter dell'attributo esercizio_doc_attivo
 */
public void setEsercizio(java.lang.Integer esercizio_doc_attivo) {
	this.esercizio = esercizio_doc_attivo;
}
/* 
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
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
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento)  {
	this.esercizio_ori_accertamento=esercizio_ori_accertamento;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 15.37.39)
 * @param newPg_accertamento java.lang.Long
 */
public void setPg_accertamento(java.lang.Long newPg_accertamento) {
	pg_accertamento = newPg_accertamento;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 15.37.39)
 * @param newPg_accertamento_scadenzario java.lang.Long
 */
public void setPg_accertamento_scadenzario(java.lang.Long newPg_accertamento_scadenzario) {
	pg_accertamento_scadenzario = newPg_accertamento_scadenzario;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
/* 
 * Setter dell'attributo pg_documento_amm
 */
public void setPg_documento_amm(Long pg_documento_amm) {
	this.pg_documento_amm = pg_documento_amm;
}
/**
 * Insert the method's description here.
 * Creation date: (03/06/2003 21:43:59)
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
}
