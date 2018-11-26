package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SospesoBase extends SospesoKey implements Keyed {
	// CAUSALE VARCHAR(200)
	private java.lang.String causale;

	// CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cd_cds_origine;

	// CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cd_uo_origine;

	// DS_ANAGRAFICO VARCHAR(200)
	private java.lang.String ds_anagrafico;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// FL_STORNATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_stornato;

	// IM_ASSOCIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato;

	// IM_ASS_MOD_1210 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ass_mod_1210;

	// IM_SOSPESO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_sospeso;

	// STATO_SOSPESO CHAR(1) NOT NULL
	private java.lang.String stato_sospeso;

	// TI_CC_BI CHAR(1) NOT NULL
	private java.lang.String ti_cc_bi;

	// CD_SOSPESO_PADRE(20) 
	private java.lang.String cd_sospeso_padre;

	// CD_PROPRIO_SOSPESO(20) 
	private java.lang.String cd_proprio_sospeso;
	
	// DT_STORNO TIMESTAMP NULL
	private java.sql.Timestamp dt_storno;
	
	private java.lang.String destinazione;
	
	private java.lang.String tipo_contabilita;
	
public java.lang.String getDestinazione() {
		return destinazione;
	}
	public void setDestinazione(java.lang.String destinazione) {
		this.destinazione = destinazione;
	}
	public java.lang.String getTipo_contabilita() {
		return tipo_contabilita;
	}
	public void setTipo_contabilita(java.lang.String tipo_contabilita) {
		this.tipo_contabilita = tipo_contabilita;
	}
public SospesoBase() {
	super();
}
public SospesoBase(java.lang.String cd_cds,java.lang.String cd_sospeso,java.lang.Integer esercizio,java.lang.String ti_entrata_spesa,java.lang.String ti_sospeso_riscontro) {
	super(cd_cds,cd_sospeso,esercizio,ti_entrata_spesa,ti_sospeso_riscontro);
}
/* 
 * Getter dell'attributo causale
 */
public java.lang.String getCausale() {
	return causale;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (06/02/2003 12.36.35)
 * @return java.lang.String
 */
public java.lang.String getCd_proprio_sospeso() {
	return cd_proprio_sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (06/02/2003 12.36.35)
 * @return java.lang.String
 */
public java.lang.String getCd_sospeso_padre() {
	return cd_sospeso_padre;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo ds_anagrafico
 */
public java.lang.String getDs_anagrafico() {
	return ds_anagrafico;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo fl_stornato
 */
public java.lang.Boolean getFl_stornato() {
	return fl_stornato;
}
/* 
 * Getter dell'attributo im_ass_mod_1210
 */
public java.math.BigDecimal getIm_ass_mod_1210() {
	return im_ass_mod_1210;
}
/* 
 * Getter dell'attributo im_associato
 */
public java.math.BigDecimal getIm_associato() {
	return im_associato;
}
/* 
 * Getter dell'attributo im_sospeso
 */
public java.math.BigDecimal getIm_sospeso() {
	return im_sospeso;
}
/* 
 * Getter dell'attributo stato_sospeso
 */
public java.lang.String getStato_sospeso() {
	return stato_sospeso;
}
/* 
 * Getter dell'attributo ti_cc_bi
 */
public java.lang.String getTi_cc_bi() {
	return ti_cc_bi;
}
/* 
 * Setter dell'attributo causale
 */
public void setCausale(java.lang.String causale) {
	this.causale = causale;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (06/02/2003 12.36.35)
 * @param newCd_proprio_sospeso java.lang.String
 */
public void setCd_proprio_sospeso(java.lang.String newCd_proprio_sospeso) {
	cd_proprio_sospeso = newCd_proprio_sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (06/02/2003 12.36.35)
 * @param newCd_sospeso_padre java.lang.String
 */
public void setCd_sospeso_padre(java.lang.String newCd_sospeso_padre) {
	cd_sospeso_padre = newCd_sospeso_padre;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo ds_anagrafico
 */
public void setDs_anagrafico(java.lang.String ds_anagrafico) {
	this.ds_anagrafico = ds_anagrafico;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo fl_stornato
 */
public void setFl_stornato(java.lang.Boolean fl_stornato) {
	this.fl_stornato = fl_stornato;
}
/* 
 * Setter dell'attributo im_ass_mod_1210
 */
public void setIm_ass_mod_1210(java.math.BigDecimal im_ass_mod_1210) {
	this.im_ass_mod_1210 = im_ass_mod_1210;
}
/* 
 * Setter dell'attributo im_associato
 */
public void setIm_associato(java.math.BigDecimal im_associato) {
	this.im_associato = im_associato;
}
/* 
 * Setter dell'attributo im_sospeso
 */
public void setIm_sospeso(java.math.BigDecimal im_sospeso) {
	this.im_sospeso = im_sospeso;
}
/* 
 * Setter dell'attributo stato_sospeso
 */
public void setStato_sospeso(java.lang.String stato_sospeso) {
	this.stato_sospeso = stato_sospeso;
}
/* 
 * Setter dell'attributo ti_cc_bi
 */
public void setTi_cc_bi(java.lang.String ti_cc_bi) {
	this.ti_cc_bi = ti_cc_bi;
}

public java.sql.Timestamp getDt_storno() {
	return dt_storno;
}
public void setDt_storno(java.sql.Timestamp dtStorno) {
	dt_storno = dtStorno;
}
}
