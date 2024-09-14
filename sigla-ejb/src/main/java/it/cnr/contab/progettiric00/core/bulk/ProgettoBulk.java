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

package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaNuovoBP;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.contab.progettiric00.tabrif.bulk.Tipo_progettoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.si.spring.storage.StorageDriver;

public class ProgettoBulk extends ProgettoBase implements AllegatoParentBulk {

	public static final String TIPO_STATO_PROPOSTA  ="P";
	public static final String TIPO_STATO_APPROVATO ="A";

	public static final String TIPO_FASE_PREVISIONE  ="P";
	public static final String TIPO_FASE_GESTIONE ="G";
	public static final String TIPO_FASE_NON_DEFINITA ="X";
	
	public static final String DURATA_PROGETTO_PLURIENNALE ="P";
	public static final String DURATA_PROGETTO_ANNUALE 	="A";

	public static final Integer LIVELLO_PROGETTO_PRIMO 	= new Integer(1);
	public static final Integer LIVELLO_PROGETTO_SECONDO  = new Integer(2);
	public static final Integer LIVELLO_PROGETTO_TERZO 	= new Integer(3);
	public static final Integer LIVELLO_PROGETTO_ALL 	= new Integer(4);
	
	public static final String LABEL_AREA_PROGETTUALE = "Area Progettuale";
	public static final String LABEL_PROGETTO = "Progetto";
	public static final String LABEL_COMMESSA = "Commessa";
	public static final String LABEL_MODULO = "Modulo";

	public static final String TIPO_FASE_SEARCH_SOLO_PREVISIONE  ="P";
	public static final String TIPO_FASE_SEARCH_SOLO_GESTIONE ="G";
	public static final String TIPO_FASE_SEARCH_PREVISIONE_E_GESTIONE ="X";
	public static final String TIPO_FASE_SEARCH_ALL ="A";

	public static final String STATO_RIAPERTURA = "RIA";

	private it.cnr.jada.bulk.BulkList	workpackage_collegati = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList	workpackage_disponibili  = new it.cnr.jada.bulk.BulkList();

	public final static Dictionary statoKeys;
	static {
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(TIPO_STATO_PROPOSTA,"Proposta");
		statoKeys.put(TIPO_STATO_APPROVATO,"Approvato");
	};
	public final static Dictionary tipo_faseKeys;
	static {
		tipo_faseKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_faseKeys.put(TIPO_FASE_PREVISIONE,"Previsione");
		tipo_faseKeys.put(TIPO_FASE_GESTIONE,"Gestione");
	};

	public final static Dictionary tipo_faseAllKeys;
	static {
		tipo_faseAllKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_faseAllKeys.put(TIPO_FASE_PREVISIONE,"Previsione");
		tipo_faseAllKeys.put(TIPO_FASE_GESTIONE,"Gestione");
		tipo_faseAllKeys.put(TIPO_FASE_NON_DEFINITA,"Non definita");
	};
	
	public final static Dictionary tipo_faseSearchKeys;
	static {
		tipo_faseSearchKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_faseSearchKeys.put(TIPO_FASE_SEARCH_SOLO_PREVISIONE,"Solo Previsione");
		tipo_faseSearchKeys.put(TIPO_FASE_SEARCH_SOLO_GESTIONE,"Solo Gestione");
		tipo_faseSearchKeys.put(TIPO_FASE_SEARCH_PREVISIONE_E_GESTIONE,"Previsione e Gestione");
		tipo_faseSearchKeys.put(TIPO_FASE_SEARCH_ALL,"Tutto");
	};

		public final static Dictionary durata_progettoKeys;
		static {
		durata_progettoKeys = new it.cnr.jada.util.OrderedHashtable();
		durata_progettoKeys.put(DURATA_PROGETTO_PLURIENNALE,"Pluriennale");
		durata_progettoKeys.put(DURATA_PROGETTO_ANNUALE,"Annuale");
	};
	public final static Dictionary livello_progettoKeys;
		static {
		livello_progettoKeys = new it.cnr.jada.util.OrderedHashtable();
		livello_progettoKeys.put(LIVELLO_PROGETTO_PRIMO,"Progetto");
		livello_progettoKeys.put(LIVELLO_PROGETTO_SECONDO,"Commessa");
		livello_progettoKeys.put(LIVELLO_PROGETTO_TERZO,"Modulo di Attività");
		livello_progettoKeys.put(LIVELLO_PROGETTO_ALL,"Sottogruppo");
		for(int i=4;i<100;i++)
		  livello_progettoKeys.put(new Integer(i),"Sottogruppo");
	};
	public final static Dictionary livello_progetto2016Keys;
	static {
		livello_progetto2016Keys = new it.cnr.jada.util.OrderedHashtable();
		livello_progetto2016Keys.put(LIVELLO_PROGETTO_PRIMO,"Area Progettuale");
		livello_progetto2016Keys.put(LIVELLO_PROGETTO_SECONDO,"Progetto");
		livello_progetto2016Keys.put(LIVELLO_PROGETTO_ALL,"Sottogruppo");
		for(int i=3;i<100;i++)
			livello_progetto2016Keys.put(new Integer(i),"Sottogruppo");
	};
	
