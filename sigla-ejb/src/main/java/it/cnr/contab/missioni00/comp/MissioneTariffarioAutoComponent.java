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

import java.io.Serializable;
import java.rmi.RemoteException;
import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class MissioneTariffarioAutoComponent extends it.cnr.jada.comp.CRUDComponent implements IMissioneTariffarioAutoMgr,Cloneable,Serializable {
/**
 * MissioneTariffarioAutoComponent constructor comment.
 */
public MissioneTariffarioAutoComponent() {


	
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
  *		validaTariffarioAuto(userContext, oggettoBulk)
  *
**/	
public OggettoBulk creaConBulk (UserContext userContext, OggettoBulk bulk) throws ComponentException{

	Missione_tariffario_autoBulk tariffario = (Missione_tariffario_autoBulk)bulk;
	validaTariffarioAuto(userContext, tariffario);

    return super.creaConBulk(userContext, tariffario);

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

		Missione_tariffario_autoBulk tariffario = (Missione_tariffario_autoBulk)bulk;

		java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
				
		if (tariffario.getDt_inizio_validita().compareTo(dataOdierna)>0){
			Missione_tariffario_autoHome home = (Missione_tariffario_autoHome)getHome(userContext, tariffario);
			Missione_tariffario_autoBulk tariffarioPrecedente = home.findIntervalloPrecedente(tariffario, true);
			if(tariffarioPrecedente != null){
				tariffarioPrecedente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
				updateBulk(userContext, tariffarioPrecedente);
			}
			super.eliminaConBulk(userContext, tariffario);
		}else{
			tariffario.setDt_fine_validita(dataOdierna);
	    	updateBulk(userContext, tariffario);
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
		Missione_tariffario_autoBulk tariffario = (Missione_tariffario_autoBulk )super.inizializzaBulkPerModifica(aUC, bulk);
		Missione_tariffario_autoHome home = (Missione_tariffario_autoHome) getHome(aUC, tariffario);

		Missione_tariffario_autoBulk  tariffarioCancellato = home.getBulkLogicamenteCancellato(tariffario);
		if(tariffarioCancellato != null)
			tariffario.setDt_cancellazione(tariffarioCancellato.getDt_cancellazione());

		return tariffario;
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
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
private void validaTariffarioAuto (UserContext userContext,Missione_tariffario_autoBulk tariffario) throws ComponentException{

	try{
		Missione_tariffario_autoHome home = (Missione_tariffario_autoHome)getHome(userContext, tariffario);
		
		Missione_tariffario_autoBulk tariffarioCancellato = home.getBulkLogicamenteCancellato(tariffario);
		if(tariffarioCancellato!= null)
 		    throw new it.cnr.jada.comp.ApplicationException("Inserimento impossibile! Il Codice " + tariffario.getCd_tariffa_auto() + " e' stato annullato.");

	   	home.validaPeriodoInCreazione(userContext, tariffario);

	}catch(Throwable ex){
		throw handleException(ex);
	}     
}
}
