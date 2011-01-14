package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.config00.sto.bulk.*;
// import it.cnr.jada.bulk.*;

public class ListaSospesiBulk extends it.cnr.jada.bulk.OggettoBulk {
	protected java.util.Collection sospesi_cnrColl = new java.util.ArrayList();

/**
 * RicercaCdsPerSospesiBulk constructor comment.
 */
public ListaSospesiBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2002 15.44.11)
 * @return java.util.Collection
 */
public java.util.Collection getSospesi_cnrColl() {
	return sospesi_cnrColl;
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2002 15.44.11)
 * @param newSospesi_entrata_cnrColl java.util.Collection
 */
public void setSospesi_cnrColl(java.util.Collection newSospesi_entrata_cnrColl) {
	sospesi_cnrColl = newSospesi_entrata_cnrColl;
}
}
