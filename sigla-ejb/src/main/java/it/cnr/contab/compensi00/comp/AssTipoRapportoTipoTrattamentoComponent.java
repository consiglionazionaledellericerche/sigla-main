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
import java.io.Serializable;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (05/03/2002 13.31.11)
 * @author: Roberto Fantino
 */
public class AssTipoRapportoTipoTrattamentoComponent extends CRUDComponent  implements IAssTipoRapportoTipoTrattamentoMgr,Cloneable,Serializable{
/**
 * AssTipoRapportoTipoTrattamentoComponent constructor comment.
 */
public AssTipoRapportoTipoTrattamentoComponent() {
	super();
}
/**
 * Completamento dell'Associazione
 *
 * Pre-post-conditions
 *
 * Nome: Completamento Associazione
 * Pre: Viene richiesto il completamento dell'Associazione 
 * Post: Viene caricato il Tipo Trattamento dell'Associazione
 *
 * @param	userContext Lo UserContext che ha generato la richiesta
 * @param	ass			L'Associazione da completare
 *
**/
private void completaAssociazione(UserContext userContext, Ass_ti_rapp_ti_trattBulk ass) throws ComponentException {

	loadTipoTrattamento(userContext, ass);
}
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
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	Ass_ti_rapp_ti_trattBulk ass = (Ass_ti_rapp_ti_trattBulk)super.inizializzaBulkPerModifica(userContext,bulk);
	completaAssociazione(userContext, ass);

	return ass;
}
/**
 * Caricamento del Tipo Trattamento
 *
 * Pre-post-conditions
 *
 * Nome: Tipo Trattamento esistente e valido in data odierna
 * Pre: Viene richiesto il caricamento del Tipo Trattamento
 * Post: Viene caricato il Tipo Trattamento e inserito nell'Associazione
 *
 * Nome: Tipo Trattamento esistente ma NON valido in data odierna 
 * Pre: Viene richiesto il caricamento del Tipo Trattamento
 * Post: Viene caricato il Tipo Trattamento e inserito nell'Associazione
 *
 * Nome: Tipo Trattamento INESISTENTE (perchè cancellato fisicamente)
 * Pre: Viene richiesto il caricamento del Tipo Trattamento
 * Post: Viene generata un'eccezione con la descrizione dell'errore
 *			- Il Tipo Trattamento \"" + ass.getCd_trattamento() + "\" non esiste -
 *
 * @param	userContext Lo UserContext che ha generato la richiesta
 * @param	ass			L'Associazione da completare
 *
**/
private void loadTipoTrattamento(UserContext userContext, Ass_ti_rapp_ti_trattBulk ass) throws ComponentException {

	try{
		Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoTrattamento(ass.getCd_trattamento());
		filtro.setDataValidita(trattHome.getServerDate());
		Tipo_trattamentoBulk tratt = trattHome.findTipoTrattamentoValido(filtro);

		// se il tipo trattamento selezionato non è più valido
		// carico il tipo trattamento senza clausola di validita
		if (tratt==null){
			filtro.setDataValidita(null);
			tratt = trattHome.findTipoTrattamentoValido(filtro);
		}
		if (tratt==null)
			throw new ApplicationException("Il Tipo Trattamento \"" + ass.getCd_trattamento() + "\" non esiste");

		ass.setTipoTrattamento(tratt);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}

}
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
public SQLBuilder selectTipoTrattamentoByClause(UserContext userContext, Ass_ti_rapp_ti_trattBulk ass, Tipo_trattamentoBulk tratt, CompoundFindClause clauses) throws ComponentException
{
	Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);

	SQLBuilder sql = trattHome.createSQLBuilder();

	try{trattHome.addClauseValidita(sql, trattHome.getServerDate());}
	catch (it.cnr.jada.persistency.PersistencyException e){throw handleException(e);}

	sql.addClause(clauses);
	return sql;
}
}
