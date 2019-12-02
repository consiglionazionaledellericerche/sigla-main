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

package it.cnr.contab.doccont00.intcass.bulk;

import java.sql.*;
import java.util.Collection;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Distinta_cassiere_detHome extends BulkHome {
public Distinta_cassiere_detHome(java.sql.Connection conn) {
	super(Distinta_cassiere_detBulk.class,conn);
}
public Distinta_cassiere_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Distinta_cassiere_detBulk.class,conn,persistentCache);
}
/*	 * Imposta il distinta di un oggetto Distinta_cassiereBulk.
	 *
	 * @param accertamento OggettoBulk
	 *
	 * @exception PersistencyException
	 */

public long getNrDettagli(it.cnr.jada.UserContext userContext,Distinta_cassiereBulk distinta) throws PersistencyException, it.cnr.jada.comp.ComponentException 
{
	try
	{
		LoggableStatement ps =new LoggableStatement( getConnection(),
			"SELECT COUNT(*) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"DISTINTA_CASSIERE_DET " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"PG_DISTINTA = ? ",true,this.getClass());

		try
		{
			ps.setObject( 1, distinta.getEsercizio());
			ps.setString( 2, distinta.getCd_cds());
			ps.setString( 3, distinta.getCd_unita_organizzativa());
			ps.setObject( 4, distinta.getPg_distinta());		
			
			ResultSet rs = ps.executeQuery();
			try
			{
				long count; 
				if ( rs.next() )
					return  rs.getLong(1);
				else
					return  0;
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
	} catch ( Throwable e ) {
		throw new PersistencyException( e );
	}
}
/*	 * Imposta il distinta di un oggetto Distinta_cassiereBulk.
	 *
	 * @param accertamento OggettoBulk
	 *
	 * @exception PersistencyException
	 */

public Long getUltimoPg_Dettaglio(it.cnr.jada.UserContext userContext,Distinta_cassiereBulk distinta) throws SQLException, it.cnr.jada.comp.ComponentException 
{
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT PG_DETTAGLIO FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"DISTINTA_CASSIERE_DET " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"PG_DISTINTA = ? AND " +									
			"PG_DETTAGLIO = ( SELECT MAX(PG_DETTAGLIO) " +			
			"FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"DISTINTA_CASSIERE_DET " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +
			"PG_DISTINTA = ? ) " +												
			"FOR UPDATE NOWAIT",true ,this.getClass());

		try
		{
			ps.setObject( 1, distinta.getEsercizio());
			ps.setString( 2, distinta.getCd_cds());
			ps.setString( 3, distinta.getCd_unita_organizzativa());
			ps.setObject( 4, distinta.getPg_distinta());		
			ps.setObject( 5, distinta.getEsercizio());
			ps.setString( 6, distinta.getCd_cds());
			ps.setString( 7, distinta.getCd_unita_organizzativa());
			ps.setObject( 8, distinta.getPg_distinta());				
			
			ResultSet rs = ps.executeQuery();
			try
			{	
				if ( rs.next() )
					return  new Long( rs.getLong(1) + 1) ;
				else
					return  new Long( 1 ) ;
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}	
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
}
public java.util.Collection getDettaglioDistinta(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, mandato.getEsercizio());
	sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, mandato.getCd_cds());
	sql.addClause("AND", "pg_mandato", SQLBuilder.EQUALS, mandato.getPg_mandato());
	return fetchAll(sql);
}
public java.util.Collection getDettaglioDistinta(it.cnr.jada.UserContext userContext, ReversaleBulk reversale) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, reversale.getEsercizio());
	sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, reversale.getCd_cds());
	sql.addClause("AND", "pg_reversale", SQLBuilder.EQUALS, reversale.getPg_reversale());
	return fetchAll(sql);
}
}
