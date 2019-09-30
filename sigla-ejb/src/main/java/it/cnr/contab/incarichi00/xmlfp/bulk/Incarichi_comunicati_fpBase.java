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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_comunicati_fpBase extends Incarichi_comunicati_fpKey implements Keyed {

//    ID_INCARICO VARCHAR(15)
	private java.lang.String id_incarico;

//    ANNO_RIFERIMENTO DECIMAL(4,0)
	private java.lang.Integer anno_riferimento;

//    SEMESTRE_RIFERIMENTO DECIMAL(1,0)
	private java.lang.Integer semestre_riferimento;

//    CODICE_ENTE VARCHAR(7)
	private java.lang.String codice_ente;
 
//    COGNOME VARCHAR(50) NOT NULL
	private java.lang.String cognome;
 
//    NOME VARCHAR(50) NOT NULL
	private java.lang.String nome;

//    DATA_NASCITA TIMESTAMP(7)
	private java.sql.Timestamp data_nascita;

//    TI_SESSO CHAR(1)
	private java.lang.String ti_sesso;
 
//    FL_ESTERO CHAR(1) NOT NULL
	private java.lang.Boolean fl_estero;

//    CODICE_FISCALE_PARTITA_IVA VARCHAR(16)
	private java.lang.String codice_fiscale_partita_iva;

//    DESCRIZIONE_INCARICO VARCHAR(2000)
	private java.lang.String descrizione_incarico;

//    VARIAZIONI_INCARICO VARCHAR(2000)
	private java.lang.String variazioni_incarico;

//    DT_INIZIO TIMESTAMP(7)
	private java.sql.Timestamp dt_inizio;

//    DT_FINE TIMESTAMP(7)
	private java.sql.Timestamp dt_fine;
 
//    IMPORTO_PREVISTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_previsto;

//    FL_RIFERIMENTO_REGOLAMENTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_riferimento_regolamento;
 
//    FL_SALDO CHAR(1) NOT NULL
	private java.lang.Boolean fl_saldo;

//    ATTIVITA_ECONOMICA VARCHAR(5)
	private java.lang.String attivita_economica;

//    TIPO_RAPPORTO VARCHAR(5)
	private java.lang.String tipo_rapporto;
		
//    MODALITA_ACQUISIZIONE VARCHAR(5)
	private java.lang.String modalita_acquisizione;

//    TIPOLOGIA_CONSULENTE VARCHAR(5)
	private java.lang.String tipologia_consulente;

//    ESITO_INCARICO VARCHAR(5)
	private java.lang.String esito_incarico;

//    ERR_ID VARCHAR(2000)
	private java.lang.String err_id;

//    ERR_ANNO_RIFERIMENTO VARCHAR(2000)
	private java.lang.String err_anno_riferimento;

//    ERR_ERR_SEMESTRE_RIFERIMENTO VARCHAR(2000)
	private java.lang.String err_semestre_riferimento;
	
//    ERR_CODICE_ENTE VARCHAR(2000)
	private java.lang.String err_codice_ente;

//    ERR_DESCRIZIONE_INCARICO VARCHAR(2000)
	private java.lang.String err_descrizione_incarico;

//    ERR_ATTIVITA_ECONOMICA VARCHAR(2000)
	private java.lang.String err_attivita_economica;

//    ERR_DT_INIZIO VARCHAR(2000)
	private java.lang.String err_dt_inizio;

//    ERR_DT_FINE VARCHAR(2000)
	private java.lang.String err_dt_fine;

//    ERR_IMPORTO_PREVISTO VARCHAR(2000)
	private java.lang.String err_importo_previsto;

//    ERR_SALDO VARCHAR(2000)
	private java.lang.String err_saldo;
	
//    ERR_MODALITA_ACQUISIZIONE VARCHAR(2000)
	private java.lang.String err_modalita_acquisizione;
	
//    ERR_TIPO_RAPPORTO VARCHAR(2000)
	private java.lang.String err_tipo_rapporto;
	
//    ERR_VARIAZIONI_INCARICO VARCHAR(2000)
	private java.lang.String err_variazioni_incarico;
	
//    ESITO_INCARICATO_PERSONA VARCHAR(2000)
	private java.lang.String esito_incarico_persona;
	
//    ERR_COGNOME VARCHAR(2000)
	private java.lang.String err_cognome;
	
//    ERR_NOME VARCHAR(2000)
	private java.lang.String err_nome;

//    ERR_DATA_NASCITA VARCHAR(2000)
	private java.lang.String err_data_nascita;

//    ERR_SESSO VARCHAR(2000)
	private java.lang.String err_sesso;

//    ERR_ESTERO VARCHAR(2000)
	private java.lang.String err_estero;

//    ERR_CODICE_FISCALE_PARTITA_IVA VARCHAR(2000)
	private java.lang.String err_codice_fiscale_partita_iva;

	
	public Incarichi_comunicati_fpBase() {
		super();
	}

	public Incarichi_comunicati_fpBase(java.lang.Integer esercizio_repertorio, java.lang.Long pg_repertorio, java.lang.String tipo_record, java.lang.Long pg_record) {
		super(esercizio_repertorio, pg_repertorio, tipo_record, pg_record);
	}

	public java.lang.String getId_incarico() {
		return id_incarico;
	}

	public void setId_incarico(java.lang.String idIncarico) {
		id_incarico = idIncarico;
	}

	public java.lang.Integer getAnno_riferimento() {
		return anno_riferimento;
	}

	public void setAnno_riferimento(java.lang.Integer annoRiferimento) {
		anno_riferimento = annoRiferimento;
	}

	public java.lang.Integer getSemestre_riferimento() {
		return semestre_riferimento;
	}

	public void setSemestre_riferimento(java.lang.Integer semestreRiferimento) {
		semestre_riferimento = semestreRiferimento;
	}

	public java.lang.String getCodice_ente() {
		return codice_ente;
	}

	public void setCodice_ente(java.lang.String codiceEnte) {
		codice_ente = codiceEnte;
	}

	public java.lang.String getCognome() {
		return cognome;
	}

	public void setCognome(java.lang.String cognome) {
		this.cognome = cognome;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.sql.Timestamp getData_nascita() {
		return data_nascita;
	}

	public void setData_nascita(java.sql.Timestamp dataNascita) {
		data_nascita = dataNascita;
	}

	public java.lang.String getTi_sesso() {
		return ti_sesso;
	}

	public void setTi_sesso(java.lang.String tiSesso) {
		ti_sesso = tiSesso;
	}

	public java.lang.Boolean getFl_estero() {
		return fl_estero;
	}

	public void setFl_estero(java.lang.Boolean flEstero) {
		fl_estero = flEstero;
	}

	public java.lang.String getCodice_fiscale_partita_iva() {
		return codice_fiscale_partita_iva;
	}

	public void setCodice_fiscale_partita_iva(
			java.lang.String codiceFiscalePartitaIva) {
		codice_fiscale_partita_iva = codiceFiscalePartitaIva;
	}

	public java.lang.String getVariazioni_incarico() {
		return variazioni_incarico;
	}

	public void setVariazioni_incarico(
			java.lang.String variazioniIncarico) {
		variazioni_incarico = variazioniIncarico;
	}

	public java.lang.String getDescrizione_incarico() {
		return descrizione_incarico;
	}

	public void setDescrizione_incarico(java.lang.String descrizioneIncarico) {
		descrizione_incarico = descrizioneIncarico;
	}

	public java.sql.Timestamp getDt_inizio() {
		return dt_inizio;
	}

	public void setDt_inizio(java.sql.Timestamp dtInizio) {
		dt_inizio = dtInizio;
	}

	public java.sql.Timestamp getDt_fine() {
		return dt_fine;
	}

	public void setDt_fine(java.sql.Timestamp dtFine) {
		dt_fine = dtFine;
	}

	public java.math.BigDecimal getImporto_previsto() {
		return importo_previsto;
	}

	public void setImporto_previsto(java.math.BigDecimal importoPrevisto) {
		importo_previsto = importoPrevisto;
	}

	public java.lang.Boolean getFl_riferimento_regolamento() {
		return fl_riferimento_regolamento;
	}

	public void setFl_riferimento_regolamento(
			java.lang.Boolean flRiferimentoRegolamento) {
		fl_riferimento_regolamento = flRiferimentoRegolamento;
	}

	public java.lang.Boolean getFl_saldo() {
		return fl_saldo;
	}

	public void setFl_saldo(java.lang.Boolean flSaldo) {
		fl_saldo = flSaldo;
	}

	public java.lang.String getAttivita_economica() {
		return attivita_economica;
	}

	public void setAttivita_economica(java.lang.String attivitaEconomica) {
		attivita_economica = attivitaEconomica;
	}

	public java.lang.String getTipo_rapporto() {
		return tipo_rapporto;
	}

	public void setTipo_rapporto(java.lang.String tipoRapporto) {
		tipo_rapporto = tipoRapporto;
	}

	public java.lang.String getModalita_acquisizione() {
		return modalita_acquisizione;
	}

	public void setModalita_acquisizione(java.lang.String modalitaAcquisizione) {
		modalita_acquisizione = modalitaAcquisizione;
	}

	public java.lang.String getTipologia_consulente() {
		return tipologia_consulente;
	}

	public void setTipologia_consulente(java.lang.String tipologiaConsulente) {
		tipologia_consulente = tipologiaConsulente;
	}

	public java.lang.String getEsito_incarico() {
		return esito_incarico;
	}

	public void setEsito_incarico(java.lang.String esitoIncarico) {
		esito_incarico = esitoIncarico;
	}

	public java.lang.String getErr_id() {
		return err_id;
	}

	public void setErr_id(java.lang.String errId) {
		err_id = errId;
	}

	public java.lang.String getErr_anno_riferimento() {
		return err_anno_riferimento;
	}

	public void setErr_anno_riferimento(java.lang.String errAnnoRiferimento) {
		err_anno_riferimento = errAnnoRiferimento;
	}

	public java.lang.String getErr_semestre_riferimento() {
		return err_semestre_riferimento;
	}

	public void setErr_semestre_riferimento(java.lang.String errSemestreRiferimento) {
		err_semestre_riferimento = errSemestreRiferimento;
	}

	public java.lang.String getErr_codice_ente() {
		return err_codice_ente;
	}

	public void setErr_codice_ente(java.lang.String errCodiceEnte) {
		err_codice_ente = errCodiceEnte;
	}

	public java.lang.String getErr_descrizione_incarico() {
		return err_descrizione_incarico;
	}

	public void setErr_descrizione_incarico(java.lang.String errDescrizioneIncarico) {
		err_descrizione_incarico = errDescrizioneIncarico;
	}

	public java.lang.String getErr_attivita_economica() {
		return err_attivita_economica;
	}

	public void setErr_attivita_economica(java.lang.String errAttivitaEconomica) {
		err_attivita_economica = errAttivitaEconomica;
	}

	public java.lang.String getErr_dt_inizio() {
		return err_dt_inizio;
	}

	public void setErr_dt_inizio(java.lang.String errDtInizio) {
		err_dt_inizio = errDtInizio;
	}

	public java.lang.String getErr_dt_fine() {
		return err_dt_fine;
	}

	public void setErr_dt_fine(java.lang.String errDtFine) {
		err_dt_fine = errDtFine;
	}

	public java.lang.String getErr_importo_previsto() {
		return err_importo_previsto;
	}

	public void setErr_importo_previsto(java.lang.String errImportoPrevisto) {
		err_importo_previsto = errImportoPrevisto;
	}

	public java.lang.String getErr_saldo() {
		return err_saldo;
	}

	public void setErr_saldo(java.lang.String errSaldo) {
		err_saldo = errSaldo;
	}

	public java.lang.String getErr_modalita_acquisizione() {
		return err_modalita_acquisizione;
	}

	public void setErr_modalita_acquisizione(
			java.lang.String errModalitaAcquisizione) {
		err_modalita_acquisizione = errModalitaAcquisizione;
	}

	public java.lang.String getErr_tipo_rapporto() {
		return err_tipo_rapporto;
	}

	public void setErr_tipo_rapporto(java.lang.String errTipoRapporto) {
		err_tipo_rapporto = errTipoRapporto;
	}

	public java.lang.String getErr_variazioni_incarico() {
		return err_variazioni_incarico;
	}

	public void setErr_variazioni_incarico(java.lang.String errVariazioniIncarico) {
		err_variazioni_incarico = errVariazioniIncarico;
	}

	public java.lang.String getEsito_incarico_persona() {
		return esito_incarico_persona;
	}

	public void setEsito_incarico_persona(java.lang.String esitoIncaricoPersona) {
		esito_incarico_persona = esitoIncaricoPersona;
	}

	public java.lang.String getErr_cognome() {
		return err_cognome;
	}

	public void setErr_cognome(java.lang.String errCognome) {
		err_cognome = errCognome;
	}

	public java.lang.String getErr_nome() {
		return err_nome;
	}

	public void setErr_nome(java.lang.String errNome) {
		err_nome = errNome;
	}

	public java.lang.String getErr_data_nascita() {
		return err_data_nascita;
	}

	public void setErr_data_nascita(java.lang.String errDataNascita) {
		err_data_nascita = errDataNascita;
	}

	public java.lang.String getErr_sesso() {
		return err_sesso;
	}

	public void setErr_sesso(java.lang.String errSesso) {
		err_sesso = errSesso;
	}

	public java.lang.String getErr_estero() {
		return err_estero;
	}

	public void setErr_estero(java.lang.String errEstero) {
		err_estero = errEstero;
	}

	public java.lang.String getErr_codice_fiscale_partita_iva() {
		return err_codice_fiscale_partita_iva;
	}

	public void setErr_codice_fiscale_partita_iva(
			java.lang.String errCodiceFiscalePartitaIva) {
		err_codice_fiscale_partita_iva = errCodiceFiscalePartitaIva;
	}
}
