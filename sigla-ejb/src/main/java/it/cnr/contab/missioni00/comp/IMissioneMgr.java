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
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.*;

public interface IMissioneMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Cancellazione fisica del compenso
 *
 * Pre-post-conditions:
 *
 * Cancella fisicamente Compenso
 * 	Pre:  Cancellazione fisica del compenso validata tramite procedura
 * 	Post: Il sistema cancella fisicamente il compenso e inizializza alcuni
 *		  campi della missione (stato_coge, stato_coan, fl_associato_compenso)
 *
 * Condizione di errore
 * 	Pre:  Cancellazione fisica del compenso NON validata dalla procedura
 * 	Post: Il sistema non procede con la cancellazione 
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui cancellare il compenso
 *
 * @return la MissioneBulk il cui compenso e' stato cancellato
 */	

public abstract MissioneBulk cancellaCompensoPhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException;
/**
 * Cancellazione fisica Dettagli Diaria
 *
 * Pre-post-conditions:
 *
 * Nome: Cancella fisicamente Diaria
 * Pre:  L'utente ha fatto delle modifiche che comportano la cancellazione fisica
 *		 dei dettagli did iaria
 * Post: Il sistema richiede alla Component di cancellare i dettagli di diaria
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui cancellare i dettagli di diaria
 *
 */	

public abstract MissioneBulk cancellaDiariaPhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException;
/**
 * Cancellazione fisica Tappe
 *
 * Pre-post-conditions:
 *
 * Nome: Cancella fisicamente Tappe
 * Pre:  L'utente ha modificato la configurazione delle tappe della missione
 * Post: Il sistema richiede alla Component di cancellare le tappe della missione
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui cancellare le tappe
 *
 */	

public abstract MissioneBulk cancellaTappePhisically(it.cnr.jada.UserContext userContext, MissioneBulk missione) throws ComponentException, it.cnr.jada.persistency.PersistencyException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di obbligazioni congruenti con la missione che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle obbligazioni per le missioni
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
  *			La scadenza dell'obbligazione non appartiene alla stessa UO di generazione missione
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitatazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della missione
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Disabilitazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della missione e non è di tipo "diversi"
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

public abstract RemoteIterator cercaObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro) throws ComponentException ;
/**
 * Completa i dati relativi al terzo
 *
 * Pre-post-conditions:
 *
 * Nome: Completa terzo
 * Pre:  L'utente ha selezionato un nuovo terzo per la missione
 * Post: Il sistema ha valorizzato tutti i dati relativi all'anagrafico associato al terzo selezionato, in particolare
 *		 codice, nome, cognome, ragione sociale, codice fiscale, partita iva, 
 *		 modalita, termini di pagamento e tipi rapporto
 *
 * @param	uc			lo UserContext che ha generato la richiesta
 * @param	missione 	la MissioneBulk per cui e' stato selezionato un nuovo terzo
 * @param	aTerzo		il terzo di tipo V_terzo_per_compensoBulk selezioanto dall'utente
 *
 * @return la MissioneBulk coi dati relativi al terzo inizializzati
 */	

