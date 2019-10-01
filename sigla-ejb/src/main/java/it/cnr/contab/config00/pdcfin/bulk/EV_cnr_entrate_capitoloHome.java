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
 * Home che gestisce i capitoli di entrata del CNR
 */

public class EV_cnr_entrate_capitoloHome extends Elemento_voceHome {
	public EV_cnr_entrate_capitoloHome(java.sql.Connection conn) {
	super(EV_cnr_entrate_capitoloBulk.class,conn);
}
public EV_cnr_entrate_capitoloHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(EV_cnr_entrate_capitoloBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli elementi voce quelli relativi ai capitoli di entrata
 * del CNR
 * @return SQLBuilder 
 */

public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_ENTRATE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, TIPO_CAPITOLO );				
	return sql;
}
/**
 * Restituisce il SQLBuilder per selezionare le Categorie di Entrata del Cnr per l'esercizio di scrivania
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */

public SQLBuilder selectElemento_padreByClause(EV_cnr_entrate_capitoloBulk bulk,Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_ENTRATE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, (String) Elemento_voceHome.getTipoPadre(APPARTENENZA_CNR,GESTIONE_ENTRATE,TIPO_CAPITOLO ));
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );
	return sql;
}
}
