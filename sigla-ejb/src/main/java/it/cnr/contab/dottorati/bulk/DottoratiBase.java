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

package it.cnr.contab.dottorati.bulk;

import it.cnr.jada.persistency.Keyed;

import java.time.LocalDate;

public class DottoratiBase extends DottoratiKey implements Keyed {
	private static final long serialVersionUID = 1L;

	//regione Università
	private String regione;

	//Università che riceve i soldi dal CNR
	private String universitaCapofila;

	//codice terzo dell'universitàCapofila
	private String codiceTerzo;

	//ibanAteneo Capofila
	private String ibanAteneo;

	//telefono AteneoCapofila
	private String telefonoAteneo;

	//emailAteneo Capofila
	private String emailAteneo;//String pattern(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/),

	//Altra università che da i soldi 50% e 50% con CNR
	private String altreUniversita;

	//Dipartimento dell'università
	private String dipartimentoUniversita;

	//Corso di dottorato
	private String corsoDottorato;

	//impresa che da i soldi 50% e 50% con CNR
	private String impresa;

	//tematica del corso di dottorato
	private String tematica;

	//numero di cicli che vengono finanziati
	private Integer numeroCicliFinanziati;

	//ricercatore se c'è
	private String ricercatore;

	//se c'è un istituto che fa cofinanziamento
	private String istitutoCNR;

	//dipartimento del CNR dell'istituto che fa il cofinanziamento
	private String dipartimentoCNR;

	//data di stipula della convenzione
	private LocalDate dataStipulaConvenzione;

	public DottoratiBase() {
		super();
	}

	public DottoratiBase(java.lang.Integer id) {
		super(id);
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getUniversitaCapofila() {
		return universitaCapofila;
	}

	public void setUniversitaCapofila(String universitaCapofila) {
		this.universitaCapofila = universitaCapofila;
	}

	public String getCodiceTerzo() {
		return codiceTerzo;
	}

	public void setCodiceTerzo(String codiceTerzo) {
		this.codiceTerzo = codiceTerzo;
	}

	public String getIbanAteneo() {
		return ibanAteneo;
	}

	public void setIbanAteneo(String ibanAteneo) {
		this.ibanAteneo = ibanAteneo;
	}

	public String getTelefonoAteneo() {
		return telefonoAteneo;
	}

	public void setTelefonoAteneo(String telefonoAteneo) {
		this.telefonoAteneo = telefonoAteneo;
	}

	public String getEmailAteneo() {
		return emailAteneo;
	}

	public void setEmailAteneo(String emailAteneo) {
		this.emailAteneo = emailAteneo;
	}

	public String getAltreUniversita() {
		return altreUniversita;
	}

	public void setAltreUniversita(String altreUniversita) {
		this.altreUniversita = altreUniversita;
	}

	public String getDipartimentoUniversita() {
		return dipartimentoUniversita;
	}

	public void setDipartimentoUniversita(String dipartimentoUniversita) {
		this.dipartimentoUniversita = dipartimentoUniversita;
	}

	public String getCorsoDottorato() {
		return corsoDottorato;
	}

	public void setCorsoDottorato(String corsoDottorato) {
		this.corsoDottorato = corsoDottorato;
	}

	public String getImpresa() {
		return impresa;
	}

	public void setImpresa(String impresa) {
		this.impresa = impresa;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public Integer getNumeroCicliFinanziati() {
		return numeroCicliFinanziati;
	}

	public void setNumeroCicliFinanziati(Integer numeroCicliFinanziati) {
		this.numeroCicliFinanziati = numeroCicliFinanziati;
	}

	public String getRicercatore() {
		return ricercatore;
	}

	public void setRicercatore(String ricercatore) {
		this.ricercatore = ricercatore;
	}

	public String getIstitutoCNR() {
		return istitutoCNR;
	}

	public void setIstitutoCNR(String istitutoCNR) {
		this.istitutoCNR = istitutoCNR;
	}

	public String getDipartimentoCNR() {
		return dipartimentoCNR;
	}

	public void setDipartimentoCNR(String dipartimentoCNR) {
		this.dipartimentoCNR = dipartimentoCNR;
	}

	public LocalDate getDataStipulaConvenzione() {
		return dataStipulaConvenzione;
	}

	public void setDataStipulaConvenzione(LocalDate dataStipulaConvenzione) {
		this.dataStipulaConvenzione = dataStipulaConvenzione;
	}

	////da cancellare tutto quello che è dopo
/**	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private Integer esercizio;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private String cdUnitaOrganizzativa;

	// DESCRIZIONE_ATTO VARCHAR2(300 BYTE) NOT NULL
	private String descrizioneAtto;

	// ID_TIPO_ATTO_BOLLO NUMBER NOT NULL
	private Integer idTipoAttoBollo;

	// CD_PROVV VARCHAR(20)
	private String cd_provv;

	// NUMERO_PROVV DECIMAL(10,0)
	private Integer nr_provv;

	// DT_PROVV TIMESTAMP(7)
	private java.sql.Timestamp dt_provv;

	// ESERCIZIO_CONTRATTO DECIMAL(4,0) NULL
	private Integer esercizio_contratto;

	// STATO_CONTRATTO CHAR(1) NULL
	private String stato_contratto;

	// PG_CONTRATTO DECIMAL(10,0) NULL
	private Long pg_contratto;

	// NUM_DETTAGLI NUMBER(6) NOT NULL
	private Integer numDettagli;

	// NUM_RIGHE NUMBER(6)
	private Integer numRighe;

	// NUM_REGISTRO NUMBER(6)
	private Integer numRegistro;

	public DottoratiBase() {
		super();
	}

	public DottoratiBase(Integer id) {
		super(id);
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}

	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}

	public String getDescrizioneAtto() {
		return descrizioneAtto;
	}

	public void setDescrizioneAtto(String descrizioneAtto) {
		this.descrizioneAtto = descrizioneAtto;
	}

	public Integer getIdTipoAttoBollo() {
		return idTipoAttoBollo;
	}

	public void setIdTipoAttoBollo(Integer idTipoAttoBollo) {
		this.idTipoAttoBollo = idTipoAttoBollo;
	}

	public String getCd_provv() {
		return cd_provv;
	}

	public void setCd_provv(String cdProvv) {
		cd_provv = cdProvv;
	}

	public Integer getNr_provv() {
		return nr_provv;
	}

	public void setNr_provv(Integer nrProvv) {
		nr_provv = nrProvv;
	}

	public java.sql.Timestamp getDt_provv() {
		return dt_provv;
	}

	public void setDt_provv(java.sql.Timestamp dtProvv) {
		dt_provv = dtProvv;
	}

	public Integer getEsercizio_contratto() {
		return esercizio_contratto;
	}

	public void setEsercizio_contratto(Integer integer) {
		esercizio_contratto = integer;
	}

	public String getStato_contratto() {
		return stato_contratto;
	}

	public void setStato_contratto(String string) {
		stato_contratto = string;
	}

	public Long getPg_contratto() {
		return pg_contratto;
	}

	public void setPg_contratto(Long long1) {
		pg_contratto = long1;
	}

	public Integer getNumDettagli() {
		return numDettagli;
	}

	public void setNumDettagli(Integer numDettagli) {
		this.numDettagli = numDettagli;
	}

	public Integer getNumRighe() {
		return numRighe;
	}

	public void setNumRighe(Integer numRighe) {
		this.numRighe = numRighe;
	}

	public Integer getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(Integer numRegistro) {
		this.numRegistro = numRegistro;
	}*/
}
