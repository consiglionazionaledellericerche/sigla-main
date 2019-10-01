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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tappaBase extends Missione_tappaKey implements Keyed {
	// CAMBIO_TAPPA DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio_tappa;

	// CD_DIVISA_TAPPA VARCHAR(10) NOT NULL
	private java.lang.String cd_divisa_tappa;

	// DT_FINE_TAPPA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_tappa;

	// DT_INGRESSO_ESTERO TIMESTAMP
	private java.sql.Timestamp dt_ingresso_estero;

	// DT_USCITA_ESTERO TIMESTAMP
	private java.sql.Timestamp dt_uscita_estero;

	// FL_ALLOGGIO_GRATUITO CHAR(1) NOT NULL
	private java.lang.Boolean fl_alloggio_gratuito;

	// FL_COMUNE_ALTRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_comune_altro;

	// FL_COMUNE_ESTERO CHAR(1) NOT NULL
	private java.lang.Boolean fl_comune_estero;

	// FL_COMUNE_PROPRIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_comune_proprio;

	// FL_NAVIGAZIONE CHAR(1) NOT NULL
	private java.lang.Boolean fl_navigazione;

	// FL_NO_DIARIA CHAR(1) NOT NULL
	private java.lang.Boolean fl_no_diaria;

	// FL_VITTO_ALLOGGIO_GRATUITO CHAR(1) NOT NULL
	private java.lang.Boolean fl_vitto_alloggio_gratuito;

	// FL_VITTO_GRATUITO CHAR(1) NOT NULL
	private java.lang.Boolean fl_vitto_gratuito;

	// PG_NAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_nazione;
	
	// FL_RIMBORSO CHAR(1) NOT NULL
	private java.lang.Boolean fl_rimborso;

public Missione_tappaBase() {
	super();
}
public Missione_tappaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_inizio_tappa,java.lang.Integer esercizio,java.lang.Long pg_missione) {
	super(cd_cds,cd_unita_organizzativa,dt_inizio_tappa,esercizio,pg_missione);
}
/* 
 * Getter dell'attributo cambio_tappa
 */
public java.math.BigDecimal getCambio_tappa() {
	return cambio_tappa;
}
/* 
 * Getter dell'attributo cd_divisa_tappa
 */
public java.lang.String getCd_divisa_tappa() {
	return cd_divisa_tappa;
}
/* 
 * Getter dell'attributo dt_fine_tappa
 */
public java.sql.Timestamp getDt_fine_tappa() {
	return dt_fine_tappa;
}
/* 
 * Getter dell'attributo dt_ingresso_estero
 */
public java.sql.Timestamp getDt_ingresso_estero() {
	return dt_ingresso_estero;
}
/* 
 * Getter dell'attributo dt_uscita_estero
 */
public java.sql.Timestamp getDt_uscita_estero() {
	return dt_uscita_estero;
}
/* 
 * Getter dell'attributo fl_alloggio_gratuito
 */
public java.lang.Boolean getFl_alloggio_gratuito() {
	return fl_alloggio_gratuito;
}
/* 
 * Getter dell'attributo fl_comune_altro
 */
public java.lang.Boolean getFl_comune_altro() {
	return fl_comune_altro;
}
/* 
 * Getter dell'attributo fl_comune_estero
 */
public java.lang.Boolean getFl_comune_estero() {
	return fl_comune_estero;
}
/* 
 * Getter dell'attributo fl_comune_proprio
 */
public java.lang.Boolean getFl_comune_proprio() {
	return fl_comune_proprio;
}
/* 
 * Getter dell'attributo fl_navigazione
 */
public java.lang.Boolean getFl_navigazione() {
	return fl_navigazione;
}
/* 
 * Getter dell'attributo fl_no_diaria
 */
public java.lang.Boolean getFl_no_diaria() {
	return fl_no_diaria;
}
/* 
 * Getter dell'attributo fl_vitto_alloggio_gratuito
 */
public java.lang.Boolean getFl_vitto_alloggio_gratuito() {
	return fl_vitto_alloggio_gratuito;
}
/* 
 * Getter dell'attributo fl_vitto_gratuito
 */
public java.lang.Boolean getFl_vitto_gratuito() {
	return fl_vitto_gratuito;
}
/* 
 * Getter dell'attributo pg_nazione
 */
public java.lang.Long getPg_nazione() {
	return pg_nazione;
}
/* 
 * Setter dell'attributo cambio_tappa
 */
public void setCambio_tappa(java.math.BigDecimal cambio_tappa) {
	this.cambio_tappa = cambio_tappa;
}
/* 
 * Setter dell'attributo cd_divisa_tappa
 */
public void setCd_divisa_tappa(java.lang.String cd_divisa_tappa) {
	this.cd_divisa_tappa = cd_divisa_tappa;
}
/* 
 * Setter dell'attributo dt_fine_tappa
 */
public void setDt_fine_tappa(java.sql.Timestamp dt_fine_tappa) {
	this.dt_fine_tappa = dt_fine_tappa;
}
/* 
 * Setter dell'attributo dt_ingresso_estero
 */
public void setDt_ingresso_estero(java.sql.Timestamp dt_ingresso_estero) {
	this.dt_ingresso_estero = dt_ingresso_estero;
}
/* 
 * Setter dell'attributo dt_uscita_estero
 */
public void setDt_uscita_estero(java.sql.Timestamp dt_uscita_estero) {
	this.dt_uscita_estero = dt_uscita_estero;
}
/* 
 * Setter dell'attributo fl_alloggio_gratuito
 */
public void setFl_alloggio_gratuito(java.lang.Boolean fl_alloggio_gratuito) {
	this.fl_alloggio_gratuito = fl_alloggio_gratuito;
}
/* 
 * Setter dell'attributo fl_comune_altro
 */
public void setFl_comune_altro(java.lang.Boolean fl_comune_altro) {
	this.fl_comune_altro = fl_comune_altro;
}
/* 
 * Setter dell'attributo fl_comune_estero
 */
public void setFl_comune_estero(java.lang.Boolean fl_comune_estero) {
	this.fl_comune_estero = fl_comune_estero;
}
/* 
 * Setter dell'attributo fl_comune_proprio
 */
public void setFl_comune_proprio(java.lang.Boolean fl_comune_proprio) {
	this.fl_comune_proprio = fl_comune_proprio;
}
/* 
 * Setter dell'attributo fl_navigazione
 */
public void setFl_navigazione(java.lang.Boolean fl_navigazione) {
	this.fl_navigazione = fl_navigazione;
}
/* 
 * Setter dell'attributo fl_no_diaria
 */
public void setFl_no_diaria(java.lang.Boolean fl_no_diaria) {
	this.fl_no_diaria = fl_no_diaria;
}
/* 
 * Setter dell'attributo fl_vitto_alloggio_gratuito
 */
public void setFl_vitto_alloggio_gratuito(java.lang.Boolean fl_vitto_alloggio_gratuito) {
	this.fl_vitto_alloggio_gratuito = fl_vitto_alloggio_gratuito;
}
/* 
 * Setter dell'attributo fl_vitto_gratuito
 */
public void setFl_vitto_gratuito(java.lang.Boolean fl_vitto_gratuito) {
	this.fl_vitto_gratuito = fl_vitto_gratuito;
}
/* 
 * Setter dell'attributo pg_nazione
 */
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.pg_nazione = pg_nazione;
}
public java.lang.Boolean getFl_rimborso() {
	return fl_rimborso;
}
public void setFl_rimborso(java.lang.Boolean fl_rimborso) {
	this.fl_rimborso = fl_rimborso;
}
}
