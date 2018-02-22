package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.tabrif.bulk.CambioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface ICambioMgr
{


/** 
  *  tutti i controlli superati
  *    PreCondition:
  *      validaCambio = true
  *    PostCondition:
  *      Consente la modifica del cambio.
  *  validaCambio  non superata
  *    PreCondition:
  *      L'inizio del periodo è incongruente.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, non si possono creare cambi con data inizio validita inferiore a quella esistente".
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *  Controllo cambio di default
  *    PreCondition: Il cambio è quello di default
  *    PostCondition: Viene sollevata un'eccezione
  *
  *  Tutti i controlli superati.
  *    PreCondition: Nessun errore rilevato.
  *    PostCondition: Viene consentita la camcellazione.
  *
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      validaVoceIva = true
  *    PostCondition:
  *      Consente la modifica della voce iva.
  *  validaCambio non superata
  *    PreCondition:
  *      validaCambio = false
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna voce iva di default preesistente.
  *    PostCondition:
  *      restituisce true
  *  Cambio non validato.
  *    PreCondition:
  *      E' stato modificata il valore o data inizio validita di un periodo esistente o inserito un nuovo periodo con data inizio inferiore aperiodo esistente.
  *    PostCondition:
  *      Ritorna false
 */
//^^@@
public abstract boolean validaCambio (UserContext aUC,CambioBulk cambio);
}
