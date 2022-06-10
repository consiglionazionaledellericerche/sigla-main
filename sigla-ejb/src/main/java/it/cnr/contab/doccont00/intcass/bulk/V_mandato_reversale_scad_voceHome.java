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

package it.cnr.contab.doccont00.intcass.bulk;

import java.util.List;

import it.cnr.contab.compensi00.docs.bulk.V_doc_cont_compBulk;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;


public class V_mandato_reversale_scad_voceHome extends BulkHome {

	public V_mandato_reversale_scad_voceHome(java.sql.Connection conn) {
		super(V_mandato_reversale_scad_voceBulk.class,conn);
	}
	public V_mandato_reversale_scad_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(V_mandato_reversale_scad_voceBulk.class,conn,persistentCache);
	}
	public List findReversaleList(ReversaleBulk reversale) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,reversale.getEsercizio());
		sql.addClause("AND","pg_documento",SQLBuilder.EQUALS,reversale.getPg_reversale());
		sql.addClause("AND","cd_cds",SQLBuilder.EQUALS,reversale.getCd_cds());
		sql.addClause("AND","ti_documento",SQLBuilder.EQUALS,"R");
		sql.addClause("AND","ti_gestione",SQLBuilder.EQUALS,CostantiTi_gestione.TI_GESTIONE_ENTRATE);
		return fetchAll(sql);
	}

	public List findMandatiReversali(V_mandato_reversaleBulk mandato_reversaleBulk) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,mandato_reversaleBulk.getEsercizio());
		sql.addClause("AND","pg_documento",SQLBuilder.EQUALS,mandato_reversaleBulk.getPg_documento_cont());
		sql.addClause("AND","cd_cds",SQLBuilder.EQUALS,mandato_reversaleBulk.getCd_cds());
		if (mandato_reversaleBulk.isMandato()){
			sql.addClause("AND","ti_documento",SQLBuilder.EQUALS, V_doc_cont_compBulk.TIPO_DOC_CONT_MANDATO);
		} else {
			sql.addClause("AND","ti_documento",SQLBuilder.EQUALS, V_doc_cont_compBulk.TIPO_DOC_CONT_REVERSALE);
		}
		return fetchAll(sql);
	}

}