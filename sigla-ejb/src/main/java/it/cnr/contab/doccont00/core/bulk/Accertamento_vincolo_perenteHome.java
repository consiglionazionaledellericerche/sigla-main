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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Accertamento_vincolo_perenteHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Accertamento_vincolo_perenteHome(java.sql.Connection conn) {
		super(Accertamento_vincolo_perenteBulk.class,conn);
	}

	public Accertamento_vincolo_perenteHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Accertamento_vincolo_perenteBulk.class,conn,persistentCache);
	}

	public java.util.List<Accertamento_vincolo_perenteBulk> cercaDettagliVincolati(Var_stanz_resBulk variazione) throws PersistencyException, IntrospectionException
	{
		SQLBuilder sql = this.createSQLBuilder();	
		
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,variazione.getEsercizio());
		sql.addClause(FindClause.AND,"pg_variazione",SQLBuilder.EQUALS,variazione.getPg_variazione());

		return this.fetchAll(sql);
	}
}
