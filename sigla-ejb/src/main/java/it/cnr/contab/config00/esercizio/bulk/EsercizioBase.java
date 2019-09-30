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

package it.cnr.contab.config00.esercizio.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class EsercizioBase extends EsercizioKey implements Keyed {
	// DS_ESERCIZIO VARCHAR(100) NOT NULL
	private java.lang.String ds_esercizio;

	// IM_CASSA_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_cassa_iniziale;

	// ST_APERTURA_CHIUSURA CHAR(1)
	private java.lang.String st_apertura_chiusura;

public EsercizioBase() {
	super();
}
public EsercizioBase(java.lang.String cd_cds,java.lang.Integer esercizio) {
	super(cd_cds,esercizio);
}
/* 
 * Getter dell'attributo ds_esercizio
 */
public java.lang.String getDs_esercizio() {
	return ds_esercizio;
}
/* 
 * Getter dell'attributo im_cassa_iniziale
 */
public java.math.BigDecimal getIm_cassa_iniziale() {
	return im_cassa_iniziale;
}
/* 
 * Getter dell'attributo st_apertura_chiusura
 */
public java.lang.String getSt_apertura_chiusura() {
	return st_apertura_chiusura;
}
/* 
 * Setter dell'attributo ds_esercizio
 */
public void setDs_esercizio(java.lang.String ds_esercizio) {
	this.ds_esercizio = ds_esercizio;
}
/* 
 * Setter dell'attributo im_cassa_iniziale
 */
public void setIm_cassa_iniziale(java.math.BigDecimal im_cassa_iniziale) {
	this.im_cassa_iniziale = im_cassa_iniziale;
}
/* 
 * Setter dell'attributo st_apertura_chiusura
 */
public void setSt_apertura_chiusura(java.lang.String st_apertura_chiusura) {
	this.st_apertura_chiusura = st_apertura_chiusura;
}
}
