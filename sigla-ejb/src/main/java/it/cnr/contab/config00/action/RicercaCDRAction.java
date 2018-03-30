package it.cnr.contab.config00.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

import it.cnr.contab.config00.bp.*;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bp.GestioneUtenteBP;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.AbstractAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;

public class RicercaCDRAction extends AbstractAction {

	public RicercaCDRAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaCDRBP bp = null;
		try {
			bp = (RicercaCDRBP)actioncontext.createBusinessProcess("RicercaCDRBP");
			actioncontext.setBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"uo");
			valorizzaParametri(actioncontext,bp,"query");
			valorizzaParametri(actioncontext,bp,"dominio");
			valorizzaParametri(actioncontext,bp,"numMax");
			valorizzaParametri(actioncontext,bp,"user");
			valorizzaParametri(actioncontext,bp,"ricerca");
			bp.eseguiRicerca(actioncontext);
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_SIP_100);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaCDRBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
