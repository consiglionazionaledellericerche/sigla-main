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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Movimento_cogeKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_MOVIMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_movimento;

	// PG_SCRITTURA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_scrittura;

public Movimento_cogeKey() {
	super();
}
public Movimento_cogeKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_movimento,java.lang.Long pg_scrittura) {
	super();
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.pg_movimento = pg_movimento;
	this.pg_scrittura = pg_scrittura;
}
public Movimento_cogeKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_movimento,java.lang.Long pg_scrittura) {
	super();
	this.cd_cds = cd_cds;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.esercizio = esercizio;
	this.pg_movimento = pg_movimento;
	this.pg_scrittura = pg_scrittura;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Movimento_cogeKey)) return false;
	Movimento_cogeKey k = (Movimento_cogeKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_movimento(),k.getPg_movimento())) return false;
	if(!compareKey(getPg_scrittura(),k.getPg_scrittura())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_movimento
 */
public java.lang.Long getPg_movimento() {
	return pg_movimento;
}
/* 
 * Getter dell'attributo pg_scrittura
 */
public java.lang.Long getPg_scrittura() {
	return pg_scrittura;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_movimento())+
		calculateKeyHashCode(getPg_scrittura());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_movimento
 */
public void setPg_movimento(java.lang.Long pg_movimento) {
	this.pg_movimento = pg_movimento;
}
/* 
 * Setter dell'attributo pg_scrittura
 */
public void setPg_scrittura(java.lang.Long pg_scrittura) {
	this.pg_scrittura = pg_scrittura;
}
}
