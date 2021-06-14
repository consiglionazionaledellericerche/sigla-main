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

import it.cnr.contab.coepcoan00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;

import java.math.BigDecimal;
import java.util.*;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBase;
import it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceHome;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.config00.esercizio.bulk.*;
import java.sql.*;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.si.service.AuthService;

import java.io.Serializable;
import java.util.stream.Collectors;


public class ScritturaPartitaDoppiaComponent extends it.cnr.jada.comp.CRUDComponent implements IScritturaPartitaDoppiaMgr,ICRUDMgr, Cloneable, Serializable,IPrintMgr{
/**
 * ScritturaPartitaDoppiaComponent constructor comment.
 */
public ScritturaPartitaDoppiaComponent() {
	super();
}
/**
 *
 * Nome: Aggiornamento saldi
 * Pre:  E' stata richiesto l'aggiornamento dei saldi coge come conseguenza di un inserimento di  
 *       una nuova scrittura in partita doppia
 * Post: E' stata richiamata la stored procedure che esegue l'aggiornamento dei saldi coge relativi ai
 *       conti di tutti i movimenti dare e avere della scrittura
 *
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_partita_doppiaBulk</code>  che deve essere inserita
 *
 */

private void aggiornaSaldiCoge (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws ComponentException
{
    try
	{
    	LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB200.aggiornaSaldoCoge(?, ?, ?, ?, ?)}",false,this.getClass());
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
 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
 * Pre:  E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
 *       il valore '0' ad indicare tutte le classificazioni anagrafiche
 * Post: Viene restituito un RemoteIterator contenente solamente l'oggetto fittizio ( con codice '0' ) che rappresenta
 *       tutte le classificazioni anagrafiche
 *
 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
 * Pre:  E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
 *       un valore diverso da '0' 
 * Post: Viene restituito un RemoteIterator contenente la lista di oggetti di tipo TerzoBulk
 *       risultante dall'esecuzione della query sul database
 *
 * Nome: Ricerca di un attributo diverso da terzo
 * Pre:  E' stata richiesta la ricerca di un attributo diverso da 'terzo'
 * Post: Viene restituito un RemoteIterator contenente la lista degli oggettiBulk 
 *       risultante dall'esecuzione della query sul database
 * 
 *
 * @param userContext <code>UserContext</code> 
 * @param clausole <code>CompoundFindClause</code>  clausole specificate dall'utente
 * @param bulk <code>OggettoBulk</code>  oggettoBulk da ricercare
 * @param contesto <code>Scrittura_partita_doppiaBulk</code>  contesto della ricerca
 * @param attributo nome dell'attributo del contesto che deve essere ricercato
 * @return <code>RemoteIterator</code>  elenco di oggetti trovati
 *
 */

public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk,OggettoBulk contesto,String attributo) throws it.cnr.jada.comp.ComponentException {
	if ( attributo.equals("terzo") )
	{
		if ( (((TerzoBulk) bulk).getCd_terzo() != null && ((TerzoBulk) bulk).getCd_terzo().equals(TerzoBulk.TERZO_NULLO)))
		{	
			TerzoBulk terzo = getTerzoNullo();
			return new it.cnr.jada.util.ArrayRemoteIterator(new TerzoBulk[] { terzo });
		}
	}	
	return super.cerca(userContext,clausole,bulk,contesto,attributo);
}
/** 
  *  Scrittura in Partita Doppia - Esercizio COEP/COAN chiuso
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
 * Pre:  E' stata richiesta l'inserimento di una scrittura in partita doppia già validata
 * Post: L'importo della scrittura viene impostato come la somma degli importi dei movimenti avere (che coincide con la
 *       somma degli importi dei movimenti dare) e il saldo coge relativo ai conti econ-patr. dei movimenti viene aggiornato
 *       (metodo aggiornaSaldiCoge)
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inserita
 * @return <code>Scrittura_partita_doppiaBulk</code>  inserita
 *
 */

protected OggettoBulk eseguiCreaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException,PersistencyException 
{
	Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
	scrittura.setIm_scrittura( scrittura.getImTotaleAvere());
	if ( scrittura.getTerzo() == null || scrittura.getTerzo().getCd_terzo() == null )
		scrittura.setTerzo( getTerzoNullo());
	makeBulkPersistent(userContext,scrittura);
	aggiornaSaldiCoge( userContext, scrittura );
	return bulk;
}
/**
 *
 * Nome: Storno e riemissione di una scrittura
 * Pre:  E' stata richiesta la cancellazione di una scrittura in partita doppia
 * Post: E' stata richiamata la stored procedure che esegue lo storno e la riemissione della partita doppia ed
 *       aggiorna i saldi coge
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inserita
 *
 */

protected void eseguiEliminaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException,PersistencyException 
{
    try
	{
		Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;

		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB200.creaScrittStornoCoge(?, ?, ?, ?, ?)}",false,this.getClass());
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
/* restitusce un'istanza fittizia di TerzoBulk con codice = 0 a rappresentare
   la non presenza di un terzo */
private TerzoBulk getTerzoNullo() 
{
	TerzoBulk terzo = new TerzoBulk();
	terzo.setCd_terzo( TerzoBulk.TERZO_NULLO);
	terzo.setDenominazione_sede("Terzo non definito");
	terzo.setCrudStatus( terzo.NORMAL);
	return terzo;
}
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per inserimento di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzata la data di contabilizzazione (metodo inizializzaDataContabilizzazione)
 *       e il cds (con il cds di scrivania)
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inizializzato per inserimento
 * @return <code>Scrittura_partita_doppiaBulk</code>  inizializzato per inserimento
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
		if ( bulk instanceof Scrittura_partita_doppiaBulk )
		{
			Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
			scrittura = inizializzaDataContabilizzazione( userContext, scrittura );
			scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
			scrittura.setCd_cds_documento( scrittura.getCd_cds() );
			return scrittura;
		}
		
        //if(isEsercizioChiuso(userContext))
           	//return asRO(bulk,null);
           	
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
 * Pre:  E' stata richiesta l'inizializzazione per modifica di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzata la collezione di movimenti dare e movimenti avere
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un movimento coge
 * Post: Il movimento viene restituito con l'inizializzazione di default
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un saldo coge
 * Post: Il saldo viene restituito con l'inizializzazione di default
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per modifica
 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per modifica
 *
 */
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try 
	{
		bulk = super.inizializzaBulkPerModifica( userContext, bulk );
            	
		if ( bulk instanceof Scrittura_partita_doppiaBulk )
		{
			Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
			scrittura.setMovimentiDareColl( new BulkList( ((Scrittura_partita_doppiaHome) getHome( userContext, scrittura.getClass())).findMovimentiDareColl( userContext, scrittura )));
			scrittura.setMovimentiAvereColl( new BulkList( ((Scrittura_partita_doppiaHome) getHome( userContext, scrittura.getClass())).findMovimentiAvereColl( userContext, scrittura )));
			if ( scrittura.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
				scrittura.setTerzo( getTerzoNullo());
			else 
				scrittura.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( scrittura.getTerzo()));
		}
		else if ( bulk instanceof Saldo_cogeBulk )
		{
			Saldo_cogeBulk saldo = (Saldo_cogeBulk) bulk;
			if ( saldo.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
				saldo.setTerzo( getTerzoNullo());
			else 
				saldo.setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( saldo.getTerzo()));
		}
		else if ( bulk instanceof Movimento_cogeBulk )
		{
			Movimento_cogeBulk mov = (Movimento_cogeBulk) bulk;
			if ( mov.getCd_terzo().equals( TerzoBulk.TERZO_NULLO))
				mov.getScrittura().setTerzo( getTerzoNullo());
			else 
				mov.getScrittura().setTerzo( (TerzoBulk) getHome( userContext, TerzoBulk.class).findByPrimaryKey( mov.getScrittura().getTerzo()));
		}
		
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
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un movimento coge
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un saldo coge
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per ricerca
 *
 */

public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
{

	try
	{
		bulk = super.inizializzaBulkPerRicerca( userContext, bulk);
		if ( bulk instanceof Scrittura_partita_doppiaBulk )
		{
			Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
			scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
			return scrittura;
		}
		else if ( bulk instanceof Saldo_cogeBulk )
		{
			((Saldo_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Saldo_cogeBulk)bulk).getCds()));
		}
		else if ( bulk instanceof Movimento_cogeBulk )
		{
			((Movimento_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Movimento_cogeBulk)bulk).getCds()));
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
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un movimento coge
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un saldo coge
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per ricerca
 *
 */

public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException
{

	try
	{
		bulk = super.inizializzaBulkPerRicercaLibera( userContext, bulk);
		if ( bulk instanceof Scrittura_partita_doppiaBulk )
		{
			Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;
			scrittura.setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk( scrittura.getUo().getCd_unita_padre())));
			return scrittura;
		}
		else if ( bulk instanceof Saldo_cogeBulk )
		{
			((Saldo_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Saldo_cogeBulk)bulk).getCds()));
		}
		else if ( bulk instanceof Movimento_cogeBulk )
		{
			((Movimento_cogeBulk)bulk).setCds((CdsBulk)getHome( userContext, CdsBulk.class).findByPrimaryKey( ((Movimento_cogeBulk)bulk).getCds()));
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
 * Post: La data di contabilizzzaione viene inizializzata con la data odierna
 *
 * Nome: Esercizio chiuso priovvisoriamente
 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
 *       e l'esercizio contabile e' CHIUSO PROVVISORIO
 * Post: La data di contabilizzzaione viene inizializzata con il 31/12/esercizio di scrivania
 *
 * Nome: Esercizio in altro stato
 * Pre:  E' stata richiesta l'inizializzazione della data di contabilizzazione di una scrittura
 *       e l'esercizio contabile ha stato diverso da APERTO o  CHIUSO PROVVISORIO
 * Post: Una segnalazione di errore comunica all'utente l'impossibilità di creare una scrittura in partita doppia
 
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_partita_doppiaBulk</code>  la cui data deve essere inizializzata
 * @return <code>Scrittura_partita_doppiaBulk</code>  con data inizializzata
 *
 */

private Scrittura_partita_doppiaBulk inizializzaDataContabilizzazione (UserContext userContext,Scrittura_partita_doppiaBulk scrittura ) throws PersistencyException, ComponentException, javax.ejb.EJBException
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
		cs = new LoggableStatement(getConnection(userContext), "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
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
/* per le date salvate nel database come timestamp bisogna ridefinire la query nel modo seguente:
		TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
*/		

protected void ridefinisciClausoleConTimestamp(UserContext userContext,CompoundFindClause clauses) 
{
	SimpleFindClause clause;
	for ( Iterator i = clauses.iterator(); i.hasNext(); )
	{
		clause = (SimpleFindClause) i.next();
		if ( clause.getPropertyName().equalsIgnoreCase( "dt_contabilizzazione" ) )
			if ( clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL )
				clause.setSqlClause( "TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) );
			else
				clause.setSqlClause( "TRUNC( " + clause.getPropertyName() + ") " + SQLBuilder.getSQLOperator(clause.getOperator()) + " ? ");
		
	}	


}
/* per le date salvate nel database come timestamp bisogna ridefinire la query nel modo seguente:
		TRUNC( dt_nel_db) operator 'GG/MM/YYYY'
*/		
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
	
	ridefinisciClausoleConTimestamp( userContext, clauses );

	if(bulk instanceof Movimento_cogeBulk)
	{
		for ( Iterator i = clauses.iterator(); i.hasNext(); )
		{
			SimpleFindClause clause = (SimpleFindClause) i.next();
			if ( clause.getPropertyName().equalsIgnoreCase( "scrittura.attiva" ))
				if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL )
					clause.setSqlClause("SCRITTURA_PARTITA_DOPPIA.ATTIVA " + SQLBuilder.getSQLOperator(clause.getOperator()) );
				else
					clause.setSqlClause( "SCRITTURA_PARTITA_DOPPIA.ATTIVA " + SQLBuilder.getSQLOperator(clause.getOperator()) + "'" + clause.getValue() + "'");				

			if ( clause.getPropertyName().equalsIgnoreCase( "scrittura.pg_scrittura_annullata" ))
				if (clause.getOperator() == SQLBuilder.ISNOTNULL || clause.getOperator() == SQLBuilder.ISNULL  )
					clause.setSqlClause("SCRITTURA_PARTITA_DOPPIA.PG_SCRITTURA_ANNULLATA " + SQLBuilder.getSQLOperator(clause.getOperator()) );
				else
					clause.setSqlClause( "SCRITTURA_PARTITA_DOPPIA.PG_SCRITTURA_ANNULLATA " + SQLBuilder.getSQLOperator(clause.getOperator()) + "'" + clause.getValue() + "'");				
		}		
	}	
	SQLBuilder sql = getHome(userContext,bulk).selectByClause(clauses);

	return sql;
}
/**
 * esegue la seleziona un conto economico patrimoniale per un movimento coge
 *
 * Nome: Seleziona conto per movimeto avere
 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un movimento avere
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto 
 *       abbia esercizio uguale all'esercizio del movimento coge e il tipo sezione diverso da DARE
 *
 * Nome: Seleziona conto per movimeto dare
 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un movimento dare
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto 
 *       abbia esercizio uguale all'esercizio del movimento coge e il tipo sezione diverso da AVERE
 *
 * @param userContext <code>UserContext</code> 
 * @param movimento <code>Movimento_cogeBulk</code> per cui ricercare il conto
 * @param conto <code>ContoBulk</code> conto econom.patrimoniale da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return SQLBuilder
 *
 */

public SQLBuilder selectContoByClause (UserContext userContext, Movimento_cogeBulk movimento, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
	sql.addClause( clauses );
	if ( movimento.SEZIONE_AVERE.equals( movimento.getSezione()))
		sql.addClause( "AND", "ti_sezione", sql.NOT_EQUALS, ContoHome.SEZIONE_DARE);
	else if ( movimento.SEZIONE_DARE.equals( movimento.getSezione()))
		sql.addClause( "AND", "ti_sezione", sql.NOT_EQUALS, ContoHome.SEZIONE_AVERE);
	sql.addClause( "AND", "esercizio", sql.EQUALS, movimento.getEsercizio());
	return sql;
}
/**
 * esegue la seleziona un conto economico patrimoniale per un saldo coge
 *
 * Nome: Seleziona conto per saldo coge
 * Pre:  E' stata richiesta la ricerca di un conto economico patrimoniale per un saldo coge
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il conto 
 *       abbia esercizio uguale all'esercizio del saldo coge 
 *
 * @param userContext <code>UserContext</code> 
 * @param saldo <code>Saldo_cogeBulk</code> per cui ricercare il conto
 * @param conto <code>ContoBulk</code> conto econom.patrimoniale da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return SQLBuilder
 *
 */

public SQLBuilder selectContoByClause (UserContext userContext, Saldo_cogeBulk saldo, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
	sql.addClause( clauses );
	sql.addClause( "AND", "esercizio", sql.EQUALS, saldo.getEsercizio());
	return sql;
}


/**
 * esegue la seleziona un terzo per una scrittura
 *
 * Nome: Seleziona terzo per scrittura
 * Pre:  E' stata richiesta la ricerca di un terzo per una scrittura in partita doppia
 * Post: Viene restituito il SQLBuilder con le clausole specificate dall'utente ed inoltre le clausole che il terzo
 *       non abbia data di fine rapporto < della data odierna
 *
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_partita_doppiaBulk</code> per cui ricercare il terzo
 * @param terzo <code>TerzoBulk</code> terzo da ricercare
 * @param clauses <code>CompoundFindClause</code> clausole specificate dall'utente
 * @return SQLBuilder
 *
 */

public SQLBuilder selectTerzoByClause (UserContext userContext, Scrittura_partita_doppiaBulk scrittura, TerzoBulk terzo, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, terzo.getClass(), "V_TERZO_CF_PI").createSQLBuilder();
	sql.addClause( clauses );
    sql.addSQLClause( "AND", "(DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");			
	return sql;
}
/**
 * valida la correttezza dell'associazione fra anagrafico e conto
 *
 * Nome: Nessuna associzione fra anagrafico-conto
 * Pre:  Per la scrittura in partita doppia e' stato selezionato un terzo le cui caratteristiche anagrafiche non
 *       sono state messe in relazione con neanche un conto definito per la scrittura (sia in avere che in dare)
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 * Nome: Almeno un'associzione fra anagrafico-conto
 * Pre:  Per la scrittura in partita doppia e' stato selezionato un terzo le cui caratteristiche anagrafiche 
 *       sono state messe in relazione con un conto definito per la scrittura (in avere o in dare)
 * Post: La scrittura supera la validazione anagrafica-conto
 *
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_partita_doppiaBulk</code> da validare
 *
 */

private void validaAssociazioneAnagConto (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws ComponentException, PersistencyException
{
	List result = ((Ass_anag_voce_epHome) getHome(userContext, Ass_anag_voce_epBulk.class)).findAssociazioniPerScrittura( scrittura );
	if ( result == null || (result != null && result.size() == 0 ))
			throw new ApplicationException( "Non esiste nessuna associazione fra l'anagrafica selezionata e i conti economico-patrimoniali" );
	Ass_anag_voce_epBulk ass = (Ass_anag_voce_epBulk) result.get(0);
	Movimento_cogeBulk movimento;
	for ( Iterator j = scrittura.getMovimentiAvereColl().iterator(); j.hasNext(); )
	{
		movimento = (Movimento_cogeBulk) j.next();
		if ( movimento.getConto().equalsByPrimaryKey( ass.getConto()))
			return;
	}
	for ( Iterator j = scrittura.getMovimentiDareColl().iterator(); j.hasNext(); )
	{
		movimento = (Movimento_cogeBulk) j.next();
		if ( movimento.getConto().equalsByPrimaryKey( ass.getConto()))
			return;
	}		
	throw new ApplicationException( "Non esiste nessuna associazione fra l'anagrafica selezionata e i conti economico-patrimoniali" );
	
}
/**
 * valida la correttezza di un oggetto di tipo <code>Scrittura_partita_doppiaBulk</code> passato in ingresso.
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
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> da validare
 *
 */

protected void validaCreaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	super.validaCreaModificaConBulk( userContext, bulk );
	try
	{	
		validaScrittura( userContext, (Scrittura_partita_doppiaBulk) bulk );
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
	
}
/**
 * valida la correttezza di un oggetto di tipo <code>Scrittura_partita_doppiaBulk</code> passato in ingresso.
 *
 * Nome: Nessun movimento avere
 * Pre:  Per la scrittura in partita doppia non e' stato definito nessun movimento avere
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 * Nome: Nessun movimento dare
 * Pre:  Per la scrittura in partita doppia non e' stato definito nessun movimento dare
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 * Nome: Somma movimenti dare diversa da somma movimenti avere
 * Pre:  La somma degli importi dei movimenti dare e' diversa dalla somma degli importi dei movimenti avere
 * Post: Una segnalazione di errore viene restituita all'utente
 *
 * Nome: Terzo
 * Pre:  La scrittura e' stata definita per un terzo e la validazione terzo-conti (metodo 'validaAssociazioneAnagConto')
 *       non e' stata superata
 * Post: Una segnalazione di errore viene restituita all'utente

 * Nome: Tutti i controlli superati
 * Pre:  La scrittura ha almeno un movimento avere, ha almeno un movimento dare e la somma degli importi dei
 *       movimenti avere e dare coincide. Inoltre non e' stato specificato un terzo oppure e' stato specificato un
 *       terzo e tale terzo ha superato la validazione coi conti.
 * Post: La scrittura supera la validazione e può pertanto essere salvata
 *
 * @param userContext <code>UserContext</code> 
 * @param scrittura <code>Scrittura_partita_doppiaBulk</code> da validare
 *
 */

private void validaScrittura (UserContext userContext, Scrittura_partita_doppiaBulk scrittura ) throws ComponentException, PersistencyException
{
	if ( scrittura.getMovimentiAvereColl().size() == 0 )
		throw new ApplicationException( "E' necessario definire almeno un movimento Avere");
	if ( scrittura.getMovimentiDareColl().size() == 0 )
		throw new ApplicationException( "E' necessario definire almeno un movimento Dare");
	if ( scrittura.getImTotaleAvere().compareTo( scrittura.getImTotaleDare()) != 0 )
			throw new ApplicationException( "Gli importi totali dei movimenti Dare e Avere devono essere uguali");

    if ( scrittura.getTerzo() != null && scrittura.getTerzo().getCd_terzo() != null && !scrittura.getTerzo().equalsByPrimaryKey( getTerzoNullo()))
    	validaAssociazioneAnagConto( userContext, scrittura );	
	
		
	
}
/* (non-Javadoc)
 * @see it.cnr.jada.comp.IPrintMgr#inizializzaBulkPerStampa(it.cnr.jada.UserContext, it.cnr.jada.bulk.OggettoBulk)
 */
public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext,OggettoBulk oggettobulk) throws ComponentException { 
     if (oggettobulk instanceof Stampa_elenco_movimentiBulk) 
		return  inizializzaBulkPerStampa(usercontext,(Stampa_elenco_movimentiBulk)oggettobulk);
	 return oggettobulk;	
}

public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext,Stampa_elenco_movimentiBulk stampa) throws ComponentException { 
	//	Imposta l'Esercizio come quello di scrivania
	stampa.setEsercizio(CNRUserContext.getEsercizio(usercontext));
	
	
	try{
			String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(usercontext);
		
			CdsHome cdsHome = (CdsHome)getHome(usercontext, CdsBulk.class);
			CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
			if (!cds.getCd_unita_organizzativa().equals(ente.getCd_unita_padre())){
				stampa.setCdsForPrint(cds);
				stampa.setCdsForPrintEnabled(true);
			} else {
				stampa.setCdsForPrint(new CdsBulk());
				stampa.setCdsForPrintEnabled(false);
			}
			
		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}	
	
		try{
			String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(usercontext);
		
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(usercontext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

			if (!uo.isUoCds()){
				stampa.setUoForPrint(uo);
				stampa.setUoForPrintEnabled(true);
			} else {
				stampa.setUoForPrint(new Unita_organizzativaBulk());
				stampa.setUoForPrintEnabled(false);
			}
			
		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}
		
	return stampa;
}

public OggettoBulk stampaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
	if (oggettobulk instanceof Stampa_elenco_movimentiBulk) 
		return  stampaConBulk(usercontext,(Stampa_elenco_movimentiBulk)oggettobulk);
	 return oggettobulk;
}

