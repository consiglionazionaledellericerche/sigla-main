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

package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;

import java.io.Serializable;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.sql.*;
import java.util.Optional;
import java.util.StringTokenizer;
import java.rmi.*;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su CdrBulk
 */

public class CDRComponent extends it.cnr.jada.comp.CRUDComponent implements ICDRMgr, java.io.Serializable, Cloneable {


//@@<< CONSTRUCTORCST
	public  CDRComponent()
	{
//>>

//<< CONSTRUCTORCSTL
		/*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

	}
/* Gestisce l'aggiornamento dell'attributo esercizio fine del Cdr e di tutte le linee di attività che
   dipendono da quel Cdr.
 *  
 * Nome: Modifica dell'attributo esercizio fine 
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr e' stata generata e
 *       tutti i controlli sono stati superati
 * Post: Tutti gli esercizi fine delle linee di attività definite per
 *		 quel Cdr e che hanno esercizio fine maggiore rispetto a quello del Cdr sono stati aggiornati
 *
 * Nome: Modifica dell'attributo esercizio fine - Errore
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr e' stata generata e
 *       la validazione di tale attributo non e' stata superata ( metodo 'verificaEsercizioFine')
 * Post: Una Application Exception viene generata per segnalare all'utente l'impossibilità ad effettuare tale
 *		 modifica
*/

private void aggiornaEsercizioFine(UserContext userContext, CdrBulk cdr) throws it.cnr.jada.comp.ComponentException 
{
	try
	{
		CdrHome cdrHome = (CdrHome) getHome( userContext, CdrBulk.class);

		//rileggo la Uo dal db per verificare se e' stato modificato l'esercizio fine
		CdrBulk cdrDB = (CdrBulk) cdrHome.findByPrimaryKey( new CdrKey(cdr.getCd_centro_responsabilita()));
		if ( cdrDB.getEsercizio_fine() == null && cdr.getEsercizio_fine() == null )
			return;
		if ( cdrDB.getEsercizio_fine() != null && cdrDB.getEsercizio_fine().compareTo(cdr.getEsercizio_fine()) == 0 )
			return;

		verificaEsercizioFine( userContext, cdr );
		
		cdrHome.aggiornaEsercizioFinePerLineeAttivita( cdr.getEsercizio_fine(),  cdr.getCd_centro_responsabilita());				
	
	} catch (Throwable e) 
	{
		throw handleException(cdr,e);
	} 

}
public CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {

	try {
		it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
		user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

		CdrBulk cdr = new CdrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
		cdr = (CdrBulk)getHome(userContext, CdrBulk.class, null, getFetchPolicyName("find")).findByPrimaryKey(cdr);
		Optional.ofNullable(cdr).orElseThrow(()->new ApplicationException("Errore: CDR di Scrivania non individuato!"));
		cdr.setUnita_padre((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa())));
		return cdr;
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw new ComponentException(e);
	}
}
/**
 * Esegue una operazione di creazione di un CdrBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di Cdr dipendente da un CDS di tipo AREA
 * Pre:  La richiesta di creazione di un Cdr che dipende da una unita' organizzativa definita per un CDS di tipo 
 *       Area di ricerca è stata generata
 * Post: Viene generate una ApplicationException con il messaggio "Non è possibile aggiungere un CDR ad un'area
 * 		 di ricerca"
 *
 * Nome: Creazione di Cdr dipendente da un CDS di tipo SAC
 * Pre:  La richiesta di creazione di un Cdr che dipende da una unita' organizzativa definita per un CDS di tipo 
 *       Struttura Amministrativa Centrale è stata generata
 * Post: Un Cdr di secondo livello viene creato con i dati inseriti dall'utente, se l'utente non ha specificato un 
 *       codice proprio del Cdr ne viene generato uno automaticamente; il cdr di afferenza assegnato e' il CDR di 
 *       primo livello dell'Unita' organizzativa da cui dipende il Cdr appena creato.
 *
 * Nome: Creazione di Cdr dipendente da un CDS di tipo diverso da SAC
 * Pre:  La richiesta di creazione di un Cdr che dipende da una unita' organizzativa definita per un CDS di tipologia 
 *       differente da Struttura Amministrativa Centrale è stata generata
 * Post: Un Cdr di secondo livello viene creato con i dati inseriti dall'utente, se l'utente non ha specificato un 
 *       codice proprio del Cdr ne viene generato uno automaticamente; il cdr di afferenza assegnato e' il CDR
 *       responsabile dell'UO CDS. 
 *
 * Nome: Errore di unita' organizzativa inesistente
 * Pre:  L'unita' organizzativa specificata dall'utente non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Cdr afferenza inesistente
 * Pre:  Il Cdr di afferenza del Cdr da creare non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile del Cdr non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di chiave duplicata
 * Pre:  Esiste già un CdrBulk persistente che possiede la stessa chiave
 * 		 primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore per Esercizio Fine
 * Pre:  L'attributo esercizio fine del Cdr da creare non e' valido (la validazione e' eseguita dal metodo
 *       'verificaEsercizioFine')
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il CdrBulk che deve essere creato
 * @return	il CdrBulk risultante dopo l'operazione di creazione.
 */	

public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException
{
	String keyAff;
	try
	{
		CdrBulk cdrBulk = (CdrBulk) bulk;

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext))
			throw new ApplicationException("Non è possibile creare cdr ad esercizio chiuso.");
	
