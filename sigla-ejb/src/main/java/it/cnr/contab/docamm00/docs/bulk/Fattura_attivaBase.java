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

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class Fattura_attivaBase extends Fattura_attivaKey implements Keyed {
	// CAMBIO DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cd_divisa;

	// CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cd_modalita_pag;

	// CD_MODALITA_PAG_UO_CDS VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag_uo_cds;

	public Long getIdPendenzaPagopa() {
		return idPendenzaPagopa;
	}

	public void setIdPendenzaPagopa(Long idPendenzaPagopa) {
		this.idPendenzaPagopa = idPendenzaPagopa;
	}

	// CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cd_termini_pag;

	// CD_TERMINI_PAG_UO_CDS VARCHAR(10)
	private java.lang.String cd_termini_pag_uo_cds;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Long idPendenzaPagopa;

	// CD_TERZO_UO_CDS DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo_uo_cds;

	// CD_TIPO_SEZIONALE VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_sezionale;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;
	
	// CODICE_DESTINATARIO_FATT VARCHAR(7)
	private java.lang.String codiceDestinatarioFatt;
	
	private java.lang.String mailFatturaElettronica;
	
	private java.lang.String pecFatturaElettronica;
	
	// CODICE_UNIVOCO_UFFICIO_IPA VARCHAR(6)
	private java.lang.String codiceUnivocoUfficioIpa;

	// CODICE_INVIO_SDI VARCHAR(30)
	private java.lang.String codiceInvioSdi;
	
	// STATO_INVIO_SDI VARCHAR(3)
	private java.lang.String statoInvioSdi;

	// NOTE_INVIO_SDI VARCHAR(500)
	private java.lang.String noteInvioSdi;

	// NC_ANNULLO_SDI VARCHAR(1)
	private java.lang.String ncAnnulloSdi;

	// DS_FATTURA_ATTIVA VARCHAR(200)
	private java.lang.String ds_fattura_attiva;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	private java.sql.Timestamp dt_ordine;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// DT_EMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_emissione;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// DT_SCADENZA TIMESTAMP
	private java.sql.Timestamp dt_scadenza;

	// FL_CONGELATA CHAR(1) NOT NULL
	private java.lang.Boolean fl_congelata;

	private java.lang.Boolean fl_ordine_elettronico;

	public Timestamp getDt_ordine() {
		return dt_ordine;
	}

	public void setDt_ordine(Timestamp dt_ordine) {
		this.dt_ordine = dt_ordine;
	}

	public Boolean getFl_ordine_elettronico() {
		return fl_ordine_elettronico;
	}

	public void setFl_ordine_elettronico(Boolean fl_ordine_elettronico) {
		this.fl_ordine_elettronico = fl_ordine_elettronico;
	}

	// FL_EXTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_extra_ue;

	// FL_INTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_intra_ue;

	// FL_LIQUIDAZIONE_DIFFERITA CHAR(1) NOT NULL
	private java.lang.Boolean fl_liquidazione_differita;

	// FL_SAN_MARINO CHAR(1) NOT NULL
	private java.lang.Boolean fl_san_marino;

	// FL_STAMPA CHAR(1) NOT NULL
	private java.lang.Boolean fl_stampa;

	private java.lang.Boolean flFatturaElettronica;
	
	
	// IM_TOTALE_FATTURA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_fattura;

	// IM_TOTALE_IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_imponibile;

	// IM_TOTALE_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_iva;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NOTE VARCHAR(300)
	private java.lang.String note;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_BANCA DECIMAL(10,0)
	private java.lang.Long pg_banca;

	// PG_BANCA_UO_CDS DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca_uo_cds;

	// PROTOCOLLO_IVA DECIMAL(10,0)
	private java.lang.Long protocollo_iva;

	// PROTOCOLLO_IVA_GENERALE DECIMAL(10,0)
	private java.lang.Long protocollo_iva_generale;

	// PROGR_UNIVOCO_ANNO DECIMAL(10,0)
	private java.lang.Long progrUnivocoAnno;

	// DT_CONSEGNA_SDI TIMESTAMP 
	private java.sql.Timestamp dtConsegnaSdi;

	private java.sql.Timestamp dtRicezioneSdi;
	
	// NOME_FILE_INVIO_SDI CHAR(100) NOT NULL
	private java.lang.String nomeFileInvioSdi;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// RIFERIMENTO_ORDINE VARCHAR(50)
	private java.lang.String riferimento_ordine;

	// STATO_COAN CHAR(1) NOT NULL
	private java.lang.String stato_coan;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

	// TI_CAUSALE_EMISSIONE CHAR(1) NOT NULL
	private java.lang.String ti_causale_emissione;

	// TI_FATTURA CHAR(1) NOT NULL
	private java.lang.String ti_fattura;

	private Long pg_fattura_esterno;
	
	// TI_BENE_SERVIZIO CHAR(1) NOT NULL
	private java.lang.String ti_bene_servizio;
	
	private java.lang.String fl_pagamento_anticipato;
	
public Fattura_attivaBase() {
	super();
}
public Fattura_attivaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva);
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
 * Getter dell'attributo cd_terzo_uo_cds
 */
