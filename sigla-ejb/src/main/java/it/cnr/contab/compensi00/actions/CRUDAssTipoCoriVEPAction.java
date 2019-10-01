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


public class CRUDAssTipoCoriVEPAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDAssTipoCoriVEPAction() {
		super();
	}
	/*public Forward doBlankSearchFind_voce(ActionContext context,Ass_tipo_cori_voce_epBulk bulk)
	{
		try{
			fillModel(context);
			ContoBulk elem= new ContoBulk();
			elem.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			bulk.setVoce_ep(elem);
			bulk.setVoce_ep_contr(elem);
			((CRUDBP)context.getBusinessProcess()).setDirty(true);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	}*/
}
