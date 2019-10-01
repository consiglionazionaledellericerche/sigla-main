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
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
public interface IUnita_organizzativaMgr extends ICRUDMgr
{




/**
 * Esegue una operazione di ricerca di un CdsBulk o di una Unita_organizzativaBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca di un CdsBulk o di una Unita_organizzativaBulk
 * Pre:  La richiesta di ricerca di un CdsBulk o di una Unita_organizzativaBulk è stata generata
 * Post: La lista di CdsBulk o di Unita_organizzativaBulk che soddisfano i criteri di ricerca sono stati recuperati
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole eventuali clausole di ricerca specificate dall'utente
 * @param	bulk un CdsBulk o di una Unita_organizzativaBulk che deve essere ricercato
 * @return	la lista di CdsBulk o di Unita_organizzativaBulk risultante dopo l'operazione di ricerca.
 */

public abstract it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di creazione di un Cds o di una Unita' Organizzativa. 
 *
 * Pre-post-conditions:
 ************************************   CDS *************************************************
 * Nome: Creazione di Cds 
 * Pre:  La richiesta di creazione di un Cds è stata generata
 * Post: Un Cds viene creato con i dati inseriti dall'utente, il suo codice se non specificato dall'utente  
 *       viene generato automaticamente; vengono create l'Unita' Organizzativa UO-CDS e il Cdr Responsabile dell'U0_CDS.
 *       Al Cdr viene assegnato un livello in base alla regola descritta nel metodo 'creaCdrBulk'
 *
 *
 * Nome: Cds di tipo SAC gia' esistente
 * Pre:  La richiesta di creazione di un Cds con tiplogia SAC è stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 * 
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile del Cds non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di chiave duplicata
 * Pre:  Esiste già un CdsBulk persistente che possiede la stessa chiave
 * 		 primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Esercizio Fine non valido
 * Pre:  Per un Cds di tipo SAC e' stato specificato un valore di esercizio fie diverso dal valore di default (2100)
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 *
 ************************************   UNITA ORGANIZZATIVA *************************************************
 *
 * Nome: Creazione di Unità organizzativa dipendente da un CDS di tipo AREA
 * Pre:  La richiesta di creazione di una unità organizzativa che dipende da un CDS di tipo 
 *       Area di ricerca è stata generata
 * Post: Viene generate una ApplicationException con il messaggio "Non è possibile aggiungere una unità organizzativa ad un'area
 * 		 di ricerca"
 *
 * Nome: Creazione di Unita' organizzativa con specifica di area di ricerca collegata
 * Pre:  La richiesta di creazione di un' Unita' organizzativa è stata generata con specifica di codice CDS di area di ricerca collegata
 * Post:
 *      Un' unita organizzativa viene creata con i dati inseriti dall'utente, il suo codice se non specificato dall'utente viene generato automaticamente; viene creato il Cdr Responsabile dell'U0 a cui viene assegnato un livello in base alla seguente regola:
 *      - Se il CDS da cui dipende è di tipo SAC, il CDR è di I livello.
 *      - Se il CDS da cui dipende non è di tipo SAC, il CDR è di II livello.
 *
 *      Viene aggiornato il piano dei conti finanziari per la parte 1 spese cnr categoria 1 come segue:
 *      - Si determina se esiste il CDS area come SOTTOARTICOLO della linea del PDC Finanziario parte 1 spese cnr categoria 1 che contiene il CDS presidente dell'area collegata all'UO in processo come CAPITOLO. Nel caso tale SOTTOARTICOLO non sia presente, viene creato
 *
 * Nome: Creazione di Unita' organizzativa collegata ad area di ricerca che è Presidente dell'Area
 * Pre: La richiesta di creazione di un' Unita' organizzativa collegata ad Area di ricerca e che sia Presidente dell'area è stata generata
 * Post: Un' unita organizzativa viene creata collegata ad un area come Presidente dell'area
 *
 * Nome: Creazione di Unita' organizzativa
 * Pre:  La richiesta di creazione di un' Unita' organizzativa è stata generata
 * Post: Un' unita organizzativa viene creata con i dati inseriti dall'utente, il suo codice se non specificato 
 *       dall'utente viene generato automaticamente; viene creato il Cdr Responsabile dell'U0 a cui viene assegnato
 *       un livello in base alla regola descritta nel metodo 'creaCdrBulk':
 *
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile dell' Unita' organizzativa non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Responsabile amministrativo inesistente
 * Pre:  Il Codice Terzo definito come responsabile amministrativo dell' Unita' organizzativa non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente 
 *
 * Nome: Errore di chiave duplicata
 * Pre:  Esiste già un' Unita_organizzativaBulk persistente che possiede la stessa chiave
 * 		 primaria di quella specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore Presidente dell'area già definito
 * Pre:  Esiste già di un presidente dell'area a cui è collegata l'UO
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Cds inesistente
 * Pre:  Il Cds specificato dall'utente non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Area Ricerca inesistente
 * Pre:  Il Cds specificato come Area di Ricerca per l'Unita' Organizzativa non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Una UO del SAC non può essere presidente dell'AREA
 * Pre:  L'UO appartiene al SAC ed è richiesto che sia Presidente dell'area a cui è collegata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Esercizio Fine non valido per unita padre
 * Pre:  L'esercio fine dell'UO è superiore all'esercizio fine del Cds da cui l'UO dipende
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il CdsBulk o l'Unita_organizzativaBulk che deve essere creato
 * @return	il CdsBulk o l'Unita_organizzativaBulk risultante dopo l'operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di eliminazione di CdsBulk o di Unita_organizzativaBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di un Cds 
 * Pre:  La richiesta di cancellazione di un Cds e' stata generata
 * Post: Il Cds e' stato cancellato
 *
 * Nome: Cancellazione di un'Unita' Organizzativa
 * Pre:  La richiesta di cancellazione di un'Unita' Organizzativa e' stata generata
 * Post: L'Unita' Organizzativa  e' stata cancellata
 *
 * Nome: Cancellazione di un'Unita' Organizzativa con Area di Ricerca 
 * Pre:  La richiesta di cancellazione di un'Unita' Organizzativa con area di Ricerca e' stata generata
 * Post: L'Unita' Organizzativa  e' stata cancellata e il Piano dei Conti finanziario viene aggiornato
 *
 * Nome: Cancellazione di un'Unita' Organizzativa UO-CDS
 * Pre:  La richiesta di cancellazione di un'Unita' Organizzativa UO-CDS e' stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di CdsBulk o di UnitaOrganizzativaBulk che deve essere cancellata 
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

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
 * Nome: Tutti i controlli superati
 * Pre: L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di modifica di un Cds o di una Unita' Organizzativa. 
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica di Cds 
 * Pre:  La richiesta di modifica di un Cds è stata generata
 * Post: Un Cds e' stato modificato con i dati inseriti dall'utente 
 *
 * Nome: Modifica della descrizione di un  Cds 
 * Pre:  La richiesta di modifica della descrizione di un Cds è stata generata
 * Post: La descrizione del Cds e' stata modificata e la descrizione dell'UO-CDS e' stata modificata 
 *
 * Nome: Modifica dell'esercizio fine di un  Cds - OK
 * Pre:  La richiesta di modifica dell'esercizio fine di un CDS è stata generata e il nuovo esercizio fine e' valido
 *       (metodo 'verificaEsercizioFine')
 * Post: L'esercizio fine del Cds e' stato modificato; sono stati aggiornati in cascata anche tutti gli esercizi fine di
 *       tutte le UO che dipendono dal CDS e hanno esercizio fine maggiore rispetto a quello del Cds; sono stati aggiornati 
 *       in cascata anche tutti gli esercizi fine di tutti i Cdr che dipendono dalle UO che dipendono dal Cds e che hanno
 *       esercizio fine maggiore rispetto a quello del Cds; sono stati aggiornati in cascata anche tutti gli esercizi fine di
 *       tutte le linee di attività definite per i Cdr che dipendono dalle UO che dipendono dal Cds e che hanno esercizio 
 *       fine maggiore rispetto a quello del Cds.
 *
 * Nome: Modifica dell'esercizio fine di un  Cds - Errore
 * Pre:  La richiesta di modifica dell'esercizio fine di un CDS è stata generata e il nuovo esercizio fine non e' valido
 *       (metodo 'verificaEsercizioFine')
 * Post: Una ApplicationException viene lanciata per segnalare all'utente l'impossibilità di aggiornare l'esercizio fine
 *
 * Nome: Modifica dell'esercizio fine di una Unita organizzativa - OK
 * Pre:  La richiesta di modifica dell'esercizio fine di un'Unita organizzativa è stata generata e il nuovo esercizio fine e' valido
 *       (metodo 'verificaEsercizioFine')
 * Post: L'esercizio fine dell'Unita organizzativa e' stato modificato; sono stati aggiornati 
 *       in cascata anche tutti gli esercizi fine di tutti i Cdr che dipendono dall'Unita organizzativa e che hanno
 *       esercizio fine maggiore rispetto a quello dell'Unita organizzativa; sono stati aggiornati in cascata anche tutti 
 *       gli esercizi fine di tutte le linee di attività definite per i Cdr che dipendono dall'Unita organizzativa e che hanno esercizio 
 *       fine maggiore rispetto a quello dell'Unita organizzativa
 *
 * Nome: Modifica dell'esercizio fine di un'Unita organizzativa - Errore
 * Pre:  La richiesta di modifica dell'esercizio fine di un'Unita organizzativa è stata generata e il nuovo esercizio fine non e' valido
 *       (metodo 'verificaEsercizioFine')
 * Post: Una ApplicationException viene lanciata per segnalare all'utente l'impossibilità di aggiornare l'esercizio fine

 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile del Cds non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Modifica di Unita' Organizzativa
 * Pre:  La richiesta di modifica di un' Unita' Organizzativa è stata generata
 * Post: L'Unita' Organizzativa e' stato modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato
 *       aggiornato di conseguenza
 * 
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile dell' Unita' organizzativa non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Responsabile amministrativo inesistente
 * Pre:  Il Codice Terzo definito come responsabile amministrativo dell' Unita' organizzativa non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente 
 *
 * Nome: Errore di Area Ricerca inesistente
 * Pre:  Il Cds specificato come Area di Ricerca per l'Unita' Organizzativa non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Modifica di Unita' Organizzativa con modifica di Area di Ricerca specificata
 * Pre: L'area di ricerca specificata è cambiata
 * Post: L'Unità Organizzativa è stata modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato aggiornato eventualemente eliminando il SOTTOARTICOLO corrispondente all'area di origine e creando quello corrispondente all'area di destinazione
 *       n.b. "eventualmente" si riferisce al fatto che l'aggiunta o l'eliminazione del SOTTOARTICOLO sono subordinati al cambiamento o eliminazione di presidente di area. In uqesto caso l'intervento sul PDC finanziario potrebbe essere diretto all'aggiunzione o eliminazione di sottoarticoli relativi alle due aree iniziale e finale.
 *
 * Nome: Modifica di Unita' Organizzativa con rimozione di Area di Ricerca specificata
 * Pre: L'area di ricerca non è più specificata.
 * Post: L'Unità Organizzativa è stata modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato aggiornato eventualemente eliminando il SOTTOARTICOLO corrispondente all'area di origine
 *       n.b. "eventualmente" si riferisce al fatto che l'eliminazione del SOTTOARTICOLO è subordinato al fatto che l'uo eliminata era il presidente dell'areadente dell'area. Per una definizione di SOTTOARTICOLO vedi specifica di "creaConBulk"
 *
 * Nome: Modifica di Unita' Organizzativa con aggiunzione di Area di Ricerca specificata
 * Pre: L'area di ricerca non era specificata ed ora lo è
 * Post: L'Unità Organizzativa è stata modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato aggiornato eventualemente aggiungendo il SOTTOARTICOLO corrispondente all'area di destinazione.
 *       n.b. "eventualmente" si riferisce al fatto che l'aggiunta del SOTTOARTICOLO è subordinato al fatto che l'uo collegata all'area risulti anche presidente dell'area. Per una definizione di SOTTOARTICOLO vedi specifica di "creaConBulk"
 *
 * Nome: Errore di esistenza del Presidente dell'area
 * Pre: L'UO in modifica specifica i essere Presidente dell'area ed esiste per l'area di ricerca un presidente dell'area diverso dall'UO stessa
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da visualizzare all'utente
 *
 * Nome: Una UO del SAC non può essere presidente dell'AREA
 * Pre:  L'UO appartiene al SAC ed è richiesto che sia Presidente dell'area a cui è collegata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il CdsBulk o l'Unita_organizzativaBulk che deve essere modiifcato
 * @return	il CdsBulk o l'Unita_organizzativaBulk risultante dopo l'operazione di modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}