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

package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ordine_dettKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_ORDINE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_ordine;

	// PG_DETTAGLIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_dettaglio;

public Ordine_dettKey() {
	super();
}
public Ordine_dettKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_dettaglio,java.lang.Long pg_ordine) {
	super();
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.pg_dettaglio = pg_dettaglio;
	this.pg_ordine = pg_ordine;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ordine_dettKey)) return false;
	Ordine_dettKey k = (Ordine_dettKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_dettaglio(),k.getPg_dettaglio())) return false;
	if(!compareKey(getPg_ordine(),k.getPg_ordine())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_dettaglio
 */
public java.lang.Long getPg_dettaglio() {
	return pg_dettaglio;
}
/* 
 * Getter dell'attributo pg_ordine
 */
public java.lang.Long getPg_ordine() {
	return pg_ordine;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_dettaglio())+
		calculateKeyHashCode(getPg_ordine());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_dettaglio
 */
public void setPg_dettaglio(java.lang.Long pg_dettaglio) {
	this.pg_dettaglio = pg_dettaglio;
}
/* 
 * Setter dell'attributo pg_ordine
 */
public void setPg_ordine(java.lang.Long pg_ordine) {
	this.pg_ordine = pg_ordine;
}
}
