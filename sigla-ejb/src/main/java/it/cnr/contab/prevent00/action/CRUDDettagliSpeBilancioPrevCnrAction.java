package it.cnr.contab.prevent00.action;
import it.cnr.contab.prevent00.bp.CRUDDettagliSpeBilancioPrevCnrBP;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * Action di gestione dettagli di spesa del bilancio preventivo CNR
 */

public class CRUDDettagliSpeBilancioPrevCnrAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDDettagliSpeBilancioPrevCnrAction() {
	super();
}

/**
 * Gestione della cancellazione del dettaglio di spesa CNR
 *
 * @param context	L'ActionContext della richiesta
 */

public Forward doElimina(ActionContext context) throws java.rmi.RemoteException
{
	CRUDDettagliSpeBilancioPrevCnrBP bp = (CRUDDettagliSpeBilancioPrevCnrBP)getBusinessProcess(context);
	Voce_f_saldi_cmpBulk dettaglio = (Voce_f_saldi_cmpBulk) bp.getModel();
	
	if(dettaglio.getFl_sola_lettura().booleanValue())
	{
		setMessage(context, 0, "Dettaglio non eliminabile !");		
	}
	else
	{	
		try
		{		
			super.doElimina(context);
		}			
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}	
	return context.findDefaultForward();
}
}