package it.cnr.contab.doccont00.consultazioni.action;


import it.cnr.contab.doccont00.consultazioni.bp.ConsConfrontoEntSpeTotBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_confronto_ent_spe_totBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsConfrontoEntSpeTotAction extends ConsultazioniAction{

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsConfrontoEntSpeTotBP bp = (ConsConfrontoEntSpeTotBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			ConsConfrontoEntSpeTotBP consultazioneBP = (ConsConfrontoEntSpeTotBP)context.createBusinessProcess("ConsConfrontoEntSpeTotBP");
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
				consultazioneBP.setIterator(context,bp.createConsConfrontoEntSpeTotComponentSession().findConsultazioneModulo(context.getUserContext(),consultazioneBP.getPathConsultazione(),livelloDestinazione,consultazioneBP.getBaseclause(),bp.getFindclause()));
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}


	public Forward doConsultaModulo(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeTotBP.LIV_BASEMOD);
	}
	public Forward doConsultaGae(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeTotBP.LIV_BASEMODGAE);
		}
	public Forward doConsultaVoce(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeTotBP.LIV_BASEMODGAEVOCE);
	}
	/*public Forward doConsultaDett(ActionContext context) {
		return doConsulta(context, ConsConfrontoEntSpeTotBP.LIV_BASEMODGAEVOCEDET);
	}*/
	
	
	
	
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsConfrontoEntSpeTotBP) {
			ConsConfrontoEntSpeTotBP bp = (ConsConfrontoEntSpeTotBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}
