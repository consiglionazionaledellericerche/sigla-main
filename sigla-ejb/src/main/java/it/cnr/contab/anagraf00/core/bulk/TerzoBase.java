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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class TerzoBase extends TerzoKey implements Keyed {
    // CAP_COMUNE_SEDE VARCHAR(5)
    private java.lang.String cap_comune_sede;

    private java.lang.Boolean flSbloccoFatturaElettronica;

    // CD_ANAG DECIMAL(8,0) NOT NULL
    private java.lang.Integer cd_anag;

    // CD_PRECEDENTE VARCHAR(20)
    private java.lang.String cd_precedente;

    // CD_UNITA_ORGANIZZATIVA VARCHAR(60)
    private java.lang.String cd_unita_organizzativa;

    // DENOMINAZIONE_SEDE VARCHAR(200) NOT NULL
    private java.lang.String denominazione_sede;

    // DT_CANC TIMESTAMP
    private java.sql.Timestamp dt_canc;

    // DT_FINE_RAPPORTO TIMESTAMP
    private java.sql.Timestamp dt_fine_rapporto;

    // FRAZIONE_SEDE VARCHAR(100)
    private java.lang.String frazione_sede;

    // NOME_UNITA_ORGANIZZATIVA VARCHAR(100)
    private java.lang.String nome_unita_organizzativa;

    // NOTE VARCHAR(300)
    private java.lang.String note;

    // CODICE_UNIVOCO_UFFICIO_IPA VARCHAR(6)
    private java.lang.String codiceUnivocoUfficioIpa;

    // CODICE_DESTINATARIO_FATT VARCHAR(7)
    private java.lang.String codiceDestinatarioFatt;

    // NUMERO_CIVICO_SEDE VARCHAR(5)
    private java.lang.String numero_civico_sede;

    // PG_COMUNE_SEDE DECIMAL(10,0) NOT NULL
    private java.lang.Long pg_comune_sede;

    // PG_RAPP_LEGALE DECIMAL(10,0)
    private java.lang.Long pg_rapp_legale;

    // TI_TERZO CHAR(1) NOT NULL
    private java.lang.String ti_terzo;

    // VIA_SEDE VARCHAR(100) NOT NULL
    private java.lang.String via_sede;

    private java.lang.String codiceUnivocoPcc;

    private java.lang.String denominazionePcc;

    public TerzoBase() {
        super();
    }

    public TerzoBase(java.lang.Integer cd_terzo) {
        super(cd_terzo);
    }

    /*
     * Getter dell'attributo cap_comune_sede
     */
    public java.lang.String getCap_comune_sede() {
        return cap_comune_sede;
    }

    /*
     * Setter dell'attributo cap_comune_sede
     */
    public void setCap_comune_sede(java.lang.String cap_comune_sede) {
        this.cap_comune_sede = cap_comune_sede;
    }

    /*
     * Getter dell'attributo cd_anag
     */
    public java.lang.Integer getCd_anag() {
        return cd_anag;
    }

    /*
     * Setter dell'attributo cd_anag
     */
    public void setCd_anag(java.lang.Integer cd_anag) {
        this.cd_anag = cd_anag;
    }

    /*
     * Getter dell'attributo cd_precedente
     */
    public java.lang.String getCd_precedente() {
        return cd_precedente;
    }

    /*
     * Setter dell'attributo cd_precedente
     */
    public void setCd_precedente(java.lang.String cd_precedente) {
        this.cd_precedente = cd_precedente;
    }

    /*
     * Getter dell'attributo cd_unita_organizzativa
     */
    public java.lang.String getCd_unita_organizzativa() {
        return cd_unita_organizzativa;
    }

    /*
     * Setter dell'attributo cd_unita_organizzativa
     */
    public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
        this.cd_unita_organizzativa = cd_unita_organizzativa;
    }

    /*
     * Getter dell'attributo denominazione_sede
     */
    public java.lang.String getDenominazione_sede() {
        return denominazione_sede;
    }

    /*
     * Setter dell'attributo denominazione_sede
     */
    public void setDenominazione_sede(java.lang.String denominazione_sede) {
        this.denominazione_sede = denominazione_sede;
    }

    /*
     * Getter dell'attributo dt_canc
     */
    public java.sql.Timestamp getDt_canc() {
        return dt_canc;
    }

    /*
     * Setter dell'attributo dt_canc
     */
    public void setDt_canc(java.sql.Timestamp dt_canc) {
        this.dt_canc = dt_canc;
    }

    /*
     * Getter dell'attributo dt_fine_rapporto
     */
    public java.sql.Timestamp getDt_fine_rapporto() {
        return dt_fine_rapporto;
    }

    /*
     * Setter dell'attributo dt_fine_rapporto
     */
    public void setDt_fine_rapporto(java.sql.Timestamp dt_fine_rapporto) {
        this.dt_fine_rapporto = dt_fine_rapporto;
    }

    /*
     * Getter dell'attributo frazione_sede
     */
    public java.lang.String getFrazione_sede() {
        return frazione_sede;
    }

    /*
     * Setter dell'attributo frazione_sede
     */
    public void setFrazione_sede(java.lang.String frazione_sede) {
        this.frazione_sede = frazione_sede;
    }

    /*
     * Getter dell'attributo nome_unita_organizzativa
     */
    public java.lang.String getNome_unita_organizzativa() {
        return nome_unita_organizzativa;
    }

    /*
     * Setter dell'attributo nome_unita_organizzativa
     */
    public void setNome_unita_organizzativa(java.lang.String nome_unita_organizzativa) {
        this.nome_unita_organizzativa = nome_unita_organizzativa;
    }

    /*
     * Getter dell'attributo note
     */
    public java.lang.String getNote() {
        return note;
    }

    /*
     * Setter dell'attributo note
     */
    public void setNote(java.lang.String note) {
        this.note = note;
    }

    /*
     * Getter dell'attributo numero_civico_sede
     */
    public java.lang.String getNumero_civico_sede() {
        return numero_civico_sede;
    }

    /*
     * Setter dell'attributo numero_civico_sede
     */
    public void setNumero_civico_sede(java.lang.String numero_civico_sede) {
        this.numero_civico_sede = numero_civico_sede;
    }

    /*
     * Getter dell'attributo pg_comune_sede
     */
    public java.lang.Long getPg_comune_sede() {
        return pg_comune_sede;
    }

    /*
     * Setter dell'attributo pg_comune_sede
     */
    public void setPg_comune_sede(java.lang.Long pg_comune_sede) {
        this.pg_comune_sede = pg_comune_sede;
    }

    /*
     * Getter dell'attributo pg_rapp_legale
     */
    public java.lang.Long getPg_rapp_legale() {
        return pg_rapp_legale;
    }

    /*
     * Setter dell'attributo pg_rapp_legale
     */
    public void setPg_rapp_legale(java.lang.Long pg_rapp_legale) {
        this.pg_rapp_legale = pg_rapp_legale;
    }

    /*
     * Getter dell'attributo ti_terzo
     */
    public java.lang.String getTi_terzo() {
        return ti_terzo;
    }

    /*
     * Setter dell'attributo ti_terzo
     */
    public void setTi_terzo(java.lang.String ti_terzo) {
        this.ti_terzo = ti_terzo;
    }

    /*
     * Getter dell'attributo via_sede
     */
    public java.lang.String getVia_sede() {
        return via_sede;
    }

    /*
     * Setter dell'attributo via_sede
     */
    public void setVia_sede(java.lang.String via_sede) {
        this.via_sede = via_sede;
    }

    public java.lang.String getCodiceUnivocoUfficioIpa() {
        return codiceUnivocoUfficioIpa;
    }

    public void setCodiceUnivocoUfficioIpa(java.lang.String codiceUnivocoUfficioIpa) {
        this.codiceUnivocoUfficioIpa = codiceUnivocoUfficioIpa;
    }

    public java.lang.String getCodiceUnivocoPcc() {
        return codiceUnivocoPcc;
    }

    public void setCodiceUnivocoPcc(java.lang.String codiceUnivocoPcc) {
        this.codiceUnivocoPcc = codiceUnivocoPcc;
    }

    public java.lang.String getDenominazionePcc() {
        return denominazionePcc;
    }

    public void setDenominazionePcc(java.lang.String denominazionePcc) {
        this.denominazionePcc = denominazionePcc;
    }

    public java.lang.String getCodiceDestinatarioFatt() {
        return codiceDestinatarioFatt;
    }

    public void setCodiceDestinatarioFatt(java.lang.String codiceDestinatarioFatt) {
        this.codiceDestinatarioFatt = codiceDestinatarioFatt;
    }

    public java.lang.Boolean getFlSbloccoFatturaElettronica() {
        return flSbloccoFatturaElettronica;
    }

    public void setFlSbloccoFatturaElettronica(java.lang.Boolean flSbloccoFatturaElettronica) {
        this.flSbloccoFatturaElettronica = flSbloccoFatturaElettronica;
    }
}
