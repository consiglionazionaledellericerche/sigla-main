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

package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_fondo_eco_mandatoKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_CODICE_FONDO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_codice_fondo;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_CDS_MANDATO VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_mandato;

	// ESERCIZIO_MANDATO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_mandato;

	// PG_MANDATO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_mandato;
public Ass_fondo_eco_mandatoKey() {
	super();
}
public Ass_fondo_eco_mandatoKey(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	this.cd_cds = cd_cds;
	this.cd_codice_fondo = cd_codice_fondo;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.esercizio = esercizio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_fondo_eco_mandatoKey)) return false;
	Ass_fondo_eco_mandatoKey k = (Ass_fondo_eco_mandatoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_codice_fondo(),k.getCd_codice_fondo())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_mandato
 */
public java.lang.String getCd_cds_mandato() {
	return cd_cds_mandato;
}
/* 
 * Getter dell'attributo cd_codice_fondo
 */
public java.lang.String getCd_codice_fondo() {
	return cd_codice_fondo;
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
 * Getter dell'attributo esercizio_mandato
 */
public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_codice_fondo())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getEsercizio());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_mandato
 */
public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.cd_cds_mandato = cd_cds_mandato;
}
/* 
 * Setter dell'attributo cd_codice_fondo
 */
public void setCd_codice_fondo(java.lang.String cd_codice_fondo) {
	this.cd_codice_fondo = cd_codice_fondo;
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
 * Setter dell'attributo esercizio_mandato
 */
public void setEsercizio_mandato(java.lang.Integer esercizio_mandato) {
	this.esercizio_mandato = esercizio_mandato;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
}
