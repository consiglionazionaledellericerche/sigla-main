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
* Date 10/10/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Timestamp;
import java.util.Iterator;

import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
public class V_liquid_cori_mancantiHome extends BulkHome {
	public V_liquid_cori_mancantiHome(java.sql.Connection conn) {
		super(V_liquid_cori_mancantiBulk.class, conn);
	}
	public V_liquid_cori_mancantiHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_liquid_cori_mancantiBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		
    Timestamp da=null,a=null;
       for (Iterator i=compoundfindclause.iterator();i.hasNext();){
       	SimpleFindClause clausola=(SimpleFindClause) i.next();
       	if (clausola.getPropertyName().compareTo("dt_da")==0)
       	   da=(Timestamp)clausola.getValue();
		if (clausola.getPropertyName().compareTo("dt_a")==0)
		   a=(Timestamp) clausola.getValue();   
       }

		setColumnMap("DISTINCT");				
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.setDistinctClause(true);
		sql.resetColumns();
		sql.addColumn("V_LIQUID_CORI_MANCANTI.CD_CDS");
		sql.addColumn("V_LIQUID_CORI_MANCANTI.CD_UNITA_ORGANIZZATIVA");
		sql.addColumn("V_LIQUID_CORI_MANCANTI.CD_GRUPPO_CR");
		sql.addColumn("V_LIQUID_CORI_MANCANTI.CD_REGIONE");
		sql.addColumn("V_LIQUID_CORI_MANCANTI.PG_COMUNE");
		sql.addSQLClause("AND","V_LIQUID_CORI_MANCANTI.ESERCIZIO_COMPENSO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		sql.addSQLClause("AND","trunc(V_LIQUID_CORI_MANCANTI.DT_MANDATO)",sql.GREATER_EQUALS,da);
		sql.addSQLClause("AND","trunc(V_LIQUID_CORI_MANCANTI.DT_MANDATO)",sql.LESS_EQUALS, a);

		//if (compoundfindclause!= null)
		  // sql.addClause(compoundfindclause);
		return sql;
	}

}