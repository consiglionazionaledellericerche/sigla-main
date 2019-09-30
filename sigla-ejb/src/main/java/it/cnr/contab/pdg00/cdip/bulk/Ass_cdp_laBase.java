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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_cdp_laBase extends Ass_cdp_laKey implements Keyed {
	// DT_SCARICO TIMESTAMP
	private java.sql.Timestamp dt_scarico;

	// FL_DIP_ALTRA_UO CHAR(1) NOT NULL
	private java.lang.Boolean fl_dip_altra_uo;

	// PRC_LA_A1 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_la_a1;

	// PRC_LA_A2 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_la_a2;

	// PRC_LA_A3 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_la_a3;

	// STATO VARCHAR(5) NOT NULL
	private java.lang.String stato;

public Ass_cdp_laBase() {
	super();
}
public Ass_cdp_laBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese) {
	super(cd_centro_responsabilita,cd_linea_attivita,esercizio,id_matricola,mese);
}
/* 
 * Getter dell'attributo dt_scarico
 */
public java.sql.Timestamp getDt_scarico() {
	return dt_scarico;
}
/* 
 * Getter dell'attributo fl_dip_altra_uo
 */
public java.lang.Boolean getFl_dip_altra_uo() {
	return fl_dip_altra_uo;
}
/* 
 * Getter dell'attributo prc_la_a1
 */
public java.math.BigDecimal getPrc_la_a1() {
	return prc_la_a1;
}
/* 
 * Getter dell'attributo prc_la_a2
 */
public java.math.BigDecimal getPrc_la_a2() {
	return prc_la_a2;
}
/* 
 * Getter dell'attributo prc_la_a3
 */
public java.math.BigDecimal getPrc_la_a3() {
	return prc_la_a3;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo dt_scarico
 */
public void setDt_scarico(java.sql.Timestamp dt_scarico) {
	this.dt_scarico = dt_scarico;
}
/* 
 * Setter dell'attributo fl_dip_altra_uo
 */
public void setFl_dip_altra_uo(java.lang.Boolean fl_dip_altra_uo) {
	this.fl_dip_altra_uo = fl_dip_altra_uo;
}
/* 
 * Setter dell'attributo prc_la_a1
 */
public void setPrc_la_a1(java.math.BigDecimal prc_la_a1) {
	this.prc_la_a1 = prc_la_a1;
}
/* 
 * Setter dell'attributo prc_la_a2
 */
public void setPrc_la_a2(java.math.BigDecimal prc_la_a2) {
	this.prc_la_a2 = prc_la_a2;
}
/* 
 * Setter dell'attributo prc_la_a3
 */
public void setPrc_la_a3(java.math.BigDecimal prc_la_a3) {
	this.prc_la_a3 = prc_la_a3;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
