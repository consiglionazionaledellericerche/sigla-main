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

import java.rmi.RemoteException;

import it.cnr.contab.doccont00.bp.CRUDSospesoCNRBP;
import it.cnr.contab.doccont00.bp.ListaSospesiCNRBP;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.SospesoRiscontroComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.ListaSospesiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.AbstractSelezionatoreBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ListaSospesiCNRAction extends ConsultazioniAction {
public ListaSospesiCNRAction() {
	super();
}
/**
 * Metodo utilizzato per gestire la modifica di un sospeso di entratata CNR
 */
public Forward doEditaSospeso(ActionContext context) 
{
	
	try 
	{
		ListaSospesiCNRBP listaBp = (ListaSospesiCNRBP) context.getBusinessProcess();
		SospesoBulk sospeso = (SospesoBulk) listaBp.getFocusedElement(context);;
			
		CRUDSospesoCNRBP bp = (CRUDSospesoCNRBP)context.createBusinessProcess("CRUDSospesoCNRBP");
		bp.setEditable( !sospeso.getFl_stornato() );
		bp.edit( context, sospeso );
//		context.addHookForward("close",this,"doRefreshLista");				
		return context.addBusinessProcess(bp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
/*
	try 
	{
		fillModel(context);
		ListaSospesiEntrataCNRBP bp = (ListaSospesiEntrataCNRBP)context.getBusinessProcess();
		bp.cambiaStatoAssociatoACds( context );
		bp.setMessage("Lo stato dei sospesi e' stato aggiornato");
		return context.findDefaultForward();
	}
	catch ( Exception e )
	{
		return handleException( context, e )	;
	}	
	*/	
}
public Forward doSelection(ActionContext context,String name) {
	try 
	{
//		fillModel(context);
		AbstractSelezionatoreBP bp = (AbstractSelezionatoreBP)context.getBusinessProcess();
		bp.setFocus(context);
		bp.setSelection(context);
		return context.findDefaultForward();
	}
	catch(Exception e) 
	{
		return handleException(context,e);
	}
}

public Forward doCambiaVisibilita(ActionContext context)
		throws RemoteException {
	ListaSospesiCNRBP bp = (ListaSospesiCNRBP) context.getBusinessProcess();
	SospesoBulk bulk = (SospesoBulk)bp.getModel();
	try {
		fillModel(context);
		String statoSospeso = bulk.getStatoTextForSearch();
		EJBCommonServices.closeRemoteIterator(context, bp.detachIterator());
		RemoteIterator iterator = ((SospesoRiscontroComponentSession)
				bp.createComponentSession("CNRDOCCONT00_EJB_SospesoRiscontroComponentSession", SospesoRiscontroComponentSession.class)).
				cercaSospesiPerStato(context.getUserContext(), bp.getFindclause(), (ListaSospesiBulk)bp.createModel( context ), statoSospeso);
		bp.setIterator(context, iterator);
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		bulk.setStatoText(null);
		return handleException(context,e);
	}
}

}
