/*
 * Created on Nov 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.consultazioni.action;

import it.cnr.contab.pdg00.consultazioni.bp.*;
import it.cnr.contab.pdg00.consultazioni.bulk.*;
import it.cnr.contab.prevent00.consultazioni.bp.ConsPdgPianoRipartoBP;
import it.cnr.contab.prevent00.consultazioni.bulk.V_cons_pdg_modulo_classBulk;
import it.cnr.contab.prevent00.consultazioni.bulk.V_cons_pdg_piano_ripartoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPdgPianoRipartoAction extends ConsultazioniAction {
	public Forward doConsultaDettagliModuloClass(ActionContext context) {
		try {
			fillModel(context);
			ConsPdgPianoRipartoBP bp = (ConsPdgPianoRipartoBP)context.getBusinessProcess();

			V_cons_pdg_piano_ripartoBulk cla = (V_cons_pdg_piano_ripartoBulk)bp.getModel();

			if (cla==null) {
				bp.setMessage("Nessun dettaglio selezionato");
				return context.findDefaultForward();
			}
					
			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","esercizio",SQLBuilder.EQUALS,cla.getEsercizio());
			clause.addClause("AND","id_classificazione",SQLBuilder.EQUALS,cla.getId_classificazione());
			clause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,cla.getCd_centro_responsabilita());

			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsPdgModuloClassBP");
			
			ricercaLiberaBP.addToBaseclause(clause);
			ricercaLiberaBP.openIterator(context);
			ricercaLiberaBP.setSearchResultColumnSet("nuovo_pdgp");
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
}