package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ass_mandato_fondo_ecoBulk extends V_ass_mandato_fondo_ecoBase {

	private it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = null;
	private it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk fondo;
public V_ass_mandato_fondo_ecoBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/31/2002 4:38:38 PM)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk getFondo() {
	return fondo;
}
/**
 * Insert the method's description here.
 * Creation date: (5/31/2002 4:38:15 PM)
 * @return it.cnr.contab.doccont00.core.bulk.MandatoIBulk
 */
public it.cnr.contab.doccont00.core.bulk.MandatoIBulk getMandato() {
	return mandato;
}
/**
 * Insert the method's description here.
 * Creation date: (5/31/2002 4:38:38 PM)
 * @param newFondo it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public void setFondo(it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk newFondo) {
	fondo = newFondo;
}
/**
 * Insert the method's description here.
 * Creation date: (5/31/2002 4:38:15 PM)
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoIBulk
 */
public void setMandato(it.cnr.contab.doccont00.core.bulk.MandatoIBulk newMandato) {
	mandato = newMandato;
}
}
