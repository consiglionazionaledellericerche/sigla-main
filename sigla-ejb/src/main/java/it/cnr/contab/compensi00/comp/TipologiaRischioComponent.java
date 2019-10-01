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

package it.cnr.contab.compensi00.comp;

import java.io.Serializable;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

/**
 * Insert the type's description here.
 * Creation date: (22/03/2002 11.05.38)
 * @author: Roberto Fantino
 */
public class TipologiaRischioComponent extends it.cnr.jada.comp.CRUDComponent implements ITipologiaRischioMgr, Cloneable, Serializable{
/**
 * TipologiaRischioComponent constructor comment.
 */
public TipologiaRischioComponent() {
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
  *		  Viene inviato il messaggio: "La Data Inizio Validita non è valida. Deve essere maggiore di xxx"
  *
  * Nome: Tutte le validazioni precedenti superate
  * Pre:  E' stata richiesta la creazione di oggetto che supera tutte le validazioni
  * Post: Viene aggiornata la data fine validita dell'oggetto e consentito l'inserimento
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk che deve essere creato
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo di validzione:
  *		validaRischio(userContext, oggettoBulk)
  *
**/	
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	Tipologia_rischioBulk tipologia = (Tipologia_rischioBulk)bulk;
	validaRischio(userContext, tipologia);
	
	return super.creaConBulk(userContext, bulk);
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
public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	try {

		Tipologia_rischioBulk tipologia = (Tipologia_rischioBulk)bulk;

		java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		
		if (tipologia.getDt_inizio_validita().compareTo(dataOdierna)>0){
			Tipologia_rischioHome home = (Tipologia_rischioHome)getHome(userContext, tipologia);
			Tipologia_rischioBulk tipoPrec = home.findIntervalloPrecedente(tipologia, true);
			if(tipoPrec != null){
				tipoPrec.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
				updateBulk(userContext, tipoPrec);
			}
			super.eliminaConBulk(userContext, tipologia);
		}else{
			tipologia.setDt_fine_validita(dataOdierna);
	    	updateBulk(userContext, tipologia);
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
 * Controlli di validazione del periodo di inizio/fine validita' del nuovo record
 * Per poter CREARE un nuovo record:
 *
 * 1. Se non ho altri record in tabella con la stessa chiave, procedo con l'nserimento senza ulteriori controlli
 * 2. Se ho altri record con stessa chiave:
 *
 *		2.1. la data di inizio validita' del nuovo record deve essere maggiore di quella del record piu' recente
 *		2.2. la data di inizio validita' del nuovo record deve essere maggiore della data fine validita del record piu' recente
 *			==> la data di fine validita' del record piu' recente viene aggiornata con la data di inizio validita' del nuovo record meno un giorno
 * 		Altrimenti viene emesso il messaggio:
 *			 "La Data Inizio Validita non è valida. Deve essere maggiore di xxx"
 *
 * Pre-post-conditions
 *
 * Nome: Sovrapposizione con intervalli precedenti: periodo di Inizio/Fine validita del nuovo oggetto non valido
 * Pre: La data inizio validita dell'oggetto corrente è contenuta in un intervallo pre-esistente
 * Post: L'oggetto non viene inserito
 *		 Viene inviato il messaggio:
 *			 "La Data Inizio Validita non è valida. Deve essere maggiore di xxx"
 *
 * Nome: Validazioni precedenti superate
 * Pre: Validazioni precedenti superate
 * Post: Viene aggiornata la data fine validità del record più recente e viene consentito l'inserimento
 * 		 dell'oggetto bulk corrente
 *
*/
private void validaRischio(UserContext userContext, Tipologia_rischioBulk rischio) throws ComponentException{
	
	try {
		Tipologia_rischioHome home = (Tipologia_rischioHome)getHome(userContext, rischio);
	   	home.validaPeriodoInCreazione(userContext, rischio);

	}catch(Throwable ex){
		throw handleException(ex);
	}
}
}
