package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_sit_bil_cds_cnrBulk extends V_sit_bil_cds_cnrBase {

public V_sit_bil_cds_cnrBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_accertato_accreditato'
 *
 * @return Il valore della proprietà 'im_accertato_accreditato'
 */
public java.math.BigDecimal getIm_accertato_accreditato() {
	return getIm_accertato_impegnato().subtract( getIm_accreditato() );
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_incassato_accreditato'
 *
 * @return Il valore della proprietà 'im_incassato_accreditato'
 */
public java.math.BigDecimal getIm_incassato_accreditato() {
	return getIm_pagato_incassato_competenza().subtract( getIm_accreditato() );
}
}
