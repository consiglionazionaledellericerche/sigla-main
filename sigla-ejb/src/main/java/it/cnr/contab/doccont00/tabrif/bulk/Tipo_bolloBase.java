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

package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_bolloBase extends Tipo_bolloKey implements Keyed {
	// CLASSE_TIPO_BOLLO CHAR(1) NOT NULL
	private java.lang.String classe_tipo_bollo;

	// DS_TIPO_BOLLO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_bollo;

	// FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

	// FL_DEFAULT CHAR(1) NOT NULL
	private java.lang.Boolean fl_default;

	// IM_TIPO_BOLLO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_tipo_bollo;

	// TI_ENTRATA_SPESA CHAR(1) NOT NULL
	private java.lang.String ti_entrata_spesa;

public Tipo_bolloBase() {
	super();
}
public Tipo_bolloBase(java.lang.String cd_tipo_bollo) {
	super(cd_tipo_bollo);
}
/* 
 * Getter dell'attributo classe_tipo_bollo
 */
public java.lang.String getClasse_tipo_bollo() {
	return classe_tipo_bollo;
}
/* 
 * Getter dell'attributo ds_tipo_bollo
 */
public java.lang.String getDs_tipo_bollo() {
	return ds_tipo_bollo;
}
/* 
 * Getter dell'attributo fl_cancellato
 */
public java.lang.Boolean getFl_cancellato() {
	return fl_cancellato;
}
/* 
 * Getter dell'attributo fl_default
 */
public java.lang.Boolean getFl_default() {
	return fl_default;
}
/* 
 * Getter dell'attributo im_tipo_bollo
 */
public java.math.BigDecimal getIm_tipo_bollo() {
	return im_tipo_bollo;
}
/* 
 * Getter dell'attributo ti_entrata_spesa
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}
/* 
 * Setter dell'attributo classe_tipo_bollo
 */
public void setClasse_tipo_bollo(java.lang.String classe_tipo_bollo) {
	this.classe_tipo_bollo = classe_tipo_bollo;
}
/* 
 * Setter dell'attributo ds_tipo_bollo
 */
public void setDs_tipo_bollo(java.lang.String ds_tipo_bollo) {
	this.ds_tipo_bollo = ds_tipo_bollo;
}
/* 
 * Setter dell'attributo fl_cancellato
 */
public void setFl_cancellato(java.lang.Boolean fl_cancellato) {
	this.fl_cancellato = fl_cancellato;
}
/* 
 * Setter dell'attributo fl_default
 */
public void setFl_default(java.lang.Boolean fl_default) {
	this.fl_default = fl_default;
}
/* 
 * Setter dell'attributo im_tipo_bollo
 */
public void setIm_tipo_bollo(java.math.BigDecimal im_tipo_bollo) {
	this.im_tipo_bollo = im_tipo_bollo;
}
/* 
 * Setter dell'attributo ti_entrata_spesa
 */
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.ti_entrata_spesa = ti_entrata_spesa;
}
}
