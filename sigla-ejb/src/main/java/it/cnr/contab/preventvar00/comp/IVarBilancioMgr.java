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

package it.cnr.contab.preventvar00.comp;

import it.cnr.contab.preventvar00.bulk.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;

/**
 * Insert the type's description here.
 * Creation date: (03/04/2002 17.27.37)
 * @author: Roberto Fantino
 */
public interface IVarBilancioMgr extends it.cnr.jada.comp.ICRUDMgr {
/**
 * Insert the method's description here.
 * Creation date: (09/04/2002 10.20.37)
 * @return it.cnr.jada.bulk.OggettoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param bulk it.cnr.jada.bulk.OggettoBulk
 * @exception it.cnr.jada.comp.ComponentException The exception description.
 */
public abstract OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException;
/**
  *
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di creazione.
  *
  * Pre-post-conditions:
  *
  * Nome: Tutti i controlli superati
  * Pre: E' stata richiesta l'inizializzazione di una istanza di Var_bilancioBulk per inserimento
  * Post: L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		 per una operazione di creazione
  *		 In particolare:
  *			- Carico il Bilancio associato all'ESERCIZIO e al CDS di scrivania
  *
  * @param	userContext lo UserContext che ha generato la richiesta
  * @param	bulk OggettoBulk l'istanza di Var_bilancioBulk da inizializzare
  * @return	OggettoBulk l'istanza di Var_bilancioBulk inizializzata
  *
  * Metodo privato chiamato:
  *		completaBulk(UserContext userContext, Var_bilancioBulk varBilancio);
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
  *			In particolare vengono caricate tutti i dettagli associati alla Variazione di Bilancio selezionata
  * 
  * @param	userContext lo UserContext che ha generato la richiesta
  * @param	bulk	l'OggettoBulk da preparare
  * @return	l'OggettoBulk preparato
  *
  * Metodo privato chiamato:
  *		caricaDettagli(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di ricerca.
  * Inizializzazione di una istanza di Var_bilancioBulk per ricerca
  *
  * Nome: Inizializzazione per ricerca
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Var_bilancioBulk per ricerca
  * Post: Viene inizializzato il Bilancio della variazione caricandolo da db
  *
  * @param	userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param	bulk <code>OggettoBulk</code> la variazione da inizializzare per la ricerca
  * @return	la variazione inizializzata per la ricerca
  *
  * Metodo privato chiamato:
  *		completaBulk(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di ricerca guidata.
  * Inizializzazione di una istanza di Var_bilancioBulk per ricerca guidata
  *
  * Nome: Inizializzazione per ricerca
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Var_bilancioBulk per ricerca guidata
  * Post: Viene inizializzato il Bilancio della variazione caricandolo da db
  *
  * @param	userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param	bulk <code>OggettoBulk</code> la variazione da inizializzare per la ricerca guidata
  * @return	la variazione inizializzata per la ricerca guidata
  *
  * Metodo privato chiamato:
  *		completaBulk(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Insert the method's description here.
 * Creation date: (09/04/2002 10.20.37)
 * @return it.cnr.jada.bulk.OggettoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param bulk it.cnr.jada.bulk.OggettoBulk
 * @exception it.cnr.jada.comp.ComponentException The exception description.
 */
public abstract OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
/**
  *
  * Viene richiesto il salvataggio definitivo della Variazione di Bilancio selezionata
  *
  * Pre-post-conditions:
  *
  * Nome: Salvataggio definitivo della Variazione
  * Pre: Viene richiesto il salvataggio definitivo della Variazione
  * Post: Viene salvata in modo definitivo la Variazione di Bilancio selezionata
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	varBilancio l'OggettoBulk da salvara in modo definitivo
  * @return	la variazione di bilancio aggiornata
  *
  * Metodi privati chiamati:
  *		esitaVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
  *		reloadVarBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/

public abstract it.cnr.contab.preventvar00.bulk.Var_bilancioBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.preventvar00.bulk.Var_bilancioBulk param1) throws it.cnr.jada.comp.ComponentException;
}
