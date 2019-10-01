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
