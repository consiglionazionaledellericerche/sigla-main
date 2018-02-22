package it.cnr.contab.docamm00.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;


public interface ITariffarioMgr
{


/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      La tariffa inserita ha data inizio validità interna all'ultimo periodo preesistente (con data fine = infinito) OR è il primo record della validità della tariffa e ha fine = infinito.
  *    PostCondition:
  *      Consente l'inserimento della tariffa.
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *      Si e verificato un errore.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  si è verificato un errore".
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Eliminazione periodo.
  *    PreCondition:
  *      Periodi presenti =1.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  deve esistere almeno un periodo".
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentita la camcellazione.
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna condizione di errore.
  *    PostCondition:
  *      Consente la modifica della tariffa.
  *  validazione tariffa non superata
  *    PreCondition:
  *      Sono state modificate le date di validita di un periodo esistente.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna condizione di errore.
  *    PostCondition:
  *      Consente la modifica della tariffa.
  *  validazione tariffa non superata
  *    PreCondition:
  *      Sono state modificate le date di validita di un periodo esistente.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */
//^^@@
public abstract Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException ;
}
