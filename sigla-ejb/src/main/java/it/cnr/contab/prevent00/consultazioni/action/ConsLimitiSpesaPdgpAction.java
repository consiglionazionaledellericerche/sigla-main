/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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