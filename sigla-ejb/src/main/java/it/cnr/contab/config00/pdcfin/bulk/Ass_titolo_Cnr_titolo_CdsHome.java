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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce i titoli del Cnr associati ai titoli del Cds.
 */
public class Ass_titolo_Cnr_titolo_CdsHome extends Ass_ev_evHome {
public Ass_titolo_Cnr_titolo_CdsHome(java.sql.Connection conn) {
	super(Ass_titolo_Cnr_titolo_CdsBulk.class,conn);
}
public Ass_titolo_Cnr_titolo_CdsHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Ass_titolo_Cnr_titolo_CdsBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli elementi voce quelli relativi ai titoli di spesa
 * del CNR e ai titoli di spesa del CDS.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_TITOLO );	
	sql.addClause("AND", "ti_appartenenza_coll", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione_coll", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce_coll", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_TITOLO );		
	sql.addClause("AND", "cd_natura", SQLBuilder.EQUALS, "*" );
	return sql; 
}
/**
 * Restituisce il SQLBuilder per selezionare le Categorie di Titolo di Spesa del Cds per l'esercizio di scrivania.
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */
public SQLBuilder selectElemento_voce_collByClause( Ass_titolo_Cnr_titolo_CdsBulk bulk, Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_TITOLO );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );
	return sql;
}
/**
 * Restituisce il SQLBuilder per selezionare le Categorie di Titolo di Spesa del Cnr per l'esercizio di scrivania.
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */
public SQLBuilder selectElemento_voceByClause( Ass_titolo_Cnr_titolo_CdsBulk bulk, Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_TITOLO );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );
	return sql;
}
}
