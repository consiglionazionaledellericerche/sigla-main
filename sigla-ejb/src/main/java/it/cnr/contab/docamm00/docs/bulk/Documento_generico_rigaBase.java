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
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Documento_generico_rigaBase extends Documento_generico_rigaKey implements Keyed {
	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cd_modalita_pag;

	// CD_MODALITA_PAG_UO_CDS VARCHAR(10)
	private java.lang.String cd_modalita_pag_uo_cds;

	// CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cd_termini_pag;

	// CD_TERMINI_PAG_UO_CDS VARCHAR(10)
	private java.lang.String cd_termini_pag_uo_cds;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_CESSIONARIO DECIMAL(8,0)
	private java.lang.Integer cd_terzo_cessionario;

	// CD_TERZO_UO_CDS DECIMAL(8,0)
	private java.lang.Integer cd_terzo_uo_cds;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DS_RIGA VARCHAR(200)
	private java.lang.String ds_riga;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;

	// IM_RIGA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_riga;

	// IM_RIGA_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_riga_divisa;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NOTE VARCHAR(300)
	private java.lang.String note;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_accertamento_scadenzario;

	// PG_BANCA DECIMAL(10,0)
	private java.lang.Long pg_banca;

	// PG_BANCA_UO_CDS DECIMAL(10,0)
	private java.lang.Long pg_banca_uo_cds;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;
	
	// PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

	public Documento_generico_rigaBase() {
	super();
}

public Documento_generico_rigaBase(java.lang.String cd_cds,java.lang.String cd_tipo_documento_amm,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_documento_generico,java.lang.Long progressivo_riga) {
	super(cd_cds,cd_tipo_documento_amm,cd_unita_organizzativa,esercizio,pg_documento_generico,progressivo_riga);
}

/* 
 * Getter dell'attributo cd_cds_accertamento
 */
public java.lang.String getCd_cds_accertamento() {
	return cd_cds_accertamento;
}

/* 
 * Getter dell'attributo cd_cds_obbligazione
 */
public java.lang.String getCd_cds_obbligazione() {
	return cd_cds_obbligazione;
}

/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}

/* 
 * Getter dell'attributo cd_modalita_pag_uo_cds
 */
public java.lang.String getCd_modalita_pag_uo_cds() {
	return cd_modalita_pag_uo_cds;
}

/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}

/* 
 * Getter dell'attributo cd_termini_pag_uo_cds
 */
public java.lang.String getCd_termini_pag_uo_cds() {
	return cd_termini_pag_uo_cds;
}

/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}

/* 
 * Getter dell'attributo cd_terzo_cessionario
 */
public java.lang.Integer getCd_terzo_cessionario() {
	return cd_terzo_cessionario;
}

/* 
 * Getter dell'attributo cd_terzo_uo_cds
 */
public java.lang.Integer getCd_terzo_uo_cds() {
	return cd_terzo_uo_cds;
}

/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}

/* 
 * Getter dell'attributo cognome
 */
public java.lang.String getCognome() {
	return cognome;
}

/* 
 * Getter dell'attributo ds_riga
 */
public java.lang.String getDs_riga() {
	return ds_riga;
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
 * Getter dell'attributo esercizio_accertamento
 */
public java.lang.Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
}

/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}

/* 
 * Getter dell'attributo im_riga
 */
public java.math.BigDecimal getIm_riga() {
	return im_riga;
}

/* 
 * Getter dell'attributo im_riga_divisa
 */
public java.math.BigDecimal getIm_riga_divisa() {
	return im_riga_divisa;
}

/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}

/* 
 * Getter dell'attributo note
 */
public java.lang.String getNote() {
	return note;
}

/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
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
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}

/* 
 * Getter dell'attributo pg_banca_uo_cds
 */
public java.lang.Long getPg_banca_uo_cds() {
	return pg_banca_uo_cds;
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
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}

/* 
 * Getter dell'attributo stato_cofi
 */
public java.lang.String getStato_cofi() {
	return stato_cofi;
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
 * Setter dell'attributo cd_cds_obbligazione
 */
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.cd_cds_obbligazione = cd_cds_obbligazione;
}

/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}

/* 
 * Setter dell'attributo cd_modalita_pag_uo_cds
 */
public void setCd_modalita_pag_uo_cds(java.lang.String cd_modalita_pag_uo_cds) {
	this.cd_modalita_pag_uo_cds = cd_modalita_pag_uo_cds;
}

/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}

/* 
 * Setter dell'attributo cd_termini_pag_uo_cds
 */
public void setCd_termini_pag_uo_cds(java.lang.String cd_termini_pag_uo_cds) {
	this.cd_termini_pag_uo_cds = cd_termini_pag_uo_cds;
}

/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}

/* 
 * Setter dell'attributo cd_terzo_cessionario
 */
public void setCd_terzo_cessionario(java.lang.Integer cd_terzo_cessionario) {
	this.cd_terzo_cessionario = cd_terzo_cessionario;
}

/* 
 * Setter dell'attributo cd_terzo_uo_cds
 */
public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
	this.cd_terzo_uo_cds = cd_terzo_uo_cds;
}

/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}

/* 
 * Setter dell'attributo cognome
 */
public void setCognome(java.lang.String cognome) {
	this.cognome = cognome;
}

/* 
 * Setter dell'attributo ds_riga
 */
public void setDs_riga(java.lang.String ds_riga) {
	this.ds_riga = ds_riga;
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
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
}

/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}

/* 
 * Setter dell'attributo im_riga
 */
public void setIm_riga(java.math.BigDecimal im_riga) {
	this.im_riga = im_riga;
}

/* 
 * Setter dell'attributo im_riga_divisa
 */
public void setIm_riga_divisa(java.math.BigDecimal im_riga_divisa) {
	this.im_riga_divisa = im_riga_divisa;
}

/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}

/* 
 * Setter dell'attributo note
 */
public void setNote(java.lang.String note) {
	this.note = note;
}

/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
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
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}

/* 
 * Setter dell'attributo pg_banca_uo_cds
 */
public void setPg_banca_uo_cds(java.lang.Long pg_banca_uo_cds) {
	this.pg_banca_uo_cds = pg_banca_uo_cds;
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
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}

/* 
 * Setter dell'attributo stato_cofi
 */
public void setStato_cofi(java.lang.String stato_cofi) {
	this.stato_cofi = stato_cofi;
}

/* 
 * Setter dell'attributo ti_associato_manrev
 */
public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
	this.ti_associato_manrev = ti_associato_manrev;
}

}
