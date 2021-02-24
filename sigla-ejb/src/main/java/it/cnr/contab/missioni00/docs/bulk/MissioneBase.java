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

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.persistency.Keyed;

public class MissioneBase extends MissioneKey implements Keyed {
	// CD_CDS_ANTICIPO VARCHAR(30)
	@JsonIgnore
	private java.lang.String cd_cds_anticipo;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	@JsonIgnore
	private java.lang.String cd_cds_obbligazione;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	@JsonIgnore
	private java.lang.String cd_modalita_pag;

	// CD_TERMINI_PAG VARCHAR(10)
	@JsonIgnore
	private java.lang.String cd_termini_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TIPO_MISSIONE VARCHAR(10)
	@JsonIgnore
	private java.lang.String cd_tipo_missione;

	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	@JsonIgnore
	private java.lang.String cd_tipo_rapporto;

	// CD_TRATTAMENTO VARCHAR(10)
	@JsonIgnore
	private java.lang.String cd_trattamento;

	// CD_UO_ANTICIPO VARCHAR(30)
	@JsonIgnore
	private java.lang.String cd_uo_anticipo;

	// CODICE_FISCALE VARCHAR(20)
	@StoragePolicy(name="P:emp:cf", property=@StorageProperty(name="emp:codice"))
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DS_MISSIONE VARCHAR(300) NOT NULL
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:description"))
	@StorageProperty(name="emppay:descDoc")
	private java.lang.String ds_missione;

	// DT_CANCELLAZIONE TIMESTAMP
	@JsonIgnore
	private java.sql.Timestamp dt_cancellazione;

	// DT_EMISSIONE_MANDATO TIMESTAMP
	@JsonIgnore
	private java.sql.Timestamp dt_emissione_mandato;

	// DT_FINE_MISSIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_missione;

	// DT_INIZIO_MISSIONE TIMESTAMP NOT NULL
	@StorageProperty(name="emppay:datDoc",converterBeanName="cmis.converter.timestampToCalendarConverter")
	private java.sql.Timestamp dt_inizio_missione;

	// DT_PAGAMENTO_FONDO_ECO TIMESTAMP
	@JsonIgnore
	private java.sql.Timestamp dt_pagamento_fondo_eco;

	// DT_PAGAMENTO_MANDATO TIMESTAMP
	@JsonIgnore
	private java.sql.Timestamp dt_pagamento_mandato;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	@JsonIgnore
	private java.sql.Timestamp dt_registrazione;

	// DT_TRASMISSIONE_MANDATO TIMESTAMP
	@JsonIgnore
	private java.sql.Timestamp dt_trasmissione_mandato;

	// ESERCIZIO_ANTICIPO DECIMAL(4,0)
	@JsonIgnore
	private java.lang.Integer esercizio_anticipo;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	@JsonIgnore
	private java.lang.Integer esercizio_obbligazione;

	// FL_ASSOCIATO_COMPENSO CHAR(1) NOT NULL
	private java.lang.Boolean fl_associato_compenso;

	// FL_COMUNE_ALTRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_comune_altro;

	// FL_COMUNE_ESTERO CHAR(1) NOT NULL
	private java.lang.Boolean fl_comune_estero;

	// FL_COMUNE_PROPRIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_comune_proprio;

	// IM_DIARIA_LORDA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_diaria_lorda;

	// IM_DIARIA_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_diaria_netto;

	// IM_LORDO_PERCEPIENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_lordo_percepiente;

	// IM_NETTO_PECEPIENTE DECIMAL(15,2) NOT NULL
	@StorageProperty(name="emppay:impNetto")
	private java.math.BigDecimal im_netto_pecepiente;

	// IM_QUOTA_ESENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_quota_esente;

	// IM_SPESE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese;

	// IM_SPESE_ANTICIPATE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_anticipate;

	// IM_TOTALE_MISSIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_missione;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_ANTICIPO DECIMAL(10,0)
	@JsonIgnore
	private java.lang.Long pg_anticipo;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	@JsonIgnore
	private java.lang.Long pg_banca;

    // ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	@JsonIgnore
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0)
	@JsonIgnore
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	@JsonIgnore
	private java.lang.Long pg_obbligazione_scadenzario;

	// PG_RIF_INQUADRAMENTO DECIMAL(10,0) NOT NULL
	@JsonIgnore
	private java.lang.Long pg_rif_inquadramento;

	// RAGIONE_SOCIALE VARCHAR(100)
	@JsonIgnore
	private java.lang.String ragione_sociale;

	// STATO_COAN CHAR(1) NOT NULL
	private java.lang.String stato_coan;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// STATO_PAGAMENTO_FONDO_ECO CHAR(1) NOT NULL
	private java.lang.String stato_pagamento_fondo_eco;

	// TI_ANAGRAFICO CHAR(1) NOT NULL
	private java.lang.String ti_anagrafico;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;

	// TI_PROVVISORIO_DEFINITIVO CHAR(1) NOT NULL
	private java.lang.String ti_provvisorio_definitivo;
	
	// IM_RIMBORSO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rimborso;

	private java.lang.String stato_liquidazione;
	
	// ID_RIMBORSO_MISSIONE DECIMAL(16,0)
    private Long idRimborsoMissione;

	// ID_FLUSSO VARCHAR(100)
    private String idFlusso;

    // ID_FLUSSO_ORDINE_MISSIONE VARCHAR(100)
    private String idFlussoOrdineMissione;

    // ID_FOLDER_RIMBORSO_MISSIONE VARCHAR(100)
    private String idFolderRimborsoMissione;

    // ID_FOLDER_ORDINE_MISSIONE VARCHAR(100)
    private String idFolderOrdineMissione;
	
	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	@JsonIgnore
	private java.lang.Boolean daRimborsoDaCompletare;

public MissioneBase() {
	super();
}
public MissioneBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_missione) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_missione);
}
/* 
 * Getter dell'attributo cd_cds_anticipo
 */
