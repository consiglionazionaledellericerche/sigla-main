package it.cnr.contab.client.docamm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;


public class FatturaAttiva  implements Serializable{

	public FatturaAttiva() {
		super();
	}

	private Integer esercizio;
	public Integer getEsercizio() {
		return esercizio;
	}
	@XmlElement(required=true)
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	private java.util.ArrayList<FatturaAttivaRiga> righefat;
	
	private java.util.ArrayList<FatturaAttivaIntra> righeIntra;
	
	private String numeroFattura;

	private String cod_errore;
	private String desc_errore;
	private String ti_causale_emissione;
	private String ti_fattura;
	private String utcr;
	private String cd_cds_origine;
	private String cd_uo_origine;
	
	private Long pg_fattura_esterno;
	private String cd_tipo_sezionale;
	private Date dt_registrazione;
	// NOTE VARCHAR(300)
	private java.lang.String note;
	private Long pg_fattura_attiva;
	private java.math.BigDecimal cambio;

	// CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cd_divisa;

	// CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cd_modalita_pag;

	// CD_MODALITA_PAG_UO_CDS VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag_uo_cds;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_UO_CDS DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo_uo_cds;

	// DS_FATTURA_ATTIVA VARCHAR(200)
	private java.lang.String ds_fattura_attiva;
	
	// FL_EXTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_extra_ue;

	// FL_INTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_intra_ue;

	// FL_LIQUIDAZIONE_DIFFERITA CHAR(1) NOT NULL
	private java.lang.Boolean fl_liquidazione_differita;

	// FL_SAN_MARINO CHAR(1) NOT NULL
	private java.lang.Boolean fl_san_marino;
	
	// PG_BANCA DECIMAL(10,0)
	private java.lang.Long pg_banca;

	// PG_BANCA_UO_CDS DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca_uo_cds;
	
	private String rif_ordine;
	
	private java.math.BigDecimal Im_totale_imponibile;
	
	private java.math.BigDecimal Im_totale_iva;
	
	private Date dt_protocollo;
	private java.lang.Long nr_protocollo_iva;
	private String ti_bene_servizio;
	private String fl_pagamento_anticipato;
	public String getCod_errore() {
		return cod_errore;
	}

	public void setCod_errore(String cod_errore) {
		this.cod_errore = cod_errore;
	}

	public String getDesc_errore() {
		return desc_errore;
	}

	public void setDesc_errore(String desc_errore) {
		this.desc_errore = desc_errore;
	}

	public String getTi_causale_emissione() {
		return ti_causale_emissione;
	}
	@XmlElement(required=true)
	public void setTi_causale_emissione(String ti_causale_emissione) {
		this.ti_causale_emissione = ti_causale_emissione;
	}

	public String getTi_fattura() {
		return ti_fattura;
	}
	@XmlElement(required=true)
	public void setTi_fattura(String ti_fattura) {
		this.ti_fattura = ti_fattura;
	}

	public String getUtcr() {
		return utcr;
	}
	@XmlElement(required=true)
	public void setUtcr(String utcr) {
		this.utcr = utcr;
	}

	public String getCd_uo_origine() {
		return cd_uo_origine;
	}
	@XmlElement(required=true)
	public void setCd_uo_origine(String cd_uo_origine) {
		this.cd_uo_origine = cd_uo_origine;
	}
	
	public Long getPg_fattura_esterno() {
		return pg_fattura_esterno;
	}
	@XmlElement(required=true)
	public void setPg_fattura_esterno(Long pg_fattura_esterno) {
		this.pg_fattura_esterno = pg_fattura_esterno;
	}

	public String getCd_tipo_sezionale() {
		return cd_tipo_sezionale;
	}
	@XmlElement(required=true)
	public void setCd_tipo_sezionale(String cd_tipo_sezionale) {
		this.cd_tipo_sezionale = cd_tipo_sezionale;
	}

	public java.lang.String getNote() {
		return note;
	}

