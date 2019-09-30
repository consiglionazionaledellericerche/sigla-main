
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

package it.cnr.contab.pdg00.consultazioni.bulk;
import java.sql.SQLException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_dipendenteHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;


public class Param_cons_costi_personaleHome extends BulkHome {
	public Param_cons_costi_personaleHome(java.sql.Connection conn) {
		super(Param_cons_costi_personaleBulk.class, conn);
	}
	public Param_cons_costi_personaleHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Param_cons_costi_personaleBulk.class, conn, persistentCache);
	}
	
	
	public SQLBuilder selectV_dipendenteByClause( Param_cons_costi_personaleBulk parametri, V_dipendenteHome home, V_dipendenteBulk bulk, CompoundFindClause clause) throws PersistencyException, SQLException, ValidationException {
				
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(clause);
		sql.addClause("AND", "esercizio", sql.EQUALS, parametri.getEsercizio());
		sql.addClause("AND", "mese", sql.EQUALS, parametri.getMese());
//		sql.addClause("AND", "cd_unita_organizzativa",sql.EQUALS, parametri.getCd_uo());
		sql.addClause("AND", "id_matricola", sql.EQUALS , parametri.getId_matricola());
		sql.addClause("AND", "nominativo", sql.EQUALS , parametri.getNominativo());
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome( Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!parametri.getCd_uo().equals( ente.getCd_unita_organizzativa())){
			sql.addClause("AND","cd_unita_organizzativa",sql.EQUALS,parametri.getCd_uo());	
		}
		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
	
	

	public SQLBuilder selectV_moduloByClause(Param_cons_costi_personaleBulk parametri,ProgettoHome home,ProgettoBulk bulk,CompoundFindClause clause) throws ComponentException, PersistencyException, ValidationException {
		
		SQLBuilder sql = getHomeCache().getHome(ProgettoBulk.class, "V_PROGETTO_PADRE").createSQLBuilder();
		sql.addClause(clause);
		sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
		sql.addClause("AND", "tipo_fase", sql.EQUALS, ProgettoBulk.TIPO_FASE_PREVISIONE);
		if(parametri.getCd_commessa()!=null && parametri.getV_commessa().getPg_progetto()!= null)
			sql.addClause("AND", "pg_progetto_padre", sql.EQUALS, parametri.getV_commessa().getPg_progetto());
		sql.addClause("AND", "esercizio", sql.EQUALS, parametri.getEsercizio());
		sql.addClause("AND", "cd_unita_organizzativa",sql.EQUALS, parametri.getCd_uo());
		sql.addClause("AND", "cd_modulo", sql.EQUALS , parametri.getCd_modulo());
		
		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}

	public SQLBuilder selectV_commessaByClause( Param_cons_costi_personaleBulk parametri,ProgettoHome home,ProgettoBulk bulk,CompoundFindClause clause) throws ComponentException, PersistencyException, ValidationException {
		
		SQLBuilder sql = getHomeCache().getHome(ProgettoBulk.class, "V_PROGETTO_PADRE").createSQLBuilder();
						
		sql.addClause(clause);
		sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
		sql.addClause("AND", "tipo_fase", sql.EQUALS, ProgettoBulk.TIPO_FASE_PREVISIONE);
		sql.addClause("AND", "esercizio", sql.EQUALS, parametri.getEsercizio());
		sql.addClause("AND", "cd_unita_organizzativa",sql.EQUALS, parametri.getCd_uo());
		sql.addClause("AND", "cd_commessa", sql.EQUALS , parametri.getCd_commessa());
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome( Unita_organizzativa_enteBulk.class).findAll().get(0);
		/*if (!parametri.getCd_uo().equals( ente.getCd_unita_organizzativa())){
			
			ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");
			sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(CNRUserContext.));
		}	*/
		if (clause != null) 
			sql.addClause(clause);
		return sql;
		
	}
	 
	
	public SQLBuilder selectV_uoByClause(Param_cons_costi_personaleBulk parametri,Unita_organizzativaHome home, Unita_organizzativaBulk bulk,CompoundFindClause clause) throws ComponentException, PersistencyException, ValidationException {
		
		SQLBuilder sql = home.createSQLBuilder();
		
		sql.addClause(clause);
		sql.addClause("AND", "esercizio", sql.EQUALS, parametri.getEsercizio());
		sql.addClause("AND", "mese", sql.EQUALS, parametri.getMese());
		sql.addClause("AND", "cd_unita_padre", sql.EQUALS, parametri.getCds());		
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS , parametri.getCd_uo());
		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
}