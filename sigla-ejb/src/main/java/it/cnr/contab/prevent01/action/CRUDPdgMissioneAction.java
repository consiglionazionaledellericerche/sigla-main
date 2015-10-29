/*
 * Created on Oct 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import it.cnr.contab.prevent01.bp.CRUDPdgMissioneBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class CRUDPdgMissioneAction extends it.cnr.jada.util.action.CRUDAction {
	private static final long serialVersionUID = 1L;

	/**
	 * CRUDAnagraficaAction constructor comment.
	 */
	public CRUDPdgMissioneAction() {
		super();
	}

	public Forward doAggiungiTipoUo(ActionContext context) {
		try	{
			CRUDPdgMissioneBP bp = (CRUDPdgMissioneBP)context.getBusinessProcess();
			bp.addToAssPdgMissioneTipiUo(context);
			return context.findDefaultForward();
		} catch(Exception ex) {
			return handleException(context,ex);
		}
	}


	public Forward doRimuoviTipoUo(ActionContext context) {
		try	{
			CRUDPdgMissioneBP bp = (CRUDPdgMissioneBP)context.getBusinessProcess();
			bp.removeFromAssPdgMissioneTipiUo(context);
			return context.findDefaultForward();
		} catch(Exception ex) {
			return handleException(context,ex);
		}
	}
}
