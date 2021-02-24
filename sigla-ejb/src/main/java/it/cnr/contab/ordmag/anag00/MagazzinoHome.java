/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;

public class MagazzinoHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public MagazzinoHome(Connection conn) {
		super(MagazzinoBulk.class, conn);
	}
	public MagazzinoHome(Connection conn, PersistentCache persistentCache) {
		super(MagazzinoBulk.class, conn, persistentCache);
	}
	public java.util.List findCategoriaGruppoInventList(MagazzinoBulk mag ) throws IntrospectionException,PersistencyException, ApplicationException
	{
		PersistentHome repHome = getHomeCache().getHome(AbilitBeneServMagBulk.class);
		SQLBuilder sql = repHome.createSQLBuilder();
		
		sql.addSQLClause( FindClause.AND, "cd_magazzino", SQLBuilder.EQUALS, mag.getCdMagazzino());
		sql.addSQLClause( FindClause.AND, "cd_cds", SQLBuilder.EQUALS, mag.getCdCds());
		return repHome.fetchAll(sql);
	}
	
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		return sql;
	}

	private SQLBuilder selectTipoMovimento(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql = tipoMovimentoMagHome.selectByClause(userContext, clause);
		return sql;
	}


	public SQLBuilder selectTipoMovimentoMagCarMagByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.CARICO_AUTOMATICO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagCarTraByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.CARICO_AUTOMATICO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);

	}

	public SQLBuilder selectTipoMovimentoMagCarFmaByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.CARICO_AUTOMATICO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagTraScaByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.SCARICO_AUTOMATICO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagRvPosByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.RETTIFICA_PREZZO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagRvNegByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.RETTIFICA_PREZZO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagChiByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.CHIUSURE);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagTraCarByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.CARICO_TRASFERIMENTO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}

	public SQLBuilder selectTipoMovimentoMagScaUoByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, TipoMovimentoMagHome  tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		if ( clause==null)
			clause =  new CompoundFindClause();
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.EQUALS, TipoMovimentoMagBulk.SCARICO_AUTOMATICO);
		return selectTipoMovimento(userContext,magazzinoBulk,tipoMovimentoMagHome,tipoMovimentoMagBulk,clause);
	}



	private SQLBuilder selectRaggrMagazzino(UserContext userContext, MagazzinoBulk magazzinoBulk, RaggrMagazzinoHome  raggrMagazzinoHome, RaggrMagazzinoBulk raggrMagazzinoBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql = raggrMagazzinoHome.selectByClause(userContext, clause);
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		return sql;
	}
	public SQLBuilder selectRaggrMagazzinoRimByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, RaggrMagazzinoHome  raggrMagazzinoHome, RaggrMagazzinoBulk raggrMagazzinoBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		return selectRaggrMagazzino(userContext,magazzinoBulk,raggrMagazzinoHome,raggrMagazzinoBulk,clause);
	}

	public SQLBuilder selectRaggrMagazzinoScaByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, RaggrMagazzinoHome  raggrMagazzinoHome, RaggrMagazzinoBulk raggrMagazzinoBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		return selectRaggrMagazzino(userContext,magazzinoBulk,raggrMagazzinoHome,raggrMagazzinoBulk,clause);
	}

	public SQLBuilder selectLuogoConsegnaMagByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, LuogoConsegnaMagHome  luogoConsegnaMagHome, LuogoConsegnaMagBulk luogoConsegnaMagBulk, CompoundFindClause clause)  throws  PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql = luogoConsegnaMagHome.selectByClause(userContext, clause);
		sql.addSQLClause("AND","CD_CDS",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		return sql;
	}

	public SQLBuilder selectMagazziniAbilitatiByClause(UserContext userContext, UnitaOperativaOrdBulk unitaOperativa, String tipoOperazione, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = this.selectByClause(compoundfindclause);
		sql.addClause(FindClause.AND, "cdCds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));

		if (unitaOperativa != null){
			AbilUtenteUopOperBulk abil = (AbilUtenteUopOperBulk)getHomeCache().getHome(AbilUtenteUopOperBulk.class).findByPrimaryKey(new AbilUtenteUopOperBulk(userContext.getUser(), unitaOperativa.getCdUnitaOperativa(), tipoOperazione));
			if (abil == null) {
				sql.addSQLClause(FindClause.AND, "1!=1");
			} else if (Boolean.FALSE.equals(abil.getTuttiMagazzini())) {
				sql.addTableToHeader("ABIL_UTENTE_UOP_OPER_MAG", "B");
				sql.addSQLJoin("MAGAZZINO.CD_CDS", "B.CD_CDS");
				sql.addSQLJoin("MAGAZZINO.CD_MAGAZZINO", "B.CD_MAGAZZINO");
				sql.addSQLClause(FindClause.AND, "B.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, abil.getCdTipoOperazione());
				sql.addSQLClause(FindClause.AND, "B.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, abil.getCdUnitaOperativa());
				sql.addSQLClause(FindClause.AND, "B.CD_UTENTE", SQLBuilder.EQUALS, abil.getCdUtente());
			}
		}

		return sql;
	}

	public SQLBuilder selectUnitaOperativaOrdByClause(UserContext userContext, MagazzinoBulk magazzinoBulk, UnitaOperativaOrdHome  unitaOperativaOrdHome, UnitaOperativaOrdBulk unitaOperativaOrdBulk, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql = unitaOperativaOrdHome.selectByClause(clause);

		sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		return sql;
	}



}