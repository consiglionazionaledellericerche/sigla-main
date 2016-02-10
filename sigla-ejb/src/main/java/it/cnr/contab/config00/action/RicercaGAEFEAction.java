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

public class RicercaGAEFEAction extends AbstractAction {

	public RicercaGAEFEAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaGAEFEBP bp = null;
		try {
			((HttpActionContext)actioncontext).getSession(true).setMaxInactiveInterval(350);
			String user;
			bp = (RicercaGAEFEBP)actioncontext.createBusinessProcess("RicercaGAEFEBP");
			actioncontext.addBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"cdr");
			valorizzaParametri(actioncontext,bp,"modulo");
			valorizzaParametri(actioncontext,bp,"user");
			if (bp.getUser()!= null)
				user = bp.getUser();
			else
				user = "MACRO";	
			
			CNRUserInfo ui = new CNRUserInfo();
			UtenteBulk utente = new UtenteBulk();
			utente.setCd_utente(user);
			ui.setUserid(utente.getCd_utente());
			if(bp.getEsercizio()!=null)
				ui.setEsercizio(new Integer(bp.getEsercizio()));
			else
				ui.setEsercizio(new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));

			ui.setUtente(utente);
			actioncontext.setUserInfo(ui);			
			actioncontext.setUserContext(new CNRUserContext(user,actioncontext.getSessionId(),ui.getEsercizio(),null,null,null));
			
			bp.eseguiRicerca(actioncontext);
			
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_SIP_100);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaGAEFEBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
