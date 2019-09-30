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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
/**
 * Insert the type's description here.
 * Creation date: (16/03/2004 17.49.23)
 * @author: Gennaro Borriello
 */
public class EstrazioneCUDBulk extends EstrazioneCUDVBulk {

	// ANAGRAFICO
	private AnagraficoBulk anagrafico;
/**
 * EstrazioneCUDBulk constructor comment.
 */
public EstrazioneCUDBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (02/02/2004 12.40.57)
 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagrafico() {
	return anagrafico;
}
/**
 * Restituisce il cd_anagrafico selezionato per l'elaborazione.
 *	Se Anagrafico == null restituisce %, ad indicare tutte le anagrafiche.
 * 
 * @return String
 */
public String getCdAnagParameter() {

	if (getAnagrafico()==null)
		return "%";
	if (getAnagrafico().getCd_anag()==null)
		return "%";

	return getAnagrafico().getCd_anag().toString();
}

public boolean isROAnagrafico(){
	return anagrafico == null || anagrafico.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (02/02/2004 12.40.57)
 * @param newAnagrafico it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
public void setAnagrafico(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagrafico) {
	anagrafico = newAnagrafico;
}
}
