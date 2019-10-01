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
 * Creation date: (06/06/2002 16.33.36)
 * @author: Roberto Fantino
 */
public class CRUDTipoContributoRitenutaAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDTipoContributoRitenutaAction constructor comment.
 */
public CRUDTipoContributoRitenutaAction() {
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
		CRUDTipoContributoRitenutaBP bp = (CRUDTipoContributoRitenutaBP)getBusinessProcess(context);
		Tipo_contributo_ritenutaBulk tipoCori = (Tipo_contributo_ritenutaBulk)bp.getModel();

		java.util.List l = null;
		if (tipoCori.getCd_contributo_ritenuta()!=null){
			TipoContributoRitenutaComponentSession session = (TipoContributoRitenutaComponentSession)bp.createComponentSession();
			l = session.caricaIntervalli(context.getUserContext(), tipoCori);
		}else
			tipoCori.setDs_contributo_ritenuta(null);
		
		tipoCori.setIntervalli(l);
		if (tipoCori.getIntervalli()!=null && !tipoCori.getIntervalli().isEmpty()){
			Tipo_contributo_ritenutaBulk obj = (Tipo_contributo_ritenutaBulk)tipoCori.getIntervalli().get(0);
			tipoCori.setDs_contributo_ritenuta(obj.getDs_contributo_ritenuta());
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

		CRUDTipoContributoRitenutaBP bp = (CRUDTipoContributoRitenutaBP)getBusinessProcess(context);
		bp.delete(context);
		Tipo_contributo_ritenutaBulk cori = (Tipo_contributo_ritenutaBulk)bp.getModel();
		if (cori.getDt_ini_validita().compareTo(CompensoBulk.getDataOdierna())>0){
			bp.reset(context);
			bp.setMessage("Cancellazione effettuata");
		}else{
			bp.edit(context, cori);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
