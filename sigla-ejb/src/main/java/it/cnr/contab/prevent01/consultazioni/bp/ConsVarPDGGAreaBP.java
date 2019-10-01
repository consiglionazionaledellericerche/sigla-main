
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

import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_var_pdgg_areaBulk;
import it.cnr.contab.prevent01.consultazioni.ejb.ConsVarPDGGAreaComponentSession;
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

public class ConsVarPDGGAreaBP extends ConsultazioniBP {
	public static final String LIVELLO_ETR= "ETR";
	public static final String LIVELLO_SPE= "SPE";
	public static final String LIVELLO_ETRCDR= "ETRCDR";
	public static final String LIVELLO_SPECDR= "SPECDR";
	
	public static final String LIVELLO_CDR= "CDR";
	public static final String LIVELLO_AREA= "AREA";
	public static final String LIVELLO_MOD= "MOD";
	public static final String LIVELLO_LIV1= "LIV1";
	public static final String LIVELLO_LIV2= "LIV2";
	public static final String LIVELLO_LIV3= "LIV3";
	public static final String LIVELLO_LIN= "LIN";
	public static final String LIVELLO_VOC= "VOC";
	public static final String LIVELLO_DET= "DET";
	
	private String livelloConsultazione;
	private String pathConsultazione;
	private String ds_livello1;
	private String ds_livello2;
	private String ds_livello3;
	
	public ConsVarPDGGAreaComponentSession createVarPdggAreaComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		
		   return (ConsVarPDGGAreaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_ConsVarPDGGAreaComponentSession",ConsVarPDGGAreaComponentSession.class);
	}

	   public Classificazione_vociComponentSession createClassificazioneVociComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		   return (Classificazione_vociComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Classificazione_vociComponentSession",Classificazione_vociComponentSession.class);
	   }

	   protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		   Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
			
