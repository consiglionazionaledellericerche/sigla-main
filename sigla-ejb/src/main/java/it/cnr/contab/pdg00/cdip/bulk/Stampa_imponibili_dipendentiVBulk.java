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

package it.cnr.contab.pdg00.cdip.bulk;

/**
 * Insert the type's description here.
 * Creation date: (14/04/2003 13.21.13)
 * @author: Gennaro Borriello
 */
public class Stampa_imponibili_dipendentiVBulk extends it.cnr.jada.bulk.OggettoBulk {

	private Integer esercizio;
	private String cd_cds;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	
	private boolean isUOForPrintEnabled;
/**
 * Stampa_imponibili_dipendentiVBulk constructor comment.
 */
public Stampa_imponibili_dipendentiVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (14/04/2003 13.26.57)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {
	
	if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (20/04/2004 11.21.05)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (14/04/2003 13.26.57)
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
public boolean isROCdUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (14/04/2003 13.26.57)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (14/04/2003 13.26.57)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (20/04/2004 11.21.05)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (14/04/2003 13.26.57)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (14/04/2003 13.26.57)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
}
