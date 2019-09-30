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

package it.cnr.contab.ordmag.ordini.action;

import java.rmi.RemoteException;

import it.cnr.contab.ordmag.ordini.bp.GenerazioneOrdineDaRichiesteBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Lista di Obbligazioni)
 */
public class GenerazioneOrdineDaRichiesteAction extends it.cnr.jada.util.action.CRUDAction {
public GenerazioneOrdineDaRichiesteAction() {
	super();
}
//public Forward doEmettiOrdine(ActionContext context) {
//
//	try {
//
//		GenerazioneOrdineDaRichiesteBP goBP = (GenerazioneOrdineDaRichiesteBP)context.getBusinessProcess();
//		CRUDOrdineAcqBP bp;
//
//			bp = (CRUDOrdineAcqBP)context.createBusinessProcess("CRUDOrdineAcqBP",new Object[] { "M" });
//
//			OrdineAcqBulk ordine = bp.creaOrdineDaRichieste(context,lista);
//			if(ordine == null){
//				setErrorMessage(context, "Ordine non creato");
//				return context.findDefaultForward();
//			}
//			return context.addBusinessProcess(bp);
//		return context.findDefaultForward();
//
//	} catch(Exception e) {
//		return handleException(context,e);
//	}
//}

private void gestioneSalvataggio(ActionContext actioncontext) throws ValidationException, ApplicationException,  BusinessProcessException {
	GenerazioneOrdineDaRichiesteBP goBP = (GenerazioneOrdineDaRichiesteBP)actioncontext.getBusinessProcess();
	it.cnr.jada.util.action.Selection selection = goBP.getRichieste().getSelection();
	OrdineAcqBulk bulk = (OrdineAcqBulk) goBP.getModel();
	java.util.List richieste = goBP.getRichieste().getDetails();
	bulk.setRichiesteDaTrasformareInOrdineColl(new BulkList<>());
	for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
		VRichiestaPerOrdiniBulk richiesta = (VRichiestaPerOrdiniBulk)richieste.get(i.nextIndex());
		bulk.getRichiesteDaTrasformareInOrdineColl().add(richiesta);
	}
	if (bulk.getRichiesteDaTrasformareInOrdineColl() == null || bulk.getRichiesteDaTrasformareInOrdineColl().isEmpty()){
		throw new it.cnr.jada.comp.ApplicationException("Selezionare almeno una richiesta da trasformare in ordine!");
	} else {
		OrdineAcqBulk ordine = goBP.creaOrdineDaRichieste(actioncontext);
//		bulk.setRigheConsegnaDaEvadereColl(new BulkList<>());
//		return listaBolleScarico;
	}
}
@Override
public Forward doSalva(ActionContext actioncontext) throws RemoteException {
	try
	{
		fillModel(actioncontext);
//		List<BollaScaricoMagBulk> listaBolleScarico = 
				gestioneSalvataggio(actioncontext);
//		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)actioncontext.getBusinessProcess();
//		if (!listaBolleScarico.isEmpty()){
//			SelezionatoreListaBP nbp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("BolleScaricoGenerate");
//			nbp.setMultiSelection(false);
//
//			BollaScaricoMagBulk bollaScaricoMagBulk = listaBolleScarico.get(0);
//
//			OggettoBulk instance = (OggettoBulk)bollaScaricoMagBulk;
//
//			RemoteIterator iterator = ((EvasioneOrdineComponentSession)bp.createComponentSession()).preparaQueryBolleScaricoDaVisualizzare(actioncontext.getUserContext(), listaBolleScarico);
//			
//			nbp.setIterator(actioncontext,iterator);
//			BulkInfo bulkInfo = BulkInfo.getBulkInfo(instance.getClass());
//			nbp.setBulkInfo(bulkInfo);
//
//			String columnsetName = bp.getColumnSetForBollaScarico();
//			if (columnsetName != null)
//				nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary(columnsetName));
//			return actioncontext.addBusinessProcess(nbp);
//		}
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

public Forward doCercaRichieste(ActionContext context) 
{
	try 
	{
		GenerazioneOrdineDaRichiesteBP bp = (GenerazioneOrdineDaRichiesteBP)getBusinessProcess(context);
		fillModel( context );
		OrdineAcqBulk bulk = (OrdineAcqBulk) bp.getModel();
		bp.cercaRichieste(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}


}
