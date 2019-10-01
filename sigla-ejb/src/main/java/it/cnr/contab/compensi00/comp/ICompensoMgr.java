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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.14.13)
 * @author: Roberto Fantino
 */
public interface ICompensoMgr extends ICRUDMgr {
/**
  *	Richiama la procedura Oracle CNRCTB550.AGGIORNAMONTANTI
  *	che aggiorna gi montanti del compenso
  *
**/
public void aggiornaMontanti(UserContext userContext, CompensoBulk compenso) throws ComponentException;
/**
  * Aggiornamento dell'obbligazione e della scadenza associata al compenso
  * a seguito di una richiesta di salvataggio (modifica/creazione) di un compenso
  *
  * Pre-post-conditions
  *
  * Nome: Aggiornamento obbligazione/scadenza associata a compenso
  *	Pre: Viene richiesto un aggiornamento della obbligazione, della scadenza
  * 	 e dei saldi a seguito di una richiesta di salvataggio del compenso associato
  * Post: Viene eseguito l'aggiornamento dei saldi, dell'obbligazione e della scadenza
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso l'OggettoBulk che ha scatenato la richiesta di aggiornamento
  *
**/
public CompensoBulk aggiornaObbligazione(UserContext userContext, CompensoBulk compenso, OptionRequestParameter status) throws ComponentException;
/**
  * Viene richiesto un nuovo progressivo temporaneo per il compenso
  *
  * Pre-post-conditions
  *
  * Name: Richiesta nuovo progressivo temporaneo
  * Pre: Viene richiesto un nuovo progressivo temporaneo
  * Post: Viene restituito un nuovo progressivo temporaneo da assegnare al compenso
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso il CompensoBulk a cui deve essere assegnato il nuovo progressivo
  * @return	il nuovo progressivo temporaneo da utilizzare
  *
**/

public abstract java.lang.Long assegnaProgressivoTemporaneo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
//^^@@
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
//^^@@
public abstract it.cnr.jada.util.RemoteIterator cercaObbligazioni(UserContext context, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro) throws ComponentException;
/**
  * Completamento dell'OggettoBulk <compenso> aggiornando i campi 
  * relativi al terzo selezionato <vTerzo>
  *
  * Pre-post-conditions
  *
  * Nome: Completamento del compenso
  * Pre: Viene richiesto il completamento del compenso
  * Post: Viene restituito il compenso completo di tutti i dati 
  *		  relativi al terzo selezionato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso l'OggettoBulk da completare
  * @param	vTerzo terzo con cui completare il compenso
  * @return l'OggettoBulk completo
  *
  * Metodo privato chiamato:
  *		completaTerzo(UserContext userContext, CompensoBulk compenso);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene modificata in modo manuale un contributo/ritenuta del compenso
  *
  * Pre-post_conditions
  *
  * Nome: Conferma modifica manuale di un contributo/ritenuta
  *	Pre: Viene richiesta l'aggiornamento degli importi del compenso a seguito 
  * 	 della modifica di un suo contributo/ritenuta
  *	Post: Vengono aggiornati gli importi del compenso a seguito della modifica
  *		  manuale di un contributi/ritenuta
  * 	  Esecuzione del package CNRCTB550.AGGCOMPENSOSENZACALCOLI
  *		  Viene restituito il compenso aggiornato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso il compenso da aggiornare
  * @param	cori il contributo/ritenuta modificato
  * @return	il compenso aggiornato dopo l'esecuzione della procedura oracle
  *
  * Metodi privati chiamati:
  *		aggCompensoSenzaCalcoli(UswerContext userContext, CompensoBulk compenso);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk confermaModificaCORI(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Ritorna il conguaglio a cui è stato associato il compenso selezionato
 *
 * Pre-post_conditions
 *
 * Nome: Compenso associato a conguaglio
 * Pre: Viene richiesta il conguaglio a cui il compenso risulta associato
 * Post: Viene restituito il conguaglio
 *
 * Nome: Compenso NON associato a conguaglio
 * Pre: Il compenso non è stato associato a nessun conguaglio
 * Post: Viene restituito il valore NULL
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	compenso	il compenso da aggiornare
 * @return	il conguaglio a cui è associato il compenso
 *
**/
public abstract ConguaglioBulk conguaglioAssociatoACompenso(UserContext userContext, CompensoBulk compenso) throws ComponentException ;
/**
  * Esegue una operazione di creazione di un OggettoBulk.
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la creazione di una istanza di CompensoBulk che supera la validazione
  * Post: Consente l'inserimento del compenso assegnandogli un progressivo definitivo e cancellando il
  *		  compenso con progressivo temporaneo precedentemente creato
  *			- assegna un progressivo definitivo al compenso
  * 		- aggiorna l'obbligazione associata
  *			- inserisce il compenso
  * 		- inserisce le righe Contributo/Ritenuta
  *			- inserisce le righe Contributo/Ritenuta dettaglio
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la creazione di una istanza di CompensoBulk che NON supera la validazione
  * Post: Viene generata una eccezione con la descrizione dell'errore
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk il compenso che deve essere creato
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo di validzione del compenso:
  *		validaCompenso(CompensoBulk compenso)
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Esegue una operazione di creazione di un OggettoBulk.
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la creazione di una istanza di CompensoBulk che supera la validazione
  * Post: Consente l'inserimento del compenso assegnandogli un progressivo definitivo e cancellando il
  *		  compenso con progressivo temporaneo precedentemente creato
  *			- assegna un progressivo definitivo al compenso
  * 		- aggiorna l'obbligazione associata
  *			- inserisce il compenso
  * 		- inserisce le righe Contributo/Ritenuta
  *			- inserisce le righe Contributo/Ritenuta dettaglio
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la creazione di una istanza di CompensoBulk che NON supera la validazione
  * Post: Viene generata una eccezione con la descrizione dell'errore
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk il compenso che deve essere creato
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo di validzione del compenso:
  *		validaCompenso(CompensoBulk compenso)
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta l'esecuzione della procedura Oracle CNRCTB550.CONTABILIZZACOMPENSOCOFI
  *	per la contabilizzare il compenso
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre: Viene richiesta la contabilizzazione COFI del compenso a 
  *		 seguito di una validazione superata
  * Post: Viene eseguita la procedura oracle per la Contabilizzazione COFI del compenso
  *
  * Nome: Validazione NON superata
  * Pre: Viene richiesta la contabilizzazione COFI del compenso a
  *      seguito di una validazione NON superata
  * Post: Contabilizzazione COFI non eseguita.
  * 	  Viene sollevata una ValidationExecption con la descrizione dell'errore
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio il conguaglio da abilitare
  * @return	il conguaglio aggiornato dopo l'esecuzione della procedura oracle
  *
  * Metodo di validazione compenso per contabilizzazione
  *		validaCompensoPerContabilizzazione(CompensoBulk compenso);
  * 	contabilizzaCompensoCofi(CompensoBulk compenso);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk doContabilizzaCompensoCofi(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * 
  *
**/

public abstract EstrazioneCUDVBulk doElaboraCUD(it.cnr.jada.UserContext param0, EstrazioneCUDVBulk param1) throws it.cnr.jada.comp.ComponentException;
public CompensoBulk elaboraScadenze(UserContext userContext, CompensoBulk compenso, Obbligazione_scadenzarioBulk oldScad, Obbligazione_scadenzarioBulk newScad) throws ComponentException;
/**
  * Viene richiesta la cancellazione del Compenso Temporaneo
  * generato da un'operazione di inserimento o modifica (nel caso di clone)
  *
  * Pre-post-conditions
  *
  * Nome: Cancellazione della copia del compenso originale
  * Pre: Viene richiesta la cancellazione della copia del compenso originale
  * Post: Viene eliminata fisicamente la copia del compenso creata precedentemente
  *
  * @param userContex lo UserContext che ha generato la richiesta
  * @param compenso Oggetto buulk da cancellare
  * @param tmp Progressivo della copia del compenso creata
  *
**/
public void eliminaCompensoTemporaneo(UserContext userContext, CompensoBulk compenso, Long tmp) throws ComponentException;
/**
  * Viene richiesta l'eliminazione del Compenso
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata e compenso NON cancellabile fisicamente
  * Pre: Il compenso passa i criteri di validità di business per l'operazione
  *		 di cancellazione ed è in stato Pagato o associato a Mandato/Reversale 
  * Post: Viene consentita la cancellazione logica del compenso
  * 	  con relativi aggiornamenti degli importi dell'obbligazione associata
  *
  * Nome: Validazione superata e compenso cancellabile fisicamente
  * Pre: Il compenso passa i criteri di validità di business per l'operazione
  *		 di cancellazione ed è in stato Iniziale oppure Contabilizzato ma non associato a Mandato/Reversale
  * Post: Viene consentita la cancellazione fisica del compenso
  * 	  con relativi aggiornamenti degli importi dell'obbligazione associata
  *
  * Nome: Validazione NON superata
  * Pre: Il compenso non passa i criteri di validità di business per l'operazione
  *		 di cancellazione 
  * Post: Viene impedita la cancellazione sia fisica che logica del compenso
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da eliminare
  * @return	void
  *
  * Metodo privato:
  *		aggiornaObbligazioneSuCancellazione(UserContext userContext, CompensoBulk compenso, OptionRequestParamenter status);
  *
**/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Scollega una obbligazione dal compenso relativo
 *
 * Pre-post-conditions
 *
 * Nome: Scollego obbligazione da compenso
 * Pre: Viene richiesto di scollegare una obbligazione da compenso
 * Post: L'obbligazione viene scollegata e il compenso aggiornato
 *
**/ 
public abstract CompensoBulk eliminaObbligazione(UserContext userContext, CompensoBulk compenso) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta l'esecuzione della procedura Oracle CNRCTB550.ELABORACOMPENSO
  *
  * Pre-post-conditions:
  *
  * Nome: Calcolo CO/RI per Compenso - Validazione superata
  * Pre: Viene richiesto il calcolo dei Contributi/Ritenute legate al compenso
  * 	 a fronte di una validazione superata positivamente
  * Post: Viene eseguita la procedura per il calcolo dei Contributi/Ritenute
  *		  Viene resitutito il compenso aggiornato
  *
  * Nome: Calcolo CO/RI per Compenso - Validazione NON superata
  * Pre: Viene richiesto il calcolo dei Contributi/Ritenute legate al compenso
  * 	 a fronte di una validazione NON superata
  * Post: Procedura per il calcolo dei Contributi/Ritenute NON eseguita
  * 	  Invio di una eccezione con messaggio d'errore associato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso il compenso da aggiornare
  * @return	il compenso aggiornato dopo creazione dei contributi/ritenute
  *
  * Metodo di validazione compenso per contabilizzazione
  *		validaCompensoSuEseguiCalcolo(CompensoBulk compenso);
  * 	elaboraCompenso(UserContext userContext, CompensoBulk compenso);
  *
**/
public CompensoBulk eseguiCalcolo(UserContext userContext, CompensoBulk compenso) throws ComponentException;
/**
  * Viene richiesta la lista delle Banche associate ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il compenso
  * Post: Non vengono caricate le banche.
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il compenso
  * Post: Viene restituita la lista delle Banche associate al Terzo
  * 	  e alla Modalità di Pagamento selezionata
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso l'OggettoBulk da completare
  * @return	La lista delle banche associate al terzo
  *
**/

public abstract java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la lista delle Modalita di Pagamento associate ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il compenso
  * Post: Non vengono caricate le modalita di pagamento
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il compenso
  * Post: Viene restituita la lista delle Modalita di pagamento associate al Terzo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da completare
  * @return	La lista delle modalita di pagamento associate al terzo
  *
**/

public abstract java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la lista dei Termini di pagamento associati ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il compenso
  * Post: Non vengono caricati i termini di pagamento
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il compenso
  * Post: Viene restituita la lista dei Termini di pagamento associati al Terzo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da completare
  * @return	La lista dei Termini di pagamento associati al terzo
  *
**/

public abstract java.util.Collection findTermini(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la lista dei Tipi di rapporto associati ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il compenso
  * Post: Non vengono caricati i Tipi di rapporto
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il compenso
  * Post: Viene restituita la lista dei Tipi di rapporto associati al Terzo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso l'OggettoBulk da completare
  * @return	La lista dei Tipi di rapporto associati al terzo
  *
**/

public abstract java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
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
  * Pre: E' stato selezionato un tipo di rapporto valido per il compenso
  * Post: Viene restituita la lista dei Tipi di Trattamento
  *		  legati al Tipo di rapporto selezionato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	compenso l'OggettoBulk da completare
  * @return	La lista dei Tipi di Trattamento associati al Tipo Rapporto selezionato
  *
**/

public abstract java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la data odierna
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @return	La data odierna
  *
**/
public abstract java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException;
/**
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di modifica.
  *
  * Pre-post-conditions:
  *
  * Nome: Oggetto non esistente
  * Pre: L'OggettoBulk specificato non esiste.
  * Post: Viene generata una CRUDException con la descrizione dell'errore.
  *
  * Nome: Tutti i controlli superati
  * Pre: L'OggettoBulk specificato esiste.
  * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
  *		 per l'operazione di presentazione e modifica nell'interfaccia visuale.
  *		 L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
  *		 ottenuto concatenando il nome della component con la stringa ".edit"
  * 
  * @param	uc	lo UserContext che ha generato la richiesta
  * @param	bulk	l'OggettoBulk da preparare
  * @return	l'OggettoBulk preparato
  *
  * Metodo privato chiamato:
  *		completaCompenso(UserContaxt userContext, CompensoBulk compenso);
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Prepara un compenso per la presentazione all'utente per una possibile
  * operazione di modifica.
  * Il compenso viene completato con i dati ricevuti dalla minicarriera
  *
  * Pre-post-conditions:
  *
  * Nome: Inizializzazione compenso da minicarriera
  * Pre: Si richiede di inizializzare il compenso con i dati della minicarriera
  * Post: Viene restituito il compenso inizializzato con tutti gli oggetti collegati e preparato
  *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
  * 
  * @param	userContext lo UserContext che ha generato la richiesta
  * @param	compenso il compenso da preparare
  * @param	minicarriera la minicarriera da cui prendere le informazioni per completare il compenso
  * @return	il compenso preparato
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMinicarriera(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param2,java.util.List param3) throws it.cnr.jada.comp.ComponentException;
/**
  * Prepara un compenso per la presentazione all'utente per una possibile
  * operazione di modifica.
  * Il compenso viene completato con i dati ricevuti dalla missione
  *
  * Pre-post-conditions:
  *
  * Nome: Inizializzazione compenso da missione
  * Pre: Si richiede di inizializzare il compenso con i dati della missione
  * Post: Viene restituito il compenso inizializzato con tutti gli oggetti collegati e preparato
  *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
  * 
  * @param	userContext lo UserContext che ha generato la richiesta
  * @param	compenso il compenso da preparare
  * @param	missione la missione da cui prendere le informazioni per completare il compenso
  * @return	il compenso preparato
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMissione(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param2) throws it.cnr.jada.comp.ComponentException;
public abstract CompensoBulk inserisciCompenso(UserContext userContext, CompensoBulk compenso) throws it.cnr.jada.comp.ComponentException ;
/**
  * Viene richiesto lo stato cofi del compenso
  *
  * Pre-post-conditions
  *
  *	Nome: Compenso ANNULLATO - Stato COFI uguale ad 'A'
  *	Pre: Il compenso è annullato
  *	Post: Ritorna <true>. Il compenso è annullato
  *
  *	Nome: Compenso NON ANNULLATO - Stato COFI diverso da 'A'
  *	Pre: Il compenso non è annullato
  *	Post: Ritorna <false>. Il compenso non è annullato
  *
  * @param userContext lo UserContext che ha generato la richiesta
  * @param compenso il compenso da controllare  
  * @return vero se il compenso è anullato, falso altrimenti
  *
**/

public abstract boolean isCompensoAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la lista dei Documenti Contabili (Mandati e Reversali) associati al compenso
  *
  * Pre-post-conditions:
  *
  * Nome: Compenso non pagato
  * Pre: Viene richiesta la lista dei Documenti Contabili per un compenso non pagato
  * Post: Viene restituita una ApplicationException con la descrizione dell'errore
  *			"Il compenso non è pagato"
  *
  * Nome: Compenso pagato
  * Pre: Viene richiesta la lista dei Documenti Contabili per un compenso pagato
  * Post: Viene restituita la lista dei Documenti Contabili associati
  *
  * @param	userContext		lo UserContext che ha generato la richiesta
  * @param	bulk			il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
public java.util.List loadDocContAssociati(UserContext userContext, CompensoBulk compenso) throws ComponentException;
/**
  * Esegue una operazione di modifica di un OggettoBulk.
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la modifica di una istanza di CompensoBulk che supera la validazione
  * Post: Consente la modifica del compenso cancellando il compenso clone precedentemente creato (se necessario)
  * 	  e aggiornando l'importo dell'obbligazione associata
  * 		- aggiorna l'obbligazione associata
  *			- aggiorna il compenso
  * 		- cancellazione compenso clone (se necessario)
  *		  Ritorna il compenso aggiornato
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la modifica di una istanza di CompensoBulk che NON supera la validazione
  * Post: Viene generata una eccezione con la descrizione dell'errore
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk il compenso che deve essere modificato
  * @return	l'OggettoBulk risultante dopo l'operazione di modifica.
  *
  * Metodo di validzione del compenso:
  *		validaCompenso(CompensoBulk compenso)
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Esegue una operazione di modifica di un OggettoBulk.
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la modifica di una istanza di CompensoBulk che supera la validazione
  * Post: Consente la modifica del compenso cancellando il compenso clone precedentemente creato (se necessario)
  * 	  e aggiornando l'importo dell'obbligazione associata
  * 		- aggiorna l'obbligazione associata
  *			- aggiorna il compenso
  * 		- cancellazione compenso clone (se necessario)
  *		  Ritorna il compenso aggiornato
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la modifica di una istanza di CompensoBulk che NON supera la validazione
  * Post: Viene generata una eccezione con la descrizione dell'errore
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk il compenso che deve essere modificato
  * @return	l'OggettoBulk risultante dopo l'operazione di modifica.
  *
  * Metodo di validzione del compenso:
  *		validaCompenso(CompensoBulk compenso)
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene modificato il tipo trattamento del compenso
  *
  * Pre-post-conditions
  *
  *	Nome: Modifica del Tipo Trattamento
  *	Pre: Viene modificato il Tipo Trattamento del compenso
  *	Post: Vengono ricalcolati i dati relativi alla Regione Irap, Voce Iva, 
  * 	  Tipologia Rischio
  *
  * @param userContext	lo UserContext che ha generato la richiesta
  * @param compenso 	il compenso da aggiornare
  * @return il compenso aggiornato
  *
  *	
  * Metodo richiamato:
  *		setDatiLiquidazione(userContext, compenso)
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoTrattamentoChange(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene Richiesto il caricamento di un compenso
  *
  * Pre-post_conditions
  *
  * Nome: Caricamento compenso
  *	Pre: Viene richiesto il caricamento da db del compenso
  *	Post: Viene caricato da database il compenso insieme a tutti 
  *		gli oggetti complessi necessari ad una sua corretta gestione
  *			- terzo
  *			- tipo trattamento
  *			- regione Irap, voce iva, tipologia rischio
  *			- contributi/ritenuta
  *			- dettagli di ogni contriuto/ritenuta
  *			- minicarriera
  *			- conguaglio
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk		il compenso che deve essere ri-caricato
  * @return	il compenso aggiornato
  *
  * Metodo privato chiamato:
  *		completaCompenso(UserContext userContext, CompensoBulk compenso);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.CompensoBulk reloadCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Ri-sincronizzazione della scadenza dopo un'operazione di modifica
 *
 * Pre-post-conditions
 *
 * Nome: Ri-sincronizzazione scadenza
 * Pre: Viene richiesta una rilettura da database della scadenza
 * Post: Viene restituita la scadenza sincronizzata
 *
 * @param	userContext	Lo UserContext che ha generato la richiesta
 * @param	scadenza	La scadenza da sincronizzare
 * @return	La scadenza sincronizzata
*/
public abstract Obbligazione_scadenzarioBulk resyncScadenza(UserContext userContext, Obbligazione_scadenzarioBulk scadenza) throws ComponentException;

public abstract it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException ;
/**
 * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo che ha aperto il compenso
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void rollbackToSavePoint(it.cnr.jada.UserContext userContext, String savePointName) throws ComponentException;
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void setSavePoint(it.cnr.jada.UserContext userContext, String savePointName) throws ComponentException;
/**
  * Viene richiesto di aggiornare l'importo associato a documenti amministrativi
  * dell'obbligazione associata al compenso
  * 
  * Pre-post-conditions
  *
  * Nome: Aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  * Pre: Richiesto l'aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  * Post: Il dettaglio viene aggiornato
**/

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la validazione dell'obbligazione creata
  *			- 1 sola scadenza inserita
  *			- Importo obbligazione e importo scadenza NON NULLI
  *			- Importo obbligazione uguale a importo scadenza
  *			- Importo obbligazione uguale a importo compenso
  *			- Terzo dell'obbligazione uguale al terzo del compenso o di tipo entità DIVERSI
  *
  * Pre-post-conditions
  *
  * Nome: Selezionata più di una scadenza
  *	Pre: E' stata selezionata più di una scadenza da associare al compenso
  *	Post: Non viene consentita la creazione/modifica dell'obbligazione
  *		  Generata una ApplicationException con il messaggio:
  *			"E' possibile creare solo una scadenza di importo pari all'importo del compenso."
  *
  * Nome: Importi obblig./scadenza NULLI
  *	Pre: L'importo della obblig. e/o della scadenza è nullo
  *	Post: Non viene consentita la creazione/modifica dell'obbligazione
  *		  Generata una ApplicationException con il messaggio:
  *			"L'Importo dell'obbligazione/scadenza è un dato obbligatorio"
  *
  * Nome: Importi obblig./scadenza diversi da quelli del compenso
  *	Pre: L'importo della obblig. è diverso da quello della scadenza oppure l'importo della scadenza
  * 	 è diverso dall'importo lordo del compenso
  *	Post: Non viene consentita la creazione/modifica dell'obbligazione
  *		  Generata una ApplicationException con il messaggio:
  *			"L'importo dell'obbligazione deve corrispondere all'importo della scadenza"
  *			"L'importo dell scadenza deve corrispondere all'importo del compenso"
  *
  * Nome: Terzo selezionato NON valido
  *	Pre: Il terzo selezionato è diverso dal terzo del compenso oppure il tipo entità NON è DIVERSI
  *	Post: Non viene consentita la creazione/modifica dell'obbligazione
  *		  Generata una ApplicationException con il messaggio:
  *			"L'obbligazione deve avere un creditore valido!"
  *
  * Nome: Tutte le validazioni precedenti superate
  * Pre: L'obbligazione supera tutte le validazioni precedenti
  * Post: Viene validato l'OggettoBulk
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	scadenza		la scadenza da validare
  *
 **/

public abstract void validaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,it.cnr.jada.bulk.OggettoBulk param2) throws ComponentException;
/**
  * Viene richiesta la validazione del terzo selezionato
  *	Ritorna una ApplicationException con la descrizione 
  * dell'errore relativo
  *
  *	errorCode		Significato
  *	=========		===========	
  *		0			Tutto bene
  *		1			Terzo assente
  *		2			Terzo non valido alla data registrazione
  *		3			Controllo se ho inserito le modalità di pagamento
  *		4			Tipo rapporto assente
  *		5			Tipo di rapporto non valido in data inizio competenza coge
  *		6			Tipo trattamento assente
  *		7			Tipo trattamento non valido alla data registrazione
  *
  * Pre-post-conditions
  *
  * Nome: Terzo assente
  *	Pre: Non è stato selezionato un terzo
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  * 		"Inserire il terzo"
  *
  * Nome: Terzo non valido alla data registrazione
  *	Pre: Il terzo selezionato non è valido alla data registrazione
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Terzo selezionato non è valido in Data Registrazione"
  *
  * Nome: Modalita di pagamento assente
  *	Pre: Non è stato selezionata una modalita di pagamento
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare la Modalità di pagamento"
  *
  * Nome: Tipo rapporto assente
  *	Pre: Non è stato selezionato un tipo rapporto
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare il Tipo Rapporto"
  *
  * Nome: Tipo rapporto non valido alla data inizio competenza coge
  *	Pre: Il tipo rapporto selezionato non è valido in data competenza coge
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Tipo Rapporto selezionato non è valido alla Data Inizio Competenza"
  *
  * Nome: Tipo trattamento assente
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare il Tipo Trattamento"
  *
  * Nome: Tipo trattamento non valido alla data registrazione
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Il Tipo Trattamento selezionato non è valido alla Data Registrazione"
  *
  * Nome: Terzo valido
  *	Pre: Il terzo selezionato non ha errori
  *	Post: Il terzo è valido e prosegue con l'operazione
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	compenso		il compenso di cui validare il terzo
  *
 **/
public abstract void validaTerzo(UserContext userContext, CompensoBulk compenso) throws ComponentException;
/**
  * Viene richiesta la validazione del terzo selezionato
  *	Ritorna il codice di Errore relativo alla validzione
  *
  *	errorCode		Significato
  *	=========		===========	
  *		0			Tutto bene
  *		1			Terzo assente
  *		2			Terzo non valido alla data registrazione
  *		3			Controllo se ho inserito le modalità di pagamento
  *		4			Tipo rapporto assente
  *		5			Tipo di rapporto non valido in data inizio competenza coge
  *		6			Tipo trattamento assente
  *		7			Tipo trattamento non valido alla data registrazione
  *
  * Pre-post-conditions
  *
  * Nome: Terzo assente
  *	Pre: Non è stato selezionato un terzo
  *	Post: Ritorna il valore 1
  *
  * Nome: Terzo non valido alla data registrazione
  *	Pre: Il terzo selezionato non è valido alla data registrazione
  *	Post: Ritorna il valore 2
  *
  * Nome: Modalita di pagamento assente
  *	Pre: Non è stato selezionata una modalita di pagamento
  *	Post: Ritorna il valore 3
  *
  * Nome: Tipo rapporto assente
  *	Pre: Non è stato selezionato un tipo rapporto
  *	Post: Ritorna il valore 4
  *
  * Nome: Tipo rapporto non valido alla data inizio competenza coge
  *	Pre: Il tipo rapporto selezionato non è valido in data competenza coge
  *	Post: Ritorna il valore 5
  *
  * Nome: Tipo trattamento assente
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna il valore 6
  *
  * Nome: Tipo trattamento non valido alla data registrazione
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna il valore 7
  *
  * Nome: Terzo valido
  *	Pre: Il terzo selezionato non ha errori
  *	Post: Ritorna il valore 0
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	compenso		il compenso di cui validare il terzo
  * @return	il codice di errore relativo
  *
 **/
public abstract int validaTerzo(UserContext userContext, CompensoBulk compenso, boolean checkModPag) throws ComponentException;
}
