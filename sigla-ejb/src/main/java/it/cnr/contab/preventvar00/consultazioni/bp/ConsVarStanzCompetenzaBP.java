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

import it.cnr.contab.config00.bp.CRUDClassificazioneVociBP;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.Monito_cococoBulk;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.contab.preventvar00.ejb.ConsVarStanzCompetenzaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;

public class ConsVarStanzCompetenzaBP extends BulkBP
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
			V_cons_var_pdggBulk bulk = new V_cons_var_pdggBulk();
			V_classificazione_vociBulk liv = new V_classificazione_vociBulk();
			CompoundFindClause clauses = new CompoundFindClause();
			
			Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
			clauses.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
			setParametriLivelli(((ConsVarStanzCompetenzaComponentSession) createComponentSession()).findParametriLivelli(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
			
			setModel(context,bulk);
			bulk.setTi_gestione(V_cons_var_pdggBulk.TI_GESTIONE_ENTRATE);
			bulk.setV_classificazione_voci(liv);
		

		} catch(Throwable e) {
			throw handleException(e);
		}
		super.init(config,context);
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


	public ConsVarStanzCompetenzaComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsVarStanzCompetenzaComponentSession)createComponentSession("CNRPREVENTVAR00_EJB_ConsVarStanzCompetenzaComponentSession",ConsVarStanzCompetenzaComponentSession.class);
	}

	public void valorizzaDate(ActionContext context, V_cons_var_pdggBulk bulk) throws ValidationException{
		if (bulk.getDt_approvazione_da()== null || bulk.getDt_approvazione_a() == null)
			throw new ValidationException("Valorizzare le Date di approvazione");
	}

	
	public void valorizzaTi_Gestione(ActionContext context, V_cons_var_pdggBulk bulk) throws ValidationException{
		if (bulk.getTi_gestione()== null)
			throw new ValidationException("Valorizzare il tipo di gestione");
	}
	
	
	
	public String getDescrizioneClassificazione() {
	 	   return descrizioneClassificazione;
	    }

	    public void setDescrizioneClassificazione(String string) {
		   descrizioneClassificazione = string;
	    }
	

	
}
