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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import java.util.Vector;
import it.cnr.contab.docamm00.docs.bulk.*;
import java.util.BitSet;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public interface IFatturaAttivaSingolaMgr extends IDocumentoAmministrativoMgr
{
/** 
  *  Addebita i dettagli selezionati.
  *    PreCondition:
  *      In nota di debito viene richiesta la contabilizzazione dei dettagli selezionati.
  *    PostCondition:
  *      Vegono addebitati nella nota di debito passata i "dettagliDaInventariare" sull'obbligazione/accertamento selezionato/creato.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk addebitaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1,java.util.List param2,java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *	non utilizzato
*/

public abstract void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,java.lang.String param3,java.lang.Integer param4,java.lang.Long param5,java.lang.String param6) throws it.cnr.jada.comp.ComponentException;
public void annullaSelezionePerStampa(
	UserContext userContext,
	Fattura_attivaBulk fatturaAttiva)
	throws ComponentException;
/** 
  *  Calcola i consuntivi del documento amministrativo
  *    PreCondition:
  *      viene richiesto il consuntivo
  *    PostCondition:
  *      Vegono restituiti i valori calcolati.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException;
public Long callGetPgPerProtocolloIVA(
	UserContext userContext)
	throws  it.cnr.jada.comp.ComponentException;
public Long callGetPgPerStampa(
	UserContext userContext)
	throws  it.cnr.jada.comp.ComponentException;
public void cancellaDatiPerProtocollazioneIva(
	UserContext param0,
	Fattura_attivaBulk param1,
	Long param2) 
	throws ComponentException;
public void cancellaDatiPerStampaIva(
	UserContext param0,
	Fattura_attivaBulk param1,
	Long param2) 
	throws ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di Accertamenti congruenti con la fattura che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle Accertamenti per le fatture 
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle scadenze delle Accertamenti.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Accertamento definitiva
  *		PreCondition:
  *			La scadenza non appartiene ad un'Accertamento definitiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Accertamenti non cancellate
  *		PreCondition:
  *			La scadenza appartiene ad un'Accertamento cancellata
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Accertamenti associate ad altri documenti amministrativi
  *		PreCondition:
  *			La scadenza appartiene ad un'Accertamento associata ad altri documenti amministrativi
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Accertamenti della stessa UO
  *		PreCondition:
  *			La scadenza dell'Accertamento non appartiene alla stessa UO di generazione fattura 
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitatazione filtro di selezione sul debitore dell'Accertamento
  *		PreCondition:
  *			La scadenza dell'Accertamento ha un debitore diverso da quello della fattura 
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Disabilitazione filtro di selezione sul debitore dell'Accertamento
  *		PreCondition:
  *			La scadenza dell'Accertamento ha un debitore diverso da quello della fattura  e non è di tipo "diversi"
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro di selezione sulla data di scadenza
  *		PreCondition:
  *			La scadenza dell'Accertamento ha una data scadenza precedente alla data di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro importo scadenza
  *		PreCondition:
  *			La scadenza dell'Accertamento ha un importo di scadenza inferiore a quella di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro sul progressivo dell'Accertamento
  *		PreCondition:
  *			La scadenza dell'Accertamento non ha progressivo specificato
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Associazione di una scadenza a titolo capitolo dei beni servizio inventariabili da contabilizzare
  *		PreCondition:
  *			L'Accertamento non ha titolo capitolo dei beni servizio inventariabili da contabilizzare
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_accertamentiVBulk param1) throws it.cnr.jada.comp.ComponentException;
/**  
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk cercaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  dettagli della fattura selezionata per l'inserimento di dettagli nelle note di credito.
  *    PreCondition:
  *      Viene richiesta la lista dei dettagli della fattura per l'inserimento di dettagli nelle note di credito.
  *    PostCondition:
  *      Viene restituita la lista dei dettagli della fattura selezionata per l'inserimento di dettagli nelle note di credito.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  dettagli della fattura selezionata per l'inserimento di dettagli nelle note di debito.
  *    PreCondition:
  *      Viene richiesta la lista dei dettagli della fattura per l'inserimento di dettagli nelle note di debito.
  *    PostCondition:
  *      Viene restituita la lista dei dettagli della fattura selezionata per l'inserimento di dettagli nelle note di debito.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle fatture congruenti con la nota di credito che si sta creando/modificando.
  *   	PostCondition:
  *  		La fattura viene aggiunta alla lista delle fatture congruenti.
  *	Validazione lista delle fatture per le note di credito
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle fatture.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fornitore nota di credito = fornitore fattura 
  *		PreCondition:
  *			Il fornitore della fattura non è lo stesso di quello della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	CDS di appartenenza
  *		PreCondition:
  *			La fattura non appartiene al CDS di creazione della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Esercizio di appartenenza
  *		PreCondition:
  *			L'esercizio della fattura non è lo stesso di quello della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Unità organizzativa di appartenenza
  *		PreCondition:
  *			La UO della fattura non è la stessa di quella della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle fatture congruenti con la nota di debito che si sta creando/modificando.
  *   	PostCondition:
  *  		La fattura viene aggiunta alla lista delle fatture congruenti.
  *	Validazione lista delle fatture per le note di debito
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle fatture.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fornitore nota di debito = fornitore fattura 
  *		PreCondition:
  *			Il fornitore della fattura non è lo stesso di quello della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	CDS di appartenenza
  *		PreCondition:
  *			La fattura non appartiene al CDS di creazione della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Esercizio di appartenenza
  *		PreCondition:
  *			L'esercizio della fattura non è lo stesso di quello della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Unità organizzativa di appartenenza
  *		PreCondition:
  *			La UO della fattura non è la stessa di quella della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Fornisce i dati aggiuntivi del cliente selezionato
  *		PreCondition:
  * 		Viene selezionato o cambiato il cliente
  *   	PostCondition:
  *  		Fornisce i dati aggiuntivi del cliente selezionato
*/

