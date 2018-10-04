package it.cnr.contab.progettiric00.core.bulk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;

import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.Parametri_enteHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.Ass_uo_areaBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
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
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
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

	public SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC,String campo) throws PersistencyException{
		SQLBuilder sql = abilitazioni(aUC);    	
		sql.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO",campo);
		return sql;    	
	}	    

	private SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC) throws PersistencyException{
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(aUC)));
		ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_ABIL_PROGETTI");    	
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
		sql.openParenthesis("AND");		  
		
		Parametri_enteBulk parEnte = ((Parametri_enteHome)getHomeCache().getHome(Parametri_enteBulk.class)).getParametriEnteAttiva();
		if (parEnte.isAbilProgettoUO())
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

    public Persistent findByPrimaryKey(UserContext userContext,Object persistent) throws PersistencyException {
    	return findByPrimaryKey(userContext,(Persistent)persistent);
    }
	@Override
    public Persistent findByPrimaryKey(UserContext userContext,Persistent persistent) throws PersistencyException {
        ProgettoHome progettohome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class,"V_PROGETTO_PADRE");
        ProgettoBulk progetto = ((ProgettoBulk)persistent);
        if (progetto.getEsercizio() == null && Optional.ofNullable(userContext).isPresent())
            progetto.setEsercizio(CNRUserContext.getEsercizio(userContext));
        if (progetto.getTipo_fase() == null)
            progetto.setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
        return progettohome.findByPrimaryKey(persistent);
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
			Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(userContext, new Progetto_sipBulk(new Integer(geco_progetto.getEsercizio().intValue()),new Integer(geco_progetto.getId_prog().intValue()),geco_progetto.getFase()));
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
			Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(userContext, new Progetto_sipBulk(new Integer(geco_commessa.getEsercizio().intValue()),new Integer(geco_commessa.getId_comm().intValue()),geco_commessa.getFase()));
			if (progetto_sip != null){
				progetto_sip.setProgettopadre((Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(userContext, new Progetto_sipBulk(new Integer(geco_commessa.getEsercizio().intValue()),new Integer(geco_commessa.getId_prog_padre().intValue()),geco_commessa.getFase())));
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
				if (geco_commessa.getCod_3rzo_resp() != null)
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
				Progetto_other_fieldBulk progetto_other_fieldBulk = new Progetto_other_fieldBulk();
				progetto_other_fieldBulk.setPg_progetto(geco_commessa.getId_comm().intValue());
				progetto_other_fieldBulk.setStato(Progetto_other_fieldBulk.STATO_INIZIALE);
				progetto_other_fieldBulk.setToBeCreated();
				progetto_other_fieldHome.insert(progetto_other_fieldBulk, userContext);

			}
		}
	}
	public void verificaModuli(UserContext userContext, ProgettoBulk progetto, Class<? extends OggettoBulk> bulkClass) throws FetchException, PersistencyException, ComponentException{
		List<Geco_moduloIBulk> moduliGeco = Utility.createProgettoGecoComponentSession().cercaModuliGeco(userContext, progetto, bulkClass);
		for (Iterator<Geco_moduloIBulk> iterator = moduliGeco.iterator(); iterator.hasNext();) {
			Geco_moduloIBulk geco_modulo = iterator.next();
			Progetto_sipHome progetto_sip_home =  (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
			Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(userContext, new Progetto_sipBulk(new Integer(geco_modulo.getEsercizio().intValue()),new Integer(geco_modulo.getId_mod().intValue()),geco_modulo.getFase()));
			if (progetto_sip != null){
				progetto_sip.setProgettopadre((Progetto_sipBulk)progetto_sip_home.findByPrimaryKey(userContext, new Progetto_sipBulk(new Integer(geco_modulo.getEsercizio().intValue()),new Integer(geco_modulo.getId_comm().intValue()),geco_modulo.getFase())));
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
				if (geco_modulo.getCod_3rzo_gest() != null)
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
	
	public java.util.Collection findDettagliPianoEconomico(it.cnr.jada.UserContext userContext,ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		Progetto_piano_economicoHome dettHome = (Progetto_piano_economicoHome)getHomeCache().getHome(Progetto_piano_economicoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,testata.getPg_progetto());
		sql.addOrderBy("esercizio_piano, cd_voce_piano");
		List<Progetto_piano_economicoBulk> pianoList = dettHome.fetchAll(sql);
		List<Progetto_piano_economicoBulk> pianoListNew = new ArrayList<>();
		for (Progetto_piano_economicoBulk progetto_piano_economicoBulk : pianoList)
			pianoListNew.add((Progetto_piano_economicoBulk)dettHome.completeBulkRowByRow(userContext, progetto_piano_economicoBulk));
		return pianoListNew;
	}
	
	public Progetto_other_fieldBulk findProgettoOtherField(it.cnr.jada.UserContext userContext,ProgettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome otherHome = getHomeCache().getHome(Progetto_other_fieldBulk.class);
		return (Progetto_other_fieldBulk)otherHome.findByPrimaryKey(new Progetto_other_fieldBulk(testata.getPg_progetto()));
	}
}