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
 * Date 06/12/2012
 */
package it.cnr.contab.config00.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.ApplicationPersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class CigHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public CigHome(Connection conn) {
		super(CigBulk.class, conn);
	}
	public CigHome(Connection conn, PersistentCache persistentCache) {
		super(CigBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectUnitaOrganizzativaByClause(UserContext userContext, CigBulk cigBulk, 
			Unita_organizzativaHome unita_organizzativaHome, Unita_organizzativaBulk unita_organizzativaBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = unita_organizzativaHome.selectByClause(userContext, compoundfindclause);
		filtraUO(userContext, sql, false);
		return sql;
	}
	
	@Override
	public SQLBuilder selectByClause(UserContext userContext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(userContext, compoundfindclause);
		filtraUO(userContext, sql, true);
		return sql;
	}
	
	private void filtraUO(UserContext userContext, SQLBuilder sql, boolean join) throws PersistencyException{
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) 
				getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!CNRUserContext.getCd_unita_organizzativa(userContext).equals(ente.getCd_unita_organizzativa())){
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
			if(!uoScrivania.isUoCds())
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
			else
				if (join){
					sql.addTableToHeader("UNITA_ORGANIZZATIVA");
					sql.addSQLJoin("CIG.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
					sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
				}else
					sql.addSQLClause("AND","CD_UNITA_PADRE",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
		}
		
	}
	
	@Override
	public void update(Persistent persistent, UserContext userContext)
			throws PersistencyException {
		CigBulk cig = (CigBulk)persistent;
		CigBulk cigDB = (CigBulk) findByPrimaryKey(persistent);
		SQLBuilder sql = getHomeCache().getHome(ContrattoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_CIG", SQLBuilder.EQUALS, cig.getCdCig());
		if (!cig.getCdTerzoRup().equals(cigDB.getCdTerzoRup()) && !getHomeCache().getHome(ContrattoBulk.class).fetchAll(sql).isEmpty())
			throw new ApplicationPersistencyException("Non è possibile cambiare il Terzo in quanto legato ad un Contratto!");  
		super.update(persistent, userContext);
	}
}