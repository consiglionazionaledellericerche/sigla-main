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

package it.cnr.contab.inventario00.bp;
import java.util.Iterator;

import it.cnr.contab.inventario00.consultazioni.bulk.V_cons_registro_inventarioBulk;
import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.contab.inventario00.ejb.ConsRegistroInventarioComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;
 
public class ConsRegistroInventarioBP extends ConsultazioniBP{	
	private String pathConsultazione;
	private String livelloConsultazione;
	public static final String QUOTE= "QUOTE";
	public static final String VALORI= "BASECONS";
	public boolean consultazione ;
	
	public boolean isquoteButtonHidden() {
		if (getParent()!=null && getParent().getName().compareTo(new String("ConsElencoinventariobeniBP"))==0) 
			return true;
		else 
			return false;
	}

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
				String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
				Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

				CompoundFindClause clauses = new CompoundFindClause();
			
				clauses.addClause("AND", "esercizio", SQLBuilder.LESS_EQUALS, esercizio);
				clauses.addClause("AND", "esercizio_carico_bene", SQLBuilder.LESS_EQUALS, esercizio);
				clauses.addClause("AND", "cd_cds", SQLBuilder.EQUALS, cds);
				if(!isUoEnte(context)){
					 clauses.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
				}
				setBaseclause(clauses);
			if (getPathConsultazione()==null) {	
				setPathConsultazione(VALORI);
				setLivelloConsultazione(VALORI);
			}
			else
			{
				setPathConsultazione(QUOTE);
				setLivelloConsultazione(QUOTE);
			}			
			super.init(config,context);
			initVariabili(context,null,getPathConsultazione());   
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			setIterator(context,createConsRegistroInventarioComponentSession().findConsultazione(context.getUserContext(),getPathConsultazione(),getBaseclause(),null));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	
	public ConsRegistroInventarioComponentSession createConsRegistroInventarioComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsRegistroInventarioComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_ConsRegistroInventarioComponentSession",ConsRegistroInventarioComponentSession.class);
	}
  
   public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException  
   {
			   try {
				 if (pathProvenienza == null || livello_destinazione.equals(this.VALORI)){
					setPathConsultazione(this.VALORI);
					setLivelloConsultazione(this.VALORI);
				 }
				else{
					setPathConsultazione(this.QUOTE);	 
					setLivelloConsultazione(this.QUOTE);
				}	 
		   		setSearchResultColumnSet(getPathConsultazione());
		   		setFreeSearchSet(getPathConsultazione());
		   		setTitle();
		   		setMultiSelection(false);
	   		}catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   	}	   	
   }
	 public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			return createConsRegistroInventarioComponentSession().findConsultazione(context.getUserContext(),getPathConsultazione(),getBaseclause(),compoundfindclause);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	  
	  public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		   if (getPathConsultazione().equals(this.VALORI)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.quote");
			   button.setSeparator(true);
			   listButton.addElement(button);
		   }
		   return listButton;
	   }	
	   public String getPathConsultazione() {
		   return pathConsultazione;
	   }
	   public void setPathConsultazione(String string) {
		   pathConsultazione = string;
	   }
		public boolean isPresenteQuote() {
		   return getPathConsultazione().indexOf(QUOTE)>=0;
	   }
	   public boolean isPresenteValori() {
				  return getPathConsultazione().indexOf(VALORI)>=0;
	   }
	   /**
		* Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
		* sulla base del path della consultazione
		*/
	   public void setTitle() {
		   String title="";
		  
			if (isPresenteValori()) title = title.concat("Consultazioni Registro Inventario Valori");
			if (isPresenteQuote()) title = title.concat("Consultazioni Registro Inventario Quote");
			
		   getBulkInfo().setShortDescription(title);   
		   getBulkInfo().setLongDescription("Consultazioni Registro Inventario");
	   }
		public String getLivelloConsultazione() {
			return livelloConsultazione;
		}
	
		public void setLivelloConsultazione(String string) {
			livelloConsultazione = string;
		}

		public boolean isConsultazione() {
			return consultazione;
		}

		public void setConsultazione(boolean consultazione) {
			this.consultazione = consultazione;
		}
		public boolean isUoEnte(ActionContext context){	
			Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
			if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
				return true;	
			return false; 
		}	

  }
