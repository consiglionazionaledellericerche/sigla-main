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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_inv_bene_fatturaBase extends Ass_inv_bene_fatturaKey implements Persistent {
	// CD_CDS_FATT_ATT VARCHAR(30)
	private java.lang.String cd_cds_fatt_att;

	// CD_CDS_FATT_PASS VARCHAR(30)
	private java.lang.String cd_cds_fatt_pass;

	// CD_UO_FATT_ATT VARCHAR(30)
	private java.lang.String cd_uo_fatt_att;

	// CD_UO_FATT_PASS VARCHAR(30)
	private java.lang.String cd_uo_fatt_pass;

	// ESERCIZIO_FATT_ATT DECIMAL(4,0)
	private java.lang.Integer esercizio_fatt_att;

	// ESERCIZIO_FATT_PASS DECIMAL(4,0)
	private java.lang.Integer esercizio_fatt_pass;

	// NR_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long nr_inventario;

	// PG_FATTURA_ATTIVA DECIMAL(10,0)
	private java.lang.Long pg_fattura_attiva;

	// PG_FATTURA_PASSIVA DECIMAL(10,0)
	private java.lang.Long pg_fattura_passiva;

	// PG_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_inventario;

	// PROGRESSIVO DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivo;

	// PROGRESSIVO_RIGA_FATT_ATT DECIMAL(10,0)
	private java.lang.Long progressivo_riga_fatt_att;

	// PROGRESSIVO_RIGA_FATT_PASS DECIMAL(10,0)
	private java.lang.Long progressivo_riga_fatt_pass;
	
	//	ESERCIZIO DECIMAL(4,0) 
	 private java.lang.Integer esercizio;

	 // TI_DOCUMENTO CHAR(1) 
	 private java.lang.String ti_documento;

	 
	 // PG_BUONO_C_S DECIMAL(10,0)
	 private java.lang.Long pg_buono_c_s;
	 
	private java.lang.String cd_cds_doc_gen;

	 // CD_TIPO_DOCUMENTO_AMM VARCHAR(10) 
	private java.lang.String cd_tipo_documento_amm;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) 
	private java.lang.String cd_uo_doc_gen;

	// ESERCIZIO DECIMAL(4,0) 
	private java.lang.Integer esercizio_doc_gen;

	// PG_DOCUMENTO_GENERICO DECIMAL(10,0) 
	private java.lang.Long pg_documento_generico;

	// PROGRESSIVO_RIGA_DOC_GEN DECIMAL(10,0)
	private Long progressivo_riga_doc_gen;
		
		
public Ass_inv_bene_fatturaBase() {
	super();
}
public Ass_inv_bene_fatturaBase(Long pg_riga) {
	super(pg_riga);
}
/* 
 * Getter dell'attributo cd_cds_fatt_att
 */
public java.lang.String getCd_cds_fatt_att() {
	return cd_cds_fatt_att;
}
/* 
 * Getter dell'attributo cd_cds_fatt_pass
 */
public java.lang.String getCd_cds_fatt_pass() {
	return cd_cds_fatt_pass;
}
/* 
 * Getter dell'attributo cd_uo_fatt_att
 */
public java.lang.String getCd_uo_fatt_att() {
	return cd_uo_fatt_att;
}
/* 
 * Getter dell'attributo cd_uo_fatt_pass
 */
public java.lang.String getCd_uo_fatt_pass() {
	return cd_uo_fatt_pass;
}
/* 
 * Getter dell'attributo esercizio_fatt_att
 */
public java.lang.Integer getEsercizio_fatt_att() {
	return esercizio_fatt_att;
}
/* 
 * Getter dell'attributo esercizio_fatt_pass
 */
public java.lang.Integer getEsercizio_fatt_pass() {
	return esercizio_fatt_pass;
}
/* 
 * Getter dell'attributo nr_inventario
 */
public java.lang.Long getNr_inventario() {
	return nr_inventario;
}
/* 
 * Getter dell'attributo pg_fattura_attiva
 */
public java.lang.Long getPg_fattura_attiva() {
	return pg_fattura_attiva;
}
/* 
 * Getter dell'attributo pg_fattura_passiva
 */
public java.lang.Long getPg_fattura_passiva() {
	return pg_fattura_passiva;
}
/* 
 * Getter dell'attributo pg_inventario
 */
public java.lang.Long getPg_inventario() {
	return pg_inventario;
}
/* 
 * Getter dell'attributo progressivo
 */
public java.lang.Long getProgressivo() {
	return progressivo;
}
/* 
 * Getter dell'attributo progressivo_riga_fatt_att
 */
public java.lang.Long getProgressivo_riga_fatt_att() {
	return progressivo_riga_fatt_att;
}
/* 
 * Getter dell'attributo progressivo_riga_fatt_pass
 */
