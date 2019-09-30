
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

package it.cnr.contab.bollo00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.bollo00.bulk.V_cons_atto_bolloBulk;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

public class ConsAttoBolloBP extends ConsultazioniBP {
	private static final long serialVersionUID = 1L;

	public static final String LIVELLO_TIP= "TIP";
	public static final String LIVELLO_UO= "UO";
	public static final String LIVELLO_DET= "DET";
	
	private String livelloConsultazione;
	private String pathConsultazione;

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			V_cons_atto_bolloBulk bulk = new V_cons_atto_bolloBulk();
			bulk.setTipoConsultazione(V_cons_atto_bolloBulk.TIPO_CONS_TIPO_ATTO);
			this.setModel(context, bulk);
			
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)createComponentSession().findByPrimaryKey(context.getUserContext(), 
					new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(context.getUserContext())));
	
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(context.getUserContext()));
			if (!uo.isUoEnte())
				clauses.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());
	
			setBaseclause(clauses);
				
			if (getPathConsultazione()==null) {
		   		setPathConsultazione(LIVELLO_TIP);					
		   		setLivelloConsultazione(LIVELLO_TIP);
					
				super.init(config,context);
				initVariabili(context, null,getPathConsultazione());   
			}	 		
		}catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} 
	}

	public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (pathProvenienza == null){
				setPathConsultazione(livello_destinazione);
				setLivelloConsultazione(livello_destinazione);
			} else {
				setPathConsultazione(pathProvenienza.concat(livello_destinazione));
				setLivelloConsultazione(livello_destinazione);
			}
		
			setSearchResultColumnSet(getPathConsultazione());
			setFreeSearchSet(getPathConsultazione());
			setTitle();

			if (LIVELLO_DET.equals(livello_destinazione))
			   setMultiSelection(false);
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.consuo");
		button.setSeparator(true);
		listButton.addElement(button);

		Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.constip");
		button2.setSeparator(true);
		listButton.addElement(button2);
		
		Button button3 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
		button3.setSeparator(true);
		listButton.addElement(button3);

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
	
	public boolean isPresenteUO() {
		return getPathConsultazione().indexOf(LIVELLO_UO)>=0;
	}
	
	public boolean isPresenteTIP() {
		return getPathConsultazione().indexOf(LIVELLO_TIP)>=0;
	}

	public boolean isPresenteDET() {
		return getPathConsultazione().indexOf(LIVELLO_DET)>=0;
	}
	
	/**
	  * Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
	  * sulla base del path della consultazione
	  */
	public void setTitle() {
		StringBuffer title = new StringBuffer("Elenco Documenti Bollo Virtuale per ");
		if (getPathConsultazione().startsWith(LIVELLO_TIP)) {
			title = title.append("Tipo");

		   	if (isPresenteUO()) 
		   		title = title.append("\\UO");
		   	if (isPresenteDET()) 
		   		title = title.append("\\Dettagli");
		} else if (getPathConsultazione().startsWith(LIVELLO_UO)) {
			title = title.append("UO");

		   	if (isPresenteTIP()) 
		   		title = title.append("\\Tipo");
		   	if (isPresenteDET()) 
		   		title = title.append("\\Dettagli");
		}

		getBulkInfo().setShortDescription(title.toString());
		getBulkInfo().setLongDescription("Elenco Documenti Bollo Virtuale");
	}
	
	/**
	  * Ritorna la CompoundFindClause ottenuta in base alla selezione effettuata
	  *
	  * @param field il campo da aggiornare 
	  * @param label il nuovo valore da sostituire al vecchio
	  */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public CompoundFindClause getSelezione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	   try	{
		   CompoundFindClause clauses = null;
		   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) {
			   V_cons_atto_bolloBulk atto = (V_cons_atto_bolloBulk)i.next();
			   CompoundFindClause parzclause = new CompoundFindClause();
	
			   if (isPresenteUO()) 
				   parzclause.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, atto.getCdUnitaOrganizzativa());
			   if (isPresenteTIP()) { 
				   parzclause.addClause(FindClause.AND, "cdTipoAtto", SQLBuilder.EQUALS, atto.getCdTipoAtto());
				   parzclause.addClause(FindClause.AND, "imBollo", SQLBuilder.EQUALS, atto.getImBollo());
			   }
			   clauses = clauses.or(clauses, parzclause);
		   }
		   return clauses;
	   } catch(Throwable e) {
		   throw new BusinessProcessException(e);
	   }
	}

	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);

			V_cons_atto_bolloBulk model = (V_cons_atto_bolloBulk)getModel();
			model.setNumGeneraleFogli(0);
			model.setNumGeneraleEsemplari(0);
			model.setImGeneraleBollo(BigDecimal.ZERO);
			
			RemoteIterator ri = Utility.createAttoBolloComponentSession().findConsultazioneDettaglio(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null, true);
			while (ri.hasMoreElements()) {
				V_cons_atto_bolloBulk detail = (V_cons_atto_bolloBulk) ri.nextElement();
				if (Tipo_atto_bolloBulk.TIPO_FOGLIO.equals(detail.getTiDettagli()))
					model.setNumGeneraleFogli(model.getNumGeneraleFogli()+detail.getNumDettagli());
				else if (Tipo_atto_bolloBulk.TIPO_ESEMPLARE.equals(detail.getTiDettagli()))
					model.setNumGeneraleEsemplari(model.getNumGeneraleEsemplari()+detail.getNumDettagli());
				else
					throw new ApplicationException("Esistono dettagli diversi da Fogli/Esemplari. Funzione non disponibile.");
				((V_cons_atto_bolloBulk)getModel()).setImGeneraleBollo(model.getImGeneraleBollo().abs().add(detail.getImTotaleBollo()));
			}
			EJBCommonServices.closeRemoteIterator(context, ri);
			return Utility.createAttoBolloComponentSession().findConsultazioneDettaglio(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),compoundfindclause, false);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			V_cons_atto_bolloBulk model = (V_cons_atto_bolloBulk)getModel();
			model.setNumGeneraleFogli(0);
			model.setNumGeneraleEsemplari(0);
			model.setImGeneraleBollo(BigDecimal.ZERO);
			
			RemoteIterator ri = Utility.createAttoBolloComponentSession().findConsultazioneDettaglio(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null, true);
			while (ri.hasMoreElements()) {
				V_cons_atto_bolloBulk detail = (V_cons_atto_bolloBulk) ri.nextElement();
				if (Tipo_atto_bolloBulk.TIPO_FOGLIO.equals(detail.getTiDettagli()))
					model.setNumGeneraleFogli(model.getNumGeneraleFogli()+detail.getNumDettagli());
				else if (Tipo_atto_bolloBulk.TIPO_ESEMPLARE.equals(detail.getTiDettagli()))
					model.setNumGeneraleEsemplari(model.getNumGeneraleEsemplari()+detail.getNumDettagli());
				else
					throw new ApplicationException("Esistono dettagli diversi da Fogli/Esemplari. Funzione non disponibile.");
				((V_cons_atto_bolloBulk)getModel()).setImGeneraleBollo(model.getImGeneraleBollo().abs().add(detail.getImTotaleBollo()));
			}
			EJBCommonServices.closeRemoteIterator(context, ri);
			setIterator(context,Utility.createAttoBolloComponentSession().findConsultazioneDettaglio(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null,false));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void cambiaVisibilita(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			V_cons_atto_bolloBulk bulk = (V_cons_atto_bolloBulk)this.getModel();
			String tipoConsultazione = bulk.getTipoConsultazione();
			EJBCommonServices.closeRemoteIterator(context,this.detachIterator());
			if (V_cons_atto_bolloBulk.TIPO_CONS_TIPO_ATTO.equals(tipoConsultazione)) {
				setPathConsultazione(LIVELLO_TIP);
				setLivelloConsultazione(LIVELLO_TIP);
			} else {
				setPathConsultazione(LIVELLO_UO);					
				setLivelloConsultazione(LIVELLO_UO);
			}

			this.initVariabili(context, null, getPathConsultazione());   
			this.openIterator(context);

			this.refresh(context);
			bulk.setTipoConsultazione(tipoConsultazione);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public boolean isConsUoButtonHidden(){
		return !(getPathConsultazione().startsWith(LIVELLO_TIP) &&
				LIVELLO_TIP.equals(getLivelloConsultazione()));
	}
	
	public boolean isConsTipButtonHidden(){
		return !(getPathConsultazione().startsWith(LIVELLO_UO) &&
				LIVELLO_UO.equals(getLivelloConsultazione()));
	}

	public boolean isDettagliButtonHidden(){
		return !isConsUoButtonHidden() || !isConsTipButtonHidden() || LIVELLO_DET.equals(getLivelloConsultazione());
	}
}
