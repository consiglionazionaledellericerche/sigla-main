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

import java.io.Serializable;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10.18.19)
 * @author: Roberto Fantino
 */
public class TrattamentoCORIComponent extends CRUDComponent implements ITrattamentoCORIMgr,Cloneable,Serializable{
/**
 * TrattamentoCORIComponent constructor comment.
 */
public TrattamentoCORIComponent() {
	super();
}
private void checkTrattamentiPrecedenti(UserContext userContext, Trattamento_coriBulk trattCORI) throws ComponentException, it.cnr.jada.persistency.PersistencyException{
	
	// Controllo data inizio validita
	Trattamento_coriHome home = (Trattamento_coriHome)getHome(userContext, Trattamento_coriBulk.class);

	java.sql.Timestamp maxData = (java.sql.Timestamp)home.findMax(trattCORI,"dt_inizio_validita");
	if (maxData!=null){
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(maxData);
		cal.set(cal.HOUR_OF_DAY,0);
		cal.set(cal.MINUTE,0);
		cal.set(cal.SECOND,0);
		cal.set(cal.MILLISECOND,0);

		maxData = new java.sql.Timestamp(cal.getTime().getTime());
			if (trattCORI.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna(home.getServerTimestamp()))<=0)
			throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita deve essere superiore alla data odierna");
			if (trattCORI.getDt_inizio_validita().compareTo(maxData)<=0){
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(maxData));
		}

