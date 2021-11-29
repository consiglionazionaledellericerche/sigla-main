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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Movimento_cogeBase extends Movimento_cogeKey implements Keyed {
    // CD_TERZO DECIMAL(8,0)
    private java.lang.Integer cd_terzo;

    // CD_VOCE_EP VARCHAR(45)
    private java.lang.String cd_voce_ep;

    // DT_A_COMPETENZA_COGE TIMESTAMP
    private java.sql.Timestamp dt_a_competenza_coge;

    // DT_DA_COMPETENZA_COGE TIMESTAMP
    private java.sql.Timestamp dt_da_competenza_coge;

    // IM_MOVIMENTO DECIMAL(15,2)
    private java.math.BigDecimal im_movimento;

    // SEZIONE VARCHAR(1) NOT NULL
    private java.lang.String sezione;

    // STATO VARCHAR(1)
    private java.lang.String stato;

    // TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
    private java.lang.String ti_istituz_commerc;

    // TI_RIGA VARCHAR(4) NULL
    private java.lang.String ti_riga;

    // CD_TIPO_DOCUMENTO VARCHAR(10)
    private java.lang.String cd_tipo_documento;

    // CD_CDS_DOCUMENTO VARCHAR(30)
    private java.lang.String cd_cds_documento;

    // CD_UO_DOCUMENTO VARCHAR(30)
    private java.lang.String cd_uo_documento;

    // ESERCIZIO_DOCUMENTO DECIMAL(4,0)
    private java.lang.Integer esercizio_documento;

    // PG_NUMERO_DOCUMENTO DECIMAL(10,0)
    private java.lang.Long pg_numero_documento;

    // FL_MODIFICABILE CHAR(1)
    private java.lang.Boolean fl_modificabile;

    public Movimento_cogeBase() {
        super();
    }

    public Movimento_cogeBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_movimento, java.lang.Long pg_scrittura) {
        super(cd_cds, esercizio, pg_movimento, pg_scrittura);
    }

    public Movimento_cogeBase(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_movimento, java.lang.Long pg_scrittura) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_movimento, pg_scrittura);
    }

    /*
     * Getter dell'attributo cd_terzo
     */
    public java.lang.Integer getCd_terzo() {
        return cd_terzo;
    }

    /*
     * Setter dell'attributo cd_terzo
     */
    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.cd_terzo = cd_terzo;
    }

    /*
     * Getter dell'attributo cd_voce_ep
     */
    public java.lang.String getCd_voce_ep() {
        return cd_voce_ep;
    }

    /*
     * Setter dell'attributo cd_voce_ep
     */
    public void setCd_voce_ep(java.lang.String cd_voce_ep) {
        this.cd_voce_ep = cd_voce_ep;
    }

    /*
     * Getter dell'attributo dt_a_competenza_coge
     */
    public java.sql.Timestamp getDt_a_competenza_coge() {
        return dt_a_competenza_coge;
    }

    /*
     * Setter dell'attributo dt_a_competenza_coge
     */
    public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
        this.dt_a_competenza_coge = dt_a_competenza_coge;
    }

    /*
     * Getter dell'attributo dt_da_competenza_coge
     */
    public java.sql.Timestamp getDt_da_competenza_coge() {
        return dt_da_competenza_coge;
    }

    /*
     * Setter dell'attributo dt_da_competenza_coge
     */
    public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
        this.dt_da_competenza_coge = dt_da_competenza_coge;
    }

    /*
     * Getter dell'attributo im_movimento
     */
    public java.math.BigDecimal getIm_movimento() {
        return im_movimento;
    }

    /*
     * Setter dell'attributo im_movimento
     */
    public void setIm_movimento(java.math.BigDecimal im_movimento) {
        this.im_movimento = im_movimento;
    }

    /*
     * Getter dell'attributo sezione
     */
    public java.lang.String getSezione() {
        return sezione;
    }

    /*
     * Setter dell'attributo sezione
     */
    public void setSezione(java.lang.String sezione) {
        this.sezione = sezione;
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
     * Getter dell'attributo ti_istituz_commerc
     */
    public java.lang.String getTi_istituz_commerc() {
        return ti_istituz_commerc;
    }

    /*
     * Setter dell'attributo ti_istituz_commerc
     */
    public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
        this.ti_istituz_commerc = ti_istituz_commerc;
    }

    public String getTi_riga() {
        return ti_riga;
    }

    public void setTi_riga(String ti_riga) {
        this.ti_riga = ti_riga;
    }

    public String getCd_tipo_documento() {
        return cd_tipo_documento;
    }

    public void setCd_tipo_documento(String cd_tipo_documento) {
        this.cd_tipo_documento = cd_tipo_documento;
    }

    public String getCd_cds_documento() {
        return cd_cds_documento;
    }

    public void setCd_cds_documento(String cd_cds_documento) {
        this.cd_cds_documento = cd_cds_documento;
    }

    public String getCd_uo_documento() {
        return cd_uo_documento;
    }

    public void setCd_uo_documento(String cd_uo_documento) {
        this.cd_uo_documento = cd_uo_documento;
    }

    public Integer getEsercizio_documento() {
        return esercizio_documento;
    }

    public void setEsercizio_documento(Integer esercizio_documento) {
        this.esercizio_documento = esercizio_documento;
    }

    public Long getPg_numero_documento() {
        return pg_numero_documento;
    }

    public void setPg_numero_documento(Long pg_numero_documento) {
        this.pg_numero_documento = pg_numero_documento;
    }

    public Boolean getFl_modificabile() {
        return fl_modificabile;
    }

    public void setFl_modificabile(Boolean fl_modificabile) {
        this.fl_modificabile = fl_modificabile;
    }
}
