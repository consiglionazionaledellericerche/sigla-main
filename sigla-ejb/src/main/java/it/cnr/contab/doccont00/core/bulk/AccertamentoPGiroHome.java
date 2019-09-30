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

import java.util.*;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class AccertamentoPGiroHome extends AccertamentoHome {
public AccertamentoPGiroHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
public AccertamentoPGiroHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
public AccertamentoPGiroHome(java.sql.Connection conn) {
	super(AccertamentoPGiroBulk.class, conn);
}
public AccertamentoPGiroHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(AccertamentoPGiroBulk.class, conn, persistentCache);
}
/**
 * Metodo per selezionare gli accertamenti su partita di giro.
 *
 * @return sql il risultato della selezione
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, new Boolean(true));
	return sql;
}
/* ricerca dell'uo Cds */
public CdsBulk findCdsSAC () throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException
{
	CdsHome cdsHome = (CdsHome) getHomeCache().getHome( CdsBulk.class );
	SQLBuilder sql = cdsHome.createSQLBuilder();
	sql.addClause("AND","cd_tipo_unita",sql.EQUALS, (Tipo_unita_organizzativaHome.TIPO_UO_SAC) );
	sql.addClause("AND","fl_cds",sql.EQUALS, new Boolean(true) );
	List result = cdsHome.fetchAll( sql );
	if (result.size() > 0)
		return (CdsBulk) result.get(0);		
	return null;
}
/**
 * Metodo per selezionare i capitoli di Entrata Cnr o Cds.
 *
 * @param bulk <code>AccertamentoPGiroBulk</code> l'accertamento su partita di giro
 * @param home istanza di <code>Voce_fHome</code>
 * @param voce_f <code>Voce_fBulk</code> i capitoli di entrata o di spesa
 * @param clause <code>CompoundFindClause</code> le clausole della selezione
 *
 * @return sql i capitoli di Entrata definiti per l'accertamento
 */
public SQLBuilder selectCapitoloByClause(AccertamentoBulk acc, V_voce_f_partita_giroHome home, V_voce_f_partita_giroBulk voce_f, CompoundFindClause clause) throws IntrospectionException, PersistencyException, java.sql.SQLException 
{
	AccertamentoPGiroBulk bulk = (AccertamentoPGiroBulk) acc;
	SQLBuilder sql = getHomeCache().getHome( V_voce_f_partita_giroBulk.class ).createSQLBuilder();
	boolean isNuovoPdg = ((Parametri_cnrHome)getHomeCache().getHome( Parametri_cnrBulk.class )).isNuovoPdg(bulk.getEsercizio());

	if (acc instanceof AccertamentoPGiroResiduoBulk)
    	sql.addSQLClause( "AND", "fl_solo_competenza", sql.EQUALS, "N");
	else if(acc instanceof AccertamentoPGiroBulk )
	    	sql.addSQLClause( "AND", "fl_solo_residuo", sql.EQUALS, "N"); 
	 if ( bulk.getCd_uo_ente().equals( bulk.getCd_unita_organizzativa()))
	{
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
		/* simona 14.5.2002 
		CdsBulk cds = findCdsSAC();
		sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, cds.getCd_unita_organizzativa() );		*/
		if (!isNuovoPdg) {
			if (acc instanceof AccertamentoPGiroResiduoBulk){
				sql.openParenthesis( "AND");		
				sql.openParenthesis( "AND");
				sql.addSQLClause( "AND", "cd_unita_organizzativa", sql.EQUALS, bulk.getCd_uo_origine());
				sql.addSQLClause( "OR", "cd_unita_organizzativa", sql.ISNULL,null);
				sql.closeParenthesis();
				sql.closeParenthesis();		
			}else{
				sql.openParenthesis( "AND");		
				sql.openParenthesis( "AND");
				sql.addSQLClause( "AND", "cd_unita_organizzativa", sql.EQUALS, bulk.getCd_uo_origine());
				sql.addSQLClause( "AND", "fl_azzera_residui",  sql.EQUALS, "N");
				sql.closeParenthesis();
				sql.openParenthesis( "OR");
				sql.addSQLClause( "OR", "cd_unita_organizzativa", sql.ISNULL,null);
				sql.addSQLClause( "AND", "fl_azzera_residui",  sql.EQUALS, "Y");
				sql.closeParenthesis();
				sql.closeParenthesis();		
			}
		}
	}	
	else // == ACCERT_PGIRO
	{
		if (!isNuovoPdg)
			sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
	}	
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
//	sql.addClause("AND", "ti_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_CAPITOLO  );
	sql.addClause("AND", "fl_mastrino", SQLBuilder.EQUALS, new Boolean(true)  );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE );	
	sql.addSQLClause( "AND", "FL_PARTITA_GIRO", sql.EQUALS, "Y" );
	if ( !bulk.getCd_cds_origine().equals( findCd_cdsSAC()))
		sql.addSQLClause( "AND", "FL_VOCE_SAC", sql.EQUALS, "N" );	

	sql.addClause( clause );	
	return sql;

}
public SQLBuilder selectElemento_voceContrByClause( AccertamentoBulk bulk, Elemento_voceHome home,Elemento_voceBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = getHomeCache().getHome(Elemento_voceBulk.class).createSQLBuilder();
	if (clause != null) 
	  sql.addClause(clause);
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );		
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
	sql.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, new Boolean(true) );	
	if ( !Tipo_unita_organizzativaHome.TIPO_UO_SAC.equals(bulk.getCds().getCd_tipo_unita())  )
			sql.addClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, new Boolean( false) );		
	sql.addClause( clause ); 
	if (bulk instanceof AccertamentoPGiroResiduoBulk)
    	sql.addSQLClause( "AND", "fl_solo_competenza", sql.EQUALS, "N");
	else if(bulk instanceof AccertamentoPGiroBulk )
	   	sql.addSQLClause( "AND", "fl_solo_residuo", sql.EQUALS, "N"); 
	return sql;
		
}
}
