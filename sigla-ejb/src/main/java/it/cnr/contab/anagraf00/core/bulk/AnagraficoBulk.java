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

package it.cnr.contab.anagraf00.core.bulk;

import java.lang.String;
import java.util.Dictionary;
import java.util.Optional;


import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.jada.bulk.*;

/**
 * Gestione dei dati relativi alla tabella Anagrafico
 */

public class AnagraficoBulk extends AnagraficoBase {

	private it.cnr.jada.bulk.BulkList carichi_familiari_anag = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList dichiarazioni_intento = new it.cnr.jada.bulk.BulkList();
	//private it.cnr.jada.bulk.BulkList terzi = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList assGruppoIva = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList rapporti = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList pagamenti_esterni = new BulkList();	
	private it.cnr.jada.bulk.BulkList associatiStudio = new it.cnr.jada.bulk.BulkList();
	private String descrizioneAnagrafica = "";
	public final static Dictionary ITALIANO_ESTERO;
	public final static Dictionary ti_entitaKeys;
	public final static Dictionary ti_entitaPersonaKeys;
	public final static Dictionary ti_entitaPersona_ridKeys;
	public final static Dictionary ti_entitaStrutturaKeys;
	public final static Dictionary ENTITA_GIURIDICA;
	public final static Dictionary ENTITA_FISICA;
	public final static Dictionary SESSO;
	public final static Dictionary ti_titoloStudioKeys;

	private boolean abilitatoTrattamenti;
	private boolean notGestoreIstat;
	private ComuneBulk  comune_fiscale;
	private ComuneBulk  comune_nascita;

	private NazioneBulk nazionalita;

	private it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk classificazione_anag;
	private java.util.Collection classificazioni_anag;

	private java.util.Collection caps_comune;

	private AnagraficoBulk cd_ente_app;

	//private java.util.List rif_termini_pagamento;
	//private java.util.List rif_modalita_pagamento;

	public final static String ALTRO         = "A";
	public final static String DITTA_INDIVID = "D";
	public final static String GRUPPO_IVA    = "G";
	public final static String DIVERSI       = "D";
	public final static String ENTE_PUBBLICO = "P";
	public final static String FEMMINA       = "F";
	public final static String FISICA        = "F";
	public final static String GIURIDICA     = "G";
	public final static String MASCHIO       = "M";
	public final static String STRUT_CNR     = "U";

	public final static int ENTITA_PERSONA    = 1;
	public final static int ENTITA_STRUTTURA  = 2;
	public final static int ENTITA_INDEFINITA = 0;

	public final static String ELEMENTARI = "ELEMEN";
	public final static String MEDIA_INFERIORE = "MEDINF";
	public final static String MEDIA_SUPERIORE = "MEDSUP";
	public final static String LAUREA = "LAUREA";
	public final static String SPECIALIZZAZIONE = "SPECIA";

	static {
		SESSO = new it.cnr.jada.util.OrderedHashtable();
		SESSO.put(MASCHIO,"Maschio");
		SESSO.put(FEMMINA,"Femmina");

		ITALIANO_ESTERO = new it.cnr.jada.util.OrderedHashtable();
		ITALIANO_ESTERO.put(NazioneBulk.ITALIA,"Italiana");
		ITALIANO_ESTERO.put(NazioneBulk.EXTRA_CEE,"Estera");
		
		ti_titoloStudioKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_titoloStudioKeys.put(ELEMENTARI,"Licenza Elementare");
		ti_titoloStudioKeys.put(MEDIA_INFERIORE,"Diploma Inferiore");
		ti_titoloStudioKeys.put(MEDIA_SUPERIORE,"Diploma Superiore");
		ti_titoloStudioKeys.put(LAUREA,"Laurea");
		ti_titoloStudioKeys.put(SPECIALIZZAZIONE,"Spec.ne Post Universitaria");

		ti_entitaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_entitaKeys.put(DIVERSI,"Diversi");
		ti_entitaKeys.put(GIURIDICA,"Persona giuridica");
		ti_entitaKeys.put(FISICA,"Persona fisica");
		ti_entitaKeys.put(STRUT_CNR,"Struttura CNR");
		
		ti_entitaPersonaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_entitaPersonaKeys.put(GIURIDICA,"Persona giuridica");
		ti_entitaPersonaKeys.put(FISICA,"Persona fisica");
		ti_entitaPersonaKeys.put(DIVERSI,"Diversi");
		
		ti_entitaPersona_ridKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_entitaPersona_ridKeys.put(GIURIDICA,"Persona giuridica");
		ti_entitaPersona_ridKeys.put(FISICA,"Persona fisica");
		
		
		ti_entitaStrutturaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_entitaStrutturaKeys.put(STRUT_CNR,"Struttura CNR");
		
		ENTITA_GIURIDICA = new it.cnr.jada.util.OrderedHashtable();
		ENTITA_GIURIDICA.put(ENTE_PUBBLICO,"Ente pubblico");
		ENTITA_GIURIDICA.put(GRUPPO_IVA,"Gruppo IVA");
		ENTITA_GIURIDICA.put(ALTRO,"Altro");
		
		ENTITA_FISICA = new it.cnr.jada.util.OrderedHashtable();
		ENTITA_FISICA.put(DITTA_INDIVID,"Ditta individuale");
		ENTITA_FISICA.put(ALTRO,"Altro");
	};

