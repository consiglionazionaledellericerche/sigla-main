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
