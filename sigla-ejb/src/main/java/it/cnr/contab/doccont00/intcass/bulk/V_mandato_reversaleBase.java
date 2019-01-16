package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.persistency.*;

public class V_mandato_reversaleBase extends V_mandato_reversaleKey implements Persistent {

	// CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cd_cds_origine;

	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cd_uo_origine;

	// DS_DOCUMENTO_CONT VARCHAR(300)
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:description"))
	@StorageProperty(name="doccont:descDoc")
	private java.lang.String ds_documento_cont;

	// DT_ANNULLAMENTO TIMESTAMP
	private java.sql.Timestamp dt_annullamento;

	// DT_EMISSIONE TIMESTAMP
	@StorageProperty(name="doccont:datDoc")				
	private java.sql.Timestamp dt_emissione;

	// DT_PAGAMENTO_INCASSO TIMESTAMP
	private java.sql.Timestamp dt_pagamento_incasso;

	// DT_TRASMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_trasmissione;

	// IM_DOCUMENTO_CONT DECIMAL(15,2)
	@StorageProperty(name="doccont:importo")					
	private java.math.BigDecimal im_documento_cont;

	// IM_PAGATO_INCASSATO DECIMAL(15,2)
	private java.math.BigDecimal im_pagato_incassato;

	// STATO CHAR(1)
	private java.lang.String stato;

	// STATO_TRASMISSIONE CHAR(1)
	@StorageProperty(name="doccont:stato_trasmissione")
	private java.lang.String stato_trasmissione;

	// TI_DOCUMENTO_CONT CHAR(1)
	private java.lang.String ti_documento_cont;

	// DT_RITRASMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_ritrasmissione;

	// TI_CC_BI CHAR(1) NOT NULL
	private java.lang.String ti_cc_bi;

	// IM_RITENUTE DECIMAL(15,2)
	private java.math.BigDecimal im_ritenute;

	// PG_DOCUMENTO_CONT DECIMAL(10,0)
	private java.lang.Long pg_documento_cont_padre;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	private java.lang.String cd_tipo_documento_cont_padre;

	// TI_DOCUMENTO_CONT CHAR(1)
	private java.lang.String ti_documento_cont_padre;

	// VERSAMENTO_CORI CHAR(1)
	private java.lang.Boolean versamento_cori;

	// DT_FIRMA TIMESTAMP
	private java.sql.Timestamp dt_firma;

	private String tipo_debito_siope;

	private String esitoOperazione;

