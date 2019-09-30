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

package it.cnr.contab.pdg00.action;

/**
 * Gestione delle Action del crud dei dettagli del PdG
 */

import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.jada.util.action.BulkBP;

public class CRUDPdGAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDPdGAction() {
	super();
}
	/**
	 * Cancellazione concatenata dell'Elemento_voce selezionato alla cancellazione della Linea_attivita selezionata
	 *
	 * @param context	Contesto in utilizzo.
	 * @param bulk		Non usato.
	 *
	 * @return	it.cnr.jada.action.Forward	La pagina da visualizzare.
	 */

	public it.cnr.jada.action.Forward doBlankSearchLinea_attivita(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) {

		Pdg_preventivo_detBulk model = (Pdg_preventivo_detBulk)((BulkBP)context.getBusinessProcess()).getModel();
		model.setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk());
		model.setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk());
		return context.findDefaultForward();
	}

}
