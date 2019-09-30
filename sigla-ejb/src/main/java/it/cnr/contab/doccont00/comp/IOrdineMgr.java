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

package it.cnr.contab.doccont00.comp;

import it.cnr.contab.doccont00.ordine.bulk.OrdineBulk;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;

/**
 * Insert the type's description here.
 * Creation date: (01/02/2002 11.43.25)
 * @author: Roberto Fantino
 */
public interface IOrdineMgr extends ICRUDMgr {
/**
 *
 * Viene richiesto il completamento dell'ordine con i dati relativi al terzo
 *
 * Pre-post-conditions
 *
 * Nome: Completamento ordine da terzo
 * Pre: Vengono richiesti i Termini e le Modalità di Pagamento del Terzo selezionato
 * Post: Viene restituito l'ordine con i Termini e le Modalità di Pagamento associati
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ordine l'OggettoBulk da completare
 * @return	l'OggettoBulk completo
 *
**/

public abstract it.cnr.contab.doccont00.ordine.bulk.OrdineBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.ordine.bulk.OrdineBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Viene richeista l'eliminazione dell'Ordine
 *
 * Pre-post-conditions:
 *
 * Nome: Validazione superata
 * Pre: L'ordine NON è stato stampato in modo definitivo
 * Post: Viene consentita l'eliminazione dell'ordine
 *
 * Nome: Validazione NON superata
 * Pre: L'ordine è stato stampato in modo definitivo
 * Post: Viene generata una ApplicationException con il messaggio:
 *		 "L'Ordine è già stato stampato. Non è possibile eliminarlo!"
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk l'OggettoBulk da eliminare
 * @return	void
 *
 * Metodo privato chiamato per la validazione:
 *		validaOrdineSuElimina(OrdineBulk ordine);
 *
**/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Viene richiesta la lista delle Banche associate ad un Terzo
 *
 * Pre-post-conditions:
 *
 * Nome: Terzo NON selezionato
 * Pre: Non è stato selezionato un Terzo per l'ordine
 * Post: Non vengono caricate le banche.
 *
 * Nome: Terzo selezionato
 * Pre: E' stato selezionato un Terzo valido per l'ordine 
 * Post: Viene restituita la lista delle Banche associate al Terzo
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ordine l'OggettoBulk da completare
 * @return	La lista delle banche associate al terzo
 *
**/

public abstract java.util.List findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.ordine.bulk.OrdineBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di creazione.
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: E' stata richiesta l'inizializzazione di una istanza di OrdineBulk per inserimento
 * Post: L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
 *		 per una operazione di creazione
 *		 In particolare:
 *			- Data registrazione = data odierna
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bulk <code>OggettoBulk</code> l'istanza di Ordine bulk da inizializzare
 * @return <code>OggettoBulk</code> l'istanza di OrdineBulk inizializzata
 *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 * Pre-post-conditions:
 *
 * Nome: Oggetto non esistente
 * Pre: L'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore.
 *
 * Nome: Tutti i controlli superati
 * Pre:	L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 *
 *			In particolare vengono caricate tutte le Righe associate all'Ordine selezionato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
