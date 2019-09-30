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

package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ordine_dettHome extends BulkHome {
public Ordine_dettHome(java.sql.Connection conn) {
	super(Ordine_dettBulk.class,conn);
}
public Ordine_dettHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ordine_dettBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (01/02/2002 12.08.03)
 * @return it.cnr.jada.bulk.BulkList
 * @param ordine it.cnr.contab.doccont00.ordine.bulk.OrdineBulk
 */
public java.util.List findDetailsFor(OrdineBulk ordine) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	sql.addClause("AND","esercizio",sql.EQUALS,ordine.getEsercizio());
	sql.addClause("AND","cd_cds",sql.EQUALS,ordine.getCd_cds());
	sql.addClause("AND","pg_ordine",sql.EQUALS,ordine.getPg_ordine());

	return fetchAll(sql);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	
	try{
		Ordine_dettBulk dett = (Ordine_dettBulk)bulk;

		long pg = ((Long)findAndLockMax(dett,"pg_dettaglio",new Long(0))).longValue() + 1;
		dett.setPg_dettaglio(new Long(pg));

	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.persistency.PersistencyException(e);
	}
}
}
