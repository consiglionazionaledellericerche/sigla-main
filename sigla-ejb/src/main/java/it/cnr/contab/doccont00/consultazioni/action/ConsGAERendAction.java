/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;

public class ConsGAERendAction extends ConsultazioniAction {

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsGAERendBP bp = (ConsGAERendBP)context.getBusinessProcess();
			int i=bp.getElementsCount();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			ConsGAERendBP	consultazioneBP = (ConsGAERendBP)context.createBusinessProcess("ConsGAERendBP");
			consultazioneBP.initVariabili(context, livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause()==null?true:(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString())))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context,livelloDestinazione));
			//???? da mod
			consultazioneBP.setIterator(context,consultazioneBP.createConsGAEComResSintComponentSession().findConsultazioneRend(context.getUserContext(),consultazioneBP.getBaseclause(),null));			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConsultaDettagli(ActionContext context) {
		return doConsulta(context, ConsGAERendBP.DETT);
	}
	
}