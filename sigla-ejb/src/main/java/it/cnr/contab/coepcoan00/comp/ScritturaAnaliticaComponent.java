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

package it.cnr.contab.coepcoan00.comp;

import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.coepcoan00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import java.util.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.config00.esercizio.bulk.*;
import java.sql.*;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

import java.io.Serializable;

public class ScritturaAnaliticaComponent extends it.cnr.jada.comp.CRUDComponent implements IScritturaAnaliticaMgr,ICRUDMgr, Cloneable, Serializable{
/**
 * ScritturaAnaliticaComponent constructor comment.
 */
public ScritturaAnaliticaComponent() {
	super();
}
/**
 *
 * Nome: Aggiornamento saldi
 * Pre:  E' stato richiesto l'aggiornamento dei saldi coan come conseguenza di un inserimento di  
 *       una nuova scrittura analitica
 * Post: E' stata richiamata la stored procedure che esegue l'aggiornamento dei saldi coan relativi ai
 *       conti di tutti i movimenti dare e avere della scrittura
 *
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_analiticaBulk</code>  che deve essere inserita
 *
 */

private void aggiornaSaldiCoan (UserContext userContext, Scrittura_analiticaBulk scrittura ) throws ComponentException
{
    try
	{
    	LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB200.aggiornaSaldoCoan(?, ?, ?, ?, ?)}",false,this.getClass());
		try
		{
			cs.setString( 1, scrittura.getCd_cds() );				
			cs.setObject( 2, scrittura.getEsercizio() );
			cs.setString( 3, scrittura.getCd_unita_organizzativa() );				
			cs.setObject( 4, scrittura.getPg_scrittura() );
			cs.setString( 5, scrittura.getUser());
			cs.executeQuery();
		}
		catch ( java.lang.Exception e )
		{
			throw new ComponentException( e );
		}
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException(e);
	}	
}
/** 
  *  Scrittura Analitica - Esercizio COEP/COAN chiuso
  *    PreCondition:
  *      L'esrcizio COEP/COAN risulta chiuso per il CdS di scrivania
  *    PostCondition:
  *      Non  viene consentita il salvataggio.
  *   
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return l'oggetto <code>OggettoBulk</code> creato
**/
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	if (isEsercizioChiuso(userContext))
		throw new ApplicationException ("Attenzione: esercizio economico chiuso o in fase di chiusura.");
		

	return super.creaConBulk(userContext, bulk);
}
/**
 *
 * Nome: Inserimento di una scrittura
 * Pre:  E' stato richiesto l'inserimento di una scrittura analitica già validata
 * Post: L'importo della scrittura viene impostato come la somma degli importi dei movimenti
 *       e il saldo coan relativo ai conti econ-patr. dei movimenti viene aggiornato
 *       (metodo aggiornaSaldiCoan)
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code>  che deve essere inserita
 * @return <code>Scrittura_analiticaBulk</code>  inserita
 *
 */

protected OggettoBulk eseguiCreaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException,PersistencyException 
{
	Scrittura_analiticaBulk scrittura = (Scrittura_analiticaBulk) bulk;
	scrittura.setIm_scrittura( scrittura.getImTotaleMov());
//	if ( scrittura.getTerzo() == null || scrittura.getTerzo().getCd_terzo() == null )
//		scrittura.setTerzo( getTerzoNullo());
	for ( Iterator i = scrittura.getMovimentiColl().iterator(); i.hasNext(); )
		((Movimento_coanBulk)i.next()).setTerzo( scrittura.getTerzo());
	makeBulkPersistent(userContext,scrittura);
	aggiornaSaldiCoan( userContext, scrittura );
	return bulk;
}
/**
 *
 * Nome: Storno e riemissione di una scrittura
 * Pre:  E' stata richiesta la cancellazione di una scrittura analitica
 * Post: E' stata richiamata la stored procedure che esegue lo storno e la riemissione della scrittura analitica ed
 *       aggiorna i saldi coan
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code>  che deve essere inserita
 *
 */

