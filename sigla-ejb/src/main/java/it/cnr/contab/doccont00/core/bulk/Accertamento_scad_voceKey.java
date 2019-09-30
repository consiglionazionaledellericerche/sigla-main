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

public class Accertamento_scad_voceKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_originale;

	// PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento;

	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_linea_attivita;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento_scadenzario;

public Accertamento_scad_voceKey() {
	super();
}
public Accertamento_scad_voceKey(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario) {
	super();
	this.cd_cds = cd_cds;
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_linea_attivita = cd_linea_attivita;
	this.esercizio = esercizio;
	this.esercizio_originale = esercizio_originale;
	this.pg_accertamento = pg_accertamento;
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Accertamento_scad_voceKey)) return false;
	Accertamento_scad_voceKey k = (Accertamento_scad_voceKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_originale(),k.getEsercizio_originale())) return false;
	if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
	if(!compareKey(getPg_accertamento_scadenzario(),k.getPg_accertamento_scadenzario())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_linea_attivita
 */
public java.lang.String getCd_linea_attivita() {
	return cd_linea_attivita;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_originale
 */
public java.lang.Integer getEsercizio_originale() {
	return esercizio_originale;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento_scadenzario
 */
public java.lang.Long getPg_accertamento_scadenzario() {
	return pg_accertamento_scadenzario;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_linea_attivita())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_originale())+
		calculateKeyHashCode(getPg_accertamento())+
		calculateKeyHashCode(getPg_accertamento_scadenzario());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_linea_attivita
 */
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.cd_linea_attivita = cd_linea_attivita;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo esercizio_originale
 */
public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
	this.esercizio_originale = esercizio_originale;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento_scadenzario
 */
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) {
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
}
}
