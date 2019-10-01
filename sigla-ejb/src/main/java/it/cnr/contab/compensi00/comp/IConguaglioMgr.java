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

import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (08/07/2002 16.58.07)
 * @author: Roberto Fantino
 */
public interface IConguaglioMgr extends it.cnr.jada.comp.ICRUDMgr {
/**
  * Completamento dell'OggettoBulk <conguaglio> aggiornando i campi 
  * relativi al terzo selezionato <vTerzo>
  *
  * Pre-post-conditions
  *
  * Nome: Completamento del conguaglio
  * Pre: Viene richiesto il completamento del conguaglio
  * Post: Viene restituito il conguaglio completo di tutti i dati 
  *		  relativi al terzo selezionato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da completare
  * @param	vTerzo terzo con cui completare il conguaglio
  * @return l'OggettoBulk completo
  *
  * Metodo privato chiamato:
  *		completaTerzo(UserContext userContext, ConguaglioBulk conguaglio);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
  * Esegue una operazione di creazione di un OggettoBulk.
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la creazione di una istanza di ConguaglioBulk che supera la validazione
  * Post: Consente l'inserimento del conguaglio assegnandogli un progressivo definitivo e cancellando il
  *		  conguaglio con progressivo temporaneo precedentemente creato
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la creazione di una istanza di ConguaglioBulk che NON supera la validazione
  * Post: Viene generata una eccezione con la descrizione dell'errore
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk il conguaglio che deve essere creato
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo privato chiamato:
  *		validaConguaglio(ConguaglioBulk conguaglio)
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta l'esecuzione della procedura Oracle CNRCTB650.ABILITACONGUAGLIO
  *	per abilitare il conguaglio alla creazione del compenso
  *
  * Pre-post-conditions:
  *
  * Nome: Validazione superata
  * Pre: Viene richiesta l'abilitazione del conguaglio per la creazione del compenso
  * Post: Viene assegnato (se assente) un nuovo progressivo temporaneo al conguaglio 
  *		  ed eseguita la procedura oracle per l'abiltazione del conguaglio
  *
  * Nome: Validazione NON superata
  * Pre: Viene richiesta l'abilitazione del conguaglio per la creazione del compenso
  * Post: Viene sollevata una ValidationExecption con la descrizione dell'errore
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio il conguaglio da abilitare
  * @return	il conguaglio aggiornato dopo l'esecuzione della procedura oracle
  *
  * Metodi privati chiamati:
  *		validaConguaglioPerCalcolo(ConguaglioBulk conguaglio);
  *		abilitaConguaglio(UserContext userContext, ConguaglioBulk conguaglio);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk doAbilitaConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta l'esecuzione della procedura Oracle CNRCTB650.CREACOMPENSOCONGUAGLIO
  *	per completare il conguaglio e creare il compenso corrispondente
  *
  * Pre-post-conditions:
  *
  * Nome: Chiamata procedura oracle
  * Pre: Viene richiesta l'esecuzione della procedura Oracle
  * Post: Viene eseguita la procedura associata e creato il compenso
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio il conguaglio da abilitare
  * @return	il conguaglio aggiornato dopo l'esecuzione della procedura oracle
  *
  * Metodi privati chiamati:
  *		creaCompensoConguaglio(UserContext userContext, ConguaglioBulk conguaglio);
  *		reloadConguaglio(UserContext userContext, ConguaglioBulk conguaglio);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk doCreaCompensoConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la lista delle Banche associate ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il conguaglio
  * Post: Non vengono caricate le banche.
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il conguaglio
  * Post: Viene restituita la lista delle Banche associate al Terzo
  * 	  e alla Modalità di Pagamento selezionata
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da completare
  * @return	La lista delle banche associate al terzo
  *
**/

public abstract java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Viene richiesta la lista delle Modalita di Pagamento associate ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il conguaglio
  * Post: Non vengono caricate le modalita di pagamento
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il conguaglio
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
  * Pre: Non è stato selezionato un Terzo per il conguaglio
  * Post: Non vengono caricati i termini di pagamento
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il conguaglio
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
  * Pre: Non è stato selezionato un Terzo per il conguaglio
  * Post: Non vengono caricati i Tipi di rapporto
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il conguaglio
  * Post: Viene restituita la lista dei Tipi di rapporto associati al Terzo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da completare
  * @return	La lista dei Tipi di rapporto associati al terzo
  *
**/

public abstract java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException;
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
  * Pre: E' stato selezionato un tipo di rapporto valido per il conguaglio
  * Post: Viene restituita la lista dei Tipi di Trattamento
  *		  legati al Tipo di rapporto selezionato
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da completare
  * @return	La lista dei Tipi di Trattamento associati al Tipo Rapporto selezionato
  *
**/

public abstract java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException;
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
 *		completaConguaglio(UserContaxt userContext, ConguaglioBulk conguaglio);
 *
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Verifica se il conguaglio e' stato annullato
  *
  * Pre-post-conditions
  *
  *	Nome: Conguaglio ANNULLATO - Data cancellazione valorizzata
  *	Pre: Il conguaglio è annullato
  *	Post: Ritorna <true>. Il conguaglio è annullato
  *
  *	Nome: Conguaglio NON ANNULLATO - Data cancellazione non valorizzata
  *	Pre: Il conguaglio non è annullato
  *	Post: Ritorna <false>. Il conguaglio non è annullato
  *
  * @param 	userContext 	lo UserContext che ha generato la richiesta
  * @param 	conguaglio 		il conguaglio da controllare  
  * @return TRUE 			e il conguaglio è anullato, FALSE altrimenti
  *
**/
public abstract boolean isConguaglioAnnullato(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException;
/**
  * Viene Richiesto il caricamento di un conguaglio
  *
  * Pre-post_conditions
  *
  * Nome: Caricamento conguaglio
  *	Pre: Viene richiesto il caricamento da db del conguaglio
  *	Post: Viene caricato da database il conguaglio insieme a tutti 
  *		  gli oggetti complessi necessari ad una sua corretta gestione
  *			- terzo
  *			- tipo trattamento
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk il conguaglio che deve essere ri-caricato
  * @return	il conguaglio aggiornato
  *
  * Metodo privato chiamato:
  *		completaConguaglio(UserContext userContext, ConguaglioBulk conguaglio);
  *
**/

public abstract it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk reloadConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException;
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
  * @param	conguaglio		il compenso di cui validare il terzo
  *
 **/
public abstract void validaTerzo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException;
/**
  * Viene richiesta la validazione del terzo selezionato
  *	Ritorna il codice di Errore relativo alla validazione
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
  * @param	conguaglio		il conguaglio di cui validare il terzo
  * @return	il codice di errore relativo
  *
 **/
public int validaTerzo(UserContext userContext, ConguaglioBulk conguaglio, boolean checkModPag) throws ComponentException;
}