		Trattamento_coriBulk trattPrec = (Trattamento_coriBulk)home.findByPrimaryKey(new Trattamento_coriBulk(trattCORI.getCd_contributo_ritenuta(),trattCORI.getCd_trattamento(),maxData), true);
		trattPrec.setDt_fine_validita(CompensoBulk.decrementaData(trattCORI.getDt_inizio_validita()));
		updateBulk(userContext,trattPrec);
		trattCORI.setId_riga(trattPrec.getId_riga());
	}
}
/** 
 *	Completamento Trattamento CORI
 *	PreCondition:
 *		Viene richiesto di completare il trattamento CO/RI selezionato
 *	PostCondition:
 *		Viene restituito il trattamento CO/RI selezionato con i dati completi
 *			- tipo trattamento valido in data odierna
 *         - tipo contributo/ritenuta valido in data odierna
 *         - la lista di tutti i trattamenti CO/RI con lo stesso tipo trattamento
 *
**/
private void completaTrattamentoCori(UserContext userContext, Trattamento_coriBulk trattCORI) throws ComponentException{

	try{
		Trattamento_coriHome trattCORIHome = (Trattamento_coriHome)getHome(userContext, trattCORI);
		trattCORIHome.completaTrattamentoCori(trattCORI);

		trattCORI.setRighe(new it.cnr.jada.bulk.BulkList(loadRighe(userContext, trattCORI)));

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 * Esegue una operazione di creazione di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Validazione NON superata: campi obbligatori non riempiti
 * Pre: Viene richiesto l'inserimento di un cori non valido (campi obbligatori non riempiti)
 * Post: L'oggetto non viene inserito
 *		  Viene inviato il messaggio corrispondente all'campo vuoto
 *
 * Nome: Validazione NON superata: sovrapposizione con cori precedenti
 * Pre: Viene richiesto l'inserimento di un cori non valido (sovrapposizione con cori precedenti)
 * Post: L'oggetto non viene inserito
 *		  Viene inviato il messaggio corrispondente
 *
 * Nome: Tutte le validazioni precedenti superate
 * Pre:  E' stata richiesta la creazione di un cori che supera tutte le validazioni
 * Post: Viene consentito l'inserimento
 *
 * @param 	userContext	lo UserContext che ha generato la richiesta
 * @param 	bulk		OggettoBulk che deve essere creato
 * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
 *
 * Metodo di validazione:
 *		validaTrattamento(userContext, oggettoBulk)
 *
**/	
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	try{

		Trattamento_coriBulk trattCori = (Trattamento_coriBulk)bulk;
		Trattamento_coriHome home = (Trattamento_coriHome)getHome(userContext, trattCori);

		trattCori.setId_riga(new java.text.DecimalFormat("000").format(home.getNextPgRiga(trattCori)));
		validaTrattamento(userContext, trattCori);
		checkTrattamentiPrecedenti(userContext, trattCori);

		return super.creaConBulk(userContext, trattCori);

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 * Ricerca righe associate al Tipo Trattamento
 *
 * Pre-post-conditions
 *
 * Nome: Ricerca intervalli associati al tipo trattamento selezionato
 * Pre: Viene richiesta la lista delle righe del Trattamento CO/RI
 *		che hanno il Tipo Trattamento selezionato
 * Post: Viene inserita nel Trattamento CO/RI <trattCORI> la lista
 *       di tutti i Trattamenti CO/RI con lo stesso Tipo Trattamento
 *
*/
public Trattamento_coriBulk fillAllRows(UserContext userContext, Trattamento_coriBulk trattCORI) throws ComponentException{

	trattCORI.setRighe(new it.cnr.jada.bulk.BulkList(loadRighe(userContext, trattCORI)));
	return trattCORI;
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 * Pre-post-conditions:
 *
 * Oggetto non esistente
 * Pre: L'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore.
 *
 * Tutti i controlli superati
 * Pre: L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 *
 * Metodo privato chiamato: completaTrattamentoCori()
 *
*/	
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	Trattamento_coriBulk trattCORI = (Trattamento_coriBulk)super.inizializzaBulkPerModifica(userContext, bulk);
	completaTrattamentoCori(userContext, trattCORI);

	return trattCORI;
}
/**
 *	Vengono caricati tutti i Trattamenti CO/RI con
 *  lo stesso Tipo Trattamento del Trattamento CO/RI
 *	selezionato <trattCORI>
 *
 *	Parametri:
 *		- trattCORI
 *
**/

private java.util.List loadRighe(UserContext userContext, Trattamento_coriBulk trattCORI) throws ComponentException{

	try{
		Trattamento_coriHome home = (Trattamento_coriHome)getHome(userContext, Trattamento_coriBulk.class);
		return home.findAllRowsForTrattamento(trattCORI);

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 * Richiesta di modifica di un Trattamento Cori
 *
 * Pre-post-conditions
 *
 *
 * Nome: Validazione NON superata: campi obbligatori non riempiti
 * Pre: Viene richiesto l'aggiornamento di un cori non valido (campi obbligatori non riempiti)
 * Post: L'oggetto non viene inserito
 *		  Viene inviato il messaggio corrispondente all'campo vuoto
 *
 * Nome: Tutte le validazioni precedenti superate
 * Pre:  E' stata richiesta la modifica di un trattamento che supera tutte le validazioni
 * Post: Viene consentito l'aggiornamento
 *
**/
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	validaTrattamento(userContext, (Trattamento_coriBulk)bulk);
	return super.modificaConBulk(userContext, bulk);
}
/**
  * Recupero solo i tipo contributo ritenuta validi ad oggi
  *
**/
public SQLBuilder selectTipoContributoRitenutaByClause(UserContext userContext, Trattamento_coriBulk trattCORI, Tipo_contributo_ritenutaBulk cori, CompoundFindClause clauses) throws ComponentException {

	try{
		Tipo_contributo_ritenutaHome coriHome = (Tipo_contributo_ritenutaHome)getHome(userContext, Tipo_contributo_ritenutaBulk.class);
		SQLBuilder sql = coriHome.createSQLBuilder();
		coriHome.addClauseValidita(sql, coriHome.getServerDate());

		sql.addClause(clauses);
	
		return sql;
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  * Recupero solo i tipi trattamento validi ad oggi
  *
**/
public SQLBuilder selectTipoTrattamentoByClause(UserContext userContext, Trattamento_coriBulk trattCORI, Tipo_trattamentoBulk tratt, CompoundFindClause clauses) throws ComponentException {

	Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
	SQLBuilder sql = trattHome.createSQLBuilder();
	
	try{trattHome.addClauseValidita(sql, trattHome.getServerDate());}
	catch(it.cnr.jada.persistency.PersistencyException ex){throw handleException(ex);}

	sql.addClause(clauses);

	return sql;
}
private void validaTrattamento(UserContext userContext, Trattamento_coriBulk trattCori) throws ComponentException {

	// Valido il trattamento
	try{
		Trattamento_coriHome home = (Trattamento_coriHome)getHome(userContext, trattCori);
		home.validaTrattamento(trattCori);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
}
