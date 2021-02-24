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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.contab.anagraf00.core.bulk.*;

import java.rmi.RemoteException;
import java.sql.*;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import javax.ejb.EJBException;

/**
 * Home dell'unità organizzativa bulk
 * Creation date: (12/04/2001 08:32:22)
 */

public class Unita_organizzativaHome extends BulkHome {
/**
 * Costruttore
 * Creation date: (12/04/2001 08:32:22)
 * @param conn connessione db
 * @return l'istanza creata
 */

public Unita_organizzativaHome(java.sql.Connection conn) {
	super(Unita_organizzativaBulk.class,conn);
}
/**
 * Costruttore
 * Creation date: (12/04/2001 08:32:22)
 * @param conn connessione db
 * @param persistentCache
 * @return l'istanza creata
 */

public Unita_organizzativaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Unita_organizzativaBulk.class,conn,persistentCache);
}
/**
 * Aggiorna l'esercizio fine dei CDR figli per aggiornamento dell'esercizio fine dell'UO in processo
 * 
 * @param esercizio	Esercizio
 * @param codiceUO Codice unità organizzativa in processo
 * @throws ApplicationException	
 * @throws PersistencyException	
 */
public void aggiornaEsercizioFinePerCdrFigli(Integer esercizio,String codiceUO)  throws ApplicationException, PersistencyException
{
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"UPDATE " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"LINEA_ATTIVITA " +
			"SET ESERCIZIO_FINE = ? " +
			"WHERE " +
			"ESERCIZIO_FINE > ?  AND " +
			"CD_CENTRO_RESPONSABILITA IN " +			
			"(SELECT CD_CENTRO_RESPONSABILITA FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"CDR " +
			"WHERE  " +
			"CD_UNITA_ORGANIZZATIVA = ?) ",true,this.getClass());
		try
		{
			ps.setObject( 1, esercizio );
			ps.setObject( 2, esercizio );		
			ps.setString( 3, codiceUO );
		
			ps.executeUpdate();
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}

		ps = new LoggableStatement(getConnection(),
			"UPDATE " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"CDR " +
			"SET ESERCIZIO_FINE = ? " +
			"WHERE " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"ESERCIZIO_FINE > ?   ",true,this.getClass());
		try
		{
			ps.setObject( 1, esercizio );
			ps.setString( 2, codiceUO );
			ps.setObject( 3, esercizio );		
		
			ps.executeUpdate();
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}

		// aggiorno CDR Responsabile dell'UO con ESERCIZIO_FINE = ESERCIZIO_FINE del CDS
		ps = new LoggableStatement(getConnection(),
		"UPDATE " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"CDR " +
		"SET ESERCIZIO_FINE = ? " +
		"WHERE " +
		"CD_UNITA_ORGANIZZATIVA = ? AND " +
		"TO_NUMBER(CD_PROPRIO_CDR) = 0",true,this.getClass());
		try
		{
			ps.setObject( 1, esercizio );
			ps.setString( 2, codiceUO );
		
			ps.executeUpdate();
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
		
	}
	catch ( SQLException e )
	{
			throw new PersistencyException( e );
	}
	
}
/**
 * Determina un nuovo codice per l'UO in processo
 * @param esercizio esercizio in cui viene creata l'UO
 * @param codiceCDS codice del CDS di appartenenza dell'UO
 * @return il nuovo codice
 */

public String creaNuovoCodice(Integer esercizio,String codiceCDS)  throws ApplicationException, PersistencyException
{
	String codice;
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_PROPRIO_UNITA FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"UNITA_ORGANIZZATIVA " +
			"WHERE " +
			"CD_UNITA_PADRE = ? AND " +
			"CD_PROPRIO_UNITA = ( SELECT MAX(CD_PROPRIO_UNITA) " +			
			"FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"UNITA_ORGANIZZATIVA WHERE " +
			"CD_UNITA_PADRE = ?) " +
			"FOR UPDATE NOWAIT",true,this.getClass());
		try
		{
			ps.setString( 1, codiceCDS );
			ps.setString( 2, codiceCDS );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					codice = rs.getString( 1 );
					if ( codice != null )
					{
						long cdLong = Long.parseLong( codice ) + 1;
						codice = String.valueOf( cdLong );
					}
					else
						codice = String.valueOf( 0 );
				}
				else
					codice = String.valueOf( 0 );	
			}	
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
		
		return codice;
	}
	catch (java.lang.NumberFormatException e)
	{
			throw new ApplicationException( "Esistono codice non numerici nel database. " );			
	}
	catch ( SQLException e )
	{
			throw new PersistencyException( e );
	}
	
}
/**
 * Determina un nuovo codice per l'UO in processo
 * @param esercizio esercizio in cui viene creata l'UO
 * @param codiceCDS codice del CDS di appartenenza dell'UO
 * @return il nuovo codice
 */

