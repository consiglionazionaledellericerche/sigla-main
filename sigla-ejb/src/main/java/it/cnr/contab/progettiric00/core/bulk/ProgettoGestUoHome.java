package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class ProgettoGestUoHome extends BulkHome implements ConsultazioniRestHome {
	public ProgettoGestUoHome(java.sql.Connection conn) {
		super(ProgettoGestUoBulk.class,conn);
	}
	public ProgettoGestUoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(ProgettoGestUoBulk.class,conn,persistentCache);
	}
	protected ProgettoGestUoHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache)
	{
		super(class1, connection, persistentcache);
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		if ( !isUoEnte(userContext)){
			sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
		}
		return sql;
	}

	private Boolean isUoEnte(UserContext userContext) throws PersistencyException, ComponentException{
		Unita_organizzativa_enteBulk uoEnte = getUoEnte(userContext);
		if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
			return true;
		}
		return false;
	}

	private Unita_organizzativa_enteBulk getUoEnte(UserContext userContext)
			throws PersistencyException, ComponentException {
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class ).findAll().get(0);
		return uoEnte;
	}
}