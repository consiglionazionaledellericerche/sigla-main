package it.cnr.contab.segnalazioni00.action;

import java.rmi.RemoteException;

import it.cnr.contab.segnalazioni00.bp.CRUDAttivitaSiglaBP;
import it.cnr.contab.segnalazioni00.bulk.Attivita_siglaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.OptionBP;

public class CRUDAttivitaSiglaAction extends CRUDAction {

	public Forward doSalva(ActionContext context) throws RemoteException {
		try {
			if (context.getBusinessProcess() instanceof CRUDAttivitaSiglaBP) {
				CRUDAttivitaSiglaBP bp = (CRUDAttivitaSiglaBP)getBusinessProcess(context);
			Attivita_siglaBulk attivita = (Attivita_siglaBulk)bp.getModel();
			fillModel( context );
			bp.validaAttivita(context, attivita);	
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
		
		return super.doSalva(context);
	}
	
}
