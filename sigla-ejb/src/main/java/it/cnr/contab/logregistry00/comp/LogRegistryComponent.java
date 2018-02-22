package it.cnr.contab.logregistry00.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;


public class LogRegistryComponent 
	extends it.cnr.jada.comp.RicercaComponent 
	implements ILogRegistryMgr, java.io.Serializable, Cloneable {

/**
 * LogRegistryComponent constructor comment.
 */
public LogRegistryComponent() {
	super();
}
/**
 * Esegue una operazione di ricerca di un OggettoBulk con clausole.
 *
 * Pre-post-conditions:
 *
 * Nome: Clausole non specificate
 * Pre: L'albero delle clausole non è specficato (nullo)
 * Post: Viene generato un albero di clausole usando tutti i valori non nulli degli 
 *			attributi dell'OggettoBulk specificato come prototipo. L'elenco degli
 * 			attributi da utilizzare per ottenere le clausole è estratto dal
 * 			BulkInfo dell'OggettoBulk
 *
 * Nome: Tutti i controlli superati
 * Pre: Albero delle clausole di ricerca specificato (non nullo)
 * Post: Viene effettuata una ricerca di OggettoBulk compatibili con il bulk specificato. 
 * 			La ricerca deve essere effettuata utilizzando le clausole specificate da "clausole".
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".find"
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole	Una CompoundFindClause che descrive l'albero di clausole
 * 			da applicare nella ricerca
 * @param	bulk	l'OggettoBulk che è stato usato come prototipo per la generazione
 * 			delle clausole di ricerca.
 * @return	Un RemoteIterator sul risultato della ricerca
 */	
public it.cnr.jada.util.RemoteIterator cercaTabelleDiLog(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		return iterator(
			userContext,
			select(userContext,clausole,bulk),
			bulk.getClass(),
			getFetchPolicyName("find"));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
