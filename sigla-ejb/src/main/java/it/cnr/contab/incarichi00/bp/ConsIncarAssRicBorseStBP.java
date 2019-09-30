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

package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.contab.incarichi00.ejb.ConsIncarAssRicBorseStComponentSession;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.contab.preventvar00.ejb.ConsVarStanzCompetenzaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;

public class ConsIncarAssRicBorseStBP extends BulkBP
{
	public Parametri_livelliBulk parametriLivelli;
	private String descrizioneClassificazione;
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		try {
			VIncarichiAssRicBorseStBulk bulk = new VIncarichiAssRicBorseStBulk();
			
			bulk = ((ConsIncarAssRicBorseStComponentSession)createComponentSession()).impostaDatiIniziali(context.getUserContext(), bulk);		
			
			setModel(context,bulk);

		} catch(Throwable e) {
			throw handleException(e);
		}
		super.init(config,context);
	}

	public VIncarichiAssRicBorseStBulk createNewBulk(ActionContext context) throws BusinessProcessException {
		try {
			VIncarichiAssRicBorseStBulk bulk = new VIncarichiAssRicBorseStBulk();
			bulk.setUser(context.getUserInfo().getUserid());
			return bulk;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	
	public String getColumnsetForGenericSearch() {
		return "protocollazioneIvaSet";
	}

	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	
	public Parametri_livelliBulk getParametriLivelli(ActionContext actioncontext) throws BusinessProcessException {
		try {
			if (parametriLivelli == null)
				setParametriLivelli(((ConsVarStanzCompetenzaComponentSession) createComponentSession()).findParametriLivelli(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		}
		return getParametriLivelli();
	}

	public Parametri_livelliBulk getParametriLivelli() {
		return parametriLivelli;
	}

	public void setParametriLivelli(Parametri_livelliBulk bulk) {
		parametriLivelli = bulk;
	}
	
/*	public String getLabelCd_livello1(){
		String etr_spe = ((V_cons_var_pdggBulk)getModel()).getTi_gestione();
		if (etr_spe.equals("S"))
			return getParametriLivelli().getDs_livello1s();
		return getParametriLivelli().getDs_livello1e();
	}*/

	public String getLabelFind_classificazione_voci(){
		String etr_spe = ((V_cons_var_pdggBulk)getModel()).getTi_gestione();
		if (etr_spe.equals("S"))
			return getParametriLivelli().getDs_livello1s();
		return getParametriLivelli().getDs_livello1e();
	}
	
	public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
		} catch(Exception e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}


	public ConsIncarAssRicBorseStComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsIncarAssRicBorseStComponentSession)createComponentSession("CNRINCARICHI00_EJB_ConsIncarAssRicBorseStComponentSession",ConsIncarAssRicBorseStComponentSession.class);
	}

	public void controlloSelezioni(ActionContext context, VIncarichiAssRicBorseStBulk bulk) throws ValidationException{
		if (!bulk.almenoUnaTipologiaSelezionata()){
			throw new ValidationException("E' necessario selezionare almeno una tipologia.");
		}
		if (!bulk.almenoUnoStatoSelezionato()){
			throw new ValidationException("E' necessario selezionare almeno uno stato.");
		}
	}

	
	
	
	public String getDescrizioneClassificazione() {
	 	   return descrizioneClassificazione;
	    }

	    public void setDescrizioneClassificazione(String string) {
		   descrizioneClassificazione = string;
	    }
	

	
}
