package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
public class DispCassaCapitoloBulk extends it.cnr.jada.bulk.OggettoBulk {
	protected MandatoBulk mandato;
	protected List dispCassaColl;
/**
 * DispCassaCapitoloBulk constructor comment.
 */
public DispCassaCapitoloBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @return java.util.List
 */
public java.util.List getDispCassaColl() {
	return dispCassaColl;
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public MandatoBulk getMandato() {
	return mandato;
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @param newCassaColl java.util.List
 */
public void setDispCassaColl(java.util.List newCassaColl) {
	dispCassaColl = newCassaColl;
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public void setMandato(MandatoBulk newMandato) {
	mandato = newMandato;
}
}
