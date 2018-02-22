package it.cnr.contab.compensi00.comp;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 14.45.34)
 * @author: CNRADM
 */
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IDetrazioniLavoroMgr extends ICRUDMgr {



/** 
  *  Tutti i controlli di validazione del periodo di inizio/fine validita' del nuovo record
  *  superati
  *    PreCondition:
  *      la tabella contiene altri record con stessa chiave di quello che sto inserendo
  *		 e la data di inizio validità del nuovo record e' successiva a quella 
  *		 del record (con stessa chiave) piu' recente in tabella (cioe' che ha data fine 
  *		 validita = infinito)
  *    PostCondition:
  *      Consente l'inserimento del Detrazioni
  
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, si è verificato un errore".
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Tutti i controlli per la cancellazione del record sono stati superati
  *    	PreCondition:
  *		   	Deve esistere un record con chiave uguale a quella del record da cancellare e con data fine validita 
  *		   	uguale a data di inizio validita del record da cancellare meno un giorno
  *    PostCondition:
  *        	Cancello il record ed aggiorno il record di periodo precendente a quello cancvellato 
  *		   	mettendo la sua data di fine ad infinito (31/12/2200)
  *
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *        	la tabella non contiene un altro record con stessa chiave di quello che sto cancellando
  *			e con data fine validita uguale alla data inizio validita del record da cancellare meno un giorno
  *    PostCondition:
  *        	Viene inviato il messaggio "Attenzione, deve esistere almeno un periodo".
**/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
