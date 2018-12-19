package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class ReversaleBase extends ReversaleKey implements Keyed {
	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_cont;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// DS_REVERSALE VARCHAR(300) NOT NULL
	private java.lang.String ds_reversale;

	// DT_ANNULLAMENTO TIMESTAMP
	private java.sql.Timestamp dt_annullamento;

	// DT_EMISSIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_emissione;

	// DT_INCASSO TIMESTAMP
	private java.sql.Timestamp dt_incasso;

	// DT_RITRASMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_ritrasmissione;

	// DT_TRASMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_trasmissione;

	// IM_INCASSATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_incassato;

	// IM_REVERSALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reversale;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// STATO_TRASMISSIONE CHAR(1) NOT NULL
	private java.lang.String stato_trasmissione;

	// TI_COMPETENZA_RESIDUO CHAR(1) NOT NULL
	private java.lang.String ti_competenza_residuo;

	// TI_REVERSALE CHAR(1) NOT NULL
	private java.lang.String ti_reversale;

	// DT_FIRMA TIMESTAMP
	private java.sql.Timestamp dt_firma;

	private java.lang.String stato_trasmissione_annullo;
	
	private java.lang.Boolean fl_riemissione;
	
	private java.sql.Timestamp dt_firma_annullo;
	
	private java.lang.Long pg_reversale_riemissione;

	// ESITO_OPERAZIONE VARCHAR2(30)
	private java.lang.String esitoOperazione;

	// DT_ORA_ESITO_OPERAZIONE TIMESTAMP
	private java.sql.Timestamp dtOraEsitoOperazione;

	// ERRORE_SIOPE_PLUS VARCHAR2(2000)
	private java.lang.String erroreSiopePlus;

	public ReversaleBase() {
		super();
	}
	public ReversaleBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_reversale) {
		super(cd_cds,esercizio,pg_reversale);
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
	 * Getter dell'attributo ds_reversale
	 */
	public java.lang.String getDs_reversale() {
		return ds_reversale;
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
	 * Getter dell'attributo dt_incasso
	 */
	public java.sql.Timestamp getDt_incasso() {
		return dt_incasso;
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
	 * Getter dell'attributo im_incassato
	 */
	public java.math.BigDecimal getIm_incassato() {
		return im_incassato;
	}
	/* 
	 * Getter dell'attributo im_reversale
	 */
	public java.math.BigDecimal getIm_reversale() {
		return im_reversale;
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
	 * Getter dell'attributo ti_reversale
	 */
	public java.lang.String getTi_reversale() {
		return ti_reversale;
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
	 * Setter dell'attributo ds_reversale
	 */
	public void setDs_reversale(java.lang.String ds_reversale) {
		this.ds_reversale = ds_reversale;
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
	 * Setter dell'attributo dt_incasso
	 */
	public void setDt_incasso(java.sql.Timestamp dt_incasso) {
		this.dt_incasso = dt_incasso;
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
	 * Setter dell'attributo im_incassato
	 */
	public void setIm_incassato(java.math.BigDecimal im_incassato) {
		this.im_incassato = im_incassato;
	}
	/* 
	 * Setter dell'attributo im_reversale
	 */
	public void setIm_reversale(java.math.BigDecimal im_reversale) {
		this.im_reversale = im_reversale;
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
	 * Setter dell'attributo ti_reversale
	 */
	public void setTi_reversale(java.lang.String ti_reversale) {
		this.ti_reversale = ti_reversale;
	}
	public java.sql.Timestamp getDt_firma() {
		return dt_firma;
	}
	public void setDt_firma(java.sql.Timestamp dt_firma) {
		this.dt_firma = dt_firma;
	}
	public java.lang.String getStato_trasmissione_annullo() {
		return stato_trasmissione_annullo;
	}
	public void setStato_trasmissione_annullo(
			java.lang.String stato_trasmissione_annullo) {
		this.stato_trasmissione_annullo = stato_trasmissione_annullo;
	}
	public java.lang.Boolean getFl_riemissione() {
		return fl_riemissione;
	}
	public void setFl_riemissione(java.lang.Boolean fl_riemissione) {
		this.fl_riemissione = fl_riemissione;
	}
	public java.sql.Timestamp getDt_firma_annullo() {
		return dt_firma_annullo;
	}
	public void setDt_firma_annullo(java.sql.Timestamp dt_firma_annullo) {
		this.dt_firma_annullo = dt_firma_annullo;
	}
	public java.lang.Long getPg_reversale_riemissione() {
		return pg_reversale_riemissione;
	}
	public void setPg_reversale_riemissione(java.lang.Long pg_reversale_riemissione) {
		this.pg_reversale_riemissione = pg_reversale_riemissione;
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