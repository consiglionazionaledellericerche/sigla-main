/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsFileCassiereAction extends ConsultazioniAction {

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsFileCassiereBP bp = (ConsFileCassiereBP)context.getBusinessProcess();
			int i=bp.getElementsCount();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();
			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}

			ConsFileCassiereBP	consultazioneBP = (ConsFileCassiereBP)context.createBusinessProcess("ConsFileCassiereBP");
			consultazioneBP.initVariabili(context, livelloDestinazione, livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context,livelloDestinazione));
				consultazioneBP.setIterator(context,consultazioneBP.createFileCassiereComponentSession().findConsultazione(context.getUserContext(),
						livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConsultaDett(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BASE);
	}
	public Forward doConsultaBott1(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT1);
	}
	public Forward doConsultaBott2(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT2);
	}
	public Forward doConsultaBott3(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT3);
	}
	public Forward doConsultaBott4(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT4);
	}
	public Forward doConsultaBott5(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT5);
	}
	public Forward doConsultaBott6(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT6);
	}
	public Forward doConsultaBott7(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT7);
	}
	public Forward doConsultaBott8(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT8);
	}
	public Forward doConsultaBott9(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT9);
	}
	public Forward doConsultaBott10(ActionContext context) {
		return doConsulta(context, ConsFileCassiereBP.BOTT10);
	}
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsFileCassiereBP) {
			ConsFileCassiereBP bp = (ConsFileCassiereBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}