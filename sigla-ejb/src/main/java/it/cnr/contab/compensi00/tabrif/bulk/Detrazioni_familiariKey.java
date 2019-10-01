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

public class Detrazioni_familiariKey extends OggettoBulk implements KeyedPersistent {
	// IM_INFERIORE DECIMAL(15,2) NOT NULL (PK)
	private java.math.BigDecimal im_inferiore;

	// TI_PERSONA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_persona;

	// NUMERO DECIMAL(3,0) NOT NULL (PK)
	private java.lang.Integer numero;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Detrazioni_familiariKey() {
	super();
}
public Detrazioni_familiariKey(java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.Integer numero,java.lang.String ti_persona) {
	super();
	this.dt_inizio_validita = dt_inizio_validita;
	this.im_inferiore = im_inferiore;
	this.numero = numero;
	this.ti_persona = ti_persona;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Detrazioni_familiariKey)) return false;
	Detrazioni_familiariKey k = (Detrazioni_familiariKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getIm_inferiore(),k.getIm_inferiore())) return false;
	if(!compareKey(getTi_persona(),k.getTi_persona())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Detrazioni_familiariKey)) return false;
	Detrazioni_familiariKey k = (Detrazioni_familiariKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getIm_inferiore(),k.getIm_inferiore())) return false;
	if(!compareKey(getNumero(),k.getNumero())) return false;
	if(!compareKey(getTi_persona(),k.getTi_persona())) return false;
	return true;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Getter dell'attributo im_inferiore
 */
public java.math.BigDecimal getIm_inferiore() {
	return im_inferiore;
}
/* 
 * Getter dell'attributo numero
 */
public java.lang.Integer getNumero() {
	return numero;
}
/* 
 * Getter dell'attributo ti_persona
 */
public java.lang.String getTi_persona() {
	return ti_persona;
}
public int hashCode() {
	return
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getIm_inferiore())+
		calculateKeyHashCode(getTi_persona());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getIm_inferiore())+
		calculateKeyHashCode(getNumero())+
		calculateKeyHashCode(getTi_persona());
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Setter dell'attributo im_inferiore
 */
public void setIm_inferiore(java.math.BigDecimal im_inferiore) {
	this.im_inferiore = im_inferiore;
}
/* 
 * Setter dell'attributo numero
 */
public void setNumero(java.lang.Integer numero) {
	this.numero = numero;
}
/* 
 * Setter dell'attributo ti_persona
 */
public void setTi_persona(java.lang.String ti_persona) {
	this.ti_persona = ti_persona;
}
}