public abstract it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk completaCliente(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Fornisce i dati aggiuntivi del cliente selezionato
  *		PreCondition:
  * 		Viene selezionato o cambiato il cliente
  *   	PostCondition:
  *  		Fornisce i dati aggiuntivi del cliente selezionato
*/

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Validazione riga.
  *    PreCondition:
  *      E' stata richiesta la contabilizzazione dei dettagli di fattura selezionati ma almeno un dettaglio
  *      non supera i controlli del metodo 'validaRiga'.
  *    PostCondition:
  *      Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *    PreCondition:
  *	E' stata richiesta la contabilizzazione dei dettagli di fattura selezionati. Ogni dettaglio
  *	supera i controlli del metodo 'validaRiga'.
  *    PostCondition:
  *      Consente il passaggio alla riga seguente.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Quadratura delle scadenze Accertamenti di fattura non estera o estera senza lettera di pagamento.
  *		PreCondition:
  * 		La somma algebrica dei dettagli, storni e addebiti (metodo 'calcolaTotaleAccertamentoPer') insistenti sull'elenco di dettagli associati
  *			alla scadenza Accertamento è uguale all'importo della scadenza Accertamento stessa
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Controlli non superati.
  *		PreCondition:
  * 		Non vengono superate tutte le validazioni
  *   	PostCondition:
  *  		messaggio di errore.
 */

public abstract void controllaQuadraturaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della fattura.
  *  validazione eliminazione fattura.
  *    PreCondition:
  *      E' stata eliminata una fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può eliminare una fattura in stato IVA B o C"
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  valida eliminazione dettaglio
  *    PreCondition:
  *      E' stato eliminato un dettaglio in  in una fattura in stato IVA B o C.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio in una fattura in stato IVA B o C".
  *  tutti i controlli superati
  *    PreCondition:
  *      Nessun errore è stato rilevato.
  *    PostCondition:
  *      Viene dato il consenso per l'eliminazione della riga.
  *      
  *  valida eliminazione dettaglio pagato.
  *    PreCondition:
  *      E' stato eliminato un dettaglio in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio pagato".
  *  eliminazione dettaglio in fattura pagata.
  *    PreCondition:
  *      E' stato eliminato un dettaglio in una fattura con testata in stato P
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio in una fattura pagata".
 */

public abstract void eliminaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
public boolean esistonoDatiPerProtocollazioneIva(
	UserContext userContext,
	Long pgProtocollazioneIva)
	throws ComponentException, PersistencyException;
public boolean esistonoDatiPerStampaIva(
	UserContext userContext,
	Long pgStampa)
	throws ComponentException, PersistencyException;
/** 
  *  sezionale non valido.
  *    PreCondition:
  *      E' stata selezionato sezionale non valido.
  *    PostCondition:
  *      Viene inviato il messaggio: "Il tipo di sezionale non è valido".
  *  tutti i controlli superati.
  *    PreCondition:
  *      E' stata selezionato sezionale valido.
  *    PostCondition:
  *      Viene ritornato il vettore dei sezionali corrispondenti.
  *      
 */

public abstract java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  sezionale non valido.
  *    PreCondition:
  *      E' stata selezionato sezionale non valido.
  *    PostCondition:
  *      Viene inviato il messaggio: "Il tipo di sezionale non è valido".
  *  tutti i controlli superati.
  *    PreCondition:
  *      E' stata selezionato sezionale valido.
  *    PostCondition:
  *      Viene ritornato il vettore dei sezionali corrispondenti.
  *      
 */

public abstract java.util.Vector estraeSezionaliPerRistampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, Vector param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	ricerca degli accertamenti per le fatture attive
  *		PreCondition:
  * 		Richiesto un accertamento
  *   	PostCondition:
  *  		Restituisce la collezione di accertamenti
 */

public abstract it.cnr.jada.util.RemoteIterator findAccertamentiFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	lista dei dettagli.
  *		PreCondition:
  * 		Richiesta di caricamento dettagli di una fattura, nota di credito, nota di debito
  *   	PostCondition:
  *  		Restituisce la lista dei dettagli
 */

public abstract java.util.List findDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *	ricerca delle banche.
  *		PreCondition:
  * 		Richiesta ricerca delle banche
  *   	PostCondition:
  *  		Restituisce la collezione di banche
 */

public abstract java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/** 
  *	ricerca delle banche per la uo
  *		PreCondition:
  * 		Richiesta ricerca delle banche
  *   	PostCondition:
  *  		Restituisce la collezione di banche
 */

public abstract java.util.Vector findListabancheuo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,PersistencyException;
/** 
  *	ricerca delle note di credito.
  *		PreCondition:
  * 			Richiesta ricerca delle note di credito generate dalla fattura in argomento
  *   		PostCondition:
  *  			Restituisce la lista delle ricorrenze
 */

public abstract it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	ricerca delle note di debito.
  *		PreCondition:
  * 			Richiesta ricerca delle note di debito generate dalla fattura in argomento
  *   		PostCondition:
  *  			Restituisce la lista delle ricorrenze
 */

public abstract it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Ricerca dei tariffari
  *    	PreCondition:
  *     	Un nuovo tariffario è stato selezionato dall'utente.
  *    	PostCondition:
  *    		Restituisce la collezione dei tariffari validi per la fattura 
 */

public abstract it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk findTariffario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *	Dettagli di fattura non inventariati
  *		PreCondition:
  * 		Richiesta dell'esistenza di dettagli non inventariati
  *   	PostCondition:
  *  		Restituisce la conferma
 */

public abstract boolean hasFatturaAttivaARowNotInventoried(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
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
  *      L'OggettoBulk viene caricato con tutti gli oggetti collegati.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di ricerca.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per l'operazione inserimento criteri di ricerca.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di ricerca libera.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per l'utilizzo come prototipo in in una operazione di ricerca libera
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
public void inizializzaSelezionePerStampa(
	UserContext userContext,
	Fattura_attivaBulk fatturaAttiva)
	throws ComponentException;
/** 
  *  Validazione riga.
  *    PreCondition:
  *      validaRiga non superata.
  *    PostCondition:
  *      Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *    PreCondition:
  *      validaRiga superato
  *    PostCondition:
  *      Consente il passaggio alla riga seguente.
 */

public abstract void inserisciRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *    PreCondition:
  *      validaFattura non superata.
  *    PostCondition:
  *      Non  viene consentita la registrazione della fattura.
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      Viene consentito il salvataggio del documento.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *    PreCondition:
  *      validaFattura non superata.
  *    PostCondition:
  *      Non  viene consentita la registrazione della fattura.
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      Viene consentito il salvataggio del documento.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
public Integer modificaSelezionePerStampa(
	UserContext userContext,
	Fattura_attivaBulk fatturaAttiva, 
	OggettoBulk[] fatture,
	BitSet old_ass,
	BitSet ass,
	Long pgProtocollazione,
	Integer offSet,
	Long pgStampa,
	java.sql.Timestamp dataStampa)
	throws ComponentException;
/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */

public abstract void protocolla(it.cnr.jada.UserContext param0,java.sql.Timestamp param1, Long param2) throws it.cnr.jada.comp.ComponentException;
public Filtro_ricerca_doc_amm_protocollabileVBulk selezionaTuttiPerStampa(
	UserContext param0,
	Filtro_ricerca_doc_amm_protocollabileVBulk param1) 
	throws ComponentException;
public Filtro_ricerca_doc_amm_ristampabileVBulk selezionaTuttiPerStampa(
	UserContext param0,
	Filtro_ricerca_doc_amm_ristampabileVBulk param1) 
	throws ComponentException;
public Fattura_attivaBulk setContoEnteIn(
	it.cnr.jada.UserContext param0, 
	Fattura_attivaBulk param1, 
	java.util.List param2)
 	throws ComponentException;
/** 
  *  Storna i dettagli selezionati.
  *    PreCondition:
  *      In nota di credito viene richiesta la contabilizzazione dei dettagli selezionati.
  *    PostCondition:
  *      Vengono stornati nella nota di credito passata i "dettagliDaStornare" sull'obbligazione/accertamento selezionato/creato.
  */

public abstract it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk stornaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1,java.util.List param2,java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Aggiornamento di un dettaglio di documento amministrativo
  *    PreCondition:
  *      Richiesto l'aggiornamento di un dettaglio di documento amministrativo
  *    PostCondition:
  *      Il dettaglio viene aggiornato
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
/** 
  *  validazione modifica  esercizio e tipo fattura (G1)
  *    PreCondition:
  *      Sono stati modificati campi relativi all' esercizio, stato iva  e tipo fattura.
  *      
  *    PostCondition:
  *      Viene inviato un messaggio "Attenzione: Sono stati modificati campi relativi all' esercizio, stato iva  e tipo fattura. Non è possibile validare le modifiche apportate".
  *  tutti i controlli superati
  *    PreCondition:
  *      Nessuna situazione di errore di validazione è stata rilevata.
  *    PostCondition:
  *      Consentita la registrazione.
  *  validazione numero di dettagli maggiore di zero.
  *    PreCondition:
  *      Il numero di dettagli nella fattura è zero
  *    PostCondition:
  *      Viene inviato un messaggio: "Attenzione non possono esistere fatture senza almeno un dettaglio".
  *  validazione aggiunta dettagli n fatture con stato iva B o C.
  *    PreCondition:
  *      E' stato aggiunto un dettaglio in  fatture con stato iva B o C .
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono aggiungere dettagli in fatture con stato iva B o C ."
  *  validazione modifica sezionale
  *    PreCondition:
  *      E' stato modificato un sezionale in fatture con dettagli in stato not I e stato iva B o C.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono eliminare  dettagli in  fatture parzialmente contabilizzate e stato iva B o C"
  *  validazione modifica testata (G3)
  *    PreCondition:
  *      Sono stati modificati i campi  data fattura di emissione , importo, flag IntraUE, flag San Marino, sezionale in fatture con stato iva B o C .
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono modificare questi campi in fatture pagate o in stato IVA B o C"
  *  validazione modifica testata campo terzo.(G5)
  *    PreCondition:
  *      E stato modificato il campo terzo nella testata in stato (B or C) or (A and testata=P).
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione lo stato della fattura non consente di modificare il cliente".
  *  validazione modifica fattura pagata.
  *    PreCondition:
  *      E' satata eseguita una modifica in fattura con testata in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può modificare nulla in una fattura pagata".
  *  validazione associazione scadenze
  *    PreCondition:
  *      Esistono dettagli non collegati ad accertamento.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione esistono dettagli non collegati ad accertamento."
 */

public abstract void validaFattura(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  validazione bene.
  *    PreCondition:
  *      Il bene  relativo alla riga fattura in via di variazione risulta di tipo soggetto ad inventario.
  *    PostCondition:
  *      Viene inviato un messaggio all'utente "Questo bene è soggetto ad inventario".
  *      NOTA: Vanno dettagliate le condizioni di inventario non appena disponibile il relativo use case
  *  tutti i controli superati
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentita la registrazione riga.
  *  validazione modifica imponibile, iva, totale, aliquota (G2)
  *    PreCondition:
  *      Sono stati modificati i campi  imponibile, iva, totale, aliquiota (G2) in fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio "Attenzione:  questa modifica non è permessa"
  *  validazione modifica dettaglio pagato.
  *    PreCondition:
  *      E' stato modificato un dettaglio in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può eliminare un dettaglio già pagato".
  *  validazione modifica dettaglio di fattura già pagata.
  *    PreCondition:
  *      E' stato modificato un dettaglio di fattura in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può modificare un dettaglio in una fattura già pagata".
 */

public abstract void validaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Verifica l'esistenza e apertura dell'inventario
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene consentita l'attività richiesta
  *  L'inventario non esiste
  *    PreCondition:
  *      L'inventario per CDS e UO correnti non esiste
  *    PostCondition:
  *      Viene visualizzato messaggio "non esiste un inventario per questo CDS"
  *  L'inventario non è aperto
  *    PreCondition:
  *      L'inventario per CDS e UO correnti esiste ma non è aperto
  *    PostCondition:
  *      Viene visualizzato messaggio "l'inventario per questo CDS non è aperto"
 */

public abstract void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException;
}
