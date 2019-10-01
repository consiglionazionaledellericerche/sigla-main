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

/*
 * Created on Oct 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;



import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bulk.Stampa_situazione_analitica_x_GAEBulk;
import it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaSituazioneAnaliticaXGaeAction extends ParametricPrintAction {
	public StampaSituazioneAnaliticaXGaeAction() {
		super();
	}
	
	public PdGPreventivoComponentSession createPdgComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (PdGPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGPreventivoComponentSession", PdGPreventivoComponentSession.class);
	}
	
	public Forward doBlankSearchFindUoForPrint(ActionContext context, Stampa_situazione_analitica_x_GAEBulk stampa){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_analitica_x_GAEBulk stampa_gae = ((Stampa_situazione_analitica_x_GAEBulk)bp.getModel());
		stampa_gae.setUoForPrint(new Unita_organizzativaBulk());
		stampa_gae.setCdrForPrint(new CdrBulk());
		
		return context.findDefaultForward();
	}
	
	public Forward doBlankSearchFindCdsForPrint(ActionContext context, Stampa_situazione_analitica_x_GAEBulk stampa){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_analitica_x_GAEBulk stampa_gae = ((Stampa_situazione_analitica_x_GAEBulk)bp.getModel());
		stampa_gae.setCdsForPrint(new CdsBulk());
		stampa_gae.setUoForPrint(new Unita_organizzativaBulk());
		stampa_gae.setCdrForPrint(new CdrBulk());
		
		return context.findDefaultForward();
	}
}
