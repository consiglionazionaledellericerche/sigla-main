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

public class RicercaUOAction extends AbstractAction {

	public RicercaUOAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaUOBP bp = null;
		try {
			((HttpActionContext)actioncontext).getSession(true).setMaxInactiveInterval(350);
			String user;
			bp = (RicercaUOBP)actioncontext.createBusinessProcess("RicercaUOBP");
			actioncontext.addBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"query");
			valorizzaParametri(actioncontext,bp,"dominio");
			valorizzaParametri(actioncontext,bp,"numMax");
			valorizzaParametri(actioncontext,bp,"user");
			valorizzaParametri(actioncontext,bp,"ricerca");
			if (bp.getUser()!= null)
				user = bp.getUser();
			else
				user = "MACRO";	
			
			CNRUserInfo ui = new CNRUserInfo();
			UtenteBulk utente = new UtenteBulk();
			utente.setCd_utente(user);
			ui.setUserid(utente.getCd_utente());
			ui.setEsercizio(new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
			ui.setUtente(utente);
			actioncontext.setUserInfo(ui);
			String valore = ((HttpActionContext)actioncontext).getParameter("cds");
			if (valore != null && !valore.trim().equals(""))
				actioncontext.setUserContext(new CNRUserContext(user,actioncontext.getSessionId(),new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,valore,null));
			else
				actioncontext.setUserContext(new CNRUserContext(user,actioncontext.getSessionId(),new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null));
			bp.eseguiRicerca(actioncontext);
			
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_SIP_100);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaUOBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
