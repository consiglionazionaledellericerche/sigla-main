/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.util.Dictionary;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.Termini_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.ordmag.anag00.NotaPrecodificataBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.StrServ;
public class OrdineAcqBulk extends OrdineAcqBase implements AllegatoParentBulk {
	protected BulkList righeOrdineColl= new BulkList();
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	/**
	 * [NOTA_PRECODIFICATA Rappresenta l'anagrafica delle note precodificate.]
	 **/
	private NotaPrecodificataBulk notaPrecodificata =  new NotaPrecodificataBulk();
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [NUMERAZIONE_ORD Numeratori Ordini]
	 **/
	private NumerazioneOrdBulk numerazioneOrd =  new NumerazioneOrdBulk();
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * [DIVISA La divisa è la moneta utilizzata nella transazione amministrativa, essa si individua attraverso il codice internazionale.
Associati ad ogni divisa sono i cambi che, nel caso di valute fuori dall'Euro, deve potere essere variato periodicamente.]
	 **/
	private DivisaBulk divisa =  new DivisaBulk();
	/**
	 * [BANCA Descrive i riferimenti bancari, postali ed altro previsti per ogni terzo. Non è definita alcuna associazione tra tali riferimenti e le modalità di pagamento.]
	 **/
	private BancaBulk banca =  new BancaBulk();
	/**
	 * [MODALITA_PAGAMENTO Descrive le modalità di pagamento previste per un dato terzo.]
	 **/
	private Modalita_pagamentoBulk modalitaPagamento =  new Modalita_pagamentoBulk();
	/**
	 * [TERMINI_PAGAMENTO Descrive i termini di pagamento previsti per un dato terzo.]
	 **/
	private Termini_pagamentoBulk terminiPagamento =  new Termini_pagamentoBulk();
	/**
	 * [TERZO Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();
	private TerzoBulk terzoCdr =  new TerzoBulk();
	private V_persona_fisicaBulk responsabileProcPers;
	private V_persona_fisicaBulk firmatarioPers;
	private V_persona_fisicaBulk direttorePers;

	public final static String STATO_ANNULLATO = "ANN";
	public final static String STATO_INSERITO = "INS";
	public final static String STATO_DEFINITIVO = "DEF";
	public final static String STATO_INVIATO_ORDINE = "INV";
	
	private Boolean isUtenteAbilitatoInserimentoOrdine = true;
	private Boolean isForApprovazione = false;
		
	public final static Dictionary STATO;
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INSERITO,"Inserita");
		STATO.put(STATO_ANNULLATO,"Annullata");
		STATO.put(STATO_DEFINITIVO,"Definitiva");
		STATO.put(STATO_INVIATO_ORDINE,"Inviata in Ordine");
	}

	/**
	 * [CONTRATTO Anagrafica dei Contratti (attivi e passivi)]
	 **/
	private ContrattoBulk contratto =  new ContrattoBulk();
	/**
	 * [TIPO_ORDINE Causali di annullamento consegne Ordini.]
	 **/
	private TipoOrdineBulk tipoOrdine =  new TipoOrdineBulk();
	/**
	 * [PROCEDURE_AMMINISTRATIVE Tabella contenente le Procedure Amministrative]
	 **/
	private Procedure_amministrativeBulk procedureAmministrative =  new Procedure_amministrativeBulk();
	/**
	 * [CIG null]
	 **/
	private CigBulk cig =  new CigBulk();
	/**
	 * [CUP null]
	 **/
	private CupBulk cup =  new CupBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ
	 **/
	public OrdineAcqBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ
	 **/
	public OrdineAcqBulk(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
		setNumerazioneOrd( new NumerazioneOrdBulk(cdUnitaOperativa,esercizio,cdNumeratore) );
		setUnitaOperativaOrd( new UnitaOperativaOrdBulk(cdUnitaOperativa) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica delle note precodificate.]
	 **/
	public NotaPrecodificataBulk getNotaPrecodificata() {
		return notaPrecodificata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica delle note precodificate.]
	 **/
	public void setNotaPrecodificata(NotaPrecodificataBulk notaPrecodificata)  {
		this.notaPrecodificata=notaPrecodificata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numeratori Ordini]
	 **/
	public NumerazioneOrdBulk getNumerazioneOrd() {
		return numerazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numeratori Ordini]
	 **/
	public void setNumerazioneOrd(NumerazioneOrdBulk numerazioneOrd)  {
		this.numerazioneOrd=numerazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [La divisa è la moneta utilizzata nella transazione amministrativa, essa si individua attraverso il codice internazionale.
Associati ad ogni divisa sono i cambi che, nel caso di valute fuori dall'Euro, deve potere essere variato periodicamente.]
	 **/
	public DivisaBulk getDivisa() {
		return divisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [La divisa è la moneta utilizzata nella transazione amministrativa, essa si individua attraverso il codice internazionale.
Associati ad ogni divisa sono i cambi che, nel caso di valute fuori dall'Euro, deve potere essere variato periodicamente.]
	 **/
	public void setDivisa(DivisaBulk divisa)  {
		this.divisa=divisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Descrive i riferimenti bancari, postali ed altro previsti per ogni terzo. Non è definita alcuna associazione tra tali riferimenti e le modalità di pagamento.]
	 **/
	public BancaBulk getBanca() {
		return banca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Descrive i riferimenti bancari, postali ed altro previsti per ogni terzo. Non è definita alcuna associazione tra tali riferimenti e le modalità di pagamento.]
	 **/
	public void setBanca(BancaBulk banca)  {
		this.banca=banca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Descrive le modalità di pagamento previste per un dato terzo.]
	 **/
	public Modalita_pagamentoBulk getModalitaPagamento() {
		return modalitaPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Descrive le modalità di pagamento previste per un dato terzo.]
	 **/
	public void setModalitaPagamento(Modalita_pagamentoBulk modalitaPagamento)  {
		this.modalitaPagamento=modalitaPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Descrive i termini di pagamento previsti per un dato terzo.]
	 **/
	public Termini_pagamentoBulk getTerminiPagamento() {
		return terminiPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Descrive i termini di pagamento previsti per un dato terzo.]
	 **/
	public void setTerminiPagamento(Termini_pagamentoBulk terminiPagamento)  {
		this.terminiPagamento=terminiPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	public TerzoBulk getTerzo() {
		return terzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Anagrafica dei Contratti (attivi e passivi)]
	 **/
	public ContrattoBulk getContratto() {
		return contratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anagrafica dei Contratti (attivi e passivi)]
	 **/
	public void setContratto(ContrattoBulk contratto)  {
		this.contratto=contratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Causali di annullamento consegne Ordini.]
	 **/
	public TipoOrdineBulk getTipoOrdine() {
		return tipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Causali di annullamento consegne Ordini.]
	 **/
	public void setTipoOrdine(TipoOrdineBulk tipoOrdine)  {
		this.tipoOrdine=tipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Tabella contenente le Procedure Amministrative]
	 **/
	public Procedure_amministrativeBulk getProcedureAmministrative() {
		return procedureAmministrative;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Tabella contenente le Procedure Amministrative]
	 **/
	public void setProcedureAmministrative(Procedure_amministrativeBulk procedureAmministrative)  {
		this.procedureAmministrative=procedureAmministrative;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public CigBulk getCig() {
		return cig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setCig(CigBulk cig)  {
		this.cig=cig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public CupBulk getCup() {
		return cup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setCup(CupBulk cup)  {
		this.cup=cup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		Unita_organizzativaBulk UnitaOrganizzativa = this.getUnitaOrganizzativa();
		if (UnitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	public java.lang.String getCdCdsNotaPrec() {
		NotaPrecodificataBulk notaPrecodificata = this.getNotaPrecodificata();
		if (notaPrecodificata == null)
			return null;
		return getNotaPrecodificata().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCdsNotaPrec(java.lang.String cdCdsNotaPrec)  {
		this.getNotaPrecodificata().setCdCds(cdCdsNotaPrec);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNotaPrecodificata]
	 **/
	public java.lang.String getCdNotaPrecodificata() {
		NotaPrecodificataBulk notaPrecodificata = this.getNotaPrecodificata();
		if (notaPrecodificata == null)
			return null;
		return getNotaPrecodificata().getCdNotaPrecodificata();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNotaPrecodificata]
	 **/
	public void setCdNotaPrecodificata(java.lang.String cdNotaPrecodificata)  {
		this.getNotaPrecodificata().setCdNotaPrecodificata(cdNotaPrecodificata);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		NumerazioneOrdBulk numerazioneOrd = this.getNumerazioneOrd();
		if (numerazioneOrd == null)
			return null;
		return getNumerazioneOrd().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getNumerazioneOrd().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		NumerazioneOrdBulk numerazioneOrd = this.getNumerazioneOrd();
		if (numerazioneOrd == null)
			return null;
		return getNumerazioneOrd().getCdNumeratore();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.getNumerazioneOrd().setCdNumeratore(cdNumeratore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdDivisa]
	 **/
	public java.lang.String getCdDivisa() {
		DivisaBulk divisa = this.getDivisa();
		if (divisa == null)
			return null;
		return getDivisa().getCd_divisa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdDivisa]
	 **/
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.getDivisa().setCd_divisa(cdDivisa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgBanca]
	 **/
	public java.lang.Long getPgBanca() {
		BancaBulk banca = this.getBanca();
		if (banca == null)
			return null;
		return getBanca().getPg_banca();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgBanca]
	 **/
	public void setPgBanca(java.lang.Long pgBanca)  {
		this.getBanca().setPg_banca(pgBanca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaPag]
	 **/
	public java.lang.String getCdModalitaPag() {
		Modalita_pagamentoBulk modalitaPagamento = this.getModalitaPagamento();
		if (modalitaPagamento == null)
			return null;
		return getModalitaPagamento().getCd_modalita_pag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaPag]
	 **/
	public void setCdModalitaPag(java.lang.String cdModalitaPag)  {
		this.getModalitaPagamento().setCd_modalita_pag(cdModalitaPag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerminiPag]
	 **/
	public java.lang.String getCdTerminiPag() {
		Termini_pagamentoBulk terminiPagamento = this.getTerminiPagamento();
		if (terminiPagamento == null)
			return null;
		return getTerminiPagamento().getCd_termini_pag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerminiPag]
	 **/
	public void setCdTerminiPag(java.lang.String cdTerminiPag)  {
		this.getTerminiPagamento().setCd_termini_pag(cdTerminiPag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [responsabileProc]
	 **/
	public java.lang.Integer getResponsabileProc() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [responsabileProc]
	 **/
	public void setResponsabileProc(java.lang.Integer responsabileProc)  {
		this.getTerzo().setCd_terzo(responsabileProc);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [firmatario]
	 **/
	public java.lang.Integer getFirmatario() {
		V_persona_fisicaBulk firmatario = this.getFirmatarioPers();
		if (firmatario == null)
			return null;
		return getFirmatarioPers().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [firmatario]
	 **/
	public void setFirmatario(java.lang.Integer firmatario)  {
		this.getTerzo().setCd_terzo(firmatario);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [direttore]
	 **/
	public java.lang.Integer getDirettore() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [direttore]
	 **/
	public void setDirettore(java.lang.Integer direttore)  {
		this.getTerzo().setCd_terzo(direttore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdr]
	 **/
	public java.lang.Integer getCdr() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdr]
	 **/
	public void setCdr(java.lang.Integer cdr)  {
		this.getTerzo().setCd_terzo(cdr);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioContratto]
	 **/
	public java.lang.Integer getEsercizioContratto() {
		ContrattoBulk contratto = this.getContratto();
		if (contratto == null)
			return null;
		return getContratto().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioContratto]
	 **/
	public void setEsercizioContratto(java.lang.Integer esercizioContratto)  {
		this.getContratto().setEsercizio(esercizioContratto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoContratto]
	 **/
	public java.lang.String getStatoContratto() {
		ContrattoBulk contratto = this.getContratto();
		if (contratto == null)
			return null;
		return getContratto().getStato();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoContratto]
	 **/
	public void setStatoContratto(java.lang.String statoContratto)  {
		this.getContratto().setStato(statoContratto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgContratto]
	 **/
	public java.lang.Long getPgContratto() {
		ContrattoBulk contratto = this.getContratto();
		if (contratto == null)
			return null;
		return getContratto().getPg_contratto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgContratto]
	 **/
	public void setPgContratto(java.lang.Long pgContratto)  {
		this.getContratto().setPg_contratto(pgContratto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOrdine]
	 **/
	public java.lang.String getCdTipoOrdine() {
		TipoOrdineBulk tipoOrdine = this.getTipoOrdine();
		if (tipoOrdine == null)
			return null;
		return getTipoOrdine().getCdTipoOrdine();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOrdine]
	 **/
	public void setCdTipoOrdine(java.lang.String cdTipoOrdine)  {
		this.getTipoOrdine().setCdTipoOrdine(cdTipoOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProcAmm]
	 **/
	public java.lang.String getCdProcAmm() {
		Procedure_amministrativeBulk procedureAmministrative = this.getProcedureAmministrative();
		if (procedureAmministrative == null)
			return null;
		return getProcedureAmministrative().getCd_proc_amm();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProcAmm]
	 **/
	public void setCdProcAmm(java.lang.String cdProcAmm)  {
		this.getProcedureAmministrative().setCd_proc_amm(cdProcAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCig]
	 **/
	public java.lang.String getCdCig() {
		CigBulk cig = this.getCig();
		if (cig == null)
			return null;
		return getCig().getCdCig();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCig]
	 **/
	public void setCdCig(java.lang.String cdCig)  {
		this.getCig().setCdCig(cdCig);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		CupBulk cup = this.getCup();
		if (cup == null)
			return null;
		return getCup().getCdCup();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.getCup().setCdCup(cdCup);
	}
	public TerzoBulk getTerzoCdr() {
		return terzoCdr;
	}
	public void setTerzoCdr(TerzoBulk terzoCdr) {
		this.terzoCdr = terzoCdr;
	}
	public V_persona_fisicaBulk getResponsabileProcPers() {
		return responsabileProcPers;
	}
	public void setResponsabileProcPers(V_persona_fisicaBulk responsabileProcPers) {
		this.responsabileProcPers = responsabileProcPers;
	}
	public V_persona_fisicaBulk getFirmatarioPers() {
		return firmatarioPers;
	}
	public void setFirmatarioPers(V_persona_fisicaBulk firmatarioPers) {
		this.firmatarioPers = firmatarioPers;
	}
	public V_persona_fisicaBulk getDirettorePers() {
		return direttorePers;
	}
	public void setDirettorePers(V_persona_fisicaBulk direttorePers) {
		this.direttorePers = direttorePers;
	}
	public Boolean getIsUtenteAbilitatoInserimentoOrdine() {
		return isUtenteAbilitatoInserimentoOrdine;
	}
	public void setIsUtenteAbilitatoInserimentoOrdine(Boolean isUtenteAbilitatoInserimentoOrdine) {
		this.isUtenteAbilitatoInserimentoOrdine = isUtenteAbilitatoInserimentoOrdine;
	}
	public Boolean getIsForApprovazione() {
		return isForApprovazione;
	}
	public void setIsForApprovazione(Boolean isForApprovazione) {
		this.isForApprovazione = isForApprovazione;
	}
	public BulkList getRigheOrdineColl() {
		return righeOrdineColl;
	}
	public void setRigheOrdineColl(BulkList righeOrdineColl) {
		this.righeOrdineColl = righeOrdineColl;
	}
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;		
	}
	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(StrServ.lpad(this.getNumero().toString(),9,"0"));
		return nomeFile.toString();
	}
	public String recuperoIdOrdineAsString(){
		return StrServ.replace(getCdCds(), ".", "")+getEsercizio()+StrServ.replace(getCdUnitaOperativa(), ".", "")+StrServ.replace(getCdNumeratore(), ".", "")+StrServ.lpad(getNumero().toString(), 5);
	}
	public Boolean isInserito(){
		return getStato() != null && getStato().equals(STATO_INSERITO);
	}
	public Boolean isAnnullato(){
		return getStato() != null && getStato().equals(STATO_ANNULLATO);
	}
	public Boolean isDefinitivo(){
		return getStato() != null && getStato().equals(STATO_DEFINITIVO);
	}
}