public abstract MissioneBulk completaTerzo(UserContext uc, MissioneBulk missione, it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk aTerzo) throws ComponentException;
/**
 * Creazione missione
 *
 * Pre-post-conditions:
 *
 * Nome: creazione temporanea di missione
 * Pre:  L'utente ha selezionato fine inserimento spese
 * Post: Il sistema salva la missione temporaneamente
 
 * Nome: creazione di missione
 * Pre:  L'utente ha selezionato il bottone di salvataggio della missione
 * Post: Aggiornamento dell'anticipo associato alla missione,
 *		 aggiornamento dell'obbligazione associata alla missione ( metodo 'aggiornaObbligazione') 
 *		 aggiornamento della missione con progressivo definitivo e stato cofi = contabilizzato
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	la MissioneBulk da creare
 *
 * @return  la MissioneBulk creata
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Creazione missione
 *
 * Pre-post-conditions:
 *
 * Nome: creazione temporanea di missione
 * Pre:  L'utente ha selezionato fine inserimento spese
 * Post: Il sistema salva la missione temporaneamente
 *
 * Nome: creazione di missione
 * Pre:  Validazione della scadenza di obbligazione associata alla missione
 * Post: Il sistema prosegue con il salvataggio della missione
 *
 * Nome: creazione di missione
 * Pre:  L'utente ha selezionato il bottone di salvataggio della missione 
 * Post: Aggiornamento dell'anticipo associato alla missione,
 *		 aggiornamento dell'obbligazione associata alla missione ( metodo 'aggiornaObbligazione') 
 *		 aggiornamento della missione con progressivo definitivo e stato cofi = contabilizzato
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	la MissioneBulk da creare
 * @param	status		serve per gestire l'eccezione lanciata dall'obbligazione
 *						nel caso non ci sia disponibilita' di cassa 
 *
 * @return  la MissioneBulk creata
 */	

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Cancellazione missione
 *
 * Pre-post-conditions:
 *
 * Nome: cancellazione missione
 * Pre:  Validazione cancellazione superata
 * Post: Il sistema procede con :
 *			- una cancellazione logica se la procedura ritorna il valore 1 
 *			- una cancellazione fisica se la procedura ritorna il valore 2
 *		 e scollega l'eventuale scadenza associata alla missione o al compenso
 *
 * Pre:  Validazione cancellazione NON superata
 * Post: Il sistema non procede con la cancellazione della missione
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk		la MissioneBulk da cancellare
 *
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Carica i dati relativi al cambio
 *
 * Pre-post-conditions:
 *
 * Nome: Carica cambio
 * Pre:  E' stata generata una richiesta di caricamento di un cambio associato ad una certa divisa e valido 
 *       in una certa data 
 * Post: Il sistema restituisce il cambio valido per la divisa data
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	divisa	la DivisaBulk per cui ricercare il cambio
 * @param	dataCambio	la data per cui il cambio deve essere valido
 *
 * @return il CambioBulk trovato oppure null se non esiste nessun cambio valido per quella divisa in quella data
 */

public abstract it.cnr.contab.docamm00.tabrif.bulk.CambioBulk findCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk param1,java.sql.Timestamp param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/**
 * Carica i dati relativi agli Inquadramenti
 *
 * Pre-post-conditions:
 *
 * Nome: Carica inquadramenti
 * Pre:  E' stata generata una richiesta di caricamento degli inquadramenti disponibili per il terzo specificato per una
 *       missione
 * Post: Il sistema restituisce l'elenco degli inquadramenti validi relativi al terzo della missione, con tipo 
 *       rapporto uguale a quello della missione e data di registrazione inclusa nell'intervallo di validità
 *       dell'inquadramento
 *
 * @param	uc			lo UserContext che ha generato la richiesta
 * @param	missione 	la MissioneBulk per cui selezionare gli inquadramenti
 *
 * @return la collezione di Rif_inquadramentoBulk valida per la missione
 */	

public abstract java.util.Collection findInquadramenti(UserContext aUC, MissioneBulk missione) throws ComponentException;
/**
 * Carica i dati relativi agli Inquadramenti e ai Tipi di Trattamento
 *
 * Pre-post-conditions:
 *
 * Nome: Carica Inquadramenti e Tipi Trattamento
 * Pre:  E' stata generata una richiesta di caricamento degli inquadramenti disponibili per il terzo specificato per una
 *       missione e dei relativi tipi di trattamento
 * Post: Il sistema restituisce la missione con gli inquadramenti (metodo findInquadramenti)
 *       e i Tipi Trattamento ( metodo 'findTipi_trattamento') validi relativi al terzo della missione 
 *
 * @param	uc			lo UserContext che ha generato la richiesta
 * @param	missione 	la MissioneBulk per cui selezionare gli inquadramenti e i tipi di trattamento
 *
 * @return la MissioneBulk con gli inquadramenti e i tipi di trattamento inizializzati
 */	

public abstract MissioneBulk findInquadramentiETipiTrattamento(UserContext aUC, MissioneBulk missione) throws ComponentException ;
/**
 * Carica i dati relativi alle coordinate bancarie 
 *
 * Pre-post-conditions:
 *
 * Nome: Carica banche
 * Pre:  E' stata generata una richiesta di caricamento delle coordinate bancarie relative al terzo della missione
 * Post: Il sistema restituisce la lista delle coordinate bancarie relative al terzo della missione
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	missione la MissioneBulk da cui ricavare il terzo per cui selezionare le coordinate bancarie
 *
 * @return la collezione di istanze di tipo BancaBulk 
 */

