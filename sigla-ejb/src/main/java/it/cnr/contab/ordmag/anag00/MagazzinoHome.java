/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;

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
	public SQLBuilder selectMagazziniAbilitatiByClause(UserContext userContext, UnitaOperativaOrdBulk unitaOperativa, String tipoOperazione, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = this.selectByClause(compoundfindclause);
		sql.addClause(FindClause.AND, "cdCds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));

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
		return sql;
	}

}