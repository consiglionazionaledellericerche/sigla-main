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
import it.cnr.contab.doccont00.action.CRUDAbstractObbligazioneAction;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Azione che gestisce le richieste relative alla Gestione delle pendenza PagoPA
 */
public class PendenzePagopaAction extends CRUDAbstractObbligazioneAction {
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
}
