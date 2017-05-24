/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcep.cla.bulk;

import it.cnr.contab.config00.pdcep.bulk.Voce_epHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Classificazione_voci_ep_pat_liv8Home extends Classificazione_voci_ep_pat_liv7Home {

	public Classificazione_voci_ep_pat_liv8Home(java.sql.Connection conn) {
		super(Classificazione_voci_ep_pat_liv8Bulk.class, conn);
	}
	public Classificazione_voci_ep_pat_liv8Home(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Classificazione_voci_ep_pat_liv8Bulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
 		SQLBuilder sql = super.selectPat(usercontext, compoundfindclause);
		sql.addSQLClause("AND","cd_livello8",SQLBuilder.ISNOTNULL, null);
		return sql;
	}	
}