		CdrHome cdrHome = (CdrHome) getHomeCache(userContext).getHome( bulk.getClass());

		Unita_organizzativaBulk uoBulk = cdrBulk.getUnita_padre();
		
		verificaEsercizioFine( userContext, cdrBulk)	;
		
		try
		{
		   lockBulk( userContext,uoBulk );
		}
		catch ( it.cnr.jada.persistency.FindException e )
		{
			throw new ApplicationException( "Codice Unita Organizzativa " + uoBulk.getCd_unita_organizzativa() + " inesistente" );
		}	
		
		CdsBulk cdsBulk = uoBulk.getUnita_padre();
		if ( cdsBulk == null )
			throw  new ApplicationException( "Codice CDS inesistente" );

		
		if ( cdsBulk.getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ))
			keyAff = cdrHome.findCdCdrAfferenzaForSAC( cdrBulk.getUnita_padre().getCd_unita_organizzativa());							
		else if (cdsBulk.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_AREA))
			throw new ApplicationException("Non è possibile aggiungere CDR ad un'area di ricera");
		else // Macro
			keyAff = cdrHome.findCdCdrAfferenzaForMacro( cdsBulk.getCd_unita_organizzativa());
			
		if ( keyAff == null ) //non esiste CDR Afferenza
			throw  new ApplicationException( "Non esiste CDR di primo livello" );
		cdrBulk.setCd_cdr_afferenza( keyAff );			
		cdrBulk.setLivello( CdrHome.CDR_SECONDO_LIVELLO );
		if ( cdrBulk.getCd_proprio_cdr() == null || cdrBulk.getCd_proprio_cdr().equals(""))
		{
			String key = cdrHome.creaNuovoCodiceCDR(  cdrBulk.getUnita_padre().getCd_unita_organizzativa(), cdrBulk.getLivello());
			cdrBulk.setCd_proprio_cdr( getLunghezza_chiavi().formatCdrKey( userContext,key,  cdrBulk.getLivello() ) );
		}
		else
			cdrBulk.setCd_proprio_cdr( getLunghezza_chiavi().formatCdrKey( userContext,cdrBulk.getCd_proprio_cdr(), cdrBulk.getLivello() ));
		
		cdrBulk.setCd_centro_responsabilita( cdrBulk.getUnita_padre().getCd_unita_organizzativa().concat(".").concat(cdrBulk.getCd_proprio_cdr()));

