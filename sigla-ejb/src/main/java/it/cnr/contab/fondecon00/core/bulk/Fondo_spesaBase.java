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

package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_spesaBase extends Fondo_spesaKey implements Keyed {
	// CAP_FORNITORE VARCHAR(5)
	private java.lang.String cap_fornitore;

	// CD_CDS_DOC_AMM VARCHAR(30)
	private java.lang.String cd_cds_doc_amm;

	// CD_CDS_MANDATO VARCHAR(30)
	private java.lang.String cd_cds_mandato;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10)
	private java.lang.String cd_tipo_documento_amm;

	// CD_UO_DOC_AMM VARCHAR(30)
	private java.lang.String cd_uo_doc_amm;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// DENOMINAZIONE_FORNITORE VARCHAR(200)
	private java.lang.String denominazione_fornitore;

	// DS_FORNITORE VARCHAR(300)
	private java.lang.String ds_fornitore;

	// DS_SPESA VARCHAR(300)
	private java.lang.String ds_spesa;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// DT_SPESA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_spesa;

	// ESERCIZIO_DOC_AMM DECIMAL(4,0)
	private java.lang.Integer esercizio_doc_amm;

	// ESERCIZIO_MANDATO DECIMAL(4,0)
	private java.lang.Integer esercizio_mandato;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;

	// FL_DOCUMENTATA CHAR(1)
	private java.lang.Boolean fl_documentata;

	// FL_FORNITORE_SALTUARIO CHAR(1)
	private java.lang.Boolean fl_fornitore_saltuario;

	// FL_OBBLIGAZIONE CHAR(1)
	private java.lang.Boolean fl_obbligazione;

	// FL_REINTEGRATA CHAR(1)
	private java.lang.Boolean fl_reintegrata;

	// IM_AMMONTARE_SPESA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ammontare_spesa;

	// IM_NETTO_SPESA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_netto_spesa;

	// INDIRIZZO_FORNITORE VARCHAR(200)
	private java.lang.String indirizzo_fornitore;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_COMUNE DECIMAL(10,0)
	private java.lang.Long pg_comune;

	// PG_DOCUMENTO_AMM DECIMAL(10,0)
	private java.lang.Long pg_documento_amm;

	// PG_MANDATO DECIMAL(10,0)
	private java.lang.Long pg_mandato;

    // ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;

	// TEL_FORNITORE VARCHAR(20)
	private java.lang.String tel_fornitore;

public Fondo_spesaBase() {
	super();
}
public Fondo_spesaBase(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fondo_spesa) {
	super(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio,pg_fondo_spesa);
}
/* 
 * Getter dell'attributo cap_fornitore
 */
public java.lang.String getCap_fornitore() {
	return cap_fornitore;
}
/* 
 * Getter dell'attributo cd_cds_doc_amm
 */
public java.lang.String getCd_cds_doc_amm() {
	return cd_cds_doc_amm;
}
/* 
 * Getter dell'attributo cd_cds_mandato
 */
public java.lang.String getCd_cds_mandato() {
	return cd_cds_mandato;
}
/* 
 * Getter dell'attributo cd_cds_obbligazione
 */
public java.lang.String getCd_cds_obbligazione() {
	return cd_cds_obbligazione;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
/* 
 * Getter dell'attributo cd_uo_doc_amm
 */
public java.lang.String getCd_uo_doc_amm() {
	return cd_uo_doc_amm;
}
/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/* 
 * Getter dell'attributo denominazione_fornitore
 */
public java.lang.String getDenominazione_fornitore() {
	return denominazione_fornitore;
}
/* 
 * Getter dell'attributo ds_fornitore
 */
public java.lang.String getDs_fornitore() {
	return ds_fornitore;
}
/* 
 * Getter dell'attributo ds_spesa
 */
public java.lang.String getDs_spesa() {
	return ds_spesa;
}
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge() {
	return dt_a_competenza_coge;
}
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge() {
	return dt_da_competenza_coge;
}
/* 
 * Getter dell'attributo dt_spesa
 */
public java.sql.Timestamp getDt_spesa() {
	return dt_spesa;
}
/* 
 * Getter dell'attributo esercizio_doc_amm
 */
public java.lang.Integer getEsercizio_doc_amm() {
	return esercizio_doc_amm;
}
/* 
 * Getter dell'attributo esercizio_mandato
 */
public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}
/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/* 
 * Getter dell'attributo fl_documentata
 */
public java.lang.Boolean getFl_documentata() {
	return fl_documentata;
}
/* 
 * Getter dell'attributo fl_fornitore_saltuario
 */
public java.lang.Boolean getFl_fornitore_saltuario() {
	return fl_fornitore_saltuario;
}
/* 
 * Getter dell'attributo fl_obbligazione
 */
public java.lang.Boolean getFl_obbligazione() {
	return fl_obbligazione;
}
/* 
 * Getter dell'attributo fl_reintegrata
 */
public java.lang.Boolean getFl_reintegrata() {
	return fl_reintegrata;
}
/* 
 * Getter dell'attributo im_ammontare_spesa
 */