protected void eseguiEliminaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException,PersistencyException 
{
    try
	{
		Scrittura_analiticaBulk scrittura = (Scrittura_analiticaBulk) bulk;

		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB200.creaScrittStornoCoan(?, ?, ?, ?, ?)}",false,this.getClass());
		try
		{
			cs.setString( 1, scrittura.getCd_cds() );				
			cs.setObject( 2, scrittura.getEsercizio() );
			cs.setString( 3, scrittura.getCd_unita_organizzativa() );		
			cs.setObject( 4, scrittura.getPg_scrittura() );
			cs.setString( 5, scrittura.getUser());
			cs.executeQuery();
		}
		catch ( java.lang.Exception e )
		{
			throw new ComponentException( e );
		}
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException(e);
	}
}
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per inserimento di una scrittura analitica 
 * Post: La scrittura viene restituita con inizializzata la data di contabilizzazione (metodo inizializzaDataContabilizzazione)
 *       e il cds (con il cds di scrivania)
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code>  che deve essere inizializzato per inserimento
 * @return <code>Scrittura_analiticaBulk</code>  inizializzato per inserimento
 *
 */

public OggettoBulk inizializzaBulkPerInserimento (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	
	/* Gennaro Borriello - (30/09/2004 11.39.59)
	 *	In caso di esercizio COAN/COEP chiuso, blocca l'utente.
	 *	(Su indicazione di Massimo Bartolucci)
	*/ 	
	if(isEsercizioChiuso(userContext))
		throw  new it.cnr.jada.comp.ApplicationException("Attenzione: esercizio economico chiuso o in fase di chiusura.");

	try
	{
		bulk = super.inizializzaBulkPerInserimento( userContext, bulk);
		if ( bulk instanceof Scrittura_analiticaBulk )
		{
			Scrittura_analiticaBulk scrittura = (Scrittura_analiticaBulk) bulk;
			scrittura = inizializzaDataContabilizzazione( userContext, scrittura );
			scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
			scrittura.setCd_cds_documento( scrittura.getCd_cds() );
			return scrittura;
		}
		
        if(isEsercizioChiuso(userContext))
           	return asRO(bulk,null);
				 
		return bulk;
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per modifica di una scrittura analitica
 * Post: La scrittura viene restituita con inizializzata la collezione di movimenti dare e movimenti avere
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un movimento coan
 * Post: Il movimento viene restituito con l'inizializzazione di default
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un saldo coan
 * Post: Il saldo viene restituito con l'inizializzazione di default
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> che devono essere inizializzati per modifica
 * @return <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> inizializzati per modifica
 *
 */
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try 
	{
		bulk = super.inizializzaBulkPerModifica( userContext, bulk );
		if ( bulk instanceof Scrittura_analiticaBulk )
		{
			Scrittura_analiticaBulk scrittura = (Scrittura_analiticaBulk) bulk;
			scrittura.setMovimentiColl( new BulkList( ((Scrittura_analiticaHome) getHome( userContext, scrittura.getClass())).findMovimentiColl( userContext, scrittura )));
			/*
			if ( scrittura.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
				scrittura.setTerzo( getTerzoNullo());
			else 
				scrittura.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( scrittura.getTerzo()));
			*/
		}
		/*
		else if ( bulk instanceof Saldo_coanBulk )
		{
			Saldo_coanBulk saldo = (Saldo_coanBulk) bulk;
			if ( saldo.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
				saldo.setTerzo( getTerzoNullo());
			else 
				saldo.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( saldo.getTerzo()));
		}
		else if ( bulk instanceof Movimento_coanBulk )
		{
			Movimento_coanBulk mov = (Movimento_coanBulk) bulk;
			if ( mov.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
				mov.getScrittura().setTerzo( getTerzoNullo());
			else 
				mov.getScrittura().setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( mov.getScrittura().getTerzo()));
		}
		*/	
        if(isEsercizioChiuso(userContext))
			return asRO(bulk,"Attenzione: esercizio economico chiuso o in fase di chiusura.");
		
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di una scrittura analitica 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un movimento coan
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un saldo coan
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> inizializzati per ricerca
 *
 */

public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
{

	try
	{
		bulk = super.inizializzaBulkPerRicerca( userContext, bulk);
		if ( bulk instanceof Scrittura_analiticaBulk )
		{
			Scrittura_analiticaBulk scrittura = (Scrittura_analiticaBulk) bulk;
			scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
			return scrittura;
		}
		else if ( bulk instanceof Saldo_coanBulk )
		{
			((Saldo_coanBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Saldo_coanBulk)bulk).getCds()));
		}
		else if ( bulk instanceof Movimento_coanBulk )
		{
			((Movimento_coanBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Movimento_coanBulk)bulk).getCds()));
		}	
		
		
		return bulk;
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di una scrittura analitica 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un movimento coan
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un saldo coan
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> inizializzati per ricerca
 *
 */

public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException
{

	try
	{
		bulk = super.inizializzaBulkPerRicercaLibera( userContext, bulk);
		if ( bulk instanceof Scrittura_analiticaBulk )
		{
			Scrittura_analiticaBulk scrittura = (Scrittura_analiticaBulk) bulk;
			scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
			return scrittura;
		}
		else if ( bulk instanceof Saldo_coanBulk )
		{
			((Saldo_coanBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Saldo_coanBulk)bulk).getCds()));
		}
		else if ( bulk instanceof Movimento_coanBulk )
		{
			((Movimento_coanBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Movimento_coanBulk)bulk).getCds()));
		}	
		
		
		return bulk;
	}
	catch ( Exception e )
	{
		throw handleException( bulk, e );
	}
}
/**
 *
 * Nome: Esercizio aperto
 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
 *       e l'esercizio contabile e' APERTO
 * Post: La data di contabilizzazione viene inizializzata con la data odierna
 *
 * Nome: Esercizio chiuso provvisoriamente
 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
 *       e l'esercizio contabile e' CHIUSO PROVVISORIO
 * Post: La data di contabilizzazione viene inizializzata con il 31/12/esercizio di scrivania
 *
 * Nome: Esercizio in altro stato
 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
 *       e l'esercizio contabile ha stato diverso da APERTO o  CHIUSO PROVVISORIO
 * Post: Una segnalazione di errore comunica all'utente l'impossibilità di creare una scrittura analitica
 
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_analiticaBulk</code>  la cui data deve essere inizializzata
 * @return <code>Scrittura_analiticaBulk</code>  con data inizializzata
 *
 */

