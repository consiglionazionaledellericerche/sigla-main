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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Distinta_cassiere_detKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private Integer esercizio;

	// PG_DISTINTA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_distinta;

	// PG_DETTAGLIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_dettaglio;

public Distinta_cassiere_detKey() {
	super();
}
public Distinta_cassiere_detKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,Integer esercizio,java.lang.Long pg_dettaglio,java.lang.Long pg_distinta) {
	this.cd_cds = cd_cds;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.esercizio = esercizio;
	this.pg_dettaglio = pg_dettaglio;
	this.pg_distinta = pg_distinta;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Distinta_cassiere_detKey)) return false;
	Distinta_cassiere_detKey k = (Distinta_cassiere_detKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_dettaglio(),k.getPg_dettaglio())) return false;
	if(!compareKey(getPg_distinta(),k.getPg_distinta())) return false;
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
public Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_dettaglio
 */
public java.lang.Long getPg_dettaglio() {
	return pg_dettaglio;
}
/* 
 * Getter dell'attributo pg_distinta
 */
public java.lang.Long getPg_distinta() {
	return pg_distinta;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_dettaglio())+
		calculateKeyHashCode(getPg_distinta());
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
public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_dettaglio
 */
public void setPg_dettaglio(java.lang.Long pg_dettaglio) {
	this.pg_dettaglio = pg_dettaglio;
}
/* 
 * Setter dell'attributo pg_distinta
 */
public void setPg_distinta(java.lang.Long pg_distinta) {
	this.pg_distinta = pg_distinta;
}
}
