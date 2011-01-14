package it.cnr.contab.utenze00.action;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.utente00.nav.ejb.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;

import javax.servlet.http.HttpSessionBindingEvent;

public class SelezionatoreCdrAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
public SelezionatoreCdrAction() {
	super();
}
public Forward basicDoBringBack(ActionContext context) throws BusinessProcessException {
	try {
		SelezionatoreCdrBP bp = (SelezionatoreCdrBP)context.getBusinessProcess();
		bp.selezionaCdr(context);
		return context.findForward("desktop");
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public static GestioneLoginComponentSession getComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
	return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class);
}
}