public java.lang.Integer getCd_terzo_uo_cds() {
	return cd_terzo_uo_cds;
}
/* 
 * Getter dell'attributo cd_tipo_sezionale
 */
public java.lang.String getCd_tipo_sezionale() {
	return cd_tipo_sezionale;
}
/* 
 * Getter dell'attributo cd_uo_origine
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
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
 * Getter dell'attributo ds_fattura_attiva
 */
public java.lang.String getDs_fattura_attiva() {
	return ds_fattura_attiva;
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
 * Getter dell'attributo dt_emissione
 */
public java.sql.Timestamp getDt_emissione() {
	return dt_emissione;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/* 
 * Getter dell'attributo fl_congelata
 */
public java.lang.Boolean getFl_congelata() {
	return fl_congelata;
}
/* 
 * Getter dell'attributo fl_extra_ue
 */
public java.lang.Boolean getFl_extra_ue() {
	return fl_extra_ue;
}
/* 
 * Getter dell'attributo fl_intra_ue
 */
public java.lang.Boolean getFl_intra_ue() {
	return fl_intra_ue;
}
/* 
 * Getter dell'attributo fl_liquidazione_differita
 */
public java.lang.Boolean getFl_liquidazione_differita() {
	return fl_liquidazione_differita;
}
/* 
 * Getter dell'attributo fl_san_marino
 */
public java.lang.Boolean getFl_san_marino() {
	return fl_san_marino;
}
/* 
 * Getter dell'attributo fl_stampa
 */
public java.lang.Boolean getFl_stampa() {
	return fl_stampa;
}
/* 
 * Getter dell'attributo im_totale_fattura
 */
public java.math.BigDecimal getIm_totale_fattura() {
	return im_totale_fattura;
}
/* 
 * Getter dell'attributo im_totale_imponibile
 */
public java.math.BigDecimal getIm_totale_imponibile() {
	return im_totale_imponibile;
}
/* 
 * Getter dell'attributo im_totale_iva
 */
public java.math.BigDecimal getIm_totale_iva() {
	return im_totale_iva;
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
 * Getter dell'attributo protocollo_iva
 */
public java.lang.Long getProtocollo_iva() {
	return protocollo_iva;
}
/* 
 * Getter dell'attributo protocollo_iva_generale
 */
public java.lang.Long getProtocollo_iva_generale() {
	return protocollo_iva_generale;
}
/* 
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}
/* 
 * Getter dell'attributo riferimento_ordine
 */
public java.lang.String getRiferimento_ordine() {
	return riferimento_ordine;
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
 * Getter dell'attributo ti_associato_manrev
 */
public java.lang.String getTi_associato_manrev() {
	return ti_associato_manrev;
}
/* 
 * Getter dell'attributo ti_causale_emissione
 */
public java.lang.String getTi_causale_emissione() {
	return ti_causale_emissione;
}
/* 
 * Getter dell'attributo ti_fattura
 */
public java.lang.String getTi_fattura() {
	return ti_fattura;
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
 * Setter dell'attributo cd_terzo_uo_cds
 */
public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
	this.cd_terzo_uo_cds = cd_terzo_uo_cds;
}
/* 
 * Setter dell'attributo cd_tipo_sezionale
 */
public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
	this.cd_tipo_sezionale = cd_tipo_sezionale;
}
/* 
 * Setter dell'attributo cd_uo_origine
 */
public void setCd_uo_origine(java.lang.String cd_uo_origine) {
	this.cd_uo_origine = cd_uo_origine;
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
 * Setter dell'attributo ds_fattura_attiva
 */
public void setDs_fattura_attiva(java.lang.String ds_fattura_attiva) {
	this.ds_fattura_attiva = ds_fattura_attiva;
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
 * Setter dell'attributo dt_emissione
 */
public void setDt_emissione(java.sql.Timestamp dt_emissione) {
	this.dt_emissione = dt_emissione;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/* 
 * Setter dell'attributo fl_congelata
 */
public void setFl_congelata(java.lang.Boolean fl_congelata) {
	this.fl_congelata = fl_congelata;
}
/* 
 * Setter dell'attributo fl_extra_ue
 */
public void setFl_extra_ue(java.lang.Boolean fl_extra_ue) {
	this.fl_extra_ue = fl_extra_ue;
}
/* 
 * Setter dell'attributo fl_intra_ue
 */
public void setFl_intra_ue(java.lang.Boolean fl_intra_ue) {
	this.fl_intra_ue = fl_intra_ue;
}
/* 
 * Setter dell'attributo fl_liquidazione_differita
 */
public void setFl_liquidazione_differita(java.lang.Boolean fl_liquidazione_differita) {
	this.fl_liquidazione_differita = fl_liquidazione_differita;
}
/* 
 * Setter dell'attributo fl_san_marino
 */
public void setFl_san_marino(java.lang.Boolean fl_san_marino) {
	this.fl_san_marino = fl_san_marino;
}
/* 
 * Setter dell'attributo fl_stampaStoreService
 */
public void setFl_stampa(java.lang.Boolean fl_stampa) {
	this.fl_stampa = fl_stampa;
}
/* 
 * Setter dell'attributo im_totale_fattura
 */
public void setIm_totale_fattura(java.math.BigDecimal im_totale_fattura) {
	this.im_totale_fattura = im_totale_fattura;
}
/* 
 * Setter dell'attributo im_totale_imponibile
 */
public void setIm_totale_imponibile(java.math.BigDecimal im_totale_imponibile) {
	this.im_totale_imponibile = im_totale_imponibile;
}
/* 
 * Setter dell'attributo im_totale_iva
 */
public void setIm_totale_iva(java.math.BigDecimal im_totale_iva) {
	this.im_totale_iva = im_totale_iva;
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
 * Setter dell'attributo protocollo_iva
 */
public void setProtocollo_iva(java.lang.Long protocollo_iva) {
	this.protocollo_iva = protocollo_iva;
}
/* 
 * Setter dell'attributo protocollo_iva_generale
 */
public void setProtocollo_iva_generale(java.lang.Long protocollo_iva_generale) {
	this.protocollo_iva_generale = protocollo_iva_generale;
}
/* 
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}
/* 
 * Setter dell'attributo riferimento_ordine
 */
public void setRiferimento_ordine(java.lang.String riferimento_ordine) {
	this.riferimento_ordine = riferimento_ordine;
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
 * Setter dell'attributo ti_associato_manrev
 */
public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
	this.ti_associato_manrev = ti_associato_manrev;
}
/* 
 * Setter dell'attributo ti_causale_emissione
 */
public void setTi_causale_emissione(java.lang.String ti_causale_emissione) {
	this.ti_causale_emissione = ti_causale_emissione;
}
/* 
 * Setter dell'attributo ti_fattura
 */
public void setTi_fattura(java.lang.String ti_fattura) {
	this.ti_fattura = ti_fattura;
}
public Long getPg_fattura_esterno() {
	return pg_fattura_esterno;
}
public void setPg_fattura_esterno(Long pg_fattura_esterno) {
	this.pg_fattura_esterno = pg_fattura_esterno;
}
public java.lang.String getTi_bene_servizio() {
	return ti_bene_servizio;
}
public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
	this.ti_bene_servizio = ti_bene_servizio;
}
public java.lang.String getFl_pagamento_anticipato() {
	return fl_pagamento_anticipato;
}
public void setFl_pagamento_anticipato(java.lang.String fl_pagamento_anticipato) {
	this.fl_pagamento_anticipato = fl_pagamento_anticipato;
}
public java.lang.String getCodiceUnivocoUfficioIpa() {
	return codiceUnivocoUfficioIpa;
}
public void setCodiceUnivocoUfficioIpa(java.lang.String codiceUnivocoUfficioIpa) {
	this.codiceUnivocoUfficioIpa = codiceUnivocoUfficioIpa;
}
public java.lang.String getCodiceInvioSdi() {
	return codiceInvioSdi;
}
public void setCodiceInvioSdi(java.lang.String codiceInvioSdi) {
	this.codiceInvioSdi = codiceInvioSdi;
}
public java.lang.String getStatoInvioSdi() {
	return statoInvioSdi;
}
public void setStatoInvioSdi(java.lang.String statoInvioSdi) {
	this.statoInvioSdi = statoInvioSdi;
}
public java.lang.String getNoteInvioSdi() {
	return noteInvioSdi;
}
public void setNoteInvioSdi(java.lang.String noteInvioSdi) {
	this.noteInvioSdi = noteInvioSdi;
}
public java.lang.String getNcAnnulloSdi() {
	return ncAnnulloSdi;
}
public void setNcAnnulloSdi(java.lang.String ncAnnulloSdi) {
	this.ncAnnulloSdi = ncAnnulloSdi;
}
public java.lang.Long getProgrUnivocoAnno() {
	return progrUnivocoAnno;
}
public void setProgrUnivocoAnno(java.lang.Long progrUnivocoAnno) {
	this.progrUnivocoAnno = progrUnivocoAnno;
}
public java.sql.Timestamp getDtConsegnaSdi() {
	return dtConsegnaSdi;
}
public void setDtConsegnaSdi(java.sql.Timestamp dtConsegnaSdi) {
	this.dtConsegnaSdi = dtConsegnaSdi;
}
public java.lang.String getNomeFileInvioSdi() {
	return nomeFileInvioSdi;
}
public void setNomeFileInvioSdi(java.lang.String nomeFileInvioSdi) {
	this.nomeFileInvioSdi = nomeFileInvioSdi;
}
public java.lang.String getCodiceDestinatarioFatt() {
	return codiceDestinatarioFatt;
}
public void setCodiceDestinatarioFatt(java.lang.String codiceDestinatarioFatt) {
	this.codiceDestinatarioFatt = codiceDestinatarioFatt;
}
public java.lang.String getMailFatturaElettronica() {
	return mailFatturaElettronica;
}
public void setMailFatturaElettronica(java.lang.String mailFatturaElettronica) {
	this.mailFatturaElettronica = mailFatturaElettronica;
}
public java.lang.String getPecFatturaElettronica() {
	return pecFatturaElettronica;
}
public void setPecFatturaElettronica(java.lang.String pecFatturaElettronica) {
	this.pecFatturaElettronica = pecFatturaElettronica;
}
public java.lang.Boolean getFlFatturaElettronica() {
	return flFatturaElettronica;
}
public void setFlFatturaElettronica(java.lang.Boolean flFatturaElettronica) {
	this.flFatturaElettronica = flFatturaElettronica;
}
public java.sql.Timestamp getDtRicezioneSdi() {
	return dtRicezioneSdi;
}
public void setDtRicezioneSdi(java.sql.Timestamp dtRicezioneSdi) {
	this.dtRicezioneSdi = dtRicezioneSdi;
}
}
