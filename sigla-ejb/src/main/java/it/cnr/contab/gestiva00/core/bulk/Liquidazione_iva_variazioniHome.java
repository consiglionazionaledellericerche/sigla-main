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

package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Liquidazione_iva_variazioniHome extends BulkHome {
	private static final long serialVersionUID = 1L;
	
	public Liquidazione_iva_variazioniHome(java.sql.Connection conn) {
		super(Liquidazione_iva_variazioniBulk.class,conn);
	}
	
	public Liquidazione_iva_variazioniHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Liquidazione_iva_variazioniBulk.class,conn,persistentCache);
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException {
		try {
			((Liquidazione_iva_variazioniBulk)bulk).setPg_dettaglio(
				new Long(
					((Long)findAndLockMax( bulk, "pg_dettaglio", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}	
}
