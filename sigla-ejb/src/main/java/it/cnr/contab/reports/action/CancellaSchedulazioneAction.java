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

package it.cnr.contab.reports.action;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.ejb.OfflineReportComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.FormAction;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.ejb.EJBException;
import java.rmi.RemoteException;

public class CancellaSchedulazioneAction extends FormAction{
	@Override
	@SuppressWarnings("unused")
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		if(((HttpActionContext)actioncontext).getParameter("pgStampa")== null)
			return super.doDefault(actioncontext);
		Long pgStampa = new Long(((HttpActionContext)actioncontext).getParameter("pgStampa").substring(2));
		String indirizzoEMail = ((HttpActionContext)actioncontext).getParameter("indirizzoEMail");
		BusinessProcess bp = actioncontext.getBusinessProcess();
		bp.setResource("pgStampa", String.valueOf(pgStampa));
		bp.setResource("indirizzoEMail", indirizzoEMail);
		UserContext userContext = AdminUserContext.getInstance(actioncontext.getSessionId());
		try {
			Print_spoolerBulk printSpooler = geComponent(actioncontext).findPrintSpooler(userContext, pgStampa);
			if (printSpooler == null){
				openMessage(actioncontext, "La lista di distribuzione della stampa, è stata eliminata!");
				return super.doDefault(actioncontext);
			}
			String msg = "Si conferma la cancellazione dell'indirizzo "+indirizzoEMail+"<BR>dalla lista di distribuzione della stampa \""+printSpooler.getDsStampa()+"\"?";
			return openConfirm( actioncontext, msg, OptionBP.CONFIRM_YES_NO, "doConfirmCancellaSchedulazione");
		} catch (ComponentException | BusinessProcessException e) {
			handleException(actioncontext, e);
		}
		return super.doDefault(actioncontext);
	}
	public Forward doConfirmCancellaSchedulazione(ActionContext actioncontext,it.cnr.jada.util.action.OptionBP option) {
		UserContext userContext = AdminUserContext.getInstance(actioncontext.getSessionId());
		BusinessProcess bp = actioncontext.getBusinessProcess();
		if (option.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			try {
				geComponent(actioncontext).cancellaSchedulazione(userContext, new Long(bp.getResource("pgStampa")), bp.getResource("indirizzoEMail"));
				openMessage(actioncontext, "La cancellazione è stata effettuata.");
			} catch (Exception e) {
				handleException(actioncontext, e);
			}
		}
		return actioncontext.findDefaultForward();
	}
	private OfflineReportComponentSession geComponent(ActionContext actioncontext) throws EJBException {
		return (it.cnr.contab.reports.ejb.OfflineReportComponentSession)EJBCommonServices.createEJB("BREPORTS_EJB_OfflineReportComponentSession",it.cnr.contab.reports.ejb.OfflineReportComponentSession.class);
	}
}
