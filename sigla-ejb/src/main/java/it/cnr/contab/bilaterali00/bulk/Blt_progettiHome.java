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
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Orderable;

import java.util.List;
public class Blt_progettiHome extends BulkHome {
	public Blt_progettiHome(java.sql.Connection conn) {
		super(Blt_progettiBulk.class, conn);
	}
	public Blt_progettiHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Blt_progettiBulk.class, conn, persistentCache);
	}
	public SQLBuilder getSQLBuilderBltProgrammaVisiteItaList(Blt_programma_visiteHome home, Blt_progettiBulk progetto) throws IntrospectionException,PersistencyException 
	{
		SQLBuilder sql = home.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addClause(FindClause.AND,"flStraniero",SQLBuilder.EQUALS, Boolean.FALSE);
		return sql;
	}
	public SQLBuilder getSQLBuilderBltProgrammaVisiteStrList(Blt_programma_visiteHome home, Blt_progettiBulk progetto ) throws IntrospectionException,PersistencyException 
	{
		SQLBuilder sql = home.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addClause(FindClause.AND,"flStraniero",SQLBuilder.EQUALS, Boolean.TRUE);
		return sql;
	}
	public java.util.List findBltProgrammaVisiteItaList( it.cnr.jada.UserContext userContext,Blt_progettiBulk progetto) throws IntrospectionException,PersistencyException 
	{
		Blt_programma_visiteHome programmaVisiteHome = (Blt_programma_visiteHome)getHomeCache().getHome(Blt_programma_visiteBulk.class );
		SQLBuilder sql = getSQLBuilderBltProgrammaVisiteItaList(programmaVisiteHome, progetto);
		sql.addOrderBy("ANNO_VISITA");
		List l =  programmaVisiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findBltProgrammaVisiteStrList( it.cnr.jada.UserContext userContext,Blt_progettiBulk progetto ) throws IntrospectionException,PersistencyException 
	{
		Blt_programma_visiteHome programmaVisiteHome = (Blt_programma_visiteHome)getHomeCache().getHome(Blt_programma_visiteBulk.class );
		SQLBuilder sql = getSQLBuilderBltProgrammaVisiteStrList(programmaVisiteHome, progetto);
		sql.addOrderBy("ANNO_VISITA");
		List l =  programmaVisiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findBltAutorizzatiItaList( it.cnr.jada.UserContext userContext,Blt_progettiBulk progetto ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome autorizzatiHome = getHomeCache().getHome(Blt_autorizzatiBulk.class );
		SQLBuilder sql = autorizzatiHome.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addClause(FindClause.AND,"tiItalianoEstero",SQLBuilder.EQUALS, NazioneBulk.ITALIA);

		sql.addOrderBy("CD_TERZO");
		List l =  autorizzatiHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findBltAutorizzatiStrList( it.cnr.jada.UserContext userContext,Blt_progettiBulk progetto ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome autorizzatiHome = getHomeCache().getHome(Blt_autorizzatiBulk.class );
		SQLBuilder sql = autorizzatiHome.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addClause(FindClause.AND,"tiItalianoEstero",SQLBuilder.NOT_EQUALS, NazioneBulk.ITALIA);

		sql.addOrderBy("CD_TERZO");
		List l =  autorizzatiHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public SQLBuilder getSQLBuilderBltVisiteItaList( Blt_visiteHome home, Blt_progettiBulk progetto) throws IntrospectionException,PersistencyException 
	{
		SQLBuilder sql = home.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addTableToHeader("BLT_AUTORIZZATI");
		sql.addSQLJoin("BLT_AUTORIZZATI.CD_ACCORDO", SQLBuilder.EQUALS, "BLT_VISITE.CD_ACCORDO");
		sql.addSQLJoin("BLT_AUTORIZZATI.CD_PROGETTO", SQLBuilder.EQUALS, "BLT_VISITE.CD_PROGETTO");
		sql.addSQLJoin("BLT_AUTORIZZATI.CD_TERZO", SQLBuilder.EQUALS, "BLT_VISITE.CD_TERZO");
		sql.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI.TI_ITALIANO_ESTERO",SQLBuilder.EQUALS, NazioneBulk.ITALIA);
		return sql;
	}
	public SQLBuilder getSQLBuilderBltVisiteStrList( Blt_visiteHome home, Blt_progettiBulk progetto) throws IntrospectionException,PersistencyException 
	{
		SQLBuilder sql = home.createSQLBuilder();
		if (progetto.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, progetto.getCd_accordo());

		if (progetto.getCd_progetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, progetto.getCd_progetto());
		
		sql.addTableToHeader("BLT_AUTORIZZATI");
		sql.addSQLJoin("BLT_AUTORIZZATI.CD_ACCORDO", SQLBuilder.EQUALS, "BLT_VISITE.CD_ACCORDO");
		sql.addSQLJoin("BLT_AUTORIZZATI.CD_PROGETTO", SQLBuilder.EQUALS, "BLT_VISITE.CD_PROGETTO");
		sql.addSQLJoin("BLT_AUTORIZZATI.CD_TERZO", SQLBuilder.EQUALS, "BLT_VISITE.CD_TERZO");
		sql.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI.TI_ITALIANO_ESTERO",SQLBuilder.NOT_EQUALS, NazioneBulk.ITALIA);
		return sql;
	}

	public java.util.List findBltVisiteItaList( it.cnr.jada.UserContext userContext,Blt_visiteBulk visita) throws IntrospectionException,PersistencyException 
	{
		Blt_visiteHome visiteHome = (Blt_visiteHome)getHomeCache().getHome(Blt_visiteBulk.class );
		SQLBuilder sql = getSQLBuilderBltVisiteItaList(visiteHome, visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti());

		sql.addTableToHeader("BLT_AUTORIZZATI_DETT");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_ACCORDO", SQLBuilder.EQUALS, "BLT_VISITE.CD_ACCORDO");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_PROGETTO", SQLBuilder.EQUALS, "BLT_VISITE.CD_PROGETTO");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_TERZO", SQLBuilder.EQUALS, "BLT_VISITE.CD_TERZO");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.PG_AUTORIZZAZIONE", SQLBuilder.EQUALS, "BLT_VISITE.PG_AUTORIZZAZIONE");
		sql.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.EQUALS, visita.getAnnoVisita());
		
		List l =  visiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findBltVisiteStrList( it.cnr.jada.UserContext userContext,Blt_visiteBulk visita) throws IntrospectionException,PersistencyException 
	{
		Blt_visiteHome visiteHome = (Blt_visiteHome)getHomeCache().getHome(Blt_visiteBulk.class );
		SQLBuilder sql = getSQLBuilderBltVisiteStrList(visiteHome, visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti());

		sql.addTableToHeader("BLT_AUTORIZZATI_DETT");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_ACCORDO", SQLBuilder.EQUALS, "BLT_VISITE.CD_ACCORDO");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_PROGETTO", SQLBuilder.EQUALS, "BLT_VISITE.CD_PROGETTO");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.CD_TERZO", SQLBuilder.EQUALS, "BLT_VISITE.CD_TERZO");
		sql.addSQLJoin("BLT_AUTORIZZATI_DETT.PG_AUTORIZZAZIONE", SQLBuilder.EQUALS, "BLT_VISITE.PG_AUTORIZZAZIONE");
		sql.addSQLClause(FindClause.AND,"BLT_AUTORIZZATI_DETT.ANNO_VISITA",SQLBuilder.EQUALS, visita.getAnnoVisita());
		
		List l =  visiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.Collection findCaps_comune(Blt_progettiBulk progetto, it.cnr.contab.anagraf00.tabter.bulk.CapHome capHome, it.cnr.contab.anagraf00.tabter.bulk.CapBulk clause) throws IntrospectionException, PersistencyException {
		return ((it.cnr.contab.anagraf00.tabter.bulk.ComuneHome)getHomeCache().getHome(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk.class)).findCaps(progetto.getComuneEnteResponsIta());
	}
}