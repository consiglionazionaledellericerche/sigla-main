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
package it.cnr.contab.pdg00.consultazioni.action;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.pdg00.consultazioni.bp.*;
import it.cnr.contab.pdg00.consultazioni.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPDGAssestatoAggregatoAction extends ConsultazioniAction {
	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsPDGAssestatoAggregatoBP bp = (ConsPDGAssestatoAggregatoBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			ConsPDGAssestatoAggregatoBP consultazioneBP = null;
			if (bp instanceof ConsPDGAssestatoAggregatoSpeBP) 
				consultazioneBP = (ConsPDGAssestatoAggregatoSpeBP)context.createBusinessProcess("ConsPDGAssestatoAggregatoSpeBP");
			else
				consultazioneBP = (ConsPDGAssestatoAggregatoEtrBP)context.createBusinessProcess("ConsPDGAssestatoAggregatoEtrBP");
			
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
				if (consultazioneBP instanceof ConsPDGAssestatoAggregatoSpeBP)
					consultazioneBP.setIterator(context,consultazioneBP.createPdgAssestatoAggregatoComponentSession().findConsultazioneSpe(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
				else
					consultazioneBP.setIterator(context,consultazioneBP.createPdgAssestatoAggregatoComponentSession().findConsultazioneEtr(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
				
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doConsultaCds(ActionContext context) {
		return doConsulta(context, ConsPDGAssestatoAggregatoBP.LIVELLO_CDS);
	}
	public Forward doConsultaUo(ActionContext context) {
		return doConsulta(context, ConsPDGAssestatoAggregatoBP.LIVELLO_UO);
	}
	public Forward doConsultaLivello1(ActionContext context) {
		return doConsulta(context, ConsPDGAssestatoAggregatoBP.LIVELLO_LIV1);
	}
	public Forward doConsultaLivello2(ActionContext context) {
		return doConsulta(context, ConsPDGAssestatoAggregatoBP.LIVELLO_LIV2);
	}
	public Forward doConsultaDettagli(ActionContext context) {
		return doConsulta(context, ConsPDGAssestatoAggregatoBP.LIVELLO_DET);
	}
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsPDGAssestatoAggregatoBP) {
			ConsPDGAssestatoAggregatoBP bp = (ConsPDGAssestatoAggregatoBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}