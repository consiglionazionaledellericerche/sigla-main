package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.ConsStatoInvioMandatiBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio_mandatiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

import java.util.List;

public class ConsStatoInvioMandatiAction extends ConsultazioniAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConsStatoInvioMandatiAction() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Forward doVisualizzaContabili(ActionContext context) throws BusinessProcessException{
		try {
			ConsStatoInvioMandatiBP bp = (ConsStatoInvioMandatiBP)context.getBusinessProcess();
			if (bp.getSelection() != null)
				bp.setSelection(context);
			List<V_cons_stato_invio_mandatiBulk> selectelElements = bp.getSelectedElements(context);
			if (selectelElements == null || selectelElements.isEmpty())
				bp.setMessage("Selezionare almeno un Mandato");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
