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

package it.cnr.contab.config00.tabnum.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_baseBase extends Numerazione_baseKey implements Keyed {
	// CD_CORRENTE VARCHAR(10)
	private java.lang.String cd_corrente;

	// CD_INIZIALE VARCHAR(10)
	private java.lang.String cd_iniziale;

	// CD_MASSIMO VARCHAR(10)
	private java.lang.String cd_massimo;

public Numerazione_baseBase() {
	super();
}
public Numerazione_baseBase(java.lang.String colonna,java.lang.Integer esercizio,java.lang.String tabella) {
	super(colonna,esercizio,tabella);
}
/* 
 * Getter dell'attributo cd_corrente
 */
public java.lang.String getCd_corrente() {
	return cd_corrente;
}
/* 
 * Getter dell'attributo cd_iniziale
 */
public java.lang.String getCd_iniziale() {
	return cd_iniziale;
}
/* 
 * Getter dell'attributo cd_massimo
 */
public java.lang.String getCd_massimo() {
	return cd_massimo;
}
/* 
 * Setter dell'attributo cd_corrente
 */
public void setCd_corrente(java.lang.String cd_corrente) {
	this.cd_corrente = cd_corrente;
}
/* 
 * Setter dell'attributo cd_iniziale
 */
public void setCd_iniziale(java.lang.String cd_iniziale) {
	this.cd_iniziale = cd_iniziale;
}
/* 
 * Setter dell'attributo cd_massimo
 */
public void setCd_massimo(java.lang.String cd_massimo) {
	this.cd_massimo = cd_massimo;
}
}
