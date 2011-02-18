package it.cnr.contab.cori00.actions;

import it.cnr.contab.cori00.bp.CRUDLiquidazioneCORIBP;
import it.cnr.contab.cori00.bp.LiquidazioneCoriF24EPBP;
import it.cnr.contab.cori00.bp.LiquidazioneMassaCoriBP;
import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;
import it.cnr.contab.pdg00.bp.ContabilizzazioneFlussoStipendialeMensileBP;
import it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;


public class LiquidazioneCoriF24EPAction extends it.cnr.jada.util.action.SelezionatoreListaAction {

	public LiquidazioneCoriF24EPAction() {
	}
	public Forward doVisualizzaDettagli(ActionContext context)throws BusinessProcessException {	

		LiquidazioneCoriF24EPBP bp = (LiquidazioneCoriF24EPBP)context.getBusinessProcess();

		Liquid_coriBulk liq = (Liquid_coriBulk)bp.getFocusedElement();
		if (liq == null) {
			bp.setMessage("E' necessario selezionare una liquidazione.");
			return context.findDefaultForward();
		}

		if (liq.getCd_cds() == null ||
			liq.getCd_unita_organizzativa() == null ||
			liq.getEsercizio() == null ||
			liq.getPg_liquidazione() == null) {
			bp.setMessage("E' necessario selezionare una liquidazione.");
			return context.findDefaultForward();
		}
			
		CRUDLiquidazioneCORIBP liqBP = CRUDLiquidazioneCORIBP.getBusinessProcessFor(context, liq, "VRSWTh");
		
		liqBP.edit(context,liq);

		return context.addBusinessProcess(liqBP);
	}
	public Forward doBringBack(ActionContext context) {
		return context.findDefaultForward();
	}
	public Forward doF24EPTot(ActionContext context) throws BusinessProcessException {
		LiquidazioneCoriF24EPBP bp = (LiquidazioneCoriF24EPBP)context.getBusinessProcess();
		bp.saveSelection(context);
		long selectElements = bp.getSelection().size();

		if (selectElements == 0)
			selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
		
		if (selectElements == 0) {
			bp.setMessage("E' necessario selezionare almeno una liquidazione.");
			return context.findDefaultForward();
		}
		 try {
			bp.EstrazioneTot(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	public Forward doReset(ActionContext context) {

		try {
			LiquidazioneCoriF24EPBP bp= (LiquidazioneCoriF24EPBP) context.getBusinessProcess();
			bp.refresh(context);

			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}	
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		try {
			LiquidazioneCoriF24EPBP bp= (LiquidazioneCoriF24EPBP) context.getBusinessProcess();
			bp.refresh(context);
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}
}
