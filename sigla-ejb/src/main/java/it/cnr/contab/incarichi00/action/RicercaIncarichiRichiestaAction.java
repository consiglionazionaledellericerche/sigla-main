/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.incarichi00.action;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

public class RicercaIncarichiRichiestaAction extends AbstractAction {
    private transient static final Logger logger = LoggerFactory.getLogger(RicercaIncarichiRichiestaAction.class);

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
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&...&tipoInc=1                  * Incarichi di collaborazione relativi ad attività di consulenza con tipo attività 3 e 7
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&...&tipoInc=2                  * Incarichi di collaborazione relativi ad altre tipologie di attività con tipo attività 1,2,4,5,6 e 8
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&...&tipoInc=3                  * Assegni di ricerca con tipo attività 10
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&...&tipoInc=4                  * Borse di studio con tipo attività 11
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=3&...&tipoInc=5                  * Tirocini con tipo attività 12

	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=4                                * elenco contratti conferiti
	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=6&anno=2008                      * elenco contratti conferiti nel 2008 con l'informazione del liquidato al 31/01/2009

	 * http://siglaas4.cedrc.cnr.it:8180/SIGLA/RicercaIncarichiRichiesta.do?tipofile=5                                * elenco incarichi per art.18
	*/
			 
	public RicercaIncarichiRichiestaAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaIncarichiRichiestaBP bp = null;
		try {
			String user;
			bp = (RicercaIncarichiRichiestaBP)actioncontext.createBusinessProcess("RicercaIncarichiRichiestaBP");
			actioncontext.setBusinessProcess(bp);
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
			valorizzaParametri(actioncontext,bp,"tipoInc");
			
			bp.eseguiRicerca(actioncontext);
		} catch (Exception e) {
			logger.error("ERROR in RicercaIncarichiRichiestaAction ", e);
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
