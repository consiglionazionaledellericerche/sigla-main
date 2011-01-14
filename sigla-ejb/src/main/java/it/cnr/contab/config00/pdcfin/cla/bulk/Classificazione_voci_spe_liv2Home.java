/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Classificazione_voci_spe_liv2Home extends Classificazione_voci_spe_liv1Home {
	protected Classificazione_voci_spe_liv2Home(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected Classificazione_voci_spe_liv2Home(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	public Classificazione_voci_spe_liv2Home(java.sql.Connection conn) {
		super(Classificazione_voci_spe_liv2Bulk.class, conn);
	}
	public Classificazione_voci_spe_liv2Home(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Classificazione_voci_spe_liv2Bulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectSpese(usercontext, compoundfindclause);
		sql.addSQLClause("AND","cd_livello2",SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause("AND","cd_livello3",SQLBuilder.ISNULL, null);
		return sql;
	}	
}
