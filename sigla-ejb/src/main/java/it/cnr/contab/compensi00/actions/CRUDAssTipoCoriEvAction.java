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

import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_evBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDBP;


public class CRUDAssTipoCoriEvAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDAssTipoCoriEvAction() {
		super();
	}
	public Forward doBlankSearchFind_elemento_voce(ActionContext context,Ass_tipo_cori_evBulk bulk)
	{
		try{
			fillModel(context);
			Elemento_voceBulk elem= new Elemento_voceBulk();
			elem.setTi_gestione(bulk.getTi_gestione());
			elem.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
			elem.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			bulk.setElemento_voce(elem);
			((CRUDBP)context.getBusinessProcess()).setDirty(true);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	}
}
