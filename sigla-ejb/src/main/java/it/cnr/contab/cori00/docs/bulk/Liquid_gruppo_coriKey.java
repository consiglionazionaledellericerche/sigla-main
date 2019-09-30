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

public class Liquid_gruppo_coriKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_uo_origine;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// PG_COMUNE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_comune;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_origine;

	// PG_LIQUIDAZIONE DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer pg_liquidazione;

	// PG_LIQUIDAZIONE_ORIGINE DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer pg_liquidazione_origine;

	// CD_REGIONE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_regione;

	// CD_GRUPPO_CR VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_cr;

public Liquid_gruppo_coriKey() {
	super();
}
public Liquid_gruppo_coriKey(java.lang.String cd_cds,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione) {
	super();
	this.cd_cds = cd_cds;
	this.cd_gruppo_cr = cd_gruppo_cr;
	this.cd_regione = cd_regione;
	this.esercizio = esercizio;
	this.pg_comune = pg_comune;
	this.pg_liquidazione = pg_liquidazione;
}
public Liquid_gruppo_coriKey(java.lang.String cd_cds,java.lang.String cd_cds_origine,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.String cd_unita_organizzativa,java.lang.String cd_uo_origine,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione,java.lang.Integer pg_liquidazione_origine) {
	super();
	this.cd_cds = cd_cds;
	this.cd_cds_origine = cd_cds_origine;
	this.cd_gruppo_cr = cd_gruppo_cr;
	this.cd_regione = cd_regione;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.cd_uo_origine = cd_uo_origine;
	this.esercizio = esercizio;
	this.pg_comune = pg_comune;
	this.pg_liquidazione = pg_liquidazione;
	this.pg_liquidazione_origine = pg_liquidazione_origine;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Liquid_gruppo_coriKey)) return false;
	Liquid_gruppo_coriKey k = (Liquid_gruppo_coriKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_cds_origine(),k.getCd_cds_origine())) return false;
	if(!compareKey(getCd_gruppo_cr(),k.getCd_gruppo_cr())) return false;
	if(!compareKey(getCd_regione(),k.getCd_regione())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getCd_uo_origine(),k.getCd_uo_origine())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_comune(),k.getPg_comune())) return false;
	if(!compareKey(getPg_liquidazione(),k.getPg_liquidazione())) return false;
	if(!compareKey(getPg_liquidazione_origine(),k.getPg_liquidazione_origine())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
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
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
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
 * Getter dell'attributo pg_liquidazione
 */
public java.lang.Integer getPg_liquidazione() {
	return pg_liquidazione;
}
/* 
 * Getter dell'attributo pg_liquidazione_origine
 */
public java.lang.Integer getPg_liquidazione_origine() {
	return pg_liquidazione_origine;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_cds_origine())+
		calculateKeyHashCode(getCd_gruppo_cr())+
		calculateKeyHashCode(getCd_regione())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getCd_uo_origine())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_comune())+
		calculateKeyHashCode(getPg_liquidazione())+
		calculateKeyHashCode(getPg_liquidazione_origine());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
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
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
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
 * Setter dell'attributo pg_liquidazione
 */
public void setPg_liquidazione(java.lang.Integer pg_liquidazione) {
	this.pg_liquidazione = pg_liquidazione;
}
/* 
 * Setter dell'attributo pg_liquidazione_origine
 */
public void setPg_liquidazione_origine(java.lang.Integer pg_liquidazione_origine) {
	this.pg_liquidazione_origine = pg_liquidazione_origine;
}
}
