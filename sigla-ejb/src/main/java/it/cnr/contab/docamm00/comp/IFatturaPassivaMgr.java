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

import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import java.util.Vector;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public interface IFatturaPassivaMgr extends IDocumentoAmministrativoMgr {

/** 
  *  Addebita i dettagli selezionati.
  *    PreCondition:
  *      In nota di debito viene richiesta la contabilizzazione dei dettagli selezionati.
  *    PostCondition:
  *      Vegono addebitati nella nota di debito passata i "dettagliDaInventariare" sull'obbligazione/accertamento selezionato/creato.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk addebitaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1,java.util.List param2,java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException;
/** 
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
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rivelata.
  *    PostCondition:
  *      Permesso il ritorno dell cambio nella fattura.
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk cercaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli  superati.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista dei dettagli della fattura passiva selezionata per l'inserimento di dettagli nelle note di debito.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli  superati.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista dei dettagli della fattura passiva selezionata per l'inserimento di dettagli
  *		 nelle note di debito.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle fatture passive congruenti con la nota di credito che si sta creando/modificando.
  *   	PostCondition:
  *  		La fattura viene aggiunta alla lista delle fatture congruenti.
  *	Validazione lista delle fatture passive per le note di credito
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle fatture passive.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fornitore nota di credito = fornitore fattura passiva
  *		PreCondition:
  *			Il fornitore della fattura passiva non è lo stesso di quello della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	CDS di appartenenza
  *		PreCondition:
  *			La fattura non appartiene al CDS di creazione della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Esercizio di appartenenza
  *		PreCondition:
  *			L'esercizio della fattura passiva non è lo stesso di quello della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Unità organizzativa di appartenenza
  *		PreCondition:
  *			La UO della fattura passiva non è la stessa di quella della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle fatture passive congruenti con la nota di debito che si sta creando/modificando.
  *   	PostCondition:
  *  		La fattura viene aggiunta alla lista delle fatture congruenti.
  *	Validazione lista delle fatture passive per le note di debito
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle fatture passive.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fornitore nota di debito = fornitore fattura passiva
  *		PreCondition:
  *			Il fornitore della fattura passiva non è lo stesso di quello della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	CDS di appartenenza
  *		PreCondition:
  *			La fattura non appartiene al CDS di creazione della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Esercizio di appartenenza
  *		PreCondition:
  *			L'esercizio della fattura passiva non è lo stesso di quello della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Unità organizzativa di appartenenza
  *		PreCondition:
  *			La UO della fattura passiva non è la stessa di quella della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di obbligazioni congruenti con la fattura passiva che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle obbligazioni per le fatture passive
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
  *			La scadenza dell'obbligazione non appartiene alla stessa UO di generazione fattura passiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitatazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della fattura passiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Disabilitazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della fattura passiva e non è di tipo "diversi"
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
  *	Associazione di una scadenza a titolo capitolo dei beni servizio inventariabili da contabilizzare
  *		PreCondition:
  *			L'obbligazione non ha titolo capitolo dei beni servizio inventariabili da contabilizzare
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Sono state inserite nella nota di credito degli accertamenti su cui stornare i dettagli
  *   	PostCondition:
  *  		Ricerca la lista delle tipologie e modalità di pagamento per l'ente.
  *	Validazione dell'ente
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle tipologie e modalità di pagamento per l'ente.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk completaEnte(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale.
  *    PreCondition:
  *      Vengono richiesti i dati relativi al fornitore della fattura passiva
  *    PostCondition:
  *      vengono trasmessi i dati relativi al fornitore.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk completaFornitore(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Validazione riga.
  *    PreCondition:
  *      E' stata richiesta la contabilizzazione dei dettagli di fattura passiva selezionati ma almeno un dettaglio
  *      non supera i controlli del metodo 'validaRiga'.
  *    PostCondition:
  *      Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *    PreCondition:
  *	E' stata richiesta la contabilizzazione dei dettagli di fattura passiva selezionati. Ogni dettaglio
  *	supera i controlli del metodo 'validaRiga'.
  *    PostCondition:
  *      Consente il passaggio alla riga seguente.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Non è stato rilevato nessun errore.
  *    PostCondition:
  *      Nessun messaggio.
  *  Valida quadratura IVA
  *    PreCondition:
  *      l totale imponibile +IVA  di esteso a tutte le righe non quadra con il totale imponibile + IVA della fattura.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione, la somma degli imponibili delle righe + la somma della relativa iva non quadra con il totale fattura".
  *      
 */

public abstract void controllaQuadraturaConti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Quadratura delle scadenze obbligazioni di fattura passiva non estera o estera senza lettera di pagamento.
  *		PreCondition:
  * 		La somma algebrica dei dettagli, storni e addebiti (metodo 'calcolaTotaleObbligazionePer') insistenti sull'elenco di dettagli associati
  *			alla scadenza obbligazione è uguale all'importo della scadenza obbligazione stessa
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Quadratura delle scadenze obbligazioni di fattura passiva estera con lettera di pagamento.
  *		PreCondition:
  * 		L'importo della lettera di pagamento è uguale all'importo della scadenza obbligazione
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Controlli non superati.
  *		PreCondition:
  * 		Non vengono superate tutte le validazioni
  *   	PostCondition:
  *  		Emette errore con messaggio:"Quadratura non superata".
 */

public abstract void controllaQuadraturaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
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
public Fattura_passivaBulk eliminaLetteraPagamentoEstero(
	UserContext param0,
	Fattura_passivaBulk param1)
	throws ComponentException;
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

public abstract void eliminaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  fattura istituzionale
  *    PreCondition:
  *      La fattura è di tipo istituzionale.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali corrispondente al tipo  sezionale istituzionale.
  *  fattura di tipo commerciale o promiscua 
  *    PreCondition:
  *      E' stato selezionata una fattura di tipo commerciale o promiscua.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali corrispondenti al tipo commerciale.
  *  fattura di tipo intra UE
  *    PreCondition:
  *      E' stata selezionata una fattura di tipo intra UE.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali intra UE.
  *  fattura relativa alla repubblica di S.Marino
  *    PreCondition:
  *      E' stata selezionata una fattura relativa alla Repubblica di S.Marino.
  *    PostCondition:
  *      Sono stati estratti i vettori dei sezionali corrispondenti alla repubblica di s.Marino.
  *  fattura di tipo non valido
  *    PreCondition:
  *      E' stata selezionata una fattura di tipo non valido.
  *    PostCondition:
  *      Viene inviato il messaggio: "Il tipo di fattura selezionato non è valido".
 */

public abstract java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta dell'anagrafico del cessionario
  *   	PostCondition:
  *  		Restituisce l'anagrafico
 */

public abstract it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
  *   	PostCondition:
  *  		Restituisce la lista dei dettagli
 */

public abstract java.util.List findDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche del fornitore.
 */

public abstract java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche dell'ente.
 */

public abstract java.util.Collection findListabancheuo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/** 
  *	Normale.
  *		PreCondition:
  * 			Richiesta ricerca delle note di credito generate dalla fattura passiva in argomento
  *   		PostCondition:
  *  			Restituisce la lista delle ricorrenze
 */

public abstract it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 			Richiesta ricerca delle note di debito generate dalla fattura passiva in argomento
  *   		PostCondition:
  *  			Restituisce la lista delle ricorrenze
 */

public abstract it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Ricerca obbligazioni disponibili per storno o addebito di dettagli di fattura passiva pagata.
  *    	PreCondition:
  *     	Nessuna condizione di errore rilevata.
  *    	PostCondition:
  *    		Restituisce l'elenco dei dettagli di fatture passive da cui ricavare le scadenze di obbligazione
  *			da utilizzare.
  *  Dettagli non pagati.
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca è già stato pagato
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  Dettagli di fatture dello stesso fornitore.
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non è di fattura passiva dello stesso fornitore
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  Tipo di fattura
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non è di tipo fattura passiva
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  CDS
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non è del CDS di appartenenza
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  UO
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non è della UO di appartenenza
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  Importo scadenza
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non è collegato ad una scadenza con importo
  *			maggiore o uguale all'importo passato in argomento
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
 */

public abstract it.cnr.jada.util.RemoteIterator findObbligazioniFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1,java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta dell'anagrafico ente per Esercizio e Unità organizzativa di scrivania
  *   	PostCondition:
  *  		Restituisce l'anagrafico
  *	Esercizio dell'anagrafico Ente
  *		PreCondition:
  *			L'anagrafico trovato non appartiene all'esercizio corrente
  * 	PostCondition:
  *  		Viene inviato il messaggio "Non e' stato definito in anagrafico il terzo per l'ente".
  *	Unità Organizzativa dell'anagrafico Ente
  *		PreCondition:
  *			L'anagrafico trovato non appartiene alla UO dell'esercizio corrente
  * 	PostCondition:
  *  		Viene inviato il messaggio "Non e' stato definito una unità organizzativa per l'Ente.".
 */

public abstract it.cnr.contab.anagraf00.core.bulk.TerzoBulk findTerzoUO(it.cnr.jada.UserContext param0,java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta della UO ente per l'esercizio corrente
  *   	PostCondition:
  *  		Restituisce la UO
  *	Unità Organizzativa
  *		PreCondition:
  *			La UO trovata non appartiene all'esercizio corrente
  * 		PostCondition:
  *  			Viene inviato il messaggio "Non e' stato definito una unità organizzativa per l'Ente.".
 */

public abstract it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk findUOEnte(it.cnr.jada.UserContext param0,java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Dettagli di fattura passiva non inventariati
  *		PreCondition:
  * 		Richiesta dell'esistenza di dettagli FP non inventariati
  *   	PostCondition:
  *  		Restituisce la conferma
 */

public abstract boolean hasFatturaPassivaARowNotInventoried(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Validazione riga.
  *		PreCondition:
  *    		Viene richiesto l'inserimento di un dettaglio in documento passivo e il dettaglio non ha superato il metodo 'validaRiga'
  *		PostCondition:
  *			Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *		PreCondition:
  *			Viene richiesto l'inserimento di un dettaglio in documento passivo e il dettaglio ha superato il metodo 'validaRiga'
  *		PostCondition:
  *			Inserisce il dettaglio e consente il passaggio alla riga seguente.
 */

public abstract void inserisciRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
public abstract boolean isBeneServizioPerSconto(UserContext param0,	Fattura_passiva_rigaBulk param1) throws ComponentException;
/** 
  *  Non passa validazione di business
  *    PreCondition:
  *      L'OggettoBulk non passa i criteri di validità di business per l'operazione di modifica
  *    PostCondition:
  *      Viene generata una ComponentException con detail la ValidationException che descrive l'errore di validazione.
  *  Oggetto non trovato
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore
  *  Oggetto scaduto
  *    PreCondition:
  *      L'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore
  *  Oggetto occupato
  *    PreCondition:
  *      L'OggettoBulk specificato è bloccato da qualche altro utente.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene rimosso il dettaglio dalla lista delle associazioni con i beni dell'inventario.
 */

public abstract void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk param1,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rivelata.
  *    PostCondition:
  *      Imposta nella fattura la valuta di default del sistema.
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */

public abstract it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk selezionaValutaDiDefault(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
public Nota_di_creditoBulk setContoEnteIn(
	it.cnr.jada.UserContext param0, 
	Nota_di_creditoBulk param1, 
	java.util.List param2)
 	throws ComponentException;
/** 
  *  Storna i dettagli selezionati.
  *    PreCondition:
  *      In nota di credito viene richiesta la contabilizzazione dei dettagli selezionati.
  *    PostCondition:
  *      Vegono stornati nella nota di credito passata i "dettagliDaStornare" sull'obbligazione/accertamento selezionato/creato.
  */

public abstract it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk stornaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1,java.util.List param2,java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException;
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
  *  validazione numero fattura
  *    PreCondition:
  *      Il numero della fattura fornitore è gia presente nell'archivio fatture.
  *    PostCondition:
  *      Viene visualizzato il messaggio "Attenzione duplicazione fattura: Il numero di fattura risulta già registrato".
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
  *      Sono stati modificati i campi  numero fattura di  emissione, data fattura di emissione , importo, flag IntraUE, flag San Marino, sezionale,   
  *      valuta in fatture con stato iva B o C .
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono modificare questi campi in fatture pagate o in stato IVA B o C"
  *  validazione modifica testata campo fornitore.(G5)
  *    PreCondition:
  *      E stato modificato il campo fornitore nella testata in stato (B or C) or (A and testata=P).
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione lo stato della fattura non consente di modificare il fornitore".
  *  validazione modifica fattura pagata.
  *    PreCondition:
  *      E' satata eseguita una modifica in fattura con testata in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può modificare nulla in una fattura pagata".
  *  validazione quadratura IVA.
  *    PreCondition:
  *      Il totale imponibile +IVA  di tutte le righe non quadra con il totale fattura riporato in testata.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione, la somma degli imponibili delle righe + la somma della relativa iva non quadra con il totale  
  *	fattura".
  *  validazione associazione scadenze
  *    PreCondition:
  *      Esistono dettagli non collegati ad obbligazione.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono modificare questi campi in fatture pagate o in stato IVA B o C"
 */

public abstract void validaFattura(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  validazione bene.
  *    PreCondition:
  *      Il bene  relativo alla riga fattura in via di variazione risulta di tipo soggetto ad inventario.
  *    PostCondition:
  *      Viene inviato un messaggio all'utente "Questo bene è soggetto ad inventario".
  *  tutti i controli superati
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentita la registrazione riga.
  *  validazione modifica imponibile, iva, totale, aliquiota, tipologia (istituzionale/commerciale) (G2)
  *    PreCondition:
  *      Sono stati modificati i campi  imponibile, iva, totale, aliquiota, tipologia (istituzionale/commerciale) (G2) in fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio "Attenzione:  questa modifica non è permessa"
  *  validazione modifica/eliminazione dettaglio pagato.
  *    PreCondition:
  *      E' stata richiesta la modifica o l'eliminazione di un dettaglio pagato.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può modificare o eliminare un dettaglio già pagato".
  *  validazione modifica/eliminazione dettaglio di fattura interamente pagata.
  *    PreCondition:
  *      E' stato modificato un dettaglio di fattura con testata in in stato pagato.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può modificare un dettaglio in una fattura già pagata".
  *  Tipologia di riga fattura
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza specificare il tipo di riga (Commerciale/Istituzionale/Promiscuo)
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare una tipologia per la riga inserita".
  *  Bene/servizio di riga fattura
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza specificare il bene/servizio
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare un bene/servizio per la riga inserita".
  *  Validazione Voce IVA
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza specificare la Voce IVA
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare una Voce IVA per la riga inserita".
  *  Validazione degli importi e quantità
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura con prezzo unitario, importo iva e quantità non validi
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare un prezzo unitario, importo iva e quantità validi".
  *  Data competenza COGE
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza data "competenza da" e "competenza a"
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare la data "competenza da" e "competenza a"
  *  Validazione date competenza COGE
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura con intervallo temporale identificato dalle date "competenza da" e     	   
  *	"competenza a" non valido ("competenza da" maggiore di "competenza a")
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare date "competenza da" e "competenza a" valide."
 */

public abstract void validaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException;
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

public abstract void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException;
}
