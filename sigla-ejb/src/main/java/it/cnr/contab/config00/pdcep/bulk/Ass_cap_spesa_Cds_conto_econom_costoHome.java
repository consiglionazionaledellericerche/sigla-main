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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce i capitoli di spesa del Cds associati ai conti economico
 * patrimoniali di costo.
 */
public class Ass_cap_spesa_Cds_conto_econom_costoHome extends Ass_ev_voceepHome {
public Ass_cap_spesa_Cds_conto_econom_costoHome(java.sql.Connection conn) {
	super(Ass_cap_spesa_Cds_conto_econom_costoBulk.class,conn);
}
public Ass_cap_spesa_Cds_conto_econom_costoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Ass_cap_spesa_Cds_conto_econom_costoBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli elementi voce quelli relativi ai capitoli di spesa
 * del CDS.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.APPARTENENZA_CDS);
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE);
	return sql; 
}
/**
 * Restituisce il SQLBuilder per selezionare le Categorie di Spesa del Cds per l'esercizio di scrivania.
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */
public SQLBuilder selectElemento_voceByClause( Ass_cap_spesa_Cds_conto_econom_costoBulk bulk, Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException, IntrospectionException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );	
	return sql;
}
/**
 * Restituisce il SQLBuilder per selezionare gli elementi con Sezione diversa da Avere per l'esercizio di scrivania
 * e voce di costo.
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */
public SQLBuilder selectVoce_epByClause( Ass_cap_spesa_Cds_conto_econom_costoBulk bulk, ContoHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException, IntrospectionException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_voce_ep", SQLBuilder.EQUALS, home.TIPO_CONTO );		
	sql.addClause("AND", "ti_sezione", SQLBuilder.NOT_EQUALS, home.SEZIONE_AVERE );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
	/*SimpleFindClause s1 = new SimpleFindClause("AND", "natura_voce",  SQLBuilder.EQUALS, "EPC" );
	SimpleFindClause s2 = new SimpleFindClause("OR", "natura_voce", SQLBuilder.EQUALS, "EEC" );
	CompoundFindClause c = new CompoundFindClause(s1, s2);
	sql.addClause( c );*/
	sql.addClause( clause );
	return sql;
	
}
public SQLBuilder selectVoce_ep_contrByClause( Ass_cap_spesa_Cds_conto_econom_costoBulk bulk, ContoHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException, IntrospectionException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_voce_ep", SQLBuilder.EQUALS, home.TIPO_CONTO );		
	sql.addClause("AND", "ti_sezione", SQLBuilder.NOT_EQUALS, home.SEZIONE_DARE );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
	/*SimpleFindClause s1 = new SimpleFindClause("AND", "natura_voce",  SQLBuilder.EQUALS, "EPC" );
	SimpleFindClause s2 = new SimpleFindClause("OR", "natura_voce", SQLBuilder.EQUALS, "EEC" );
	CompoundFindClause c = new CompoundFindClause(s1, s2);
	sql.addClause( c );*/
	sql.addClause( clause );
	return sql;
	
}
}
