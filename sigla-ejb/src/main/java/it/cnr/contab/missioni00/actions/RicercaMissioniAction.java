package it.cnr.contab.missioni00.actions;

import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.missioni00.bp.RicercaMissioniBP;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.AbstractAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

public class RicercaMissioniAction extends AbstractAction {

	public RicercaMissioniAction () {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaMissioniBP bp = null;
		try {
			bp = (RicercaMissioniBP)actioncontext.createBusinessProcess("RicercaMissioniBP");
			actioncontext.setBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"query");
			valorizzaParametri(actioncontext,bp,"dominio");
			valorizzaParametri(actioncontext,bp,"uo");
			valorizzaParametri(actioncontext,bp,"terzo");
			valorizzaParametri(actioncontext,bp,"voce");
			valorizzaParametri(actioncontext,bp,"cdr");
			valorizzaParametri(actioncontext,bp,"gae");
			valorizzaParametri(actioncontext,bp,"numMax");
			valorizzaParametri(actioncontext,bp,"user");
			valorizzaParametri(actioncontext,bp,"ricerca");
			valorizzaParametri(actioncontext,bp,"dt_inizio_rend");
			valorizzaParametri(actioncontext,bp,"dt_fine_rend");
			bp.eseguiRicerca(actioncontext);
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_SIP_100);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaMissioniBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
