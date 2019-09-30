
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

package it.cnr.contab.prevent01.consultazioni.bp;

import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgp_titBulk;
import it.cnr.contab.prevent01.consultazioni.ejb.ConsPDGPTitComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class ConsPDGPTitBP extends ConsultazioniBP {
	public static final String LIVELLO_ETRLIV1= "ETRLIV1";
	public static final String LIVELLO_SPELIV1= "SPELIV1";
	public static final String LIVELLO_CDR= "CDR";
	public static final String LIVELLO_LIV1= "LIV1";
	public static final String LIVELLO_LIV2= "LIV2";
	public static final String LIVELLO_LIV3= "LIV3";
	public static final String LIVELLO_DET= "DET";
	
	private String livelloConsultazione;
	private String pathConsultazione;
	private String ds_livello1;
	private String ds_livello2;
	private String ds_livello3;
	private String anno_corrente,anno_successivo,anno_successivo_successivo;
	private boolean flNuovoPdg = false;

	public ConsPDGPTitComponentSession createPdgpTitComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		
		   return (ConsPDGPTitComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_ConsPDGPTitComponentSession", ConsPDGPTitComponentSession.class);
	}

	   public Classificazione_vociComponentSession createClassificazioneVociComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		   return (Classificazione_vociComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Classificazione_vociComponentSession",Classificazione_vociComponentSession.class);
	   }

	   protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		   try {
			   Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
			   Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), esercizio); 
			   setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
	
			   CompoundFindClause clauses = new CompoundFindClause();
			   clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
			   setBaseclause(clauses);
			
			   if (getPathConsultazione()==null) {
					if (this instanceof ConsPDGPTitEtrBP){
						setPathConsultazione(this.LIVELLO_ETRLIV1);					
						setLivelloConsultazione(this.LIVELLO_ETRLIV1);
					} 
					else
					{
						setPathConsultazione(this.LIVELLO_SPELIV1);					
						setLivelloConsultazione(this.LIVELLO_SPELIV1);
					} 
				
					super.init(config,context);
					initVariabili(context, null,getPathConsultazione());   
			   }
			} catch (ComponentException e) {
				throw new BusinessProcessException(e);
			} catch (RemoteException e) {
				throw new BusinessProcessException(e);
			}			   
	   }
	   public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
		   try {
			
				 if (pathProvenienza == null && (livello_destinazione.equals(this.LIVELLO_SPELIV1)||livello_destinazione.equals(this.LIVELLO_ETRLIV1))){
					if (this instanceof ConsPDGPTitEtrBP){
						setPathConsultazione(this.LIVELLO_ETRLIV1);
						setLivelloConsultazione(this.LIVELLO_ETRLIV1);
					  }else
					  {
						setPathConsultazione(this.LIVELLO_SPELIV1);
						setLivelloConsultazione(this.LIVELLO_SPELIV1);
					  }					  
			   }
			   else
			   {
				   if (this.isFlNuovoPdg() && "DET".equals(livello_destinazione)) {
					   setPathConsultazione(pathProvenienza.concat(livello_destinazione).concat("NEW"));
					   setLivelloConsultazione(livello_destinazione.concat("NEW"));
				   } else {
					   setPathConsultazione(pathProvenienza.concat(livello_destinazione));
					   setLivelloConsultazione(livello_destinazione);
				   }
			   }
		
			   setSearchResultColumnSet(getPathConsultazione());
			   setFreeSearchSet(getPathConsultazione());
			   setTitle();
			   setDs_livello1(getDs_livello1(context.getUserContext()));
			   setDs_livello2(getDs_livello2(context.getUserContext()));
			   setDs_livello3(getDs_livello3(context.getUserContext()));
			   
			

			 anno_corrente = CNRUserContext.getEsercizio(context.getUserContext()).toString();
			 anno_successivo = new Integer(CNRUserContext.getEsercizio(context.getUserContext()).intValue() + 1).toString();
			 anno_successivo_successivo = new Integer(CNRUserContext.getEsercizio(context.getUserContext()).intValue() + 2).toString();
			
			   if (livello_destinazione.equals(this.LIVELLO_DET))
				   setMultiSelection(false);
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	
	   public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		   if (getLivelloConsultazione().equals(this.LIVELLO_ETRLIV1)||getLivelloConsultazione().equals(this.LIVELLO_SPELIV1)) {
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello2");
				button.setLabel(getDs_livello2());
				button.setSeparator(true);
				
				listButton.addElement(button);
				Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cdr");
			   	button2.setSeparator(true);
			   	listButton.addElement(button2);
		   }
			if (getLivelloConsultazione().equals(this.LIVELLO_CDR)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
					   button.setSeparator(true);
					   listButton.addElement(button);
			}
		   	
		   	if (getLivelloConsultazione().equals(this.LIVELLO_LIV1)) {
				Button buttonLiv2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello2");
				buttonLiv2.setLabel(getDs_livello2());
				buttonLiv2.setSeparator(true);
				listButton.addElement(buttonLiv2);		   
			}

		   	if (getLivelloConsultazione().equals(this.LIVELLO_LIV2)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello3");
			   button.setLabel(getDs_livello3());
			   button.setSeparator(true);
			   listButton.addElement(button);
		   }
			if (getLivelloConsultazione().equals(this.LIVELLO_LIV3)) {
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cdr");
				button.setSeparator(true);
				listButton.addElement(button);
			}

		   return listButton;
	   }	

	   public String getLivelloConsultazione() {
		   return livelloConsultazione;
	   }
	   public void setLivelloConsultazione(String string) {
		   livelloConsultazione = string;
	   }
	   public String getPathConsultazione() {
		   return pathConsultazione;
	   }
	   public void setPathConsultazione(String string) {
		   pathConsultazione = string;
	   }
	   public String getPathDestinazione(String destinazione) {
		   return getPathConsultazione().concat(destinazione);
	   }
		public boolean isPresenteCDR() {
		   return getPathConsultazione().indexOf(LIVELLO_CDR)>=0;
	   }
	   
		public boolean isPresenteLIV1() {
		   return getPathConsultazione().indexOf(LIVELLO_LIV1)>=0;
	  }
	  public boolean isPresenteLIV2() {
		   return getPathConsultazione().indexOf(LIVELLO_LIV2)>=0;
	   }
	  public boolean isPresenteLIV3() {
			  return getPathConsultazione().indexOf(LIVELLO_LIV3)>=0;
	 }
	   
	   public boolean isPresenteDET() {
		   return getPathConsultazione().indexOf(LIVELLO_DET)>=0;
	   }
	   /**
		* Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
		* sulla base del path della consultazione
		*/
	   public void setTitle() {
		   String title=null;
		   if (this instanceof ConsPDGPTitEtrBP)
			   title = "Riepilogo PDGP per Titolo Entrate";
		   else
			   title = "Riepilogo PDGP per Titolo Spese";
			
			
			
			if (isPresenteLIV1()) title = title.concat("- Titolo");
			if (isPresenteCDR()) title = title.concat("\\CdR");
			if (isPresenteLIV2()) title = title.concat("\\Categoria");
			if (isPresenteLIV3()) title = title.concat("\\").concat(getDs_livello3());
			if (isPresenteDET()) title = title.concat("\\Dettagli");
		   getBulkInfo().setShortDescription(title);
		   if (this instanceof ConsPDGPTitEtrBP)
			   getBulkInfo().setLongDescription("Riepilogo PDGP Entrate");
		   else
			   getBulkInfo().setLongDescription("Riepilogo PDGP Spese");
	   }
	   public String getDs_livello1() {
		   return ds_livello1;
	   }
	   public String getDs_livello2() {
		   return ds_livello2;
	   }
		public String getDs_livello3() {
			 return ds_livello3;
		}
	   public void setDs_livello1(String string) {
		   ds_livello1 = string;
	   }
	   public void setDs_livello2(String string) {
		   ds_livello2 = string;
	   }
	  public void setDs_livello3(String string) {
			   ds_livello3 = string;
	   }
	 
	   /**
		* Ritorna la descrizione del primo livello della classificazione ufficiale
		*
		* @param userContext il context di riferimento 
		* @return String la descrizione del livello richiesto 
		*/
	   public String getDs_livello1(UserContext userContext) throws BusinessProcessException {
		   try {
			   if (getDs_livello1()==null) {
				   if ( this instanceof ConsPDGPTitSpeBP)
					   setDs_livello1(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_SPESE,
																											  new Integer(Classificazione_vociHome.LIVELLO_PRIMO)));
				   else
					   setDs_livello1(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_ENTRATE,
																											  new Integer(Classificazione_vociHome.LIVELLO_PRIMO)));
			   }
			   return getDs_livello1();
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	   /**
		* Ritorna la descrizione del secondo livello della classificazione ufficiale
		*
		* @param userContext il context di riferimento 
		* @return String la descrizione del livello richiesto 
		*/
	   public String getDs_livello2(UserContext userContext) throws it.cnr.jada.action.BusinessProcessException {
		   try {
			   if (getDs_livello2()==null) {
				   if (this instanceof ConsPDGPTitSpeBP)
					   setDs_livello2(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_SPESE,
																											  new Integer(Classificazione_vociHome.LIVELLO_SECONDO)));
				   else
					   setDs_livello2(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_ENTRATE,
																											  new Integer(Classificazione_vociHome.LIVELLO_SECONDO)));
			   }
			   return getDs_livello2();
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	/**
		  * Ritorna la descrizione del terzo livello della classificazione ufficiale
		  *
		  * @param userContext il context di riferimento 
		  * @return String la descrizione del livello richiesto 
		  */
		 public String getDs_livello3(UserContext userContext) throws it.cnr.jada.action.BusinessProcessException {
			 try {
				 if (getDs_livello3()==null) {
					 if (this instanceof ConsPDGPTitSpeBP)
						 setDs_livello3(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																												CNRUserContext.getEsercizio(userContext),
																												Elemento_voceHome.GESTIONE_SPESE,
																												new Integer(Classificazione_vociHome.LIVELLO_TERZO)));
					 else
						 setDs_livello3(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																												CNRUserContext.getEsercizio(userContext),
																												Elemento_voceHome.GESTIONE_ENTRATE,
																												new Integer(Classificazione_vociHome.LIVELLO_TERZO)));
				 }
				 return getDs_livello3();
			 }catch(Throwable e) {
				 throw new BusinessProcessException(e);
			 }
		 }
	
	   /**
		* Ritorna la CompoundFindClause ottenuta in base alla selezione effettuata
		*
		* @param field il campo da aggiornare 
		* @param label il nuovo valore da sostituire al vecchio
		*/
	   public CompoundFindClause getSelezione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		   try	{
			   CompoundFindClause clauses = null;
			   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
			   {
				   V_cons_pdgp_titBulk wpb = (V_cons_pdgp_titBulk)i.next();
				   CompoundFindClause parzclause = new CompoundFindClause();
	
				   if (isPresenteCDR()) 
					   parzclause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,wpb.getCd_centro_responsabilita());
					if (isPresenteLIV1()) 
					   parzclause.addClause("AND","cd_livello1",SQLBuilder.EQUALS,wpb.getCd_livello1());
				   if (isPresenteLIV2()) 
					   parzclause.addClause("AND","cd_livello2",SQLBuilder.EQUALS,wpb.getCd_livello2());
				   if (isPresenteLIV3()) 
  					   parzclause.addClause("AND","cd_livello3",SQLBuilder.EQUALS,wpb.getCd_livello3());
				   if (isPresenteDET()) 
					   parzclause.addClause("AND","cd_classificazione",SQLBuilder.EQUALS,wpb.getCd_classificazione());
	
				   clauses = clauses.or(clauses, parzclause);
			   }
			   return clauses;
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	}
	public String getLabelCd_livello1(){
			return ds_livello1;
	}
	public String getLabelCd_livello2(){
			return ds_livello2;
	}
	public String getLabelCd_livello3(){
		return ds_livello3;
	}
	public String getColumnLabelCd_livello1(){
				return ds_livello1;
	}
	public String getColumnLabelCd_livello2(){
				return ds_livello2;
	}	
	public String getColumnLabelCd_livello3(){
			return ds_livello3;
	}
	public String getLabelTot_ent_aree_a1(){
		return "Previsione entrate area "+ anno_corrente;
	}
	public String getLabelTot_ent_aree_a2(){
		return "Previsione entrate area "+ anno_successivo;
	}
	public String getLabelTot_ent_aree_a3(){
		return "Previsione entrate area "+ anno_successivo_successivo;
	}
	public String getLabelTot_ent_ist_a1(){
		return "Previsione entrate istituto "+ anno_corrente;
	}
	public String getLabelTot_ent_ist_a2(){
		return "Previsione entrate istituto "+ anno_successivo;
	}
	public String getLabelTot_ent_ist_a3(){
		return "Previsione entrate istituto "+ anno_successivo_successivo;
	}
	public String getLabelTot_ent_a1(){
		return "Tot. Previsione "+ anno_corrente;
	}
	public String getLabelTot_ent_a2(){
		return "Tot. Previsione "+ anno_successivo;
	}
	public String getLabelTot_ent_a3(){
		return "Tot. Previsione "+ anno_successivo_successivo;
	}
	public String getLabelIm_prev_a2(){
		return "Previsione "+ anno_successivo;
	}	
	public String getLabelIm_prev_a3(){
		return "Previsione "+ anno_successivo_successivo;
	}
	public String getColumnLabelIm_prev_a2(){
		return "Previsione "+ anno_successivo;
	}	
	public String getColumnLabelIm_prev_a3(){
		return "Previsione "+ anno_successivo_successivo;
	}			
	public String getHeaderLabelTot_ent_aree_a1(){
		return "Previsione "+ anno_corrente;
	}
	public String getHeaderLabelTot_ent_aree_a2(){
		return "Previsione "+ anno_successivo;
	}
	public String getHeaderLabelTot_ent_aree_a3(){
		return "Previsione "+ anno_successivo_successivo;
	}	
	public String getHeaderLabelTot_ent_ist_a1(){
		return "Previsione "+ anno_corrente;
	}
	public String getHeaderLabelTot_ent_ist_a2(){
		return "Previsione "+ anno_successivo;
	}
	public String getHeaderLabelTot_ent_ist_a3(){
		return "Previsione "+ anno_successivo_successivo;
	}	
	public String getHeaderLabelTot_ent_a1(){
		return "Tot. Previsione "+ anno_corrente;
	}
	public String getHeaderLabelTot_ent_a2(){
		return "Tot. Previsione "+ anno_successivo;
	}
	public String getHeaderLabelTot_ent_a3(){
		return "Tot. Previsione "+ anno_successivo_successivo;
	}	
	public String getHeaderLabelIm_prev_a2(){
		return "Previsione "+ anno_successivo;
	}	
	public String getHeaderLabelIm_prev_a3(){
		return "Previsione "+ anno_successivo_successivo;
	}	
	public String getHeaderLabelIm_dec_ist_int(){
		return "Gestione Decentrata Istituto "+ anno_corrente;
	}
	public String getHeaderLabelIm_dec_ist_est(){
		return "Gestione Decentrata Istituto "+ anno_corrente;
	}
	public String getHeaderLabelIm_dec_area_int(){
		return "Gestione Decentrata Area "+ anno_corrente;
	}
	public String getHeaderLabelIm_dec_area_est(){
		return "Gestione Decentrata Area "+ anno_corrente;
	}
	public String getHeaderLabelImp_tot_dec_int(){
		return "Gestione Decentrata "+ anno_corrente;
	}
	public String getHeaderLabelImp_tot_dec_est(){
		return "Gestione Decentrata "+ anno_corrente;
	}
	public String getHeaderLabelImp_tot_decentrato(){
		return "Gestione Decentrata "+ anno_corrente;
	}
	public String getHeaderLabelTratt_econ_int(){
		return "Gestione Accentrata "+anno_corrente+" Trattamento Economico";
	}
	public String getHeaderLabelTratt_econ_est(){
		return "Gestione Accentrata "+anno_corrente+" Trattamento Economico";
	}
	public String getHeaderLabelIm_acc_altre_sp_int(){
		return "Gestione Accentrata "+anno_corrente+" altre Spese";
	}
	public String getHeaderLabelImp_tot_comp_int(){
		return "Totale Competenza "+ anno_corrente;
	}	
	public String getHeaderLabelImp_tot_comp_est(){
		return "Totale Competenza "+ anno_corrente;
	}
	public String getHeaderLabelIm_costi_generali(){
		return "Costi "+ anno_corrente;
	}	
	public String getHeaderLabelIm_costi_figurativi(){
		return "Costi "+ anno_corrente;
	}	

	public void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
	public String getColumnLabelCd_progetto(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_AREA_PROGETTUALE;
		else
			return ProgettoBulk.LABEL_PROGETTO;
	}	
	public String getFindLabelCd_progetto(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_AREA_PROGETTUALE;
		else
			return ProgettoBulk.LABEL_PROGETTO;
	}	
	public String getColumnLabelCd_commessa(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_PROGETTO;
		else
			return ProgettoBulk.LABEL_COMMESSA;
	}	
	public String getFindLabelCd_commessa(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_PROGETTO;
		else
			return ProgettoBulk.LABEL_COMMESSA;
	}	
	public String getColumnLabelDs_progetto(){
		if (this.isFlNuovoPdg())
			return "Desc. ".concat(ProgettoBulk.LABEL_AREA_PROGETTUALE);
		else
			return "Desc. ".concat(ProgettoBulk.LABEL_PROGETTO);
	}	
	public String getFindLabelDs_progetto(){
		if (this.isFlNuovoPdg())
			return "Desc. ".concat(ProgettoBulk.LABEL_AREA_PROGETTUALE);
		else
			return "Desc. ".concat(ProgettoBulk.LABEL_PROGETTO);
	}	
	public String getColumnLabelDs_commessa(){
		if (this.isFlNuovoPdg())
			return "Desc. ".concat(ProgettoBulk.LABEL_PROGETTO);
		else
			return "Desc. ".concat(ProgettoBulk.LABEL_COMMESSA);
	}	
	public String getFindLabelDs_commessa(){
		if (this.isFlNuovoPdg())
			return "Desc. ".concat(ProgettoBulk.LABEL_PROGETTO);
		else
			return "Desc. ".concat(ProgettoBulk.LABEL_COMMESSA);
	}	
}