    public final static Map<String,String> statoOfKeys = Arrays.asList(StatoProgetto.values())
            .stream()
            .collect(Collectors.toMap(
            		StatoProgetto::value,
            		StatoProgetto::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    
	private Tipo_progettoBulk tipo;
	private Unita_organizzativaBulk unita_organizzativa;
	private TerzoBulk responsabile;
	private DivisaBulk divisa;
	protected ProgettoBulk progettopadre;
	private DipartimentoBulk dipartimento;
	protected java.util.Collection progetti_figli;
	private BulkList dettagli = new BulkList();
	private BulkList dettagliFinanziatori = new BulkList();
	private BulkList dettagliPartner_esterni = new BulkList();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoTotale = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAnnoCorrente = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAltriAnni = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList speseEsercizio = new BulkList();
	private Commessa_spesaBulk spese;
	private Parametri_cdsBulk parametriCds;
	private Pdg_programmaBulk pdgProgramma;
	private Pdg_missioneBulk pdgMissione;
	private Boolean fl_previsione;
	private Boolean fl_gestione;
	private String tipoFaseToSearch;
	private Progetto_other_fieldBulk otherField;
	private BulkList<Pdg_moduloBulk> pdgModuli = new BulkList();
	private BulkList<V_saldi_voce_progettoBulk> vociBilancioMovimentate = new BulkList<V_saldi_voce_progettoBulk>();
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	private BulkList<Progetto_rimodulazioneBulk> rimodulazioni = new BulkList<Progetto_rimodulazioneBulk>();
	private BulkList<ContrattoBulk> contratti = new BulkList<ContrattoBulk>();
	private BulkList<Progetto_anagraficoBulk> anagraficheProgetto = new BulkList<Progetto_anagraficoBulk>();
	private boolean amministra = Boolean.FALSE;

	public ProgettoBulk() {
		super();
	}
	public ProgettoBulk(java.lang.Integer esercizio,java.lang.Integer pg_progetto,java.lang.String tipo_fase) {
		super(esercizio,pg_progetto,tipo_fase);
	}

	/**
	* Aggiunge il progetto figlio alla collezione progetti_figli
	*
	* @param figlio da aggiungere
	*/
	public void addToProgetti_figli(ProgettoBulk figlio) {
	  if (progetti_figli == null)
		progetti_figli = new java.util.LinkedList();
	  progetti_figli.add(figlio);
	}
	public int addToDettagli(Progetto_uoBulk dett) {
		dett.setPg_progetto( getPg_progetto() );
		dettagli.add(dett);
		return dettagli.size()-1;
	}

	public int addToDettagliFinanziatori(Progetto_finanziatoreBulk dett) {
		dett.setPg_progetto( getPg_progetto() );
		dettagliFinanziatori.add(dett);
		return dettagliFinanziatori.size()-1;
	}

	public int addToDettagliPartner_esterni(Progetto_partner_esternoBulk dett) {
		dett.setPg_progetto( getPg_progetto() );
		dettagliPartner_esterni.add(dett);
		return dettagliPartner_esterni.size()-1;
	}

	public int addToDettagliPianoEconomicoTotale(Progetto_piano_economicoBulk dett) {
		dett.setProgetto( this );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoTotale.add(dett);
		return dettagliPianoEconomicoTotale.size()-1;
	}

	public int addToDettagliPianoEconomicoAnnoCorrente(Progetto_piano_economicoBulk dett) {
		dett.setProgetto( this );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoAnnoCorrente.add(dett);
		return dettagliPianoEconomicoAnnoCorrente.size()-1;
	}

	public int addToDettagliPianoEconomicoAltriAnni(Progetto_piano_economicoBulk dett) {
		dett.setProgetto( this );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoAltriAnni.add(dett);
		return dettagliPianoEconomicoAltriAnni.size()-1;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {dettagli,dettagliFinanziatori,dettagliPartner_esterni,dettagliPianoEconomicoTotale,dettagliPianoEconomicoAnnoCorrente,dettagliPianoEconomicoAltriAnni,anagraficheProgetto};
	}

public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
public java.lang.Integer getCd_responsabile_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = this.getResponsabile();
	if (responsabile == null)
		return null;
	return responsabile.getCd_terzo();
}
public java.lang.String getCd_tipo_progetto() {
	Tipo_progettoBulk tipo = this.getTipo();
	if (tipo == null)
		return null;
	return tipo.getCd_tipo_progetto();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.20.58)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getDettagli() {
	return dettagli;
}
public it.cnr.jada.bulk.BulkList getDettagliFinanziatori() {
	return dettagliFinanziatori;
}
public BulkList<Progetto_piano_economicoBulk> getDettagliPianoEconomicoTotale() {
	return dettagliPianoEconomicoTotale;
}
public BulkList<Progetto_piano_economicoBulk> getDettagliPianoEconomicoAnnoCorrente() {
	return dettagliPianoEconomicoAnnoCorrente;
}
public BulkList<Progetto_piano_economicoBulk> getDettagliPianoEconomicoAltriAnni() {
	return dettagliPianoEconomicoAltriAnni;
}

/**
 * Restituisce il valore della proprietà 'rOprogetto'
 *
 * @return Il valore della proprietà 'rOprogetto'
 */
public boolean isROprogetto() {
	/* la prima condizione perchè il campo appariva
	 * non utilizzabile nelle ricerchi guidate */ 
	if (getCrudStatus() == UNDEFINED)
		return false;

	return getProgettopadre() == null ||
			getProgettopadre().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL ||
	         !getProgettopadre().isOperabile();
}

public boolean isROfind_nodo_padre() {

	if (getCrudStatus() == UNDEFINED)
		return false;

	return getProgettopadre() != null && !getProgettopadre().isOperabile();
}

/**
 * Restituisce il valore della proprietà 'rOprogetto'
 *
 * @return Il valore della proprietà 'rOprogetto'
 */
public boolean isROprogettopadre() {

	return true;
}
public boolean isROprogettoCodifica() {
	if (getParametriCds()!=null && getParametriCds().getFl_progetto_numeratore() != null) {
		if (getParametriCds().getFl_progetto_numeratore().booleanValue())
			return true;
		else
			return false;
	}
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.27.32)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.28)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getResponsabile() {
	return responsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 14.29.45)
 * @return it.cnr.contabres.progettiric00.tabrif.bulk.Tipo_progettoBulk
 */
public Tipo_progettoBulk getTipo() {
	return tipo;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.24.21)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public boolean isRODt_fine() {
	return getDt_fine() != null;
}
public Progetto_uoBulk removeFromDettagli(int index) {
	Progetto_uoBulk dett = (Progetto_uoBulk)dettagli.remove(index);
	return dett;
}
public Progetto_finanziatoreBulk removeFromDettagliFinanziatori(int index) {
	Progetto_finanziatoreBulk dett = (Progetto_finanziatoreBulk)dettagliFinanziatori.remove(index);
	return dett;
}
public Progetto_partner_esternoBulk removeFromDettagliPartner_esterni(int index) {
	Progetto_partner_esternoBulk dett = (Progetto_partner_esternoBulk)dettagliPartner_esterni.remove(index);
	return dett;
}

public Progetto_piano_economicoBulk removeFromDettagliPianoEconomicoTotale(int index) {
	Progetto_piano_economicoBulk dett = (Progetto_piano_economicoBulk)dettagliPianoEconomicoTotale.remove(index);
	return dett;
}
public Progetto_piano_economicoBulk removeFromDettagliPianoEconomicoAnnoCorrente(int index) {
	Progetto_piano_economicoBulk dett = (Progetto_piano_economicoBulk)dettagliPianoEconomicoAnnoCorrente.remove(index);
	return dett;
}
public Progetto_piano_economicoBulk removeFromDettagliPianoEconomicoAltriAnni(int index) {
	Progetto_piano_economicoBulk dett = (Progetto_piano_economicoBulk)dettagliPianoEconomicoAltriAnni.remove(index);
	return dett;
}

public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
}
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.getResponsabile().setCd_terzo(cd_responsabile_terzo);
}
public void setCd_tipo_progetto(java.lang.String cd_tipo_fondo) {
	this.getTipo().setCd_tipo_progetto(cd_tipo_fondo);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.20.58)
 * @param newDettagli it.cnr.jada.bulk.BulkList
 */
