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

package it.cnr.contab.cori00.actions;

import it.cnr.contab.cori00.bp.ConsLiquidCoriBP;
import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;
import it.cnr.contab.cori00.views.bulk.ParSelConsLiqCoriBulk;
import it.cnr.contab.cori00.views.bulk.VConsLiqCoriBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import javax.ejb.RemoveException;
import java.rmi.RemoteException;


public class ConsLiquidCoriAction extends BulkAction {

	public Forward doBringBackSearchDaLiquidazione(ActionContext context,
													   ParSelConsLiqCoriBulk parametri,
													   Liquid_coriBulk liquid_coriBulk)
			throws java.rmi.RemoteException {

		if (liquid_coriBulk != null && parametri.getDaLiquidazione() == null){
			parametri.setDaLiquidazione(liquid_coriBulk);
		}
		return context.findDefaultForward();
	}

	public Forward doBlankSearchDaLiquidazione(ActionContext context,
													   ParSelConsLiqCoriBulk parametri)
			throws java.rmi.RemoteException {

		parametri.setDaLiquidazione(null);
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchALiquidazione(ActionContext context,
												   ParSelConsLiqCoriBulk parametri,
												   Liquid_coriBulk liquid_coriBulk)
			throws java.rmi.RemoteException {

		if (liquid_coriBulk != null && parametri.getDaLiquidazione() == null){
			parametri.setaLiquidazione(liquid_coriBulk);
		}
		return context.findDefaultForward();
	}

	public Forward doBlankSearchALiquidazione(ActionContext context,
											   ParSelConsLiqCoriBulk parametri)
			throws java.rmi.RemoteException {

		parametri.setaLiquidazione(null);
		return context.findDefaultForward();
	}

	public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
			try {
				ConsLiquidCoriBP bp= (ConsLiquidCoriBP) context.getBusinessProcess();
				ParSelConsLiqCoriBulk selezione = (ParSelConsLiqCoriBulk)bp.getModel();
				bp.fillModel(context);
				bp.controlloSelezioneValorizzata(context,selezione);

					it.cnr.jada.util.RemoteIterator ri = bp.ricercaCori(context);
					ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
					if (ri.countElements() == 0) {
						it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
						throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
					}
						SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
						nbp.setIterator(context,ri);
						nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(VConsLiqCoriBulk.class));
						HookForward hook = (HookForward)context.findForward("seleziona");
						return context.addBusinessProcess(nbp);
				} catch (Exception e) {
					return handleException(context,e);
				}

		}
}