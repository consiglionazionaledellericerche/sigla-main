package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.sql.*;

public class Pagamento_esternoHome extends BulkHome {
	public Pagamento_esternoHome(java.sql.Connection conn) {
		super(Pagamento_esternoBulk.class,conn);
	}
	public Pagamento_esternoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Pagamento_esternoBulk.class,conn,persistentCache);
	}
/**
 * Imposta il pg_pagamento di un oggetto <code>Pagamento_esternoBulk</code>.
 * @param userContext 
 * @param bulk <code>Pagamento_esternoBulk</code>
 * @exception PersistencyException
*/

public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException 
{
	try
	{
		Pagamento_esternoBulk pagamento_esterno = (Pagamento_esternoBulk) bulk;
		Integer x;
		
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT PG_PAGAMENTO FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"PAGAMENTO_ESTERNO " +
			"WHERE CD_ANAG = ? " +
			"AND PG_PAGAMENTO = ( SELECT MAX(PG_PAGAMENTO) " +
			" FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
            "PAGAMENTO_ESTERNO " +
			"WHERE CD_ANAG = ? )" +
			"FOR UPDATE NOWAIT" ,true,this.getClass());
		try
		{
			ps.setObject( 1, pagamento_esterno.getCd_anag() );
			ps.setObject( 2, pagamento_esterno.getCd_anag() );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if(rs.next())
					x = new Integer(rs.getInt(1) + 1);
				else
					x = new Integer(0);
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
		
		pagamento_esterno.setPg_pagamento( x );
	}
	catch ( SQLException e )
	{
		throw new PersistencyException( e );
	}
		
}	
}
