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
 * Creation date: (02/02/2004 14.13.44)
 * @author: Gennaro Borriello
 */
public class EstrazioniFiscaliVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// ESERCIZIO 
	private java.lang.Integer esercizio;

	// ANAGRAFICO
	private AnagraficoBulk anagrafico;

	// ID_REPORT
	private java.math.BigDecimal id_report;
/**
 * EstrazioniFiscaliVBulk constructor comment.
 */
public EstrazioniFiscaliVBulk() {
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
/**
 * Insert the method's description here.
 * Creation date: (02/02/2004 12.40.57)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (03/02/2004 14.52.50)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getId_report() {
	return id_report;
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
/**
 * Insert the method's description here.
 * Creation date: (02/02/2004 12.40.57)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (03/02/2004 14.52.50)
 * @param newId_report java.math.BigDecimal
 */
public void setId_report(java.math.BigDecimal newId_report) {
	id_report = newId_report;
}
}
