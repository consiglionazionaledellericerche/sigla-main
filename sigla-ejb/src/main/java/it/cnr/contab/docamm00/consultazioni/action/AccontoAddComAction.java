package it.cnr.contab.docamm00.consultazioni.action;

import java.rmi.RemoteException;
import javax.ejb.RemoveException;

import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk;
import it.cnr.contab.docamm00.consultazioni.bp.ConsAccontoAddComBP;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.FormBP;

public class AccontoAddComAction extends it.cnr.jada.util.action.BulkAction{
public AccontoAddComAction() {
	super();
}

/**
 * Gestisce il cambiamento del mese impostando le relative dati inizio e fine
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	it.cnr.contab.docamm00.consultazioni.bp.AccontoAddComBP bp= (it.cnr.contab.docamm00.consultazioni.bp.AccontoAddComBP) context.getBusinessProcess();
	try {
		bp.fillModel(context); 
		Acconto_classific_coriBulk acconto = (Acconto_classific_coriBulk) bp.getModel();
		ConsAccontoAddComBP consBP = (ConsAccontoAddComBP)context.createBusinessProcess("ConsAccontoAddComBP",new Object[] {"Tr"});
		context.addBusinessProcess(consBP);
		consBP.openIterator(context,acconto);
	    return context.findDefaultForward();
	} catch (Exception e) {
			return handleException(context,e); 
	} 
}
public Forward doCalcola(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	it.cnr.contab.docamm00.consultazioni.bp.AccontoAddComBP bp= (it.cnr.contab.docamm00.consultazioni.bp.AccontoAddComBP) context.getBusinessProcess();
	try {
		bp.fillModel(context); 
		Acconto_classific_coriBulk acconto = (Acconto_classific_coriBulk) bp.getModel();
		bp.validaPercentuale(context,acconto);
		bp.calcola(context, acconto);
		setMessage(context, FormBP.WARNING_MESSAGE, "Operazione completata. Visualizzare i dati utilizzando l'apposito pulsante.");
	    return context.findDefaultForward();
	} catch (Exception e) {
			return handleException(context,e); 
	} 
}
public Forward doOnPercentualeChange(ActionContext context) {
	try {
		it.cnr.contab.docamm00.consultazioni.bp.AccontoAddComBP bp= (it.cnr.contab.docamm00.consultazioni.bp.AccontoAddComBP) context.getBusinessProcess();
		Acconto_classific_coriBulk acconto = (Acconto_classific_coriBulk) bp.getModel();
		try {
			bp.fillModel(context);
		} catch(it.cnr.jada.bulk.FillException e) {
			acconto.setPercentuale(null);
			bp.setModel(context,acconto);
			throw e; 
		}	
	} catch(Throwable e) {
		return handleException(context, e);
	}		

	return context.findDefaultForward();
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
}
