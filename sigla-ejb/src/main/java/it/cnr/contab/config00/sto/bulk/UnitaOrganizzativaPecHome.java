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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/06/2010
 */
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class UnitaOrganizzativaPecHome extends BulkHome {
	public UnitaOrganizzativaPecHome(Connection conn) {
		super(UnitaOrganizzativaPecBulk.class, conn);
	}
	public UnitaOrganizzativaPecHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaOrganizzativaPecBulk.class, conn, persistentCache);
	}
	public UnitaOrganizzativaPecBulk recuperoUoPec(Unita_organizzativaBulk unita_organizzativa)throws PersistencyException{
		String uoPec;
		if (unita_organizzativa.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
			uoPec = unita_organizzativa.getCd_unita_organizzativa();
		else
			uoPec = unita_organizzativa.getCd_unita_padre();
		return (UnitaOrganizzativaPecBulk)findByPrimaryKey(new UnitaOrganizzativaPecBulk(uoPec));
	}
}