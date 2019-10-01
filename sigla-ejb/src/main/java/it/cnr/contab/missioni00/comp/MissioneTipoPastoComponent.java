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

package it.cnr.contab.missioni00.comp;

import it.cnr.contab.anagraf00.tabter.bulk.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public class MissioneTipoPastoComponent extends it.cnr.jada.comp.CRUDComponent implements IMissioneTipoPastoMgr,Cloneable,Serializable
{
public  MissioneTipoPastoComponent()
{
	/*Default constructor*/
}
/**
  * Esegue una operazione di creazione di un OggettoBulk.
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione NON superata: sovrapposizione con intervalli precedenti
  * Pre: Viene richeisto l'inserimento di un oggetto con data Inizio/Fine validita non compatibile
  * 	 con intervalli precedenti
  * Post: L'oggetto non viene inserito
  *		  Viene inviato il messaggio: "Attenzione sovrapposizione con intervalli di validità preesistenti"
  *
  * Nome: Validazione NON superata: oggetto bulk ANNULLATO
  * Pre:  E' stata richiesta la creazione di oggetto con versioni precedenti aventi data cancellazione NON nulla
  * Post: L'oggetto non viene inserito
  *		  Viene sollevato un messaggio di errore "Inserimento impossibile ! Il Codice xxx e' stato annullato."
  *
  * Nome: Tutte le validazioni precedenti superate
  * Pre:  E' stata richiesta la creazione di oggetto che supera tutte le validazioni
  * Post: Viene consentito l'inserimento dell'oggetto
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk che deve essere creato
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo di validzione:
  *		validaPastoSuInserimento(userContext, oggettoBulk)
  *
**/
public OggettoBulk creaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException{

	Missione_tipo_pastoBulk pasto = (Missione_tipo_pastoBulk)bulk;
	validaPastoSuInserimento(userContext,pasto);

	return super.creaConBulk(userContext, pasto);
}
/**
  * Viene richiesta l'eliminazione dell'Oggetto bulk
  *
  * Pre-post-conditions:
  *
  * Nome: Cancellazione di un intervallo futuro (cancellazione fisica)
  * Pre: Viene richiesta la cancellazione di un oggetto bulk con data inizio validita successiva alla data odierna
  * Post: L'oggetto bulk specificato viene cancellato fisicamente dalla Tabella e la versione precedente del record
  * 	  (se esiste) viene aggiornata impostanto la sua data Fine validita ad infinito (31/12/2200)
  *
  * Nome: Cancellazione di un intervallo attivo (cancellazione logica)
  * Pre: Viene richiesta la cancellazione di un oggetto bulk con data inizio validita precedente alla Data odierna
  * Post: Imposto la data Fine validita dell'oggetto a data odierna e aggiorno il record della Tabella
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk		l'OggettoBulk da eliminare
  *
**/
public void eliminaConBulk (UserContext aUC, OggettoBulk bulk) throws ComponentException {

	try{

		Missione_tipo_pastoBulk tipoPasto = (Missione_tipo_pastoBulk)bulk;

		java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		
		if (tipoPasto.getDt_inizio_validita().compareTo(dataOdierna)>0){
			Missione_tipo_pastoHome tipoPastoHome = (Missione_tipo_pastoHome)getHome(aUC, Missione_tipo_pastoBulk.class);
			Missione_tipo_pastoBulk tipoPastoPrecedente = tipoPastoHome.findIntervalloPrecedente(tipoPasto, true);
			if(tipoPastoPrecedente != null){
				tipoPastoPrecedente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
				updateBulk(aUC, tipoPastoPrecedente);
			}
			super.eliminaConBulk(aUC, tipoPasto);
		}else{
			tipoPasto.setDt_fine_validita(dataOdierna);
	    	updateBulk(aUC, tipoPasto);
		}
	}catch(javax.ejb.EJBException ex){
		throw handleException(bulk, ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch(it.cnr.jada.bulk.BusyResourceException ex){
		throw handleException(bulk, ex);
	}catch(it.cnr.jada.bulk.OutdatedResourceException ex){
		throw handleException(bulk, ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}

/**
 * Viene richiesto il caricamento della divisa associata alla nazione Italia
 * 
 * Pre-post_conditions
 *
 * Nome: Ricerca Divisa associata alla Nazione Italia
 * Pre: Viene richiesta la divisa di default associata alla nazione Italia
 * Post: Viene restituita la divisa di default
 *
 * Nome: Ricerca Divisa associata ad una nazione Estera
 * Pre: Viene richiesta la divisa di default associata ad una nazione estera
 * Post: Viene restituita una divisa vuota
 *
 * @param userContext lo UserContext che genera la richesta
 * @param tipoNazione indica il tipo di nazione di cui è richiesta la divisa
 * @return la nazione trovata
 *
**/
private DivisaBulk findDivisa(UserContext userContext, String tipoNazione) throws ComponentException{

	try{

		if (NazioneBulk.ITALIA.equals(tipoNazione)){
			DivisaHome home = (DivisaHome)getHome(userContext, DivisaBulk.class);
			return home.getDivisaDefault(userContext);
		}
		return new DivisaBulk();

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}catch(javax.ejb.EJBException ex){
		throw handleException(ex);
	}
}
/**
  * Viene richiesto il caricamento della nazione associata al <tipoNazione>
  * 
  * Pre-post_conditions
  *
  * Nome: Ricerca Nazione
  * Pre: Viene richiesta la nazione associata al tipo selezionato dall'utente (I = Italia, * = Indifferente)
  * Post: Viene restituita la nazione del tipo selezionato
  *
  * @param userContext lo UserContext che genera la richesta
  * @param tipoNazione indica il tipo di nazione da cercare (I = Italia, * = Indifferente)
  * @return la nazione trovata
  *
**/
private NazioneBulk findNazione(it.cnr.jada.UserContext userContext, String tipoNazione) throws ComponentException{

	try{
		if (TipoAreaGeografica.ESTERO.compareTo(tipoNazione)!=0){
			NazioneHome home = (NazioneHome)getHome(userContext, NazioneBulk.class);
			return home.findNazione(tipoNazione);
		}
		return new NazioneBulk();

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/*
private RifAreePaesiEsteriBulk findAreePaesiEsteri(it.cnr.jada.UserContext userContext, Missione_tipo_pastoBulk bulk) throws ComponentException{

	try{
		if(bulk.getNazione().getTi_nazione().compareTo(TipoAreaGeografica.INDIFFERENTE)!=0)
		{
			RifAreePaesiEsteriHome home = (RifAreePaesiEsteriHome)getHome(userContext, RifAreePaesiEsteriBulk.class);
			return home.findAreePaesiEsteri(bulk.getNazione().getCd_area_estera());
		}
		return new RifAreePaesiEsteriBulk()	;

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
*/

/**
 * Viene richiesto il completamento dell'oggetto bulk passato come parametro
 * Vengono cercate la nazione e la valuta associate al tipo area geografica
 * selezionato dall'utente
 * 
 * Pre-post_conditions
 *
 * Nome: Ricerca nazione corrispondente al Tipo Area Geografica dell'oggetto
 * Pre: Viene richiesta la nazione associata al tipo area geografica selezionato
 * Post: Viene caricata la Nazione corrispondente e impostata nell'oggetto bulk
 *
 * Nome: Ricerca valuta corrispondente alla Nazione trovata
 * Pre: Viene richiesta la valuta associata alla nazione trovata
 * Post: Viene caricata la valuta corrispondente e impostata nell'oggetto bulk
 *
 * @param userContext	lo UserContext che genera la richesta
 * @param bulk			oggetto bulk da completare
 * @return Oggetto Bulk completo di nazione e valuta
 *
**/
public Missione_tipo_pastoBulk gestioneNazione(UserContext userContext, Missione_tipo_pastoBulk bulk) throws ComponentException{

	bulk.setNazione(findNazione(userContext, bulk.getTi_area_geografica()));
	bulk.setDivisa(findDivisa(userContext, bulk.getTi_area_geografica()));

	return bulk;
}

/**
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di modifica.
  *
  * Pre-post-conditions:
  *
  * Nome: Oggetto non esistente
  * Pre: L'OggettoBulk specificato non esiste.
  * Post: Viene generata una CRUDException con la descrizione dell'errore.
  *
  * Nome: Oggetto bulk ANNULLATO
  * Pre: L'OggettoBulk ha una versione con DATA CANCELLAZIONE valorizzata
  * Post: Viene impostata la stessa data di cancellazione trovata anche nel record da 
  *		  modificare in modo che venga messo in visualizzazione
  *
  * Nome: Tutti i controlli superati
  * Pre: L'OggettoBulk specificato esiste.
  * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
  *		 per l'operazione di presentazione e modifica nell'interfaccia visuale.
  *		 L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
  *		 ottenuto concatenando il nome della component con la stringa ".edit"
  * 
  * @param	uc	lo UserContext che ha generato la richiesta
  * @param	bulk	l'OggettoBulk da preparare
  * @return	l'OggettoBulk preparato
  *
**/
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException {

	try{
		Missione_tipo_pastoBulk aPasto = (Missione_tipo_pastoBulk)super.inizializzaBulkPerModifica(aUC, bulk);
		Missione_tipo_pastoHome pastoHome = (Missione_tipo_pastoHome) getHome(aUC, aPasto);

		Missione_tipo_pastoBulk aPastoCancellato = pastoHome.getBulkLogicamenteCancellato(aPasto);
		if(aPastoCancellato != null)
			aPasto.setDt_cancellazione(aPastoCancellato.getDt_cancellazione());
		
		return aPasto;
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Nazioni
  *
  * Nome: Richiesta di ricerca di una nazione
  * Pre: E' stata generata la richiesta di ricerca di una nazione
  * Post: Viene restituito l'SQLBuilder per filtrare le nazioni associate
  *		  all'area geografica selezionata
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param abbattimento l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param nazione		l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param clauses		L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public it.cnr.jada.persistency.sql.SQLBuilder selectNazioneByClause(UserContext aUC, Missione_tipo_pastoBulk bulk, NazioneBulk nazione, CompoundFindClause clauses) throws ComponentException {

	NazioneHome nazioneHome = (NazioneHome)getHome(aUC, NazioneBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = nazioneHome.createSQLBuilder();
	
	if (TipoAreaGeografica.ESTERO.equals(bulk.getTi_area_geografica()))
		sql.addClause("AND", "ti_nazione", sql.NOT_EQUALS, TipoAreaGeografica.ITALIA);

	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectRifAreePaesiEsteriByClause(UserContext aUC, Missione_tipo_pastoBulk bulk, RifAreePaesiEsteriBulk aree, CompoundFindClause clauses) throws ComponentException {

	RifAreePaesiEsteriHome areeHome = (RifAreePaesiEsteriHome)getHome(aUC, RifAreePaesiEsteriBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = areeHome.createSQLBuilder();
	if (bulk.getNazione().getTi_nazione()!= null && bulk.getNazione().getTi_nazione().compareTo(TipoAreaGeografica.INDIFFERENTE) != 0)
		sql.addClause("AND", "cd_area_estera", sql.EQUALS, bulk.getNazione().getCd_area_estera());
	sql.addClause(clauses);
	return sql;
}
/**
 * Validazione OggettoBulk su inserimento
 *
 * Pre-post-conditions
 *
 * Nome: Oggetto bulk ANNULLATO
 * Pre: L'oggetto che sto inserendo ha una versione con DATA CANCELLAZIONE valorizzata
 * Post: L'oggetto non viene inserito
 *		 Viene sollevato un messaggio di errore "Inserimento impossibile ! Il Codice xxx e' stato annullato."
 *
 * Nome: Sovrapposizione con intervalli precedenti: periodo di Inizio/Fine validita del nuovo oggetto non valido
 * Pre: Viene richeisto l'inserimento di un oggetto con data Inizio/Fine validita non compatibile
 * 		con intervalli precedenti
 * Post: L'oggetto non viene inserito
 *		 Viene inviato il messaggio: "Attenzione sovrapposizione con intervalli di validità preesistenti"
 *
 * Nome: Validazioni precedenti superate
 * Pre: Validazioni precedenti superate
 * Post: Viene consentito l'inserimento dell'oggetto bulk
 *
 * Metodo richiamato della Home:
 *		validaPeriodoInCreazione(OggettoBulk)
*/
private void validaPastoSuInserimento(UserContext userContext,Missione_tipo_pastoBulk aPasto) throws ComponentException{

	try	{

		Missione_tipo_pastoHome pastoHome = (Missione_tipo_pastoHome) getHome(userContext, Missione_tipo_pastoBulk.class);
		
		Missione_tipo_pastoBulk aPastoCancellato = pastoHome.getBulkLogicamenteCancellato(aPasto);
		if(aPastoCancellato != null)
 		    throw new it.cnr.jada.comp.ApplicationException("Inserimento impossibile! Il Codice " + aPasto.getCd_ti_pasto() + " e' stato annullato.");

	   	pastoHome.validaPeriodoInCreazione(userContext, aPasto);

	}catch(Throwable e){
		throw handleException( e );
	}     
}
}
