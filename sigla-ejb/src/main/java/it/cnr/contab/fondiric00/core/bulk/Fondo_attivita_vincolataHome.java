package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_attivita_vincolataHome extends BulkHome {
public Fondo_attivita_vincolataHome(java.sql.Connection conn) {
	super(Fondo_attivita_vincolataBulk.class,conn);
}
public Fondo_attivita_vincolataHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fondo_attivita_vincolataBulk.class,conn,persistentCache);
}
	/**
	 * Recupera tutti i dati nella tabella Fondo_assegnatario relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Fondo_assegnatarioBulk</code>
	 */

	public java.util.Collection findDettagli(Fondo_attivita_vincolataBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Fondo_assegnatarioBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","cd_fondo",sql.EQUALS,testata.getCd_fondo());
		return dettHome.fetchAll(sql);
	}

}
