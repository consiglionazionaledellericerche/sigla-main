package it.cnr.contab.ordmag.ordini.action;

import java.rmi.RemoteException;

import it.cnr.contab.docamm00.bp.CRUDDocumentoGenericoPassivoBP;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP;
import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;

public class CRUDEvasioneOrdineAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDEvasioneOrdineAction() {
        super();
    }
public Forward doCercaConsegneDaEvadere(ActionContext context) 
{
	try 
	{
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
		fillModel( context );
		EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) bp.getModel();
		bp.cercaConsegne(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}

@Override
public Forward doSalva(ActionContext actioncontext) throws RemoteException {
	try
	{
		fillModel(actioncontext);
		gestioneSalvataggio(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}
private void gestioneSalvataggio(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
	CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)actioncontext.getBusinessProcess();
	it.cnr.jada.util.action.Selection selection = bp.getConsegne().getSelection();
	EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) bp.getModel();
	java.util.List consegne = bp.getConsegne().getDetails();
	bulk.setRigheConsegnaSelezionate(new BulkList<>());
	for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
		OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)consegne.get(i.nextIndex());
		bulk.getRigheConsegnaSelezionate().add(consegna);
	}
	bp.evadiConsegne(actioncontext);
	bulk.setRigheConsegnaDaEvadereColl(new BulkList<>());
}

public Forward doOnQuantitaChange(ActionContext context) {

	try{	
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
		fillModel(context);
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context, e);
	}
}
public Forward doSelectConsegneDaEvadere(ActionContext context) {

	CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
    try {
        bp.getConsegne().setSelection(context);
    } catch (Throwable e) {
		return handleException(context, e);
    }

	OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	consegna.setQuantitaOriginaria(consegna.getQuantita());
	return context.findDefaultForward();
}
public Forward doDeselectConsegneDaEvadere(ActionContext context) {

	CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
    try {
        bp.getConsegne().setSelection(context);
    } catch (Throwable e) {
		return handleException(context, e);
    }

	OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	consegna.setQuantita(consegna.getQuantitaOriginaria());
	return context.findDefaultForward();
}
}
