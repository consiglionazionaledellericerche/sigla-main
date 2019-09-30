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

public class Mandato_terzoKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_MANDATO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_mandato;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

public Mandato_terzoKey() {
	super();
}
public Mandato_terzoKey(java.lang.String cd_cds,java.lang.Integer cd_terzo,java.lang.Integer esercizio,java.lang.Long pg_mandato) {
	super();
	this.cd_cds = cd_cds;
	this.cd_terzo = cd_terzo;
	this.esercizio = esercizio;
	this.pg_mandato = pg_mandato;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Mandato_terzoKey)) return false;
	Mandato_terzoKey k = (Mandato_terzoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_mandato(),k.getPg_mandato())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Mandato_terzoKey)) return false;
	Mandato_terzoKey k = (Mandato_terzoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_mandato(),k.getPg_mandato())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_mandato());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_mandato());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
}
