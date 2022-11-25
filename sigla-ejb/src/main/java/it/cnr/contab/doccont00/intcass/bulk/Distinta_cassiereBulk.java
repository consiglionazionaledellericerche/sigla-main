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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Optional;
import java.util.stream.Collectors;
@StorageType(name="D:doccont:document")
public class Distinta_cassiereBulk extends Distinta_cassiereBase implements AllegatoParentBulk {

	@SuppressWarnings("rawtypes")
	public final static Dictionary stato_DistintaKeys = new OrderedHashtable();
	public final static Dictionary tesoreriaKeys = new OrderedHashtable();

	public enum Tesoreria {
		BANCA_ITALIA("Banca d'Italia", "BI"),
		BANCA_TESORIERE("Banca Tesoriere", "BT");
		private final String label, value;
		private Tesoreria(String label, String value) {
			this.value = value;
			this.label = label;
		}
		public String value() {
			return value;
		}
		public String label() {
			return label;
		}

		public static Tesoreria getValueFrom(String value) {
			for (Tesoreria tesoreria : Tesoreria.values()) {
				if (tesoreria.value.equals(value))
					return tesoreria;
			}
			throw new IllegalArgumentException("Tesoreria no found for value: " + value);
		}
	}
	public enum Stato {
		PROVVISORIA("Provvisoria","PRO"),
		DEFINITIVA("Definitiva","DEF"),
		TRASMESSA("Trasmessa","TRA"),
		ACCETTATO_SIOPEPLUS("Accettato SIOPE+","ACC"),
		RIFIUTATO_SIOPEPLUS("Rifiutato SIOPE+","RIF"),
		ACCETTATO_BT("Accettato BT","ABT"),
		RIFIUTATO_BT("Rifiutato BT","RBT");
		private final String label, value;
		private Stato(String label, String value) {
			this.value = value;
			this.label = label;
		}
		public String value() {
			return value;
		}
		public String label() {
			return label;
		}
	}
	static
	{
		for (Distinta_cassiereBulk.Stato stato : Distinta_cassiereBulk.Stato.values()) {
			stato_DistintaKeys.put(stato.value, stato.label);
		}
		for (Distinta_cassiereBulk.Tesoreria tesoreria : Distinta_cassiereBulk.Tesoreria.values()) {
			tesoreriaKeys.put(tesoreria.value, tesoreria.label);
		}
	}


    private CdsBulk cds = new CdsBulk();
	private Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	private String cd_cds_ente;
	private boolean createByOtherUo=false;
	private boolean checkIbanEseguito = false;
	/* TOTALI STORICI */
	private java.math.BigDecimal totStoricoMandatiTrasmessi = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoMandatiDaRitrasmettere = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoMandatiPagamentoTrasmessi = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoMandatiRegSospesoTrasmessi = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoMandatiAccreditamentoTrasmessi = new java.math.BigDecimal(0);

	private java.math.BigDecimal totStoricoReversaliTrasmesse = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoReversaliDaRitrasmettere = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoReversaliRegSospesoTrasmesse = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoReversaliTrasferimentoTrasmesse = new java.math.BigDecimal(0);
	private java.math.BigDecimal totStoricoReversaliRitenuteTrasmesse = new java.math.BigDecimal(0);

	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();

	@StorageProperty(name="doccont:tipo")
	public String getCd_tipo_documento_cont() {
		return "DISTINTA";
	}



