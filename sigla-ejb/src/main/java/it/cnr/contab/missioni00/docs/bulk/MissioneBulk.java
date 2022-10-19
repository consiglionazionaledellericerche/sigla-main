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

package it.cnr.contab.missioni00.docs.bulk;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.missioni00.service.MissioniCMISService;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Tipo_missioneBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoStorePath;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.StrServ;
import it.cnr.jada.util.action.CRUDBP;

@StorageType(name="D:emppay:missione", parentName="D:emppay:document")
@JsonInclude(value=Include.NON_NULL)
public class MissioneBulk extends MissioneBase implements IDefferUpdateSaldi, IDocumentoAmministrativoSpesaBulk, AllegatoParentBulk, AllegatoStorePath
{
	// Testata Missione
	@JsonIgnore
	private boolean salvataggioTemporaneo = false;	// indica che sto eseguendo un salvataggio temporaneo
	@JsonIgnore
	private int annoSolare;
	@JsonIgnore
	private int esercizioScrivania;
	@JsonIgnore
	private java.util.Vector collectionGiorni;
	public final static Dictionary DA_GEMIS_DA_COMPLETARE;
	@JsonIgnore
	private java.util.List collectionGiorniSpese;
	@JsonIgnore
	private java.util.List collectionGiorniSpeseSelezionati;
	@JsonIgnore
	protected Tipo_missioneBulk tipo_missione;
	@JsonIgnore
	private java.util.Collection tipi_missione;
	public final static Dictionary<String, String> ti_istituz_commKeys;
	@JsonIgnore
	private MissioneBulk missioneIniziale;
	public final static int CANCELLAZIONE_FISICA = 2;
	public final static int CANCELLAZIONE_LOGICA = 1;	
	public final static String SALVA_DEFINITIVO = "D";
	public final static String SALVA_PROVVISORIO = "P";
	public final static String SALVA_TEMPORANEO = "T";
	public final static Dictionary<String, String> ti_salvataggioKeys;
	static
	{
		ti_salvataggioKeys = new OrderedHashtable();
		ti_salvataggioKeys.put("D", "Definitiva");
		ti_salvataggioKeys.put("P", "Provvisoria");
		ti_salvataggioKeys.put("T", "");				

		DA_GEMIS_DA_COMPLETARE = new it.cnr.jada.util.OrderedHashtable();
		DA_GEMIS_DA_COMPLETARE.put(new Boolean(false), "N");
		DA_GEMIS_DA_COMPLETARE.put(new Boolean(true), "Y");

	}	

	// Tappe
	private BulkList<Missione_tappaBulk> tappeMissioneColl = new BulkList<Missione_tappaBulk>();
	@JsonIgnore
	private PrimaryKeyHashtable tappeMissioneHash = null;
	@JsonIgnore
	private boolean tappeConfigurate = false;

	// Dettagli spesa e diaria
	private BulkList<Missione_dettaglioBulk> speseMissioneColl = new BulkList<Missione_dettaglioBulk>();
	@JsonIgnore
	private BulkList<Missione_dettaglioBulk> diariaMissioneColl = new BulkList<Missione_dettaglioBulk>();
	@JsonIgnore
	private boolean speseInserite = false;

	// Dettagli rimborso
	@JsonIgnore
	private BulkList<Missione_dettaglioBulk> rimborsoMissioneColl = new BulkList<Missione_dettaglioBulk>();

	// Consuntivo missione
	@JsonIgnore
	private java.sql.Timestamp giorno_consuntivo;
	@JsonIgnore
	private BigDecimal totaleSpeseDelGiorno = new BigDecimal(0);
	@JsonIgnore
	private BigDecimal totaleNettoDiariaDelGiorno = new BigDecimal(0);
	@JsonIgnore
	private BigDecimal totaleEsenteDiariaDelGiorno = new BigDecimal(0);
	@JsonIgnore
	private BigDecimal totaleRimborsoDelGiorno = new BigDecimal(0);
	@JsonIgnore
	private BigDecimal totaleEsenteRimborsoDelGiorno = new BigDecimal(0);

	//Unità Organizzativa
	@JsonIgnore
	private Unita_organizzativaBulk unitaOrganizzativa;

	// Anagrafico
	@JsonIgnore
	private V_terzo_per_compensoBulk v_terzo;
	protected Rif_inquadramentoBulk rif_inquadramento;
	@JsonIgnore
	private java.util.Collection inquadramenti;
	protected Tipo_rapportoBulk tipo_rapporto;
	@JsonIgnore
	private java.util.Collection tipi_rapporto;
	protected BancaBulk banca;	
	protected Rif_modalita_pagamentoBulk modalita_pagamento;
	@JsonIgnore
	protected Rif_termini_pagamentoBulk termini_pagamento;
	@JsonIgnore
	private java.util.Collection modalita;
	@JsonIgnore
	private java.util.Collection termini;
	public final static String ANAG_ALTRO = "A";
	public final static String ANAG_DIPENDENTE = "D";				
	public final static Dictionary ti_anagraficoKeys;
	static
	{
		ti_anagraficoKeys = new OrderedHashtable();
		ti_anagraficoKeys.put("D", "Dipendente");
		ti_anagraficoKeys.put("A", "Altro");		
	}

	// Diaria
	@JsonIgnore
	protected Tipo_trattamentoBulk tipo_trattamento;
	@JsonIgnore
	private java.util.Collection tipi_trattamento;

	// Compenso
	@JsonIgnore
	private CompensoBulk compenso = new CompensoBulk();

	// Anticipo
	@JsonIgnore
	private AnticipoBulk anticipo = new AnticipoBulk();
	@JsonIgnore
	private AnticipoBulk anticipoClone = new AnticipoBulk();

	// Obbligazione
	@JsonIgnore
	private Vector documentiContabiliCancellati = null;
	@JsonIgnore
	private Vector dettagliCancellati = null;
	@JsonIgnore
	private Obbligazione_scadenzarioBulk obbligazione_scadenzario = new Obbligazione_scadenzarioBulk();;
	@JsonIgnore
	private Obbligazione_scadenzarioBulk obbligazione_scadenzarioClone = new Obbligazione_scadenzarioBulk();
	@JsonIgnore
	private it.cnr.jada.bulk.PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();
	@JsonIgnore
	private java.lang.String riportata = NON_RIPORTATO;
	@JsonIgnore
	private java.lang.String riportataInScrivania = NON_RIPORTATO;

	// Dati da Rimborso Missione (GeMis)
	private Integer esercizioObblGeMis;
	private Integer esercizioOriObblGeMis;
	private String cdsObblGeMis;
	private Long pgObblGeMis;
	private Integer esercizioAnticipoGeMis;
	private String cdsAnticipoGeMis;
	private Long pgAnticipoGeMis;
	private String gaeGeMis;
	private String cdrGeMis;
	private String voceGeMis;
	private Long pgMissioneFromGeMis;
    private BigDecimal importoDaRimborsare;

	// Fondo Economale
	public final static Dictionary STATO_FONDO_ECO;
	public final static String NO_FONDO_ECO = "N";
	public final static String FONDO_ECO = "A";
	public final static String REGISTRATO_IN_FONDO_ECO = "R";
	// Stati Cofi
	public final static java.util.Dictionary STATO_COFI;	
	public final static String STATO_INIZIALE_COFI = "I";	
	public final static String STATO_CONTABILIZZATO_COFI = "C";
	public final static String STATO_PAGATO = "P";
	public final static String STATO_ANNULLATO = "A";			
	// Stati Coan
	public final static String STATO_INIZIALE_COAN = "N";
	public final static String STATO_CONTABILIZZATO_COAN = "C";
	public final static String STATO_RICONTABILIZZARE_COAN = "R";
	public final static String STATO_COAN_NON_PROCESSARE = "X";	
	// Stati Coge	
	public final static String STATO_INIZIALE_COGE = "N";	
	public final static String STATO_CONTABILIZZATO_COGE = "C";
	public final static String STATO_RICONTABILIZZARE_COGE = "R";
	public final static String STATO_COGE_NON_PROCESSARE = "X";		
	// Stati Manrev
	public final static java.util.Dictionary TI_ASSOCIATO_MANREV;
	public final static String STATO_INIZIALE_MANREV = "N";
	public final static java.lang.String ASSOCIATO_MANREV = "T";
	// Stati documento riportato
	public final static Dictionary STATI_RIPORTO;

	@JsonIgnore
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	public final static Dictionary STATO_LIQUIDAZIONE;
	static
	{
		ti_istituz_commKeys = new OrderedHashtable();
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			ti_istituz_commKeys.put(tipoIVA.value(), tipoIVA.label());
		}

		STATO_FONDO_ECO = new it.cnr.jada.util.OrderedHashtable();
		STATO_FONDO_ECO.put(NO_FONDO_ECO,"Non usare fondo economale");
		STATO_FONDO_ECO.put(FONDO_ECO,"Usa fondo economale");
		STATO_FONDO_ECO.put(REGISTRATO_IN_FONDO_ECO,"Registrato in fondo economale");

		TI_ASSOCIATO_MANREV = new it.cnr.jada.util.OrderedHashtable();
		TI_ASSOCIATO_MANREV.put(ASSOCIATO_MANREV,"Man/rev associato");
		TI_ASSOCIATO_MANREV.put(STATO_INIZIALE_MANREV,"Man/rev non associato");

		STATO_COFI = new it.cnr.jada.util.OrderedHashtable();
		STATO_COFI.put(STATO_INIZIALE_COFI,"Iniziale");
		STATO_COFI.put(STATO_CONTABILIZZATO_COFI,"Contabilizzato");
		STATO_COFI.put(STATO_PAGATO,"Pagato");
		STATO_COFI.put(STATO_ANNULLATO,"Annullato");

		STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
		STATI_RIPORTO.put(NON_RIPORTATO,"Non riportata");
		STATI_RIPORTO.put(PARZIALMENTE_RIPORTATO,"Parzialmente riportata");
		STATI_RIPORTO.put(COMPLETAMENTE_RIPORTATO,"Completamente riportata");

