/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/12/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class UtenteFirmaDettaglioHome extends BulkHome {
	public UtenteFirmaDettaglioHome(Connection conn) {
		super(UtenteFirmaDettaglioBulk.class, conn);
	}
	public UtenteFirmaDettaglioHome(Connection conn, PersistentCache persistentCache) {
		super(UtenteFirmaDettaglioBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectUnitaOrganizzativaByClause(UtenteFirmaDettaglioBulk utenteFirmaDettaglioBulk, Unita_organizzativaHome unita_organizzativaHome,
			Unita_organizzativaBulk unita_organizzativaBulk, CompoundFindClause compoundfindclause) {
        SQLBuilder sqlbuilder = unita_organizzativaHome.createSQLBuilderEsteso();
        sqlbuilder.addClause(compoundfindclause);
        return sqlbuilder;		
	}
}