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
 * Creation date: (04/07/2003 10.21.02)
 * @author: Gennaro Borriello
 */
public class Stampa_sospesi_cnr_assoc_cdsVBulk extends Stampa_sospesi_riscontriVBulk {

	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;

	private boolean isCdsEnte;
/**
 * Stampa_sospesi_cnr_assoc_cdsVBulk constructor comment.
 */
public Stampa_sospesi_cnr_assoc_cdsVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsCRForPrint() {

   if (isCdsEnte()){
	    if (getCdsForPrint() == null || getCdsForPrint().getCd_unita_organizzativa() == null || getCdsForPrint().getCd_unita_organizzativa().equals(""))
	    	return "*";

	    return getCdsForPrint().getCd_unita_organizzativa();
    }
    
    return getCd_cds();
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2003 10.53.44)
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
	return cdsForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2003 10.53.44)
 * @return boolean
 */
public boolean isCdsEnte() {
	return isCdsEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdCdsForPrint() {
	return getCdsForPrint()==null || getCdsForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2003 10.53.44)
 * @param newCdsForPrint it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk newCdsForPrint) {
	cdsForPrint = newCdsForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (10/12/2003 10.53.44)
 * @param newIsCdsEnte boolean
 */
public void setIsCdsEnte(boolean newIsCdsEnte) {
	isCdsEnte = newIsCdsEnte;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
			
}
}
