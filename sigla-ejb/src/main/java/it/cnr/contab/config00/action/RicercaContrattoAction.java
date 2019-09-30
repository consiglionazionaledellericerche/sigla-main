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

package it.cnr.contab.config00.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import it.cnr.contab.config00.bp.*;
import it.cnr.contab.config00.bulk.RicercaContrattoBulk;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.AbstractAction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;

public class RicercaContrattoAction extends AbstractAction {

	public RicercaContrattoAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaContrattoBP bp = null;
		try {
			bp = (RicercaContrattoBP)actioncontext.createBusinessProcess("RicercaContrattoBP");
			actioncontext.setBusinessProcess(bp);
			RicercaContrattoBulk bulk=new RicercaContrattoBulk();
			valorizzaParametri(actioncontext,bp,"uo");
			valorizzaParametri(actioncontext,bp,"oggetto");
			valorizzaParametri(actioncontext,bp,"giuridica");
			valorizzaParametri(actioncontext,bp,"stato");
			valorizzaParametri(actioncontext,bp,"esercizio_da");
			valorizzaParametri(actioncontext,bp,"esercizio_a");
			valorizzaParametri(actioncontext,bp,"id");
			valorizzaParametri(actioncontext,bp,"num");
			valorizzaParametri(actioncontext,bp,"daNum");
			valorizzaParametri(actioncontext,bp,"user");
			valorizzaParametri(actioncontext,bp,"esercizio");
			bp.eseguiRicerca(actioncontext);
		} catch (ParseException e) {
			bp.setCodiceErrore(Constants.ERRORE_CON_202);	
		} catch (Exception e) {
			bp.setCodiceErrore(Constants.ERRORE_CON_205);
		}
		return actioncontext.findDefaultForward();
	}
	private void valorizzaParametri(ActionContext actioncontext,RicercaContrattoBP bp,String parametro) throws IntrospectionException, InvocationTargetException, ParseException{
		if(parametro.compareTo("uo")==0){
			String[] lista_uo = ((HttpActionContext)actioncontext).getRequest().getParameterValues(parametro);
			Introspector.setPropertyValue(bp,parametro,lista_uo);
		}else{
			String valore = ((HttpActionContext)actioncontext).getParameter(parametro);
			if (valore != null && !valore.trim().equals(""))
				Introspector.setPropertyValue(bp,parametro,valore);
		}
	}
}
