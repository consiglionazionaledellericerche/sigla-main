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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.bp.IDocumentoContabileBP;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Gennaro Borriello
 */

import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;

public class RisultatoEliminazioneAction extends it.cnr.jada.util.action.BulkAction {	
/**
 * CRUDFatturaPassivaAction constructor comment.
 */
public RisultatoEliminazioneAction() {
	super();
}
/**
 * Gestisce un comando "annulla riporta".
 */
public Forward doAnnullaRiporta(ActionContext context) throws BusinessProcessException {

	RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
	if (bp.isEditOnly()) {
		it.cnr.jada.bulk.OggettoBulk undoBringBack = bp.getBulkClone();
		context.closeBusinessProcess();
		HookForward bringBackForward = (HookForward)context.findForward("bringback");
		if (bringBackForward == null)
			return context.findDefaultForward();
		if (undoBringBack != null)
			bringBackForward.addParameter("undoBringBack",undoBringBack);
		return bringBackForward;
	} 
	return context.findDefaultForward();
}
/**
 * Ritorna al gestore della cancellazione dopo la modifica di un doc amm
 */
 
public Forward doBringBackModificaDocumentoAmministrativo(ActionContext context) {

	try {
		if (((HookForward)context.getCaller()).getParameter("undoBringBack") == null) {
			RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
			IDocumentoAmministrativoBulk docAmm = (IDocumentoAmministrativoBulk)bp.getDocumentiAmministrativiController().getModel();
			((Risultato_eliminazioneVBulk)bp.getModel()).removeFromDocumentiAmministrativiScollegati(docAmm);
			bp.getDocumentiAmministrativiController().reset(context);
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Ritorna al gestore della cancellazione dopo la modifica di un doc cont
 */
public Forward doBringBackModificaDocumentoContabile(ActionContext context) {

	try {
		if (((HookForward)context.getCaller()).getParameter("undoBringBack") == null) {
			RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
			IDocumentoContabileBulk docAmm = (IDocumentoContabileBulk)bp.getDocumentiContabiliController().getModel();
			((Risultato_eliminazioneVBulk)bp.getModel()).removeFromDocumentiContabiliScollegati(docAmm);
			bp.getDocumentiContabiliController().reset(context);
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando "riporta".
 */
public Forward doConfermaRiporta(ActionContext context,int option) {
	try {
		RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
		bp.validate(context);

		Risultato_eliminazioneVBulk model = (Risultato_eliminazioneVBulk)bp.getBringBackModel();
		if (model == null)
			throw new MessageToUser("E' necessario selezionare qualcosa",it.cnr.jada.util.action.FormBP.ERROR_MESSAGE);

		//Aggiorna i saldi dei documenti contabili interessati all'operazione nel 
		//caso in cui ne esistano e siano stati modificati
		//N.B. nel metodo di ritorno viene effettuata subito la commit, pertanto
		//non esistono problemi di lock.
		bp.aggiornaSaldi(context, model);
			
		context.closeBusinessProcess();
		HookForward bringBackForward = (HookForward)context.findForward("bringback");
		if (bringBackForward == null)
			return context.findDefaultForward();
		bringBackForward.addParameter("bringback",model);
		return bringBackForward;
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la chiusura del gestore cancellazioni
 */
public Forward doConfirmCloseForm(ActionContext context,int option) throws BusinessProcessException {

	return doAnnullaRiporta(context);
}
/**
 * Apre il documento amministrativo selezionato per riportarlo in quadratura.
 */
public Forward doModificaDocumentoAmministrativo(ActionContext context) {
	
	try {
		RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
		fillModel(context);

		IDocumentoAmministrativoBulk docAmm = (IDocumentoAmministrativoBulk)bp.getDocumentiAmministrativiController().getModel();
		if (docAmm == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare un documento amministrativo!");

		CRUDBP abp = (CRUDBP)context.createBusinessProcess(docAmm.getManagerName(),new Object[] { "MRSWTh" });
		((IDocumentoAmministrativoBP)abp).setIsDeleting(true);
		
		context.addHookForward("bringback",this,"doBringBackModificaDocumentoAmministrativo");

		Forward forward = context.addBusinessProcess(abp);
		abp.edit(context, (it.cnr.jada.bulk.OggettoBulk)docAmm);
		((IDocumentoAmministrativoBulk)abp.getModel()).setIsDeleting(true);
		
		return forward;
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Apre il documento contabile selezionato per eventuali modifiche
 */
public Forward doModificaDocumentoContabile(ActionContext context) {

	try {
		RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
		fillModel(context);

		IDocumentoContabileBulk docCont = (IDocumentoContabileBulk)bp.getDocumentiContabiliController().getModel();
		if (docCont == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare una scadenza!");

		context.addHookForward("bringback",this,"doBringBackModificaDocumentoContabile");
		it.cnr.jada.util.action.CRUDBP abp = (it.cnr.jada.util.action.CRUDBP)context.getUserInfo().createBusinessProcess(context,docCont.getManagerName(),new Object[] { "VRSWTh" });
		((IDocumentoContabileBP)abp).setDeleting(true);
		
		abp.edit(context, (it.cnr.jada.bulk.OggettoBulk)docCont);

		return context.addBusinessProcess(abp);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando "riporta".
 */
public Forward doRiporta(ActionContext context) {
	try {
		
		RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)context.getBusinessProcess();
		fillModel(context);
		return doConfermaRiporta(context,it.cnr.jada.util.action.OptionBP.NO_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
