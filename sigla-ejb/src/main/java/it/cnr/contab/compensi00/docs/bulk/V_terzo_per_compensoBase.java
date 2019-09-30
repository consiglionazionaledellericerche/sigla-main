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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_terzo_per_compensoBase extends V_terzo_per_compensoKey implements Persistent {
	// CD_ANAG DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_anag;

	// CD_PROVINCIA_FISCALE VARCHAR(10) NOT NULL
	private java.lang.String cd_provincia_fiscale;

	// CD_REGIONE_FISCALE VARCHAR(10) NOT NULL
	private java.lang.String cd_regione_fiscale;

	// CD_TERZO_PRECEDENTE VARCHAR(20)
	private java.lang.String cd_terzo_precedente;

	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DS_COMUNE_FISCALE VARCHAR(100) NOT NULL
	private java.lang.String ds_comune_fiscale;

	// DS_PROVINCIA_FISCALE VARCHAR(100) NOT NULL
	private java.lang.String ds_provincia_fiscale;

	// DS_REGIONE_FISCALE VARCHAR(100) NOT NULL
	private java.lang.String ds_regione_fiscale;

	// DS_TIPO_RAPPORTO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_rapporto;

	// DT_FINE_VALIDITA_TERZO TIMESTAMP
	private java.sql.Timestamp dt_fine_validita_terzo;

	// DT_FIN_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fin_validita;

	// DT_INI_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_validita;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_COMUNE_FISCALE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_comune_fiscale;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;


public V_terzo_per_compensoBase() {
	super();
}
public V_terzo_per_compensoBase(java.lang.Integer cd_terzo, String ti_dipendente_altro) {
	super(cd_terzo, ti_dipendente_altro);
}
/* 
 * Getter dell'attributo cd_anag
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
/* 
 * Getter dell'attributo cd_provincia_fiscale
 */
public java.lang.String getCd_provincia_fiscale() {
	return cd_provincia_fiscale;
}
/* 
 * Getter dell'attributo cd_regione_fiscale
 */
public java.lang.String getCd_regione_fiscale() {
	return cd_regione_fiscale;
}
/**
 * Insert the method's description here.
 * Creation date: (3/10/2003 11:28:12 AM)
 * @return java.lang.String
 */
public java.lang.String getCd_terzo_precedente() {
	return cd_terzo_precedente;
}
/* 
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
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
 * Getter dell'attributo ds_comune_fiscale
 */
public java.lang.String getDs_comune_fiscale() {
	return ds_comune_fiscale;
}
/* 
 * Getter dell'attributo ds_provincia_fiscale
 */
public java.lang.String getDs_provincia_fiscale() {
	return ds_provincia_fiscale;
}
/* 
 * Getter dell'attributo ds_regione_fiscale
 */
public java.lang.String getDs_regione_fiscale() {
	return ds_regione_fiscale;
}
/* 
 * Getter dell'attributo ds_tipo_rapporto
 */
public java.lang.String getDs_tipo_rapporto() {
	return ds_tipo_rapporto;
}
/* 
 * Getter dell'attributo dt_fin_validita
 */
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
/* 
 * Getter dell'attributo dt_fine_validita_terzo
 */
public java.sql.Timestamp getDt_fine_validita_terzo() {
	return dt_fine_validita_terzo;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo pg_comune_fiscale
 */
public java.lang.Long getPg_comune_fiscale() {
	return pg_comune_fiscale;
}
/* 
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}
/* 
 * Setter dell'attributo cd_anag
 */
public void setCd_anag(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
/* 
 * Setter dell'attributo cd_provincia_fiscale
 */
public void setCd_provincia_fiscale(java.lang.String cd_provincia_fiscale) {
	this.cd_provincia_fiscale = cd_provincia_fiscale;
}
/* 
 * Setter dell'attributo cd_regione_fiscale
 */
public void setCd_regione_fiscale(java.lang.String cd_regione_fiscale) {
	this.cd_regione_fiscale = cd_regione_fiscale;
}
/**
 * Insert the method's description here.
 * Creation date: (3/10/2003 11:28:12 AM)
 * @param newCd_terzo_precedente java.lang.String
 */
public void setCd_terzo_precedente(java.lang.String newCd_terzo_precedente) {
	cd_terzo_precedente = newCd_terzo_precedente;
}
/* 
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
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
 * Setter dell'attributo ds_comune_fiscale
 */
public void setDs_comune_fiscale(java.lang.String ds_comune_fiscale) {
	this.ds_comune_fiscale = ds_comune_fiscale;
}
/* 
 * Setter dell'attributo ds_provincia_fiscale
 */
public void setDs_provincia_fiscale(java.lang.String ds_provincia_fiscale) {
	this.ds_provincia_fiscale = ds_provincia_fiscale;
}
/* 
 * Setter dell'attributo ds_regione_fiscale
 */
public void setDs_regione_fiscale(java.lang.String ds_regione_fiscale) {
	this.ds_regione_fiscale = ds_regione_fiscale;
}
/* 
 * Setter dell'attributo ds_tipo_rapporto
 */
public void setDs_tipo_rapporto(java.lang.String ds_tipo_rapporto) {
	this.ds_tipo_rapporto = ds_tipo_rapporto;
}
/* 
 * Setter dell'attributo dt_fin_validita
 */
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
/* 
 * Setter dell'attributo dt_fine_validita_terzo
 */
public void setDt_fine_validita_terzo(java.sql.Timestamp dt_fine_validita_terzo) {
	this.dt_fine_validita_terzo = dt_fine_validita_terzo;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo pg_comune_fiscale
 */
public void setPg_comune_fiscale(java.lang.Long pg_comune_fiscale) {
	this.pg_comune_fiscale = pg_comune_fiscale;
}
/* 
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}
}
