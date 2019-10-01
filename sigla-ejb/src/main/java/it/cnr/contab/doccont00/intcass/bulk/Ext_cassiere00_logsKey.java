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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ext_cassiere00_logsKey extends OggettoBulk implements KeyedPersistent {
	// NOME_FILE VARCHAR(20) NOT NULL (PK)
	private java.lang.String nome_file;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_ESECUZIONE DECIMAL(22,0) NOT NULL (PK)
	private java.math.BigDecimal pg_esecuzione;

public Ext_cassiere00_logsKey() {
	super();
}
public Ext_cassiere00_logsKey(java.lang.Integer esercizio,java.lang.String nome_file,java.math.BigDecimal pg_esecuzione) {
	super();
	this.esercizio = esercizio;
	this.nome_file = nome_file;
	this.pg_esecuzione = pg_esecuzione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ext_cassiere00_logsKey)) return false;
	Ext_cassiere00_logsKey k = (Ext_cassiere00_logsKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getNome_file(),k.getNome_file())) return false;
	if(!compareKey(getPg_esecuzione(),k.getPg_esecuzione())) return false;
	return true;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo nome_file
 */
public java.lang.String getNome_file() {
	return nome_file;
}
/* 
 * Getter dell'attributo pg_esecuzione
 */
public java.math.BigDecimal getPg_esecuzione() {
	return pg_esecuzione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getNome_file())+
		calculateKeyHashCode(getPg_esecuzione());
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo nome_file
 */
public void setNome_file(java.lang.String nome_file) {
	this.nome_file = nome_file;
}
/* 
 * Setter dell'attributo pg_esecuzione
 */
public void setPg_esecuzione(java.math.BigDecimal pg_esecuzione) {
	this.pg_esecuzione = pg_esecuzione;
}
}
