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
import it.cnr.contab.doccont00.bp.ConsDispCompetenzaResiduoIstitutoBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.preventvar00.ejb.ConsAssCompPerDataComponentSession;
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
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;


public class ConsAssCompPerDataBP extends BulkBP
{
	public Parametri_livelliBulk parametriLivelli;
	private String descrizioneClassificazione;
	private String livelloConsultazione;
	private String pathConsultazione;
	
	public static final String LIV_BASE= "BASE";
	public static final String LIV_BASEVARPIU= "VARPIU";
	public static final String LIV_BASEVARMENO= "VARMENO";
	
	
	public ConsAssCompPerDataComponentSession createConsAssCompPerDataComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsAssCompPerDataComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENTVAR00_EJB_ConsAssCompPerDataComponentSession", ConsAssCompPerDataComponentSession.class);
	}
	
	public ConsAssCompPerDataComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsAssCompPerDataComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENTVAR00_EJB_ConsAssCompPerDataComponentSession", ConsAssCompPerDataComponentSession.class);
	}
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
			V_cons_ass_comp_per_dataBulk bulk = new V_cons_ass_comp_per_dataBulk();
			
			CompoundFindClause clauses = new CompoundFindClause();
			Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
            clauses.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizio);
			
//			setParametriLivelli(((ConsVarStanzCompetenzaComponentSession) createComponentSession()).findParametriLivelli(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
			
			setModel(context,bulk);
			bulk.setTi_gestione(V_cons_ass_comp_per_dataBulk.TI_GESTIONE_ENTRATE);
			bulk.setEsercizio(esercizio);
//			bulk.setV_classificazione_voci(liv);
		   
//			setBaseclause(clauses);	
		
		super.init(config,context);
	}
	   
	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;	
		return false; 
	}	
 
	public void setTitle() {
		
		   String title=null;
		   		   title = "Consultazione Assestato Competenza alla Data";
		
			getBulkInfo().setShortDescription(title);
		}	

	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	
	public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
		} catch(Exception e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}

	public void valorizzaDate(ActionContext context, V_cons_ass_comp_per_dataBulk bulk) throws ValidationException{
		if (bulk.getData_approvazione_var()== null)
			throw new ValidationException("Valorizzare la Data");
	}

}
