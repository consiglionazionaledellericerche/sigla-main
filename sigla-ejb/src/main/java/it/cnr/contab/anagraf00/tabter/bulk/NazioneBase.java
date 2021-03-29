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

package it.cnr.contab.anagraf00.tabter.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.jada.persistency.Keyed;

public class NazioneBase extends NazioneKey implements Keyed {
    // CD_CATASTALE VARCHAR(10)
    @JsonIgnore
    private java.lang.String cd_catastale;

    // CD_DIVISA VARCHAR(10)
    @JsonIgnore
    private java.lang.String cd_divisa;

    // CD_DIVISA_PER_MISSIONE VARCHAR(10)
    @JsonIgnore
    private java.lang.String cd_divisa_per_missione;

    // CD_ISO VARCHAR(10)
    @JsonIgnore
    private java.lang.String cd_iso;

    // CD_NAZIONE VARCHAR(10) NOT NULL
    @JsonIgnore
    private java.lang.String cd_nazione;

    // CD_NAZIONE_770 VARCHAR(10)
    @JsonIgnore
    private java.lang.String cd_nazione_770;

    // DS_NAZIONE VARCHAR(100) NOT NULL
    @JsonIgnore
    private java.lang.String ds_nazione;

    // NAZIONALITA VARCHAR(100) NOT NULL
    @JsonIgnore
    private java.lang.String nazionalita;

    // TI_NAZIONE CHAR(1) NOT NULL
    @JsonIgnore
    private java.lang.String ti_nazione;

    // FL_IBAN BOOLEAN NOT NULL
    @JsonIgnore
    private java.lang.Boolean fl_iban;

    // STRUTTURA_IBAN CHAR(100) NOT NULL
    @JsonIgnore
    private java.lang.String struttura_iban;

    @JsonIgnore
    private java.lang.String struttura_piva;

    // CD_AREA_ESTERA CHAR(2) NOT NULL
    @JsonIgnore
    private java.lang.String cd_area_estera;

    // FL_SEPA BOOLEAN
    @JsonIgnore
    private java.lang.Boolean fl_sepa;

    public NazioneBase() {
        super();
    }

    public NazioneBase(java.lang.Long pg_nazione) {
        super(pg_nazione);
    }

    /*
     * Getter dell'attributo cd_catastale
     */
    public java.lang.String getCd_catastale() {
        return cd_catastale;
    }

    /*
     * Setter dell'attributo cd_catastale
     */
    public void setCd_catastale(java.lang.String cd_catastale) {
        this.cd_catastale = cd_catastale;
    }

    /*
     * Getter dell'attributo cd_divisa
     */
    public java.lang.String getCd_divisa() {
        return cd_divisa;
    }

    /*
     * Setter dell'attributo cd_divisa
     */
    public void setCd_divisa(java.lang.String cd_divisa) {
        this.cd_divisa = cd_divisa;
    }

    /*
     * Getter dell'attributo cd_divisa_per_missione
     */
    public java.lang.String getCd_divisa_per_missione() {
        return cd_divisa_per_missione;
    }

    /*
     * Setter dell'attributo cd_divisa_per_missione
     */
    public void setCd_divisa_per_missione(java.lang.String cd_divisa_per_missione) {
        this.cd_divisa_per_missione = cd_divisa_per_missione;
    }

    /*
     * Getter dell'attributo cd_iso
     */
    public java.lang.String getCd_iso() {
        return cd_iso;
    }

    /*
     * Setter dell'attributo cd_iso
     */
    public void setCd_iso(java.lang.String cd_iso) {
        this.cd_iso = cd_iso;
    }

    /*
     * Getter dell'attributo cd_nazione
     */
    public java.lang.String getCd_nazione() {
        return cd_nazione;
    }

    /*
     * Setter dell'attributo cd_nazione
     */
    public void setCd_nazione(java.lang.String cd_nazione) {
        this.cd_nazione = cd_nazione;
    }

    /*
     * Getter dell'attributo cd_nazione_770
     */
    public java.lang.String getCd_nazione_770() {
        return cd_nazione_770;
    }

    /*
     * Setter dell'attributo cd_nazione_770
     */
    public void setCd_nazione_770(java.lang.String cd_nazione_770) {
        this.cd_nazione_770 = cd_nazione_770;
    }

    /*
     * Getter dell'attributo ds_nazione
     */
    public java.lang.String getDs_nazione() {
        return ds_nazione;
    }

    /*
     * Setter dell'attributo ds_nazione
     */
    public void setDs_nazione(java.lang.String ds_nazione) {
        this.ds_nazione = ds_nazione;
    }

    /*
     * Getter dell'attributo nazionalita
     */
    public java.lang.String getNazionalita() {
        return nazionalita;
    }

    /*
     * Setter dell'attributo nazionalita
     */
    public void setNazionalita(java.lang.String nazionalita) {
        this.nazionalita = nazionalita;
    }

    /*
     * Getter dell'attributo ti_nazione
     */
    public java.lang.String getTi_nazione() {
        return ti_nazione;
    }

    /*
     * Setter dell'attributo ti_nazione
     */
    public void setTi_nazione(java.lang.String ti_nazione) {
        this.ti_nazione = ti_nazione;
    }

    public java.lang.Boolean getFl_iban() {
        return fl_iban;
    }

    public void setFl_iban(java.lang.Boolean fl_iban) {
        this.fl_iban = fl_iban;
    }

    public java.lang.String getStruttura_iban() {
        return struttura_iban;
    }

    public void setStruttura_iban(java.lang.String struttura_iban) {
        this.struttura_iban = struttura_iban;
    }

    public java.lang.String getCd_area_estera() {
        return cd_area_estera;
    }

    public void setCd_area_estera(java.lang.String cd_area_estera) {
        this.cd_area_estera = cd_area_estera;
    }

    public java.lang.String getStruttura_piva() {
        return struttura_piva;
    }

    public void setStruttura_piva(java.lang.String struttura_piva) {
        this.struttura_piva = struttura_piva;
    }

	public Boolean getFl_sepa() {
		return fl_sepa;
	}

	public void setFl_sepa(Boolean fl_sepa) {
		this.fl_sepa = fl_sepa;
	}
}