	public V_mandato_reversaleBase() {
		super();
	}
	public V_mandato_reversaleBase( Integer esercizio, String cd_tipo_documento_cont, String cd_cds, Long pg_documento_cont) {
		super( esercizio, cd_tipo_documento_cont, cd_cds, pg_documento_cont );
	}
	/* 
	 * Getter dell'attributo cd_cds_origine
	 */
	public java.lang.String getCd_cds_origine() {
		return cd_cds_origine;
	}
	/* 
	 * Getter dell'attributo cd_terzo
	 */
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/06/2002 16.54.17)
	 * @return java.lang.Long
	 */
	public java.lang.String getCd_tipo_documento_cont_padre() {
		return cd_tipo_documento_cont_padre;
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
	 * Getter dell'attributo ds_documento_cont
	 */
	public java.lang.String getDs_documento_cont() {
		return ds_documento_cont;
	}
	/* 
	 * Getter dell'attributo dt_annullamento
	 */
	public java.sql.Timestamp getDt_annullamento() {
		return dt_annullamento;
	}
	/* 
	 * Getter dell'attributo dt_emissione
	 */
	public java.sql.Timestamp getDt_emissione() {
		return dt_emissione;
	}
	/* 
	 * Getter dell'attributo dt_pagamento_incasso
	 */
	public java.sql.Timestamp getDt_pagamento_incasso() {
		return dt_pagamento_incasso;
	}
	/**
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getDt_ritrasmissione() {
		return dt_ritrasmissione;
	}
	/* 
	 * Getter dell'attributo dt_trasmissione
	 */
	public java.sql.Timestamp getDt_trasmissione() {
		return dt_trasmissione;
	}
	/* 
	 * Getter dell'attributo im_documento_cont
	 */
	public java.math.BigDecimal getIm_documento_cont() {
		return im_documento_cont;
	}
	/* 
	 * Getter dell'attributo im_pagato_incassato
	 */
	public java.math.BigDecimal getIm_pagato_incassato() {
		return im_pagato_incassato;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getIm_ritenute() {
		return im_ritenute;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/06/2002 17.36.51)
	 * @return java.lang.Long
	 */
	public java.lang.Long getPg_documento_cont_padre() {
		return pg_documento_cont_padre;
	}
	/* 
	 * Getter dell'attributo stato
	 */
	public java.lang.String getStato() {
		return stato;
	}
	/* 
	 * Getter dell'attributo stato_trasmissione
	 */
	public java.lang.String getStato_trasmissione() {
		return stato_trasmissione;
	}
	/**
	 * @return java.lang.String
	 */
	public java.lang.String getTi_cc_bi() {
		return ti_cc_bi;
	}
	/* 
	 * Getter dell'attributo ti_documento_cont
	 */
	public java.lang.String getTi_documento_cont() {
		return ti_documento_cont;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/06/2002 10.46.59)
	 * @return java.lang.String
	 */
	public java.lang.String getTi_documento_cont_padre() {
		return ti_documento_cont_padre;
	}
	/* 
	 * Setter dell'attributo cd_cds_origine
	 */
	public void setCd_cds_origine(java.lang.String cd_cds_origine) {
		this.cd_cds_origine = cd_cds_origine;
	}
	/* 
	 * Setter dell'attributo cd_terzo
	 */
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/06/2002 16.54.17)
	 * @param newCd_tipo_documento_cont_padre java.lang.Long
	 */
	public void setCd_tipo_documento_cont_padre(java.lang.String newCd_tipo_documento_cont_padre) {
		cd_tipo_documento_cont_padre = newCd_tipo_documento_cont_padre;
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
	 * Setter dell'attributo ds_documento_cont
	 */
	public void setDs_documento_cont(java.lang.String ds_documento_cont) {
		this.ds_documento_cont = ds_documento_cont;
	}
	/* 
	 * Setter dell'attributo dt_annullamento
	 */
	public void setDt_annullamento(java.sql.Timestamp dt_annullamento) {
		this.dt_annullamento = dt_annullamento;
	}
	/* 
	 * Setter dell'attributo dt_emissione
	 */
	public void setDt_emissione(java.sql.Timestamp dt_emissione) {
		this.dt_emissione = dt_emissione;
	}
	/* 
	 * Setter dell'attributo dt_pagamento_incasso
	 */
	public void setDt_pagamento_incasso(java.sql.Timestamp dt_pagamento_incasso) {
		this.dt_pagamento_incasso = dt_pagamento_incasso;
	}
	/**
	 * @param newDt_ritrasmissione java.sql.Timestamp
	 */
	public void setDt_ritrasmissione(java.sql.Timestamp newDt_ritrasmissione) {
		dt_ritrasmissione = newDt_ritrasmissione;
	}
	/* 
	 * Setter dell'attributo dt_trasmissione
	 */
	public void setDt_trasmissione(java.sql.Timestamp dt_trasmissione) {
		this.dt_trasmissione = dt_trasmissione;
	}
	/* 
	 * Setter dell'attributo im_documento_cont
	 */
	public void setIm_documento_cont(java.math.BigDecimal im_documento_cont) {
		this.im_documento_cont = im_documento_cont;
	}
	/* 
	 * Setter dell'attributo im_pagato_incassato
	 */
	public void setIm_pagato_incassato(java.math.BigDecimal im_pagato_incassato) {
		this.im_pagato_incassato = im_pagato_incassato;
	}
	/**
	 * @param newIm_ritenute java.math.BigDecimal
	 */
	public void setIm_ritenute(java.math.BigDecimal newIm_ritenute) {
		im_ritenute = newIm_ritenute;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/06/2002 17.36.51)
	 * @param newPg_documento_cont_padre java.lang.Long
	 */
	public void setPg_documento_cont_padre(java.lang.Long newPg_documento_cont_padre) {
		pg_documento_cont_padre = newPg_documento_cont_padre;
	}
	/* 
	 * Setter dell'attributo stato
	 */
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
	/* 
	 * Setter dell'attributo stato_trasmissione
	 */
	public void setStato_trasmissione(java.lang.String stato_trasmissione) {
		this.stato_trasmissione = stato_trasmissione;
	}
	/**
	 * @param newTi_cc_bi java.lang.String
	 */
	public void setTi_cc_bi(java.lang.String newTi_cc_bi) {
		ti_cc_bi = newTi_cc_bi;
	}
	/* 
	 * Setter dell'attributo ti_documento_cont
	 */
	public void setTi_documento_cont(java.lang.String ti_documento_cont) {
		this.ti_documento_cont = ti_documento_cont;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/06/2002 10.46.59)
	 * @param newTi_documento_cont_padre java.lang.String
	 */
	public void setTi_documento_cont_padre(java.lang.String newTi_documento_cont_padre) {
		ti_documento_cont_padre = newTi_documento_cont_padre;
	}

	/**
	 * @return
	 */
	public java.lang.Boolean getVersamento_cori() {
		return versamento_cori;
	}

	/**
	 * @param boolean1
	 */
	public void setVersamento_cori(java.lang.Boolean boolean1) {
		versamento_cori = boolean1;
	}
	public java.sql.Timestamp getDt_firma() {
		return dt_firma;
	}
	public void setDt_firma(java.sql.Timestamp dt_firma) {
		this.dt_firma = dt_firma;
	}

	public String getTipo_debito_siope() {
		return tipo_debito_siope;
	}

	public void setTipo_debito_siope(String tipo_debito_siope) {
		this.tipo_debito_siope = tipo_debito_siope;
	}

	public String getEsitoOperazione() {
		return esitoOperazione;
	}

	public void setEsitoOperazione(String esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}
}