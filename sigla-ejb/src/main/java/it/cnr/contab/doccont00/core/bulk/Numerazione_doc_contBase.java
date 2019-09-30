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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_contBase extends Numerazione_doc_contKey implements Keyed {
	// CORRENTE DECIMAL(10,0) NOT NULL
	private java.lang.Long corrente;

	// PRIMO DECIMAL(10,0) NOT NULL
	private java.lang.Long primo;

	// ULTIMO DECIMAL(10,0) NOT NULL
	private java.lang.Long ultimo;

public Numerazione_doc_contBase() {
	super();
}
public Numerazione_doc_contBase(java.lang.String cd_cds,java.lang.String cd_tipo_documento_cont,java.lang.Integer esercizio) {
	super(cd_cds,cd_tipo_documento_cont,esercizio);
}
/* 
 * Getter dell'attributo corrente
 */
public java.lang.Long getCorrente() {
	return corrente;
}
/* 
 * Getter dell'attributo primo
 */
public java.lang.Long getPrimo() {
	return primo;
}
/* 
 * Getter dell'attributo ultimo
 */
public java.lang.Long getUltimo() {
	return ultimo;
}
/* 
 * Setter dell'attributo corrente
 */
public void setCorrente(java.lang.Long corrente) {
	this.corrente = corrente;
}
/* 
 * Setter dell'attributo primo
 */
public void setPrimo(java.lang.Long primo) {
	this.primo = primo;
}
/* 
 * Setter dell'attributo ultimo
 */
public void setUltimo(java.lang.Long ultimo) {
	this.ultimo = ultimo;
}
}
