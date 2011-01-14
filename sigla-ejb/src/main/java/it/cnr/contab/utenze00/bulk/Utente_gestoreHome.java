/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/05/2009
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Utente_gestoreHome extends BulkHome {
	public Utente_gestoreHome(Connection conn) {
		super(Utente_gestoreBulk.class, conn);
	}
	public Utente_gestoreHome(Connection conn, PersistentCache persistentCache) {
		super(Utente_gestoreBulk.class, conn, persistentCache);
	}
	
	/* Seleziona gli utenti CDS */
	public SQLBuilder selectUtenteByClause(Utente_gestoreBulk utentegest,  UtenteHome home, UtenteBulk utente, CompoundFindClause clause) throws ComponentException, BusinessProcessException, PersistencyException {

//		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
//		UtenteHome home = (UtenteHome)getHome(userContext, utente);
		SQLBuilder sql = home.createSQLBuilder();

	    sql.addClause("AND","ti_utente",SQLBuilder.EQUALS,utente.UTENTE_AMMINISTRATORE_KEY);
		sql.addClause("AND","fl_autenticazione_ldap",SQLBuilder.EQUALS,Boolean.FALSE);
		sql.addClause("AND","dt_fine_validita",SQLBuilder.LESS_EQUALS,utentegest.getDataOdierna());
		return sql;
	}

	/* Seleziona gli utenti AMM */
	public SQLBuilder selectGestoreByClause(Utente_gestoreBulk utentegest, UtenteHome home, UtenteBulk utente, CompoundFindClause clause) throws ComponentException, BusinessProcessException, PersistencyException {

//		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
//		UtenteHome home = (UtenteHome)getHome(userContext, utente);
		SQLBuilder sql = home.createSQLBuilder();

	    sql.addClause("AND","ti_utente",SQLBuilder.EQUALS,utente.UTENTE_AMMINISTRATORE_KEY);
		sql.addClause("AND","fl_autenticazione_ldap",SQLBuilder.EQUALS,Boolean.TRUE);
		sql.addClause("AND","dt_fine_validita",SQLBuilder.GREATER_EQUALS,utentegest.getDataOdierna());
		return sql;
	}


}