public String creaNuovoCodice(String codiceCDS)  throws ApplicationException, PersistencyException
{
	String codice;
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_PROPRIO_UNITA FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"UNITA_ORGANIZZATIVA " +
			"WHERE " +
			"CD_UNITA_PADRE = ? AND " +
			"CD_PROPRIO_UNITA = ( SELECT MAX(CD_PROPRIO_UNITA) " +			
			"FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"UNITA_ORGANIZZATIVA WHERE " +
			"CD_UNITA_PADRE = ?) " +
			"FOR UPDATE NOWAIT",true,this.getClass());
		try
		{
			ps.setString( 1, codiceCDS );
			ps.setString( 2, codiceCDS );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					codice = rs.getString( 1 );
					if ( codice != null )
					{
						long cdLong = Long.parseLong( codice ) + 1;
						codice = String.valueOf( cdLong );
					}
					else
						codice = String.valueOf( 0 );
				}
				else
					codice = String.valueOf( 0 );	
			}
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
		
		return codice;
	}
	catch (java.lang.NumberFormatException e)
	{
			throw new ApplicationException( "Esistono codice non numerici nel database. " );			
	}
	catch ( SQLException e )
	{
			throw new PersistencyException( e );
	}
	
}
/**
 * Aggiunge condizioni cablate per le ricerche sul bulk collegato a questa home.
 * In particolare imposta fl_cds a true per filtrare le sole unità organizzative
 * Scarta le UO di tipo ENTE perchè non gestite on-line
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilder()
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_cds", SQLBuilder.EQUALS, new Boolean(false));
	sql.addClause("AND", "cd_tipo_unita", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
	return sql;

}
/**
 * Aggiunge condizioni cablate per le ricerche sul bulk collegato a questa home.
 * In particolare imposta fl_cds a true per filtrare le sole unità organizzative
 * e NON scarta le UO di tipo ENTE 
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilderEsteso()
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_cds", SQLBuilder.EQUALS, new Boolean(false));
	return sql;

}
/**
 * Aggiunge condizioni cablate per le ricerche sul bulk collegato a questa home.
 * In particolare imposta fl_cds a true per filtrare le sole unità organizzative
 * e NON scarta le UO di tipo ENTE 
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilderArea()
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_cds", SQLBuilder.EQUALS, new Boolean(true));
	sql.addClause("AND","cd_tipo_unita",sql.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_AREA);
	return sql;

}
/**
 * Estrae il CDR responsabile dell'UO in processo
 * 
 *
 * @param uo 	
 * @return Cdr responsabile 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public CdrBulk findCdrResponsbileUo(Unita_organizzativaBulk uo) throws PersistencyException {

	SQLBuilder sql = getHomeCache().getHome(CdrBulk.class).createSQLBuilder();
	sql.addSQLClause("AND","TO_NUMBER(CD_PROPRIO_CDR)",sql.EQUALS,new Integer(0));
	sql.addClause("AND","cd_unita_organizzativa",sql.EQUALS,uo.getCd_unita_organizzativa());
	Broker broker = createBroker(sql);
	if (!broker.next()) return null;
	return (CdrBulk)broker.fetch(CdrBulk.class);
}
/**
 * Recupera l'UO CDS del cds specificato
 *
 * @param esercizio	non utilizzato
 * @param cd_cds codice del cds
 * @return uo_cds
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public Unita_organizzativaBulk findUo_cds(Integer esercizio,String cd_cds) throws PersistencyException,IntrospectionException {
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND","cd_unita_padre",sql.EQUALS,cd_cds);
	sql.addClause("AND","fl_uo_cds",sql.EQUALS,Boolean.TRUE);
	Broker broker = createBroker(sql);
	if (!broker.next()) return null;
	Unita_organizzativaBulk uo = (Unita_organizzativaBulk)broker.fetch(getPersistentClass());
	if (broker.next())
		throw new FetchException("Individuata più di una uocds per un cds");
	return uo;
}
/**
 * Recupera l'UO CDS del cds a cui appartiene l'UO specificata 
 *
 * @param esercizio	non utilizzato
 * @param cd_unita_organizzativa UO specificata
 * @return uo_cds
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public Unita_organizzativaBulk findUo_cdsByUo(Integer esercizio,String cd_unita_organizzativa) throws PersistencyException,IntrospectionException {
	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("UNITA_ORGANIZZATIVA","CDS");
	sql.addSQLJoin("CDS.CD_UNITA_PADRE","UNITA_ORGANIZZATIVA.CD_UNITA_PADRE");
	sql.addSQLClause("AND","CDS.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,cd_unita_organizzativa);
	sql.addClause("AND","fl_uo_cds",sql.EQUALS,Boolean.TRUE);
	Broker broker = createBroker(sql);
	if (!broker.next()) return null;
	Unita_organizzativaBulk uo = (Unita_organizzativaBulk)broker.fetch(getPersistentClass());
	if (broker.next())
		throw new FetchException("Individuata più di una uocds per un cds");
	return uo;
}
/**
 * Ritorna l'UO di afferenza di una data uo attraverso l'afferenza di un CDR di secondo livello
 * ad un CDR di primo
 *
 * @param uo uo afferente	
 * @return uo di afferenza
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public Unita_organizzativaBulk findUoDiRiferimento(Unita_organizzativaBulk uo) throws PersistencyException,IntrospectionException {
	CdrBulk cdrRUO = findCdrResponsbileUo( uo );

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader( "CDR ");
	sql.addSQLJoin( "CDR.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLClause("AND","CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS, cdrRUO.getCd_cdr_afferenza());
	Broker broker = createBroker(sql);
	if (!broker.next()) return null;
	return (Unita_organizzativaBulk)broker.fetch(getPersistentClass());
}

/**
 * Recupera tutti i dati nella tabella ASS_UO_AREA relativi alla testata in uso.
 *
 * @param testata La testata in uso.
 *
 * @return java.util.Collection Collezione di oggetti <code>Ass_uo_areaBulk</code>
 */
