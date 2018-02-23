package it.cnr.contab.inventario00.docs.bulk;

/**
 * Oggetto creato per la gestione del controllo sulla Data Inizio Validità del Consegnatario
 *	in fase di apertura dell'Inventario.
 *
 * Creation date: (28/10/2002 11.09.17)
 * @author: Gennaro Borriello
 */
public class OptionRequestParameter implements java.io.Serializable {
	
	private java.lang.Boolean checkDataConsegnatarioRequired = Boolean.TRUE;
/**
 * OptionRequestParameter constructor comment.
 */
public OptionRequestParameter() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (28/10/2002 11.11.15)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getCheckDataConsegnatarioRequired() {
	return checkDataConsegnatarioRequired;
}
/**
 * Insert the method's description here.
 * Creation date: (28/10/2002 11.11.15)
 * @param newCheckDataConsegnatarioRequired java.lang.Boolean
 */
public void setCheckDataConsegnatarioRequired(java.lang.Boolean newCheckDataConsegnatarioRequired) {
	checkDataConsegnatarioRequired = newCheckDataConsegnatarioRequired;
}
}