public java.lang.Long getProgressivo_riga_fatt_pass() {
	return progressivo_riga_fatt_pass;
}
/* 
 * Setter dell'attributo cd_cds_fatt_att
 */
public void setCd_cds_fatt_att(java.lang.String cd_cds_fatt_att) {
	this.cd_cds_fatt_att = cd_cds_fatt_att;
}
/* 
 * Setter dell'attributo cd_cds_fatt_pass
 */
public void setCd_cds_fatt_pass(java.lang.String cd_cds_fatt_pass) {
	this.cd_cds_fatt_pass = cd_cds_fatt_pass;
}
/* 
 * Setter dell'attributo cd_uo_fatt_att
 */
public void setCd_uo_fatt_att(java.lang.String cd_uo_fatt_att) {
	this.cd_uo_fatt_att = cd_uo_fatt_att;
}
/* 
 * Setter dell'attributo cd_uo_fatt_pass
 */
public void setCd_uo_fatt_pass(java.lang.String cd_uo_fatt_pass) {
	this.cd_uo_fatt_pass = cd_uo_fatt_pass;
}
/* 
 * Setter dell'attributo esercizio_fatt_att
 */
public void setEsercizio_fatt_att(java.lang.Integer esercizio_fatt_att) {
	this.esercizio_fatt_att = esercizio_fatt_att;
}
/* 
 * Setter dell'attributo esercizio_fatt_pass
 */
public void setEsercizio_fatt_pass(java.lang.Integer esercizio_fatt_pass) {
	this.esercizio_fatt_pass = esercizio_fatt_pass;
}
/* 
 * Setter dell'attributo nr_inventario
 */
public void setNr_inventario(java.lang.Long nr_inventario) {
	this.nr_inventario = nr_inventario;
}
/* 
 * Setter dell'attributo pg_fattura_attiva
 */
public void setPg_fattura_attiva(java.lang.Long pg_fattura_attiva) {
	this.pg_fattura_attiva = pg_fattura_attiva;
}
/* 
 * Setter dell'attributo pg_fattura_passiva
 */
public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
	this.pg_fattura_passiva = pg_fattura_passiva;
}
/* 
 * Setter dell'attributo pg_inventario
 */
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.pg_inventario = pg_inventario;
}
/* 
 * Setter dell'attributo progressivo
 */
public void setProgressivo(java.lang.Long progressivo) {
	this.progressivo = progressivo;
}
/* 
 * Setter dell'attributo progressivo_riga_fatt_att
 */
public void setProgressivo_riga_fatt_att(java.lang.Long progressivo_riga_fatt_att) {
	this.progressivo_riga_fatt_att = progressivo_riga_fatt_att;
}
/* 
 * Setter dell'attributo progressivo_riga_fatt_pass
 */
public void setProgressivo_riga_fatt_pass(java.lang.Long progressivo_riga_fatt_pass) {
	this.progressivo_riga_fatt_pass = progressivo_riga_fatt_pass;
}
	
public java.lang.Integer getEsercizio() {
	return esercizio;
}

public java.lang.Long getPg_buono_c_s() {
	return pg_buono_c_s;
}

public java.lang.String getTi_documento() {
	return ti_documento;
}


public void setEsercizio(java.lang.Integer integer) {
	esercizio = integer;
}

public void setPg_buono_c_s(java.lang.Long long1) {
	pg_buono_c_s = long1;
}

public void setTi_documento(java.lang.String string) {
	ti_documento = string;
}
public java.lang.String getCd_cds_doc_gen() {
	return cd_cds_doc_gen;
}
public void setCd_cds_doc_gen(java.lang.String cd_cds_doc_gen) {
	this.cd_cds_doc_gen = cd_cds_doc_gen;
}
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}
public java.lang.String getCd_uo_doc_gen() {
	return cd_uo_doc_gen;
}
public void setCd_uo_doc_gen(java.lang.String cd_uo_doc_gen) {
	this.cd_uo_doc_gen = cd_uo_doc_gen;
}
public java.lang.Integer getEsercizio_doc_gen() {
	return esercizio_doc_gen;
}
public void setEsercizio_doc_gen(java.lang.Integer esercizio_doc_gen) {
	this.esercizio_doc_gen = esercizio_doc_gen;
}
public java.lang.Long getPg_documento_generico() {
	return pg_documento_generico;
}
public void setPg_documento_generico(java.lang.Long pg_documento_generico) {
	this.pg_documento_generico = pg_documento_generico;
}
public Long getProgressivo_riga_doc_gen() {
	return progressivo_riga_doc_gen;
}
public void setProgressivo_riga_doc_gen(Long progressivo_riga_doc_gen) {
	this.progressivo_riga_doc_gen = progressivo_riga_doc_gen;
}

}