		STATO_LIQUIDAZIONE = new it.cnr.jada.util.OrderedHashtable();
		STATO_LIQUIDAZIONE.put(LIQ, "Liquidabile");
		STATO_LIQUIDAZIONE.put(NOLIQ, "Non Liquidabile");
		STATO_LIQUIDAZIONE.put(SOSP, "Liquidazione sospesa");

	}
	@JsonIgnore
	private java.sql.Timestamp dataInizioObbligoRegistroUnico;
	public MissioneBulk() {
		super();
	}
	public MissioneBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_missione) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_missione);
	}
	/**
	 * Metodo richiesto dall' interfaccia IDefferUpdateSaldi.
	 * Il metodo aggiunge alla PrimaryKeyHashMap dei saldi il documento contabile (in chiave)
	 * e i relativi valori di business (in valore). Se la mappa non esiste viene creata
	 */

	public void addToDefferredSaldi(IDocumentoContabileBulk docCont, java.util.Map values) 
	{
		if (docCont != null) 
		{
			if (deferredSaldi == null)
				deferredSaldi = new PrimaryKeyHashMap();
			if (!deferredSaldi.containsKey(docCont))
				deferredSaldi.put(docCont, values);
			else 
			{
				java.util.Map firstValues = (java.util.Map)deferredSaldi.get(docCont);
				deferredSaldi.remove(docCont);
				deferredSaldi.put(docCont, firstValues);
			}
		}
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk.
	 */

	public void addToDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) 
	{
		if (dettaglio != null && ((OggettoBulk)dettaglio).getCrudStatus() == OggettoBulk.NORMAL) 
		{
			getDettagliCancellati().addElement(dettaglio);
			addToDocumentiContabiliCancellati(dettaglio.getScadenzaDocumentoContabile());
		}
	}

	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk.
	 * La collection 'documentiContabiliCancellati' contiene le scadenze elaborate dalla missione
	 * che devono essere cancellate/inserite/modificate a db
	 */

	public void addToDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk scadenza) 
	{
		if(getDocumentiContabiliCancellati() == null)
			setDocumentiContabiliCancellati(new Vector());

		// documentiContabiliCancellati contiene le scadenze non piu' agganciate alla missione
		// che pero' devo essere inserite op aggiornate in tabella
		if(	(scadenza != null) && (((OggettoBulk)scadenza).getCrudStatus() == OggettoBulk.NORMAL) &&
				(!BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)scadenza)))
		{
			scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0));
			getDocumentiContabiliCancellati().addElement(scadenza);
		}
	}

	/**
	 * Il metodo inizializza il dettaglio di spesa che si sta creando
	 */

	public int addToSpeseMissioneColl( Missione_dettaglioBulk spesa ) 
	{
		// Assegno un progressivo al dettaglio di spesa
		spesa.setPg_riga(assegnaProgressivoSpesa(spesa));

		spesa.setMissione(this);
		if(getPg_missione() != null)
			spesa.setPg_missione(getPg_missione());		
		spesa.setTipo_spesa(new Missione_tipo_spesaBulk());	
		spesa.setTi_spesa_diaria(spesa.TIPO_SPESA);
		spesa.setCd_cds(getCd_cds());
		spesa.setCd_unita_organizzativa(getCd_unita_organizzativa());
		spesa.setEsercizio(getEsercizio());
		spesa.setUser(this.getUser());

		// Inizializzo i campi "not null" che sono della diaria e non della spesa
		spesa.setIm_quota_esente(new BigDecimal(0));
		spesa.setIm_diaria_lorda(new BigDecimal(0));
		spesa.setIm_diaria_netto(new BigDecimal(0));
		spesa.setFl_diaria_manuale(new Boolean(false));

		spesa.setIm_rimborso(new BigDecimal(0));

		// Inizializzo i campi "not null" di particolari tipi di spesa
		spesa.setIm_base_maggiorazione(new BigDecimal(0));
		spesa.setIm_maggiorazione(new BigDecimal(0));
		spesa.setIm_maggiorazione_euro(new java.math.BigDecimal(0));	
		spesa.setPercentuale_maggiorazione(new BigDecimal(0));
		spesa.setIndennita_chilometrica(new BigDecimal(0));

		speseMissioneColl.add(spesa);

		return speseMissioneColl.size()-1;
	}
	/**
	 * Il metodo inizializza la tappa che si sta creando
	 */

	public int addToTappeMissioneColl( Missione_tappaBulk tappa ) 
	{
		tappeMissioneColl.add(tappa);

		tappa.setMissione(this);
		if(getPg_missione() != null)
			tappa.setPg_missione(getPg_missione());	
		tappa.setCd_cds(getCd_cds());
		tappa.setCd_unita_organizzativa(getCd_unita_organizzativa());
		tappa.setEsercizio(getEsercizio());
		tappa.setUser(this.getUser());
		tappa.setComune(tappa.COMUNE_ALTRO);
		tappa.setFl_comune_altro(new Boolean(true));
		tappa.setFl_comune_proprio(new Boolean(false));
		tappa.setFl_comune_estero(new Boolean(false));

		tappa.setFl_no_diaria(new Boolean(true));
		tappa.setFlag_spese(tappa.NESSUNO);	
		tappa.setFl_rimborso(new Boolean(false));

		return tappeMissioneColl.size()-1;
	}
	/**
	 * Il metodo determina se i campi necessari per ricercare i tipi rapporto
	 * sono stati inizializzati o meno
	 */
	public boolean areCampiPerRicercaTipiRapportoValorizzati() 
	{
		return (getV_terzo()!=null && getV_terzo().getCd_terzo()!=null && getDt_inizio_missione()!=null);
	}
	/**
	 * Il metodo determina se i campi necessari per ricercare i tipi trattamento
	 * sono stati inizializzati o meno
	 */
	public boolean areCampiPerRicercaTipiTrattamentoValorizzati() 
	{
		return (getCd_tipo_rapporto()!=null && getDt_registrazione()!=null);
	}
	/**
	 *	Il metodo ritorno un progressivo per il nuovo dettaglio di spesa ciclando anche sulla
	 *	lista dei dettagli cancellati
	 */

	public Long assegnaProgressivoSpesa(Missione_dettaglioBulk spesa) 
	{
		long max = 0;
		for (Iterator i = getDettagliMissioneColl().iterator(); i.hasNext();) 
		{
			long prog = ((Missione_dettaglioBulk)i.next()).getPg_riga().longValue();
			if (prog > max) 
				max = prog;
		}
		long maxDel = 0;	
		for ( Iterator i = getDettagliMissioneColl().deleteIterator(); i.hasNext(); )
		{
			long prog = ((Missione_dettaglioBulk)i.next()).getPg_riga().longValue();
			if (prog > maxDel) 
				maxDel = prog;
		}
		if(max>=maxDel)
			return new Long(max+1);
		else
			return new Long(maxDel+1);
	}
	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo delle spese,
	 * della diaria e della missione
	 */ 

	public void calcolaConsuntivi() 
	{
		calcolaConsuntiviSpese();			
		calcolaConsuntiviDiaria();
		calcolaConsuntiviRimborso();
		calcolaConsuntiviMissione();
	}
	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo diaria 
	 * della missione
	 */

	public void calcolaConsuntiviDiaria() 
	{
		setIm_diaria_netto(new BigDecimal(0));
		setIm_quota_esente(new BigDecimal(0));	
		setIm_diaria_lorda(new BigDecimal(0));

		if(getDiariaMissioneColl() == null)
			return;

		for ( Iterator i = getDiariaMissioneColl().iterator(); i.hasNext(); )
		{
			Missione_dettaglioBulk diaria = (Missione_dettaglioBulk)i.next();

			if(diaria.getIm_diaria_netto() != null)
			{
				setIm_diaria_netto(getIm_diaria_netto().add(diaria.getIm_diaria_netto()));
				setIm_diaria_netto(getIm_diaria_netto().setScale(2, BigDecimal.ROUND_HALF_UP));
			}	
			if(diaria.getIm_quota_esente() != null)
			{
				setIm_quota_esente(getIm_quota_esente().add(diaria.getIm_quota_esente()));
				setIm_quota_esente(getIm_quota_esente().setScale(2, BigDecimal.ROUND_HALF_UP));
			}	
			if(diaria.getIm_diaria_lorda() != null)
			{
				setIm_diaria_lorda(getIm_diaria_lorda().add(diaria.getIm_diaria_lorda()));
				setIm_diaria_lorda(getIm_diaria_lorda().setScale(2, BigDecimal.ROUND_HALF_UP));
			}	
		}
	}
	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo diaria della missione
	 * di un determinato giorno
	 */

	public void calcolaConsuntiviDiariaDelGiorno() 
	{
		setTotaleNettoDiariaDelGiorno(new BigDecimal(0));
		setTotaleEsenteDiariaDelGiorno(new BigDecimal(0));			

		if((getGiorno_consuntivo() == null) || (getDiariaMissioneColl() == null))
			return;

		for ( Iterator i = getDiariaMissioneColl().iterator(); i.hasNext(); )
		{
			Missione_dettaglioBulk spesa = (Missione_dettaglioBulk)i.next();

			if(getGiorno_consuntivo().equals(spesa.getDt_inizio_tappa()))
			{
				if(spesa.getIm_diaria_netto() != null)
					setTotaleNettoDiariaDelGiorno(getTotaleNettoDiariaDelGiorno().add(spesa.getIm_diaria_netto()));
				if(spesa.getIm_quota_esente() != null)	
					setTotaleEsenteDiariaDelGiorno(getTotaleEsenteDiariaDelGiorno().add(spesa.getIm_quota_esente()));			
			}	
		}	
	}
	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo rimborso 
	 * della missione
	 */

	public void calcolaConsuntiviRimborso() 
	{
		setIm_rimborso(new BigDecimal(0));
		setIm_quota_esente(new BigDecimal(0));	

		if(getRimborsoMissioneColl() == null)
			return;

		for ( Iterator i = getRimborsoMissioneColl().iterator(); i.hasNext(); )
		{
			Missione_dettaglioBulk rimborso = (Missione_dettaglioBulk)i.next();

			if(rimborso.getIm_rimborso() != null)
			{
				setIm_rimborso(getIm_rimborso().add(rimborso.getIm_rimborso()));
				setIm_rimborso(getIm_rimborso().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			}	
			if(rimborso.getIm_quota_esente() != null)
			{
				setIm_quota_esente(getIm_quota_esente().add(rimborso.getIm_quota_esente()));
				setIm_quota_esente(getIm_quota_esente().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			}	
		}
	}
	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo rimborso della missione
	 * di un determinato giorno
	 */

	public void calcolaConsuntiviRimborsoDelGiorno() 
	{
		setTotaleRimborsoDelGiorno(new BigDecimal(0));
		setTotaleEsenteRimborsoDelGiorno(new BigDecimal(0));			

		if((getGiorno_consuntivo() == null) || (getRimborsoMissioneColl() == null))
			return;

		for ( Iterator i = getRimborsoMissioneColl().iterator(); i.hasNext(); )
		{
			Missione_dettaglioBulk rimborso = (Missione_dettaglioBulk)i.next();

			if(getGiorno_consuntivo().equals(rimborso.getDt_inizio_tappa()))
			{
				if(rimborso.getIm_rimborso() != null)
					setTotaleRimborsoDelGiorno(getTotaleRimborsoDelGiorno().add(rimborso.getIm_rimborso()));
				if(rimborso.getIm_quota_esente() != null)	
					setTotaleEsenteRimborsoDelGiorno(getTotaleEsenteRimborsoDelGiorno().add(rimborso.getIm_quota_esente()));			
			}	
		}	
	}

	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo della missione
	 */

	public void calcolaConsuntiviMissione() 
	{
		setIm_totale_missione(new BigDecimal(0));
		setIm_lordo_percepiente(new BigDecimal(0));	
		setIm_netto_pecepiente(new BigDecimal(0));

		if(getIm_spese() == null)
			setIm_spese(new BigDecimal(0));
		if(getIm_diaria_netto() == null)
			setIm_diaria_netto(new BigDecimal(0));
		if(getIm_diaria_lorda() == null)
			setIm_diaria_lorda(new BigDecimal(0));
		if(getIm_rimborso() == null)
			setIm_rimborso(new BigDecimal(0));

		if(isMissioneConCompenso())
		{
			if(isCompensoDaModificareInManuale())
			{
				setIm_totale_missione(new BigDecimal(0));
				setIm_lordo_percepiente(new BigDecimal(0));
				setIm_netto_pecepiente(new BigDecimal(0));
			}
			else
			{
				setIm_netto_pecepiente(getCompenso().getIm_netto_percipiente());
				setIm_lordo_percepiente(getCompenso().getIm_lordo_percipiente());
				setIm_totale_missione(getCompenso().getIm_totale_compenso());			
			}	
		}
		else	
		{
			setIm_totale_missione(getIm_spese().add(getIm_diaria_netto().add(getIm_rimborso())));
			setIm_totale_missione(getIm_totale_missione().setScale(2, BigDecimal.ROUND_HALF_UP));

			// 	In caso la missione non abbia il compenso i due importi seguenti sono uguali
			//	al totale della missione
			setIm_lordo_percepiente(getIm_totale_missione());
			setIm_netto_pecepiente(getIm_totale_missione());
		}
	}
	/**
	 * Il metodo calcola tutti gli importi necessari per il consuntivo spese della missione
	 */

	public void calcolaConsuntiviSpese() 
	{
		setIm_spese(new BigDecimal(0));				// Spese non anticipate
		setIm_spese_anticipate(new BigDecimal(0));

		if(getSpeseMissioneColl() == null)
			return;

		for ( Iterator i = getSpeseMissioneColl().iterator(); i.hasNext(); )
		{
			Missione_dettaglioBulk spesa = (Missione_dettaglioBulk)i.next();

			if(spesa.getIm_totale_spesa() != null)
			{
				if(spesa.isSpesaAnticipata())
				{
					//	L'importo della spesa anticipata e' uguale al campo im_spesa_euro questo perche'
					//	nel caso di spesa anticipata di trasporto non deve includere la maggiorazione
					setIm_spese_anticipate(getIm_spese_anticipate().add(spesa.getIm_spesa_euro()));
					setIm_spese_anticipate(getIm_spese_anticipate().setScale(2, BigDecimal.ROUND_HALF_UP));

					if(spesa.isTrasporto())
					{
						// 	La maggiorazione di una spesa anticipata deve essere
						//	sommata all'im_spesa (cioe' all'importo non anticipato)
						setIm_spese(getIm_spese().add(spesa.getIm_maggiorazione_euro()));
						setIm_spese(getIm_spese().setScale(2, BigDecimal.ROUND_HALF_UP));						
					}	
				}	
				else
				{
					setIm_spese(getIm_spese().add(spesa.getIm_totale_spesa()));
					setIm_spese(getIm_spese().setScale(2, BigDecimal.ROUND_HALF_UP));
				}	
			}	
		}	
	}
	/**
	 * Il  metodo calcola tutti gli importi necessari per il consuntivo spese della missione
	 * di un determinato giorno
	 */

	public void calcolaConsuntiviSpeseDelGiorno() 
	{
		setTotaleSpeseDelGiorno(new BigDecimal(0));

		if((getSpeseMissioneColl() == null) || (getGiorno_consuntivo() == null))
			return;

		for ( Iterator i = getSpeseMissioneColl().iterator(); i.hasNext(); )
		{
			Missione_dettaglioBulk spesa = (Missione_dettaglioBulk)i.next();

			if((getGiorno_consuntivo().equals(spesa.getDt_inizio_tappa())) && 
					(spesa.getIm_totale_spesa() != null))
				setTotaleSpeseDelGiorno(getTotaleSpeseDelGiorno().add(spesa.getIm_totale_spesa()));
		}	
	}
	/**
	 * Il metodo rimuove tutti i dettagli di diaria dalla relativa collection ed inizializza
	 * gli importi della missione relativi alla diaria
	 */

	public void cancellaDiaria() 
	{
		if(getDiariaMissioneColl() == null || getDiariaMissioneColl().isEmpty())
			return;

		setDiariaMissioneColl(new BulkList());

		setIm_diaria_lorda(new BigDecimal(0));
		setIm_diaria_netto(new BigDecimal(0));
		setIm_quota_esente(new BigDecimal(0));	
	}
	/**
	 * Il metodo rimuove tutti i dettagli di diaria dalla relativa collection ed inizializza
	 * gli importi della missione relativi alla diaria
	 */

	public void cancellaRimborso() 
	{
		if(getRimborsoMissioneColl() == null || getRimborsoMissioneColl().isEmpty())
			return;

		setRimborsoMissioneColl(new BulkList());

		setIm_rimborso(new BigDecimal(0));
		setIm_quota_esente(new BigDecimal(0));	
	}
	/**
	 * Il metodo rimuove tutti i dettagli di spesa dalla relativa collection
	 */
	public void cancellaSpese() 
	{
		if((getSpeseMissioneColl() == null) || (getSpeseMissioneColl().isEmpty()))
			return;

		for ( Iterator i =  getSpeseMissioneColl().iterator(); i.hasNext(); )
			removeFromSpeseMissioneColl(0);
	}
	/**
	 * Il metodo rimuove tuttw le tappe dalla relativa collection
	 */
	public void cancellaTappe() 
	{
		if((getTappeMissioneColl() == null) || (getTappeMissioneColl().isEmpty()))
			return;

		for ( Iterator i =  getTappeMissioneColl().iterator(); i.hasNext(); )
			removeFromTappeMissioneColl(0);
	}
	/**
	 * Il metodo verifica la validità del periodo (data e ora) di inizio e fine missione.
	 * - la data inzio deve essere antecedente a quella di fine
	 * - la data inzio deve essere diversa da quella di fine
	 * - la durata della missione non puo' essere superiore ai sei mesi
	 * - validazione degli esercizi delle date di inizio e fine missione (isPeriodoMissioneInEserciziValidi)
	 */

	public void checkValiditaInizioFineMissione() throws it.cnr.jada.action.MessageToUser
	{
		if((getDt_inizio_missione() == null) || (getDt_fine_missione() == null))
			throw new it.cnr.jada.action.MessageToUser( "Valorizzare le date di inizio e fine missione !" ); 		

		isPeriodoMissioneInEserciziValidi();

		if(getDt_inizio_missione().after(getDt_fine_missione()))
			throw new it.cnr.jada.action.MessageToUser( "La Data/Ora inizio missione non deve essere successiva alla Data/Ora fine missione !" ); 		

		if(getDt_inizio_missione().equals(getDt_fine_missione()))
			throw new it.cnr.jada.action.MessageToUser( "La Data/Ora inizio missione non deve essere uguale alla Data/Ora fine missione !" );

		GregorianCalendar gdf = getGregorianCalendar(getDt_fine_missione());
		GregorianCalendar gdi = getGregorianCalendar(getDt_inizio_missione());
		gdf.add(java.util.Calendar.DAY_OF_YEAR, -240);
		if(gdf.after(gdi))
			throw new it.cnr.jada.action.MessageToUser( "La durata della missione non puo' essere superiore ai 240 giorni !" );	
	}
	/**
	 * Il metodo genera le tappe non configurate facendole ereditare dalla tappa giorno precedente 
	 * @throws ParseException 
	 */

	public void generaTappeNonConfigurate(java.sql.Timestamp data_fine_diaria_miss_estero) throws ValidationException, ParseException
	{
		if(	(getCollectionGiorni() == null) || (getCollectionGiorni().isEmpty()) ||
				(getTappeMissioneColl() == null) || (getTappeMissioneColl().isEmpty()))
			return;

		// Riordino le tappe configurate per giorno (dt_inizio_tappa)
		ordinaTappePerDataInizioTappa();		

		if(getCollectionGiorni().size() == getTappeMissioneColl().size())
			return;

		// Verifico che la tappa del primo giorno della missione sia stata configurata
		isTappaPrimoGiornoConfigurata();

		Missione_tappaBulk tappa=new Missione_tappaBulk();
		Missione_tappaBulk tappaDaClonare=new Missione_tappaBulk();

		// Verifico se sono state configurate le tappe per tutti i giorni della missione		
		int index;
		boolean found;
		// Ciclo sulla collection dei giorni della missione
		for ( Iterator i = getCollectionGiorni().iterator(); i.hasNext(); )
		{
			// gg = giorno da verificare se e' stato configurato
			java.sql.Timestamp gg = (java.sql.Timestamp) i.next();
			GregorianCalendar ggGreg = (GregorianCalendar)getGregorianCalendar(gg).clone();
			if(!gg.equals(getCollectionGiorni().get(0)))
			{
				GregorianCalendar ggDaClonareGreg = (GregorianCalendar)ggGreg.clone();		
				ggDaClonareGreg.set(GregorianCalendar.HOUR_OF_DAY, 0);
				ggDaClonareGreg.set(GregorianCalendar.MINUTE, 0);
				ggDaClonareGreg.add(java.util.Calendar.DATE, -1);		// Giorno precedente	
				java.sql.Timestamp ggDaClonare = new java.sql.Timestamp(ggDaClonareGreg.getTime().getTime());		

				index=0;
				found=false;

				// ciclo sulla collection delle tappe configurate
				while(index<=getTappeMissioneColl().size()-1 && found==false)
				{
					tappa = (Missione_tappaBulk)getTappeMissioneColl().get(index);
					GregorianCalendar inizioTappaGreg = (GregorianCalendar)getGregorianCalendar(tappa.getDt_inizio_tappa()).clone();
					inizioTappaGreg.set(GregorianCalendar.HOUR_OF_DAY, 0);
					inizioTappaGreg.set(GregorianCalendar.MINUTE, 0);

					if(inizioTappaGreg.equals(ggDaClonareGreg))
						tappaDaClonare = tappa;			// Tappa da cui copiare

					if(inizioTappaGreg.equals(ggGreg))
						found=true;						// OK! la tappa del giorno 'gg' e' stata configurata	 	  	
					else
						index++;								
				}		

				if(!found)
				{
					// La tappa del giorno 'gg' non e' stata configurata
					// quindi la copio da 'tappaDaClonare'
					Missione_tappaBulk newTappa = new Missione_tappaBulk();
					newTappa = (Missione_tappaBulk)tappaDaClonare.clone();
					newTappa.setDt_inizio_tappa((java.sql.Timestamp)gg.clone());
					/*
				if (newTappa.getFl_comune_estero().booleanValue())
				{
					//java.sql.Timestamp data_fine_diaria_miss_estero;
					//Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
				    //data_fine_diaria_miss_estero = sess.getDt01(userContext, new Integer(0), "*", "DIARIA_MISS_ESTERO", "DATA_FINE");
					//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
					//data_fine_diaria_miss_estero =  new java.sql.Timestamp(sdf.parse("30/05/2010").getTime());
						if ((newTappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero)>0))
						{
							newTappa.setFl_no_diaria(new Boolean(true));
						}
				}	
					 */
					newTappa.setCrudStatus(OggettoBulk.TO_BE_CREATED);
					getTappeMissioneColl().add(newTappa);
					ordinaTappePerDataInizioTappa();				
				}
			}
		}
	}
	/**
	 * Il metodo gestisce la sincronizzazione delle scadenze elaborate dalla missione.
	 * La scadenza precedentemente selezionata (che ho slegato):
	 * - se appartiene alla stessa obligazione della nuova scadenza che si sta associando potrebbe 
	 *	  essere stata modificata dall'utente in fase di aggiornamento manuale. Ecco perche' occorre
	 *   risincronizzarla
	 * - se creata da missione ma mai salvata dovra' comunque essere inserita in tabella
	 *   con "im_associato_doc_amm" = 0
	 * - se precedentemente associata alla missione salvata devo aggiornarne "im_associato_doc_amm" = 0
	 *	- se sganciata perche' cancellata non devo aggiornarne l'"im_associato_doc_amm"
	 */

	public void gestisciCambioSelezioneScadenza(Obbligazione_scadenzarioBulk newScadenza)
	{
		//	Sincronizzo le scadenze che ho tra le cancellate perche' l'utente
		//	potrebbe averle modificate
		sincronizzaScadenzeCancellate(newScadenza);

		// La missione non aveva associato alcuna scadenza
		if(!isMissioneConObbligazione())		
			return;

		// La scadenza associata alla missione e' sempre la stessa
		if(newScadenza.equalsByPrimaryKey(getObbligazione_scadenzario()))
			return;

		// 	Se scopro, che la scadenza da sostituire appartiene alla stessa obbligazione
		//	della nuova scadenza, devo sincronizzarla perchè l'utente prima di riportare
		//	quella nuova potrebbe avermi modificato quella da sostituire (cioe' quella attulamente 
		//	associata).
		if(newScadenza.getObbligazione().equalsByPrimaryKey(getObbligazione_scadenzario().getObbligazione()))
		{	
			boolean trovata=false;
			BulkList coll = newScadenza.getObbligazione().getObbligazione_scadenzarioColl();
			if(coll != null && !coll.isEmpty())	// 	Se coll e' vuota significa che non arrivo da obbligazione
			{									//	ma dal filtro di ricerca delle obbligazioni
				for ( Iterator i = coll.iterator(); i.hasNext(); )
				{
					Obbligazione_scadenzarioBulk aScadenza = (Obbligazione_scadenzarioBulk)i.next();
					if(aScadenza.equalsByPrimaryKey(getObbligazione_scadenzario()))
					{
						setObbligazione_scadenzario(aScadenza);
						trovata = true;
					}	
				}
				if(!trovata)
					return;		//	significa che la scadenza che sto sostituendo
				//	e' stata cancellata
			}
		}	

		addToDocumentiContabiliCancellati(getObbligazione_scadenzario());

		if(getDocumentiContabiliCancellati() == null || getDocumentiContabiliCancellati().isEmpty())
			return;

		//	Se la scadenza appena associata alla missione l'avevo precedente sganciata dalla
		//	missione e quindi inserita nella collection dei Cancellati, devo rimuoverla da
		//	tale collection.
		for ( Iterator i = ((Vector)getDocumentiContabiliCancellati().clone()).iterator(); i.hasNext(); )	
		{
			Obbligazione_scadenzarioBulk aScadDaSganciare = (Obbligazione_scadenzarioBulk)i.next();
			if(aScadDaSganciare.equalsByPrimaryKey(newScadenza))
			{
				getDocumentiContabiliCancellati().remove(aScadDaSganciare);
				break;
			}	
		}	
	}

	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */

	public it.cnr.contab.docamm00.docs.bulk.AccertamentiTable getAccertamentiHash() 
	{
		return null;
	}
	/**
	 * Il metodo ritorna il valore dell'anno solare in cui si sta lavorando 
	 * (serve per chiusura)
	 */
	public int getAnnoSolare() {
		return annoSolare;
	}
	/**
	 * Il metodo ritorna l'eventuale anticipo associato alla missione
	 */
	public AnticipoBulk getAnticipo() {
		return anticipo;
	}
	/**
	 * Il metodo ritorna l'eventuale anticipo associato ad una missione gia' esistente 
	 * che si sta per modificare. Tale anticipo viene salvato in questa proprietà perche'
	 * se l'utente dovesse associare un anticipo diverso occorre aggiornare il vecchio anticipo
	 * (riplulire i campi relativi alla sua associazione con la missione)
	 */
	public AnticipoBulk getAnticipoClone() {
		return anticipoClone;
	}
	/**
	 * Il metodo ritorna la Banca
	 */
	public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
		return banca;
	}
	/**
	 * Restituisce la lista delle tappe e dei dettagli di diaria/spesa necessari al salvataggio
	 * della missione
	 */

	public BulkCollection[] getBulkLists() 
	{
		// Metti solo le liste di oggetti che devono essere resi persistenti

		return new it.cnr.jada.bulk.BulkCollection[] { tappeMissioneColl, speseMissioneColl, diariaMissioneColl, rimborsoMissioneColl };
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_cds_anticipo'
	 */
	public java.lang.String getCd_cds_anticipo() {
		it.cnr.contab.missioni00.docs.bulk.AnticipoBulk anticipo = this.getAnticipo();
		if (anticipo == null)
			return null;
		return anticipo.getCd_cds();
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_cds_obbligazione'
	 */
	public java.lang.String getCd_cds_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
		if (obbligazione_scadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_modalita_pag'
	 */
	public java.lang.String getCd_modalita_pag() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
		if (modalita_pagamento == null)
			return null;
		return modalita_pagamento.getCd_modalita_pag();
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_termini_pag'
	 */
	public java.lang.String getCd_termini_pag() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
		if (termini_pagamento == null)
			return null;
		return termini_pagamento.getCd_termini_pag();
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 * Tale metodo ritorna il tipo del documento amministrativo (MISSIONE)
	 */

	public java.lang.String getCd_tipo_doc_amm() 
	{
		return it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_MISSIONE;
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_tipo_missione'
	 */
	public java.lang.String getCd_tipo_missione() {
		it.cnr.contab.missioni00.tabrif.bulk.Tipo_missioneBulk tipo_missione = this.getTipo_missione();
		if (tipo_missione == null)
			return null;
		return tipo_missione.getCd_tipo_missione();
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_tipo_rapporto'
	 */
	public java.lang.String getCd_tipo_rapporto() {
		it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto = this.getTipo_rapporto();
		if (tipo_rapporto == null)
			return null;
		return tipo_rapporto.getCd_tipo_rapporto();
	}
	/**
	 *  Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 */

	public java.lang.String getCd_uo() 
	{
		return getCd_unita_organizzativa();
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'cd_uo_anticipo'
	 */
	public java.lang.String getCd_uo_anticipo() {
		it.cnr.contab.missioni00.docs.bulk.AnticipoBulk anticipo = this.getAnticipo();
		if (anticipo == null)
			return null;
		return anticipo.getCd_unita_organizzativa();
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */

	public java.lang.Class getChildClass() 
	{
		return null;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */

	public List getChildren() 
	{
		return null;
	}

	/**
	 * Il metodo restituisce il valore dell'attributo 'collectionGiorni'
	 * Tale proprietà contiene tutti i singoli giorni tra l'inizio e la fine della missione
	 */
	public java.util.Vector getCollectionGiorni() {
		return collectionGiorni;
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'collectionGiorniSpese'
	 * Tale proprietà contiene tutti i singoli giorni tra l'inizio e la fine della missione,
	 * essa viene usata per la selezione del giorno della spesa
	 */
	public java.util.List getCollectionGiorniSpese() 
	{
		if((getCollectionGiorni() == null) || (getCollectionGiorni().size() == 0))
			return getCollectionGiorni();

		if(getTappeMissioneHash() == null)
			return getCollectionGiorni();

		// Puo' capitare che non esiste la tappa per l'ultimo giorno di missione
		// (vedi commento del metodo "verificaUltimaTappa")
		java.sql.Timestamp ultimoGiorno = (java.sql.Timestamp)getCollectionGiorni().lastElement();
		if(getTappeMissioneHash().containsKey(ultimoGiorno))
			return getCollectionGiorni();
		else
			return new Vector(getCollectionGiorni().subList(0, getCollectionGiorni().size()-1));
	}
	/**
	 * Il metodo restituisce il valore dell'attributo 'collectionGiorniSpeseSelezionati' che indica
	 * i giorni selezionati per la creazione di una spesa
	 */
	public java.util.List getCollectionGiorniSpeseSelezionati() {
		return collectionGiorniSpeseSelezionati;
	}
	/**
	 * Tale metodo restituisce il compenso eventualmente associato alla missione
	 */
	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk getCompenso() {
		return compenso;
	}
	/**
	 * Il metodo restituisce il Timestamp ricevuto come parametro come istanza di Calendar 
	 * inizializzandone ore, minuti, secondi a zero
	 */ 
	public java.util.Calendar getDateCalendar(java.sql.Timestamp date) 
	{
		if (date == null)
			date = new java.sql.Timestamp(System.currentTimeMillis());

		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(java.util.Calendar.HOUR, 0);
		calendar.set(java.util.Calendar.MINUTE, 0);
		calendar.set(java.util.Calendar.SECOND, 0);
		calendar.set(java.util.Calendar.MILLISECOND, 0);
		calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		return calendar;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
	 */

	public it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi() 
	{
		return deferredSaldi;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
	 */

	public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) 
	{
		if (docCont != null && deferredSaldi != null)
		{
			for (java.util.Iterator i = deferredSaldi.keySet().iterator(); i.hasNext();) 
			{
				IDocumentoContabileBulk key = (IDocumentoContabileBulk)i.next();
				if (((OggettoBulk)docCont).equalsByPrimaryKey((OggettoBulk)key))
					return key;
			}
		}	
		return null;	
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 * Il metodo ritorna la descrizione della missione
	 */
	public java.lang.String getDescrizione_spesa() 
	{
		return getDs_missione();
	}
	/**
	 * Il  metodo ritorna il valore dell'attributo 'dettagliCancellati'
	 */
	public java.util.Vector getDettagliCancellati() {
		return dettagliCancellati;
	} 
	/**
	 * Il metodo ritorna una BulkList contenente tutti i dettagli della missione (spese + diarie)
	 */

	@JsonIgnore
	public BulkList<Missione_dettaglioBulk> getDettagliMissioneColl() 
	{
		BulkList<Missione_dettaglioBulk> listaDettagli = new BulkList<Missione_dettaglioBulk>();

		if(getSpeseMissioneColl() != null && !getSpeseMissioneColl().isEmpty())
			listaDettagli.addAll(getSpeseMissioneColl());

		if(getDiariaMissioneColl() != null && ! getDiariaMissioneColl().isEmpty())
			listaDettagli.addAll(getDiariaMissioneColl());

		if(getRimborsoMissioneColl() != null && ! getRimborsoMissioneColl().isEmpty())
			listaDettagli.addAll(getRimborsoMissioneColl());

		return listaDettagli;
	}
	/**
	 * Il metodo ritorna una BulkList contenente tutti i dettagli di diaria della missione
	 */

	public it.cnr.jada.bulk.BulkList<Missione_dettaglioBulk> getDiariaMissioneColl() {
		return diariaMissioneColl;
	}
	/**
	 * Il metodo ritorna una BulkList contenente tutti i dettagli dei rimborsi della missione
	 */

	public it.cnr.jada.bulk.BulkList<Missione_dettaglioBulk> getRimborsoMissioneColl() {
		return rimborsoMissioneColl;
	}
	/**
	 * Il  metodo ritorna il valore dell'attributo 'documentiContabiliCancellati'
	 */

	public java.util.Vector getDocumentiContabiliCancellati() { 
		return documentiContabiliCancellati;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */
	public Class getDocumentoAmministrativoClassForDelete() 
	{
		return MissioneBulk.class;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */
	public Class getDocumentoContabileClassForDelete() 
	{
		return it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk.class;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 * Ritorna il valore dell'attributo dt_a_competenza_coge
	 */
	public java.sql.Timestamp getDt_a_competenza_coge() {
		return null;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 * Ritorna il valore dell'attributo dt_da_competenza_coge
	 */
	public java.sql.Timestamp getDt_da_competenza_coge() {
		return null;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'esercizio_anticipo'
	 */
	public java.lang.Integer getEsercizio_anticipo() {
		it.cnr.contab.missioni00.docs.bulk.AnticipoBulk anticipo = this.getAnticipo();
		if (anticipo == null)
			return null;
		return anticipo.getEsercizio();
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'esercizio_obbligazione'
	 */
	public java.lang.Integer getEsercizio_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
		if (obbligazione_scadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getEsercizio();
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'esercizioScrivania'
	 */
	public int getEsercizioScrivania() {
		return esercizioScrivania;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'giorno_consuntivo'
	 */
	public java.sql.Timestamp getGiorno_consuntivo() {
		return giorno_consuntivo;
	}
	/**
	 * Dato un Timestamp, tale metodo ritorna la relativa istanza di GregorianCalendar 
	 */
	public GregorianCalendar getGregorianCalendar(java.sql.Timestamp ts) 
	{
		if(ts == null)
			return null;
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		gc.setTime(ts);

		return(gc);
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 * Il metodo ritorna l'importo della missione
	 */
	public java.math.BigDecimal getImporto_netto_spesa() 
	{
		return getImporto_spesa();
	}
	/**
	 * Il metodo inizializza l'importo della scadenza della obbligazione.
	 * Se la missione non ha anticipo l'importo della scadenza e' pari a quello della missione.
	 * Se la missione ha un anticipo l'importo della scadenza è pari  a quello della missione meno
	 * quello dell'anticipo
	 */

	public java.math.BigDecimal getImporto_scadenza_obbligazione() 
	{
		if(getIm_totale_missione() == null)
			setIm_totale_missione(new BigDecimal(0));

		if(!isMissioneConAnticipo())
			return getIm_totale_missione();
		else
			return getIm_totale_missione().subtract(getAnticipo().getIm_anticipo());	
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 * Il metodo ritorna l'importo della missione
	 */
	public java.math.BigDecimal getImporto_spesa() 
	{
		java.math.BigDecimal imSpesa = getIm_totale_missione();

		if(isMissioneConAnticipo())
		{
			java.math.BigDecimal imAnticipo = getAnticipo().getIm_anticipo();
			imSpesa = imSpesa.add(imAnticipo.negate());
		}

		return imSpesa;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 * Il metodo ritorna l'importo dell'anticipo
	 */

	public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) 
	{
		if (importo == null) 
			return null;
		return importo.negate();
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'inquadramenti'
	 */
	public java.util.Collection getInquadramenti() {
		return inquadramenti;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */
	public String getManagerName() 
	{
		return "CRUDMissioneBP";
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */
	public java.lang.String getManagerOptions() 
	{
		// NON CANCELLARE QUESTO COMMENTO: E' DA ABILITARE AL POSTO DEL RESTO NEL CASO DI APERTURA
		// IN MODIFICA
		//return "MTh";

		return "VTh";
	}
	/**
	 * Il metodo ritorna la missione iniziale, cioe' la missione prima di qualsiasi modifica
	 */
	public MissioneBulk getMissioneIniziale() {
		return missioneIniziale;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'modalita'
	 */
	public java.util.Collection getModalita() {
		return modalita;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'modalita_pagamento'
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
		return modalita_pagamento;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'obbligazione_scadenzario'
	 */
	public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazione_scadenzario() {
		return obbligazione_scadenzario;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'obbligazione_scadenzarioClone'
	 */
	public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazione_scadenzarioClone() {
		return obbligazione_scadenzarioClone;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */
	public ObbligazioniTable getObbligazioniHash() 
	{
		ObbligazioniTable ot = new ObbligazioniTable();
		if (getObbligazione_scadenzario() != null)
			ot.put(getObbligazione_scadenzario(), new Vector());
		return ot;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'pg_anticipo'
	 */
	public java.lang.Long getPg_anticipo() {
		it.cnr.contab.missioni00.docs.bulk.AnticipoBulk anticipo = this.getAnticipo();
		if (anticipo == null)
			return null;
		return anticipo.getPg_anticipo();
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'pg_banca'
	 */
	public java.lang.Long getPg_banca() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
		if (banca == null)
			return null;
		return banca.getPg_banca();
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 * Ritorna il progressivo della missione
	 */

	public java.lang.Long getPg_doc_amm() 
	{
		return getPg_missione();
	}
	/**
	 * Il metodo ritorna l'esercizio originale dell'obbligazione la cui scadenza e' associata alla missione
	 */
	public Integer getEsercizio_ori_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
		if (obbligazione_scadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getEsercizio_originale();
	}
	/**
	 * Il metodo ritorna il progressivo dell'obbligazione la cui scadenza e' associata alla missione
	 */
	public java.lang.Long getPg_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
		if (obbligazione_scadenzario == null)
			return null;
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
		if (obbligazione == null)
			return null;
		return obbligazione.getPg_obbligazione();
	}
	/**
	 * Il metodo ritorna il progressivo della scadenza e' associata alla missione
	 */
	public java.lang.Long getPg_obbligazione_scadenzario() {
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
		if (obbligazione_scadenzario == null)
			return null;
		return obbligazione_scadenzario.getPg_obbligazione_scadenzario();
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'pg_rif_inquadramento'
	 */
	public java.lang.Long getPg_rif_inquadramento() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk rif_inquadramento = this.getRif_inquadramento();
		if (rif_inquadramento == null)
			return null;
		return rif_inquadramento.getPg_rif_inquadramento();
	}
	/**
	 * Il metodo ritorna il primo giorno della lista dei giorni selezionati per l'inserimento delle spese
	 */
	public java.sql.Timestamp getPrimoGiornoSpesaSelezionato() 
	{
		if(getCollectionGiorniSpeseSelezionati()==null || getCollectionGiorniSpeseSelezionati().isEmpty())
			return null;

		return (java.sql.Timestamp)getCollectionGiorniSpeseSelezionati().get(0);		
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'rif_inquadramento'
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk getRif_inquadramento() {
		return rif_inquadramento;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'riportata'
	 */
	public java.lang.String getRiportata() {
		return riportata;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (02/11/2004 14.30.59)
	 * @return java.lang.String
	 */
	public java.lang.String getRiportataInScrivania() {
		return riportataInScrivania;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'speseMissioneColl'
	 */
	public it.cnr.jada.bulk.BulkList<Missione_dettaglioBulk> getSpeseMissioneColl() {
		return speseMissioneColl;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione degli stati Cofi della missione
	 */
	public java.util.Dictionary getStato_cofiKeys() 
	{
		return STATO_COFI;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione degli stati Cofi della missione
	 */
	public Dictionary getStato_cofiKeysForSearch() 
	{
		it.cnr.jada.util.OrderedHashtable d = (it.cnr.jada.util.OrderedHashtable)getStato_cofiKeys();
		if (d == null) return null;

		it.cnr.jada.util.OrderedHashtable clone = (it.cnr.jada.util.OrderedHashtable)d.clone();
		clone.remove(STATO_INIZIALE_COFI);
		return clone;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione degli stati della Registrazione nel Fondo
	 * Economale della missione
	 */

	public Dictionary getStato_pagamento_fondo_ecoKeys() {

		if (getStato_pagamento_fondo_eco() != null &&
				REGISTRATO_IN_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco())) {
			return STATO_FONDO_ECO;
		}

		OrderedHashtable oh = (OrderedHashtable)((OrderedHashtable)STATO_FONDO_ECO).clone();
		oh.remove(REGISTRATO_IN_FONDO_ECO);
		return oh;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione degli stati della Registrazione nel Fondo
	 * Economale della missione
	 */

	public Dictionary getStato_pagamento_fondo_ecoKeysForSearch() {

		return STATO_FONDO_ECO;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'tappeMissioneColl'
	 */
	public it.cnr.jada.bulk.BulkList<Missione_tappaBulk> getTappeMissioneColl() {
		return tappeMissioneColl;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'tappeMissioneHash'
	 */
	public it.cnr.jada.bulk.PrimaryKeyHashtable getTappeMissioneHash() {
		return tappeMissioneHash;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'termini'
	 */
	public java.util.Collection getTermini() {
		return termini;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'termini_pagamento'
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento() {
		return termini_pagamento;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'terzo'
	 */
	@JsonIgnore
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo()
	{
		if (getV_terzo()==null)
			return null;
		return getV_terzo().getTerzo();
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 * Il  metodo ritorna il terzo della missione
	 */
	@JsonIgnore
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa()
	{
		return getTerzo();
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione del tipo anagrafico
	 */
	public java.util.Dictionary getTi_anagraficoKeys() {
		return ti_anagraficoKeys;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione di ti_associato_manrev
	 */
	public java.util.Dictionary getTi_associato_manrevKeys() 
	{
		return TI_ASSOCIATO_MANREV;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione del tipo istituzionale/commerciale
	 */
	public java.util.Dictionary getTi_istituz_commKeys() {
		return ti_istituz_commKeys;
	}
	/**
	 * Il metodo restituisce il dictionary per la gestione del tipo salvataggio
	 */
	public java.util.Dictionary getTi_salvataggioKeys() {
		return ti_salvataggioKeys;
	}
	/**
	 * Il metodo restituisce l'attributo 'tipi_missione'
	 */
	public java.util.Collection getTipi_missione() {
		return tipi_missione;
	}
	/**
	 * Il metodo restituisce l'attributo 'tipi_rapporto'
	 */
	public java.util.Collection getTipi_rapporto() {
		return tipi_rapporto;
	}
	/**
	 * Il metodo restituisce l'attributo 'tipi_trattamento'
	 */
	public java.util.Collection getTipi_trattamento() {
		return tipi_trattamento;
	}
	/**
	 * Il metodo restituisce l'attributo 'tipo_missione'
	 */
	public it.cnr.contab.missioni00.tabrif.bulk.Tipo_missioneBulk getTipo_missione() {
		return tipo_missione;
	}
	/**
	 * Il metodo restituisce l'attributo 'tipo_rapporto'
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipo_rapporto() {
		return tipo_rapporto;
	}
	/**
	 * Il metodo restituisce l'attributo 'tipo_trattamento'
	 */
	public it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk getTipo_trattamento() {
		return tipo_trattamento;
	}
	/**
	 * Il metodo restituisce l'attributo 'totaleEsenteDiariaDelGiorno'
	 */
	public java.math.BigDecimal getTotaleEsenteDiariaDelGiorno() {
		return totaleEsenteDiariaDelGiorno;
	}
	/**
	 * Il metodo restituisce l'attributo 'totaleNettoDiariaDelGiorno'
	 */
	public java.math.BigDecimal getTotaleNettoDiariaDelGiorno() {
		return totaleNettoDiariaDelGiorno;
	}
	/**
	 * Il metodo restituisce l'attributo 'totaleEsenteRimborsoDelGiorno'
	 */
	public java.math.BigDecimal getTotaleEsenteRimborsoDelGiorno() {
		return totaleEsenteRimborsoDelGiorno;
	}
	/**
	 * Il metodo restituisce l'attributo 'totaleRimborsoDelGiorno'
	 */
	public java.math.BigDecimal getTotaleRimborsoDelGiorno() {
		return totaleRimborsoDelGiorno;
	}
	/**
	 * Il metodo restituisce l'attributo 'totaleSpeseDelGiorno'
	 */
	public java.math.BigDecimal getTotaleSpeseDelGiorno() {
		return totaleSpeseDelGiorno;
	}
	/**
	 * Il metodo restituisce l'attributo 'v_terzo'
	 */
	public it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk getV_terzo() {
		return v_terzo;
	}
	/**
	 * Il  metodo gestisce la creazione di una spesa su piu' giorni.
	 * Se l'utente ha selezionato 'n' giorni per l'inserimento di piu' spese,
	 * al termine della conferma della spesa del primo giorno l'applicazione deve creare
	 * n-1 istanze di spesa uguali a quella processata (del primo giorno) 
	 * e le aggiunge alla collection dei dettagli assegnando ogni volta un giorno diverso
	 *	e un progressivo diverso
	 */

	public void imputaSpesaSuPiuGiorni(Missione_dettaglioBulk primaSpesa) 
	{
		java.sql.Timestamp aGiorno = null;
		for (java.util.Iterator i = getCollectionGiorniSpeseSelezionati().iterator(); i.hasNext();)
		{
			aGiorno = (java.sql.Timestamp)i.next();
			if(!aGiorno.equals(primaSpesa.getDt_inizio_tappa()))
			{
				Missione_dettaglioBulk nuovaSpesa = new Missione_dettaglioBulk();
				nuovaSpesa = (Missione_dettaglioBulk)primaSpesa.clone();
				nuovaSpesa.setDt_inizio_tappa(aGiorno);				
				nuovaSpesa.setPg_riga(assegnaProgressivoSpesa(nuovaSpesa));
				getSpeseMissioneColl().add(nuovaSpesa);
			}	
		}
	}
	/**
	 * Il metodo inzializza la missione da modificare
	 */
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) 
	{
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
		unita_organizzativa =	it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
		setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());	
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

		setTi_provvisorio_definitivo(MissioneBulk.SALVA_TEMPORANEO);
		setV_terzo(new V_terzo_per_compensoBulk());
		setTi_anagrafico(ANAG_DIPENDENTE);
		setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());

		setStato_cofi(STATO_INIZIALE_COFI);
		setStato_coge(STATO_INIZIALE_COGE);
		setStato_coan(STATO_INIZIALE_COAN);
		setFl_associato_compenso(new Boolean(false));	
		setTi_associato_manrev(STATO_INIZIALE_MANREV);
		setStato_pagamento_fondo_eco(NO_FONDO_ECO);
		setStato_liquidazione(SOSP);
		//	La data di registrazione la inizializzo sulla Component

		return this;
	}
	/**
	 * Il metodo inzializza la missione da cercare
	 */
	public OggettoBulk initializeForSearch(CRUDBP bp, ActionContext context) 
	{
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
		unita_organizzativa =	it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
		setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());	
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

		setV_terzo(new V_terzo_per_compensoBulk());
		setTi_anagrafico(null);
		setTi_istituz_commerc(null);

		if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) 
		{
			setStato_cofi(STATO_CONTABILIZZATO_COFI);
			setStato_pagamento_fondo_eco(FONDO_ECO);
			setStato_liquidazione(LIQ);
			if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(unita_organizzativa.getCd_tipo_unita()))
				setCd_unita_organizzativa(null);
		}

		return this;
	}
	/**
	 * Il  metodo inizializza a zero gli importi del giorno selezionato nel Tab Consuntivo
	 */
	public void inizializzaGiornoConsuntivo()
	{
		setTotaleSpeseDelGiorno(new BigDecimal(0));
		setTotaleNettoDiariaDelGiorno(new BigDecimal(0));
		setTotaleEsenteDiariaDelGiorno(new BigDecimal(0));
		setTotaleRimborsoDelGiorno(new BigDecimal(0));
		setTotaleEsenteRimborsoDelGiorno(new BigDecimal(0));
	}
	/**
	 * Il metodo inizializza gli attributi relativi al terzo con valori NULL
	 */
	public void inizializzaTerzo()
	{
		V_terzo_per_compensoBulk vTerzo = new V_terzo_per_compensoBulk();
		vTerzo.setTi_dipendente_altro(getTi_anagrafico());

		setV_terzo(vTerzo);
		setCd_terzo(null);
		setNome(null);
		setCognome(null);
		setRagione_sociale(null);
		setCodice_fiscale(null);
		setPartita_iva(null);
		setTermini(null);
		setTermini_pagamento(null);			
		setModalita(null);
		setModalita_pagamento(null);
		setBanca(null);
		setInquadramenti(null);
		setRif_inquadramento(null);
		setTipi_rapporto(null);
		setTipo_rapporto(null);
		setTipi_trattamento(null);
		setTipo_trattamento(null);
	}
	/**
	 * Il metodo stabilisce se i dati relativi alla banca sono abilitati.
	 * Abilitati :
	 * - se la modalità di pagamento e' stata selezionata
	 * - se l'eventuale compenso non e' stato pagato
	 * - la missione non e' pagata (isEditable)
	 * - se l'esercizio del documento e quello scrivania di scrivania sono diversi da quello solare 
	 *   l' obbligazione non deve essere stata riportata  (isEditable)
	 */
	public boolean isAbledToInsertBank() 
	{
		return !(getV_terzo() != null && 
				getV_terzo().getCrudStatus() == OggettoBulk.NORMAL &&
				getModalita_pagamento() != null &&
				(getCompenso()==null || !getCompenso().isPagato()) &&
				isEditable());
	}


	/**
	 * Il metodo ritorna TRUE se la missione e' stata cancellata logicamente
	 */
	public boolean isAnnullato() 
	{
		return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
	}
	/**
	 * Il metodo ritorna TRUE se e' cambiato l'importo dell'anticipo
	 */

	public boolean isAnticipoModificato() 
	{
		if(getMissioneIniziale() == null)
			return false;

		if(!getMissioneIniziale().isMissioneConAnticipo() && !isMissioneConAnticipo())
			return false;

		if(!getMissioneIniziale().isMissioneConAnticipo() && isMissioneConAnticipo())
			return true;

		if(getMissioneIniziale().isMissioneConAnticipo() && !isMissioneConAnticipo())
			return true;

		if(getMissioneIniziale().getAnticipo().getIm_anticipo().compareTo(getAnticipo().getIm_anticipo()) != 0)
			return true;

		return false;
	}
	/**
	 * Il metodo ritorna TRUE se l'anticipo e' stato rimborsato 
	 */

	public boolean isAnticipoRimborsato() 
	{
		return(isMissioneConAnticipo() && getAnticipo().hasRimborso());
	}
	/**
	 * Il metodo ritorna TRUE se la missione non e' pagata via fondo economale
	 */
	public boolean isByFondoEconomale() {

		return	!NO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco());
	}
	/**
	 * Il metodo ritorna TRUE se la missione e' stata cancellata logicamente
	 */
	public boolean isCancellataLogicamente() 
	{
		return getDt_cancellazione() != null;
	}
	/**
	 * Il metodo stabilisce se le modifiche portate alla missione implicano la modifica del compenso
	 * Se uno dei seguenti attributi e' cambiato allora al salvataggio della missione 
	 * l'applicazione deve modificare e salvare il compenso senza doverlo aprire
	 */

	public boolean isCompensoDaModificareInAutomatico() 
	{
		// Inizializzo l'unico attributo che potrebbe essere nullo
		if(getTermini_pagamento() == null)
			setTermini_pagamento(new Rif_termini_pagamentoBulk());
		Optional.ofNullable(missioneIniziale).
		filter(map -> map.getTermini_pagamento() == null).
		ifPresent(map -> map.setTermini_pagamento(new Rif_termini_pagamentoBulk()));

		if(	!Optional.ofNullable(getModalita_pagamento()).orElse(new Rif_modalita_pagamentoBulk()).equalsByPrimaryKey(Optional.ofNullable(missioneIniziale).map(MissioneBulk::getModalita_pagamento).orElse(null)) ||
				!Optional.ofNullable(getBanca()).orElse(new BancaBulk()).equalsByPrimaryKey(Optional.ofNullable(missioneIniziale).map(MissioneBulk::getBanca).orElse(null)) ||
				!Optional.ofNullable(getTermini_pagamento()).orElse(new Rif_termini_pagamentoBulk()).equalsByPrimaryKey(Optional.ofNullable(missioneIniziale).map(MissioneBulk::getTermini_pagamento).orElse(null)))
		{
			getCompenso().setModalitaPagamento(getModalita_pagamento());
			getCompenso().setTerminiPagamento(getTermini_pagamento());
			getCompenso().setBanca(getBanca());

			return true;
		}
		return false;
	}
	/**
	 * Il metodo stabilisce se le modifiche portate alla missione implicano la modifica del compenso
	 * Se uno dei seguenti attributi e' cambiato allora al salvataggio della missione 
	 * l'applicazione deve modificare il compenso aprendolo
	 */
	public boolean isCompensoDaModificareInManuale() 
	{
		try{
			return (!getTipo_trattamento().equalsByPrimaryKey(getMissioneIniziale().getTipo_trattamento()) ||
					getIm_spese().compareTo(getMissioneIniziale().getIm_spese()) != 0 ||
					getIm_diaria_netto().compareTo(getMissioneIniziale().getIm_diaria_netto()) != 0 ||
					getIm_quota_esente().compareTo(getMissioneIniziale().getIm_quota_esente()) != 0 ||
					getIm_rimborso().compareTo(getMissioneIniziale().getIm_rimborso()) != 0||
					getDt_registrazione().compareTo(getMissioneIniziale().getDt_registrazione()) != 0 ||
					getDt_inizio_missione().compareTo(getMissioneIniziale().getDt_inizio_missione()) != 0 ||
					!getTipo_rapporto().equalsByPrimaryKey(getMissioneIniziale().getTipo_rapporto()) ||
					!getV_terzo().equalsByPrimaryKey(getMissioneIniziale().getV_terzo()) ||
					isAnticipoModificato() ||
					getDt_fine_missione().compareTo(missioneIniziale.getDt_fine_missione())!=0			 
					);
		}catch(NullPointerException e){
			return false;
		}
	}
	/**
	 * Il metodo stabilisce se il compenso associato alla missione puo' essere modificato o meno.
	 * Il compenso è modificabile se non e' ancora stato pagato (mandato / fondo)
	 */

	public boolean isCompensoModificabile() throws it.cnr.jada.comp.ApplicationException
	{
		if(isCompensoObbligatorio() && isMissioneConCompenso() && getCompenso().isPagato())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! La missione e' associata ad un compenso gia' pagato!");

		return true;	
	}
	/**
	 * Il metodo stabilisce se la missione deve essere associata ad un compenso.
	 * Il compenso non deve essere creato solo se TUTTE le tappe sono in COMUNE ALTRO
	 */
	public boolean isCompensoObbligatorio() 
	{
		if(getTappeMissioneColl() == null || getTappeMissioneColl().isEmpty())
			return false;

		Missione_tappaBulk tappa=null;		
		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )	
		{
			tappa = (Missione_tappaBulk) i.next();
			if((tappa.isEstera() && tappa.getFl_no_diaria() == false) || tappa.isComuneProprio() ||
					(tappa.isEstera() && tappa.getFl_rimborso() == true)	)
				return true;		
		}		
		return false;
	}
	/**
	 * Il metodo stabilisce se l'utente può creare/modificare/cancellare una tappa.
	 * L'operazione può essere effettuata se :
	 * - la missione non ho associato alcun compenso pagato (con mandato o fondo)
	 * - l'utente non ho ancora inserito alcun dettaglio di spesa
	 * - l'utente non ho associato alcun anticipo rimborsato
	 * L'eventuale cancellazione della diaria, nel caso di missione senza spese,
	 * verra' gestita al rientro del metodo
	 */
	@JsonIgnore
	public boolean isConfigurazioneTappeModificabile() throws it.cnr.jada.comp.ApplicationException
	{
		isCompensoModificabile();

		if(isAnticipoRimborsato())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! La missione ha associato un anticipo rimborsato.");

		if(getSpeseMissioneColl()!=null && !getSpeseMissioneColl().isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Per modificare la configurazione delle tappe occorre prima cancellare le spese/diaria inserite !");

		return true;	
	}
	/**
	 * Il metodo stabilisce se l'utente può modificare la data fine missione.
	 * La modifica può essere effettuata solo se l'eventuale compenso associato e' modificabile
	 * e se la missione non e' associata ad anticipo rimborsato
	 */ 

	public void isDataFineModificabile() throws it.cnr.jada.comp.ApplicationException
	{
		isCompensoModificabile();

		if(isAnticipoRimborsato())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! La missione ha associato un anticipo rimborsato.");	
	}
	/**
	 * Il metodo stabilisce se l'utente può modificare la data inizio missione.
	 * La modifica può essere effettuata solo se l'eventuale compenso associato e' modificabile
	 *	o se la missione non ha associato un anticipo gia' rimborsato
	 */
	public void isDataInizioModificabile() throws it.cnr.jada.comp.ApplicationException
	{
		isCompensoModificabile();

		if(isAnticipoRimborsato())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! La missione ha associato un anticipo rimborsato.");		
	}
	/**
	 * Il metodo stabilisce se la data di registrazione puo' essere modificata o meno.
	 * La modifica puo' essere effettuata se se :
	 * - l'eventuale compenso associato e' modificabile
	 * - la missione non e' associata ad anticipo rimborsato
	 */

	public void isDataRegistrazioneModificabile() throws it.cnr.jada.comp.ApplicationException, java.text.ParseException, javax.ejb.EJBException
	{
		isCompensoModificabile();

		if(isAnticipoRimborsato())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! La missione ha associato un anticipo rimborsato.");	
	}

	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 */
	public boolean isDeleting() {
		return false;
	}
	/**
	 * Il metodo restituisce TRUE se :
	 * - la missione non e' pagata
	 * - se l'esercizio del documento e quello scrivania di scrivania sono diversi da quello solare 
	 *   l' obbligazione non deve essere stata riportata  (isROPerChiusura)
	 */
	public boolean isEditable() 
	{
		return !isPagata() && !isROPerChiusura();
	}
	/**
	 * Il metodo ritorna TRUE se la missione è associata ad un anticipo di importo maggiore
	 */
	public boolean isImportoAnticipoMaggioreDiMissione() 
	{
		if(!isMissioneConAnticipo())
			return false;

		calcolaConsuntivi();

		if(getAnticipo().getIm_anticipo() == null)
			getAnticipo().setIm_anticipo(new BigDecimal(0));

		if(getAnticipo().getIm_anticipo().compareTo(getIm_totale_missione()) >= 0)
			return true;

		return false;
	}
	/**
	 * Il metodo ritorna TRUE se la data di inizio missione e' cambiata
	 */

	public boolean isInizioMissioneCambiato(java.sql.Timestamp newDataInizio, java.sql.Timestamp oldDataInizio) 
	{
		if(oldDataInizio == null && newDataInizio != null)
			return true;

		if(oldDataInizio != null && newDataInizio == null)
			return true;

		if(oldDataInizio == null && newDataInizio == null)
			return true;				

		if(!oldDataInizio.equals(newDataInizio))
			return true;

		return false;	
	}
	/**
	 * Il metodo stabilisce se l'utente può creare/modificare/cancellare una spesa.
	 * L'operazione uò essere effettuata se :
	 * - non ho associato alcun compenso pagato (con mandato o fondo)
	 * - non ho associato un anticipo rimborsato
	 */
	public boolean isInserimentoSpeseModificabile()
			throws it.cnr.jada.comp.ApplicationException {
		isCompensoModificabile();

		if (isAnticipoRimborsato())
			throw new it.cnr.jada.comp.ApplicationException(
					"Impossibile proseguire! La missione ha associato un anticipo rimborsato.");

		return true;
	}
	/**
	 * Il metodo stabilisce se l'utente può modificare il Tipo Istituzionale/commerciale
	 * L'operazione uò essere effettuata se :
	 * - l'eventuale compenso associato e' modificabile
	 * - la missione non ha associato un anticipo gia' rimborsato
	 */

	public void isIstituzionaleCommercialeModificabile() throws it.cnr.jada.comp.ApplicationException
	{
		isCompensoModificabile();

		if(isAnticipoRimborsato())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile proseguire! La missione ha associato un anticipo rimborsato.");		
	}
	/**
	 * Il metodo stabilisce se visualizzare o meno la Labe 'Documento riportato' qundo
	 * l'obbligazione associata alla missione e' riportata.
	 * Potrebbe infatti capitare che la missione risulti riportata ma in realtà non ha obbligazione.
	 */

	public boolean isLabelRiportoToShow() 
	{
		if(isMissioneConCompenso())
			return getCompenso().isObbligazioneObbligatoria();

		return isMissioneConObbligazione();
	}
	/**
	 * Il  metodo ritorna TRUE se la missione e' collegata ad un anticipo
	 */

	public boolean isMissioneConAnticipo()
	{
		return (getAnticipo() != null && 
				getPg_anticipo() != null && 
				getAnticipo().getEsercizio() != null &&
				getAnticipo().getCd_cds() != null &&
				getAnticipo().getCd_unita_organizzativa() != null);
	}
	/**
	 * Il  metodo ritorna TRUE se la missione e' collegata (gia' salvata ad un compenso) 
	 * ad un compenso
	 */

	public boolean isMissioneConCompenso()
	{
		return (getFl_associato_compenso() != null && getFl_associato_compenso().booleanValue());
	}
	/**
	 * Il  metodo ritorna TRUE se la missione e' collegata o ad un anticipo o ad una
	 * obbligazione o ad un compenso
	 */

	public boolean isMissioneConDocumenti()
	{
		return (isMissioneConAnticipo() || isMissioneConCompenso() || isMissioneConObbligazione());
	}
	/**
	 * Il  metodo ritorna TRUE se la missione e' collegata ad una scadenza di obbligazione
	 */

	public boolean isMissioneConObbligazione()
	{
		return (getObbligazione_scadenzario() != null && 
				getEsercizio_ori_obbligazione() != null &&
				getPg_obbligazione() != null &&
				getPg_obbligazione_scadenzario() != null);
	}
	/**
	 * Il  metodo ritorna TRUE se la missione e' stata salvata in modo definitivo
	 */
	public boolean isMissioneDefinitiva() 
	{ 
		return MissioneBulk.SALVA_DEFINITIVO.equals(getTi_provvisorio_definitivo());
	}
	/**
	 * Il  metodo ritorna TRUE se la missione e' stata salvata in modo provvisorio
	 */
	public boolean isMissioneProvvisoria() 
	{
		return MissioneBulk.SALVA_PROVVISORIO.equals(getTi_provvisorio_definitivo());
	}
	/**
	 * Il metodo ritorna TRUE se l' obbligazione deve essere associata alla missione.
	 * La missione richiede l'obbligazione se non ho il compenso oppure se l'eventuale 
	 * anticipo associato non ha importo maggiore di quello della missione
	 */
	public boolean isObbligazioneObbligatoria() 
	{
		return (!isCompensoObbligatorio() && !isImportoAnticipoMaggioreDiMissione());
	}
	/**
	 * Il  metodo riotrna TRUE se la missione e' stata pagata
	 */
	public boolean isPagata() 
	{
		return 	(STATO_PAGATO.equalsIgnoreCase(getStato_cofi())) || 
				(REGISTRATO_IN_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco()));
	}
	/**
	 * Il metodo valida il periodo della missione.
	 * - la data inzio puo' essere al massimo nell'esercizio percedente rispetto a quello della missione
	 * - la data di fine puo' essere al piu' nell'esercizio successivo a quello della missione
	 * - non puo' accadere che la data di inizio sia nell'esercizio precedente rispetto a quello della missione
	 *   e contemporaneamente che la data di fine sia nell'esercizio successivo a quello della missione
	 * - la data di inzio e quella di fine possono essere entrambe nello  stesso esercizio della missione o 
	 *   nell'esercizio precedente a quello della missione oppure la data inzio nello stesso esercizio della missione
	 *   e quella di fine nell'esercizio successivo
	 */

	public void isPeriodoMissioneInEserciziValidi() 
	{
		if(getDt_inizio_missione() == null ||  getDt_fine_missione() == null)
			return;

		GregorianCalendar inizio = getGregorianCalendar(getDt_inizio_missione());
		GregorianCalendar fine = getGregorianCalendar(getDt_fine_missione());

		if(	fine.get(GregorianCalendar.YEAR) > (getEsercizio().intValue()+1))
			throw new it.cnr.jada.action.MessageToUser( "La data di fine missione non e' valida!" );	  			 

		if(	inizio.get(GregorianCalendar.YEAR) == (getEsercizio().intValue()-1) && 
				fine.get(GregorianCalendar.YEAR) == (getEsercizio().intValue()+1))
			throw new it.cnr.jada.action.MessageToUser( "Le date di inizio/fine missione possono solo essere entrambe nell'esercizio precedente, in quello corrente o a cavallo di esercizio !" );
		if(	fine.get(GregorianCalendar.YEAR) - inizio.get(GregorianCalendar.YEAR) > 1 )
			throw new it.cnr.jada.action.MessageToUser( "La durata della missione non può essere a cavallo di 3 anni !" );

	}
	/**
	 * Il metodo ritorna TRUE se l'obbligazione la cui scadenza e' associata alla missione
	 * e' stata riportata
	 */
	public boolean isRiportata() 
	{
		return !NON_RIPORTATO.equals(riportata);
	}
	/**
	 * isRiportataInScrivania method comment.
	 */
	public boolean isRiportataInScrivania() {

		return !NON_RIPORTATO.equals(riportataInScrivania);
	}
	public boolean isROAnticipo() 
	{
		return anticipo == null || anticipo.getCrudStatus() == NORMAL;	 
	}
	/**
	 * Il metodo definisce le regole base per l'abilitazione dei campi in caso di obbligazione riportata
	 * Se carico un anticipo con esercizio precedente a quello solare :
	 * - esercizio scrivania != anno solare e obbligazione riportata --> nessuna modifica al documento
	 * - esercizio scrivania != anno solare e obbligazione non riportata --> qualsiasi modifica consentita
	 * - esercizio scrivania = anno solare e obbligazione riportata --> nessuna modifica tranne CoFi e Annulla
	 * - esercizio scrivania = anno solare e obbligazione non riportata --> nessuna modifica al documento
	 */

	public boolean isROPerChiusura()
	{
		if(getEsercizio()==null)
			return false;
		// Gennaro Borriello/Farinella Luisa - (05/11/2004 12.23.28)
		// Modif. relativa alla nuova gestione di isRiportata()	
		// MB - (06/11/2004 12.23.28)
		if (getEsercizio().intValue() == getEsercizioScrivania()) {
			return isRiportata();
		} else 
			return true;
	}
	/**
	 * Il metodo stabilisce se il campo 'select' relatico al fondo economale e' abilitato o meno.
	 * Disabilitato se :
	 * - la missione non ha obbligazione
	 * - la missione e' pagata (isEditable)
	 * - se l'esercizio del documento e quello scrivania di scrivania sono diversi da quello solare 
	 *   e l' obbligazione e' stata riportata  (isEditable)
	 */
	public boolean isROStato_pagamento_fondo_eco()  
	{
		if(!isMissioneConObbligazione() || !isEditable())
			return true;

		return false;
	}
	/**
	 * Il metodo stabilisce se i dati del terzo sono abilitati o meno.
	 * Disabilitati se :
	 * - la missione ha documenti associati		E
	 * - la missione e' pagata (isEditable) 	E
	 * - se l'esercizio del documento e quello scrivania di scrivania sono 
	 *   diversi da quello solare e l' obbligazione e' stata riportata  (isEditable) 
	 */
	public boolean isROTerzo() 
	{
		return 	v_terzo == null || v_terzo.getCrudStatus() == NORMAL || 
				(	getPg_missione() != null && 
				getPg_missione().compareTo(new Long(0))>0 && 
				isMissioneConDocumenti() &&
				!isEditable()
						);
	}
	/**
	 * Il metodo stabilisce se i dati del terzo sono abilitati o meno.
	 * Disabilitati se :
	 * - la missione ha documenti associati		E
	 * - la missione e' pagata (isEditable) 	E
	 * - se l'esercizio del documento e quello scrivania di scrivania sono 
	 *   diversi da quello solare e l' obbligazione e' stata riportata  (isEditable) 
	 */
	public boolean isROTerzoPerModifica() 
	{
		//  Se la missione e' in modifica e ho dei documenti associati non posso
		//	modificare il terzo
		return	getPg_missione() != null && 
				getPg_missione().compareTo(new Long(0))>0 && 
				isMissioneConDocumenti() &&
				!isEditable();
	} 
	/**
	 * Il metodo ritorna il valore dell'attributo 'salvataggioTemporaneo'
	 */
	public boolean isSalvataggioTemporaneo() {
		return salvataggioTemporaneo;
	}
	/**
	 * Il metodo stabilisce se la scadenza appena associata era stata messa precedentemente tra quelle da
	 * da cancellare.
	 * Cio' succede quando scollego e ricollego una stessa scadenza o la scadenza che gia' 
	 * avevo salvato con la missione (clone) o una scadenza appena creata ma non ancora
	 * salvata con la missione (numerazione negativa)
	 */

	public boolean isScadenzaDaRimuovereDaiCancellati() 
	{
		if(getDocumentiContabiliCancellati()==null || getDocumentiContabiliCancellati().isEmpty())
			return false;

		if(getObbligazione_scadenzarioClone()!=null && getObbligazione_scadenzario().equalsByPrimaryKey(getObbligazione_scadenzarioClone()))
			return true;

		Obbligazione_scadenzarioBulk scadenza=null;	
		for ( Iterator i = getDocumentiContabiliCancellati().iterator(); i.hasNext(); )
		{
			scadenza = (Obbligazione_scadenzarioBulk) i.next();
			if(scadenza.equalsByPrimaryKey(getObbligazione_scadenzario()))
				return true;
		}			
		return false;
	}
	/**
	 * Il metodo ritorna TRUE se l'utente ha selezionato piu' di un giorno di spesa
	 */

	public boolean isSelezioneGiorniSpesaMultipla() 
	{
		if(getCollectionGiorniSpeseSelezionati()==null || getCollectionGiorniSpeseSelezionati().isEmpty())
			return false;

		if(getCollectionGiorniSpeseSelezionati().size()>1)
			return true;

		return false;
	}
	/**
	 * Il metodo verifica se e' valida la selezione multipla dei giorni per i quali inserire la spesa.
	 * Il metodo ritorna TRUE se le tappe dei giorni selezionati hanno tutte lo stesso progressivo della nazione e 
	 * la stessa area geografica. (Questo serve affinche' i tipi spesa, pasto, rimborso km... siano
	 * coerenti, cioe' abbiano ad esempio gli massimali per ogni giorno)	
	 */

	public boolean isSelezioneGiorniSpesaValida() 
	{
		if(getCollectionGiorniSpeseSelezionati()==null || getCollectionGiorniSpeseSelezionati().isEmpty())
			return true;

		//	Ricavo la tappa del primo giorno selezionato perche' confrontero' il suo progressivo nazione e il 
		//	il suo tipo area geografica	(estero o italia) con quelli delle tappe degli altri giorni selezionati
		java.sql.Timestamp primoGG = (java.sql.Timestamp)getCollectionGiorniSpeseSelezionati().get(0);
		Missione_tappaBulk primaTappa = (Missione_tappaBulk) getTappeMissioneHash().get(primoGG);

		Missione_tappaBulk aTappa=null;
		java.sql.Timestamp aGiorno=null;

		//	Ciclo su tutti i giorni selezionati e mi assicuro che le relative tappe abbiano tutte
		//	lo stesso progressivo della nazione e la stessa area geografica	
		for (Iterator i = getCollectionGiorniSpeseSelezionati().iterator(); i.hasNext();)
		{
			aGiorno = (java.sql.Timestamp)i.next();		
			aTappa = (Missione_tappaBulk) getTappeMissioneHash().get(aGiorno);
			if(!primaTappa.getPg_nazione().equals(aTappa.getPg_nazione()))
				return false;

			if(aTappa.getNazione()!=null && primaTappa.getNazione()!=null && !(primaTappa.getNazione().getTi_nazione()).equals(aTappa.getNazione().getTi_nazione()))
				return false;
		}

		return true;
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'speseInserite'
	 */
	public boolean isSpeseInserite() {
		return speseInserite;
	}
	/**
	 * Il metodo verifica se la tappa del primo giorno e' stata configurata. 
	 * E' necessario configurarla perche' altrimenti non ha una precedente dalla quale ereditare
	 */

	public void isTappaPrimoGiornoConfigurata() throws ValidationException
	{
		Missione_tappaBulk primaTappa = (Missione_tappaBulk)getTappeMissioneColl().get(0);

		GregorianCalendar gcPrimaTappa = (GregorianCalendar)getGregorianCalendar(primaTappa.getDt_inizio_tappa()).clone();				
		gcPrimaTappa.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gcPrimaTappa.set(GregorianCalendar.MINUTE, 0);

		GregorianCalendar gcInizioMissione = (GregorianCalendar)getGregorianCalendar(getDt_inizio_missione()).clone();
		gcInizioMissione.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gcInizioMissione.set(GregorianCalendar.MINUTE, 0);

		if(!gcInizioMissione.equals(gcPrimaTappa))
			throw new ValidationException("Configurare la tappa del primo giorno della missione !");
	}
	/**
	 * Il  metodo ritorna il valore dell'attributo 'tappeConfigurate'
	 */
	public boolean isTappeConfigurate() {
		return tappeConfigurate;
	}
	/**
	 * Il metodo verifica se il terzo e' valido rispetto alla data di registrazione della missione
	 */

	public boolean isTerzoValido() 
	{
		if((getV_terzo() == null) || (getV_terzo().getDt_fine_validita_terzo() == null))
			return true;

		if(getDt_inizio_missione() == null)
			return false;

		return((getV_terzo().getDt_fine_validita_terzo().compareTo(getDt_inizio_missione()) >= 0) );
	}
	/**
	 * Il metodo riordina in modo crescente i giorni della missione
	 */
	public void ordinaCollectionGiorni( ) 
	{
		// riordino la lista dei giorni

		Collections.sort(getCollectionGiorni(),new Comparator() {	

			public int compare(Object o1, Object o2) 
			{
				java.sql.Timestamp ts1 = (java.sql.Timestamp) o1;
				java.sql.Timestamp ts2 = (java.sql.Timestamp) o2;			
				return ts1.compareTo(ts2);
			}
		});

		return;
	}
	/**
	 * Il metodo ordina le tappe per data inizio
	 */
	public void ordinaTappePerDataInizioTappa( ) 
	{
		// riordino la lista delle tappe configurate per "data inizio tappa"

		Collections.sort(getTappeMissioneColl(),new Comparator() {	

			public int compare(Object o1, Object o2) 
			{
				Missione_tappaBulk os1 = (Missione_tappaBulk) o1;
				Missione_tappaBulk os2 = (Missione_tappaBulk) o2;

				return os1.getDt_inizio_tappa().compareTo( os2.getDt_inizio_tappa());
			}
		});

		return;
	}
	/**
	 * Il metodo memorizzo in una Hashtable tutte le tappe al termine della loro configurazione
	 */

	public void putInHashtableTappe(Missione_tappaBulk tappa)
	{
		if(getTappeMissioneHash() == null)
			tappeMissioneHash = new PrimaryKeyHashtable();

		if(tappeMissioneHash.containsKey(tappa.getDt_inizio_tappa()))
			return;

		tappeMissioneHash.put(tappa.getDt_inizio_tappa(), tappa);

		return;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
	 * Il metodo rimuove il documento contabile dalla mappa dei saldi in differita 
	 */
	public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) 
	{
		if(docCont != null && deferredSaldi != null &&	deferredSaldi.containsKey(docCont))
			deferredSaldi.remove(docCont);
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 */
	public int removeFromDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio) 
	{
		if (BulkCollections.containsByPrimaryKey(getDettagliCancellati(), (OggettoBulk)dettaglio))
			getDettagliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDettagliCancellati(), (OggettoBulk)dettaglio));
		return getDettagliCancellati().size()-1;
	}

	/**
	 * Il metodo rimuove dalla collection dei dettagli di diaria un solo dettaglio
	 */
	public Missione_dettaglioBulk removeFromDiariaMissioneColl(int index) 
	{
		Missione_dettaglioBulk diaria = (Missione_dettaglioBulk)diariaMissioneColl.remove(index);
		diaria.setToBeDeleted();

		return diaria;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
	 */
	public int removeFromDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio) 
	{
		if(getDocumentiContabiliCancellati() == null)
			return -1;

		if (BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk)dettaglio))
			getDocumentiContabiliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDocumentiContabiliCancellati(),(OggettoBulk)dettaglio));
		return getDocumentiContabiliCancellati().size()-1;
	}

	/**
	 * Il metodo rimuove dalla Hashtable l'istanza di tappa appena cancellata dall'utente
	 */

	public void removeFromHashtableTappe(Missione_tappaBulk tappa)
	{
		if(getTappeMissioneHash() == null)
			return;

		if(tappeMissioneHash.containsKey(tappa.getDt_inizio_tappa()))
			tappeMissioneHash.remove(tappa.getDt_inizio_tappa());

		return;
	}
	/**
	 * Il metodo rimuove dalla collection dei dettagli di spesa un solo dettaglio
	 */
	public Missione_dettaglioBulk removeFromSpeseMissioneColl(int index) 
	{
		Missione_dettaglioBulk spesa = (Missione_dettaglioBulk)speseMissioneColl.remove(index);
		spesa.setToBeDeleted();

		return spesa;
	}
	/**
	 * Il metodo rimuove dalla collection delle tappe una sola tappa
	 */
	public Missione_tappaBulk removeFromTappeMissioneColl(int index) 
	{
		Missione_tappaBulk tappa = (Missione_tappaBulk)tappeMissioneColl.remove(index);
		tappa.setToBeDeleted();

		return tappa;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
	 * Imposta la mappa dei saldi in differita al valore di default 
	 */
	public void resetDefferredSaldi() 
	{
		deferredSaldi = null;	
	}
	/**
	 * Il metodo ressetto il TIME delle date di inizio/fine tappa per forzare la riconfigurazione
	 * delle tappe
	 */
	public void resettaInizioFineTappe() 
	{
		Missione_tappaBulk tappa = null;
		java.util.GregorianCalendar gcInizio = null;

		if((getTappeMissioneColl() == null) || (getTappeMissioneColl().isEmpty()))
			return;

		int index=0;

		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )
		{
			tappa = (Missione_tappaBulk) i.next();

			if(tappa.getDt_inizio_tappa() != null)
			{
				index = getCollectionGiorni().indexOf(tappa.getDt_inizio_tappa());

				gcInizio = getGregorianCalendar(tappa.getDt_inizio_tappa());
				gcInizio.set(java.util.GregorianCalendar.HOUR_OF_DAY, 0);			
				gcInizio.set(java.util.GregorianCalendar.MINUTE, 0);

				if(index >= 0)
					getCollectionGiorni().set(index, new java.sql.Timestamp(gcInizio.getTime().getTime()));

				tappa.setDt_inizio_tappa(new java.sql.Timestamp(gcInizio.getTime().getTime()));
				tappa.setDt_fine_tappa(null);
			}
		}
		setTappeConfigurate(false);
	}
	/**
	 * Il metodo prepara una collection contenente i singoli giorni compresi tra la data di 
	 * inizio e fine missione. 
	 */

	public void riempiElencoGiorniColl( ) 
	{
		if((getCollectionGiorni() != null) && (getCollectionGiorni().size() > 0))
			return;

		setCollectionGiorni(new Vector());

		GregorianCalendar gcInizio = getGregorianCalendar(getDt_inizio_missione());
		gcInizio.set(GregorianCalendar.HOUR_OF_DAY, 0);			
		gcInizio.set(GregorianCalendar.MINUTE, 0);

		GregorianCalendar gcFine = getGregorianCalendar(getDt_fine_missione());
		gcFine.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gcFine.set(GregorianCalendar.MINUTE, 0);

		getCollectionGiorni().add(new java.sql.Timestamp(gcInizio.getTime().getTime()));

		// Ore e minuti dei GregoriaCalendar definiti li inizializzo a 0 perche' altrimenti 
		// non funzionerebbe correttamente il "before"
		while(gcInizio.before(gcFine))
		{
			gcInizio.add(java.util.Calendar.DATE, 1);
			getCollectionGiorni().add(new java.sql.Timestamp(gcInizio.getTime().getTime())); 
		}	
	}
	/**
	 * Il metodo inizializza la collection dei giorni ogni volta che carica in modifca una missione
	 */

	public void riempiElencoGiorniCollPerModifica( ) 
	{
		setCollectionGiorni(new Vector());

		if(getTappeMissioneHash() != null)
			for(Enumeration e = getTappeMissioneHash().keys(); e.hasMoreElements();)
				getCollectionGiorni().add(e.nextElement());
	}
	/**
	 * Il metodo predispongo le tappe all'inserimento.
	 * Il  metodo inizializza le liste dei dettagli di spesa/diaria perche' essi sono stati
	 * cancellati automaticamente dalla cancellazione delle tappe (Constraint).
	 */

	public void ripristinaCrudStatusFigli() 
	{
		// 	Avendo cancellato tutte le tappe in tabella devo predisporre tutte le tappe
		//	che ho in memoria come se dovessero essere inserite per la prima volta
		Missione_tappaBulk tappa = null;
		for ( java.util.Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )
		{
			tappa = (Missione_tappaBulk)i.next();
			if(tappa.getCrudStatus() == OggettoBulk.NORMAL)
				tappa.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			if(tappa.isToBeUpdated())
				tappa.setCrudStatus(OggettoBulk.TO_BE_CREATED);
		}
		// 	Le tappe che risultano da cancellare non le considero piu' perche' le ho appena
		//	cancellate a DB 
		for ( java.util.Iterator i = getTappeMissioneColl().deleteIterator(); i.hasNext(); )
		{
			tappa = (Missione_tappaBulk)i.next();
			if(tappa.isToBeDeleted())
				tappa.setCrudStatus(OggettoBulk.UNDEFINED);			
		}
		// 	Inizializzo le collection dei dettagli (spese/diaria) perche'
		//	cancellando le tappe vengono cancellati anche i relativi dettagli 
		//	(spese e diaria) da un Constraint. 
		setSpeseMissioneColl(new BulkList());		
		setDiariaMissioneColl(new BulkList());	
		setRimborsoMissioneColl(new BulkList());
	}
	/**
	 * il  metodo imposta il valore dell'attributo 'annoSolare'
	 */ 
	public void setAnnoSolare(int newAnnoSolare) {
		annoSolare = newAnnoSolare;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'anticipo'
	 */ 
	public void setAnticipo(AnticipoBulk newAnticipo) {
		anticipo = newAnticipo;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'anticipoClone'
	 */ 
	public void setAnticipoClone(AnticipoBulk newAnticipoClone) {
		anticipoClone = newAnticipoClone;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'banca'
	 */ 
	public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
		banca = newBanca;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_cds_anticipo'
	 */ 
	public void setCd_cds_anticipo(java.lang.String cd_cds_anticipo) {
		this.getAnticipo().setCd_cds(cd_cds_anticipo);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_ds_obbligazione'
	 */ 
	public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
		this.getObbligazione_scadenzario().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds_obbligazione);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_modalita_pag'
	 */ 
	public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
		this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_termini_pag'
	 */ 
	public void setCd_termini_pag(java.lang.String cd_termini_pag) {
		this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
	}
	/**
	 * Metodo richiesto dall'interfaccia IDocumentoAmministrativoSpesaBulk
	 */
	public void setCd_tipo_doc_amm(java.lang.String newCd_tipo_doc_amm) {}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_tipo_missione'
	 */ 
	public void setCd_tipo_missione(java.lang.String cd_tipo_missione) {
		this.getTipo_missione().setCd_tipo_missione(cd_tipo_missione);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_tipo_rapporto'
	 */ 
	public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
		this.getTipo_rapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
	}
	/**
	 * Metodo richiesto dall'interfaccia IDocumentoAmministrativoSpesaBulk
	 * Imposta il valore dell'attributo 'cd_unita_organizzativa'
	 */
	public void setCd_uo(java.lang.String newCd_uo) 
	{
		setCd_unita_organizzativa(newCd_uo);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'cd_uo_anticipo'
	 */
	public void setCd_uo_anticipo(java.lang.String cd_uo_anticipo) {
		this.getAnticipo().setCd_unita_organizzativa(cd_uo_anticipo);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'collectionGiorni'
	 */
	public void setCollectionGiorni(java.util.Vector newCollectionGiorni) {
		collectionGiorni = newCollectionGiorni;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'collectionGiorniSpese'
	 */
	public void setCollectionGiorniSpese(java.util.List newCollectionGiorniSpese) {
		collectionGiorniSpese = newCollectionGiorniSpese;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'collectionGiorniSpeseSelezionati'
	 */
	public void setCollectionGiorniSpeseSelezionati(java.util.List newCollectionGiorniSpeseSelezionati) {
		collectionGiorniSpeseSelezionati = newCollectionGiorniSpeseSelezionati;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'compenso'
	 */
	public void setCompenso(it.cnr.contab.compensi00.docs.bulk.CompensoBulk newCompenso) {
		compenso = newCompenso;
	}
	/**
	 * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
	 */
	public void setDeferredSaldi(it.cnr.jada.bulk.PrimaryKeyHashMap newDeferredSaldi) 
	{
		deferredSaldi = newDeferredSaldi;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'dettagliCancellati'
	 */
	public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {
		dettagliCancellati = newDettagliCancellati;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'diariaMissioneColl'
	 */
	public void setDiariaMissioneColl(it.cnr.jada.bulk.BulkList<Missione_dettaglioBulk> newDiariaMissioneColl) {
		diariaMissioneColl = newDiariaMissioneColl;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'rimborsoMissioneColl'
	 */
	public void setRimborsoMissioneColl(it.cnr.jada.bulk.BulkList<Missione_dettaglioBulk> newRimborsoMissioneColl) {
		rimborsoMissioneColl = newRimborsoMissioneColl;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'documentiContabiliCancellati'
	 */
	public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati) {
		documentiContabiliCancellati = newDocumentiContabiliCancellati;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'esercizioAnticipo'
	 */
	public void setEsercizio_anticipo(java.lang.Integer esercizio_anticipo) {
		this.getAnticipo().setEsercizio(esercizio_anticipo);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'esercizioObbligazione'
	 */
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
		this.getObbligazione_scadenzario().getObbligazione().setEsercizio(esercizio_obbligazione);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'esercizioScrivania'
	 */
	public void setEsercizioScrivania(int newEsercizioScrivania) {
		esercizioScrivania = newEsercizioScrivania;
	}
	/**
	 * Il metodo imposta le flag relative il comune estero/proprio/altro ogni volta che
	 * l'utente fa un salvataggio della missione 
	 */
	public void setFlagComuni()
	{
		Missione_tappaBulk tappa;

		setFl_comune_altro(new Boolean(false));
		setFl_comune_estero(new Boolean(false));
		setFl_comune_proprio(new Boolean(false));

		if((getTappeMissioneColl() == null) || (getTappeMissioneColl().size() == 0))
			return;

		for( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )
		{
			tappa = (Missione_tappaBulk) i.next();

			if(tappa.getFl_comune_altro().booleanValue())
				setFl_comune_altro(new Boolean(true));
			if(tappa.getFl_comune_proprio().booleanValue())
				setFl_comune_proprio(new Boolean(true));
			if(tappa.getFl_comune_estero().booleanValue())
				setFl_comune_estero(new Boolean(true));						
		}	
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'giornoConsuntivo'
	 */
	public void setGiorno_consuntivo(java.sql.Timestamp newGiorno_consuntivo) {
		giorno_consuntivo = newGiorno_consuntivo;
	}
	/**
	 * Il metodo imposta correttamente data/ora di inizio/fine tappa in base alla configurazione
	 * @throws ValidationException 
	 */
	public void setInizioFineTappe(java.sql.Timestamp data_fine_diaria_miss_estero) throws ValidationException
	{
		Missione_tappaBulk tappa = null;
		int index=0;

		if((getTappeMissioneColl() == null) || (getTappeMissioneColl().size() == 0))
			return;

		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )
		{
			tappa = (Missione_tappaBulk) i.next();
			if(tappa == null || tappa.getDt_inizio_tappa() == null)
				throw new ValidationException("Valorizzare la Data di inizio della tappa.");
			GregorianCalendar inizioTappa = (GregorianCalendar)getGregorianCalendar(tappa.getDt_inizio_tappa()).clone();
			GregorianCalendar inizioMissione = (GregorianCalendar) getGregorianCalendar(getDt_inizio_missione()).clone();
			GregorianCalendar fineMissione = (GregorianCalendar)getGregorianCalendar(getDt_fine_missione()).clone();

			Missione_tappaBulk tappaPrecedente=null;
			Missione_tappaBulk tappaSuccessiva=null;  	
			if(index>0)
				tappaPrecedente = (Missione_tappaBulk)((Missione_tappaBulk) getTappeMissioneColl().get(index-1)).clone();
			if(index<=getTappeMissioneColl().size()-2)	
				tappaSuccessiva = (Missione_tappaBulk)((Missione_tappaBulk) getTappeMissioneColl().get(index+1)).clone();

			/************* Imposto Data/Ora inizio tappa **********************/
			if( (inizioTappa.get(GregorianCalendar.DATE) == inizioMissione.get(GregorianCalendar.DATE)) &&
					(inizioTappa.get(GregorianCalendar.MONTH) == inizioMissione.get(GregorianCalendar.MONTH)) &&	 		
					(inizioTappa.get(GregorianCalendar.YEAR) == inizioMissione.get(GregorianCalendar.YEAR)))	
			{
				if(tappa.isEstera() && (!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero)>0)))
				{
					GregorianCalendar inizioPrimaTappa = (GregorianCalendar)inizioMissione.clone();
					inizioPrimaTappa.set(GregorianCalendar.HOUR_OF_DAY, 0);
					inizioPrimaTappa.set(GregorianCalendar.MINUTE, 0);

					// Reimposto il giorno nella collection perche' aveva ora nulla
					// Se non lo facessi quando modifico una tappa non mi seleziona il gg
					if(getCollectionGiorni().contains(tappa.getDt_inizio_tappa()))
					{
						getCollectionGiorni().remove(tappa.getDt_inizio_tappa());
						getCollectionGiorni().add(new java.sql.Timestamp(inizioPrimaTappa.getTime().getTime()));					
					}	

					tappa.setDt_inizio_tappa(new java.sql.Timestamp(inizioPrimaTappa.getTime().getTime()));
					// Reimposto "inizioTappa" perche' potrebbe servirmi valorizzare la fine
					inizioTappa = (GregorianCalendar)inizioPrimaTappa.clone();

				}
				else
				{	
					// Reimposto il giorno nella collection perche' aveva ora nulla
					// Se non lo facessi quando modifico una tappa non mi seleziona il gg
					if(getCollectionGiorni().contains(tappa.getDt_inizio_tappa()))
					{
						getCollectionGiorni().remove(tappa.getDt_inizio_tappa());
						getCollectionGiorni().add((java.sql.Timestamp)getDt_inizio_missione().clone());					
					}	

					tappa.setDt_inizio_tappa((java.sql.Timestamp)getDt_inizio_missione().clone());
					// Reimposto "inizioTappa" perche' potrebbe servirmi valorizzare la fine
					inizioTappa = (GregorianCalendar)getGregorianCalendar(tappa.getDt_inizio_tappa()).clone();
				}
			}	
			else	
			{
				// Non e' la prima tappa
				GregorianCalendar gcInizio = (GregorianCalendar)getGregorianCalendar(tappaPrecedente.getDt_fine_tappa()).clone();
				if((gcInizio.get(GregorianCalendar.HOUR_OF_DAY) == 23) && (gcInizio.get(GregorianCalendar.MINUTE) == 59))
				{
					gcInizio.add(java.util.Calendar.DATE, 1);				
					gcInizio.set(GregorianCalendar.HOUR_OF_DAY, 0);
					gcInizio.set(GregorianCalendar.MINUTE, 0);				
				}
				if(getCollectionGiorni().contains(tappa.getDt_inizio_tappa()))
				{
					// Reimposto il giorno nella collection perche' aveva ora nulla
					// Se non lo facessi quando modifico una tappa non mi seleziona il gg
					getCollectionGiorni().remove(tappa.getDt_inizio_tappa());
					getCollectionGiorni().add(new java.sql.Timestamp(gcInizio.getTime().getTime()));					
				}	

				tappa.setDt_inizio_tappa(new java.sql.Timestamp(gcInizio.getTime().getTime()));
				// Reimposto "inizioTappa" perche' potrebbe servirmi valorizzare la fine
				inizioTappa = (GregorianCalendar)getGregorianCalendar(tappa.getDt_inizio_tappa()).clone();			
			}

			/************* Imposto Data/Ora fine tappa **********************/
			if(	(inizioTappa.get(GregorianCalendar.DATE) == fineMissione.get(GregorianCalendar.DATE)) &&
					(inizioTappa.get(GregorianCalendar.MONTH) == fineMissione.get(GregorianCalendar.MONTH)) &&	 		
					(inizioTappa.get(GregorianCalendar.YEAR) == fineMissione.get(GregorianCalendar.YEAR)))	 	  	
			{
				// Ultima tappa
				if(tappa.getFl_comune_estero().booleanValue() && (!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero)>0)))
				{
					// Estero
					fineMissione.set(GregorianCalendar.HOUR_OF_DAY, 23);
					fineMissione.set(GregorianCalendar.MINUTE, 59);
					tappa.setDt_fine_tappa(new java.sql.Timestamp(fineMissione.getTime().getTime()));		
				}
				else
					tappa.setDt_fine_tappa((java.sql.Timestamp)getDt_fine_missione().clone());
			}
			else
			{
				// Non e' l'ultima tappa
				if(tappa.getFl_comune_estero().booleanValue()&& (!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero)>0)))
				{ 
					// Estero
					inizioTappa.set(GregorianCalendar.HOUR_OF_DAY, 23);
					inizioTappa.set(GregorianCalendar.MINUTE, 59);
					tappa.setDt_fine_tappa(new java.sql.Timestamp(inizioTappa.getTime().getTime()));					
				}		
				else
				{
					// Estere - non è la prima nè l'ultima tappa
					if(	((tappaSuccessiva != null) && (tappaSuccessiva.getFl_comune_estero().booleanValue()) && (!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero)>0))) ||
							((tappaPrecedente != null) && (tappaPrecedente.getFl_comune_estero().booleanValue())&& (!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero)>0))))
					{
						inizioTappa.set(GregorianCalendar.HOUR_OF_DAY, 23);
						inizioTappa.set(GregorianCalendar.MINUTE, 59);					
					}
					else //Italia oppure non diaria
					{
						//if(!((inizioTappa.get(GregorianCalendar.HOUR_OF_DAY) == 0) && (inizioTappa.get(GregorianCalendar.MINUTE)==0)))
						inizioTappa.add(java.util.Calendar.DATE, 1);					
						//if((inizioTappa.get(GregorianCalendar.HOUR_OF_DAY) == 0) && (inizioTappa.get(GregorianCalendar.MINUTE)==0))
						//{
						//	inizioTappa.set(GregorianCalendar.HOUR_OF_DAY, 23);
						//	inizioTappa.set(GregorianCalendar.MINUTE, 59);					
						//}						
					}	
					tappa.setDt_fine_tappa(new java.sql.Timestamp(inizioTappa.getTime().getTime()));
				}
			}
			index++;
		}
		verificaUltimaTappa();

		return;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'inquadramenti'
	 */
	public void setInquadramenti(java.util.Collection newInquadramenti) {
		inquadramenti = newInquadramenti;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'isDeleting'
	 */
	public void setIsDeleting(boolean deletingStatus) {}

	private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

	@Override
	public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
		return scrittura_partita_doppia;
	}
	@Override
	public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
		this.scrittura_partita_doppia = scrittura_partita_doppia;
	}

	@Override
	public TipoDocumentoEnum getTipoDocumentoEnum() {
		return TipoDocumentoEnum.fromValue(this.getCd_tipo_doc_amm());
	}

	/**
	 * Il metodo inizializza alcuni campi della missione che la rendono DEFINITIVA
	 */
	public void setMissioneDefinitiva() 
	{
		setStato_coge(STATO_INIZIALE_COGE);
		setStato_coan(STATO_INIZIALE_COAN);
		setTi_provvisorio_definitivo(SALVA_DEFINITIVO);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'missioneIniziale'
	 */
	public void setMissioneIniziale(MissioneBulk newMissioneIniziale) {
		missioneIniziale = newMissioneIniziale;
	}
	/**
	 * Il metodo inizializzo alcuni campi della missione che la rendono PROVVISORIA
	 */
	public void setMissioneProvvisoria() 
	{
		setStato_coge(STATO_COGE_NON_PROCESSARE);
		setStato_coan(STATO_COAN_NON_PROCESSARE);
		setTi_provvisorio_definitivo(SALVA_PROVVISORIO);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'modalita'
	 */
	public void setModalita(java.util.Collection newModalita) {
		modalita = newModalita;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'modalita_pagamento'
	 */
	public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
		modalita_pagamento = newModalita_pagamento;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'obbligazione_scadenzario'
	 */
	public void setObbligazione_scadenzario(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newObbligazione_scadenzario) {
		obbligazione_scadenzario = newObbligazione_scadenzario;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'obbligazione_scadenzarioClone'
	 */
	public void setObbligazione_scadenzarioClone(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newObbligazione_scadenzarioClone) {
		obbligazione_scadenzarioClone = newObbligazione_scadenzarioClone;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'pg_anticipo'
	 */
	public void setPg_anticipo(java.lang.Long pg_anticipo) {
		this.getAnticipo().setPg_anticipo(pg_anticipo);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'pg_banca'
	 */
	public void setPg_banca(java.lang.Long pg_banca) {
		this.getBanca().setPg_banca(pg_banca);
	}
	/**
	 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
	 * il metodo ritorna il progressivo della missione
	 */
	public void setPg_doc_amm(java.lang.Long newPg) 
	{
		setPg_missione(newPg);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'esercizio_ori_obbligazione'
	 */
	public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
		this.getObbligazione_scadenzario().getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'pg_obbligazione'
	 */
	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.getObbligazione_scadenzario().getObbligazione().setPg_obbligazione(pg_obbligazione);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'pg_obbligazione_scadenzario'
	 */
	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
		this.getObbligazione_scadenzario().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'pg_rif_inquadramento'
	 */
	public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
		this.getRif_inquadramento().setPg_rif_inquadramento(pg_rif_inquadramento);
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'rif_inquadramento'
	 */
	public void setRif_inquadramento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk newRif_inquadramento) {
		rif_inquadramento = newRif_inquadramento;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'riportata'
	 */
	public void setRiportata(java.lang.String newRiportata) {
		riportata = newRiportata;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (02/11/2004 14.30.59)
	 * @param newRiportataInScrivania java.lang.String
	 */
	public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
		riportataInScrivania = newRiportataInScrivania;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'salvataggioTemporaneo'
	 */
	public void setSalvataggioTemporaneo(boolean newSalvataggioTemporaneo) {
		salvataggioTemporaneo = newSalvataggioTemporaneo;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'speseInserite'
	 */
	public void setSpeseInserite(boolean newSpeseInserite) 
	{
		speseInserite = newSpeseInserite;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'speseMissioneColl'
	 */
	public void setSpeseMissioneColl(it.cnr.jada.bulk.BulkList<Missione_dettaglioBulk> newSpeseMissioneColl) {
		speseMissioneColl = newSpeseMissioneColl;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tappeConfigurate'
	 */
	public void setTappeConfigurate(boolean newTappeConfigurate) 
	{
		tappeConfigurate = newTappeConfigurate;
		if(tappeConfigurate)
		{
			for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )
			{
				Missione_tappaBulk tappa = (Missione_tappaBulk) i.next();
				putInHashtableTappe(tappa);
			}	
		}
		if(!tappeConfigurate)
		{
			setTappeMissioneHash(new PrimaryKeyHashtable());
		}	
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tappeMissioneColl'
	 */
	public void setTappeMissioneColl(it.cnr.jada.bulk.BulkList<Missione_tappaBulk> newTappeMissioneColl) {
		tappeMissioneColl = newTappeMissioneColl;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tappeMissioneHash'
	 */
	public void setTappeMissioneHash(it.cnr.jada.bulk.PrimaryKeyHashtable newTappeMissioneHash) {
		tappeMissioneHash = newTappeMissioneHash;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'termini'
	 */
	public void setTermini(java.util.Collection newTermini) {
		termini = newTermini;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'termini_pagameto'
	 */
	public void setTermini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento) {
		termini_pagamento = newTermini_pagamento;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tipi_missione'
	 */
	public void setTipi_missione(java.util.Collection newTipi_missione) {
		tipi_missione = newTipi_missione;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tipi_rapporto'
	 */
	public void setTipi_rapporto(java.util.Collection newTipi_rapporto) {
		tipi_rapporto = newTipi_rapporto;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tipi_trattamento'
	 */
	public void setTipi_trattamento(java.util.Collection newTipi_trattamento) {
		tipi_trattamento = newTipi_trattamento;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tipo_missione'
	 */
	public void setTipo_missione(it.cnr.contab.missioni00.tabrif.bulk.Tipo_missioneBulk newTipo_missione) {
		tipo_missione = newTipo_missione;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tipo_rapporto'
	 */
	public void setTipo_rapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipo_rapporto) {
		tipo_rapporto = newTipo_rapporto;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'tipo_trattamento'
	 */
	public void setTipo_trattamento(it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk newTipo_trattamento) {
		tipo_trattamento = newTipo_trattamento;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'totaleEsenteDiariaDelGiorno'
	 */
	public void setTotaleEsenteDiariaDelGiorno(java.math.BigDecimal newTotaleEsenteDiariaDelGiorno) {
		totaleEsenteDiariaDelGiorno = newTotaleEsenteDiariaDelGiorno;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'totaleNettoDiariaDelGiorno'
	 */
	public void setTotaleNettoDiariaDelGiorno(java.math.BigDecimal newTotaleNettoDiariaDelGiorno) {
		totaleNettoDiariaDelGiorno = newTotaleNettoDiariaDelGiorno;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'totaleEsenteRimborsoDelGiorno'
	 */
	public void setTotaleEsenteRimborsoDelGiorno(java.math.BigDecimal newTotaleEsenteRimborsoDelGiorno) {
		totaleEsenteRimborsoDelGiorno = newTotaleEsenteRimborsoDelGiorno;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'totaleRimborsoDelGiorno'
	 */
	public void setTotaleRimborsoDelGiorno(java.math.BigDecimal newTotaleRimborsoDelGiorno) {
		totaleRimborsoDelGiorno = newTotaleRimborsoDelGiorno;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'totaleSpeseDelGiorno'
	 */
	public void setTotaleSpeseDelGiorno(java.math.BigDecimal newTotaleSpeseDelGiorno) {
		totaleSpeseDelGiorno = newTotaleSpeseDelGiorno;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'v_terzo'
	 */
	public void setV_terzo(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk newV_terzo) {
		v_terzo = newV_terzo;
	}
	/**
	 * Il metodo sincronizza la scadenza di obbligazione del compenso (positiva) con la medesima, ma con numerazione
	 * ancora negativa, contenuta nel deferredSaldi della missione.
	 * Nel caso in cui la quota esente e l'importo netto della diaria siano uguali --> il compenso non ha obbligazione
	 * Nel caso in cui l'anticipo e' maggiore della missione --> il compenso ha obbligazione 
	 */
	public void sincronizzaObbligazioneCompenso(it.cnr.jada.UserContext uc) 
	{
		if(getCompenso().getObbligazioneScadenzario()==null)
			return;

		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = getCompenso().getObbligazioneScadenzario().getObbligazione();
		PrimaryKeyHashMap relazioni = getCompenso().getRelationsDocContForSaldi();
		if (relazioni != null) 
		{
			Long temporaneo = (Long)relazioni.get(obbligazione);
			if (temporaneo != null && temporaneo.longValue()<0 && relazioni != null && !relazioni.isEmpty()) 
			{
				it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obblTemporanea = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)obbligazione.clone();
				obblTemporanea.setPg_obbligazione(temporaneo);
				if (getDefferredSaldi() != null) 
				{
					java.util.Map data = (java.util.Map) getDefferredSaldi().get(obblTemporanea);
					removeFromDefferredSaldi(obblTemporanea);
					addToDefferredSaldi(obbligazione, data);

					//	Imposto la seguente Flag in modo che l'aggiornamento dei saldi riconosca
					//	che sto aggiornando i saldi per una obbligazione con numerazione
					//	positiva ma che non e' mai stata salvata fuori dalla transazione
					//	corrente
					java.util.Map values = (java.util.Map)getDefferredSaldi().get(obbligazione);
					if (values == null)
						values = new java.util.HashMap();

					values.put("isObbTemp", Boolean.TRUE);
				}
			}
		}
	}
	/**
	 * Il metodo sincronizza le scadenze di obbligazione del compenso (positiva) con le medesime, ma con numerazione
	 * ancora negativa, contenuta nel deferredSaldi della missione.
	 * Nel caso in cui la quota esente e l'importo netto della diaria siano uguali --> il compenso non ha obbligazione
	 * Nel caso in cui l'anticipo e' maggiore della missione --> il compenso ha obbligazione 
	 */
	public void sincronizzaObbligazioniCancellateCompenso(it.cnr.jada.UserContext uc) 
	{
		if(getCompenso().getDocumentiContabiliCancellati()==null || getCompenso().getDocumentiContabiliCancellati().isEmpty())
			return;

		PrimaryKeyHashMap relazioni = getCompenso().getRelationsDocContForSaldi();
		if (relazioni == null || relazioni.isEmpty()) 
			return;

		for ( Iterator i = getCompenso().getDocumentiContabiliCancellati().iterator(); i.hasNext(); )
		{

			Obbligazione_scadenzarioBulk scadCancellata = (Obbligazione_scadenzarioBulk) i.next();
			it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazioneCancellata = scadCancellata.getObbligazione();

			Long temporaneo = (Long)relazioni.get(obbligazioneCancellata);
			if (temporaneo != null && temporaneo.longValue()<0 ) 
			{
				it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obblTemporanea = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)obbligazioneCancellata.clone();
				obblTemporanea.setPg_obbligazione(temporaneo);
				if (getDefferredSaldi() != null) 
				{
					java.util.Map data = (java.util.Map) getDefferredSaldi().get(obblTemporanea);
					removeFromDefferredSaldi(obblTemporanea);
					addToDefferredSaldi(obbligazioneCancellata, data);

					//	Imposto la seguente Flag in modo che l'aggiornamento dei saldi riconosca
					//	che sto aggiornando i saldi per una obbligazione con numerazione
					//	positiva ma che non e' mai stata salvata fuori dalla transazione
					//	corrente
					java.util.Map values = (java.util.Map)getDefferredSaldi().get(obbligazioneCancellata);
					if (values == null)
						values = new java.util.HashMap();

					values.put("isObbTemp", Boolean.TRUE);
				}
			}
		}
	}
	/**
	 * Il metodo sincronizza le scadenze elaborate dalla missione ad ogni rientro da ogni aggiornamento 
	 * manuale della scadenza associata. 
	 * Se la scadenza appena aggiornata appartiene alla stessa obbligazione di una delle scadenze elaborate,
	 * l'utente avrebbe potute modificare una di queste.
	 * Se non dovessi trovare una delle scadenze elaborate dalla missione e appartenente alla stessa obbligazione 
	 * di quella appena aggiornata significa che l'utente l'ha eliminata fisicamente.
	 */
	public void sincronizzaScadenzeCancellate(Obbligazione_scadenzarioBulk newScadenza)
	{
		if(getDocumentiContabiliCancellati()==null || getDocumentiContabiliCancellati().isEmpty())
			return;

		if(!newScadenza.getObbligazione().equalsByPrimaryKey(getObbligazione_scadenzario().getObbligazione()))
			return;

		boolean trovata=false;
		BulkList coll = newScadenza.getObbligazione().getObbligazione_scadenzarioColl();
		if(coll == null)
			return;

		for ( Iterator c = ((Vector)getDocumentiContabiliCancellati().clone()).iterator(); c.hasNext(); )
		{
			Obbligazione_scadenzarioBulk aScadCanc = (Obbligazione_scadenzarioBulk)c.next();		
			trovata = false;

			for ( Iterator i = coll.iterator(); i.hasNext(); )
			{
				Obbligazione_scadenzarioBulk aScadenza = (Obbligazione_scadenzarioBulk)i.next();

				if(aScadenza.equalsByPrimaryKey(aScadCanc))
				{
					getDocumentiContabiliCancellati().remove(aScadCanc);
					getDocumentiContabiliCancellati().add(aScadenza);
					trovata = true;
				}	
			}
			if(!trovata && aScadCanc.getObbligazione().equalsByPrimaryKey(newScadenza.getObbligazione()))
				getDocumentiContabiliCancellati().remove(aScadCanc);		
		}
	}

	/**
	 * Il metodo verifica che il periodo (data e ora) di inizio e fine missione sia corretto.
	 * Se non sono ancora state configurate le tappe preparo la collection
	 * contenente i singoli giorni compresi tra la data di inizio e fine missione
	 */

	public void validaPeriodoMissione() throws it.cnr.jada.action.MessageToUser
	{
		checkValiditaInizioFineMissione();
		riempiElencoGiorniColl();
	}
	/**
	 * Il metod valida i dati inseriti nel Tab Anagrafico, ossia :
	 * - che sia valorizzato il terzo
	 * - che siano selezionati modalita' di pagamento, tipo rapporto e inquadramento 
	 */

	public void validaTabAnagrafico() throws it.cnr.jada.action.MessageToUser
	{
		if(getCd_terzo() == null || getV_terzo().getCd_terzo() == null)
			throw new it.cnr.jada.action.MessageToUser( "Selezionare un Terzo" );	

		if(getCd_modalita_pag() == null)
			throw new it.cnr.jada.action.MessageToUser( "Selezionare la Modalita' di Pagamento" );

		if(getPg_banca() == null)
			throw new it.cnr.jada.action.MessageToUser( "Selezionare un'altra Modalita' di Pagamento" );		

		if(getCd_tipo_rapporto() == null)
			throw new it.cnr.jada.action.MessageToUser( "Selezionare il Tipo Rapporto" );

		if(getPg_rif_inquadramento() == null)
			throw new it.cnr.jada.action.MessageToUser( "Selezionare l' Inquadramento" );
	} 
	/**
	 * Il metodo valida i dati inseriti nel Tab Testata, ossia :
	 * - che sia valorizzato la descrizione
	 * - che il periodo (data e ora) di inizio e fine missione sia corretto.
	 * - che la data di registrazione non sia nulla. Gli altri controlli sulla data 
	 *	  di registrazione vengono fatti dal bottone salva/annulla vicino al campo
	 */

	public void validaTabTestata() throws it.cnr.jada.action.MessageToUser
	{
		if(getDt_registrazione() == null)
			throw new it.cnr.jada.action.MessageToUser( "Inserire la data di registrazione !" );

		validaPeriodoMissione();

		if(getDs_missione() == null)
			throw new it.cnr.jada.action.MessageToUser( "Inserire una descrizione !" );
		if (getDt_registrazione().after(dataInizioObbligoRegistroUnico))
		{
			if(getStato_liquidazione()==null)
				throw new it.cnr.jada.action.MessageToUser("Inserire lo stato della liquidazione!");

		}	
		//	if(!isMissioneDefinitiva() && getCd_tipo_missione() == null)
		//		throw new it.cnr.jada.action.MessageToUser( "Selezionare l' ambito della missione" );
	} 
	/**
	 * Il metodo valida la missione che si sta creando/modificando
	 */
	public void validate() throws ValidationException 
	{
		/*************  ULTIME INIZIALIZZAZIONI MISSIONE ****************/
		setFlagComuni();
		calcolaConsuntivi();	// Ricalcolo i totali del consuntivo


		/*************  CONTROLLI VALIDITA CAMPI MISSIONE ****************/
		super.validate();

		if((getTappeMissioneColl() == null) || (getTappeMissioneColl().isEmpty()))
			throw new ValidationException("Configurare le tappe !");		

		if(!isTappeConfigurate())
			throw new ValidationException("Selezionare il bottone di Fine Configurazione Tappe !");

		// Significa verificare che la diaria sia stata creata
		if((!isSpeseInserite()) && (!isSalvataggioTemporaneo()))	
			throw new ValidationException("Selezionare il bottone di Fine Inserimento Spese !");

		// Importo totale della missione nullo
		if(!isSalvataggioTemporaneo() &&
				(getIm_spese()==null || getIm_spese().compareTo(new BigDecimal(0))==0) &&
				(getIm_diaria_netto()==null || getIm_diaria_netto().compareTo(new BigDecimal(0))==0) &&
				(getIm_rimborso()==null || getIm_rimborso().compareTo(new BigDecimal(0))==0)
				)
			throw new ValidationException("Impossibile proseguire con il salvataggio! La missione ha importo nullo.");
		// 	Il tipo trattamento e' necessario per generare il compenso se ho della
		//	Diaria all'estero
		if((getTipo_trattamento() == null || getTipo_trattamento().getCd_trattamento() == null) &&
				!isSalvataggioTemporaneo() && isCompensoObbligatorio() && isMissioneDefinitiva())
			throw new ValidationException("Selezionare il Tipo Trattamento !");

		// 	La validazione della scadenza di obbligazione legata alla missione viene fatta nel modificaConBulk
		//	e nell creaConBulk
	}

	/**
	 * Il metodo verifica l'ultima tappa configurata
	 * Caso in cui :
	 *		dt_inizio_missione = 20/3/2002 4:00		in Italia
	 *		dt_fine_missione = 21/3/2002 3:00		in Italia
	 *
	 * Il risultato deve essere una sola tappa dal 20/3/2002 4:00 al 21/3/2002 3:00
	 *
	 * Caso in cui :
	 * 		dt_inizio_missione = 20/3/2002 1:00		in Italia
	 *		dt_fine_missione = 21/3/2002 1:00		in Italia
	 *
	 * Il risultato deve essere una sola tappa dal 20/3/2002 1:00 al 21/3/2002 1:00
	 */

	public void verificaUltimaTappa()
	{
		Missione_tappaBulk tappa=null;
		Missione_tappaBulk penultimaTappa=null; 

		tappa = (Missione_tappaBulk)getTappeMissioneColl().get(getTappeMissioneColl().size()-1);

		if((getTappeMissioneColl().size()-2) >= 0)
			penultimaTappa = (Missione_tappaBulk)getTappeMissioneColl().get(getTappeMissioneColl().size()-2);

		if((tappa.getDt_inizio_tappa().after(tappa.getDt_fine_tappa())) || (tappa.getDt_inizio_tappa().equals(tappa.getDt_fine_tappa())))
		{	
			//if((!tappa.getFl_comune_estero().booleanValue()) && (penultimaTappa != null))
			if((penultimaTappa != null))
				penultimaTappa.setDt_fine_tappa((java.sql.Timestamp)getDt_fine_missione().clone());

			int indice = getTappeMissioneColl().size()-1	;
			Missione_tappaBulk aTappa = (Missione_tappaBulk)getTappeMissioneColl().get(indice);
			aTappa.setToBeDeleted();	
			removeFromTappeMissioneColl(indice);
		}	
	}

	public boolean isTappeEstereCoerenti()
	{
		if(getTappeMissioneColl() == null || getTappeMissioneColl().isEmpty())
			return false;

		Missione_tappaBulk tappa=null;
		Boolean rimborso=null;
		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )	
		{
			tappa = (Missione_tappaBulk) i.next();
			if(tappa.isEstera() && rimborso == null)
				rimborso = tappa.getFl_rimborso();
			if (tappa.isEstera() && tappa.getFl_rimborso().compareTo(rimborso)!=0)
				return false;
		}
		Boolean diaria=null;
		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )	
		{
			tappa = (Missione_tappaBulk) i.next();
			if(tappa.isEstera() && diaria == null)
				diaria = tappa.getFl_no_diaria();
			if (tappa.isEstera() && tappa.getFl_no_diaria().compareTo(diaria)!=0)
				return false;
		}
		return true;
	}
	public boolean isRimborsoValidoPerDurataTappeEstere()
	{
		if(getTappeMissioneColl() == null || getTappeMissioneColl().isEmpty())
			return false;

		Missione_tappaBulk tappa=null;
		//Boolean rimborso=null;
		Long oreTappa=null;
		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )	
		{
			tappa = (Missione_tappaBulk) i.next();
			if(tappa.isEstera() && tappa.getFl_rimborso())
			{
				//conto le ore delle tappe estere
				java.util.GregorianCalendar inizioTappa = (java.util.GregorianCalendar)getGregorianCalendar(tappa.getDt_inizio_tappa()).clone();
				java.util.GregorianCalendar fineTappa = (java.util.GregorianCalendar)getGregorianCalendar(tappa.getDt_inizio_tappa()).clone();
				oreTappa =  fineTappa.getTime().getTime() - inizioTappa.getTime().getTime();	
			}

		}		
		return false;
	}
	public boolean isMissioneConRimborso()
	{
		if(getTappeMissioneColl() == null || getTappeMissioneColl().isEmpty())
			return false;

		Missione_tappaBulk tappa=null;
		for ( Iterator i = getTappeMissioneColl().iterator(); i.hasNext(); )	
		{
			tappa = (Missione_tappaBulk) i.next();
			if(tappa.isEstera() && tappa.getFl_rimborso())
				return true;
		}		
		return false;
	}
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorg:descrizione"))
	public String getDsUnitaOrganizzativa(){
		if (getUnitaOrganizzativa() == null)
			return null;
		return getUnitaOrganizzativa().getDs_unita_organizzativa();
	}
	public Dictionary getStato_liquidazioneKeys() {
		return STATO_LIQUIDAZIONE;
	}
	public java.sql.Timestamp getDataInizioObbligoRegistroUnico() {
		return dataInizioObbligoRegistroUnico;
	}
	public void setDataInizioObbligoRegistroUnico(
			java.sql.Timestamp dataInizioObbligoRegistroUnico) {
		this.dataInizioObbligoRegistroUnico = dataInizioObbligoRegistroUnico;
	}
	public Integer getEsercizioObblGeMis() {
		return esercizioObblGeMis;
	}
	public void setEsercizioObblGeMis(Integer esercizioObblGeMis) {
		this.esercizioObblGeMis = esercizioObblGeMis;
	}
	public Integer getEsercizioOriObblGeMis() {
		return esercizioOriObblGeMis;
	}
	public void setEsercizioOriObblGeMis(Integer esercizioOriObblGeMis) {
		this.esercizioOriObblGeMis = esercizioOriObblGeMis;
	}
	public String getCdsObblGeMis() {
		return cdsObblGeMis;
	}
	public void setCdsObblGeMis(String cdsObblGeMis) {
		this.cdsObblGeMis = cdsObblGeMis;
	}
	public Long getPgObblGeMis() {
		return pgObblGeMis;
	}
	public void setPgObblGeMis(Long pgObblGeMis) {
		this.pgObblGeMis = pgObblGeMis;
	}
	public String getGaeGeMis() {
		return gaeGeMis;
	}
	public void setGaeGeMis(String gaeGeMis) {
		this.gaeGeMis = gaeGeMis;
	}
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;		
	}
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(
			BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	public boolean isMissioneFromGemis()
	{
		if(getIdRimborsoMissione() != null)
			return true;

		return false;
	}
	public Integer getEsercizioAnticipoGeMis() {
		return esercizioAnticipoGeMis;
	}
	public void setEsercizioAnticipoGeMis(Integer esercizioAnticipoGeMis) {
		this.esercizioAnticipoGeMis = esercizioAnticipoGeMis;
	}
	public String getCdsAnticipoGeMis() {
		return cdsAnticipoGeMis;
	}
	public void setCdsAnticipoGeMis(String cdsAnticipoGeMis) {
		this.cdsAnticipoGeMis = cdsAnticipoGeMis;
	}
	public Long getPgAnticipoGeMis() {
		return pgAnticipoGeMis;
	}
	public void setPgAnticipoGeMis(Long pgAnticipoGeMis) {
		this.pgAnticipoGeMis = pgAnticipoGeMis;
	}
	public java.util.Dictionary getDaRimborsoDaCompletareKeys() {
		return DA_GEMIS_DA_COMPLETARE;
	}
	public Long getPgMissioneFromGeMis() {
		return pgMissioneFromGeMis;
	}
	public void setPgMissioneFromGeMis(Long pgMissioneFromGeMis) {
		this.pgMissioneFromGeMis = pgMissioneFromGeMis;
	}
	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(StrServ.lpad(this.getPg_missione().toString(),9,"0"));
		return nomeFile.toString();
	}
	public BigDecimal getImportoDaRimborsare() {
		return importoDaRimborsare;
	}
	public void setImportoDaRimborsare(BigDecimal importoDaRimborsare) {
		this.importoDaRimborsare = importoDaRimborsare;
	}
	public String getCdrGeMis() {
		return cdrGeMis;
	}
	public void setCdrGeMis(String cdrGeMis) {
		this.cdrGeMis = cdrGeMis;
	}
	public String getVoceGeMis() {
		return voceGeMis;
	}
	public void setVoceGeMis(String voceGeMis) {
		this.voceGeMis = voceGeMis;
	}

	@Override
	public List<String> getStorePath() {
		MissioniCMISService missioniCMISService = SpringUtil.getBean("missioniCMISService",
				MissioniCMISService.class);
		List<String> paths = new ArrayList<String>();
		if (Optional.ofNullable(getIdRimborsoMissione()).isPresent()) {
			paths.add(missioniCMISService.getStorageObjectBykey(getIdFolderOrdineMissione()).getPath());
			if (isMissioneFromGemis() && getIdFolderRimborsoMissione() != null) {
				paths.add(missioniCMISService.getCMISPathFromFolderRimborso(this));
			}
		}
		return paths;
	}
	public static boolean isAbilitatoCancellazioneMissioneFromGemis(it.cnr.jada.UserContext param0) throws ComponentException, RemoteException{
		return Utility.getRuoloComponentSession().isAbilitatoCancellazioneMissioneGemis(param0);
	}

	@Override
	public String getCd_tipo_doc() {
		return this.getCd_tipo_doc_amm();
	}

	@Override
	public Long getPg_doc() {
		return this.getPg_doc_amm();
	}

	/**
	 * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
	 */
	@Override
	public Timestamp getDtInizioLiquid() {
		return null;
	}

	/**
	 * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
	 */
	@Override
	public Timestamp getDtFineLiquid() {
		return null;
	}

	/**
	 * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
	 */
	@Override
	public String getTipoLiquid() {
		return null;
	}

	/**
	 * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
	 */
	@Override
	public Long getReportIdLiquid() {
		return null;
	}

	@Override
	public Timestamp getDt_contabilizzazione() {
		return this.getDt_registrazione();
	}
}
