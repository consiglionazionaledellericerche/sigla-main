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

package it.cnr.contab.config00.contratto.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ContrattoDatiSintesiBulk extends OggettoBulk implements Persistent {

    private Integer esercizio;
    private Long pg_contratto;
    private String oggetto;
    private Timestamp dt_stipula;
    private Timestamp dt_proroga;
    private Timestamp dt_fine_validita;
    private Timestamp dt_inizio_validita;
    private BigDecimal im_contratto_passivo;

    public ContrattoDatiSintesiBulk() {

    }
    public ContrattoDatiSintesiBulk(Integer esercizio, Long pg_contratto, String oggetto, Timestamp dt_stipula, Timestamp dt_proroga, Timestamp dt_fine_validita, Timestamp dt_inizio_validita,  BigDecimal im_contratto_passivo) {
        this.dt_stipula = dt_stipula;
        this.dt_proroga = dt_proroga;
        this.dt_fine_validita = dt_fine_validita;
        this.dt_inizio_validita = dt_inizio_validita;
        this.esercizio = esercizio;
        this.pg_contratto = pg_contratto;
        this.im_contratto_passivo = im_contratto_passivo;
        this.oggetto = oggetto;
    }

    public Timestamp getDt_stipula() {
        return dt_stipula;
    }

    public void setDt_stipula(Timestamp dt_stipula) {
        this.dt_stipula = dt_stipula;
    }

    public Timestamp getDt_proroga() {
        return dt_proroga;
    }

    public void setDt_proroga(Timestamp dt_proroga) {
        this.dt_proroga = dt_proroga;
    }

    public Timestamp getDt_fine_validita() {
        return dt_fine_validita;
    }

    public void setDt_fine_validita(Timestamp dt_fine_validita) {
        this.dt_fine_validita = dt_fine_validita;
    }

    public Timestamp getDt_inizio_validita() {
        return dt_inizio_validita;
    }

    public void setDt_inizio_validita(Timestamp dt_inizio_validita) {
        this.dt_inizio_validita = dt_inizio_validita;
    }
    public Integer getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(Integer esercizio) {
        this.esercizio = esercizio;
    }

    public Long getPg_contratto() {
        return pg_contratto;
    }

    public void setPg_contratto(Long pg_contratto) {
        this.pg_contratto = pg_contratto;
    }

    public BigDecimal getIm_contratto_passivo() {
        return im_contratto_passivo;
    }

    public void setIm_contratto_passivo(BigDecimal im_contratto_passivo) {
        this.im_contratto_passivo = im_contratto_passivo;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }
}
