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

package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.jada.persistency.*;

public class Mandato_rigaRestBase extends Mandato_rigaRestKey implements Keyed {
	// CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cd_modalita_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_CEDENTE DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo_cedente;

	// DS_MANDATO_RIGA VARCHAR(300)
	private java.lang.String ds_mandato_riga;

	// FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pgiro;

	// IM_MANDATO_RIGA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_mandato_riga;

	// IM_RITENUTE_RIGA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ritenute_riga;

	// PG_BANCA DECIMAL(10,0)
	private java.lang.Long pg_banca;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

public Mandato_rigaRestBase() {
	super();
}
public Mandato_rigaRestBase(java.lang.String cd_cds,java.lang.String cd_cds_doc_amm,java.lang.String cd_tipo_documento_amm,java.lang.String cd_uo_doc_amm,java.lang.Integer esercizio,java.lang.Integer esercizio_doc_amm,java.lang.Integer esercizio_obbligazione,java.lang.Long pg_doc_amm,java.lang.Long pg_mandato,java.lang.Integer esercizio_ori_obbligazione,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario) {
	super(cd_cds,cd_cds_doc_amm,cd_tipo_documento_amm,cd_uo_doc_amm,esercizio,esercizio_doc_amm,esercizio_obbligazione,pg_doc_amm,pg_mandato,esercizio_ori_obbligazione,pg_obbligazione,pg_obbligazione_scadenzario);
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
/**
 * Insert the method's description here.
 * Creation date: (27/02/2003 10.12.12)
 * @return java.lang.Integer
 */
public java.lang.Integer getCd_terzo_cedente() {
	return cd_terzo_cedente;
}
/* 
 * Getter dell'attributo ds_mandato_riga
 */
public java.lang.String getDs_mandato_riga() {
	return ds_mandato_riga;
}
/* 
 * Getter dell'attributo fl_pgiro
 */
public java.lang.Boolean getFl_pgiro() {
	return fl_pgiro;
}
/* 
 * Getter dell'attributo im_mandato_riga
 */
public java.math.BigDecimal getIm_mandato_riga() {
	return im_mandato_riga;
}
/* 
 * Getter dell'attributo im_ritenute_riga
 */
public java.math.BigDecimal getIm_ritenute_riga() {
	return im_ritenute_riga;
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
/**
 * Insert the method's description here.
 * Creation date: (27/02/2003 10.12.12)
 * @param newCd_terzo_cedente java.lang.Integer
 */
public void setCd_terzo_cedente(java.lang.Integer newCd_terzo_cedente) {
	cd_terzo_cedente = newCd_terzo_cedente;
}
/* 
 * Setter dell'attributo ds_mandato_riga
 */
public void setDs_mandato_riga(java.lang.String ds_mandato_riga) {
	this.ds_mandato_riga = ds_mandato_riga;
}
/* 
 * Setter dell'attributo fl_pgiro
 */
public void setFl_pgiro(java.lang.Boolean fl_pgiro) {
	this.fl_pgiro = fl_pgiro;
}
/* 
 * Setter dell'attributo im_mandato_riga
 */
public void setIm_mandato_riga(java.math.BigDecimal im_mandato_riga) {
	this.im_mandato_riga = im_mandato_riga;
}
/* 
 * Setter dell'attributo im_ritenute_riga
 */
public void setIm_ritenute_riga(java.math.BigDecimal im_ritenute_riga) {
	this.im_ritenute_riga = im_ritenute_riga;
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
