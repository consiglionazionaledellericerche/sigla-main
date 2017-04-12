package it.cnr.contab.docamm00.consultazioni.action;

import it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagBP;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmAnagManrevBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkAction;

public class ConsDocammAnagFitAction extends BulkAction {


	public Forward doCerca(ActionContext context){
		it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagFitBP bp= (it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagFitBP) context.getBusinessProcess();
		try {
			bp.fillModel(context); 
			VDocAmmAnagManrevBulk bulk = (VDocAmmAnagManrevBulk) bp.getModel();
		if (bulk.getAnagrafico()!=null && bulk.getAnagrafico().getCd_anag()!=null){
			bulk.setCdAnag(bulk.getAnagrafico().getCd_anag());
			ConsDocammAnagBP consBP = (ConsDocammAnagBP)context.createBusinessProcess("ConsDocammAnagBP");
			context.addBusinessProcess(consBP);
			consBP.openIterator(context,bulk);
		    return context.findDefaultForward();
		}else
			throw new it.cnr.jada.comp.ApplicationException("Attenzione bisogna indicare un anagrafico!");
			
		} catch (Exception e) {
				return handleException(context,e); 
		} 
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