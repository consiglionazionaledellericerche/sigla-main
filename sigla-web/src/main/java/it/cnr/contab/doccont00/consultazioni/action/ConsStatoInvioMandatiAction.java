package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.ConsStatoInvioMandatiBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio_mandatiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

import java.util.List;

public class ConsStatoInvioMandatiAction extends ConsultazioniAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public Forward doVisualizzaContabili(ActionContext context) {
		try {
			ConsStatoInvioMandatiBP bp = (ConsStatoInvioMandatiBP)context.getBusinessProcess();
			if (bp.getSelection() != null)
				bp.setSelection(context);
			List<V_cons_stato_invio_mandatiBulk> selectelElements = bp.getSelectedElements(context);
			if (selectelElements == null || selectelElements.isEmpty())
				bp.setMessage("Selezionare almeno un Mandato");
			else
				bp.setScaricaContabile(Boolean.TRUE);		
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
