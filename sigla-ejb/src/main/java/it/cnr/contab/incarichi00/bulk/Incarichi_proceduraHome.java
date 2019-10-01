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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.List;
public class Incarichi_proceduraHome extends BulkHome {
	public Incarichi_proceduraHome(Connection conn) {
		super(Incarichi_proceduraBulk.class, conn);
	}
	public Incarichi_proceduraHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_proceduraBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, it.cnr.jada.comp.ComponentException {
		try {
			((Incarichi_proceduraBulk)bulk).setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			((Incarichi_proceduraBulk)bulk).setPg_procedura(
					new Long(
					((Long)findAndLockMax( bulk, "pg_procedura", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			 throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		}
	}
	public java.util.List findIncarichi_procedura_annoList( it.cnr.jada.UserContext userContext,Incarichi_proceduraBulk procedura ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome procAnnoHome = getHomeCache().getHome(Incarichi_procedura_annoBulk.class );
		SQLBuilder sql = procAnnoHome.createSQLBuilder();
		if (procedura.getEsercizio()==null)
			sql.addClause("AND","esercizio",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS, procedura.getEsercizio());
		
		if (procedura.getPg_procedura()==null)
			sql.addClause("AND","pg_procedura",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","pg_procedura",SQLBuilder.EQUALS, procedura.getPg_procedura());
		
		sql.addOrderBy("ESERCIZIO_LIMITE");
		List l =  procAnnoHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findIncarichi_repertorioList( it.cnr.jada.UserContext userContext,Incarichi_proceduraBulk procedura ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome repHome = getHomeCache().getHome(Incarichi_repertorioBulk.class );
		SQLBuilder sql = repHome.createSQLBuilder();
		if (procedura.getEsercizio()==null)
			sql.addClause("AND","esercizio_procedura",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio_procedura",SQLBuilder.EQUALS, procedura.getEsercizio());
		
		if (procedura.getPg_procedura()==null)
			sql.addClause("AND","pg_procedura",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","pg_procedura",SQLBuilder.EQUALS, procedura.getPg_procedura());
		
		return repHome.fetchAll(sql);
//		getHomeCache().fetchAll(userContext);
//		return l;
	}
	/**
	 * Recupera tutti i dati nella tabella INCARICHI_PROCEDURA_ARCHIVIO relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Incarichi_procedura_archivioBulk</code>
	 */

	public java.util.Collection findArchivioAllegati(Incarichi_proceduraBulk procedura) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Incarichi_procedura_archivioBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS,procedura.getEsercizio());
		sql.addClause("AND","pg_procedura",SQLBuilder.EQUALS,procedura.getPg_procedura());
		sql.addOrderBy("PROGRESSIVO_RIGA");
		return dettHome.fetchAll(sql);
	}	
	public java.util.Collection findIncarichiProcedura(Incarichi_richiestaBulk incRichiesta) throws PersistencyException{
		SQLBuilder sql = super.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_richiesta",SQLBuilder.EQUALS,incRichiesta.getEsercizio());
		sql.addClause(FindClause.AND,"pg_richiesta",SQLBuilder.EQUALS,incRichiesta.getPg_richiesta());
		sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_proceduraBulk.STATO_ANNULLATO);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_proceduraBulk.STATO_RESPINTO);
		return fetchAll(sql);
	}
	/**
	 * Recupera il totale delle Obbligazioni legate all'incarico
	 *
	 * @param esercizio del'incarico
	 * @param progressivo dell'incarico
	 *
	 * @return java.math.BigDecimal
	 */

	public SQLBuilder calcolaTotObbligazioni(it.cnr.jada.UserContext userContext,Incarichi_proceduraBulk incarico) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(IM_OBBLIGAZIONE) TOTALE ");
		sql.addTableToHeader("INCARICHI_REPERTORIO");
		sql.addSQLJoin("OBBLIGAZIONE.ESERCIZIO_REP", "INCARICHI_REPERTORIO.ESERCIZIO");
		sql.addSQLJoin("OBBLIGAZIONE.PG_REPERTORIO", "INCARICHI_REPERTORIO.PG_REPERTORIO");
		sql.addSQLClause("AND","INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA",SQLBuilder.EQUALS,incarico.getEsercizio());
		sql.addSQLClause("AND","INCARICHI_REPERTORIO.PG_PROCEDURA",SQLBuilder.EQUALS,incarico.getPg_procedura());
		sql.addJoin("esercizio","esercizio_originale");		
		return sql;
	}

	public java.util.List findIncarichi_procedura_noteList( it.cnr.jada.UserContext userContext,Incarichi_proceduraBulk procedura ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome procNoteHome = getHomeCache().getHome(Incarichi_procedura_noteBulk.class );
		SQLBuilder sql = procNoteHome.createSQLBuilder();
		if (procedura.getEsercizio()==null)
			sql.addClause(FindClause.AND,"esercizio",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, procedura.getEsercizio());
		
		if (procedura.getPg_procedura()==null)
			sql.addClause(FindClause.AND,"pg_procedura",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"pg_procedura",SQLBuilder.EQUALS, procedura.getPg_procedura());
		
		sql.addOrderBy("PG_NOTA");
		List l =  procNoteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
}