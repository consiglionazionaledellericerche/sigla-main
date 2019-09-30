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

package it.cnr.contab.missioni00.comp;

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.missioni00.docs.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.*;

public interface IAnticipoMgr extends it.cnr.jada.comp.ICRUDMgr
{


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

public RemoteIterator cercaObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro) throws ComponentException;
/**
 * Completa i dati relativi al terzo
 *
 * Pre-post-conditions:
 *
 * Nome: Completa terzo
 * Pre:  L'utente ha selezionato un nuovo terzo per l'anticipo
 * Post: Il sistema ha valorizzato tutti i dati relativi all'anagrafico associato al terzo selezionato, in particolare
 *	     nome, cognome, ragione sociale, codice fiscale, partita iva, modalita e termini di pagamento 
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	anticipo	l'AnticipoBulk per cui e' stato selezionato un nuovo terzo
 * @param	aTerzo	il terzo di tipo V_terzo_per_compensoBulk selezioanto dall'utente
 *
 * @return l'AnticipoBulk coi dati relativi al terzo inizializzati
 */

public abstract it.cnr.contab.missioni00.docs.bulk.AnticipoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Crea l'anticipo
 *
 * Pre-post-conditions:
 *
 * Nome: Crea anticipo con obbligazione già esistente
 * Pre:  L'utente ha completato l'inserimento dei dati relativi ad un nuovo anticipo e ne chiede il salvataggio
 *       L'utente ha selezionato una scadenza di obbligazione già esistente
 * Post: Il sistema aggiona l'obbligazione e i suoi saldi( metodo 'aggiornaObbligazione' ), imposta lo stato dell'anticipo a CONTABILIZZATO e ne salva i dati
 *
 * Nome: Crea anticipo con obbligazione nuova
 * Pre:  L'utente ha completato l'inserimento dei dati relativi ad un nuovo anticipo e ne chiede il salvataggio
 *       L'utente ha creato una nuova obbligazione nel contesto transazionalel dell'anticipo
 * Post: Il sistema rende definitiva l'obbligazione provvisoria e ne aggiona i saldi( metodo 'aggiornaObbligazione' );
 *       il sistema imposta lo stato dell'anticipo a CONTABILIZZATO e ne salva i dati 
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	l'AnticipoBulk per cui e' stato richiesta la creazione
 *
 * @return l'AnticipoBulk salvato
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Crea l'anticipo
 *
 * Pre-post-conditions:
 *
 * Nome: Validazione della scadenza di obbligazione associata all'anticipo
 * Pre:  La scadenza legata all'anticipo passa la validazione
 * Post: Il sistema prosegue con la creazione dell'anticipo
 *
 * Nome: Crea anticipo con obbligazione già esistente
 * Pre:  L'utente ha completato l'inserimento dei dati relativi ad un nuovo anticipo e ne chiede il salvataggio
 *       L'utente ha selezionato una scadenza di obbligazione già esistente
 * Post: Il sistema aggiona l'obbligazione e i suoi saldi( metodo 'aggiornaObbligazione' ), imposta lo stato dell'anticipo a CONTABILIZZATO e ne salva i dati
 *
 * Nome: Crea anticipo con obbligazione nuova
 * Pre:  L'utente ha completato l'inserimento dei dati relativi ad un nuovo anticipo e ne chiede il salvataggio
 *       L'utente ha creato una nuova obbligazione nel contesto transazionalel dell'anticipo
 * Post: Il sistema rende definitiva l'obbligazione provvisoria e ne aggiona i saldi( metodo 'aggiornaObbligazione' );
 *       il sistema imposta lo stato dell'anticipo a CONTABILIZZATO e ne salva i dati 
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk		l'AnticipoBulk per cui e' stato richiesta la creazione
 * @param	status		serve per gestire l'eccezione lanciata dall'obbligazione
 *						nel caso non ci sia disponibilita' di cassa  
 *
 * @return l'AnticipoBulk salvato
 */
 
public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Crea rimborso
 * Pre:  Una richiesta di creazione di un rimborso completo di un anticipo è stata generata
 *       L'anticipo e' in stato pagato
 *       L'utente ha selezionato la linea di attività per cui creare l'accertamento su cui contabilizzare il rimborso
 * Post: Viene chiamata la stored procedure che 
 *       - crea un accertamento nel bilancio del CNR con voce del piano uguale
 *         a quella specificata in Configurazione CNR e linea di attività uguale a quella speciifcata dall'utente
 *       - crea un rimborso contabilizzato sull'accertamento precedente 
 *
 * Nome: Crea rimborso - errore 
 * Pre:  Una richiesta di creazione di un rimborso completo di un anticipo è stata generata
 *       L'utente non ha selezionato una linea di attività per cui creare l'accertamento su cui contabilizzare il rimborso
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	anticipo l' AnticipoBulk per cui creare il rimborso
 * @return 	l'AnticipoBulk con il rimborso associato
 *
 */

