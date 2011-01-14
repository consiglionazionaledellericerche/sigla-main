package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author aimprota
 */
public class Parametri_enteHome extends BulkHome
{
	//Parametri_enteBulk parametri;

    /**
     * @param class1
     * @param connection
     */
    public Parametri_enteHome(Connection connection){
        super(Parametri_enteBulk.class, connection);        
    }

    /**
     * @param connection
     * @param persistentcache
     */
    public Parametri_enteHome(Connection connection, PersistentCache persistentcache){
        super(Parametri_enteBulk.class, connection, persistentcache);
    }
    
	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1 o 1
	 * nel caso non ci siano record
	 *
	 * @param par_ent Parametri Ente
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk par_ent) throws PersistencyException {
		try {
			((Parametri_enteBulk)par_ent).setId(new Integer( ((Integer)findAndLockMax( par_ent, "id", new Integer(0) )).intValue()+1 ));
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Ritorna un SQLBuilder con la columnMap del ricevente
	 */
	public SQLBuilder selectRigaAttiva() {
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND", "ATTIVO", sql.EQUALS, "Y");	
		return sql;
	}

    public int contaRigheAttive(it.cnr.jada.UserContext userContext) throws IntrospectionException, PersistencyException {
    	try{
    		PersistentHome dettHome = getHomeCache().getHome(Parametri_enteBulk.class);
    		SQLBuilder sql = dettHome.createSQLBuilder();
    		sql.addClause("AND", "ATTIVO", sql.EQUALS, "Y");
    		return(sql.executeCountQuery(getConnection()));  
    	}catch(SQLException sqlException){
    		throw SQLExceptionHandler.getInstance().handleSQLException(sqlException);
    	}
    }
}
