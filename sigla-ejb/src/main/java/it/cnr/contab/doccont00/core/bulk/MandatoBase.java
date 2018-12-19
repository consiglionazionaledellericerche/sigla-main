package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class MandatoBase extends MandatoKey implements Keyed {
	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// DS_MANDATO VARCHAR(300) NOT NULL
	private java.lang.String ds_mandato;

	// DT_ANNULLAMENTO TIMESTAMP
	private java.sql.Timestamp dt_annullamento;

	// DT_EMISSIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_emissione;

	// DT_PAGAMENTO TIMESTAMP
	private java.sql.Timestamp dt_pagamento;

	// DT_RITRASMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_ritrasmissione;

	// DT_TRASMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_trasmissione;

	// IM_MANDATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_mandato;

	// IM_PAGATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_pagato;

	// IM_RITENUTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ritenute;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// STATO_TRASMISSIONE CHAR(1) NOT NULL
	private java.lang.String stato_trasmissione;

	// TI_COMPETENZA_RESIDUO CHAR(1) NOT NULL
	private java.lang.String ti_competenza_residuo;

	// TI_MANDATO CHAR(1) NOT NULL
	private java.lang.String ti_mandato;

	// DT_FIRMA TIMESTAMP
	private java.sql.Timestamp dt_firma;
	
	// fl_riemissione CHAR(1) 
	private java.lang.Boolean  fl_riemissione;

	private java.lang.Long pg_mandato_riemissione;
	
	// stato_trasmissione_annullo CHAR(1) 
	private java.lang.String stato_trasmissione_annullo;
	
	private java.sql.Timestamp dt_firma_annullo;
		
	private java.sql.Timestamp dt_pagamento_richiesta;

	// ESITO_OPERAZIONE VARCHAR2(30)
	private java.lang.String esitoOperazione;

	// DT_ORA_ESITO_OPERAZIONE TIMESTAMP
	private java.sql.Timestamp dtOraEsitoOperazione;

	// ERRORE_SIOPE_PLUS VARCHAR2(2000)
	private java.lang.String erroreSiopePlus;

	public MandatoBase() {
		super();
	}
	public MandatoBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_mandato) {
		super(cd_cds,esercizio,pg_mandato);
	}
	/* 
	 * Getter dell'attributo cd_cds_origine
	 */
	public java.lang.String getCd_cds_origine() {
		return cd_cds_origine;
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
	 * Getter dell'attributo ds_mandato
	 */
	public java.lang.String getDs_mandato() {
		return ds_mandato;
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
	 * Getter dell'attributo dt_pagamento
	 */
	public java.sql.Timestamp getDt_pagamento() {
		return dt_pagamento;
	}
	/* 
	 * Getter dell'attributo dt_ritrasmissione
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
	 * Getter dell'attributo im_mandato
	 */
	public java.math.BigDecimal getIm_mandato() {
		return im_mandato;
	}
	/* 
	 * Getter dell'attributo im_pagato
	 */
	public java.math.BigDecimal getIm_pagato() {
		return im_pagato;
	}
	/* 
	 * Getter dell'attributo im_ritenute
	 */
	public java.math.BigDecimal getIm_ritenute() {
		return im_ritenute;
	}
	/* 
	 * Getter dell'attributo stato
	 */
	public java.lang.String getStato() {
		return stato;
	}
	/* 
	 * Getter dell'attributo stato_coge
	 */
	public java.lang.String getStato_coge() {
		return stato_coge;
	}
	/* 
	 * Getter dell'attributo stato_trasmissione
	 */
	public java.lang.String getStato_trasmissione() {
		return stato_trasmissione;
	}
	/* 
	 * Getter dell'attributo ti_competenza_residuo
	 */
	public java.lang.String getTi_competenza_residuo() {
		return ti_competenza_residuo;
	}
	/* 
	 * Getter dell'attributo ti_mandato
	 */
	public java.lang.String getTi_mandato() {
		return ti_mandato;
	}
	/* 
	 * Setter dell'attributo cd_cds_origine
	 */
	public void setCd_cds_origine(java.lang.String cd_cds_origine) {
		this.cd_cds_origine = cd_cds_origine;
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
	 * Setter dell'attributo ds_mandato
	 */
	public void setDs_mandato(java.lang.String ds_mandato) {
		this.ds_mandato = ds_mandato;
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
	 * Setter dell'attributo dt_pagamento
	 */
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento) {
		this.dt_pagamento = dt_pagamento;
	}
	/* 
	 * Setter dell'attributo dt_ritrasmissione
	 */
	public void setDt_ritrasmissione(java.sql.Timestamp dt_ritrasmissione) {
		this.dt_ritrasmissione = dt_ritrasmissione;
	}
	/* 
	 * Setter dell'attributo dt_trasmissione
	 */
	public void setDt_trasmissione(java.sql.Timestamp dt_trasmissione) {
		this.dt_trasmissione = dt_trasmissione;
	}
	/* 
	 * Setter dell'attributo im_mandato
	 */
	public void setIm_mandato(java.math.BigDecimal im_mandato) {
		this.im_mandato = im_mandato;
	}
	/* 
	 * Setter dell'attributo im_pagato
	 */
	public void setIm_pagato(java.math.BigDecimal im_pagato) {
		this.im_pagato = im_pagato;
	}
	/* 
	 * Setter dell'attributo im_ritenute
	 */
	public void setIm_ritenute(java.math.BigDecimal im_ritenute) {
		this.im_ritenute = im_ritenute;
	}
	/* 
	 * Setter dell'attributo stato
	 */
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
	/* 
	 * Setter dell'attributo stato_coge
	 */
	public void setStato_coge(java.lang.String stato_coge) {
		this.stato_coge = stato_coge;
	}
	/* 
	 * Setter dell'attributo stato_trasmissione
	 */
	public void setStato_trasmissione(java.lang.String stato_trasmissione) {
		this.stato_trasmissione = stato_trasmissione;
	}
	/* 
	 * Setter dell'attributo ti_competenza_residuo
	 */
	public void setTi_competenza_residuo(java.lang.String ti_competenza_residuo) {
		this.ti_competenza_residuo = ti_competenza_residuo;
	}
	/* 
	 * Setter dell'attributo ti_mandato
	 */
	public void setTi_mandato(java.lang.String ti_mandato) {
		this.ti_mandato = ti_mandato;
	}
	public java.sql.Timestamp getDt_firma() {
		return dt_firma;
	}
	public void setDt_firma(java.sql.Timestamp dt_firma) {
		this.dt_firma = dt_firma;
	}
	public java.lang.Boolean getFl_riemissione() {
		return fl_riemissione;
	}
	public void setFl_riemissione(java.lang.Boolean fl_riemissione) {
		this.fl_riemissione = fl_riemissione;
	}
	public java.lang.String getStato_trasmissione_annullo() {
		return stato_trasmissione_annullo;
	}
	public void setStato_trasmissione_annullo(
			java.lang.String stato_trasmissione_annullo) {
		this.stato_trasmissione_annullo = stato_trasmissione_annullo;
	}
	public java.sql.Timestamp getDt_firma_annullo() {
		return dt_firma_annullo;
	}
	public void setDt_firma_annullo(java.sql.Timestamp dt_firma_annullo) {
		this.dt_firma_annullo = dt_firma_annullo;
	}
	public java.lang.Long getPg_mandato_riemissione() {
		return pg_mandato_riemissione;
	}
	public void setPg_mandato_riemissione(java.lang.Long pg_mandato_riemissione) {
		this.pg_mandato_riemissione = pg_mandato_riemissione;
	}
	public java.sql.Timestamp getDt_pagamento_richiesta() {
		return dt_pagamento_richiesta;
	}
	public void setDt_pagamento_richiesta(java.sql.Timestamp dt_pagamento_richiesta) {
		this.dt_pagamento_richiesta = dt_pagamento_richiesta;
	}

	public String getEsitoOperazione() {
		return esitoOperazione;
	}

	public void setEsitoOperazione(String esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}

	public String getErroreSiopePlus() {
		return erroreSiopePlus;
	}

	public void setErroreSiopePlus(String erroreSiopePlus) {
		this.erroreSiopePlus = erroreSiopePlus;
	}

	public Timestamp getDtOraEsitoOperazione() {
		return dtOraEsitoOperazione;
	}

	public void setDtOraEsitoOperazione(Timestamp dtOraEsitoOperazione) {
		this.dtOraEsitoOperazione = dtOraEsitoOperazione;
	}
}