public abstract it.cnr.contab.missioni00.docs.bulk.AnticipoBulk creaRimborsoCompleto(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Cancellazione anticipo
 *
 * Pre-post-conditions:
 *
 * Nome: 	cancellazione anticipo
 * Pre:  	L'utente vuole eliminare un anticipo
 * Post: 	Il sistema scollega la scadenza di obbligazione dell'anticipo azzerandone
 *			l'importo associato a documento amministrativo
 *
 * Nome: 	cancellazione fisica anticipo
 * Pre:  	L'utente vuole eliminare un anticipo non associato a mandato e non 
 *			contabilizzato in Coge/Coan
 * Post: 	Il sistema consente l'eliminazione fisica dell'anticipo
 *
 * Nome: 	cancellazione logica anticipo
 * Pre:  	L'utente vuole eliminare un anticipo o associato a mandato o 
 *			contabilizzato in Coge/Coan
 * Post: 	Il sistema consente l'eliminazione logica dell'anticipo
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk		l' AnticipoBulk da cancellare
 *
 */
public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Carica i dati relativi alle coordinate bancarie 
 *
 * Pre-post-conditions:
 *
 * Nome: Carica banche
 * Pre:  E' stata generata una richiesta di caricamento delle coordinate bancarie relative al terzo dell'anticipo
 * Post: Il sistema restituisce la lista delle coordinate bancarie relative al terzo dell'anticipo
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	anticipo l' AnticipoBulk da cui ricavare il terzo per cui selezionare le coordinate bancarie
 *
 * @return la collezione di istanze di tipo BancaBulk 
 */

public abstract java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/**
 * Cambio data data registrazione
 *
 * Pre-post-conditions:
 *
 * Nome: cambio data - ok 
 * Pre:  L'utente ha modificato la data di registrazione dell'anticipo
 *       La nuova data supera la validazione ( metodo 'validaDataRegistrazione')
 * Post: Il sistema carica l'eventuale nuovo cambio relativo alla divisa dell'anticipo nella nuova data specificata
 *
 * Nome: cambio data - errore
 * Pre:  L'utente ha modificato la data di registrazione dell'anticipo
 *       La nuova data non supera la validazione ( metodo 'validaDataRegistrazione')
 * Post: Il sistema segnala all'utente che la nuova data non ha superato la validazione
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	anticipo l' AnticipoBulk per cui gestire la nuova data di registrazioen
 *
 * @return l'AnticipoBulk con caricato il cambio relativo alla divisa per la nuova data di registrazione
*/

public abstract it.cnr.contab.missioni00.docs.bulk.AnticipoBulk gestisciCambioDataRegistrazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.sql.SQLException;
/**
  *	 Inizializzazione data registrazione e data competenza coge da/a
  *    PreCondition:
  *      Inizializzazione della data registrazione e della data competenza coge da/a
  *    PostCondition:
  *      Il sistema inizializza la data registrazione e la data competenza coge da/a
  *		 con la data di sistema
  *
  *	 Inizializzazione divisa e cambio
  *    PreCondition:
  *      Inizializzazione della divisa e del cambio dell'anticipo che si sta creando
  *    PostCondition:
  *      Il sistema inizializza la divisa di default e il relativo cambio valido alla data di registrazione
  *
  *  Esercizio non aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in uno stato diverso da APERTO
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare missioni.
  *  Esercizio aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in stato APERTO
  *    PostCondition:
  *      e' possibile procedere con la creazione dell' anticipo
  *
  * @param aUC 	lo user context 
  * @param bulk l'istanza di  <code>AnticipoBulk</code> che si sta creando
  * @return 	l'istanza di  <code>AnticipoBulk</code> 
  *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Inizializzazione per modifica
 *
 * Pre-post-conditions:
 *
 * Nome: inizializzazione
 * Pre:  L'utente ha richiesto l'inizializzzaione dei dati di un anticipo già inserito per una eventuale modifica
 * Post: Il sistema restituisce l'anticipo con impostati tutti i dati relativi al terzo (metodo 'caricaTerzoInModificaAnticipo') 
 *       e all'eventuale rimborso (metodo 'inizializzaRimborso') e missione collegata
 *
 * @param	aUC		lo UserContext che ha generato la richiesta
 * @param	bulk 	l' AnticipoBulk da inizializzare
 *
 * @return l'AnticipoBulk inizializzato
*/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesto lo stato cofi dell'anticipo
  *
  * Pre-post-conditions
  *
  *	Nome: Anticipo ANNULLATO - Stato COFI uguale ad 'A' 
  *	Pre: L'anticipo è annullato
  *	Post: Ritorna <true>. L'anticipo è annullato
  *
  *	Nome: Anticipo NON ANNULLATO - Stato COFI diverso da 'A'
  *	Pre: L'anticipo non è annullato
  *	Post: Ritorna <false>. L'anticipo non è annullato
  *
  * @param userContext 	lo UserContext che ha generato la richiesta
  * @param anticipo 	L'anticipo da controllare  
  * @return vero se l'anticipo è anullato
  *			falso altrimenti
  *
**/

public abstract boolean isAnticipoAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Modifica l'anticipo
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica anticipo 
 * Pre:  L'utente ha completato l'inserimento delle modifiche dei dati da apportare all'anticipo e ne chiede il salvataggio
 * Post: Il sistema aggiona l'obbligazione associata all'anticipo e i suoi saldi( metodo 'aggiornaObbligazione' )
 *       e salva i dati dell'anticipo
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	l'AnticipoBulk per cui e' stato richiesta la modifica
 *
 * @return l'AnticipoBulk salvato
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Modifica l'anticipo
 *
 * Pre-post-conditions:
 *
 * Nome: Validazione della scadenza di obbligazione associata all'anticipo
 * Pre:  La scadenza legata all'anticipo passa la validazione
 * Post: Il sistema prosegue con la modifica dell'anticipo
 *
 * Nome: Modifica anticipo 
 * Pre:  L'utente ha completato l'inserimento delle modifiche dei dati da apportare all'anticipo e ne chiede il salvataggio
 * Post: Il sistema aggiona l'obbligazione associata all'anticipo e i suoi saldi( metodo 'aggiornaObbligazione' )
 *       e salva i dati dell'anticipo
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk		l'AnticipoBulk per cui e' stato richiesta la modifica
 * @param	status		serve per gestire l'eccezione lanciata dall'obbligazione
 *						nel caso non ci sia disponibilita' di cassa
 *
 * @return l'AnticipoBulk salvato
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Riporta avanti il documento contabile del Rimborso
 *
*/

public abstract AnticipoBulk riportaRimborsoAvanti(UserContext userContext, AnticipoBulk anticipo) throws  ComponentException; 
/**
 * Riporta indietro il documento contabile del Rimborso
 *
*/

public abstract AnticipoBulk riportaRimborsoIndietro(UserContext userContext, AnticipoBulk anticipo)throws ComponentException ;
/**
 * Annulla le modifiche apportate all'anticipo e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: 	Rollback to savePoint
 * Pre:  	Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: 	Tutte le modifiche effettuate sull'anticipo da quando si e' impostato il savepoint vengono annullate
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public abstract void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException;
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo 
 * fino a quel momento 
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public abstract void setSavePoint(UserContext userContext, String savePointName) throws ComponentException;
/**
  * Viene richiesta la validazione dell'obbligazione associata all'anticipo
  *
  * Pre-post-conditions
  *
  * Nome: 	Scadenza non selezionata
  *	Pre: 	Non e' stata selezionata la scadenza da associare all'anticipo
  *	Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
  *		  	Generata una ApplicationException con il messaggio:	"Nessuna obbligazione associata!"
  *
  * Nome: 	Importi obbligazione/scadenza NULLI
  *	Pre: 	L'importo della obbligazione e/o della scadenza è nullo
  *	Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
  *		  	Generata una ApplicationException con il messaggio:	
  *			"L'importo dell'obbligazione/scadenza è un dato obbligatorio"
  *
  * Nome: 	Importo scadenza diverso da quello dell'anticipo
  *	Pre: 	L'importo della scadenza è diverso da quello dell' anticipo
  *	Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
  *		  	Generata una ApplicationException con il messaggio:
  *			"La scadenza di obbligazione associata ha un importo diverso da quello dell'anticipo!"
  *
  * Nome: 	Data scadenza NON valida
  *	Pre: 	La scadenza selezionata ha una data minore della data di registrazione dell'anticipo
  *	Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
  *		  	Generata una ApplicationException con il messaggio:
  *			"La data della scadenza dell'obbligazione deve essere successiva alla data di registrazione dell' anticipo!"
  *  
  * Nome: 	Terzo selezionato NON valido
  *	Pre: 	Il terzo selezionato è diverso dal terzo dell'anticipo oppure il tipo entità NON è DIVERSI
  *	Post: 	Non viene consentita l'associazione della scadenza con l'anticipo
  *		  	Generata una ApplicationException con il messaggio:
  *			"L'obbligazione deve avere un creditore valido!"
  *
  * Nome: 	Tutte le validazioni precedenti superate
  * Pre: 	L'obbligazione supera tutte le validazioni precedenti
  * Post: 	Viene validata l'associazione dell'anticipo con la scadenza di obbligazione
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	scadenza		la scadenza da validare
  *
 **/
public abstract void validaObbligazione(UserContext userContext, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza, OggettoBulk bulk) throws ComponentException;
}
