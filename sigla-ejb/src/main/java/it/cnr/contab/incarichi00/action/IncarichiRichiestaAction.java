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

package it.cnr.contab.incarichi00.action;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import java.rmi.RemoteException;

import it.cnr.contab.incarichi00.bp.CRUDIncarichiRepertorioBP;
import it.cnr.contab.incarichi00.bp.CRUDIncarichiRichiestaBP;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_richiestaBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

public class IncarichiRichiestaAction extends it.cnr.jada.util.action.CRUDAction{

public IncarichiRichiestaAction() {
	super();
}

public Forward doPubblicaSulSito(ActionContext context){
	try 
	{
		fillModel( context );
		return openConfirm(context, "Attenzione! Confermi la pubblicazione sul sito della \"Verifica Professionalità Interne\" ?", OptionBP.CONFIRM_YES_NO, "doConfirmPubblicaSulSito");
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmPubblicaSulSito(ActionContext context,OptionBP option) {
	try 
	{
		if ( option.getOption() == OptionBP.YES_BUTTON) 
		{
			CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(context);
			bp.pubblicaSulSito(context);
			bp.edit(context,bp.getModel());
		}
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doAnnullaPubblicazioneSulSito(ActionContext context){
	try 
	{
		fillModel( context );
		return openConfirm(context, "Attenzione! Confermi l'annullamento della pubblicazione sul sito della ricerca di professionalità interne all'ente?", OptionBP.CONFIRM_YES_NO, "doConfirmAnnullaPubblicazioneSulSito");
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmAnnullaPubblicazioneSulSito(ActionContext context,OptionBP option) {
	try 
	{
		if ( option.getOption() == OptionBP.YES_BUTTON) 
		{
			CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(context);
			bp.annullaPubblicazioneSulSito(context);
			bp.edit(context,bp.getModel());
		}
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doRicercaChiusa(ActionContext context){
	return doRicercaChiusa(context, false);
}
private Forward doRicercaChiusa(ActionContext context, boolean isByRichiediContratto){
	try 
	{
		fillModel( context );
		CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(context);
		bp.validaChiusuraRicerca(context);
		if (isByRichiediContratto)
			return openConfirm(context, "La ricerca di professionalità interna è conclusa.\nDopo il salvataggio si procederà alla registrazione dell'incarico.\nSi vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmRicercaChiusaByRichiediContratto");
		else if (((Incarichi_richiestaBulk)bp.getModel()).getNrRisorseNonTrovate()==0)
			return openConfirm(context, "La ricerca di professionalità interna è conclusa.\nSi vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmRicercaChiusa");
		else
			return openConfirm(context, "La ricerca di professionalità interna è conclusa.\nDopo il salvataggio si potrà effettuare la registrazione dell'incarico.\nSi vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmRicercaChiusa");
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmRicercaChiusa(ActionContext context,int option) {
	try 
	{
		if ( option == OptionBP.YES_BUTTON) 
		{
			CRUDBP bp = getBusinessProcess(context);
			((Incarichi_richiestaBulk)bp.getModel()).setStato(Incarichi_richiestaBulk.STATO_CHIUSO);
			bp.update(context);
			bp.edit(context,bp.getModel());
		}
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmRicercaChiusaByRichiediContratto(ActionContext context,int option) {
	if ( option == OptionBP.YES_BUTTON)
	{
		doConfirmRicercaChiusa(context, option);
		return doRichiediContratto(context, true);
	}
	return context.findDefaultForward();
}
public Forward doRichiediContratto(ActionContext context){
	return doRichiediContratto(context, false);
}
public Forward doRichiediContratto(ActionContext context, boolean isByRicercaChiusa){
	try 
	{
		fillModel( context );
		CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(context);
		bp.validaRichiestaContratto(context);
		if (bp.isRicercaDaChiudere())
			return doRicercaChiusa(context, true);
		else if (isByRicercaChiusa)
			return doConfirmRichiediContratto(context, OptionBP.YES_BUTTON);
		else
			return openConfirm(context, "Si desidera avviare la procedura di conferimento incarico?", OptionBP.CONFIRM_YES_NO, "doConfirmRichiediContratto");
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmRichiediContratto(ActionContext context,int option) {
	try 
	{
		if ( option == OptionBP.YES_BUTTON) 
		{
			CRUDBP bp = getBusinessProcess(context);
			bp.update(context);
			bp.edit(context,bp.getModel());

			BulkBP newBP = (BulkBP)context.getUserInfo().createBusinessProcess(
					context,
					"CRUDIncarichiProceduraBP",
					new Object[] {
						"M",
						bp.getModel()
					}
				);

			context.addHookForward("close",this,"doBringBackRichiediContratto");

			return context.addBusinessProcess(newBP);
		}
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doBlankSearchCds(ActionContext context, Incarichi_richiestaBulk incarico) 
{	
	incarico.setCds(new CdsBulk());
	incarico.setUnita_organizzativa(null);

	return context.findDefaultForward();
}
public Forward doBringBackSearchUnita_organizzativa(ActionContext context, Incarichi_richiestaBulk incarico, Unita_organizzativaBulk uo) 
{
	try 
	{
		CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(context);
		bp.completaUnitaOrganizzativa(context, uo);
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doSalva(ActionContext actioncontext) throws RemoteException {
	try 
	{
		fillModel( actioncontext );
		CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(actioncontext);
		bp.validate( actioncontext );
		Incarichi_richiestaBulk model = (Incarichi_richiestaBulk)bp.getModel();
		
		if (model.isRichiestaInScadenza())
			if (model.getNr_risorse_trovate_si() + 
				model.getNr_risorse_trovate_no() +
				model.getNr_risorse_trovate_na() == model.getNr_risorse_da_trovare())
				return doRicercaChiusa(actioncontext);
			else
			{	bp.update(actioncontext);
				bp.edit(actioncontext,bp.getModel());
				return actioncontext.findDefaultForward();
			}
	}
	catch(Throwable e) 
	{
		return handleException(actioncontext,e);
	}
	return super.doSalva(actioncontext);
}
public Forward doOnPersonaleInternoChange(ActionContext actioncontext) {
	try {
		fillModel( actioncontext );
		CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)getBusinessProcess(actioncontext);
		Incarichi_richiestaBulk model = (Incarichi_richiestaBulk)bp.getModel();
		
		if (model.getNr_risorse_da_trovare()==1) {
			model.setNr_risorse_trovate_si(0);
			model.setNr_risorse_trovate_no(0);
			model.setNr_risorse_trovate_na(0);
			if (model.getPersonale_interno() != null){
				if (model.getPersonale_interno().equals(Incarichi_richiestaBulk.PERSONALE_INTERNO_TROVATO))
					model.setNr_risorse_trovate_si(1);
				else if (model.getPersonale_interno().equals(Incarichi_richiestaBulk.PERSONALE_INTERNO_NON_TROVATO))
					model.setNr_risorse_trovate_no(1);
				else if	(model.getPersonale_interno().equals(Incarichi_richiestaBulk.PERSONALE_INTERNO_TROVATO_NON_ADATTO))
					model.setNr_risorse_trovate_na(1);
			}
		}				
		return actioncontext.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(actioncontext, ex);
	}
}
public Forward doBringBackRichiediContratto(ActionContext context) {
	try {
		CRUDBP bp = (CRUDBP)context.getBusinessProcess();
		bp.edit(context, bp.getModel());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doApriIncarichiProcedura(ActionContext context) {
	try {
		CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)context.getBusinessProcess();
		Incarichi_richiestaBulk richiesta = (Incarichi_richiestaBulk)bp.getModel();

		if (richiesta==null || richiesta.getCrudStatus()==CRUDBP.SEARCH){
			bp.setMessage("Non è stata selezionata alcuna verifica di professionalità interna.");
			return context.findDefaultForward();
		}
		else if (richiesta.getIncarichi_proceduraColl() == null ||
				 richiesta.getIncarichi_proceduraColl().isEmpty()) {
			bp.setMessage("La verifica di professionalità interna selezionata non risulta collegato ad alcuna procedura di conferimento incarichi.");
			return context.findDefaultForward();
		}
		else if (bp.getIncarichiProceduraColl() == null ||
				 bp.getIncarichiProceduraColl().getModel() == null) {
			bp.setMessage("Non risulta selezionata nessuna procedura di conferimento incarichi.");
			return context.findDefaultForward();
		}

		CRUDBP newBP = (CRUDBP)context.getUserInfo().createBusinessProcess(
				context,
				"CRUDIncarichiProceduraBP",
				new Object[] {
					"M",
					bp.getModel()
				}
			);

		newBP.edit(context, bp.getIncarichiProceduraColl().getModel());
		context.addHookForward("close",this,"doBringBackApriIncarichiProcedura");

		return context.addBusinessProcess(newBP);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doBringBackApriIncarichiProcedura(ActionContext context) {
	try {
		CRUDBP bp = (CRUDBP)context.getBusinessProcess();
		bp.edit(context, bp.getModel());
		bp.setTab("tab","tabIncarichiProcedura");
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
