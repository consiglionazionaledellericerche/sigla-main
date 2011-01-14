package it.cnr.contab.config00.action;

/**
 * Action che gestisce il cambiamento di stato dell'esercizio contabile.
 */
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

public class CRUDEsercizioAction extends CRUDAction {
/**
 * Metodo che permette di cambiare lo stato dell'esercizio contabile.
 * @param  context <code>ActionContext</code> in uso.
 * @return Forward Oggetto restituito dal metodo <code>handleException</code>,
 *				   che gestisce le principali eccezioni.
 */
public Forward doApriPdG( ActionContext context)
{
	try 
	{
		boolean modified = fillModel(context);
		((it.cnr.contab.config00.bp.CRUDConfigEsercizioBP) getBusinessProcess(context)).apriPdg(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}

	
}
/**
 * Metodo che permette di cambiare lo stato dell'esercizio contabile.
 * @param  context <code>ActionContext</code> in uso.
 * @return Forward Oggetto restituito dal metodo <code>handleException</code>,
 *				   che gestisce le principali eccezioni.
 */
public Forward doCambiaStato( ActionContext context)
{
	try 
	{
		boolean modified = fillModel(context);
		((it.cnr.contab.config00.bp.CRUDConfigEsercizioBP) getBusinessProcess(context)).cambiaStato(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}

	
}
}
