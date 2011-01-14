package it.cnr.contab.compensi00.docs.bulk;

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
}
