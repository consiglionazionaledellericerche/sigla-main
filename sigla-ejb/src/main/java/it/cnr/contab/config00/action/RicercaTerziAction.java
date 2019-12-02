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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

public class RicercaTerziAction extends AbstractAction {

	public RicercaTerziAction() {
		super();
	}
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		RicercaTerziBP bp = null;
		try {
			bp = (RicercaTerziBP)actioncontext.createBusinessProcess("RicercaTerziBP");
            actioncontext.setBusinessProcess(bp);
			bp.setUser("MACRO");
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

			if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("cerca"))
				bp.eseguiRicerca(actioncontext);
			else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("rendicontazione"))
				  bp.eseguiRicerca_rendicontazione(actioncontext);
			else if (bp.getServizio()!= null && bp.getServizio().equalsIgnoreCase("cercacompleta"))
				  bp.eseguiRicerca(actioncontext);
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
