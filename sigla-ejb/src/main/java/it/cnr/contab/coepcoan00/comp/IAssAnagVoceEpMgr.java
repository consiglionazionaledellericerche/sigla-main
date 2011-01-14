package it.cnr.contab.coepcoan00.comp;

import java.util.*;
import java.util.Vector;
import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
public interface IAssAnagVoceEpMgr extends ICRUDMgr
{

/**
 * Nome: Ricerca dell'attributo relativo alla classificazione anagrafica con codice '*'
 * Pre:  E' stata richiesta la ricerca di una classificazione anagrafica e l'utente ha specificato come codice
 *       il valore '*' ad indicare tutte le classificazioni anagrafiche
 * Post: Viene restituito un RemoteIterator contenente solamente l'oggetto fittizio ( con codice '*' ) che rappresenta
 *       tutte le classificazioni anagrafiche
 *
 * Nome: Ricerca dell'attributo relativo alla classificazione anagrafica con codice diverso  da '*'
 * Pre:  E' stata richiesta la ricerca di una classificazione anagrafica e l'utente ha specificato come codice
 *       un valore diverso da '*' o l'utente non ha specificato alcun codice
 * Post: Viene restituito un RemoteIterator contenente la lista di oggetti di tipo Classificazione_anagBulk
 *       risultante dall'esecuzione della query sul database
 *
 * Nome: Ricerca di un attributo diverso dalla classificazione anagrafica
 * Pre:  E' stata richiesta la ricerca di un attributo dell'associazione anag/contoEP diverso dalla classificazione 
 *       anagrafica
 * Post: Viene restituito un RemoteIterator contenente la lista degli oggettiBulk 
 *       risultante dall'esecuzione della query sul database
 * 
 *
 * @param userContext <code>UserContext</code> 
 * @param clausole <code>CompoundFindClause</code>  clausole specificate dall'utente
 * @param bulk <code>OggettoBulk</code>  oggettoBulk da ricercare
 * @param contesto <code>Ass_anag_voce_epBulk</code>  contesto della ricerca
 * @param attributo nome dell'attributo del contesto che deve essere ricercato
 * @return <code>RemoteIterator</code>  elenco di oggetti trovati
 *
 */

public abstract it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di un'associazione Anagrafico/Conto EP
 * Pre:  E' stata richiesta l'inizializzazione per modifica di un'associazione Anagrafico/Conto EP
 * Post: L'associazione viene restituita con inizializzata la classificazione anagrafica ( se il codice della classificazione
 *       anagrafica e' diverso da '*', la classificazione viene letta dal database, altrimenti la classificazione viene
 *       valorizzata con un oggetto fittizio che rappresenta tutte le classificazioni anagrafiche) 
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Ass_anag_voce_epBulk</code>  che deve essere inizializzata per modifica
 * @return <code>Ass_anag_voce_epBulk</code>  inizializzati per modifica
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
