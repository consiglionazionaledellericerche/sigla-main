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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.docamm00.bp.RicercaFatturaPassivaBP;
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

public class RicercaFatturaPassivaAction extends AbstractAction {

	public RicercaFatturaPassivaAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaFatturaPassivaBP bp = null;
		try {
			bp = (RicercaFatturaPassivaBP)actioncontext.createBusinessProcess("RicercaFatturaPassivaBP");
			actioncontext.addBusinessProcess(bp);
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
	private void valorizzaParametri(ActionContext actioncontext,RicercaFatturaPassivaBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
		if (valore != null && !valore.trim().equals(""))
			Introspector.setPropertyValue(bp,parametro,valore);
	}
}
