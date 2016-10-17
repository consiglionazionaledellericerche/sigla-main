/*
 * Created on Apr 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import it.cnr.contab.prevent01.bp.StampaPdgpBilancioBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaPdgpBilancioAction extends it.cnr.contab.reports.action.ParametricPrintAction {

	/**
	 * 
	 */
	public StampaPdgpBilancioAction() {
		super();
	}

	public Forward doOnTipoChange(ActionContext context) {
		try{
			StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
			fillModel(context);
			try {
				bp.loadModelBulkOptions(context);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	
	public Forward doOnFonteChange(ActionContext context) {
		try{
			StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
			fillModel(context);
			try {
				bp.loadModelBulkOptions(context);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
}
