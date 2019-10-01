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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_coriBase extends Liquid_coriKey implements Keyed {
	// DA_ESERCIZIO_PRECEDENTE CHAR(1) NOT NULL
	private Boolean da_esercizio_precedente;

	// DT_A TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a;

	// DT_DA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da;

	// STATO CHAR(1)
	private java.lang.String stato;

public Liquid_coriBase() {
	super();
}
public Liquid_coriBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Integer pg_liquidazione) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_liquidazione);
}
/**
 * Insert the method's description here.
 * Creation date: (30/05/2003 12.09.13)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getDa_esercizio_precedente() {
	return da_esercizio_precedente;
}
/* 
 * Getter dell'attributo dt_a
 */
public java.sql.Timestamp getDt_a() {
	return dt_a;
}
/* 
 * Getter dell'attributo dt_da
 */
public java.sql.Timestamp getDt_da() {
	return dt_da;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/**
 * Insert the method's description here.
 * Creation date: (30/05/2003 12.09.13)
 * @param newDa_esercizio_precedente java.lang.Boolean
 */
public void setDa_esercizio_precedente(java.lang.Boolean newDa_esercizio_precedente) {
	da_esercizio_precedente = newDa_esercizio_precedente;
}
/* 
 * Setter dell'attributo dt_a
 */
public void setDt_a(java.sql.Timestamp dt_a) {
	this.dt_a = dt_a;
}
/* 
 * Setter dell'attributo dt_da
 */
public void setDt_da(java.sql.Timestamp dt_da) {
	this.dt_da = dt_da;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
