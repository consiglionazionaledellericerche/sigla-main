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

package it.cnr.contab.segnalazioni00.bp;

import it.cnr.contab.segnalazioni00.bulk.Attivita_siglaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDAttivitaSiglaBP extends SimpleCRUDBP{

	public CRUDAttivitaSiglaBP() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CRUDAttivitaSiglaBP(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	public void validaAttivita(ActionContext context, Attivita_siglaBulk bulk) throws ValidationException
	{
		if(bulk.getStato() == null)
			throw new ValidationException("Il campo Stato non può essere vuoto!");
		if(bulk.getTipo_attivita() == null)
			throw new ValidationException("Il campo Tipo non può essere vuoto!");				
	}
}
