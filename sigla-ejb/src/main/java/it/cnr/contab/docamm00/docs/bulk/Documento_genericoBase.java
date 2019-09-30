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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Documento_genericoBase extends Documento_genericoKey implements Keyed {
	// CAMBIO DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cd_divisa;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// DATA_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp data_registrazione;

	// DS_DOCUMENTO_GENERICO VARCHAR(300)
	private java.lang.String ds_documento_generico;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// DT_PAGAMENTO_FONDO_ECO TIMESTAMP
	private java.sql.Timestamp dt_pagamento_fondo_eco;

	// DT_SCADENZA TIMESTAMP
	private java.sql.Timestamp dt_scadenza;

	// ESERCIZIO_LETTERA DECIMAL(4,0)
	private java.lang.Integer esercizio_lettera;

	// IM_TOTALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale;

	// PG_LETTERA DECIMAL(10,0)
	private java.lang.Long pg_lettera;

	// STATO_COAN CHAR(1) NOT NULL
	private java.lang.String stato_coan;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// STATO_PAGAMENTO_FONDO_ECO CHAR(1) NOT NULL
	private java.lang.String stato_pagamento_fondo_eco;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;
	
	private java.lang.String stato_liquidazione;
	
	private java.lang.String causale;

	// ID_TIPO_DOCUMENTO_GENERICO NUMBER NOT NULL
	private java.lang.Integer idTipoDocumentoGenerico;

public Documento_genericoBase() {
	super();
}
public Documento_genericoBase(java.lang.String cd_cds,java.lang.String cd_tipo_documento_amm,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_documento_generico) {
	super(cd_cds,cd_tipo_documento_amm,cd_unita_organizzativa,esercizio,pg_documento_generico);
}
/* 
 * Getter dell'attributo cambio
 */
public java.math.BigDecimal getCambio() {
	return cambio;
}
/* 
 * Getter dell'attributo cd_cds_origine
 */
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/* 
 * Getter dell'attributo data_registrazione
 */
public java.sql.Timestamp getData_registrazione() {
	return data_registrazione;
}
/* 
 * Getter dell'attributo ds_documento_generico
 */
public java.lang.String getDs_documento_generico() {
	return ds_documento_generico;
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
 * Getter dell'attributo dt_pagamento_fondo_eco
 */
public java.sql.Timestamp getDt_pagamento_fondo_eco() {
	return dt_pagamento_fondo_eco;
}
/* 
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/* 
 * Getter dell'attributo esercizio_lettera
 */
public java.lang.Integer getEsercizio_lettera() {
	return esercizio_lettera;
}
/* 
 * Getter dell'attributo im_totale
 */
public java.math.BigDecimal getIm_totale() {
	return im_totale;
}
/* 
 * Getter dell'attributo pg_lettera
 */
public java.lang.Long getPg_lettera() {
	return pg_lettera;
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
 * Getter dell'attributo stato_pagamento_fondo_eco
 */
public java.lang.String getStato_pagamento_fondo_eco() {
	return stato_pagamento_fondo_eco;
}
/* 
 * Getter dell'attributo ti_associato_manrev
 */
public java.lang.String getTi_associato_manrev() {
	return ti_associato_manrev;
}
/* 
 * Getter dell'attributo ti_istituz_commerc
 */
public java.lang.String getTi_istituz_commerc() {
	return ti_istituz_commerc;
}
/* 
 * Setter dell'attributo cambio
 */
public void setCambio(java.math.BigDecimal cambio) {
	this.cambio = cambio;
}
/* 
 * Setter dell'attributo cd_cds_origine
 */
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
}
/* 
 * Setter dell'attributo data_registrazione
 */
public void setData_registrazione(java.sql.Timestamp data_registrazione) {
	this.data_registrazione = data_registrazione;
}
/* 
 * Setter dell'attributo ds_documento_generico
 */
public void setDs_documento_generico(java.lang.String ds_documento_generico) {
	this.ds_documento_generico = ds_documento_generico;
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
 * Setter dell'attributo dt_pagamento_fondo_eco
 */
public void setDt_pagamento_fondo_eco(java.sql.Timestamp dt_pagamento_fondo_eco) {
	this.dt_pagamento_fondo_eco = dt_pagamento_fondo_eco;
}
/* 
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/* 
 * Setter dell'attributo esercizio_lettera
 */
public void setEsercizio_lettera(java.lang.Integer esercizio_lettera) {
	this.esercizio_lettera = esercizio_lettera;
}
/* 
 * Setter dell'attributo im_totale
 */
public void setIm_totale(java.math.BigDecimal im_totale) {
	this.im_totale = im_totale;
}
/* 
 * Setter dell'attributo pg_lettera
 */
public void setPg_lettera(java.lang.Long pg_lettera) {
	this.pg_lettera = pg_lettera;
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
 * Setter dell'attributo stato_pagamento_fondo_eco
 */
public void setStato_pagamento_fondo_eco(java.lang.String stato_pagamento_fondo_eco) {
	this.stato_pagamento_fondo_eco = stato_pagamento_fondo_eco;
}
/* 
 * Setter dell'attributo ti_associato_manrev
 */
public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
	this.ti_associato_manrev = ti_associato_manrev;
}
/* 
 * Setter dell'attributo ti_istituz_commerc
 */
public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
	this.ti_istituz_commerc = ti_istituz_commerc;
}
public java.lang.String getStato_liquidazione() {
	return stato_liquidazione;
}
public void setStato_liquidazione(java.lang.String stato_liquidazione) {
	this.stato_liquidazione = stato_liquidazione;
}
public java.lang.String getCausale() {
	return causale;
}
public void setCausale(java.lang.String causale) {
	this.causale = causale;
}
public java.lang.Integer getIdTipoDocumentoGenerico() {
	return idTipoDocumentoGenerico;
}
public void setIdTipoDocumentoGenerico(java.lang.Integer idTipoDocumentoGenerico) {
	this.idTipoDocumentoGenerico = idTipoDocumentoGenerico;
}
}