	/* TOTALI DISTINTA */
	/* mandati */
	private	java.math.BigDecimal totMandatiAccreditamento = new java.math.BigDecimal(0);
	private	java.math.BigDecimal totMandatiPagamento = new java.math.BigDecimal(0);
	private	java.math.BigDecimal totMandatiRegSospeso = new java.math.BigDecimal(0);
	/* mandati annullati */
	private	java.math.BigDecimal totMandatiAccreditamentoAnnullati = new java.math.BigDecimal(0);
	private	java.math.BigDecimal totMandatiPagamentoAnnullati = new java.math.BigDecimal(0);
	private java.math.BigDecimal totMandatiRegSospesoAnnullati = new java.math.BigDecimal(0);
	/* mandati annullati ritrasmessi */
	private java.math.BigDecimal totMandatiAccreditamentoAnnullatiRitrasmessi = new java.math.BigDecimal(0);
	private java.math.BigDecimal totMandatiPagamentoAnnullatiRitrasmessi = new java.math.BigDecimal(0);
	private java.math.BigDecimal totMandatiRegSospesoAnnullatiRitrasmessi = new java.math.BigDecimal(0);
	/* reversali */
	private java.math.BigDecimal totReversaliRegSospesoCC = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliRegSospesoBI = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliRitenute = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliTrasferimento = new java.math.BigDecimal(0);
	/* reversali annullate */
	private java.math.BigDecimal totReversaliRegSospesoCCAnnullate = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliRegSospesoBIAnnullate = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliRitenuteAnnullate = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliTrasferimentoAnnullate = new java.math.BigDecimal(0);
	/* reversali annullate ritrasmesse */
	private java.math.BigDecimal totReversaliRegSospesoCCAnnullateRitrasmesse = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliRegSospesoBIAnnullateRitrasmesse = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliRitenuteAnnullateRitrasmesse = new java.math.BigDecimal(0);
	private java.math.BigDecimal totReversaliTrasferimentoAnnullateRitrasmesse = new java.math.BigDecimal(0);


