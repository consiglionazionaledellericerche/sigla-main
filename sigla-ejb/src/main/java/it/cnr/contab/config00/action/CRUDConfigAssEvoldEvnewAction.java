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

package it.cnr.contab.config00.action;

import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDConfigAssEvoldEvnewAction extends CRUDAction{
	private static final long serialVersionUID = 1L;

	public CRUDConfigAssEvoldEvnewAction() {
		super();
	}

	public Forward doOnTipoGestioneSearchChange(ActionContext context) {
		try{
			fillModel(context);
			SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
			Ass_evold_evnewBulk bulk = (Ass_evold_evnewBulk)bp.getModel();
			bulk.setElemento_voce_old(new Elemento_voceBulk());
			bulk.setElemento_voce_new(new Elemento_voceBulk());
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
}