public void setDettagli(it.cnr.jada.bulk.BulkList newDettagli) {
	dettagli = newDettagli;
}
public void setDettagliFinanziatori(it.cnr.jada.bulk.BulkList newDettagliFinanziatori) {
	dettagliFinanziatori = newDettagliFinanziatori;
}
public void setDettagliPianoEconomicoTotale(BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoTotale) {
	this.dettagliPianoEconomicoTotale = dettagliPianoEconomicoTotale;
}
public void setDettagliPianoEconomicoAnnoCorrente(BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAnnoCorrente) {
	this.dettagliPianoEconomicoAnnoCorrente = dettagliPianoEconomicoAnnoCorrente;
}
public void setDettagliPianoEconomicoAltriAnni(BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAltriAnni) {
	this.dettagliPianoEconomicoAltriAnni = dettagliPianoEconomicoAltriAnni;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.27.32)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.28)
 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setResponsabile(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newResponsabile) {
	responsabile = newResponsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 14.29.45)
 * @param newTipo it.cnr.contabres.progettiric00.tabrif.bulk.Tipo_progettoBulk
 */
public void setTipo(Tipo_progettoBulk newTipo) {
	tipo = newTipo;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.24.21)
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
	/**
	 * Returns the progettopadre.
	 * @return ProgettoBulk
	 */
	public ProgettoBulk getProgettopadre() {
		return progettopadre;
	}

	/**
	 * Sets the progettopadre.
	 * @param progettopadre The progettopadre to set
	 */
	public void setProgettopadre(ProgettoBulk progettopadre) {
		this.progettopadre = progettopadre;
	}


	public java.lang.Integer getPg_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getPg_progetto();
	}
	public Integer getEsercizio_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getEsercizio();
	}
	public String getTipo_fase_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getTipo_fase();
	}
	/**
	 * Sets the pg_progetto_padre.
	 * @param progetto_padre The pg_progetto_padre to set
	 */
	public void setPg_progetto_padre(java.lang.Integer progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new ProgettoBulk());
		this.getProgettopadre().setPg_progetto(progetto_padre);
	}
    public void setEsercizio_progetto_padre(Integer esercizio_progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new ProgettoBulk());
		this.getProgettopadre().setEsercizio(esercizio_progetto_padre);
    }
    public void setTipo_fase_progetto_padre(String tipo_fase_progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new ProgettoBulk());
		this.getProgettopadre().setTipo_fase(tipo_fase_progetto_padre);
    }
	public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setProgettopadre(new ProgettoBulk());
		setDipartimento(new DipartimentoBulk());