public OggettoBulk stampaConBulk(UserContext usercontext, Stampa_elenco_movimentiBulk stampa) throws ComponentException {
	if ( stampa.getContoForPrint()==null || stampa.getContoForPrint().getCd_voce_ep() == null)
			throw new ApplicationException( "E' necessario inserire un Conto");
	if ( stampa.getattiva()==null )
			throw new ApplicationException( "E' necessario valorizzare il campo 'Attiva'");
	if ( stampa.gettipologia()==null )
			throw new ApplicationException( "E' necessario valorizzare il campo 'Tipologia'");
	return stampa;
}
public SQLBuilder selectContoForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, ContoBulk conto, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, conto.getClass()).createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addClause( clauses );
	return sql;
}
public SQLBuilder selectTerzoForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, TerzoBulk terzo, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome( userContext, terzo.getClass(),"V_TERZO_CF_PI").createSQLBuilder();
	sql.addClause( clauses );
	return sql;
}
public SQLBuilder selectUoForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
	sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdsForPrint().getCd_unita_organizzativa());
	sql.addClause( clauses );
	return sql;
}
public SQLBuilder selectCdsForPrintByClause (UserContext userContext, Stampa_elenco_movimentiBulk stampa, CdsBulk cds, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
	return sql;
			
}

	public Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppia(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, PersistencyException {
		Ass_tipo_cori_voce_epHome aEffCoriHome = (Ass_tipo_cori_voce_epHome) getHome(userContext, Ass_tipo_cori_voce_epBulk.class);

		Scrittura_partita_doppiaBulk scritturaPartitaDoppiaBulk = new Scrittura_partita_doppiaBulk();
		scritturaPartitaDoppiaBulk.setEsercizio(docamm.getEsercizio());
		scritturaPartitaDoppiaBulk.setEsercizio_documento_amm(docamm.getEsercizio());
		scritturaPartitaDoppiaBulk.setCd_cds_documento(docamm.getCd_cds());
		scritturaPartitaDoppiaBulk.setCd_tipo_documento(docamm.getCd_tipo_doc_amm());
		scritturaPartitaDoppiaBulk.setPg_numero_documento(docamm.getPg_doc_amm());

		if (docamm.getTipoDocumentoEnum().isDocumentoPassivo()) {
			Fattura_passivaBulk fatpas = (Fattura_passivaBulk) docamm;
			List<IDocumentoAmministrativoRigaBulk> righeDocamm = docamm.getChildren();

			Map<Integer, Map<Timestamp, Map<Timestamp, List<IDocumentoAmministrativoRigaBulk>>>> mapTerzo =
					righeDocamm.stream().collect(Collectors.groupingBy(IDocumentoAmministrativoRigaBulk::getCd_terzo,
							Collectors.groupingBy(IDocumentoAmministrativoRigaBulk::getDt_da_competenza_coge,
									Collectors.groupingBy(IDocumentoAmministrativoRigaBulk::getDt_a_competenza_coge))));

			if (fatpas.isIstituzionale()) {
				//Registrazione IVA
				boolean registraIva = false;
				if (Bene_servizioBulk.BENE.equalsIgnoreCase(fatpas.getTi_bene_servizio())) {
					if (fatpas.isSanMarinoSenzaIVA() || fatpas.getFl_intra_ue().booleanValue() || fatpas.getFl_merce_intra_ue().booleanValue())
						registraIva = true;
				}
				if (!registraIva && Bene_servizioBulk.SERVIZIO.equalsIgnoreCase(fatpas.getTi_bene_servizio())) {
					if (fatpas.getTipo_sezionale().getFl_servizi_non_residenti().booleanValue())
						registraIva = true;
				}
				if (!registraIva && fatpas.getFl_split_payment().booleanValue())
					registraIva = true;

				if (registraIva) {
					Ass_tipo_cori_voce_epBulk aEffCori = aEffCoriHome.getAssCoriEp(CNRUserContext.getEsercizio(userContext), CompensoBulk.CODICE_IVA, Contributo_ritenutaBulk.TIPO_ENTE, Movimento_cogeBulk.SEZIONE_DARE);

					String aCdVoceIvaEp = Optional.ofNullable(aEffCori).flatMap(el -> Optional.ofNullable(el.getVoce_ep_contr())).flatMap(el -> Optional.ofNullable(el.getCd_voce_ep()))
								.orElseThrow(() -> new ApplicationRuntimeException("Conto ep di contropartita non trovato (Esercizio: " + CNRUserContext.getEsercizio(userContext) +
										" - Codice Contributo: " + CompensoBulk.CODICE_IVA + " - Tipe E/P: " + Contributo_ritenutaBulk.TIPO_ENTE + " - Sezione: " + Movimento_cogeBulk.SEZIONE_DARE + ")"));

					String aSezioneContoIva = fatpas.getTipoDocumentoEnum().isNotaCreditoPassiva() ? Movimento_cogeBulk.SEZIONE_DARE : Movimento_cogeBulk.SEZIONE_AVERE;

					mapTerzo.keySet().stream().forEach(aCdTerzo -> {
						mapTerzo.get(aCdTerzo).keySet().forEach(aDtDaCompCoge -> {
							mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).keySet().forEach(aDtACompCoge -> {
								//Registrazione IVA
								List<IDocumentoAmministrativoRigaBulk> righeDocammTerzo = mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).get(aDtACompCoge);
								BigDecimal imIva = righeDocammTerzo.stream().map(IDocumentoAmministrativoRigaBulk::getIm_iva)
										.reduce(BigDecimal.ZERO, BigDecimal::add);
								Movimento_cogeBulk movimentoIva = new Movimento_cogeBulk();
								movimentoIva.setCd_cds(fatpas.getCd_cds_origine());
								movimentoIva.setEsercizio(fatpas.getEsercizio());
								movimentoIva.setCd_unita_organizzativa(fatpas.getCd_uo_origine());
								movimentoIva.setCd_voce_ep(aCdVoceIvaEp);
								movimentoIva.setSezione(aSezioneContoIva);
								movimentoIva.setIm_movimento(imIva.abs());
								movimentoIva.setTi_istituz_commerc(fatpas.getTi_istituz_commerc());
								movimentoIva.setCd_terzo(aCdTerzo);
								movimentoIva.setDt_da_competenza_coge(aDtDaCompCoge);
								movimentoIva.setDt_a_competenza_coge(aDtACompCoge);
								movimentoIva.setStato(Movimento_cogeBulk.STATO_DEFINITIVO);

								if (aSezioneContoIva.equals(Movimento_cogeBulk.SEZIONE_DARE))
									scritturaPartitaDoppiaBulk.addToMovimentiDareColl(movimentoIva);
								else
									scritturaPartitaDoppiaBulk.addToMovimentiAvereColl(movimentoIva);

							});
						});
					});
				}

				//Registrazione COSTO
				String aSezioneContoEp = findSezioneContoEp(userContext, docamm);

				mapTerzo.keySet().stream().forEach(aCdTerzo -> {
					mapTerzo.get(aCdTerzo).keySet().forEach(aDtDaCompCoge -> {
						mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).keySet().forEach(aDtACompCoge -> {
							List<IDocumentoAmministrativoRigaBulk> righeDocammTerzo = mapTerzo.get(aCdTerzo).get(aDtDaCompCoge).get(aDtACompCoge);
							Map<Integer, Map<String, Map<String, Map<String, List<IDocumentoAmministrativoRigaBulk>>>>> mapVoce =
									righeDocammTerzo.stream().collect(Collectors.groupingBy(rigaDoc->rigaDoc.getScadenzaDocumentoContabile().getFather().getEsercizio(),
											Collectors.groupingBy(rigaDoc2->rigaDoc2.getScadenzaDocumentoContabile().getFather().getTi_appartenenza(),
													Collectors.groupingBy(rigaDoc3->rigaDoc3.getScadenzaDocumentoContabile().getFather().getTi_gestione(),
															Collectors.groupingBy(rigaDoc4->rigaDoc4.getScadenzaDocumentoContabile().getFather().getCd_elemento_voce())))));

							mapVoce.keySet().stream().forEach(aEseVoce -> {
								mapVoce.get(aEseVoce).keySet().forEach(aTiAppartenenza -> {
									mapVoce.get(aEseVoce).get(aTiAppartenenza).keySet().forEach(aTiGestione -> {
										mapVoce.get(aEseVoce).get(aTiAppartenenza).get(aTiGestione).keySet().forEach(aCdVoce -> {
											try {
												List<IDocumentoAmministrativoRigaBulk> righeDocammVoce = mapVoce.get(aEseVoce).get(aTiAppartenenza).get(aTiGestione).get(aCdVoce);
												BigDecimal imImponibile = righeDocammVoce.stream().map(IDocumentoAmministrativoRigaBulk::getIm_imponibile)
														.reduce(BigDecimal.ZERO, BigDecimal::add);

												Voce_epBulk aCdContoEp = findContoEp(userContext, new Elemento_voceBulk(aCdVoce, aEseVoce, aTiAppartenenza, aTiGestione));
												String aSezioneRigheContoEp = imImponibile.compareTo(BigDecimal.ZERO)>0
														?aSezioneContoEp
														:(Movimento_cogeBulk.SEZIONE_DARE.equals(aSezioneContoEp)?Movimento_cogeBulk.SEZIONE_AVERE:Movimento_cogeBulk.SEZIONE_DARE);

												Movimento_cogeBulk movimentoContoEp = new Movimento_cogeBulk();
												movimentoContoEp.setCd_cds(fatpas.getCd_cds_origine());
												movimentoContoEp.setEsercizio(fatpas.getEsercizio());
												movimentoContoEp.setCd_unita_organizzativa(fatpas.getCd_uo_origine());
												movimentoContoEp.setCd_voce_ep(aCdContoEp.getCd_voce_ep());
												movimentoContoEp.setSezione(aSezioneRigheContoEp);
												movimentoContoEp.setIm_movimento(imImponibile.abs());
												movimentoContoEp.setTi_istituz_commerc(fatpas.getTi_istituz_commerc());
												movimentoContoEp.setCd_terzo(aCdTerzo);
												movimentoContoEp.setDt_da_competenza_coge(aDtDaCompCoge);
												movimentoContoEp.setDt_a_competenza_coge(aDtACompCoge);
												movimentoContoEp.setStato(Movimento_cogeBulk.STATO_DEFINITIVO);

												if (aSezioneRigheContoEp.equals(Movimento_cogeBulk.SEZIONE_DARE))
													scritturaPartitaDoppiaBulk.addToMovimentiDareColl(movimentoContoEp);
												else
													scritturaPartitaDoppiaBulk.addToMovimentiAvereColl(movimentoContoEp);
											} catch (ComponentException|PersistencyException e) {
												throw new ApplicationRuntimeException(e);
											}
										});
									});
								});
							});
						});
					});
				});
			} //end fatpas.isIstituzionale()
		}

		return scritturaPartitaDoppiaBulk;
	}

	private Voce_epBulk findContoEp(UserContext userContext, Elemento_voceBulk voceBilancio) throws ComponentException, PersistencyException {
		Ass_ev_voceepHome assEvVoceEpHome = (Ass_ev_voceepHome) getHome(userContext, Ass_ev_voceepBulk.class);
		List<Ass_ev_voceepBulk> listAss = assEvVoceEpHome.findVociEpAssociateVoce(voceBilancio);
		if (listAss.isEmpty())
			throw new ApplicationException("Associazione tra voce del piano finanziario " + voceBilancio.getEsercizio() + "/" +
					voceBilancio.getTi_appartenenza() + "/" +
					voceBilancio.getTi_gestione() + "/" +
					voceBilancio.getCd_elemento_voce() + " ed economica non trovata.");
		else if (listAss.size() > 1)
			throw new ApplicationException("Troppi conti economici risultano associati alla voce " + voceBilancio.getEsercizio() + "/" +
					voceBilancio.getTi_appartenenza() + "/" +
					voceBilancio.getTi_gestione() + "/" +
					voceBilancio.getCd_elemento_voce() + ".");

		return listAss.get(0).getVoce_ep();
	}

	private String findSezioneContoEp(UserContext userContext, IDocumentoAmministrativoBulk docamm) throws ComponentException, PersistencyException {
		Tipo_documento_ammHome tipoDocHome = (Tipo_documento_ammHome)getHome(userContext, Tipo_documento_ammBulk.class);
		Tipo_documento_ammBulk tipoDoc = (Tipo_documento_ammBulk)tipoDocHome.findByPrimaryKey(docamm.getCd_tipo_doc_amm());

		if (docamm.getTipoDocumentoEnum().isNotaCreditoPassiva() || docamm.getTipoDocumentoEnum().isNotaCreditoAttiva())
			return Elemento_voceHome.GESTIONE_SPESE.equals(tipoDoc.getTi_entrata_spesa())?Movimento_cogeBulk.SEZIONE_AVERE:Movimento_cogeBulk.SEZIONE_DARE;
		else
			return Elemento_voceHome.GESTIONE_SPESE.equals(tipoDoc.getTi_entrata_spesa())?Movimento_cogeBulk.SEZIONE_DARE:Movimento_cogeBulk.SEZIONE_AVERE;
	}

}
