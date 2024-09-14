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

package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_confronto_ent_speBulk;

import it.cnr.contab.doccont00.ejb.ConsConfrontoEntSpeComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;


public class ConsConfrontoEntSpeBP extends ConsultazioniBP {
	
	public static final String LIV_BASE= "BASE";
	public static final String LIV_BASEMOD= "MOD";
	public static final String LIV_BASEMODGAE= "GAE";
	public static final String LIV_BASEMODGAEVOCE= "VOCE";
	
	private String livelloConsultazione;
	private String pathConsultazione;
	private boolean flNuovoPdg = false;
	
	public ConsConfrontoEntSpeComponentSession createConsConfrontoEntSpeComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsConfrontoEntSpeComponentSession", ConsConfrontoEntSpeComponentSession.class);
	}

	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			CompoundFindClause clause = new CompoundFindClause(getBaseclause(), compoundfindclause);
			return createConsConfrontoEntSpeComponentSession().findConsultazioneModulo(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),compoundfindclause);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			setIterator(context,createConsConfrontoEntSpeComponentSession().findConsultazioneModulo(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	
	protected void init(it.cnr.jada.action.Config config,ActionContext context) throws BusinessProcessException {
		try {
			Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), esercizio); 
			setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());

			String cds = CNRUserContext.getCd_cds(context.getUserContext());
			CompoundFindClause clauses = new CompoundFindClause();
			String uo_scrivania = CNRUserContext.getCd_unita_organizzativa(context.getUserContext())+"%";
		  
		    Unita_organizzativaBulk uo = new Unita_organizzativaBulk(uo_scrivania);
		   
			if(!isUoEnte(context) && !uo.isUoCds())	 {					
				clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
				clauses.addClause("AND", "cds",SQLBuilder.EQUALS, cds);
			}

			if(!isUoEnte(context) && uo.isUoCds())	 {					
				clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
				clauses.addClause("AND", "cds",SQLBuilder.EQUALS, cds);
			}
			   
			if (isUoEnte(context))
				clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
				
			setBaseclause(clauses);	
					
			if (getPathConsultazione()==null) {
				setPathConsultazione(this.LIV_BASE);					
				setLivelloConsultazione(this.LIV_BASE);
			} 
				
			super.init(config,context);
			initVariabili(context,null,getPathConsultazione()); 
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} 
	}

	   public void initVariabili(ActionContext context, String pathProvenienza, String livello_destinazione) throws BusinessProcessException {
		   try {
				   if ((pathProvenienza==null) && (livello_destinazione.equals(this.LIV_BASE))) {
		   				setPathConsultazione(this.LIV_BASE);
		   				setLivelloConsultazione(this.LIV_BASE);
		   			}
		   		/*	if ((pathProvenienza==LIV_BASE) && (livello_destinazione.equals(this.LIV_BASEMOD))) {
		   				setPathConsultazione(this.LIV_BASEMOD);
		   				setLivelloConsultazione(this.LIV_BASEMOD);
		   			}
			   		if (pathConsultazione.equals(this.LIV_BASEMOD) && (livello_destinazione.equals(this.LIV_BASEMODGAE))){
					 		setPathConsultazione(this.LIV_BASEMODGAE);
							setLivelloConsultazione(this.LIV_BASEMODGAE);
					}
			   		if (pathConsultazione.equals(this.LIV_BASEMODGAE) && (livello_destinazione.equals(this.LIV_BASEMODGAEVOCE))){
			   			setPathConsultazione(this.LIV_BASEMODGAEVOCE);
						setLivelloConsultazione(this.LIV_BASEMODGAEVOCE);
			   		}*/
			   		if (pathProvenienza!=null){
		   		 		setPathConsultazione(pathProvenienza.concat(livello_destinazione));
		   		 		setLivelloConsultazione(livello_destinazione);
		   		 	}
			   		if (livello_destinazione.equals(this.LIV_BASEMODGAEVOCE))
						setMultiSelection(false);
			   		
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
	
	public String getLivelloConsultazione() {
		return livelloConsultazione;
	}
	
	public void setLivelloConsultazione(String livelloConsultazione) {
		this.livelloConsultazione = livelloConsultazione;
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
	
	public CompoundFindClause getSelezione(ActionContext context) throws BusinessProcessException {
		   try	{
			   CompoundFindClause clauses = null;
			   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
			   {
			   	V_cons_confronto_ent_speBulk wpb = (V_cons_confronto_ent_speBulk)i.next();
				   CompoundFindClause parzclause = new CompoundFindClause();
				
				   		parzclause.addClause("AND","cds",SQLBuilder.EQUALS,wpb.getCds());
//				   		parzclause.addClause("AND","cd_unita_organizzativa",SQLBuilder.EQUALS,wpb.getCd_centro_responsabilita());
				   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
				  
				   		  if (isPresenteMOD()) 
							   parzclause.addClause("AND","cd_modulo",SQLBuilder.EQUALS,wpb.getCd_modulo());
						  if (isPresenteGAE()) 
							   parzclause.addClause("AND","cd_linea_attivita",SQLBuilder.EQUALS,wpb.getCd_linea_attivita());
						   if (isPresenteVOCE()) {
							   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,wpb.getCd_elemento_voce());
						   }
						   
				   clauses = clauses.or(clauses, parzclause);
			   }
			   return clauses;
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	}
	
	
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
	   
	   		if (getLivelloConsultazione().equals(this.LIV_BASE)) {
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_mod");
				if (this.isFlNuovoPdg()) {
					button.setTitle("Consultazione per "+ProgettoBulk.LABEL_PROGETTO);
					button.setLabel("Dettagli "+ProgettoBulk.LABEL_PROGETTO);
				}
				button.setSeparator(true);
			   listButton.addElement(button);
			}

	   		if (getLivelloConsultazione().equals(this.LIV_BASEMOD)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_gae");
					   button.setSeparator(true);
					   listButton.addElement(button);
			}
		   	
			if (getLivelloConsultazione().equals(this.LIV_BASEMODGAE)) {
				   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_voce");
				   button.setSeparator(true);
				   listButton.addElement(button);
			}
			return listButton;
	   }
	public void setTitle() {
		
		   String title=null;
		   		   title = "Confronto Entrate-Spese per CDS Competenza";
			
			if (isPresenteMOD()) title = title.concat(" - "+(!this.isFlNuovoPdg()?ProgettoBulk.LABEL_MODULO:ProgettoBulk.LABEL_PROGETTO));
			if (isPresenteGAE()) title = title.concat(" - GAE");
			if (isPresenteVOCE()) title = title.concat(" - Voce");
		
			getBulkInfo().setShortDescription(title);
		}

	public boolean isPresenteMOD() {
		return getPathConsultazione().indexOf(LIV_BASEMOD)>=0;
		}
	
	public boolean isPresenteGAE() {
	   return getPathConsultazione().indexOf(LIV_BASEMODGAE)>=0;
	}
	
	public boolean isPresenteVOCE() {
	   return getPathConsultazione().indexOf(LIV_BASEMODGAEVOCE)>=0;
	}
	
	public void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
	
	public String getColumnLabelCd_modulo(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_PROGETTO;
		else
			return ProgettoBulk.LABEL_MODULO;
	}	
	public String getFindLabelCd_progetto(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_PROGETTO;
		else
			return ProgettoBulk.LABEL_MODULO;
	}	
}