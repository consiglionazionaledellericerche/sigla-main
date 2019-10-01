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
 * Created on May 23, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.action;

import java.util.Iterator;

import it.cnr.contab.consultazioni.bp.V_cons_partite_giroBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_partite_giroBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_cons_partite_giroAction extends ConsultazioniAction {

	public Forward doDettagli(ActionContext context) {

		try {

			V_cons_partite_giroBP bp = (V_cons_partite_giroBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();
			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}

			CompoundFindClause clauses = null;
			if (bp.getElementsCount()!=selectElements){
				
				for (Iterator i = bp.getSelectedElements(context).iterator();i.hasNext();) 
				{
					V_cons_partite_giroBulk wpb = (V_cons_partite_giroBulk) i.next();
					
					CompoundFindClause parzclause = new CompoundFindClause();
					parzclause.addClause("AND","cd_cds_origine_obb",SQLBuilder.EQUALS,wpb.getCd_cds_origine_obb());
					parzclause.addClause("AND","cd_uo_origine_obb",SQLBuilder.EQUALS,wpb.getCd_uo_origine_obb());
					parzclause.addClause("AND","cd_elemento_voce_obb",SQLBuilder.EQUALS,wpb.getCd_elemento_voce_obb());
					parzclause.addClause("AND","cd_elemento_voce_acr",SQLBuilder.EQUALS,wpb.getCd_elemento_voce_acr());
				
					clauses = clauses.or(clauses, parzclause);
				}
			}
			CompoundFindClause findclause = bp.getFindclause();
			if (findclause==null)
				findclause = clauses;
			else
				findclause.addChild(clauses);

			ConsultazioniBP ricercaLiberaBP = null;

			if (bp.getName().equals("ConsDocContPartiteGiroCdsBP"))
			  	ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsDocContPartiteGiroCdsDetBP");
			else if(bp.getName().equals("ConsDocContPartiteGiroCnrBP"))
  				ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsDocContPartiteGiroCnrDetBP");
			
			ricercaLiberaBP.addToBaseclause(findclause);
			ricercaLiberaBP.openIterator(context);
			
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);

		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
