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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CdrBase extends CdrKey implements Keyed {
	// CD_CDR_AFFERENZA VARCHAR(30)
	private java.lang.String cd_cdr_afferenza;

	// CD_PROPRIO_CDR VARCHAR(30) NOT NULL
	private java.lang.String cd_proprio_cdr;

	// CD_RESPONSABILE DECIMAL(8,0)
	private java.lang.Integer cd_responsabile;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;

	// ESERCIZIO_FINE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_fine;

	// ESERCIZIO_INIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_inizio;

	// INDIRIZZO VARCHAR(200)
	private java.lang.String indirizzo;

	// LIVELLO DECIMAL(2,0) NOT NULL
	private java.lang.Integer livello;

public CdrBase() {
	super();
}
public CdrBase(java.lang.String cd_centro_responsabilita) {
	super(cd_centro_responsabilita);
}
/* 
 * Getter dell'attributo cd_cdr_afferenza
 */
public java.lang.String getCd_cdr_afferenza() {
	return cd_cdr_afferenza;
}
/* 
 * Getter dell'attributo cd_proprio_cdr
 */
public java.lang.String getCd_proprio_cdr() {
	return cd_proprio_cdr;
}
/* 
 * Getter dell'attributo cd_responsabile
 */
public java.lang.Integer getCd_responsabile() {
	return cd_responsabile;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo ds_cdr
 */
public java.lang.String getDs_cdr() {
	return ds_cdr;
}
/* 
 * Getter dell'attributo esercizio_fine
 */
public java.lang.Integer getEsercizio_fine() {
	return esercizio_fine;
}
/* 
 * Getter dell'attributo esercizio_inizio
 */
public java.lang.Integer getEsercizio_inizio() {
	return esercizio_inizio;
}
/* 
 * Getter dell'attributo indirizzo
 */
public java.lang.String getIndirizzo() {
	return indirizzo;
}
/* 
 * Getter dell'attributo livello
 */
public java.lang.Integer getLivello() {
	return livello;
}
/* 
 * Setter dell'attributo cd_cdr_afferenza
 */
public void setCd_cdr_afferenza(java.lang.String cd_cdr_afferenza) {
	this.cd_cdr_afferenza = cd_cdr_afferenza;
}
/* 
 * Setter dell'attributo cd_proprio_cdr
 */
public void setCd_proprio_cdr(java.lang.String cd_proprio_cdr) {
	this.cd_proprio_cdr = cd_proprio_cdr;
}
/* 
 * Setter dell'attributo cd_responsabile
 */
public void setCd_responsabile(java.lang.Integer cd_responsabile) {
	this.cd_responsabile = cd_responsabile;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo ds_cdr
 */
public void setDs_cdr(java.lang.String ds_cdr) {
	this.ds_cdr = ds_cdr;
}
/* 
 * Setter dell'attributo esercizio_fine
 */
public void setEsercizio_fine(java.lang.Integer esercizio_fine) {
	this.esercizio_fine = esercizio_fine;
}
/* 
 * Setter dell'attributo esercizio_inizio
 */
public void setEsercizio_inizio(java.lang.Integer esercizio_inizio) {
	this.esercizio_inizio = esercizio_inizio;
}
/* 
 * Setter dell'attributo indirizzo
 */
public void setIndirizzo(java.lang.String indirizzo) {
	this.indirizzo = indirizzo;
}
/* 
 * Setter dell'attributo livello
 */
public void setLivello(java.lang.Integer livello) {
	this.livello = livello;
}
}
