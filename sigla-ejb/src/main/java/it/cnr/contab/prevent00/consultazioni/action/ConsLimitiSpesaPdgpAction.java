/*
 * Created on Sep 20, 2011
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.consultazioni.action;

import it.cnr.contab.prevent00.consultazioni.bp.ConsLimitiSpesaPdgpBP;
import it.cnr.contab.prevent00.consultazioni.bulk.VLimiteSpesaDetPdgpBulk;
import it.cnr.contab.prevent00.consultazioni.bulk.VLimiteSpesaPdgpBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsLimitiSpesaPdgpAction extends ConsultazioniAction {
	public Forward doConsultaDettagliModuloClass(ActionContext context) {
		try {
			fillModel(context);
			ConsLimitiSpesaPdgpBP bp = (ConsLimitiSpesaPdgpBP)context.getBusinessProcess();

			VLimiteSpesaPdgpBulk bulk = (VLimiteSpesaPdgpBulk)bp.getModel();

			if (bulk==null) {
				bp.setMessage("Nessun dettaglio selezionato");
				return context.findDefaultForward();
			}
					
			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,bulk.getEsercizio());
			clause.addClause("AND","idClassificazione",SQLBuilder.EQUALS,bulk.getIdClassificazione());
			clause.addClause("AND","cdCds",SQLBuilder.EQUALS,bulk.getCdCds());
			clause.addClause("AND","fonte",SQLBuilder.EQUALS,bulk.getFonte());
			clause.addClause("AND","cdArea",SQLBuilder.EQUALS,bulk.getCdArea());
			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsLimitiSpesaDetPdgpBP");
			
			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);
			
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
}