//		if ( cdrBulk.getCd_responsabile() != null )
//			cdrBulk.setResponsabile( loadResponsabile( cdrBulk.getCd_responsabile() ));

		if ( cdrBulk.getResponsabile() != null && cdrBulk.getResponsabile().getCd_terzo() != null )
		{
			try	{ lockBulk( userContext,cdrBulk.getResponsabile());	}
			catch (it.cnr.jada.persistency.FindException e)
			{
				throw handleException(new ApplicationException( "Responsabile non esiste"));
			}
		}	

		insertBulk( userContext, cdrBulk );
		return cdrBulk;
	}
	catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) 
	{
		if (e.getPersistent() != bulk)
			throw handleException(bulk,e);
		try 
		{
			throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata",e,bulk,(OggettoBulk)getHome(userContext,bulk).findByPrimaryKey(bulk)));
		} catch(Throwable ex) 
		{
			throw handleException(bulk,ex);
		}
	}			
	
	catch (Exception e)
	{
		throw handleException(bulk,e);
	}		
}
/**
 * Esegue una operazione di eliminazione di CdrBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di un Cdr non responsabile dell'UO
 * Pre:  La richiesta di cancellazione di un Cdr non responsabile di una Unita' Organizzativa e' stata generata
 * Post: Il Cdr e' stato cancellato
 *
 * Nome: Cancellazione di un Cdr responsabile dell'UO
 * Pre:  La richiesta di cancellazione di un Cdr responsabile di una Unita' Organizzativa e' stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di CdrBulk che deve essere cancellata 
 */

public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try
	{
		CdrBulk cdr = (CdrBulk) bulk;

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,cdr))
			throw new ApplicationException("Non è possibile eliminare cdr con esercizio di terminazione chiuso.");
		
		if ( Long.parseLong( cdr.getCd_proprio_cdr()) == 0 )
			throw new it.cnr.jada.comp.ApplicationException( "Non è possibile cancellare il CDR responsabile dell'UO");
		makeBulkPersistent( userContext,bulk );
	}
	catch(it.cnr.jada.persistency.sql.ReferentialIntegrityException e) 
	{
		throw handleException(new ApplicationException( "La cancellazione non e' consentita in quanto la struttura organizzativa selezionata e' utilizzata. Si consiglia l'impostazione dell'Esercizio di Terminazione. "));
	}	
	
	catch (Throwable e) 
	{
		throw handleException(bulk,e);
	} 
}
/*
 * Estrae l'UO dell'utente collegato
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @return istanza di Unita_organizzativaBulk
 */

private CdsBulk getCDSUtente(UserContext userContext) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	it.cnr.contab.utenze00.bulk.UtenteComuneBulk aU = new it.cnr.contab.utenze00.bulk.UtenteComuneBulk(userContext.getUser());

	
	it.cnr.contab.utenze00.bulk.UtenteComuneHome aH = (it.cnr.contab.utenze00.bulk.UtenteComuneHome)getHome(userContext, aU.getClass());

	 aU=(it.cnr.contab.utenze00.bulk.UtenteComuneBulk)aH.findByPrimaryKey(aU);	

	 if(aU.getTi_utente().equals(it.cnr.contab.utenze00.bulk.UtenteBulk.UTENTE_AMMINISTRATORE_KEY)) {
	  if(aU.getCd_cds_configuratore().equals("*")) {
	   EnteHome aEnteHome = (EnteHome)getHome(userContext,EnteBulk.class);
	   java.util.List aL = aEnteHome.findAll();
	   if(aL.size() > 0)
	    return (EnteBulk)aL.get(0);
	   else
	    throw new ApplicationException("CDS Ente non trovato!");
	  } else {
	   CdsHome aCDSHome = (CdsHome)getHome(userContext,CdsBulk.class);
	   CdsBulk aCDS = new CdsBulk(aU.getCd_cds_configuratore());
	   return (CdsBulk)aCDSHome.findByPrimaryKey(aCDS);
	  }
	 }
	 
	 CdrBulk cdr = new CdrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
	 cdr = (CdrBulk)getHome(userContext, CdrBulk.class, null, getFetchPolicyName("find")).findByPrimaryKey(cdr);

	 CdrHome aCDRHome = (CdrHome)getHome(userContext,CdrBulk.class);
	 CdrBulk aCDR = (CdrBulk)aCDRHome.findByPrimaryKey(cdr);
	  
	 Unita_organizzativaHome aUOHome = (Unita_organizzativaHome)getHome(userContext,Unita_organizzativaBulk.class);
	 Unita_organizzativaBulk aUO = (Unita_organizzativaBulk)aUOHome.findByPrimaryKey(aCDR.getUnita_padre());

	 CdsHome aCDSHome = (CdsHome)getHome(userContext,CdsBulk.class);
	 CdsBulk aCDS = new CdsBulk(aUO.getUnita_padre().getCd_unita_organizzativa());
	 return (CdsBulk)aCDSHome.findByPrimaryKey(aCDS);
}
/**
 * Restituisce la Home dell'EJB Lunghezza_chiavi component  
 */
