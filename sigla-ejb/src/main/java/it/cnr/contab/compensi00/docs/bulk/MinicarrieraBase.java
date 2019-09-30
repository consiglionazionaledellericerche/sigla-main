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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class MinicarrieraBase extends MinicarrieraKey implements Keyed {
	// ALIQUOTA_IRPEF_MEDIA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota_irpef_media;

	// CD_CDS_MINICARRIERA_ORI VARCHAR(30)
	private java.lang.String cd_cds_minicarriera_ori;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cd_termini_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

	// CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private java.lang.String cd_trattamento;

	// CD_UO_MINICARRIERA_ORI VARCHAR(30)
	private java.lang.String cd_uo_minicarriera_ori;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DS_MINICARRIERA VARCHAR(300)
	private java.lang.String ds_minicarriera;

	// DT_CESSAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cessazione;

	// DT_FINE_MINICARRIERA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_minicarriera;

	// DT_INIZIO_MINICARRIERA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_inizio_minicarriera;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// DT_RINNOVO TIMESTAMP
	private java.sql.Timestamp dt_rinnovo;

	// DT_RIPRISTINO TIMESTAMP
	private java.sql.Timestamp dt_ripristino;

	// DT_SOSPENSIONE TIMESTAMP
	private java.sql.Timestamp dt_sospensione;

	// ESERCIZIO_MINICARRIERA_ORI DECIMAL(4,0)
	private java.lang.Integer esercizio_minicarriera_ori;

	// FL_ESCLUDI_QVARIA_DEDUZIONE CHAR(1) NOT NULL
	private java.lang.Boolean fl_escludi_qvaria_deduzione;

	// FL_TASSAZIONE_SEPARATA CHAR(1) NOT NULL
	private java.lang.Boolean fl_tassazione_separata;

	// IMPONIBILE_IRPEF_ESEPREC1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_irpef_eseprec1;

	// IMPONIBILE_IRPEF_ESEPREC2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_irpef_eseprec2;

	// IM_TOTALE_MINICARRIERA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_minicarriera;

	// MESI_ANTICIPO_POSTICIPO DECIMAL(3,0) NOT NULL
	private java.lang.Integer mesi_anticipo_posticipo;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NUMERO_RATE DECIMAL(3,0) NOT NULL
	private java.lang.Integer numero_rate;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// PG_MINICARRIERA_ORI DECIMAL(10,0)
	private java.lang.Long pg_minicarriera_ori;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// STATO_ASS_COMPENSO CHAR(1) NOT NULL
	private java.lang.String stato_ass_compenso;

	// TI_ANAGRAFICO CHAR(1) NOT NULL
	private java.lang.String ti_anagrafico;

	// TI_ANTICIPO_POSTICIPO CHAR(1) NOT NULL
	private java.lang.String ti_anticipo_posticipo;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;
	
	// ESERCIZIO_REP DECIMAL(4,0)
	private java.lang.Integer esercizio_rep;

	// PG_REPERTORIO DECIMAL(10,0)
	private java.lang.Long pg_repertorio;
	
	// TI_PRESTAZIONE CHAR(1) NULL
	private java.lang.String ti_prestazione;
	
public MinicarrieraBase() {
	super();
}
public MinicarrieraBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_minicarriera) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_minicarriera);
}
/* 
 * Getter dell'attributo aliquota_irpef_media
 */
public java.math.BigDecimal getAliquota_irpef_media() {
	return aliquota_irpef_media;
}
/* 
 * Getter dell'attributo cd_cds_minicarriera_ori
 */
public java.lang.String getCd_cds_minicarriera_ori() {
	return cd_cds_minicarriera_ori;
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
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
}
/* 
 * Getter dell'attributo cd_trattamento
 */
public java.lang.String getCd_trattamento() {
	return cd_trattamento;
}
/* 
 * Getter dell'attributo cd_uo_minicarriera_ori
 */
