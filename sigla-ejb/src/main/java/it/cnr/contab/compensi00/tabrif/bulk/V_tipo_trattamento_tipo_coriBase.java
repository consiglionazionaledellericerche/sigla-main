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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_tipo_trattamento_tipo_coriBase extends OggettoBulk implements Persistent {
	// CALCOLO_IMPONIBILE VARCHAR(20) NOT NULL
	private java.lang.String calcolo_imponibile;

	// CD_CLASSIFICAZIONE_CORI CHAR(2) NOT NULL
	private java.lang.String cd_classificazione_cori;

	// CD_CORI VARCHAR(10) NOT NULL
	private java.lang.String cd_cori;

	// CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private java.lang.String cd_trattamento;

	// DT_FIN_VAL_TIPO_CORI TIMESTAMP
	private java.sql.Timestamp dt_fin_val_tipo_cori;

	// DT_FIN_VAL_TRATTAMENTO TIMESTAMP
	private java.sql.Timestamp dt_fin_val_trattamento;

	// DT_FIN_VAL_TRATT_CORI TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fin_val_tratt_cori;

	// DT_INI_VAL_TIPO_CORI TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_val_tipo_cori;

	// DT_INI_VAL_TRATTAMENTO TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_val_trattamento;

	// DT_INI_VAL_TRATT_CORI TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_val_tratt_cori;

	// FL_DETRAZIONI_DIPENDENTE CHAR(1) NOT NULL
	private java.lang.Boolean fl_detrazioni_dipendente;

	// FL_DETRAZIONI_FAMILIARI CHAR(1) NOT NULL
	private java.lang.Boolean fl_detrazioni_familiari;

	// FL_IRPEF_ANNUALIZZATA CHAR(1) NOT NULL
	private java.lang.Boolean fl_irpef_annualizzata;

	// FL_SCRIVI_MONTANTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_scrivi_montanti;

	// FL_TASSAZIONE_SEPARATA CHAR(1) NOT NULL
	private java.lang.Boolean fl_tassazione_separata;

	// FL_USO_IN_LORDIZZA CHAR(1) NOT NULL
	private java.lang.Boolean fl_uso_in_lordizza;

	// ID_RIGA CHAR(3) NOT NULL
	private java.lang.String id_riga;

	// PG_CLASSIFICAZIONE_MONTANTI DECIMAL(10,0)
	private java.lang.Long pg_classificazione_montanti;

	// PRECISIONE DECIMAL(9,6)
	private java.math.BigDecimal precisione;

	// SEGNO CHAR(1) NOT NULL
	private java.lang.String segno;

	// TI_ANAGRAFICO CHAR(1) NOT NULL
	private java.lang.String ti_anagrafico;

	// TI_CASSA_COMPETENZA CHAR(2) NOT NULL
	private java.lang.String ti_cassa_competenza;

public V_tipo_trattamento_tipo_coriBase() {
	super();
}
/* 
 * Getter dell'attributo calcolo_imponibile
 */
public java.lang.String getCalcolo_imponibile() {
	return calcolo_imponibile;
}
/* 
 * Getter dell'attributo cd_classificazione_cori
 */
public java.lang.String getCd_classificazione_cori() {
	return cd_classificazione_cori;
}
/* 
 * Getter dell'attributo cd_cori
 */
public java.lang.String getCd_cori() {
	return cd_cori;
}
/* 
 * Getter dell'attributo cd_trattamento
 */
public java.lang.String getCd_trattamento() {
	return cd_trattamento;
}
/* 
 * Getter dell'attributo dt_fin_val_tipo_cori
 */
public java.sql.Timestamp getDt_fin_val_tipo_cori() {
	return dt_fin_val_tipo_cori;
}
/* 
 * Getter dell'attributo dt_fin_val_tratt_cori
 */
public java.sql.Timestamp getDt_fin_val_tratt_cori() {
	return dt_fin_val_tratt_cori;
}
/* 
 * Getter dell'attributo dt_fin_val_trattamento
 */
public java.sql.Timestamp getDt_fin_val_trattamento() {
	return dt_fin_val_trattamento;
}
/* 
 * Getter dell'attributo dt_ini_val_tipo_cori
 */
public java.sql.Timestamp getDt_ini_val_tipo_cori() {
	return dt_ini_val_tipo_cori;
}
/* 
 * Getter dell'attributo dt_ini_val_tratt_cori
 */
public java.sql.Timestamp getDt_ini_val_tratt_cori() {
	return dt_ini_val_tratt_cori;
}
/* 
 * Getter dell'attributo dt_ini_val_trattamento
 */
public java.sql.Timestamp getDt_ini_val_trattamento() {
	return dt_ini_val_trattamento;
}
/* 
 * Getter dell'attributo fl_detrazioni_dipendente
 */
public java.lang.Boolean getFl_detrazioni_dipendente() {
	return fl_detrazioni_dipendente;
}
/* 
 * Getter dell'attributo fl_detrazioni_familiari
 */
public java.lang.Boolean getFl_detrazioni_familiari() {
	return fl_detrazioni_familiari;
}
/* 
 * Getter dell'attributo fl_irpef_annualizzata
 */
public java.lang.Boolean getFl_irpef_annualizzata() {
	return fl_irpef_annualizzata;
}
/* 
 * Getter dell'attributo fl_scrivi_montanti
 */
public java.lang.Boolean getFl_scrivi_montanti() {
	return fl_scrivi_montanti;
}
/* 
 * Getter dell'attributo fl_tassazione_separata
 */
public java.lang.Boolean getFl_tassazione_separata() {
	return fl_tassazione_separata;
}
/* 
 * Getter dell'attributo fl_uso_in_lordizza
 */
public java.lang.Boolean getFl_uso_in_lordizza() {
	return fl_uso_in_lordizza;
}
/* 
 * Getter dell'attributo id_riga
 */
public java.lang.String getId_riga() {
	return id_riga;
}
/* 
 * Getter dell'attributo pg_classificazione_montanti
 */
public java.lang.Long getPg_classificazione_montanti() {
	return pg_classificazione_montanti;
}
/* 
 * Getter dell'attributo precisione
 */
public java.math.BigDecimal getPrecisione() {
	return precisione;
}
/* 
 * Getter dell'attributo segno
 */
public java.lang.String getSegno() {
	return segno;
}
/* 
 * Getter dell'attributo ti_anagrafico
 */
public java.lang.String getTi_anagrafico() {
	return ti_anagrafico;
}
/* 
 * Getter dell'attributo ti_cassa_competenza
 */
public java.lang.String getTi_cassa_competenza() {
	return ti_cassa_competenza;
}
/* 
 * Setter dell'attributo calcolo_imponibile
 */
public void setCalcolo_imponibile(java.lang.String calcolo_imponibile) {
	this.calcolo_imponibile = calcolo_imponibile;
}
/* 
 * Setter dell'attributo cd_classificazione_cori
 */
public void setCd_classificazione_cori(java.lang.String cd_classificazione_cori) {
	this.cd_classificazione_cori = cd_classificazione_cori;
}
/* 
 * Setter dell'attributo cd_cori
 */
public void setCd_cori(java.lang.String cd_cori) {
	this.cd_cori = cd_cori;
}
/* 
 * Setter dell'attributo cd_trattamento
 */
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
/* 
 * Setter dell'attributo dt_fin_val_tipo_cori
 */
public void setDt_fin_val_tipo_cori(java.sql.Timestamp dt_fin_val_tipo_cori) {
	this.dt_fin_val_tipo_cori = dt_fin_val_tipo_cori;
}
/* 
 * Setter dell'attributo dt_fin_val_tratt_cori
 */
public void setDt_fin_val_tratt_cori(java.sql.Timestamp dt_fin_val_tratt_cori) {
	this.dt_fin_val_tratt_cori = dt_fin_val_tratt_cori;
}
/* 
 * Setter dell'attributo dt_fin_val_trattamento
 */
public void setDt_fin_val_trattamento(java.sql.Timestamp dt_fin_val_trattamento) {
	this.dt_fin_val_trattamento = dt_fin_val_trattamento;
}
/* 
 * Setter dell'attributo dt_ini_val_tipo_cori
 */
public void setDt_ini_val_tipo_cori(java.sql.Timestamp dt_ini_val_tipo_cori) {
	this.dt_ini_val_tipo_cori = dt_ini_val_tipo_cori;
}
/* 
 * Setter dell'attributo dt_ini_val_tratt_cori
 */
public void setDt_ini_val_tratt_cori(java.sql.Timestamp dt_ini_val_tratt_cori) {
	this.dt_ini_val_tratt_cori = dt_ini_val_tratt_cori;
}
/* 
 * Setter dell'attributo dt_ini_val_trattamento
 */
public void setDt_ini_val_trattamento(java.sql.Timestamp dt_ini_val_trattamento) {
	this.dt_ini_val_trattamento = dt_ini_val_trattamento;
}
/* 
 * Setter dell'attributo fl_detrazioni_dipendente
 */
public void setFl_detrazioni_dipendente(java.lang.Boolean fl_detrazioni_dipendente) {
	this.fl_detrazioni_dipendente = fl_detrazioni_dipendente;
}
/* 
 * Setter dell'attributo fl_detrazioni_familiari
 */
public void setFl_detrazioni_familiari(java.lang.Boolean fl_detrazioni_familiari) {
	this.fl_detrazioni_familiari = fl_detrazioni_familiari;
}
/* 
 * Setter dell'attributo fl_irpef_annualizzata
 */
public void setFl_irpef_annualizzata(java.lang.Boolean fl_irpef_annualizzata) {
	this.fl_irpef_annualizzata = fl_irpef_annualizzata;
}
/* 
 * Setter dell'attributo fl_scrivi_montanti
 */
public void setFl_scrivi_montanti(java.lang.Boolean fl_scrivi_montanti) {
	this.fl_scrivi_montanti = fl_scrivi_montanti;
}
/* 
 * Setter dell'attributo fl_tassazione_separata
 */
public void setFl_tassazione_separata(java.lang.Boolean fl_tassazione_separata) {
	this.fl_tassazione_separata = fl_tassazione_separata;
}
/* 
 * Setter dell'attributo fl_uso_in_lordizza
 */
public void setFl_uso_in_lordizza(java.lang.Boolean fl_uso_in_lordizza) {
	this.fl_uso_in_lordizza = fl_uso_in_lordizza;
}
/* 
 * Setter dell'attributo id_riga
 */
public void setId_riga(java.lang.String id_riga) {
	this.id_riga = id_riga;
}
/* 
 * Setter dell'attributo pg_classificazione_montanti
 */
public void setPg_classificazione_montanti(java.lang.Long pg_classificazione_montanti) {
	this.pg_classificazione_montanti = pg_classificazione_montanti;
}
/* 
 * Setter dell'attributo precisione
 */
public void setPrecisione(java.math.BigDecimal precisione) {
	this.precisione = precisione;
}
/* 
 * Setter dell'attributo segno
 */
public void setSegno(java.lang.String segno) {
	this.segno = segno;
}
/* 
 * Setter dell'attributo ti_anagrafico
 */
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	this.ti_anagrafico = ti_anagrafico;
}
/* 
 * Setter dell'attributo ti_cassa_competenza
 */
public void setTi_cassa_competenza(java.lang.String ti_cassa_competenza) {
	this.ti_cassa_competenza = ti_cassa_competenza;
}
}
