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

import java.util.List;

import it.cnr.contab.docamm00.bp.CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.VDocammElettroniciAttiviBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class SelezionatoreDocumentiAmministrativiFatturazioneElettronicaAction extends SelezionatoreListaAction {
/**
 * DocumentiAmministrativiProtocollabiliAction constructor comment.
 */
public SelezionatoreDocumentiAmministrativiFatturazioneElettronicaAction() {
	super();
}

public Forward doPredisponiPerLaFirma(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OggettoBulk bulk = bp.getModel();
	VDocammElettroniciAttiviBulk docamm = (VDocammElettroniciAttiviBulk)bulk;
	try {
		fillModel(context);
		bp.setModel(context, bulk);
		bp.setSelection(context);
		bp.predisponiPerLaFirma(context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

public Forward doSignOTP(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();

	try {
		fillModel(context);
		bp.setSelection(context);
		List<VDocammElettroniciAttiviBulk> selectedElements = bp.getSelectedElements(context);
		bp.recuperoFilePerFirma(context, selectedElements);
		BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
		firmaOTPBP.setModel(context, new FirmaOTPBulk());
		context.addHookForward("firmaOTP",this,"doBackFirmaOTP");
		return context.addBusinessProcess(firmaOTPBP);
	} catch(Exception e) {
		return handleException(context,e);
	}
}


public Forward doBackFirmaOTP(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OggettoBulk bulk = bp.getModel();
	HookForward caller = (HookForward)context.getCaller();
	FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
	try {
		fillModel(context);
		bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
		bp.setModel(context, bulk);
		bp.setSelection(context);
		bp.firmaOTP(context, firmaOTPBulk);			
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}	

@Override
public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
	return actioncontext.findDefaultForward();
}
public Forward doCambiaVisibilita(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OggettoBulk bulk = bp.getModel();
	VDocammElettroniciAttiviBulk docamm = ((VDocammElettroniciAttiviBulk)bulk);
	try {
		fillModel(context);
		String stato = docamm.getStatoFattElett();
		bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
		docamm = ((VDocammElettroniciAttiviBulk)bulk);
		docamm.setStatoFattElett(stato);
		bp.setModel(context, bulk);
		bp.openIterator(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
