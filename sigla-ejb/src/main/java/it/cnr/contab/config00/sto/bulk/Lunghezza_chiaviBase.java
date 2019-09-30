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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lunghezza_chiaviBase extends Lunghezza_chiaviKey implements Keyed {
	// LUNGHEZZA DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal lunghezza;

public Lunghezza_chiaviBase() {
	super();
}
public Lunghezza_chiaviBase(java.lang.String attributo,java.lang.Integer esercizio,java.lang.Integer livello,java.lang.String tabella) {
	super(attributo,esercizio,livello,tabella);
}
/* 
 * Getter dell'attributo lunghezza
 */
public java.math.BigDecimal getLunghezza() {
	return lunghezza;
}
/* 
 * Setter dell'attributo lunghezza
 */
public void setLunghezza(java.math.BigDecimal lunghezza) {
	this.lunghezza = lunghezza;
}
}