private Scrittura_analiticaBulk inizializzaDataContabilizzazione (UserContext userContext,Scrittura_analiticaBulk scrittura ) throws PersistencyException, ComponentException, javax.ejb.EJBException
{
	EsercizioBulk esercizio = (EsercizioBulk) getHome( userContext, EsercizioBulk.class ).findByPrimaryKey( new EsercizioBulk( ((CNRUserContext)userContext).getCd_cds(), ((CNRUserContext)userContext).getEsercizio()));
	if ( esercizio == null )
		throw new ApplicationException( "Attenzione esercizio non definito per il cds di scrivania!");

	/* Gennaro Borriello - (23/09/2004 10.33.19)
	 *	Err. 838 - Deve essere possibile poter registrare delle scritture coep/coan
	 *	anche se l'esercizio finanziario è chiuso.
	*/ 
	if ( !esercizio.getSt_apertura_chiusura().equals( esercizio.STATO_APERTO) && 
		!esercizio.getSt_apertura_chiusura().equals( esercizio.STATO_CHIUSO_DEF))
		throw new ApplicationException( "Attenzione esercizio non in stato aperto per il cds di scrivania!");
	scrittura.setDt_contabilizzazione( it.cnr.contab.doccont00.comp.DateServices.getDt_valida( userContext));
		
	return scrittura;
}
/**
  *	Controllo se l'esercizio di scrivania e' aperto
  *
  * Nome: Controllo chiusura esercizio
  * Pre:  E' stata richiesta la creazione o modifica di una scrittura
  * Post: Viene chiamata una stored procedure che restituisce 
  *		  -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
  *		  -		'N' altrimenti
  *		  Se l'esercizio e' chiuso e' impossibile proseguire
  *
  * @param  userContext <code>UserContext</code>
  
  * @return boolean : TRUE se stato = C
  *					  FALSE altrimenti
  */
