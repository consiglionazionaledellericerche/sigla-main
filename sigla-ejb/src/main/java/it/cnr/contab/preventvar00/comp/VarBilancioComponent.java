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

package it.cnr.contab.preventvar00.comp;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.ejb.PDCFinComponentSession;
import it.cnr.contab.config00.esercizio.bulk.*;

import java.io.Serializable;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk;
import it.cnr.contab.config00.pdcfin.bulk.EV_cnr_spese_capitoloBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.preventvar00.bulk.*;

import java.math.*;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.preventvar00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Insert the type's description here.
 * Creation date: (03/04/2002 17.28.05)
 * @author: Roberto Fantino
 */
public class VarBilancioComponent extends it.cnr.jada.comp.CRUDComponent implements IVarBilancioMgr, IPrintMgr, Cloneable, Serializable{
/**
 * VarBilancioComponent constructor comment.
 */
public VarBilancioComponent() {
	super();
}
/**
  *
  * Vengono caricati da db tutti i dettagli associati alla
  * variazione di Bilancio <varBilancio> e vengono memorizzati nella
  * variazione stessa
  *
  * @param userContext - lo UserContext che ha generato la richiesta
  * @param varBilancio - l'istanza di Var_bilancioBulk da completare
  *
**/
private void caricaDettagli(UserContext userContext, Var_bilancioBulk varBilancio) throws ComponentException{

	try{

		Var_bilancio_detHome home = (Var_bilancio_detHome)getHome(userContext, Var_bilancio_detBulk.class);
		varBilancio.setDettagli(new it.cnr.jada.bulk.BulkList(home.caricaDettagli(userContext, varBilancio)));
	
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(varBilancio, ex);
	}
	
}
/**
  *
  * Richiama la procedura Oracle CNRCTB055.checkVariazioneBilancio
  * che verifica la variazione inserita dall'utente
  *
  * @param userContext - lo UserContext che ha generato la richiesta
  * @param varBilancio - l'istanza di Var_bilancioBulk da verificare
  *
**/
private void checkVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio) throws ComponentException{

	try{
		LoggableStatement cs =new LoggableStatement(getConnection(userContext),"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB055.checkVariazioneBilancio(?,?,?,?)}",false,this.getClass());
		try{
			cs.setObject( 1, varBilancio.getEsercizio());
			cs.setObject( 2, varBilancio.getCd_cds() );		
			cs.setObject( 3, varBilancio.getTi_appartenenza());
			cs.setObject( 4, varBilancio.getPg_variazione() );
			cs.execute();
		}finally{
			cs.close();
		}
	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}

}
/**
  *
  * Viene richiesto il completamento dell'OggettoBulk <varBilancio>
  *
  * Pre-post-conditions
  *
  * Nome: Completamento della variazione di bilancio
  * Pre: Viene richiesto il completamento della variazione di bilancio
  * Post: Viene restituita la variazione di Bilancio completa di bilancio 
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	varBilancio l'OggettoBulk da completare
  * @return	l'OggettoBulk completo
  *
**/
private Var_bilancioBulk completaBulk(UserContext userContext, Var_bilancioBulk varBilancio) throws it.cnr.jada.comp.ComponentException {
	varBilancio.setBilancio(loadBilancio(userContext));
	return varBilancio;
}
/**
  *
  * Richiesta di creazione di una variazione di bilancio
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la creazione di una istanza di Var_bilancioBulk
  * Post: La Variazione di Bilancio inserita viene salvata sul db
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la creazione di una istanza di Var_bilancioBulk
  * Post: La Variazione di Bilancio NON viene salvata su db.
  *		 Viene emesso il messaggio errore corrispondente.
  *
  * @param 	userContext	lo UserContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk la Variazione di bilancio che deve essere creata
  * @return	l'OggettoBulk risultante dopo l'operazione di creazione.
  *
  * Metodo privato chiamato:
  *		checkVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
public OggettoBulk eseguiCreaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {

	bulk = super.eseguiCreaConBulk(userContext, bulk);
	checkVariazioneBilancio(userContext, (Var_bilancioBulk)bulk);	

	return bulk;
}
/**
  *
  * Richiesta di modifica di una variazione di bilancio
  *
  * Nome: Validazione superata
  * Pre:  E' stata richiesta la modifica di un'istanza di Var_bilancioBulk
  * Post: La Variazione di Bilancio selezionata viene salvata sul db
  *
  * Nome: Validazione NON superata
  * Pre:  E' stata richiesta la modifica di una istanza di Var_bilancioBulk
  * Post: La Variazione di Bilancio selezionata NON viene salvata su db.
  * 		 Viene emesso il messaggio errore corrispondente.
  *
  * @param 	userContext lo serContext che ha generato la richiesta
  * @param 	bulk		OggettoBulk la Variazione di bilancio che deve essere modificata
  * @return	l'OggettoBulk risultante dopo l'operazione di modifica.
  *
  * Metodo privato chiamato:
  *		checkVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
public OggettoBulk eseguiModificaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException , it.cnr.jada.persistency.PersistencyException{

	bulk = super.eseguiModificaConBulk(userContext, bulk);
	Var_bilancio_detHome detthome = (Var_bilancio_detHome)getHome(userContext,Var_bilancio_detBulk.class);	
	for (Iterator dettagli = detthome.caricaDettagli(userContext, (Var_bilancioBulk)bulk).iterator();dettagli.hasNext();){
		Var_bilancio_detBulk dettaglio = (Var_bilancio_detBulk)dettagli.next();
		if (((Var_bilancioBulk)bulk).getTi_variazione().equalsIgnoreCase(Var_bilancioResiduiBulk.VAR_ECO)&&
				dettaglio.getImportoSpesa().compareTo(Utility.ZERO) != -1){
			throw new ApplicationException("Le Economie possono avere solo importi negativi");
		}
		if (((Var_bilancioBulk)bulk).getTi_variazione().equalsIgnoreCase(Var_bilancioResiduiBulk.VAR_MSP)&&
				dettaglio.getImportoSpesa().compareTo(Utility.ZERO) != 1){
			throw new ApplicationException("Le Maggiori spese possono avere solo importi positivi");
		}
	}
	checkVariazioneBilancio(userContext, (Var_bilancioBulk)bulk);
	return (bulk);
}

/** Nuovo metodo per eliminare una variazione. 
 * 	Nell'eliminazione veniva richiamato il metodo eseguiModificaConBulk
 *  e quindi anche checkVariazioneBilancio che non è necessario tale controllo
 *  è rimasto solo per il salvataggio.  
 *  Ora viene solo impostato lo stato ad A nel BP**/

