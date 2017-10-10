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
public class AbilUtenteUopOperMagHome extends BulkHome {
	public AbilUtenteUopOperMagHome(Connection conn) {
		super(AbilUtenteUopOperMagBulk.class, conn);
	}
	public AbilUtenteUopOperMagHome(Connection conn, PersistentCache persistentCache) {
		super(AbilUtenteUopOperMagBulk.class, conn, persistentCache);
	}
	public java.util.Collection findMagazziniAbilitati(UserContext userContext, String tipoOperazione, 
			UnitaOperativaOrdBulk unitaOperativaBulk) throws IntrospectionException, PersistencyException {	
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","CD_TIPO_OPERAZIONE",sql.EQUALS,tipoOperazione);
		sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,userContext.getUser());
		sql.addSQLClause("AND","CD_UNITA_OPERATIVA",sql.EQUALS,unitaOperativaBulk.getCdUnitaOperativa());
		return fetchAll(sql);
	}
	
}