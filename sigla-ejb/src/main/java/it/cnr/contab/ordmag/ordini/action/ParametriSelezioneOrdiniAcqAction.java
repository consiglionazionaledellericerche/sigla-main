package it.cnr.contab.ordmag.ordini.action;

import it.cnr.contab.ordmag.ordini.bp.ParametriSelezioneOrdiniAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class ParametriSelezioneOrdiniAcqAction extends BulkAction {
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

	
//	public Forward doOnDaDataCompetenzaChange(ActionContext context) {
//		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
//		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();
//
//
//		try {
//			fillModel(context);
//			if (parametri.getaDataCompetenza() == null && parametri.getDaDataCompetenza() != null)
//				parametri.setaDataCompetenza(parametri.getDaDataCompetenza());
//			return context.findDefaultForward();
//		} catch (Exception ex) {
//			try
//			{
//				return handleException(context, ex);
//			}
//			catch (Exception e)
//			{
//				return handleException(context, e);
//			}
//		}
//	}
//
//	public Forward doOnDaDataMovimentoChange(ActionContext context) {
//		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
//		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();
//
//
//		try {
//			fillModel(context);
//			if (parametri.getaDataMovimento() == null && parametri.getDaDataMovimento() != null)
//				parametri.setaDataMovimento(parametri.getDaDataMovimento());
//			return context.findDefaultForward();
//		} catch (Exception ex) {
//			try
//			{
//				return handleException(context, ex);
//			}
//			catch (Exception e)
//			{
//				return handleException(context, e);
//			}
//		}
//	}
//
//	public Forward doOnDaDataOrdineChange(ActionContext context) {
//		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
//		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();
//
//
//		try {
//			fillModel(context);
//			if (parametri.getaDataOrdine() == null && parametri.getDaDataOrdine() != null)
//				parametri.setaDataOrdine(parametri.getDaDataOrdine());
//			return context.findDefaultForward();
//		} catch (Exception ex) {
//			try
//			{
//				return handleException(context, ex);
//			}
//			catch (Exception e)
//			{
//				return handleException(context, e);
//			}
//		}
//	}
//
//	public Forward doOnDaDataOrdineDefChange(ActionContext context) {
//		ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)context.getBusinessProcess();
//		ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();
//
//
//		try {
//			fillModel(context);
//			if (parametri.getaDataOrdineDef() == null && parametri.getDaDataOrdineDef() != null)
//				parametri.setaDataOrdineDef(parametri.getDaDataOrdineDef());
//			return context.findDefaultForward();
//		} catch (Exception ex) {
//			try
//			{
//				return handleException(context, ex);
//			}
//			catch (Exception e)
//			{
//				return handleException(context, e);
//			}
//		}
//	}
//
//	public Forward doBringBackSearchFindDaBeneServizio(ActionContext context,
//			ParametriSelezioneMovimentiBulk parametri,
//			Bene_servizioBulk bene)
//			throws java.rmi.RemoteException {
//
//			parametri.setDaBeneServizio(bene);
//			if (bene != null && (parametri.getaBeneServizio() == null || parametri.getaBeneServizio().getCd_bene_servizio() == null)){
//				parametri.setaBeneServizio(bene);
//			}
//			return context.findDefaultForward();
//	}
//
//	public Forward doBringBackSearchFindDaUnitaOperativaOrd(ActionContext context,
//			ParametriSelezioneMovimentiBulk parametri,
//			UnitaOperativaOrdBulk uop)
//			throws java.rmi.RemoteException {
//
//			parametri.setDaUnitaOperativaRicevente(uop);
//			if (uop != null && (parametri.getaUnitaOperativaRicevente() == null || parametri.getaUnitaOperativaRicevente().getCdUnitaOperativa() == null)){
//				parametri.setaUnitaOperativaRicevente(uop);
//			}
//			return context.findDefaultForward();
//	}

	public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {

        ParametriSelezioneOrdiniAcqBP bp = (ParametriSelezioneOrdiniAcqBP) context.getBusinessProcess();
    	try {
    		bp.fillModel(context); 

    		it.cnr.jada.util.RemoteIterator ri = bp.ricercaOrdiniAcq(context);
    		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
    		if (ri.countElements() == 0) {
    			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
    			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
    		}
    		//if (bp.getFunction() != null && bp.getFunction().compareTo(new Character('V')) == 0){
    			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
    			nbp.setIterator(context,ri);
    			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(OrdineAcqBulk.class));
    			HookForward hook = (HookForward)context.findForward("seleziona"); 
    			return context.addBusinessProcess(nbp); 

    		//}
//    		SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("SelezionatoreMovimentiDaAnnullareBP");
//            nbp.setMultiSelection(true);
//    		nbp.setIterator(context,ri);
//    		nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(MovimentiMagBulk.class));
//    		context.findForward("seleziona");
//			context.addHookForward("close",this,"doDefault");
//    		return context.addBusinessProcess(nbp);
    				    		
    	} catch (Exception e) {
    			return handleException(context,e); 
    	}
    	
    }


}
