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


import java.math.BigDecimal;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.bp.RicercaSingoloContoBP;
import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg00.bp.StampaVariazioniPdgRiepBP;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Stampa_pdg_variazione_riepilogoBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaPdgVariazioneAction extends ParametricPrintAction {
	public StampaPdgVariazioneAction() {
		super();
	}
	public Forward doSelezionaRiepilogoVariazioni(ActionContext context) {
		try {
			StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
			HookForward caller = (HookForward)context.getCaller();
			List variazioni = (List) caller.getParameter("selectedElements");
			if (variazioni != null)
				for (Iterator iVariazioni = variazioni.iterator(); iVariazioni.hasNext();){
					Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk)iVariazioni.next();
					((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).addToRiepilogovariazioni(pdg_variazione);
				}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doCambiaStato(ActionContext context) {
		StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
		String oldStato = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).getStato();
		try {
			fillModel(context);
			String newStato = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).getStato();
			if(!oldStato.equals(newStato) && !newStato.equals(Stampa_pdg_variazione_riepilogoBulk.STATO_TUTTI))
			  ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setRiepilogovariazioni(new BulkList());
			if(!oldStato.equals(newStato) && newStato.equals(Stampa_pdg_variazione_riepilogoBulk.STATO_APP)) {
				((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setDataApprovazione_daEnabled(false);
				((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setDataApprovazione_aEnabled(false);
			}	
			else {
				((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setDataApprovazione_daEnabled(true);
				((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setDataApprovazione_aEnabled(true);
				((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setDataApprovazione_da(null);
				((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).setDataApprovazione_a(null);
			}
			
			
		} catch (FillException e) {
			return handleException(context,e);
		}
		
		return context.findDefaultForward();
	}	
	public Forward doAddToCRUDMain_RiepilogoVariazioni(ActionContext context) {
		try {
			fillModel(context);
			Pdg_variazioneBulk pdg_variazione = new Pdg_variazioneBulk();
			StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
			CompoundFindClause clauses = new CompoundFindClause();
			Stampa_pdg_variazione_riepilogoBulk stampa_variazioni = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel());
			if (stampa_variazioni.getRiepilogovariazioni() != null){
				for (Iterator iVariazioni = stampa_variazioni.getRiepilogovariazioni().iterator(); iVariazioni.hasNext();){
					Pdg_variazioneBulk pdg_var = (Pdg_variazioneBulk)iVariazioni.next();
					clauses.addClause("AND","pg_variazione_pdg",SQLBuilder.NOT_EQUALS,pdg_var.getPg_variazione_pdg());
				}
			}
			it.cnr.jada.util.RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,createPdgComponentSession().cercaForPrintRiepilogo(context.getUserContext(),clauses,pdg_variazione,bp.getModel()));
			int count = ri.countElements();
			if (count == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);				
				bp.setMessage("Nessuna variazione selezionabile.");
			} else {
				it.cnr.jada.util.action.SelezionatoreListaBP nbp = (it.cnr.jada.util.action.SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
				nbp.setIterator(context,ri);
				nbp.setBulkInfo(pdg_variazione.getBulkInfo());
				nbp.setMultiSelection(true);
				context.addHookForward("seleziona",this,"doSelezionaRiepilogoVariazioni");
				return context.addBusinessProcess(nbp);
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	
	public PdGVariazioniComponentSession createPdgComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (PdGVariazioniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGVariazioniComponentSession", PdGVariazioniComponentSession.class);
	}
	
	public Forward doBringBackSearchFindUOForPrint(ActionContext context,Stampa_pdg_variazione_riepilogoBulk stampa, Unita_organizzativaBulk uo){
		StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
		Stampa_pdg_variazione_riepilogoBulk stampa_variazioni = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel());
		if (uo != null && !uo.equalsByPrimaryKey(stampa_variazioni.getUoForPrint()))
		  stampa_variazioni.setRiepilogovariazioni(new BulkList());
		stampa_variazioni.setUoForPrint(uo);     
		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindCDSForPrint(ActionContext context,Stampa_pdg_variazione_riepilogoBulk stampa, CdsBulk cds){
		StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
		Stampa_pdg_variazione_riepilogoBulk stampa_variazioni = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel());
		if (cds != null && !cds.equalsByPrimaryKey(stampa_variazioni.getCdsForPrint())){
			stampa_variazioni.setRiepilogovariazioni(new BulkList());
			stampa_variazioni.setUoForPrint(new Unita_organizzativaBulk());
		}
		stampa_variazioni.setCdsForPrint(cds);     
		return context.findDefaultForward();
	}
	public Forward doBlankSearchFindUOForPrint(ActionContext context, Stampa_pdg_variazione_riepilogoBulk stampa){
		StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
		Stampa_pdg_variazione_riepilogoBulk stampa_variazioni = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel());
		stampa_variazioni.setRiepilogovariazioni(new BulkList());
		stampa_variazioni.setUoForPrint(new Unita_organizzativaBulk());	
		stampa_variazioni.setDataApprovazione_da(null);
		stampa_variazioni.setDataApprovazione_a(null);	
		return context.findDefaultForward();
	}
	public Forward doBlankSearchFindCDSForPrint(ActionContext context, Stampa_pdg_variazione_riepilogoBulk stampa){
		StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();
		Stampa_pdg_variazione_riepilogoBulk stampa_variazioni = ((Stampa_pdg_variazione_riepilogoBulk)bp.getModel());
		stampa_variazioni.setRiepilogovariazioni(new BulkList());	
		stampa_variazioni.setUoForPrint(new Unita_organizzativaBulk());
		stampa_variazioni.setCdsForPrint(new CdsBulk());
		stampa_variazioni.setDataApprovazione_da(null);
		stampa_variazioni.setDataApprovazione_a(null);
		
		return context.findDefaultForward();
	}
	/*
	 * Serve per settare Id_Report
	 */  
	public Forward doPrint(ActionContext context) {
		StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)context.getBusinessProcess();;
		Forward forward = super.doPrint(context);
		if (context.getBusinessProcess() instanceof OfflineReportPrintBP){
			OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.getBusinessProcess();
			printbp.setId_report_generico(((Stampa_pdg_variazione_riepilogoBulk)bp.getModel()).getPg_stampa());
		}
		return forward;
	}
}
