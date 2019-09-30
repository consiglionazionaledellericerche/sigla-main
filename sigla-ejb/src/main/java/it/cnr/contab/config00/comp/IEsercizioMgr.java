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
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
public interface IEsercizioMgr extends ICRUDMgr
{




/**
 * Richiama la stored procedure che apre i piani di gestione per l'esercizio selezionata
 *
 * Pre-post-conditions:
 *
 * Nome: Apertura PdG da parte dell'ENTE
 * Pre:  La richiesta di aprire i piani di gestione è stata fatta dal CDS ENTE e l'esercizio è in stato STATO_INIZIALE = 'I'
 * Post: Viene richiamata la stored procedure che aggiorna a STATO_PDG_APERTO = 'G' lo stato del PDG ENTE 
 * 
 * Nome: Apertura PDG da parte di CDS non ENTE da stato = a STATO_INIZIALE
 * Pre:	 La richiesta di aprire i propri piani di gestione è stata fatta da un CDS non ENTE per un esercizio in STATO_INIZIALE = 'I'
 * Pre:  Lo stato dell'esercizio per l'ENTE è in STATO_PDG_APERTO = 'G'
 * Post: Viene richiamata la stored procedure che apre tutti i piani di gestione dei cdr appartenenti al CDS e viene aggiornato
 *       lo stato dell'esercizio selezionato dal valore STATO_INIZIALE al valore STATO_PDG_APERTO per il CDS in processo 
 *
 * Nome: Apertura PDG da parte di CDS non ENTE da stato DIVERSO da STATO_INIZIALE
 * Pre:	 La richiesta di aprire i propri piani di gestione è stata fatta da un CDS non ENTE per un esercizio in stato DIVERSO da STATO_INIZIALE
 * Pre:  Lo stato dell'esercizio per l'ENTE è in STATO_PDG_APERTO = 'G'
 * Post: Viene richiamata la stored procedure che apre tutti i piani di gestione dei cdr appartenenti al CDS
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk per il quale e' necessario aprire i pianio di gestione
 * @return	L'EsercizioBulk risultante dopo l'operazione di modifica stato
 */

public abstract it.cnr.contab.config00.esercizio.bulk.EsercizioBulk apriPianoDiGestione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di modifica dello stato di un EsercizioBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica stato Esercizio iniziale
 * Pre:  La richiesta di modifica dello stato di un Esercizio con stato 'iniziale' è stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente ("Non e' possibile cambiare lo stato iniziale");
 *
 * Nome: Modifica stato Esercizio a aperto - OK
 * Pre:  La richiesta di modifica dello stato di un Esercizio da 'Piano di gestione aperto' a 'aperto' è stata generata 
 *       e lo stato dell'esercizio precedente per il cds corrente e' chiuso ( provvisoriamente o definitivamente) e 
 *       non esistono cds con (esercizio - 2) in stato diverso da chiuso (provvisoriamente o definitivamente)
 * Post: Lo stato dell'Esercizio viene aggiornato ad 'aperto'
 *
 * Nome: Modifica stato Esercizio a aperto - Errore 1
 * Pre:  La richiesta di modifica dello stato di un Esercizio da 'iniziale' a 'aperto' è stata generata e lo stato
 *       dell'esercizio precedente per il cds corrente non e' chiuso ( provvisoriamente o definitivamente)
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente ("L'esercizio precedente non è stato chiuso");
 *
 * Nome: Modifica stato Esercizio a aperto - Errore 2
 * Pre:  La richiesta di modifica dello stato di un Esercizio da 'iniziale' a 'aperto' è stata generata e esiste almeno
 *       un cds per il quale l'esercizio del secondo anno precedente (esercizio -2) e' in stato non chiuso
 *       provvisoriamente o definitivamente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente ("Esistono esercizi non chiusi per l'anno XXXX");

 * Nome: Modifica stato Esercizio chiuso definitivamente
 * Pre:  La richiesta di modifica dello stato di un Esercizio con stato 'chiuso definitivamente' è stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente ("Non e' possibile cambiare lo stato");
 *
 * Nome: Modifica stato Esercizio a chiuso 
 * Pre:  La richiesta di modifica dello stato di un Esercizio a 'chiuso' ( provvisoriamente o definitivamente)
 *       è stata generata 
 * Post: Lo stato dell'Esercizio e' stato aggiornato
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk il cui stato deve essere modificato
 * @return	L'EsercizioBulk risultante dopo l'operazione di modifica stato
 */

public abstract it.cnr.contab.config00.esercizio.bulk.EsercizioBulk cambiaStatoConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di creazione di un EsercizioBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione dell'esercizio per l'ente
 * Pre:  La richiesta di creazione dell'esercizio per l'ente è stata generata
 * Post: L'esercizio dell'ente e' stato creato con stato 'iniziale'
 *
 * Nome: Creazione di un esercizio consecutivo ad un esercizio già creato per cds ente
 * Pre:  La richiesta di creazione di un esercizio consecutivo ad un esercizio già creato in precedenza
 *       è stata generata
 * Post: Il nuovo Esercizio e' stato creato con stato 'iniziale'
 *
 * Nome: Creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio esiste per l'ente
 * Pre:  La richiesta di creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio esiste per l'ente
 *       è stata generata
 * Post: Il nuovo Esercizio e' stato creato con stato 'iniziale'
 *
 * Nome: Creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio non esiste per l'ente
 * Pre:  La richiesta di creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio non esiste per l'ente
 *       è stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Creazione di un esercizio non consecutivo agli esercizi già creati
 * Pre:  La richiesta di creazione di un esercizio non consecutivo agli esercizi già creat in precedenza
 *       è stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk che deve essere creato
 * @return	L'EsercizioBulk risultante dopo l'operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Impedisce la cancellazione di un EsercizioBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione Esercizio
 * Pre:  La richiesta di cancellazione di un Esercizio è stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente ("Non e' possibile cancellare un Esercizio");
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk 
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Inizializza un esercizio per l'inserimento
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione per inserimento
 * Pre:  La richiesta di inizializzazione di un EsercizioBulk per inserimento e' stata generata
 * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk 
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Inizializza un esercizio per la modifica
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione per modifica
 * Pre:  La richiesta di inizializzazione di un EsercizioBulk per modifica e' stata generata
 * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk 
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Inizializza un esercizio per ricerca
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione per ricerca
 * Pre:  La richiesta di inizializzazione di un EsercizioBulk per ricerca e' stata generata
 * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk 
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Inizializza un esercizio per ricerca libera
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione per ricerca libera
 * Pre:  La richiesta di inizializzazione di un EsercizioBulk per ricerca libera e' stata generata
 * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l' EsercizioBulk 
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}