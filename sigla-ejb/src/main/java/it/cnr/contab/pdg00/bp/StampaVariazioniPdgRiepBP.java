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

package it.cnr.contab.pdg00.bp;


import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Insert the type's description here.
 * Creation date: (18/10/2005 16.21.29)
 * @author: 
 */
public class StampaVariazioniPdgRiepBP extends it.cnr.contab.reports.bp.ParametricPrintBP{

	private SimpleDetailCRUDController crudRiepilogoVariazioni = new SimpleDetailCRUDController( "RiepilogoVariazioni", Pdg_variazioneBulk.class, "riepilogovariazioni", this) ;
		
		
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudRiepilogoVariazioni() {
		return crudRiepilogoVariazioni;
	}

	/**
	 * @param controller
	 */
	public void setCrudRiepilogoVariazioni(SimpleDetailCRUDController controller) {
		crudRiepilogoVariazioni = controller;
	}
}