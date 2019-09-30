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
 * Created on Apr 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.action;

import java.util.Iterator;
import it.cnr.contab.config00.consultazioni.bulk.V_cons_commesse_contrattiBulk;
import it.cnr.contab.consultazioni.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsTotaleDocContCommessaContrattoAction
	extends ConsultazioniAction {
		
		public Forward doAccertamenti(ActionContext context) {

			try {

				ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
				CompoundFindClause clauses = new CompoundFindClause();
				//clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"ETR");
				java.math.BigDecimal tot_doc_cont = null;
				if (bp.getSelection() != null && bp.getSelection().size() != bp.getElementsCount()){
					tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
					bp.setSelection(context);
	
					if ( bp.getSelectedElements(context) == null )
						return (Forward)context.findDefaultForward();
				
					if (bp.getSelectedElements(context).isEmpty()) {
						bp.setMessage("Non è stata selezionata nessuna riga.");
						return context.findDefaultForward();
					}							
					for (Iterator i = bp.getSelectedElements(context).iterator();i.hasNext();) 
					{
						V_cons_commesse_contrattiBulk wpb = (V_cons_commesse_contrattiBulk) i.next();
						CompoundFindClause claOR = new CompoundFindClause();						
						claOR.addClause("AND","cd_commessa",SQLBuilder.EQUALS,wpb.getCd_commessa());
						clauses = clauses.or(clauses,claOR);
						tot_doc_cont = tot_doc_cont.add(wpb.getTotale_entrate());
					}
				}
				if(tot_doc_cont != null && tot_doc_cont.compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP))==0){
					bp.setMessage("Non ci sono Accertamenti da visualizzare.");
					return bp.findDefaultForward();
				}							
				CompoundFindClause findclause = bp.getFindclause();
				if (findclause==null)
					findclause = new CompoundFindClause();
				if (bp.getBaseclause() != null)
				    findclause.addChild(bp.getBaseclause());	
				if (clauses!=null)	
					findclause.addChild(clauses);
				findclause.addClause("AND","tipo",SQLBuilder.EQUALS,"ETR");
				
				ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsDocContCommessaContrattoBP");
				ricercaLiberaBP.addToBaseclause(findclause);
				ricercaLiberaBP.setSearchResultColumnSet("AccertamentiContratti");
				ricercaLiberaBP.openIterator(context);
			
				context.addHookForward("close",this,"doDefault");
				return context.addBusinessProcess(ricercaLiberaBP);
			} catch(Throwable e) {
				return handleException(context,e);
			}
		}
		public Forward doObbligazioni(ActionContext context) {

			try {

				ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
				CompoundFindClause clauses = new CompoundFindClause();
				//clauses.addClause("AND","tipo",SQLBuilder.EQUALS,"SPE");
				java.math.BigDecimal tot_doc_cont = null;
				if (bp.getSelection() != null && bp.getSelection().size() != bp.getElementsCount()){
					tot_doc_cont = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
					bp.setSelection(context);
	
					if ( bp.getSelectedElements(context) == null )
						return (Forward)context.findDefaultForward();
				
					if (bp.getSelectedElements(context).isEmpty()) {
						bp.setMessage("Non è stata selezionata nessuna riga.");
						return context.findDefaultForward();
					}							
					for (Iterator i = bp.getSelectedElements(context).iterator();i.hasNext();) 
					{
						V_cons_commesse_contrattiBulk wpb = (V_cons_commesse_contrattiBulk) i.next();
						CompoundFindClause claOR = new CompoundFindClause();						
						claOR.addClause("AND","cd_commessa",SQLBuilder.EQUALS,wpb.getCd_commessa());
						clauses = clauses.or(clauses,claOR);
						tot_doc_cont = tot_doc_cont.add(wpb.getTotale_spese());
					}
				}
				if(tot_doc_cont != null && tot_doc_cont.compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP))==0){
					bp.setMessage("Non ci sono Obbligazioni da visualizzare.");
					return bp.findDefaultForward();
				}				
				CompoundFindClause findclause = bp.getFindclause();				
				if (findclause==null)
					findclause = new CompoundFindClause();
				if (bp.getBaseclause() != null)	
					findclause.addChild(bp.getBaseclause());	
				if (clauses!=null){					
					findclause.addChild(clauses);						
				}
				findclause.addClause("AND","tipo",SQLBuilder.EQUALS,"SPE");
				
				ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsDocContCommessaContrattoBP");
				ricercaLiberaBP.addToBaseclause(findclause);
				ricercaLiberaBP.setSearchResultColumnSet("ObbligazioniContratti");
				ricercaLiberaBP.openIterator(context);
			
				context.addHookForward("close",this,"doDefault");
				return context.addBusinessProcess(ricercaLiberaBP);
			} catch(Throwable e) {
				return handleException(context,e);
			}
		}		
}
