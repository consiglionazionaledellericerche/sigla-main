package it.cnr.contab.progettiric00.core.bulk;

import java.util.ArrayList;
import java.util.List;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Progetto_rimodulazioneHome extends BulkHome {
	public Progetto_rimodulazioneHome(java.sql.Connection conn) {
		super(Progetto_rimodulazioneBulk.class,conn);
	}
	
	public Progetto_rimodulazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_rimodulazioneBulk.class,conn,persistentCache);
	}
	
	public java.util.Collection<Progetto_rimodulazione_ppeBulk> findDettagliRimodulazione(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Progetto_rimodulazione_ppeHome dettHome = (Progetto_rimodulazione_ppeHome)getHomeCache().getHome(Progetto_rimodulazione_ppeBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Progetto_rimodulazione_voceBulk> findDettagliVoceRimodulazione(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Progetto_rimodulazione_voceHome dettHome = (Progetto_rimodulazione_voceHome)getHomeCache().getHome(Progetto_rimodulazione_voceBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Progetto_rimodulazioneBulk> findRimodulazioni(it.cnr.jada.UserContext userContext,Integer pgProgetto) throws IntrospectionException, PersistencyException {
		Progetto_rimodulazioneHome dettHome = (Progetto_rimodulazioneHome)getHomeCache().getHome(Progetto_rimodulazioneBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return dettHome.fetchAll(sql);
	}	
}