public java.util.Collection findAssociazioneUoArea(UserContext usercontext, Unita_organizzativaBulk testata) throws IntrospectionException, PersistencyException {
	PersistentHome dettHome = getHomeCache().getHome(Ass_uo_areaBulk.class);
	SQLBuilder sql = dettHome.createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,testata.getCd_unita_organizzativa());
	sql.addOrderBy("CD_AREA_RICERCA");
	return dettHome.fetchAll(sql);
}	

/**
 * Calcola il numero di unità organizzative appartenenti allo stesso CDS del bulk passato
 * collegate all'area di ricerca.
 * L'uo passata deve aver valorizzati l'unità padre e il cds area
 * Creation date: (12/04/2001 08:32:22)
 * @param uo unità organizzativa collegata ad area
 * @return il numero di uo appartenenti allo stesso cds di uo e collegate all'area a cui è collegato uo
 */

public int getNumeroUoCollegateAdAreaInCds(Unita_organizzativaBulk uo)  throws ApplicationException, PersistencyException
{
	String codice;

	if(uo.getUnita_padre() == null)
	 throw new ApplicationException("Riferimenti all'uo padre non specificati!");

	if(uo.getCds_area_ricerca() == null)
	 throw new ApplicationException("Riferimenti all'area di ricerca non specificati!");
	
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT COUNT(*) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"UNITA_ORGANIZZATIVA " +
			"WHERE " +
			"CD_UNITA_PADRE = ? AND " +
			"CD_AREA_RICERCA = ?",true,this.getClass());
		try
		{
			ps.setString( 1, uo.getUnita_padre().getCd_unita_organizzativa() );
			ps.setString( 2, uo.getCds_area_ricerca().getCd_unita_organizzativa() );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					return rs.getInt(1);
				}
			}
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
		
        return 0;
	} 
	catch ( SQLException e )
	{
			throw new PersistencyException( e );
	}
	
}
/**
 * Ritorna false se l'esercizio di fine impostato sull'UO è minore del massimo esercizio 
 * esistente per pdg dell'UO in processo
 *
 * @param uo	
 * @return 
 * @throws PersistencyException	
 */
public boolean verificaEsercizioPreventivo( Unita_organizzativaBulk uo ) throws PersistencyException
{	try
	{
		int esercizioPdG;
		
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT MAX(ESERCIZIO) FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"PDG_PREVENTIVO " +
			"WHERE " +
			"CD_CENTRO_RESPONSABILITA LIKE ? ",true,this.getClass());
		try
		{
			ps.setString( 1, uo.getCd_unita_organizzativa() + "%" );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					esercizioPdG = rs.getInt( 1 );
					if ( esercizioPdG > 0 && esercizioPdG > uo.getEsercizio_fine().intValue())
						return false;
				}
			}
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
		
		return true;
	}
	catch ( SQLException e )
	{
			throw new PersistencyException( e );
	}
}

}