public Var_bilancioBulk eliminaVariazione(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException , it.cnr.jada.persistency.PersistencyException{

	bulk = super.eseguiModificaConBulk(userContext, bulk);
	Var_bilancio_detHome detthome = (Var_bilancio_detHome)getHome(userContext,Var_bilancio_detBulk.class);	
	for (Iterator dettagli = detthome.caricaDettagli(userContext, (Var_bilancioBulk)bulk).iterator();dettagli.hasNext();){
		Var_bilancio_detBulk dettaglio = (Var_bilancio_detBulk)dettagli.next();
		if (((Var_bilancioBulk)bulk).getTi_variazione().equalsIgnoreCase(Var_bilancioResiduiBulk.VAR_ECO)&&
				dettaglio.getImportoSpesa().compareTo(Utility.ZERO) != -1){
			throw new ApplicationException("Le Economie possono avere solo importi negativi");
		}
		if (((Var_bilancioBulk)bulk).getTi_variazione().equalsIgnoreCase(Var_bilancioResiduiBulk.VAR_MSP)&&
				dettaglio.getImportoSpesa().compareTo(Utility.ZERO) != 1){
			throw new ApplicationException("Le Maggiori spese possono avere solo importi positivi");
		}
	}
//	checkVariazioneBilancio(userContext, (Var_bilancioBulk)bulk);
	return (Var_bilancioBulk) (bulk);
}
/**
  *
  * Richiama la procedura Oracle CNRCTB055.esitaVariazioneBilancio
  * che rende definitiva la variazione selezionata
  *
  * @param userContext - lo UserContext che ha generato la richiesta
  * @param varBilancio - l'istanza di Var_bilancioBulk da rendere definitiva
  *
**/
private void esitaVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio) throws ComponentException{
	try{
		LoggableStatement cs = new LoggableStatement(getConnection(userContext),"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB055.esitaVariazioneBilancio(?,?,?,?,?)}",false,this.getClass());
		try{
			cs.setObject( 1, varBilancio.getEsercizio());
			cs.setObject( 2, varBilancio.getCd_cds() );		
			cs.setObject( 3, varBilancio.getTi_appartenenza());
			cs.setObject( 4, varBilancio.getPg_variazione() );
			cs.setObject( 5, varBilancio.getUtuv());
			cs.executeQuery();
		}finally{
			cs.close();
		}
	}catch(java.sql.SQLException ex){
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
  * Pre: E' stata richiesta l'inizializzazione di una istanza di Var_bilancioBulk per inserimento
  * Post: L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		 per una operazione di creazione
  *		 In particolare:
  *			- Carico il Bilancio associato all'ESERCIZIO e al CDS di scrivania
  *
  * @param	userContext lo UserContext che ha generato la richiesta
  * @param	bulk OggettoBulk l'istanza di Var_bilancioBulk da inizializzare
  * @return	OggettoBulk l'istanza di Var_bilancioBulk inizializzata
  *
  * Metodo privato chiamato:
  *		completaBulk(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	bulk = super.inizializzaBulkPerInserimento(userContext,bulk);
	completaBulk(userContext, (Var_bilancioBulk)bulk);
	if (!(bulk instanceof Var_bilancioResiduiBulk)) {
		try {
			Esercizio_baseHome homeEsercizioBase = (Esercizio_baseHome)getHome(userContext, Esercizio_baseBulk.class);
			Esercizio_baseBulk esercizioBase = (Esercizio_baseBulk)homeEsercizioBase.findByPrimaryKey(new Esercizio_baseBulk(((Var_bilancioBulk)bulk).getEsercizio()));
		    ((Var_bilancioBulk)bulk).setEsercizio_res( esercizioBase );
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
	}
	return bulk;		
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
  *			In particolare vengono caricate tutti i dettagli associati alla Variazione di Bilancio selezionata
  * 
  * @param	userContext lo UserContext che ha generato la richiesta
  * @param	bulk	l'OggettoBulk da preparare
  * @return	l'OggettoBulk preparato
  *
  * Metodo privato chiamato:
  *		caricaDettagli(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/	
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	bulk = super.inizializzaBulkPerModifica(userContext,bulk);
	caricaDettagli(userContext, (Var_bilancioBulk)bulk);

	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		return asRO(bulk,"Variazione di bilancio non modificabile ad esercizio chiuso.");

	return bulk;		
}
/** 
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di ricerca.
  * Inizializzazione di una istanza di Var_bilancioBulk per ricerca
  *
  * Nome: Inizializzazione per ricerca
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Var_bilancioBulk per ricerca
  * Post: Viene inizializzato il Bilancio della variazione caricandolo da db
  *
  * @param	userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param	bulk <code>OggettoBulk</code> la variazione da inizializzare per la ricerca
  * @return	la variazione inizializzata per la ricerca
  *
  * Metodo privato chiamato:
  *		completaBulk(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	bulk = super.inizializzaBulkPerRicerca(userContext,bulk);
	completaBulk(userContext, (Var_bilancioBulk)bulk);

	return bulk;		
}
/** 
  * Prepara un OggettoBulk per la presentazione all'utente per una possibile
  * operazione di ricerca guidata.
  * Inizializzazione di una istanza di Var_bilancioBulk per ricerca guidata
  *
  * Nome: Inizializzazione per ricerca
  * Pre:  E' stata richiesta l'inizializzazione di una istanza di Var_bilancioBulk per ricerca guidata
  * Post: Viene inizializzato il Bilancio della variazione caricandolo da db
  *
  * @param	userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param	bulk <code>OggettoBulk</code> la variazione da inizializzare per la ricerca guidata
  * @return	la variazione inizializzata per la ricerca guidata
  *
  * Metodo privato chiamato:
  *		completaBulk(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk bulk) throws ComponentException {
	return inizializzaBulkPerRicerca(userContext,bulk);
}
/**
 * inizializzaBulkPerStampa method comment.
 */
public OggettoBulk inizializzaBulkPerStampa(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	return bulk;
}
protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,Integer esercizio) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext,esercizio,CNRUserContext.getCd_cds(userContext));
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/**
  * Viene richiesto il caricamento del Bilancio associato CDS di scrivania e all'ESERCIZIO di scrivania
  *
  * Pre-post-condition
  *
  * Nome: Unita Organizzativa di tipo "ente"
  * Pre: Viene richiesto il caricamento del bilancio
  * Post: Viene restituito il bilancio con TIPO_APPARTENENZA = "C", CDS ed ESERCIZIO di scrivania
  *
  * Nome: Unita Organizzativa NON di tipo "ente"
  * Pre: Viene richiesto il caricamento del bilancio
  * Post: Viene restituito il bilancio con TIPO_APPARTENENZA = "D", CDS ed ESERCIZIO di scrivania
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @return	l'istanza di Bilancio_preventivoBulk
  *
**/
private Bilancio_preventivoBulk loadBilancio(UserContext userContext) throws ComponentException{

	try{

		Integer ese = CNRUserContext.getEsercizio(userContext);
		String cdCds = CNRUserContext.getCd_cds(userContext);
		String tipoApp = null;

		Unita_organizzativaHome homeUO = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)homeUO.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			tipoApp = it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.tipo_appartenenza_cnr;
		else
			tipoApp = it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.tipo_appartenenza_cds;

		Bilancio_preventivoHome homeBil = (Bilancio_preventivoHome)getHome(userContext, Bilancio_preventivoBulk.class);
		Bilancio_preventivoBulk bil = (Bilancio_preventivoBulk)homeBil.findByPrimaryKey(new Bilancio_preventivoBulk(cdCds, ese, tipoApp));
		if (bil==null)
			throw new it.cnr.jada.comp.ApplicationException("Non è stato definito il Bilancio preventivo per il Cds " + cdCds.toString() + " ed esercizio " + ese.toString() );

		getHomeCache(userContext).fetchAll(userContext);

		return(bil);
	
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
	
}
/**
  *
  * Viene richiesto un nuovo progressivo per la variazione di bilancio
  *
  * Pre-post-conditions
  *
  * Name: Esisitono variazioni precedenti
  * Pre: Viene richiesto un nuovo progressivo
  * Post: Viene restituito un nuovo progressivo da assegnare alla variazione
  * 		 calcolato come MAX+1
  *
  * Name: Non esisitono variazioni precedenti
  * Pre: Viene richiesto un progressivo per assegnare alla prima variazione di bilancio
  * Post: Viene restituito il progressivo 1 da assegnare alla prima variazione
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	varBilancio La Variazione a cui deve essere assegnato il nuovo progressivo
  * @return	il nuovo progressivo da utilizzare
  *
**/
private Long recuperaNuovoProgressivo(UserContext userContext, Var_bilancioBulk varBilancio) throws ComponentException{
	try{
		Long tmp = (Long)getHome(userContext, varBilancio).findAndLockMax(varBilancio, "pg_variazione",null);
		if (tmp==null)
			tmp = new Long(0);

		return (new Long(tmp.intValue()+1));

	}catch(it.cnr.jada.bulk.BusyResourceException ex){
		throw handleException(varBilancio, ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(varBilancio, ex);
	}
}
/**
  *
  * Viene Richiesto il caricamento di una Variazione di Bilancio
  *
  * Pre-post_conditions
  *
  * Nome: Caricamento Variazione di Bilancio
  *	Pre: viene richiesto il caricamento da db della variazione
  *	Post: viene caricata da database la Variazione di Bilancio insieme a tutti 
  *		  gli oggetti complessi necessari ad una sua corretta gestione
  *			- i dettagli (istanze di Var_bilancio_dettBulk)
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk La Variazione che deve essere ri-caricata
  * @return	la variazione di bilancio aggiornata
  *
  * Metodo privato chiamato:
  *		caricaDettagli(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
private Var_bilancioBulk reloadVarBilancio(UserContext userContext, Var_bilancioBulk bulk) throws ComponentException{

	try {

		Persistent varBilancio = getHome(userContext, bulk).findByPrimaryKey(bulk);

		getHomeCache(userContext).fetchAll(userContext);
		caricaDettagli(userContext, (Var_bilancioBulk)varBilancio);

		return (Var_bilancioBulk)varBilancio;

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}				
}
/**
  *
  * Viene richiesto il salvataggio definitivo della Variazione di Bilancio selezionata
  *
  * Pre-post-conditions:
  *
  * Nome: Salvataggio definitivo della Variazione
  * Pre: Viene richiesto il salvataggio definitivo della Variazione
  * Post: Viene salvata in modo definitivo la Variazione di Bilancio selezionata
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	varBilancio l'OggettoBulk da salvara in modo definitivo
  * @return	la variazione di bilancio aggiornata
  *
  * Metodi privati chiamati:
  *		esitaVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
  *		reloadVarBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
  *
**/
public Var_bilancioBulk salvaDefinitivo(UserContext userContext, Var_bilancioBulk varBilancio) throws ComponentException{
    try {
    	 if (varBilancio.getTi_variazione()!=null && (varBilancio.getTi_variazione().compareTo(varBilancio.VAR_QUADRATURA)==0)||(varBilancio.getTi_variazione().compareTo(varBilancio.STORNO_E)==0)||(varBilancio.getTi_variazione().compareTo(varBilancio.STORNO_S)==0))
  			if (!verificaQuadratura(varBilancio))
  				throw new it.cnr.jada.comp.ApplicationException("Attenzione, la variazione non è in quadratura, impossibile procedere al salvataggio definitivo.");
	    lockBulk(userContext, varBilancio);
	    validaModificaConBulk(userContext, varBilancio);
	    varBilancio.setToBeUpdated();
//	    varBilancio = (Var_bilancioBulk) eseguiModificaConBulk(userContext, varBilancio);
		eseguiModificaConBulk(userContext, varBilancio);
		
		// solo nel salvataggio definitivo (almeno per ora, in caso contrario inserire
		// questo codice in validaModificaConBulk)
		// verifichiamo che i dati della voce siano stati caricati correttamente, 
		// cioè che siano presenti nella tabella dei saldi VOCE_F_SALDI_CDR_LINEA
		Iterator it = varBilancio.getDettagli().iterator();
		while(it.hasNext()) {
			Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk)it.next();
//			aggiunta la condizione sul getCrudStatus()!=OggettoBulk.UNDEFINED
//			perchè la riga in tabella viene aggiunta nel package CNRCTB055 e deve poter continuare
			if (varBilDett.getVoceFSaldi().getCrudStatus()!=OggettoBulk.NORMAL && varBilDett.getVoceFSaldi().getCrudStatus()!=OggettoBulk.UNDEFINED)
				throw new it.cnr.jada.comp.ApplicationException("La voce "+varBilDett.getCd_voce()+" non è corretta. Modificare o eliminare il dettaglio relativo.");
		}

    } catch (it.cnr.jada.persistency.PersistencyException e) {
	 throw handleException(varBilancio,e);
    } catch (it.cnr.jada.bulk.OutdatedResourceException e) {
	 throw handleException(varBilancio,e);
    } catch (it.cnr.jada.bulk.BusyResourceException e) {
	 throw handleException(varBilancio,e);
	}
 	esitaVariazioneBilancio(userContext, varBilancio);
	return reloadVarBilancio(userContext, varBilancio);
}
/**
  *
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Causale di variazione
  *
  * Nome: Richiesta di ricerca di una Causale
  * Pre: E' stata generata la richiesta di ricerca di una causale
  * Post: Viene restituito l'SQLBuilder per filtrare le causali con TIPO_CAUSALE = "U",
  *
  * @param userContext Lo userContext che ha generato la richiesta
  * @param varBilancio l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param causale l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  * 		  costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  * 		   della query.
  *
**/
public SQLBuilder selectCausaleVariazioneByClause(UserContext userContext, Var_bilancioBulk varBilancio, Causale_var_bilancioBulk causale, CompoundFindClause clauses) throws ComponentException {

	Causale_var_bilancioHome home = (Causale_var_bilancioHome)getHome(userContext, causale);

	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND", "TI_CAUSALE", sql.EQUALS, Causale_var_bilancioBulk.UTENTE);

	sql.addClause(clauses);
	return sql;
}
/**
  *
  * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Variazioni ai progetti
  *
  * Nome: Richiesta di ricerca di una Variazione di Progetto
  * Pre: E' stata generata la richiesta di ricerca di una Variazione di Progetto
  * Post: Viene restituito l'SQLBuilder per filtrare le Variazione di Progetto
  *
  * @param userContext Lo userContext che ha generato la richiesta
  * @param varBilancio l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param pgd_variazione l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  * 		  costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  * 		   della query.
  *
**/
public SQLBuilder selectPdg_variazioneByClause(UserContext userContext, Var_bilancioBulk varBilancio, Pdg_variazioneBulk pgd_variazione, CompoundFindClause clauses) throws ComponentException {

	Pdg_variazioneHome home = (Pdg_variazioneHome)getHome(userContext, pgd_variazione);

	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND", "STATO", sql.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
	sql.addClause(clauses);

	// Effettuo la ricerca dei PDG non associati a Variazioni di Bilancio
	SQLBuilder sql2 = getHome(userContext, Var_bilancioBulk.class).createSQLBuilder();
	sql2.addSQLJoin("VAR_BILANCIO.ESERCIZIO_PDG_VARIAZIONE","PDG_VARIAZIONE.ESERCIZIO");
	sql2.addSQLJoin("VAR_BILANCIO.PG_VARIAZIONE_PDG","PDG_VARIAZIONE.PG_VARIAZIONE_PDG");
	if (varBilancio.getPg_variazione() != null){
		sql2.openParenthesis("AND");
		sql2.addSQLClause("OR", "VAR_BILANCIO.CD_CDS", sql2.NOT_EQUALS, varBilancio.getCd_cds());
		sql2.addSQLClause("OR", "VAR_BILANCIO.ESERCIZIO", sql2.NOT_EQUALS, varBilancio.getEsercizio());
		sql2.addSQLClause("OR", "VAR_BILANCIO.TI_APPARTENENZA", sql2.NOT_EQUALS, varBilancio.getTi_appartenenza());
		sql2.addSQLClause("OR", "VAR_BILANCIO.PG_VARIAZIONE", sql2.NOT_EQUALS, varBilancio.getPg_variazione());
		sql2.closeParenthesis();
	}
	sql.addSQLNotExistsClause("AND",sql2);
    
    sql.addOrderBy("PDG_VARIAZIONE.ESERCIZIO, PDG_VARIAZIONE.PG_VARIAZIONE_PDG");
    
	return sql;
}
public SQLBuilder selectVar_stanz_resByClause(UserContext userContext, Var_bilancioBulk varBilancio, Var_stanz_resBulk var_stanz_res, CompoundFindClause clauses) throws ComponentException {
    if (varBilancio.getEsercizio_res() == null || (varBilancio.getEsercizio_res() != null && varBilancio.getEsercizio_importi() == null))
    	throw new ApplicationException("Valorizzare l'esercizio residuo.");
	Var_stanz_resHome home = (Var_stanz_resHome)getHome(userContext, var_stanz_res);

	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND", "STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
	sql.addSQLClause("AND", "ESERCIZIO_RES", SQLBuilder.EQUALS, varBilancio.getEsercizio_importi());
	sql.addClause(clauses);

	// Effettuo la ricerca delle variazioni allo stanziamento residuo non associate a Variazioni di Bilancio definitive
	SQLBuilder sql2 = getHome(userContext, Var_bilancioBulk.class).createSQLBuilder();
	sql2.addSQLJoin("VAR_BILANCIO.ESERCIZIO_VAR_STANZ_RES","VAR_STANZ_RES.ESERCIZIO");
	sql2.addSQLJoin("VAR_BILANCIO.PG_VAR_STANZ_RES","VAR_STANZ_RES.PG_VARIAZIONE");
	sql2.addSQLClause("AND", "VAR_BILANCIO.STATO", SQLBuilder.EQUALS, Var_bilancioBulk.DEFINITIVA);
	if (varBilancio.getPg_variazione() != null){
		sql2.openParenthesis("AND");
		sql2.addSQLClause("OR", "VAR_BILANCIO.CD_CDS", SQLBuilder.NOT_EQUALS, varBilancio.getCd_cds());
		sql2.addSQLClause("OR", "VAR_BILANCIO.ESERCIZIO", SQLBuilder.NOT_EQUALS, varBilancio.getEsercizio());
		sql2.addSQLClause("OR", "VAR_BILANCIO.TI_APPARTENENZA", SQLBuilder.NOT_EQUALS, varBilancio.getTi_appartenenza());
		sql2.addSQLClause("OR", "VAR_BILANCIO.PG_VARIAZIONE", SQLBuilder.NOT_EQUALS, varBilancio.getPg_variazione());
		sql2.closeParenthesis();
	}
	sql.addSQLNotExistsClause("AND",sql2);
	return sql;
}

/**
  *
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Voce_f_saldi_cmpBulk
  *
  * Nome: Richiesta di ricerca di una Voce_f_saldi_cmpBulk
  * Pre: E' stata generata la richiesta di ricerca di una Voce_f_saldi_cmpBulk
  * Post: Viene restituito l'SQLBuilder per filtrare le Voce_f_saldi_cmpBulk da legare al dettaglio della variazione
  *
  * @param userContext Lo userContext che ha generato la richiesta
  * @param varDett l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param voce l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  * 		  costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  * 		   della query.
  *
**/
public SQLBuilder selectVoceFSaldiByClause(UserContext userContext, Var_bilancio_detBulk varDett, V_assestato_voceBulk voce, CompoundFindClause clauses) throws ComponentException {

	V_assestato_voceHome voceHome = (V_assestato_voceHome)getHome(userContext, voce);
	Var_bilancioBulk varBilancio = varDett.getVarBilancio();
	if (varBilancio.getEsercizio_res() == null || (varBilancio.getEsercizio_res() != null && varBilancio.getEsercizio_importi() == null))
    	throw new ApplicationException("Valorizzare l'esercizio residuo.");

	SQLBuilder sql = voceHome.createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, varDett.getEsercizio());
	sql.addSQLClause("AND", "ESERCIZIO_RES", SQLBuilder.EQUALS, varBilancio.getEsercizio_importi());
	sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, Utility.createCdrComponentSession().getCdrEnte(userContext).getCd_centro_responsabilita());
	sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.EQUALS, Voce_f_saldi_cdr_lineaBulk.TIPO_APPARTENENZA_CNR);
	sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, varDett.getTi_gestione());
	sql.addClause(clauses);
	return sql;
}
/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	return bulk;
}
/**
  *
  *	Viene richiesta la validazione di una Variazione di Bilancio 
  * a seguito di una richiesta di creazione della stessa
  *		  1 - controllo campi chiave primaria
  *		  2 - controllo campi "not nullable"
  *		  3 - controllo variazione
  *
  *	Pre-post_conditions
  *
  * Nome: Validazione 1) , 2) e 3) superate
  * Pre: Viene richiesta la validazione della Variazione di Bilancio
  * Post: Viene validato l'OggettoBulk pronto per la fase di inserimento
  *
  * Nome: Almeno una delle validazioni 1) , 2) o 3) NON superate
  * Pre: Viene richiesta la validazione della Variazione di Bilancio
  * Post: Viene restituita una ComponentException con l'errore relativo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da validare
  *
  * Metodo privato chiamato:
  * 		verificaVariazione(Var_bilancioBulk varBilancio);
  *
**/
public void validaCreaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{
		
	if (((Var_bilancioBulk)bulk).getEsercizio_res() == null)
		throw new it.cnr.jada.comp.ApplicationException("Valorizzare l'esercizio residuo.");

	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare variazioni di bilancio ad esercizio chiuso.");

	((Var_bilancioBulk)bulk).setPg_variazione(recuperaNuovoProgressivo(userContext, ((Var_bilancioBulk)bulk)));
	((Var_bilancioBulk)bulk).setStato(Var_bilancioBulk.PROVVISORIA);

	checkSQLConstraints(userContext, ((Var_bilancioBulk)bulk), true, false);
	super.validaCreaConBulk(userContext, bulk);

	verificaVariazione(((Var_bilancioBulk)bulk));
}
/**
  *
  *	Viene richiesta la validazione di una Variazione di Bilancio
  * a seguito di una richiesta di modifica della stessa
  *		  1 - controllo campi "not nullable"
  *		  2 - controllo variazione
  *
  *	Pre-post_conditions
  *
  * Nome: Validazione 1) e 2) superate
  * Pre: Viene richiesta la validazione della Variazione di Bilancio
  * Post: Viene validato l'OggettoBulk pronto per la fase di modifica
  *
  * Nome: Almeno una delle validazioni 1) o 2) NON superate
  * Pre: Viene richiesta la validazione della Variazione di Bilancio
  * Post: Viene restituita una ComponentException con l'errore relativo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da validare
  *
  * Metodo privato chiamato:
  * 		verificaVariazione(Var_bilancioBulk varBilancio);
  *
**/
public void validaModificaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	Var_bilancioBulk varBilancio = (Var_bilancioBulk)bulk;

	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare variazioni di bilancio ad esercizio chiuso.");
		
	super.validaModificaConBulk(userContext, bulk);

	verificaVariazione(varBilancio);
}
/**
  *
  *	Viene richiesta la quadratura degli importi di una Variazione di Bilancio
  *	Quadratura: entrate = spese
  *
  *	Pre-post_conditions
  *
  * Nome: Variazione di bilancio quadrata
  * Pre: Viene richiesta la quadratura tra entrate e spese
  * Post: Viene restituito il valore TRUE
  *
  * Nome: Variazione di bilancio NON quadrata
  * Pre: Viene richiesta la quadratura tra entrate e spese
  * Post: Viene restituito il valore FALSE
  *
  * @param	varBilancio l'OggettoBulk da validare
  *
**/
private boolean verificaQuadratura(Var_bilancioBulk varBilancio) {

	return varBilancio.verificaQuadratura();
}
/**
  *
  *	Viene richiesta la validazione di una Variazione di Bilancio
  *
  *	Pre-post_conditions
  *
  * Nome: Variazione di bilancio quadrata
  * Pre: Viene richiesta la quadratura degli importi di una Variazione di Bilancio
  * Post: Viene validato l'OggettoBulk
  *
  * Nome: Variazione di bilancio NON quadrata
  * Pre: Viene richiesta la quadratura degli importi di una Variazione di Bilancio
  * Post: Viene restituita una ComponentException con il messaggio:
  * 		"Non è verificata la quadratura"
  *
  * Nome: Variazione di bilancio con dettagli
  * Pre: Viene richiesta l'esistenza di dettagli legati alla Variazione di Bilancio
  * Post: Viene validato l'OggettoBulk
  *
  * Nome: Variazione di bilancio SENZA dettagli
  * Pre: Viene richiesta l'esistenza di dettagli legati alla Variazione di Bilancio
  * Post: Viene restituita una ComponentException con il messaggio:
  * 		"Inserire almeno un dettaglio."
  *
  * @param	varBilancio l'OggettoBulk da validare
  *
  * Metodo privato chiamato:
  * 		verificaQuadratura(Var_bilancioBulk varBilancio);
  *
**/
private void verificaVariazione(Var_bilancioBulk varBilancio) throws ComponentException{
 	if (varBilancio.getDettagli().isEmpty()) 
		throw new it.cnr.jada.comp.ApplicationException("Inserire almeno un dettaglio.");
}
public List findEsercizi_res(CNRUserContext userContext,Var_bilancioResiduiBulk var_bilancio) throws it.cnr.jada.comp.ComponentException {
	List lista;
	try {
			Esercizio_baseHome home = (Esercizio_baseHome)getHome(userContext,Esercizio_baseBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
		    sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.LESS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		    sql.addOrderBy("ESERCIZIO DESC");
			Broker broker = home.createBroker(sql);
			lista = home.fetchAll(broker);
			broker.close();
	}catch(Exception e) {
		throw handleException(e);
	}
	return lista;	
}
public List findEsercizi_res(CNRUserContext userContext,Var_bilancioBulk var_bilancio) throws it.cnr.jada.comp.ComponentException {
	List lista;
	try {
			Esercizio_baseHome home = (Esercizio_baseHome)getHome(userContext,Esercizio_baseBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
		    sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.LESS_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		    if( var_bilancio.getVar_stanz_res()!=null && var_bilancio.getEsercizio_var_stanz_res()!=null)
		    	sql.addSQLClause("AND", "ESERCIZIO",SQLBuilder.NOT_EQUALS,var_bilancio.getEsercizio());
		    else if(var_bilancio.getPdg_variazione()!=null && var_bilancio.getPg_variazione_pdg()!=null)
		    	sql.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,var_bilancio.getEsercizio());
		    
		    sql.addOrderBy("ESERCIZIO DESC");
			Broker broker = home.createBroker(sql);
			lista = home.fetchAll(broker);
			broker.close();
	}catch(Exception e) {
		throw handleException(e);
	}
	return lista;	
}
/**
 * Crea la ComponentSession da usare per effettuare le operazioni relative alle Piano dei Conti Finanziario
 *
 * @return PDCFinComponentSession l'istanza di <code>PDCFinComponentSession</code> che serve per gestire il Piano dei Conti Finanziario
 */
private PDCFinComponentSession createPDCFinComponentSession() throws ComponentException 
{
	try
	{
		return (PDCFinComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_PDCFinComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/*
 *  creazione Variazione Bilancio di Regolarizzazione
 *    PreCondition:
 *      E' stata generata la richiesta di creazione di una Variazione di Bilancio di regolarizzazione
 *    PostCondition:
 *      Viene creata una variazione di bilancio di regolarizzazione a partire dal 
 *       mandato di regolarizzazione associato.
 *      Viene creata una reversaleTerzo (metodo creaReversaleTerzo).
 *      Viene creato un documento generico attivo (metodo docGenerico_creaDocumentoGenerico). Viene creata una 
 *		 riga di documento generico (metodo docGenerico_creaDocumentoGenericoRiga) per ogni scadenza dell'accertamento 
 *		 (specificato dall'utente) non ancora associata a documenti amministrativi.
 *      Vengono create tante righe di reversale quante sono quelle del documento generico (metodo creaReversaleRiga).
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param mandato <code>MandatoBulk</code> il mandato di regolarizzazione
 *
 * @return variazione <code>Var_bilancioBulk</code> la variazione di regolarizzazione creata
 */
public Var_bilancioBulk creaVariazioneBilancioDiRegolarizzazione(UserContext userContext, MandatoBulk mandato ) throws ComponentException
{
	try
	{
		// Inizializzo il Cd_unita_organizzativa/cd_cds con l'Unita Organizzativa Ente e 
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);

		// BILANCIO PREVENTIVO
		Bilancio_preventivoBulk bilancio = (Bilancio_preventivoBulk) getHome(userContext, Bilancio_preventivoBulk.class).findByPrimaryKey(new Bilancio_preventivoBulk(uoEnte.getCd_unita_padre(),mandato.getEsercizio(),Elemento_voceHome.APPARTENENZA_CNR));

		// VARIAZIONE BILANCIO
		Var_bilancioBulk varBilancio = new Var_bilancioBulk();

		varBilancio.setToBeCreated();
		varBilancio.setUser( mandato.getUser() );
		varBilancio.setBilancio(bilancio);
		varBilancio.setEsercizio_res( new Esercizio_baseBulk(mandato.getEsercizio()) );
		varBilancio.setTi_variazione( Var_bilancioBulk.VAR_REGOLARIZZAZIONE );
		varBilancio.setDs_variazione("Variazione Automatica per Regolarizzazione Contabile effettuata dal CDS " + mandato.getCd_cds() +
				                    " tramite mandato nr. " + mandato.getPg_mandato() + " del " + mandato.getEsercizio());
		varBilancio.setMandato( (MandatoIBulk)mandato );

		// DETTAGLI VARIAZIONE	
		PDCFinComponentSession PDCFinSession = createPDCFinComponentSession();
		BigDecimal totaleVoceCnr = new BigDecimal(0);
		PrimaryKeyHashtable hashAssestatoVociCnr = new PrimaryKeyHashtable();
		
		for ( Iterator i = ((MandatoIBulk)mandato).getMandato_rigaColl().iterator(); i.hasNext(); )
		{
			Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) getHome( userContext, ObbligazioneBulk.class  ).findByPrimaryKey( new ObbligazioneBulk(riga.getCd_cds(), riga.getEsercizio_obbligazione(), riga.getEsercizio_ori_obbligazione(), riga.getPg_obbligazione()) );
			Obbligazione_scadenzarioHome osHome = (Obbligazione_scadenzarioHome)getHome( userContext, Obbligazione_scadenzarioBulk.class  );
			Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk) osHome.findByPrimaryKey( new Obbligazione_scadenzarioBulk(riga.getCd_cds(), riga.getEsercizio_obbligazione(), riga.getEsercizio_ori_obbligazione(), riga.getPg_obbligazione(), riga.getPg_obbligazione_scadenzario()) );
			os.setObbligazione_scad_voceColl( new BulkList( osHome.findObbligazione_scad_voceList(userContext, os )));
			for ( Iterator osvIterator = os.getObbligazione_scad_voceColl().iterator(); osvIterator.hasNext(); )
			{
				Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk)osvIterator.next();
				Elemento_voceBulk voceCds = (Elemento_voceBulk) getHome( userContext, EV_cds_spese_capitoloBulk.class  ).findByPrimaryKey( new Elemento_voceBulk(obbligazione.getCd_elemento_voce(), obbligazione.getEsercizio(), obbligazione.getTi_appartenenza(), obbligazione.getTi_gestione()) );
				java.lang.String voceCnr = PDCFinSession.getVoceCnr(userContext, voceCds, osv.getLinea_attivita());

				if (voceCnr==null) 
					throw new it.cnr.jada.comp.ApplicationException("Non è stato possibile ottenere, partendo dalla voce del CDS " + voceCds.getCd_elemento_voce() + " la corrispondente voce Ente.");

				totaleVoceCnr = (BigDecimal) hashAssestatoVociCnr.get( voceCnr );			
				if ( totaleVoceCnr == null || totaleVoceCnr.compareTo(new BigDecimal(0)) == 0)
					hashAssestatoVociCnr.put( voceCnr, osv.getIm_voce());
				else
				{
					totaleVoceCnr = totaleVoceCnr.add( osv.getIm_voce() );
					hashAssestatoVociCnr.put( voceCnr, totaleVoceCnr );
				}			
			}
		}

		java.lang.String key;
		for ( Enumeration e = hashAssestatoVociCnr.keys(); e.hasMoreElements(); ) 
		{
			key = (java.lang.String)e.nextElement();

			//riga di bilancio
			Var_bilancio_detBulk varBilDett = new Var_bilancio_detBulk();

			//voce di bilancio
			varBilDett.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
			varBilDett.setCd_voce(key);

			//importo
			varBilDett.setIm_variazione( ((BigDecimal) hashAssestatoVociCnr.get( key )).negate() );

			//associazione riga alla variazione
			varBilDett.setToBeCreated();
			varBilancio.addToDettagli( varBilDett );

			V_assestato_voceBulk assestatoVoce = ((Var_bilancio_detHome)getHome(userContext, Var_bilancio_detBulk.class)).caricaVoceFSaldi(userContext, varBilDett, varBilancio); 

			if (assestatoVoce==null)
				throw new ApplicationException("Attenzione, non è stato possibile recuperare i saldi per la voce Ente " + key + ".");

			varBilDett.setVoceFSaldi( assestatoVoce );
		}

		varBilancio = (Var_bilancioBulk) creaConBulk( userContext, varBilancio);
		salvaDefinitivo(userContext, varBilancio);
		return varBilancio;
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
}