public java.lang.String getCd_cds_anticipo() {
	return cd_cds_anticipo;
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
 * Getter dell'attributo cd_tipo_missione
 */
public java.lang.String getCd_tipo_missione() {
	return cd_tipo_missione;
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
 * Getter dell'attributo cd_uo_anticipo
 */
public java.lang.String getCd_uo_anticipo() {
	return cd_uo_anticipo;
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
 * Getter dell'attributo ds_missione
 */
public java.lang.String getDs_missione() {
	return ds_missione;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_emissione_mandato
 */
public java.sql.Timestamp getDt_emissione_mandato() {
	return dt_emissione_mandato;
}
/* 
 * Getter dell'attributo dt_fine_missione
 */
public java.sql.Timestamp getDt_fine_missione() {
	return dt_fine_missione;
}
/* 
 * Getter dell'attributo dt_inizio_missione
 */
public java.sql.Timestamp getDt_inizio_missione() {
	return dt_inizio_missione;
}
/* 
 * Getter dell'attributo dt_pagamento_fondo_eco
 */
public java.sql.Timestamp getDt_pagamento_fondo_eco() {
	return dt_pagamento_fondo_eco;
}
/* 
 * Getter dell'attributo dt_pagamento_mandato
 */
public java.sql.Timestamp getDt_pagamento_mandato() {
	return dt_pagamento_mandato;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo dt_trasmissione_mandato
 */
public java.sql.Timestamp getDt_trasmissione_mandato() {
	return dt_trasmissione_mandato;
}
/* 
 * Getter dell'attributo esercizio_anticipo
 */
public java.lang.Integer getEsercizio_anticipo() {
	return esercizio_anticipo;
}
/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/* 
 * Getter dell'attributo fl_associato_compenso
 */
public java.lang.Boolean getFl_associato_compenso() {
	return fl_associato_compenso;
}
/* 
 * Getter dell'attributo fl_comune_altro
 */
public java.lang.Boolean getFl_comune_altro() {
	return fl_comune_altro;
}
/* 
 * Getter dell'attributo fl_comune_estero
 */
public java.lang.Boolean getFl_comune_estero() {
	return fl_comune_estero;
}
/* 
 * Getter dell'attributo fl_comune_proprio
 */
public java.lang.Boolean getFl_comune_proprio() {
	return fl_comune_proprio;
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
 * Getter dell'attributo im_lordo_percepiente
 */
public java.math.BigDecimal getIm_lordo_percepiente() {
	return im_lordo_percepiente;
}
/* 
 * Getter dell'attributo im_netto_pecepiente
 */
public java.math.BigDecimal getIm_netto_pecepiente() {
	return im_netto_pecepiente;
}
/* 
 * Getter dell'attributo im_quota_esente
 */
public java.math.BigDecimal getIm_quota_esente() {
	return im_quota_esente;
}
/* 
 * Getter dell'attributo im_spese
 */
public java.math.BigDecimal getIm_spese() {
	return im_spese;
}
/* 
 * Getter dell'attributo im_spese_anticipate
 */
public java.math.BigDecimal getIm_spese_anticipate() {
	return im_spese_anticipate;
}
/* 
 * Getter dell'attributo im_totale_missione
 */
public java.math.BigDecimal getIm_totale_missione() {
	return im_totale_missione;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo pg_anticipo
 */
public java.lang.Long getPg_anticipo() {
	return pg_anticipo;
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
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
/* 
 * Getter dell'attributo pg_rif_inquadramento
 */
public java.lang.Long getPg_rif_inquadramento() {
	return pg_rif_inquadramento;
}
/* 
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
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
 * Getter dell'attributo ti_istituz_commerc
 */
public java.lang.String getTi_istituz_commerc() {
	return ti_istituz_commerc;
}
/* 
 * Getter dell'attributo ti_provvisorio_definitivo
 */
public java.lang.String getTi_provvisorio_definitivo() {
	return ti_provvisorio_definitivo;
}
/* 
 * Setter dell'attributo cd_cds_anticipo
 */
public void setCd_cds_anticipo(java.lang.String cd_cds_anticipo) {
	this.cd_cds_anticipo = cd_cds_anticipo;
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
 * Setter dell'attributo cd_tipo_missione
 */
public void setCd_tipo_missione(java.lang.String cd_tipo_missione) {
	this.cd_tipo_missione = cd_tipo_missione;
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
 * Setter dell'attributo cd_uo_anticipo
 */
public void setCd_uo_anticipo(java.lang.String cd_uo_anticipo) {
	this.cd_uo_anticipo = cd_uo_anticipo;
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
 * Setter dell'attributo ds_missione
 */
public void setDs_missione(java.lang.String ds_missione) {
	this.ds_missione = ds_missione;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_emissione_mandato
 */
public void setDt_emissione_mandato(java.sql.Timestamp dt_emissione_mandato) {
	this.dt_emissione_mandato = dt_emissione_mandato;
}
/* 
 * Setter dell'attributo dt_fine_missione
 */
public void setDt_fine_missione(java.sql.Timestamp dt_fine_missione) {
	this.dt_fine_missione = dt_fine_missione;
}
/* 
 * Setter dell'attributo dt_inizio_missione
 */
public void setDt_inizio_missione(java.sql.Timestamp dt_inizio_missione) {
	this.dt_inizio_missione = dt_inizio_missione;
}
/* 
 * Setter dell'attributo dt_pagamento_fondo_eco
 */
public void setDt_pagamento_fondo_eco(java.sql.Timestamp dt_pagamento_fondo_eco) {
	this.dt_pagamento_fondo_eco = dt_pagamento_fondo_eco;
}
/* 
 * Setter dell'attributo dt_pagamento_mandato
 */
public void setDt_pagamento_mandato(java.sql.Timestamp dt_pagamento_mandato) {
	this.dt_pagamento_mandato = dt_pagamento_mandato;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo dt_trasmissione_mandato
 */
public void setDt_trasmissione_mandato(java.sql.Timestamp dt_trasmissione_mandato) {
	this.dt_trasmissione_mandato = dt_trasmissione_mandato;
}
/* 
 * Setter dell'attributo esercizio_anticipo
 */
public void setEsercizio_anticipo(java.lang.Integer esercizio_anticipo) {
	this.esercizio_anticipo = esercizio_anticipo;
}
/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}
/* 
 * Setter dell'attributo fl_associato_compenso
 */
public void setFl_associato_compenso(java.lang.Boolean fl_associato_compenso) {
	this.fl_associato_compenso = fl_associato_compenso;
}
/* 
 * Setter dell'attributo fl_comune_altro
 */
public void setFl_comune_altro(java.lang.Boolean fl_comune_altro) {
	this.fl_comune_altro = fl_comune_altro;
}
/* 
 * Setter dell'attributo fl_comune_estero
 */
public void setFl_comune_estero(java.lang.Boolean fl_comune_estero) {
	this.fl_comune_estero = fl_comune_estero;
}
/* 
 * Setter dell'attributo fl_comune_proprio
 */
public void setFl_comune_proprio(java.lang.Boolean fl_comune_proprio) {
	this.fl_comune_proprio = fl_comune_proprio;
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
 * Setter dell'attributo im_lordo_percepiente
 */
public void setIm_lordo_percepiente(java.math.BigDecimal im_lordo_percepiente) {
	this.im_lordo_percepiente = im_lordo_percepiente;
}
/* 
 * Setter dell'attributo im_netto_pecepiente
 */
public void setIm_netto_pecepiente(java.math.BigDecimal im_netto_pecepiente) {
	this.im_netto_pecepiente = im_netto_pecepiente;
}
/* 
 * Setter dell'attributo im_quota_esente
 */
public void setIm_quota_esente(java.math.BigDecimal im_quota_esente) {
	this.im_quota_esente = im_quota_esente;
}
/* 
 * Setter dell'attributo im_spese
 */
public void setIm_spese(java.math.BigDecimal im_spese) {
	this.im_spese = im_spese;
}
/* 
 * Setter dell'attributo im_spese_anticipate
 */
public void setIm_spese_anticipate(java.math.BigDecimal im_spese_anticipate) {
	this.im_spese_anticipate = im_spese_anticipate;
}
/* 
 * Setter dell'attributo im_totale_missione
 */
public void setIm_totale_missione(java.math.BigDecimal im_totale_missione) {
	this.im_totale_missione = im_totale_missione;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo pg_anticipo
 */
public void setPg_anticipo(java.lang.Long pg_anticipo) {
	this.pg_anticipo = pg_anticipo;
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
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
/* 
 * Setter dell'attributo pg_rif_inquadramento
 */
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.pg_rif_inquadramento = pg_rif_inquadramento;
}
/* 
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
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
/* 
 * Setter dell'attributo ti_istituz_commerc
 */
public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
	this.ti_istituz_commerc = ti_istituz_commerc;
}
/* 
 * Setter dell'attributo ti_provvisorio_definitivo
 */
public void setTi_provvisorio_definitivo(java.lang.String ti_provvisorio_definitivo) {
	this.ti_provvisorio_definitivo = ti_provvisorio_definitivo;
}
public java.math.BigDecimal getIm_rimborso() {
	return im_rimborso;
}
public void setIm_rimborso(java.math.BigDecimal im_rimborso) {
	this.im_rimborso = im_rimborso;
}
public java.lang.String getStato_liquidazione() {
	return stato_liquidazione;
}
public void setStato_liquidazione(java.lang.String stato_liquidazione) {
	this.stato_liquidazione = stato_liquidazione;
}
public Long getIdRimborsoMissione() {
	return idRimborsoMissione;
}
public void setIdRimborsoMissione(Long idRimborsoMissione) {
	this.idRimborsoMissione = idRimborsoMissione;
}
public String getIdFlusso() {
	return idFlusso;
}
public void setIdFlusso(String idFlusso) {
	this.idFlusso = idFlusso;
}
public String getIdFlussoOrdineMissione() {
	return idFlussoOrdineMissione;
}
public void setIdFlussoOrdineMissione(String idFlussoOrdineMissione) {
	this.idFlussoOrdineMissione = idFlussoOrdineMissione;
}
public String getIdFolderRimborsoMissione() {
	return idFolderRimborsoMissione;
}
public void setIdFolderRimborsoMissione(String idFolderRimborsoMissione) {
	this.idFolderRimborsoMissione = idFolderRimborsoMissione;
}
public String getIdFolderOrdineMissione() {
	return idFolderOrdineMissione;
}
public void setIdFolderOrdineMissione(String idFolderOrdineMissione) {
	this.idFolderOrdineMissione = idFolderOrdineMissione;
}
public java.lang.Boolean getDaRimborsoDaCompletare() {
	return daRimborsoDaCompletare;
}
public void setDaRimborsoDaCompletare(java.lang.Boolean daRimborsoDaCompletare) {
	this.daRimborsoDaCompletare = daRimborsoDaCompletare;
}
}
