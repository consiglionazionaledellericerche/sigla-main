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

package it.cnr.contab.doccont00.comp;

import java.io.Serializable;
import it.cnr.contab.doccont00.ordine.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import java.sql.*;
import it.cnr.contab.doccont00.intcass.bulk.Stampa_vpg_situazione_cassaVBulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (01/02/2002 11.43.58)
 * @author: Roberto Fantino
 */
public class OrdineComponent
	extends CRUDComponent
	implements IOrdineMgr,Cloneable,Serializable, IPrintMgr{
/**
 * OrdineComponent constructor comment.
 */
public OrdineComponent() {
	super();
}
/**
 *
 * Viene richiesto il completamento dell'ordine con i dati relativi al terzo
 *
 * Pre-post-conditions
 *
 * Nome: Completamento ordine da terzo
 * Pre: Vengono richiesti i Termini e le Modalità di Pagamento del Terzo selezionato
 * Post: Viene restituito l'ordine con i Termini e le Modalità di Pagamento associati
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ordine l'OggettoBulk da completare
 * @return	l'OggettoBulk completo
 *
**/
public OrdineBulk completaTerzo(UserContext userContext,OrdineBulk ordine) throws ComponentException {

	try {

		TerzoHome home = (TerzoHome)getHome(userContext, ordine.getTerzo());
		ordine.setTermini(home.findRif_termini_pagamento(ordine.getTerzo()));
		ordine.setModalita(home.findRif_modalita_pagamento(ordine.getTerzo()));

		return ordine;

	}catch (Throwable ex){
		throw handleException(ex);
	}

}
/**
 *
 * Viene richeista l'eliminazione dell'Ordine
 *
 * Pre-post-conditions:
 *
 * Nome: Validazione superata
 * Pre: L'ordine NON è stato stampato in modo definitivo
 * Post: Viene consentita l'eliminazione dell'ordine
 *
 * Nome: Validazione NON superata
 * Pre: L'ordine è stato stampato in modo definitivo
 * Post: Viene generata una ApplicationException con il messaggio:
 *		 "L'Ordine è già stato stampato. Non è possibile eliminarlo!"
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk l'OggettoBulk da eliminare
 * @return	void
 *
 * Metodo privato chiamato per la validazione:
 *		validaOrdineSuElimina(OrdineBulk ordine);
 *
**/
public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if(!validaOrdineSuElimina((OrdineBulk)bulk))
		throw new ApplicationException( "L'Ordine è già stato stampato. Non è possibile eliminarlo!");

	super.eliminaConBulk(userContext,bulk);

}
/**
 *
 * Viene richiesta la lista delle Banche associate ad un Terzo
 *
 * Pre-post-conditions:
 *
 * Nome: Terzo NON selezionato
 * Pre: Non è stato selezionato un Terzo per l'ordine
 * Post: Non vengono caricate le banche.
 *
 * Nome: Terzo selezionato
 * Pre: E' stato selezionato un Terzo valido per l'ordine 
 * Post: Viene restituita la lista delle Banche associate al Terzo
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	ordine l'OggettoBulk da completare
 * @return	La lista delle banche associate al terzo
 *
**/
public java.util.List findListabanche(UserContext userContext, OrdineBulk ordine) throws ComponentException{

	try {
		if(ordine.getTerzo() == null) 
			return null;

		return getHome(userContext, BancaBulk.class).fetchAll(selectBancaByClause(userContext, ordine, null, null));
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
protected Timestamp getData_registrazione(UserContext userContext,OrdineBulk ordine) throws ComponentException 
{
	try
	{
		OrdineBulk ordineDaDB = (OrdineBulk) getHome( userContext, OrdineBulk.class ).findByPrimaryKey( ordine );
		return ordineDaDB.getDt_registrazione();

	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException( e );
	}		

}	
/**
 * Recupera il la data corrente dal server db
 *
 * @return La data corrente (solo data) fornita dal db
 * @throws PersistencyException Se si verifica qualche eccezione SQL
 */
private Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {

	try
	{
		return getHome(userContext, OrdineBulk.class).getServerDate();
	}
	catch(it.cnr.jada.persistency.PersistencyException ex)
	{
		throw handleException(ex);
	}
}
/**
 *
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di creazione.
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: E' stata richiesta l'inizializzazione di una istanza di OrdineBulk per inserimento
 * Post: L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
 *		 per una operazione di creazione
 *		 In particolare:
 *			- Data registrazione = data odierna
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bulk <code>OggettoBulk</code> l'istanza di Ordine bulk da inizializzare
 * @return <code>OggettoBulk</code> l'istanza di OrdineBulk inizializzata
 *
**/
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	OrdineBulk ordine = (OrdineBulk)super.inizializzaBulkPerInserimento(userContext, bulk);
/*
	java.util.Calendar gc = java.util.Calendar.getInstance();
	gc.set(java.util.Calendar.HOUR, 0);
	gc.set(java.util.Calendar.MINUTE, 0);
	gc.set(java.util.Calendar.SECOND, 0);
	gc.set(java.util.Calendar.MILLISECOND, 0);
	gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	java.sql.Timestamp dataOdierna = new java.sql.Timestamp(gc.getTime().getTime());

	ordine.setDt_registrazione(dataOdierna);
*/
	try
	{
		ordine.setDt_registrazione(DateServices.getDt_valida( userContext));
	}
	catch ( javax.ejb.EJBException e )
	{
		throw handleException( e );
	}		

	return ordine;
}
/**
 *
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
 * Pre:	L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 *
 *			In particolare vengono caricate tutte le Righe associate all'Ordine selezionato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 *
**/	
public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) throws ComponentException {

	try {
		OrdineBulk ordine = (OrdineBulk)super.inizializzaBulkPerModifica(userContext,bulk);

		Ordine_dettHome ordDettHome = (Ordine_dettHome)getHome(userContext,Ordine_dettBulk.class);
		ordine.setDettagli(new BulkList(ordDettHome.findDetailsFor(ordine)));

		//return ordine;

		return completaTerzo(userContext, ordine);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_vpg_situazione_cassaVBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	
	stampa.setDataInizio(DateServices.getFirstDayOfYear(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));

	stampa.setTi_stato(Stampa_vpg_situazione_cassaVBulk.STATO_INVIATO);
	
	try{
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setUoForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setUoForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_ordineBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setObbligazione(new ObbligazioneBulk());
	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));

	stampa.setPgInizio(new Integer(0));
	stampa.setPgFine(new Integer(999999999));
	
	try{
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setIsUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		}
			
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_ordineBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_ordineBulk)bulk);
	else if (bulk instanceof it.cnr.contab.doccont00.intcass.bulk.Stampa_vpg_situazione_cassaVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_vpg_situazione_cassaVBulk)bulk);
		
	return bulk;
}
/**
 *
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sull'Ordine
 *	
 * Pre-post-conditions:
 *
 * Nome: Richiesta di ricerca di un ordine
 * Pre:  E' stata generata la richiesta di ricerca di un ordine
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'ordine abbia esercizio di creazione uguale a quello di scrivania
 *       e CDS di origine uguale a quella di scrivania
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @param bulk istanza di OrdineBulk che deve essere utilizzata per la ricerca
 * @return il SQLBuilder con le clausole aggiuntive
 *
**/
public it.cnr.jada.persistency.sql.Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	SQLBuilder sql = (SQLBuilder)super.select(userContext, clauses, bulk);
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

	sql.addClause(clauses);
	return sql;

}
/**
 *
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Banche
 *
 * Nome: Richiesta di ricerca di una banca
 * Pre: E' stata generata la richiesta di ricerca delle banche
 * Post: Viene restituito l'SQLBuilder per filtrare le banche NON CANCELLATE,
 *		 associate al TERZO selezionato e al TIPO PAGAMENTO selezionato
 *
**/
public SQLBuilder selectBancaByClause(UserContext userContext, OrdineBulk ordine, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {

	BancaHome bancaHome = (BancaHome)getHome(userContext, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
	return bancaHome.selectBancaFor(ordine.getModalitaPagamento(), ordine.getCd_terzo());
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa situazione di cassa
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_vpg_situazione_cassaVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	sql.addClause(clauses);
	return sql;
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa di un Ordine
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_ordineBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	sql.addClause(clauses);
	return sql;

}
/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	
	if (bulk instanceof Stampa_ordineBulk)
		validateBulkForPrint(aUC, (Stampa_ordineBulk)bulk);
	else if (bulk instanceof Stampa_vpg_situazione_cassaVBulk)
		validateBulkForPrint(aUC, (Stampa_vpg_situazione_cassaVBulk)bulk);

	return bulk;
}
protected void validaCreaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	try
	{
		super.validaCreaModificaConBulk( userContext, bulk );
		Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio().intValue());
		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		Timestamp dataDaDB = null;

		if ( bulk.isToBeUpdated() )
			dataDaDB = getData_registrazione( userContext, (OrdineBulk) bulk);

		if (bulk.isToBeCreated() || (bulk.isToBeUpdated() && dataDaDB.compareTo( ((OrdineBulk)bulk).getDt_registrazione()) != 0 ))
			if ( today.after(lastDayOfTheYear ) &&
				  ((OrdineBulk)bulk).getDt_registrazione().compareTo( lastDayOfTheYear) != 0 )
						throw  new ApplicationException( "La data di registrazione deve essere " +
		   									java.text.DateFormat.getDateInstance().format( lastDayOfTheYear ));					

	}
	catch ( javax.ejb.EJBException e )
	{
		throw handleException( e );
	}		

}	
/**
 *
 *	Ritorna VERO se l'ordine <ordine> è stato stampato in modo definitivo;
 *	FALSO altrimenti
 *
 * @param	ordine l'Ordine da validare
 * @return	TRUE se l'ordine è eliminabile, FALSE altrimenti
 *
**/
private boolean validaOrdineSuElimina(OrdineBulk ordine) {

	return !ordine.isStampato();
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_vpg_situazione_cassaVBulk stampa) throws ComponentException{

	try{
		Timestamp dataOdierna = getDataOdierna(userContext);
		java.sql.Timestamp lastDayOfYear = DateServices.getLastDayOfYear(stampa.getEsercizio().intValue());
		if (stampa.getEsercizio()==null)
			throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		if (stampa.getCd_cds()==null)
			throw new ValidationException("Il campo CDS e' obbligatorio");

		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

		java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
		if (stampa.getDataInizio().compareTo(firstDayOfYear)<0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
		}
		if (stampa.getDataFine().compareTo(lastDayOfYear)>0 && lastDayOfYear.compareTo(dataOdierna)>0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
		}

	
	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_ordineBulk stampa) throws ComponentException{

	try{	
		/**** Controlli sui PG_INIZIO/PG_FINE *****/
		if (stampa.getPgInizio()==null)
			throw new ValidationException("Il campo NUMERO INIZIO è obbligatorio");
		if (stampa.getPgFine()==null)
			throw new ValidationException("Il campo NUMERO FINE è obbligatorio");
		if (stampa.getPgInizio().compareTo(stampa.getPgFine())>0)
			throw new ValidationException("Il NUMERO INIZIO non può essere superiore al NUMERO FINE");
		if (stampa.getUoForPrint().getCd_unita_organizzativa()==null)
			throw new ValidationException("Il campo Unità Organizzativa è obbligatorio");
		
	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
}
