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

package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.persistency.*;

public class OrdineBase extends OrdineKey implements Keyed {
	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_TERMINI_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_termini_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TIPO_CONSEGNA VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_consegna;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// IM_OBBLIGAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbligazione;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NOTE VARCHAR(300)
	private java.lang.String note;

	// NUMERO_CIVICO_SEDE VARCHAR(5)
	private java.lang.String numero_civico_sede;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4) NOT NULL
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// STATO VARCHAR(1)
	private java.lang.String stato;

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

	// VIA_SEDE VARCHAR(100) NOT NULL
	private java.lang.String via_sede;

	// CLAUSOLA VARCHAR(3000)  NULL
	private java.lang.String clausola;
	
public OrdineBase() {
	super();
}
public OrdineBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_ordine) {
	super(cd_cds,esercizio,pg_ordine);
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_consegna
 */
public java.lang.String getCd_tipo_consegna() {
	return cd_tipo_consegna;
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
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo im_obbligazione
 */
public java.math.BigDecimal getIm_obbligazione() {
	return im_obbligazione;
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
 * Getter dell'attributo numero_civico_sede
 */
public java.lang.String getNumero_civico_sede() {
	return numero_civico_sede;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
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
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Getter dell'attributo via_sede
 */
public java.lang.String getVia_sede() {
	return via_sede;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_consegna
 */
public void setCd_tipo_consegna(java.lang.String cd_tipo_consegna) {
	this.cd_tipo_consegna = cd_tipo_consegna;
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
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo im_obbligazione
 */
public void setIm_obbligazione(java.math.BigDecimal im_obbligazione) {
	this.im_obbligazione = im_obbligazione;
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
 * Setter dell'attributo numero_civico_sede
 */
public void setNumero_civico_sede(java.lang.String numero_civico_sede) {
	this.numero_civico_sede = numero_civico_sede;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
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
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
/* 
 * Setter dell'attributo via_sede
 */
public void setVia_sede(java.lang.String via_sede) {
	this.via_sede = via_sede;
}
public java.lang.String getClausola() {
	return clausola;
}
public void setClausola(java.lang.String clausola) {
	this.clausola = clausola;
}
}
