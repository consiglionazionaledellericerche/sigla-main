package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RapportoHome extends BulkHome {
	public RapportoHome(java.sql.Connection conn) {
		super(RapportoBulk.class,conn);
	}
	public RapportoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(RapportoBulk.class,conn,persistentCache);
	}
	public java.util.Collection findByCdAnagCdTipoRapporto(Integer anag, String tipo_rap) throws IntrospectionException, PersistencyException {
		PersistentHome rHome = getHomeCache().getHome(RapportoBulk.class);
		SQLBuilder sql = rHome.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anag);
		sql.addClause("AND","cd_tipo_rapporto",sql.EQUALS,tipo_rap);
		sql.addOrderBy("DT_INI_VALIDITA DESC");
		return rHome.fetchAll(sql);
	}
}
