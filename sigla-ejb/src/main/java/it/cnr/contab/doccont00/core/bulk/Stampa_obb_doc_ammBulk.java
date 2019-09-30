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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

/**
 * Insert the type's description here.
 * Creation date: (02/05/2006)
 * @author: Flavia Giardina
 */
public class Stampa_obb_doc_ammBulk extends it.cnr.jada.bulk.OggettoBulk {

	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	private boolean cdsForPrintEnabled;
	private boolean uoForPrintEnabled;
	private boolean esercizioDocForPrintEnabled;
	private Integer esercizio;
	private Unita_organizzativaBulk uoForPrint;
	private Elemento_voceBulk elementoVoceForPrint;
	private Esercizio_baseBulk esercizioDocForPrint;
	
	
	
/**
 * Stampa_obb_doc_ammBulk constructor comment.
 */
public Stampa_obb_doc_ammBulk() {
	super();
}

public CdsBulk getCdsForPrint() {
	return cdsForPrint;
}

public void setCdsForPrint(CdsBulk cdsForPrint) {
	this.cdsForPrint = cdsForPrint;
}

public boolean isCdsForPrintEnabled() {
	return cdsForPrintEnabled;
}

public String getCdCdsForPrint() {

		if (getCdsForPrint()==null)
			return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
			return "*";

		return getCdsForPrint().getCd_unita_organizzativa().toString();
	}


public void setCdsForPrintEnabled(boolean cdsForPrintEnabled) {
	this.cdsForPrintEnabled = cdsForPrintEnabled;
}

public Integer getEsercizio() {
	return esercizio;
}

public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}

public Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}

public void setUoForPrint(Unita_organizzativaBulk uoForPrint) {
	this.uoForPrint = uoForPrint;
}

public boolean isUoForPrintEnabled() {
	return uoForPrintEnabled;
}

public void setUoForPrintEnabled(boolean uoForPrintEnabled) {
	this.uoForPrintEnabled = uoForPrintEnabled;
}

public String getCdUoForPrint() {

    if (getUoForPrint() == null)
        return "*";
    if (getUoForPrint().getCd_unita_organizzativa() == null)
        return "*";

    return getUoForPrint().getCd_unita_organizzativa().toString();

}
public Elemento_voceBulk getElementoVoceForPrint() {
	return elementoVoceForPrint;
}

public void setElementoVoceForPrint(Elemento_voceBulk elementoVoceForPrint) {
	this.elementoVoceForPrint = elementoVoceForPrint;
}

public String getCdElementoVoceForPrint() {

    if (getElementoVoceForPrint() == null)
        return "*";
    if (getElementoVoceForPrint().getCd_elemento_voce() == null)
        return "*";

    return getElementoVoceForPrint().getCd_elemento_voce().toString();

}

public Esercizio_baseBulk getEsercizioDocForPrint() {
	return esercizioDocForPrint;
}

public void setEsercizioDocForPrint(Esercizio_baseBulk esercizioDocForPrint) {
	this.esercizioDocForPrint = esercizioDocForPrint;
}

public String getCdEsercizioDocForPrint() {

    if (getEsercizioDocForPrint() == null)
        return "*";
    if (getEsercizioDocForPrint().getEsercizio() == null)
        return "*";

    return getEsercizioDocForPrint().getEsercizio().toString();

}
public boolean isROEsercizioDocForPrint() {
	 return isEsercizioDocForPrintEnabled();
}

public boolean isEsercizioDocForPrintEnabled() {
	  return esercizioDocForPrintEnabled;
}

public void setEsercizioDocForPrintEnabled(boolean b) {
	esercizioDocForPrintEnabled = b;
}
}
