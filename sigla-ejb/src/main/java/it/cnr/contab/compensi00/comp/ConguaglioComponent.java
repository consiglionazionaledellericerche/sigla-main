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

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.util.Utility;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (08/07/2002 16.57.40)
 * @author: Roberto Fantino
 */
public class ConguaglioComponent extends it.cnr.jada.comp.CRUDComponent implements IConguaglioMgr,Cloneable,Serializable{
/**
 * ConguaglioComponent constructor comment.
 */
public ConguaglioComponent() {
	super();
}
/**
  *	Richiama la procedura Oracle CNRCTB650.AbilitaConguaglio
  *	che riempie i dati esterni del conguaglio e abilita per la
  *	creazione del compenso
  *
**/
private void abilitaConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{
	try{
	controlloRiduzioneCuneo32020(userContext, conguaglio);
	}catch(ComponentException ex){
		throw new ApplicationException(ex);
	}
	try{
		LoggableStatement cs = new LoggableStatement(getConnection(userContext), 
				"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB650.AbilitaConguaglio(?,?,?,?)}",false,this.getClass());
		try{
			cs.setObject( 1, conguaglio.getCd_cds()                 );
			cs.setObject( 2, conguaglio.getCd_unita_organizzativa() );
			cs.setObject( 3, conguaglio.getEsercizio()              );
			cs.setObject( 4, conguaglio.getPg_conguaglio()          );

			cs.execute();
		}finally{
			cs.close();
		}
	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}
}
/**
  * Aggiorno il compenso associato al conguaglio
  * assegnandogli un progressivo definitivo e cancellando il
  *	compenso con progressivo temporaneo precedentemente creato
  *		- assegna un progressivo definitivo al compenso
  * 	- aggiorna l'obbligazione associata
  *		- inserisce il compenso
  * 	- inserisce le righe Contributo/Ritenuta
  *		- inserisce le righe Contributo/Ritenuta dettaglio
  *
**/	
private CompensoBulk aggiornaCompenso(UserContext userContext, CompensoBulk compenso) throws ComponentException{

	try{

		CompensoComponentSession session = (CompensoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCOMPENSI00_EJB_CompensoComponentSession", CompensoComponentSession.class);
		return session.inserisciCompenso(userContext, compenso);

	}catch(javax.ejb.EJBException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
/**
  * Viene richiesto un nuovo progressivo per il conguaglio
  *
  * Pre-post-conditions
  *
  * Name: Richiesta nuovo progressivo
  * Pre: Viene richiesto un nuovo progressivo
  * Post: Viene restituito un nuovo progressivo da assegnare al conguaglio
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio il Conguaglio a cui deve essere assegnato il nuovo progressivo
  * @return	il nuovo progressivo da utilizzare
  *
**/
private Long assegnaProgressivo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {

	try {
		// Assegno un nuovo progressivo al conguaglio
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(conguaglio);
		return progressiviSession.getNextPG(userContext, numerazione);

	}catch(javax.ejb.EJBException ex){
		throw handleException(conguaglio, ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(conguaglio, ex);
	}

}
/**
  * Viene richiesto un nuovo progressivo temporaneo per il conguaglio
  *
  * Pre-post-conditions
  *
  * Name: Richiesta nuovo progressivo temporaneo
  * Pre: Viene richiesto un nuovo progressivo temporaneo
  * Post: Viene restituito un nuovo progressivo temporaneo da assegnare al conguaglio
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio il Conguaglio a cui deve essere assegnato il nuovo progressivo
  * @return	il nuovo progressivo temporaneo da utilizzare
  *
**/
private Long assegnaProgressivoTemporaneo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {

	try {
		// Assegno un nuovo progressivo temporaneo al conguaglio
		NumerazioneTempDocAmmComponentSession session = (NumerazioneTempDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_NumerazioneTempDocAmmComponentSession", NumerazioneTempDocAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(conguaglio);
		return session.getNextTempPG(userContext, numerazione);

	}catch(javax.ejb.EJBException ex){
		throw handleException(conguaglio, ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(conguaglio, ex);
	}
}
/**
  * Viene richiesto il completamento dell'OggettoBulk <conguaglio>
  * caricando da db i seguenti oggetti complessi
  *		- terzo
  *		- tipo trattamento
  *
  * Pre-post-conditions
  *
  * Nome: Completamento del conguaglio
  * Pre: Viene richiesto il completamento del conguaglio
  * Post: Viene completato il conguaglio con i seguenti dati:
  *			- terzo
  *			- tipo trattamento
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da completare
  *
  * Metodi privati chiamati:
  *		completaTerzo(UserContext userContext, ConguaglioBulk conguaglio);
  *		loadTipoTrattamento(UserContext userContext, ConguaglioBulk conguaglio);
  *
**/
private void completaConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{

		conguaglio.setV_terzo((V_terzo_per_conguaglioBulk)loadVTerzo(userContext,conguaglio));
		completaTerzo(userContext, conguaglio);
		conguaglio.setTipiTrattamento(findTipiTrattamento(userContext, conguaglio));
		loadTipoTrattamento(userContext, conguaglio);

		getHomeCache(userContext).fetchAll(userContext);

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  * Completamento dell'OggettoBulk <conguaglio> aggiornando i campi 
  * relativi al terzo (nome, cognome, ragSoc, cf, pIva, comune, prov, reg)
  *	e caricando da db i seguenti oggetti complessi
  *		- modalita pagamento
  *		- termini di pagamento
  *		- tipo di rapporto
  *
  * Pre-post-conditions
  *
  * Nome: Completamento del conguaglio
  * Pre: Viene richiesto il completamento del conguaglio
  * Post: Viene completato il conguaglio con i seguenti dati relativi al terzo:
  * 		- nome, cognome, ragSoc, cf, pIva, comune, prov, reg
  *			- modalita pagamento
  *			- termini di pagamento
  *			- tipo di rapporto
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da completare
  * @return l'OggettoBulk completo
  *
**/
private ConguaglioBulk completaTerzo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {

	if (conguaglio != null) {

		V_terzo_per_conguaglioBulk vTerzo = conguaglio.getV_terzo();
		conguaglio.setCd_terzo(vTerzo.getCd_terzo());
		conguaglio.setNome(vTerzo.getNome());
		conguaglio.setCognome(vTerzo.getCognome());
		conguaglio.setRagione_sociale(vTerzo.getRagione_sociale());
		conguaglio.setCodice_fiscale(vTerzo.getCodice_fiscale());
		conguaglio.setPartita_iva(vTerzo.getPartita_iva());
		conguaglio.setPg_comune(vTerzo.getPg_comune_fiscale());
		conguaglio.setCd_provincia(vTerzo.getCd_provincia_fiscale());
		conguaglio.setCd_regione(vTerzo.getCd_regione_fiscale());

		conguaglio.setModalita(findModalita(userContext, conguaglio));
		conguaglio.setTermini(findTermini(userContext, conguaglio));
		conguaglio.setTipiRapporto(findTipiRapporto(userContext, conguaglio));
	}
	return conguaglio;
}
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
public ConguaglioBulk completaTerzo(UserContext userContext, ConguaglioBulk conguaglio, V_terzo_per_compensoBulk vTerzo) throws ComponentException {

	conguaglio.setV_terzo((V_terzo_per_conguaglioBulk)vTerzo);
	return completaTerzo(userContext, conguaglio);
	
}
/**
  *	Richiama la procedura Oracle CNRCTB650.CreaCompensoConguaglio
  *	che crea il compenso a partire dal conguaglio selezionato
  *
**/
private void creaCompensoConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{
		LoggableStatement cs = new LoggableStatement(getConnection(userContext),
				"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB650.CreaCompensoConguaglio(?,?,?,?,?,?,?,?)}",false,this.getClass());
		try{
			cs.setObject( 1, conguaglio.getCd_cds()                 );
			cs.setObject( 2, conguaglio.getCd_unita_organizzativa() );
			cs.setObject( 3, conguaglio.getEsercizio()              );
			cs.setObject( 4, conguaglio.getPg_conguaglio()          );
			cs.setObject( 5, conguaglio.getCd_cds()                 );
			cs.setObject( 6, conguaglio.getCd_unita_organizzativa() );
			cs.setObject( 7, conguaglio.getEsercizio()              );
			cs.setObject( 8, conguaglio.getPgTempCompenso()         );
			cs.execute();
		}finally{
			cs.close();
		}
	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}
}
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
  *		validaConguaglio(UserContext userContext, ConguaglioBulk conguaglio)
  *
**/	
public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	try {
		
		ConguaglioBulk conguaglio = (ConguaglioBulk)bulk;
		validaConguaglio(userContext, conguaglio);

		CompensoBulk compenso = conguaglio.getCompenso();

		Long pgCompensoTemp = compenso.getPg_compenso();
		compenso = aggiornaCompenso(userContext, compenso);
		Long pgCompenso = compenso.getPg_compenso();

		Long pgConguaglioTemp = conguaglio.getPg_conguaglio();
		Long pgConguaglio = assegnaProgressivo(userContext, conguaglio);
		conguaglio.setPg_conguaglio(pgConguaglio);
		conguaglio.setCompenso(compenso);
		insertBulk(userContext, conguaglio);

		upgKeyAssCompensoConguaglio(userContext, conguaglio, pgConguaglioTemp, pgCompensoTemp);
		
		conguaglio.setPg_conguaglio(pgConguaglioTemp);
		deleteBulk(userContext, conguaglio);
		conguaglio.setPg_conguaglio(pgConguaglio);

		compenso.setPg_compenso(pgCompensoTemp);
		deleteBulk(userContext, compenso);
		compenso.setPg_compenso(pgCompenso);

	    if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(conguaglio.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
	          throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");			
		
		return conguaglio;

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}
}
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
public ConguaglioBulk doAbilitaConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{
		validaConguaglioPerCalcolo(userContext, conguaglio);
		Long pgTmp = null;
		
		if (conguaglio.getPg_conguaglio()==null){
			pgTmp = assegnaProgressivoTemporaneo(userContext, conguaglio);
			conguaglio.setPg_conguaglio(pgTmp);
			insertBulk(userContext, conguaglio);
		}else{
			updateBulk(userContext, conguaglio);
		}

		abilitaConguaglio(userContext, conguaglio);
		return reloadConguaglio(userContext, conguaglio);

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(conguaglio,ex);
	}
}
/**
  * Viene richiesta l'esecuzione della procedura Oracle CNRCTB650.CREACOMPENSOCONGUAGLIO
  *	per completare il conguaglio e creare il compenso corrispondente
  *
  * Pre-post-conditions:
  *
  * Nome: Esiste un compenso creato precedentemete
  * Pre: Viene richiesta una seconda esecuzione della procedura di creazione 
  *      del compenso/conguaglio a seguito di una modifica effettuata nei dati
  * Post: Viene aggiornato il conguaglio scollegandolo dal vecchio compenso,
  *       viene cancellato il vecchio compenso, viene creato il nuovo progressivo 
  *       ed eseguita la procedura CNRCTB650.CREACOMPENSOCONGUAGLIO
  *
  * Nome: Non esistono compensi precedentemente creati: prima esecuzione della procedura
  * Pre: Viene richiesta l'esecuzione della procedura Oracle
  * Post: Viene creato un nuovo progressivo per il nuovo compenso ed 
  *       eseguita la procedura CNRCTB650.CREACOMPENSOCONGUAGLIO
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
public ConguaglioBulk doCreaCompensoConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{

		validaCreaCompensoConguaglio(userContext, conguaglio);

		Long oldPgCompenso = conguaglio.getPg_compenso();

		CompensoComponentSession session = (CompensoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCOMPENSI00_EJB_CompensoComponentSession", CompensoComponentSession.class);
		conguaglio.setPgTempCompenso(session.assegnaProgressivoTemporaneo(userContext, new CompensoBulk(conguaglio.getCd_cds(), conguaglio.getCd_unita_organizzativa(), conguaglio.getEsercizio(), null)));

		if (oldPgCompenso!=null)
			conguaglio.setPg_compenso(null);
		updateBulk(userContext, conguaglio);

		if (oldPgCompenso!=null){
			conguaglio.getCompenso().setPg_compenso(oldPgCompenso);
			deleteBulk(userContext, conguaglio.getCompenso());
		}

		creaCompensoConguaglio(userContext, conguaglio);
		return reloadConguaglio(userContext, conguaglio);

	}catch(javax.ejb.EJBException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 * Cancellazione conguaglio
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione conguaglio
 * Pre:  Validazione cancellazione superata
 * Post: Se la procedura ritorna il valore 1 significa che e' stata fatta
 *		 una cancellazione LOGICA del conguaglio. Il sistema deve quindi
 *		 rileggere tale conguaglio essendo stato aggiornato dalla procedura.
 *		 Se invece la procedura ritorna il valore 2 significa che il sistema 
 *		 deve procedere con la cancellazione FISICA del conguaglio.
 *
 * Pre:  Validazione cancellazione NON superata
 * Post: Il sistema non procede con la cancellazione del conguaglio
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk		il ConguaglioBulk da cancellare
 *
 */
public void eliminaConBulk (UserContext aUC, OggettoBulk bulk) throws ComponentException 
{
	ConguaglioBulk conguaglio = (ConguaglioBulk)bulk;

	try 
	{
		int rc = getTipoCancellazione(aUC, conguaglio);

		if(rc == 0)
			throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare il conguaglio !");
	
		if (rc == conguaglio.CANCELLAZIONE_FISICA)
			super.eliminaConBulk(aUC, conguaglio);
		else if (rc == conguaglio.CANCELLAZIONE_LOGICA)
			conguaglio = (ConguaglioBulk) getHome( aUC, ConguaglioBulk.class).findByPrimaryKey( conguaglio );

		if (!verificaStatoEsercizio(aUC, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( conguaglio.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio())))
			throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!");									
	}
	catch (Throwable e) 
	{
		throw handleException(e);
	} 	
}
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
public java.util.List findListaBanche(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try {
		if(conguaglio.getTerzo() == null) 
			return null;

		return getHome(userContext, BancaBulk.class).fetchAll(selectBancaByClause(userContext, conguaglio, null, null));
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
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
public java.util.Collection findModalita(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	try {

		ConguaglioBulk conguaglio = (ConguaglioBulk)bulk;
		if (conguaglio.getTerzo() == null)
			return null;

		TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
		return terzoHome.findRif_modalita_pagamento(conguaglio.getTerzo());

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch (it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}
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
public java.util.Collection findTermini(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		ConguaglioBulk conguaglio= (ConguaglioBulk)bulk;
		if (conguaglio.getTerzo() == null)
			return null;

		TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
		return terzoHome.findRif_termini_pagamento(conguaglio.getTerzo());

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch (it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}

}
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
public java.util.Collection findTipiRapporto(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{
		if (conguaglio.getTerzo() == null)
			return null;

		it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome home = (it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome)getHome(userContext, it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk.class);
		return home.findTipiRapporto(conguaglio.getV_terzo(), conguaglio.getDt_da_competenza_coge());

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(conguaglio, ex);
	}
}
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
public java.util.Collection findTipiTrattamento(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{
		if (conguaglio.getTipoRapporto() == null)
			return null;

		Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext,Tipo_trattamentoBulk.class);
		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoRapporto(conguaglio.getCd_tipo_rapporto());
		filtro.setTipoAnagrafico(conguaglio.getTi_anagrafico());
		filtro.setDataValidita(conguaglio.getDt_registrazione());
		filtro.setFlDefaultCongualio(Boolean.TRUE);
		filtro.setFlSenzaCalcoli(Boolean.FALSE);
				
		TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
		TerzoBulk tKey = new TerzoBulk(conguaglio.getCd_terzo());
		TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
		 
		AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome( AnagraficoBulk.class );
		AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
		AnagraficoBulk a = (AnagraficoBulk)aHome.findByPrimaryKey(aKey);
				
		if (a.getFl_cervellone()&& 
			!(conguaglio.getEsercizio().compareTo(a.getAnno_inizio_res_fis().intValue()) < 0) &&
			!(conguaglio.getEsercizio().compareTo(a.getAnno_fine_agevolazioni().intValue()) > 0))

			filtro.setFlAgevolazioniCervelli(new Boolean(a.getFl_cervellone()));
		else
			filtro.setFlAgevolazioniCervelli(new Boolean(false));
		
		return trattHome.findTipiTrattamento(filtro);
		
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(conguaglio, ex);
	}
}
/**
 * Pre:  L'esercizio di scrivania è antecedente a quello corrente
 * Post: La data restituita viene inizializzata al 31/12/esercizio scrivania
 *
 * Pre:  L'esercizio di scrivania NON è antecedente a quello corrente
 * Post: La data restituita viene inizializzata alla data odierna
 *
 * @param	aUC		lo UserContext che ha generato la richiesta
 * @param	bulk 	Il ConguaglioBulk la cui data deve essere inizializzata.
 *
 * @return 	La data correttamente inizializzata
*/

private java.sql.Timestamp getDataPerInizializzazioni(UserContext userContext,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException, java.text.ParseException
{
	ConguaglioBulk conguaglio = (ConguaglioBulk) bulk;

	java.sql.Timestamp tsOdierno = ((ConguaglioHome) getHome(userContext, conguaglio)).getServerDate();

   	java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
   	tsOdiernoGregorian.setTime(tsOdierno);

	if (tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR) > conguaglio.getEsercizio().intValue()) 
	{
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
       	return(new java.sql.Timestamp(sdf.parse("31/12/"+conguaglio.getEsercizio().intValue()).getTime()));
    } 
    return tsOdierno;
}
/**
 * Tipo di Cancellazione
 *
 * Pre-post-conditions:
 *
 * Nome: Tipo di Cancellazione
 * Pre:  L'utente ha richiesto la cancellazione del conguaglio
 * Post: Il sistema richiama la stored procedure che stabilisce se la cancellazione
 *       del conguaglio puo' avvenire e se deve essere logica o fisica.
 *		 
 *
 * @param	userContext			lo UserContext che ha generato la richiesta
 * @param	missione			la MissioneBulk da cancellare
 * @param	cancellaAnticipo	0 = non cancellare anticipo
 *								1 = cancella anticipo 				
 *
 * @return  il tipo di cancellazione (0 = non cancellabile; 2 = cancellazione
 *			fisica; 1 = cancellazione logica)
 */	

private int getTipoCancellazione (UserContext aUC, OggettoBulk bulk) throws ComponentException 
{
	ConguaglioBulk conguaglio = (ConguaglioBulk)bulk;
	int rc = 0;
	LoggableStatement cs = null ;	

	try 
	{
		try
		{
			cs = new LoggableStatement(getConnection(aUC), 
					"{call 	"+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+"CNRCTB650.eseguiDelConguaglio(?,?,?,?,?,?,?)}",false,this.getClass());

			cs.setObject( 1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));			
			cs.setObject( 2, conguaglio.getCd_cds()                 );		
			cs.setObject( 3, conguaglio.getCd_unita_organizzativa() );		
			cs.setObject( 4, conguaglio.getEsercizio()              );
			cs.setObject( 5, conguaglio.getPg_conguaglio()          );
			cs.setObject( 6, conguaglio.getUser()          );			
       		cs.setObject( 7, new Integer(rc));
	        cs.registerOutParameter(7, java.sql.Types.INTEGER);

			cs.executeQuery();
	        rc = (cs.getInt(7));
		}
		finally 
		{
		    cs.close();
		}		
		return rc;
	} 
	catch (java.sql.SQLException e)
	{
		throw handleException(conguaglio, e);
	}
}
/**
  * Gestione della validazione del terzo selezionato
  *	Ritorna una ComponentException con il messaggio realtivo all'errore
  *
  *	errorCode		Significato
  *	=========		===========	
  *		0			Tutto bene
  *		1			Terzo assente
  *		2			Terzo non valido alla data registrazione
  *		3			Controllo se ho inserito le modalità di pagamento
  *		4			Controllo se la modalità di pagamento è valida (ha una banca associata)
  *		5			Tipo rapporto assente
  *		6			Tipo di rapporto non valido in data inizio competenza coge
  *		7			Tipo trattamento assente
  *		8			Tipo trattamento non valido alla data registrazione
  *
**/
private void handleExceptionsTerzo(int error) throws ComponentException{

	switch (error) {
		case 1: {
			throw new it.cnr.jada.comp.ApplicationException("Inserire il terzo");		
		}case 2: {
			throw new it.cnr.jada.comp.ApplicationException("Il Terzo selezionato non è valido in Data Registrazione");
		}case 3: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare la Modalità di pagamento");
		}case 4: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare una Modalità di Pagamento valida");
		}case 5: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare il Tipo Rapporto");
		}case 6: {
			throw new it.cnr.jada.comp.ApplicationException("Il Tipo Rapporto selezionato non è valido alla Data Inizio Competenza");
		}case 7: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare il Tipo Trattamento");
		}case 8: {
			throw new it.cnr.jada.comp.ApplicationException("Il Tipo Trattamento selezionato non è valido alla Data Registrazione");
		}
	}
}
	
/**
 * Prepara un OggettoBulk (conguaglio) per l'inserimento
 *
 * Pre-post-conditions:
 *
 * Nome: Verifica stato esercizio
 * Pre:  Esercizio aperto
 * Post: Il sistema prosegue con l'inizializzazione del conguaglio
 *
 * Nome: Verifica stato esercizio
 * Pre:  Esercizio chiuso
 * Post: Viene generata una ApplicationException "Impossibile crare un conguaglio".
 *
 * Nome: Inizializzazione della data di registrazione e delle date di competenza 
 *		 del conguaglio.
 * Pre:  Esercizio del conguaglio (esercizio scrivania) antecedente a quello corrente
 * Post: Le date vengono inizializzate al 31/12 dell'esercizio del conguaglio
 *
 * Nome: Inizializzazione della data di registrazione e delle date di competenza 
 *		 del conguaglio.
 * Pre:  Esercizio del conguaglio (esercizio scrivania) uguale a quello corrente
 * Post: Le date vengono inizializzate alla data odierna
 * 
 * @param	uc		lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da inizializzare per l'inserimento
 * @return	l'OggettoBulk inizializzato
 */	
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	ConguaglioBulk conguaglio = (ConguaglioBulk)super.inizializzaBulkPerInserimento(userContext, bulk);
	
	try 
	{
		if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( conguaglio.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire un conguaglio per un esercizio non aperto!");
			
		//	Inizializzazione e controlli della data di registrazione	
		conguaglio.setDt_registrazione(getDataPerInizializzazioni(userContext, conguaglio));
		conguaglio.setDt_a_competenza_coge(conguaglio.getDt_registrazione());
		conguaglio.setDt_da_competenza_coge(conguaglio.getDt_registrazione());
	} 
	catch (it.cnr.jada.persistency.PersistencyException e) 
	{
		throw handleException(conguaglio, e);
	} 
	catch (java.text.ParseException e) 
	{
		throw handleException(conguaglio, e);
	}	
	
	return conguaglio;
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 * Pre-post-conditions:
 *
 * Nome: 	Esercizio successivo a quello di srivania
 * Pre: 	L'esercizio del conguaglio da caricare e' successivo a quello di scrivania
 * Post: 	Viene sollevata una ApplicationException
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
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	ConguaglioBulk conguaglio = (ConguaglioBulk) bulk;
	
	if (conguaglio.getEsercizio() == null)
		throw new it.cnr.jada.comp.ApplicationException("L'esercizio del documento non è valorizzato! Impossibile proseguire.");
			
	if (conguaglio.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
		throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");
			
	conguaglio = (ConguaglioBulk)super.inizializzaBulkPerModifica(userContext, bulk);
	completaConguaglio(userContext, conguaglio);
	conguaglio.setStatoToNormale();

	return conguaglio;
		
}
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
public boolean isConguaglioAnnullato(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException
{
	try
	{
		ConguaglioHome home = (ConguaglioHome)getHome(userContext, conguaglio);
		ConguaglioBulk obj = (ConguaglioBulk)home.findByPrimaryKey(conguaglio);
		if (obj == null)
			return false;
		
		return(obj.getDt_cancellazione()!=null);
	}
	catch(it.cnr.jada.persistency.PersistencyException ex)
	{
		throw handleException(conguaglio, ex);
	}
}
private boolean isTipoRapportoValido(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {

	try{

		Tipo_rapportoHome home = (Tipo_rapportoHome)getHome(userContext, Tipo_rapportoBulk.class);
		return home.isTipoRapportoValido(conguaglio.getV_terzo(), conguaglio.getCd_tipo_rapporto(), conguaglio.getDt_da_competenza_coge());

	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}
}
private boolean isTipoTrattamentoValido(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	try{

		Tipo_trattamentoHome home = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoRapporto(conguaglio.getCd_tipo_rapporto());
		filtro.setCdTipoTrattamento(conguaglio.getCd_trattamento());
		filtro.setTipoAnagrafico(conguaglio.getTi_anagrafico());
		filtro.setDataValidita(conguaglio.getDt_registrazione());
		filtro.setFlDefaultCongualio(Boolean.TRUE);
		filtro.setFlSenzaCalcoli(Boolean.FALSE);
		return home.isTipoTrattamentoValido(filtro);

	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  *	Viene caricato da db il TIPO TRATTAMENTO associato al conguaglio
  * e valido in Data Registrazione del conguaglio
  *
**/
private void loadTipoTrattamento(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {

	try {

		Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);

		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoTrattamento(conguaglio.getCd_trattamento());
		filtro.setTipoAnagrafico(conguaglio.getTi_anagrafico());
		filtro.setDataValidita(conguaglio.getDt_registrazione());
		filtro.setFlDefaultCongualio(Boolean.TRUE);
		filtro.setFlSenzaCalcoli(Boolean.FALSE);
		conguaglio.setTipoTrattamento(trattHome.findTipoTrattamentoValido(filtro));

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  *	Viene caricato da db il TERZO associato al conguaglio
  * valido in Data Registrazione
  * e con tipi rapporto validi in Data Competenza Coge
  *
**/
private V_terzo_per_compensoBulk loadVTerzo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {

	try {

		V_terzo_per_conguaglioHome home = (V_terzo_per_conguaglioHome)getHome(userContext, V_terzo_per_conguaglioBulk.class, "DISTINCT_TERZO");
		return home.loadVTerzo(userContext,conguaglio.getTi_anagrafico(), conguaglio.getCd_terzo(), conguaglio.getDt_registrazione(), conguaglio.getDt_da_competenza_coge());
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
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
public ConguaglioBulk reloadConguaglio(UserContext userContext, ConguaglioBulk bulk) throws ComponentException{

	try {

		ConguaglioHome home = (ConguaglioHome)getHome(userContext, bulk);
		ConguaglioBulk conguaglio = (ConguaglioBulk)home.findByPrimaryKey(bulk);
		getHomeCache(userContext).fetchAll(userContext);

		completaConguaglio(userContext, conguaglio);
		return conguaglio;

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}				
}
/**
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Conguaglio
  *	
  * Pre-post-conditions:
  *
  * Nome: Richiesta di ricerca di un conguaglio
  * Pre:  E' stata generata la richiesta di ricerca di un conguaglio
  * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e,
  *		  in aggiunta, le clausole che il conguaglio abbia esercizio di creazione uguale a quello di scrivania,
  *       CDS di origine uguale a quella di scrivania, Unita Organizzativa uguale a quella di scrivania
  * 
  * @param userContext lo userContext che ha generato la richiesta
  * @param clauses clausole di ricerca gia' specificate dall'utente
  * @param bulk istanza di ConguaglioBulk che deve essere utilizzata per la ricerca
  * @return il SQLBuilder con le clausole aggiuntive
  *
**/
public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SimpleFindClause clause;
	if(clauses!=null)
	{
		for ( java.util.Iterator i = clauses.iterator(); i.hasNext(); )
		{
			clause = (SimpleFindClause) i.next();
			if (clause.getPropertyName().equalsIgnoreCase( "dt_cancellazione" ))
				if ( clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL )
					clause.setSqlClause( "TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) );
				else
					clause.setSqlClause( "TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) + " ? ");				
		}	
	}
	ConguaglioBulk conguaglio = (ConguaglioBulk)bulk;
	SQLBuilder sql = (SQLBuilder)super.select(userContext, clauses, bulk);
	sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, conguaglio.getCd_cds());
	sql.addSQLClause("AND", "CONGUAGLIO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, conguaglio.getCd_unita_organizzativa());
	sql.addSQLClause("AND", "TI_ANAGRAFICO", sql.EQUALS, it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk.ALTRO);

	sql.addTableToHeader("TERZO");
	sql.addSQLJoin("CONGUAGLIO.CD_TERZO", "TERZO.CD_TERZO");
	sql.addSQLClause("AND","TERZO.CD_PRECEDENTE", SQLBuilder.EQUALS, conguaglio.getV_terzo().getCd_terzo_precedente());

	sql.addClause(clauses);
	return sql;
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Banche
  *
  * Nome: Richiesta di ricerca di una banca
  * Pre: E' stata generata la richiesta di ricerca delle banche
  * Post: Viene restituito l'SQLBuilder per filtrare le banche NON CANCELLATE,
  *		  associate al TERZO selezionato e al TIPO PAGAMENTO selezionato
  *
  * @param userContext Lo userContext che ha generato la richiesta
  * @param conguaglio l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param banca l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  * 		  costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectBancaByClause(UserContext userContext, ConguaglioBulk conguaglio, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {

	BancaHome bancaHome = (BancaHome)getHome(userContext, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
	return bancaHome.selectBancaFor(conguaglio.getModalitaPagamento(), conguaglio.getCd_terzo());
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul V_terzo
  *
  * Nome: Validazione superata
  * Pre: E' stata generata la richiesta di ricerca di un V_terzo_per_conguaglioBulk
  *		 a fronte di un corretto inserimento delle date di competenza
  * Post: Viene restituito l'SQLBuilder per filtrare i terzi
  *		  con TIPO ANAGRAFICO compatibile con quello selezionato, validi alla 
  * 	  Data Registrazione e con tipi rapporto validi in Data Competenza Coge
  *
  * Nome: Validazione NON superata
  * Pre: E' stata generata la richiesta di ricerca di un V_terzo_per_conguaglioBulk
  *		 a fronte di un errato inserimento delle date di competenza
  * Post: Viene restituita una ValidationExecption con la desrizione dell'Errore relativo
  *
  * @param userContext Lo userContext che ha generato la richiesta
  * @param conguaglio l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param vTerzo l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  * 		  costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectV_terzoByClause(UserContext userContext,ConguaglioBulk conguaglio, V_terzo_per_compensoBulk vTerzo, CompoundFindClause clauses) throws ComponentException {

	try{
		conguaglio.validaDateCompetenzaCoge();

		V_terzo_per_conguaglioHome home = (V_terzo_per_conguaglioHome)getHome(userContext,V_terzo_per_conguaglioBulk.class,"DISTINCT_TERZO");
		return home.selectVTerzo(conguaglio.getTi_anagrafico(), conguaglio.getCd_terzo(), conguaglio.getDt_registrazione(), conguaglio.getDt_da_competenza_coge(), clauses);

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  *	Richiama la procedura Oracle CNRCTB650.upgKeyAssCompensoConguaglio
  *	che aggiorna i record della tabella di Relazione tra Conguaglio e Compenso
  *
**/
private void upgKeyAssCompensoConguaglio(UserContext userContext, ConguaglioBulk conguaglio, Long pgConguaglioTemp, Long pgCompensoTemp) throws ComponentException{

	try{
		LoggableStatement cs = new LoggableStatement(getConnection(userContext),
				"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB650.upgKeyAssCompensoConguaglio(?,?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		try{
			cs.setObject( 1, conguaglio.getCd_cds()                 );
			cs.setObject( 2, conguaglio.getCd_unita_organizzativa() );
			cs.setObject( 3, conguaglio.getEsercizio()              );
			cs.setObject( 4, pgConguaglioTemp						);
			cs.setObject( 5, conguaglio.getPg_conguaglio()			);
			cs.setObject( 6, conguaglio.getCd_cds_compenso()		);
			cs.setObject( 7, conguaglio.getCd_uo_compenso()			);
			cs.setObject( 8, conguaglio.getEsercizio_compenso()		);
			cs.setObject( 9, pgCompensoTemp							);
			cs.setObject(10, conguaglio.getPg_compenso()			);

			cs.execute();
		}finally{
			cs.close();
		}
	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}
}
/**
  *	Viene richiesta la validazione di una Conguaglio prima del salvataggio
  *
  *	Pre-post_conditions
  *
  * Nome: Validazione Testata conguaglio, validazione Terzo e validazione Dati Esterni superata
  * Pre: Data registrazione, Date competenza coge, Descrizione conguaglio valide;
  * 	 Terzo valido. Tutti i dati esterni inseriti
  * Post: Viene validato l'OggettoBulk
  *
  * Nome: Validazione Testata conguaglio NON superata
  * Pre: Data registrazione, Date competenza o Descrizione conguaglio non validi
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * Nome: Validazione Terzo NON superata
  * Pre: Il terzo selezionato non risulta valido
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * Nome: Validazione Dati Esterni NON superata
  * Pre: Uno degli importi dei dati esterni è null
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * @param	userContext	Lo userContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da validare
  *
**/

public void controlloRiduzioneCuneo32020(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException {
}

private void validaConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException
{

	// Controllo Testata Conguaglio
	try{conguaglio.validaTestata();
	controlloRiduzioneCuneo32020(userContext, conguaglio);}
	catch(java.text.ParseException ex){throw handleException(ex);}
	catch(javax.ejb.EJBException ex){throw handleException(ex);}		

	// Controllo Terzo
	validaTerzo(userContext, conguaglio);

	conguaglio.validaDatiEsterni();
}
/**
  *	Viene richiesta la validazione di una Conguaglio prima dell'esecuzione 
  * della procedura oracle ABILITACONGUAGLIO
  *
  *	Pre-post_conditions
  *
  * Nome: Validazione Testata conguaglio, validazione Terzo e validazione Dati Esterni superata
  * Pre: Data registrazione, Date competenza coge, Descrizione conguaglio valide;
  * 	 Terzo valido. Tutti i dati esterni inseriti
  * Post: Viene validato l'OggettoBulk
  *
  * Nome: Validazione Testata conguaglio NON superata
  * Pre: Data registrazione, Date competenza o Descrizione conguaglio non validi
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * Nome: Validazione Terzo NON superata
  * Pre: Il terzo selezionato non risulta valido
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * Nome: Validazione Dati Esterni NON superata
  * Pre: Uno degli importi dei dati esterni è null
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * @param	userContext	Lo userContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da validare
  *
**/
private void validaConguaglioPerCalcolo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException
{
	// Controllo Testata Conguaglio
	try{conguaglio.validaTestata();}
	catch(java.text.ParseException ex){throw handleException(ex);}
	catch(javax.ejb.EJBException ex){throw handleException(ex);}		

	// Controllo Terzo
	validaTerzo(userContext, conguaglio);

	conguaglio.validaDatiEsterni();
}
/**
  *	Viene richiesta la validazione di una Conguaglio prima dell'esecuzione 
  * della procedura oracle CREACOMPENSOCONGUAGLIO
  *
  *	Pre-post_conditions
  *
  * Nome: Validazione Testata conguaglio, validazione Terzo e validazione Dati Esterni superata
  * Pre: Data registrazione, Date competenza coge, Descrizione conguaglio valide;
  * 	 Terzo valido. Tutti i dati esterni inseriti
  * Post: Viene validato l'OggettoBulk
  *
  * Nome: Validazione Testata conguaglio NON superata
  * Pre: Data registrazione, Date competenza o Descrizione conguaglio non validi
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * Nome: Validazione Terzo NON superata
  * Pre: Il terzo selezionato non risulta valido
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * Nome: Validazione Dati Esterni NON superata
  * Pre: Uno degli importi dei dati esterni è null
  * Post: Viene restituita una ComponentException con la desrizione dell'errore
  *
  * @param	userContext	Lo userContext che ha generato la richiesta
  * @param	conguaglio l'OggettoBulk da validare
  *
**/
private void validaCreaCompensoConguaglio(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException
{
	// Controllo Testata Conguaglio
	try{conguaglio.validaTestata();}
	catch(java.text.ParseException ex){throw handleException(ex);}
	catch(javax.ejb.EJBException ex){throw handleException(ex);}		

	// Controllo Terzo
	validaTerzo(userContext, conguaglio);

	conguaglio.validaDatiEsterni();
}
public void validaAltriDatiEsterni(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException
{
	try {
		AnagraficoBulk cnrAnag = (AnagraficoBulk)getHome(userContext,AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(new Integer(1)));
		if (conguaglio.getCodice_fiscale_esterno()!=null &&
			 (conguaglio.getCodice_fiscale_esterno().equals(cnrAnag.getCodice_fiscale())||
			  conguaglio.getCodice_fiscale_esterno().equals(cnrAnag.getPartita_iva())))
		{
			throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Il Codice fiscale/Partita IVA del Datore di lavoro non può essere quello del C.N.R.");
		}
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}
public String verificaIncoerenzaCarichiFam (UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException
{
	String message = null;
	TerzoBulk terzo;
	try {
		terzo = (TerzoBulk)getHome(userContext,TerzoBulk.class).findByPrimaryKey(new TerzoBulk(conguaglio.getCd_terzo()));

	    AnagraficoBulk anag =(AnagraficoBulk)getHome(userContext,AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(terzo.getCd_anag()));
		//getHomeCache(userContext).fetchAll(userContext);
	    AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
	    
		if (conguaglio.getDetrazioni_fi_esterno().compareTo(new BigDecimal(0))!=0 &&
		    !sess.esisteFiglioValido(userContext, anag))
		{
			message = "Compenso creato in modo corretto. Attenzione! Nei dati esterni sono state inserite le detrazioni per i figli ma nei carichi familiari del terzo non sono presenti figli validi.";
			return message;
		}	
		if (conguaglio.getDetrazioni_co_esterno().compareTo(new BigDecimal(0))!=0 &&
		    !sess.esisteConiugeValido(userContext, anag, null))
		{
			message = "Compenso creato in modo corretto. Attenzione! Nei dati esterni sono state inserite le detrazioni per il coniuge ma nei carichi familiari del terzo non è presente un coniuge valido.";
			return message;
		}
		if (conguaglio.getDetrazioni_al_esterno().compareTo(new BigDecimal(0))!=0 &&
		    !sess.esisteAltroFamValido(userContext, anag))
		{
			message = "Compenso creato in modo corretto. Attenzione! Nei dati esterni sono state inserite le detrazioni per altri familiari ma nei carichi familiari del terzo non sono presenti altri familiari validi.";
			return message;
		}
	} catch (PersistencyException e) {
		throw handleException(e);
	} catch (RemoteException e) {
		throw handleException(e);
	}
	return message;
}
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
  *		4			Banca non inserita
  *		5			Tipo rapporto assente
  *		6			Tipo di rapporto non valido in data inizio competenza coge
  *		7			Tipo trattamento assente
  *		8			Tipo trattamento non valido alla data registrazione
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
  * Nome: Modalita di pagamento non valida
  *	Pre: Non è stato selezionata una modalita di pagamento valida (con banca)
  *	Post: Ritorna una ApplicationException con la descrizione dell'errore
  *			"Selezionare una Modalità di pagamento valida"
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
public void validaTerzo(UserContext userContext, ConguaglioBulk conguaglio) throws ComponentException{

	int error = validaTerzo(userContext, conguaglio, true);
	handleExceptionsTerzo(error);
}
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
  *		4			Banca non inserita
  *		5			Tipo rapporto assente
  *		6			Tipo di rapporto non valido in data inizio competenza coge
  *		7			Tipo trattamento assente
  *		8			Tipo trattamento non valido alla data registrazione
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
  * Nome: Banca non inserita
  *	Pre: Non è stato selezionato un conto corretto
  *	Post: Ritorna il valore 4
  *
  * Nome: Tipo rapporto assente
  *	Pre: Non è stato selezionato un tipo rapporto
  *	Post: Ritorna il valore 5
  *
  * Nome: Tipo rapporto non valido alla data inizio competenza coge
  *	Pre: Il tipo rapporto selezionato non è valido in data competenza coge
  *	Post: Ritorna il valore 6
  *
  * Nome: Tipo trattamento assente
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna il valore 7
  *
  * Nome: Tipo trattamento non valido alla data registrazione
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna il valore 8
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
public int validaTerzo(UserContext userContext, ConguaglioBulk conguaglio, boolean checkModPag) throws ComponentException{

	TerzoBulk terzo = conguaglio.getTerzo();

	// terzo assente
	if (terzo==null)
		return 1;

	// terzo non valido alla data registrazione
	if (terzo.getDt_fine_rapporto()!=null && terzo.getDt_fine_rapporto().compareTo(conguaglio.getDt_registrazione())<0)
		return 2;

	// Controllo se ho inserito le modalità di pagamento
	if (checkModPag && conguaglio.getModalitaPagamento()==null)
		return 3;

	// banca assente
	if (checkModPag && conguaglio.getBanca()==null)
		return 4;

	// tipo rapporto assente
	if (conguaglio.getTipoRapporto()==null)
		return 5;
	
	// rapporto non valido in data inizio competenza coge
	if (!isTipoRapportoValido(userContext, conguaglio))
		return 6;

	// tipo trattamento assente
	if (conguaglio.getTipoTrattamento()==null)
		return 7;

	// tipo trattamento non valido alla data registrazione
	if (!isTipoTrattamentoValido(userContext, conguaglio))
		return 8;

	return(0);
}
public boolean verificaStatoEsercizio(UserContext userContext,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) throws ComponentException 
{
	try 
	{
		it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome)getHome(userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class);
		
		return !eHome.isEsercizioChiuso(userContext, anEsercizio.getEsercizio(), anEsercizio.getCd_cds());
	} 
	catch (it.cnr.jada.persistency.PersistencyException e) 
	{
		throw handleException(e);
	}
}
public boolean isGestiteDeduzioniIrpef(UserContext userContext) throws ComponentException {
	try {
		Parametri_cnrBulk par = ((Parametri_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext));
		return 
		    par.getFl_deduzione_irpef();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public boolean isGestiteDetrazioniFamily(UserContext userContext) throws ComponentException {
	try {
		Parametri_cnrBulk par = ((Parametri_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext));
		return 
		    par.getFl_detrazioni_family();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
