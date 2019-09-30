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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Reversale_rigaBase extends Reversale_rigaKey implements Keyed {
	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_UO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo_uo;

	// DS_REVERSALE_RIGA VARCHAR(300)
	private java.lang.String ds_reversale_riga;

	// FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pgiro;

	// IM_REVERSALE_RIGA DECIMAL(15,2)
	private java.math.BigDecimal im_reversale_riga;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

public Reversale_rigaBase() {
	super();
}
public Reversale_rigaBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario,java.lang.Long pg_reversale) {
	super(cd_cds,esercizio,esercizio_accertamento,esercizio_ori_accertamento,pg_accertamento,pg_accertamento_scadenzario,pg_reversale);
}
public Reversale_rigaBase(java.lang.String cd_cds,java.lang.String cd_cds_doc_amm,java.lang.String cd_tipo_documento_amm,java.lang.String cd_uo_doc_amm,java.lang.Integer esercizio,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_doc_amm,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario,java.lang.Long pg_doc_amm,java.lang.Long pg_reversale) {
	super(cd_cds,cd_cds_doc_amm,cd_tipo_documento_amm,cd_uo_doc_amm,esercizio,esercizio_accertamento,esercizio_doc_amm,esercizio_ori_accertamento,pg_accertamento,pg_accertamento_scadenzario,pg_doc_amm,pg_reversale);
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_terzo_uo
 */
public java.lang.Integer getCd_terzo_uo() {
	return cd_terzo_uo;
}
/* 
 * Getter dell'attributo ds_reversale_riga
 */
public java.lang.String getDs_reversale_riga() {
	return ds_reversale_riga;
}
/* 
 * Getter dell'attributo fl_pgiro
 */
public java.lang.Boolean getFl_pgiro() {
	return fl_pgiro;
}
/* 
 * Getter dell'attributo im_reversale_riga
 */
public java.math.BigDecimal getIm_reversale_riga() {
	return im_reversale_riga;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_terzo_uo
 */
public void setCd_terzo_uo(java.lang.Integer cd_terzo_uo) {
	this.cd_terzo_uo = cd_terzo_uo;
}
/* 
 * Setter dell'attributo ds_reversale_riga
 */
public void setDs_reversale_riga(java.lang.String ds_reversale_riga) {
	this.ds_reversale_riga = ds_reversale_riga;
}
/* 
 * Setter dell'attributo fl_pgiro
 */
public void setFl_pgiro(java.lang.Boolean fl_pgiro) {
	this.fl_pgiro = fl_pgiro;
}
/* 
 * Setter dell'attributo im_reversale_riga
 */
public void setIm_reversale_riga(java.math.BigDecimal im_reversale_riga) {
	this.im_reversale_riga = im_reversale_riga;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
