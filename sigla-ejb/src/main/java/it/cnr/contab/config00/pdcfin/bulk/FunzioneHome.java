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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class FunzioneHome extends BulkHome {
	static it.cnr.jada.util.OrderedHashtable funzioni = null;
	final static int LUNGHEZZA_CHIAVE = 2;
/**
 * <!-- @TODO: da completare -->
 * Costruisce un FunzioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public FunzioneHome(java.sql.Connection conn) {
	super(FunzioneBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un FunzioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public FunzioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(FunzioneBulk.class,conn,persistentCache);
}
/**
 * Carica in una hashtable l'elenco di Funzioni presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable loadFunzioni() throws PersistencyException, IntrospectionException {
	if ( funzioni == null )
	{
			funzioni = new it.cnr.jada.util.OrderedHashtable();
			FunzioneBulk funBulk = new FunzioneBulk();
		//	java.util.List lista = find( (Persistent) funBulk, false );
			SQLBuilder sql = select( funBulk );
			sql.addClause("AND","fl_utilizzabile",sql.EQUALS,new Boolean(true));
			sql.addOrderBy( "cd_funzione" );
			java.util.List lista = fetchAll( sql );
			
			for (java.util.Iterator i = lista.iterator();i.hasNext();)
			{			
				funBulk = (FunzioneBulk) i.next();
				funzioni.put( funBulk.getCd_funzione(),funBulk.getCd_funzione() + " - " + funBulk.getDs_funzione());
		}

	}
	
	return funzioni;
}
/*
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_utilizzabile", SQLBuilder.EQUALS, new Boolean(true));
	return sql; 
}
*/
/**
 * Restituisce l'sqlBuilder senza alcun filtro
 * 
 * @return
 */
public SQLBuilder createSQLBuilderCompleto( )	
{
	return super.createSQLBuilder();
}
}