private boolean isEsercizioChiuso(UserContext userContext) throws ComponentException
{
	LoggableStatement cs = null;	
	String status = null;

	try
	{
		cs = new LoggableStatement(getConnection(userContext),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+	"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());		

		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, CNRUserContext.getEsercizio(userContext)	);		
		cs.setObject( 3, CNRUserContext.getCd_cds(userContext)		);		
		
		cs.executeQuery();

		status = new String(cs.getString(1));

	    if(status.compareTo("Y")==0)
	    	return true;
	    	
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
	
    return false;		    	
}
/** 
  *  Modifica Scrittura Analitica - Esercizio COEP/COAN chiuso
  *    PreCondition:
  *      L'esrcizio COEP/COAN risulta chiuso per il CdS di scrivania
  *    PostCondition:
  *      Non  viene consentita il salvataggio.
  *   
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return l'oggetto <code>OggettoBulk</code> creato
**/
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) 
	throws ComponentException
{
	
	if (isEsercizioChiuso(aUC))
		throw new ApplicationException ("Attenzione: esercizio economico chiuso o in fase di chiusura.");
	

	return super.modificaConBulk(aUC, bulk);
}
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	/* COMPORTAMENTO DI DEFAULT - INIZIO */
	if (clauses == null) 
	{
		if (bulk != null)
			clauses = bulk.buildFindClauses(null);
	} else
		clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(clauses,bulk.buildFindClauses(Boolean.FALSE));
	/* COMPORTAMENTO DI DEFAULT - FINE */			
			
	if(bulk instanceof Movimento_coanBulk)
	{
		for ( Iterator i = clauses.iterator(); i.hasNext(); )
		{
			SimpleFindClause clause = (SimpleFindClause) i.next();
			if ( clause.getPropertyName().equalsIgnoreCase( "scrittura.attiva" ))
				if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL )
					clause.setSqlClause("SCRITTURA_ANALITICA.ATTIVA " + SQLBuilder.getSQLOperator(clause.getOperator()) );
				else
					clause.setSqlClause( "SCRITTURA_ANALITICA.ATTIVA " + SQLBuilder.getSQLOperator(clause.getOperator()) + "'" + clause.getValue() + "'");				

			if ( clause.getPropertyName().equalsIgnoreCase( "scrittura.pg_scrittura_annullata" ))
				if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL  )
					clause.setSqlClause("SCRITTURA_ANALITICA.PG_SCRITTURA_ANNULLATA " + SQLBuilder.getSQLOperator(clause.getOperator()) );
				else
					clause.setSqlClause( "SCRITTURA_ANALITICA.PG_SCRITTURA_ANNULLATA " + SQLBuilder.getSQLOperator(clause.getOperator()) + "'" + clause.getValue() + "'");				
		}		
	}
	SQLBuilder sql = getHome(userContext,bulk).selectByClause(clauses);
	
	return sql;
}
/**
 * Esegue la selezione di un conto economico patrimoniale per un movimento coan
 *
 * Nome: Seleziona conto per movimento coan
 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un movimento coan
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto 
 *       abbia esercizio uguale all'esercizio del movimento coan e natura voce di tipo "EEC"(Economico d'Esercizio Costo)
 *		 oppure 'EER' (Economico d'Esercizio Ricavo)
 *
 * @param userContext <code>UserContext</code> 
 * @param movimento <code>Movimento_coanBulk</code> per cui ricercare il conto
 * @param conto <code>ContoBulk</code> conto econom.patrimoniale da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return SQLBuilder
 *
 */

