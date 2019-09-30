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
