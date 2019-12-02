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
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.List;
public class Blt_programma_visiteHome extends BulkHome {
	public Blt_programma_visiteHome(Connection conn) {
		super(Blt_programma_visiteBulk.class, conn);
	}
	public Blt_programma_visiteHome(Connection conn, PersistentCache persistentCache) {
		super(Blt_programma_visiteBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException {
		try {
			Blt_programma_visiteBulk oggetto=(Blt_programma_visiteBulk)bulk;
				oggetto.setPgRecord(
					new Long(
						((Long)findAndLockMax( oggetto, "pgRecord", new Long(0) )).longValue()+1
					)
				);
			} catch(it.cnr.jada.bulk.BusyResourceException e) {
				throw new PersistencyException(e);
			}
   	}
	public java.util.List findBltVisiteList( it.cnr.jada.UserContext userContext,Blt_programma_visiteBulk programmaVisite ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome visiteHome = getHomeCache().getHome(Blt_visiteBulk.class );
		SQLBuilder sql = visiteHome.createSQLBuilder();
		if (programmaVisite.getCdAccordo()==null)
			sql.addClause(FindClause.AND,"cdAccordoPrg",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordoPrg",SQLBuilder.EQUALS, programmaVisite.getCdAccordo());

		if (programmaVisite.getCdProgetto()==null)
			sql.addClause(FindClause.AND,"cdProgettoPrg",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgettoPrg",SQLBuilder.EQUALS, programmaVisite.getCdProgetto());
		
		if (programmaVisite.getPgRecord()==null)
			sql.addClause(FindClause.AND,"pgRecordPrg",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"pgRecordPrg",SQLBuilder.EQUALS, programmaVisite.getPgRecord());

		List l =  visiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findBltProgrammaVisiteStrList( it.cnr.jada.UserContext userContext,Blt_progettiBulk progetto ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome programmaVisiteHome = getHomeCache().getHome(Blt_programma_visiteBulk.class );
		SQLBuilder sql = programmaVisiteHome.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addClause(FindClause.AND,"flStraniero",SQLBuilder.EQUALS, Boolean.TRUE);
		sql.addOrderBy("ANNO_VISITA");
		List l =  programmaVisiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
}