private it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession getLunghezza_chiavi( ) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		return (Lunghezza_chiaviComponentSession) EJBCommonServices.createEJB( "CNRCONFIG00_EJB_Lunghezza_chiaviComponentSession", Lunghezza_chiaviComponentSession.class );
	}
	catch (Exception e )
	{
		throw handleException( e ) ;
	}		
}
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	bulk = super.inizializzaBulkPerModifica(userContext,bulk);
	if (isEsercizioChiuso(userContext,(CdrBulk)bulk))
		bulk = asRO(bulk,"Non è possibile modificare cdr con esercizio di terminazione chiuso.");
	return bulk;
}
/**
 * Esegue una operazione di inserimento di un CdrBulk nel database
 *
 * Pre-post-conditions:
 *
 * Nome: Inserimento di Cdr 
 * Pre:  La richiesta di inserimento di un Cdr e' stata generata
 * Post: Il Cdr e' stato inserito nel database e la stored procedure che genera
 * 		 la struttura degli elementi voci dipendenti da Cdr e' stata richiamata
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	o il CdrBulk che deve essere inserito
*/
public void insertBulk(UserContext userContext,OggettoBulk o) throws PersistencyException,ComponentException 
{
	super.insertBulk(userContext,o);
	if ( o instanceof CdrBulk )
	{
		try
		{
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
			if (!parCnr.getFl_nuovo_pdg()) {
				CdrBulk cdr = (CdrBulk) o;
				lockBulk( userContext, cdr );
				/* CNRCTB001.creaEsplVociCDR(?,?) */
				LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
						"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
						+ "CNRCTB001.creaEsplVociCDR(?,?,?)}",false,this.getClass());
				try
				{
					cs.setObject( 1, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio() );
					cs.setString( 2, cdr.getCd_centro_responsabilita());
					cs.setString( 3, null); // passando null come user, i dati relativi a dacr/duva/utcr/utuv vengono ereditati dal cdr specificato
					cs.executeQuery();
				}
				catch ( Exception e )
				{
					throw handleException( o, e );
				}
				finally
				{
					cs.close();
				}
			}
		}
		catch ( Exception e )
		{
			throw handleException(e);
		}
	}		
}
/**
 * TRUE se il Cdr passato come parametro e' Ente
 * FALSE altrimenti
 *
*/
public boolean isCdrEnte(UserContext userContext,CdrBulk cdr) throws ComponentException {
	try {
		return ((CdrHome)getHome(userContext, CdrBulk.class)).isEnte(cdr);
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public CdrBulk getCdrEnte(UserContext userContext)throws ComponentException{	
	try {
		Unita_organizzativaHome unita_organizzativaHome = (Unita_organizzativaHome)getHome(userContext,Unita_organizzativaBulk.class);
		SQLBuilder sql = unita_organizzativaHome.createSQLBuilderEsteso();
		sql.addClause("AND", "cd_tipo_unita", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
		Broker broker = unita_organizzativaHome.createBroker(sql);
		Unita_organizzativaBulk unitaEnte = null;
		if (broker.next()){
			unitaEnte = (Unita_organizzativaBulk)broker.fetch(Unita_organizzativaBulk.class);
		}
		if (unitaEnte == null)
			throw new ApplicationException("Unità ENTE non trovata!");
		CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class,"V_CDR_VALIDO");
		SQLBuilder sqlCdr = cdrHome.createSQLBuilderEsteso();
		sqlCdr.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, unitaEnte.getCd_unita_organizzativa());
		Broker brokerCdr = cdrHome.createBroker(sqlCdr);
		if (brokerCdr.next()){
			return (CdrBulk)brokerCdr.fetch(CdrBulk.class);
		}
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	return null;
}
/**
 * TRUE se il Cdr di scrivania e' Ente
 * FALSE altrimenti
 *
*/
public boolean isEnte(UserContext userContext) throws ComponentException {
	return isCdrEnte(userContext, cdrFromUserContext(userContext));
}
protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,CdrBulk cdr) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext,cdr.getEsercizio_fine(),cdr.getUnita_padre().getCd_unita_padre());
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/**
 * Esegue una operazione di modifca di un CdrBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica di attributi diversi da esercizio fine
 * Pre:  La richiesta di modifica di un attributo diverso da esercizio fine per un Cdr è stata generata
 * Post: Il Cdr e' stato modificato
 *
 * Nome: Modifica dell'attributo esercizio fine - Errore 
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr e' stata generata
 *       e la verifica della correttezza del nuovo esercizio (eseguita dal metodo 'verificaEsercizioFine') non e'
 *		 stata superata
 * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
 *
 * Nome: Modifica dell'attributo esercizio fine - Ok
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr e' stata generata e
 *       tutti i controlli sono stati superati
 * Post: L'esercizio fine del Cdr e' stato aggiornato e tutti gli esercizi fine delle linee di attività definite per
 *		 quel Cdr e che hanno esercizio fine maggiore rispetto a quello del Cdr sono stati aggiornati 
 *       (metodo 'aggiornaEsercizioFine')
 *
 * Nome: Errore di Responsabile inesistente
 * Pre:  Il Codice Terzo definito come responsabile del Cdr non è presente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il CdrBulk che deve essere modificato
 * @return	il CdrBulk risultante dopo l'operazione di modifica
 */	