public abstract java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/**
  * Viene richiesta la lista dei Tipi di rapporto associati ad un Terzo e validi
  *	in data inizio missione
  *
  * Pre-post-conditions:
  *
  * Nome:	Terzo NON selezionato o data inizio missione non valorizzata
  * Pre: 	Non è stato selezionato un Terzo per la missione oppure non e'
  *			stata inserita la data inizio missione
  * Post: 	Non vengono caricati i Tipi di rapporto
  *
  * Nome: 	Terzo selezionato e data inizio missione valorizzata
  * Pre: 	E' stato selezionato un Terzo valido per la missione ed e' stata
  *			inserita la data inizio missione
  * Post: 	Viene restituita la lista dei Tipi di rapporto associati al Terzo e
  *			validi in data inizio missione
  *
  * @param	userContext		lo UserContext che ha generato la richiesta
  * @param	bulk 			l'OggettoBulk da completare
  * @return	La lista dei Tipi di rapporto associati al terzo e validi in data inizio 
  *			missione
  *
**/
public java.util.Collection findTipi_rapporto(UserContext userContext, OggettoBulk bulk) throws ComponentException;
/**
  * Viene richiesta la lista dei Tipi di Trattamento legati
  * al Tipo di Rapporto selezionato
  *
  * Pre-post-conditions:
  *
  * Nome: Tipo di Rapporto NON selezionato
  * Pre: Non è stato selezionato il tipo di rapporto
  * Post: Non vengono caricati i Tipi Trattamento
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un tipo di rapporto valido per la missione
  * Post: Viene restituita la lista dei Tipi di Trattamento
  *		  legati al Tipo di rapporto selezionato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk 		l'OggettoBulk da completare
  * @return	La lista dei Tipi di Trattamento associati al Tipo Rapporto selezionato
  *
**/
public abstract java.util.Collection findTipi_trattamento(UserContext userContext, OggettoBulk bulk) throws ComponentException ;
/**
 * Genera diaria
 *
 * Pre-post-conditions:
 *
 * Nome: Genera diaria
 * Pre:  L'utente ha richiesto la generazione della diaria per una missione
 * Post: Il sistema richiama la stored procedure che genera la diaria; il sistema carica i dettagli della diaria 
 *       (metodo 'ritornaDiariaGenerata')
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui generare la diaria
 *
 * @return  la MissioneBulk con la diaria generata
 */

