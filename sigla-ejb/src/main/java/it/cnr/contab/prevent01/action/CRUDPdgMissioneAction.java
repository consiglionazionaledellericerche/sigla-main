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

/*
 * Created on Oct 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import it.cnr.contab.prevent01.bp.CRUDPdgMissioneBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class CRUDPdgMissioneAction extends it.cnr.jada.util.action.CRUDAction {
	private static final long serialVersionUID = 1L;

	/**
	 * CRUDAnagraficaAction constructor comment.
	 */
	public CRUDPdgMissioneAction() {
		super();
	}

	public Forward doAggiungiTipoUo(ActionContext context) {
		try	{
			CRUDPdgMissioneBP bp = (CRUDPdgMissioneBP)context.getBusinessProcess();
			bp.addToAssPdgMissioneTipiUo(context);
			return context.findDefaultForward();
		} catch(Exception ex) {
			return handleException(context,ex);
		}
	}


	public Forward doRimuoviTipoUo(ActionContext context) {
		try	{
			CRUDPdgMissioneBP bp = (CRUDPdgMissioneBP)context.getBusinessProcess();
			bp.removeFromAssPdgMissioneTipiUo(context);
			return context.findDefaultForward();
		} catch(Exception ex) {
			return handleException(context,ex);
		}
	}
}
