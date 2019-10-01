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

package it.cnr.contab.doccont00.intcass.bulk;

/**
 * Insert the type's description here.
 * Creation date: (04/07/2003 10.20.18)
 * @author: Gennaro Borriello
 */
public class Stampa_sospesi_cnr_assoc_revVBulk extends Stampa_sospesi_riscontriVBulk {
	

	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	
	private boolean isUOForPrintEnabled;
/**
 * Stampa_sospesi_cnr_assoc_revVBulk constructor comment.
 */
public Stampa_sospesi_cnr_assoc_revVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {

    if (getUoForPrint() == null)
        return "*";
    if (getUoForPrint().getCd_unita_organizzativa() == null)
        return "*";

    return getUoForPrint().getCd_unita_organizzativa().toString();

}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2003 10.22.12)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdUOForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2003 10.22.12)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2003 10.22.12)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2003 10.22.12)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
			
}
}
