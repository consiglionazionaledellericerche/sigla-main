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
public interface IPDCEconPatrMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Esegue una operazione di ricerca di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca di Capoconto/Conto
 * Pre:  La richiesta di ricerca di un Capoconto/Contoè stata generata
 * Post: La lista di Conti/Capoconti che soddisfano i criteri di ricerca sono stati recuperati
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole eventuali clausole di ricerca specificate dall'utente
 * @param	bulk il ContoBulk o CapocontoBulk che deve essere ricercato
 * @return	la lista di ContoBulk o CapocontoBulk risultante dopo l'operazione di ricerca.
 */

public abstract it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di ricerca di un attributo di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca di attributo diverso da 'riapre_a_conto'
 * Pre:  La richiesta di ricerca di un attributo ricerca di un Capoconto/Conto è stata generata
 * Post: La lista dei valori possibili per quell'attributo e' stata recuperata
 *
 * Nome: Ricerca di attributo uguale a 'riapre_a_conto' con codice = codice conto
 * Pre:  La richiesta di ricerca dell'attributo 'riapre_a_conto' di un Conto è stata generata e il codice selezionato
 *       dall'utente e' il codice del Conto stesso
 * Post: L'istanza di Conto viene ritornata senza effettuare la ricerca
 *
 * Nome: Ricerca di attributo uguale a 'riapre_a_conto' con codice diverso da codice conto
 * Pre:  La richiesta di ricerca dell'attributo 'riapre_a_conto' di un Conto è stata generata e il codice selezionato
 *       dall'utente e' diverso dal codice del Conto stesso
 * Post: La lista di Conti che soddisfano le condizioni imposte dall'utente viene ritornata 
 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole eventuali clausole di ricerca specificate dall'utente
 * @param	bulk l'attributo che deve essere ricercato
 * @param	contesto il ContoBulk o CapocontoBulk da utilizzare come contesto della ricerca
 * @param	attributo il nome dell'attributo del ContoBulk o del CapocontoBulk che deve essere ricercato 
 * @return	la lista di oggetti risultanti dopo l'operazione di ricerca.
 */

public abstract it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di creazione di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di Capoconto senza codice proprio
 * Pre:  La richiesta di creazione di un Capoconto senza aver specificato un codice proprio è stata generata
 * Post: Un Capoconto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato generato 
 *       automaticamente.
 *
 * Nome: Creazione di Capoconto con codice proprio
 * Pre:  La richiesta di creazione di un Capoconto con un codice proprio specificato dall'utente
 *       è stata generata
 * Post: Un Capoconto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato formattato
 *
 * Nome: Errore di chiave duplicata per Capoconto
 * Pre:  Esiste già un Capoconto persistente che possiede la stessa chiave
 * 		 primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Creazione di Conto senza codice proprio
 * Pre:  La richiesta di creazione di un Conto senza aver specificato un codice proprio è stata generata
 * Post: Un Conto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato generato 
 *       automaticamente.
 *
 * Nome: Creazione di Conto con codice proprio
 * Pre:  La richiesta di creazione di un Conto con un codice proprio specificato dall'utente
 *       è stata generata
 * Post: Un Conto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato formattato
 *
 * Nome: Creazione di Conto senza codice 'riapre su conto'
 * Pre:  La richiesta di creazione di un Conto senza aver specificato il codice del conto su cui riapre
 *       è stata generata. Il conto riepiloga a stato patrimoniale (SPA) (Richiesta CNR n.35)
 * Post: Un Conto e' stato creato e il codice del conto su cui riapre e' il codice del conto creato
 *
 * Nome: Errore di chiave duplicata per Conto
 * Pre:  Esiste già un Conto persistente che possiede la stessa chiave primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Capoconto inesistente
 * Pre:  Il capoconto specificato dall'utente non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il ContoBulk o CapocontoBulk che deve essere creato
 * @return	il ContoBulk o CapocontoBulk risultante dopo l'operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di eliminazione di ContoBulk o di un CapocontoBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di un CapocontoBulk senza Conti da lui dipendenti
 * Pre:  La richiesta di cancellazione di un Capoconto che non ha Conti dipendenti e' stata generata
 * Post: Il Capoconto e' stato cancellato
 *
 * Nome: Cancellazione di un CapocontoBulk con Conti da lui dipendenti
 * Pre:  La richiesta di cancellazione di un Capoconto che ha Conti dipendenti e' stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Cancellazione di un ContoBulk
 * Pre:  La richiesta di cancellazione di un Contoe' stata generata
 * Post: Il Capoconto e' stato cancellato
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di CapocontoBulk o ContoBulk che deve essere cancellata 
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Esegue una operazione di modifica di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica di Capoconto 
 * Pre:  La richiesta di modifica di un Capoconto è stata generata
 * Post: Il Capoconto e' stato modificato
 *
 * Nome: Modifica di Conto 
 * Pre:  La richiesta di modifica di un Conto è stata generata
 * Post: Il Conto e' stato modificato
 *
 * Nome: Modifica di Conto senza codice 'riapre su conto' 
 * Pre:  La richiesta di modifica di un Conto senza aver specificato il codice del conto su cui riapre è stata generata
 * Post: Il Conto e' stato modificato e il codice del conto su cui riapre e' il codice del conto stesso
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il ContoBulk o CapocontoBulk che deve essere modificato
 * @return	il ContoBulk o CapocontoBulk risultante dopo l'operazione di modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}