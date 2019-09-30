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

package it.cnr.contab.config00.bulk;

import java.sql.Connection;
import java.sql.SQLException;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;

/**
 * @author aimprota
 */
public class Parametri_enteHome extends BulkHome
{
	private static final long serialVersionUID = 1L;

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

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk parEnte) throws PersistencyException {
		try {
			((Parametri_enteBulk)parEnte).setId(new Integer( ((Integer)findAndLockMax( parEnte, "id", new Integer(0) )).intValue()+1 ));
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	
	/**
	 * Ritorna un SQLBuilder con la columnMap del ricevente
	 */
	public SQLBuilder selectRigaAttiva() {
		SQLBuilder sql = createSQLBuilder();
		sql.addClause(FindClause.AND, "attivo", SQLBuilder.EQUALS, Boolean.TRUE);
		return sql;
	}

    public int contaRigheAttive(it.cnr.jada.UserContext userContext) throws PersistencyException {
    	try{
    		return selectRigaAttiva().executeCountQuery(getConnection());  
    	}catch(SQLException sqlException){
    		throw SQLExceptionHandler.getInstance().handleSQLException(sqlException);
    	}
    }

    public Parametri_enteBulk getParametriEnteAttiva() throws PersistencyException{
		return (Parametri_enteBulk) this.fetchAll(selectRigaAttiva()).get(0);
	}	

    public boolean isInformixAttivo() throws PersistencyException{
    	Parametri_enteBulk parente = getParametriEnteAttiva();
    	if (parente!=null)
    		return parente.getFl_informix();
    	return true;
    }

    @Override
    public void initializeBulkForInsert(UserContext usercontext, OggettoBulk oggettobulk)
    		throws PersistencyException, ComponentException {
    	((Parametri_enteBulk)oggettobulk).setFl_autenticazione_ldap(Boolean.FALSE);
    	((Parametri_enteBulk)oggettobulk).setAttivo(Boolean.FALSE);    	
    	((Parametri_enteBulk)oggettobulk).setFl_informix(Boolean.FALSE);    	
    	((Parametri_enteBulk)oggettobulk).setFl_gae_es(Boolean.FALSE);    	
    	((Parametri_enteBulk)oggettobulk).setFl_prg_pianoeco(Boolean.FALSE);    	
    	super.initializeBulkForInsert(usercontext, oggettobulk);
    }
    
}
