package it.cnr.contab.config00.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

import it.cnr.contab.config00.bp.RicercaTerziBP;
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

public class RicercaTerziAction extends AbstractAction {

	public RicercaTerziAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaTerziBP bp = null;
		try {
			((HttpActionContext)actioncontext).getSession(true).setMaxInactiveInterval(350);
			String user;
			bp = (RicercaTerziBP)actioncontext.createBusinessProcess("RicercaTerziBP");
			actioncontext.addBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"query");
			valorizzaParametri(actioncontext,bp,"dominio");
			valorizzaParametri(actioncontext,bp,"servizio");
			valorizzaParametri(actioncontext,bp,"tipoterzo");
			valorizzaParametri(actioncontext,bp,"numMax");
			valorizzaParametri(actioncontext,bp,"user");
			valorizzaParametri(actioncontext,bp,"via");
			valorizzaParametri(actioncontext,bp,"civico");
			valorizzaParametri(actioncontext,bp,"cap");
			valorizzaParametri(actioncontext,bp,"nazione");
			valorizzaParametri(actioncontext,bp,"provincia");
			valorizzaParametri(actioncontext,bp,"comune");
			valorizzaParametri(actioncontext,bp,"ragione_sociale");
			valorizzaParametri(actioncontext,bp,"partita_iva");
			valorizzaParametri(actioncontext,bp,"cognome");
			valorizzaParametri(actioncontext,bp,"nome");
			valorizzaParametri(actioncontext,bp,"codice_fiscale");
			valorizzaParametri(actioncontext,bp,"data_nascita");
			valorizzaParametri(actioncontext,bp,"nazione_nascita");
			valorizzaParametri(actioncontext,bp,"provincia_nascita");
			valorizzaParametri(actioncontext,bp,"comune_nascita");
			valorizzaParametri(actioncontext,bp,"sesso");
			valorizzaParametri(actioncontext,bp,"cd_terzo");			
			valorizzaParametri(actioncontext,bp,"ricerca");
			valorizzaParametri(actioncontext,bp,"dt_inizio_rend");
			valorizzaParametri(actioncontext,bp,"dt_fine_rend");
			valorizzaParametri(actioncontext,bp,"dip");
			if (bp.getUser()!= null){
				if(bp.getUser().length()>20)
					user = bp.getUser().substring(0, 20);
				else
					user = bp.getUser();
			}
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
			if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("cerca"))
				bp.eseguiRicerca(actioncontext);
			else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("rendicontazione"))
				  bp.eseguiRicerca_rendicontazione(actioncontext);
			else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("inserimento")){
				bp.inserisciTerzo(actioncontext);
			}else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("elimina")){
				bp.eliminaTerzo(actioncontext);
			}else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("modifica")){
				bp.modificaTerzo(actioncontext);
			}else{
				bp.setCodiceErrore(Constants.ERRORE_SIP_103);
			}
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_SIP_100);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaTerziBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
