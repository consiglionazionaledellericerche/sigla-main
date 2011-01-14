/*
 * Created on Oct 21, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import it.cnr.contab.prevent01.bp.CRUDStatoCdrPdGPBP;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDStatoCdrPdGPAction extends CRUDAction  {

	public Forward doCambiaStato( ActionContext context)
	{
		try 
		{
			CRUDStatoCdrPdGPBP bp = (CRUDStatoCdrPdGPBP) getBusinessProcess(context);
			boolean modified = fillModel(context);
			bp.cambiaStati(context, true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doRiportaStatoPrecedente( ActionContext context)
	{
		try 
		{
			CRUDStatoCdrPdGPBP bp = (CRUDStatoCdrPdGPBP) getBusinessProcess(context);
			boolean modified = fillModel(context);
			bp.cambiaStati(context, false);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
