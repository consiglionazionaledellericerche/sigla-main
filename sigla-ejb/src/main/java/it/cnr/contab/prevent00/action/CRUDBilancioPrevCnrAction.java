package it.cnr.contab.prevent00.action;
import it.cnr.contab.prevent00.bp.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.jada.action.*;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per
 * la gestione del preventivo finanziario CNR
 * @author: Paola sala
 */
public class CRUDBilancioPrevCnrAction extends it.cnr.jada.util.action.BulkAction {
public CRUDBilancioPrevCnrAction() 
{
	super();
}

/**
 * Gestione dell'approvazione del bilancio preventivo
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doApprovaBilancioCnr(ActionContext context) 
{
	try 
	{
		fillModel(context);
		it.cnr.jada.UserContext userContext = context.getUserContext();		
		CRUDBilancioPrevCnrBP bp = (CRUDBilancioPrevCnrBP)context.getBusinessProcess();
		Bilancio_preventivoBulk bilancioPrev = (Bilancio_preventivoBulk) bp.getModel();

		// verifico se il bilancio e' in stato B
		bilancioPrev.verificaSeAmmessaApprovazione();
		
		// metto lo stato del bilancio a 'C'		
		bilancioPrev = bp.createBilancioPreventivoComponentSession().approvaBilancioPreventivo(userContext, bilancioPrev);	

		if(bilancioPrev.getDt_approvazione() != null)
		{
			setMessage(context,0, "Bilancio approvato !");
		} 		
		bp.setModel(context,bilancioPrev);
		
		return context.findDefaultForward();
	} 
	catch(Throwable e) 
	{
		return handleException(context,e);
	}	
}

/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doApriStampe(it.cnr.jada.action.ActionContext context) {
		try {
//			throw new it.cnr.jada.comp.ApplicationException("Stampa non supportata");
			fillModel(context);
			it.cnr.contab.prevent00.bp.BilancioStampePreventivoBP bp = (it.cnr.contab.prevent00.bp.BilancioStampePreventivoBP)context.createBusinessProcess("BilancioStampePreventivoBP");
			bp.setModel(context, ((it.cnr.jada.util.action.BulkBP)context.getBusinessProcess()).getModel());
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

/**
 * Gestione dei dettagli di entrata
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doGestioneDettagliEntrate(ActionContext context) 
{
	try 
	{
		CRUDBilancioPrevCnrBP bp = (CRUDBilancioPrevCnrBP)context.getBusinessProcess();		
		CRUDDettagliEtrBilancioPrevCnrBP bpDett;
		 
		if(bp.isEditable() && !((Bilancio_preventivoBulk)bp.getModel()).getStato().equals(Bilancio_preventivoBulk.STATO_C))
		{			
			// MODIFICA - Creo il business process del dettaglio ENTRATA del bilancio preventivo
			bpDett = (CRUDDettagliEtrBilancioPrevCnrBP)context.createBusinessProcess("CRUDDettagliEtrBilancioPrevCnrBP", new Object[] { "M" });
		}
		else
		{
			// VISUALIZZA - Creo il business process del dettaglio ENTRATA del bilancio preventivo
			bpDett = (CRUDDettagliEtrBilancioPrevCnrBP)context.createBusinessProcess("CRUDDettagliEtrBilancioPrevCnrBP", new Object[] { "V" });				
		}
		return context.addBusinessProcess(bpDett);
	} 
	catch(Throwable e) 
	{ 
		return handleException(context,e);
	}
}

/**
 * Gestione dei dettagli di spesa
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doGestioneDettagliSpese(ActionContext context) 
{
	try 
	{
		CRUDBilancioPrevCnrBP bp = (CRUDBilancioPrevCnrBP)context.getBusinessProcess();		
		CRUDDettagliSpeBilancioPrevCnrBP bpDett;
		 
		if(bp.isEditable() && !((Bilancio_preventivoBulk)bp.getModel()).getStato().equals(Bilancio_preventivoBulk.STATO_C))
		{			
			// MODIFICA - Creo il business process del dettaglio SPESA del bilancio preventivo
			bpDett = (CRUDDettagliSpeBilancioPrevCnrBP)context.createBusinessProcess("CRUDDettagliSpeBilancioPrevCnrBP", new Object[] { "M" });
		}
		else
		{
			// VISUALIZZA - Creo il business process del dettaglio SPESA del bilancio preventivo
			bpDett = (CRUDDettagliSpeBilancioPrevCnrBP)context.createBusinessProcess("CRUDDettagliSpeBilancioPrevCnrBP", new Object[] { "V" });				
		}
		return context.addBusinessProcess(bpDett);
	} 
	catch(Throwable e) 
	{ 
		return handleException(context,e);
	}
}

/**
 * Gestione della produzione del bilancio finanziario CNR
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doProduceBilancioCnr(ActionContext context) 
{
	CRUDBilancioPrevCnrBP bp = (CRUDBilancioPrevCnrBP)context.getBusinessProcess();
	try
	{
		bp.predisponeBilancioPreventivoCNR(context);
		bp.setMessage("Operazione completata");
	} 
	catch(Throwable e) 
	{ 
		return handleException(context,e);
	}	
	return(context.findDefaultForward());	
}

/**
 * Gestione variazioni di bilancio CNR 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doVariazioniBilancioCnr(ActionContext context) 
{
	return(context.findDefaultForward());	
}
}