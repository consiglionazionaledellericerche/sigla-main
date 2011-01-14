package it.cnr.contab.doccont00.ordine.bulk;

/**
 * Insert the type's description here.
 * Creation date: (20/05/2003 17.54.43)
 * @author: Gennaro Borriello
 */
public class Stampa_ordineBulk extends OrdineBulk {
	
	private java.lang.Integer pgInizio;
	private java.lang.Integer pgFine;
	
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	
	private boolean isUOForPrintEnabled;
/**
 * Stampa_ordineBulk constructor comment.
 */
public Stampa_ordineBulk() {
	super();
}
/**
 * Stampa_ordineBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_ordine java.lang.Long
 */
public Stampa_ordineBulk(String cd_cds, Integer esercizio, Long pg_ordine) {
	super(cd_cds, esercizio, pg_ordine);
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {
	
	/*if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";*/
		
	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
 * @return java.lang.Integer
 */
public java.lang.Integer getPgInizio() {
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
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
 * Creation date: (20/05/2003 18.01.42)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
 * @param newPgFine java.lang.Integer
 */
public void setPgFine(java.lang.Integer newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
 * @param newPgInizio java.lang.Integer
 */
public void setPgInizio(java.lang.Integer newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/05/2003 18.01.42)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
public void validate() throws it.cnr.jada.bulk.ValidationException {
}		
}
