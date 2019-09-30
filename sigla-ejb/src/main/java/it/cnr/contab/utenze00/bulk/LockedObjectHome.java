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
 * Created by BulkGenerator 1.1.0 [15/09/2008]
 * Date 17/09/2008
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class LockedObjectHome extends BulkHome {
	public LockedObjectHome(Connection conn) {
		super(LockedObjectBulk.class, conn);
	}
	public LockedObjectHome(Connection conn, PersistentCache persistentCache) {
		super(LockedObjectBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectCdsByClause(UserContext userContext, LockedObjectBulk lockedObject, CdsHome home, CdsBulk cds,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException,PersistencyException,IntrospectionException {
		CdsBulk cdsScrivania = (CdsBulk)home.findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(userContext)));
		if (!cdsScrivania.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			cds.setCd_unita_organizzativa(cdsScrivania.getCd_unita_organizzativa());
		SQLBuilder sql = home.createSQLBuilderIncludeEnte();
		sql.addClause(cds.buildFindClauses(true));
		if (clause != null)
			sql.addClause(clause);
		SQLBuilder sqlIN = createSQLBuilder();
		sqlIN.resetColumns();
		sqlIN.addColumn("cd_cds");
		sql.addSQLINClause(FindClause.AND, "cd_unita_organizzativa", sqlIN);
		if (cdsScrivania.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
			SQLBuilder sqlUnion = home.createSQLBuilderIncludeEnte();			
			sqlUnion.addClause(FindClause.AND, "cd_tipo_unita", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
			return sql.union(sqlUnion,true);
		}
		return sql;
	}
	
	public List findUtenti(UserContext userContext, LockedObjectBulk lockedObject) throws PersistencyException {
		UtenteHome utenteHome = (UtenteHome) getHomeCache().getHome(UtenteBulk.class);
		SQLBuilder sql = utenteHome.createSQLBuilder();
		SQLBuilder sqlIN = createSQLBuilder();
		sqlIN.resetColumns();
		sqlIN.addColumn("cd_utente");
		if (!lockedObject.getCds().getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			sqlIN.addClause(FindClause.AND, "cds.cd_unita_organizzativa", SQLBuilder.EQUALS, lockedObject.getCdCds());	
		sql.addSQLINClause(FindClause.AND, "cd_utente", sqlIN);
		return utenteHome.fetchAll(sql);
	}
	
	public List findOggetti(UserContext userContext, LockedObjectBulk lockedObject) throws PersistencyException {
		SQLBuilder sql = createSQLBuilder();
		sql.addClause(FindClause.AND, "utente.cd_utente", SQLBuilder.EQUALS, lockedObject.getCdUtente());
		return fetchAll(sql);
	}
	
	public List findLockedObjectsForUser(UtenteBulk utente) throws PersistencyException {
		SessionTraceHome sessionTraceHome = (SessionTraceHome) getHomeCache().getHome(SessionTraceBulk.class);
		SQLBuilder sql = sessionTraceHome.createSQLBuilder();
		sql.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, utente.getCd_utente());		
		return sessionTraceHome.fetchAll(sql);
	}	
}