public java.math.BigDecimal getIm_ammontare_spesa() {
	return im_ammontare_spesa;
}
/* 
 * Getter dell'attributo im_netto_spesa
 */
public java.math.BigDecimal getIm_netto_spesa() {
	return im_netto_spesa;
}
/* 
 * Getter dell'attributo indirizzo_fornitore
 */
public java.lang.String getIndirizzo_fornitore() {
	return indirizzo_fornitore;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo pg_comune
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
/* 
 * Getter dell'attributo pg_documento_amm
 */
public java.lang.Long getPg_documento_amm() {
	return pg_documento_amm;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
/* 
 * Getter dell'attributo tel_fornitore
 */
public java.lang.String getTel_fornitore() {
	return tel_fornitore;
}
/* 
 * Setter dell'attributo cap_fornitore
 */
public void setCap_fornitore(java.lang.String cap_fornitore) {
	this.cap_fornitore = cap_fornitore;
}
/* 
 * Setter dell'attributo cd_cds_doc_amm
 */
public void setCd_cds_doc_amm(java.lang.String cd_cds_doc_amm) {
	this.cd_cds_doc_amm = cd_cds_doc_amm;
}
/* 
 * Setter dell'attributo cd_cds_mandato
 */
public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.cd_cds_mandato = cd_cds_mandato;
}
/* 
 * Setter dell'attributo cd_cds_obbligazione
 */
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.cd_cds_obbligazione = cd_cds_obbligazione;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}
/* 
 * Setter dell'attributo cd_uo_doc_amm
 */
public void setCd_uo_doc_amm(java.lang.String cd_uo_doc_amm) {
	this.cd_uo_doc_amm = cd_uo_doc_amm;
}
/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}
/* 
 * Setter dell'attributo denominazione_fornitore
 */
public void setDenominazione_fornitore(java.lang.String denominazione_fornitore) {
	this.denominazione_fornitore = denominazione_fornitore;
}
/* 
 * Setter dell'attributo ds_fornitore
 */
public void setDs_fornitore(java.lang.String ds_fornitore) {
	this.ds_fornitore = ds_fornitore;
}
/* 
 * Setter dell'attributo ds_spesa
 */
public void setDs_spesa(java.lang.String ds_spesa) {
	this.ds_spesa = ds_spesa;
}
/* 
 * Setter dell'attributo dt_a_competenza_coge
 */
public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
	this.dt_a_competenza_coge = dt_a_competenza_coge;
}
/* 
 * Setter dell'attributo dt_da_competenza_coge
 */
public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
	this.dt_da_competenza_coge = dt_da_competenza_coge;
}
/* 
 * Setter dell'attributo dt_spesa
 */
public void setDt_spesa(java.sql.Timestamp dt_spesa) {
	this.dt_spesa = dt_spesa;
}
/* 
 * Setter dell'attributo esercizio_doc_amm
 */
public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm) {
	this.esercizio_doc_amm = esercizio_doc_amm;
}
/* 
 * Setter dell'attributo esercizio_mandato
 */
public void setEsercizio_mandato(java.lang.Integer esercizio_mandato) {
	this.esercizio_mandato = esercizio_mandato;
}
/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}
/* 
 * Setter dell'attributo fl_documentata
 */
public void setFl_documentata(java.lang.Boolean fl_documentata) {
	this.fl_documentata = fl_documentata;
}
/* 
 * Setter dell'attributo fl_fornitore_saltuario
 */
public void setFl_fornitore_saltuario(java.lang.Boolean fl_fornitore_saltuario) {
	this.fl_fornitore_saltuario = fl_fornitore_saltuario;
}
/* 
 * Setter dell'attributo fl_obbligazione
 */
public void setFl_obbligazione(java.lang.Boolean fl_obbligazione) {
	this.fl_obbligazione = fl_obbligazione;
}
/* 
 * Setter dell'attributo fl_reintegrata
 */
public void setFl_reintegrata(java.lang.Boolean fl_reintegrata) {
	this.fl_reintegrata = fl_reintegrata;
}
/* 
 * Setter dell'attributo im_ammontare_spesa
 */
public void setIm_ammontare_spesa(java.math.BigDecimal im_ammontare_spesa) {
	this.im_ammontare_spesa = im_ammontare_spesa;
}
/* 
 * Setter dell'attributo im_netto_spesa
 */
public void setIm_netto_spesa(java.math.BigDecimal im_netto_spesa) {
	this.im_netto_spesa = im_netto_spesa;
}
/* 
 * Setter dell'attributo indirizzo_fornitore
 */
public void setIndirizzo_fornitore(java.lang.String indirizzo_fornitore) {
	this.indirizzo_fornitore = indirizzo_fornitore;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo pg_comune
 */
public void setPg_comune(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
/* 
 * Setter dell'attributo pg_documento_amm
 */
public void setPg_documento_amm(java.lang.Long pg_documento_amm) {
	this.pg_documento_amm = pg_documento_amm;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
/* 
 * Setter dell'attributo tel_fornitore
 */
public void setTel_fornitore(java.lang.String tel_fornitore) {
	this.tel_fornitore = tel_fornitore;
}
}
