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

public class Missione_dettaglioBase extends Missione_dettaglioKey implements Keyed {
	// CAMBIO_SPESA DECIMAL(15,4)
	private java.math.BigDecimal cambio_spesa;

	// CD_DIVISA_SPESA VARCHAR(10)
	private java.lang.String cd_divisa_spesa;

	// CD_TI_PASTO VARCHAR(20)
	private java.lang.String cd_ti_pasto;

	// CD_TI_SPESA VARCHAR(20)
	private java.lang.String cd_ti_spesa;

	// CHILOMETRI DECIMAL(15,2)
	private java.math.BigDecimal chilometri;

	// DS_GIUSTIFICATIVO VARCHAR(100)
	private java.lang.String ds_giustificativo;

	// DS_NO_GIUSTIFICATIVO VARCHAR(100)
	private java.lang.String ds_no_giustificativo;

	// DS_SPESA VARCHAR(100)
	private java.lang.String ds_spesa;

	// DS_TI_SPESA VARCHAR(100)
	private java.lang.String ds_ti_spesa;

	// FL_DIARIA_MANUALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_diaria_manuale;

	// FL_SPESA_ANTICIPATA CHAR(1)
	private java.lang.Boolean fl_spesa_anticipata;

	// ID_GIUSTIFICATIVO VARCHAR(20)
	private java.lang.String id_giustificativo;

	// IM_BASE_MAGGIORAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_base_maggiorazione;

	// IM_DIARIA_LORDA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_diaria_lorda;

	// IM_DIARIA_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_diaria_netto;

	// IM_MAGGIORAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_maggiorazione;

	// IM_MAGGIORAZIONE_EURO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_maggiorazione_euro;

	// IM_QUOTA_ESENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_quota_esente;

	// IM_SPESA_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spesa_divisa;

	// IM_SPESA_EURO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spesa_euro;

	// IM_SPESA_MAX DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spesa_max;

	// IM_SPESA_MAX_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spesa_max_divisa;

	// IM_TOTALE_SPESA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_spesa;

	// INDENNITA_CHILOMETRICA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal indennita_chilometrica;

	// LOCALITA_SPOSTAMENTO VARCHAR(200)
	private java.lang.String localita_spostamento;

	// PERCENTUALE_MAGGIORAZIONE DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale_maggiorazione;

	// TI_AUTO CHAR(1)
	private java.lang.String ti_auto;

	// TI_CD_TI_SPESA CHAR(1)
	private java.lang.String ti_cd_ti_spesa;

	// TI_SPESA_DIARIA CHAR(1) NOT NULL
	private java.lang.String ti_spesa_diaria;

	// IM_DIARIA_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rimborso;
	
    // ID_FOLDER_DETTAGLI_GEMIS VARCHAR(100)
    private String idFolderDettagliGemis;

public Missione_dettaglioBase() {
	super();
}
public Missione_dettaglioBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_inizio_tappa,java.lang.Integer esercizio,java.lang.Long pg_missione,java.lang.Long pg_riga) {
	super(cd_cds,cd_unita_organizzativa,dt_inizio_tappa,esercizio,pg_missione,pg_riga);
}
/* 
 * Getter dell'attributo cambio_spesa
 */
public java.math.BigDecimal getCambio_spesa() {
	return cambio_spesa;
}
/* 
 * Getter dell'attributo cd_divisa_spesa
 */
public java.lang.String getCd_divisa_spesa() {
	return cd_divisa_spesa;
}
/* 
 * Getter dell'attributo cd_ti_pasto
 */
public java.lang.String getCd_ti_pasto() {
	return cd_ti_pasto;
}
/* 
 * Getter dell'attributo cd_ti_spesa
 */
public java.lang.String getCd_ti_spesa() {
	return cd_ti_spesa;
}
/* 
 * Getter dell'attributo chilometri
 */
public java.math.BigDecimal getChilometri() {
	return chilometri;
}
/* 
 * Getter dell'attributo ds_giustificativo
 */
public java.lang.String getDs_giustificativo() {
	return ds_giustificativo;
}
/* 
 * Getter dell'attributo ds_no_giustificativo
 */
public java.lang.String getDs_no_giustificativo() {
	return ds_no_giustificativo;
}
/* 
 * Getter dell'attributo ds_spesa
 */
public java.lang.String getDs_spesa() {
	return ds_spesa;
}
/* 
 * Getter dell'attributo ds_ti_spesa
 */
public java.lang.String getDs_ti_spesa() {
	return ds_ti_spesa;
}
/* 
 * Getter dell'attributo fl_diaria_manuale
 */
public java.lang.Boolean getFl_diaria_manuale() {
	return fl_diaria_manuale;
}
/* 
 * Getter dell'attributo fl_spesa_anticipata
 */
public java.lang.Boolean getFl_spesa_anticipata() {
	return fl_spesa_anticipata;
}
/* 
 * Getter dell'attributo id_giustificativo
 */
