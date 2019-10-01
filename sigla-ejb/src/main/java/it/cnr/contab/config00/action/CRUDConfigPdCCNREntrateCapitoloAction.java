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

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * Azione che gestisce le richieste relative alla Gestione Unita' Organizzativa
 */
public class CRUDConfigPdCCNREntrateCapitoloAction extends it.cnr.jada.util.action.CRUDAction {
	public CRUDConfigPdCCNREntrateCapitoloAction() {
		super();
	}
	
	public Forward doBringBackSearchFind_elemento_padre(ActionContext context, Elemento_voceBulk elemento_voce, Elemento_voceBulk elemento_padre)	throws java.rmi.RemoteException {
		if (elemento_padre != null) {
			elemento_voce.setElemento_padre(elemento_padre);
			elemento_voce.setFl_partita_giro(elemento_padre.getFl_partita_giro());
		}
		return context.findDefaultForward();	
	}

}
