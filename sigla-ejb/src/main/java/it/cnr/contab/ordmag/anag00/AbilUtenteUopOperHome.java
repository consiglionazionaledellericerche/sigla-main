/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class AbilUtenteUopOperHome extends BulkHome {
	public AbilUtenteUopOperHome(Connection conn) {
		super(AbilUtenteUopOperBulk.class, conn);
	}
	public AbilUtenteUopOperHome(Connection conn, PersistentCache persistentCache) {
		super(AbilUtenteUopOperBulk.class, conn, persistentCache);
	}
	public Boolean isUtenteAbilitato(UserContext userContext, String tipoOperazione, 
			String unitaOperativa) throws IntrospectionException, PersistencyException {	
		AbilUtenteUopOperBulk abil = (AbilUtenteUopOperBulk)findByPrimaryKey(userContext, new AbilUtenteUopOperBulk(userContext.getUser(), unitaOperativa, tipoOperazione));
		if (abil != null){
			return true;
		}
		return false;
	}
}