public SQLBuilder selectContoByClause (UserContext userContext, Movimento_coanBulk movimento, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
	sql.addClause( clauses );
	sql.addClause( "AND", "esercizio", sql.EQUALS, movimento.getEsercizio());
    sql.addSQLClause( "AND", "(VOCE_EP.NATURA_VOCE = 'EEC' OR VOCE_EP.NATURA_VOCE = 'EER')");			
	return sql;
}
/**
 * Esegue la selezione di una linea di attività per un movimento coan
 *
 * Nome: Seleziona linea att. per movimento coan
 * Pre:  E' stata richiesta la ricerca di una linea di attività per un movimento coan
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il cdr della 
 *       linea di attività afferisca all'unità organizzativa di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param movimento <code>Movimento_coanBulk</code> per cui ricercare il conto
 * @param conto <code>Linea_attivitaBulk</code> linea att. da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return SQLBuilder
 *
 */

public SQLBuilder selectLattByClause( UserContext userContext, Movimento_coanBulk movimento, WorkpackageBulk latt, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, latt.getClass(),"V_LINEA_ATTIVITA_VALIDA" ).createSQLBuilder();
	sql.addTableToHeader( "CDR");
	sql.addSQLJoin( "CDR.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA" );
	sql.addSQLClause( "AND", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
	sql.addSQLClause( "AND", "CDR.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_unita_organizzativa());
	sql.addClause( clauses );
	return sql;
}
/**
 * Esegue la selezione di un terzo per una scrittura
 *
 * Nome: Seleziona terzo per scrittura
 * Pre:  E' stata richiesta la ricerca di un terzo per una scrittura analitica
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il terzo
 *       non abbia data di fine rapporto < della data odierna
 *
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_analiticaBulk</code> per cui ricercare il terzo
 * @param terzo <code>TerzoBulk</code> terzo da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return SQLBuilder
 *
 */

public SQLBuilder selectTerzoByClause (UserContext userContext, Scrittura_analiticaBulk scrittura, TerzoBulk terzo, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, terzo.getClass(), "V_TERZO_CF_PI").createSQLBuilder();
	sql.addClause( clauses );
    sql.addSQLClause( "AND", "(DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");			
	return sql;
}
/**
 * Valida la correttezza di un oggetto di tipo <code>Scrittura_analiticaBulk</code> passato in ingresso.
 *
 * Nome: validazione superata
 * Pre:  La scrittura supera la validazione ( metodo validaScrittura)
 * Post: La scrittura può essere inserita nel database
 *
 * Nome: validazione non superata
 * Pre:  La scrittura non supera la validazione ( metodo validaScrittura)
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> da validare
 *
 */

protected void validaCreaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	super.validaCreaModificaConBulk( userContext, bulk );
	try
	{	
		validaScrittura( userContext, (Scrittura_analiticaBulk) bulk );
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
	
}
/**
 * Valida la correttezza di un oggetto di tipo <code>Scrittura_analiticaBulk</code> passato in ingresso.
 *
 * Nome: Nessun movimento dare o avere
 * Pre:  Per la scrittura analitica non e' stato definito nessun movimento
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 * Nome: Tutti controlli superati
 * Pre:  La scrittura analitica ha almeno un movimento
 * Post: La scrittura analitica supera la validazione
 
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_analiticaBulk</code> da validare
 *
 */

private void validaScrittura (UserContext userContext, Scrittura_analiticaBulk scrittura ) throws ComponentException, PersistencyException
{
	if ( scrittura.getMovimentiColl().size() == 0 )
		throw new ApplicationException( "E' necessario definire almeno un movimento");

   // if ( scrittura.getTerzo() != null && scrittura.getTerzo().getCd_terzo() != null && !scrittura.getTerzo().equals( getTerzoNullo()))
   // 	validaAssociazioneAnagConto( userContext, scrittura );	
	
}
}
