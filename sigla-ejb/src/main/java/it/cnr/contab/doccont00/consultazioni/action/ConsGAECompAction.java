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
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.consultazioni.action;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.doccont00.consultazioni.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class ConsGAECompAction extends ConsultazioniAction {

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsGAECompBP bp = (ConsGAECompBP)context.getBusinessProcess();
			int i=bp.getElementsCount();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();
			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}

			ConsGAECompBP consultazioneBP = null;
			if (bp instanceof ConsGAECompSpeBP) 
				consultazioneBP = (ConsGAECompSpeBP)context.createBusinessProcess("ConsGAECompSpeBP");
			else
				consultazioneBP = (ConsGAECompEtrBP)context.createBusinessProcess("ConsGAECompEtrBP");
			
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
			if (consultazioneBP instanceof ConsGAECompSpeBP)
				consultazioneBP.setIterator(context,consultazioneBP.createGAECompComponentSession().findConsultazioneSpe(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			else
				consultazioneBP.setIterator(context,consultazioneBP.createGAECompComponentSession().findConsultazioneEtr(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doConsultaVarPiu(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_VARP);
	}
	public Forward doConsultaVarMeno(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_VARM);
	}
	public Forward doConsultaImp(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_IMP);
	}
	public Forward doConsultaMan(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_MAN);
	}
	public Forward doConsultaVoci(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_VOC);
	}
	public Forward doConsultaAcc(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_ACC);
	}
	public Forward doConsultaRev(ActionContext context) {
		return doConsulta(context, ConsGAECompBP.LIVELLO_REV);
	}
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsGAECompBP) {
			ConsGAECompBP bp = (ConsGAECompBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}