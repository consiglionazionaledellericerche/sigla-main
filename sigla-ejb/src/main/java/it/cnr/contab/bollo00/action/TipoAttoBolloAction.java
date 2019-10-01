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

package it.cnr.contab.bollo00.action;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class TipoAttoBolloAction extends it.cnr.jada.util.action.CRUDAction{
	private static final long serialVersionUID = 1L;

	public TipoAttoBolloAction() {
		super();
	}

	public Forward doOnTipoCalcoloChange(ActionContext context) {
		try{
			fillModel(context);
	
			Tipo_atto_bolloBulk tipoAtto = (Tipo_atto_bolloBulk)getBusinessProcess(context).getModel();
			tipoAtto.setLimiteCalcolo(null);
			tipoAtto.setRigheFoglio(null);
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
}