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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class MandatoComunicaDatiBulk extends OggettoBulk implements Persistent {
    public final static Integer ANNO_INIZIO_PUBBLICAZIONE = 2016;
    private Timestamp daData;
    private Timestamp aData;
    private java.lang.String cd_cds;
    private java.lang.Integer esercizio;
    private java.lang.Long pg_mandato;
    private java.sql.Timestamp dt_pagamento;
    private java.math.BigDecimal importo;
    private String cdLiv4;
    private String dsLiv4;
    private String denominazioneSede;

    public MandatoComunicaDatiBulk(String cd_cds, Integer esercizio, Long pg_mandato, Timestamp dt_pagamento, String cdLiv4, String dsLiv4, String denominazioneSede) {
        this.cd_cds = cd_cds;
        this.esercizio = esercizio;
        this.pg_mandato = pg_mandato;
        this.dt_pagamento = dt_pagamento;
        this.cdLiv4 = cdLiv4;
        this.dsLiv4 = dsLiv4;
        this.denominazioneSede = denominazioneSede;
    }

    public MandatoComunicaDatiBulk() {
        super();
    }

    public String getCdLiv4() {
        return cdLiv4;
    }

    public void setCdLiv4(String cdLiv4) {
        this.cdLiv4 = cdLiv4;
    }

    public String getDsLiv4() {
        return dsLiv4;
    }

    public void setDsLiv4(String dsLiv4) {
        this.dsLiv4 = dsLiv4;
    }

    public String getDenominazioneSede() {
        return denominazioneSede;
    }

    public void setDenominazioneSede(String denominazioneSede) {
        this.denominazioneSede = denominazioneSede;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public String getCd_cds() {
        return cd_cds;
    }

    public void setCd_cds(String cd_cds) {
        this.cd_cds = cd_cds;
    }

    public Integer getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(Integer esercizio) {
        this.esercizio = esercizio;
    }

    public Long getPg_mandato() {
        return pg_mandato;
    }

    public void setPg_mandato(Long pg_mandato) {
        this.pg_mandato = pg_mandato;
    }

    public Timestamp getDt_pagamento() {
        return dt_pagamento;
    }

    public void setDt_pagamento(Timestamp dt_pagamento) {
        this.dt_pagamento = dt_pagamento;
    }

}
