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

package it.cnr.contab.logregistry00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Log_registryBase extends Log_registryKey implements Keyed {
	// DS_TABLE_LOG VARCHAR(100) NOT NULL
	private java.lang.String ds_table_log;

	// DT_ATTIVAZIONE_AD TIMESTAMP
	private java.sql.Timestamp dt_attivazione_ad;

	// DT_ATTIVAZIONE_AI TIMESTAMP
	private java.sql.Timestamp dt_attivazione_ai;

	// DT_ATTIVAZIONE_AU TIMESTAMP
	private java.sql.Timestamp dt_attivazione_au;

	// DT_DISATTIVAZIONE_AD TIMESTAMP
	private java.sql.Timestamp dt_disattivazione_ad;

	// DT_DISATTIVAZIONE_AI TIMESTAMP
	private java.sql.Timestamp dt_disattivazione_ai;

	// DT_DISATTIVAZIONE_AU TIMESTAMP
	private java.sql.Timestamp dt_disattivazione_au;

	// DT_ULTIMO_START_AD TIMESTAMP
	private java.sql.Timestamp dt_ultimo_start_ad;

	// DT_ULTIMO_START_AI TIMESTAMP
	private java.sql.Timestamp dt_ultimo_start_ai;

	// DT_ULTIMO_START_AU TIMESTAMP
	private java.sql.Timestamp dt_ultimo_start_au;

	// NOME_PKG VARCHAR(40) NOT NULL
	private java.lang.String nome_pkg;

	// NOME_TABLE_LOG VARCHAR(30) NOT NULL
	private java.lang.String nome_table_log;

	// NOME_TRG_AD VARCHAR(40) NOT NULL
	private java.lang.String nome_trg_ad;

	// NOME_TRG_AI VARCHAR(40) NOT NULL
	private java.lang.String nome_trg_ai;

	// NOME_TRG_AU VARCHAR(40) NOT NULL
	private java.lang.String nome_trg_au;

	// NUM_MAX_RIGHE_TABLE_LOG DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal num_max_righe_table_log;

	// NUM_RIGHE_IN_TABLE_LOG DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal num_righe_in_table_log;

	// STATO_TRG_AD CHAR(1) NOT NULL
	private java.lang.String stato_trg_ad;

	// STATO_TRG_AI CHAR(1) NOT NULL
	private java.lang.String stato_trg_ai;

	// STATO_TRG_AU CHAR(1) NOT NULL
	private java.lang.String stato_trg_au;

public Log_registryBase() {
	super();
}
public Log_registryBase(java.lang.String nome_table_src) {
	super(nome_table_src);
}
/* 
 * Getter dell'attributo ds_table_log
 */
public java.lang.String getDs_table_log() {
	return ds_table_log;
}
/* 
 * Getter dell'attributo dt_attivazione_ad
 */
public java.sql.Timestamp getDt_attivazione_ad() {
	return dt_attivazione_ad;
}
/* 
 * Getter dell'attributo dt_attivazione_ai
 */
public java.sql.Timestamp getDt_attivazione_ai() {
	return dt_attivazione_ai;
}
/* 
 * Getter dell'attributo dt_attivazione_au
 */
public java.sql.Timestamp getDt_attivazione_au() {
	return dt_attivazione_au;
}
/* 
 * Getter dell'attributo dt_disattivazione_ad
 */
public java.sql.Timestamp getDt_disattivazione_ad() {
	return dt_disattivazione_ad;
}
/* 
 * Getter dell'attributo dt_disattivazione_ai
 */
public java.sql.Timestamp getDt_disattivazione_ai() {
	return dt_disattivazione_ai;
}
/* 
 * Getter dell'attributo dt_disattivazione_au
 */
public java.sql.Timestamp getDt_disattivazione_au() {
	return dt_disattivazione_au;
}
/* 
 * Getter dell'attributo dt_ultimo_start_ad
 */
public java.sql.Timestamp getDt_ultimo_start_ad() {
	return dt_ultimo_start_ad;
}
/* 
 * Getter dell'attributo dt_ultimo_start_ai
 */
public java.sql.Timestamp getDt_ultimo_start_ai() {
	return dt_ultimo_start_ai;
}
/* 
 * Getter dell'attributo dt_ultimo_start_au
 */
public java.sql.Timestamp getDt_ultimo_start_au() {
	return dt_ultimo_start_au;
}
/* 
 * Getter dell'attributo nome_pkg
 */
public java.lang.String getNome_pkg() {
	return nome_pkg;
}
/* 
 * Getter dell'attributo nome_table_log
 */
public java.lang.String getNome_table_log() {
	return nome_table_log;
}
/* 
 * Getter dell'attributo nome_trg_ad
 */
public java.lang.String getNome_trg_ad() {
	return nome_trg_ad;
}
/* 
 * Getter dell'attributo nome_trg_ai
 */
public java.lang.String getNome_trg_ai() {
	return nome_trg_ai;
}
/* 
 * Getter dell'attributo nome_trg_au
 */
public java.lang.String getNome_trg_au() {
	return nome_trg_au;
}
/* 
 * Getter dell'attributo num_max_righe_table_log
 */
public java.math.BigDecimal getNum_max_righe_table_log() {
	return num_max_righe_table_log;
}
/* 
 * Getter dell'attributo num_righe_in_table_log
 */
public java.math.BigDecimal getNum_righe_in_table_log() {
	return num_righe_in_table_log;
}
/* 
 * Getter dell'attributo stato_trg_ad
 */
public java.lang.String getStato_trg_ad() {
	return stato_trg_ad;
}
/* 
 * Getter dell'attributo stato_trg_ai
 */
public java.lang.String getStato_trg_ai() {
	return stato_trg_ai;
}
/* 
 * Getter dell'attributo stato_trg_au
 */
public java.lang.String getStato_trg_au() {
	return stato_trg_au;
}
/* 
 * Setter dell'attributo ds_table_log
 */
public void setDs_table_log(java.lang.String ds_table_log) {
	this.ds_table_log = ds_table_log;
}
/* 
 * Setter dell'attributo dt_attivazione_ad
 */
public void setDt_attivazione_ad(java.sql.Timestamp dt_attivazione_ad) {
	this.dt_attivazione_ad = dt_attivazione_ad;
}
/* 
 * Setter dell'attributo dt_attivazione_ai
 */
public void setDt_attivazione_ai(java.sql.Timestamp dt_attivazione_ai) {
	this.dt_attivazione_ai = dt_attivazione_ai;
}
/* 
 * Setter dell'attributo dt_attivazione_au
 */
public void setDt_attivazione_au(java.sql.Timestamp dt_attivazione_au) {
	this.dt_attivazione_au = dt_attivazione_au;
}
/* 
 * Setter dell'attributo dt_disattivazione_ad
 */
public void setDt_disattivazione_ad(java.sql.Timestamp dt_disattivazione_ad) {
	this.dt_disattivazione_ad = dt_disattivazione_ad;
}
/* 
 * Setter dell'attributo dt_disattivazione_ai
 */
public void setDt_disattivazione_ai(java.sql.Timestamp dt_disattivazione_ai) {
	this.dt_disattivazione_ai = dt_disattivazione_ai;
}
/* 
 * Setter dell'attributo dt_disattivazione_au
 */
public void setDt_disattivazione_au(java.sql.Timestamp dt_disattivazione_au) {
	this.dt_disattivazione_au = dt_disattivazione_au;
}
/* 
 * Setter dell'attributo dt_ultimo_start_ad
 */
public void setDt_ultimo_start_ad(java.sql.Timestamp dt_ultimo_start_ad) {
	this.dt_ultimo_start_ad = dt_ultimo_start_ad;
}
/* 
 * Setter dell'attributo dt_ultimo_start_ai
 */
public void setDt_ultimo_start_ai(java.sql.Timestamp dt_ultimo_start_ai) {
	this.dt_ultimo_start_ai = dt_ultimo_start_ai;
}
/* 
 * Setter dell'attributo dt_ultimo_start_au
 */
public void setDt_ultimo_start_au(java.sql.Timestamp dt_ultimo_start_au) {
	this.dt_ultimo_start_au = dt_ultimo_start_au;
}
/* 
 * Setter dell'attributo nome_pkg
 */
public void setNome_pkg(java.lang.String nome_pkg) {
	this.nome_pkg = nome_pkg;
}
/* 
 * Setter dell'attributo nome_table_log
 */
public void setNome_table_log(java.lang.String nome_table_log) {
	this.nome_table_log = nome_table_log;
}
/* 
 * Setter dell'attributo nome_trg_ad
 */
public void setNome_trg_ad(java.lang.String nome_trg_ad) {
	this.nome_trg_ad = nome_trg_ad;
}
/* 
 * Setter dell'attributo nome_trg_ai
 */
public void setNome_trg_ai(java.lang.String nome_trg_ai) {
	this.nome_trg_ai = nome_trg_ai;
}
/* 
 * Setter dell'attributo nome_trg_au
 */
public void setNome_trg_au(java.lang.String nome_trg_au) {
	this.nome_trg_au = nome_trg_au;
}
/* 
 * Setter dell'attributo num_max_righe_table_log
 */
public void setNum_max_righe_table_log(java.math.BigDecimal num_max_righe_table_log) {
	this.num_max_righe_table_log = num_max_righe_table_log;
}
/* 
 * Setter dell'attributo num_righe_in_table_log
 */
public void setNum_righe_in_table_log(java.math.BigDecimal num_righe_in_table_log) {
	this.num_righe_in_table_log = num_righe_in_table_log;
}
/* 
 * Setter dell'attributo stato_trg_ad
 */
public void setStato_trg_ad(java.lang.String stato_trg_ad) {
	this.stato_trg_ad = stato_trg_ad;
}
/* 
 * Setter dell'attributo stato_trg_ai
 */
public void setStato_trg_ai(java.lang.String stato_trg_ai) {
	this.stato_trg_ai = stato_trg_ai;
}
/* 
 * Setter dell'attributo stato_trg_au
 */
public void setStato_trg_au(java.lang.String stato_trg_au) {
	this.stato_trg_au = stato_trg_au;
}
}
