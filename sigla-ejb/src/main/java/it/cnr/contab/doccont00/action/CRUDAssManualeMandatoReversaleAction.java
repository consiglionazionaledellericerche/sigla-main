package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
/**
 * Insert the type's description here.
 * Creation date: (20/11/2002 12.27.20)
 * @author: Roberto Fantino
 */
public class CRUDAssManualeMandatoReversaleAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDMandatoReversaliAction constructor comment.
 */
public CRUDAssManualeMandatoReversaleAction() {
	super();
}
/**
 * Associa una o più reversali al mandato selezionato
 *
**/
public Forward doAggiungiReversali(ActionContext context) {

	try{
		fillModel(context);
		CRUDAssManualeMandatoReversaleBP bp = (CRUDAssManualeMandatoReversaleBP)context.getBusinessProcess();
		for (java.util.Iterator i = bp.getReversaliDisponibiliCRUDController().iterator();i.hasNext();)
			((OggettoBulk)i.next()).setUser(context.getUserContext().getUser());
		bp.getReversaliDisponibiliCRUDController().remove(context);
		bp.getReversaliAssociateCRUDController().reset(context);
		return context.findDefaultForward();

	} catch(FillException ex) {
		return handleException(context, ex);
	} catch(ValidationException ex) {
		return handleException(context, ex);
	} catch(BusinessProcessException ex) {
		return handleException(context, ex);
	}
}
/**
 * Rimove una o più reversali al mandato selezionato
 *
**/
public Forward doRimuoviReversali(ActionContext context) {

	try{
		fillModel(context);
		CRUDAssManualeMandatoReversaleBP bp = (CRUDAssManualeMandatoReversaleBP)context.getBusinessProcess();
		for (java.util.Iterator i = bp.getReversaliAssociateCRUDController().iterator();i.hasNext();)
			((OggettoBulk)i.next()).setUser(context.getUserContext().getUser());
		bp.getReversaliAssociateCRUDController().remove(context);
		bp.getReversaliDisponibiliCRUDController().reset(context);
		return context.findDefaultForward();

	} catch(FillException ex) {
		return handleException(context, ex);
	} catch(ValidationException ex) {
		return handleException(context, ex);
	} catch(BusinessProcessException ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doRiportaSelezione(ActionContext context)  throws java.rmi.RemoteException {
	
	CRUDAssManualeMandatoReversaleBP bp = (CRUDAssManualeMandatoReversaleBP)context.getBusinessProcess();
	HookForward caller = (HookForward)context.getCaller();
	MandatoIBulk mandato = (MandatoIBulk)caller.getParameter("focusedElement");

	return super.doRiportaSelezione(context);
}
}
