package it.cnr.contab.prevent00.action;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.prevent00.bp.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.jada.action.*;

/**
 * Action di gestione dettagli di entrata del bilancio preventivo CDS
 */

public class CRUDDettagliEtrBilancioPrevCdsAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDDettagliEtrBilancioPrevCdsAction() {
	super();
}

/**
 * Gestione della cancellazione del dettaglio di entrata CDS
 *
 * @param context	L'ActionContext della richiesta
 */

public Forward doElimina(ActionContext context) throws java.rmi.RemoteException
{
	CRUDDettagliEtrBilancioPrevCdsBP bp = (CRUDDettagliEtrBilancioPrevCdsBP)getBusinessProcess(context);
	Voce_f_saldi_cmpBulk dettaglio = (Voce_f_saldi_cmpBulk) bp.getModel();
	
	if(dettaglio.getFl_sola_lettura().booleanValue())
	{
		setMessage(context,0, "Dettaglio non eliminabile !");		
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