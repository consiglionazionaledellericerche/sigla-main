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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_unita_organizzativaHome extends BulkHome {
	private it.cnr.jada.util.OrderedHashtable tipologiaKeys;
	
	public static final String  TIPO_UO_SAC  = new String( "SAC" );
	public static final String  TIPO_UO_IST  = new String( "IST" );
	public static final String  TIPO_UO_PNIR = new String( "PNIR" );
	public static final String  TIPO_UO_AREA = new String( "AREA" );
	public static final String  TIPO_UO_ENTE = new String( "ENTE" );

	
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Tipo_unita_organizzativaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Tipo_unita_organizzativaHome(java.sql.Connection conn) {
	super(Tipo_unita_organizzativaBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Tipo_unita_organizzativaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Tipo_unita_organizzativaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_unita_organizzativaBulk.class,conn,persistentCache);
}
/**
 * Carica in una hashtable l'elenco di Tipologie di CDS  presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable loadTipologiaCdsKeys() throws it.cnr.jada.comp.ApplicationException {
	if ( tipologiaKeys == null )
	{
		try
		{
			tipologiaKeys = new it.cnr.jada.util.OrderedHashtable();
			Tipo_unita_organizzativaBulk tuoBulk = new Tipo_unita_organizzativaBulk();
			SQLBuilder sql = select( tuoBulk );
	        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
			sql.addOrderBy( "cd_tipo_unita" );
			java.util.List tipi = fetchAll( sql );			
			for (java.util.Iterator i = tipi.iterator();i.hasNext();)
			{			
				tuoBulk = (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk) i.next();
				tipologiaKeys.put( tuoBulk.getCd_tipo_unita(), tuoBulk.getCd_tipo_unita().concat(" - ").concat( tuoBulk.getDs_tipo_unita()));
			}
		}	
		catch (Exception e )
		{
			tipologiaKeys = null ;
			throw new it.cnr.jada.comp.ApplicationException( "Non è possibile recuperare le tipologie dei Centri di Spesa. " );
		}				

	}
	return tipologiaKeys;
}
/**
 * Carica in una hashtable l'elenco di Tipologie di UO  presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */
public it.cnr.jada.util.OrderedHashtable loadTipologiaKeys() throws it.cnr.jada.comp.ApplicationException {
	if ( tipologiaKeys == null )
	{
		try
		{
			tipologiaKeys = new it.cnr.jada.util.OrderedHashtable();
			Tipo_unita_organizzativaBulk tuoBulk = new Tipo_unita_organizzativaBulk();
			SQLBuilder sql = select( tuoBulk );
			sql.addOrderBy( "cd_tipo_unita" );
			java.util.List tipi = fetchAll( sql );			
			for (java.util.Iterator i = tipi.iterator();i.hasNext();)
			{			
				tuoBulk = (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk) i.next();
				tipologiaKeys.put( tuoBulk.getCd_tipo_unita(), tuoBulk.getCd_tipo_unita().concat(" - ").concat( tuoBulk.getDs_tipo_unita()));
			}
		}	
		catch (Exception e )
		{
			tipologiaKeys = null ;
			throw new it.cnr.jada.comp.ApplicationException( "Non è possibile recuperare le tipologie dei Centri di Spesa. " );
		}				

	}
	return tipologiaKeys;
}
}
