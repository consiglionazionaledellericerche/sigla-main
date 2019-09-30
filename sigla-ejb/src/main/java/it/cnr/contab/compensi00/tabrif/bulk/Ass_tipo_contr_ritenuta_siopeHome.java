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
 * Date 27/09/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;
import it.cnr.contab.config00.bulk.Codici_siopeHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Ass_tipo_contr_ritenuta_siopeHome extends BulkHome {
	public Ass_tipo_contr_ritenuta_siopeHome(Connection conn) {
		super(Ass_tipo_contr_ritenuta_siopeBulk.class, conn);
	}
	public Ass_tipo_contr_ritenuta_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_tipo_contr_ritenuta_siopeBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectSiope_spesaByClause( Ass_tipo_contr_ritenuta_siopeBulk  bulk, Codici_siopeHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, bulk.TI_GESTIONE_SPESE);
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());	
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectSiope_entrataByClause( Ass_tipo_contr_ritenuta_siopeBulk  bulk, Codici_siopeHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, bulk.TI_GESTIONE_ENTRATE);
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());	
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectTipo_contributoByClause(Ass_tipo_contr_ritenuta_siopeBulk  bulk, Tipo_contributo_ritenutaHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException { 
		
		SQLBuilder sql = home.createSQLBuilder();
		home.addClauseValidita(sql, home.getServerDate());
		sql.addClause(clause);
		if (bulk.getCrudStatus()== OggettoBulk.TO_BE_CREATED){
			SQLBuilder sql2 =createSQLBuilder();
			sql2.addSQLJoin("ass_tipo_contr_ritenuta_siope.cd_contributo_ritenuta",SQLBuilder.EQUALS,"tipo_contributo_ritenuta.cd_contributo_ritenuta");
			sql2.addSQLJoin("ass_tipo_contr_ritenuta_siope.dt_ini_validita",SQLBuilder.EQUALS,"tipo_contributo_ritenuta.dt_ini_validita");
			sql2.addSQLClause("AND","ass_tipo_contr_ritenuta_siope.esercizio",SQLBuilder.EQUALS,bulk.getEsercizio());
			sql.addSQLNotExistsClause("AND",sql2);
		}
		return sql;
	}
}