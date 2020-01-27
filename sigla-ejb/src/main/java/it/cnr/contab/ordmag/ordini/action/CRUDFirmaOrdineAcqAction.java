package it.cnr.contab.ordmag.ordini.action;

import java.rmi.RemoteException;

import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;

public class CRUDFirmaOrdineAcqAction extends CRUDOrdineAcqAction {

public CRUDFirmaOrdineAcqAction() {
        super();
    }
public Forward doFirmaOrdine(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
		if (ordine.isStatoAllaFirma()){
			ordine.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
			java.sql.Timestamp dataReg = null;
			try {
				dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}

			ordine.setDataOrdineDef(dataReg);
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
		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
		if (ordine.isStatoAllaFirma()){
			ordine.setStato(OrdineAcqBulk.STATO_IN_APPROVAZIONE);
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
