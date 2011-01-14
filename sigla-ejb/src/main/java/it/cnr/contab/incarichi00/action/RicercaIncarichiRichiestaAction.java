package it.cnr.contab.incarichi00.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.incarichi00.bp.RicercaIncarichiRichiestaBP;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.AbstractAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;

public class RicercaIncarichiRichiestaAction extends AbstractAction {

	/* per testare si può usare:
	 * 
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=1                                * tutte le richieste di verifica professionalità interna valide (V_incarichi_richiesta)
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=1&dominio=data&query=c           * solo le richieste di incarico in corso
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=1&dominio=data&query=s           * solo le richieste di incarico scadute (e quelle non ancora attive)
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=1&dominio=data&query=c&anno=2008 * solo le richieste di incarico in corso pubblicate nel 2008 
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=1&dominio=data&query=s&anno=2008 * solo le richieste di incarico scadute (e quelle non ancora attive) pubblicate nel 2008 

	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=2                                * tutte le richieste di collaborazione valide (V_incarichi_collaborazione)
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=2&dominio=data&query=c           * solo le richieste di collaborazione in corso
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=2&dominio=data&query=s           * solo le richieste di collaborazione scadute (e quelle non ancora attive)
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=2&dominio=data&query=c&anno=2008 * solo le richieste di collaborazione in corso pubblicate nel 2008 
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=2&dominio=data&query=s&anno=2008 * solo le richieste di collaborazione scadute (e quelle non ancora attive) pubblicate nel 2008 

	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3                                * elenco incarichi di collaborazione conferiti (V_incarichi_elenco)
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&dominio=data&query=c           * solo incarichi di collaborazione in corso
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&dominio=data&query=s           * solo incarichi di collaborazione scadute (e quelle non ancora attive)
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&dominio=data&query=c&anno=2008 * solo incarichi di collaborazione in corso pubblicate nel 2008
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&dominio=data&query=s&anno=2008 * solo incarichi di collaborazione scadute (e quelle non ancora attive) pubblicate nel 2008
	*/
			 
	public RicercaIncarichiRichiestaAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaIncarichiRichiestaBP bp = null;
		try {
			((HttpActionContext)actioncontext).getSession(true).setMaxInactiveInterval(0);
			String user;
			bp = (RicercaIncarichiRichiestaBP)actioncontext.createBusinessProcess("RicercaIncarichiRichiestaBP");
			actioncontext.addBusinessProcess(bp);
			valorizzaParametri(actioncontext,bp,"tipofile");
			valorizzaParametri(actioncontext,bp,"query");
			valorizzaParametri(actioncontext,bp,"anno");
			valorizzaParametri(actioncontext,bp,"dominio");
			valorizzaParametri(actioncontext,bp,"numMax");
			valorizzaParametri(actioncontext,bp,"page");
			valorizzaParametri(actioncontext,bp,"rows");
			valorizzaParametri(actioncontext,bp,"user");
			valorizzaParametri(actioncontext,bp,"order");
			valorizzaParametri(actioncontext,bp,"strRic");			
			valorizzaParametri(actioncontext,bp,"cdCds");
			
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
			actioncontext.setUserContext(new CNRUserContext(user,actioncontext.getSessionId(),new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null));
			bp.eseguiRicerca(actioncontext);
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_INC_100);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaIncarichiRichiestaBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
