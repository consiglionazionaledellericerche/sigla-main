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

package it.cnr.contab.doccont00.action;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.ListIterator;

import javax.ejb.RemoveException;

import it.cnr.contab.doccont00.bp.ConsRiscontriEtrBP;
import it.cnr.contab.doccont00.bp.ConsRiscontriUscBP;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;


public class ConsRiscontriAction extends ConsultazioniAction{

	public Forward doConsultaEntrateRiscontri(ActionContext context) throws BusinessProcessException {
		
		try {
				ConsRiscontriEtrBP bp = (ConsRiscontriEtrBP)context.getBusinessProcess();
				bp.setSelection(context);
				long selectElements = bp.getSelection().size();

				if (selectElements == 0)
					selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
				
				if (selectElements == 0) {
					bp.setMessage("Non è stata selezionata nessuna riga.");
					return context.findDefaultForward();
				}
					
					CompoundFindClause clause = new CompoundFindClause();
					ConsultazioniBP ricercaDettagliBP = null;
					ricercaDettagliBP = (ConsultazioniBP)context.createBusinessProcess("ConsRiscontriEtrDetBP");
				
					for (Iterator i = bp.getSelectedElements(context).iterator();i.hasNext();) 
						 {
						  SospesoBulk sos = (SospesoBulk)i.next();
						  CompoundFindClause parzclause = new CompoundFindClause();
						  
						  parzclause.addClause("AND","cd_cds",SQLBuilder.EQUALS,sos.getCd_cds());
						  parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,sos.getEsercizio());
						  parzclause.addClause("AND","ti_entrata_spesa",SQLBuilder.EQUALS,sos.TIPO_ENTRATA);
						  parzclause.addClause("AND","ti_sospeso_riscontro",SQLBuilder.EQUALS,sos.TI_RISCONTRO);
						  parzclause.addClause("AND","cd_sospeso",SQLBuilder.EQUALS,sos.getCd_sospeso());
			
						  clause = clause.or(clause, parzclause); 
						 }
							
						  ricercaDettagliBP.addToBaseclause(clause);
						  ricercaDettagliBP.openIterator(context);
						  context.addHookForward("close",this,"doDefault");
						  return context.addBusinessProcess(ricercaDettagliBP);			 
			
			}catch(Throwable e) {
				 return handleException(context,e);			 
	}
			
}

public Forward doConsultaUsciteRiscontri(ActionContext context) throws BusinessProcessException {
		
			try {
							
				ConsRiscontriUscBP bp = (ConsRiscontriUscBP)context.getBusinessProcess();
				bp.setSelection(context);
				long selectElements = bp.getSelection().size();

				if (selectElements == 0)
					selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
				
				if (selectElements == 0) {
					bp.setMessage("Non è stata selezionata nessuna riga.");
					return context.findDefaultForward();
				}
				CompoundFindClause clause = null;
				ConsultazioniBP ricercaDettagliBP = null;
				ricercaDettagliBP = (ConsultazioniBP)context.createBusinessProcess("ConsRiscontriUscDetBP");
				
					for (Iterator i = bp.getSelectedElements(context).iterator();i.hasNext();) 
						 {
						  SospesoBulk sos = (SospesoBulk)i.next();
						  CompoundFindClause parzclause = new CompoundFindClause();
						  
						  parzclause.addClause("AND","cd_cds",SQLBuilder.EQUALS,sos.getCd_cds());
						  parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,sos.getEsercizio());
						  parzclause.addClause("AND","ti_entrata_spesa",SQLBuilder.EQUALS,sos.TIPO_SPESA);
						  parzclause.addClause("AND","ti_sospeso_riscontro",SQLBuilder.EQUALS,sos.TI_RISCONTRO);
						  parzclause.addClause("AND","cd_sospeso",SQLBuilder.EQUALS,sos.getCd_sospeso());

						  clause = clause.or(clause, parzclause);
						 }
						
						ricercaDettagliBP.addToBaseclause(clause);
						ricercaDettagliBP.openIterator(context);
						context.addHookForward("close",this,"doDefault");
						return context.addBusinessProcess(ricercaDettagliBP);
			
			}catch(Throwable e) {
				 return handleException(context,e);	
	}
}
}