package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_obbligazione_im_mandatoBulk extends ObbligazioneBulk {
		// IM_MANDATO DECIMAL(22,0)
	private java.math.BigDecimal im_mandato;


public V_obbligazione_im_mandatoBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_mandato'
 *
 * @return Il valore della proprietà 'im_mandato'
 */
public java.math.BigDecimal getIm_mandato() {
	return im_mandato;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'im_mandato'
 *
 * @param newIm_mandato	Il valore da assegnare a 'im_mandato'
 */
public void setIm_mandato(java.math.BigDecimal newIm_mandato) {
	im_mandato = newIm_mandato;
}
}
