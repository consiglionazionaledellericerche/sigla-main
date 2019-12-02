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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProgettoGestKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO NUMBER(4) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_PROGETTO NUMBER(10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	// TIPO_FASE VARCHAR(1) NOT NULL (PK)
	private java.lang.String tipo_fase;
	
public ProgettoGestKey() {
	super();
}
public ProgettoGestKey(java.lang.Integer esercizio,java.lang.Integer pg_progetto,java.lang.String tipo_fase) {
	super();
	this.esercizio = esercizio;
	this.pg_progetto = pg_progetto;
	this.tipo_fase = tipo_fase;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ProgettoGestKey)) return false;
	ProgettoGestKey k = (ProgettoGestKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
	if(!compareKey(getTipo_fase(),k.getTipo_fase())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_progetto
 */
public java.lang.Integer getPg_progetto() {
	return pg_progetto;
}
public int primaryKeyHashCode() {
	return	calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getPg_progetto())+
			calculateKeyHashCode(getTipo_fase());
}
/* 
 * Setter dell'attributo pg_progetto
 */
public void setPg_progetto(java.lang.Integer pg_progetto) {
	this.pg_progetto = pg_progetto;
}
public void setPg_progetto(java.math.BigDecimal pg_progetto) {
	this.pg_progetto = new java.lang.Integer(pg_progetto.intValue());
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
public java.lang.String getTipo_fase() {
	return tipo_fase;
}
public void setTipo_fase(java.lang.String tipo_fase) {
	this.tipo_fase = tipo_fase;
}
}