package it.cnr.contab.anagraf00.action;

import java.rmi.RemoteException;

import it.cnr.contab.anagraf00.bp.CRUDTipologieIstatBP;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;

/**
 * Insert the type's description here.
 * Creation date: (24/04/2007)
 * @author: fgiardina
 */
public class CRUDTipologieIstatAction extends CRUDAction {

public CRUDTipologieIstatAction() {
	super();
}


public Forward doSalva(ActionContext context) throws RemoteException {
	try {	
		CRUDTipologieIstatBP bp = (CRUDTipologieIstatBP)getBusinessProcess(context);
		Tipologie_istatBulk tipologieIstat = (Tipologie_istatBulk)bp.getModel();
		fillModel( context );
		bp.validainserimento(context, tipologieIstat);
		
	} catch(Throwable e) {
		return handleException(context, e);
	}
	return super.doSalva(context);
}


}

