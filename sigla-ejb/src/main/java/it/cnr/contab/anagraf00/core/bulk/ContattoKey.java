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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ContattoKey extends OggettoBulk implements KeyedPersistent {
	// PG_CONTATTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_contatto;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

public ContattoKey() {
	super();
}
public ContattoKey(java.lang.Integer cd_terzo,java.lang.Long pg_contatto) {
	super();
	this.cd_terzo = cd_terzo;
	this.pg_contatto = pg_contatto;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ContattoKey)) return false;
	ContattoKey k = (ContattoKey)o;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getPg_contatto(),k.getPg_contatto())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo pg_contatto
 */
public java.lang.Long getPg_contatto() {
	return pg_contatto;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getPg_contatto());
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo pg_contatto
 */
public void setPg_contatto(java.lang.Long pg_contatto) {
	this.pg_contatto = pg_contatto;
}
}