public abstract it.cnr.contab.missioni00.docs.bulk.MissioneBulk generaDiaria(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Esercizio non aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in uno stato diverso da APERTO
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare missioni.
  *  Esercizio aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in stato APERTO
  *    PostCondition:
  *      e' possibile procedere con la creazione della missione
  *
  *  Inizializzazione data di registrazione
  *    PreCondition:
  *      se l'esercizio di scrivania (quello della missione) e' uguale all'esercizio corrente
  *    PostCondition:
  *      inizializzo la data di registrazione con la data odierna
  *
  *  Inizializzazione data di registrazione
  *    PreCondition:
  *      Se l'esercizio di scrivania (quello della missione) e' antecedente all'esercizio corrente
  *    PostCondition:
  *      inizializzo la data di registrazione con la data 31/12/esercizio di scrivania
  *
  * @param aUC 	lo user context 
  * @param bulk l'istanza di  MissioneBulk che si sta creando
  * @return 	l'istanza di  MissioneBulk inizializzata 
  *
 */
public abstract OggettoBulk inizializzaBulkPerInserimento(UserContext aUC, OggettoBulk bulk) throws ComponentException ;
/**
 *
 * Pre-post-conditions:
 *
 * Nome: inizializza Missione
 * Pre:  L'utente ha richiesto l'inizializzzaione dei dati di una missione già inserita per una eventuale modifica
 * Post: Il sistema carica la missione, il terzo della missione, gli inquadramenti e i tipi di rapporto,
 *       le tappe della missione (metodo 'caricaTappeMissione'), i dettagli di spesa della missione (metodo 'caricaDettagliMissione'),
 *       gli eventuali anticipi, obbligazioni o compensi (metodo 'loadCompenso')
 *       su cui la missione e' stata contabilizzata 
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	la MissioneBulk da inizializzare
 *
 * @return  la MissioneBulk inizializzata
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Pre-post-conditions:
 *
 * Nome: inizializza Divisa e Cambio Per spesa di tipo RimborsoKm
 * Pre:  Ad una spesa di una missione e' stato associato il tipo RimborsoKm
 * Post: Il sistema imposta la divisa della spesa con la divisa di default e il cambio della spesa con il cambio
 *       valido per la divisa e per la data di inizio della tappa della spesa
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	aSpesa	la Missione_dettaglioBulk per tipo rimborso km
 *
 * @return  la MissioneBulk con la spesa inizializzata
 */

public abstract it.cnr.contab.missioni00.docs.bulk.MissioneBulk inizializzaDivisaCambioPerRimborsoKm(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,javax.ejb.EJBException,java.rmi.RemoteException,it.cnr.jada.bulk.ValidationException;
/**
  * Viene richiesto lo stato cofi della missione
  *
  * Pre-post-conditions
  *
  *	Nome: Missione ANNULLATA - Stato COFI uguale ad 'A' 
  *	Pre: La missione è annullata
  *	Post: Ritorna <true>. La missione è annullata
  *
  *	Nome: Missione NON ANNULLATA - Stato COFI diverso da 'A'
  *	Pre: La missione non è annullata
  *	Post: Ritorna <false>. La missione non è annullata
  *
  * @param userContext 	lo UserContext che ha generato la richiesta
  * @param missione 	La missione da controllare  
  * @return vero se la missione è anullata
  *			falso altrimenti
  *
**/

public abstract boolean isMissioneAnnullata(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Carica compenso
 *
 * Pre-post-conditions:
 *
 * Nome: Carica compenso
 * Pre:  Una missione deve essere inizializzata per modifica
 *       La missione ha un compenso
 * Post: Il sistema richiede alla Component che gestisce il compenso il suo caricamento
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui caricare il compenso
 *
 * @return  la MissioneBulk con il compenso caricato
 */

public abstract it.cnr.contab.missioni00.docs.bulk.MissioneBulk loadCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Modifica missione
 *
 * Pre-post-conditions:
 *
 * Nome: modifica
 * Pre:  Una missione e' stata modificata
 * Post: Il sistema aggiorna lo stato coan/coge della missione e aggiorna l'obbligazione associata alla missione
 *       ( metodo 'aggiornaObbligazione')
 *
 * Nome: elimina anticipo
 * Pre:  Una missione e' stata modificata
 *       L'utente ha scollegato l'anticipo dalla missione
 * Post: Il sistema aggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
 *
 * Nome: collega anticipo
 * Pre:  Una missione e' stata modificata
 *       L'utente ha collegato un anticipo alla missione
 * Post: Il sistema agggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
  
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui caricare il compenso
 * @param	status		serve per gestire l'eccezione lanciata dall'obbligazione
 *						nel caso non ci sia disponibilita' di cassa
 *
 * @return  la MissioneBulk con il compenso caricato
 */	

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Modifica missione
 *
 * Pre-post-conditions:
 *
 * Nome: modifica missione
 * Pre:  Validazione dell' eventuale obbligazione collegata alla missione andata a buon fine
 * Post: Il sistema prosegue con l'aggiornamento della missione
 *
 * Nome: modifica
 * Pre:  Una missione e' stata modificata
 * Post: Il sistema aggiorna lo stato coan/coge della missione e aggiorna l'obbligazione associata alla missione
 *       ( metodo 'aggiornaObbligazione')
 *
 * Nome: elimina anticipo
 * Pre:  Una missione e' stata modificata
 *       L'utente ha scollegato l'anticipo dalla missione
 * Post: Il sistema aggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
 *
 * Nome: collega anticipo
 * Pre:  Una missione e' stata modificata
 *       L'utente ha collegato un anticipo alla missione
 * Post: Il sistema agggiorna lo stato dell'anticipo ( metodo 'aggiornaAnticipo')
  
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk per cui caricare il compenso
 * @param	status		serve per gestire l'eccezione lanciata dall'obbligazione
 *						nel caso non ci sia disponibilita' di cassa
 *
 * @return  la MissioneBulk con il compenso caricato
 */	

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Annulla le modifiche apportate alla missione e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: 	Rollback to savePoint
 * Pre:  	Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: 	Tutte le modifiche effettuate sulla missione da quando si e' impostato il savepoint vengono annullate
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public abstract void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException;
/**
 * Inzializzazione divisa e cambio
 *
 * Pre-post-conditions:
 *
 * Nome: inizializzazione
 * Pre:  L'utente ha richiesto la modifica della nazione di una tappa 
 * Post: Il sistema inizializza la tappa impostando come divisa la divisa definita per la nuova nazione
 *       e come cambio il cambio della divisa valido per la data di inizio tappa
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk a cui appartiene la tappa
 * @param	tappa	la Missione_tappaBulk da inizializzare
 *
 * @return MissioneBulk con la tappa inizializzata
 */

public abstract it.cnr.contab.missioni00.docs.bulk.MissioneBulk setDivisaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Inzializzazione tappa
 *
 * Pre-post-conditions:
 *
 * Nome: inizializzazione
 * Pre:  L'utente ha richiesto la creazione di una nuova tappa 
 * Post: Il sistema inizializza la nuova tappa impostando la nazione a Italia, la divisa alla divisa definita per l'Italia
 *       e il cambio al cambio della divisa valido per la data di inizio tappa
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk a cui appartiene la nuova tappa
 * @param	tappa	la Missione_tappaBulk da inizializzare
 *
 * @return MissioneBulk con la nuova tappa inizializzata
 */

public abstract it.cnr.contab.missioni00.docs.bulk.MissioneBulk setNazioneDivisaCambioItalia(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2) throws it.cnr.jada.comp.ComponentException;
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
 * Aggiornamento anticipo
 *
 * Pre-post-conditions:
 *
 * Nome: salvataggio missione + anticio di importo maggiore + compenso
 * Pre:  Il sistema salva una missione collegata ad un anticipo di importo maggiore e
 *		 ad un compenso
 * Post: Il sistema aggiorna la linea di attivita' dell'anticipo collegato con quella
 *		 selezionata nel compenso
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la missione il cui anticipo e' da aggiornare
 *
 * @return 
 *
 */	
public abstract void updateAnticipo(UserContext aUC, MissioneBulk missione) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException;
/**
 * Aggiornamento compenso
 *
 * Pre-post-conditions:
 *
 * Nome: aggiornamento compenso in automatico
 * Pre:  Il sistema salva una missione per aggiornarne alcuni campi che
 *		 comportano l'aggiornamento dei relativi campi del compenso collegato.
 * Post: Il sistema aggiorna il compenso collegato alla missione
 *
 * Nome: Rilettura del compenso modificato
 * Pre:  Il sistema ha aggiornato il compenso
 * Post: Il sistema rilegge la versione del compenso aggiornata
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	missione	la missione il cui compenso e' da aggiornare
 *
 * @return la missione il cui compenso e' stato aggiornato
 *
 */	

public abstract MissioneBulk updateCompenso(UserContext aUC, MissioneBulk missione) throws	ComponentException, it.cnr.jada.persistency.PersistencyException ;
/** 
  *  Validazione esercizio data registrazione
  *    PreCondition:
  *      L'esercizio della data registrazione e' antecedente a quello di scrivania 
  *		(quello della missione) ed e' APERTO
  *    PostCondition:
  *      La data di registrazione viene validata
  *
  *  Validazione esercizio data registrazione
  *    PreCondition:
  *      L'esercizio della data registrazione e' antecedente a quello di scrivania 
  *		(quello della missione) e NON e' APERTO
  *    PostCondition:
  *      La data di registrazione non viene validata
  *
  * @param aUC 	lo user context 
  * @param bulk l'istanza di  MissioneBulk di cui e' variata la data di registrazione
  *
 */
public abstract void validaEsercizioDataRegistrazione(UserContext aUC, MissioneBulk missione) throws ComponentException ;
/**
 * Calcolo importi e validazione massimali importi
 *
 * Pre-post-conditions:
 *
 * Nome: calcola importi spesa per rimborso km
 * Pre:  E' stata richiesto il calcolo degli importi di una spesa per rimborso km
 * Post: Il sistema calcola "im_spesa_euro" moltiplicando i km per l'indennità chilometrica (sempre in EURO).
 *		 Il sistema inizializza pone : "im_spesa_euro", "im_spesa_divisa" e "im_totale_spesa" tutti uguali.
 * 
 * Nome: conversione importo spesa con tipologia diversa da rimborso km
 * Pre:  E' stata richiesta la conversione dell'importo inserito dall'utente di una spesa diversa da rimborso km
 * Post: Se l'utente ha selezionato una divisa straniera, il sistema converte "im_spesa_divisa" nella divisa di default 
 *		 (metodo 'getImportoSpesaEuro')
 *
 * Nome: validazione massimale spesa
 * Pre:  E' stata richiesta la validazione dell'importo della spesa
 * Post: Il sistema verifica che "im_spesa_euro" non sia maggiore del massimale della spesa
 *		 (limite_max_spesa eventualmente convertito se tipo_spesa.cd_divisa!=EURO), metodo 'validaMassimaleTipoSpesa'
 *
 * Nome: valida spesa di tipo pasto
 * Pre:  E' stata richiesta la validazione dell'importo di una spesa di tipo pasto
 * Post: Il sistema verifica che "im_spesa_euro" non sia maggiore del massimale del pasto.
 *		 (limite_max_pasto eventualmente convertito se tipo_pasto!=EURO), metodo 'validaMassimaleTipoPasto'
 *
 * Nome: calcolo importo totale spesa
 * Pre:  E' stato richiesto il calcolo del totale della spesa (EURO)
 * Post: Se la spesa e' un trasporto l'"im_totale_spesa" e' uguale alla somma di "im_spesa_euro" con
 *		 "im_maggiorazione" (eventualmente convertita se l'utente ha selezionato una divisa straniera).
 *		 Se la spesa non e' un trasporto l'"im_totale_spesa" e' uguale a "im_spesa_euro".
 * 
 * @param	uc			lo UserContext che ha generato la richiesta
 * @param	missione	la MissioneBulk a cui appartiene la spesa
 * @param	spesa		la Missione_dettaglioBulk per cui effettuare i cacloli e la validazione degli importi
 *
 * @return MissioneBulk validata
 */	
public abstract it.cnr.contab.missioni00.docs.bulk.MissioneBulk validaMassimaliSpesa(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException,it.cnr.jada.bulk.ValidationException;
/**
  * Viene richiesta la validazione dell'obbligazione associata alla missione
  *
  * Pre-post-conditions
  *
  *	Pre: 	La missione e' collegata a compenso o ad anticipo di importo maggiore
  *	Post: 	Non esiste alcuna scadenza legata alla missione (non faccio controlli)
  *
  * Nome: 	Scadenza non selezionata
  *	Pre: 	Non e' stata selezionata la scadenza da associare alla missione
  *	Post: 	Non viene consentita l'associazione della scadenza con la missione
  *		  	Generata una ApplicationException con il messaggio:	"Nessuna obbligazione associata!"
  *
  * Nome: 	Importi obbligazione/scadenza NULLI
  *	Pre: 	L'importo della obbligazione e/o della scadenza è nullo
  *	Post: 	Non viene consentita l'associazione della scadenza con la missione
  *		  	Generata una ApplicationException con il messaggio:	
  *			"L'importo dell'obbligazione/scadenza è un dato obbligatorio"
  *
  * Nome: 	Importo scadenza diverso da quello della missione
  *	Pre: 	L'importo della scadenza è diverso da quello della missione
  *	Post: 	Non viene consentita l'associazione della scadenza con la missione
  *		  	Generata una ApplicationException con il messaggio:
  *			"La scadenza di obbligazione associata ha un importo diverso da quello della missione!"
  *
  * Nome: 	Data scadenza NON valida
  *	Pre: 	La scadenza selezionata ha una data minore della data di registrazione della missione
  *	Post: 	Non viene consentita l'associazione della scadenza con la missione
  *		  	Generata una ApplicationException con il messaggio:
  *			"La data della scadenza dell'obbligazione deve essere successiva alla data di registrazione della missione!"
  *  
  * Nome: 	Terzo selezionato NON valido
  *	Pre: 	Il terzo selezionato è diverso dal terzo della missione oppure il tipo entità NON è DIVERSI
  *	Post: 	Non viene consentita l'associazione della scadenza con la missione
  *		  	Generata una ApplicationException con il messaggio:
  *			"L'obbligazione deve avere un creditore valido!"
  *
  * Nome: 	Tutte le validazioni precedenti superate
  * Pre: 	L'obbligazione supera tutte le validazioni precedenti
  * Post: 	Viene validata l'associazione della missione con la scadenza di obbligazione
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	scadenza		la scadenza da validare
  *
 **/
public abstract void validaObbligazione(UserContext userContext, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza, OggettoBulk bulk) throws ComponentException;
/**
  *	Validazione del Terzo
  *
  * Pre-post-conditions
  *
  * Nome: 	Terzo assente
  *	Pre: 	Non è stato selezionato un terzo
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  * 		"Inserire il terzo"
  *
  * Nome: 	Terzo non valido alla data registrazione
  *	Pre: 	Il terzo selezionato non è valido alla data registrazione
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Terzo selezionato non è valido in Data Registrazione"
  *
  * Nome: 	Modalita di pagamento assente
  *	Pre: 	Non è stato selezionata una modalita di pagamento
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare la Modalità di pagamento"
  *
  * Nome: 	Modalita di pagamento non valida
  *	Pre: 	La modalita di pagamento non e' valida (con banca)
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare una Modalità di pagamento valida"
  *
  * Nome: 	Tipo rapporto assente
  *	Pre: 	Non è stato selezionato un tipo rapporto
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare il Tipo Rapporto"
  *
  * Nome: 	Tipo rapporto non valido alla data inizio missione
  *	Pre: 	Il tipo rapporto selezionato non è valido in data inizio missione
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Tipo Rapporto selezionato non è valido alla Data Inizio Missione"
  *
  * Nome: 	Tipo trattamento assente
  *	Pre: 	Non è stato selezionato un tipo trattamento
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare il Tipo Trattamento"
  *
  * Nome: 	Tipo trattamento non valido alla data registrazione
  *	Pre: 	Il tipo trattamento non e' valido alla data di registrazione
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Tipo Trattamento selezionato non è valido alla Data Registrazione"
  *
  * Nome: 	Inquadramento assente
  *	Pre: 	Non è stato selezionato un inquadramento
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare l'Inquadramento"
  *
  * Nome: 	Inquadramento non valido alla data registrazione
  *	Pre: 	L'inquadramento non e' valido alla data di registrazione
  *	Post: 	Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Tipo Trattamento selezionato non è valido alla Data Registrazione"
  *  
  * Nome: 	Terzo valido
  *	Pre: 	Il terzo selezionato non ha errori
  *	Post: 	Il terzo è valido e prosegue con l'operazione
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	missione		la missione di cui validare il terzo
  *
 **/

public abstract void validaTerzo(UserContext userContext, MissioneBulk missione) throws ComponentException;
/**
  *
  * Validazione del Terzo
  *
  * Pre-post-conditions
  *
  * Nome: 	Terzo assente
  *	Pre: 	Non è stato selezionato un terzo
  *	Post:	Ritorna il valore 1
  *
  * Nome: 	Terzo non valido alla data registrazione
  *	Pre: 	Il terzo selezionato non è valido alla data registrazione
  *	Post: 	Ritorna il valore 2
  *
  * Nome: 	Modalita di pagamento assente
  *	Pre: 	Non è stato selezionata una modalita di pagamento
  *	Post: 	Ritorna il valore 3
  *
  * Nome: 	Banca non inserita
  *	Pre: 	Non è stato selezionato un conto corretto
  *	Post: 	Ritorna il valore 4
  *
  * Nome: 	Tipo rapporto assente
  *	Pre: 	Non è stato selezionato un tipo rapporto
  *	Post: 	Ritorna il valore 5
  *
  * Nome: 	Tipo rapporto non valido alla data inizio missione
  *	Pre: 	Il tipo rapporto selezionato non è valido in data inizio missione
  *	Post: 	Ritorna il valore 6
  *
  *	Nome: 	Inquadramento assente
  *	Pre: 	Non è stato selezionato un inquadramento
  *	Post: 	Ritorna il valore 7
  *
  * Nome: 	Inquadramento non valido alla data registrazione
  *	Pre: 	L'inquadramento selezionato non è valido in data registrazione
  *	Post: 	Ritorna il valore 8
  * 
  *	Nome: 	Tipo trattamento assente
  *	Pre: 	Non è stato selezionato un tipo trattamento
  *	Post: 	Ritorna il valore 9
  *
  * Nome: 	Tipo trattamento non valido alla data registrazione
  *	Pre: 	Il tipo trattamento selezionato non è valido in data registrazione
  *	Post: 	Ritorna il valore 10
  *  
  * Nome: 	Terzo valido
  *	Pre: 	Il terzo selezionato non ha errori
  *	Post: 	Ritorna il valore 0
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	missione		la missione di cui validare il terzo
  * @param	checkModPag		Flag che stabilisce se occorre validare anche le modalita
  *							di pagamento e la banca
  * @return	il codice di errore relativo
  *
 **/
 
public abstract int validaTerzo(UserContext userContext, MissioneBulk missione, boolean checkModPag) throws ComponentException;
}
