/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Pdg_missioneHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Pdg_missioneHome(java.sql.Connection conn) {
		super(Pdg_missioneBulk.class, conn);
	}
	
	public Pdg_missioneHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_missioneBulk.class, conn, persistentCache);
	}

	@SuppressWarnings("rawtypes")
	public java.util.List findAss_pdg_missione_tipo_uoList( Pdg_missioneBulk missione ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome home = getHomeCache().getHome(Ass_pdg_missione_tipo_uoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"pdgMissione",SQLBuilder.EQUALS, missione);
		return home.fetchAll(sql);
	}
}