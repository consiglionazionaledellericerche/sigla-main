/*
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.action;

import java.rmi.RemoteException;

import it.cnr.contab.doccont00.bp.CRUDCupBP;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDCupAction extends CRUDAction {

	
	public CRUDCupAction() {
		super();
	}
	
	
	public Forward doSalva(ActionContext context) throws RemoteException {
		try {
			if (context.getBusinessProcess() instanceof CRUDCupBP) {
			CRUDCupBP bp = (CRUDCupBP)getBusinessProcess(context);
			CupBulk cup = (CupBulk)bp.getModel();
			fillModel( context );
			bp.validainserimento(context, cup);	
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
		
		return super.doSalva(context);
	}
	
	public Forward doElimina(ActionContext actioncontext) throws RemoteException{
		try{
			if (actioncontext.getBusinessProcess() instanceof CRUDCupBP)
				return super.doElimina(actioncontext);
			
	    }
	    catch(Throwable throwable){
	        return handleException(actioncontext, throwable);
	    }
	    return super.doElimina(actioncontext);
}
	
}


