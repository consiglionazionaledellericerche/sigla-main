/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ubicazione_beneHome extends BulkHome {
public Ubicazione_beneHome(java.sql.Connection conn) {
	super(Ubicazione_beneBulk.class,conn);
}
public Ubicazione_beneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ubicazione_beneBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 11.23.36)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 * @param bulk it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public Ubicazione_beneBulk getParent(Ubicazione_beneBulk bulk) throws PersistencyException, IntrospectionException{

	if (bulk == null)
		return null;
	
	SQLBuilder sql = createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS_PADRE",sql.EQUALS,bulk.getCd_cds());
	sql.addSQLClause("AND","CD_UO_PADRE",sql.EQUALS,bulk.getCd_unita_organizzativa());
	sql.addSQLClause("AND","CD_UBICAZIONE",sql.EQUALS,bulk.getCd_ubicazione_padre());

	java.util.Collection coll = this.fetchAll(sql);
	if (coll.size() != 1)
		return null;

	return (Ubicazione_beneBulk)coll.iterator().next();
}
/**
 * Recupera i figli dell'oggetto bulk
 * Creation date: (28/11/2001 10.57.42)
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 * @param bulk it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */

public SQLBuilder selectChildrenFor(it.cnr.jada.UserContext aUC, Ubicazione_beneBulk ubi) {

	SQLBuilder sql = createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC));
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC));

	if (ubi == null)
		sql.addSQLClause("AND","CD_UBICAZIONE_PADRE",sql.ISNULL,null);
	else
		sql.addSQLClause("AND","CD_UBICAZIONE_PADRE",sql.EQUALS,ubi.getCd_ubicazione());

				
	sql.addOrderBy("CD_UBICAZIONE");
	return sql;
}
}
