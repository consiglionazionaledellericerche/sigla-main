/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Gruppo_fileHome extends BulkHome {
	public Gruppo_fileHome(Connection conn) {
		super(Gruppo_fileBulk.class, conn);
	}
	public Gruppo_fileHome(Connection conn, PersistentCache persistentCache) {
		super(Gruppo_fileBulk.class, conn, persistentCache);
	}

	public java.util.List findTipo_file_associati(Gruppo_fileBulk gruppo_file) throws IntrospectionException, PersistencyException {
		if (gruppo_file != null && gruppo_file.getCd_gruppo_file() != null) {
			PersistentHome dettHome = getHomeCache().getHome(Tipo_fileBulk.class);
			SQLBuilder sql = dettHome.createSQLBuilder();
			sql.addTableToHeader("ASS_TIPO_GRUPPO_FILE");
			sql.addSQLJoin("TIPO_FILE.CD_TIPO_FILE", "ASS_TIPO_GRUPPO_FILE.CD_TIPO_FILE");
			sql.addSQLClause("AND","ASS_TIPO_GRUPPO_FILE.CD_GRUPPO_FILE",SQLBuilder.EQUALS,gruppo_file.getCd_gruppo_file());
			return dettHome.fetchAll(sql);
		}
		return null;
	}	
}