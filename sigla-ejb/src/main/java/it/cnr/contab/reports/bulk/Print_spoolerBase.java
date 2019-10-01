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

package it.cnr.contab.reports.bulk;

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class Print_spoolerBase extends Print_spoolerKey implements Keyed {
    // DS_STAMPA VARCHAR(300)
    private java.lang.String dsStampa;

    // DS_UTENTE VARCHAR(300)
    private java.lang.String dsUtente;

    // DT_SCADENZA TIMESTAMP
    private java.sql.Timestamp dtScadenza;

    // ID_REPORT_GENERICO DECIMAL(22,0)
    private java.math.BigDecimal idReportGenerico;

    // INTERVALLO_FINE DECIMAL(4,0)
    private java.lang.Integer intervalloFine;

    // INTERVALLO_INIZIO DECIMAL(4,0)
    private java.lang.Integer intervalloInizio;

    // NOME_FILE VARCHAR(300)
    private java.lang.String nomeFile;

    // PRIORITA DECIMAL(1,0) NOT NULL
    private java.lang.Integer priorita;

    // PRIORITA_SERVER DECIMAL(1,0) NOT NULL
    private java.lang.Integer prioritaServer;

    // REPORT VARCHAR(300)
    private java.lang.String report;

    // SERVER VARCHAR(200)
    private java.lang.String server;

    // STATO CHAR(1)
    private java.lang.String stato;

    // TI_VISIBILITA CHAR(1) NOT NULL
    private java.lang.String tiVisibilita;

    // VISIBILITA VARCHAR(30)
    private java.lang.String visibilita;

    //  FL_EMAIL CHAR(1)
    private java.lang.Boolean flEmail;

    //    EMAIL_A VARCHAR(250)
    private java.lang.String emailA;

    //    EMAIL_CC VARCHAR(250)
    private java.lang.String emailCc;

    //    EMAIL_CCN VARCHAR(250)
    private java.lang.String emailCcn;

    //    EMAIL_SUBJECT VARCHAR(250)
    private java.lang.String emailSubject;

    //    EMAIL_BODY VARCHAR(4000)
    private java.lang.String emailBody;

    //	  DT_PARTENZA DATE
    private Timestamp dtPartenza;

    //  INTERVALLO NUMBER(10)
    private Long intervallo;

    //	  TI_INTERVALLO CHAR(1)
    private String tiIntervallo;

    //	  DT_PROSSIMA_ESECUZIONE DATE
    private Timestamp dtProssimaEsecuzione;

    // CD_SERVIZIO_PEC VARCHAR(20)
    private java.lang.String cd_servizio_pec;

    // DS_SERVIZIO_PEC VARCHAR(500)
    private java.lang.String ds_oggetto_pec;

    // DS_NUMREG_PEC VARCHAR(500)
    private java.lang.String ds_numreg_pec;

    public Print_spoolerBase() {
        super();
    }

    public Print_spoolerBase(java.lang.Long pg_stampa) {
        super(pg_stampa);
    }

    /*
     * Getter dell'attributo ds_stampa
     */
    public java.lang.String getDsStampa() {
        return dsStampa;
    }

    /*
     * Setter dell'attributo ds_stampa
     */
    public void setDsStampa(java.lang.String ds_stampa) {
        this.dsStampa = ds_stampa;
    }

    /*
     * Getter dell'attributo ds_utente
     */
    public java.lang.String getDsUtente() {
        return dsUtente;
    }

    /*
     * Setter dell'attributo ds_utente
     */
    public void setDsUtente(java.lang.String ds_utente) {
        this.dsUtente = ds_utente;
    }

    /*
     * Getter dell'attributo dt_scadenza
     */
    public java.sql.Timestamp getDtScadenza() {
        return dtScadenza;
    }

    /*
     * Setter dell'attributo dt_scadenza
     */
    public void setDtScadenza(java.sql.Timestamp dt_scadenza) {
        this.dtScadenza = dt_scadenza;
    }

    /*
     * Getter dell'attributo id_report_generico
     */
    public java.math.BigDecimal getIdReportGenerico() {
        return idReportGenerico;
    }

    /*
     * Setter dell'attributo id_report_generico
     */
    public void setIdReportGenerico(java.math.BigDecimal id_report_generico) {
        this.idReportGenerico = id_report_generico;
    }

    /*
     * Getter dell'attributo intervallo_fine
     */
    public java.lang.Integer getIntervalloFine() {
        return intervalloFine;
    }

    /*
     * Setter dell'attributo intervallo_fine
     */
    public void setIntervalloFine(java.lang.Integer intervallo_fine) {
        this.intervalloFine = intervallo_fine;
    }

    /*
     * Getter dell'attributo intervallo_inizio
     */
    public java.lang.Integer getIntervalloInizio() {
        return intervalloInizio;
    }

    /*
     * Setter dell'attributo intervallo_inizio
     */
    public void setIntervalloInizio(java.lang.Integer intervallo_inizio) {
        this.intervalloInizio = intervallo_inizio;
    }

    /*
     * Getter dell'attributo nome_file
     */
    public java.lang.String getNomeFile() {
        return nomeFile;
    }

    /*
     * Setter dell'attributo nome_file
     */
    public void setNomeFile(java.lang.String nome_file) {
        this.nomeFile = nome_file;
    }

    /*
     * Getter dell'attributo priorita
     */
    public java.lang.Integer getPriorita() {
        return priorita;
    }

    /*
     * Setter dell'attributo priorita
     */
    public void setPriorita(java.lang.Integer priorita) {
        this.priorita = priorita;
    }

    /*
     * Getter dell'attributo priorita_server
     */
    public java.lang.Integer getPrioritaServer() {
        return prioritaServer;
    }

    /*
     * Setter dell'attributo priorita_server
     */
    public void setPrioritaServer(java.lang.Integer priorita_server) {
        this.prioritaServer = priorita_server;
    }

    /*
     * Getter dell'attributo report
     */
    public java.lang.String getReport() {
        return report;
    }

    /*
     * Setter dell'attributo report
     */
    public void setReport(java.lang.String report) {
        this.report = report;
    }

    /*
     * Getter dell'attributo server
     */
    public java.lang.String getServer() {
        return server;
    }

    /*
     * Setter dell'attributo server
     */
    public void setServer(java.lang.String server) {
        this.server = server;
    }

    /*
     * Getter dell'attributo stato
     */
    public java.lang.String getStato() {
        return stato;
    }

    /*
     * Setter dell'attributo stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }

    /*
     * Getter dell'attributo ti_visibilita
     */
    public java.lang.String getTiVisibilita() {
        return tiVisibilita;
    }

    /*
     * Setter dell'attributo ti_visibilita
     */
    public void setTiVisibilita(java.lang.String ti_visibilita) {
        this.tiVisibilita = ti_visibilita;
    }

    /*
     * Getter dell'attributo visibilita
     */
    public java.lang.String getVisibilita() {
        return visibilita;
    }

    /*
     * Setter dell'attributo visibilita
     */
    public void setVisibilita(java.lang.String visibilita) {
        this.visibilita = visibilita;
    }

    public java.lang.String getEmailA() {
        return emailA;
    }

    public void setEmailA(java.lang.String email_a) {
        this.emailA = email_a;
    }

    public java.lang.String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(java.lang.String email_body) {
        this.emailBody = email_body;
    }

    public java.lang.String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(java.lang.String email_cc) {
        this.emailCc = email_cc;
    }

    public java.lang.String getEmailCcn() {
        return emailCcn;
    }

    public void setEmailCcn(java.lang.String email_ccn) {
        this.emailCcn = email_ccn;
    }

    public java.lang.String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(java.lang.String email_subject) {
        this.emailSubject = email_subject;
    }

    public java.lang.Boolean getFlEmail() {
        return flEmail;
    }

    public void setFlEmail(java.lang.Boolean fl_email) {
        this.flEmail = fl_email;
    }

    public Timestamp getDtPartenza() {
        return dtPartenza;
    }

    public void setDtPartenza(Timestamp dt_partenza) {
        this.dtPartenza = dt_partenza;
    }

    public Long getIntervallo() {
        return intervallo;
    }

    public void setIntervallo(Long intervallo) {
        this.intervallo = intervallo;
    }

    public String getTiIntervallo() {
        return tiIntervallo;
    }

    public void setTiIntervallo(String s) {
        tiIntervallo = s;
    }

    public Timestamp getDtProssimaEsecuzione() {
        return dtProssimaEsecuzione;
    }

    public void setDtProssimaEsecuzione(Timestamp dt_ultima_esecuzione) {
        this.dtProssimaEsecuzione = dt_ultima_esecuzione;
    }

    public java.lang.String getCd_servizio_pec() {
        return cd_servizio_pec;
    }

    public void setCd_servizio_pec(java.lang.String cd_servizio_pec) {
        this.cd_servizio_pec = cd_servizio_pec;
    }

    public java.lang.String getDs_oggetto_pec() {
        return ds_oggetto_pec;
    }

    public void setDs_oggetto_pec(java.lang.String ds_oggetto_pec) {
        this.ds_oggetto_pec = ds_oggetto_pec;
    }

    public java.lang.String getDs_numreg_pec() {
        return ds_numreg_pec;
    }

    public void setDs_numreg_pec(java.lang.String ds_numreg_pec) {
        this.ds_numreg_pec = ds_numreg_pec;
    }
}
