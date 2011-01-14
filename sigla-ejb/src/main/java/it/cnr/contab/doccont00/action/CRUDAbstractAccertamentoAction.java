package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (03/07/2003 16.14.44)
 * @author: Simonetta Costa
 */
public abstract class CRUDAbstractAccertamentoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDAbstractObbligazioneAction constructor comment.
 */
public CRUDAbstractAccertamentoAction() {
	super();
}
public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException 
{

	CRUDVirtualAccertamentoBP bp = (CRUDVirtualAccertamentoBP)context.getBusinessProcess();

	try 
	{
		fillModel(context);
		bp.riportaAvanti(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doRiportaIndietro(ActionContext context) throws java.rmi.RemoteException 
{

	CRUDVirtualAccertamentoBP bp = (CRUDVirtualAccertamentoBP)context.getBusinessProcess();

	try 
	{
		fillModel(context);
		bp.riportaIndietro(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
}
