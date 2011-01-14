/*
* Created by Generator 1.0
* Date 12/05/2005
*/
package it.cnr.contab.anagraf00.tabrif.bulk;
import java.sql.SQLException;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneEsteroHome;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneItalianoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Codici_rapporti_inpsHome extends BulkHome {
	public Codici_rapporti_inpsHome(java.sql.Connection conn) {
		super(Codici_rapporti_inpsBulk.class, conn);
	}
	public Codici_rapporti_inpsHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Codici_rapporti_inpsBulk.class, conn, persistentCache);
	}
	public boolean findCodiceRapportoConAttivitaObbl(String cd_rapp_inps) throws SQLException{

		SQLBuilder sql = createSQLBuilder();

		sql.addSQLClause("AND","CD_RAPPORTO_INPS",sql.EQUALS, cd_rapp_inps);
		sql.addSQLClause("AND","FL_CANCELLATO",sql.EQUALS, "N");
		sql.addSQLClause("AND","FL_ATTIVITA_OBBL",sql.EQUALS, "Y");

		return sql.executeExistsQuery(getConnection());
	}
	@Override
	public SQLBuilder selectByClause(CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(compoundfindclause);
		sql.addSQLClause("AND","FL_CANCELLATO",sql.EQUALS, "N");
		return sql;
	}
}