		   CompoundFindClause clauses = new CompoundFindClause();
		   clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
		 
		
		   if (getPathConsultazione()==null) {
				if (this instanceof ConsVarPDGGAreaEtrBP){
			   		setPathConsultazione(this.LIVELLO_ETRCDR);					
			   		setLivelloConsultazione(this.LIVELLO_ETRCDR);
			   		clauses.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
				} 
				else
				{
					setPathConsultazione(this.LIVELLO_SPECDR);					
					setLivelloConsultazione(this.LIVELLO_SPECDR);
					clauses.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
				} 
				
				setBaseclause(clauses);
			   	super.init(config,context);
				initVariabili(context, null,getPathConsultazione());   
		   }	 		
	   }
	   public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
		   try {
			
			     if (pathProvenienza == null && (livello_destinazione.equals(this.LIVELLO_ETRCDR)||livello_destinazione.equals(this.LIVELLO_SPECDR))){
					if (this instanceof ConsVarPDGGAreaEtrBP){
						setPathConsultazione(this.LIVELLO_ETRCDR);
						setLivelloConsultazione(this.LIVELLO_ETRCDR);
					  }else
					  {
						setPathConsultazione(this.LIVELLO_SPECDR);
						setLivelloConsultazione(this.LIVELLO_SPECDR);
					  }					  
			   }
			   else
			   {
				   setPathConsultazione(pathProvenienza.concat(livello_destinazione));
				   setLivelloConsultazione(livello_destinazione);
			   }
		
			   setSearchResultColumnSet(getPathConsultazione());
			   setFreeSearchSet(getPathConsultazione());
			   setTitle();
			   setDs_livello1(getDs_livello1(context.getUserContext()));
			   setDs_livello2(getDs_livello2(context.getUserContext()));
			   setDs_livello3(getDs_livello3(context.getUserContext()));
			   if (livello_destinazione.equals(this.LIVELLO_DET))
				   setMultiSelection(false);
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	
	   public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		   if (getLivelloConsultazione().equals(this.LIVELLO_ETRCDR)||getLivelloConsultazione().equals(this.LIVELLO_SPECDR)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.area");
			   button.setSeparator(true);
			   listButton.addElement(button);
		   }
		   if (getLivelloConsultazione().equals(this.LIVELLO_AREA)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.modulo");
			   button.setSeparator(true);
			   listButton.addElement(button);

			   Button buttonLiv1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello1");
			   buttonLiv1.setLabel(getDs_livello1());
			   listButton.addElement(buttonLiv1);
		   }
		   if (getLivelloConsultazione().equals(this.LIVELLO_MOD)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello1");
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
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.linea");
			   button.setSeparator(true);
			   listButton.addElement(button);
		   }
		if (getLivelloConsultazione().equals(this.LIVELLO_LIN)){
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.voce");
			button.setSeparator(true);
			listButton.addElement(button);
		}

		if (getLivelloConsultazione().equals(this.LIVELLO_VOC)) {
			Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
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
		public boolean isPresenteLIN() {
			 return getPathConsultazione().indexOf(LIVELLO_LIN)>=0;
	   }
		public boolean isPresenteVOC() {
			 return getPathConsultazione().indexOf(LIVELLO_VOC)>=0;
	   }
	   public boolean isPresenteAREA() {
		   return getPathConsultazione().indexOf(LIVELLO_AREA)>=0;
	   }
	   
	   public boolean isPresenteMOD() {
		   return getPathConsultazione().indexOf(LIVELLO_MOD)>=0;
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
		   if (this instanceof ConsVarPDGGAreaEtrBP)
			   title = "Riepilogo Variazioni PDGG per Area/Istituto-Entrate";
		   else
			   title = "Riepilogo Variazioni PDGG per Area/Istituto-Spese";
		
		   	if (isPresenteCDR()) title = title.concat(" - CdR");
			if (isPresenteAREA()) title = title.concat("\\Area");
			if (isPresenteMOD()) title = title.concat("\\Modulo");
			if (isPresenteLIV1()) title = title.concat("\\Titolo");
			if (isPresenteLIV2()) title = title.concat("\\Categoria");
			if (isPresenteLIV3()) title = title.concat("\\").concat(getDs_livello3());
		   	if (isPresenteDET()) title = title.concat("\\Dettagli");
		   getBulkInfo().setShortDescription(title);
		   if (this instanceof ConsVarPDGGAreaEtrBP)
			   getBulkInfo().setLongDescription("Riepilogo Variazioni PDGG Entrate");
		   else
			   getBulkInfo().setLongDescription("Riepilogo Variazioni PDGG Spese");
	   }
	   public String getDs_livello1() {
		   return ds_livello1;
	   }
	   public String getDs_livello2() {
		   return ds_livello2;
	   }
	   public void setDs_livello1(String string) {
		   ds_livello1 = string;
	   }
	   public void setDs_livello2(String string) {
		   ds_livello2 = string;
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
				   if (this instanceof ConsVarPDGGAreaSpeBP)
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
				   if (this instanceof ConsVarPDGGAreaSpeBP)
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
		   * Ritorna la descrizione del secondo livello della classificazione ufficiale
		   *
		   * @param userContext il context di riferimento 
		   * @return String la descrizione del livello richiesto 
		   */
		  public String getDs_livello3(UserContext userContext) throws it.cnr.jada.action.BusinessProcessException {
			  try {
				  if (getDs_livello3()==null) {
					  if (this instanceof ConsVarPDGGAreaSpeBP)
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
				   V_cons_var_pdgg_areaBulk wpb = (V_cons_var_pdgg_areaBulk)i.next();
				   CompoundFindClause parzclause = new CompoundFindClause();
	
				   if (isPresenteCDR()){
					   if(wpb.getCd_cdr_assegnatario()!=null)
						   parzclause.addClause("AND","cd_cdr_assegnatario",SQLBuilder.EQUALS,wpb.getCd_cdr_assegnatario());
					   else
						   parzclause.addClause("AND","cd_cdr_assegnatario",SQLBuilder.ISNULL,null);
				   }
				   if (isPresenteLIV1()) 
					   parzclause.addClause("AND","cd_livello1",SQLBuilder.EQUALS,wpb.getCd_livello1());
				   if (isPresenteLIV2()) 
					   parzclause.addClause("AND","cd_livello2",SQLBuilder.EQUALS,wpb.getCd_livello2());
				   if (isPresenteLIV3()) 
						parzclause.addClause("AND","cd_livello3",SQLBuilder.EQUALS,wpb.getCd_livello3());	   
				   if (isPresenteVOC()) 
					   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,wpb.getCd_elemento_voce());
				   if (isPresenteLIN()) 
					   parzclause.addClause("AND","cd_linea_attivita",SQLBuilder.EQUALS,wpb.getCd_linea_attivita());
				   if (isPresenteMOD()) 
					   parzclause.addClause("AND","cd_modulo",SQLBuilder.EQUALS,wpb.getCd_modulo());
				   if (isPresenteAREA()) 
					   parzclause.addClause("AND","cd_cds_area",SQLBuilder.EQUALS,wpb.getCd_cds_area());
				  
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
	
	/**
	 * @return
	 */
	public String getDs_livello3() {
		return ds_livello3;
	}

	/**
	 * @param string
	 */
	public void setDs_livello3(String string) {
		ds_livello3 = string;
	}
   }
