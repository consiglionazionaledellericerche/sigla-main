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
import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class MissioneRimborsoKmComponent extends it.cnr.jada.comp.CRUDComponent implements IMissioneRimborsoKmMgr,Cloneable,Serializable{
/**
 * MissioneRimborsoKmComponent constructor comment.
 */
public MissioneRimborsoKmComponent() {

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
  *		validaRimborsoKm(userContext, oggettoBulk)
  *
**/	
public OggettoBulk creaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException{

	Missione_rimborso_kmBulk rimborsoKm = (Missione_rimborso_kmBulk)bulk;
	validaRimborsoKm(userContext, rimborsoKm);

    return super.creaConBulk(userContext, rimborsoKm);
            
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
public void eliminaConBulk (UserContext userContext, OggettoBulk bulk) throws ComponentException {

	try{

		Missione_rimborso_kmBulk rimborsoKm = (Missione_rimborso_kmBulk)bulk;

		java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();		
		
		if (rimborsoKm.getDt_inizio_validita().compareTo(dataOdierna)>0){
			Missione_rimborso_kmHome home = (Missione_rimborso_kmHome)getHome(userContext, rimborsoKm);
			Missione_rimborso_kmBulk rimborsoKmPrecedente = home.findIntervalloPrecedente(rimborsoKm, true);
			if(rimborsoKmPrecedente != null){
				rimborsoKmPrecedente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
				updateBulk(userContext, rimborsoKmPrecedente);
			}
			super.eliminaConBulk(userContext, rimborsoKm);
		}else{
			rimborsoKm.setDt_fine_validita(dataOdierna);
	    	updateBulk(userContext, rimborsoKm);
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
 * @param userContext	lo UserContext che genera la richesta
 * @param bulk			oggetto bulk da completare
 * @return Oggetto Bulk completo di nazione e valuta
 *
**/
public Missione_rimborso_kmBulk gestioneNazione(UserContext userContext, Missione_rimborso_kmBulk bulk) throws ComponentException{

	bulk.setNazione(findNazione(userContext, bulk.getTi_area_geografica()));

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
public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {

	try{
		Missione_rimborso_kmBulk rimborsoKm = (Missione_rimborso_kmBulk)super.inizializzaBulkPerModifica(aUC, bulk);
		Missione_rimborso_kmHome home = (Missione_rimborso_kmHome) getHome(aUC, rimborsoKm);

		Missione_rimborso_kmBulk rimborsoCancellato = home.getBulkLogicamenteCancellato(rimborsoKm);
		if(rimborsoCancellato != null)
			rimborsoKm.setDt_cancellazione(rimborsoCancellato.getDt_cancellazione());
		
		return rimborsoKm;
	}catch(Exception e){
		throw handleException(e);
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
public it.cnr.jada.persistency.sql.SQLBuilder selectNazioneByClause(UserContext aUC, Missione_rimborso_kmBulk bulk, NazioneBulk nazione, CompoundFindClause clauses) throws ComponentException {

	NazioneHome nazioneHome = (NazioneHome)getHome(aUC, NazioneBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = nazioneHome.createSQLBuilder();
	
	if (TipoAreaGeografica.ESTERO.equals(bulk.getTi_area_geografica()))
		sql.addClause("AND", "ti_nazione", sql.NOT_EQUALS, TipoAreaGeografica.ITALIA);

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
private void validaRimborsoKm(UserContext userContext, Missione_rimborso_kmBulk rimborsoKm) throws ComponentException{

	try{
		Missione_rimborso_kmHome home = (Missione_rimborso_kmHome)getHome(userContext, rimborsoKm);
		
		Missione_rimborso_kmBulk rimborsoCancellato = home.getBulkLogicamenteCancellato(rimborsoKm);
		if(rimborsoCancellato != null)
 		    throw new it.cnr.jada.comp.ApplicationException("Inserimento impossibile! Il rimborso e' stato annullato.");

	   	home.validaPeriodoInCreazione(userContext,rimborsoKm);

	}catch(Throwable ex){
		throw handleException(ex);
	}     
}
}
