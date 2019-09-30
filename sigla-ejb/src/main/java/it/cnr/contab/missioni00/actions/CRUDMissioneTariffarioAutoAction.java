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

package it.cnr.contab.missioni00.actions;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.missioni00.bp.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (25/07/2002 17.00.47)
 * @author: Roberto Fantino
 */
public class CRUDMissioneTariffarioAutoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDMissioneTariffarioAutoAction constructor comment.
 */
public CRUDMissioneTariffarioAutoAction() {
	super();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDMissioneTariffarioAutoBP bp = (CRUDMissioneTariffarioAutoBP )getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			Missione_tariffario_autoBulk tariffario = (Missione_tariffario_autoBulk)bp.getModel();
			if (tariffario.getDt_inizio_validita().compareTo(CompensoBulk.getDataOdierna())>0){
				bp.reset(context);
				bp.setMessage("Cancellazione effettuata");
			}else{
				bp.edit(context, tariffario);
				bp.setMessage("Annullamento effettuato");
			}
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
