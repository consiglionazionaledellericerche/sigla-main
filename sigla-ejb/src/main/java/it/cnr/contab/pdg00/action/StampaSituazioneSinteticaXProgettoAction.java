/*
 * Created on Apr 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bulk.Stampa_situazione_sintetica_x_progettoBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class StampaSituazioneSinteticaXProgettoAction extends it.cnr.contab.reports.action.ParametricPrintAction {

	public StampaSituazioneSinteticaXProgettoAction() {
		super();
	}
	
	@Override
	public Forward doPrint(ActionContext context) {
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_sintetica_x_progettoBulk model = (Stampa_situazione_sintetica_x_progettoBulk)bp.getModel();
		model.setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
		model.setCd_unita_organizzativa(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
		// TODO Auto-generated method stub
		return super.doPrint(context);
	}

}
