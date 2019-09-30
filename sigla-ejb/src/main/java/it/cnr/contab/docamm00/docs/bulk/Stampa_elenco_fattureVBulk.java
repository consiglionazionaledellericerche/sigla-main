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

package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (26/08/2004 15.28.52)
 * @author: Gennaro Borriello
 */
public class Stampa_elenco_fattureVBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	// Esercizio di scrivania
	private Integer esercizio;

	// Esercizio Fattura
	private Integer esercizio_fattura;

	// Il Cds di scrivania
	private String cd_cds;

	// Terzo
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo;
	
	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private boolean uoForPrintEnabled;

	// Pg_da Fattura
	private java.lang.Integer pgInizio;
	// Pg_a Fattura
	private java.lang.Integer pgFine;

	// Dt_da Emissione Fattura
	private java.sql.Timestamp dt_emissione_from;
	// Dt_a Emissione Fattura
	private java.sql.Timestamp dt_emissione_to;

	// Dt_da Registrazione Fattura
	private java.sql.Timestamp dt_registrazione_from;
	// Dt_a Registrazione Fattura
	private java.sql.Timestamp dt_registrazione_to;

	// Dt_da inizio competenza
	private java.sql.Timestamp dt_inizio_comp_from;
	// Dt_a inizio competenza
	private java.sql.Timestamp dt_inizio_comp_to;

	// Dt_da fine competenza
	private java.sql.Timestamp dt_fine_comp_from;
	// Dt_a fine competenza
	private java.sql.Timestamp dt_fine_comp_to;

	private final java.text.DateFormat DATE_FORMAT = new java.text.SimpleDateFormat("yyyy/MM/dd");


/**
 * Stampa_elenco_fattureVBulk constructor comment.
 */
public Stampa_elenco_fattureVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
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
public Integer getCdTerzoForPrint() {

	if (getTerzo()==null)
		return new Integer(0);
	if (getTerzo().getCd_terzo()==null)
		return new Integer(0);

	return getTerzo().getCd_terzo();
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
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_emissione_from() {
		
	return dt_emissione_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_emissione_fromForPrint() {
	
	if (dt_emissione_from == null)
		return getLowDate();
		
	return dt_emissione_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_emissione_to() {
		
	return dt_emissione_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_emissione_toForPrint() {
	
	if (dt_emissione_to == null)
		return getHighDate();
		
	return dt_emissione_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_fine_comp_from() {
		
	return dt_fine_comp_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_fine_comp_fromForPrint() {
	
	if (dt_fine_comp_from == null)
		return getLowDate();
		
	return dt_fine_comp_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_fine_comp_to() {
		
	return dt_fine_comp_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_fine_comp_toForPrint() {
	
	if (dt_fine_comp_to == null)
		return getHighDate();
		
	return dt_fine_comp_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_inizio_comp_from() {
		
	return dt_inizio_comp_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_inizio_comp_fromForPrint() {
	
	if (dt_inizio_comp_from == null)
		return getLowDate();
		
	return dt_inizio_comp_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_inizio_comp_to() {
		
	return dt_inizio_comp_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_inizio_comp_toForPrint() {
	
	if (dt_inizio_comp_to == null)
		return getHighDate();
		
	return dt_inizio_comp_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_registrazione_from() {
	return dt_registrazione_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_registrazione_fromForPrint() {
	
	if (dt_registrazione_from == null)
		return getLowDate();
		
	return dt_registrazione_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_registrazione_to() {
	return dt_registrazione_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_registrazione_toForPrint() {
	
	if (dt_registrazione_to == null)
		return getHighDate();
		
	return dt_registrazione_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio_fattura() {
		
	return esercizio_fattura;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio_fatturaForPrint() {
	
	if (esercizio_fattura == null)
		return new Integer(0);
		
	return esercizio_fattura;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
private java.sql.Timestamp getHighDate() {
		
	return java.sql.Timestamp.valueOf("2100-12-31 00:00:00.0");
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.sql.Timestamp
 */
private java.sql.Timestamp getLowDate() {
	
	return java.sql.Timestamp.valueOf("1900-01-01 00:00:00.0");
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgFine() {
		
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgFineForPrint() {

	if (pgFine == null)
		return new Integer(999999999);
		
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgInizio() {
		
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgInizioForPrint() {

	if (pgInizio == null)
		return new Integer(0);
		
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 16.23.13)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
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
public boolean isROTerzo() {
	return getTerzo()==null || getTerzo().getCrudStatus()==NORMAL;
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
 * Creation date: (26/08/2004 15.39.45)
 * @return boolean
 */
public boolean isUoForPrintEnabled() {
	return uoForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_emissione_from java.sql.Timestamp
 */
public void setDt_emissione_from(java.sql.Timestamp newDt_emissione_from) {
	dt_emissione_from = newDt_emissione_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_emissione_to java.sql.Timestamp
 */
public void setDt_emissione_to(java.sql.Timestamp newDt_emissione_to) {
	dt_emissione_to = newDt_emissione_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_fine_comp_from java.sql.Timestamp
 */
public void setDt_fine_comp_from(java.sql.Timestamp newDt_fine_comp_from) {
	dt_fine_comp_from = newDt_fine_comp_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_fine_comp_to java.sql.Timestamp
 */
public void setDt_fine_comp_to(java.sql.Timestamp newDt_fine_comp_to) {
	dt_fine_comp_to = newDt_fine_comp_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_inizio_comp_from java.sql.Timestamp
 */
public void setDt_inizio_comp_from(java.sql.Timestamp newDt_inizio_comp_from) {
	dt_inizio_comp_from = newDt_inizio_comp_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_inizio_comp_to java.sql.Timestamp
 */
public void setDt_inizio_comp_to(java.sql.Timestamp newDt_inizio_comp_to) {
	dt_inizio_comp_to = newDt_inizio_comp_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_registrazione_from java.sql.Timestamp
 */
public void setDt_registrazione_from(java.sql.Timestamp newDt_registrazione_from) {
	dt_registrazione_from = newDt_registrazione_from;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newDt_registrazione_to java.sql.Timestamp
 */
public void setDt_registrazione_to(java.sql.Timestamp newDt_registrazione_to) {
	dt_registrazione_to = newDt_registrazione_to;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newEsercizio_fattura java.lang.Integer
 */
public void setEsercizio_fattura(java.lang.Integer newEsercizio_fattura) {
	esercizio_fattura = newEsercizio_fattura;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newPgFine java.lang.Integer
 */
public void setPgFine(java.lang.Integer newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newPgInizio java.lang.Integer
 */
public void setPgInizio(java.lang.Integer newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 16.23.13)
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2004 15.39.45)
 * @param newUoForPrintEnabled boolean
 */
public void setUoForPrintEnabled(boolean newUoForPrintEnabled) {
	uoForPrintEnabled = newUoForPrintEnabled;
}
}
