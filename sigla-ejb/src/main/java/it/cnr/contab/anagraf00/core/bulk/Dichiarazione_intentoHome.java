package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Dichiarazione_intentoHome extends BulkHome {
	public Dichiarazione_intentoHome(java.sql.Connection conn) {
		super(Dichiarazione_intentoBulk.class,conn);
	}
	public Dichiarazione_intentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Dichiarazione_intentoBulk.class,conn,persistentCache);
	}
	/**
	 * Metodo non utilizzato e non utilizzabile. Problemi nella gestione del varchar come proggressivo.
	 *
	 * @param esportatore <code>Carico_familiare_anagBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk esportatore) throws it.cnr.jada.persistency.PersistencyException {
		try {
			java.sql.Connection contact = getConnection();
			java.sql.ResultSet rs = contact.createStatement().executeQuery("SELECT MAX(ID_DICHIARAZIONE) FROM "+
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"DICHIARAZIONE_INTENTO WHERE CD_ANAG = '" + ((Dichiarazione_intentoBulk)esportatore).getAnagrafico().getCd_anag() + "'");
			Long x;
			if(rs.next())
				x = new Long(rs.getLong(1) + 1);
			else
				x = new Long(0);
			((Dichiarazione_intentoBulk)esportatore).setId_dichiarazione( x.toString() );
		} catch(java.sql.SQLException sqle) {
			throw new PersistencyException(sqle);
		}
	}
}
