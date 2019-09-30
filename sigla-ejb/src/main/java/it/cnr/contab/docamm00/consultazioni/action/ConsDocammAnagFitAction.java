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

package it.cnr.contab.docamm00.consultazioni.action;

import it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagBP;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmAnagManrevBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkAction;

public class ConsDocammAnagFitAction extends BulkAction {


	public Forward doCerca(ActionContext context){
		it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagFitBP bp= (it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagFitBP) context.getBusinessProcess();
		try {
			bp.fillModel(context); 
			VDocAmmAnagManrevBulk bulk = (VDocAmmAnagManrevBulk) bp.getModel();
		if (bulk.getAnagrafico()!=null && bulk.getAnagrafico().getCd_anag()!=null){
			bulk.setCdAnag(bulk.getAnagrafico().getCd_anag());
			ConsDocammAnagBP consBP = (ConsDocammAnagBP)context.createBusinessProcess("ConsDocammAnagBP");
			context.addBusinessProcess(consBP);
			consBP.openIterator(context,bulk);
		    return context.findDefaultForward();
		}else
			throw new it.cnr.jada.comp.ApplicationException("Attenzione bisogna indicare un anagrafico!");
			
		} catch (Exception e) {
				return handleException(context,e); 
		} 
	}
	public Forward doCloseForm(ActionContext actioncontext)
			throws BusinessProcessException
		{
			try
			{
					return doConfirmCloseForm(actioncontext, 4);
			}
			catch(Throwable throwable)
			{
				return handleException(actioncontext, throwable);
			}
		}
}