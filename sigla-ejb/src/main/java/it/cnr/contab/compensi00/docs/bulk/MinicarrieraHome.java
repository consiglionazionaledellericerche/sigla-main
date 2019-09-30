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

package it.cnr.contab.compensi00.docs.bulk;

import java.util.List;

import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class MinicarrieraHome extends BulkHome {
public MinicarrieraHome(java.sql.Connection conn) {
	super(MinicarrieraBulk.class,conn);
}
public MinicarrieraHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(MinicarrieraBulk.class,conn,persistentCache);
}
public java.util.List findMinicarrieraIncaricoList( it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk incarico ) throws IntrospectionException,PersistencyException 
{
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO_REP",SQLBuilder.EQUALS, incarico.getEsercizio());
	sql.addSQLClause("AND","PG_REPERTORIO",SQLBuilder.EQUALS, incarico.getPg_repertorio());
	List l =  fetchAll(sql);
	getHomeCache().fetchAll(userContext);
	return l;
}
}
