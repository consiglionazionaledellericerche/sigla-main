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

package it.cnr.contab.anagraf00.bp;


import it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (24/04/2007 10:40:12)
 * @author: fgiardina
 */
public class CRUDTipologieIstatBP extends SimpleCRUDBP {

		
		public CRUDTipologieIstatBP() {
			super();
		}
public CRUDTipologieIstatBP(String function) throws BusinessProcessException {
	super(function);
}
public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	Tipologie_istatBulk tipologia = (Tipologie_istatBulk)super.createNewBulk(context);
	return tipologia;
}

public void validainserimento(ActionContext context, Tipologie_istatBulk bulk) throws ValidationException{
	if ( bulk.getDs_tipologia()==null) 
		throw new ValidationException("E' necessario inserire la Descrizione");
	
}

}
