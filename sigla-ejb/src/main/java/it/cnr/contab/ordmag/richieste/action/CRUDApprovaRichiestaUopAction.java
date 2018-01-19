package it.cnr.contab.ordmag.richieste.action;

import java.rmi.RemoteException;

import it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;

public class CRUDApprovaRichiestaUopAction extends CRUDRichiestaUopAction {

public CRUDApprovaRichiestaUopAction() {
        super();
    }
public Forward doInviaOrdine(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		RichiestaUopBulk richiesta = (RichiestaUopBulk) bp.getModel();
		if (richiesta.isDefinitiva()){
			richiesta.setStato(RichiestaUopBulk.STATO_INVIATA_ORDINE);
			java.sql.Timestamp dataReg = null;
			try {
				dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
				richiesta.setDataInvio(dataReg);
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}
		}
		getBusinessProcess(actioncontext).save(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}

public Forward doSblocca(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		RichiestaUopBulk richiesta = (RichiestaUopBulk) bp.getModel();
		if (richiesta.isDefinitiva()){
			richiesta.setStato(RichiestaUopBulk.STATO_INSERITA);
		}
		getBusinessProcess(actioncontext).save(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}

}
