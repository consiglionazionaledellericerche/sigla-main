
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

package it.cnr.contab.doccont00.consultazioni.bp;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_competenza_entBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_competenza_speBulk;
import it.cnr.contab.doccont00.consultazioni.ejb.ConsGAECompComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class ConsGAECompBP extends ConsultazioniBP {
	
	public static final String LIVELLO_ETR= "ETR";
	public static final String LIVELLO_SPE= "SPE";
	public static final String LIVELLO_ETRLIN= "ETRLIN";
	public static final String LIVELLO_SPELIN= "SPELIN";
	public static final String LIVELLO_LIN= "LIN";
	public static final String LIVELLO_VOC= "VOC";
	public static final String LIVELLO_VARP= "VARP";
	public static final String LIVELLO_VARM= "VARM";
	public static final String LIVELLO_IMP= "IMP";
	public static final String LIVELLO_MAN= "MAN";
	public static final String LIVELLO_ACC= "ACC";
	public static final String LIVELLO_REV= "REV";
	
	
	private String livelloConsultazione;
	private String pathConsultazione;
	private boolean flNuovoPdg = false;

	public ConsGAECompComponentSession createGAECompComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		
		   return (ConsGAECompComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsGAECompComponentSession",ConsGAECompComponentSession.class);
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
					if (this instanceof ConsGAECompEtrBP){
				   		setPathConsultazione(this.LIVELLO_ETRLIN);					
				   		setLivelloConsultazione(this.LIVELLO_ETRLIN);
					} 
					else
					{
						setPathConsultazione(this.LIVELLO_SPELIN);					
						setLivelloConsultazione(this.LIVELLO_SPELIN);
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
			
			     if (pathProvenienza == null && (livello_destinazione.equals(this.LIVELLO_ETRLIN)||livello_destinazione.equals(this.LIVELLO_SPELIN))){
					if (this instanceof ConsGAECompEtrBP){
						setPathConsultazione(this.LIVELLO_ETRLIN);
						setLivelloConsultazione(this.LIVELLO_ETRLIN);
					  }else
					  {
						setPathConsultazione(this.LIVELLO_SPELIN);
						setLivelloConsultazione(this.LIVELLO_SPELIN);
					  }					  
			   }
			     else{
					   setPathConsultazione(pathProvenienza.concat(livello_destinazione));
					   setLivelloConsultazione(livello_destinazione);					   
				   }	

			   if (!isFlNuovoPdg()) {
				   setSearchResultColumnSet(getPathConsultazione());
				   setFreeSearchSet(getPathConsultazione());
			   } else {
				   String path = getPathConsultazione().replace(this.LIVELLO_ETRLIN, this.LIVELLO_ETRLIN.concat("2"));
				   path = path.replace(this.LIVELLO_SPELIN, this.LIVELLO_SPELIN.concat("2"));
				   setSearchResultColumnSet(path);
				   setFreeSearchSet(path);
			   }

			   setTitle();
			   
			   if ((livello_destinazione.equals(this.LIVELLO_MAN))||(livello_destinazione.equals(this.LIVELLO_REV))||livello_destinazione.equals(this.LIVELLO_VARP)||livello_destinazione.equals(this.LIVELLO_VARM))
				   setMultiSelection(false);
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	
	   public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		   if (getLivelloConsultazione().equals(this.LIVELLO_ETRLIN)||(getLivelloConsultazione().equals(this.LIVELLO_SPELIN))) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.voce");
			   button.setSeparator(true);
			   listButton.addElement(button);
		   }
		   if ((this instanceof ConsGAECompEtrBP)  && getLivelloConsultazione().endsWith(this.LIVELLO_VOC)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.varpiu");
			   button.setSeparator(true);
			   listButton.addElement(button);

			   Button button1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.varmeno");
			   button1.setSeparator(true);
			   listButton.addElement(button1);
			   
			   Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.accertamenti");
			   button.setSeparator(true);
			   listButton.addElement(button2);
			   
			   Button button3 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.reversali");
			   button.setSeparator(true);
			   listButton.addElement(button3);

		   }else  if ((this instanceof ConsGAECompSpeBP) && getLivelloConsultazione().endsWith(this.LIVELLO_VOC)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.varpiu");
			   button.setSeparator(true);
			   listButton.addElement(button);

			   Button button1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.varmeno");
			   button1.setSeparator(true);
			   listButton.addElement(button1);
			   
			   Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.impegni");
			   button2.setSeparator(true);
			   listButton.addElement(button2);
			   
			   Button button3 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.mandati");
			   button3.setSeparator(true);
			   listButton.addElement(button3);

		   }
		   if ((this instanceof ConsGAECompEtrBP) && getLivelloConsultazione().endsWith(this.LIVELLO_ACC)) {
		       Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.reversali");
			   button.setSeparator(true);
			   listButton.addElement(button);
		   }
		   if ((this instanceof ConsGAECompSpeBP) && getLivelloConsultazione().endsWith(this.LIVELLO_IMP)) {
			    Button buttonLiv = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.mandati");
				buttonLiv.setSeparator(true);
				listButton.addElement(buttonLiv);		   
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
	   public boolean isPresenteLIN() {
			 return getPathConsultazione().indexOf(LIVELLO_LIN)>=0;
	   }
	   public boolean isPresenteVOC() {
			 return getPathConsultazione().indexOf(LIVELLO_VOC)>=0;
	   }
	   public boolean isPresenteVARP() {
		   return getPathConsultazione().indexOf(LIVELLO_VARP)>=0;
	   }
	   public boolean isPresenteVARM() {
		   return getPathConsultazione().indexOf(LIVELLO_VARM)>=0;
	   }
	   public boolean isPresenteIMP() {
		   return getPathConsultazione().indexOf(LIVELLO_IMP)>=0;
	   }
	   public boolean isPresenteACC() {
		   return getPathConsultazione().indexOf(LIVELLO_ACC)>=0;
	   }
	   public boolean isPresenteMAN() {
		   return getPathConsultazione().indexOf(LIVELLO_MAN)>=0;
	   }
	   public boolean isPresenteREV() {
		   return getPathConsultazione().indexOf(LIVELLO_REV)>=0;
	   }
	   /**
		* Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
		* sulla base del path della consultazione
		*/
	   public void setTitle() {
		   String title=null;
		   if (this instanceof ConsGAECompEtrBP)
			   title = "Riepilogo GAE Competenza Entrate";
		   else
			   title = "Riepilogo GAE Competenza Spese";
		    if (isPresenteVOC()) title = title.concat("- Voce");
			if (isPresenteVARP()) title = title.concat("\\Variazione Più");
			if (isPresenteVARM()) title = title.concat("\\Variazione Meno");
			if (isPresenteIMP()) title = title.concat("\\Impegno");
			if (isPresenteMAN()) title = title.concat("\\Mandato");
			if (isPresenteACC()) title = title.concat("\\Accertamento");
		   	if (isPresenteREV()) title = title.concat("\\Reversale");
		   getBulkInfo().setShortDescription(title);
		   if (this instanceof ConsGAECompEtrBP)
			   getBulkInfo().setLongDescription("Riepilogo GAE Competenza Entrate");
		   else
			   getBulkInfo().setLongDescription("Riepilogo GAE Competenza Spese");
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
			   if (this instanceof ConsGAECompEtrBP){
				   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
				   {
					   V_cons_gae_competenza_entBulk bulk = (V_cons_gae_competenza_entBulk)i.next();
					   CompoundFindClause parzclause = new CompoundFindClause();
		
					   if (isPresenteLIN()){
						   parzclause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,bulk.getCd_centro_responsabilita());
						   parzclause.addClause("AND","cd_linea_attivita",SQLBuilder.EQUALS,bulk.getCd_linea_attivita());
					   }
					   if (isPresenteVOC()) 
						   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,bulk.getCd_elemento_voce());
					   if (isPresenteVARP()) 
						   parzclause.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,bulk.getPg_variazione_pdg());
					   if (isPresenteVARM()) 
						   parzclause.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,bulk.getPg_variazione_pdg());
					   if (isPresenteACC()){ 
						   parzclause.addClause("AND","cd_cds_acc",SQLBuilder.EQUALS,bulk.getCd_cds_acc());
						   parzclause.addClause("AND","pg_accertamento",SQLBuilder.EQUALS,bulk.getPg_accertamento());
						   parzclause.addClause("AND","pg_accertamento_scadenzario",SQLBuilder.EQUALS,bulk.getPg_accertamento_scadenzario());
					   }
					   if (isPresenteREV()) {
						   parzclause.addClause("AND","cd_cds_rev",SQLBuilder.EQUALS,bulk.getCd_cds_rev());
						   parzclause.addClause("AND","pg_reversale",SQLBuilder.EQUALS,bulk.getPg_reversale());
					   }
					   clauses = clauses.or(clauses, parzclause);
				   }
			   }else{
				   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
				   {
					   V_cons_gae_competenza_speBulk bulk = (V_cons_gae_competenza_speBulk)i.next();
					   CompoundFindClause parzclause = new CompoundFindClause();
		
					   if (isPresenteLIN()){
						   parzclause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,bulk.getCd_centro_responsabilita());
						   parzclause.addClause("AND","cd_linea_attivita",SQLBuilder.EQUALS,bulk.getCd_linea_attivita());
					   }
					   if (isPresenteVOC()) 
						   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,bulk.getCd_elemento_voce());
					   if (isPresenteVARP()) 
						   parzclause.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,bulk.getPg_variazione_pdg());
					   if (isPresenteVARM()) 
						   parzclause.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,bulk.getPg_variazione_pdg());
					   if (isPresenteIMP()){ 
						   parzclause.addClause("AND","cd_cds_obb",SQLBuilder.EQUALS,bulk.getCd_cds_obb());
						   parzclause.addClause("AND","pg_obbligazione",SQLBuilder.EQUALS,bulk.getPg_obbligazione());
						   parzclause.addClause("AND","pg_obbligazione_scadenzario",SQLBuilder.EQUALS,bulk.getPg_obbligazione_scadenzario());
					   }
					   if (isPresenteMAN()) {
						   parzclause.addClause("AND","cd_cds_man",SQLBuilder.EQUALS,bulk.getCd_cds_man());
						   parzclause.addClause("AND","pg_mandato",SQLBuilder.EQUALS,bulk.getPg_mandato());
					   }
					   clauses = clauses.or(clauses, parzclause);
				   }
			   }
			   return clauses;
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
 
		public void setFlNuovoPdg(boolean flNuovoPdg) {
			this.flNuovoPdg = flNuovoPdg;
		}
		public boolean isFlNuovoPdg() {
			return flNuovoPdg;
		}
		public String getColumnLabelCd_commessa(){
			if (this.isFlNuovoPdg())
				return ProgettoBulk.LABEL_AREA_PROGETTUALE;
			else
				return ProgettoBulk.LABEL_COMMESSA;
		}	
		public String getFindLabelCd_commessa(){
			if (this.isFlNuovoPdg())
				return ProgettoBulk.LABEL_AREA_PROGETTUALE;
			else
				return ProgettoBulk.LABEL_COMMESSA;
		}	
		public String getColumnLabelCd_modulo(){
			if (this.isFlNuovoPdg())
				return ProgettoBulk.LABEL_PROGETTO;
			else
				return ProgettoBulk.LABEL_MODULO;
		}	
		public String getFindLabelCd_modulo(){
			if (this.isFlNuovoPdg())
				return ProgettoBulk.LABEL_PROGETTO;
			else
				return ProgettoBulk.LABEL_MODULO;
		}	
		public String getColumnLabelDs_commessa(){
			if (this.isFlNuovoPdg())
				return "Desc. ".concat(ProgettoBulk.LABEL_AREA_PROGETTUALE);
			else
				return "Desc. ".concat(ProgettoBulk.LABEL_COMMESSA);
		}	
		public String getFindLabelDs_commessa(){
			if (this.isFlNuovoPdg())
				return "Desc. ".concat(ProgettoBulk.LABEL_AREA_PROGETTUALE);
			else
				return "Desc. ".concat(ProgettoBulk.LABEL_COMMESSA);
		}	
		public String getColumnLabelDs_modulo(){
			if (this.isFlNuovoPdg())
				return "Desc. ".concat(ProgettoBulk.LABEL_PROGETTO);
			else
				return "Desc. ".concat(ProgettoBulk.LABEL_MODULO);
		}	
		public String getFindLabelDs_modulo(){
			if (this.isFlNuovoPdg())
				return "Desc. ".concat(ProgettoBulk.LABEL_PROGETTO);
			else
				return "Desc. ".concat(ProgettoBulk.LABEL_MODULO);
		}

}
