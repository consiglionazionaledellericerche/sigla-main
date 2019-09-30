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

public class RimborsoBase extends RimborsoKey implements Keyed {
	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// CD_CDS_ANTICIPO VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_anticipo;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_MODALITA_PAG_UO_CDS VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag_uo_cds;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_UO_CDS DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo_uo_cds;

	// CD_UO_ANTICIPO VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_anticipo;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// DS_RIMBORSO VARCHAR(300)
	private java.lang.String ds_rimborso;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// DT_REGISTRAZIONE TIMESTAMP
	private java.sql.Timestamp dt_registrazione;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_ANTICIPO DECIMAL(4,0)
	private java.lang.Integer esercizio_anticipo;

	// IM_RIMBORSO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rimborso;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_accertamento_scadenzario;

	// PG_ANTICIPO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_anticipo;

	// PG_BANCA_UO_CDS DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca_uo_cds;

	// STATO_COAN CHAR(1) NOT NULL
	private java.lang.String stato_coan;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// TI_ANAGRAFICO CHAR(1) NOT NULL
	private java.lang.String ti_anagrafico;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

public RimborsoBase() {
	super();
}
public RimborsoBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_rimborso) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_rimborso);
}
/* 
 * Getter dell'attributo cd_cds_accertamento
 */
public java.lang.String getCd_cds_accertamento() {
	return cd_cds_accertamento;
}
/* 
 * Getter dell'attributo cd_cds_anticipo
 */
public java.lang.String getCd_cds_anticipo() {
	return cd_cds_anticipo;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_modalita_pag_uo_cds
 */
public java.lang.String getCd_modalita_pag_uo_cds() {
	return cd_modalita_pag_uo_cds;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_terzo_uo_cds
 */
public java.lang.Integer getCd_terzo_uo_cds() {
	return cd_terzo_uo_cds;
}
/* 
 * Getter dell'attributo cd_uo_anticipo
 */
public java.lang.String getCd_uo_anticipo() {
	return cd_uo_anticipo;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo ds_rimborso
 */
public java.lang.String getDs_rimborso() {
	return ds_rimborso;
}
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge() {
	return dt_a_competenza_coge;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge() {
	return dt_da_competenza_coge;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo esercizio_accertamento
 */
public java.lang.Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
}
/* 
 * Getter dell'attributo esercizio_anticipo
 */
public java.lang.Integer getEsercizio_anticipo() {
	return esercizio_anticipo;
}
/* 
 * Getter dell'attributo im_rimborso
 */
public java.math.BigDecimal getIm_rimborso() {
	return im_rimborso;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
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
/* 
 * Getter dell'attributo pg_anticipo
 */
public java.lang.Long getPg_anticipo() {
	return pg_anticipo;
}
/* 
 * Getter dell'attributo pg_banca_uo_cds
 */
public java.lang.Long getPg_banca_uo_cds() {
	return pg_banca_uo_cds;
}
/* 
 * Getter dell'attributo stato_coan
 */
public java.lang.String getStato_coan() {
	return stato_coan;
}
/* 
 * Getter dell'attributo stato_cofi
 */
public java.lang.String getStato_cofi() {
	return stato_cofi;
}
/* 
 * Getter dell'attributo stato_coge
 */
public java.lang.String getStato_coge() {
	return stato_coge;
}
/* 
 * Getter dell'attributo ti_anagrafico
 */
public java.lang.String getTi_anagrafico() {
	return ti_anagrafico;
}
/* 
 * Getter dell'attributo ti_associato_manrev
 */
public java.lang.String getTi_associato_manrev() {
	return ti_associato_manrev;
}
/* 
 * Setter dell'attributo cd_cds_accertamento
 */
public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
	this.cd_cds_accertamento = cd_cds_accertamento;
}
/* 
 * Setter dell'attributo cd_cds_anticipo
 */
public void setCd_cds_anticipo(java.lang.String cd_cds_anticipo) {
	this.cd_cds_anticipo = cd_cds_anticipo;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_modalita_pag_uo_cds
 */
public void setCd_modalita_pag_uo_cds(java.lang.String cd_modalita_pag_uo_cds) {
	this.cd_modalita_pag_uo_cds = cd_modalita_pag_uo_cds;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_terzo_uo_cds
 */
public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
	this.cd_terzo_uo_cds = cd_terzo_uo_cds;
}
/* 
 * Setter dell'attributo cd_uo_anticipo
 */
public void setCd_uo_anticipo(java.lang.String cd_uo_anticipo) {
	this.cd_uo_anticipo = cd_uo_anticipo;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo ds_rimborso
 */
public void setDs_rimborso(java.lang.String ds_rimborso) {
	this.ds_rimborso = ds_rimborso;
}
/* 
 * Setter dell'attributo dt_a_competenza_coge
 */
public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
	this.dt_a_competenza_coge = dt_a_competenza_coge;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_da_competenza_coge
 */
public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
	this.dt_da_competenza_coge = dt_da_competenza_coge;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
}
/* 
 * Setter dell'attributo esercizio_anticipo
 */
public void setEsercizio_anticipo(java.lang.Integer esercizio_anticipo) {
	this.esercizio_anticipo = esercizio_anticipo;
}
/* 
 * Setter dell'attributo im_rimborso
 */
public void setIm_rimborso(java.math.BigDecimal im_rimborso) {
	this.im_rimborso = im_rimborso;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
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
/* 
 * Setter dell'attributo pg_anticipo
 */
public void setPg_anticipo(java.lang.Long pg_anticipo) {
	this.pg_anticipo = pg_anticipo;
}
/* 
 * Setter dell'attributo pg_banca_uo_cds
 */
public void setPg_banca_uo_cds(java.lang.Long pg_banca_uo_cds) {
	this.pg_banca_uo_cds = pg_banca_uo_cds;
}
/* 
 * Setter dell'attributo stato_coan
 */
public void setStato_coan(java.lang.String stato_coan) {
	this.stato_coan = stato_coan;
}
/* 
 * Setter dell'attributo stato_cofi
 */
public void setStato_cofi(java.lang.String stato_cofi) {
	this.stato_cofi = stato_cofi;
}
/* 
 * Setter dell'attributo stato_coge
 */
public void setStato_coge(java.lang.String stato_coge) {
	this.stato_coge = stato_coge;
}
/* 
 * Setter dell'attributo ti_anagrafico
 */
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	this.ti_anagrafico = ti_anagrafico;
}
/* 
 * Setter dell'attributo ti_associato_manrev
 */
public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
	this.ti_associato_manrev = ti_associato_manrev;
}
}
