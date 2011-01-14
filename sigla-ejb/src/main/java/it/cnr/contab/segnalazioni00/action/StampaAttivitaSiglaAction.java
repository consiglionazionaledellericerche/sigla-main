/*
 * Created on Ago 8, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.segnalazioni00.action;

import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.segnalazioni00.bulk.Stampa_attivita_siglaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaAttivitaSiglaAction extends it.cnr.contab.reports.action.ParametricPrintAction {

	/**
	 * 
	 */
	public StampaAttivitaSiglaAction() {
		super();
	}
	
	
	public Forward doSelezionaStato(ActionContext context) {
		try{
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_attivita_siglaBulk stampa = (Stampa_attivita_siglaBulk)bp.getModel();
			stampa.selezionaStati();
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	
	public Forward doSelezionaTipoAttivita(ActionContext context) {
		try{
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_attivita_siglaBulk stampa = (Stampa_attivita_siglaBulk)bp.getModel();
			stampa.selezionaTipiAttivita();
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	
}
