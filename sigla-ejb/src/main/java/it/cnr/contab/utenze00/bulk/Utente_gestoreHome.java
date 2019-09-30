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
 * Date 29/05/2009
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;
import java.sql.SQLException;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteHome;
import it.cnr.contab.pdg00.consultazioni.bulk.Param_cons_costi_personaleBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.ValidationException;
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
	public SQLBuilder selectUtenteByClause(Utente_gestoreBulk utentegest,UtenteHome home,UtenteBulk bulk,CompoundFindClause clause) throws PersistencyException, SQLException, ValidationException,ComponentException {

		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(bulk.buildFindClauses(true));
	    sql.addClause("AND","ti_utente",SQLBuilder.EQUALS,bulk.UTENTE_AMMINISTRATORE_KEY);
//		sql.addClause("AND","fl_autenticazione_ldap",SQLBuilder.EQUALS,Boolean.FALSE);
		try {
			sql.addClause("AND","dt_fine_validita",SQLBuilder.GREATER_EQUALS,utentegest.getDataOdierna());
		} catch (BusinessProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.addClause(clause);
		return sql;
	}

	/* Seleziona gli utenti AMM */
	public SQLBuilder selectGestoreByClause(Utente_gestoreBulk utentegest,UtenteHome home,UtenteBulk bulk,CompoundFindClause clause) throws PersistencyException, SQLException, ValidationException,ComponentException {

		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(bulk.buildFindClauses(true));
	    sql.addClause("AND","ti_utente",SQLBuilder.EQUALS,bulk.UTENTE_AMMINISTRATORE_KEY);
		sql.addClause("AND","fl_autenticazione_ldap",SQLBuilder.EQUALS,Boolean.TRUE);
		try {
			sql.addClause("AND","dt_fine_validita",SQLBuilder.GREATER_EQUALS,utentegest.getDataOdierna());
		} catch (BusinessProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.addClause(clause);
		return sql;
	}
	

}