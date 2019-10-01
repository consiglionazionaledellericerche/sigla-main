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
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
public interface IPDCFinMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Esegue una operazione di ricerca di un Elemento_voceBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca di Elemento voce
 * Pre:  La richiesta di ricerca di un Elemento voce è stata generata
 * Post: La lista di Elemento_vocebulk che soddisfano i criteri di ricerca sono stati recuperati
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole eventuali clausole di ricerca specificate dall'utente
 * @param	bulk l'Elemento_voceBulk che deve essere ricercato
 * @return	la lista di Elemento_voceBulk risultante dopo l'operazione di ricerca.
 */

public abstract it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di creazione di un Elemento_voceBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di Elemento_voce senza codice proprio
 * Pre:  La richiesta di creazione di un Elemento_voce senza aver specificato un codice proprio è stata generata
 * Post: Un Elemento_voceBulk stato creato con i dati inseriti dall'utente e il suo codice e' stato generato 
 *       automaticamente 
 *
 * Nome: Creazione di Elemento_voce con codice proprio
 * Pre:  La richiesta di creazione di un Elemento_voce con codice proprio specificato è stata generata
 * Post: Un Elemento_voceBulk stato creato con i dati inseriti dall'utente e il suo codice e' stato formattato
 *
 * Nome: Creazione di Elemento_voce di tipo CNR Spese Capitolo
 * Pre:  La richiesta di creazione di un Elemento_voce di tipo Capitolo di Spesa CNR è stata generata
 * Post: Un Capitolo di Spesa del CNR viene creato con i dati inseriti dall'utente; come elemento padre di tale capitolo
 *       viene ricercata la categoria con Codice = 2 (già creata in automatico) sotto al Titolo specificato dall'utente; .
 *
 * Nome: Creazione di Elemento_voce di tipo CDS Spese Capitolo
 * Pre:  La richiesta di creazione di un Elemento_voce di tipo CDS Spese Capitolo è stata generata
 * Post: Un capitolo di spesa del CDS  e' stato creato e tutte le associazioni Ass_ev_funz_tipoCdsBulk 
 *       selezionate dall'utente sono state create
 * 
 * Nome: Errore di elemento_voce padre inesistente
 * Pre:  L'elemento voce specificato come padre dell'elemento voce da creare non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di chiave duplicata
 * Pre:  Esiste già un Elemento_voceBulk persistente che possiede la stessa chiave
 * 		 primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l'Elemento_voceBulk che deve essere creato
 * @return	il Elemento_voceBulk risultante dopo l'operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di eliminazione di Elemento_voceBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di un Elemento voce senza altri elementi voce a lui associati
 * Pre:  La richiesta di cancellazione di un Elemento voce che non ha altri elementi voce associati
 *       e' stata generata
 * Post: L'Elemento_voceBulk e' stato cancellato
 *
 * Nome: Cancellazione di un Elemento voce con altri elementi a lui associati
 * Pre:  La richiesta di cancellazione di un Elemento voce che ha elementi voce associati
 *       e' stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 * 
 * Nome: Cancellazione di un Elemento voce di tipo CNR Spese Capitolo
 * Pre:  La richiesta di cancellazione di un Elemento voce di tipo CNR Spese Capitolo e' stata generata
 * Post: L'Elemento_voceBulk e' stato cancellato e tutte le associazioni Ass_ev_funz_tipoCds a lui associato sono
 *       state cancellate
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Elemento_voceBulk che deve essere cancellata
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
 * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
 * Post: L'elenco di funzioni e di tipologie di CDS viene caricato
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Elemento_voceBulk che deve essere inizializzata
 * @return l' Elemento_voceBulk inizializzato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
 * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
 * Post: L' Elemento_voceBulk viene aggiornato con l'elenco delle istanze di Ass_ev_funz_tipoCdsBulk assegnate in
 *		 precedenza a questo Elemento voce
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Elemento_voceBulk che deve essere inizializzata
 * @return l' Elemento_voceBulk inizializzato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
 * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
 * Post: L'elenco di funzioni e di tipologie di CDS viene caricato
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Elemento_voceBulk che deve essere inizializzata
 * @return l' Elemento_voceBulk inizializzato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
 * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
 * Post: L'elenco di funzioni e di tipologie di CDS viene caricato
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Elemento_voceBulk che deve essere inizializzata
 * @return l' Elemento_voceBulk inizializzato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Carica in una hashtable l'elenco di Tipologie di CDS  presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */
public abstract it.cnr.jada.util.OrderedHashtable loadTipologieCdsKeys(UserContext userContext) throws ComponentException;
}