//		setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
		return super.initializeForFreeSearch(bp,context);
	}
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setTipoFaseToSearch(ProgettoBulk.TIPO_FASE_SEARCH_ALL);
		return super.initializeForSearch(bp,context);
	}
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		if((bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT)&&(bp instanceof TestataProgettiRicercaBP || bp instanceof TestataProgettiRicercaNuovoBP)){
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setUnita_organizzativa(unita_organizzativa);
		setStato(ProgettoBulk.TIPO_STATO_PROPOSTA);
		setFl_previsione(Boolean.FALSE);
		setFl_gestione(Boolean.FALSE);
	  }
	  return this;
	}
	/**
	 * Returns the progetti_figli.
	 * @return java.util.Collection
	 */
	public java.util.Collection getProgetti_figli() {
		return progetti_figli;
	}

	/**
	 * Sets the progetti_figli.
	 * @param progetti_figli The progetti_figli to set
	 */
	public void setProgetti_figli(java.util.Collection progetti_figli) {
		this.progetti_figli = progetti_figli;
	}
	/**
	 * Returns the dettagliPartner_esterni.
	 * @return BulkList
	 */
	public BulkList getDettagliPartner_esterni() {
		return dettagliPartner_esterni;
	}

	/**
	 * Sets the dettagliPartner_esterni.
	 * @param dettagliPartner_esterni The dettagliPartner_esterni to set
	 */
	public void setDettagliPartner_esterni(BulkList dettagliPartner_esterni) {
		this.dettagliPartner_esterni = dettagliPartner_esterni;
	}

	public void setWorkpackage_collegati(BulkList workpackage_collegati) {
		this.workpackage_collegati = workpackage_collegati;
	}

	public BulkList getWorkpackage_collegati() {
		return workpackage_collegati;
	}

	public void setWorkpackage_disponibili(BulkList workpackage_disponibili) {
		this.workpackage_disponibili=workpackage_disponibili;
	}

	public BulkList getWorkpackage_disponibili() {
		return workpackage_disponibili;
	}

	public int addToWorkpackage_collegati(WorkpackageBulk wp) {
		this.workpackage_collegati.add(wp);
		return this.workpackage_collegati.size()-1;
	}

	public int addToWorkpackage_disponibili(WorkpackageBulk wp) {
		this.workpackage_disponibili.add(wp);
		return this.workpackage_disponibili.size()-1;
	}

	public WorkpackageBulk addToWorkpackage_collegati(int index) {
		WorkpackageBulk wp = (WorkpackageBulk)this.workpackage_disponibili.remove(index);
		wp.setProgetto(this);
		this.workpackage_collegati.add(wp);
		return wp;
	}

	public WorkpackageBulk addToWorkpackage_disponibili(int index) {
		WorkpackageBulk wp = (WorkpackageBulk)this.workpackage_collegati.remove(index);
		Integer pg = null;
		wp.setPg_progetto(pg);
		this.workpackage_disponibili.add(wp);
		return wp;
	}

	public void resetWorkpackages()
	{
		workpackage_disponibili.clear();
		workpackage_collegati.clear();
	}

	/**
	 * @return
	 */
	public Commessa_spesaBulk getSpese() {
		return spese;
	}

	/**
	 * @param bulk
	 */
	public void setSpese(Commessa_spesaBulk bulk) {
		spese = bulk;
	}

	public java.math.BigDecimal getGenerale_affitto() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getGenerale_affitto();
	}

	public void setGenerale_affitto(java.math.BigDecimal generale_affitto) {
		this.getSpese().setGenerale_affitto(generale_affitto);
	}

	/**
	 * @return
	 */
	public BulkList getSpeseEsercizio() {
		return speseEsercizio;
	}

	/**
	 * @param list
	 */
	public void setSpeseEsercizio(BulkList list) {
		speseEsercizio = list;
		if (speseEsercizio!= null && speseEsercizio.size()>0)
			this.setSpese((Commessa_spesaBulk) list.get(0));
		else
			this.setSpese(null);
	}

	/**
	 * @return
	 */
	public BigDecimal getAcc_tfr() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAcc_tfr();
	}

	/**
	 * @return
	 */
	public BigDecimal getAmm_altri_beni() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAmm_altri_beni();
	}

	/**
	 * @return
	 */
	public BigDecimal getAmm_immobili() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAmm_immobili();
	}

	/**
	 * @return
	 */
	public BigDecimal getAmm_tecnico() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAmm_tecnico();
	}

	/**
	 * @return
	 */
	public BigDecimal getCc_brev_pi() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getCc_brev_pi();
	}

	/**
	 * @return
	 */
	public BigDecimal getEdilizia() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getEdilizia();
	}

	/**
	 * @return
	 */
	public BigDecimal getGestione_nave() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getGestione_nave();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_fo() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_fo();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_min() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_min();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_privati() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_privati();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_ue_int() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_ue_int();
	}

	/**
	 * @param acc_tfr
	 */
	public void setAcc_tfr(BigDecimal acc_tfr) {
		this.getSpese().setAcc_tfr(acc_tfr);
	}

	/**
	 * @param amm_altri_beni
	 */
	public void setAmm_altri_beni(BigDecimal amm_altri_beni) {
		this.getSpese().setAmm_altri_beni(amm_altri_beni);
	}

	/**
	 * @param amm_immobili
	 */
	public void setAmm_immobili(BigDecimal amm_immobili) {
		this.getSpese().setAmm_immobili(amm_immobili);
	}

	/**
	 * @param amm_tecnico
	 */
	public void setAmm_tecnico(BigDecimal amm_tecnico) {
		this.getSpese().setAmm_tecnico(amm_tecnico);
	}

	/**
	 * @param cc_brev_pi
	 */
	public void setCc_brev_pi(BigDecimal cc_brev_pi) {
		this.getSpese().setCc_brev_pi(cc_brev_pi);
	}

	/**
	 * @param edilizia
	 */
	public void setEdilizia(BigDecimal edilizia) {
		this.getSpese().setEdilizia(edilizia);
	}

	/**
	 * @param gestione_nave
	 */
	public void setGestione_nave(BigDecimal gestione_nave) {
		this.getSpese().setGestione_nave(gestione_nave);
	}

	/**
	 * @param res_fo
	 */
	public void setRes_fo(BigDecimal res_fo) {
		this.getSpese().setRes_fo(res_fo);
	}

	/**
	 * @param res_min
	 */
	public void setRes_min(BigDecimal res_min) {
		this.getSpese().setRes_min(res_min);
	}

	/**
	 * @param res_privati
	 */
	public void setRes_privati(BigDecimal res_privati) {
		this.getSpese().setRes_privati(res_privati);
	}

	/**
	 * @param res_ue_int
	 */
	public void setRes_ue_int(BigDecimal res_ue_int) {
		this.getSpese().setRes_ue_int(res_ue_int);
	}

	public BigDecimal getTotaleSpese() {
		if (getSpese()!=null) {
			BigDecimal tot = new BigDecimal("0");
			if (this.getGenerale_affitto()!=null)
				tot = tot.add(this.getGenerale_affitto());
			if (this.getGestione_nave()!=null)
				tot = tot.add(this.getGestione_nave());
			if (this.getCc_brev_pi()!=null)
				tot = tot.add(this.getCc_brev_pi());
			if (this.getEdilizia()!=null)
				tot = tot.add(this.getEdilizia());
			return(tot);
		}
		else
			return(null);
	}

	public BigDecimal getTotaleCostiFigurativi() {
		if (getSpese()!=null) {
			BigDecimal tot = new BigDecimal("0");
			if (this.getAmm_immobili()!=null)
				tot = tot.add(this.getAmm_immobili());
			if (this.getAcc_tfr()!=null)
				tot = tot.add(this.getAcc_tfr());
			if (this.getAmm_tecnico()!=null)
				tot = tot.add(this.getAmm_tecnico());
			if (this.getAmm_altri_beni()!=null)
				tot = tot.add(this.getAmm_altri_beni());
			return(tot);
		}
		else
			return(null);
	}

	public BigDecimal getTotaleRisorsePresunte() {
		if (getSpese()!=null) {
			BigDecimal tot = new BigDecimal("0");
			if (this.getRes_fo()!=null)
				tot = tot.add(this.getRes_fo());
			if (this.getRes_min()!=null)
				tot = tot.add(this.getRes_min());
			if (this.getRes_privati()!=null)
				tot = tot.add(this.getRes_privati());
			if (this.getRes_ue_int()!=null)
				tot = tot.add(this.getRes_ue_int());
			return(tot);
		}
		else
			return(null);
	}
	/**
	 * @return
	 */
	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}

	/**
	 * @param bulk
	 */
	public void setDipartimento(DipartimentoBulk bulk) {
		dipartimento = bulk;
	}
	
	public java.lang.String getCd_dipartimento() {
		DipartimentoBulk dipartimento = this.getDipartimento();
		if (dipartimento == null)
			return null;
		return dipartimento.getCd_dipartimento();
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento) {
		this.getDipartimento().setCd_dipartimento(cd_dipartimento);
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo dipartimento
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isRODipartimento() {
		/* la prima condizione perchè il campo appariva
		 * non utilizzabile nelle ricerchi guidate */ 
		if (isToBeCreated() || getCrudStatus() == UNDEFINED)
			return false;
		return dipartimento == null || dipartimento.getCrudStatus() == NORMAL || dipartimento.getCrudStatus() == TO_BE_UPDATED;
	}	

	/**
	 * @return
	 */
	public Parametri_cdsBulk getParametriCds() {
		return parametriCds;
	}

	/**
	 * @param bulk
	 */
	public void setParametriCds(Parametri_cdsBulk bulk) {
		parametriCds = bulk;
	}
	public boolean isProgetto(){
		return getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO);
	}
	public boolean isCommessa(){
		return getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	}
	public boolean isModulo(){
		return getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_TERZO);
	}

	public Pdg_programmaBulk getPdgProgramma() {
		return pdgProgramma;
	}
	
	public void setPdgProgramma(Pdg_programmaBulk pdgProgramma) {
		this.pdgProgramma = pdgProgramma;
	}
	@Override
	public String getCd_programma() {
		Pdg_programmaBulk pdgProgramma = this.getPdgProgramma();
		if (pdgProgramma == null)
			return null;
		return pdgProgramma.getCd_programma();
	}
	
	@Override
	public void setCd_programma(String cd_programma) {
		this.getPdgProgramma().setCd_programma(cd_programma);
	}

	public Pdg_missioneBulk getPdgMissione() {
		return pdgMissione;
	}
	
	public void setPdgMissione(Pdg_missioneBulk pdgMissione) {
		this.pdgMissione = pdgMissione;
	}
	@Override
	public String getCd_missione() {
		Pdg_missioneBulk pdgMissione = this.getPdgMissione();
		if (pdgMissione == null)
			return null;
		return pdgMissione.getCd_missione();
	}
	
	@Override
	public void setCd_missione(String cd_missione) {
		this.getPdgMissione().setCd_missione(cd_missione);
	}

	public Boolean getFl_previsione() {
		return fl_previsione;
	}
	public void setFl_previsione(Boolean fl_previsione) {
		this.fl_previsione = fl_previsione;
	}
	
	public Boolean getFl_gestione() {
		return fl_gestione;
	}
	public void setFl_gestione(Boolean fl_gestione) {
		this.fl_gestione = fl_gestione;
	}
	
	public String getTipoFaseToSearch() {
		return tipoFaseToSearch;
	}
	
	public void setTipoFaseToSearch(String tipoFaseToSearch) {
		this.tipoFaseToSearch = tipoFaseToSearch;
	}
	
	public void setOtherField(Progetto_other_fieldBulk otherField) {
		this.otherField = otherField;
	}
	
	public Progetto_other_fieldBulk getOtherField() {
		return otherField;
	}
	
	public boolean isROPianoEconomico() {
		return Boolean.TRUE.equals(getOtherField().getTipoFinanziamento().getFlPianoEcoFin()) &&
				!getDettagliPianoEconomicoTotale().isEmpty() &&
				!getDettagliPianoEconomicoAnnoCorrente().isEmpty() &&
				!getDettagliPianoEconomicoAltriAnni().isEmpty();
	}

	public Integer getAnnoInizioOf() {
		return Optional.ofNullable(this.getOtherField())
				.map(Progetto_other_fieldBulk::getAnnoInizio)
				.orElse(0);
	}		

	public Integer getAnnoFineOf() {
		return Optional.ofNullable(this.getOtherField())
				.map(Progetto_other_fieldBulk::getAnnoFine)
				.orElse(9999);
	}

	/*
    	Ritorna l'anno di inizio utile per il caricamento del piano economico
    	E' sempre pari a 14 anni precedenti all'anno inizio.
 	*/
	public Integer getAnnoInizioForPianoEconomico() {
		return Optional.ofNullable(this.getAnnoInizioOf())
				.map(el->el-Progetto_piano_economicoBulk.ANNIPRE_PIANOECONOMICO)
				.filter(el->el.compareTo(0)>0)
				.orElse(0);
	}

	/*
	 * Indica che è obbligatorio indicare le date del progetto
	 */
	public boolean isDatePianoEconomicoRequired() {
		return Optional.ofNullable(this.getOtherField())
				.map(Progetto_other_fieldBulk::isDatePianoEconomicoRequired)
				.orElse(Boolean.FALSE);
	}
	
	/*
	 * Indica se è obbligatorio inserire il piano economico sulla base della tipologia 
	 * di finanziamento associata
	 */
	public boolean isPianoEconomicoRequired() {
		return Optional.ofNullable(this.getOtherField())
				.map(Progetto_other_fieldBulk::isPianoEconomicoRequired)
				.orElse(Boolean.FALSE);
	}
	
	public BulkList<Progetto_piano_economicoBulk> getPianoEconomicoSummaryVoce() {
		Map<String, List<Progetto_piano_economicoBulk>> resultByVoce = 
				this.getAllDetailsProgettoPianoEconomico().stream()
					 .filter(el->el.getVoce_piano_economico()!=null)
					 .filter(el->el.getVoce_piano_economico().getCd_unita_organizzativa()!=null)
					 .filter(el->el.getVoce_piano_economico().getCd_voce_piano()!=null)					 
					 .collect(Collectors.groupingBy(x->{
						 return x.getCd_unita_organizzativa().concat(x.getCd_voce_piano());
					 }));
		
		return new BulkList<Progetto_piano_economicoBulk>(resultByVoce.keySet().stream()
				.sorted((vocePiano1,vocePiano2)->vocePiano1.compareTo(vocePiano2))
				.map(vocePiano->{
						Progetto_piano_economicoBulk bulk = new Progetto_piano_economicoBulk();
						bulk.setVoce_piano_economico(resultByVoce.get(vocePiano).stream().findFirst().get().getVoce_piano_economico());
						bulk.setIm_spesa_finanziato(resultByVoce.get(vocePiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setIm_spesa_cofinanziato(resultByVoce.get(vocePiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						
						List<V_saldi_piano_econom_progettoBulk> listSaldiSpesa = resultByVoce.get(vocePiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoSpesa).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettSpesa = new Ass_progetto_piaeco_voceBulk();
						dettSpesa.setSaldoSpesa(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiSpesa));
						bulk.addToVociBilancioAssociate(dettSpesa);

						List<V_saldi_piano_econom_progettoBulk> listSaldiEntrata = resultByVoce.get(vocePiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoEntrata).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettEntrata = new Ass_progetto_piaeco_voceBulk();
						dettEntrata.setSaldoEntrata(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiEntrata));
						bulk.addToVociBilancioAssociate(dettEntrata);

						return bulk;
					})
				.collect(Collectors.toList()));
	}

	public BulkList<Progetto_piano_economicoBulk> getPianoEconomicoSummaryAnno() {
		Map<Integer, List<Progetto_piano_economicoBulk>> resultByEsercizio = 
				this.getAllDetailsProgettoPianoEconomico().stream()
					 .filter(el->Optional.ofNullable(el.getEsercizio_piano()).isPresent())
					 .collect(Collectors.groupingBy(x->{
						 return x.getEsercizio_piano();
					 }));
		
		return new BulkList<Progetto_piano_economicoBulk>(resultByEsercizio.keySet().stream()
				.sorted((esercizioPiano1,esercizioPiano2)->esercizioPiano1.compareTo(esercizioPiano2))
				.map(esercizioPiano->{
						Progetto_piano_economicoBulk bulk = new Progetto_piano_economicoBulk();
						bulk.setEsercizio_piano(esercizioPiano);
						bulk.setIm_spesa_finanziato(resultByEsercizio.get(esercizioPiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setIm_spesa_cofinanziato(resultByEsercizio.get(esercizioPiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						
						List<V_saldi_piano_econom_progettoBulk> listSaldiSpesa = resultByEsercizio.get(esercizioPiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoSpesa).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettSpesa = new Ass_progetto_piaeco_voceBulk();
						dettSpesa.setSaldoSpesa(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiSpesa));
						bulk.addToVociBilancioAssociate(dettSpesa);

						List<V_saldi_piano_econom_progettoBulk> listSaldiEntrata = resultByEsercizio.get(esercizioPiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoEntrata).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettEntrata = new Ass_progetto_piaeco_voceBulk();
						dettEntrata.setSaldoEntrata(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiEntrata));
						bulk.addToVociBilancioAssociate(dettEntrata);

						return bulk;
					})
				.collect(Collectors.toList()));
	}
	
	public BulkList<V_saldi_voce_progettoBulk> getVociMovimentateNonAssociate() {
		return new BulkList<V_saldi_voce_progettoBulk>(
				getVociBilancioMovimentate().stream()
					.filter(voceMov->{
						return !this.getAllDetailsProgettoPianoEconomico().stream()
								.filter(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
								.flatMap(ppe->ppe.getVociBilancioAssociate().stream())
								.filter(voceAss->voceAss.getElemento_voce()!=null)
								.filter(voceAss->voceAss.getEsercizio_voce()!=null)
								.filter(voceAss->voceAss.getEsercizio_voce().equals(voceMov.getEsercizio_voce()))
								.filter(voceAss->voceAss.getTi_appartenenza().equals(voceMov.getTi_appartenenza()))
								.filter(voceAss->voceAss.getTi_gestione().equals(voceMov.getTi_gestione()))
								.filter(voceAss->voceAss.getCd_elemento_voce().equals(voceMov.getCd_elemento_voce()))
								.findAny().isPresent();
					})
					.collect(Collectors.toList()));
	}
	
	public boolean isDettagliPianoEconomicoPresenti() {
		return !getDettagliPianoEconomicoAnnoCorrente().isEmpty() ||
				!getDettagliPianoEconomicoAltriAnni().isEmpty() ||
				!getDettagliPianoEconomicoTotale().isEmpty();
	}

	public BulkList<Progetto_piano_economicoBulk> getAllDetailsProgettoPianoEconomico() {
		BulkList<Progetto_piano_economicoBulk> items = new BulkList<Progetto_piano_economicoBulk>();
		items.addAll(getDettagliPianoEconomicoTotale());
		items.addAll(getDettagliPianoEconomicoAnnoCorrente());
		items.addAll(getDettagliPianoEconomicoAltriAnni());
		return items;
	}

	public java.math.BigDecimal getImFinanziato() {
		return Optional.ofNullable(getOtherField()).map(Progetto_other_fieldBulk::getImFinanziato).orElse(BigDecimal.ZERO);
	}

	public java.math.BigDecimal getImCofinanziato() {
		return Optional.ofNullable(getOtherField()).map(Progetto_other_fieldBulk::getImCofinanziato).orElse(BigDecimal.ZERO);
	}

	public java.math.BigDecimal getImTotale() {
		return this.getImFinanziato().add(this.getImCofinanziato());
	}

	public java.math.BigDecimal getImFinanziatoRipartito() {
		return this.getAllDetailsProgettoPianoEconomico().stream()
				.map(el->Optional.ofNullable(el.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO))
				.reduce((x, y)->x.add(y))
				.orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getImCofinanziatoRipartito() {
		return this.getAllDetailsProgettoPianoEconomico().stream()
				.map(el->Optional.ofNullable(el.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO))
				.reduce((x, y)->x.add(y))
				.orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getImTotaleRipartito() {
		return this.getAllDetailsProgettoPianoEconomico().stream()
				.map(el->Optional.ofNullable(el.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)
						.add(Optional.ofNullable(el.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)))
				.reduce((x, y)->x.add(y))
				.orElse(BigDecimal.ZERO);
	}

	public java.math.BigDecimal getImFinanziatoDaRipartire() {
		return this.getImFinanziato().subtract(this.getImFinanziatoRipartito());
	}
	
	public java.math.BigDecimal getImCofinanziatoDaRipartire() {
		return this.getImCofinanziato().subtract(this.getImCofinanziatoRipartito());
	}
	
	public java.math.BigDecimal getImTotaleDaRipartire() {
		return this.getImTotale().subtract(this.getImTotaleRipartito());
	}
	
	public void validaDateProgetto() throws ValidationException {
		if (Optional.ofNullable(this.getOtherField()).isPresent())
			this.getOtherField().validaDateProgetto();

		this.getAllDetailsProgettoPianoEconomico().stream()
			.filter(progetto_piano_economicoBulk -> Optional.ofNullable(progetto_piano_economicoBulk.getEsercizio_piano()).isPresent())
			.filter(el->el.getEsercizio_piano().compareTo(this.getAnnoInizioForPianoEconomico())<0)
			.map(Progetto_piano_economicoBulk::getEsercizio_piano)
			.min(Comparator.comparing(Integer::valueOf))
			.ifPresent(annoMin->{
				throw new ApplicationRuntimeException("Non è possibile indicare una data di inizio con anno maggiore del "+
						(annoMin+Progetto_piano_economicoBulk.ANNIPRE_PIANOECONOMICO)+" essendo già caricato un piano economico "+
						"per l'anno "+annoMin+" (è possibile associare piani economici di massimo "+Progetto_piano_economicoBulk.ANNIPRE_PIANOECONOMICO+
						" anni precedenti rispetto alla data di inizio del progetto).");
			});

		this.getAllDetailsProgettoPianoEconomico().stream()
			.filter(progetto_piano_economicoBulk -> Optional.ofNullable(progetto_piano_economicoBulk.getEsercizio_piano()).isPresent())
			.filter(el->el.getEsercizio_piano().compareTo(this.getAnnoFineOf())>0)
			.map(Progetto_piano_economicoBulk::getEsercizio_piano)
			.max(Comparator.comparing(Integer::valueOf))
			.ifPresent(annoMax->{
				throw new ApplicationRuntimeException("Non è possibile indicare una data di fine/proroga con anno inferiore al "+annoMax+
						" per il quale risulta già caricato un piano economico.");
		});
	}
	
	public BulkList<Pdg_moduloBulk> getPdgModuli() {
		return pdgModuli;
	}
	
	public void setPdgModuli(BulkList<Pdg_moduloBulk> pdgModuli) {
		this.pdgModuli = pdgModuli;
	}

	//Indica se il progetto è in stato RO a causa dello stato che possiede
	//Un progetto è readonly se Approvato, Chiuso o Annullato
	public boolean isROProgettoForStato() {
		return Optional.ofNullable(this)
				.flatMap(el->Optional.ofNullable(el.getOtherField()))
				.filter(el->el.isStatoAnnullato()||el.isStatoChiuso()||el.isStatoApprovato())
				.isPresent();
	}
	
	public boolean isROProgettoPianoEconomico() {
		return isROProgettoForStato() ||
				Optional.ofNullable(this)
				.flatMap(el->Optional.ofNullable(el.getPdgModuli()))
				.map(el->el.stream())
				.orElse(Stream.empty())
				.filter(el->el.getEsercizio().equals(this.getEsercizio()))
				.filter(el->!el.getStato().equals(Pdg_moduloBulk.STATO_AC))
				.findAny().isPresent();
	}
	
	public String getStatoPrg() {
		Optional<Progetto_other_fieldBulk> optPrg = Optional.ofNullable(this.getOtherField());
		if (optPrg.filter(Progetto_other_fieldBulk::isStatoChiuso).isPresent())
			return StatoProgetto.STATO_CHIUSURA.value();
		return optPrg.map(el->el.getStato()).orElse(null);
	}
	
	public boolean isProgettoScaduto() {
		return (this.getOtherField().isStatoChiuso() || this.getOtherField().isStatoApprovato()) &&
				Optional.of(this.getOtherField().getDtFineEffettiva())
						.map(dtScadenza->dtScadenza.before(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp()))
						.orElse(Boolean.FALSE);
	}
	
	public BulkList<V_saldi_voce_progettoBulk> getVociBilancioMovimentate() {
		return vociBilancioMovimentate;
	}
	
	public void setVociBilancioMovimentate(BulkList<V_saldi_voce_progettoBulk> vociBilancioMovimentate) {
		this.vociBilancioMovimentate = vociBilancioMovimentate;
	}
	
	@Override
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		Optional.ofNullable(allegato)
			    .filter(AllegatoProgettoBulk.class::isInstance)
			    .map(AllegatoProgettoBulk.class::cast)
			    .ifPresent(el->el.setProgetto(this));
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
	
	public String getStorePath() {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				this.getCd_unita_organizzativa(),
				"Progetti",
				getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}
	
	public String getCMISFolderName() {
		String suffix = "Progetto ";
		suffix = suffix.concat(String.valueOf(this.getCd_progetto()));
		suffix = suffix.concat(" - "+String.valueOf(this.getPg_progetto()));
		return suffix;
	}
	
	public BulkList<Progetto_rimodulazioneBulk> getRimodulazioni() {
		return rimodulazioni;
	}
	
	public void setRimodulazioni(BulkList<Progetto_rimodulazioneBulk> rimodulazioni) {
		this.rimodulazioni = rimodulazioni;
	}
	
	public String getVersione() {
		Optional<Progetto_rimodulazioneBulk> lastRim = getRimodulazioni().stream()
							.filter(el->!el.isStatoRespinto())
							.sorted(Comparator.comparing(Progetto_rimodulazioneBulk::getPg_rimodulazione).reversed())
							.findFirst();

		if (lastRim.isPresent()) {
			StringBuffer version = new StringBuffer(
				lastRim.get().getPg_rimodulazione()+
					lastRim.map(el->{
						if (el.isStatoProvvisorio()) return "P";
						if (el.isStatoDefinitivo()) return "D";
						if (el.isStatoValidato()) return "V";
						if (el.isStatoApprovato()) return "A";
						return "";
					})
					.orElse(""));
			return version.append(Optional.ofNullable(lastRim.get().getPg_gen_rimodulazione())
					.map(el->" (Rim."+el+")")
					.orElse("")).toString();
		} else
			return "0";
	}
	
	public boolean isStatoPrgApprovato() {
		return StatoProgetto.STATO_APPROVATO.value().equals(this.getStatoPrg());
	}

	public boolean isStatoPrgChiuso() {
		return StatoProgetto.STATO_CHIUSURA.value().equals(this.getStatoPrg());
	}
	
	@Override
	public Object clone() {
		ProgettoBulk prgClone = (ProgettoBulk)super.clone();
		prgClone.setDettagliPianoEconomicoTotale(new BulkList<>(prgClone.getDettagliPianoEconomicoTotale().stream().map(Progetto_piano_economicoBulk::clone).collect(Collectors.toList())));
		prgClone.setDettagliPianoEconomicoAnnoCorrente(new BulkList<>(prgClone.getDettagliPianoEconomicoAnnoCorrente().stream().map(Progetto_piano_economicoBulk::clone).collect(Collectors.toList())));
		prgClone.setDettagliPianoEconomicoAltriAnni(new BulkList<>(prgClone.getDettagliPianoEconomicoAltriAnni().stream().map(Progetto_piano_economicoBulk::clone).collect(Collectors.toList())));
		return prgClone;
	}
	
	public BulkList<ContrattoBulk> getContratti() {
		return contratti;
	}
	public void setContratti(BulkList<ContrattoBulk> contratti) {
		this.contratti = contratti;
	}


	public BulkList<Progetto_anagraficoBulk> getAnagraficheProgetto() {
		return anagraficheProgetto;
	}

	public void setAnagraficheProgetto(BulkList<Progetto_anagraficoBulk> anagraficheProgetto) {
		this.anagraficheProgetto = anagraficheProgetto;
	}

	public int addToAnagraficheProgetto(Progetto_anagraficoBulk dett){
		dett.setPgProgetto(this.getPg_progetto());
		getAnagraficheProgetto().add(dett);
		return anagraficheProgetto.size()-1;
	}

	public Progetto_anagraficoBulk removeFromAnagraficheProgetto(int index) {
		Progetto_anagraficoBulk dett = (Progetto_anagraficoBulk)getAnagraficheProgetto().remove(index);
		return dett;
	}

	public boolean isAmministra() {
		return amministra;
	}

	public void setAmministra(boolean amministra) {
		this.amministra = amministra;
	}
}