	protected int ti_entita_persona_struttura = ENTITA_INDEFINITA;

	private it.cnr.contab.anagraf00.tabrif.bulk.Codici_attivita_inpsBulk attivitaInps;
	private it.cnr.contab.anagraf00.tabrif.bulk.Codici_altra_forma_ass_inpsBulk altraAssPrevidInps;
	private java.lang.Boolean fl_codice_fiscale_forzato;

	private Anagrafico_esercizioBulk anagrafico_esercizio;

	private boolean dipendente;
	private boolean utilizzata = Boolean.FALSE;
	private boolean utilizzata_detrazioni = Boolean.FALSE;
	private Tipologie_istatBulk tipologia_istat;
	private boolean uo_ente=Boolean.FALSE;
	/**
	 * Costruttore di default.
	 *
	 */

	public AnagraficoBulk() {
	}
public AnagraficoBulk(java.lang.Integer cd_anag) {
	super(cd_anag);
}
	/**
	 * Metodo per l'aggiunta di un elemento <code>Carico_familiare_anagBulk</code> alla <code>Collection</code>
	 * dei carichi familiari dell'anagrafica.
	 *
	 * @param nuovoCarico_familiare Carico familiare da aggiungere.
	 *
	 * @return la posizione nella lista
	 *
	 * @see removeFromCarichi_familiari_anag
	 */

	public int addToCarichi_familiari_anag(Carico_familiare_anagBulk nuovoCarico_familiare) {
		nuovoCarico_familiare.setAnagrafico(this);
		nuovoCarico_familiare.setFl_primo_figlio(Boolean.FALSE);
		nuovoCarico_familiare.setFl_handicap(Boolean.FALSE);
		nuovoCarico_familiare.setFl_primo_figlio_manca_con(Boolean.FALSE);
		carichi_familiari_anag.add(nuovoCarico_familiare);
		return carichi_familiari_anag.size()-1;
	}
	/**
	 * Metodo per l'aggiunta di un elemento <code>Dichiarazione_intentoBulk</code> alla <code>Collection</code>
	 * delle dichiarazioni di intento.
	 *
	 * @param nuovoDichiarazione_intento Dichiarazioni di intento da aggiungere.
	 *
	 * @return la posizione nella lista
	 *
	 * @see removeFromDichiarazioni_intento
	 */

	public int addToDichiarazioni_intento(Dichiarazione_intentoBulk nuovoDichiarazione_intento) {
		nuovoDichiarazione_intento.setAnagrafico(this);
		dichiarazioni_intento.add(nuovoDichiarazione_intento);
		return dichiarazioni_intento.size()-1;
	}
	/**
	 * Metodo per l'aggiunta di un elemento <code>RapportoBulk</code> alla <code>Collection</code>
	 * dei rapporti.
	 *
	 * @param rapporto Rapporto da aggiungere.
	 *
	 * @return la posizione nella lista
	 *
	 * @see removeFromRapporti
	 */

	public int addToAssGruppoIva(AssGruppoIvaAnagBulk assGruppoIvaAnagBulk) {
		assGruppoIva.add(assGruppoIvaAnagBulk);
		if (isGruppoIVA()){
			assGruppoIvaAnagBulk.setAnagraficoGruppoIva(this);
		} else {
			assGruppoIvaAnagBulk.setAnagrafico(this);
		}
		return assGruppoIva.size()-1;
	}
	public int addToRapporti(RapportoBulk rapporto) {
		rapporti.add(rapporto);
		rapporto.setAnagrafico(this);
		return rapporti.size()-1;
	}
	/**
	 * Metodo per l'aggiunta di un elemento <code>Pagamento_esternoBulk</code> alla lista
	 * dei pagamenti_esterni.
	 *
	 * @param pagamento_esterno elemento da aggiungere.
	 *
	 * @return la posizione nella lista
	 *
	 * @see removeFromPagamenti_esterni
	 */

