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

package it.cnr.contab.ordmag.richieste.action;

import java.rmi.RemoteException;

import it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;

public class CRUDApprovaRichiestaUopAction extends CRUDRichiestaUopAction {

public CRUDApprovaRichiestaUopAction() {
        super();
    }
public Forward doInviaOrdine(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		RichiestaUopBulk richiesta = (RichiestaUopBulk) bp.getModel();
		if (richiesta.isDefinitiva()){
			richiesta.setStato(RichiestaUopBulk.STATO_INVIATA_ORDINE);
			java.sql.Timestamp dataReg = null;
			try {
				dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
				richiesta.setDataInvio(dataReg);
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}
		}
		getBusinessProcess(actioncontext).save(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}

public Forward doSblocca(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		RichiestaUopBulk richiesta = (RichiestaUopBulk) bp.getModel();
		if (richiesta.isDefinitiva()){
			richiesta.setStato(RichiestaUopBulk.STATO_INSERITA);
		}
		getBusinessProcess(actioncontext).save(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}

}
