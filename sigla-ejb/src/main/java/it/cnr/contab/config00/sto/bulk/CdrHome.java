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

import java.rmi.*;
import java.sql.*;
import it.cnr.contab.config00.util.*;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CdrHome extends BulkHome {

	public static final Integer CDR_PRIMO_LIVELLO = new Integer(1);
	public static final Integer CDR_SECONDO_LIVELLO =  new Integer(2);	
		
	public static final String  DEFAULT_DS_CDR = new String("CDR responsabile dell'UO" );

protected CdrHome(Class clazz,java.sql.Connection conn) {
	super(clazz,conn);
}
protected CdrHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un CdrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public CdrHome(java.sql.Connection conn) {
	super(CdrBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un CdrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public CdrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(CdrBulk.class,conn,persistentCache);
}
/**
 * Determina un nuovo codice per l'UO in processo
 * @param esercizio esercizio in cui viene creata l'UO
 * @param codiceCDS codice del CDS di appartenenza dell'UO
 * @return il nuovo codice
 */

public void aggiornaEsercizioFinePerLineeAttivita(Integer esercizio,String codiceCDR)  throws ApplicationException, PersistencyException
{
	try
	{
		LoggableStatement ps =new LoggableStatement(getConnection(),
			"UPDATE " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"LINEA_ATTIVITA " +
			"SET ESERCIZIO_FINE = ? " +
			"WHERE " +
			"ESERCIZIO_FINE > ?   AND " +			
			"CD_CENTRO_RESPONSABILITA = ?  " ,true,this.getClass());
		try
		{
			ps.setObject( 1, esercizio );
			ps.setObject( 2, esercizio );		
			ps.setString( 3, codiceCDR );
		
			ps.executeUpdate();
		}
		catch ( SQLException e )
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
 * Genera un nuovo codice per CdrBulk calcolando il progressivo successivo al massimo esistente
 * per l'unita organizzativa da cui dipende
 * @param codiceUO codice completo dell'unita organizzativa
 * @param livello livello del Cdr
 * @return String codice creato
 */



public String creaNuovoCodiceCDR( String codiceUO, Integer livello) throws ApplicationException, PersistencyException
{	
	String codice;

	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_PROPRIO_CDR FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"CDR " +
			"WHERE " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"CD_PROPRIO_CDR = ( SELECT MAX(CD_PROPRIO_CDR) " +			
			"FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"CDR WHERE " +
			"CD_UNITA_ORGANIZZATIVA = ?)" +
			"FOR UPDATE NOWAIT",true ,this.getClass());
		try
		{
			ps.setString( 1, codiceUO );
			ps.setString( 2, codiceUO );
		
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
			catch ( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
//		return getLunghezza_chiaviHome().create().formatCdrKey( codice, esercizio, livello );
			return codice;
		}
		catch ( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
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
 * In particolare impone che il CDR non appartenga ad UO ENTE
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilder()
{
	SQLBuilder sql = createSQLBuilderEsteso();
	sql.addSQLClause("AND", " not exists (select 1 from "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"UNITA_ORGANIZZATIVA where" +
		"    UNITA_ORGANIZZATIVA.cd_unita_organizzativa = "+sql.getColumnMap().getTableName()+".cd_unita_organizzativa " +
		"and UNITA_ORGANIZZATIVA.cd_tipo_unita = ? ) "
	);
	sql.addParameter(Tipo_unita_organizzativaHome.TIPO_UO_ENTE,Types.VARCHAR,0);
	return sql;

}
/**
 * Aggiunge condizioni cablate per le ricerche sul bulk collegato a questa home.
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilderEsteso()
{
	SQLBuilder sql = super.createSQLBuilder();
	return sql;

}
/**
 * Recupera il codice del CDR di afferenza per un Cdr che dipende da un CDS di tipo diverso da SAC.
 * In particolare individua prima l'UO-CDS del CDS specificato e successivamente il CDR di I livello sotto all'UO-CDS.
 * @param esercizio esercizio del cdr
 * @param codiceCDS codice del CDS
 * @return String codice del cdr di I livello sotto all'UO-CDS 
 */

public String findCdCdrAfferenzaForMacro( String codiceCDS) throws PersistencyException, ApplicationException
{	
	try
	{
		//select uo_cds

		String uoCds;
		ResultSet rs;
		
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_UNITA_ORGANIZZATIVA FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"UNITA_ORGANIZZATIVA " +
			"WHERE " +
			"FL_UO_CDS = 'Y' AND " +
			"CD_UNITA_PADRE = ? ",true,this.getClass());
		try
		{	
			ps.setString( 1, codiceCDS );

			rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
					uoCds = rs.getString( 1 );
				else
					throw new ApplicationException( "UO-CDS non trovato!");
			}
			catch ( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch ( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
			
		ps = new LoggableStatement(getConnection(),			
			"SELECT CD_CENTRO_RESPONSABILITA FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"CDR WHERE " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"LIVELLO = ?",true ,this.getClass());
		try
		{
			ps.setString( 1, uoCds );
			ps.setObject( 2, CDR_PRIMO_LIVELLO );
		
			rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
					return rs.getString( 1 );
				else
					return null;
			}
			catch ( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch ( SQLException e )
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
 * Recupera il codice del CDR di afferenza per un Cdr che dipende da un CDS di tipo SAC.
 * In particolare individua il CDR di I livello sotto all'Unita Organizzativa.
 * @param esercizio esercizio del cdr
 * @param codiceUO codice dell'Unita organizzativa da cui dipende il Cdr
 * @return String codice del cdr di I livello sotto all'Unita Organizzativa
 */

public String findCdCdrAfferenzaForSAC( String codiceUO) throws PersistencyException, ApplicationException
{	
	try
	{
		
		LoggableStatement ps =new LoggableStatement(getConnection(),
			"SELECT CD_CENTRO_RESPONSABILITA FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"CDR " +
			"WHERE " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"LIVELLO = ?" ,true,this.getClass());
		try
		{
			ps.setString( 1, codiceUO );
			ps.setObject( 2, CDR_PRIMO_LIVELLO );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
					return rs.getString( 1 );
				else
					return null;
			}
			catch ( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch ( SQLException e )
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
 * Ritorna il CDR ENTE
 *
 * @param esercizio	esercizio
 * @return Cdr Ente
 * @throws PersistencyException	
 * @throws ApplicationException	
 */
public CdrBulk findCdrEnte(Integer esercizio) throws PersistencyException, ApplicationException {
		 Unita_organizzativaHome uo_home = (Unita_organizzativaHome)getHomeCache().getHome(Unita_organizzativaBulk.class);
		SQLBuilder sql = uo_home.createSQLBuilderEsteso();
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.FL_UO_CDS",sql.EQUALS,"Y");
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_TIPO_UNITA",sql.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.ESERCIZIO_INIZIO",sql.LESS_EQUALS, esercizio);
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.ESERCIZIO_FINE",sql.GREATER_EQUALS, esercizio);				
		java.util.List uo = uo_home.fetchAll(sql);
		if (uo.isEmpty()) return null;
		sql = createSQLBuilderEsteso();
		sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,((Unita_organizzativaBulk)uo.get(0)).getCd_unita_organizzativa());
		sql.addSQLClause("AND","TO_NUMBER(CDR.CD_PROPRIO_CDR) = 0");
		sql.addSQLClause("AND","ESERCIZIO_INIZIO",sql.LESS_EQUALS, esercizio);
		sql.addSQLClause("AND","ESERCIZIO_FINE",sql.GREATER_EQUALS, esercizio);		
		Broker broker = createBroker(sql);
		CdrBulk cdr = null;
		if (broker.next())
			cdr = (CdrBulk)fetch(broker);
		return cdr;
}
/**
 * Ritorna il cdr dell'UO CDS del SAC
 * 
 *
 * @param esercizio	
 * @return Cdr
 * @throws PersistencyException	
 * @throws ApplicationException	
 */
public CdrBulk findCdrSAC(Integer esercizio) throws PersistencyException, ApplicationException {
		BulkHome uo_home = (BulkHome)getHomeCache().getHome(Unita_organizzativaBulk.class);
		SQLBuilder sql = uo_home.createSQLBuilder();
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.FL_UO_CDS",sql.EQUALS,"Y");
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_TIPO_UNITA",sql.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.ESERCIZIO_INIZIO",sql.LESS_EQUALS, esercizio);
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.ESERCIZIO_FINE",sql.GREATER_EQUALS, esercizio);				
		java.util.List uo = uo_home.fetchAll(sql);
		if (uo.isEmpty()) return null;
		sql = createSQLBuilder();
		sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,((Unita_organizzativaBulk)uo.get(0)).getCd_unita_organizzativa());
		sql.addSQLClause("AND","TO_NUMBER(CDR.CD_PROPRIO_CDR) = 0");
		sql.addSQLClause("AND","ESERCIZIO_INIZIO",sql.LESS_EQUALS, esercizio);
		sql.addSQLClause("AND","ESERCIZIO_FINE",sql.GREATER_EQUALS, esercizio);		
		Broker broker = createBroker(sql);
		CdrBulk cdr = null;
		if (broker.next())
			cdr = (CdrBulk)fetch(broker);
		return cdr;

}
/**
 * Ritorna una lista contenente tutte i CDR che afferiscono nel CDR passato
 * 
 * @param Cdr classe Bulk del CDR di cui si vuol conoscere la lista dei CDR che afferiscono 	
 * @return Lista Cdr
 * @throws PersistencyException	
 * @throws ApplicationException	
**/
public java.util.List<CdrBulk> findCdrAfferenti(CdrBulk cdr) throws PersistencyException
{
	BulkHome cdrHome = (BulkHome)getHomeCache().getHome(CdrBulk.class);
	SQLBuilder sql = cdrHome.createSQLBuilder();
	sql.addSQLClause("AND","CD_CDR_AFFERENZA",sql.ISNOTNULL, null);
	sql.addSQLClause("AND","CD_CDR_AFFERENZA",sql.EQUALS, cdr.getCd_centro_responsabilita());

	return cdrHome.fetchAll(sql);
}

public boolean isEnte(CdrBulk cdr) throws PersistencyException{

	getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(cdr.getUnita_padre());
	return cdr.isCdrAC();
}
/**
 * Ritorna false se l'esercizio di fine impostato sul cdr è minore del massimo esercizio 
 * esistente per pdg del cdr in processo
 *
 * @param cdr Cdr in processo	
 * @return boolean
 * @throws PersistencyException	
 */
public boolean verificaEsercizioPreventivo( CdrBulk cdr ) throws PersistencyException
{	try
	{
		int esercizioPdG;
		
		LoggableStatement ps =new LoggableStatement(getConnection(),
			"SELECT MAX(ESERCIZIO) FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"PDG_PREVENTIVO " +
			"WHERE " +
			"CD_CENTRO_RESPONSABILITA = ? ",true,this.getClass());
		try
		{
			ps.setString( 1, cdr.getCd_centro_responsabilita() );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					esercizioPdG = rs.getInt( 1 );
					if ( esercizioPdG > 0 && esercizioPdG > cdr.getEsercizio_fine().intValue())
						return false;
				}
			}
			catch ( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch ( SQLException e )
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

public java.util.Collection findPdgModuloDettagli(UserContext userContext, CdrBulk testata, Integer livelloProgetto) throws IntrospectionException, PersistencyException {
	PersistentHome dettHome = getHomeCache().getHome(Pdg_moduloBulk.class);
	SQLBuilder sql = dettHome.createSQLBuilder();
	sql.addClause("AND","esercizio",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());

	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome( Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		Progetto_sipHome progettohome = (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
		sql.addSQLExistsClause("AND",progettohome.abilitazioni(userContext,"PDG_MODULO.PG_PROGETTO",livelloProgetto)); 	
	  //sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM PROGETTO_UO WHERE PDG_MODULO.PG_PROGETTO = PROGETTO_UO.PG_PROGETTO AND PROGETTO_UO.CD_UNITA_ORGANIZZATIVA = ?)");
	  //sql.addParameter(((CNRUserContext) userContext).getCd_unita_organizzativa() ,java.sql.Types.VARCHAR,0);
	}
	return dettHome.fetchAll(sql);
}
public java.util.Collection findPdgEsercizioDettagli(UserContext userContext, CdrBulk testata) throws IntrospectionException, PersistencyException {
	PersistentHome dettHome = getHomeCache().getHome(Pdg_esercizioBulk.class);
	SQLBuilder sql = dettHome.createSQLBuilder();
	sql.addTableToHeader("V_CDR_VALIDO");
	sql.addSQLJoin("PDG_ESERCIZIO.CD_CENTRO_RESPONSABILITA", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
	sql.addTableToHeader("UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLClause("AND", "V_CDR_VALIDO.esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

	Unita_organizzativaBulk uo = new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext));
	uo = (Unita_organizzativaBulk) getHomeCache().getHome(uo).findByPrimaryKey(uo);

	Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class ).findAll().get(0);

	if (!uoEnte.equalsByPrimaryKey(uo))
		if (uo.getLivello().compareTo(new Integer(1))==0
			|| uo.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC) 
			|| uo.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA)) {
			sql.addSQLClause("AND", "V_CDR_VALIDO.cd_unita_organizzativa", SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());
		}
		else {
			sql.addSQLClause("AND", "V_CDR_VALIDO.livello", SQLBuilder.EQUALS, new Integer(1));
			sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.cd_unita_padre", SQLBuilder.EQUALS, uo.getCd_unita_padre());
		}
	sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
	return dettHome.fetchAll(sql);
}
}
