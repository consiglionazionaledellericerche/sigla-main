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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 16/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Ass_tipo_cori_voce_epHome extends BulkHome {
	public Ass_tipo_cori_voce_epHome(Connection conn) {
		super(Ass_tipo_cori_voce_epBulk.class, conn);
	}
	public Ass_tipo_cori_voce_epHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_cori_voce_epBulk.class, conn, persistentCache);
	}

	public Ass_tipo_cori_voce_epBulk getAssCoriEp(int esercizio, String aTipoCori, String aTipoEntePercipiente, String aSezione) throws PersistencyException {
		return (Ass_tipo_cori_voce_epBulk)findByPrimaryKey(new Ass_tipo_cori_voce_epBulk(esercizio, aTipoCori, aTipoEntePercipiente, aSezione));
	}
}