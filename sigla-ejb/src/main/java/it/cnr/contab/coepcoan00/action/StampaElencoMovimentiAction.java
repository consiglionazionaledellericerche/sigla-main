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
 * Created on Ago 8, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.coepcoan00.action;

import it.cnr.contab.coepcoan00.core.bulk.Stampa_elenco_movimentiBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaElencoMovimentiAction extends it.cnr.contab.reports.action.ParametricPrintAction {

	/**
	 * 
	 */
	public StampaElencoMovimentiAction() {
		super();
	}
	/**
	 * Ripulisce il searchtool dell'Elemento voce
	 * @author mspasiano
	 * @return it.cnr.jada.action.Forward
	 * @param context it.cnr.jada.action.ActionContext
	 */
	/*public Forward doOnTipoChange(ActionContext context) {

		try{
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_elenco_movimentiBulk stampa = (Stampa_elenco_movimentiBulk)bp.getModel();
			//String beforeChange = stampa.getTi_etr_spe();
			fillModel(context);
			stampa = (Stampa_elenco_movimentiBulk)bp.getModel();
			//if (!beforeChange.equals(stampa.getTi_etr_spe()))
			//  stampa.setElementoVoceForPrint(null);
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}*/
	
	public Forward doSeleziona(ActionContext context) {
		try{
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_elenco_movimentiBulk stampa = (Stampa_elenco_movimentiBulk)bp.getModel();
			stampa.selezionaRagruppamenti();
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	
}