public java.lang.String getCd_uo_minicarriera_ori() {
	return cd_uo_minicarriera_ori;
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
 * Getter dell'attributo ds_minicarriera
 */
public java.lang.String getDs_minicarriera() {
	return ds_minicarriera;
}
/* 
 * Getter dell'attributo dt_cessazione
 */
public java.sql.Timestamp getDt_cessazione() {
	return dt_cessazione;
}
/* 
 * Getter dell'attributo dt_fine_minicarriera
 */
public java.sql.Timestamp getDt_fine_minicarriera() {
	return dt_fine_minicarriera;
}
/* 
 * Getter dell'attributo dt_inizio_minicarriera
 */
public java.sql.Timestamp getDt_inizio_minicarriera() {
	return dt_inizio_minicarriera;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo dt_rinnovo
 */
public java.sql.Timestamp getDt_rinnovo() {
	return dt_rinnovo;
}
/* 
 * Getter dell'attributo dt_ripristino
 */
public java.sql.Timestamp getDt_ripristino() {
	return dt_ripristino;
}
/* 
 * Getter dell'attributo dt_sospensione
 */
public java.sql.Timestamp getDt_sospensione() {
	return dt_sospensione;
}
/* 
 * Getter dell'attributo esercizio_minicarriera_ori
 */
public java.lang.Integer getEsercizio_minicarriera_ori() {
	return esercizio_minicarriera_ori;
}
/* 
 * Getter dell'attributo fl_escludi_qvaria_deduzione
 */
public java.lang.Boolean getFl_escludi_qvaria_deduzione() {
	return fl_escludi_qvaria_deduzione;
}
/* 
 * Getter dell'attributo fl_tassazione_separata
 */
public java.lang.Boolean getFl_tassazione_separata() {
	return fl_tassazione_separata;
}
/* 
 * Getter dell'attributo im_totale_minicarriera
 */
public java.math.BigDecimal getIm_totale_minicarriera() {
	return im_totale_minicarriera;
}
/* 
 * Getter dell'attributo imponibile_irpef_eseprec1
 */
public java.math.BigDecimal getImponibile_irpef_eseprec1() {
	return imponibile_irpef_eseprec1;
}
/* 
 * Getter dell'attributo imponibile_irpef_eseprec2
 */
public java.math.BigDecimal getImponibile_irpef_eseprec2() {
	return imponibile_irpef_eseprec2;
}
/* 
 * Getter dell'attributo mesi_anticipo_posticipo
 */
public java.lang.Integer getMesi_anticipo_posticipo() {
	return mesi_anticipo_posticipo;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo numero_rate
 */
public java.lang.Integer getNumero_rate() {
	return numero_rate;
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
 * Getter dell'attributo pg_minicarriera_ori
 */
public java.lang.Long getPg_minicarriera_ori() {
	return pg_minicarriera_ori;
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
 * Getter dell'attributo stato_ass_compenso
 */
public java.lang.String getStato_ass_compenso() {
	return stato_ass_compenso;
}
/* 
 * Getter dell'attributo ti_anagrafico
 */
public java.lang.String getTi_anagrafico() {
	return ti_anagrafico;
}
/* 
 * Getter dell'attributo ti_anticipo_posticipo
 */
public java.lang.String getTi_anticipo_posticipo() {
	return ti_anticipo_posticipo;
}
/* 
 * Getter dell'attributo ti_istituz_commerc
 */
public java.lang.String getTi_istituz_commerc() {
	return ti_istituz_commerc;
}
/* 
 * Setter dell'attributo aliquota_irpef_media
 */
public void setAliquota_irpef_media(java.math.BigDecimal aliquota_irpef_media) {
	this.aliquota_irpef_media = aliquota_irpef_media;
}
/* 
 * Setter dell'attributo cd_cds_minicarriera_ori
 */
public void setCd_cds_minicarriera_ori(java.lang.String cd_cds_minicarriera_ori) {
	this.cd_cds_minicarriera_ori = cd_cds_minicarriera_ori;
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
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
}
/* 
 * Setter dell'attributo cd_trattamento
 */
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
/* 
 * Setter dell'attributo cd_uo_minicarriera_ori
 */
public void setCd_uo_minicarriera_ori(java.lang.String cd_uo_minicarriera_ori) {
	this.cd_uo_minicarriera_ori = cd_uo_minicarriera_ori;
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
 * Setter dell'attributo ds_minicarriera
 */
public void setDs_minicarriera(java.lang.String ds_minicarriera) {
	this.ds_minicarriera = ds_minicarriera;
}
/* 
 * Setter dell'attributo dt_cessazione
 */
public void setDt_cessazione(java.sql.Timestamp dt_cessazione) {
	this.dt_cessazione = dt_cessazione;
}
/* 
 * Setter dell'attributo dt_fine_minicarriera
 */
public void setDt_fine_minicarriera(java.sql.Timestamp dt_fine_minicarriera) {
	this.dt_fine_minicarriera = dt_fine_minicarriera;
}
/* 
 * Setter dell'attributo dt_inizio_minicarriera
 */
public void setDt_inizio_minicarriera(java.sql.Timestamp dt_inizio_minicarriera) {
	this.dt_inizio_minicarriera = dt_inizio_minicarriera;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo dt_rinnovo
 */
public void setDt_rinnovo(java.sql.Timestamp dt_rinnovo) {
	this.dt_rinnovo = dt_rinnovo;
}
/* 
 * Setter dell'attributo dt_ripristino
 */
public void setDt_ripristino(java.sql.Timestamp dt_ripristino) {
	this.dt_ripristino = dt_ripristino;
}
/* 
 * Setter dell'attributo dt_sospensione
 */
public void setDt_sospensione(java.sql.Timestamp dt_sospensione) {
	this.dt_sospensione = dt_sospensione;
}
/* 
 * Setter dell'attributo esercizio_minicarriera_ori
 */
public void setEsercizio_minicarriera_ori(java.lang.Integer esercizio_minicarriera_ori) {
	this.esercizio_minicarriera_ori = esercizio_minicarriera_ori;
}
/* 
 * Setter dell'attributo fl_escludi_qvaria_deduzione
 */
public void setFl_escludi_qvaria_deduzione(java.lang.Boolean fl_escludi_qvaria_deduzione) {
	this.fl_escludi_qvaria_deduzione = fl_escludi_qvaria_deduzione;
}
/* 
 * Setter dell'attributo fl_tassazione_separata
 */
public void setFl_tassazione_separata(java.lang.Boolean fl_tassazione_separata) {
	this.fl_tassazione_separata = fl_tassazione_separata;
}
/* 
 * Setter dell'attributo im_totale_minicarriera
 */
public void setIm_totale_minicarriera(java.math.BigDecimal im_totale_minicarriera) {
	this.im_totale_minicarriera = im_totale_minicarriera;
}
/* 
 * Setter dell'attributo imponibile_irpef_eseprec1
 */
public void setImponibile_irpef_eseprec1(java.math.BigDecimal imponibile_irpef_eseprec1) {
	this.imponibile_irpef_eseprec1 = imponibile_irpef_eseprec1;
}
/* 
 * Setter dell'attributo imponibile_irpef_eseprec2
 */
public void setImponibile_irpef_eseprec2(java.math.BigDecimal imponibile_irpef_eseprec2) {
	this.imponibile_irpef_eseprec2 = imponibile_irpef_eseprec2;
}
/* 
 * Setter dell'attributo mesi_anticipo_posticipo
 */
public void setMesi_anticipo_posticipo(java.lang.Integer mesi_anticipo_posticipo) {
	this.mesi_anticipo_posticipo = mesi_anticipo_posticipo;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo numero_rate
 */
public void setNumero_rate(java.lang.Integer numero_rate) {
	this.numero_rate = numero_rate;
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
 * Setter dell'attributo pg_minicarriera_ori
 */
public void setPg_minicarriera_ori(java.lang.Long pg_minicarriera_ori) {
	this.pg_minicarriera_ori = pg_minicarriera_ori;
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
 * Setter dell'attributo stato_ass_compenso
 */
public void setStato_ass_compenso(java.lang.String stato_ass_compenso) {
	this.stato_ass_compenso = stato_ass_compenso;
}
/* 
 * Setter dell'attributo ti_anagrafico
 */
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	this.ti_anagrafico = ti_anagrafico;
}
/* 
 * Setter dell'attributo ti_anticipo_posticipo
 */
public void setTi_anticipo_posticipo(java.lang.String ti_anticipo_posticipo) {
	this.ti_anticipo_posticipo = ti_anticipo_posticipo;
}
/* 
 * Setter dell'attributo ti_istituz_commerc
 */
public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
	this.ti_istituz_commerc = ti_istituz_commerc;
}
public java.lang.Integer getEsercizio_rep() {
	return esercizio_rep;
}
public void setEsercizio_rep(java.lang.Integer esercizio_rep) {
	this.esercizio_rep = esercizio_rep;
}
public java.lang.Long getPg_repertorio() {
	return pg_repertorio;
}
public void setPg_repertorio(java.lang.Long pg_repertorio) {
	this.pg_repertorio = pg_repertorio;
}
public java.lang.String getTi_prestazione() {
	return ti_prestazione;
}
public void setTi_prestazione(java.lang.String ti_prestazione) {
	this.ti_prestazione = ti_prestazione;
}
}
