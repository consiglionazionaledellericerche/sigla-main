package it.cnr.contab.progettiric00.core.bulk;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import it.cnr.contab.anagraf00.core.bulk.Anagrafico_esercizioBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.blob.bulk.*;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.progettiric00.geco.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ProgettoGestHome extends BulkHome implements ConsultazioniRestHome {
	public ProgettoGestHome(java.sql.Connection conn) {
		super(ProgettoGestBulk.class,conn);
	}
	public ProgettoGestHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(ProgettoGestBulk.class,conn,persistentCache);
	}
	protected ProgettoGestHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache)
	{
		super(class1, connection, persistentcache);
	}
	/**
	 * Recupera tutti i dati nella tabella Progetto_uo relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Progetto_uoBulk</code>
	 */

	public java.util.Collection findDettagli(ProgettoGestBulk testata) throws IntrospectionException, PersistencyException {
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
	
	public java.util.Collection findDettagliFinanziatori(ProgettoGestBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Progetto_finanziatoreBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}

	public java.util.Collection findDettagliSpese(it.cnr.jada.UserContext userContext,ProgettoGestBulk testata) throws IntrospectionException, PersistencyException {
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
	
	public java.util.Collection findDettagliPartner_esterni(ProgettoGestBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Progetto_partner_esternoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i dati nella tabella Postit relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>PostItBulk</code>
	 */
	
	public java.util.Collection findDettagliPostIt(ProgettoGestBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(PostItBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","pg_progetto",sql.EQUALS,testata.getPg_progetto());
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Insert the method's description here.
	 * Creation date: (27/07/2004 11.23.36)
	 * @return ProgettoGestBulk
	 * @param bulk ProgettoGestBulk
	 */
	public ProgettoGestBulk getParent(ProgettoGestBulk bulk) throws PersistencyException, IntrospectionException{
    
		if (bulk == null)
			return null;
    	
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bulk.getEsercizio_progetto_padre());
		sql.addSQLClause("AND","PG_PROGETTO",sql.EQUALS,bulk.getPg_progetto_padre());
		sql.addSQLClause("AND","TIPO_FASE",sql.EQUALS,bulk.getTipo_fase_progetto_padre());
		java.util.Collection coll = this.fetchAll(sql);
		if (coll.size() != 1)
			return null;
    
		return (ProgettoGestBulk)coll.iterator().next();
	}
	/**
	 * Recupera i figli dell'oggetto bulk
	 * Creation date: (27/07/2004 11.23.36)
	 * @return it.cnr.jada.persistency.sql.SQLBuilder
	 * @param bulk ProgettoGestBulk
	 */
    
	public SQLBuilder selectChildrenForWorkpackage(it.cnr.jada.UserContext aUC, ProgettoGestBulk ubi){
		ProgettoGestHome progettohome = (ProgettoGestHome)getHomeCache().getHome(ProgettoGestBulk.class,"V_PROGETTO_PADRE");    	    
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
				  if (ubi != null && ubi.getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_PRIMO))
					sql.addSQLExistsClause("AND",abilitazioniCommesse(aUC));
				  else if (ubi != null && ubi.getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_SECONDO))
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
	 * @param bulk ProgettoGestBulk
	 */
    
	public SQLBuilder selectChildrenFor(it.cnr.jada.UserContext aUC, ProgettoGestBulk ubi){
		ProgettoGestHome progettohome = (ProgettoGestHome)getHomeCache().getHome(ProgettoGestBulk.class,"V_PROGETTO_PADRE");
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
		}
		try{	
		  // Se uo 999.000 in scrivania: visualizza tutti i progetti
		  Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk)  getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		  if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			if (ubi == null)
			  sql.addSQLExistsClause("AND",abilitazioniProgetti(aUC));
			if (ubi != null && ubi.getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_PRIMO))
			  sql.addSQLExistsClause("AND",abilitazioniCommesse(aUC));
			else if (ubi != null && ubi.getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_SECONDO))
			  sql.addSQLExistsClause("AND",abilitazioniModuli(aUC)); 
		  }            				
		}catch (PersistencyException ex){}
		return sql;
	}    

	public java.util.List findWorkpackage_disponibili(it.cnr.jada.UserContext userContext,ProgettoGestBulk commessa) throws IntrospectionException, PersistencyException 
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

	public java.util.List findWorkpackage_collegati(it.cnr.jada.UserContext userContext,ProgettoGestBulk commessa) throws IntrospectionException, PersistencyException 
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
	
	public Parametri_cdsBulk parametriCds(it.cnr.jada.UserContext aUC, ProgettoGestBulk bulk) throws it.cnr.jada.comp.ComponentException, PersistencyException {
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
		ProgettoGestHome progettohome = (ProgettoGestHome)getHomeCache().getHome(ProgettoGestBulk.class,"V_ABIL_PROGETTI");    	
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
		sql.openParenthesis("AND");		  
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_PADRE",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(aUC));
		if (uo.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0){
			SQLBuilder sqlArea = getHomeCache().getHome(Ass_uo_areaBulk.class).createSQLBuilder();
			sqlArea.addTableToHeader("UNITA_ORGANIZZATIVA UO");
			sqlArea.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", "UO.CD_UNITA_PADRE");
			sqlArea.addSQLJoin("ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA", "UO.CD_UNITA_ORGANIZZATIVA");
			sqlArea.addSQLClause("AND","ASS_UO_AREA.CD_AREA_RICERCA",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(aUC));
			sqlArea.addSQLClause("AND","ASS_UO_AREA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(aUC));
			sql.addSQLExistsClause("OR",sqlArea);
		}
		sql.closeParenthesis();
		return sql;    	
	}
    public Persistent findByPrimaryKey(UserContext userContext,Object persistent) throws PersistencyException {
    	return findByPrimaryKey(userContext,(Persistent)persistent);
    }
	@Override
    public Persistent findByPrimaryKey(UserContext userContext,Persistent persistent) throws PersistencyException {
    	ProgettoGestBulk progetto = ((ProgettoGestBulk)persistent);
    	if (progetto.getEsercizio() == null)
    		progetto.setEsercizio(CNRUserContext.getEsercizio(userContext));
    	if (progetto.getTipo_fase() == null)        	    	
    		progetto.setTipo_fase(ProgettoGestBulk.TIPO_FASE_NON_DEFINITA);
    	return super.findByPrimaryKey(persistent);
    }
	
	private void handleExceptionMail(UserContext userContext, Exception e){
	}
	
	public DipartimentoBulk findDipartimento(UserContext userContext, Progetto_sipBulk bulk) throws it.cnr.jada.comp.ComponentException, PersistencyException {
		ProgettoGestHome prgHome = (ProgettoGestHome)getHomeCache().getHome(ProgettoGestBulk.class);
		DipartimentoHome dipHome = (DipartimentoHome)getHomeCache().getHome(DipartimentoBulk.class);
		ProgettoGestBulk commessa = null, progetto = null;
		
		if (bulk.isModulo())
			commessa = (ProgettoGestBulk)prgHome.findByPrimaryKey(userContext, bulk.getProgettopadre());
		else if (bulk.isCommessa()) 
			commessa = (ProgettoGestBulk)prgHome.findByPrimaryKey(userContext, bulk);
			
		if (bulk.isProgetto()) 
			progetto = (ProgettoGestBulk)prgHome.findByPrimaryKey(userContext, bulk);
		else
			progetto= (ProgettoGestBulk)prgHome.findByPrimaryKey(userContext, commessa.getProgettopadre());

		return (DipartimentoBulk)dipHome.findByPrimaryKey(progetto.getDipartimento());
	} 
	@Override
	public void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception) throws ObjectNotFoundException {
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (compoundfindclause != null && compoundfindclause.getClauses() != null){
			Boolean trovataCondizioneUo = false;
			CompoundFindClause newClauses = new CompoundFindClause();
			Enumeration e = compoundfindclause.getClauses();
			SQLBuilder sqlExists = null;
			while(e.hasMoreElements() ){
				FindClause findClause = (FindClause) e.nextElement();
				if (findClause instanceof SimpleFindClause){
					SimpleFindClause clause = (SimpleFindClause)findClause;
					int operator = clause.getOperator();
					if (clause.getPropertyName() != null && clause.getPropertyName().equals("cd_unita_organizzativa") &&
							operator == SQLBuilder.EQUALS){
						trovataCondizioneUo = true;
						ProgettoHome progettoHome = (ProgettoHome) getHomeCache().getHome(ProgettoBulk.class);
						sqlExists = progettoHome.createSQLBuilder();
						sqlExists.addTableToHeader("V_ABIL_PROGETTI");
						sqlExists.addSQLJoin("PROGETTO.ESERCIZIO", "PROGETTO_GEST.ESERCIZIO");
						sqlExists.addSQLJoin("PROGETTO.TIPO_FASE", "PROGETTO_GEST.TIPO_FASE");
						sqlExists.addSQLJoin("PROGETTO.PG_PROGETTO", "PROGETTO_GEST.PG_PROGETTO");
						sqlExists.addSQLJoin("PROGETTO_GEST.ESERCIZIO", "V_ABIL_PROGETTI.ESERCIZIO_COMMESSA");
						sqlExists.addSQLJoin("PROGETTO_GEST.TIPO_FASE", "V_ABIL_PROGETTI.TIPO_FASE_COMMESSA");
						sqlExists.addSQLJoin("PROGETTO_GEST.PG_PROGETTO", "V_ABIL_PROGETTI.PG_COMMESSA");
						sqlExists.addSQLClause("AND","V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, clause.getValue());
					} else {
						newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
					}
				}
			}
			if (trovataCondizioneUo){
				sql = selectByClause(userContext, newClauses);
				sql.addSQLExistsClause("AND", sqlExists);
			}
		}

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