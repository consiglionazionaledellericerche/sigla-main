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

package it.cnr.contab.progettiric00.consultazioni.action;

import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataBP;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataDettagliBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.progettiric00.consultazioni.bp.ConsProgEcoVoceGaeBP;
import it.cnr.contab.progettiric00.consultazioni.bp.ConsProgEcoVoceGaeDettBP;
import it.cnr.contab.progettiric00.consultazioni.bulk.ConsProgettiEcoVociGaeBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.BulkAction;

import javax.ejb.RemoveException;
import java.rmi.RemoteException;


public class ConsProgEcoVociGaeAction extends BulkAction {


public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	try {
		ConsProgEcoVoceGaeBP bp= (ConsProgEcoVoceGaeBP) context.getBusinessProcess();
		ConsProgettiEcoVociGaeBulk selezione = (ConsProgettiEcoVociGaeBulk)bp.getModel();
		bp.fillModel(context); 
		bp.valorizzaProgetto(context,selezione);

		
		ConsProgEcoVoceGaeDettBP dettBP = (ConsProgEcoVoceGaeDettBP) context.createBusinessProcess("ConsProgEcoVoceGaeDettBP");
		CompoundFindClause clause = new CompoundFindClause();
		clause.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, selezione.getPg_progetto());
		if (selezione.getTipoStampa().equals(ConsProgettiEcoVociGaeBulk.SINTETICA)){
			clause.addClause("AND", ConsProgettiEcoVociGaeBulk.SINTETICA, SQLBuilder.EQUALS, true);
			dettBP.impostaColonne(false);
		} else {
			dettBP.impostaColonne(true);
		}
		it.cnr.jada.util.RemoteIterator ri = dettBP.createComponentSession().cerca(context.getUserContext(), clause, selezione);
		
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		if (ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile");
		}
			
		dettBP.setIterator(context,ri);
		dettBP.setMultiSelection(false);
		return context.addBusinessProcess(dettBP);
		
	} catch (Exception e) {
			return handleException(context,e); 
	}
	
}
}