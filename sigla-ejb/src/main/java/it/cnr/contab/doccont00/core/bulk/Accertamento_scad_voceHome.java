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

package it.cnr.contab.doccont00.core.bulk;

import java.math.BigDecimal;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Accertamento_scad_voceHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Accertamento_scad_voceHome(java.sql.Connection conn) {
	super(Accertamento_scad_voceBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Accertamento_scad_voceHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Accertamento_scad_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Accertamento_scad_voceBulk.class,conn,persistentCache);
}
/**
 * Ricerco i dettagli di tutte le scadenze dell'accertamento dato
 * 
 *
 * @param accertamento	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findDettagli_scadenze( AccertamentoBulk accertamento ) throws IntrospectionException,PersistencyException 
{
	SQLBuilder sql = createSQLBuilder();
	
	sql.addClause("AND","cd_cds",sql.EQUALS, accertamento.getCd_cds());
	sql.addClause("AND","esercizio",sql.EQUALS, accertamento.getEsercizio());
	sql.addClause("AND","esercizio_originale",sql.EQUALS, accertamento.getEsercizio_originale());
	sql.addClause("AND","pg_accertamento",sql.EQUALS, accertamento.getPg_accertamento());
	sql.addOrderBy("PG_ACCERTAMENTO_SCADENZARIO");
	
	return fetchAll(sql);
}
}
