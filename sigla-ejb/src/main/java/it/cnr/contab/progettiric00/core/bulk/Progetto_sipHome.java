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
 * Created on Oct 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Progetto_sipHome extends BulkHome {

	public Progetto_sipHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_sipBulk.class,conn,persistentCache);
	}
	protected Progetto_sipHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache)
	{
		super(class1, connection, persistentcache);
	}
	public SQLBuilder createSQLBuilder() {
		SQLBuilder sql = super.createSQLBuilder();
		sql.addClause("AND","tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_PREVISIONE);
		return sql;
	}
	public SQLBuilder createSQLBuilderAll() {
		return super.createSQLBuilder();
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (27/07/2004 11.23.36)
	 * @return ProgettoBulk
	 * @param bulk ProgettoBulk
	 */
	public ProgettoBulk getParent(Progetto_sipBulk bulk) throws PersistencyException, IntrospectionException{
    
		if (bulk == null)
			return null;
    	
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio_progetto_padre());
		sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,bulk.getPg_progetto_padre());
		sql.addSQLClause("AND","TIPO_FASE",SQLBuilder.EQUALS,bulk.getTipo_fase_progetto_padre());
		
		java.util.Collection coll = this.fetchAll(sql);
		if (coll.size() != 1)
			return null;
    
		return (ProgettoBulk)coll.iterator().next();
	}

	/**
	 * Recupera i figli dell'oggetto bulk
	 * Creation date: (27/07/2004 11.23.36)
	 * @return it.cnr.jada.persistency.sql.SQLBuilder
	 * @param ubi ProgettoBulk
	 */
    
	public SQLBuilder selectChildrenFor(it.cnr.jada.UserContext aUC, Progetto_sipBulk ubi){
		Progetto_sipHome progettohome = (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class,"V_PROGETTO_PADRE");
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(aUC));
		if (ubi == null){
			sql.addSQLClause("AND","ESERCIZIO_PROGETTO_PADRE",sql.ISNULL,null);
			sql.addSQLClause("AND","PG_PROGETTO_PADRE",sql.ISNULL,null);
			sql.addSQLClause("AND","TIPO_FASE_PROGETTO_PADRE",sql.ISNULL,null);
		}else{
			sql.addSQLClause("AND","ESERCIZIO_PROGETTO_PADRE",sql.EQUALS,ubi.getEsercizio());
			sql.addSQLClause("AND","PG_PROGETTO_PADRE",sql.EQUALS,ubi.getPg_progetto());
			sql.addSQLClause("AND","TIPO_FASE_PROGETTO_PADRE",sql.EQUALS,ubi.getTipo_fase());
			if (ubi.isCommessa())
				sql.addClause("AND", "fl_utilizzabile", sql.EQUALS, Boolean.TRUE);				
		}
		try{	
		  // Se uo 999.000 in scrivania: visualizza tutti i progetti
		  Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk)  getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		  if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			if (ubi == null)
			  sql.addSQLExistsClause("AND",abilitazioniProgetti(aUC));
			if (ubi != null && ubi.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO))
			  sql.addSQLExistsClause("AND",abilitazioniCommesse(aUC));
			else if (ubi != null && ubi.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO))
			  sql.addSQLExistsClause("AND",abilitazioniModuli(aUC)); 
		  }            				
		}catch (PersistencyException ex){}
		return sql;
	}    
	public SQLBuilder abilitazioniProgetti(it.cnr.jada.UserContext aUC) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		sql.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_PROGETTO","V_PROGETTO_PADRE.ESERCIZIO");
		sql.addSQLJoin("V_ABIL_PROGETTI.PG_PROGETTO","V_PROGETTO_PADRE.PG_PROGETTO");
		sql.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_PROGETTO","V_PROGETTO_PADRE.TIPO_FASE");
		return sql;    	
	}	
	public SQLBuilder abilitazioniCommesse(it.cnr.jada.UserContext aUC) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		sql.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_COMMESSA","V_PROGETTO_PADRE.ESERCIZIO");
		sql.addSQLJoin("V_ABIL_PROGETTI.PG_COMMESSA","V_PROGETTO_PADRE.PG_PROGETTO");
		sql.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_COMMESSA","V_PROGETTO_PADRE.TIPO_FASE");
		return sql;    	
	}
	public SQLBuilder abilitazioniModuli(it.cnr.jada.UserContext aUC) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		sql.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_MODULO","V_PROGETTO_PADRE.ESERCIZIO");
		sql.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO","V_PROGETTO_PADRE.PG_PROGETTO");
		sql.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_MODULO","V_PROGETTO_PADRE.TIPO_FASE");
		return sql;    	
	}
	public SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC,String campo,Integer livelloProgetto) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		if (ProgettoBulk.LIVELLO_PROGETTO_SECONDO.equals(livelloProgetto)) {
			sql.addSQLJoin("V_ABIL_PROGETTI.PG_COMMESSA", campo);
			sql.addSQLClause(FindClause.AND, "V_ABIL_PROGETTI.ESERCIZIO_COMMESSA", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(aUC));
		} else {
			sql.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO", campo);
			sql.addSQLClause(FindClause.AND, "V_ABIL_PROGETTI.ESERCIZIO_MODULO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(aUC));
		}
		return sql;    	
	}	    
	private SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC) throws PersistencyException{
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(aUC)));
		Progetto_sipHome progettohome = (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class,"V_ABIL_PROGETTI");    	
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
		sql.openParenthesis("AND");

		Parametri_enteBulk parEnte = ((Parametri_enteHome)getHomeCache().getHome(Parametri_enteBulk.class)).getParametriEnteAttiva();
		boolean abilProgettoUO = parEnte.isAbilProgettoUO();
		Optional<String> abilProgetti = ((Parametri_cdsHome) getHomeCache().getHome(Parametri_cdsBulk.class)).getAbilProgetti(aUC, CNRUserContext.getCd_cds(aUC));
		if (abilProgetti.isPresent()) {
			abilProgettoUO = abilProgetti.get().equalsIgnoreCase(V_struttura_organizzativaHome.LIVELLO_UO);
		}
		if (abilProgettoUO)
			sql.addSQLClause(FindClause.AND,"V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(aUC));
		else
			sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_PADRE",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(aUC));

		if (uo.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0){
			PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
			Parametri_cnrBulk parCNR = (Parametri_cnrBulk)parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(aUC)));
			if (!parCNR.getFl_nuovo_pdg()) {
				SQLBuilder sqlArea = getHomeCache().getHome(Ass_uo_areaBulk.class).createSQLBuilder();
				sqlArea.addTableToHeader("UNITA_ORGANIZZATIVA UO");
				sqlArea.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", "UO.CD_UNITA_PADRE");
				sqlArea.addSQLJoin("ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA", "UO.CD_UNITA_ORGANIZZATIVA");
				sqlArea.addSQLClause("AND","ASS_UO_AREA.CD_AREA_RICERCA",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(aUC));
				sqlArea.addSQLClause("AND","ASS_UO_AREA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(aUC));
				sql.addSQLExistsClause("OR",sqlArea);
			}
		}
		sql.closeParenthesis();
		return sql;    	
	}

	public DipartimentoBulk findDipartimento(UserContext userContext, Progetto_sipBulk bulk) throws it.cnr.jada.comp.ComponentException, PersistencyException {
		Progetto_sipHome prgHome = (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
		DipartimentoHome dipHome = (DipartimentoHome)getHomeCache().getHome(DipartimentoBulk.class);
		Progetto_sipBulk commessa = null, progetto = null;
		
		if (bulk.isModulo())
			commessa = (Progetto_sipBulk)prgHome.findByPrimaryKey(userContext, bulk.getProgettopadre());
		else if (bulk.isCommessa()) 
			commessa = (Progetto_sipBulk)prgHome.findByPrimaryKey(userContext, bulk);
			
		if (bulk.isProgetto()) 
			progetto = (Progetto_sipBulk)prgHome.findByPrimaryKey(userContext, bulk);
		else
			progetto = (Progetto_sipBulk)prgHome.findByPrimaryKey(userContext, commessa.getProgettopadre());

		return (DipartimentoBulk)dipHome.findByPrimaryKey(progetto.getDipartimento());
	}

	public Persistent findByPrimaryKey(UserContext userContext, Object persistent) throws PersistencyException {
		return findByPrimaryKey(userContext,(Persistent)persistent);
	}

	@Override
	public Persistent findByPrimaryKey(UserContext userContext,Persistent persistent) throws PersistencyException {
		Progetto_sipBulk progetto = ((Progetto_sipBulk)persistent);
		if (progetto.getEsercizio() == null && Optional.ofNullable(userContext).isPresent())
			progetto.setEsercizio(CNRUserContext.getEsercizio(userContext));
		if (progetto.getTipo_fase() == null)
			progetto.setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
		return findByPrimaryKey(persistent);
	}
}
