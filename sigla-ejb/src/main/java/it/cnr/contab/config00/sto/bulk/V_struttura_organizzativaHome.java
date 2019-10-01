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
* Created by Generator 1.0
* Date 04/11/2005
*/
package it.cnr.contab.config00.sto.bulk;
import java.util.Collection;
import java.util.List;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_struttura_organizzativaHome extends BulkHome {
	public final static String LIVELLO_CDS = "CDS";
	public final static String LIVELLO_UO = "UO";
	public final static String LIVELLO_CDR = "CDR";

	public V_struttura_organizzativaHome(java.sql.Connection conn) {
		super(V_struttura_organizzativaBulk.class, conn);
	}
	public V_struttura_organizzativaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_struttura_organizzativaBulk.class, conn, persistentCache);
	}
	/**
	 * Metodo per cercare nella struttura organizzativa dell'esercizio <code>esercizio</code> 
	 * tutte le UO appartenenti al CDS cui appartiene la UO richiedente.
	 *
	 * @param uo <code>UnitaOrganizzativaBulk</code> l'Unità Organizzativa richiedente
	 * @param esercizio <code>int</code> l'esercizio della struttura organizzativa da interrogare
	 *
	 * @return result una collection di Uo <Unita_organizzativeBulk> appartenenti al CDS di appartenenza della UO richiedente nell'esercizio indicato.
	 */
	public Collection findUoCollegateCDS(Unita_organizzativaBulk uo, Integer esercizio) throws PersistencyException
	{
		PersistentHome homeStrutt = getHomeCache().getHome( V_struttura_organizzativaBulk.class );
		SQLBuilder sqlStrutt = homeStrutt.createSQLBuilder();
		sqlStrutt.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",sqlStrutt.EQUALS, esercizio);

		sqlStrutt.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");		
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", "B.ESERCIZIO");
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_CDS", "B.CD_CDS");
		sqlStrutt.addSQLClause("AND","B.CD_ROOT",sqlStrutt.EQUALS, uo.getCd_unita_organizzativa() );
		sqlStrutt.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", sqlStrutt.EQUALS, this.LIVELLO_UO);
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHomeCache().getHome( Unita_organizzativaBulk.class );
		SQLBuilder sql = uoHome.createSQLBuilder();
		sql.addSQLExistsClause("AND", sqlStrutt);

		return uoHome.fetchAll( sql);
	}	
	/**
	 * Metodo per cercare nella struttura organizzativa dell'esercizio <code>esercizio</code> 
	 * tutte i CDR appartenenti alla UO richiedente.
	 *
	 * @param uo <code>UnitaOrganizzativaBulk</code> l'Unità Organizzativa richiedente
	 * @param esercizio <code>int</code> l'esercizio della struttura organizzativa da interrogare
	 *
	 * @return result una Collection di Cdr <CdrBulk> appartenenti alla UO richiedente nell'esercizio indicato.
	 */
	public Collection findCDRCollegatiUO(Unita_organizzativaBulk uo, Integer esercizio) throws PersistencyException
	{
		PersistentHome homeStrutt = getHomeCache().getHome( V_struttura_organizzativaBulk.class );
		SQLBuilder sqlStrutt = homeStrutt.createSQLBuilder();
		sqlStrutt.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",sqlStrutt.EQUALS, esercizio);
		sqlStrutt.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",sqlStrutt.EQUALS, uo.getCd_unita_organizzativa() );
		sqlStrutt.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", sqlStrutt.EQUALS, this.LIVELLO_CDR);
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "CDR.CD_CENTRO_RESPONSABILITA");

		CdrHome cdrHome = (CdrHome)getHomeCache().getHome( CdrBulk.class );
		SQLBuilder sql = cdrHome.createSQLBuilder();
		sql.addSQLExistsClause("AND", sqlStrutt);

		return cdrHome.fetchAll( sql);
	}
	/**
	 * Metodo per cercare nella struttura organizzativa dell'esercizio <code>esercizio</code> 
	 * il CDR di riferimento della CDS cui appartiene la UO richiedente.
	 *
	 * @param uo <code>UnitaOrganizzativaBulk</code> l'Unità Organizzativa richiedente
	 * @param esercizio <code>int</code> l'esercizio della struttura organizzativa da interrogare
	 *
	 * @return result il Cdr <CdrBulk> di riferimento della CDS cui appartiene la UO richiedente nell'esercizio indicato.
	 */
	public CdrBulk findCDRBaseCDS(Unita_organizzativaBulk uo, Integer esercizio) throws PersistencyException
	{
		PersistentHome homeStrutt = getHomeCache().getHome( V_struttura_organizzativaBulk.class );
		SQLBuilder sqlStrutt = homeStrutt.createSQLBuilder();
		sqlStrutt.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",sqlStrutt.EQUALS, esercizio);

		sqlStrutt.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");		
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", "B.ESERCIZIO");
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_CDS", "B.CD_CDS");
		sqlStrutt.addSQLClause("AND","B.CD_ROOT",sqlStrutt.EQUALS, uo.getCd_unita_organizzativa() );
		sqlStrutt.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", sqlStrutt.EQUALS, this.LIVELLO_CDR);
		sqlStrutt.addSQLClause("AND", "v_struttura_organizzativa.fl_uo_cds", sqlStrutt.EQUALS, "Y");
		sqlStrutt.addSQLClause("AND", "v_struttura_organizzativa.fl_cdr_uo", sqlStrutt.EQUALS, "Y");
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "CDR.CD_CENTRO_RESPONSABILITA");

		CdrHome cdrHome = (CdrHome)getHomeCache().getHome( CdrBulk.class );
		SQLBuilder sql = cdrHome.createSQLBuilder();
		sql.addSQLExistsClause("AND", sqlStrutt);

		List result = cdrHome.fetchAll( sql );
		if (result.size() > 0)
			return (CdrBulk)result.get(0);		
		return null;
	}
	/**
	 * Metodo per cercare nella struttura organizzativa dell'esercizio <code>esercizio</code> 
	 * il CDR di riferimento della UO richiedente.
	 *
	 * @param uo <code>UnitaOrganizzativaBulk</code> l'Unità Organizzativa richiedente
	 * @param esercizio <code>int</code> l'esercizio della struttura organizzativa da interrogare
	 *
	 * @return result il Cdr <CdrBulk> di riferimento della UO richiedente nell'esercizio indicato.
	 */
	public Collection findCDRBaseUO(Unita_organizzativaBulk uo, Integer esercizio) throws PersistencyException
	{
		PersistentHome homeStrutt = getHomeCache().getHome( V_struttura_organizzativaBulk.class );
		SQLBuilder sqlStrutt = homeStrutt.createSQLBuilder();
		sqlStrutt.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",sqlStrutt.EQUALS, esercizio);

		sqlStrutt.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");		
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", "B.ESERCIZIO");
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_CDS", "B.CD_CDS");
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "B.CD_UNITA_ORGANIZZATIVA");
		sqlStrutt.addSQLClause("AND","B.CD_ROOT",sqlStrutt.EQUALS, uo.getCd_unita_organizzativa() );
		sqlStrutt.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", sqlStrutt.EQUALS, this.LIVELLO_CDR);
		sqlStrutt.addSQLClause("AND", "v_struttura_organizzativa.fl_cdr_uo", sqlStrutt.EQUALS, "Y");
		sqlStrutt.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "CDR.CD_CENTRO_RESPONSABILITA");

		CdrHome cdrHome = (CdrHome)getHomeCache().getHome( CdrBulk.class );
		SQLBuilder sql = cdrHome.createSQLBuilder();
		sql.addSQLExistsClause("AND", sqlStrutt);

		return cdrHome.fetchAll( sql);
	}
}