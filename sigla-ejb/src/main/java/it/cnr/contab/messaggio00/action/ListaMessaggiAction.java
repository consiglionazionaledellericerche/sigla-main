package it.cnr.contab.messaggio00.action;

import it.cnr.contab.messaggio00.bulk.*;
import it.cnr.contab.messaggio00.bp.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (10/09/2002 16:35:53)
 * @author: CNRADM
 */
public class ListaMessaggiAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
/**
 * ListaMessaggiAction constructor comment.
 */
public ListaMessaggiAction() {
	super();
}
public Forward doBringBack(ActionContext context) throws BusinessProcessException {
	return context.findDefaultForward();
}
public Forward doRimuoviCorrente(ActionContext context) throws BusinessProcessException {
	ListaMessaggiBP bp = (ListaMessaggiBP)context.getBusinessProcess();
	bp.saveSelection(context);
	it.cnr.contab.messaggio00.bulk.MessaggioBulk messaggio = (it.cnr.contab.messaggio00.bulk.MessaggioBulk)bp.getFocusedElement();
	if (messaggio != null) 
		try {
			bp.createComponentSession().leggiMessaggi(context.getUserContext(),new it.cnr.contab.messaggio00.bulk.MessaggioBulk[] { messaggio });
			bp.refresh(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	return context.findDefaultForward();
}
public Forward doRimuoviSelezionati(ActionContext context) throws BusinessProcessException {
	ListaMessaggiBP bp = (ListaMessaggiBP)context.getBusinessProcess();
	bp.saveSelection(context);
	java.util.List messaggi = bp.getSelectedElements(context);
	
	if (!messaggi.isEmpty()) 
		try {
			bp.createComponentSession().leggiMessaggi(context.getUserContext(),(MessaggioBulk[])messaggi.toArray(new MessaggioBulk[messaggi.size()]));
			bp.refresh(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	return context.findDefaultForward();
}
public Forward doSort(ActionContext actioncontext, String s, String s1)
	throws BusinessProcessException {
	if (s1.equals("iconaOpenClose"))
		return super.doSort(actioncontext, s, "visionato");
	return super.doSort(actioncontext, s, s1);
}

}