public java.lang.String getId_giustificativo() {
	return id_giustificativo;
}
/* 
 * Getter dell'attributo im_base_maggiorazione
 */
public java.math.BigDecimal getIm_base_maggiorazione() {
	return im_base_maggiorazione;
}
/* 
 * Getter dell'attributo im_diaria_lorda
 */
public java.math.BigDecimal getIm_diaria_lorda() {
	return im_diaria_lorda;
}
/* 
 * Getter dell'attributo im_diaria_netto
 */
public java.math.BigDecimal getIm_diaria_netto() {
	return im_diaria_netto;
}
/* 
 * Getter dell'attributo im_maggiorazione
 */
public java.math.BigDecimal getIm_maggiorazione() {
	return im_maggiorazione;
}
/* 
 * Getter dell'attributo im_maggiorazione_euro
 */
public java.math.BigDecimal getIm_maggiorazione_euro() {
	return im_maggiorazione_euro;
}
/* 
 * Getter dell'attributo im_quota_esente
 */
public java.math.BigDecimal getIm_quota_esente() {
	return im_quota_esente;
}
/* 
 * Getter dell'attributo im_spesa_divisa
 */
public java.math.BigDecimal getIm_spesa_divisa() {
	return im_spesa_divisa;
}
/* 
 * Getter dell'attributo im_spesa_euro
 */
public java.math.BigDecimal getIm_spesa_euro() {
	return im_spesa_euro;
}
/* 
 * Getter dell'attributo im_spesa_max
 */
public java.math.BigDecimal getIm_spesa_max() {
	return im_spesa_max;
}
/* 
 * Getter dell'attributo im_spesa_max_divisa
 */
public java.math.BigDecimal getIm_spesa_max_divisa() {
	return im_spesa_max_divisa;
}
/* 
 * Getter dell'attributo im_totale_spesa
 */
public java.math.BigDecimal getIm_totale_spesa() {
	return im_totale_spesa;
}
/* 
 * Getter dell'attributo indennita_chilometrica
 */
public java.math.BigDecimal getIndennita_chilometrica() {
	return indennita_chilometrica;
}
/* 
 * Getter dell'attributo localita_spostamento
 */
public java.lang.String getLocalita_spostamento() {
	return localita_spostamento;
}
/* 
 * Getter dell'attributo percentuale_maggiorazione
 */
public java.math.BigDecimal getPercentuale_maggiorazione() {
	return percentuale_maggiorazione;
}
/* 
 * Getter dell'attributo ti_auto
 */
public java.lang.String getTi_auto() {
	return ti_auto;
}
/* 
 * Getter dell'attributo ti_cd_ti_spesa
 */
public java.lang.String getTi_cd_ti_spesa() {
	return ti_cd_ti_spesa;
}
/* 
 * Getter dell'attributo ti_spesa_diaria
 */
public java.lang.String getTi_spesa_diaria() {
	return ti_spesa_diaria;
}
/* 
 * Setter dell'attributo cambio_spesa
 */
public void setCambio_spesa(java.math.BigDecimal cambio_spesa) {
	this.cambio_spesa = cambio_spesa;
}
/* 
 * Setter dell'attributo cd_divisa_spesa
 */
public void setCd_divisa_spesa(java.lang.String cd_divisa_spesa) {
	this.cd_divisa_spesa = cd_divisa_spesa;
}
/* 
 * Setter dell'attributo cd_ti_pasto
 */
public void setCd_ti_pasto(java.lang.String cd_ti_pasto) {
	this.cd_ti_pasto = cd_ti_pasto;
}
/* 
 * Setter dell'attributo cd_ti_spesa
 */
public void setCd_ti_spesa(java.lang.String cd_ti_spesa) {
	this.cd_ti_spesa = cd_ti_spesa;
}
/* 
 * Setter dell'attributo chilometri
 */
public void setChilometri(java.math.BigDecimal chilometri) {
	this.chilometri = chilometri;
}
/* 
 * Setter dell'attributo ds_giustificativo
 */
public void setDs_giustificativo(java.lang.String ds_giustificativo) {
	this.ds_giustificativo = ds_giustificativo;
}
/* 
 * Setter dell'attributo ds_no_giustificativo
 */
public void setDs_no_giustificativo(java.lang.String ds_no_giustificativo) {
	this.ds_no_giustificativo = ds_no_giustificativo;
}
/* 
 * Setter dell'attributo ds_spesa
 */
public void setDs_spesa(java.lang.String ds_spesa) {
	this.ds_spesa = ds_spesa;
}
/* 
 * Setter dell'attributo ds_ti_spesa
 */
public void setDs_ti_spesa(java.lang.String ds_ti_spesa) {
	this.ds_ti_spesa = ds_ti_spesa;
}
/* 
 * Setter dell'attributo fl_diaria_manuale
 */
