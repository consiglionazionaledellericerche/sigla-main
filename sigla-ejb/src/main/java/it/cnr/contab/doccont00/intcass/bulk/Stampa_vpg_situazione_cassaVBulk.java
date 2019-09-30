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
 * Creation date: (26/08/2004 11.54.48)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_situazione_cassaVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// Esercizio di scrivania
	private Integer esercizio;

	// Il Cds di scrivania
	private String cd_cds;

	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private boolean uoForPrintEnabled;

	// Data da
	private java.sql.Timestamp dataInizio;

	// Data a
	private java.sql.Timestamp dataFine;

	// TI_STATO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_stato;

	public static final String STATO_INVIATO = "I";
	public static final String STATO_EMESSO  = "E";
	public static final String STATO_RISCONTRATO = "R";
	
	public final static java.util.Dictionary ti_statoKeys;
	static 
	{
		ti_statoKeys = new java.util.Hashtable();
		ti_statoKeys.put(STATO_INVIATO, "Inviato");
		ti_statoKeys.put(STATO_EMESSO,	"Emesso");
		ti_statoKeys.put(STATO_RISCONTRATO,	"Riscontrato");
	};
/**
 * Stampa_situazione_cassaVBulk constructor comment.
 */
public Stampa_vpg_situazione_cassaVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.lang.String
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
 * Creation date: (26/08/2004 12.00.28)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (13/02/2003 14.11.01)
 * @return java.lang.Integer
 */
public Integer getTc() {
	return new Integer(0);
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @return java.lang.String
 */
public java.lang.String getTi_stato() {
	return ti_stato;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getTi_statoKeys() {
	return ti_statoKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @return boolean
 */
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.01.53)
 * @return boolean
 */
public boolean isUoForPrintEnabled() {
	return uoForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @param newTi_stato java.lang.String
 */
public void setTi_stato(java.lang.String newTi_stato) {
	ti_stato = newTi_stato;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.00.28)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 12.01.53)
 * @param newUoForPrintEnabled boolean
 */
public void setUoForPrintEnabled(boolean newUoForPrintEnabled) {
	uoForPrintEnabled = newUoForPrintEnabled;
}
}
