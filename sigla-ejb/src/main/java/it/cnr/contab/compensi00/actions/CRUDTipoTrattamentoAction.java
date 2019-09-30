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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;

/**
 * Insert the type's description here.
 * Creation date: (05/06/2002 13.31.29)
 * @author: CNRADM
 */
public class CRUDTipoTrattamentoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDTipoTrattamentoAction constructor comment.
 */
public CRUDTipoTrattamentoAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (05/06/2002 13.32.14)
 * @return it.cnr.jada.action.Forward
 */
public Forward doCaricaIntervalli(ActionContext context) {

	try {
		fillModel(context);
		CRUDTipoTrattamentoBP bp = (CRUDTipoTrattamentoBP)getBusinessProcess(context);
		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bp.getModel();

		java.util.List l = null;
		if (tratt.getCd_trattamento()!=null){
			TipoTrattamentoComponentSession session = (TipoTrattamentoComponentSession)bp.createComponentSession();
			l = session.caricaIntervalli(context.getUserContext(), tratt);
		}else
			tratt.setDs_ti_trattamento(null);
		
		tratt.setIntervalli(l);
		if (tratt.getIntervalli()!=null && !tratt.getIntervalli().isEmpty()){
			Tipo_trattamentoBulk obj = (Tipo_trattamentoBulk)tratt.getIntervalli().get(0);
			tratt.setDs_ti_trattamento(obj.getDs_ti_trattamento());
		}

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDTipoTrattamentoBP bp = (CRUDTipoTrattamentoBP)getBusinessProcess(context);
		bp.delete(context);
		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bp.getModel();
		if (tratt.getDt_ini_validita().compareTo(CompensoBulk.getDataOdierna())>0){
			bp.reset(context);
			bp.setMessage("Cancellazione effettuata");
		}else{
			bp.edit(context, tratt);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
