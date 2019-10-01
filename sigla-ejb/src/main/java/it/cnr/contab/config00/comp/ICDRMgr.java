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

package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface ICDRMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Esegue una operazione di creazione di un CdrBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di Cdr dipendente da un CDS di tipo AREA
 * Pre:  La richiesta di creazione di un Cdr che dipende da una unita' organizzativa definita per un CDS di tipo 
 *       Area di ricerca è stata generata
 * Post: Viene generate una ApplicationException con il messaggio "Non è possibile aggiungere un CDR ad un'area
 * 		 di ricerca"
 *
 * Nome: Creazione di Cdr dipendente da un CDS di tipo SAC
 * Pre:  La richiesta di creazione di un Cdr che dipende da una unita' organizzativa definita per un CDS di tipo 
 *       Struttura Amministrativa Centrale è stata generata
 * Post: Un Cdr di secondo livello viene creato con i dati inseriti dall'utente, se l'utente non ha specificato un 
 *       codice proprio del Cdr ne viene generato uno automaticamente; il cdr di afferenza assegnato e' il CDR di 
 *       primo livello dell'Unita' organizzativa da cui dipende il Cdr appena creato.
 *
 * Nome: Creazione di Cdr dipendente da un CDS di tipo diverso da SAC
 * Pre:  La richiesta di creazione di un Cdr che dipende da una unita' organizzativa definita per un CDS di tipologia 
 *       differente da Struttura Amministrativa Centrale è stata generata
 * Post: Un Cdr di secondo livello viene creato con i dati inseriti dall'utente, se l'utente non ha specificato un 
 *       codice proprio del Cdr ne viene generato uno automaticamente; il cdr di afferenza assegnato e' il CDR
 *       responsabile dell'UO CDS. 
 *
 * Nome: Errore di unita' organizzativa inesistente
 * Pre:  L'unita' organizzativa specificata dall'utente non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Cdr afferenza inesistente
 * Pre:  Il Cdr di afferenza del Cdr da creare non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile del Cdr non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di chiave duplicata
 * Pre:  Esiste già un CdrBulk persistente che possiede la stessa chiave
 * 		 primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore per Esercizio Fine
 * Pre:  L'attributo esercizio fine del Cdr da creare non e' valido (la validazione e' eseguita dal metodo
 *       'verificaEsercizioFine')
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il CdrBulk che deve essere creato
 * @return	il CdrBulk risultante dopo l'operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Esegue una operazione di eliminazione di CdrBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di un Cdr non responsabile dell'UO
 * Pre:  La richiesta di cancellazione di un Cdr non responsabile di una Unita' Organizzativa e' stata generata
 * Post: Il Cdr e' stato cancellato
 *
 * Nome: Cancellazione di un Cdr responsabile dell'UO
 * Pre:  La richiesta di cancellazione di un Cdr responsabile di una Unita' Organizzativa e' stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di CdrBulk che deve essere cancellata 
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * TRUE se il Cdr passato come parametro e' Ente
 * FALSE altrimenti
 *
*/
public abstract boolean isCdrEnte(UserContext userContext,CdrBulk cdr) throws ComponentException;
/**
 * TRUE se il Cdr di scrivania e' Ente
 * FALSE altrimenti
 *
*/
public abstract boolean isEnte(UserContext userContext) throws ComponentException;
/**
 * Esegue una operazione di modifca di un CdrBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica di attributi diversi da esercizio fine
 * Pre:  La richiesta di modifica di un attributo diverso da esercizio fine per un Cdr è stata generata
 * Post: Il Cdr e' stato modificato
 *
 * Nome: Modifica dell'attributo esercizio fine - Errore 
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr e' stata generata
 *       e la verifica della correttezza del nuovo esercizio (eseguita dal metodo 'verificaEsercizioFine') non e'
 *		 stata superata
 * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
 *
 * Nome: Modifica dell'attributo esercizio fine - Ok
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr e' stata generata e
 *       tutti i controlli sono stati superati
 * Post: L'esercizio fine del Cdr e' stato aggiornato e tutti gli esercizi fine delle linee di attività definite per
 *		 quel Cdr e che hanno esercizio fine maggiore rispetto a quello del Cdr sono stati aggiornati 
 *       (metodo 'aggiornaEsercizioFine')
 *
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile del Cdr non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il CdrBulk che deve essere modificato
 * @return	il CdrBulk risultante dopo l'operazione di modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
