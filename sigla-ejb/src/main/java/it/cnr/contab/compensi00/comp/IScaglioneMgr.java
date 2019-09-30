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

import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public interface IScaglioneMgr extends ICRUDMgr{
/**
 * Esegue una operazione di creazione di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Nessuno scaglione aggiunto alla lista
 * Pre:  E' stato richiesto l'inserimento di uno scaglione che NON è stato aggiunto alla lista
 * Post: Viene generata una eccezione con la descrizione dell'errore
 *			- "Inserire gli scaglioni" -
 *
 * Nome: Validazione scaglione superata
 * Pre:  E' stato richiesto l'inserimento di uno scaglione che supera la validazione
 * Post: Viene completato lo scaglione recuperando i dati di testata e viene
 *		 consentito l'inserimento dello stesso
 *
 * Nome: Validazione NON superata
 * Pre:  E' stato richiesto l'inserimento di uno scaglione che NON supera la validazione
 * Post: Viene generata una eccezione con la descrizione dell'errore
 *
 * @param 	userContext	lo UserContext che ha generato la richiesta
 * @param 	bulk		OggettoBulk il compenso che deve essere creato
 * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
 *
 * Metodo di validzione privato:
 *		validaScaglione(userContext, scaglione)
 *
**/	
public abstract OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException;
/** 
 * Cancellazione Scaglione
 *
 * Pre-post-conditions
 *
 * Nome: Cancellazione FISICA dello scaglione
 * Pre: Viene richiesta la cancellazione di uno scaglione con Data INIZIO Validita
 *		superiore alla data odierna
 * Post: Cancello fisicamente il record in questione
 *
 * Nome: Cancellazione LOGICA dello scaglione
 * Pre: Viene richiesta la cancellazione di uno scaglione con Data INIZIO Validita
 *		inferiore alla data odierna e Data FINE Validita superiore alla data odierna
 * Post: Cancello logicamente lo scaglione selezionato impostanto come data Fine Validita
 *		 la data odierna
**/
public abstract void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;
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
 *		completaScaglione(userContext, scaglione)
 *
 *
*/	
public abstract OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException ;
/**
 * Controllo esistenza altri periodi successivi a quello selezionato
 *
 * Pre-post-conditions
 *
 * Nome: L'intervallo in processo è l'ultimo intervallo esistente
 * Pre: La data di inizio validità dell'intervallo in processo >= della massima data di inizio di intervalli
 * Post: Viene ritornato TRUE
 *
 * Nome: L'intervallo in processo non è l'ultimo intervallo esistente
 * Pre: La data di inizio validità dell'intervallo in processo < della massima data di inizio di intervalli
 * Post: Viene ritornato FALSE
 *
*/
public abstract boolean isUltimoIntervallo(UserContext userContext, ScaglioneBulk scaglione) throws ComponentException;
/**
 * Richiesta di modifica di uno Scaglione
 *
 * Pre-post-conditions
 *
 * Nome: Modifica di intervallo avente Data INIZIO Validita futura
 * Pre: Viene richiesta una modifica di uno scaglione avente la data inizio validita superiore alla data odierna
 * Post: Viene aggiornato lo scaglione in processo
 *
 * Nome: Modifica di uno scaglione avente Data FINE Validita < alla data odierna
 * Pre: Viene richiesta una modifica di uno scaglione avente la data fine validita precedente alla data odierna
 * Post: Viene sollevata un'eccezione
 *
 * Nome: Modifica di intervallo avente la data INIZIO validita <= alla data odierna
 * Pre: Viene richiesta una modifica di uno scaglione avente la data inizio validita precedente alla data odierna
 * Post: La data di fine validità dello scaglione corrente viene posta = data odierna
 *       Viene creato il nuovo scaglione con data di inizio validità = alla data odierna + 1
 *
**/
public abstract OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException ;
/**
 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Comune
 *
 * Pre-post-conditions
 *
 * Nome: Richiesta di ricerca di un Comune
 * Pre: E' stata generata la richiesta di ricerca di un Comune
 * Post: Viene restituito l'SQLBuilder per filtrare i Comuni
 *		appartenenti ad una determinata provincia
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param scaglione		l'OggettoBulk che rappresenta il contesto della ricerca.
 * @param comune		l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
 *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
 * @param clauses		L'albero logico delle clausole da applicare alla ricerca
 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
 *
**/
public abstract SQLBuilder selectComuneByClause(UserContext userContext, ScaglioneBulk scaglione, ComuneBulk comune, CompoundFindClause clauses) throws ComponentException ;
/**
 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Contributo/Ritenuta
 *
 * Pre-post-conditions
 *
 * Nome: Richiesta di ricerca di un Contributo/Ritenuta
 * Pre: E' stata generata la richiesta di ricerca di un Contributo/Ritenuta
 * Post: Viene restituito l'SQLBuilder per filtrare i Tipi Contributo/Ritenuta validi in data odierna
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param scaglione		l'OggettoBulk che rappresenta il contesto della ricerca.
 * @param cori			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
 *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
 * @param clauses		L'albero logico delle clausole da applicare alla ricerca
 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
 *
**/
public abstract SQLBuilder selectContributo_ritenutaByClause(UserContext userContext, ScaglioneBulk scaglione, Tipo_contributo_ritenutaBulk cori, CompoundFindClause clauses) throws ComponentException ;
/**
 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Provincia
 *
 * Pre-post-conditions
 *
 * Nome: Richiesta di ricerca di una Provincia
 * Pre: E' stata generata la richiesta di ricerca di una Provincia
 * Post: Viene restituito l'SQLBuilder per filtrare le Provincie
 *		appartenenti ad una determinata regione
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param scaglione		l'OggettoBulk che rappresenta il contesto della ricerca.
 * @param provincia		l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
 *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
 * @param clauses		L'albero logico delle clausole da applicare alla ricerca
 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
 *
**/
public abstract SQLBuilder selectProvinciaByClause(UserContext userContext, ScaglioneBulk scaglione, ProvinciaBulk provincia, CompoundFindClause clauses) throws ComponentException ;
/**
 * Validazione dello scaglione
 *
 * Pre-post-conditions
 *
 * Nome: Controlli di validazione del periodo di inizio/fine validita' 
 *		 del nuovo record superati
 * Pre: Validazione periodo inizio/fine superata (data inizio validita del nuovo 
 *		record deve essere maggiore del record piu' recente presente in tabella)
 * Post: Consente l'inserimento del record e l'aggiornamento della data di fine 
 *		 validita del penultimo record
 *
 * Nome: Validazioni non superate
 * Pre: Controlli su periodo NON OK
 * Post: Non inserisco l'oggetto
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param bulk			l'OggettoBulk (scaglione) da validare
 *
**/
public abstract void validaScaglione(UserContext userContext,OggettoBulk bulk) throws ComponentException ;
}
