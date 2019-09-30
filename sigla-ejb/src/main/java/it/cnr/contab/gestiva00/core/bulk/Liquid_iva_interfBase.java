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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_iva_interfBase extends Liquid_iva_interfKey implements Keyed {
	// IVA_DEBITO DECIMAL(15,2)
	private java.math.BigDecimal iva_debito;

	// IVA_CREDITO DECIMAL(15,2)
	private java.math.BigDecimal iva_credito;
	
	// NOTE VARCHAR(1000)
	private java.lang.String note;

	// FL_GIA_ELABORATA VARCHAR(1) NOT NULL
	private java.lang.Boolean fl_gia_eleborata;

public Liquid_iva_interfBase() {
	super();
}
public Liquid_iva_interfBase(java.lang.Integer pg_caricamento,java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.String ti_liquidazione) {
	super(pg_caricamento,cd_cds,esercizio,cd_unita_organizzativa,dt_fine,dt_inizio,ti_liquidazione);
}
/* 
 * Getter dell'attributo iva_credito
 */
public java.math.BigDecimal getIva_credito() {
	return iva_credito;
}
/* 
 * Getter dell'attributo iva_debito
 */
public java.math.BigDecimal getIva_debito() {
	return iva_debito;
}
/* 
 * Getter dell'attributo fl_gia_elaborata
 */
public java.lang.Boolean getFl_gia_eleborata() {
	return fl_gia_eleborata;
}
/* 
 * Getter dell'attributo note
 */
public java.lang.String getNote() {
	return note;
}
/* 
 * Setter dell'attributo note
 */
public void setNote(java.lang.String note) {
	this.note = note;
}
/* 
 * Setter dell'attributo iva_credito
 */
public void setIva_credito(java.math.BigDecimal iva_credito) {
	this.iva_credito = iva_credito;
}
/* 
 * Setter dell'attributo iva_debito
 */
public void setIva_debito(java.math.BigDecimal iva_debito) {
	this.iva_debito = iva_debito;
}
/* 
 * Setter dell'attributo stato
 */
public void setFl_gia_eleborata(java.lang.Boolean fl_gia_eleborata) {
	this.fl_gia_eleborata = fl_gia_eleborata;
}
}
