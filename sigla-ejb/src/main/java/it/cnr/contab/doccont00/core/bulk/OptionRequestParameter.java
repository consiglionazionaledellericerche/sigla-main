package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (5/28/2002 3:05:49 PM)
 * @author: Simonetta Costa
 */
public class OptionRequestParameter implements java.io.Serializable {
	private java.lang.Boolean checkDisponibilitaDiCassaRequired = Boolean.TRUE;
	private java.lang.Boolean checkDisponibilitaContrattoRequired = Boolean.TRUE;
	
/**
 * OptionRequestParameter constructor comment.
 */
public OptionRequestParameter() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 12:07:44 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean isCheckDisponibilitaDiCassaRequired() {
	return checkDisponibilitaDiCassaRequired;
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 12:07:44 PM)
 * @param newCheckDisponibilitaDiCassaRequired java.lang.Boolean
 */
public void setCheckDisponibilitaDiCassaRequired(java.lang.Boolean newCheckDisponibilitaDiCassaRequired) {
	checkDisponibilitaDiCassaRequired = newCheckDisponibilitaDiCassaRequired;
}

public void setCheckDisponibilitaContrattoRequired(java.lang.Boolean checkDisponibilitaContrattoRequired) {
	this.checkDisponibilitaContrattoRequired = checkDisponibilitaContrattoRequired;
}
public java.lang.Boolean isCheckDisponibilitaContrattoRequired() {
	return checkDisponibilitaContrattoRequired;
}
}