public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try
	{
		CdrBulk cdrBulk = (CdrBulk) bulk;

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,cdrBulk))
			throw new ApplicationException("Non è possibile modificare cdr con esercizio di terminazione chiuso.");
		
		if ( cdrBulk.getResponsabile() != null && cdrBulk.getResponsabile().getCd_terzo() != null )
		{
			try	{ lockBulk( userContext,cdrBulk.getResponsabile());	}
			catch (it.cnr.jada.persistency.FindException e)
			{
				throw handleException(new ApplicationException( "Responsabile non esiste"));
			}
		}
//		getHomeCache().fetchAll();
		
		//aggiornamento a casacata dell'esercizio fine
		aggiornaEsercizioFine( userContext, cdrBulk);
			
		makeBulkPersistent( userContext,cdrBulk );
		return cdrBulk;
	} catch (Throwable e) 
	{
		throw handleException(bulk,e);
	} 
}
/*
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su CdrBulk, per visualizzare solo
 * i cdr validi per l'esercizio di scrivania
 *	
 * Pre-post-conditions:
 *
 * Nome: Richiesta di ricerca CdrBulk
 * Pre:  E' stata generata la richiesta di ricerca di un CdrBulk
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
 *       clausola che il cdr sia valido per l'esercizio presente in scrivania
 *       Inoltre le unità organizzative di appartenenza dei cdr ricercati devono essere tutte e sole quelle appartenenti a:
 *         1. CDS a cui appartiene il CDR specificato in UserContext (Utente comune di scrivania)
 *         2. CDS di influenza dell'utente specificato in UserContext (Utente amministratore di utenze)
 *         3. TUTTI I CDS nel caso il CDS di influenza dell'utente specificato in UserContext (Utente amministratore di utenze) sia *
 *         4. TUTTI I CDS nel caso il CDR specificato in User context (Utente comune di scrivania) sia il CDR ENTE
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @param bulk istanza di CdrBulk che deve essere utilizzata per la ricerca
 * @return il SQLBuilder con la clausola aggiuntive 
 */

protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = (SQLBuilder)super.select( userContext, clauses, bulk );
	sql.addClause( "AND", "esercizio_inizio", sql.LESS_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
	sql.addClause( "AND", "esercizio_fine", sql.GREATER_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());	

	CdsBulk aCDS = getCDSUtente(userContext);
	if(!aCDS.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
	 sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.LIKE, aCDS.getCd_unita_organizzativa()+"%");
	return sql;
}
/*
 * Aggiunge alcune clausole a tutte le operazioni di ricerca dell'unita organizzativa, padre del Cdr
 *	
 * Pre-post-conditions:
 *
 * Nome: Richiesta di ricerca dell'unita organizzativa
 * Pre:  E' stata generata la richiesta di ricerca di una Unita_organizzativaBulk da utilizzare come padre
 *		 per il Cdr
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
 *       clausola che l'unita' organizzativa sia valida per l'esercizio di scrivania e che l'unità organizzativa
 *		 dipenda da un cds con tipologia diversa da SAC e da PNIR.
 *       Inoltre le unità organizzative devono essere tutte e sole quelle appartenenti a:
 *         1. CDS a cui appartiene il CDR specificato in UserContext (Utente comune di scrivania)
 *         2. CDS di influenza dell'utente specificato in UserContext (Utente amministratore di utenze)
 *         3. TUTTI I CDS nel caso il CDS di influenza dell'utente specificato in UserContext (Utente amministratore di utenze) sia *
 *         4. TUTTI I CDS nel caso il CDR specificato in User context (Utente comune di scrivania) sia il CDR ENTE
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param cdr istanza di CdrBulk
 * @param uo istanza di Unita_organizzativaBulk che deve essere utilizzata per la ricerca 
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @return il SQLBuilder con la clausola aggiuntive 
 */