	public int addToPagamenti_esterni(Pagamento_esternoBulk pagamento_esterno) throws ValidationException {
		pagamenti_esterni.add(pagamento_esterno);
		pagamento_esterno.setAnagrafico(this);
		return pagamenti_esterni.size()-1;
	}		
/**
 * Insert the method's description here.
 * Creation date: (14/05/2002 15.45.20)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Codici_altra_forma_ass_inpsBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Codici_altra_forma_ass_inpsBulk getAltraAssPrevidInps() {
	return altraAssPrevidInps;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2003 12.28.17)
 * @return it.cnr.contab.anagraf00.core.bulk.Anagrafico_esercizioBulk
 */
public Anagrafico_esercizioBulk getAnagrafico_esercizio() {
	return anagrafico_esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2002 15.40.32)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Codici_attivita_inpsBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Codici_attivita_inpsBulk getAttivitaInps() {
	return attivitaInps;
}
	/**
	 * Restituisce le liste di componenti correlati all'anagarafica.
	 *
	 * @return it.cnr.jada.bulk.BulkCollection[]
	 *
	 * @see setBulkLists
	 */

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
														carichi_familiari_anag,
														dichiarazioni_intento,
														rapporti,
				                                        assGruppoIva,
														pagamenti_esterni,
														associatiStudio
													   };
	}
	/**
	 * Restituisce la <code>Collection</code> contenente lelenco di cap relativi
	 * al comune fiscale.
	 *
	 * @return java.util.Collection
	 *
	 * @see setCaps_comune
	 */

	public java.util.Collection getCaps_comune() {
		return caps_comune;
	}
	/**
	 * Restituisce la lista dei carichi familiari.
	 *
	 * @return it.cnr.jada.bulk.BulkList
	 *
	 * @see setCarichi_familiari_anag
	 */

	public it.cnr.jada.bulk.BulkList getCarichi_familiari_anag() {
		return carichi_familiari_anag;
	}
public java.lang.String getCd_classific_anag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk classificazione_anag = this.getClassificazione_anag();
	if (classificazione_anag == null)
		return null;
	return classificazione_anag.getCd_classific_anag();
}
	/**
	 * Restituisce l'<code>AnagraficoBulk</code> relativo al cd_ente_appartenenza.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 *
	 * @see setCd_ente_app
	 */

	public AnagraficoBulk getCd_ente_app() {
		return cd_ente_app;
	}
public java.lang.Integer getCd_ente_appartenenza() {
	it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk cd_ente_app = this.getCd_ente_app();
	if (cd_ente_app == null)
		return null;
	return cd_ente_app.getCd_anag();
}
	/**
	 * Restituisce l'<code>Classificazione_anagBulk</code> relativo al classificazione anagrafica selezionata.
	 *
	 * @return it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk
	 *
	 * @see setClassificazione_anag
	 */

	public it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk getClassificazione_anag() {
		return classificazione_anag;
	}
	/**
	 * Restituisce l'elenco delle classificazioni anagrafiche disponibili.
	 *
	 * @return java.util.Collection
	 *
	 * @see setClassificazioni_anag
	 */

	public java.util.Collection getClassificazioni_anag() {
		return classificazioni_anag;
	}
	/**
	 * Restituisce il <code>ComuneBulk</code> del comune fiscale selezionato.
	 *
	 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
	 *
	 * @see setComune_fiscale
	 */

	public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune_fiscale() {
		return comune_fiscale;
	}
	/**
	 * Restituisce il <code>ComuneBulk</code> del comune di nascita selezionato.
	 *
	 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
	 *
	 * @see setComune_nascita
	 */

	public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune_nascita() {
		return comune_nascita;
	}
	/**
	 * Ritorna la concatenazione di 3 campi rappresentativi dell'entità anagrafica (nome/cognome e ragione sociale).
	 *
	 * @return La descrizione.
	 */

	public java.lang.String getDescrizioneAnagrafica() {
		return
		  (getNome() == null ? "" : getNome()) + " " + (getCognome()==null ? "" : getCognome()) + " " + " " + (getRagione_sociale()==null ? "" : getRagione_sociale());
	}
	/**
	 * Restituisce l'elenco delle dichiarazioni di intento.
	 *
	 * @return it.cnr.jada.bulk.BulkList
	 *
	 * @see setDichiarazioni_intento
	 */

	public it.cnr.jada.bulk.BulkList getDichiarazioni_intento() {
		return dichiarazioni_intento;
	}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 15:13:40)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_codice_fiscale_forzato() {
	return fl_codice_fiscale_forzato;
}
	/**
	 * Restituisce il <code>NazioneBulk</code> relativo alla nazione della nazionalità selezionata.
	 *
	 * @return it.cnr.jada.bulk.BulkList
	 *
	 * @see setNazionalita
	 */

	public it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk getNazionalita() {
		return nazionalita;
	}
