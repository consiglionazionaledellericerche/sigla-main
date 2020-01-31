/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.action;

import java.rmi.RemoteException;

import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;

public class CRUDFirmaOrdineAcqAction extends CRUDOrdineAcqAction {

public CRUDFirmaOrdineAcqAction() {
        super();
    }
public Forward doFirmaOrdine(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
		if (ordine.isStatoAllaFirma()){
			ordine.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
			java.sql.Timestamp dataReg = null;
			try {
				dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}

			ordine.setDataOrdineDef(dataReg);
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
		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
		if (ordine.isStatoAllaFirma()){
			ordine.setStato(OrdineAcqBulk.STATO_IN_APPROVAZIONE);
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
