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

package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.contab.progettiric00.geco.bulk.Geco_area_progBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_attivitaBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessaBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessaIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessa_pbBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessa_rstlBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessa_sacBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_moduloBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_moduloIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_modulo_pbBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_modulo_rstlBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_modulo_sacBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progetto_operativoBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progetto_pbBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progetto_rstlBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progetto_sacBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.FetchException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class ProgettoHome extends BulkHome {
	public ProgettoHome(java.sql.Connection conn) {
		super(ProgettoBulk.class,conn);
	}
	public ProgettoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(ProgettoBulk.class,conn,persistentCache);
	}
	protected ProgettoHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache)
	{
		super(class1, connection, persistentcache);
	}

    @Override
    public SQLBuilder select(Persistent persistent) throws PersistencyException {
        return super.select(persistent);
    }

    /**
	 * Recupera tutti i dati nella tabella Progetto_uo relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Progetto_uoBulk</code>
	 */

	public java.util.Collection findDettagli(ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Progetto_uoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}

	/**
	 * Recupera tutti i dati nella tabella Progetto Finanziatore relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Progetto_finanziatoreBulk</code>
	 */
	
	public java.util.Collection findDettagliFinanziatori(ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Progetto_finanziatoreBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}

	public java.util.Collection findDettagliSpese(it.cnr.jada.UserContext userContext,ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Commessa_spesaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		sql.addClause("AND","esercizio",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return dettHome.fetchAll(sql);
	}

	/**
	 * Recupera tutti i dati nella tabella Progetto Partner Esterno relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Progetto_partner_esternoBulk</code>
	 */
	
	public java.util.Collection findDettagliPartner_esterni(ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Progetto_partner_esternoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}	

	/**
	 * Insert the method's description here.
	 * Creation date: (27/07/2004 11.23.36)
	 * @return ProgettoBulk
	 * @param bulk ProgettoBulk
	 */
	public ProgettoBulk getParent(ProgettoBulk bulk) throws PersistencyException, IntrospectionException{
    
		if (bulk == null)
			return null;
    	
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bulk.getEsercizio_progetto_padre());
		sql.addSQLClause("AND","PG_PROGETTO",sql.EQUALS,bulk.getPg_progetto_padre());
		sql.addSQLClause("AND","TIPO_FASE",sql.EQUALS,bulk.getTipo_fase_progetto_padre());
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
    
	public SQLBuilder selectChildrenForWorkpackage(it.cnr.jada.UserContext aUC, ProgettoBulk ubi){
		ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");    	    
		SQLBuilder sql = progettohome.createSQLBuilder();            
		if (ubi == null){
			sql.addSQLClause("AND","ESERCIZIO_PROGETTO_PADRE",sql.ISNULL,null);
			sql.addSQLClause("AND","PG_PROGETTO_PADRE",sql.ISNULL,null);
			sql.addSQLClause("AND","TIPO_FASE_PROGETTO_PADRE",sql.ISNULL,null);
			try{	
			   // Se uo 999.000 in scrivania: visualizza tutti i progetti
			   Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk)  getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			   if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				 sql.addSQLExistsClause("AND",abilitazioniProgetti(aUC));
			   }            				
			}catch (PersistencyException ex){}			
		}			
		else{
			sql.addSQLClause("AND","ESERCIZIO_PROGETTO_PADRE",sql.EQUALS,ubi.getEsercizio());
			sql.addSQLClause("AND","PG_PROGETTO_PADRE",sql.EQUALS,ubi.getPg_progetto());
			sql.addSQLClause("AND","TIPO_FASE_PROGETTO_PADRE",sql.EQUALS,ubi.getTipo_fase());
			try{	
			   // Se uo 999.000 in scrivania: visualizza tutti i progetti
			   Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk)  getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			   if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				  if (ubi != null && ubi.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO))
					sql.addSQLExistsClause("AND",abilitazioniCommesse(aUC));
				  else if (ubi != null && ubi.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO))
					sql.addSQLExistsClause("AND",abilitazioniModuli(aUC)); 
			   }            				
			}catch (PersistencyException ex){}
		}
		return sql;
	}    
	/**
	 * Recupera i figli dell'oggetto bulk
	 * Creation date: (27/07/2004 11.23.36)
	 * @return it.cnr.jada.persistency.sql.SQLBuilder
	 * @param ubi ProgettoBulk
	 */
    
	public SQLBuilder selectChildrenFor(it.cnr.jada.UserContext aUC, ProgettoBulk ubi){
		ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");
		SQLBuilder sql = progettohome.createSQLBuilder();   
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(aUC));
 	    sql.addSQLClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_NON_DEFINITA);
		if (ubi == null){
			sql.addSQLClause("AND","ESERCIZIO_PROGETTO_PADRE",sql.ISNULL,null);
			sql.addSQLClause("AND","PG_PROGETTO_PADRE",sql.ISNULL,null);
			sql.addSQLClause("AND","TIPO_FASE_PROGETTO_PADRE",sql.ISNULL,null);
		}else{
			sql.addSQLClause("AND","ESERCIZIO_PROGETTO_PADRE",sql.EQUALS,ubi.getEsercizio());
			sql.addSQLClause("AND","PG_PROGETTO_PADRE",sql.EQUALS,ubi.getPg_progetto());
			sql.addSQLClause("AND","TIPO_FASE_PROGETTO_PADRE",sql.EQUALS,ubi.getTipo_fase());
		}
		try{	
		  // Se uo 999.000 in scrivania: visualizza tutti i progetti
		  Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk)  getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		  if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			  if (((Parametri_enteHome)getHomeCache().getHome(Parametri_enteBulk.class)).isInformixAttivo()) {
				  if (ubi == null)
					  sql.addSQLExistsClause("AND",abilitazioniProgetti(aUC));
				  if (ubi != null && ubi.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO))
					  sql.addSQLExistsClause("AND",abilitazioniCommesse(aUC));
				  else if (ubi != null && ubi.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO))
					  sql.addSQLExistsClause("AND",abilitazioniModuli(aUC)); 
			  }
		  }            				
		}catch (PersistencyException ex){}
		return sql;
	}

	public SQLBuilder selectAllChildrenFor(it.cnr.jada.UserContext aUC, ProgettoBulk ubi){
		ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(aUC));
		if (ubi == null)
			sql.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
		else {
			sql.addSQLClause(FindClause.AND,"ESERCIZIO_PROGETTO_PADRE",SQLBuilder.EQUALS,ubi.getEsercizio());
			sql.addSQLClause(FindClause.AND,"PG_PROGETTO_PADRE",SQLBuilder.EQUALS,ubi.getPg_progetto());
			sql.addSQLClause(FindClause.AND,"TIPO_FASE_PROGETTO_PADRE",SQLBuilder.EQUALS,ubi.getTipo_fase());
		}
		return sql;
	}

	public java.util.List findWorkpackage_disponibili(it.cnr.jada.UserContext userContext,ProgettoBulk commessa) throws IntrospectionException, PersistencyException 
	{
		String uo = ((CNRUserContext) userContext).getCd_unita_organizzativa();
		Integer esercizio = ((CNRUserContext) userContext).getEsercizio();
		PersistentHome commessaHome = getHomeCache().getHome( WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = commessaHome.createSQLBuilder();
		sql.addTableToHeader( "CDR" );
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",sql.EQUALS,esercizio);
		sql.addSQLJoin( "CDR.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA" );
		sql.addSQLClause("AND","(PG_PROGETTO != ? OR PG_PROGETTO IS NULL)");
		sql.addParameter(commessa.getPg_progetto(),java.sql.Types.INTEGER,0);
		sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO_FINE",sql.GREATER_EQUALS,esercizio);
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO_INIZIO",sql.LESS_EQUALS,esercizio);
		sql.addOrderBy("CD_LINEA_ATTIVITA");
		return commessaHome.fetchAll(sql);
	}

	public java.util.List findWorkpackage_collegati(it.cnr.jada.UserContext userContext,ProgettoBulk commessa) throws IntrospectionException, PersistencyException 
	{
		String uo = ((CNRUserContext) userContext).getCd_unita_organizzativa();
		Integer esercizio = ((CNRUserContext) userContext).getEsercizio();
		PersistentHome commessaHome = getHomeCache().getHome( WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = commessaHome.createSQLBuilder();
		sql.addTableToHeader( "CDR" );
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",sql.EQUALS,esercizio);
		sql.addSQLJoin( "CDR.CD_CENTRO_RESPONSABILITA", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA" );
		sql.addSQLClause("AND","PG_PROGETTO = ?");
		sql.addParameter(commessa.getPg_progetto(),java.sql.Types.INTEGER,0);
		sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO_FINE",sql.GREATER_EQUALS,esercizio);
		sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO_INIZIO",sql.LESS_EQUALS,esercizio);
		sql.addOrderBy("CD_LINEA_ATTIVITA");
		return commessaHome.fetchAll(sql);
	}
	
	public Parametri_cdsBulk parametriCds(it.cnr.jada.UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException, PersistencyException {
		Parametri_cdsBulk param;
		param = (Parametri_cdsBulk) getHomeCache().getHome(Parametri_cdsBulk.class ).findByPrimaryKey( 
			new Parametri_cdsBulk(
				((CNRUserContext) aUC).getCd_cds(), 
				((CNRUserContext) aUC).getEsercizio()));
		if (param == null)
			throw new ApplicationException("Parametri CDS non trovati.");
		return param;
	}
	public SQLBuilder abilitazioniProgetti(it.cnr.jada.UserContext aUC) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		sql.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_PROGETTO","V_PROGETTO_PADRE.ESERCIZIO");
		sql.addSQLJoin("V_ABIL_PROGETTI.PG_PROGETTO","V_PROGETTO_PADRE.PG_PROGETTO");
		sql.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_PROGETTO","V_PROGETTO_PADRE.TIPO_FASE");
		return sql;    	
	}

	public SQLBuilder abilitazioniCommesse(it.cnr.jada.UserContext userContext) throws PersistencyException{
		return abilitazioniCommesse(
				CNRUserContext.getEsercizio(userContext),
				CNRUserContext.getCd_unita_organizzativa(userContext),
				CNRUserContext.getCd_cds(userContext)
		);
	}

	public SQLBuilder abilitazioniCommesse(Integer esercizio, String cdUnitaOrganizzativa, String cdCdS) throws PersistencyException{
		SQLBuilder sql = abilitazioni(esercizio, cdUnitaOrganizzativa, cdCdS);
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

	public SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC,String campo) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		sql.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO",campo);
		return sql;    	
	}

	private SQLBuilder abilitazioni(UserContext userContext) throws PersistencyException {
		return abilitazioni(
				CNRUserContext.getEsercizio(userContext),
				CNRUserContext.getCd_unita_organizzativa(userContext),
				CNRUserContext.getCd_cds(userContext)
		);
	}

	private SQLBuilder abilitazioni(Integer esercizio, String cdUnitaOrganizzativa, String cdCdS) throws PersistencyException {
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class)
				.findByPrimaryKey(new Unita_organizzativaBulk(cdUnitaOrganizzativa));
		ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_ABIL_PROGETTI");    	
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
		sql.openParenthesis("AND");

		Parametri_enteBulk parEnte = ((Parametri_enteHome)getHomeCache().getHome(Parametri_enteBulk.class)).getParametriEnteAttiva();
		boolean abilProgettoUO = parEnte.isAbilProgettoUO();
		Optional<String> abilProgetti = ((Parametri_cdsHome) getHomeCache().getHome(Parametri_cdsBulk.class)).getAbilProgetti(esercizio, cdCdS);
		if (abilProgetti.isPresent()) {
			abilProgettoUO = abilProgetti.get().equalsIgnoreCase(V_struttura_organizzativaHome.LIVELLO_UO);
		}
		if (abilProgettoUO)
			sql.addSQLClause(FindClause.AND,"V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,cdUnitaOrganizzativa);
		else
			sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_PADRE",SQLBuilder.EQUALS,cdCdS);

		if (uo.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0){
			PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
			Parametri_cnrBulk parCNR = (Parametri_cnrBulk)parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(esercizio));
			if (!parCNR.getFl_nuovo_pdg()) {
				SQLBuilder sqlArea = getHomeCache().getHome(Ass_uo_areaBulk.class).createSQLBuilder();
				sqlArea.addTableToHeader("UNITA_ORGANIZZATIVA UO");
				sqlArea.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", "UO.CD_UNITA_PADRE");
				sqlArea.addSQLJoin("ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA", "UO.CD_UNITA_ORGANIZZATIVA");
				sqlArea.addSQLClause("AND","ASS_UO_AREA.CD_AREA_RICERCA",SQLBuilder.EQUALS,cdCdS);
				sqlArea.addSQLClause("AND","ASS_UO_AREA.ESERCIZIO",SQLBuilder.EQUALS, esercizio);
				sql.addSQLExistsClause("OR",sqlArea);
			}
		}
		sql.closeParenthesis();
		return sql;    	
	}

	public void aggiornaGeco(UserContext userContext,ProgettoBulk progetto){
		if (CNRUserContext.getEsercizio(userContext) ==null )
			return;
		try {
			Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(userContext);

			if (parEnte.getFl_informix()) {
				Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
	
				if (parCnr == null) return;
				if (!parCnr.getFl_nuovo_pdg())
					aggiornaGecoOldPdg(userContext, progetto);
				else
					aggiornaGecoNewPdg(userContext, progetto);
			}				
		} catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}
	
	private void aggiornaGecoOldPdg(UserContext userContext,ProgettoBulk progetto){
		if (progetto != null && !progetto.getTipo_fase().equals(ProgettoBulk.TIPO_FASE_NON_DEFINITA)){
			try {
				progetto = (ProgettoBulk) super.findByPrimaryKey(progetto);
			} catch (PersistencyException e) {
				handleExceptionMail(userContext, e);
			}
			if (progetto != null && !progetto.getTipo_fase().equals(ProgettoBulk.TIPO_FASE_NON_DEFINITA)){
				if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO))
					aggiornaProgetti(userContext,progetto);
				else if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO))
					aggiornaCommesse(userContext,progetto);
				else if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_TERZO))
					aggiornaModuli(userContext,progetto);
			}
		}else{
			aggiornaProgetti(userContext,progetto);
			aggiornaCommesse(userContext,progetto);
			aggiornaModuli(userContext,progetto);
		}	
	}

	private void aggiornaGecoNewPdg(UserContext userContext,ProgettoBulk progetto){
		if (progetto != null && !progetto.getTipo_fase().equals(ProgettoBulk.TIPO_FASE_NON_DEFINITA)){
			try {
				progetto = (ProgettoBulk) super.findByPrimaryKey(progetto);
			} catch (PersistencyException e) {
				handleExceptionMail(userContext, e);
			}
			if (progetto != null && !progetto.getTipo_fase().equals(ProgettoBulk.TIPO_FASE_NON_DEFINITA)){
				if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO))
					aggiornaProgettiNuovoPdg(userContext,progetto);
				else if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO))
					aggiornaCommesseNuovoPdg(userContext,progetto);
				else if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_TERZO))
					aggiornaModuliNuovoPdg(userContext,progetto);
			}
		}else{
			aggiornaProgettiNuovoPdg(userContext,progetto);
			aggiornaCommesseNuovoPdg(userContext,progetto);
			aggiornaModuliNuovoPdg(userContext,progetto);
		}	
	}

	private void handleExceptionMail(UserContext userContext, Exception e){
	}
	
	public void aggiornaProgetti(UserContext userContext,ProgettoBulk progetto){
		try {
			verificaProgetti(userContext, progetto, Geco_progettoBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaProgetti(userContext, progetto,Geco_progetto_sacBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaProgetti(userContext,progetto, Geco_progetto_rstlBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaProgetti(userContext,progetto, Geco_progetto_pbBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}
	public void aggiornaCommesse(UserContext userContext,ProgettoBulk progetto){
		try {
			verificaCommesse(userContext,progetto,Geco_commessaBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaCommesse(userContext, progetto, Geco_commessa_sacBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaCommesse(userContext, progetto, Geco_commessa_rstlBulk.class);
		} catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaCommesse(userContext, progetto, Geco_commessa_pbBulk.class);
		} catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}	
	public void aggiornaModuli(UserContext userContext,ProgettoBulk progetto){
		try {
			verificaModuli(userContext,progetto,Geco_moduloBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaModuli(userContext, progetto, Geco_modulo_sacBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaModuli(userContext, progetto, Geco_modulo_rstlBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		try {
			verificaModuli(userContext, progetto, Geco_modulo_pbBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}	
	public void aggiornaProgettiNuovoPdg(UserContext userContext,ProgettoBulk progetto){
		try {
			verificaProgetti(userContext, progetto, Geco_area_progBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}
	public void aggiornaCommesseNuovoPdg(UserContext userContext,ProgettoBulk progetto){
		try {
			verificaCommesse(userContext,progetto,Geco_progetto_operativoBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}	
	public void aggiornaModuliNuovoPdg(UserContext userContext,ProgettoBulk progetto){
		try {
			verificaModuli(userContext, progetto, Geco_attivitaBulk.class);
		}catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}
	public void verificaProgetti(UserContext userContext, ProgettoBulk progetto, Class<? extends OggettoBulk> bulkClass) throws FetchException, PersistencyException, ComponentException, EJBException{
		List<Geco_progettoIBulk> progettiGeco = Utility.createProgettoGecoComponentSession().cercaProgettiGeco(userContext, progetto, bulkClass);
		for (Iterator<Geco_progettoIBulk> iterator = progettiGeco.iterator(); iterator.hasNext();) {
			Geco_progettoIBulk geco_progetto = iterator.next();
			Progetto_sipHome progetto_sip_home =  (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
			Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(new Progetto_sipBulk(new Integer(geco_progetto.getEsercizio().intValue()),new Integer(geco_progetto.getId_prog().intValue()),geco_progetto.getFase()));
			if (progetto_sip != null){
				geco_progetto.aggiornaProgettoSIP(progetto_sip);
				if (progetto_sip.isToBeUpdated()){
					progetto_sip.setUser(CNRUserContext.getUser(userContext));
					progetto_sip_home.update(progetto_sip, userContext);
				}
			}else{
				progetto_sip = new Progetto_sipBulk(new Integer(geco_progetto.getEsercizio().intValue()),new Integer(geco_progetto.getId_prog().intValue()),geco_progetto.getFase());
				progetto_sip.setCd_progetto(geco_progetto.getCod_prog());
				progetto_sip.setDs_progetto(geco_progetto.getDescr_prog());
				progetto_sip.setDipartimento((DipartimentoBulk)((DipartimentoHome)getHomeCache().getHome(DipartimentoBulk.class)).findByIdDipartimento(geco_progetto.getId_dip().intValue()));
				progetto_sip.setDt_inizio(geco_progetto.getData_istituzione_prog());
				progetto_sip.setImporto_progetto(Utility.ZERO);
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setDurata_progetto(ProgettoBulk.DURATA_PROGETTO_PLURIENNALE);
				progetto_sip.setFl_utilizzabile(Boolean.TRUE);
				progetto_sip.setLivello(ProgettoBulk.LIVELLO_PROGETTO_PRIMO);
				progetto_sip.setUser(CNRUserContext.getUser(userContext));
				geco_progetto.aggiornaProgettoSIP(progetto_sip);				
				progetto_sip.setToBeCreated();
				progetto_sip_home.insert(progetto_sip, userContext);
			}
		}
	}
	public void verificaCommesse(UserContext userContext, ProgettoBulk progetto, Class<? extends OggettoBulk> bulkClass) throws FetchException, PersistencyException, ComponentException, EJBException{
		List<Geco_commessaIBulk> commesseGeco = Utility.createProgettoGecoComponentSession().cercaCommesseGeco(userContext, progetto, bulkClass);
		for (Iterator<Geco_commessaIBulk> iterator = commesseGeco.iterator(); iterator.hasNext();) {
			Geco_commessaIBulk geco_commessa = iterator.next();
			Progetto_sipHome progetto_sip_home =  (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
			Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(new Progetto_sipBulk(new Integer(geco_commessa.getEsercizio().intValue()),new Integer(geco_commessa.getId_comm().intValue()),geco_commessa.getFase()));
			if (progetto_sip != null){
				progetto_sip.setProgettopadre((Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(new Progetto_sipBulk(new Integer(geco_commessa.getEsercizio().intValue()),new Integer(geco_commessa.getId_prog_padre().intValue()),geco_commessa.getFase())));
				geco_commessa.aggiornaProgettoSIP(progetto_sip);				
				if (progetto_sip.isToBeUpdated()){
					progetto_sip.setUser(CNRUserContext.getUser(userContext));
					progetto_sip_home.update(progetto_sip, userContext);
				}
			}else{
				progetto_sip = new Progetto_sipBulk(new Integer(geco_commessa.getEsercizio().intValue()),new Integer(geco_commessa.getId_comm().intValue()),geco_commessa.getFase());
				progetto_sip.setProgettopadre(new Progetto_sipBulk(geco_commessa.getEsercizio().intValue(),geco_commessa.getId_prog_padre().intValue(),geco_commessa.getFase()));
				progetto_sip.setCd_progetto(geco_commessa.getCod_comm());
				progetto_sip.setDs_progetto(geco_commessa.getDescr_comm());
				progetto_sip.setUnita_organizzativa((Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(geco_commessa.getCds()+"."+geco_commessa.getSede_svol_uo())));
				if (Optional.ofNullable(geco_commessa.getCod_3rzo_resp()).filter(s -> s.length() > 0).isPresent())
					progetto_sip.setResponsabile((TerzoBulk)getHomeCache().getHome(TerzoBulk.class).findByPrimaryKey(new TerzoBulk(new Integer(geco_commessa.getCod_3rzo_resp()))));				
				progetto_sip.setDt_inizio(geco_commessa.getData_inizio_attivita());
				if (geco_commessa.getEsito_negoz() != null)
					progetto_sip.setStato(geco_commessa.getEsito_negoz().equals(new Integer(2))?ProgettoBulk.TIPO_STATO_PROPOSTA:ProgettoBulk.TIPO_STATO_APPROVATO);
				else
					progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setImporto_progetto(Utility.ZERO);
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setDurata_progetto(ProgettoBulk.DURATA_PROGETTO_PLURIENNALE);
				progetto_sip.setLivello(ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
				progetto_sip.setFl_utilizzabile(Boolean.TRUE);
				progetto_sip.setUser(CNRUserContext.getUser(userContext));
				geco_commessa.aggiornaProgettoSIP(progetto_sip);
				progetto_sip.setToBeCreated();
				progetto_sip_home.insert(progetto_sip, userContext);
				/*
					Inserimento Nuove informazioni del progetto
				 */
				Progetto_other_fieldHome progetto_other_fieldHome =  (Progetto_other_fieldHome)getHomeCache().getHome(Progetto_other_fieldBulk.class);
                Progetto_other_fieldBulk progetto_other_fieldBulk = (Progetto_other_fieldBulk) progetto_other_fieldHome.findByPrimaryKey(new Progetto_other_fieldBulk(geco_commessa.getId_comm().intValue()));
				if (progetto_other_fieldBulk == null) {
                    progetto_other_fieldBulk = new Progetto_other_fieldBulk();
                    progetto_other_fieldBulk.setPg_progetto(geco_commessa.getId_comm().intValue());
                    progetto_other_fieldBulk.setStato(StatoProgetto.STATO_INIZIALE.value());
					progetto_other_fieldBulk.setFlControlliDisabled(Boolean.FALSE);
                    progetto_other_fieldBulk.setUser(CNRUserContext.getUser(userContext));
                    progetto_other_fieldBulk.setToBeCreated();
                    progetto_other_fieldHome.insert(progetto_other_fieldBulk, userContext);
                }

				progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(new Progetto_sipBulk(new Integer(geco_commessa.getEsercizio().intValue()),new Integer(geco_commessa.getId_comm().intValue()),geco_commessa.getFase()));
				progetto_sip.setOtherField(progetto_other_fieldBulk);
				progetto_sip.setToBeUpdated();
				progetto_sip_home.update(progetto_sip, userContext);

			}
		}
	}
	public void verificaModuli(UserContext userContext, ProgettoBulk progetto, Class<? extends OggettoBulk> bulkClass) throws FetchException, PersistencyException, ComponentException{
		List<Geco_moduloIBulk> moduliGeco = Utility.createProgettoGecoComponentSession().cercaModuliGeco(userContext, progetto, bulkClass);
		for (Iterator<Geco_moduloIBulk> iterator = moduliGeco.iterator(); iterator.hasNext();) {
			Geco_moduloIBulk geco_modulo = iterator.next();
			Progetto_sipHome progetto_sip_home =  (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
			Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(new Progetto_sipBulk(new Integer(geco_modulo.getEsercizio().intValue()),new Integer(geco_modulo.getId_mod().intValue()),geco_modulo.getFase()));
			if (progetto_sip != null){
				progetto_sip.setProgettopadre((Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(new Progetto_sipBulk(new Integer(geco_modulo.getEsercizio().intValue()),new Integer(geco_modulo.getId_comm().intValue()),geco_modulo.getFase())));
				geco_modulo.aggiornaProgettoSIP(progetto_sip);				
				if (progetto_sip.isToBeUpdated()){
					progetto_sip.setUser(CNRUserContext.getUser(userContext));
					progetto_sip_home.update(progetto_sip, userContext);
				}
			}else{
				progetto_sip = new Progetto_sipBulk(new Integer(geco_modulo.getEsercizio().intValue()),new Integer(geco_modulo.getId_mod().intValue()),geco_modulo.getFase());
				progetto_sip.setProgettopadre(new Progetto_sipBulk(geco_modulo.getEsercizio().intValue(),geco_modulo.getId_comm().intValue(),geco_modulo.getFase()));
				progetto_sip.setCd_progetto(geco_modulo.getCod_mod());
				progetto_sip.setDs_progetto(geco_modulo.getDescr_mod());
				/*if (geco_modulo.getCod_tip() != null)
					progetto_sip.setStato(geco_modulo.getCod_tip().equals(new Long(1))?"PS":geco_modulo.getCod_tip().equals(new Long(2))?"SC":null);
					*/
				progetto_sip.setUnita_organizzativa((Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(geco_modulo.getSede_princ_cdsuo())));
				if (Optional.ofNullable(geco_modulo.getCod_3rzo_gest()).filter(s -> s.length() > 0).isPresent())
					progetto_sip.setResponsabile((TerzoBulk)getHomeCache().getHome(TerzoBulk.class).findByPrimaryKey(new TerzoBulk(new Integer(geco_modulo.getCod_3rzo_gest()))));				
				progetto_sip.setDt_inizio(geco_modulo.getData_inizio_attivita());
				if (geco_modulo.getEsito_negoz() != null)
					progetto_sip.setStato(geco_modulo.getEsito_negoz().equals(new Integer(2))?ProgettoBulk.TIPO_STATO_PROPOSTA:ProgettoBulk.TIPO_STATO_APPROVATO);
				else
					progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setImporto_progetto(Utility.ZERO);
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setDurata_progetto(ProgettoBulk.DURATA_PROGETTO_PLURIENNALE);
				progetto_sip.setLivello(ProgettoBulk.LIVELLO_PROGETTO_TERZO);
				// stato att contab '0' terminato - '1' attivo 
				if ((geco_modulo.getStato_att_contab() != null && geco_modulo.getStato_att_contab().equals(new Long(0))))
					progetto_sip.setFl_utilizzabile(Boolean.FALSE);
				else
					progetto_sip.setFl_utilizzabile(Boolean.TRUE);
				progetto_sip.setUser(CNRUserContext.getUser(userContext));
				geco_modulo.aggiornaProgettoSIP(progetto_sip);				
				progetto_sip.setToBeCreated();
				progetto_sip_home.insert(progetto_sip, userContext);
			}
		}
	}
	public DipartimentoBulk findDipartimento(UserContext userContext, Progetto_sipBulk bulk) throws it.cnr.jada.comp.ComponentException, PersistencyException {
		ProgettoHome prgHome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class);
		DipartimentoHome dipHome = (DipartimentoHome)getHomeCache().getHome(DipartimentoBulk.class);
		ProgettoBulk commessa = null, progetto = null;
		
		if (bulk.isModulo())
			commessa = (ProgettoBulk)prgHome.findByPrimaryKey(userContext, bulk.getProgettopadre());
		else if (bulk.isCommessa()) 
			commessa = (ProgettoBulk)prgHome.findByPrimaryKey(userContext, bulk);
			
		if (bulk.isProgetto()) 
			progetto = (ProgettoBulk)prgHome.findByPrimaryKey(userContext, bulk);
		else
			progetto= (ProgettoBulk)prgHome.findByPrimaryKey(userContext, commessa.getProgettopadre());

		return (DipartimentoBulk)dipHome.findByPrimaryKey(progetto.getDipartimento());
	} 
	@Override
	public void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception) throws ObjectNotFoundException {
	}
	
	public java.util.Collection<Progetto_piano_economicoBulk> findDettagliPianoEconomico(it.cnr.jada.UserContext userContext,Integer pgProgetto) throws PersistencyException {
		Progetto_piano_economicoHome dettHome = (Progetto_piano_economicoHome)getHomeCache().getHome(Progetto_piano_economicoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		sql.addOrderBy("esercizio_piano, cd_voce_piano");
		return dettHome.fetchAll(sql);
	}
	
	public Progetto_other_fieldBulk findProgettoOtherField(it.cnr.jada.UserContext userContext,ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome otherHome = getHomeCache().getHome(Progetto_other_fieldBulk.class);
		return (Progetto_other_fieldBulk)otherHome.findByPrimaryKey(new Progetto_other_fieldBulk(testata.getPg_progetto()));
	}

	@Override
	public Persistent findByPrimaryKey(UserContext userContext,Persistent persistent) throws PersistencyException {
 		ProgettoBulk progetto = ((ProgettoBulk)persistent);
		if (progetto.getEsercizio() == null && Optional.ofNullable(userContext).isPresent())
			progetto.setEsercizio(CNRUserContext.getEsercizio(userContext));
		if (progetto.getTipo_fase() == null)
			progetto.setTipo_fase(ProgettoBulk.TIPO_FASE_NON_DEFINITA);
		return findByPrimaryKey(persistent);
	}

	public java.util.Collection findPdgModuliAssociati(it.cnr.jada.UserContext userContext,Integer pgProgetto) throws IntrospectionException, PersistencyException {
		PersistentHome pdgModuloHome = getHomeCache().getHome(Pdg_moduloBulk.class);
		SQLBuilder sql = pdgModuloHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return pdgModuloHome.fetchAll(sql);
	}

	/*
	 * Recupera le obbligazioni associate ad un progetto a partire da un anno specificato
	 */
	public java.util.Collection<ObbligazioneBulk> findObbligazioniAssociate(Integer pgProgetto, Integer annoFrom) throws IntrospectionException, PersistencyException {
		PersistentHome obblHome = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sqlObb = obblHome.createSQLBuilder();
		sqlObb.addSQLClause(FindClause.AND, "OBBLIGAZIONE.ESERCIZIO", SQLBuilder.GREATER_EQUALS, annoFrom);
			
		PersistentHome scadVoceHome = getHomeCache().getHome(Obbligazione_scad_voceBulk.class);
		SQLBuilder sqlExist = scadVoceHome.createSQLBuilder();
		sqlExist.addSQLJoin("OBBLIGAZIONE.CD_CDS", "OBBLIGAZIONE_SCAD_VOCE.CD_CDS");
		sqlExist.addSQLJoin("OBBLIGAZIONE.ESERCIZIO", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO");
		sqlExist.addSQLJoin("OBBLIGAZIONE.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE");
		sqlExist.addSQLJoin("OBBLIGAZIONE.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE");
		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "OBBLIGAZIONE_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA");
		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, pgProgetto);
			
		sqlObb.addSQLExistsClause(FindClause.AND, sqlExist);
		return obblHome.fetchAll(sqlObb);
	}

	/*
	 * Recupera le testate delle modifiche di obbligazioni cui risulta movimentata in positivo almeno una GAE
	 * associata ad un progetto a partire da un anno specificato
	 */
	public java.util.Collection<Obbligazione_modificaBulk> findModifichePositiveObbligazioniAssociate(Integer pgProgetto, Integer annoFrom) throws IntrospectionException, PersistencyException {
		PersistentHome obblModHome = getHomeCache().getHome(Obbligazione_modificaBulk.class);
		SQLBuilder sqlObblMod = obblModHome.createSQLBuilder();
		sqlObblMod.addSQLClause(FindClause.AND, "OBBLIGAZIONE_MODIFICA.ESERCIZIO", SQLBuilder.GREATER_EQUALS, annoFrom);

		PersistentHome modVoceHome = getHomeCache().getHome(Obbligazione_mod_voceBulk.class);
		SQLBuilder sqlExist = modVoceHome.createSQLBuilder();
		sqlExist.addSQLJoin("OBBLIGAZIONE_MOD_VOCE.CD_CDS","OBBLIGAZIONE_MODIFICA.CD_CDS");
		sqlExist.addSQLJoin("OBBLIGAZIONE_MOD_VOCE.ESERCIZIO","OBBLIGAZIONE_MODIFICA.ESERCIZIO");
		sqlExist.addSQLJoin("OBBLIGAZIONE_MOD_VOCE.PG_MODIFICA","OBBLIGAZIONE_MODIFICA.PG_MODIFICA");

		sqlExist.addSQLClause(FindClause.AND, "OBBLIGAZIONE_MOD_VOCE.IM_MODIFICA", SQLBuilder.GREATER, BigDecimal.ZERO);

		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "OBBLIGAZIONE_MOD_VOCE.ESERCIZIO");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "OBBLIGAZIONE_MOD_VOCE.CD_CENTRO_RESPONSABILITA");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "OBBLIGAZIONE_MOD_VOCE.CD_LINEA_ATTIVITA");
		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, pgProgetto);
			
		sqlObblMod.addSQLExistsClause(FindClause.AND, sqlExist);
		return obblModHome.fetchAll(sqlObblMod);
	}
	
	/*
	 * Recupera le variazioni di competenza associate ad un progetto a partire da un anno specificato
	 */
	public java.util.Collection<Pdg_variazioneBulk> findVariazioniCompetenzaAssociate(Integer pgProgetto, Integer annoFrom) throws IntrospectionException, PersistencyException {
		PersistentHome pdgVarHome = getHomeCache().getHome(Pdg_variazioneBulk.class);
		SQLBuilder sqlVar = pdgVarHome.createSQLBuilder();
		sqlVar.addSQLClause(FindClause.AND, "PDG_VARIAZIONE.ESERCIZIO", SQLBuilder.GREATER_EQUALS, annoFrom);
		
		PersistentHome pdgVarRigaHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sqlExist = pdgVarRigaHome.createSQLBuilder();
		sqlExist.addSQLJoin("PDG_VARIAZIONE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		sqlExist.addSQLJoin("PDG_VARIAZIONE.PG_VARIAZIONE_PDG", "PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG");
		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");
		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, pgProgetto);
		
		sqlVar.addSQLExistsClause(FindClause.AND, sqlExist);
		
		return pdgVarHome.fetchAll(sqlVar);
	}
	
	/*
	 * Recupera le variazioni di residuo associate ad un progetto a partire da un anno specificato
	 */
	public java.util.Collection<Var_stanz_resBulk> findVariazioniResiduoAssociate(Integer pgProgetto, Integer annoFrom) throws IntrospectionException, PersistencyException {
		PersistentHome varHome = getHomeCache().getHome(Var_stanz_resBulk.class);
		SQLBuilder sqlVar = varHome.createSQLBuilder();
		sqlVar.addSQLClause(FindClause.AND, "VAR_STANZ_RES.ESERCIZIO", SQLBuilder.GREATER_EQUALS, annoFrom);
		
		PersistentHome varRigaHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sqlExist = varRigaHome.createSQLBuilder();
		sqlExist.resetColumns();
		sqlExist.addColumn("1");
		sqlExist.addSQLJoin("VAR_STANZ_RES.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
		sqlExist.addSQLJoin("VAR_STANZ_RES.PG_VARIAZIONE", "VAR_STANZ_RES_RIGA.PG_VARIAZIONE");
		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VAR_STANZ_RES_RIGA.CD_CDR");
		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VAR_STANZ_RES_RIGA.CD_LINEA_ATTIVITA");
		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, pgProgetto);
		
		sqlVar.addSQLExistsClause(FindClause.AND, sqlExist);
		
		return varHome.fetchAll(sqlVar);
	}
	
    public ProgettoBulk initializePianoEconomico(UserContext userContext, ProgettoBulk progetto, boolean loadSaldi) throws PersistencyException {
    	ProgettoBulk myProgetto = (ProgettoBulk)findByPrimaryKey(userContext,progetto);
    	List<Progetto_piano_economicoBulk> progettoPiano = new BulkList<Progetto_piano_economicoBulk>(this.findDettagliPianoEconomico(userContext,myProgetto.getPg_progetto()));
		progettoPiano.stream().forEach(el->el.setProgetto(myProgetto));
		myProgetto.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>(progettoPiano.stream().filter(e->e.getEsercizio_piano().equals(Integer.valueOf(0))).collect(Collectors.toList())));
		myProgetto.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>(progettoPiano.stream().filter(e->e.getEsercizio_piano().equals(myProgetto.getEsercizio())).collect(Collectors.toList())));
		myProgetto.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>(progettoPiano.stream().filter(e->!e.getEsercizio_piano().equals(Integer.valueOf(0)) && !e.getEsercizio_piano().equals(myProgetto.getEsercizio())).collect(Collectors.toList())));

		if (loadSaldi)
			loadSaldiPianoEconomico(userContext, myProgetto);

		getHomeCache().fetchAll(userContext);
		return myProgetto;
	}

    public ProgettoBulk loadSaldiPianoEconomico(UserContext userContext, ProgettoBulk progetto) throws PersistencyException {
    	try {
    		List<V_saldi_voce_progettoBulk> vociMovimentate = ((V_saldi_voce_progettoHome)getHomeCache().getHome(V_saldi_voce_progettoBulk.class))
					.cercaSaldoVoce(progetto.getPg_progetto());

    		progetto.setVociBilancioMovimentate(
					new BulkList<V_saldi_voce_progettoBulk>(
							vociMovimentate.stream()
								.filter(el->el.getAssestato().compareTo(BigDecimal.ZERO)>0 ||
										el.getUtilizzatoAssestatoFinanziamento().compareTo(BigDecimal.ZERO)>0)
								.collect(Collectors.toList())));
				
			progetto.getDettagliPianoEconomicoAnnoCorrente().stream().forEach(ppe->{
				try {
					ppe.setVociBilancioAssociate(new BulkList<Ass_progetto_piaeco_voceBulk>(((Ass_progetto_piaeco_voceHome)getHomeCache().getHome(Ass_progetto_piaeco_voceBulk.class ))
						.findAssProgettoPiaecoVoceList(ppe.getPg_progetto(), ppe.getCd_unita_organizzativa(), ppe.getCd_voce_piano(), 
								ppe.getEsercizio_piano())));
				} catch (PersistencyException ex){
					throw new DetailedRuntimeException(ex.getMessage());
				}
				ppe.getVociBilancioAssociate().stream().forEach(voceAss->{
					V_saldi_voce_progettoBulk saldo = 
							progetto.getVociBilancioMovimentate().stream()
									.filter(el->el.getEsercizio_voce().equals(voceAss.getEsercizio_voce()))
									.filter(el->el.getTi_appartenenza().equals(voceAss.getTi_appartenenza()))
									.filter(el->el.getTi_gestione().equals(voceAss.getTi_gestione()))
									.filter(el->el.getCd_elemento_voce().equals(voceAss.getCd_elemento_voce()))
									.findFirst()
									.orElseGet(()->{
										V_saldi_voce_progettoBulk saldoNew = new V_saldi_voce_progettoBulk();
										saldoNew.setEsercizio_voce(voceAss.getEsercizio_voce());
										saldoNew.setTi_appartenenza(voceAss.getTi_appartenenza());
										saldoNew.setTi_gestione(voceAss.getTi_gestione());
										saldoNew.setCd_elemento_voce(voceAss.getCd_elemento_voce());

										saldoNew.setStanziamentoFin(BigDecimal.ZERO);
										saldoNew.setVariapiuFin(BigDecimal.ZERO);
										saldoNew.setVariamenoFin(BigDecimal.ZERO);
										saldoNew.setTrasfpiuFin(BigDecimal.ZERO);
										saldoNew.setTrasfmenoFin(BigDecimal.ZERO);

										saldoNew.setStanziamentoCofin(BigDecimal.ZERO);
										saldoNew.setVariapiuCofin(BigDecimal.ZERO);
										saldoNew.setVariamenoCofin(BigDecimal.ZERO);
										saldoNew.setTrasfpiuCofin(BigDecimal.ZERO);
										saldoNew.setTrasfmenoCofin(BigDecimal.ZERO);
										
										saldoNew.setImpaccFin(BigDecimal.ZERO);
										saldoNew.setImpaccCofin(BigDecimal.ZERO);
										saldoNew.setManrisFin(BigDecimal.ZERO);
										saldoNew.setManrisCofin(BigDecimal.ZERO);
										return saldoNew;
									});
					if (Elemento_voceHome.GESTIONE_SPESE.equals(voceAss.getTi_gestione()))
						voceAss.setSaldoSpesa(saldo);
					else
						voceAss.setSaldoEntrata(saldo);
					
				});
			});
			progetto.getDettagliPianoEconomicoAltriAnni().stream().forEach(ppe->{
				try {
					ppe.setVociBilancioAssociate(new BulkList<Ass_progetto_piaeco_voceBulk>(((Ass_progetto_piaeco_voceHome)getHomeCache().getHome(Ass_progetto_piaeco_voceBulk.class ))
						.findAssProgettoPiaecoVoceList(ppe.getPg_progetto(), ppe.getCd_unita_organizzativa(), ppe.getCd_voce_piano(), 
								ppe.getEsercizio_piano())));
				} catch (PersistencyException ex){
					throw new DetailedRuntimeException(ex.getMessage());
				}
				ppe.getVociBilancioAssociate().stream().forEach(voceAss->{
					V_saldi_voce_progettoBulk saldo =
							progetto.getVociBilancioMovimentate().stream()
									.filter(el->el.getEsercizio_voce().equals(voceAss.getEsercizio_voce()))
									.filter(el->el.getTi_appartenenza().equals(voceAss.getTi_appartenenza()))
									.filter(el->el.getTi_gestione().equals(voceAss.getTi_gestione()))
									.filter(el->el.getCd_elemento_voce().equals(voceAss.getCd_elemento_voce()))
									.findFirst()
									.orElseGet(()->{
										V_saldi_voce_progettoBulk saldoNew = new V_saldi_voce_progettoBulk();
										saldoNew.setEsercizio_voce(voceAss.getEsercizio_voce());
										saldoNew.setTi_appartenenza(voceAss.getTi_appartenenza());
										saldoNew.setTi_gestione(voceAss.getTi_gestione());
										saldoNew.setCd_elemento_voce(voceAss.getCd_elemento_voce());

										saldoNew.setStanziamentoFin(BigDecimal.ZERO);
										saldoNew.setVariapiuFin(BigDecimal.ZERO);
										saldoNew.setVariamenoFin(BigDecimal.ZERO);
										saldoNew.setTrasfpiuFin(BigDecimal.ZERO);
										saldoNew.setTrasfmenoFin(BigDecimal.ZERO);

										saldoNew.setStanziamentoCofin(BigDecimal.ZERO);
										saldoNew.setVariapiuCofin(BigDecimal.ZERO);
										saldoNew.setVariamenoCofin(BigDecimal.ZERO);
										saldoNew.setTrasfpiuCofin(BigDecimal.ZERO);
										saldoNew.setTrasfmenoCofin(BigDecimal.ZERO);

										saldoNew.setImpaccFin(BigDecimal.ZERO);
										saldoNew.setImpaccCofin(BigDecimal.ZERO);
										saldoNew.setManrisFin(BigDecimal.ZERO);
										saldoNew.setManrisCofin(BigDecimal.ZERO);
										return saldoNew;
									});
					if (Elemento_voceHome.GESTIONE_SPESE.equals(voceAss.getTi_gestione()))
						voceAss.setSaldoSpesa(saldo);
					else
						voceAss.setSaldoEntrata(saldo);

				});
			});
			return progetto;
		} catch(Exception e) {
			throw new PersistencyException( e );
		}
	}

    /**
     * Ritorna la SQLBuilder per la ricerca dei progetti su cui risulta abilitato ad operare la UO collegata 
     * nell'esercizio di scrivania (UO ed esercizio presenti sullo UserContext)
     * 
     * @param userContext lo UserContext
     * @return
     */
	public SQLBuilder selectProgettiAbilitati(it.cnr.jada.UserContext userContext) throws PersistencyException {
		ProgettoHome progettoHome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");    	    
		SQLBuilder sql = progettoHome.createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_GESTIONE);
		
		Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHomeCache().getHome(Parametri_cnrBulk.class);
		Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
		if (parCnrBulk.getFl_nuovo_pdg())
			sql.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
		else
			sql.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
		
		if (parCnrBulk.getFl_nuovo_pdg())
			sql.addSQLExistsClause(FindClause.AND,progettoHome.abilitazioniCommesse(userContext));
		else
			sql.addSQLExistsClause(FindClause.AND,progettoHome.abilitazioniModuli(userContext));
		return sql; 
	}

	/**
	 * Ritorna la SQLBuilder per la ricerca dei progetti senza filtro per UO di scrivania (UO ed esercizio presenti sullo UserContext)
	 * Utilizzata per Uo Ente
	 *
	 * @param userContext lo UserContext
	 * @return
	 */
	public SQLBuilder selectProgetti(it.cnr.jada.UserContext userContext) throws PersistencyException {
		ProgettoHome progettoHome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");
		SQLBuilder sql = progettoHome.createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_GESTIONE);

		Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHomeCache().getHome(Parametri_cnrBulk.class);
		Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));

		if (parCnrBulk.getFl_nuovo_pdg())
			sql.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
		else
			sql.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);

		ProgettoHome abilProgettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_ABIL_PROGETTI");
		SQLBuilder sqlExists = abilProgettohome.createSQLBuilder();
		sqlExists.addTableToHeader("UNITA_ORGANIZZATIVA");
		sqlExists.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
		if (parCnrBulk.getFl_nuovo_pdg()) {
			sqlExists.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_COMMESSA", "V_PROGETTO_PADRE.ESERCIZIO");
			sqlExists.addSQLJoin("V_ABIL_PROGETTI.PG_COMMESSA", "V_PROGETTO_PADRE.PG_PROGETTO");
			sqlExists.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_COMMESSA", "V_PROGETTO_PADRE.TIPO_FASE");
		} else {
			sqlExists.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_MODULO", "V_PROGETTO_PADRE.ESERCIZIO");
			sqlExists.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO", "V_PROGETTO_PADRE.PG_PROGETTO");
			sqlExists.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_MODULO", "V_PROGETTO_PADRE.TIPO_FASE");
		}

		sql.addSQLExistsClause(FindClause.AND,sqlExists);
		return sql;
	}

	public java.util.List<Progetto_rimodulazioneBulk> findRimodulazioni(Integer pgProgetto) throws PersistencyException {
		Progetto_rimodulazioneHome dettHome = (Progetto_rimodulazioneHome)getHomeCache().getHome(Progetto_rimodulazioneBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return dettHome.fetchAll(sql);
	}
	
	public java.util.List<ContrattoBulk> findContratti(Integer pgProgetto) throws PersistencyException {
		ContrattoHome dettHome = (ContrattoHome)getHomeCache().getHome(ContrattoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return dettHome.fetchAll(sql);
	}	
}