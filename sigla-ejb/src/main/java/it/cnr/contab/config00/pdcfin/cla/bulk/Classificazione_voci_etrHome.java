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

/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Classificazione_voci_etrHome extends Classificazione_vociHome {
	protected Classificazione_voci_etrHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected Classificazione_voci_etrHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	public Classificazione_voci_etrHome(java.sql.Connection conn) {
		super(Classificazione_voci_etrBulk.class, conn);
	}
	public Classificazione_voci_etrHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Classificazione_voci_etrBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		return selectEntrate(usercontext, compoundfindclause);
	}
	public SQLBuilder selectEntrate(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ti_gestione",SQLBuilder.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
		return sql;
	}	
}