/*
 * Created on Apr 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.RicercaContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_ditteBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoHome;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.contratto.bulk.OrganoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.contratto.bulk.Stampa_elenco_contrattiBulk;
import it.cnr.contab.config00.contratto.bulk.Tipo_atto_amministrativoBulk;
import it.cnr.contab.config00.contratto.bulk.Tipo_contrattoBulk;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_norma_perlaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.StringTokenizer;

import javax.ejb.EJBException;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ContrattoComponent extends it.cnr.jada.comp.CRUDDetailComponent implements Cloneable, Serializable, IPrintMgr {
	public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		if(bulk instanceof ContrattoBulk)
			return select(userContext, clauses, (ContrattoBulk) bulk);
		return super.select(userContext, clauses, bulk);			
	}
	/**
	 * Pre:  Ricerca contratti disponibili
	 * Post: Limitazione ai contratti dell'esercizio in scrivania.
	 */        
	public Query select(UserContext userContext,CompoundFindClause clauses,ContrattoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
	   SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	   sql.addSQLClause("AND","ESERCIZIO",sql.LESS_EQUALS,CNRUserContext.getEsercizio(userContext));
	   // Se uo 999.000 in scrivania: visualizza tutti i contratti
	   /*Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
	   	 sql.openParenthesis("AND");
		   sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
		   SQLBuilder sqlAssUo = getHome(userContext,Ass_contratto_uoBulk.class).createSQLBuilder();		   
		   sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
		   sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
		   sqlAssUo.addSQLClause("AND","ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
		   sql.addSQLExistsClause("OR",sqlAssUo);
		 sql.closeParenthesis();  		 
	   }*/	   
	   return sql;
	}
	/**
	 * Pre:  Ricerca contratto_padre
	 * Post: Limitazione ai contratti diversi da quello in oggetto.
	 */
	public SQLBuilder selectContratto_padreByClause (UserContext userContext, OggettoBulk bulk, ContrattoBulk contratto_padre,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if(((ContrattoBulk)bulk).getPg_contratto() != null && ((ContrattoBulk)bulk).getNatura_contabile() != null && 
		   (((ContrattoBulk)bulk).getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI) /*||
		   ((ContrattoBulk)bulk).getStato().equals(ContrattoBulk.STATO_PROVVISORIO)*/))
		   throw new ApplicationException("Non è possibile associare un contratto di riferimento!");		
		if (clause == null) 
		  clause = contratto_padre.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, contratto_padre).createSQLBuilder();
		sql.openNotParenthesis("AND");
		  sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, ((ContrattoBulk)bulk).getEsercizio());
		  sql.addSQLClause("AND", "STATO", sql.EQUALS, ((ContrattoBulk)bulk).getStato());		
		  sql.addSQLClause("AND", "PG_CONTRATTO", sql.EQUALS, ((ContrattoBulk)bulk).getPg_contratto());
		sql.closeParenthesis();
		sql.addSQLClause("AND", "NATURA_CONTABILE", sql.EQUALS, ContrattoBulk.NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI);
		sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
		if (clause != null) 
		  sql.addClause(clause);		
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Contratto
	 * Post: Limitazione ai tipi con Tipo Gestione uguale a quella in oggetto
	 */
	public SQLBuilder selectTipo_contrattoByClause (UserContext userContext, OggettoBulk bulk, Tipo_contrattoBulk tipo_contratto,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_contratto.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_contratto).createSQLBuilder();
		if(((ContrattoBulk)bulk).getNatura_contabile() == null)
		   throw new ApplicationException("Per effettuare la ricerca valorizzare il campo Natura del rapporto!");  
		sql.addSQLClause("AND", "NATURA_CONTABILE", sql.EQUALS, ((ContrattoBulk)bulk).getNatura_contabile());
		sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}

	/**
	 * Pre:  Ricerca CIG
	 * Post: Il CIG può essere collegato ad un contratto solo se vengono rispettate le seguenti regole:				
			CD_TERZO_RUP del CIG è il medesimo del contratto che si sta inserendo quindi : 
			CIG. CD_TERZO_RUP = CONTRATTO. CD_TERZO_RESP
			Il CIG non deve risultare associato ad altri contratti.
	 */
	public SQLBuilder selectCigByClause (UserContext userContext, ContrattoBulk contratto, CigBulk cig, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = cig.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, cig).createSQLBuilder();
		if(contratto.getResponsabile() == null || contratto.getResponsabile().getCd_terzo() == null)
		   throw new ApplicationException("Per effettuare la ricerca valorizzare il campo Responsabile!");  
		sql.addSQLClause(FindClause.AND, "CD_TERZO_RUP", SQLBuilder.EQUALS, contratto.getResponsabile().getCd_terzo());
		sql.addClause(FindClause.AND, "FL_VALIDO", SQLBuilder.EQUALS, Boolean.TRUE);
		SQLBuilder sqlNotExists = getHome(userContext, contratto).createSQLBuilder();
		sqlNotExists.addSQLJoin("CD_CIG", SQLBuilder.EQUALS, "CIG.CD_CIG");
		if (contratto.getPg_contratto() != null){
			sqlNotExists.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.NOT_EQUALS, contratto.getEsercizio());
			sqlNotExists.addSQLClause(FindClause.AND, "STATO", SQLBuilder.NOT_EQUALS, contratto.getStato());
			sqlNotExists.addSQLClause(FindClause.AND, "PG_CONTRATTO", SQLBuilder.NOT_EQUALS, contratto.getPg_contratto());
		}
		sql.addSQLNotExistsClause(FindClause.AND, sqlNotExists);
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}
	
	public SQLBuilder selectTipoNormaPerlaByClause (UserContext userContext, OggettoBulk bulk, Tipo_norma_perlaBulk tipo_norma_perla,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_norma_perla.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_norma_perla).createSQLBuilder();
		sql.addSQLClause("AND", "TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_norma_perlaBulk.ASS_CONTRATTI);
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}

	/**
	 * Pre:  Ricerca Tipo Provvedimento
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectAttoByClause (UserContext userContext, OggettoBulk bulk, Tipo_atto_amministrativoBulk tipo_atto_amministrativo,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_atto_amministrativo.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_atto_amministrativo).createSQLBuilder();
		sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Provvedimento di annullamento
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectAtto_annullamentoByClause (UserContext userContext, OggettoBulk bulk, Tipo_atto_amministrativoBulk tipo_atto_amministrativo,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_atto_amministrativo.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_atto_amministrativo).createSQLBuilder();
		sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Organo
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectOrganoByClause (UserContext userContext, OggettoBulk bulk, OrganoBulk tipo_organo,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_organo.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_organo).createSQLBuilder();
		sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
		sql.addSQLClause("AND", "DT_INIZIO_VALIDITA", SQLBuilder.LESS_EQUALS, ((ContrattoBulk)bulk).getDt_stipula());
		sql.addSQLClause("AND", "DT_FINE_VALIDITA", SQLBuilder.GREATER_EQUALS, ((ContrattoBulk)bulk).getDt_stipula());
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Organo di annullamento
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectOrgano_annullamentoByClause (UserContext userContext, OggettoBulk bulk, OrganoBulk tipo_organo,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_organo.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_organo).createSQLBuilder();
		sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
		sql.addSQLClause("AND", "DT_INIZIO_VALIDITA", SQLBuilder.LESS_EQUALS, ((ContrattoBulk)bulk).getDt_stipula());
		sql.addSQLClause("AND", "DT_FINE_VALIDITA", SQLBuilder.GREATER_EQUALS, ((ContrattoBulk)bulk).getDt_stipula());		
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}
	/**
	 * Pre:  Ricerca Figura giuridica interna
	 * Post: Limitazione ai terzi di tipo Unità Organizzativa
	 */
	public SQLBuilder selectFigura_giuridica_internaByClause (UserContext userContext, OggettoBulk bulk, TerzoBulk terzo,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = terzo.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, terzo).createSQLBuilder();
		sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.ISNOTNULL, null);
		// Se uo 999.000 in scrivania: visualizza tutti i progetti
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		  sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		}		
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}	
public SQLBuilder selectFigura_giuridica_esternaByClause(UserContext userContext,  OggettoBulk bulk,it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException {
		
		TerzoHome home = (TerzoHome)getHome(userContext, TerzoBulk.class, "V_TERZO_CF_PI");
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","DT_FINE_RAPPORTO",sql.ISNULL,null);
		sql.addClause(clauses); 
		return sql;
}
	/**
	 * Pre:  Ricerca Tipo Provvedimento
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectProcedura_amministrativaByClause (UserContext userContext, OggettoBulk bulk, Procedure_amministrativeBulk procedura_amministrativa,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = procedura_amministrativa.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, procedura_amministrativa).createSQLBuilder();
		sql.openParenthesis("AND");
		sql.addClause("OR", "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_FORNITURA_SERVIZI);
		sql.addClause("OR", "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_GENERICA);
		sql.closeParenthesis();
		sql.addClause("AND", "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}	
	/**
	 * Valida i campi obbligatori
	 * @param uc
	 * @param bulk
	 * @throws ComponentException
	 */
	private void validaCampiObbligatori(UserContext uc, ContrattoBulk bulk) throws ComponentException, ApplicationException, IntrospectionException, PersistencyException, SQLException{
		if(bulk.getDt_fine_validita() != null && bulk.getDt_inizio_validita() != null && 
		   bulk.getDt_fine_validita().before(bulk.getDt_inizio_validita()))
		   throw new ApplicationException("La data di inizio non può essere superiore alla data di fine.");
		if(bulk.getCd_terzo_resp() == null)
		  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("cd_terzo_resp").getLabel()); 
		if(bulk.getDt_stipula() == null)
		  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("dt_stipula").getLabel());
		if(bulk.getNatura_contabile() != null && !((ContrattoBulk)bulk).getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI)){
		  if(((ContrattoBulk)bulk).getIm_contratto_attivo() == null && 
		      (bulk.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO) ||
			   bulk.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)))
			throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("im_contratto_attivo").getLabel());
		  if((bulk.getIm_contratto_passivo() == null) && 
			  (bulk.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO) ||
			   bulk.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)))
		    throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("im_contratto_passivo").getLabel());			
		  if(( bulk.getIm_contratto_passivo_netto()==null)&& 
				  (bulk.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO) ||
				   bulk.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO)))
			    throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("im_contratto_passivo_netto").getLabel());
		}
		if(bulk.isDs_atto_non_definitoVisible() && bulk.getDs_atto_non_definito() == null)
		  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("ds_atto_non_definito").getLabel());
		if(bulk.isDs_atto_ann_non_definitoVisible() && bulk.getDs_atto_ann_non_definito() == null)
		  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("ds_atto_ann_non_definito").getLabel());	  		   
		if(bulk.isDs_organo_non_definitoVisible() && bulk.getDs_organo_non_definito() == null)
		  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("ds_organo_non_definito").getLabel());
		if(bulk.isDs_organo_ann_non_definitoVisible() && bulk.getDs_organo_ann_non_definito() == null)
		  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("ds_organo_ann_non_definito").getLabel());
		if(bulk.getDt_stipula() != null) {
			if(bulk.getOrgano() != null)
			if(bulk.getOrgano().getDt_inizio_validita()!= null)
				if(bulk.getOrgano().getDt_inizio_validita().after(bulk.getDt_stipula()) || bulk.getOrgano().getDt_fine_validita().before(bulk.getDt_stipula()))
				  throw new ApplicationException("Il Soggetto deve avere un periodo di validità che include la data di stipula del contratto.");
			if(bulk.getOrgano_annullamento() != null)
			  if(bulk.getOrgano_annullamento().getDt_inizio_validita()!= null)
				if(bulk.getOrgano_annullamento().getDt_inizio_validita().after(bulk.getDt_stipula()) || bulk.getOrgano_annullamento().getDt_fine_validita().before(bulk.getDt_stipula()))
				  throw new ApplicationException("Il Soggetto di annullamento deve avere un periodo di validità che include la data di stipula del contratto.");			 
		}
		/*Controllo obbligatorietà del protocollo informatico */
		Parametri_cdsHome cdsHome = (Parametri_cdsHome)getHome(uc, Parametri_cdsBulk.class);
		Parametri_cdsBulk param_cds = (Parametri_cdsBulk)cdsHome.findByPrimaryKey(new Parametri_cdsBulk(CNRUserContext.getCd_cds(uc),CNRUserContext.getEsercizio(uc)));
		if(param_cds.getFl_obbligo_protocollo_inf().booleanValue()){
			if(bulk.getEsercizio_protocollo() == null)
			  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("esercizio_protocollo").getLabel());	  		   
			if(bulk.getCd_protocollo_generale() == null)
			  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("cd_protocollo_generale").getLabel());	  		   			
		}
		if (bulk.getCig() != null && bulk.getCd_terzo_resp() != null && bulk.getCig().getCdTerzoRup() != null && !bulk.getCig().getCdTerzoRup().equals(bulk.getCd_terzo_resp())){
			throw new ApplicationException("Il Terzo del CIG non coincide con il Responsabile!");
		}
		if(bulk.getDt_stipula() != null ) {
			if(bulk.getDt_stipula().after(getHome(uc, ContrattoBulk.class).getServerDate())) 
				throw new ApplicationException("La data di stipula non può essere superiore alla data odierna!");
		}
		if(bulk.getDt_stipula() != null && bulk.getDt_fine_validita()!=null) {
			if(bulk.getDt_stipula().after(bulk.getDt_fine_validita()))
				throw new ApplicationException("La data di stipula non può essere superiore alla data fine validita!");
		}
		if(bulk.getDt_inizio_validita() != null && bulk.getDt_fine_validita()!=null) {
			if(bulk.getDt_inizio_validita().after(bulk.getDt_fine_validita()))
				throw new ApplicationException("La data di inizio non può essere superiore alla data fine validita!");
		}
		if(bulk.getIm_contratto_passivo() != null && bulk.getIm_contratto_passivo_netto()!=null) {
			if(bulk.getIm_contratto_passivo_netto().compareTo(bulk.getIm_contratto_passivo())>0)
				throw new ApplicationException("Importo netto passivo superiore all'importo passivo!");
		}
	    if (bulk.isPassivo() && bulk.isDefinitivo() && bulk.getFl_pubblica_contratto()!=null && bulk.getFl_pubblica_contratto())  
				if (bulk.getDitteInvitate()!=null && bulk.getDitteInvitate().size()==0 )
					throw handleException(new ApplicationException("Bisogna indicare le ditte invitate!"));

		try {
			Date data_stipula_contratti = Utility.createParametriCnrComponentSession().
			getParametriCnr(uc, CNRUserContext.getEsercizio(uc)).getData_stipula_contratti();
			if (!(bulk.getDt_stipula().before(data_stipula_contratti))){
				if ((bulk.isPassivo() || bulk.isAttivo_e_Passivo() || bulk.isSenzaFlussiFinanziari()) && ((bulk.getDirettore() == null)|| bulk.getDirettore().getCd_terzo()==null ))
					  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("direttore").getLabel());
			}
			if (!(bulk.getDt_stipula().before(data_stipula_contratti)) && bulk.isDefinitivo()){
				if ((bulk.isPassivo() || bulk.isAttivo_e_Passivo()) && bulk.getFl_mepa() == null) 
					  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("fl_mepa").getLabel());
				if ((bulk.isPassivo() || bulk.isAttivo_e_Passivo()) && bulk.getTipoNormaPerla() == null) 
						  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("tipoNormaPerla").getLabel());
				if (bulk.getTipo_contratto() != null && bulk.getTipo_contratto().getFl_cig() != null  && bulk.getTipo_contratto().getFl_cig() && bulk.getCig() == null)
					  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(bulk.getClass()).getFieldProperty("cig").getLabel());
			}
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (EJBException e) {
			throw handleException(e);
		}
		
	}
	public void controllaCancellazioneAssociazioneUo(UserContext userContext, Ass_contratto_uoBulk ass_contratto_uo) throws ComponentException{
		Ass_contratto_uoHome home = (Ass_contratto_uoHome)getHome(userContext, Ass_contratto_uoBulk.class);
		try {
			if(home.existsDocContForAssContrattoUo(ass_contratto_uo)){
					throw new ApplicationException("Impossibile eliminare l'Unità organizzativa " + ass_contratto_uo.getCd_unita_organizzativa()+
												   " poichè esistono documenti contabili associati.");				
			}
		}catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (SQLException e) {
			throw new ComponentException(e);
		}
	}	
	public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException
	{
		if(oggettobulk instanceof ContrattoBulk)
			try {
				validaCampiObbligatori(usercontext,(ContrattoBulk)oggettobulk);
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
			} catch (SQLException e) {
				throw new ComponentException(e);
			}
		return super.creaConBulk(usercontext, oggettobulk);
	}	
	public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException{
		if(bulk instanceof ContrattoBulk)
		  return modificaConBulk(uc, (ContrattoBulk) bulk);
		return super.modificaConBulk(uc, bulk);  
	}		
	/**
	 * Pre:  Controllo il progressivo negativo
	 * Post: Aggiorno il progressivo dai numeratori
	 */  			
	public OggettoBulk modificaConBulk(UserContext userContext, ContrattoBulk bulk) throws ComponentException{
		try {
			validaCampiObbligatori(userContext,(ContrattoBulk)bulk);
			ContrattoBulk contratto=(ContrattoBulk)bulk;
			Date dataStipulaParametri = ((Parametri_cnrBulk)getHome(userContext, Parametri_cnrBulk.class).
					findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getData_stipula_contratti();
			
			boolean pubblica = ((Parametri_cnrBulk)getHome(userContext, Parametri_cnrBulk.class).
					findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_pubblica_contratto().booleanValue();
		    if(pubblica){
				if (contratto.getDt_stipula().after(dataStipulaParametri) ||
					contratto.getDt_stipula().equals(dataStipulaParametri)){
					if (contratto.isPassivo() || contratto.isAttivo_e_Passivo() )
						if (contratto.isDefinitivo() && contratto.getTipo_contratto() != null && contratto.getTipo_contratto().getFl_pubblica_contratto() != null  && contratto.getTipo_contratto().getFl_pubblica_contratto())  
							contratto.setFl_pubblica_contratto(Boolean.TRUE);
						else
							contratto.setFl_pubblica_contratto(Boolean.FALSE);
					else
						contratto.setFl_pubblica_contratto(Boolean.FALSE);					
				}
		    }else 
		    	contratto.setFl_pubblica_contratto(Boolean.FALSE);

		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (SQLException e) {
			throw new ComponentException(e);
		}
		return super.modificaConBulk(userContext,bulk);
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
				isExistsContrattoValido(userContext, bulk);				
				if (bulk instanceof ContrattoBulk)
				  if(((ContrattoBulk)bulk).isProvvisorio())
				    super.eliminaConBulk(userContext, bulk);
				  else{					
				  	ContrattoHome home = (ContrattoHome)getHome(userContext, ContrattoBulk.class);
					ContrattoBulk contratto = (ContrattoBulk)bulk;					
					ContrattoBulk contrattoClone = (ContrattoBulk)bulk.clone();
					// Inserisco il nuovo contratto recupero le uo associate al contratto e le lego a quello definitivo		
					for (java.util.Iterator j=contrattoClone.getAssociazioneUO().iterator();j.hasNext();){
						Ass_contratto_uoBulk ass_contratto_uo = (Ass_contratto_uoBulk)j.next();
						ass_contratto_uo.setContratto(contrattoClone);
						ass_contratto_uo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
					}
					contrattoClone.setCrudStatus(OggettoBulk.TO_BE_CREATED);
					contrattoClone.setDt_annullamento(EJBCommonServices.getServerTimestamp());
					contrattoClone.setStato(ContrattoBulk.STATO_CESSSATO);
					contrattoClone.setFl_pubblica_contratto(Boolean.FALSE);
					super.creaConBulk(userContext,contrattoClone);
					
					Broker brokerAccertamenti = home.createBroker(home.findAccertamenti(userContext,contratto));
					while(brokerAccertamenti.next()){
						AccertamentoBulk accertamento = (AccertamentoBulk)brokerAccertamenti.fetch(AccertamentoBulk.class);
						accertamento.setContratto(contrattoClone);	
						accertamento.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
						super.modificaConBulk(userContext,accertamento);			
					}
					Broker brokerObbligazioni = home.createBroker(home.findObbligazioni(userContext,contratto));
					while(brokerObbligazioni.next()){
						ObbligazioneBulk obbligazione = (ObbligazioneBulk)brokerObbligazioni.fetch(ObbligazioneBulk.class);
						obbligazione.setContratto(contrattoClone);
						obbligazione.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
						super.modificaConBulk(userContext,obbligazione);				
					}					
					Broker brokerCompensi = home.createBroker(home.findCompensi(userContext,contratto));
					while(brokerCompensi.next()){
						CompensoBulk compenso = (CompensoBulk)brokerCompensi.fetch(CompensoBulk.class);
						compenso.setContratto(contrattoClone);
						compenso.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
						updateBulk(userContext, compenso);
						// non può essere utilizzato
						//super.modificaConBulk(userContext,obbligazione);			
					}					
					
					/* Elimino il contratto definitivo */
					contratto.setCrudStatus(OggettoBulk.TO_BE_DELETED);
					for (java.util.Iterator j=contratto.getAssociazioneUO().iterator();j.hasNext();){
						Ass_contratto_uoBulk ass_contratto_uo = (Ass_contratto_uoBulk)j.next();
						ass_contratto_uo.setCrudStatus(OggettoBulk.TO_BE_DELETED);
					}			
					super.eliminaConBulk(userContext,contratto);
					
				  }
				else{  
					((ICancellatoLogicamente)bulk).cancellaLogicamente();
					updateBulk(userContext, bulk);
				}				
			}else{
				super.eliminaConBulk(userContext, bulk);				
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException ex) {
			throw new ComponentException(ex);			
		}  
	}
	public void isExistsContrattoValido(UserContext userContext, OggettoBulk bulk) throws ComponentException{
		ContrattoHome testataHome = (ContrattoHome)getHome(userContext, ContrattoBulk.class);
		try {
			boolean exists = false;
			if(bulk instanceof Tipo_contrattoBulk)
			  exists = testataHome.existsContrattoValidoForTipo((Tipo_contrattoBulk)bulk);
			else if(bulk instanceof Tipo_atto_amministrativoBulk)
			  exists = testataHome.existsContrattoValidoForProvvedimento((Tipo_atto_amministrativoBulk)bulk);
			else if(bulk instanceof OrganoBulk)
			  exists = testataHome.existsContrattoValidoForOrgano((OrganoBulk)bulk);
			else if(bulk instanceof Procedure_amministrativeBulk)
			  exists = testataHome.existsContrattoValidoForProcedure_amministrative((Procedure_amministrativeBulk)bulk);
			  
			if(exists)
			  throw new ApplicationException("Impossibile effettuare l'annullamento, esistono Contratti ancora validi associati.");
			/*if(bulk instanceof ContrattoBulk){
				exists = testataHome.existsDocContForContratto((ContrattoBulk)bulk);
				if(exists)
				  throw new ApplicationException("Impossibile effettuare l'annullamento, esistono Documenti contabili associati.");
			}*/
			   
			  
		} catch (Exception e) {
			throw new ComponentException(e);
		}
	}
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,ContrattoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			ContrattoBulk testata = (ContrattoBulk)super.inizializzaBulkPerModifica(userContext,bulk);			
			testata=initializzaUnita_Organizzativa(userContext,testata);
			
			ContrattoHome testataHome = (ContrattoHome)getHome(userContext, ContrattoBulk.class);
			testata.setDitteInvitate(new it.cnr.jada.bulk.BulkList(testataHome.findDitteAssociate(userContext, testata, Ass_contratto_ditteBulk.LISTA_INVITATE)));
			return calcolaTotDocCont(userContext,testata);
		} catch(Exception e) {
				throw handleException(e);
		}				
	}
	public OggettoBulk cercaContrattoCessato(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			ContrattoBulk contratto = (ContrattoBulk)bulk;
			ContrattoHome testataHome = (ContrattoHome)getHome(userContext, ContrattoBulk.class);			
			return (OggettoBulk)testataHome.findByPrimaryKey(new ContrattoBulk(contratto.getEsercizio(),ContrattoBulk.STATO_CESSSATO,contratto.getPg_contratto()));
		} catch(Exception e) {
				throw handleException(e);
		}				
	}	
	public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)throws ComponentException
	{
		if (oggettobulk instanceof ContrattoBulk)
		  initializzaUo(usercontext, (ContrattoBulk) oggettobulk);
		return super.inizializzaBulkPerRicerca(usercontext, oggettobulk);  
	}
	public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)throws ComponentException
	{
		if (oggettobulk instanceof ContrattoBulk)
		  initializzaUo(usercontext, (ContrattoBulk) oggettobulk);
		return super.inizializzaBulkPerRicercaLibera(usercontext, oggettobulk);  
	}			
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)throws ComponentException
	{
		if (oggettobulk instanceof ContrattoBulk)
		  initializzaUo(usercontext, (ContrattoBulk) oggettobulk);
		return super.inizializzaBulkPerInserimento(usercontext, oggettobulk);  
	}			
	private void initializzaUo(UserContext usercontext, ContrattoBulk contratto)throws ComponentException
	{
		try {
			Unita_organizzativaHome home = (Unita_organizzativaHome)getHome( usercontext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk unita = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
			contratto.setUnita_organizzativa(unita); 
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}	
	
	public ContrattoBulk initializzaUnita_Organizzativa(UserContext usercontext, ContrattoBulk contratto)throws ComponentException
	{
		try {
			Unita_organizzativaHome home = (Unita_organizzativaHome)getHome( usercontext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk unita = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
			if (contratto.getUnita_organizzativa() == null)
				contratto.setUnita_organizzativa(unita);
			ContrattoHome testataHome = (ContrattoHome)getHome(usercontext, ContrattoBulk.class);
			contratto.setAssociazioneUODisponibili(new it.cnr.jada.bulk.BulkList(testataHome.findAssociazioneUODisponibili(contratto)));
			if (contratto != null && contratto.getPg_contratto() != null)
			  contratto.setAssociazioneUO(new it.cnr.jada.bulk.BulkList(testataHome.findAssociazioneUO(contratto)));
			getHomeCache(usercontext).fetchAll(usercontext);
			return contratto;  
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
	}	
	/**
	 * Pre:  Preparare l'oggetto alle modifiche;
	 * Post: carica la lista di dettagli associati a un Contratto
	 */
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {		
		if(bulk instanceof ContrattoBulk)
			return inizializzaBulkPerModifica(userContext, (ContrattoBulk) bulk);
		return super.inizializzaBulkPerModifica(userContext, bulk);	
	}
	/**
	  *  calcolo il Totale dei documenti contabili associati al Contratto
	  *    PreCondition:
	  *   	 Viene richiesta la visualizzazione del totale dei documenti contabili associati 
	  *		 al contratto in oggetto di tipo E o S
	  *    PostCondition:
	  *      L'utente può visualizzare il totale dei documenti 
	  *
	  * @param userContext lo user context
	  * @param contratto l'istanza di  <code>ContrattoBulk</code>
	  */
	public ContrattoBulk calcolaTotDocCont (UserContext userContext,ContrattoBulk contratto) throws ComponentException
	{
		try
		{
			if(contratto == null)
			   return contratto;
			if(contratto.getPg_contratto()==null)
			   return contratto;			   
			if(contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI))
			   return calcolaTotDocContForPadre(userContext,contratto);
			if(contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO))
			   return calcolaTotDocContForAttivoPassivo(userContext,contratto);			   
			ContrattoHome testataHome = (ContrattoHome)getHome(userContext, ContrattoBulk.class);
			
			contratto.setDitteInvitate(new it.cnr.jada.bulk.BulkList(testataHome.findDitteAssociate(userContext, contratto, Ass_contratto_ditteBulk.LISTA_INVITATE)));
			
			SQLBuilder sql = null;
			SQLBuilder sql_liq = null;
			SQLBuilder sql_pag = null;
			SQLBuilder sql_liq_netto = null;
			SQLBuilder sql_pag_netto = null;
			SQLBuilder sqlOrdini = null;
			if(contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO)){
			  sql = testataHome.calcolaTotAccertamenti( userContext, contratto);
			  sql_liq= testataHome.calcolaTotDocumentiAtt(userContext, contratto);
			  sql_pag=testataHome.calcolaTotReversali(userContext, contratto);
			}
			else if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)){
			  sql = testataHome.calcolaTotObbligazioni( userContext, contratto);
			  sql_liq= testataHome.calcolaTotDocumentiPas(userContext, contratto);
			  sql_pag=testataHome.calcolaTotMandati(userContext, contratto);

			  sqlOrdini = testataHome.calcolaTotOrdini( userContext, contratto);
			  sql_liq_netto= testataHome.calcolaTotDocumentiPasNetto(userContext, contratto);
			  sql_pag_netto=testataHome.calcolaTotMandatiNetto(userContext, contratto);
			}
			java.math.BigDecimal tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			java.math.BigDecimal tot_ordini = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_etr( tot_doc_cont );
			contratto.setTot_doc_cont_spe( tot_doc_cont );
			contratto.setTot_docamm_cont_spe( tot_doc_cont );
			contratto.setTot_docamm_cont_etr( tot_doc_cont );
			contratto.setTot_doccont_cont_spe( tot_doc_cont );
			contratto.setTot_doccont_cont_etr( tot_doc_cont );
			contratto.setTot_docamm_cont_spe_netto(tot_doc_cont );
			contratto.setTot_doccont_cont_spe_netto( tot_doc_cont );
			
			contratto.setTot_ordini( tot_doc_cont );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sql.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}
			
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			if(contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO))  						  
			   contratto.setTot_doc_cont_etr( tot_doc_cont );
			else if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO))
			   contratto.setTot_doc_cont_spe( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sql_liq.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}
			
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			
			if(contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO))  						  
				   contratto.setTot_docamm_cont_etr( tot_doc_cont );
			else if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO))
					contratto.setTot_docamm_cont_spe( tot_doc_cont );
			
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)){
					try {
						java.sql.ResultSet rs = null;
						LoggableStatement ps = null;
						try {
							ps = sql_liq_netto.prepareStatement(getConnection(userContext));
							try {
								rs = ps.executeQuery();
								if (rs.next())
								tot_doc_cont = rs.getBigDecimal(1);
							} catch (java.sql.SQLException e) {
								throw handleSQLException(e);
							} finally {
								if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
							}
						} finally {
							if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
						}
					} catch (java.sql.SQLException ex) {
						throw handleException(ex);
					}
				if (tot_doc_cont == null)
					  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
						
				contratto.setTot_docamm_cont_spe_netto( tot_doc_cont );
			 }
			   
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sql_pag.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}
			
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			if(contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_ATTIVO))  						  
			   contratto.setTot_doccont_cont_etr( tot_doc_cont );
			else if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO))
			   contratto.setTot_doccont_cont_spe( tot_doc_cont );
			
			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)){
				try {
					java.sql.ResultSet rs = null;
					LoggableStatement ps = null;
					try {
						ps = sql_pag_netto.prepareStatement(getConnection(userContext));
						try {
							rs = ps.executeQuery();
							if (rs.next())
							tot_doc_cont = rs.getBigDecimal(1);
						} catch (java.sql.SQLException e) {
							throw handleSQLException(e);
						} finally {
							if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
						}
					} finally {
						if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
					}
				} catch (java.sql.SQLException ex) {
					throw handleException(ex);
				}
			if (tot_doc_cont == null)
				  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
					
			contratto.setTot_doccont_cont_spe_netto( tot_doc_cont );
		 }

			if (contratto.getNatura_contabile().equals(ContrattoBulk.NATURA_CONTABILE_PASSIVO)) {
				try {
					java.sql.ResultSet rs = null;
					LoggableStatement ps = null;
					try {
						ps = sqlOrdini.prepareStatement(getConnection(userContext));
						try {
							rs = ps.executeQuery();
							if (rs.next())
								tot_ordini = rs.getBigDecimal(1);
						} catch (java.sql.SQLException e) {
							throw handleSQLException(e);
						} finally {
							if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
						}
					} finally {
						if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
					}
				} catch (java.sql.SQLException ex) {
					throw handleException(ex);
				}
			}
			contratto.setTot_ordini( tot_ordini );
			
			
			return contratto;		
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}	
	}
	/**
	  *  calcolo il Totale dei documenti contabili associati ai Contratti legati al contratto padre 
	  *    PreCondition:
	  *   	 Viene richiesta la visualizzazione del totale dei documenti contabili associati 
	  *		 ai Contratti legati al contratto padre
	  *    PostCondition:
	  *      L'utente può visualizzare il totale dei documenti 
	  *
	  * @param userContext lo user context
	  * @param contratto l'istanza di  <code>ContrattoBulk</code>
	  */
	private ContrattoBulk calcolaTotDocContForPadre (UserContext userContext,ContrattoBulk contratto) throws ComponentException
	{
		try
		{
			if(contratto == null)
			   return contratto;
			if(contratto.getPg_contratto()==null)
			   return contratto;			   
			Integer esercizio = ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio();
			ContrattoHome testataHome = (ContrattoHome)getHome(userContext, ContrattoBulk.class);
			
			SQLBuilder sqlPagSpeNetto,sqlEtr,sqlSpe, sqlLiqEtr,sqlLiqSpe,sqlPagEtr,sqlOrdini,sqlPagSpe,sqlLiqSpeNetto = null; 
			sqlEtr = testataHome.calcolaTotAccertamentiPadre( userContext, contratto);
			sqlSpe = testataHome.calcolaTotObbligazioniPadre( userContext, contratto);
			sqlLiqEtr =testataHome.calcolaTotDocumentiAttPadre(userContext, contratto);
			sqlLiqSpe =testataHome.calcolaTotDocumentiPasPadre(userContext, contratto);
			sqlPagEtr =testataHome.calcolaTotReversaliPadre(userContext, contratto);
			sqlPagSpe =testataHome.calcolaTotMandatiPadre(userContext, contratto);
			sqlPagSpeNetto =testataHome.calcolaTotMandatiPadreNetto(userContext, contratto);
			sqlLiqSpeNetto=testataHome.calcolaTotDocumentiPasNettoPadre(userContext, contratto);
			sqlOrdini = testataHome.calcolaTotOrdiniPadre( userContext, contratto);
			
			java.math.BigDecimal tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_etr( tot_doc_cont );
			contratto.setTot_doc_cont_spe( tot_doc_cont );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlEtr.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_etr( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlSpe.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_spe( tot_doc_cont );			
			
			tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_etr( tot_doc_cont );
			contratto.setTot_docamm_cont_spe( tot_doc_cont );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlLiqEtr.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_etr( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlLiqSpe.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_spe( tot_doc_cont );			
			
			tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_etr( tot_doc_cont );
			contratto.setTot_doccont_cont_spe( tot_doc_cont );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlPagEtr.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_etr( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlPagSpe.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_spe( tot_doc_cont );			

			java.math.BigDecimal tot_ordini = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_ordini( tot_ordini );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlOrdini.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_ordini = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_ordini == null)
				tot_ordini = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_ordini( tot_ordini );
			
			
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlPagSpeNetto.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_spe_netto(tot_doc_cont);	
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlLiqSpeNetto.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_spe_netto( tot_doc_cont );			
			

			return contratto;		
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}	
	}	
	/**
	  *  calcolo il Totale dei documenti contabili associati al Contratto sia Attivi che Passivi
	  *    PreCondition:
	  *   	 Viene richiesta la visualizzazione del totale dei documenti contabili associati 
	  *		 al contratto
	  *    PostCondition:
	  *      L'utente può visualizzare il totale dei documenti 
	  *
	  * @param userContext lo user context
	  * @param contratto l'istanza di  <code>ContrattoBulk</code>
	  */
	private ContrattoBulk calcolaTotDocContForAttivoPassivo (UserContext userContext,ContrattoBulk contratto) throws ComponentException
	{
		try
		{
			if(contratto == null)
			   return contratto;
			if(contratto.getPg_contratto()==null)
			   return contratto;			   
			Integer esercizio = ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio();
			ContrattoHome testataHome = (ContrattoHome)getHome(userContext, ContrattoBulk.class);
			SQLBuilder sqlEtr,sqlSpe,sqlLiqEtr,sqlLiqSpe,sqlPagEtr,sqlPagSpe,sqlOrdini,sqlPagSpeNetto,sqlLiqSpeNetto = null;
			sqlEtr = testataHome.calcolaTotAccertamenti( userContext, contratto);
			sqlSpe = testataHome.calcolaTotObbligazioni( userContext, contratto);
			sqlLiqEtr =testataHome.calcolaTotDocumentiAtt(userContext, contratto);
			sqlLiqSpe =testataHome.calcolaTotDocumentiPas(userContext, contratto);
			sqlPagEtr =testataHome.calcolaTotReversali(userContext, contratto);
			sqlPagSpe =testataHome.calcolaTotMandati(userContext, contratto);
			sqlOrdini =testataHome.calcolaTotOrdini(userContext, contratto);
			sqlPagSpeNetto =testataHome.calcolaTotMandati(userContext, contratto);
			sqlLiqSpeNetto =testataHome.calcolaTotDocumentiPasNetto(userContext, contratto);
		
			java.math.BigDecimal tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_etr( tot_doc_cont );
			contratto.setTot_doc_cont_spe( tot_doc_cont );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlEtr.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_etr( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlSpe.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doc_cont_spe( tot_doc_cont );		
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlLiqEtr.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_etr( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlLiqSpe.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_spe( tot_doc_cont );		
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlPagEtr.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_etr( tot_doc_cont );
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlPagSpe.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_spe( tot_doc_cont );		
			
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlPagSpeNetto.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_doccont_cont_spe_netto( tot_doc_cont );	

			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlLiqSpeNetto.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_doc_cont = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_doc_cont == null)
			  tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_docamm_cont_spe_netto( tot_doc_cont );	

			java.math.BigDecimal tot_ordini = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_ordini( tot_ordini );
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sqlOrdini.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						tot_ordini = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}	
			if (tot_ordini == null)
				tot_ordini = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			contratto.setTot_ordini( tot_ordini );
			
			return contratto;		
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}	
	}	
	/**
	  *
	  * Viene richiesto il salvataggio definitivo del Contratto
	  *
	  * Pre-post-conditions:
	  *
	  * Nome: Salvataggio definitivo del Contratto
	  * Pre: Viene richiesto il salvataggio definitivo del Contratto
	  * Post: Viene salvato in modo definitivo il contratto in questione e cancellato quello provvisorio
	  *
	  * @param	userContext	lo UserContext che ha generato la richiesta
	  * @param	contratto l'OggettoBulk da salvara in modo definitivo
	  * @return	il contratto definitivo
	  *
	  * Metodi privati chiamati:
	  *		esitaVariazioneBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
	  *		reloadVarBilancio(UserContext userContext, Var_bilancioBulk varBilancio);
	  *
	**/
	public ContrattoBulk salvaDefinitivo(UserContext userContext, ContrattoBulk contratto) throws ComponentException{
		try {
			ContrattoService contrattoService = SpringUtil.getBean("contrattoService",
					ContrattoService.class);
			Date dataStipulaParametri = ((Parametri_cnrBulk)getHome(userContext, Parametri_cnrBulk.class).
					findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getData_stipula_contratti();
			lockBulk(userContext, contratto);
			validaModificaConBulk(userContext, contratto);
			try {
				validaCampiObbligatori(userContext,contratto);
			}catch (IntrospectionException e) {
				throw new ComponentException(e);
			} catch (SQLException e) {
				throw new ComponentException(e);
			}
			if(contratto.getDt_inizio_validita() == null)
			  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("dt_inizio_validita").getLabel()); 
			if(contratto.getDt_fine_validita() == null)
			  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("dt_fine_validita").getLabel());
			if (!(contratto.getDt_stipula().before(dataStipulaParametri))){
				if ((contratto.isPassivo() || contratto.isAttivo_e_Passivo()) && contratto.getFl_mepa() == null) 
					  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("fl_mepa").getLabel());
				if ((contratto.isPassivo() || contratto.isAttivo_e_Passivo()) && contratto.getTipoNormaPerla() == null) 
						  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("tipoNormaPerla").getLabel());
				if (contratto.getTipo_contratto() != null && contratto.getTipo_contratto().getFl_cig() != null  && contratto.getTipo_contratto().getFl_cig() && contratto.getCig() == null)
					  throw new ApplicationException("Valorizzare "+BulkInfo.getBulkInfo(contratto.getClass()).getFieldProperty("cig").getLabel());
			}

			StorageObject storageObject = contrattoService.getFolderContratto(contratto);
			if (storageObject == null || !contrattoService.isDocumentoContrattoPresent(contratto))
				throw handleException(new ApplicationException("Bisogna allegare il file del Contratto!"));
			boolean pubblica = ((Parametri_cnrBulk)getHome(userContext, Parametri_cnrBulk.class).
					findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_pubblica_contratto().booleanValue();
		    if(pubblica){
				if (contratto.getDt_stipula().after(dataStipulaParametri) ||
					contratto.getDt_stipula().equals(dataStipulaParametri)){
					if (contratto.isPassivo() || contratto.isAttivo_e_Passivo())
						if (contratto.getTipo_contratto() != null && contratto.getTipo_contratto().getFl_pubblica_contratto() != null  && contratto.getTipo_contratto().getFl_pubblica_contratto())  
							contratto.setFl_pubblica_contratto(Boolean.TRUE);
						else
							contratto.setFl_pubblica_contratto(Boolean.FALSE);
					else
						contratto.setFl_pubblica_contratto(Boolean.FALSE);					
				}
				else
					contratto.setFl_pubblica_contratto(Boolean.FALSE);
		    }else 
		    	contratto.setFl_pubblica_contratto(Boolean.FALSE);
		    if (contratto.isPassivo() && contratto.getFl_pubblica_contratto()!=null && contratto.getFl_pubblica_contratto())  
					if (contratto.getDitteInvitate()!=null && contratto.getDitteInvitate().size()==0 )
						throw handleException(new ApplicationException("Bisogna indicare le ditte invitate!"));

		    ContrattoBulk contrattoClone = (ContrattoBulk)contratto.clone();
			try {
				it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
					(it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
						it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
																		it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
				contrattoClone.setPg_contratto(
					 numerazione.creaNuovoProgressivo(userContext,contrattoClone.getEsercizio(), "CONTRATTO", "PG_CONTRATTO_DEFINITIVO", CNRUserContext.getUser(userContext))
				);
			}catch(it.cnr.jada.bulk.BusyResourceException e) {
				throw new ApplicationException(e);
			}catch(Throwable e) {
				throw new ApplicationException(e);
			}
			/* Elimino il contratto provvisorio */
			contratto.setCrudStatus(OggettoBulk.TO_BE_DELETED);
			
			for (java.util.Iterator j=contratto.getDitteInvitate().iterator();j.hasNext();){
					Ass_contratto_ditteBulk ass_contratto_ditte = (Ass_contratto_ditteBulk)j.next();
					if(ass_contratto_ditte.getCrudStatus()== OggettoBulk.NORMAL)
						ass_contratto_ditte.setCrudStatus(OggettoBulk.TO_BE_DELETED);
			}		
			
			for (java.util.Iterator j=contratto.getAssociazioneUO().iterator();j.hasNext();){
				Ass_contratto_uoBulk ass_contratto_uo = (Ass_contratto_uoBulk)j.next();
				if(ass_contratto_uo.getCrudStatus()== OggettoBulk.NORMAL)
					ass_contratto_uo.setCrudStatus(OggettoBulk.TO_BE_DELETED);
			}			
			super.eliminaConBulk(userContext,contratto);
			/* Inserisco il nuovo contratto 
			   Recupero le uo associate al contratto e le lego a quello definitivo */		
			for (java.util.Iterator j=contrattoClone.getAssociazioneUO().iterator();j.hasNext();){
				Ass_contratto_uoBulk ass_contratto_uo = (Ass_contratto_uoBulk)j.next();
				ass_contratto_uo.setContratto(contrattoClone);
				ass_contratto_uo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			}
			for (java.util.Iterator j=contrattoClone.getDitteInvitate().iterator();j.hasNext();){
				Ass_contratto_ditteBulk ass_contratto_ditte = (Ass_contratto_ditteBulk)j.next();
				ass_contratto_ditte.setContratto(contrattoClone);
				ass_contratto_ditte.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			}
			contrattoClone.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			contrattoClone.setStato(ContrattoBulk.STATO_DEFINITIVO);

			ContrattoBulk contrattoDefinitivo = (ContrattoBulk)super.creaConBulk(userContext,contrattoClone);
			if (storageObject != null){
				contrattoService.changeProgressivoNodeRef(storageObject, contrattoDefinitivo);
				StorageObject storageObject1 = contrattoService.getFolderContratto(contrattoDefinitivo);
				if (storageObject1 != null){
					contrattoService.addAspect(storageObject1, "P:sigla_contratti_aspect:stato_definitivo");
					contrattoService.addConsumer(storageObject1,"GROUP_CONTRATTI");
					contrattoService.setInheritedPermission(
							contrattoService.getStorageObjectByPath(contrattoService.getCMISPathFolderContratto(contrattoDefinitivo)),
							Boolean.FALSE);
				}
			}
			return contrattoDefinitivo;
		} catch (it.cnr.jada.persistency.PersistencyException e) {
		 throw handleException(contratto,e);
		} catch (it.cnr.jada.bulk.OutdatedResourceException e) {
		 throw handleException(contratto,e);
		} catch (it.cnr.jada.bulk.BusyResourceException e) {
		 throw handleException(contratto,e);
		}
	}

	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_elenco_contrattiBulk)
			inizializzaBulkPerStampa(userContext, (Stampa_elenco_contrattiBulk)bulk);
		
		return bulk;
	}	
	
	public OggettoBulk inizializzaBulkPerStampa(UserContext userContext, Stampa_elenco_contrattiBulk stampa) 
			throws ComponentException {
	
			stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			
			
			try{
					String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
					Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
					Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

					if (!uo.isUoCds()){
										stampa.setUoForPrint(uo);
										stampa.setUoForPrintEnabled(true);
					} else {
								//stampa.setUoForPrint(new Unita_organizzativaBulk());
								stampa.setUoForPrintEnabled(false);
					}
			
					} catch (it.cnr.jada.persistency.PersistencyException pe){
								throw new ComponentException(pe);
					}
						
				return stampa;		
	}	
	
		/**
		 * stampaBulkPerStampa method comment.
		 */
		
	public OggettoBulk stampaConBulk(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_elenco_contrattiBulk)
			return  stampaConBulk(aUC, (Stampa_elenco_contrattiBulk) bulk);
		return bulk;
	}


	public OggettoBulk stampaConBulk(UserContext userContext, Stampa_elenco_contrattiBulk stampa) throws ComponentException {
		if ( stampa.getstato()==null )
				throw new ApplicationException( "E'necessario scegliere uno stato");
		if ( stampa.getDataStipula_da()==null )
				throw new ApplicationException( "E'necessario inserire la Data di Stipula");
		if ( stampa.getDataStipula_a()==null )
				throw new ApplicationException( "E'necessario inserire la Data di Stipula");
		if ( stampa.getDataInizioValidita_da()==null )
				throw new ApplicationException( "E'necessario inserire la Data di Inizio Validità");
		if ( stampa.getDataInizioValidita_a()==null )
				throw new ApplicationException( "E'necessario inserire la Data di Inizio Validità");
		if ( stampa.getDataFineValidita_da()==null )
				throw new ApplicationException( "E'necessario inserire la Data di Fine Validità");
		if ( stampa.getDataFineValidita_a()==null )
				throw new ApplicationException( "E'necessario inserire la Data di Fine Validità");
		return stampa;
	}	
	
	public SQLBuilder selectUoForPrintByClause (UserContext userContext, 
		Stampa_elenco_contrattiBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
		{	
			
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
			if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
	
			SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
			sql.addClause( clause );
			return sql; 
			}
			else
			{
							
			SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
			sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			
			sql.addClause( clause );
			return sql; 
				 
			}
		}
		
		public SQLBuilder selectTipo_contrattoForPrintByClause (UserContext userContext, 
				Stampa_elenco_contrattiBulk stampa, Tipo_contrattoBulk tc, CompoundFindClause clause) throws ComponentException, PersistencyException
				{	
					SQLBuilder sql = getHome(userContext, tc.getClass()).createSQLBuilder();
					sql.addClause( clause );
					return sql;
				}
				
				
		public SQLBuilder selectTerzo_firmatarioForPrintByClause (UserContext userContext, 
				Stampa_elenco_contrattiBulk stampa, TerzoBulk terzo, CompoundFindClause clause) throws ComponentException, PersistencyException
				{	
					/*Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
					if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
					*/
						SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
						sql.addTableToHeader( "CONTRATTO" );
						sql.setDistinctClause(true);
						sql.addSQLJoin("CONTRATTO.CD_TERZO_FIRMATARIO","TERZO.CD_TERZO");
						sql.addClause( clause );
						return sql;
					
					/*}
					else
					{
					SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
					sql.addTableToHeader( "CONTRATTO" );
					sql.setDistinctClause(true);
					sql.addSQLJoin("CONTRATTO.CD_TERZO_FIRMATARIO","TERZO.CD_TERZO");
					sql.addSQLClause("AND","TERZO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,stampa.getCdUoForPrint());
					sql.addClause( clause );
					return sql;
					}*/
				}
		public SQLBuilder selectFigura_giuridicaForPrintByClause (UserContext userContext,
				Stampa_elenco_contrattiBulk stampa, TerzoBulk terzo,  CompoundFindClause clause) throws ComponentException, PersistencyException
				{	
					/* Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
					if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){ */
					
					SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
					sql.addTableToHeader( "CONTRATTO" );
					sql.setDistinctClause(true);
					sql.addSQLJoin("CONTRATTO.FIG_GIUR_EST","TERZO.CD_TERZO");
					sql.addClause( clause );
					return sql;
					
					/*}
					else
					{
						SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
						sql.addTableToHeader( "CONTRATTO" );
						sql.setDistinctClause(true);
						sql.addSQLJoin("CONTRATTO.FIG_GIUR_EST","TERZO.CD_TERZO");
						sql.addSQLClause("AND","TERZO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,stampa.getCdUoForPrint());
						sql.addClause( clause );
						return sql;
					}*/
				}
		public java.util.List findListaContrattiWS(UserContext userContext,String uo,String tipo,String query,String dominio,String tipoRicerca)throws ComponentException{
			try {		
				ContrattoHome home = (ContrattoHome)getHome(userContext,ContrattoBulk.class);
				/*SQLBuilder sql=home.createSQLBuilder();
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
				sql.addSQLClause("AND","STATO",sql.EQUALS,ContrattoBulk.STATO_DEFINITIVO);
				sql.addSQLClause("AND","ESERCIZIO",sql.LESS_EQUALS,CNRUserContext.getEsercizio(userContext));*/
				Unita_organizzativaHome uo_home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
				Unita_organizzativaBulk u_org =(Unita_organizzativaBulk)uo_home.findByPrimaryKey(new Unita_organizzativaBulk(uo));
				
				Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(userContext, Parametri_cdsBulk.class);
				Parametri_cdsBulk param_cds;
				try {
					param_cds =
						(Parametri_cdsBulk) paramHome.findByPrimaryKey(
							new Parametri_cdsBulk(
									u_org.getCd_cds(),
									((CNRUserContext)userContext).getEsercizio()));
				} catch (PersistencyException e) {
					throw new ComponentException(e);
				}
				
				SQLBuilder sql = (SQLBuilder)super.select( userContext,null,new ContrattoBulk());
				sql.addSQLClause("AND","ESERCIZIO",sql.LESS_EQUALS,CNRUserContext.getEsercizio(userContext));
				sql.openParenthesis("AND");  
				  sql.addSQLClause("AND","NATURA_CONTABILE",SQLBuilder.EQUALS, tipo);
				  sql.addSQLClause("OR","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO);
				sql.closeParenthesis();
				if(param_cds != null && param_cds.getFl_contratto_cessato().booleanValue()){
					sql.openParenthesis("AND");  
					  sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
					  sql.addSQLClause("OR","STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_CESSSATO);
					sql.closeParenthesis();		
				}	
				else  
				  sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
				// Se uo 999.000 in scrivania: visualizza tutti i contratti
				Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
				if (!(u_org.getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa()))){
				  sql.openParenthesis("AND");
					sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,u_org.getCd_unita_organizzativa());
					SQLBuilder sqlAssUo = getHome(userContext,Ass_contratto_uoBulk.class).createSQLBuilder();		   
					sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
					sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
					sqlAssUo.addSQLJoin("CONTRATTO.STATO","ASS_CONTRATTO_UO.STATO_CONTRATTO");
					sqlAssUo.addSQLClause("AND","ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,u_org.getCd_unita_organizzativa());
					sql.addSQLExistsClause("OR",sqlAssUo);
				  sql.closeParenthesis();  		 
				}
				if (dominio.equalsIgnoreCase("codice"))
					sql.addSQLClause("AND","FIG_GIUR_EST",SQLBuilder.EQUALS,query);
				else if (dominio.equalsIgnoreCase("descrizione")){					
						sql.openParenthesis("AND");
						for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
							String queryDetail = stringtokenizer.nextToken();
							if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
								if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
									sql.addSQLClause("AND","OGGETTO",SQLBuilder.CONTAINS,queryDetail);
								else{
									sql.openParenthesis("AND");
									sql.addSQLClause("OR","OGGETTO",SQLBuilder.CONTAINS,queryDetail);
									sql.addSQLClause("OR","OGGETTO",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
									sql.closeParenthesis();
								}	
							}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
								if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
									sql.openParenthesis("AND");
									  sql.addSQLClause("AND","UPPER(OGGETTO)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
									  sql.addSQLClause("OR","OGGETTO",SQLBuilder.STARTSWITH,queryDetail+" ");
									  sql.addSQLClause("OR","OGGETTO",SQLBuilder.ENDSWITH," "+queryDetail);
									sql.closeParenthesis();  
								}else{
									sql.openParenthesis("AND");
									  sql.openParenthesis("AND");
									    sql.addSQLClause("OR","UPPER(OGGETTO)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
									    sql.addSQLClause("OR","UPPER(OGGETTO)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
									  sql.closeParenthesis();
									  sql.openParenthesis("OR");							  
									    sql.addSQLClause("OR","OGGETTO",SQLBuilder.STARTSWITH,queryDetail+" ");
									    sql.addSQLClause("OR","OGGETTO",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
									  sql.closeParenthesis();  
									  sql.openParenthesis("OR");
									    sql.addSQLClause("OR","OGGETTO",SQLBuilder.ENDSWITH," "+queryDetail);
									    sql.addSQLClause("OR","OGGETTO",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
									  sql.closeParenthesis();  
									sql.closeParenthesis();  
								}
							}
						}
						sql.closeParenthesis();
						sql.addOrderBy("OGGETTO");
					}
				
				return home.fetchAll(sql);
			}catch(it.cnr.jada.persistency.PersistencyException ex){
				throw handleException(ex);
			}
		}
		public java.util.List findListaContrattiSIP(UserContext userContext,RicercaContrattoBulk bulk)throws ComponentException{
			try {		  
				ContrattoHome home = (ContrattoHome)getHome(userContext,ContrattoBulk.class);
			 	SQLBuilder sql = (SQLBuilder)super.select( userContext,null,new ContrattoBulk());
				sql.addTableToHeader("TERZO");
				sql.addSQLJoin("TERZO.CD_TERZO",SQLBuilder.EQUALS,"CONTRATTO.FIG_GIUR_EST");
				//sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
				if(bulk.getEsercizio()!=null)
					sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bulk.getEsercizio());
				else{	
					sql.addSQLClause("AND","ESERCIZIO",sql.LESS_EQUALS,bulk.getEsercizio_a());
					sql.addSQLClause("AND","ESERCIZIO",sql.GREATER_EQUALS,bulk.getEsercizio_da());
				}
				if(bulk.getStato()!=null)
					sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, bulk.getStato());
				sql.addSQLClause("AND","STATO",SQLBuilder.NOT_EQUALS, ContrattoBulk.STATO_PROVVISORIO);
				if(bulk.getId()!=null){
					sql.addSQLClause("AND","PG_CONTRATTO",SQLBuilder.EQUALS, bulk.getId());
				}
				
				if(bulk.getOggetto()!=null){
					sql.openParenthesis("AND");
					sql.addSQLClause("AND","UPPER(OGGETTO)",SQLBuilder.EQUALS,bulk.getOggetto().toUpperCase());
					sql.addSQLClause("OR","OGGETTO",SQLBuilder.CONTAINS,bulk.getOggetto());
					sql.closeParenthesis();  
				}
				if(bulk.getGiuridica()!=null){
						sql.openParenthesis("AND");
						sql.addSQLClause("AND","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,bulk.getGiuridica().toUpperCase());
   					    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,bulk.getGiuridica());
						sql.closeParenthesis();  
				}
				if(bulk.getListaUo()!=null){
					sql.openParenthesis("AND");
					for(int s=0; s<bulk.getListaUo().length; s++){
				    	 String uo_sel = bulk.getListaUo()[s];
				    	 // Se uo 999.000 in scrivania: visualizza tutti i contratti
				    	 Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
				    	 if (!(uo_sel.equals(ente.getCd_unita_organizzativa()))){
				    		sql.openParenthesis("OR");
				    		sql.addSQLClause("OR","CONTRATTO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo_sel);
				    		SQLBuilder sqlAssUo = getHome(userContext,Ass_contratto_uoBulk.class).createSQLBuilder();		   
							sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
							sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
							sqlAssUo.addSQLJoin("CONTRATTO.STATO","ASS_CONTRATTO_UO.STATO_CONTRATTO");
							sqlAssUo.addSQLClause("AND","ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo_sel);
							sql.addSQLExistsClause("OR",sqlAssUo);
							sql.closeParenthesis();
						 }			    	 
					}
					sql.closeParenthesis();
				}
				sql.addOrderBy("OGGETTO");
				return home.fetchAll(sql);
			}catch(it.cnr.jada.persistency.PersistencyException ex){
				throw handleException(ex);
			}
		}
		public RemoteIterator findListaContrattiElenco(UserContext userContext,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca) throws ComponentException {
			ContrattoHome home = (ContrattoHome)getHome(userContext,ContrattoBulk.class);
			SQLBuilder sql = home.createSQLBuilder(); 
			sql.addTableToHeader("ANAGRAFICO,TERZO,UNITA_ORGANIZZATIVA,PROCEDURE_AMMINISTRATIVE");
			sql.addSQLJoin("TERZO.CD_TERZO", "CONTRATTO.FIG_GIUR_EST");
			sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
			sql.addSQLJoin("CONTRATTO.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
			sql.addSQLJoin("CONTRATTO.CD_PROC_AMM", "PROCEDURE_AMMINISTRATIVE.CD_PROC_AMM");
			if (anno!=null)
				//sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,anno);
				sql.addSQLClause(FindClause.AND, "to_char(contratto.dt_stipula,'yyyy')", SQLBuilder.EQUALS, anno);
			sql.addClause(FindClause.AND, "fl_pubblica_contratto", SQLBuilder.EQUALS, Boolean.TRUE);
			sql.addSQLClause(FindClause.AND, "to_char(contratto.dt_fine_validita,'yyyy-mm-dd')", SQLBuilder.GREATER_EQUALS, "2013-01-01");
			if(strRicerca!=null){
				strRicerca=strRicerca.replace("'", "''");
				sql.openParenthesis(FindClause.AND);
				sql.addSQLClause(FindClause.AND,"instr(CONTRATTO.ESERCIZIO||'/'||CONTRATTO.PG_CONTRATTO,'"+strRicerca+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(CONTRATTO.CD_CIG),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(CONTRATTO.OGGETTO),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(to_char(CONTRATTO.DT_INIZIO_VALIDITA,'dd/mm/yyyy'),'"+strRicerca+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(to_char(nvl(CONTRATTO.DT_PROROGA, CONTRATTO.DT_FINE_VALIDITA),'dd/mm/yyyy'),'"+strRicerca+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(ANAGRAFICO.PARTITA_IVA),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(ANAGRAFICO.CODICE_FISCALE),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(nvl(ANAGRAFICO.RAGIONE_SOCIALE, ANAGRAFICO.COGNOME)),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(nvl(ANAGRAFICO.RAGIONE_SOCIALE, ANAGRAFICO.NOME)),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(UPPER(PROCEDURE_AMMINISTRATIVE.CODICE_ANAC),'"+strRicerca.toUpperCase()+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(to_char(nvl(IM_CONTRATTO_PASSIVO_NETTO,IM_CONTRATTO_PASSIVO), '999999999999999D99'),'"+strRicerca+"')>0");
				sql.addSQLClause(FindClause.OR,"instr(to_char(nvl(IM_CONTRATTO_PASSIVO_NETTO,IM_CONTRATTO_PASSIVO), '999G999G999G999G999D99'),'"+strRicerca +"')>0");
				sql.closeParenthesis();
			} 
			if (query!=null && query.equals("chiave")){
				// per ricerca mirata per chiave per la visualizzazione dettaglio contratto
				if(strRicerca!=null)
					sql.addSQLClause(FindClause.AND,"CONTRATTO.ESERCIZIO||'/'||CONTRATTO.PG_CONTRATTO",SQLBuilder.EQUALS,strRicerca);
			}
			if(order!=null) {
				if (order.equals("chiave"))
					sql.addOrderBy("CONTRATTO.ESERCIZIO DESC,CONTRATTO.PG_CONTRATTO DESC");
				else if (order.equals("oggetto"))
					sql.addOrderBy("CONTRATTO.OGGETTO");
				else if (order.equals("datainizio"))
					sql.addOrderBy("CONTRATTO.DT_INIZIO_VALIDITA DESC");
			} else
				sql.addOrderBy("CONTRATTO.DT_INIZIO_VALIDITA DESC");
			return iterator(userContext, sql, ContrattoBulk.class, getFetchPolicyName("find"));
		}
		
		public RemoteIterator findContrattoByCig(UserContext userContext,
				ContrattoBulk contratto, CigBulk cig) throws ComponentException {
			try {
				return iterator(userContext, selectCigByClause(userContext, contratto, cig, null), CigBulk.class, getFetchPolicyName("find"));
			} catch (PersistencyException e) {
				throw handleException(e);
			}
		}		
}
