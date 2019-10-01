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
import it.cnr.contab.compensi00.tabrif.bulk.Quadri_770Bulk;

/**
 * Creation date: (24/09/2004)
 * @author: Aurelio D'Amico
 * @version: 1.0
 */
public class Estrazione770Bulk extends it.cnr.jada.bulk.OggettoBulk {

	// ESERCIZIO 
	private java.lang.Integer esercizio;

	// ID_REPORT
	private java.math.BigDecimal id_report;
	
	// QUADRI_770
	private Quadri_770Bulk quadri_770;
	
/**
 * EstrazioneCUDVBulk constructor comment.
 */
public Estrazione770Bulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @param newId_report java.math.BigDecimal
 */
public void setId_report(java.math.BigDecimal newId_report) {
	id_report = newId_report;
}
public Quadri_770Bulk getQuadri_770() {
	return quadri_770;
}
public void setQuadri_770(Quadri_770Bulk quadri_770) {
	this.quadri_770 = quadri_770;
}
public boolean isROQuadri_770(){
	return quadri_770 == null || quadri_770.getCrudStatus() == NORMAL;
}
public boolean isFileOrdinario() {
	if(getQuadri_770() != null && getQuadri_770().getCd_quadro()!= null)
	{
		if(getQuadri_770().getTi_modello() != null && getQuadri_770().getTi_modello().compareTo("O")== 0)
			return Boolean.TRUE;
	}
	return Boolean.FALSE;
}
public boolean isFileSemplificato() {
	if(getQuadri_770() != null && getQuadri_770().getCd_quadro()!= null)
	{
		if(getQuadri_770().getTi_modello() != null && getQuadri_770().getTi_modello().compareTo("S")== 0)
			return Boolean.TRUE;
	}
	return Boolean.FALSE;
}
}
