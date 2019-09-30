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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ScaglioneBase extends ScaglioneKey implements Keyed {
	// ALIQUOTA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota;

	// BASE_CALCOLO DECIMAL(3,0) NOT NULL
	private java.lang.Integer base_calcolo;

	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

	// IM_SUPERIORE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_superiore;

public ScaglioneBase() {
	super();
}
public ScaglioneBase(java.lang.String cd_contributo_ritenuta,java.lang.String cd_provincia,java.lang.String cd_regione,java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.Long pg_comune,java.lang.String ti_anagrafico,java.lang.String ti_ente_percipiente) {
	super(cd_contributo_ritenuta,cd_provincia,cd_regione,dt_inizio_validita,im_inferiore,pg_comune,ti_anagrafico,ti_ente_percipiente);
}
/* 
 * Getter dell'attributo aliquota
 */
public java.math.BigDecimal getAliquota() {
	return aliquota;
}
/* 
 * Getter dell'attributo base_calcolo
 */
public java.lang.Integer getBase_calcolo() {
	return base_calcolo;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo im_superiore
 */
public java.math.BigDecimal getIm_superiore() {
	return im_superiore;
}
/* 
 * Setter dell'attributo aliquota
 */
public void setAliquota(java.math.BigDecimal aliquota) {
	this.aliquota = aliquota;
}
/* 
 * Setter dell'attributo base_calcolo
 */
public void setBase_calcolo(java.lang.Integer base_calcolo) {
	this.base_calcolo = base_calcolo;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo im_superiore
 */
public void setIm_superiore(java.math.BigDecimal im_superiore) {
	this.im_superiore = im_superiore;
}
}
