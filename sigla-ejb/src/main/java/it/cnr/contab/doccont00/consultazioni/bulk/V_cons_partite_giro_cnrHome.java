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
 * Created on May 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_cons_partite_giro_cnrHome extends V_cons_partite_giroHome{

	/**
	 * Fattura_passiva_IHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public V_cons_partite_giro_cnrHome(java.sql.Connection conn) {
		super(V_cons_partite_giro_cnrBulk.class, conn);
	}
	/**
	 * Fattura_passiva_IHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public V_cons_partite_giro_cnrHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(V_cons_partite_giro_cnrBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		sql.addSQLClause("AND","V_CONS_PARTITE_GIRO.CD_UNITA_ORGANIZZATIVA_OBB",SQLBuilder.EQUALS,ente.getCd_unita_organizzativa());

		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		//	Se uo ***.000 in scrivania: visualizza l'elenco di tutte le U.O.
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){

			sql.addSQLClause("AND","V_CONS_PARTITE_GIRO.CD_CDS_ORIGINE_OBB",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));

			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
			if(!uoScrivania.isUoCds())
 				sql.addSQLClause("AND","V_CONS_PARTITE_GIRO.CD_UO_ORIGINE_OBB",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));
  		}	

		//sql.addSQLClause("AND","V_CONS_PARTITE_GIRO.CD_CDS_OBB != V_CONS_PARTITE_GIRO.CD_CDS_ORIGINE_OBB");
		//sql.addSQLClause("AND","V_CONS_PARTITE_GIRO.CD_UNITA_ORGANIZZATIVA_OBB != V_CONS_PARTITE_GIRO.CD_UO_ORIGINE_OBB");

		return sql;
	}
}
