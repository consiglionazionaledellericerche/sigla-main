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

public class Rappresentante_legaleBase extends Rappresentante_legaleKey implements Keyed {
	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50) NOT NULL
	private java.lang.String cognome;

	// DT_CANC TIMESTAMP
	private java.sql.Timestamp dt_canc;

	// DT_NASCITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_nascita;

	// NOME VARCHAR(50) NOT NULL
	private java.lang.String nome;

	// PG_COMUNE_NASCITA DECIMAL(10,0)
	private java.lang.Long pg_comune_nascita;

	// TITOLO VARCHAR(100) NOT NULL
	private java.lang.String titolo;

public Rappresentante_legaleBase() {
	super();
}
public Rappresentante_legaleBase(java.lang.Long pg_rapp_legale) {
	super(pg_rapp_legale);
}
/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/* 
 * Getter dell'attributo cognome
 */
public java.lang.String getCognome() {
	return cognome;
}
/* 
 * Getter dell'attributo dt_canc
 */
public java.sql.Timestamp getDt_canc() {
	return dt_canc;
}
/* 
 * Getter dell'attributo dt_nascita
 */
public java.sql.Timestamp getDt_nascita() {
	return dt_nascita;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo pg_comune_nascita
 */
public java.lang.Long getPg_comune_nascita() {
	return pg_comune_nascita;
}
/* 
 * Getter dell'attributo titolo
 */
public java.lang.String getTitolo() {
	return titolo;
}
/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}
/* 
 * Setter dell'attributo cognome
 */
public void setCognome(java.lang.String cognome) {
	this.cognome = cognome;
}
/* 
 * Setter dell'attributo dt_canc
 */
public void setDt_canc(java.sql.Timestamp dt_canc) {
	this.dt_canc = dt_canc;
}
/* 
 * Setter dell'attributo dt_nascita
 */
public void setDt_nascita(java.sql.Timestamp dt_nascita) {
	this.dt_nascita = dt_nascita;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo pg_comune_nascita
 */
public void setPg_comune_nascita(java.lang.Long pg_comune_nascita) {
	this.pg_comune_nascita = pg_comune_nascita;
}
/* 
 * Setter dell'attributo titolo
 */
public void setTitolo(java.lang.String titolo) {
	this.titolo = titolo;
}
}
