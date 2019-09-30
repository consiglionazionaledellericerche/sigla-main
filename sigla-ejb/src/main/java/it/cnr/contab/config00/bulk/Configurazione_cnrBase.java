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

package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Configurazione_cnrBase extends Configurazione_cnrKey implements Keyed {
	// DT01 TIMESTAMP
	private java.sql.Timestamp dt01;

	// DT02 TIMESTAMP
	private java.sql.Timestamp dt02;

	// IM01 DECIMAL(20,6)
	private java.math.BigDecimal im01;

	// IM02 DECIMAL(20,6)
	private java.math.BigDecimal im02;

	// VAL01 VARCHAR(100)
	private java.lang.String val01;

	// VAL02 VARCHAR(100)
	private java.lang.String val02;

	// VAL03 VARCHAR(100)
	private java.lang.String val03;

	// VAL04 VARCHAR(100)
	private java.lang.String val04;

public Configurazione_cnrBase() {
	super();
}
public Configurazione_cnrBase(java.lang.String cd_chiave_primaria,java.lang.String cd_chiave_secondaria,java.lang.String cd_unita_funzionale,java.lang.Integer esercizio) {
	super(cd_chiave_primaria,cd_chiave_secondaria,cd_unita_funzionale,esercizio);
}
/* 
 * Getter dell'attributo dt01
 */
public java.sql.Timestamp getDt01() {
	return dt01;
}
/* 
 * Getter dell'attributo dt02
 */
public java.sql.Timestamp getDt02() {
	return dt02;
}
/* 
 * Getter dell'attributo im01
 */
public java.math.BigDecimal getIm01() {
	return im01;
}
/* 
 * Getter dell'attributo im02
 */
public java.math.BigDecimal getIm02() {
	return im02;
}
/* 
 * Getter dell'attributo val01
 */
public java.lang.String getVal01() {
	return val01;
}
/* 
 * Getter dell'attributo val02
 */
public java.lang.String getVal02() {
	return val02;
}
/* 
 * Getter dell'attributo val03
 */
public java.lang.String getVal03() {
	return val03;
}
/* 
 * Getter dell'attributo val04
 */
public java.lang.String getVal04() {
	return val04;
}
/* 
 * Setter dell'attributo dt01
 */
public void setDt01(java.sql.Timestamp dt01) {
	this.dt01 = dt01;
}
/* 
 * Setter dell'attributo dt02
 */
public void setDt02(java.sql.Timestamp dt02) {
	this.dt02 = dt02;
}
/* 
 * Setter dell'attributo im01
 */
public void setIm01(java.math.BigDecimal im01) {
	this.im01 = im01;
}
/* 
 * Setter dell'attributo im02
 */
public void setIm02(java.math.BigDecimal im02) {
	this.im02 = im02;
}
/* 
 * Setter dell'attributo val01
 */
public void setVal01(java.lang.String val01) {
	this.val01 = val01;
}
/* 
 * Setter dell'attributo val02
 */
public void setVal02(java.lang.String val02) {
	this.val02 = val02;
}
/* 
 * Setter dell'attributo val03
 */
public void setVal03(java.lang.String val03) {
	this.val03 = val03;
}
/* 
 * Setter dell'attributo val04
 */
public void setVal04(java.lang.String val04) {
	this.val04 = val04;
}
}