public void setFl_diaria_manuale(java.lang.Boolean fl_diaria_manuale) {
	this.fl_diaria_manuale = fl_diaria_manuale;
}
/* 
 * Setter dell'attributo fl_spesa_anticipata
 */
public void setFl_spesa_anticipata(java.lang.Boolean fl_spesa_anticipata) {
	this.fl_spesa_anticipata = fl_spesa_anticipata;
}
/* 
 * Setter dell'attributo id_giustificativo
 */
public void setId_giustificativo(java.lang.String id_giustificativo) {
	this.id_giustificativo = id_giustificativo;
}
/* 
 * Setter dell'attributo im_base_maggiorazione
 */
public void setIm_base_maggiorazione(java.math.BigDecimal im_base_maggiorazione) {
	this.im_base_maggiorazione = im_base_maggiorazione;
}
/* 
 * Setter dell'attributo im_diaria_lorda
 */
public void setIm_diaria_lorda(java.math.BigDecimal im_diaria_lorda) {
	this.im_diaria_lorda = im_diaria_lorda;
}
/* 
 * Setter dell'attributo im_diaria_netto
 */
public void setIm_diaria_netto(java.math.BigDecimal im_diaria_netto) {
	this.im_diaria_netto = im_diaria_netto;
}
/* 
 * Setter dell'attributo im_maggiorazione
 */
public void setIm_maggiorazione(java.math.BigDecimal im_maggiorazione) {
	this.im_maggiorazione = im_maggiorazione;
}
/* 
 * Setter dell'attributo im_maggiorazione_euro
 */
public void setIm_maggiorazione_euro(java.math.BigDecimal im_maggiorazione_euro) {
	this.im_maggiorazione_euro = im_maggiorazione_euro;
}
/* 
 * Setter dell'attributo im_quota_esente
 */
public void setIm_quota_esente(java.math.BigDecimal im_quota_esente) {
	this.im_quota_esente = im_quota_esente;
}
/* 
 * Setter dell'attributo im_spesa_divisa
 */
public void setIm_spesa_divisa(java.math.BigDecimal im_spesa_divisa) {
	this.im_spesa_divisa = im_spesa_divisa;
}
/* 
 * Setter dell'attributo im_spesa_euro
 */
public void setIm_spesa_euro(java.math.BigDecimal im_spesa_euro) {
	this.im_spesa_euro = im_spesa_euro;
}
/* 
 * Setter dell'attributo im_spesa_max
 */
public void setIm_spesa_max(java.math.BigDecimal im_spesa_max) {
	this.im_spesa_max = im_spesa_max;
}
/* 
 * Setter dell'attributo im_spesa_max_divisa
 */
public void setIm_spesa_max_divisa(java.math.BigDecimal im_spesa_max_divisa) {
	this.im_spesa_max_divisa = im_spesa_max_divisa;
}
/* 
 * Setter dell'attributo im_totale_spesa
 */
public void setIm_totale_spesa(java.math.BigDecimal im_totale_spesa) {
	this.im_totale_spesa = im_totale_spesa;
}
/* 
 * Setter dell'attributo indennita_chilometrica
 */
public void setIndennita_chilometrica(java.math.BigDecimal indennita_chilometrica) {
	this.indennita_chilometrica = indennita_chilometrica;
}
/* 
 * Setter dell'attributo localita_spostamento
 */
public void setLocalita_spostamento(java.lang.String localita_spostamento) {
	this.localita_spostamento = localita_spostamento;
}
/* 
 * Setter dell'attributo percentuale_maggiorazione
 */
public void setPercentuale_maggiorazione(java.math.BigDecimal percentuale_maggiorazione) {
	this.percentuale_maggiorazione = percentuale_maggiorazione;
}
/* 
 * Setter dell'attributo ti_auto
 */
public void setTi_auto(java.lang.String ti_auto) {
	this.ti_auto = ti_auto;
}
/* 
 * Setter dell'attributo ti_cd_ti_spesa
 */
public void setTi_cd_ti_spesa(java.lang.String ti_cd_ti_spesa) {
	this.ti_cd_ti_spesa = ti_cd_ti_spesa;
}
/* 
 * Setter dell'attributo ti_spesa_diaria
 */
public void setTi_spesa_diaria(java.lang.String ti_spesa_diaria) {
	this.ti_spesa_diaria = ti_spesa_diaria;
}
public java.math.BigDecimal getIm_rimborso() {
	return im_rimborso;
}
public void setIm_rimborso(java.math.BigDecimal im_rimborso) {
	this.im_rimborso = im_rimborso;
}
public String getIdFolderDettagliGemis() {
	return idFolderDettagliGemis;
}
public void setIdFolderDettagliGemis(String idFolderDettagliGemis) {
	this.idFolderDettagliGemis = idFolderDettagliGemis;
}
}