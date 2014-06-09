package it.cnr.contab.brevetti.client;

import java.util.Date;

public class FatturaPassiva extends FatturaPassivaBase {

	private static final long serialVersionUID = 2905953174195070690L;

	public FatturaPassiva() {
		super();
	}

	private Long progressivo_riga;
	private Long pg_trovato;
	private String ti_fattura;
	private Date dt_registrazione;
	private Integer cd_terzo;
	private Integer cd_terzo_cessionario;
	private String cognome;
	private String nome;
	private String ragione_sociale;
	private String ds_riga_fattura;
	private String cd_voce_iva;
	private String ds_voce_iva;
	private java.lang.String cd_divisa;
	private java.math.BigDecimal cambio;
	private java.math.BigDecimal im_imponibile;
	private java.math.BigDecimal im_iva;
	private Integer esercizio_mandato;
	private Long pg_mandato;
	private Date dt_emissione_mandato;
	private Date dt_pagamento_fondo_eco;
	private String stato_pagamento_fondo_eco;
	
	public FatturaPassivaBase cloneFatturaPassivaBase() {
		FatturaPassivaBase fab = new FatturaPassivaBase();
		fab.setCd_cds(this.getCd_cds());
		fab.setCd_unita_organizzativa(this.getCd_unita_organizzativa());
		fab.setEsercizio(this.getEsercizio());
		fab.setPg_fattura_passiva(this.getPg_fattura_passiva());
		fab.setDs_fattura_passiva(this.getDs_fattura_passiva());
		fab.setCd_cds_origine(this.getCd_cds_origine());
		fab.setCd_uo_origine(this.getCd_uo_origine());
		fab.setNr_fattura_fornitore(this.getNr_fattura_fornitore());
		fab.setDt_fattura_fornitore(this.getDt_fattura_fornitore());
		fab.setPartita_iva(this.getPartita_iva());
		fab.setCodice_fiscale(this.getCodice_fiscale());
		return fab;
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
	public java.math.BigDecimal getIm_imponibile() {
		return im_imponibile;
	}
	public void setIm_imponibile(java.math.BigDecimal imImponibileDocAmm) {
		im_imponibile = imImponibileDocAmm;
	}
	public java.math.BigDecimal getIm_iva() {
		return im_iva;
	}
	public void setIm_iva(java.math.BigDecimal imIvaDocAmm) {
		im_iva = imIvaDocAmm;
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
	public void setCd_terzo_cessionario(Integer cd_terzo_cessionario) {
		this.cd_terzo_cessionario = cd_terzo_cessionario;
	}
	public Integer getCd_terzo_cessionario() {
		return cd_terzo_cessionario;
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
	public Integer getEsercizio_mandato() {
		return esercizio_mandato;
	}
	public void setEsercizio_mandato(Integer esercizio_mandato) {
		this.esercizio_mandato = esercizio_mandato;
	}
	public Long getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(Long pg_mandato) {
		this.pg_mandato = pg_mandato;
	}
	public Date getDt_emissione_mandato() {
		return dt_emissione_mandato;
	}
	public void setDt_emissione_mandato(Date dt_emissione_mandato) {
		this.dt_emissione_mandato = dt_emissione_mandato;
	}
	public void setDt_pagamento_fondo_eco(Date dt_pagamento_fondo_eco) {
		this.dt_pagamento_fondo_eco = dt_pagamento_fondo_eco;
	}
	public Date getDt_pagamento_fondo_eco() {
		return dt_pagamento_fondo_eco;
	}
	public void setStato_pagamento_fondo_eco(String stato_pagamento_fondo_eco) {
		this.stato_pagamento_fondo_eco = stato_pagamento_fondo_eco;
	}
	public String getStato_pagamento_fondo_eco() {
		return stato_pagamento_fondo_eco;
	}
}
