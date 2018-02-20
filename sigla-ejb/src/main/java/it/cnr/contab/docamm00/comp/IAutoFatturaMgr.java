package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
/**
 * Insert the type's description here.
 * Creation date: (2/12/2002 3:41:20 PM)
 * @author: Paola sala
 */
public interface IAutoFatturaMgr extends IDocumentoAmministrativoMgr {
/** 
  *  Calcolo totali di fattura.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato.
  *  Inserimento del mandato.
  *    PreCondition:
  *      Si sta inserendo o modificando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato P o L.
  *  Annullamento del mandato.
  *    PreCondition:
  *      Si sta annullando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato A.
  *  Cancellazione del mandato.
  *    PreCondition:
  *      Si sta cancellando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato Q.
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */

public abstract void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,java.lang.String param3,java.lang.Integer param4,java.lang.Long param5,java.lang.String param6) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti I controli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene restituita l'autofattura inalterata.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Creazione di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene creata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
  *  Validazione autofattura.
  *    PreCondition:
  *      Non viene superata la validazione.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio relativo alla validazione non verificata.
  *  Assegnazione di un progressivo
  *    PreCondition:
  *      Non è possibile assegnare un progressivo per l'autofattura
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio relativo.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * creaConBulk method comment.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Cancellazione di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene cancellata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  estrazione sezionali validi
  *    PreCondition:
  *      Nessun errore riscontrato.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali corrispondente al tipo  sezionale richiesto.
  *  fattura di tipo non valido
  *    PreCondition:
  *      E' stata selezionata una fattura di tipo non valido.
  *    PostCondition:
  *      Viene inviato il messaggio: "Il tipo di fattura selezionato non è valido".
  *  Tipo sezionale autofattura
  *    PreCondition:
  *      Il tipo sezionale ricercato non è di tipo autofattura
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo sezionale per autofatture intra UE
  *    PreCondition:
  *      Il tipo sezionale ricercato non è contemporaneamente di tipo autofattura e tipo intra UE
  *    PostCondition:
  *      Viene ricercato il tipo sezionale generico per autofattura.
  *  Tipo sezionale per fatture S. Marino senza iva
  *    PreCondition:
  *      Il tipo sezionale ricercato non è contemporaneamente di tipo autofattura e tipo S. Marino senza iva
  *    PostCondition:
  *      Viene ricercato il tipo sezionale generico per autofattura.
  *  Ricerca del tipo sezionale generico per autofatture
  *    PreCondition:
  *      Il tipo sezionale per autofattura non è definito
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo fatture commerciali
  *    PreCondition:
  *      Il tipo di sezionale non è commerciale
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo fatture vendita
  *    PreCondition:
  *      Il tipo di sezionale non è definito per la vendita
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo della fattura
  *    PreCondition:
  *      Il tipo della fattura non è 'autofattura'
  *    PostCondition:
  *      Non viene restituito alcun elemento
  *  Esercizio del tipo sezionale dell'autofattura
  *    PreCondition:
  *      Il tipo sezionale non è definito per l'esercizio corrente
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Unità Organizzativa del tipo sezionale dell'autofattura
  *    PreCondition:
  *      Il tipo sezionale non è definito per la UO corrente
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  */

public abstract java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene aggiornata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * modificaConBulk method comment.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Aggiornamento di un dettaglio di documento amministrativo
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Il dettaglio viene aggiornato
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws it.cnr.jada.comp.ComponentException;
}
