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

package it.cnr.contab.inventario00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (17/12/2003 14.58.36)
 * @author: Gennaro Borriello
 */
public class Stampa_beni_senza_utilizVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// Esercizio di scrivania
	private Integer esercizio;

	// Il Cds di scrivania
	private String cd_cds;

	
	// Data da
	private java.sql.Timestamp dataInizio;

	// Data a
	private java.sql.Timestamp dataFine;

	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;

	private boolean uOForPrintEnabled;

	private boolean uoEnte;

/**
 * Stampa_beni_senza_utilizVBulk constructor comment.
 */
public Stampa_beni_senza_utilizVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 14.59.33)
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

	if (getUoForPrint() != null && getUoForPrint().getCd_unita_organizzativa() != null){
		return getUoForPrint().getCd_unita_organizzativa().toString();
	} else {		
		if (isUoEnte()){
			return "*";
		}
		
		return getCd_cds() + ".*";
	}

	//String uo_default = getCd_cds() + ".*";

	//if (getUoForPrint()==null)
		//return uo_default;
	//if (getUoForPrint().getCd_unita_organizzativa()==null)
		//return uo_default;

	//return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 14.59.33)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 14.59.33)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2004 12.24.14)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 15.01.02)
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
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (22/03/2004 16.25.16)
 * @return boolean
 */
public boolean isUoEnte() {
	return uoEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2004 11.55.43)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return uOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 14.59.33)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 14.59.33)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 14.59.33)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2004 12.24.14)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (22/03/2004 16.25.16)
 * @param newUoEnte boolean
 */
public void setUoEnte(boolean newUoEnte) {
	uoEnte = newUoEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2003 15.01.02)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (18/03/2004 11.55.43)
 * @param newUOForPrintEnabled boolean
 */
public void setUOForPrintEnabled(boolean newUOForPrintEnabled) {
	uOForPrintEnabled = newUOForPrintEnabled;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
}
