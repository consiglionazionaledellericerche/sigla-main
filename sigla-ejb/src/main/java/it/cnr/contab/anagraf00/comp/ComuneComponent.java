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

package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (31/10/2002 12.16.51)
 * @author: CNRADM
 */
public class ComuneComponent extends it.cnr.jada.comp.CRUDComponent implements IComuneMgr {
/**
 * ComuneComponent constructor comment.
 */
public ComuneComponent() {
	super();
}
/**
 * Recupera un nuovo progressivo da assegnare in automatico al comune
 * 
 * @param userContext	lo UserContext che ha generato la richiesta
 * @params comune		l'OggettoBulk in cui assegnare il nuovo progressivo
 *
**/	
private void assegnaProgressivo(UserContext userContext, ComuneBulk comune) throws ComponentException{

	try{

		ComuneHome home = (ComuneHome)getHome(userContext, ComuneBulk.class);
		comune.setPg_comune(home.findNuovoProgressivo(comune));

	}catch(BusyResourceException ex){
		throw handleException(ex);
	}catch(ValidationException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 * Esegue una operazione di creazione di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Comune Italiano: Validazione CAP NON superata
 * Pre:  Viene richiesto l'inserimento di un comune italiano con codice CAP inesistente
 * Post: Viene impedito l'inserimento ed emesso un messaggio di errore
 *
 * Nome: Comune Italiano: Validazione CAP superata
 * Pre:  Viene richiesto l'inserimento di un comune italiano con codice CAP esistente
 * Post: Viene assegnato un nuovo progressivo e consentito l'inserimento
 *
 * Nome: Comune estero: Creazione
 * Pre:  E' stato richiesto l'inserimento di un nuovo comune estero
 * Post: Viene assegnato un nuovo progressivo e consentito l'inserimento
 *
 * @param userContext	lo UserContext che ha generato la richiesta
 * @params bulk			l'OggettoBulk da salvare
 * @return L'OggettoBulk creato
 *
**/	
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	ComuneBulk comune = (ComuneBulk)bulk;
	assegnaProgressivo(userContext, comune);
	validaComune(userContext, comune);

	return super.creaConBulk(userContext, comune);
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
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */	
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	try{
		ComuneBulk comune = (ComuneBulk)super.inizializzaBulkPerInserimento(userContext, bulk);

		if (comune.isItaliano()){
			NazioneHome home = (NazioneHome)getHome(userContext, NazioneBulk.class);
			comune.setNazione(home.findNazione(NazioneBulk.ITALIA));
		}
		
		return comune;

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}
}
/**
 * Esegue una operazione di modifica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Comune Italiano: Validazione CAP NON superata
 * Pre:  Viene richiesta la modifica di un comune italiano con codice CAP inesistente
 * Post: Viene impedita la modifica ed emesso un messaggio di errore
 *
 * Nome: Comune Italiano: Validazione CAP superata
 * Pre:  Viene richiesta la modifica di un comune italiano con codice CAP esistente
 * Post: Viene consentita la modifica del comune estero
 *
 * Nome: Comune estero: Modifica
 * Pre:  E' stato richiesto l'inserimento di un nuovo comune estero
 * Post: Viene consentita la modifica del comune estero
 *
 * @param userContext	lo UserContext che ha generato la richiesta
 * @params bulk			l'OggettoBulk da salvare
 * @return L'OggettoBulk creato
 *
**/	
public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	validaComune(userContext, (ComuneBulk)bulk);
	return super.modificaConBulk(userContext, bulk);
}
/**
 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Nazione
 *
 * Pre-post-conditions
 *
 * Nome: Richiesta di ricerca di una Nazione
 * Pre: E' stata generata la richiesta di ricerca di una Nazione
 * Post: Viene restituito l'SQLBuilder per filtrare le Nazioni
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param scaglione		l'OggettoBulk che rappresenta il contesto della ricerca.
 * @param provincia		l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
 *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
 * @param clauses		L'albero logico delle clausole da applicare alla ricerca
 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
 *
**/
public SQLBuilder selectNazioneByClause(UserContext userContext, ComuneBulk comune, NazioneBulk nazione, CompoundFindClause clauses) throws ComponentException {

	NazioneHome home = (NazioneHome)getHome(userContext, nazione);

	SQLBuilder sql = home.createSQLBuilder();
	if (comune.isEstero()){
		sql.addClause("AND","ti_nazione",sql.NOT_EQUALS,nazione.INDIFFERENTE);
		sql.addClause("AND","ti_nazione",sql.NOT_EQUALS,nazione.ITALIA);
	}else
		sql.addClause("AND","ti_nazione",sql.EQUALS,nazione.ITALIA);
	sql.addClause(clauses);

	return sql;
}
private void validaComune(UserContext userContext, ComuneBulk comune) throws ComponentException{
	
	if (comune.isItaliano())
		validaComuneItaliano(userContext, comune);
	else
		validaComuneEstero(userContext, comune);
}
private void validaComuneEstero(UserContext userContext, ComuneBulk comune) throws ComponentException{
}
private void validaComuneItaliano(UserContext userContext, ComuneBulk comune) throws ComponentException{
}
@Override
public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
	try {
		super.eliminaConBulk(usercontext,oggettobulk);
	} catch (Throwable e) {
		ComuneBulk comune=(ComuneBulk)oggettobulk;
		java.sql.Timestamp dataSistema = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		comune.setDt_canc(dataSistema);
		try{
		updateBulk(usercontext, comune);
		} catch (Throwable e1) {
			handleException(e1);
}
		
	}
}
}
