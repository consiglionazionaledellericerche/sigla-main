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

import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class ImpegnoPGiroHome extends ObbligazioneHome {
public ImpegnoPGiroHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
public ImpegnoPGiroHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
public ImpegnoPGiroHome(java.sql.Connection conn) {
	super(ImpegnoPGiroBulk.class, conn);
}
public ImpegnoPGiroHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ImpegnoPGiroBulk.class, conn, persistentCache);
}
/**
 * Metodo per selezionare le obbligazioni su partita di giro (di tipo IMP o OBB_PGIRO).
 *
 * @return sql il risultato della selezione
 */
public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, Boolean.TRUE );

//	SimpleFindClause clause1 = new SimpleFindClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP );
//	SimpleFindClause clause2 = new SimpleFindClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_PGIRO );
//	CompoundFindClause clause = new CompoundFindClause( clause1, clause2 );
//	sql.addClause( clause );
	return sql;
}
/**
 * Metodo per cercare la scadenza dell'accertamento su partita di giro, associato all'impegno
 *
 * @param esercizio l'esercizio dell'obbligazione su partita di giro
 * @param cd_cds il centro di spesa dell'obbligazione su partita di giro
 * @param esercizio_originale l'esercizio originale dell'obbligazione su partita di giro
 * @param pg_obbligazione il numero dell'obbligazione su partita di giro
 *
 * @return <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento su partita di giro
 *
 */
public Accertamento_scadenzarioBulk findAccertamentoScadenzarioPGiro( it.cnr.jada.UserContext userContext,Integer esercizio, String cd_cds, Integer esercizio_originale, Long pg_obbligazione ) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.comp.ApplicationException
{
	SQLBuilder sql = getHomeCache().getHome( Ass_obb_acr_pgiroBulk.class ).createSQLBuilder();
	sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, cd_cds );
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio );
	sql.addClause("AND", "esercizio_ori_obbligazione", SQLBuilder.EQUALS, esercizio_originale );
	sql.addClause("AND", "pg_obbligazione", SQLBuilder.EQUALS, pg_obbligazione );
	List result = getHomeCache().getHome( Ass_obb_acr_pgiroBulk.class ).fetchAll( sql );
	if ( result.isEmpty() )
		throw new it.cnr.jada.comp.ApplicationException( "Non esiste associazione fra impegno e accertamento su partita di giro. Esercizio: " + esercizio +
			" - Cds: " + cd_cds + " - EsOri: " + esercizio_originale + " - Impegno: " + pg_obbligazione );
	Ass_obb_acr_pgiroBulk ass = (Ass_obb_acr_pgiroBulk) result.get(0);
	sql = getHomeCache().getHome( Accertamento_scadenzarioBulk.class ).createSQLBuilder();
	sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, cd_cds );
	sql.addClause( "AND", "esercizio", SQLBuilder.EQUALS, esercizio );
	sql.addClause("AND", "esercizio_originale", SQLBuilder.EQUALS, ass.getEsercizio_ori_accertamento());
	sql.addClause("AND", "pg_accertamento", SQLBuilder.EQUALS, ass.getPg_accertamento() );
	result = getHomeCache().getHome( Accertamento_scadenzarioBulk.class ).fetchAll( sql );
	getHomeCache().fetchAll(userContext);
	return (Accertamento_scadenzarioBulk) result.get(0);
}
/**
 * Metodo per cercare la voce del piano dei conti definita per Cnr o Cds dell'impegno.
 *
 * @param bulk <code>ImpegnoPGiroBulk</code> il contesto (impegno) in cui viene fatta la ricerca dell'elemento voce
 * @param home istanza di <code>Elemento_voceHome</code>
 * @param bulkClause <code>OggettoBulk</code> elemento voce su cui viene fatta la ricerca
 * @param clause <code>CompoundFindClause</code> le clausole della selezione
 *
 * @return sql il risultato della selezione
 *
 */
public SQLBuilder selectElemento_voceByClause( ImpegnoPGiroBulk bulk, Elemento_voceHome home,Elemento_voceBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException 
{
	boolean isNuovoPdg = ((Parametri_cnrHome)getHomeCache().getHome(Parametri_cnrBulk.class)).isNuovoPdg(bulk.getEsercizio());
	
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
	if ( !isNuovoPdg && bulk.getCd_uo_ente().equals( bulk.getCd_unita_organizzativa()))
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CNR );
	else // == OBB_PGIRO
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );		
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
	if (!isNuovoPdg)
		sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, home.PARTE_2 );
	sql.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, new Boolean(true) );	
	if ( !Tipo_unita_organizzativaHome.TIPO_UO_SAC.equals(bulk.getCds().getCd_tipo_unita())  )
			sql.addClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, new Boolean( false) );		
	sql.addClause( clause ); 
	sql.addClause("AND", "fl_solo_residuo", SQLBuilder.EQUALS, new Boolean( false) );
	return sql;
		
}
public SQLBuilder selectElemento_voceContrByClause(ImpegnoPGiroBulk bulk,Elemento_voceHome home, Elemento_voceBulk voce, CompoundFindClause clause) throws IntrospectionException, PersistencyException, java.sql.SQLException 
{
	SQLBuilder sql = getHomeCache().getHome(Elemento_voceBulk.class).createSQLBuilder();
	if (clause != null) 
	  sql.addClause(clause);

	sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE );
	if (bulk instanceof ImpegnoPGiroResiduoBulk)
    	sql.addSQLClause( "AND", "fl_solo_competenza", sql.EQUALS, "N");
	else if(bulk instanceof ImpegnoPGiroBulk )
	    	sql.addSQLClause( "AND", "fl_solo_residuo", sql.EQUALS, "N"); 
	sql.addClause(FindClause.AND, "fl_azzera_residui", SQLBuilder.EQUALS, new Boolean( false) );
	sql.addSQLClause( "AND", "FL_PARTITA_GIRO", sql.EQUALS, "Y" );
	
	return sql;
 }
}