	public Distinta_cassiereBulk() {
		super();
	}
	public Distinta_cassiereBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,Integer esercizio,java.lang.Long pg_distinta) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_distinta);
		setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
		setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_unita_organizzativa));
	}
	public java.lang.String getCd_cds() {
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();
	}
	/**
	 * @return java.lang.String
	 */
	public java.lang.String getCd_cds_ente() {
		return cd_cds_ente;
	}
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
		if (unita_organizzativa == null)
			return null;
		return unita_organizzativa.getCd_unita_organizzativa();
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
	 */
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
		return cds;
	}
	public java.math.BigDecimal getTotMandati()
	{
		return totMandatiAccreditamento.add( totMandatiPagamento).add( totMandatiRegSospeso );

	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiAccreditamento() {
		return totMandatiAccreditamento;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiAccreditamentoAnnullati() {
		return totMandatiAccreditamentoAnnullati;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiAccreditamentoAnnullatiRitrasmessi() {
		return totMandatiAccreditamentoAnnullatiRitrasmessi;
	}
	public java.math.BigDecimal getTotMandatiAnnullati()
	{
		return totMandatiAccreditamentoAnnullati.add( totMandatiPagamentoAnnullati).add( totMandatiRegSospesoAnnullati );
	}
	public java.math.BigDecimal getTotMandatiAnnullatiRitrasmessi()
	{
		return totMandatiAccreditamentoAnnullatiRitrasmessi.add( totMandatiPagamentoAnnullatiRitrasmessi).add( totMandatiRegSospesoAnnullatiRitrasmessi );
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiPagamento() {
		return totMandatiPagamento;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiPagamentoAnnullati() {
		return totMandatiPagamentoAnnullati;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiPagamentoAnnullatiRitrasmessi() {
		return totMandatiPagamentoAnnullatiRitrasmessi;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiRegSospeso() {
		return totMandatiRegSospeso;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiRegSospesoAnnullati() {
		return totMandatiRegSospesoAnnullati;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotMandatiRegSospesoAnnullatiRitrasmessi() {
		return totMandatiRegSospesoAnnullatiRitrasmessi;
	}
	public java.math.BigDecimal getTotReversali()
	{
		return totReversaliRegSospesoCC.add( totReversaliTrasferimento).add( totReversaliRegSospesoBI).add(totReversaliRitenute);
	}
	public java.math.BigDecimal getTotReversaliAnnullate()
	{
		return totReversaliRegSospesoCCAnnullate.add( totReversaliTrasferimentoAnnullate).add( totReversaliRegSospesoBIAnnullate).add(totReversaliRitenuteAnnullate);
	}
	public java.math.BigDecimal getTotReversaliAnnullateRitrasmesse()
	{
		return totReversaliRegSospesoCCAnnullateRitrasmesse.add( totReversaliTrasferimentoAnnullateRitrasmesse).add( totReversaliRegSospesoBIAnnullateRitrasmesse).add(totReversaliRitenuteAnnullateRitrasmesse);
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRegSospesoBI() {
		return totReversaliRegSospesoBI;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRegSospesoBIAnnullate() {
		return totReversaliRegSospesoBIAnnullate;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRegSospesoBIAnnullateRitrasmesse() {
		return totReversaliRegSospesoBIAnnullateRitrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRegSospesoCC() {
		return totReversaliRegSospesoCC;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRegSospesoCCAnnullate() {
		return totReversaliRegSospesoCCAnnullate;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRegSospesoCCAnnullateRitrasmesse() {
		return totReversaliRegSospesoCCAnnullateRitrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRitenute() {
		return totReversaliRitenute;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRitenuteAnnullate() {
		return totReversaliRitenuteAnnullate;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliRitenuteAnnullateRitrasmesse() {
		return totReversaliRitenuteAnnullateRitrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliTrasferimento() {
		return totReversaliTrasferimento;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliTrasferimentoAnnullate() {
		return totReversaliTrasferimentoAnnullate;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotReversaliTrasferimentoAnnullateRitrasmesse() {
		return totReversaliTrasferimentoAnnullateRitrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoMandatiAccreditamentoTrasmessi() {
		return totStoricoMandatiAccreditamentoTrasmessi;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoMandatiDaRitrasmettere() {
		return totStoricoMandatiDaRitrasmettere;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoMandatiPagamentoTrasmessi() {
		return totStoricoMandatiPagamentoTrasmessi;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoMandatiRegSospesoTrasmessi() {
		return totStoricoMandatiRegSospesoTrasmessi;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoMandatiTrasmessi() {
		return totStoricoMandatiTrasmessi;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoReversaliDaRitrasmettere() {
		return totStoricoReversaliDaRitrasmettere;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoReversaliRegSospesoTrasmesse() {
		return totStoricoReversaliRegSospesoTrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoReversaliRitenuteTrasmesse() {
		return totStoricoReversaliRitenuteTrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoReversaliTrasferimentoTrasmesse() {
		return totStoricoReversaliTrasferimentoTrasmesse;
	}
	/**
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotStoricoReversaliTrasmesse() {
		return totStoricoReversaliTrasmesse;
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}
	/**
	 * Inizializza l'Oggetto Bulk per l'inserimento.
	 * @param bp Il Business Process in uso
	 * @param context Il contesto dell'azione
	 * @return OggettoBulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
		setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
		setInviaPEC(Boolean.TRUE);
		if(bp instanceof CRUDDistintaCassiereBP){
			setFl_flusso(((CRUDDistintaCassiereBP)bp).isFlusso());
			setFl_sepa(((CRUDDistintaCassiereBP)bp).isSepa());
			setFl_annulli(((CRUDDistintaCassiereBP)bp).isAnnulli());
			Optional.of(bp)
					.filter(CRUDDistintaCassiereBP.class::isInstance)
					.map(CRUDDistintaCassiereBP.class::cast)
					.flatMap(crudDistintaCassiereBP -> Optional.ofNullable(crudDistintaCassiereBP.getTesoreria()))
					.ifPresent(s -> {
						this.setCd_tesoreria(s);
					});
		} else {
			setFl_flusso(Boolean.FALSE);
			setFl_sepa(Boolean.FALSE);
			setFl_annulli(Boolean.FALSE);
		}

		return this;
	}
	/**
	 * Inizializza l'Oggetto Bulk per la ricerca
	 * @param bp Il Business Process in uso
	 * @param context Il contesto dell'azione
	 * @return OggettoBulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
		setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
		return this;
	}
	public void resetTotali()
	{
		totMandatiAccreditamento = new java.math.BigDecimal(0);
		totMandatiPagamento = new java.math.BigDecimal(0);
		totMandatiRegSospeso = new java.math.BigDecimal(0);
		totMandatiAccreditamentoAnnullati = new java.math.BigDecimal(0);
		totMandatiPagamentoAnnullati = new java.math.BigDecimal(0);
		totMandatiRegSospesoAnnullati = new java.math.BigDecimal(0);
		totMandatiAccreditamentoAnnullatiRitrasmessi = new java.math.BigDecimal(0);
		totMandatiPagamentoAnnullatiRitrasmessi = new java.math.BigDecimal(0);
		totMandatiRegSospesoAnnullatiRitrasmessi = new java.math.BigDecimal(0);
		totReversaliRegSospesoBI = new java.math.BigDecimal(0);
		totReversaliRegSospesoCC = new java.math.BigDecimal(0);
		totReversaliRitenute = new java.math.BigDecimal(0);
		totReversaliTrasferimento = new java.math.BigDecimal(0);
		totReversaliRegSospesoBIAnnullate = new java.math.BigDecimal(0);
		totReversaliRegSospesoCCAnnullate = new java.math.BigDecimal(0);
		totReversaliRitenuteAnnullate = new java.math.BigDecimal(0);
		totReversaliTrasferimentoAnnullate = new java.math.BigDecimal(0);
		totReversaliRegSospesoBIAnnullateRitrasmesse = new java.math.BigDecimal(0);
		totReversaliRegSospesoCCAnnullateRitrasmesse = new java.math.BigDecimal(0);
		totReversaliRitenuteAnnullateRitrasmesse = new java.math.BigDecimal(0);
		totReversaliTrasferimentoAnnullateRitrasmesse = new java.math.BigDecimal(0);
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.getCds().setCd_unita_organizzativa(cd_cds);
	}
	/**
	 * @param newCd_cds_ente java.lang.String
	 */
	public void setCd_cds_ente(java.lang.String newCd_cds_ente) {
		cd_cds_ente = newCd_cds_ente;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	/**
	 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
	 */
	public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
		cds = newCds;
	}
	/**
	 * @param newTotMandatiAccreditamento java.math.BigDecimal
	 */
	public void setTotMandatiAccreditamento(java.math.BigDecimal newTotMandatiAccreditamento) {
		totMandatiAccreditamento = newTotMandatiAccreditamento;
	}
	/**
	 * @param newTotMandatiAccreditamentoAnnullati java.math.BigDecimal
	 */
	public void setTotMandatiAccreditamentoAnnullati(java.math.BigDecimal newTotMandatiAccreditamentoAnnullati) {
		totMandatiAccreditamentoAnnullati = newTotMandatiAccreditamentoAnnullati;
	}
	/**
	 * @param newTotMandatiAccreditamentoAnnullatiRitrasmessi java.math.BigDecimal
	 */
	public void setTotMandatiAccreditamentoAnnullatiRitrasmessi(java.math.BigDecimal newTotMandatiAccreditamentoAnnullatiRitrasmessi) {
		totMandatiAccreditamentoAnnullatiRitrasmessi = newTotMandatiAccreditamentoAnnullatiRitrasmessi;
	}
	/**
	 * @param newTotMandatiPagamento java.math.BigDecimal
	 */
	public void setTotMandatiPagamento(java.math.BigDecimal newTotMandatiPagamento) {
		totMandatiPagamento = newTotMandatiPagamento;
	}
	/**
	 * @param newTotMandatiPagamentoAnnullati java.math.BigDecimal
	 */
	public void setTotMandatiPagamentoAnnullati(java.math.BigDecimal newTotMandatiPagamentoAnnullati) {
		totMandatiPagamentoAnnullati = newTotMandatiPagamentoAnnullati;
	}
	/**
	 * @param newTotMandatiPagamentoAnnullatiRitrasmessi java.math.BigDecimal
	 */
	public void setTotMandatiPagamentoAnnullatiRitrasmessi(java.math.BigDecimal newTotMandatiPagamentoAnnullatiRitrasmessi) {
		totMandatiPagamentoAnnullatiRitrasmessi = newTotMandatiPagamentoAnnullatiRitrasmessi;
	}
	/**
	 * @param newTotMandatiRegSospeso java.math.BigDecimal
	 */
	public void setTotMandatiRegSospeso(java.math.BigDecimal newTotMandatiRegSospeso) {
		totMandatiRegSospeso = newTotMandatiRegSospeso;
	}
	/**
	 * @param newTotMandatiRegSospesoAnnullati java.math.BigDecimal
	 */
	public void setTotMandatiRegSospesoAnnullati(java.math.BigDecimal newTotMandatiRegSospesoAnnullati) {
		totMandatiRegSospesoAnnullati = newTotMandatiRegSospesoAnnullati;
	}
	/**
	 * @param newTotMandatiRegSospesoAnnullatiRitrasmessi java.math.BigDecimal
	 */
	public void setTotMandatiRegSospesoAnnullatiRitrasmessi(java.math.BigDecimal newTotMandatiRegSospesoAnnullatiRitrasmessi) {
		totMandatiRegSospesoAnnullatiRitrasmessi = newTotMandatiRegSospesoAnnullatiRitrasmessi;
	}
	/**
	 * @param newTotReversaliRegSospesoBI java.math.BigDecimal
	 */
	public void setTotReversaliRegSospesoBI(java.math.BigDecimal newTotReversaliRegSospesoBI) {
		totReversaliRegSospesoBI = newTotReversaliRegSospesoBI;
	}
	/**
	 * @param newTotReversaliRegSospesoBIAnnullate java.math.BigDecimal
	 */
	public void setTotReversaliRegSospesoBIAnnullate(java.math.BigDecimal newTotReversaliRegSospesoBIAnnullate) {
		totReversaliRegSospesoBIAnnullate = newTotReversaliRegSospesoBIAnnullate;
	}
	/**
	 * @param newTotReversaliRegSospesoBIAnnullateRitrasmesse java.math.BigDecimal
	 */
	public void setTotReversaliRegSospesoBIAnnullateRitrasmesse(java.math.BigDecimal newTotReversaliRegSospesoBIAnnullateRitrasmesse) {
		totReversaliRegSospesoBIAnnullateRitrasmesse = newTotReversaliRegSospesoBIAnnullateRitrasmesse;
	}
	/**
	 * @param newTotReversaliRegSospesoCC java.math.BigDecimal
	 */
	public void setTotReversaliRegSospesoCC(java.math.BigDecimal newTotReversaliRegSospesoCC) {
		totReversaliRegSospesoCC = newTotReversaliRegSospesoCC;
	}
	/**
	 * @param newTotReversaliRegSospesoCCAnnullate java.math.BigDecimal
	 */
	public void setTotReversaliRegSospesoCCAnnullate(java.math.BigDecimal newTotReversaliRegSospesoCCAnnullate) {
		totReversaliRegSospesoCCAnnullate = newTotReversaliRegSospesoCCAnnullate;
	}
	/**
	 * @param newTotReversaliRegSospesoCCAnnullateRitrasmesse java.math.BigDecimal
	 */
	public void setTotReversaliRegSospesoCCAnnullateRitrasmesse(java.math.BigDecimal newTotReversaliRegSospesoCCAnnullateRitrasmesse) {
		totReversaliRegSospesoCCAnnullateRitrasmesse = newTotReversaliRegSospesoCCAnnullateRitrasmesse;
	}
	/**
	 * @param newTotReversaliRitenute java.math.BigDecimal
	 */
	public void setTotReversaliRitenute(java.math.BigDecimal newTotReversaliRitenute) {
		totReversaliRitenute = newTotReversaliRitenute;
	}
	/**
	 * @param newTotReversaliRitenuteAnnullate java.math.BigDecimal
	 */
	public void setTotReversaliRitenuteAnnullate(java.math.BigDecimal newTotReversaliRitenuteAnnullate) {
		totReversaliRitenuteAnnullate = newTotReversaliRitenuteAnnullate;
	}
	/**
	 * @param newTotReversaliRitenuteAnnullateRitrasmesse java.math.BigDecimal
	 */
	public void setTotReversaliRitenuteAnnullateRitrasmesse(java.math.BigDecimal newTotReversaliRitenuteAnnullateRitrasmesse) {
		totReversaliRitenuteAnnullateRitrasmesse = newTotReversaliRitenuteAnnullateRitrasmesse;
	}
	/**
	 * @param newTotReversaliTrasferimento java.math.BigDecimal
	 */
	public void setTotReversaliTrasferimento(java.math.BigDecimal newTotReversaliTrasferimento) {
		totReversaliTrasferimento = newTotReversaliTrasferimento;
	}
	/**
	 * @param newTotReversaliTrasferimentoAnnullate java.math.BigDecimal
	 */
	public void setTotReversaliTrasferimentoAnnullate(java.math.BigDecimal newTotReversaliTrasferimentoAnnullate) {
		totReversaliTrasferimentoAnnullate = newTotReversaliTrasferimentoAnnullate;
	}
	/**
	 * @param newTotReversaliTrasferimentoAnnullateRitrasmesse java.math.BigDecimal
	 */
	public void setTotReversaliTrasferimentoAnnullateRitrasmesse(java.math.BigDecimal newTotReversaliTrasferimentoAnnullateRitrasmesse) {
		totReversaliTrasferimentoAnnullateRitrasmesse = newTotReversaliTrasferimentoAnnullateRitrasmesse;
	}
	/**
	 * @param newTotStoricoMandatiAccreditamentoTrasmessi java.math.BigDecimal
	 */
	public void setTotStoricoMandatiAccreditamentoTrasmessi(java.math.BigDecimal newTotStoricoMandatiAccreditamentoTrasmessi) {
		totStoricoMandatiAccreditamentoTrasmessi = newTotStoricoMandatiAccreditamentoTrasmessi;
	}
	/**
	 * @param newTotStoricoMandatiDaRitrasmettere java.math.BigDecimal
	 */
	public void setTotStoricoMandatiDaRitrasmettere(java.math.BigDecimal newTotStoricoMandatiDaRitrasmettere) {
		totStoricoMandatiDaRitrasmettere = newTotStoricoMandatiDaRitrasmettere;
	}
	/**
	 * @param newTotStoricoMandatiPagamentoTrasmessi java.math.BigDecimal
	 */
	public void setTotStoricoMandatiPagamentoTrasmessi(java.math.BigDecimal newTotStoricoMandatiPagamentoTrasmessi) {
		totStoricoMandatiPagamentoTrasmessi = newTotStoricoMandatiPagamentoTrasmessi;
	}
	/**
	 * @param newTotStoricoMandatiRegSospesoTrasmessi java.math.BigDecimal
	 */
	public void setTotStoricoMandatiRegSospesoTrasmessi(java.math.BigDecimal newTotStoricoMandatiRegSospesoTrasmessi) {
		totStoricoMandatiRegSospesoTrasmessi = newTotStoricoMandatiRegSospesoTrasmessi;
	}
	/**
	 * @param newTotStoricoMandatiTrasmessi java.math.BigDecimal
	 */
	public void setTotStoricoMandatiTrasmessi(java.math.BigDecimal newTotStoricoMandatiTrasmessi) {
		totStoricoMandatiTrasmessi = newTotStoricoMandatiTrasmessi;
	}
	/**
	 * @param newTotStoricoReversaliDaRitrasmettere java.math.BigDecimal
	 */
	public void setTotStoricoReversaliDaRitrasmettere(java.math.BigDecimal newTotStoricoReversaliDaRitrasmettere) {
		totStoricoReversaliDaRitrasmettere = newTotStoricoReversaliDaRitrasmettere;
	}
	/**
	 * @param newTotStoricoReversaliRegSospesoTrasmesse java.math.BigDecimal
	 */
	public void setTotStoricoReversaliRegSospesoTrasmesse(java.math.BigDecimal newTotStoricoReversaliRegSospesoTrasmesse) {
		totStoricoReversaliRegSospesoTrasmesse = newTotStoricoReversaliRegSospesoTrasmesse;
	}
	/**
	 * @param newTotStoricoReversaliRitenuteTrasmesse java.math.BigDecimal
	 */
	public void setTotStoricoReversaliRitenuteTrasmesse(java.math.BigDecimal newTotStoricoReversaliRitenuteTrasmesse)
	{
		totStoricoReversaliRitenuteTrasmesse = newTotStoricoReversaliRitenuteTrasmesse;
	}
	/**
	 * @param newTotStoricoReversaliTrasferimentoTrasmesse java.math.BigDecimal
	 */
	public void setTotStoricoReversaliTrasferimentoTrasmesse(java.math.BigDecimal newTotStoricoReversaliTrasferimentoTrasmesse) {
		totStoricoReversaliTrasferimentoTrasmesse = newTotStoricoReversaliTrasferimentoTrasmesse;
	}
	/**
	 * @param newTotStoricoReversaliTrasmesse java.math.BigDecimal
	 */
	public void setTotStoricoReversaliTrasmesse(java.math.BigDecimal newTotStoricoReversaliTrasmesse) {
		totStoricoReversaliTrasmesse = newTotStoricoReversaliTrasmesse;
	}
	/**
	 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
		unita_organizzativa = newUnita_organizzativa;
	}
	public boolean isCreateByOtherUo() {
		return createByOtherUo;
	}
	public void setCreateByOtherUo(boolean createByOtherUo) {
		this.createByOtherUo = createByOtherUo;
	}
	public boolean isCheckIbanEseguito() {
		return checkIbanEseguito;
	}
	public void setCheckIbanEseguito(boolean checkIbanEseguito) {
		this.checkIbanEseguito = checkIbanEseguito;
	}

	public String getCMISName() {
		return getCMISFolderName().concat(".pdf");
	}

	public String getCMISFolderName() {
		String suffix = "Distinta n.";
		suffix = suffix.concat(String.valueOf(getPg_distinta_def()));
		return suffix;
	}

	public String getBaseIdentificativoFlusso() {
		return Arrays.asList(
				Optional.ofNullable(getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				getCd_unita_organizzativa(),
				Optional.ofNullable(getPg_distinta_def())
						.map(pgDistintaDef -> String.valueOf(pgDistintaDef))
						.orElse("0")
		).stream().collect(
				Collectors.joining("-")
		);

	}
	public String getIdentificativoFlusso() {
		return Arrays.asList(
				getBaseIdentificativoFlusso(),
				Optional.ofNullable(getStato())
						.filter(s -> s.equals(Stato.RIFIUTATO_SIOPEPLUS.value()))
						.map(s -> "R")
						.orElse("I"),
				String.valueOf(getPg_ver_rec())
		).stream().collect(
				Collectors.joining("-")
		);
	}

	public String getFileNameXML() {
		return getIdentificativoFlusso().concat(".xml");
	}

	public static Distinta_cassiereBulk fromIdentificativoFlusso(String identificativoFlusso) {
		try {
			final String[] split = identificativoFlusso.split("-");
			Distinta_cassiereBulk distinta = new Distinta_cassiereBulk();
			distinta.setEsercizio(Integer.valueOf(split[0]));
			distinta.setCd_unita_organizzativa(split[1]);
			distinta.setPg_distinta_def(Long.valueOf(split[2]));
			return distinta;
		} catch (NumberFormatException _ex) {
			return null;
		}
	}

	@Override
	public String toString() {
		return getCMISFolderName();
	}

	public String getStorePath() {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				getCd_unita_organizzativa(),
				"Distinte",
				Optional.ofNullable(getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public static Dictionary getStato_DistintaKeys() {
		return stato_DistintaKeys;
	}

	public void setStato(Stato stato) {
		super.setStato(stato.value());
	}



	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {
				getArchivioAllegati()};
	};

	@Override
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;
	}

	@Override
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		AllegatoGenericoBulk dett = (AllegatoGenericoBulk)getArchivioAllegati().remove(index);
		return dett;
	}

	@Override
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}

	@Override
	public void setArchivioAllegati(BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}

	public String getStatoLabel() {
		return Optional.ofNullable(getStato())
				.map(s -> {
					return Arrays.asList(Stato.values())
							.stream()
							.filter(stato -> stato.value().equals(s))
							.findAny()
							.map(stato -> stato.label())
							.orElse(null);
				})
				.orElse(null);
	}


	public Boolean isBancaItalia() {
		return Optional.ofNullable(getCd_tesoreria())
				.map(s -> Distinta_cassiereBulk.Tesoreria.getValueFrom(s))
				.map(tesoreria -> tesoreria.equals(Distinta_cassiereBulk.Tesoreria.BANCA_ITALIA))
				.orElse(Boolean.FALSE);
	}
	public Boolean isBancaTesoriere() {
		return Optional.ofNullable(getCd_tesoreria())
				.map(s -> Distinta_cassiereBulk.Tesoreria.getValueFrom(s))
				.map(tesoreria -> tesoreria.equals(Distinta_cassiereBulk.Tesoreria.BANCA_TESORIERE))
				.orElse(Boolean.FALSE);
	}
}