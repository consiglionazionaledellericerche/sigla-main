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
 * Date 02/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
public class Blt_visiteHome extends BulkHome {
	public Blt_visiteHome(Connection conn) {
		super(Blt_visiteBulk.class, conn);
	}
	public Blt_visiteHome(Connection conn, PersistentCache persistentCache) {
		super(Blt_visiteBulk.class, conn, persistentCache);
	}
	
	public Blt_autorizzati_dettBulk validaCambioDate(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, ComponentException {
		try {
			Blt_autorizzati_dettHome autorizzatiDettHome = (Blt_autorizzati_dettHome)getHomeCache().getHome(Blt_autorizzati_dettBulk.class );
			SQLBuilder sql = autorizzatiDettHome.createSQLBuilder();
			Blt_visiteBulk visita = (Blt_visiteBulk)bulk;
			Long numGgVisita = DateUtils.daysBetweenDates(visita.getDtIniVisita(), visita.getDtFinVisita())+1;
			
			if (visita.getCdAccordo()==null)
				sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
			else
				sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, visita.getCdAccordo());

			if (visita.getCdProgetto()==null)
				sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
			else
				sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, visita.getCdProgetto());
			
			if (visita.getCdTerzo()==null)
				sql.addClause(FindClause.AND,"cdTerzo",SQLBuilder.ISNULL, null);
			else
				sql.addClause(FindClause.AND,"cdTerzo",SQLBuilder.EQUALS, visita.getCdTerzo());

			if (visita.getAnnoVisita()==null)
				sql.addClause(FindClause.AND,"annoVisita",SQLBuilder.ISNULL, null);
			else
				sql.addClause(FindClause.AND,"annoVisita",SQLBuilder.EQUALS, visita.getAnnoVisita());

			List l = autorizzatiDettHome.fetchAll(sql);
			
			Blt_autorizzati_dettBulk autorizzatoDettGood=null;

			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk) iterator.next();
				autorizzatoDett.setBltVisiteColl(new BulkList( autorizzatiDettHome.findBlt_visiteList(userContext, autorizzatoDett)));
				Blt_visiteBulk visitaValida = autorizzatoDett.getBltVisitaValida();
				if (visitaValida == null || visitaValida.equalsByPrimaryKey(visita)) {
					if (autorizzatoDett.getNumMaxGgVisita().compareTo(numGgVisita.intValue())!=-1) {
						if (autorizzatoDettGood==null || autorizzatoDettGood.getNumMaxGgVisita().compareTo(autorizzatoDettGood.getNumMaxGgVisita())!=-1)
							autorizzatoDettGood=autorizzatoDett;
					}
				}
			}
			if (autorizzatoDettGood==null)
			    throw new ApplicationException("Non esiste una visita autorizzata o con un numero di giorni sufficiente per il terzo selezionato!");

			return autorizzatoDettGood;
		}  catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
		
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, ComponentException {
		Blt_autorizzati_dettBulk autorizzatoDettGood = validaCambioDate(userContext, bulk);
		try{
			Blt_visiteBulk visita = (Blt_visiteBulk)bulk;
			visita.setPgAutorizzazione(autorizzatoDettGood.getPgAutorizzazione());
			((Blt_visiteBulk)bulk).setPgVisita(
					new Long(
					((Long)findAndLockMax( bulk, "pgVisita", new Long(0) )).longValue()+1
				)
			);
		}catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}

	}
}