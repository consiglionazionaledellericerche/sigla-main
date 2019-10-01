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

public class Ass_mandato_reversaleKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO_MANDATO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_mandato;

	// CD_CDS_REVERSALE VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_reversale;

	// PG_REVERSALE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_reversale;

	// PG_MANDATO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_mandato;

	// CD_CDS_MANDATO VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_mandato;

	// ESERCIZIO_REVERSALE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_reversale;

public Ass_mandato_reversaleKey() {
	super();
}
public Ass_mandato_reversaleKey(java.lang.String cd_cds_mandato,java.lang.String cd_cds_reversale,java.lang.Integer esercizio_mandato,java.lang.Integer esercizio_reversale,java.lang.Long pg_mandato,java.lang.Long pg_reversale) {
	super();
	this.cd_cds_mandato = cd_cds_mandato;
	this.cd_cds_reversale = cd_cds_reversale;
	this.esercizio_mandato = esercizio_mandato;
	this.esercizio_reversale = esercizio_reversale;
	this.pg_mandato = pg_mandato;
	this.pg_reversale = pg_reversale;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_mandato_reversaleKey)) return false;
	Ass_mandato_reversaleKey k = (Ass_mandato_reversaleKey)o;
	if(!compareKey(getCd_cds_mandato(),k.getCd_cds_mandato())) return false;
	if(!compareKey(getCd_cds_reversale(),k.getCd_cds_reversale())) return false;
	if(!compareKey(getEsercizio_mandato(),k.getEsercizio_mandato())) return false;
	if(!compareKey(getEsercizio_reversale(),k.getEsercizio_reversale())) return false;
	if(!compareKey(getPg_mandato(),k.getPg_mandato())) return false;
	if(!compareKey(getPg_reversale(),k.getPg_reversale())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds_mandato
 */
public java.lang.String getCd_cds_mandato() {
	return cd_cds_mandato;
}
/* 
 * Getter dell'attributo cd_cds_reversale
 */
public java.lang.String getCd_cds_reversale() {
	return cd_cds_reversale;
}
/* 
 * Getter dell'attributo esercizio_mandato
 */
public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}
/* 
 * Getter dell'attributo esercizio_reversale
 */
public java.lang.Integer getEsercizio_reversale() {
	return esercizio_reversale;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo pg_reversale
 */
public java.lang.Long getPg_reversale() {
	return pg_reversale;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds_mandato())+
		calculateKeyHashCode(getCd_cds_reversale())+
		calculateKeyHashCode(getEsercizio_mandato())+
		calculateKeyHashCode(getEsercizio_reversale())+
		calculateKeyHashCode(getPg_mandato())+
		calculateKeyHashCode(getPg_reversale());
}
/* 
 * Setter dell'attributo cd_cds_mandato
 */
public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.cd_cds_mandato = cd_cds_mandato;
}
/* 
 * Setter dell'attributo cd_cds_reversale
 */
public void setCd_cds_reversale(java.lang.String cd_cds_reversale) {
	this.cd_cds_reversale = cd_cds_reversale;
}
/* 
 * Setter dell'attributo esercizio_mandato
 */
public void setEsercizio_mandato(java.lang.Integer esercizio_mandato) {
	this.esercizio_mandato = esercizio_mandato;
}
/* 
 * Setter dell'attributo esercizio_reversale
 */
public void setEsercizio_reversale(java.lang.Integer esercizio_reversale) {
	this.esercizio_reversale = esercizio_reversale;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo pg_reversale
 */
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.pg_reversale = pg_reversale;
}
}
