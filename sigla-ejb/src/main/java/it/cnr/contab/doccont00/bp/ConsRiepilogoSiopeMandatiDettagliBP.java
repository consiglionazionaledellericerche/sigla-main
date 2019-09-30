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



import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_siope_mandatiBulk;
import it.cnr.contab.doccont00.ejb.ConsRiepilogoSiopeComponentSession;

import it.cnr.contab.preventvar00.ejb.ConsAssCompPerDataComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class ConsRiepilogoSiopeMandatiDettagliBP extends ConsultazioniBP {
	
	public Parametri_livelliBulk parametriLivelli;
	private String descrizioneClassificazione;
	private String livelloConsultazione;
	private String pathConsultazione;
	
	public static final String LIV_BASE= "BASE";
	public static final String LIV_BASEDETT= "DETT";
	
	
	public ConsRiepilogoSiopeComponentSession createConsRiepilogoSiopeComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsRiepilogoSiopeComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsRiepilogoSiopeComponentSession", ConsRiepilogoSiopeComponentSession.class);
	}
	
	public ConsRiepilogoSiopeComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsRiepilogoSiopeComponentSession)createComponentSession("CNRDOCCONT00_EJB_ConsRiepilogoSiopeComponentSession",ConsRiepilogoSiopeComponentSession.class);
	}
	
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			V_cons_siope_mandatiBulk mandati = (V_cons_siope_mandatiBulk)oggettobulk;
			CompoundFindClause clause = new CompoundFindClause(getBaseclause(), compoundfindclause);
			return createConsRiepilogoSiopeComponentSession().findSiopeDettaglioMandati(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),compoundfindclause,mandati);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			ConsRiepilogoSiopeMandatiDettagliBP bp=(ConsRiepilogoSiopeMandatiDettagliBP)context.getBusinessProcess();
			V_cons_siope_mandatiBulk bulk=(V_cons_siope_mandatiBulk)bp.getModel();
			setIterator(context,createConsRiepilogoSiopeComponentSession().findSiopeDettaglioMandati(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null,bulk));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
		CompoundFindClause clauses = new CompoundFindClause();
		if (context.getBusinessProcess().getName().equals("ConsRiepilogoSiopeMandatiBP")){
			ConsRiepilogoSiopeMandatiBP bp=(ConsRiepilogoSiopeMandatiBP)context.getBusinessProcess();
			if(bp.getModel()!=null && bp.getModel() instanceof V_cons_siope_mandatiBulk){
				V_cons_siope_mandatiBulk bulk=(V_cons_siope_mandatiBulk)bp.getModel();
				Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
				clauses.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
				clauses.addClause("AND","cd_cds",SQLBuilder.EQUALS,bulk.getCds().getCd_unita_organizzativa());
				clauses.addClause("AND","dt_emissione",SQLBuilder.GREATER_EQUALS,bulk.getDt_emissione_da());
				clauses.addClause("AND","dt_emissione",SQLBuilder.LESS_EQUALS,bulk.getDt_emissione_a());
				clauses.addClause("AND","dt_pagamento",SQLBuilder.GREATER_EQUALS,bulk.getDt_pagamento_da());
				clauses.addClause("AND","dt_pagamento",SQLBuilder.LESS_EQUALS,bulk.getDt_pagamento_a());
				clauses.addClause("AND","dt_trasmissione",SQLBuilder.GREATER_EQUALS,bulk.getDt_trasmissione_da());
				clauses.addClause("AND","dt_trasmissione",SQLBuilder.LESS_EQUALS,bulk.getDt_trasmissione_a());
				
				setModel(context,bulk);
				setPathConsultazione(this.LIV_BASE);					
				setLivelloConsultazione(this.LIV_BASE);
				setBaseclause(clauses);	
				super.init(config,context);
				initVariabili(context,null,getPathConsultazione()); 
			}
	}
		
		if (context.getBusinessProcess().getName().equals("ConsRiepilogoSiopeMandatiDettagliBP")){
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
			   
		   			if ((pathConsultazione==this.LIV_BASE) && (livello_destinazione.equals(this.LIV_BASEDETT))) {
		   				setPathConsultazione(this.LIV_BASEDETT);
		   				setLivelloConsultazione(this.LIV_BASEDETT);
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
		   		   title = "Consultazione Riepilogo Codici Siope su Mandati";
			
			if (isPresenteDETT()) title = title.concat(" - Dettaglio");
			
			getBulkInfo().setShortDescription(title);
		}	

	public boolean isPresenteDETT() {
		return getPathConsultazione().indexOf(LIV_BASEDETT)>=0;
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
			   	V_cons_siope_mandatiBulk wpb = (V_cons_siope_mandatiBulk)i.next();
				   CompoundFindClause parzclause = new CompoundFindClause();
				
				   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
				   		parzclause.addClause("AND","cd_cds",SQLBuilder.EQUALS,wpb.getCd_cds());
				   		parzclause.addClause("AND","cd_siope",SQLBuilder.EQUALS,wpb.getCd_siope());
				 
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
	  		Button button = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.consdett");
			button.setSeparator(true);
			listButton.addElement(button);
			
		}
		}
			return listButton;
	   }
	}
