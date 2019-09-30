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

public class V_disp_cassa_cnrBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(22,0)
	private java.math.BigDecimal esercizio;

	// IM_DISPONIBILTA_CASSA DECIMAL(22,0)
	private java.math.BigDecimal im_disponibilta_cassa;

public V_disp_cassa_cnrBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.math.BigDecimal getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo im_disponibilta_cassa
 */
public java.math.BigDecimal getIm_disponibilta_cassa() {
	return im_disponibilta_cassa;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.math.BigDecimal esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo im_disponibilta_cassa
 */
public void setIm_disponibilta_cassa(java.math.BigDecimal im_disponibilta_cassa) {
	this.im_disponibilta_cassa = im_disponibilta_cassa;
}
}
