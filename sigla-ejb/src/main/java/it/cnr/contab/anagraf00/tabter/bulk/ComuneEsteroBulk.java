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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Gestione dei dati relativi ai comuni esteri nella tabella Comune
 */

public class ComuneEsteroBulk extends ComuneBulk {

public ComuneEsteroBulk() {
	super();
}
public ComuneEsteroBulk(java.lang.Long pg_comune) {
	super(pg_comune);
}
public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {

	super.initializeForInsert(bp, context);
	setTi_italiano_estero(COMUNE_ESTERO);
	setCd_catastale(CODICE_CATASTALE_ESTERO);
	return this;
}
public boolean isEstero(){
	return true;
}
/**
 * Oltre alla normale validate da un avviso di errore se la nazione è nulla.
 *
 * @exeption it.cnr.jada.bulk.ValidationException
*/
public void validate() throws ValidationException {

	super.validate();
	if (getDs_comune()==null)
		throw new ValidationException("Il campo DESCRIZIONE non può essere vuoto");
	if(getPg_nazione() == null)
		throw new ValidationException("Il campo NAZIONE non può essere vuoto");
}
}
