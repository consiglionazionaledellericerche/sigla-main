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
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.PdGVarSelezionatoreListaBP;
import it.cnr.contab.pdg00.bp.PdgVarApponiVistoListaBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.OptionBP;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PdgVarSelezionatoreListaAction extends ConsultazioniAction {


	public Forward doAggiorna(ActionContext context) {

		try {

			PdGVarSelezionatoreListaBP bp = (PdGVarSelezionatoreListaBP)context.getBusinessProcess();
			bp.setSelection(context);
			java.util.List l = bp.getSelectedElements(context);
			
			if (l.isEmpty()) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}

			if ( l == null )
				return (Forward)context.findDefaultForward();
			
			OptionBP optionBP = null;
			if (bp instanceof PdgVarApponiVistoListaBP)
				optionBP = openConfirm(context,"Attenzione tutti le variazioni selezionate verranno considerate vistate dal Dipartimento" +
						". Vuoi procedere?",OptionBP.CONFIRM_YES_NO,"doAggiornaStato");
			else
				optionBP = openConfirm(context,"Attenzione tutte le variazioni selezionate verranno aggiornate allo stato di Approvazione Formale" +
					". Vuoi procedere?",OptionBP.CONFIRM_YES_NO,"doAggiornaStato");
			return optionBP;	

		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doAggiornaStato(ActionContext context, OptionBP optionBP)  throws java.rmi.RemoteException {

		try {
			if (optionBP.getOption() == OptionBP.YES_BUTTON){
				PdGVarSelezionatoreListaBP bp = (PdGVarSelezionatoreListaBP)context.getBusinessProcess();
	
				java.util.List l = bp.getSelectedElements(context);
				bp.aggiornaStato(context, l);
				context.closeBusinessProcess(bp);
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

}
