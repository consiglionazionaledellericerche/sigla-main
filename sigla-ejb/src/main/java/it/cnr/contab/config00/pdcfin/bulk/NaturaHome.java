package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class NaturaHome extends BulkHome {
	private static it.cnr.jada.util.OrderedHashtable naturaKeys;
/**
 * <!-- @TODO: da completare -->
 * Costruisce un NaturaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public NaturaHome(java.sql.Connection conn) {
	super(NaturaBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un NaturaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public NaturaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(NaturaBulk.class,conn,persistentCache);
}
/**
 * Carica in una hashtable l'elenco di Nature presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */

public Dictionary loadNaturaKeys(OggettoBulk bulk) throws it.cnr.jada.comp.ApplicationException
{
	if (naturaKeys == null)
	{
		try
		{
			NaturaBulk naturaBulk = new NaturaBulk();
			java.util.List nature = find( (Persistent) naturaBulk, true );
			naturaKeys = new it.cnr.jada.util.OrderedHashtable() ;
			SQLBuilder sql = select( naturaBulk );
			sql.addOrderBy( "cd_natura" );
			
			for (java.util.Iterator i = nature.iterator();i.hasNext();)
			{			
				naturaBulk = (it.cnr.contab.config00.pdcfin.bulk.NaturaBulk) i.next();
				naturaKeys.put( naturaBulk.getCd_natura(), naturaBulk.getCd_natura().concat(" - ").concat( naturaBulk.getDs_natura()));
			}					
				
		}		
		catch (Exception e)
		{
			naturaKeys = null ;
			throw new ApplicationException( "Non è possibile recuperare la natura. " );
		}
	}	
	return naturaKeys;
}
}
