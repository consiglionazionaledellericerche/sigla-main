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

public class Liquid_gruppo_centroKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;
	
	// CD_GRUPPO_CR VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_cr;
	
	// CD_REGIONE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_regione;
	
	// PG_COMUNE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_comune;
	
	// PG_GRUPPO_CENTRO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer pg_gruppo_centro;

public Liquid_gruppo_centroKey() {
	super();
}
public Liquid_gruppo_centroKey(java.lang.String cd_cds,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione) {
	super();
	this.esercizio = esercizio;
	this.cd_gruppo_cr = cd_gruppo_cr;
	this.cd_regione = cd_regione;
	this.pg_comune = pg_comune;
	this.pg_gruppo_centro = pg_gruppo_centro;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Liquid_gruppo_centroKey)) return false;
	Liquid_gruppo_centroKey k = (Liquid_gruppo_centroKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getCd_gruppo_cr(),k.getCd_gruppo_cr())) return false;
	if(!compareKey(getCd_regione(),k.getCd_regione())) return false;
	if(!compareKey(getPg_comune(),k.getPg_comune())) return false;
	if(!compareKey(getPg_gruppo_centro(),k.getPg_gruppo_centro())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_gruppo_cr
 */
public java.lang.String getCd_gruppo_cr() {
	return cd_gruppo_cr;
}
/* 
 * Getter dell'attributo cd_regione
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_comune
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
/* 
 * Getter dell'attributo pg_gruppo_centro
 */
public java.lang.Integer getPg_gruppo_centro() {
	return pg_gruppo_centro;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+	
		calculateKeyHashCode(getCd_gruppo_cr())+
		calculateKeyHashCode(getCd_regione())+
		calculateKeyHashCode(getPg_comune())+
		calculateKeyHashCode(getPg_gruppo_centro());
}
/* 
 * Setter dell'attributo cd_gruppo_cr
 */
public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr) {
	this.cd_gruppo_cr = cd_gruppo_cr;
}
/* 
 * Setter dell'attributo cd_regione
 */
public void setCd_regione(java.lang.String cd_regione) {
	this.cd_regione = cd_regione;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_comune
 */
public void setPg_comune(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
/* 
 * Setter dell'attributo pg_gruppo_centro
 */
public void setPg_gruppo_centro(java.lang.Integer pg_gruppo_centro) {
	this.pg_gruppo_centro = pg_gruppo_centro;
}
}
