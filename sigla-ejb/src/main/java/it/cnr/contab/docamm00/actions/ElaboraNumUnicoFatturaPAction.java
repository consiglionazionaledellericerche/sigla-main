package it.cnr.contab.docamm00.actions;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.RemoveException;

import it.cnr.contab.docamm00.docs.bulk.ElaboraNumUnicoFatturaPBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.BulkBP;

public class ElaboraNumUnicoFatturaPAction extends it.cnr.jada.util.action.BulkAction{
public ElaboraNumUnicoFatturaPAction() {
	super();
}

/**
 * Gestisce il cambiamento del mese impostando le relative dati inizio e fine
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doElabora(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	 it.cnr.contab.docamm00.bp.ElaboraNumUnicoFatturaPBP bp= ( it.cnr.contab.docamm00.bp.ElaboraNumUnicoFatturaPBP) context.getBusinessProcess();
	try {
		bp.fillModel(context); 
		bp.inserisci(context,(ElaboraNumUnicoFatturaPBulk) bp.getModel());
		bp.setMessage("Progressivo univoco assegnato.");
		return context.findDefaultForward();
	} catch (Exception e) {
			return handleException(context,e); 
	} 
}

public Forward doCloseForm(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
				return doConfirmCloseForm(actioncontext, 4);
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}
public Forward doPrint(ActionContext context) {
	try { 
		BulkBP bp = (BulkBP)context.getBusinessProcess(); 
		bp.setDirty(false); 
		it.cnr.contab.reports.bp.OfflineReportPrintBP printbp = (it.cnr.contab.reports.bp.OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/docamm/docamm/registroFatturePassive.jasper");
		
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk(); 
		param.setNomeParam("Esercizio");
		param.setValoreParam(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);	
		
		return context.addBusinessProcess(printbp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
