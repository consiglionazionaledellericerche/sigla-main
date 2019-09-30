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

package it.cnr.contab.doccont00.action;



import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.bp.CRUDOrdineBP;
import it.cnr.contab.doccont00.bp.RicercaSingoloContoBP;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.OrdineComponentSession;
import it.cnr.contab.doccont00.ordine.bulk.OrdineBulk;
import it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk;
import it.cnr.contab.doccont00.singconto.bulk.V_stm_paramin_sing_contoBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class RicercaSingoloContoAction extends it.cnr.jada.util.action.CRUDAction {
public RicercaSingoloContoAction() {
		super();
	}
/**
 * Gestisce la chiusura del pannello di ricerca
 */

public Forward doAnnullaSelezioneCapitolo(ActionContext context) {

	try {
		RicercaSingoloContoBP bp = (RicercaSingoloContoBP)context.getBusinessProcess();
		bp.resetIdReport(context);
		
		return context.findDefaultForward();
	} catch (BusinessProcessException e) {
		return handleException(context, e);
	}
}
/**
 * Gestisce la chiusura del pannello di ricerca
 */

public Forward doAnnullaStampa(ActionContext context) {

	try {
		RicercaSingoloContoBP bp = (RicercaSingoloContoBP)context.getBusinessProcess();
		bp.clearSelection(context);
		bp.resetIdReport(context);
		
		return context.findDefaultForward();
	} catch (BusinessProcessException e) {
		return handleException(context, e);
	}
}
/**
 * Gestisce la chiusura del pannello di ricerca
 */

public Forward doBringBackCRUDFind_area_ricerca(
	ActionContext context,
	V_voce_f_sing_contoBulk model,
	Unita_organizzativaBulk area_ricerca) {

	return doBringBackSearchFind_area_ricerca(context, model, area_ricerca);
}
/**
 * Gestisce la chiusura del pannello di ricerca
 */

public Forward doBringBackSearchFind_area_ricerca(
	ActionContext context,
	V_voce_f_sing_contoBulk model,
	Unita_organizzativaBulk area_ricerca) {

	try {
		if (area_ricerca != null) {
			model.setArea_ricerca(area_ricerca);
			model.setCd_parte("1");
			((RicercaSingoloContoBP)context.getBusinessProcess()).setDirty(true);
		}	
		return context.findDefaultForward();
	} catch (Throwable e) {
		return handleException(context, e);
	}
}
/**
 * Cerca le scdenze disponibili per l'associazione alle spese del fondo eco
 * (Le spese sono NON documentate)
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 * @throws InstantiationException	
 * @throws RemoveException	
 */
public Forward doCerca(ActionContext context) throws java.rmi.RemoteException,InstantiationException,javax.ejb.RemoveException {
	
	try {
		fillModel(context);
		RicercaSingoloContoBP bp = (RicercaSingoloContoBP)context.getBusinessProcess();
		it.cnr.jada.util.RemoteIterator ri = bp.ricercaSingoliConti(context);
		int count = ri.countElements();
		if (count == 0) {
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
		} else {
			SelezionatoreListaBP slbp = select(
						context,
						ri,
						it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_voce_f_sing_contoBulk.class),
						null,
						"doStampa",
						null,
						bp);
			slbp.setMultiSelection(true);
			context.addHookForward("annulla_seleziona", this, "doAnnullaSelezioneCapitolo");
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la chiusura del pannello di ricerca
 */

public Forward doCloseForm(ActionContext context) {

	try {
		return doConfirmCloseForm(context, OptionBP.YES_BUTTON);
	} catch (BusinessProcessException e) {
		return handleException(context, e);
	}
}
/**
 * Riporta nella spesa la scadenza selezionata
 */
public Forward doStampa(ActionContext context) {

	//context.addHookForward("close", this, "doStampaAnnullata");
	try {

		RicercaSingoloContoBP bp = (RicercaSingoloContoBP)context.getBusinessProcess();
		if (bp.getUserTransaction() == null)
			throw new BusinessProcessException("Impossibile stampare! Contesto NON transazionale");
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp(), new Object[] { "Th" });
		printbp.setId_report_generico(bp.getPg_stampa());
		printbp.setReportName("/doccont/doccont/situaz_singolo_conto.jasper");
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("aidrpt");
		param.setValoreParam(bp.getPg_stampa().toString());
		param.setParamType("java.lang.Long");
		printbp.addToPrintSpoolerParam(param);
		
		context.addHookForward("close", this, "doAnnullaStampa");
		return context.addBusinessProcess(printbp);
		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
	public Forward doOnFl_enteChange(ActionContext context){
		try {
			fillModel(context);
			RicercaSingoloContoBP bp = (RicercaSingoloContoBP)getBusinessProcess(context);
			V_voce_f_sing_contoBulk filtro = (V_voce_f_sing_contoBulk)bp.getModel();
			if(!filtro.isEnteInScrivania() && filtro.getFl_ente() != null && filtro.getFl_ente()) {
				filtro.setTi_gestione(SospesoBulk.TIPO_SPESA);
				filtro.setElemento_voce(null);
				filtro.setVoce_f(null);
				bp.completeSearchTools(context,bp);
			}
		}catch (Throwable t) {
			return handleException(context, t);
}
		return context.findDefaultForward();
	}
	public Forward  doOnTi_gestioneChange(ActionContext context){
		try {
			fillModel(context);
			RicercaSingoloContoBP bp = (RicercaSingoloContoBP)getBusinessProcess(context);
			V_voce_f_sing_contoBulk filtro = (V_voce_f_sing_contoBulk)bp.getModel();
			filtro.setElemento_voce(null);
			filtro.setVoce_f(null);
			bp.completeSearchTools(context,bp);
			
		}catch (Throwable t) {
			return handleException(context, t);
		}
		return context.findDefaultForward();
	}
	public Forward  doOnFl_partita_giroChange(ActionContext context){
		try {
			fillModel(context);
			RicercaSingoloContoBP bp = (RicercaSingoloContoBP)getBusinessProcess(context);
			V_voce_f_sing_contoBulk filtro = (V_voce_f_sing_contoBulk)bp.getModel();
			if(filtro.getFl_partita_giro()!=null && filtro.getFl_partita_giro().booleanValue()){
				filtro.setVoce_f(null);
			}
				bp.completeSearchTools(context,bp);
				
		}catch (Throwable t) {
			return handleException(context, t);
		}
		return context.findDefaultForward();
	}
}
