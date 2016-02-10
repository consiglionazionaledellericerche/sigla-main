package it.cnr.contab.brevetti.client;

import java.util.Date;

public class FatturaAttiva extends FatturaAttivaBase {

	private static final long serialVersionUID = -5467105154764808511L;

	public FatturaAttiva() {
		super();
	}

	private Long progressivo_riga;
	private Long pg_trovato;
	private String ti_fattura;
	private Date dt_registrazione;
	private Integer cd_terzo;
	private String cognome;
	private String nome;
	private String ragione_sociale;
	private String partita_iva;
	private String codice_fiscale;
	private String ds_riga_fattura;
	private String cd_voce_iva;
	private String ds_voce_iva;
	private java.lang.String cd_divisa;
	private java.math.BigDecimal cambio;
	private java.math.BigDecimal im_imponibile;
	private java.math.BigDecimal im_iva;
	private java.math.BigDecimal fcIva;
	private Integer esercizio_reversale;
	private Long pg_reversale;
	private Date dt_emissione_reversale;
	private Date dt_emissione_accertamento;
	private Integer esercizio_accertamento;
	private Long pg_accertamento;

	public FatturaAttivaBase cloneFatturaAttivaBase() {
		FatturaAttivaBase fab = new FatturaAttivaBase();
		fab.setCd_cds(this.getCd_cds());
		fab.setCd_unita_organizzativa(this.getCd_unita_organizzativa());
		fab.setEsercizio(this.getEsercizio());
		fab.setPg_fattura_attiva(this.getPg_fattura_attiva());
		fab.setCd_cds_origine(this.getCd_cds_origine());
		fab.setCd_uo_origine(this.getCd_uo_origine());
		fab.setDt_emissione(this.getDt_emissione());
		return fab;
	}
	public String getTi_fattura() {
		return ti_fattura;
	}
	public void setTi_fattura(String tiFattura) {
		ti_fattura = tiFattura;
	}
	public Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(Integer cdTerzo) {
		cd_terzo = cdTerzo;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRagione_sociale() {
		return ragione_sociale;
	}
	public void setRagione_sociale(String ragioneSociale) {
		ragione_sociale = ragioneSociale;
	}
	public void setDt_registrazione(Date dt_registrazione) {
		this.dt_registrazione = dt_registrazione;
	}
	public Date getDt_registrazione() {
		return dt_registrazione;
	}
	public void setCambio(java.math.BigDecimal cambio) {
		this.cambio = cambio;
	}
	public java.math.BigDecimal getCambio() {
		return cambio;
	}
	public void setCd_divisa(java.lang.String cd_divisa) {
		this.cd_divisa = cd_divisa;
	}
	public java.lang.String getCd_divisa() {
		return cd_divisa;
	}
	public String getPartita_iva() {
		return partita_iva;
	}
	public void setPartita_iva(String partita_iva) {
		this.partita_iva = partita_iva;
	}
	public String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}
	public String getDs_riga_fattura() {
		return ds_riga_fattura;
	}
	public void setDs_riga_fattura(String ds_riga_fattura) {
		this.ds_riga_fattura = ds_riga_fattura;
	}
	public String getCd_voce_iva() {
		return cd_voce_iva;
	}
	public void setCd_voce_iva(String cd_voce_iva) {
		this.cd_voce_iva = cd_voce_iva;
	}
	public String getDs_voce_iva() {
		return ds_voce_iva;
	}
	public void setDs_voce_iva(String ds_voce_iva) {
		this.ds_voce_iva = ds_voce_iva;
	}
	public Integer getEsercizio_reversale() {
		return esercizio_reversale;
	}
	public void setEsercizio_reversale(Integer esercizio_reversale) {
		this.esercizio_reversale = esercizio_reversale;
	}
	public Long getPg_reversale() {
		return pg_reversale;
	}
	public void setPg_reversale(Long pg_reversale) {
		this.pg_reversale = pg_reversale;
	}
	public Date getDt_emissione_reversale() {
		return dt_emissione_reversale;
	}
	public void setDt_emissione_reversale(Date dt_emissione_reversale) {
		this.dt_emissione_reversale = dt_emissione_reversale;
	}
	public java.math.BigDecimal getIm_imponibile() {
		return im_imponibile;
	}
	public void setIm_imponibile(java.math.BigDecimal im_imponibile) {
		this.im_imponibile = im_imponibile;
	}
	public java.math.BigDecimal getIm_iva() {
		return im_iva;
	}
	public void setIm_iva(java.math.BigDecimal im_iva) {
		this.im_iva = im_iva;
	}
	public void setProgressivo_riga(Long progressivo_riga) {
		this.progressivo_riga = progressivo_riga;
	}
	public Long getProgressivo_riga() {
		return progressivo_riga;
	}
	public void setPg_trovato(Long pg_trovato) {
		this.pg_trovato = pg_trovato;
	}
	public Long getPg_trovato() {
		return pg_trovato;
	}
	public Date getDt_emissione_accertamento() {
		return dt_emissione_accertamento;
	}
	public void setDt_emissione_accertamento(Date dt_emissione_accertamento) {
		this.dt_emissione_accertamento = dt_emissione_accertamento;
	}
	public Integer getEsercizio_accertamento() {
		return esercizio_accertamento;
	}
	public void setEsercizio_accertamento(Integer esercizio_accertamento) {
		this.esercizio_accertamento = esercizio_accertamento;
	}
	public Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento(Long pg_accertamento) {
		this.pg_accertamento = pg_accertamento;
	}
	public java.math.BigDecimal getFcIva() {
		return fcIva;
	}
	public void setFcIva(java.math.BigDecimal fcIva) {
		this.fcIva = fcIva;
	}
}
