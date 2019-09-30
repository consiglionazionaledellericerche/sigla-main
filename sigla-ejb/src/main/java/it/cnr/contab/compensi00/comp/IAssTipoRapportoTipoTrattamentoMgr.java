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

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (05/03/2002 13.31.33)
 * @author: Roberto Fantino
 */
public interface IAssTipoRapportoTipoTrattamentoMgr extends ICRUDMgr{
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
 *		completaAssociazione(userContext, ass)
 *
 *
*/
public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Tipo Trattamento
 *
 * Pre-post-conditions
 *
 * Nome: Richiesta di ricerca di un Tipo Trattamento
 * Pre: E' stata generata la richiesta di ricerca di un Tipo rattamento
 * Post: Viene restituito l'SQLBuilder per filtrare i Tipi Trattamento
 *		  validi in data odierna
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param ass			l'OggettoBulk che rappresenta il contesto della ricerca.
 * @param tratt		l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
 *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
 * @param clauses		L'albero logico delle clausole da applicare alla ricerca
 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
 *			della query.
 *
**/
public abstract SQLBuilder selectTipoTrattamentoByClause(UserContext userContext, Ass_ti_rapp_ti_trattBulk ass, Tipo_trattamentoBulk tratt, CompoundFindClause clauses) throws ComponentException;
}
