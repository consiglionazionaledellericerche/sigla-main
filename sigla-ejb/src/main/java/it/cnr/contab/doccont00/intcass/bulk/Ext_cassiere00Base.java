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

public class Ext_cassiere00Base extends Ext_cassiere00Key implements Keyed {
	// DATA VARCHAR(1000)
	private java.lang.String data;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// TR VARCHAR(2) NOT NULL
	private java.lang.String tr;

public Ext_cassiere00Base() {
	super();
}
public Ext_cassiere00Base(java.lang.Integer esercizio,java.lang.String nome_file,java.lang.Long pg_rec) {
	super(esercizio,nome_file,pg_rec);
}
/* 
 * Getter dell'attributo data
 */
public java.lang.String getData() {
	return data;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Getter dell'attributo tr
 */
public java.lang.String getTr() {
	return tr;
}
/* 
 * Setter dell'attributo data
 */
public void setData(java.lang.String data) {
	this.data = data;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
/* 
 * Setter dell'attributo tr
 */
public void setTr(java.lang.String tr) {
	this.tr = tr;
}
}
