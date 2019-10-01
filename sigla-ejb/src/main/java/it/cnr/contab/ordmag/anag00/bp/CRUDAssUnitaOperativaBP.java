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

package it.cnr.contab.ordmag.anag00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.ordmag.anag00.AssUnitaOperativaOrdBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDAssUnitaOperativaBP extends SimpleCRUDBP{
	private Unita_organizzativaBulk uoSrivania;

	private final SimpleDetailCRUDController assUnitaOperativaController = new SimpleDetailCRUDController("AssUnitaOperativaController",AssUnitaOperativaOrdBulk.class,"unitaOperativaColl",this);	

	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDAssUnitaOperativaBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDAssUnitaOperativaBP(String function) {
		super(function);
	}

	public SimpleDetailCRUDController getAssUnitaOperativaController() {
		return assUnitaOperativaController;
	}

	public boolean isEditingAssociazione() 
	{
		AssUnitaOperativaOrdBulk ass = (AssUnitaOperativaOrdBulk) getAssUnitaOperativaController().getModel();
		if(ass != null && ass.isToBeCreated())
			return true;
			
		return false;
	}	
}
