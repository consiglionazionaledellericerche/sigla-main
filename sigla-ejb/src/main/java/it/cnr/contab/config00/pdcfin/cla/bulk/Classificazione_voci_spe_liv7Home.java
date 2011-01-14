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

public class Classificazione_voci_spe_liv7Home extends Classificazione_voci_spe_liv6Home {

	public Classificazione_voci_spe_liv7Home(java.sql.Connection conn) {
		super(Classificazione_voci_spe_liv7Bulk.class, conn);
	}
	public Classificazione_voci_spe_liv7Home(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Classificazione_voci_spe_liv7Bulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectSpese(usercontext, compoundfindclause);
		sql.addSQLClause("AND","cd_livello7",SQLBuilder.ISNOTNULL, null);
		return sql;
	}	
}
