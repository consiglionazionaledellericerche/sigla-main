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

package it.cnr.contab.preventvar00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Var_bilancioBase extends Var_bilancioKey implements Keyed {
	// CD_CAUSALE_VAR_BILANCIO VARCHAR(10)
	private java.lang.String cd_causale_var_bilancio;

	// DS_DELIBERA VARCHAR(300)
	private java.lang.String ds_delibera;

	// DS_VARIAZIONE VARCHAR(300)
	private java.lang.String ds_variazione;

	// ESERCIZIO_IMPORTI DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_importi;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// TI_VARIAZIONE VARCHAR(10) NOT NULL
	private java.lang.String ti_variazione;

	// ESERCIZIO_PDG_VARIAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_pdg_variazione;

	// PDG_VARIAZIONE DECIMAL(10,0)
	private java.lang.Long pg_variazione_pdg;

	// ESERCIZIO_VAR_STANZ_RES DECIMAL(4,0)
	private java.lang.Integer esercizio_var_stanz_res;

	// PG_VAR_STANZ_RES DECIMAL(10,0)
	private java.lang.Long pg_var_stanz_res;
	
	// CD_CDS_MANDATO VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_mandato;

	// ESERCIZIO_MANDATO DECIMAL(4,0)
	private java.lang.Integer esercizio_mandato;

	// PG_MANDATO DECIMAL(10,0)
	private java.lang.Long pg_mandato;

public Var_bilancioBase() {
	super();
}
public Var_bilancioBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String ti_appartenenza) {
	super(cd_cds,esercizio,pg_variazione,ti_appartenenza);
}
/* 
 * Getter dell'attributo cd_causale_var_bilancio
 */
public java.lang.String getCd_causale_var_bilancio() {
	return cd_causale_var_bilancio;
}
/* 
 * Getter dell'attributo ds_delibera
 */
public java.lang.String getDs_delibera() {
	return ds_delibera;
}
/* 
 * Getter dell'attributo ds_variazione
 */
public java.lang.String getDs_variazione() {
	return ds_variazione;
}
/* 
 * Getter dell'attributo esercizio_importi
 */
public java.lang.Integer getEsercizio_importi() {
	return esercizio_importi;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Getter dell'attributo ti_variazione
 */
public java.lang.String getTi_variazione() {
	return ti_variazione;
}
/* 
 * Getter dell'attributo esercizio_pdg_variazione
 */
public java.lang.Integer getEsercizio_pdg_variazione() {
	return esercizio_pdg_variazione;
}
/* 
 * Getter dell'attributo pdg_variazione
 */
public java.lang.Long getPg_variazione_pdg() {
	return pg_variazione_pdg;
}
/* 
 * Setter dell'attributo cd_causale_var_bilancio
 */
public void setCd_causale_var_bilancio(java.lang.String cd_causale_var_bilancio) {
	this.cd_causale_var_bilancio = cd_causale_var_bilancio;
}
/* 
 * Setter dell'attributo ds_delibera
 */
public void setDs_delibera(java.lang.String ds_delibera) {
	this.ds_delibera = ds_delibera;
}
/* 
 * Setter dell'attributo ds_variazione
 */
public void setDs_variazione(java.lang.String ds_variazione) {
	this.ds_variazione = ds_variazione;
}
/* 
 * Setter dell'attributo esercizio_importi
 */
public void setEsercizio_importi(java.lang.Integer esercizio_importi) {
	this.esercizio_importi = esercizio_importi;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
/* 
 * Setter dell'attributo ti_variazione
 */
public void setTi_variazione(java.lang.String ti_variazione) {
	this.ti_variazione = ti_variazione;
}
/*
 * Setter dell'attributo esercizio_pdg_variazione
 */
public void setEsercizio_pdg_variazione(java.lang.Integer esercizio_pdg_variazione) {
	this.esercizio_pdg_variazione = esercizio_pdg_variazione;
}
/*
 * Setter dell'attributo pg_variazione_pdg
 */
public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg) {
	this.pg_variazione_pdg = pg_variazione_pdg;
}
public java.lang.Integer getEsercizio_var_stanz_res() {
	return esercizio_var_stanz_res;
}

public void setEsercizio_var_stanz_res(java.lang.Integer esercizio_var_stanz_res) {
	this.esercizio_var_stanz_res = esercizio_var_stanz_res;
}

public java.lang.Long getPg_var_stanz_res() {
	return pg_var_stanz_res;
}

public void setPg_var_stanz_res(java.lang.Long pg_var_stanz_res) {
	this.pg_var_stanz_res = pg_var_stanz_res;
}
public java.lang.String getCd_cds_mandato() {
	return cd_cds_mandato;
}

public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.cd_cds_mandato = cd_cds_mandato;
}

public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}

public void setEsercizio_mandato(java.lang.Integer esercizio_mandato) {
	this.esercizio_mandato = esercizio_mandato;
}

public java.lang.Long getPg_mandato() {
	return pg_mandato;
}

public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
}