	public void setNote(java.lang.String note) {
		this.note = note;
	}

	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}

	@XmlElement(required=true)
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}
	
	public java.lang.Integer getCd_terzo_uo_cds() {
		return cd_terzo_uo_cds;
	}
	@XmlElement(required=true)
	public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
		this.cd_terzo_uo_cds = cd_terzo_uo_cds;
	}

	public java.lang.String getDs_fattura_attiva() {
		return ds_fattura_attiva;
	}

	public void setDs_fattura_attiva(java.lang.String ds_fattura_attiva) {
		this.ds_fattura_attiva = ds_fattura_attiva;
	}

	public java.lang.Boolean getFl_extra_ue() {
		return fl_extra_ue;
	}
	@XmlElement(required=true)
	public void setFl_extra_ue(java.lang.Boolean fl_extra_ue) {
		this.fl_extra_ue = fl_extra_ue;
	}

	public java.lang.Boolean getFl_intra_ue() {
		return fl_intra_ue;
	}
	@XmlElement(required=true)
	public void setFl_intra_ue(java.lang.Boolean fl_intra_ue) {
		this.fl_intra_ue = fl_intra_ue;
	}

	public java.lang.Boolean getFl_liquidazione_differita() {
		return fl_liquidazione_differita;
	}
	@XmlElement(required=true)
	public void setFl_liquidazione_differita(
			java.lang.Boolean fl_liquidazione_differita) {
		this.fl_liquidazione_differita = fl_liquidazione_differita;
	}

	public java.lang.Boolean getFl_san_marino() {
		return fl_san_marino;
	}
	@XmlElement(required=true)
	public void setFl_san_marino(java.lang.Boolean fl_san_marino) {
		this.fl_san_marino = fl_san_marino;
	}

	public java.lang.Long getPg_banca() {
		return pg_banca;
	}

	public void setPg_banca(java.lang.Long pg_banca) {
		this.pg_banca = pg_banca;
	}

	public java.lang.Long getPg_banca_uo_cds() {
		return pg_banca_uo_cds;
	}
	@XmlElement(required=true)
	public void setPg_banca_uo_cds(java.lang.Long pg_banca_uo_cds) {
		this.pg_banca_uo_cds = pg_banca_uo_cds;
	}

	public String getCd_cds_origine() {
		return cd_cds_origine;
	}
	@XmlElement(required=true)
	public void setCd_cds_origine(String cd_cds_origine) {
		this.cd_cds_origine = cd_cds_origine;
	}

	public java.math.BigDecimal getCambio() {
		return cambio;
	}
	@XmlElement(required=true)
	public void setCambio(java.math.BigDecimal cambio) {
		this.cambio = cambio;
	}

	public java.lang.String getCd_divisa() {
		return cd_divisa;
	}
	@XmlElement(required=true)
	public void setCd_divisa(java.lang.String cd_divisa) {
		this.cd_divisa = cd_divisa;
	}

	public java.lang.String getCd_modalita_pag() {
		return cd_modalita_pag;
	}

	public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
		this.cd_modalita_pag = cd_modalita_pag;
	}

	public java.lang.String getCd_modalita_pag_uo_cds() {
		return cd_modalita_pag_uo_cds;
	}
	@XmlElement(required=true)
	public void setCd_modalita_pag_uo_cds(java.lang.String cd_modalita_pag_uo_cds) {
		this.cd_modalita_pag_uo_cds = cd_modalita_pag_uo_cds;
	}

	public ArrayList<FatturaAttivaRiga> getRighefat() {
		return righefat;
	}
	@XmlElement(required=true)
	public void setRighefat(ArrayList<FatturaAttivaRiga> righefat) {
		this.righefat = righefat;
	}

	public Long getPg_fattura_attiva() {
		return pg_fattura_attiva;
	}

	public void setPg_fattura_attiva(Long pg_fattura_attiva) {
		this.pg_fattura_attiva = pg_fattura_attiva;
	}
	@XmlElement(required=true)
	public void setDt_registrazione(Date dt_registrazione) {
		this.dt_registrazione = dt_registrazione;
	}
	public Date getDt_registrazione() {
		return dt_registrazione;
	}
	public String getRif_ordine() {
		return rif_ordine;
	}
	public void setRif_ordine(String rif_ordine) {
		this.rif_ordine = rif_ordine;
	}
	public java.math.BigDecimal getIm_totale_imponibile() {
		return Im_totale_imponibile;
	}
	public void setIm_totale_imponibile(java.math.BigDecimal im_totale_imponibile) {
		Im_totale_imponibile = im_totale_imponibile;
	}
	public java.math.BigDecimal getIm_totale_iva() {
		return Im_totale_iva;
	}
	public void setIm_totale_iva(java.math.BigDecimal im_totale_iva) {
		Im_totale_iva = im_totale_iva;
	}
	public Date getDt_protocollo() {
		return dt_protocollo;
	}
	public void setDt_protocollo(Date dt_protocollo) {
		this.dt_protocollo = dt_protocollo;
	}
	public java.lang.Long getNr_protocollo_iva() {
		return nr_protocollo_iva;
	}
	public void setNr_protocollo_iva(java.lang.Long nr_protocollo_iva) {
		this.nr_protocollo_iva = nr_protocollo_iva;
	}
	public String getTi_bene_servizio() {
		return ti_bene_servizio;
	}
	public void setTi_bene_servizio(String ti_bene_servizio) {
		this.ti_bene_servizio = ti_bene_servizio;
	}
	public java.util.ArrayList<FatturaAttivaIntra> getRigheIntra() {
		return righeIntra;
	}
	public void setRigheIntra(java.util.ArrayList<FatturaAttivaIntra> righeIntra) {
		this.righeIntra = righeIntra;
	}
	public String getFl_pagamento_anticipato() {
		return fl_pagamento_anticipato;
	}
	public void setFl_pagamento_anticipato(String fl_pagamento_anticipato) {
		this.fl_pagamento_anticipato = fl_pagamento_anticipato;
	}
	public String getNumeroFattura() {
		return numeroFattura;
	}
	public void setNumeroFattura(String numeroFattura) {
		this.numeroFattura = numeroFattura;
	}

}
