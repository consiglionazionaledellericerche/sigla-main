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

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce i capitoli di spesa del Cnr associati alla natura con
 * i capitoli di entrata del Cds.
 */
public class Ass_cap_spesa_Cnr_natura_cap_entrata_CdsHome extends Ass_ev_evHome {
/**
 * Costruttore
 * Creation date: (12/04/2001 08:32:22)
 * @param conn connessione db
 * @return l'istanza creata
 */

public Ass_cap_spesa_Cnr_natura_cap_entrata_CdsHome(java.sql.Connection conn) {
	super(Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk.class,conn);
}
/**
 * Costruttore
 * Creation date: (12/04/2001 08:32:22)
 * @param conn connessione db
 * @param persistentCache
 * @return l'istanza creata
 */

public Ass_cap_spesa_Cnr_natura_cap_entrata_CdsHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli elementi voce quelli relativi ai titoli di spesa
 * del CNR e ai capitoli di entrata del CDS.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_TITOLO );	
	sql.addClause("AND", "ti_appartenenza_coll", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione_coll", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE );
	sql.addClause("AND", "ti_elemento_voce_coll", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_CAPITOLO );
	return sql; 
}
/**
 * Seleziona elementi voce di tipo capitolo di entrata cds
 * Creation date: (12/04/2001 08:32:22)
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool)
 * @return SQLBuilder
 */

 public SQLBuilder selectElemento_voce_collByClause( Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk bulk, Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_ENTRATE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );
	return sql;
}
/**
 * Seleziona elementi voce di tipo titolo di spesa cnr
 * Creation date: (12/04/2001 08:32:22)
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool)
 * @return SQLBuilder
 */

public SQLBuilder selectElemento_voceByClause( Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk bulk, Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_TITOLO );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );
	return sql;
}
/**
 * Seleziona i tipi esistenti di UO escludendo l'UO ente
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool)
 * @return SQLBuilder
 */

 public SQLBuilder selectTipo_unitaByClause( Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk bulk, Tipo_unita_organizzativaHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_tipo_unita", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE );
	sql.addClause( clause );
	return sql;
}
}
