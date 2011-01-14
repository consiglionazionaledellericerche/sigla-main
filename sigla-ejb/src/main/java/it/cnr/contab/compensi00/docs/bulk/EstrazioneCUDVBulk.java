package it.cnr.contab.compensi00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (02/02/2004 12.39.09)
 * @author: Gennaro Borriello
 */
public class EstrazioneCUDVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// ESERCIZIO 
	private java.lang.Integer esercizio;

	// ID_REPORT
	private java.math.BigDecimal id_report;
/**
 * EstrazioneCUDVBulk constructor comment.
 */
public EstrazioneCUDVBulk() {
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
