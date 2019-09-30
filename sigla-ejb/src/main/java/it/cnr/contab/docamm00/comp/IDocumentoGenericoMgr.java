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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface IDocumentoGenericoMgr extends IDocumentoAmministrativoMgr
{
/** 
  *  aggiorna le modalità e i temini di pagamento e la lista delle banche 
  *    PreCondition:
  *      E' stato aggiunto un dettaglio ad un generico attivo con righe già inserite.
  *    PostCondition:
  *      Vengono riportate le modalita,termini e banche della prima riga (default)
 */

public abstract it.cnr.jada.bulk.OggettoBulk aggiornaModalita(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param2,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *  Non è utilizzato
 */

public abstract void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,java.lang.String param3,java.lang.Integer param4,java.lang.Long param5,java.lang.String param6) throws it.cnr.jada.comp.ComponentException;
/** 
  *  calcola il consuntivo di un documento
  *  cambio modificato
  *    PreCondition:
  *      Viene modificato il cambio
  *    PostCondition:
  *      Aggiorna gli importi per il calcolo
  *  valuta modificata
  *    PreCondition:
  *      Viene modificato la valuta
  *    PostCondition:
  *      Aggiorna gli importi per il calcolo
  *  importo modificato
  *    PreCondition:
  *      Viene modificata la riga del documento
  *    PostCondition:
  *      Aggiorna gli importi per il calcolo
 */

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di accertamenti congruenti con il documento generico che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle accertamenti per le documenti generici
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle scadenze degli accertamenti.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	accertamento definitiva
  *		PreCondition:
  *			La scadenza non appartiene ad un'accertamento definitivo
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	accertamenti non cancellate
  *		PreCondition:
  *			La scadenza appartiene ad un'accertamento cancellato
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	accertamenti associate ad altri documenti amministrativi
  *		PreCondition:
  *			La scadenza appartiene ad un'accertamento associata ad altri documenti amministrativi
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	accertamenti della stessa UO
  *		PreCondition:
  *			La scadenza dell'accertamento non appartiene alla stessa UO di generazione documento generico
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitatazione filtro di selezione sul debitore dell'accertamento
  *		PreCondition:
  *			La scadenza dell'accertamento ha un debitore diverso da quello della documento generico
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Disabilitazione filtro di selezione sul debitore dell'accertamento
  *		PreCondition:
  *			La scadenza dell'accertamento ha un debitore diverso da quello della documento generico e non è di tipo "diversi"
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro di selezione sulla data di scadenza
  *		PreCondition:
  *			La scadenza dell'accertamento ha una data scadenza precedente alla data di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro importo scadenza
  *		PreCondition:
  *			La scadenza dell'accertamento ha un importo di scadenza inferiore a quella di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro sul progressivo dell'accertamento
  *		PreCondition:
  *			La scadenza dell'accertamento non ha progressivo specificato
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti. 
 */

public abstract it.cnr.jada.util.RemoteIterator cercaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_accertamentiVBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      viene modificato il cambio.
  *    PostCondition:
  *      Permessa la modifica del cambio.
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk cercaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di obbligazioni congruenti con la documento generico che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle obbligazioni per le documenti generici
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle scadenze delle obbligazioni.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Obbligazione definitiva
  *		PreCondition:
  *			La scadenza non appartiene ad un'obbligazione definitiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni non cancellate
  *		PreCondition:
  *			La scadenza appartiene ad un'obbligazione cancellata
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni associate ad altri documenti amministrativi
  *		PreCondition:
  *			La scadenza appartiene ad un'obbligazione associata ad altri documenti amministrativi
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni della stessa UO
  *		PreCondition:
  *			La scadenza dell'obbligazione non appartiene alla stessa UO di generazione documento generico
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitatazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della documento generico
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Disabilitazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della documento generico e non è di tipo "diversi"
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro di selezione sulla data di scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione ha una data scadenza precedente alla data di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro importo scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un importo di scadenza inferiore a quella di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro sul progressivo dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione non ha progressivo specificato
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inserisce i dati relativi al terzo
  *    PreCondition:
  *      Viene creato o modificato un terzo.
  *    PostCondition:
  *      vengono trasmessi i dati relativi al terzo.
  *  Effettua la ricontabilizzazione in Coge
  *    PreCondition:
  *      Viene creato o modificato un terzo in un documento contabilizzato in COGE.
  *    PostCondition:
  *      viene impostato il parametro per la ricontabilizzazione COGE
 */

public abstract it.cnr.jada.bulk.OggettoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param2,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *  Contabilizzazione dei dettagli
  *    PreCondition:
  *      E' stata richiesta la contabilizzazione dei dettagli di un generico attivo
  *    PostCondition:
  *      viene impostato il nuovo stato COFI della riga  
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Contabilizzazione dei dettagli
  *    PreCondition:
  *      E' stata richiesta la contabilizzazione dei dettagli di un generico passivo
  *    PostCondition:
  *      viene impostato il nuovo stato COFI della riga  
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Controllo della quadratura
  *    PreCondition:
  *      viene contabilizzata una riga/accertamento o modificato un'importo e
  *      la quadratura non è superata
  *    PostCondition:
  *      Invia un messaggio all'utente di coperto/scoperto
 */

public abstract void controllaQuadraturaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Controllo della quadratura
  *    PreCondition:
  *      viene contabilizzata una riga/obbligazione o modificato un'importo e
  *      la quadratura non è superata
  *    PostCondition:
  *      Invia un messaggio all'utente di coperto/scoperto
 */

public abstract void controllaQuadraturaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Creazione di un nuovo documento
  *	 Validazioni superate
  *    PreCondition:
  *      Viene richiesto il salvataggio di un nuovo documento
  *    PostCondition:
  *      Salva.
  *  Validazioni non superate
  *    PreCondition:
  *      Viene richiesto il salvataggio di un nuovo documento ma le validazioni
  *      non vengono superate
  *    PostCondition:
  *      Informa l'utente della causa per la quale non è possibile salvare
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Eliminazione di un documento
  *	 Elimina
  *    PreCondition:
  *      Il documento è eliminabile
  *    PostCondition:
  *      richiama la funzione deletePhisically.
  *  Annulla
  *    PreCondition:
  *      Il documento è annullabile
  *    PostCondition:
  *      richiama la funzione deleteLogically.
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
public Documento_genericoBulk eliminaLetteraPagamentoEstero(
	UserContext param0,
	Documento_genericoBulk param1)
	throws ComponentException;
/** 
  *  Controlla se una riga del documento è eliminabile
  *  lo stato del documento è PAGATO
  *    PreCondition:
  *      Richiesta di eliminare una riga
  *    PostCondition:
  *      Avverte l'utente che non è possibile eliminare dei dettagli in un documento già pagato
 */

public abstract void eliminaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	  gestione dei dati relativi al terzo cessionario
  *		PreCondition:
  * 		Richiesta dell'anagrafico del cessionario
  *   	PostCondition:
  *  		Restituisce l'anagrafico
 */

public abstract it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Richiesta di un elenco di banche
  *    PreCondition:
  *      Richiesta di un elenco di banche per la riga
  *    PostCondition:
  *      Viene restituita la lista delle banche del fornitore/cliente.
 */

public abstract java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
 */

public java.util.Collection findTipi_doc_for_search(UserContext aUC, Documento_genericoBulk doc)
    throws	ComponentException, 
    		it.cnr.jada.persistency.PersistencyException,
    		it.cnr.jada.persistency.IntrospectionException;
/** 
  *	Restituisce il terzo per la gestione della spesa
  *		PreCondition:
  * 		Richiesta del terzo per l'utilizzo dei documenti per la spesa
  *   	PostCondition:
  *  		Restituisce il terzo di default dalla configurazione CNR
 */

public abstract it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzoDefault(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di modifica.
  *    PostCondition:
  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una operazione di modifica.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  non utilizzato
 */

public abstract void inserisciRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Richiesta di salvare le midifiche apportate sul documento
  *    PreCondition:
  *      Richiesta di salvare le midifiche apportate sul documento
  *    PostCondition:
  *      Viene richiamato il metodo modificaConBulk(aUC, bulk, null)
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Richiesta di salvare le modifiche sul documento generico
  *	 validazione superata
  *    PreCondition:
  *      effettua il controllo di validazione del metodo validaDocumento
  *    PostCondition:
  *		 Vengono aggiornate le obbligazioni/accertamenti, controllati gli elementi disassociati,
  *      la lettera di pagamento e infine salvato il documento
  *	 validazione non superata
  *    PreCondition:
  *      effettua il controllo di validazione del metodo validaDocumento
  *    PostCondition:
  *      Viene restituito un messaggio di errore
  *  Documento contabilizzato in COAN
  *    PreCondition:
  *      validazione superata
  *    PostCondition:
  *      documento viene impostato come da ricontabilizzare
  *  Documento contabilizzato in COGE
  *    PreCondition:
  *      validazione superata
  *    PostCondition:
  *      documento viene impostato come da ricontabilizzare  
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
public it.cnr.contab.anagraf00.core.bulk.BancaBulk setContoEnteIn(
	it.cnr.jada.UserContext param0, 
	Documento_generico_rigaBulk param1, 
	java.util.List param2)
 	throws ComponentException;
/** 
  * Vengono richiesti i dati relativi all'ente
  *    PreCondition:
  *      vengono richiesti i dati relativi all'ente
  *    PostCondition:
  *		 vengono impostati i dati relativi alla UO e CDS dell'ente
*/

public abstract it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk setEnte(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  * gestisce la scrittura dei dati delle rige del docuemnto all'atto di eliminazione logica
  *    PreCondition:
  *      viene eliminato logicamente un documento
  *    PostCondition:
  *		 vengono resi persistenti le modifiche effettuate dal metodo deleteLogically
*/

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  *    PreCondition:
  *      Richiesto l'aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  *    PostCondition:
  *      Il dettaglio viene aggiornato
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/**  Validazione dell'intero documento amministrativo ativo/passivo
  *  tutti i controlli superati
  *    PreCondition:
  *      Nessuna situazione di errore di validazione è stata rilevata.
  *    PostCondition:
  *      Consentita la registrazione.
  *  validazione numero di dettagli maggiore di zero.
  *    PreCondition:
  *      Il numero di dettagli nel documento è zero
  *    PostCondition:
  *      Viene inviato un messaggio: "Attenzione non possono esistere documenti senza almeno un dettaglio".
  *  validazione associazione scadenze
  *    PreCondition:
  *      Esistono dettagli non collegati ad obbligazione.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione esistono dettagli non collegati ad obbligazione."
  *  validazione modifica documento pagato.
  *    PreCondition:
  *      E' satata eseguita una modifica in documento con testata in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si modificare nulla in un documento pagato".
 */

public abstract void validaDocumento(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**  Validazione di una sigola riga del documento
  *  tutti i controli superati
  *    PreCondition:
  *      Viene richiesta la validazione per salvataggio
  *    PostCondition:
  *      Viene consentita la registrazione riga.
  *  validazione modifica dettaglio pagato.
  *    PreCondition:
  *      Le date di competenza non sono esatte
  *    PostCondition:
  *      Viene inviato un messaggio:"La data di inizio competenza non può essere successiva alla fine competenza.
  *  validazione modifica  campi di dettaglio di un documento pagato.
  *    PreCondition:
  *      Non sono state inseririte le modalità di pagamento per la riga
  *    PostCondition:
  *      Viene inviato un messaggio "Inserire le modalità di pagamento per la riga xxx"
 */

public abstract void validaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
}
