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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (21/03/2003 10.02.11)
 * @author: Simonetta Costa
 */
public class ListaSospesiCNRPerCdsAction extends it.cnr.jada.util.action.BulkAction {
/**
 * ListaSospesiCNRPercds constructor comment.
 */
public ListaSospesiCNRPerCdsAction() {
	super();
}
public it.cnr.jada.action.Forward  doCercaSospesiCNR( it.cnr.jada.action.ActionContext context )
{
	try 
	{
		fillModel(context);
		ListaSospesiCNRPerCdsBP bp = (ListaSospesiCNRPerCdsBP) context.getBusinessProcess();
		OggettoBulk model = bp.getModel();
		model.validate();
		it.cnr.jada.util.RemoteIterator ri = bp.find(context,null,model);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		}
		else
		{
			bp.setModel(context,new it.cnr.contab.doccont00.core.bulk.SospesoBulk());
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.setBulkInfo(bp.getSearchBulkInfo());
//			nbp.setColumns(getBusinessProcess(context).getSearchResultColumns());
			nbp.setColumns(bp.getSearchResultColumns());
			context.addHookForward("seleziona",this,"doRiportaSelezione");			
			context.addHookForward("close",this,"doChiudiRicerca");
			return context.addBusinessProcess(nbp);
		}

	} catch(Exception e) {
		return handleException(context,e);
	}
}	
public it.cnr.jada.action.Forward  doChiudiRicerca( it.cnr.jada.action.ActionContext context )
{
	try 
	{
	   getBusinessProcess(context).setModel( context, new it.cnr.contab.doccont00.intcass.bulk.SelezionaSospesiCNRBulk());	
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}	
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doRiportaSelezione(ActionContext context)  throws java.rmi.RemoteException {
	return doChiudiRicerca( context );
}
/**
 * Restituisce il business process corrente effettuando un cast
 * a <codeCRUDBusinessProcess>CRUDBusinessProcess</code>
 * @param context il contesto di esecuzione della action
 */
protected BulkBP  getBusinessProcess(ActionContext context) {
	return (BulkBP)context.getBusinessProcess();
}
}
