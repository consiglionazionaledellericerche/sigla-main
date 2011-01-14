package it.cnr.contab.chiusura00.action;

import it.cnr.contab.chiusura00.bulk.*;
import it.cnr.contab.chiusura00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Action che gestisce l'annullamento massivo di documenti contabili
 */

public class AnnullamentoDocContAction extends it.cnr.jada.util.action.BulkAction {
public AnnullamentoDocContAction() {
	super();
}
/**
 * L'utente ha annullato la selezione dei documenti contabili fatta in precedenza
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doAnnullaSelezione(ActionContext context)  throws BusinessProcessException
{
	return context.findDefaultForward();
}
/**
 * L'utente ha richiesto la ricerca di documenti contabili idonei all'annullamento
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward  doCercaDocDaAnnullare( it.cnr.jada.action.ActionContext context )
{
	try 
	{
		
		fillModel(context);
		AnnullamentoBP bp = (AnnullamentoBP) context.getBusinessProcess();
		V_obb_acc_xxxBulk model = (V_obb_acc_xxxBulk)bp.getModel();
//		model.validate();
		it.cnr.jada.util.RemoteIterator ri = bp.cercaDocDaAnnullare(context,model);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		}
		else
		{

			bp.setModel(context,model);
			SelezionatoreListaBP nbp = select(context,
												ri,
												it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_obb_acc_xxxBulk.class),
												null,"doRiportaSelezione",null,(AnnullamentoBP)bp);

			nbp.setMultiSelection( true );
			context.addHookForward("annulla_seleziona",this,"doAnnullaSelezione");			
			context.addHookForward("close",this,"doDefault");			
			return context.findDefaultForward();
			
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * L'utente ha confermato di voler procedere con l'annullamento dei documenti contabili
 * selezionati
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param option	
 * @return Il Forward alla pagina di risposta
 */
public Forward doConfermaAnnullamento(ActionContext context, int option)
{
	try
	{
		AnnullamentoBP bp = (AnnullamentoBP)context.getBusinessProcess();		
		if (option == OptionBP.YES_BUTTON) 
			bp.confermaAnnullamento(context);		
		else
			bp.rollbackUserTransaction();
			
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * L'utente ha confermato la selezione effettuata dei documenti contabili
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doRiportaSelezione(ActionContext context)  throws BusinessProcessException
{
	
	return openConfirm(context,"Attenzione tutti i documenti selezionati verranno annullati. Vuoi procedere?",OptionBP.CONFIRM_YES_NO,"doConfermaAnnullamento");

}
}
