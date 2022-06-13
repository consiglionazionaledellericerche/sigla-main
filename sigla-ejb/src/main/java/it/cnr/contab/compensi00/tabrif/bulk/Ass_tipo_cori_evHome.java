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
 * Date 11/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_tipo_cori_evHome extends BulkHome {
	public Ass_tipo_cori_evHome(Connection conn) {
		super(Ass_tipo_cori_evBulk.class, conn);
	}

	public Ass_tipo_cori_evHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_cori_evBulk.class, conn, persistentCache);
	}

	public Ass_tipo_cori_evBulk getAssCoriEv(int esercizio, String aTipoCori, String aTiAppartenenza, String aTiGestione, String aTipoEntePercipiente) throws PersistencyException {
		Ass_tipo_cori_evBulk bulk = new Ass_tipo_cori_evBulk();
		bulk.setElemento_voce(new Elemento_voceBulk(null, esercizio, aTiAppartenenza, aTiGestione));
		bulk.setTi_ente_percepiente(aTipoEntePercipiente);
		bulk.setContributo_ritenuta(new Tipo_contributo_ritenutaBulk(aTipoCori, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()));

		return (Ass_tipo_cori_evBulk)findByPrimaryKey(bulk);
	}
}