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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class Incarichi_repertorioHome extends BulkHome {
	public Incarichi_repertorioHome(Connection conn) {
		super(Incarichi_repertorioBulk.class, conn);
	}
	public Incarichi_repertorioHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_repertorioBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, it.cnr.jada.comp.ComponentException {
		try {
			((Incarichi_repertorioBulk)bulk).setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			((Incarichi_repertorioBulk)bulk).setPg_repertorio(
					new Long(
					((Long)findAndLockMax( bulk, "pg_repertorio", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			 throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		}
	}
	public java.util.List findIncarichi_repertorio_annoList( it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk increp ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(Incarichi_repertorio_annoBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (increp.getEsercizio()==null)
			sql.addClause("AND","esercizio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS, increp.getEsercizio());
		
		if (increp.getPg_repertorio()==null)
			sql.addClause("AND","pg_repertorio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","pg_repertorio",SQLBuilder.EQUALS, increp.getPg_repertorio());
		
		sql.addOrderBy("ESERCIZIO_LIMITE");
		return incHome.fetchAll(sql);
//		getHomeCache().fetchAll(userContext);
//		return l;
	}
	/**
	 * Recupera tutti i dati nella tabella INCARICHI_REPERTORIO_ARCHIVIO relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Incarichi_repertorio_archivioBulk</code>
	 */

	public java.util.Collection findArchivioAllegati(Incarichi_repertorioBulk incarico) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Incarichi_repertorio_archivioBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,incarico.getEsercizio());
		sql.addClause("AND","pg_repertorio",SQLBuilder.EQUALS,incarico.getPg_repertorio());
		sql.addOrderBy("PROGRESSIVO_RIGA");
		return dettHome.fetchAll(sql);
	}	
	public Incarichi_repertorioBulk findByIncarichi_richiesta(Incarichi_richiestaBulk incRichiesta) throws PersistencyException{
		SQLBuilder sql = super.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_richiesta",SQLBuilder.EQUALS,incRichiesta.getEsercizio());
		sql.addClause(FindClause.AND,"pg_richiesta",SQLBuilder.EQUALS,incRichiesta.getPg_richiesta());
		sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_repertorioBulk.STATO_ANNULLATO);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_repertorioBulk.STATO_ELIMINATO);
		Broker broker = createBroker(sql);
		if (broker.next())
		  return (Incarichi_repertorioBulk)fetch(broker);
		return null;
	}
	/**
	 * Recupera il totale delle Obbligazioni legate all'incarico
	 *
	 * @param esercizio del'incarico
	 * @param progressivo dell'incarico
	 *
	 * @return java.math.BigDecimal
	 */

	public SQLBuilder calcolaTotObbligazioni(it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk incarico) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(IM_OBBLIGAZIONE) TOTALE ");
		sql.addClause("AND","esercizio_rep",SQLBuilder.EQUALS,incarico.getEsercizio());
		sql.addClause("AND","pg_repertorio",SQLBuilder.EQUALS,incarico.getPg_repertorio());
		sql.addJoin("esercizio","esercizio_originale");		
		return sql;
	}

	public java.util.List findIncarichi_repertorio_varList( it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk increp ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(Incarichi_repertorio_varBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (increp.getEsercizio()==null)
			sql.addClause("AND","esercizio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS, increp.getEsercizio());
		
		if (increp.getPg_repertorio()==null)
			sql.addClause("AND","pg_repertorio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","pg_repertorio",SQLBuilder.EQUALS, increp.getPg_repertorio());
		
		sql.addOrderBy("PROGRESSIVO_RIGA");
		return incHome.fetchAll(sql);
	}

	/**
	 * Recupera tutti i dati nella tabella ASS_INCARICO_UO relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Ass_incarico_uoBulk</code>
	 */

	public java.util.Collection findAssociazioneUO(Incarichi_repertorioBulk incarico) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Ass_incarico_uoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,incarico.getEsercizio());
		sql.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,incarico.getPg_repertorio());
		sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
		return dettHome.fetchAll(sql);
	}

	/**
	 * Recupera tutte le Uo disponibili ad essere associate
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Ass_incarico_uoBulk</code>
	 */

	public java.util.Collection findAssociazioneUODisponibili(Incarichi_repertorioBulk incarico) throws IntrospectionException, PersistencyException {	
		PersistentHome dettHome = getHomeCache().getHome(Unita_organizzativaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		if (!incarico.getAssociazioneUO().isEmpty()){
			SQLBuilder sqlEx = getHomeCache().getHome(Ass_incarico_uoBulk.class).createSQLBuilder();
			sqlEx.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,incarico.getEsercizio());
			sqlEx.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,incarico.getPg_repertorio());
			sqlEx.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA","ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA");
			sql.addSQLNotExistsClause("AND",sqlEx);
		}
		sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
		return dettHome.fetchAll(sql);
	}

	public java.util.List findIncarichi_repertorio_rappList( it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk increp ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(Incarichi_repertorio_rappBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (increp.getEsercizio()==null)
			sql.addClause("AND","esercizio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS, increp.getEsercizio());
		
		if (increp.getPg_repertorio()==null)
			sql.addClause("AND","pg_repertorio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","pg_repertorio",SQLBuilder.EQUALS, increp.getPg_repertorio());
		
		sql.addOrderBy("ANNO_COMPETENZA");
		return incHome.fetchAll(sql);
	}

	public java.util.List findIncarichi_repertorio_rapp_detList( it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk increp ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(Incarichi_repertorio_rapp_detBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (increp.getEsercizio()==null)
			sql.addClause("AND","esercizio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS, increp.getEsercizio());
		
		if (increp.getPg_repertorio()==null)
			sql.addClause("AND","pg_repertorio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","pg_repertorio",SQLBuilder.EQUALS, increp.getPg_repertorio());
		
		sql.addOrderBy("PROGRESSIVO_RIGA");
		return incHome.fetchAll(sql);
	}

	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		if (persistent instanceof Incarichi_repertorioBulk) {
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)persistent;
			try {
				if (((Incarichi_repertorioBulk)persistent).getCd_terzo()!=null) { 
					V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHomeCache().getHome(V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
					V_terzo_per_compensoBulk bulk = home.loadVTerzo(userContext,Tipo_rapportoBulk.ALTRO, incarico.getCd_terzo(), incarico.getDt_inizio_validita(), incarico.getDt_inizio_validita());
					incarico.setV_terzo(bulk);
				}
			} catch (Exception e) {}
		}
		return persistent;
	}

	public java.util.Collection findSediUo(Incarichi_repertorioBulk incarichi_repertorioBulk,
										   SedeAceHome sedeHome,
										   SedeAceBulk bulk)
			throws IntrospectionException, PersistencyException {

		return sedeHome.cercaSedi(incarichi_repertorioBulk.getCd_unita_organizzativa());
	}
}