public SQLBuilder selectUnita_padreByClause(UserContext userContext, CdrBulk cdr, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = getHome(userContext, uo.getClass(), "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
	sql.addSQLClause("AND", "CD_TIPO_UNITA", sql.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
	sql.addSQLClause("AND", "CD_TIPO_UNITA", sql.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_PNIR);
	// 11/01/2005 Eliminato da Angelo a seguito della segnalazione n° 19
    // Ora è possibile ricercare anche le UO non apparrtenenti al CDS dell'Utente
    /*   
    CdsBulk aCDS = getCDSUtente(userContext);
	if(!aCDS.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
	 sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.LIKE, aCDS.getCd_unita_organizzativa()+"%");
	}*/
	return sql;
}
/**
 * Esegue una operazione di aggiornamento di un CdrBulk nel database
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiornamento di Cdr 
 * Pre:  La richiesta di aggiornamento di un Cdr e' stata generata
 * Post: Il Cdr e' stato aggiornato nel database e la stored procedure che aggiorna
 * 		 la struttura degli elementi voci dipendenti da Cdr e' stata richiamata
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	o il CdrBulk che deve essere inserito
*/

public void updateBulk(UserContext userContext,OggettoBulk o) throws PersistencyException,ComponentException 
{
	super.updateBulk(userContext,o);
	if ( o instanceof CdrBulk )
	{
		try
		{
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
			if (!parCnr.getFl_nuovo_pdg()) {
				CdrBulk cdr = (CdrBulk) o;
				lockBulk( userContext, cdr );
				/* CNRCTB001.creaEsplVociCDR(?,?) */
				LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
						"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
						+ "CNRCTB001.creaEsplVociCDR(?,?,?)}",false,this.getClass());
				try
				{
					cs.setObject( 1, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio() );
					cs.setString( 2, cdr.getCd_centro_responsabilita());
					cs.setString( 3, null); // passando null come user, i dati relativi a dacr/duva/utcr/utuv vengono ereditati dal cdr specificato
					cs.executeQuery();
				}
				catch ( Exception e )
				{
					throw handleException( o, e );
				}
				finally
				{
					cs.close();
				}
			}
		}
		catch ( Exception e )
		{
			throw handleException(e);
		}
	}	
}
/* Verifica la correttezza dell'attributo fine.
 *
 * Nome: Errore per Cdr responsabile UO
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr Responsabile dell'UO e' stata generata
 * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
 *
 * Nome: Errore per UO padre
 * Pre:  Per un Cdr non Responsabile dell'UO e' stata specificato un esercizio fine superiore rispetto a quello
 *		 dell'Unita Organizzativa da cui il Cdr dipende
 * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica 
 *
 * Nome: Errore per PDG
 * Pre:  Per un Cdr non Responsabile dell'UO e' stata specificato un esercizio fine 
 *       per il quale e' già stato aperto il piano di gestione
 * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica 
 *
 * Nome: Controlli superati
 * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cdr non Responsabile dell'UO e' stata generata 
 * Post: L'esercizio fine del Cdr ha superato tutti i controlli 
*/

private void verificaEsercizioFine(UserContext userContext, CdrBulk cdr) throws it.cnr.jada.comp.ComponentException 
{
	try
	{
		CdrHome cdrHome = (CdrHome) getHome( userContext, CdrBulk.class);

		// non e' possibile impostare l'es. terminaz. per CDR resposnabile UO
		if ( cdr.getCd_proprio_cdr() != null && Long.parseLong( cdr.getCd_proprio_cdr()) == 0 )
			throw new it.cnr.jada.comp.ApplicationException( "Non è possibile impostare l'esercizio di terminazione ad un CDR responsabile dell'UO");
			
		//l'esercizio fine deve essere <= dell'esercizio fine dell'UO da cui dipende
		if ( cdr.getUnita_padre().getEsercizio_fine() != null &&
			 cdr.getUnita_padre().getEsercizio_fine().compareTo( cdr.getEsercizio_fine() ) < 0 )
				throw handleException( new ApplicationException(" Esercizio di terminazione deve essere minore o uguale a " + cdr.getUnita_padre().getEsercizio_fine().toString()));

		//l'esercizio fine deve essere > dell'esercizio per cui e' gia' stato previsto un preventivo
		if ( !cdrHome.verificaEsercizioPreventivo( cdr ) )
			throw handleException( new ApplicationException("L'Esercizio di terminazione deve essere superiore agli esercizi per cui sono stati definiti dei preventivi"));
	
	} catch (Throwable e) 
	{
		throw handleException(cdr,e);
	} 

}
public java.util.List findListaCDRWS(UserContext userContext,String uo,String query,String dominio,String tipoRicerca)throws ComponentException{
	try {		
		CdrHome home = (CdrHome)getHome(userContext,CdrBulk.class);
		SQLBuilder sql = home.createSQLBuilderEsteso();//(SQLBuilder)super.select( userContext,null,new CdrBulk());
		sql.addSQLClause( "AND", "esercizio_inizio", sql.LESS_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
		sql.addSQLClause( "AND", "esercizio_fine", sql.GREATER_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!(uo.equals(ente.getCd_unita_organizzativa())))
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
		if (dominio != null && dominio.equalsIgnoreCase("codice"))
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,query);
		else if (dominio != null && dominio.equalsIgnoreCase("descrizione")){
			
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DS_CDR",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DS_CDR",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DS_CDR",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DS_CDR)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DS_CDR",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DS_CDR",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DS_CDR)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DS_CDR)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DS_CDR",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DS_CDR",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DS_CDR",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DS_CDR",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("CD_CENTRO_RESPONSABILITA,DS_CDR");
			}
		
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
}
