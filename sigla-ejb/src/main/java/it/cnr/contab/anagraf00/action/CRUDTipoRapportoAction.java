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

package it.cnr.contab.anagraf00.action;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (13/03/2002 13.12.33)
 * @author: CNRADM
 */
public class CRUDTipoRapportoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDTipoRapportoAction constructor comment.
 */
public CRUDTipoRapportoAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 13.13.18)
 * @return it.cnr.jada.action.Forward
 * @param context it.cnr.jada.action.ActionContext
 */
public it.cnr.jada.action.Forward doOnTipoAnagraficoClick(it.cnr.jada.action.ActionContext context) {

	try{
		fillModel(context);

		SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
		Tipo_rapportoBulk tipoRapporto = (Tipo_rapportoBulk)bp.getModel();

		tipoRapporto.setTi_rapporto_altro(null);

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
}
