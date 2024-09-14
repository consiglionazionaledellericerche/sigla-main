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

import java.util.Iterator;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.ejb.ConsSospesiEntSpeComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_cons_sospesiBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;


public class ConsSospesiBP extends ConsultazioniBP {
	
	public static final String LIV_SOSREV= "SOSREV";
	public static final String LIV_SOSREVRDETT= "RDETT";
	public static final String LIV_SOSREVRDETTREV= "REV";
	
	public static final String LIV_SOSMAN= "SOSMAN";
	public static final String LIV_SOSMANMDETT= "MDETT";
	public static final String LIV_SOSMANMDETTMAN= "MAN";
	
	private String livelloConsultazione;
	private String pathConsultazione;	
	private boolean tesoreria_unica=false;

	public ConsSospesiEntSpeComponentSession createConsSospesiEntSpeComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsSospesiEntSpeComponentSession", ConsSospesiEntSpeComponentSession.class);
	}

	protected void init(it.cnr.jada.action.Config config,ActionContext context) throws BusinessProcessException {
		  try {
		   Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
		   String cds = CNRUserContext.getCd_cds(context.getUserContext());
		   CompoundFindClause clauses = new CompoundFindClause();
		   String uo_scrivania = CNRUserContext.getCd_unita_organizzativa(context.getUserContext());
		   setTesoreria_unica(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(),CNRUserContext.getEsercizio(context.getUserContext())).getFl_tesoreria_unica().booleanValue());
		   Unita_organizzativaBulk uo = new Unita_organizzativaBulk(uo_scrivania);
		   	clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
		   	if(!tesoreria_unica)
		   		clauses.addClause("AND", "cd_cds",SQLBuilder.EQUALS, cds);
		   	else
		   		if(!isUoEnte(context)){
		   			clauses.addClause("AND", "cd_cds_origine",SQLBuilder.EQUALS, cds);		   				
		   		}else
		   			clauses.addClause("AND", "cd_cds",SQLBuilder.EQUALS, cds);
		   			
		   	
			/* if(!isUoEnte(context) && !uo.isUoCds())	 {					
					clauses.addClause("AND", "cd_cds",SQLBuilder.EQUALS, cds);
				}

			   if(!isUoEnte(context) && uo.isUoCds())	 {					
					clauses.addClause("AND", "cd_cds",SQLBuilder.EQUALS, cds);
				}*/
			   
					if (getPathConsultazione()==null && this instanceof ConsSospesiEntrateBP) {
							setPathConsultazione(LIV_SOSREV);					
							setLivelloConsultazione(LIV_SOSREV);
					
					} 
					if (getPathConsultazione()==null && this instanceof ConsSospesiSpeseBP) {
						setPathConsultazione(LIV_SOSMAN);					
						setLivelloConsultazione(LIV_SOSMAN);
					} 
					
					setBaseclause(clauses);	
				
			super.init(config,context);
			initVariabili(context,null,getPathConsultazione()); 
		  }catch(Throwable e) {
			   	throw new BusinessProcessException(e);
		   }  
		}

	   public void initVariabili(ActionContext context, String pathProvenienza, String livello_destinazione) throws BusinessProcessException {
		   try {
			   if(this instanceof ConsSospesiEntrateBP)
				   if ((pathProvenienza==null) && (livello_destinazione.equals(LIV_SOSREV))) {
		   				setPathConsultazione(LIV_SOSREV);
		   				setLivelloConsultazione(LIV_SOSREV);
		   			}
			   		
			   	if(this instanceof ConsSospesiSpeseBP)
			   		if ((pathProvenienza==null) && (livello_destinazione.equals(LIV_SOSMAN))) {
		   				setPathConsultazione(LIV_SOSMAN);
		   				setLivelloConsultazione(LIV_SOSMAN);
		   			}
			   	
			   		if (pathProvenienza!=null){
		   		 		setPathConsultazione(pathProvenienza.concat(livello_destinazione));
		   		 		setLivelloConsultazione(livello_destinazione);
		   		 	}
			   	
			   		
			   		if (livello_destinazione.equals(LIV_SOSREVRDETTREV) || livello_destinazione.equals(LIV_SOSMANMDETTMAN))
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
	
	public CompoundFindClause getSelezione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{
			   CompoundFindClause clauses = null;
			   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
			   {
			   	V_cons_sospesiBulk bulk = (V_cons_sospesiBulk)i.next();
			    CompoundFindClause parzclause = new CompoundFindClause();
			   
				    parzclause.addClause("AND","cd_cds",SQLBuilder.EQUALS,bulk.getCd_cds());
				    
				    if(!tesoreria_unica)
				    	parzclause.addClause("AND","cd_sospeso_padre",SQLBuilder.EQUALS,bulk.getCd_sospeso());
				   	else
				   		if(!isUoEnte(context)){
				   			parzclause.addClause("AND","cd_sospeso",SQLBuilder.EQUALS,bulk.getCd_sospeso());		   				
				   		}else
				   			parzclause.addClause("AND","cd_sospeso_padre",SQLBuilder.EQUALS,bulk.getCd_sospeso());
				   
			    clauses = clauses.or(clauses, parzclause);
			   }
			   return clauses;
		 	}catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	}
	
	
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
	   
	   		if (getLivelloConsultazione().equals(LIV_SOSREV)||getLivelloConsultazione().equals(LIV_SOSMAN)) {
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_sos");
				button.setSeparator(true);
				   listButton.addElement(button);
			}

	   		if (getLivelloConsultazione().equals(LIV_SOSREVRDETT)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_RevColl");
					   button.setSeparator(true);
					   listButton.addElement(button);
			}
		   	
	   		if (getLivelloConsultazione().equals(LIV_SOSMANMDETT)) {
				   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_ManColl");
				   button.setSeparator(true);
				   listButton.addElement(button);
		}
	   	
			return listButton;
	   }
	public void setTitle() {
		
		   String title=null;
		   		   title = "Sospesi";
			
		   		
			if (isPresenteRDETT()) title = title.concat(" - Reversali Collegate");
			if (isPresenteMDETT()) title = title.concat(" - Mandati Collegati");
			
			if (isPresenteREV()) title = title.concat(" - Reversale");
			if (isPresenteMAN()) title = title.concat(" - Mandato");
			
			getBulkInfo().setShortDescription(title);
		}

	public boolean isPresenteRDETT() {
		return getLivelloConsultazione().equals(LIV_SOSREVRDETT);
		}
	
	public boolean isPresenteMDETT() {
		return getLivelloConsultazione().equals(LIV_SOSMANMDETT);
		}
	
	public boolean isPresenteREV() {
	   return getLivelloConsultazione().equals(LIV_SOSREVRDETTREV);
	}
	
	public boolean isPresenteMAN() {
	   return getLivelloConsultazione().equals(LIV_SOSMANMDETTMAN);
	}
	
	public boolean isTesoreria_unica() {
		return tesoreria_unica;
	}

	public void setTesoreria_unica(boolean tesoreria_unica) {
		this.tesoreria_unica = tesoreria_unica;
	}
}
