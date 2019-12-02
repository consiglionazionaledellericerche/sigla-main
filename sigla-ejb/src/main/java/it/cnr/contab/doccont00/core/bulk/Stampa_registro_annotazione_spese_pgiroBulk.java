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

import it.cnr.contab.config00.sto.bulk.CdsBulk;

/**
 * Insert the type's description here.
 * Creation date: (27/01/2003 15.29.47)
 * @author: Simonetta Costa
 */
public class Stampa_registro_annotazione_spese_pgiroBulk extends ObbligazioneBulk {
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	private java.lang.Integer pgInizio;
	private java.lang.Integer pgFine;
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsOrigineForPrint;

	// Stati
	public final static String STATO_OBB_TUTTI = "*";
	public final static java.util.Dictionary statoObbligazioneKeys;
	private boolean cdsUOInScrivania = false;
	private CdsBulk cdsEnte;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;

	private boolean isUOForPrintEnabled;
	private boolean isCdsForPrintEnabled;

	private boolean stampa_cds;
	private boolean stampa_cnr;

	static 
	{
		statoObbligazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		statoObbligazioneKeys.put(STATO_OBB_PROVVISORIO, "Provvisoria");
		statoObbligazioneKeys.put(STATO_OBB_DEFINITIVO,  "Definitiva");
		statoObbligazioneKeys.put(STATO_OBB_STORNATO,    "Stornata");
		statoObbligazioneKeys.put(STATO_OBB_TUTTI, 		 "Tutti");
	}
/**
 * Stampa_registro_annotazione_spese_pgiroBulk constructor comment.
 */
public Stampa_registro_annotazione_spese_pgiroBulk() {
	super();
}
/**
 * Stampa_registro_annotazione_spese_pgiroBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer
 * @param pg_obbligazione java.lang.Long
 */
public Stampa_registro_annotazione_spese_pgiroBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione);
}
public CdsBulk getCdsEnte() {
	return cdsEnte;
}
public void setCdsEnte(CdsBulk cdsEnte) {
	this.cdsEnte = cdsEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsForPrint() {

	return getCdsEnte().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCdsOrigineCRForPrint() {
	
	if (getCdsOrigineForPrint()==null)
		return "*";
	if (getCdsOrigineForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getCdsOrigineForPrint().getCd_unita_organizzativa().toString();


}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsOrigineForPrint() {
	return cdsOrigineForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUoForPrint() {
	
	if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgInizio() {
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 16.22.06)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getStatoObbligazioneKeys() {
	return statoObbligazioneKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 12.00.24)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @return boolean
 */
public boolean isCdsForPrintEnabled() {
	return isCdsForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2003 12:20:20 PM)
 * @return boolean
 */
public boolean isCdsUOInScrivania() {
	return cdsUOInScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdsOrigineForPrint() {
	return getCdsOrigineForPrint()==null || getCdsOrigineForPrint().getCrudStatus()==NORMAL;
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
 * Creation date: (03/04/2003 9.19.48)
 * @return boolean
 */
public boolean isStampa_cds() {
	return stampa_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @return boolean
 */
public boolean isStampa_cnr() {
	return stampa_cnr;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @param newCdsOrigineForPrint it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCdsOrigineForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk newCdsOrigineForPrint) {
	cdsOrigineForPrint = newCdsOrigineForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2003 12:20:20 PM)
 * @param newCdsUOInScrivania boolean
 */
public void setCdsUOInScrivania(boolean newCdsUOInScrivania) {
	cdsUOInScrivania = newCdsUOInScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @param newIsCdsForPrintEnabled boolean
 */
public void setIsCdsForPrintEnabled(boolean newIsCdsForPrintEnabled) {
	isCdsForPrintEnabled = newIsCdsForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @param newPgFine java.lang.Integer
 */
public void setPgFine(java.lang.Integer newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2003 15.34.09)
 * @param newPgInizio java.lang.Integer
 */
public void setPgInizio(java.lang.Integer newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @param newStampa_cds boolean
 */
public void setStampa_cds(boolean newStampa_cds) {
	stampa_cds = newStampa_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.19.48)
 * @param newStampa_cnr boolean
 */
public void setStampa_cnr(boolean newStampa_cnr) {
	stampa_cnr = newStampa_cnr;
}
/**
 * Insert the method's description here.
 * Creation date: (23/01/2003 12.00.24)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unitaOrganizzativa) {
	uoForPrint = unitaOrganizzativa;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
}
