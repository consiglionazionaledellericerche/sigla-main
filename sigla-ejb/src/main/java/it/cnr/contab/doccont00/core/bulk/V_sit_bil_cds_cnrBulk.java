/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
