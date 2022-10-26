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

package it.cnr.contab.coepcoan00.tabrif.bulk;

import it.cnr.contab.coepcoan00.core.bulk.*;
import java.util.*;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.config00.pdcep.bulk.Voce_epBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_anag_voce_epHome extends BulkHome {
public Ass_anag_voce_epHome(java.sql.Connection conn) {
	super(Ass_anag_voce_epBulk.class,conn);
}
public Ass_anag_voce_epHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_anag_voce_epBulk.class,conn,persistentCache);
}
public List<Ass_anag_voce_epBulk> findAssociazioniPerScrittura( Scrittura_partita_doppiaBulk scrittura ) throws PersistencyException
{
//	AnagraficoBulk anag = (AnagraficoBulk) getHome( AnagraficoBulk.class).findByPrimaryKey( scrittura.getTerzo().getAnagrafico());
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "esercizio", sql.EQUALS, scrittura.getEsercizio());
	sql.openParenthesis( "AND" );
	sql.addClause( "AND", "ti_terzo", sql.EQUALS, scrittura.getTerzo().getTi_terzo());
	sql.addClause( "OR", "ti_terzo", sql.EQUALS, "*");
	sql.closeParenthesis();
	sql.openParenthesis( "AND" );
	sql.addClause( "AND", "italiano_estero", sql.EQUALS, scrittura.getTerzo().getAnagrafico().getTi_italiano_estero());
	sql.addClause( "OR", "italiano_estero", sql.EQUALS, "*");
	sql.closeParenthesis();
	sql.openParenthesis( "AND" );
	sql.addClause( "AND", "ti_entita", sql.EQUALS, scrittura.getTerzo().getAnagrafico().getTi_entita());
	sql.addClause( "OR", "ti_entita", sql.EQUALS, "*");
	sql.closeParenthesis();
	if ( scrittura.getTerzo().getAnagrafico().getTi_entita().equals( AnagraficoBulk.GIURIDICA) )
	{
		sql.openParenthesis( "AND" );
		sql.addClause( "AND", "ente_altro", sql.EQUALS, scrittura.getTerzo().getAnagrafico().getTi_entita_giuridica());
		sql.addClause( "OR", "ente_altro", sql.EQUALS, "*");
		sql.closeParenthesis();
	}	
	sql.openParenthesis( "AND" );
	sql.addClause( "AND", "cd_classific_anag", sql.EQUALS, scrittura.getTerzo().getAnagrafico().getCd_classific_anag());
	sql.addClause( "OR", "cd_classific_anag", sql.EQUALS, "*");
	sql.closeParenthesis();
	sql.addOrderBy( "ti_terzo desc" );
	sql.addOrderBy( "italiano_estero  desc" );
	sql.addOrderBy( "ti_entita desc" );
	sql.addOrderBy( "ente_altro desc" );
	sql.addOrderBy( "cd_classific_anag desc" );

	List result = fetchAll( sql );
	return result;
}
}
