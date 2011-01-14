package it.cnr.contab.config00.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

import it.cnr.contab.config00.bp.RicercaNaProComBP;
import it.cnr.contab.config00.bp.RicercaTerziBP;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.AbstractAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;

public class RicercaNaProComAction extends AbstractAction {

	public RicercaNaProComAction() {
		super();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaNaProComBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaNaProComBP bp = null;
		try {
			String user;
			bp = (RicercaNaProComBP)actioncontext.createBusinessProcess("RicercaNaProComBP");
			actioncontext.addBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"servizio");
			valorizzaParametri(actioncontext,bp,"nazione");
			valorizzaParametri(actioncontext,bp,"provincia");
			valorizzaParametri(actioncontext,bp,"ds_comune");
			valorizzaParametri(actioncontext,bp,"pg_nazione");
			valorizzaParametri(actioncontext,bp,"numMax");
			valorizzaParametri(actioncontext,bp,"user");
			if (bp.getUser()!= null)
				if(bp.getUser().length()>20)
					user = bp.getUser().substring(0, 20);
				else
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
			actioncontext.setUserContext(new CNRUserContext(user,actioncontext.getSessionId(),new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null));
			if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("caricaNazioni"))
				bp.caricaNazioni(actioncontext);
			else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("caricaProvince")){
				bp.caricaProvice(actioncontext);
			}else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("caricaComuni")){
				bp.caricaComuni(actioncontext);
			}else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("nuovoComune")){
				bp.nuovoComune(actioncontext);
			}else{
				bp.setCodiceErrore(Constants.ERRORE_SIP_103);
			}
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_SIP_100);
		}
		return actioncontext.findDefaultForward();
	}

}
