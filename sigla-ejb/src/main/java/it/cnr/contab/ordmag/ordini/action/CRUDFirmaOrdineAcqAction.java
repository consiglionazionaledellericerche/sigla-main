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

import it.cnr.contab.docamm00.bp.CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.ordmag.ordini.bp.CRUDFirmaOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.BulkBP;

public class CRUDFirmaOrdineAcqAction extends CRUDOrdineAcqAction {

public CRUDFirmaOrdineAcqAction() {
        super();
    }
public Forward doFirmaOrdine(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDFirmaOrdineAcqBP bp = (CRUDFirmaOrdineAcqBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
		try {
			fillModel(actioncontext);

			BulkBP firmaOTPBP = (BulkBP) actioncontext.createBusinessProcess("FirmaOTPBP");
			firmaOTPBP.setModel(actioncontext, new FirmaOTPBulk());
			actioncontext.addHookForward("firmaOTP",this,"doBackFirmaOTP");
			return actioncontext.addBusinessProcess(firmaOTPBP);
		} catch(Exception e) {
			return handleException(actioncontext,e);
		}
		/*
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

		 */
	}
	/*
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}*/
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	//return actioncontext.findDefaultForward();
}
	public Forward doBackFirmaOTP(ActionContext context) {
		CRUDFirmaOrdineAcqBP bp = (CRUDFirmaOrdineAcqBP)getBusinessProcess(context);
		OggettoBulk bulk = ( OrdineAcqBulk) bp.getModel();
		HookForward caller = (HookForward)context.getCaller();
		FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
		try {
			fillModel(context);
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			bp.setModel(context, bulk);
			//bp.setSelection(context);
			bp.firmaOTP(context, firmaOTPBulk);
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
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
