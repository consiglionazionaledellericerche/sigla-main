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

package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.magazzino.bp.ParametriSelezioneMovimentiMagBP;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ParametriSelezioneMovimentiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class ParametriSelezioneMovimentiAction extends BulkAction {
    /**
     * Gestisce una richiesta di ricerca.
     * <p>
     * L'implementazione di default utilizza il metodo astratto <code>read</code>
     * di <code>CRUDBusinessProcess</code>.
     * Se la ricerca fornisce più di un risultato viene creato un
     * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
     * Al business process viene anche chiesto l'elenco delle colonne da
     * visualizzare.
     */

	
	public Forward doOnDaDataCompetenzaChange(ActionContext context) {
		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();


		try {
			fillModel(context);
			if (parametri.getaDataCompetenza() == null && parametri.getDaDataCompetenza() != null)
				parametri.setaDataCompetenza(parametri.getDaDataCompetenza());
			return context.findDefaultForward();
		} catch (Exception ex) {
			try
			{
				return handleException(context, ex);			
			}
			catch (Exception e) 
			{
				return handleException(context, e);
			}
		}
	}
	
	public Forward doOnDaDataMovimentoChange(ActionContext context) {
		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();


		try {
			fillModel(context);
			if (parametri.getaDataMovimento() == null && parametri.getDaDataMovimento() != null)
				parametri.setaDataMovimento(parametri.getDaDataMovimento());
			return context.findDefaultForward();
		} catch (Exception ex) {
			try
			{
				return handleException(context, ex);			
			}
			catch (Exception e) 
			{
				return handleException(context, e);
			}
		}
	}
	
	public Forward doOnDaDataOrdineChange(ActionContext context) {
		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();


		try {
			fillModel(context);
			if (parametri.getaDataOrdine() == null && parametri.getDaDataOrdine() != null)
				parametri.setaDataOrdine(parametri.getDaDataOrdine());
			return context.findDefaultForward();
		} catch (Exception ex) {
			try
			{
				return handleException(context, ex);			
			}
			catch (Exception e) 
			{
				return handleException(context, e);
			}
		}
	}
	
	public Forward doOnDaDataOrdineDefChange(ActionContext context) {
		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();


		try {
			fillModel(context);
			if (parametri.getaDataOrdineDef() == null && parametri.getDaDataOrdineDef() != null)
				parametri.setaDataOrdineDef(parametri.getDaDataOrdineDef());
			return context.findDefaultForward();
		} catch (Exception ex) {
			try
			{
				return handleException(context, ex);			
			}
			catch (Exception e) 
			{
				return handleException(context, e);
			}
		}
	}
	
	public Forward doBringBackSearchFindDaBeneServizio(ActionContext context,
			ParametriSelezioneMovimentiBulk parametri,
			Bene_servizioBulk bene) 
			throws java.rmi.RemoteException {

			parametri.setDaBeneServizio(bene);
			if (bene != null && (parametri.getaBeneServizio() == null || parametri.getaBeneServizio().getCd_bene_servizio() == null)){
				parametri.setaBeneServizio(bene);
			}
			return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindDaUnitaOperativaOrd(ActionContext context,
			ParametriSelezioneMovimentiBulk parametri,
			UnitaOperativaOrdBulk uop) 
			throws java.rmi.RemoteException {

			parametri.setDaUnitaOperativaRicevente(uop);
			if (uop != null && (parametri.getaUnitaOperativaRicevente() == null || parametri.getaUnitaOperativaRicevente().getCdUnitaOperativa() == null)){
				parametri.setaUnitaOperativaRicevente(uop);
			}
			return context.findDefaultForward();
	}

	public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {

        ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP) context.getBusinessProcess();
    	try {
    		bp.fillModel(context); 

    		it.cnr.jada.util.RemoteIterator ri = bp.ricercaMovimenti(context);
    		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
    		if (ri.countElements() == 0) {
    			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
    			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
    		}
    		if (bp.getFunction() != null && bp.getFunction().compareTo(new Character('V')) == 0){
    			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
    			nbp.setIterator(context,ri);
    			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(MovimentiMagBulk.class)); 
    			HookForward hook = (HookForward)context.findForward("seleziona"); 
    			return context.addBusinessProcess(nbp); 

    		}
    		SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("SelezionatoreMovimentiDaAnnullareBP");
            nbp.setMultiSelection(true);
    		nbp.setIterator(context,ri);
    		nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(MovimentiMagBulk.class)); 
    		context.findForward("seleziona"); 
			context.addHookForward("close",this,"doDefault");			
    		return context.addBusinessProcess(nbp); 
    				    		
    	} catch (Exception e) {
    			return handleException(context,e); 
    	}
    	
    }

	@Override
	public Forward doCloseForm(ActionContext actioncontext) throws BusinessProcessException {
		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)actioncontext.getBusinessProcess();
		bp.setDirty(false);
		return super.doCloseForm(actioncontext);
	}
}
