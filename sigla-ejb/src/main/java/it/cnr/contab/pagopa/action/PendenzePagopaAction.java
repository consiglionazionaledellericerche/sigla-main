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

package it.cnr.contab.pagopa.action;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bp.CRUDTipo_linea_attivitaBP;
import it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.action.CRUDAbstractObbligazioneAction;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.pagopa.bp.CRUDConsPagamentoPagopaBP;
import it.cnr.contab.pagopa.bp.CRUDPendenzaPagopaBP;
import it.cnr.contab.pagopa.bp.CRUDPagamentoPagopaBP;
import it.cnr.contab.pagopa.bulk.PagamentoPagopaBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.pagopa.ejb.PendenzaPagopaComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.*;

import java.rmi.RemoteException;

/**
 * Azione che gestisce le richieste relative alla Gestione delle pendenza PagoPA
 */
public class PendenzePagopaAction extends CRUDAction {
public PendenzePagopaAction() {
	super();
}
public Forward doBlankSearchFindTerzo(ActionContext context, PendenzaPagopaBulk pendenzaPagopaBulk)
{
	try 
	{
		pendenzaPagopaBulk.setTerzo(new TerzoBulk());
		pendenzaPagopaBulk.getTerzo().setAnagrafico( new AnagraficoBulk());
		
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la validazione di nuovo terzo creato
 	 * @param context <code>ActionContext</code> in uso.
	 * @param pendenzaPagopaBulk Oggetto di tipo <code>PendenzaPagopaBulk</code>
	 * @param terzo Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCreaTerzo(ActionContext context, PendenzaPagopaBulk pendenzaPagopaBulk, TerzoBulk terzo)
{
	try 
	{
		if (terzo != null )
		{
			pendenzaPagopaBulk.setTerzo( terzo );
		}	
		return context.findDefaultForward();
	}

	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDBP bp = getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare la pendenza");
		} else {
			bp.delete(context);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	public Forward doVisualizzaIncassiAvviso(ActionContext context) {
		try {
			CRUDBP bp = getBusinessProcess(context);
			fillModel(context);
			if (bp.isDirty())
				return openContinuePrompt(context,"doVisualizzaIncassiAvviso");
			return doVisualizzaIncassiAvviso(context, OptionBP.YES_BUTTON);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doVisualizzaIncassiAvviso(ActionContext context,int option) {
		try {
			if (option == OptionBP.YES_BUTTON) {
				return visualizzaIncassi(context);
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	private Forward visualizzaIncassi(ActionContext context) throws BusinessProcessException, RemoteException, ComponentException {
		CRUDPendenzaPagopaBP pendenzaPagopaBP = (CRUDPendenzaPagopaBP) getBusinessProcess(context);
		PendenzaPagopaBulk model = (PendenzaPagopaBulk) pendenzaPagopaBP.getModel();

		it.cnr.jada.util.RemoteIterator ri = null;

		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((PendenzaPagopaComponentSession)pendenzaPagopaBP.createComponentSession()).cercaPagamenti(context.getUserContext(),model));

		int count = ri.countElements();
		if (count == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			throw new it.cnr.jada.comp.ApplicationException("Nessun Pagamento Esistente");
			} else {
			ConsultazioniBP nbp = (ConsultazioniBP)context.createBusinessProcess("CRUDConsPagamentoPagopaBP");
			nbp.setIterator(context,ri);
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(PagamentoPagopaBulk.class));
			context.addHookForward("bringback",this,"doBringBackIncassi");
			return context.addBusinessProcess(nbp);
		}
	}

	private Forward visualizzaIncassoSingolo(ActionContext context) throws BusinessProcessException, RemoteException, InstantiationException, javax.ejb.RemoveException {
		CRUDPendenzaPagopaBP bp = (CRUDPendenzaPagopaBP)getBusinessProcess(context);
		String function = bp.isEditable() ? "M" : "V";
		if (bp.isBringBack())
			function += "R";
		CRUDPagamentoPagopaBP pagamentiBP = (CRUDPagamentoPagopaBP) context.createBusinessProcess("CRUDPagamentoPagopaBP",new Object[] { function, bp.getModel() });
		if (bp.isBringBack())
			context.addHookForward("bringback",this,"doBringBackIncassi");
		Forward forward = context.addBusinessProcess(pagamentiBP);
		pagamentiBP.resetForSearch(context);
		return doCerca(context);
	}

	public Forward doBringBackIncassi(ActionContext context) {
		try {
			HookForward hook = (HookForward)context.getCaller();
			return riporta(context,(OggettoBulk)hook.getParameter("bringback"));
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

}
