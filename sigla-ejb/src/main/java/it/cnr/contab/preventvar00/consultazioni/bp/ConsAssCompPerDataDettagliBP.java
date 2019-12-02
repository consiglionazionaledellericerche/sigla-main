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

package it.cnr.contab.preventvar00.consultazioni.bp;

import java.util.Iterator;

import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstCdrGaeBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstVoceBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.preventvar00.ejb.ConsAssCompPerDataComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;


public class ConsAssCompPerDataDettagliBP extends ConsultazioniBP
{
	public Parametri_livelliBulk parametriLivelli;
	private String descrizioneClassificazione;
	private String livelloConsultazione;
	private String pathConsultazione;
	
	public static final String LIV_BASE= "BASE";
	public static final String LIV_BASEVARPIU= "VARPIU";
	public static final String LIV_BASEVARMENO= "VARMENO";
	public static final String LIV_BASESTANZ= "STANZ";
	
	
	public ConsAssCompPerDataComponentSession createConsAssCompPerDataComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsAssCompPerDataComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENTVAR00_EJB_ConsAssCompPerDataComponentSession", ConsAssCompPerDataComponentSession.class);
	}
	
	public ConsAssCompPerDataComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsAssCompPerDataComponentSession)createComponentSession("CNRPREVENTVAR00_EJB_ConsAssCompPerDataComponentSession",ConsAssCompPerDataComponentSession.class);
	}
	
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			V_cons_ass_comp_per_dataBulk assestato = (V_cons_ass_comp_per_dataBulk)oggettobulk;
			CompoundFindClause clause = new CompoundFindClause(getBaseclause(), compoundfindclause);
			return createConsAssCompPerDataComponentSession().findVariazioniDettaglio(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),compoundfindclause,assestato);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			ConsAssCompPerDataDettagliBP bp=(ConsAssCompPerDataDettagliBP)context.getBusinessProcess();
			V_cons_ass_comp_per_dataBulk bulk=(V_cons_ass_comp_per_dataBulk)bp.getModel();
			setIterator(context,createConsAssCompPerDataComponentSession().findVariazioniDettaglio(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null,bulk));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
		CompoundFindClause clauses = new CompoundFindClause();
		if (context.getBusinessProcess().getName().equals("ConsAssCompPerDataBP")){
			ConsAssCompPerDataBP bp=(ConsAssCompPerDataBP)context.getBusinessProcess();
			if(bp.getModel()!=null && bp.getModel() instanceof V_cons_ass_comp_per_dataBulk){
				V_cons_ass_comp_per_dataBulk bulk=(V_cons_ass_comp_per_dataBulk)bp.getModel();
				Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
				clauses.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
				clauses.addClause("AND","TI_GESTIONE",SQLBuilder.EQUALS,bulk.getTi_gestione());
				clauses.addClause("AND","DATA_APPROVAZIONE_VAR",SQLBuilder.LESS_EQUALS,bulk.getData_approvazione_var());
				setModel(context,bulk);
				setPathConsultazione(this.LIV_BASE);					
				setLivelloConsultazione(this.LIV_BASE);
				setBaseclause(clauses);	
				super.init(config,context);
				initVariabili(context,null,getPathConsultazione()); 
			}
//		setParametriLivelli(((ConsVarStanzCompetenzaComponentSession) createComponentSession()).findParametriLivelli(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
//		bulk.setV_classificazione_voci(liv);
	}
		
		if (context.getBusinessProcess().getName().equals("ConsAssCompPerDataDettagliBP")){
			setPathConsultazione(this.LIV_BASE);
			setBaseclause(clauses);	
			super.init(config,context);	
		}		
	}
	
	   public void initVariabili(ActionContext context, String pathProvenienza, String livello_destinazione) throws BusinessProcessException {
		   try{		
			   if ((pathProvenienza==null) && (livello_destinazione==this.LIV_BASE)){
						setPathConsultazione(this.LIV_BASE);					
						setLivelloConsultazione(this.LIV_BASE);
						setMultiSelection(true);
		   			}
			   
		   			if ((pathConsultazione==this.LIV_BASE) && (livello_destinazione.equals(this.LIV_BASEVARPIU))) {
		   				setPathConsultazione(this.LIV_BASEVARPIU);
		   				setLivelloConsultazione(this.LIV_BASEVARPIU);
		   				setMultiSelection(false);
		   			}
		   			if ((pathConsultazione==this.LIV_BASE) && (livello_destinazione.equals(this.LIV_BASEVARMENO))) {
		   				setPathConsultazione(this.LIV_BASEVARMENO);
		   				setLivelloConsultazione(this.LIV_BASEVARMENO);
		   				setMultiSelection(false);
		   			}
		   			if ((pathConsultazione==this.LIV_BASE) && (livello_destinazione.equals(this.LIV_BASESTANZ))) {
		   				setPathConsultazione(this.LIV_BASESTANZ);
		   				setLivelloConsultazione(this.LIV_BASESTANZ);
		   				setMultiSelection(false);
		   			}
		   			if (pathProvenienza!=null){
		   		 		setPathConsultazione(pathProvenienza.concat(livello_destinazione));
		   		 		setLivelloConsultazione(livello_destinazione);
		   			}
		   			
			   setSearchResultColumnSet(getPathConsultazione());
			   setFreeSearchSet(getPathConsultazione());
			   setTitle();
			
		  }
			
			 catch(Throwable e) {
			   	throw new BusinessProcessException(e);
		   }   		
	   }
	   
	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;	
		return false; 
	}	
	public String getPathConsultazione() {
		   return pathConsultazione;
	   }
	public void setPathConsultazione(String string) {
		   pathConsultazione = string;
	   }
	public String getLivelloConsultazione() {
		return livelloConsultazione;
	}
	
	public void setLivelloConsultazione(String livelloConsultazione) {
		this.livelloConsultazione = livelloConsultazione;
	}
	
	public String getPathDestinazione(String destinazione) {
		   return getPathConsultazione().concat(destinazione);
	}
	
	public void setTitle() {
		
		   String title=null;
		   		   title = "Consultazione Assestato Competenza alla Data";
			
			if (isPresenteVARPIU()) title = title.concat(" - Dettaglio Variazioni in Più");
			if (isPresenteVARMENO()) title = title.concat(" - Dettaglio Variazioni in Meno");
			if (isPresenteSTANZ()) title = title.concat(" - Dettaglio Stanziamento Iniziale");
		
			getBulkInfo().setShortDescription(title);
		}	

	public boolean isPresenteVARPIU() {
		return getPathConsultazione().indexOf(LIV_BASEVARPIU)>=0;
		}
	
	public boolean isPresenteVARMENO() {
	   return getPathConsultazione().indexOf(LIV_BASEVARMENO)>=0;
	}
	
	public boolean isPresenteSTANZ() {
		   return getPathConsultazione().indexOf(LIV_BASESTANZ)>=0;
		}
	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	
	public CompoundFindClause getSelezione(ActionContext context) throws BusinessProcessException {
		   try	{
			   CompoundFindClause clauses = null;
			   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
			   {
			   	V_cons_ass_comp_per_dataBulk wpb = (V_cons_ass_comp_per_dataBulk)i.next();
				   CompoundFindClause parzclause = new CompoundFindClause();
				
				   		parzclause.addClause("AND","cd_dipartimento",SQLBuilder.EQUALS,wpb.getCd_dipartimento());
				   		parzclause.addClause("AND","cd_livello1",SQLBuilder.EQUALS,wpb.getCd_livello1());
				   		parzclause.addClause("AND","cd_livello2",SQLBuilder.EQUALS,wpb.getCd_livello2());
				 
				   clauses = clauses.or(clauses, parzclause);
			   }
			   return clauses;
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	}
	
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		if (getLivelloConsultazione()!=null){
		if (getLivelloConsultazione().equals(this.LIV_BASE)) {
	  		Button button = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.consdettpiu");
			button.setSeparator(true);
			listButton.addElement(button);
			Button button2 = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.consdettmeno");
			button2.setSeparator(true);
			listButton.addElement(button2);
			Button button3 = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.consdettstanz");
			button2.setSeparator(true);
			listButton.addElement(button3);
		}
		}
			return listButton;
	   }
}
