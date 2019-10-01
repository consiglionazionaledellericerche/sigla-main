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
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (23/11/2001 15.02.34)
 * @author: Paola sala
 */
 
public class MissioneDiariaComponent extends it.cnr.jada.comp.CRUDComponent implements IMissioneDiariaMgr,Cloneable,Serializable
{
/**
 * MissioneDiariaComponent constructor comment.
 */
public MissioneDiariaComponent() {
	super();
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
  * Nome: Tutte le validazioni precedenti superate
  * Pre:  E' stata richiesta la creazione di oggetto che supera tutte le validazioni
  * Post: Viene consentito l'inserimento dell'oggetto
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk che deve essere creato
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo di validzione:
  *		validaDiariaSuInserimento(userContext, oggettoBulk)
  *
**/	
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	Missione_diariaBulk diara = (Missione_diariaBulk)bulk;
	validaDiariaSuInserimento(userContext,diara);

	return super.creaConBulk(userContext, diara);
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
public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException {

	try {

		Missione_diariaBulk aDiaria = (Missione_diariaBulk)bulk;

		java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();		
		
		if (aDiaria.getDt_inizio_validita().compareTo(dataOdierna)>0){
			Missione_diariaHome aDiariaHome = (Missione_diariaHome)getHome(userContext, aDiaria);
			Missione_diariaBulk aDiariaPrecedente = aDiariaHome.findIntervalloPrecedente(aDiaria, true);
			if(aDiariaPrecedente != null){
				aDiariaPrecedente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
				updateBulk(userContext, aDiariaPrecedente);
			}
			super.eliminaConBulk(userContext, aDiaria);
		}else{
			aDiaria.setDt_fine_validita(dataOdierna);
	    	updateBulk(userContext, aDiaria);
		}

	}catch(javax.ejb.EJBException ex){
	   throw handleException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
	   throw handleException(ex);
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
private DivisaBulk findDivisa(UserContext userContext, Missione_diariaBulk diaria) throws ComponentException{

	try{

		String tipoNazione = null;
		if (diaria.getNazione()!=null)
			tipoNazione = diaria.getNazione().getTi_nazione();

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
 * Viene richiesto il completamento dell'oggetto bulk passato come parametro
 * Viene cercata la valuta di default associata alla nazione 
 * selezionato dall'utente
 * 
 * Pre-post_conditions
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
public Missione_diariaBulk gestioneNazione(UserContext userContext, Missione_diariaBulk bulk) throws ComponentException{

	bulk.setDivisa(findDivisa(userContext, bulk));
	return bulk;
}
/**
 * Validazione OggettoBulk su inserimento
 *
 * Pre-post-conditions
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
 * Metodo richiamato:
 *		validaDataInizioValidita(OggettoBulk)
*/
private void validaDiariaSuInserimento(UserContext userContext,Missione_diariaBulk diaria) throws ComponentException {
	try{

		Missione_diariaHome home = (Missione_diariaHome)getHome(userContext, diaria);
	   	home.validaPeriodoInCreazione(userContext, diaria);

	}catch(Throwable ex){
		throw handleException(ex);
	}
}
}