public java.lang.Long getPg_comune_fiscale() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_fiscale = this.getComune_fiscale();
	if (comune_fiscale == null)
		return null;
	return comune_fiscale.getPg_comune();
}
public java.lang.Long getPg_comune_nascita() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_nascita = this.getComune_nascita();
	if (comune_nascita == null)
		return null;
	return comune_nascita.getPg_comune();
}
public java.lang.Long getPg_nazione_fiscale() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_fiscale = this.getComune_fiscale();
	if (comune_fiscale == null)
		return null;
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = comune_fiscale.getNazione();
	if (nazione == null)
		return null;
	return nazione.getPg_nazione();
}
public java.lang.Long getPg_nazione_nazionalita() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazionalita = this.getNazionalita();
	if (nazionalita == null)
		return null;
	return nazionalita.getPg_nazione();
}
	/**
	 * Restituisce l'elenco dei rapporti.
	 *
	 * @return it.cnr.jada.bulk.BulkList
	 *
	 * @see setRapporti
	 */

	public it.cnr.jada.bulk.BulkList getAssGruppoIva() {
		return assGruppoIva;
	}
	public it.cnr.jada.bulk.BulkList getRapporti() {
		return rapporti;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di entità fisica.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_entita_fisicaKeys() {
		return ENTITA_FISICA;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di entità giuridica.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_entita_giuridicaKeys() {
		return ENTITA_GIURIDICA;
	}
/**
 * Insert the method's description here.
 * Creation date: (23/07/2001 10:49:21)
 * @return int
 */
public int getTi_entita_persona_struttura() {
	return ti_entita_persona_struttura;
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di entità.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_entitaKeys() {
		switch(ti_entita_persona_struttura) {
			case ENTITA_PERSONA:
				if(!isUo_ente()&& 
				  (this.getCrudStatus()==OggettoBulk.TO_BE_CREATED ||this.getCrudStatus()==OggettoBulk.TO_BE_UPDATED||this.getCrudStatus()==OggettoBulk.UNDEFINED)) 
					return ti_entitaPersona_ridKeys;
				else
					return ti_entitaPersonaKeys;
			case ENTITA_STRUTTURA: return ti_entitaStrutturaKeys;
			default: return ti_entitaKeys;
		}
	}
	public String getTi_italiano_estero_anag() {
		if (NazioneBulk.ITALIA.equals(getTi_italiano_estero() ))
			return NazioneBulk.ITALIA;
		else if (getTi_italiano_estero() == null)
			return null;
		else
			return NazioneBulk.EXTRA_CEE;
	}

	public boolean isItaliano() {
		return Optional.ofNullable(getTi_italiano_estero())
				.map(s -> s.equals(NazioneBulk.ITALIA))
				.orElse(false);
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi italiano/estero.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_italiano_esteroKeys() {
		return ITALIANO_ESTERO;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di sesso.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_sessoKeys() {
		return SESSO;
	}
	
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 17.27.35)
 * @return boolean
 */
public boolean isDipendente() {
	return dipendente;
}
	/**
	 * Restituisce <code>true</code> se l'anagrafica si rifarisce ad una persona fisica e
	 * ditta individuale.
	 *
	 * @return boolean
	 */

	public boolean isDittaIndividuale() {
		return isPersonaFisica() && DITTA_INDIVID.equals(getTi_entita_fisica());
	}
	/**
	 * Restituisce <code>true</code> se l'anagrafica si rifarisce a diversi.
	 *
	 * @return boolean
	 */

	public boolean isDiversi() {
		return DIVERSI.equals(getTi_entita());
	}
	/**
	 * Restituisce <code>true</code> se l'anagrafica si rifarisce ad una persona giuridica e
	 * ente pubblico.
	 *
	 * @return boolean
	 */

	public boolean isGruppoIVA() {
		return isPersonaGiuridica() && GRUPPO_IVA.equals(getTi_entita_giuridica());
	}
	public boolean isEntePubblico() {
		return isPersonaGiuridica() && ENTE_PUBBLICO.equals(getTi_entita_giuridica());
	}
	/**
	 * Restituisce <code>true</code> se l'anagrafica si rifarisce ad una persona fisica.
	 *
	 * @return boolean
	 */

	public boolean isPersonaFisica() {
		return FISICA.equals(getTi_entita());
	}
	/**
	 * Restituisce <code>true</code> se l'anagrafica si rifarisce ad una persona giuridica.
	 *
	 * @return boolean
	 */

	public boolean isPersonaGiuridica() {
		return GIURIDICA.equals(getTi_entita());
	}
/**
 * Indica quando altra_ass_previd_inps deve essere read only.
 *
 * @return boolean
 */

public boolean isROaltra_ass_previd_inps() {
	return altraAssPrevidInps == null || altraAssPrevidInps.getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Indica quando cd_attivita_inps deve essere read only.
 *
 * @return boolean
 */

public boolean isROcd_attivita_inps() {

	return getAttivitaInps() == null || getAttivitaInps().getCrudStatus() == OggettoBulk.NORMAL;
}
	/**
	 * Indica quando cd_ente_appartenenza deve essere read only.
	 *
	 * @return boolean
	 */

	public boolean isROcd_ente_appartenenza() {
		return cd_ente_app == null || cd_ente_app.getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Indica quando ds_comune_fiscale deve essere read only.
	 *
	 * @return boolean
	 */

	public boolean isROds_comune_fiscale() {
		return comune_fiscale == null || comune_fiscale.getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Indica quando ds_comune_nascita deve essere read only.
	 *
	 * @return boolean
	 */

	public boolean isROds_comune_nascita() {
		return comune_nascita == null || comune_nascita.getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Indica quando ds_nazione_nazionalita deve essere read only.
	 *
	 * @return boolean
	 */

	public boolean isROds_nazione_nazionalita() {
		return nazionalita == null || nazionalita.getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Restituisce <code>true</code> se l'anagrafica si rifarisce ad una struttura CNR.
	 *
	 * @return boolean
	 */

	public boolean isStrutturaCNR() {
		return STRUT_CNR.equals(getTi_entita());
	}
	/**
	 * Elimina il <code>Carico_familiare_anagBulk</code> alla posizione index dalla lista
	 * carichi_familiari_anag.
	 *
	 * @param index Indice dell'elemento da cancellare.
	 *
	 * @return Carico_familiare_anagBulk
	 *
	 * @see addToCarichi_familiari_anag
	 */

	public Carico_familiare_anagBulk removeFromCarichi_familiari_anag(int index) {
		return (Carico_familiare_anagBulk)carichi_familiari_anag.remove(index);
	}
	/**
	 * Elimina il <code>Dichiarazione_intentoBulk</code> alla posizione index dalla lista
	 * dichiarazioni_intento.
	 *
	 * @param index Indice dell'elemento da cancellare.
	 *
	 * @return Dichiarazione_intentoBulk
	 *
	 * @see addToDichiarazioni_intento
	 */

	public Dichiarazione_intentoBulk removeFromDichiarazioni_intento(int index) {
		return (Dichiarazione_intentoBulk)dichiarazioni_intento.remove(index);
	}
	/**
	 * Elimina il <code>RapportoBulk</code> alla posizione index dalla lista rapporti.
	 *
	 * @param index Indice dell'elemento da cancellare.
	 *
	 * @return RapportoBulk
	 *
	 * @see addToRapporti
	 */

	public RapportoBulk removeFromRapporti(int index) {
		RapportoBulk rpp = (RapportoBulk)rapporti.remove(index);
		rpp.setInquadramenti(null);
		return rpp;
	}

	public AssGruppoIvaAnagBulk removeFromAssGruppoIva(int index) {
		AssGruppoIvaAnagBulk ass = (AssGruppoIvaAnagBulk) assGruppoIva.remove(index);
		return ass;
	}
	/**
	 * Elimina l'<code>Pagamento_esternoBulk</code> alla posizione index dalla lista
	 * pagamenti_esterni.
	 *
	 * @param index Indice dell'elemento da cancellare.
	 *
	 * @return Pagamento_esternoBulk
	 *
	 * @see addToPagamenti_esterni
	 */

	public Pagamento_esternoBulk removeFromPagamenti_esterni(int index) {
		return (Pagamento_esternoBulk)pagamenti_esterni.remove(index);
	}	
/**
 * Insert the method's description here.
 * Creation date: (14/05/2002 15.45.20)
 * @param newAltraAssPrevidInps it.cnr.contab.anagraf00.tabrif.bulk.Codici_altra_forma_ass_inpsBulk
 */
public void setAltraAssPrevidInps(it.cnr.contab.anagraf00.tabrif.bulk.Codici_altra_forma_ass_inpsBulk newAltraAssPrevidInps) {
	altraAssPrevidInps = newAltraAssPrevidInps;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2003 12.28.17)
 * @param newAnagrafico_esercizio it.cnr.contab.anagraf00.core.bulk.Anagrafico_esercizioBulk
 */
public void setAnagrafico_esercizio(Anagrafico_esercizioBulk newAnagrafico_esercizio) {
	anagrafico_esercizio = newAnagrafico_esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (14/05/2002 15.40.32)
 * @param newAttivitaInps it.cnr.contab.anagraf00.tabrif.bulk.Codici_attivita_inpsBulk
 */
public void setAttivitaInps(it.cnr.contab.anagraf00.tabrif.bulk.Codici_attivita_inpsBulk newAttivitaInps) {
	attivitaInps = newAttivitaInps;
}
	/**
	 * Imposta la <code>Collection</code> contenente lelenco di cap relativi
	 * al comune fiscale.
	 *
	 * @param newCaps_comune <code>java.util.Collection</code>
	 *
	 * @see getCaps_comune
	 */

	public void setCaps_comune(java.util.Collection newCaps_comune) {
		caps_comune = newCaps_comune;
	}
	/**
	 * Imposta la lista dei carichi familiari.
	 *
	 * @param newCarichi_familiari_anag <code>it.cnr.jada.bulk.BulkList</code>
	 *
	 * @see getCarichi_familiari_anag
	 */

	public void setCarichi_familiari_anag(it.cnr.jada.bulk.BulkList newCarichi_familiari_anag) {
		carichi_familiari_anag = newCarichi_familiari_anag;
	}
public void setCd_classific_anag(java.lang.String cd_classific_anag) {
	this.getClassificazione_anag().setCd_classific_anag(cd_classific_anag);
}
	/**
	 * Imposta l'<code>AnagraficoBulk</code> relativo al cd_ente_appartenenza.
	 *
	 * @param newCd_ente_app <code>it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk</code>
	 *
	 * @see getCd_ente_app
	 */

	public void setCd_ente_app(AnagraficoBulk newCd_ente_app) {
		cd_ente_app = newCd_ente_app;
	}
public void setCd_ente_appartenenza(java.lang.Integer cd_ente_appartenenza) {
	this.getCd_ente_app().setCd_anag(cd_ente_appartenenza);
}
	/**
	 * Imposta l'<code>Classificazione_anagBulk</code> relativo al classificazione anagrafica selezionata.
	 *
	 * @param newClassificazione_anag <code>it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk</code>
	 *
	 * @see getClassificazione_anag
	 */

	public void setClassificazione_anag(it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk newClassificazione_anag) {
		classificazione_anag = newClassificazione_anag;
	}
	/**
	 * Imposta l'elenco delle classificazioni anagrafiche disponibili.
	 *
	 * @param newClassificazioni_anag <code>java.util.Collection</code>
	 *
	 * @see getClassificazioni_anag
	 */

	public void setClassificazioni_anag(java.util.Collection newClassificazioni_anag) {
		classificazioni_anag = newClassificazioni_anag;
	}
	/**
	 * Imposta il <code>ComuneBulk</code> del comune fiscale selezionato.
	 *
	 * @param newComune_fiscale <code>it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk</code>
	 *
	 * @see getComune_fiscale
	 */

	public void setComune_fiscale(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune_fiscale) {
		comune_fiscale = newComune_fiscale;
	}
	/**
	 * Imposta il <code>ComuneBulk</code> del comune di nascita selezionato.
	 *
	 * @param newComune_nascita <code>it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk</code>
	 *
	 * @see getComune_nascita
	 */

	public void setComune_nascita(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune_nascita) {
		comune_nascita = newComune_nascita;
	}
	/**
	 * Imposta la descridzine riferita all'anagrafica.
	 *
	 * @param newDescrizioneAnagrafica La descrizione.
	 *
	 * @see getDescrizioneAnagrafica
	 */

	public void setDescrizioneAnagrafica(java.lang.String newDescrizioneAnagrafica) {
		descrizioneAnagrafica = newDescrizioneAnagrafica;
	}
	/**
	 * Imposta l'elenco delle dichiarazioni di intento.
	 *
	 * @param newDichiarazioni_intento <code>it.cnr.jada.bulk.BulkList</code>
	 *
	 * @see getDichiarazioni_intento
	 */

	public void setDichiarazioni_intento(it.cnr.jada.bulk.BulkList newDichiarazioni_intento) {
		dichiarazioni_intento = newDichiarazioni_intento;
	}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 17.27.35)
 * @param newDipendente boolean
 */
public void setDipendente(boolean newDipendente) {
	dipendente = newDipendente;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2002 15:13:40)
 * @param newFl_codice_fiscale_forzato java.lang.Boolean
 */
public void setFl_codice_fiscale_forzato(java.lang.Boolean newFl_codice_fiscale_forzato) {
	fl_codice_fiscale_forzato = newFl_codice_fiscale_forzato;
}
	/**
	 * Imposta il <code>NazioneBulk</code> relativo alla nazione della nazionalità selezionata.
	 *
	 * @param newNazionalita <code>it.cnr.jada.bulk.BulkList</code>
	 *
	 * @see getNazionalita
	 */

	public void setNazionalita(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk newNazionalita) {
		nazionalita = newNazionalita;
	}
public void setPg_comune_fiscale(java.lang.Long pg_comune_fiscale) {
	this.getComune_fiscale().setPg_comune(pg_comune_fiscale);
}
public void setPg_comune_nascita(java.lang.Long pg_comune_nascita) {
	this.getComune_nascita().setPg_comune(pg_comune_nascita);
}
public void setPg_nazione_fiscale(java.lang.Long pg_nazione_fiscale) {
	if (this.getComune_fiscale()!=null &&  this.getComune_fiscale().getNazione()!=null )
	getComune_fiscale().getNazione().setPg_nazione(pg_nazione_fiscale);
}
public void setPg_nazione_nazionalita(java.lang.Long pg_nazione_nazionalita) {
	this.getNazionalita().setPg_nazione(pg_nazione_nazionalita);
}

	public void setAssGruppoIva(BulkList anagraficoGruppiIvaCollegati) {
		this.assGruppoIva = anagraficoGruppiIvaCollegati;
	}

	/**
	 * Imposta l'elenco dei rapporti.
	 *
	 * @param newRapporti <code>it.cnr.jada.bulk.BulkList</code>
	 *
	 * @see getRapporti
	 */

	public void setRapporti(it.cnr.jada.bulk.BulkList newRapporti) {
		rapporti = newRapporti;
	}
/**
 * Insert the method's description here.
 * Creation date: (23/07/2001 10:49:21)
 * @param newTi_entita_persona_struttura int
 */
public void setTi_entita_persona_struttura(int newTi_entita_persona_struttura) {
	ti_entita_persona_struttura = newTi_entita_persona_struttura;
}
	public void setTi_italiano_estero_anag(String ti_italiano_estero) {
		if(NazioneBulk.ITALIA.equals( ti_italiano_estero ))
			setTi_italiano_estero( NazioneBulk.ITALIA );
		else if(NazioneBulk.ITALIA.equals( getTi_italiano_estero() ))
			setTi_italiano_estero( NazioneBulk.EXTRA_CEE );
	}
	/**
	 * Returns the pagamenti_esterni.
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList getPagamenti_esterni() {
		return pagamenti_esterni;
	}

	/**
	 * Sets the pagamenti_esterni.
	 * @param pagamenti_esterni The pagamenti_esterni to set
	 */
	public void setPagamenti_esterni(
		it.cnr.jada.bulk.BulkList pagamenti_esterni) {
		this.pagamenti_esterni = pagamenti_esterni;
	}
	public void validate() throws ValidationException{
		super.validate();
		if(getDt_nascita()!=null && getDt_nascita().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
			throw new ValidationException("La Data di nascita non può essere antecedente alla data odierna");
		if (getFl_cervellone() == true){
			if (getDt_inizio_res_italia()==null)
				throw new ValidationException("Inserire la Data di Inizio residenza/domicilio in italia");
			if (dipendente){
				if (getAnno_inizio_res_fis()==null){
					throw new ValidationException("Inserire l'anno di inizio residenza");
				}
				if (getAnno_fine_agevolazioni()==null){
					throw new ValidationException("Inserire l'anno di fine agevolazioni");
				}

			}

		}
		if (getFl_abilita_diaria_miss_est() == true && (getDt_inizio_diaria_miss_est()==null || getDt_fine_diaria_miss_est()==null))
			throw new ValidationException("Inserire la Data di inizio e fine autorizzazione ad avere la diaria per particolari missioni estere");
		
	}
	
	public void setTipologia_istat(it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk newTipologia_istat) {
		tipologia_istat = newTipologia_istat;
	}
	
	public it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk getTipologia_istat() {
		return tipologia_istat;
	}
	
	public boolean isROds_tipologia_istat() {
		return tipologia_istat == null || tipologia_istat.getCrudStatus() == OggettoBulk.NORMAL;
	}
	
	public boolean isUtilizzata() {
		return utilizzata;
	}
	public void setUtilizzata(boolean utilizzata) {
		this.utilizzata = utilizzata;
	}

	/**
	 * Indica quando il codice fiscale deve essere read only.
	 *
	 * @return boolean
	 */
	public boolean isROcodice_fiscale() {
		return getCodice_fiscale()!=null&&isUtilizzata();
	}
	/**
	 * Indica quando la partita iva deve essere read only.
	 *
	 * @return boolean
	 */
	public boolean isROpartita_iva() {
		if (getTi_italiano_estero()!=null && getPartita_iva()!=null && 
			NazioneBulk.CEE.compareTo(getTi_italiano_estero())==0) 
			return false;
		
		return getPartita_iva()!=null&&isUtilizzata();
	}
	public boolean isUo_ente() {
		return uo_ente;
	}
	public void setUo_ente(boolean uo_ente) {
		this.uo_ente = uo_ente;
	}
	public boolean isUtilizzata_detrazioni() {
		return utilizzata_detrazioni;
	}
	public void setUtilizzata_detrazioni(boolean utilizzata_detrazioni) {
		this.utilizzata_detrazioni = utilizzata_detrazioni;
	}
	
	public boolean isStudioAssociato() {
		return getFl_studio_associato()!=null && getFl_studio_associato().booleanValue();
	}

	public int addToAssociatiStudio(Anagrafico_terzoBulk anagrafico_terzo) throws ValidationException {
		associatiStudio.add(anagrafico_terzo);
		anagrafico_terzo.setAnagrafico(this);
		anagrafico_terzo.setTi_legame(Anagrafico_terzoBulk.LEGAME_STUDIO_ASSOCIATO);
		return associatiStudio.size()-1;
	}		

	public Anagrafico_terzoBulk removeFromAssociatiStudio(int index) {
		return (Anagrafico_terzoBulk)associatiStudio.remove(index);
	}	
	public it.cnr.jada.bulk.BulkList getAssociatiStudio() {
		return associatiStudio;
	}
	public void setAssociatiStudio(it.cnr.jada.bulk.BulkList associatiStudio) {
		this.associatiStudio = associatiStudio;
	}
	public boolean isROFl_studio_associato() {
		return associatiStudio != null && !associatiStudio.isEmpty();
	}
	public boolean isSpeciale() {
		return getFl_speciale()!=null && getFl_speciale().booleanValue();
	}
	public Dictionary getTi_titoloStudioKeys() {
		return ti_titoloStudioKeys;
	}
	public boolean isNotGestoreIstat() {
		return notGestoreIstat;
	}
	public void setNotGestoreIstat(boolean notGestoreIstat) {
		this.notGestoreIstat = notGestoreIstat;
	}
	public Boolean isPartitaIvaVerificata(){
		return getFlPivaVerificata() != null && getFlPivaVerificata().equals("Y"); 
	}
	public boolean isROAnniCervelloniAbilitati(){
		return getFl_cervellone() == null || !getFl_cervellone() || !abilitatoTrattamenti;
	}
	public boolean isAbilitatoTrattamenti() {
		return abilitatoTrattamenti;
	}

	public void setAbilitatoTrattamenti(boolean abilitatoTrattamenti) {
		this.abilitatoTrattamenti = abilitatoTrattamenti;
	}

}
