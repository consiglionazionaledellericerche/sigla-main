package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.ConsStatoInvioMandatiBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsStatoInvioMandatiAction extends ConsultazioniAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConsStatoInvioMandatiAction() {
		super();
	}

	@Override
	protected Forward perform(ActionContext actioncontext, String s) {
		Forward forward = super.perform(actioncontext, s);
		BusinessProcess bp = actioncontext.getBusinessProcess();
		try {
			if (bp.getName().equals("ConsStatoInvioMandatiBP"))
				((ConsStatoInvioMandatiBP)bp).setContabiliEnabled(actioncontext);
		} catch(Throwable e) {
			return handleException(actioncontext,e);
		}		
		return forward;
	}
}
