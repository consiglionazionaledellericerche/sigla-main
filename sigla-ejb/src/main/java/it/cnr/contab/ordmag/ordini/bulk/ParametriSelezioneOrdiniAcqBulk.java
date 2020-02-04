/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bp.ParametriSelezioneOrdiniAcqBP;
import it.cnr.jada.persistency.KeyedPersistent;

public class ParametriSelezioneOrdiniAcqBulk extends AbilitazioneOrdiniAcqBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private java.sql.Timestamp daDataCompetenza;
	private java.sql.Timestamp aDataCompetenza;
	private java.sql.Timestamp daDataMovimento;
	private java.sql.Timestamp aDataMovimento;
	private NumerazioneOrdBulk numerazioneOrd = new NumerazioneOrdBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrdine = new UnitaOperativaOrdBulk();
	private UnitaOperativaOrdBulk daUnitaOperativaRicevente = new UnitaOperativaOrdBulk();
	private UnitaOperativaOrdBulk aUnitaOperativaRicevente = new UnitaOperativaOrdBulk();
	private TerzoBulk terzo = new TerzoBulk();
	private TipoMovimentoMagBulk tipoMovimentoMag =  new TipoMovimentoMagBulk();
	private java.sql.Timestamp dataBolla;
	private String numeroBolla;
	private String lottoFornitore;
	private Bene_servizioBulk daBeneServizio = new Bene_servizioBulk();
	private Bene_servizioBulk aBeneServizio = new Bene_servizioBulk();
	private String tipoMovimento;
	private Integer annoLotto;
	private java.sql.Timestamp daDataOrdine;
	private java.sql.Timestamp aDataOrdine;
	private Integer daNumeroOrdine;
	private Integer aNumeroOrdine;
	private java.sql.Timestamp daDataOrdineDef;
	private java.sql.Timestamp aDataOrdineDef;
	private Long daProgressivo;
	private Long aProgressivo;



	public ParametriSelezioneOrdiniAcqBulk() {
		super();
	}

	public java.sql.Timestamp getDaDataCompetenza() {
		return daDataCompetenza;
	}


	public void setDaDataCompetenza(java.sql.Timestamp daDataCompetenza) {
		this.daDataCompetenza = daDataCompetenza;
	}


	public java.sql.Timestamp getaDataCompetenza() {
		return aDataCompetenza;
	}


	public void setaDataCompetenza(java.sql.Timestamp aDataCompetenza) {
		this.aDataCompetenza = aDataCompetenza;
	}


	public java.sql.Timestamp getDaDataMovimento() {
		return daDataMovimento;
	}


	public void setDaDataMovimento(java.sql.Timestamp daDataMovimento) {
		this.daDataMovimento = daDataMovimento;
	}


	public java.sql.Timestamp getaDataMovimento() {
		return aDataMovimento;
	}


	public void setaDataMovimento(java.sql.Timestamp aDataMovimento) {
		this.aDataMovimento = aDataMovimento;
	}


	public UnitaOperativaOrdBulk getDaUnitaOperativaRicevente() {
		return daUnitaOperativaRicevente;
	}


	public void setDaUnitaOperativaRicevente(UnitaOperativaOrdBulk daUnitaOperativaRicevente) {
		this.daUnitaOperativaRicevente = daUnitaOperativaRicevente;
	}


	public UnitaOperativaOrdBulk getaUnitaOperativaRicevente() {
		return aUnitaOperativaRicevente;
	}


	public void setaUnitaOperativaRicevente(UnitaOperativaOrdBulk aUnitaOperativaRicevente) {
		this.aUnitaOperativaRicevente = aUnitaOperativaRicevente;
	}


	public TerzoBulk getTerzo() {
		return terzo;
	}


	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}


	public TipoMovimentoMagBulk getTipoMovimentoMag() {
		return tipoMovimentoMag;
	}


	public void setTipoMovimentoMag(TipoMovimentoMagBulk tipoMovimentoMag) {
		this.tipoMovimentoMag = tipoMovimentoMag;
	}


	public java.sql.Timestamp getDataBolla() {
		return dataBolla;
	}


	public void setDataBolla(java.sql.Timestamp dataBolla) {
		this.dataBolla = dataBolla;
	}


	public String getNumeroBolla() {
		return numeroBolla;
	}


	public void setNumeroBolla(String numeroBolla) {
		this.numeroBolla = numeroBolla;
	}


	public String getLottoFornitore() {
		return lottoFornitore;
	}


	public void setLottoFornitore(String lottoFornitore) {
		this.lottoFornitore = lottoFornitore;
	}

	public Bene_servizioBulk getDaBeneServizio() {
		return daBeneServizio;
	}

	public void setDaBeneServizio(Bene_servizioBulk daBeneServizio) {
		this.daBeneServizio = daBeneServizio;
	}

	public Bene_servizioBulk getaBeneServizio() {
		return aBeneServizio;
	}

	public void setaBeneServizio(Bene_servizioBulk aBeneServizio) {
		this.aBeneServizio = aBeneServizio;
	}

	public String getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public Integer getAnnoLotto() {
		return annoLotto;
	}

	public void setAnnoLotto(Integer annoLotto) {
		this.annoLotto = annoLotto;
	}

	public java.sql.Timestamp getDaDataOrdine() {
		return daDataOrdine;
	}

	public void setDaDataOrdine(java.sql.Timestamp daDataOrdine) {
		this.daDataOrdine = daDataOrdine;
	}

	public java.sql.Timestamp getaDataOrdine() {
		return aDataOrdine;
	}

	public void setaDataOrdine(java.sql.Timestamp aDataOrdine) {
		this.aDataOrdine = aDataOrdine;
	}

	public Integer getDaNumeroOrdine() {
		return daNumeroOrdine;
	}

	public void setDaNumeroOrdine(Integer daNumeroOrdine) {
		this.daNumeroOrdine = daNumeroOrdine;
	}

	public Integer getaNumeroOrdine() {
		return aNumeroOrdine;
	}

	public void setaNumeroOrdine(Integer aNumeroOrdine) {
		this.aNumeroOrdine = aNumeroOrdine;
	}

	public java.sql.Timestamp getDaDataOrdineDef() {
		return daDataOrdineDef;
	}

	public void setDaDataOrdineDef(java.sql.Timestamp daDataOrdineDef) {
		this.daDataOrdineDef = daDataOrdineDef;
	}

	public java.sql.Timestamp getaDataOrdineDef() {
		return aDataOrdineDef;
	}

	public void setaDataOrdineDef(java.sql.Timestamp aDataOrdineDef) {
		this.aDataOrdineDef = aDataOrdineDef;
	}
	public ParametriSelezioneOrdiniAcqBulk initializeForSearch(
			ParametriSelezioneOrdiniAcqBP bp,
			it.cnr.jada.action.ActionContext context) {

		return this;
	}

	public Long getDaProgressivo() {
		return daProgressivo;
	}

	public void setDaProgressivo(Long daProgressivo) {
		this.daProgressivo = daProgressivo;
	}

	public Long getaProgressivo() {
		return aProgressivo;
	}

	public void setaProgressivo(Long aProgressivo) {
		this.aProgressivo = aProgressivo;
	}

	public boolean isIndicatoAlmenoUnCriterioDiSelezione() {
//		if ((aBeneServizio == null || aBeneServizio.getCd_bene_servizio() == null) && (daBeneServizio == null || daBeneServizio.getCd_bene_servizio() == null) &&
//		aDataCompetenza == null && aDataMovimento == null && aDataOrdine == null && aDataOrdineDef == null && aNumeroOrdine == null && aProgressivo == null &&
//		(aUnitaOperativaRicevente == null || aUnitaOperativaRicevente.getCdUnitaOperativa() == null)  && (daUnitaOperativaRicevente == null || daUnitaOperativaRicevente.getCdUnitaOperativa() == null) &&
//		annoLotto == null && (numerazioneOrd == null || numerazioneOrd.getCdNumeratore() == null) && (unitaOperativaOrdine == null || unitaOperativaOrdine.getCdUnitaOperativa() == null) &&
//		daDataCompetenza == null && daDataMovimento == null && daDataOrdine == null &&
//	  daDataOrdineDef == null && daNumeroOrdine == null && daProgressivo == null && dataBolla == null && lottoFornitore == null && numeroBolla == null &&
//	  (terzo == null || terzo.getCd_terzo() == null) && tipoMovimento == null && (tipoMovimentoMag == null || tipoMovimentoMag.getCdTipoMovimento() == null))
//			return false;
		return true;
	}

	public NumerazioneOrdBulk getNumerazioneOrd() {
		return numerazioneOrd;
	}

	public void setNumerazioneOrd(NumerazioneOrdBulk numerazioneOrd) {
		this.numerazioneOrd = numerazioneOrd;
	}

	public UnitaOperativaOrdBulk getUnitaOperativaOrdine() {
		return unitaOperativaOrdine;
	}

	public void setUnitaOperativaOrdine(UnitaOperativaOrdBulk unitaOperativaOrdine) {
		this.unitaOperativaOrdine = unitaOperativaOrdine;
	}

}