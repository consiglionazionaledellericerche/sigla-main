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

package it.cnr.contab.inventario00.actions;

import java.rmi.RemoteException;
import it.cnr.contab.inventario00.bp.ConsInventarioRicBP;
import it.cnr.contab.inventario00.bp.ConsInventarioRicRaggruppatiBP;
import it.cnr.contab.inventario00.consultazioni.bulk.VInventarioRicognizioneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkAction;


public class ConsInventarioRicAction extends BulkAction {

	public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException{
		try{
			ConsInventarioRicBP bp= (ConsInventarioRicBP) context.getBusinessProcess();
			VInventarioRicognizioneBulk inv = (VInventarioRicognizioneBulk)bp.getModel();
			bp.fillModel(context); 
			bp.validazione(context,inv);
			ConsInventarioRicRaggruppatiBP DettBP = (ConsInventarioRicRaggruppatiBP) context.createBusinessProcess("ConsInventarioRicRaggruppatiBP");
			it.cnr.jada.util.RemoteIterator ri = DettBP.createConsRegistroInventarioComponentSession().findConsultazioneRicognizione(context.getUserContext(),DettBP.getBaseclause(),DettBP.getFindclause(),inv);
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
			if (ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile");
			}
			DettBP.setIterator(context,ri);
			DettBP.setMultiSelection(false);
			return context.addBusinessProcess(DettBP);			
		} catch (Exception e) {
				return handleException(context,e); 
		}
	}	
}