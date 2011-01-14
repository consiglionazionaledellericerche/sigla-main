/*
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.action;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bp.CRUDConfigParametriCdsBP;
import it.cnr.contab.config00.bp.CRUDConfigParametriLivelliBP;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDAction;

/**
 * @author fgiardina
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigParametriCdsAction extends CRUDAction {

	/**
	 * Construtor from superclass
	 *
	 */
	public CRUDConfigParametriCdsAction() {
		super();
	}
	
	
	public Forward doSalva(ActionContext context) throws RemoteException {
		try {	
			CRUDConfigParametriCdsBP bp = (CRUDConfigParametriCdsBP)getBusinessProcess(context);
			Parametri_cdsBulk parCds = (Parametri_cdsBulk)bp.getModel();
			fillModel( context );
			bp.controlloCdrEntrata(context,parCds);
			bp.controlloCdrSpesa(context,parCds);	
			bp.controlloLineaEntrata(context,parCds);
			bp.controlloLineaSpesa(context,parCds);

		} catch(Throwable e) {
			return handleException(context, e);
		}
		return super.doSalva(context);
	}

}
