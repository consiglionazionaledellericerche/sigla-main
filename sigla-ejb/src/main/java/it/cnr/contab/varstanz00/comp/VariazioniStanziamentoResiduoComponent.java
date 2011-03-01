/*
 * Created on Feb 16, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.comp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseHome;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaDetBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdrKey;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.prevent00.bulk.V_assestato_residuoBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_linea_resBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_linea_resHome;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.DefaultSQLExceptionHandler;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class VariazioniStanziamentoResiduoComponent extends CRUDComponent implements Cloneable, Serializable{

	/**
	 * 
	 */
	public VariazioniStanziamentoResiduoComponent() {
		super();
	}
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		try {
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
			var_stanz_res.setEsercizio(CNRUserContext.getEsercizio(usercontext));
			var_stanz_res.setCentroDiSpesa((CdsBulk)getHome(usercontext, CdsBulk.class).findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(usercontext))));
			var_stanz_res.setCentroDiResponsabilita(Utility.createCdrComponentSession().cdrFromUserContext(usercontext));
			var_stanz_res.setStato(Var_stanz_resBulk.STATO_PROPOSTA_PROVVISORIA);
			
			
			java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
			GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
	       	tsOdiernoGregorian.setTime(tsOdierno);
	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
	        	        
	        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > CNRUserContext.getEsercizio(usercontext).intValue())
				try {
					var_stanz_res.setDt_apertura(new java.sql.Timestamp(sdf.parse("31/12/"+var_stanz_res.getEsercizio().intValue()).getTime()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else {
				var_stanz_res.setDt_apertura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(usercontext)));
				
	        }
	        
	        
//			var_stanz_res.setDt_apertura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(usercontext)));
			inizializzaSommeAZero(var_stanz_res);
			return var_stanz_res;
		} catch (DetailedRuntimeException e) {
			throw new ComponentException(e);
		}catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}		
	}
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		try {
			CdrBulk cdr = ((Var_stanz_resBulk)oggettobulk).getCdr();
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)super.inizializzaBulkPerModifica(usercontext,oggettobulk);
			var_stanz_res.setCdr(cdr);
			var_stanz_res.setCdsAbilitatoAdApprovare(isCdsAbilitatoAdApprovare(usercontext,var_stanz_res.getCentroDiResponsabilita().getCd_cds(),var_stanz_res));	
			var_stanz_res.setEnteAbilitatoAdApprovare(isEnteAbilitatoAdApprovare(usercontext,var_stanz_res));
			Var_stanz_resHome testataHome = (Var_stanz_resHome)getHome(usercontext, Var_stanz_resBulk.class);
			var_stanz_res.setAssociazioneCDR(new it.cnr.jada.bulk.BulkList(testataHome.findAssociazioneCDR(var_stanz_res)));
			var_stanz_res.setRigaVariazione(new it.cnr.jada.bulk.BulkList(testataHome.findVariazioniRiga(var_stanz_res)));
            inizializzaSommeCDR(usercontext,var_stanz_res);
			var_stanz_res.setTotale_righe_variazione(Utility.ZERO);
			for (Iterator righeVar=var_stanz_res.getRigaVariazione().iterator();righeVar.hasNext();){
				Var_stanz_res_rigaBulk varRiga = (Var_stanz_res_rigaBulk)righeVar.next();
				var_stanz_res.setTotale_righe_variazione(Utility.nvl(var_stanz_res.getTotale_righe_variazione()).add(Utility.nvl(varRiga.getIm_variazione())));
				varRiga.setDisponibilita_stanz_res(calcolaDisponibilita_stanz_res(usercontext,varRiga));
			}					
			if (var_stanz_res.getStato().equalsIgnoreCase(Var_stanz_resBulk.STATO_APPROVATA)){
				var_stanz_res.setVar_bilancio(((Var_bilancioHome)getHome(usercontext, Var_bilancioBulk.class)).findByVar_stanz_res(var_stanz_res));
			}
			getHomeCache(usercontext).fetchAll(usercontext,testataHome);
			return var_stanz_res;
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	public BigDecimal calcolaDisponibilita_stanz_res(UserContext usercontext,Var_stanz_res_rigaBulk varRiga) throws ComponentException{
		BigDecimal totale = Utility.ZERO;
		try {
			Voce_f_saldi_cdr_lineaBulk saldi = new Voce_f_saldi_cdr_lineaBulk(varRiga.getEsercizio(), 
			                                                                  varRiga.getEsercizio_res(), 
			                                                                  varRiga.getCd_cdr(), 
			                                                                  varRiga.getCd_linea_attivita(), 
			                                                                  varRiga.getElemento_voce().getTi_appartenenza(), 
			                                                                  varRiga.getElemento_voce().getTi_gestione(), 
			                                                                  varRiga.getCd_voce());
			saldi = (Voce_f_saldi_cdr_lineaBulk) getHome(usercontext, Voce_f_saldi_cdr_lineaBulk.class).findByPrimaryKey(saldi);
			if (saldi != null)
			  totale = saldi.getDispAdImpResiduoImproprio();
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return totale; 
	}
	/**
	  * Viene richiesta l'eliminazione dell'oggetto selezionato
	  *
	  * Pre-post-conditions:
	  *
	  * @param	userContext	lo UserContext che ha generato la richiesta
	  * @param	bulk l'OggettoBulk da eliminare
	  * @return	void
	  *
	**/
	public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{
		try {		
			if (bulk instanceof ICancellatoLogicamente){
				((ICancellatoLogicamente)bulk).cancellaLogicamente();
				updateBulk(userContext, bulk);
			}else{
				super.eliminaConBulk(userContext, bulk);				
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	/**
	 * 
	 * @param userContext
	 * @param var_stanz_res
	 * @param cds
	 * @param clause
	 * @return
	 * @throws ComponentException
	 * @throws PersistencyException
	 */
	public SQLBuilder selectCentro_di_responsabilitaByClause (UserContext userContext, Ass_var_stanz_res_cdrBulk ass_cdr, CdrBulk cdr, CompoundFindClause clause)	throws ComponentException, PersistencyException{
		SQLBuilder sql = getHome(userContext, CdrBulk.class,"V_CDR_VALIDO").createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		if (ass_cdr.getVar_stanz_res().getTipologia() == null)
			throw new ApplicationException("Selezionare la tipologia della Variazione.");
		if(ass_cdr.getVar_stanz_res().getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_STO_INT) ||
		   ass_cdr.getVar_stanz_res().getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO)){
		   	 sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		   	 sql.addSQLJoin("V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
			 sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_PADRE",SQLBuilder.EQUALS,ass_cdr.getVar_stanz_res().getCentroDiSpesa().getCd_unita_organizzativa());
		   }
		if(clause != null)
		  sql.addClause(clause);
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return sql; 
	}	
	
	public SQLBuilder selectCentroDiSpesaByClause (UserContext userContext,Var_stanz_resBulk var_stanz_res, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = ((CdsHome)getHome(userContext, cds.getClass(), "V_CDS_VALIDO")).createSQLBuilderIncludeEnte();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;
	}
	public SQLBuilder selectLinea_di_attivitaByClause (UserContext userContext,Var_stanz_res_rigaBulk var_stanz_res_riga, WorkpackageBulk linea_di_attivita, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
		sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, var_stanz_res_riga.getCentroTestata().getCd_centro_responsabilita());
		sql.addTableToHeader("NATURA");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
		if (var_stanz_res_riga.getVar_stanz_res().getTipologia_fin() != null){
			sql.openParenthesis("AND");
			sql.addSQLClause("OR","NATURA.TIPO",SQLBuilder.EQUALS,var_stanz_res_riga.getVar_stanz_res().getTipologia_fin());			
			it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
			try {
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_CDR_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_CDR_PERSONALE);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
			if (config != null){
				sql.addSQLClause( "OR", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, config.getVal01());
			}
			sql.closeParenthesis();
		}
		return sql;
	}
	public SQLBuilder selectElemento_voceByClause (UserContext userContext,Var_stanz_res_rigaBulk var_stanz_res_riga, Elemento_voceBulk elemento_voce, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, Elemento_voceBulk.class).createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
		sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.NOT_EQUALS, "C");
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_CDR_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_CDR_PERSONALE);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		if (config != null && var_stanz_res_riga.getCentroTestata()!=null && var_stanz_res_riga.getCentroTestata().getCd_centro_responsabilita()!=null){
			if( var_stanz_res_riga.getCentroTestata().getCd_centro_responsabilita().compareTo(config.getVal01())!=0)
				sql.addSQLClause("AND", "FL_VOCE_PERSONALE", SQLBuilder.EQUALS, "N");
		}
		return sql;
	}
	public SQLBuilder selectAssestatoResiduoByClause (UserContext userContext,Var_stanz_resBulk var_stanz_res, V_assestato_residuoBulk assestato_residuo, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, V_assestato_residuoBulk.class).createSQLBuilder();
		sql.addClause( clause );
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.NOT_EQUALS, "C");
		sql.addClause("AND", "esercizio_res", SQLBuilder.EQUALS, var_stanz_res.getEsercizio_res().getEsercizio());
		if (var_stanz_res.getTipologia_fin() != null){
			sql.addTableToHeader("NATURA");
			sql.addTableToHeader("LINEA_ATTIVITA");
			sql.addSQLJoin("V_ASSESTATO_RESIDUO.CD_LINEA_ATTIVITA","LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
			sql.addSQLJoin("V_ASSESTATO_RESIDUO.CD_CENTRO_RESPONSABILITA","LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLJoin("LINEA_ATTIVITA.CD_NATURA","NATURA.CD_NATURA");
			sql.addSQLClause("AND","NATURA.TIPO",SQLBuilder.EQUALS,var_stanz_res.getTipologia_fin());			
		}		
		if (var_stanz_res.getCdr() != null){
			sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, var_stanz_res.getCdr().getCd_centro_responsabilita());		
		}else{
			if (var_stanz_res.getCentroDiResponsabilita().getLivello().intValue() > 1){
				sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, var_stanz_res.getCentroDiResponsabilita().getCd_centro_responsabilita());
			}else{
				if (!var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO))
				  sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, var_stanz_res.getCentroDiResponsabilita().getCd_centro_responsabilita());
				else{
					SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
					sqlStruttura.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
					sqlStruttura.addSQLJoin("V_ASSESTATO_RESIDUO.CD_CENTRO_RESPONSABILITA","V_STRUTTURA_ORGANIZZATIVA.CD_CENTRO_RESPONSABILITA");
					sqlStruttura.addSQLClause("AND", "CD_CDR_AFFERENZA", SQLBuilder.EQUALS, var_stanz_res.getCentroDiResponsabilita().getCd_centro_responsabilita());
	                sql.addSQLExistsClause("AND",sqlStruttura);
				}
                
			}
		}
		return sql;
	}
	
	/**
	 * 
	 * @param userContext
	 * @param ogettoBulk
	 * @return
	 * @throws ComponentException
	 */
	public it.cnr.jada.bulk.OggettoBulk salvaDefinitivo(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		verificaTotaliDaAccertamentoModifica(userContext, var_stanz_res);
		var_stanz_res.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
		
		java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
		GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
       	tsOdiernoGregorian.setTime(tsOdierno);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
        	        
        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > CNRUserContext.getEsercizio(userContext).intValue())
			try {
				var_stanz_res.setDt_chiusura(new java.sql.Timestamp(sdf.parse("31/12/"+var_stanz_res.getEsercizio().intValue()).getTime()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else {
        	var_stanz_res.setDt_chiusura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
        }
				
//		var_stanz_res.setDt_chiusura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		var_stanz_res.setToBeUpdated();
		if (var_stanz_res.getAssociazioneCDR().isEmpty()) 
			throw new ApplicationException("Associare almeno un Centro di Responsabilitï¿½ alla Variazione.");
		var_stanz_res = (Var_stanz_resBulk)super.modificaConBulk(userContext, var_stanz_res);
		if ((var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_STO)||
		    var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_STO_INT)) &&
		    Utility.nvl(var_stanz_res.getTotale_da_ripartire()).compareTo(Utility.ZERO)!= 0){
				throw new ApplicationException("Il Totale da ripartire per uno storno deve essere zero.");
		    }
		try{
			for (java.util.Iterator j=var_stanz_res.getAssociazioneCDR().iterator();j.hasNext();){			
				Ass_var_stanz_res_cdrBulk ass_cdr = (Ass_var_stanz_res_cdrBulk)j.next();
				Ass_var_stanz_res_cdrHome ass_cdrHome = (Ass_var_stanz_res_cdrHome)getHome(userContext,Ass_var_stanz_res_cdrBulk.class);
	
				if (ass_cdrHome.findDettagliSpesa(ass_cdr).isEmpty()) { 
						throw new ApplicationException("Associare almeno un dettaglio di variazione al Centro di Responsabilitï¿½ " + ass_cdr.getCd_centro_responsabilita());
				}
				if (ass_cdr.getSpesa_diff().compareTo(Utility.ZERO) != 0)
					throw new ApplicationException("La Differenza di spesa ("+new it.cnr.contab.util.EuroFormat().format(ass_cdr.getSpesa_diff())+")"+
												   "\n" + "per il Cdr "+ ass_cdr.getCd_centro_responsabilita()+ " ï¿½ diversa da zero. ");
			}
			aggiornaLimiteSpesa(userContext, var_stanz_res);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return var_stanz_res;
	}
	/**
	 * 
	 * @param userContext
	 * @param ogettoBulk
	 * @return
	 * @throws ComponentException
	 */
	public it.cnr.jada.bulk.OggettoBulk approva(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		var_stanz_res.setStato(Pdg_variazioneBulk.STATO_APPROVATA);
		
		java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
		GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
       	tsOdiernoGregorian.setTime(tsOdierno);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
        	        
        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > CNRUserContext.getEsercizio(userContext).intValue())
			try {
				var_stanz_res.setDt_approvazione(new java.sql.Timestamp(sdf.parse("31/12/"+var_stanz_res.getEsercizio().intValue()).getTime()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else {
        	var_stanz_res.setDt_approvazione(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
//        	var_stanz_res.setDt_approvazione(EJBCommonServices.getServerDate());
        }
				
//		var_stanz_res.setDt_approvazione(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		var_stanz_res.setToBeUpdated();
		var_stanz_res = (Var_stanz_resBulk)super.modificaConBulk(userContext, var_stanz_res);
		Var_stanz_resHome testataHome = (Var_stanz_resHome)getHome(userContext, Var_stanz_resBulk.class);		
		try {
			UtenteHome utenteHome = (UtenteHome)getHome(userContext,UtenteBulk.class);
				for (java.util.Iterator j=var_stanz_res.getAssociazioneCDR().iterator();j.hasNext();){			
					Ass_var_stanz_res_cdrBulk ass_var = (Ass_var_stanz_res_cdrBulk)j.next();		
					if (inviaMessaggio(userContext,var_stanz_res,ass_var)){
					for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(ass_var.getCd_centro_responsabilita()).iterator();i.hasNext();){
						UtenteBulk utente = (UtenteBulk)i.next();
						MessaggioBulk messaggio = generaMessaggio(userContext,utente,var_stanz_res,Var_stanz_resBulk.STATO_APPROVATA);
						super.creaConBulk(userContext, messaggio);
					}
				}
			}
			for (Iterator righe = testataHome.findAllVariazioniRiga(var_stanz_res).iterator();righe.hasNext();){
				Var_stanz_res_rigaBulk varRiga = (Var_stanz_res_rigaBulk)righe.next();
				Voce_f_saldi_cdr_lineaBulk saldo = new Voce_f_saldi_cdr_lineaBulk(varRiga.getEsercizio(), 
				                                                                  varRiga.getEsercizio_res(), 
																				  varRiga.getCd_cdr(), 
																				  varRiga.getCd_linea_attivita(), 
																				  varRiga.getElemento_voce().getTi_appartenenza(), 
																				  varRiga.getElemento_voce().getTi_gestione(), 
																				  varRiga.getCd_voce());
				Voce_f_saldi_cdr_lineaBulk saldi = (Voce_f_saldi_cdr_lineaBulk) getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class).findByPrimaryKey(saldo);
				if (saldi == null){
					saldo.setToBeCreated();
					saldo.inizializzaSommeAZero();
					saldo.setCd_elemento_voce(varRiga.getElemento_voce().getCd_elemento_voce());
					saldi = (Voce_f_saldi_cdr_lineaBulk)super.creaConBulk(userContext,saldo);
				}
				if (varRiga.getIm_variazione().compareTo(Utility.ZERO) < 0)
				  saldi.setVar_meno_stanz_res_imp(saldi.getVar_meno_stanz_res_imp().add(varRiga.getIm_variazione().abs()));
				else if (varRiga.getIm_variazione().compareTo(Utility.ZERO) > 0)
				  saldi.setVar_piu_stanz_res_imp(saldi.getVar_piu_stanz_res_imp().add(varRiga.getIm_variazione().abs()));
				if (saldi.getDispAdImpResiduoImproprio().compareTo(Utility.ZERO) < 0){
					throw new ApplicationException("Impossibile effettuare l'operazione !\n"+
												   "Nell'esercizio residuo "+ varRiga.getEsercizio_res()+
												   " e per il CdR "+varRiga.getCd_cdr()+", "+
												   " Voce "+varRiga.getCd_voce()+
												   " e GAE "+varRiga.getCd_linea_attivita()+" lo stanziamento Residuo Improprio "+
												   " diventerebbe negativo ("+new it.cnr.contab.util.EuroFormat().format(saldi.getDispAdImpResiduoImproprio().abs())+")");					
				}
				saldi.setToBeUpdated();
				Utility.createSaldoComponentSession().aggiornaSaldiAnniSuccessivi(userContext,
						                                                          saldi.getCd_centro_responsabilita(),
						                                                          saldi.getCd_linea_attivita(),
						                                                          saldi.getVoce(),
						                                                          saldi.getEsercizio_res(),
						                                                          varRiga.getIm_variazione().negate(),
						                                                          saldi);
				super.modificaConBulk(userContext,saldi);
			}
			generaVariazioneBilancio(userContext, var_stanz_res);
			if (var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_STO)||
				var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO)){
				String soggetto = "E' stata approvata la Variazione allo stanziamento residuo n° "+var_stanz_res.getPg_variazione();
				generaEMAIL(userContext, var_stanz_res,soggetto,soggetto +" del "+var_stanz_res.getEsercizio()+"<BR>",null, "APP");			    	
			}						
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} 
		return var_stanz_res;
	}
	/**
	 * 
	 * @param userContext
	 * @param ogettoBulk
	 * @return
	 * @throws ComponentException
	 */
	public it.cnr.jada.bulk.OggettoBulk controllaApprova(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		Var_stanz_resHome testataHome = (Var_stanz_resHome)getHome(userContext, Var_stanz_resBulk.class);		
		try {
			for (Iterator righe = testataHome.findAllVariazioniRiga(var_stanz_res).iterator();righe.hasNext();){
				Var_stanz_res_rigaBulk varRiga = (Var_stanz_res_rigaBulk)righe.next();
				Voce_f_saldi_cdr_lineaBulk saldo = new Voce_f_saldi_cdr_lineaBulk(varRiga.getEsercizio(), 
				                                                                  varRiga.getEsercizio_res(), 
																				  varRiga.getCd_cdr(), 
																				  varRiga.getCd_linea_attivita(), 
																				  varRiga.getElemento_voce().getTi_appartenenza(), 
																				  varRiga.getElemento_voce().getTi_gestione(), 
																				  varRiga.getCd_voce());
				Voce_f_saldi_cdr_lineaBulk saldi = (Voce_f_saldi_cdr_lineaBulk) getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class).findByPrimaryKey(saldo);
				if (saldi == null){
					saldo.inizializzaSommeAZero();
					saldo.setCd_elemento_voce(varRiga.getElemento_voce().getCd_elemento_voce());
				}
				if (varRiga.getIm_variazione().compareTo(Utility.ZERO) < 0)
				  saldi.setVar_meno_stanz_res_imp(saldi.getVar_meno_stanz_res_imp().add(varRiga.getIm_variazione().abs()));
				else if (varRiga.getIm_variazione().compareTo(Utility.ZERO) > 0)
				  saldi.setVar_piu_stanz_res_imp(saldi.getVar_piu_stanz_res_imp().add(varRiga.getIm_variazione().abs()));
				if (saldi.getDispAdImpResiduoImproprio().compareTo(Utility.ZERO) < 0){
					throw new ApplicationException("Impossibile effettuare l'operazione !\n"+
												   "Nell'esercizio residuo "+ varRiga.getEsercizio_res()+
												   " e per il CdR "+varRiga.getCd_cdr()+", "+
												   " Voce "+varRiga.getCd_voce()+
												   " e GAE "+varRiga.getCd_linea_attivita()+" lo stanziamento Residuo Improprio "+
												   " diventerebbe negativo ("+new it.cnr.contab.util.EuroFormat().format(saldi.getDispAdImpResiduoImproprio().abs())+")");					
				}
			}
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return var_stanz_res;
	}
	private void generaEMAIL(UserContext userContext, Var_stanz_resBulk var_stanz_res, String soggetto, String preText, String postText,String tipo) throws ComponentException, IntrospectionException, PersistencyException{
		String formDate = "dd/MM/yyyy";
		SimpleDateFormat formatterDate = new SimpleDateFormat(formDate,Config.getHandler().getLocale());
		String text = new String();
		Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome)getHome(userContext,Utente_indirizzi_mailBulk.class);
		if (preText != null)
			text += preText + "<BR>";
		text = text +"CdR proponente: "+var_stanz_res.getCentroDiResponsabilita().getCd_ds_cdr()+"<BR>";
		text = text +"Esercizio residuo: "+var_stanz_res.getEsercizio_residuo()+"<BR>";
		text = text +"Tipologia: "+var_stanz_res.getTi_tipologiaKeys().get(var_stanz_res.getTipologia())+"<BR>";
		if (var_stanz_res.getTipologia_fin() != null)
		  text = text + var_stanz_res.getTi_tipologia_finKeys().get(var_stanz_res.getTipologia_fin())+"<BR>";
		text = text +"Data di approvazione: "+formatterDate.format(var_stanz_res.getDt_approvazione())+"<BR>";		
		text = text +"<BR>";
		text = text +"CdR abilitati a concorrervi:<BR>";
		String addressTO = null;
		for (java.util.Iterator j=var_stanz_res.getAssociazioneCDR().iterator();j.hasNext();){			
			Ass_var_stanz_res_cdrBulk ass_var = (Ass_var_stanz_res_cdrBulk)j.next();
			text = text + "CdR:"+ass_var.getCentro_di_responsabilita().getCd_ds_cdr()+ " quota assegnata " +new it.cnr.contab.util.EuroFormat().format(ass_var.getIm_spesa())+"<BR>";		
		}
		if (tipo.equalsIgnoreCase("ERR")){
			for (java.util.Iterator i= utente_indirizzi_mailHome.findUtenteMancataApprovazioneVariazioniBilancioEnte().iterator();i.hasNext();){
				Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk)i.next();
				if (addressTO == null)
				  addressTO = new String();
				else
				  addressTO = addressTO + ",";    
				addressTO = addressTO+utente_indirizzi.getIndirizzo_mail();			
			}
		}else if (tipo.equalsIgnoreCase("APP")){
			for (java.util.Iterator i= utente_indirizzi_mailHome.findUtenteApprovaVariazioniBilancio(var_stanz_res).iterator();i.hasNext();){
				Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk)i.next();
				if (addressTO == null)
				  addressTO = new String();
				else
				  addressTO = addressTO + ",";    
				addressTO = addressTO+utente_indirizzi.getIndirizzo_mail();			
			}
		}
		if (postText != null)
			text += "<BR>" + postText+ "<BR>";
		if (addressTO != null){
			try {
				SendMail.sendMail(soggetto,text,InternetAddress.parse(addressTO));
			} catch (AddressException e) {
			}
		}
	}
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		try{
			SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);

			UtenteBulk utente = (UtenteBulk) getHome( userContext, UtenteBulk.class).findByPrimaryKey(new UtenteKey(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
	
			CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class);
			CdrBulk cdrUtente = (CdrBulk)cdrHome.findByPrimaryKey(new CdrKey(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext))); 

			if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				sql.openParenthesis("AND");
					sql.addSQLClause("OR", "VAR_STANZ_RES.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrUtente.getCd_centro_responsabilita());
					for (java.util.Iterator j=cdrHome.findCdrAfferenti(cdrUtente).iterator();j.hasNext();){			
						CdrBulk cdrAfferenti = (CdrBulk)j.next();
						sql.addSQLClause("OR", "VAR_STANZ_RES.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrAfferenti.getCd_centro_responsabilita());
					}

					SQLBuilder sqlAssUo = getHome(userContext,Ass_var_stanz_res_cdrBulk.class).createSQLBuilder();
					sqlAssUo.addSQLJoin("VAR_STANZ_RES.ESERCIZIO","ASS_VAR_STANZ_RES_CDR.ESERCIZIO");
					sqlAssUo.addSQLJoin("VAR_STANZ_RES.PG_VARIAZIONE","ASS_VAR_STANZ_RES_CDR.PG_VARIAZIONE");

					sqlAssUo.openParenthesis("AND");
					sqlAssUo.addSQLClause("OR", "ASS_VAR_STANZ_RES_CDR.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrUtente.getCd_centro_responsabilita());
					for (java.util.Iterator j=cdrHome.findCdrAfferenti(cdrUtente).iterator();j.hasNext();){			
						CdrBulk cdrAfferenti = (CdrBulk)j.next();
						sqlAssUo.addSQLClause("OR", "ASS_VAR_STANZ_RES_CDR.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrAfferenti.getCd_centro_responsabilita());
					}
					sqlAssUo.closeParenthesis();
					sql.addSQLExistsClause("OR",sqlAssUo);
				sql.closeParenthesis();
			}		  		 
			return sql;
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
	}	
	/**
	 * 
	 * @param userContext
	 * @param ogettoBulk
	 * @return
	 * @throws ComponentException
	 */
	public it.cnr.jada.bulk.OggettoBulk respingi(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		var_stanz_res.setStato(Pdg_variazioneBulk.STATO_RESPINTA);
		
		java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
		GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
       	tsOdiernoGregorian.setTime(tsOdierno);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
        	        
        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > CNRUserContext.getEsercizio(userContext).intValue())
			try {
				var_stanz_res.setDt_chiusura(new java.sql.Timestamp(sdf.parse("31/12/"+var_stanz_res.getEsercizio().intValue()).getTime()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else {
        	var_stanz_res.setDt_chiusura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
        }
        
//		var_stanz_res.setDt_chiusura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		var_stanz_res.setToBeUpdated();
		var_stanz_res = (Var_stanz_resBulk)super.modificaConBulk(userContext, var_stanz_res);
		aggiornaLimiteSpesa(userContext, var_stanz_res);
		return var_stanz_res;
	}
	public it.cnr.jada.bulk.OggettoBulk statoPrecedente(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		var_stanz_res.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_PROVVISORIA);
		var_stanz_res.setDt_chiusura(null);
		var_stanz_res.setToBeUpdated();
		var_stanz_res = (Var_stanz_resBulk)super.modificaConBulk(userContext, var_stanz_res);
		aggiornaLimiteSpesa(userContext, var_stanz_res);
		return var_stanz_res;
	}
	private void aggiornaLimiteSpesa(UserContext userContext,Var_stanz_resBulk pdg) throws ComponentException {

		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
				"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB053.aggiornaLimiteSpesaVar(?,?,?,?)}",false,this.getClass());
			cs.setObject( 1, pdg.getEsercizio() );
			cs.setObject( 2, pdg.getPg_variazione());
			cs.setObject( 3,"R"); //competenza 
			cs.setObject( 4, userContext.getUser());
			try {
				lockBulk(userContext,pdg);
				cs.executeQuery();
			} catch (Throwable e) {
				throw handleException(pdg,e);
			} finally {
				cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}
	private void inizializzaSommeAZero(Var_stanz_resBulk var_stanz_res){
		for (java.util.Iterator j=var_stanz_res.getAssociazioneCDR().iterator();j.hasNext();){			
			Ass_var_stanz_res_cdrBulk ass_var_stanz = (Ass_var_stanz_res_cdrBulk)j.next();
			ass_var_stanz.setSpesa_ripartita(Utility.ZERO);
		}			
	}
	private void inizializzaSommeCDR(UserContext usercontext,Var_stanz_resBulk var_stanz_res) throws ComponentException, IntrospectionException, PersistencyException{
		BigDecimal totale_da_ripartire = Utility.ZERO;
		Ass_var_stanz_res_cdrHome AssHome = (Ass_var_stanz_res_cdrHome)getHome(usercontext, Ass_var_stanz_res_cdrBulk.class);
		for (Iterator ass_var_cdrList=var_stanz_res.getAssociazioneCDR().iterator();ass_var_cdrList.hasNext();){
			Ass_var_stanz_res_cdrBulk ass_var_cdr = (Ass_var_stanz_res_cdrBulk)ass_var_cdrList.next();
			var_stanz_res.setTotale_da_ripartire(Utility.nvl(var_stanz_res.getTotale_da_ripartire()).add(ass_var_cdr.getIm_spesa()));
			ass_var_cdr.setSpesa_ripartita(Utility.ZERO);
			for (Iterator dettagliSpesa= AssHome.findDettagliSpesa(ass_var_cdr).iterator();dettagliSpesa.hasNext();){
				Var_stanz_res_rigaBulk riga = (Var_stanz_res_rigaBulk)dettagliSpesa.next();	
				ass_var_cdr.setSpesa_ripartita(Utility.nvl(ass_var_cdr.getSpesa_ripartita()).add(Utility.nvl(riga.getIm_variazione())));
			}
		}					
	}
	public List findEsercizi_res(UserContext userContext,Var_stanz_resBulk var_stanz_res) throws it.cnr.jada.comp.ComponentException {
		List lista;
		try {
				Esercizio_baseHome home = (Esercizio_baseHome)getHome(userContext,Esercizio_baseBulk.class);
				Voce_f_saldi_cdr_linea_resHome cdr_linea_home = (Voce_f_saldi_cdr_linea_resHome)getHome(userContext,Voce_f_saldi_cdr_linea_resBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sql_cdr_linea = cdr_linea_home.createSQLBuilder();
				sql_cdr_linea.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
				sql_cdr_linea.addSQLJoin("ESERCIZIO_BASE.ESERCIZIO","VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES");
				sql_cdr_linea.addSQLClause("AND","IM_STANZ_RES_IMPROPRIO + VAR_PIU_STANZ_RES_IMP - VAR_MENO_STANZ_RES_IMP",SQLBuilder.NOT_EQUALS,Utility.ZERO);
			    sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.LESS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
			    sql.addSQLExistsClause("AND",sql_cdr_linea);
			    sql.addOrderBy("ESERCIZIO DESC");
				Broker broker = home.createBroker(sql);
				lista = home.fetchAll(broker);
				broker.close();
		}catch(Exception e) {
			throw handleException(e);
		}
		return lista;	
	}	
	public Voce_fBulk getVoce_FdaEV(UserContext userContext,Integer aEsercizio,String aTI_APPARTENENZA,String aTI_GESTIONE, String aCD_ELEMENTO_VOCE , String acd_centro_responsabilita , String acd_linea_attivita) throws  it.cnr.jada.comp.ComponentException {
		LoggableStatement cs = null;
			String cd_voce = null;
		    Voce_fBulk voceBulk = null;       
			try	{
			 cs = new LoggableStatement(getConnection(userContext), 
				 "{ ? = call " +
				 it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				 "CNRCTB053.getVoce_FdaEV(?,?,?,?,?,?)}",false,this.getClass());
			 cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
			 cs.setObject(2, aEsercizio );
			 cs.setObject(3, aTI_APPARTENENZA );
			 cs.setObject(4, aTI_GESTIONE );
			 cs.setObject(5, aCD_ELEMENTO_VOCE );
			 cs.setObject(6, acd_centro_responsabilita );
			 cs.setObject(7, acd_linea_attivita );
			 cs.executeQuery();
			 cd_voce = cs.getString(1);
			 voceBulk = new Voce_fBulk(cd_voce,aEsercizio,aTI_APPARTENENZA,aTI_GESTIONE);
			 voceBulk = (Voce_fBulk)getHome(userContext,Voce_fBulk.class).findByPrimaryKey(voceBulk);			 			 
			} catch (Throwable e) {
			throw handleException(e);
			} finally {
			 if (cs != null) try {
				 cs.close();
			 } catch (SQLException e1) {
				throw handleException(e1);
			 }
			}
			return voceBulk;
	}
	public boolean isCdsAbilitatoAdApprovare(UserContext userContext,String cd_cds, Var_stanz_resBulk var_stanz_res) throws ComponentException{
		try {
			Parametri_cdsBulk param_cds = (Parametri_cdsBulk)getHome(userContext, Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(cd_cds,((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio()));
			if (!param_cds.getFl_approva_var_stanz_res().booleanValue())
			   return false;
			for (Iterator lista = ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).findTipoVariazioniStanz_res().iterator();lista.hasNext();){
				Configurazione_cnrBulk config_cnr = (Configurazione_cnrBulk)lista.next();
				if(config_cnr.getVal01().equalsIgnoreCase(var_stanz_res.getTipologia()))
				  return true;
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return false;
	}
	private boolean isEnteAbilitatoAdApprovare(UserContext userContext, Var_stanz_resBulk var_stanz_res) throws ComponentException{
		try {
			for (Iterator lista = ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).findTipoVariazioniEnteStanz_res().iterator();lista.hasNext();){
				Configurazione_cnrBulk config_cnr = (Configurazione_cnrBulk)lista.next();
				if(config_cnr.getVal01().equalsIgnoreCase(var_stanz_res.getTipologia()))
				  return true;
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return false;
	}
	private MessaggioBulk generaMessaggio(UserContext userContext, UtenteBulk utente, Var_stanz_resBulk var_stanz_res) throws ComponentException, PersistencyException{
		return generaMessaggio(userContext, utente, var_stanz_res, null);	 	
	}
	private MessaggioBulk inizializzaMessaggio(UserContext userContext, UtenteBulk utente) throws ComponentException, PersistencyException{
		MessaggioHome messHome = (MessaggioHome)getHome(userContext,MessaggioBulk.class);
		MessaggioBulk messaggio = new MessaggioBulk();
		messaggio.setPg_messaggio(new Long(messHome.fetchNextSequenceValue(userContext,"CNRSEQ00_PG_MESSAGGIO").longValue()));
		messaggio.setCd_utente(utente.getCd_utente());
		messaggio.setPriorita(new Integer(1));
		return messaggio;
	}
	private MessaggioBulk generaMessaggio(UserContext userContext, UtenteBulk utente, Var_stanz_resBulk var_stanz_res, String tipo) throws ComponentException, PersistencyException{
		MessaggioBulk messaggio = inizializzaMessaggio(userContext, utente);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (tipo == null){
			messaggio.setDs_messaggio(sdf.format(EJBCommonServices.getServerTimestamp()) + " - ï¿½ stata aperta una nuova Variazione allo Stanziamento residuo");
		}else if (tipo.equals(Var_stanz_resBulk.STATO_APPROVATA)){
			messaggio.setDs_messaggio(sdf.format(EJBCommonServices.getServerTimestamp()) + " - ï¿½ stata approvata la Variazione allo Stanziamento residuo");
		}else if (tipo.equals(Var_stanz_resBulk.STATO_RESPINTA)){
			messaggio.setDs_messaggio(sdf.format(EJBCommonServices.getServerTimestamp()) + " - ï¿½ stata respinta la Variazione allo Stanziamento residuo");
		}
		messaggio.setCorpo("Numero variazione:"+var_stanz_res.getPg_variazione());
		messaggio.setCorpo(messaggio.getCorpo() + "\n" + "CdR proponente:"+var_stanz_res.getCentroDiResponsabilita().getCd_ds_cdr());	
		messaggio.setSoggetto(messaggio.getDs_messaggio());
		messaggio.setToBeCreated(); 
		return messaggio;	 		
	}	
	private MessaggioBulk generaMessaggioCopertura(UserContext userContext, UtenteBulk utente, Var_stanz_resBulk var_stanz_res, Ass_var_stanz_res_cdrBulk ass_var) throws ComponentException, PersistencyException{
		MessaggioBulk messaggio = inizializzaMessaggio(userContext, utente);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		messaggio.setDs_messaggio(sdf.format(EJBCommonServices.getServerTimestamp()) + " - ï¿½ stata raggiunta la quota di Spesa assegnata alla Variazione");
		messaggio.setCorpo("Numero variazione:"+var_stanz_res.getPg_variazione());
		messaggio.setCorpo(messaggio.getCorpo() + "\n" + "Il CdR :"+ass_var.getCentro_di_responsabilita().getCd_ds_cdr()+" ha coperto la quota assegnata.");
		messaggio.setSoggetto(messaggio.getDs_messaggio());
		messaggio.setToBeCreated(); 
		return messaggio;	 	
	}
	private boolean inviaMessaggio(UserContext usercontext,Var_stanz_resBulk var_stanz_res, Ass_var_stanz_res_cdrBulk ass_var) throws PersistencyException, ComponentException{
		CdrBulk cdr = (CdrBulk)getHome(usercontext,CdrBulk.class).findByPrimaryKey(new CdrBulk(ass_var.getCd_centro_responsabilita()));
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(usercontext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa()));
		if (var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_STO) ||
				uo.getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
			return true;
		return false;
	}
	public OggettoBulk modificaConBulk(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)oggettobulk;
			try {	
				UtenteHome utenteHome = (UtenteHome)getHome(usercontext,UtenteBulk.class);
				for (java.util.Iterator j=var_stanz_res.getAssociazioneCDR().iterator();j.hasNext();){			
					Ass_var_stanz_res_cdrBulk ass_var = (Ass_var_stanz_res_cdrBulk)j.next();
				if(ass_var.getCrudStatus()==OggettoBulk.TO_BE_CREATED  && ass_var.getCentro_di_responsabilita() != null && ass_var.getCd_centro_responsabilita()!=null){
					if (inviaMessaggio(usercontext,var_stanz_res,ass_var)){
						for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(ass_var.getCd_centro_responsabilita()).iterator();i.hasNext();){
							UtenteBulk utente = (UtenteBulk)i.next();
							MessaggioBulk messaggio = generaMessaggio(usercontext,utente,var_stanz_res);
							super.creaConBulk(usercontext, messaggio);
						}
						}
					}			
				}
				Ass_var_stanz_res_cdrHome AssHome = (Ass_var_stanz_res_cdrHome)getHome(usercontext, Ass_var_stanz_res_cdrBulk.class);
				boolean rigaInsMod = false;
				BigDecimal totaleRighe = Utility.ZERO;  
				for (java.util.Iterator righe=var_stanz_res.getRigaVariazione().iterator();righe.hasNext();){
					Var_stanz_res_rigaBulk var_riga = (Var_stanz_res_rigaBulk)righe.next();
					totaleRighe = totaleRighe.add(Utility.nvl(var_riga.getIm_variazione()));
					if(var_riga.getCrudStatus()==OggettoBulk.TO_BE_CREATED || var_riga.getCrudStatus()==OggettoBulk.TO_BE_UPDATED){
						rigaInsMod = true;				
					}
				}
				if (rigaInsMod){
					Ass_var_stanz_res_cdrBulk ass_var_cdr = (Ass_var_stanz_res_cdrBulk)AssHome.findByPrimaryKey(new Ass_var_stanz_res_cdrBulk(var_stanz_res.getEsercizio(), var_stanz_res.getPg_variazione(), var_stanz_res.getCdr().getCd_centro_responsabilita()));
					if (ass_var_cdr.getIm_spesa().compareTo(totaleRighe) == 0){
					if (inviaMessaggio(usercontext,var_stanz_res,ass_var_cdr)){
						for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(var_stanz_res.getCd_centro_responsabilita()).iterator();i.hasNext();){
							UtenteBulk utente = (UtenteBulk)i.next();
							MessaggioBulk messaggio = generaMessaggioCopertura(usercontext,utente,var_stanz_res,ass_var_cdr);
							super.creaConBulk(usercontext, messaggio);
						}											
					}
				}
			}
			} catch (PersistencyException e) {
			   throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
		}
		cercaProgettiResidui(usercontext,var_stanz_res);	
		return super.modificaConBulk(usercontext, var_stanz_res);
	}
	public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)throws ComponentException {
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)super.creaConBulk(usercontext, oggettobulk);
			try {	
				UtenteHome utenteHome = (UtenteHome)getHome(usercontext,UtenteBulk.class);
				for (java.util.Iterator j=var_stanz_res.getAssociazioneCDR().iterator();j.hasNext();){			
					Ass_var_stanz_res_cdrBulk ass_var = (Ass_var_stanz_res_cdrBulk)j.next();
					if(ass_var.getCd_centro_responsabilita()!=null){
					if (inviaMessaggio(usercontext,var_stanz_res,ass_var)){
						for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(ass_var.getCd_centro_responsabilita()).iterator();i.hasNext();){
							UtenteBulk utente = (UtenteBulk)i.next();
							MessaggioBulk messaggio = generaMessaggio(usercontext,utente,var_stanz_res);
							super.creaConBulk(usercontext, messaggio);
						}
					}			
				}
			}
			} catch (PersistencyException e) {
			   throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
		}
		cercaProgettiResidui(usercontext,var_stanz_res);
		return var_stanz_res;
	}
	public void cercaProgettiResidui(UserContext usercontext, OggettoBulk oggettobulk){
		try{
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)oggettobulk;
			for (java.util.Iterator j=var_stanz_res.getRigaVariazione().iterator();j.hasNext();){
				Var_stanz_res_rigaBulk var_stanz_res_rigaBulk = (Var_stanz_res_rigaBulk)j.next();
				ProgettoHome progettoHome = (ProgettoHome)getHome(usercontext,ProgettoBulk.class);
				SQLBuilder sql = progettoHome.createSQLBuilder();
				sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, var_stanz_res.getEsercizio_residuo());
				sql.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, var_stanz_res_rigaBulk.getLinea_di_attivita().getProgetto().getPg_progetto());
				Broker broker = progettoHome.createBroker(sql);
				List lista = progettoHome.fetchAll(broker);
				/**
				 * Il Modulo non esiste nell'esercizio residuo
				 */
				if (lista.isEmpty()){
					SQLBuilder sqlCrea = progettoHome.createSQLBuilder();
					sqlCrea.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
					sqlCrea.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
					sqlCrea.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, var_stanz_res_rigaBulk.getLinea_di_attivita().getProgetto().getPg_progetto());
					Broker brokerCrea = progettoHome.createBroker(sqlCrea);
					List listaCrea = progettoHome.fetchAll(brokerCrea);
					if (listaCrea.isEmpty())
						return;
					ProgettoBulk progetto = (ProgettoBulk)listaCrea.get(0);
					//Creo il Modulo
					creaProgettiResidui(usercontext, progetto.getPg_progetto(), var_stanz_res.getEsercizio_residuo());
					//Creo la Commessa
					creaProgettiResidui(usercontext, progetto.getProgettopadre().getPg_progetto(), var_stanz_res.getEsercizio_residuo());
					//Creo il Progetto
					creaProgettiResidui(usercontext, progetto.getProgettopadre().getProgettopadre().getPg_progetto(), var_stanz_res.getEsercizio_residuo());
				}
				broker.close();
			}
		}catch(Exception e){
			//Se si verifica qualsiasi tipo di errore non faccio niente
		}
	}
	
	private void creaProgettiResidui(UserContext userContext, Integer pgProgettoRes, Integer esercizioResiduo) throws ComponentException, PersistencyException{
		ProgettoHome progettoHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class);
		ProgettoHome progettoSICHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class,"PROGETTO_SIC");
		SQLBuilder sqlCrea = progettoHome.createSQLBuilder();
		sqlCrea.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sqlCrea.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
		sqlCrea.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, pgProgettoRes);
		Broker brokerCrea = progettoHome.createBroker(sqlCrea);
		List listaCrea = progettoHome.fetchAll(brokerCrea);
		if (listaCrea.isEmpty())
			return;
		ProgettoBulk progetto = (ProgettoBulk)listaCrea.get(0);

		for(Enumeration tipoFasi = ProgettoBulk.tipo_faseAllKeys.keys();tipoFasi.hasMoreElements();){
			String tipo_fase = String.valueOf(tipoFasi.nextElement());
			progetto.setEsercizio(esercizioResiduo);
			if (progetto.isModulo() || progetto.isCommessa())
				progetto.setEsercizio_progetto_padre(esercizioResiduo);
			if (progetto.getUnita_organizzativa()==null)
				progetto.setUnita_organizzativa((Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0));
			if (progetto.getResponsabile() == null)
				progetto.setResponsabile(((TerzoHome)getHome( userContext, TerzoBulk.class)).findTerzoEnte());
			if (progetto.getDt_inizio() == null)
				progetto.setDt_inizio(DateUtils.firstDateOfTheYear(esercizioResiduo));
			progetto.setUser(CNRUserContext.getUser(userContext));
			progetto.setFl_piano_triennale(true);
			progetto.setTipo_fase(tipo_fase);
			if (progetto.isModulo() || progetto.isCommessa())
				progetto.setTipo_fase_progetto_padre(tipo_fase);
			progetto.setToBeCreated();
			try{
				progettoSICHome.insert(progetto, userContext);
			}catch(Exception e){
				//Se si verifica qualsiasi tipo di errore non faccio niente
			}	
		}
		brokerCrea.close();			
	}
	
	
	public void aggiungiDettaglioVariazione(UserContext usercontext, Var_stanz_resBulk var_stanz_res, V_assestato_residuoBulk saldo)throws ComponentException {
		try {
			//Verifico innanzitutto se esiste l'associazione con il CDR altrimenti la creo
			Ass_var_stanz_res_cdrHome AssHome = (Ass_var_stanz_res_cdrHome)getHome(usercontext, Ass_var_stanz_res_cdrBulk.class);
			Ass_var_stanz_res_cdrBulk ass_var_cdrOld = new Ass_var_stanz_res_cdrBulk(var_stanz_res.getEsercizio(), var_stanz_res.getPg_variazione(), saldo.getCd_centro_responsabilita());
			Ass_var_stanz_res_cdrBulk ass_var_cdrNew = (Ass_var_stanz_res_cdrBulk)AssHome.findByPrimaryKey(ass_var_cdrOld);
            if (ass_var_cdrNew == null){
				ass_var_cdrOld.setIm_spesa(Utility.ZERO);
				ass_var_cdrOld.setToBeCreated();
				super.creaConBulk(usercontext,ass_var_cdrOld);			
            }
			//Ora posso inserire la riga di variazione
			Var_stanz_res_rigaBulk var_stanz_res_riga = new Var_stanz_res_rigaBulk();
			var_stanz_res_riga.setEsercizio_res(saldo.getEsercizio_res());
			var_stanz_res_riga.setVar_stanz_res(var_stanz_res);			
			var_stanz_res_riga.setVoce_f(new Voce_fBulk(saldo.getCd_voce(),saldo.getEsercizio(),saldo.getTi_appartenenza(),saldo.getTi_gestione()));
			var_stanz_res_riga.setLinea_di_attivita(new WorkpackageBulk(saldo.getCd_centro_responsabilita(),saldo.getCd_linea_attivita()));
			var_stanz_res_riga.setElemento_voce(new Elemento_voceBulk(saldo.getCd_elemento_voce(),saldo.getEsercizio(),saldo.getTi_appartenenza(),saldo.getTi_gestione()));
			var_stanz_res_riga.setIm_variazione(saldo.getImp_da_assegnare());
			var_stanz_res_riga.setToBeCreated();
			super.creaConBulk(usercontext,var_stanz_res_riga);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
				
		
	}
	public it.cnr.jada.bulk.OggettoBulk generaVariazioneBilancio(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		LoggableStatement cs = null;
		try	{
		  cs = new LoggableStatement(getConnection(userContext), 
			  "{call " +
			  it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			  "CNRCTB075.genera_varente_da_Varstanzres(?,?,?,?,?,?,?)}",false,this.getClass());
		  cs.setObject(1, var_stanz_res.getEsercizio() );
		  cs.setObject(2, var_stanz_res.getPg_variazione() );
		  cs.setObject(3, CNRUserContext.getUser(userContext) );
		  cs.registerOutParameter( 4, java.sql.Types.VARCHAR);
		  cs.registerOutParameter( 5, java.sql.Types.INTEGER);
		  cs.registerOutParameter( 6, java.sql.Types.CHAR);
		  cs.registerOutParameter( 7, java.sql.Types.INTEGER);		  
		  cs.executeQuery();
		  String cds_var_bil = cs.getString(4);
		  if (cds_var_bil != null ){
			  var_stanz_res.setCds_var_bil(cds_var_bil);
			  var_stanz_res.setEs_var_bil(new Integer(cs.getString(5)));
			  var_stanz_res.setTi_app_var_bil(new Character(cs.getString(6).charAt(0)));
			  var_stanz_res.setPg_var_bil(new Integer(cs.getString(7)));
		  }
		}catch (Throwable e) {
			throw handleException(e);
		} finally {
		  if (cs != null) 
			  try {
				  cs.close();
			  } catch (SQLException e1) {
				  throw handleException(e1);
			  }
		}
		return var_stanz_res;
	}
	public it.cnr.jada.bulk.OggettoBulk esitaVariazioneBilancio(UserContext userContext, it.cnr.jada.bulk.OggettoBulk ogettoBulk) throws ComponentException{
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)ogettoBulk;
		if (var_stanz_res.getCds_var_bil() != null){
			LoggableStatement cs = null;		
			try	{
			  cs = new LoggableStatement(getConnection(userContext), 
				  "{call " +
				  it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				  "CNRCTB055.esitaVariazioneBilancio(?,?,?,?,?)}",false,this.getClass());
			  cs.setObject(1, var_stanz_res.getEs_var_bil() );
			  cs.setObject(2, var_stanz_res.getCds_var_bil());
			  cs.setObject(3, var_stanz_res.getTi_app_var_bil().toString() );
			  cs.setObject(4, var_stanz_res.getPg_var_bil() );
			  cs.setObject(5, CNRUserContext.getUser(userContext) );
			  cs.executeQuery();
			}catch (SQLException e) {
				try {
					SQLExceptionHandler sqlException = SQLExceptionHandler.getInstance();
					PersistencyException eccezione = sqlException.handleSQLException(e, var_stanz_res);
					String soggetto = "Si ï¿½ verificato un errore durante l'approvazione della variazione sul bilancio dell'ente "+var_stanz_res.getEs_var_bil()+"/"+var_stanz_res.getPg_var_bil();
					
					String preText = "Si ï¿½ verificato il seguente errore durante l'approvazione della variazione sul bilancio dell'ente "+var_stanz_res.getEs_var_bil()+"/"+var_stanz_res.getPg_var_bil() + 
					                 "<BR>" + "generata in automatico a seguito della Variazione allo stanziamento residuo nï¿½"+var_stanz_res.getPg_variazione()+
					                 " del "+  var_stanz_res.getEsercizio()+".<BR><BR>"+
									 "<b>"+eccezione.getMessage()+"</b><BR><BR>"+
					                 "La Variazione al bilancio dell'Ente rimarrï¿½ pertanto PROVVISORIA.<BR>";
					generaEMAIL(userContext, var_stanz_res, soggetto, preText, null,"ERR");
					var_stanz_res.setErroreEsitaVariazioneBilancio(true);
				}catch (IntrospectionException e1) {
					throw handleException(e1);
				} catch (PersistencyException e1) {
					throw handleException(e1);
				}
			} finally {
			  if (cs != null) 
				  try {
					  cs.close();
				  } catch (SQLException e1) {
					  throw handleException(e1);
				  }
			}
		}	
		return var_stanz_res;
	}
	private void verificaTotaliDaAccertamentoModifica(UserContext usercontext, Var_stanz_resBulk var) throws ApplicationException {
		Accertamento_modificaBulk accMod = var.getAccMod();
		if (accMod!=null) {
			BigDecimal impTotale = Utility.ZERO;
			for(Iterator it = accMod.getAccertamento_mod_voceColl().iterator();it.hasNext();) {
				Accertamento_mod_voceBulk accModVoce = (Accertamento_mod_voceBulk) it.next();
				impTotale = impTotale.add(accModVoce.getIm_modifica());
			}
			if (impTotale.compareTo(Utility.nvl(var.getTotale_da_ripartire()))!=0)
				throw new ApplicationException("Impossibile effettuare l'operazione !\n"+
					   "Il totale da ripartire non corrisponde al totale della modifica all'accertamento residuo collegato.");					
		}
	}
	 /**
	 * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
	 *
	 * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
	 */
	private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws ComponentException 
	{
		try
		{
			return (Configurazione_cnrComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}	
	public void validaOrigineFontiPerAnnoResiduo(UserContext usercontext, Integer annoResiduo, String origineFonti) throws ComponentException {
		try{
			UtenteBulk utente = (UtenteBulk)(getHome(usercontext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(usercontext))));
			if (!utente.isSupervisore()) {
				Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( usercontext, CNRUserContext.getEsercizio(usercontext), CNRUserContext.getCd_cds(usercontext), Configurazione_cnrBulk.PK_ANNI_RESIDUI_VAR_ST_RES, String.valueOf(annoResiduo));	
				if ( config == null  )
					config = createConfigurazioneCnrComponentSession().getConfigurazione( usercontext, CNRUserContext.getEsercizio(usercontext), null, Configurazione_cnrBulk.PK_ANNI_RESIDUI_VAR_ST_RES, String.valueOf(annoResiduo));
				if (config != null){
					if (!origineFonti.equalsIgnoreCase(config.getVal01()) && !origineFonti.equalsIgnoreCase(config.getVal02())){
						throw new ApplicationException("Non è consentito emettere variazioni allo stanziamento residuo\n"+
						   "su residui del "+annoResiduo+" sulle "+ NaturaBulk.tipo_naturaKeys.get(origineFonti));	
					}
				}
			}
		}catch ( Exception e ){
			throw handleException( e )	;
		}	
		
	}
	
	@Override
	protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		if (oggettobulk instanceof Var_stanz_resBulk){
			Var_stanz_resBulk var = (Var_stanz_resBulk)oggettobulk;
			validaOrigineFontiPerAnnoResiduo(usercontext, var.getEsercizio_residuo(), var.getTipologia_fin());
		}
		super.validaCreaModificaConBulk(usercontext, oggettobulk);
	}
}
